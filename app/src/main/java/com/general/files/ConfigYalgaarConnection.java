package com.general.files;

import android.content.Context;
import android.location.Location;

import com.sls.yalgaar_api.YalgaarApiClient;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigYalgaarConnection implements GetLocationUpdates.LocationUpdates, UpdateFrequentTask.OnTaskRunCalled, ConfigYalgaarConnectionResponseListener.ConfigYalgaarConnectionListener {

    /**
     * Variable declaration of singleton instance.
     */
    private static ConfigYalgaarConnection configYalgaarInstance = null;

    /**
     * Variable declaration of singleton instance.
     */
    private YalgaarApiClient client = new YalgaarApiClient.Builder(MyApp.getInstance().getApplicationContext()).build();

    /**
     * This list maintains list of subscribed channels list.
     */
    ArrayList<String> listOfSubscribedList = new ArrayList<>();


    /**
     * Used to get location updates.
     */
    private GetLocationUpdates getLocUpdates;

    /**
     * A frequent task for driver's status (location + trip status)
     */
    private UpdateFrequentTask updateUserStatusTask;

    /**
     * Current location of driver
     */
    private Location userLoc = null;
    /**
     * Used to remove redundant messages (trip message or others). Internal purpose only.
     */
    private HashMap<String, String> listOfCurrentTripMsg_Map = new HashMap<>();

    /**
     * WebServer task of updateDriver status recurring task.
     */
    private ExecuteWebServerUrl currentExeTask;

    /**
     * This will indicates weather a client is killed or not.
     */
    boolean isClientKilled = false;


    private boolean isDispatchThreadLocked = false;
    private boolean isClientConnected = false;

    ConfigYalgaarConnectionResponseListener yalgaarResponseListener;


    public static ConfigYalgaarConnection getInstance() {
        if (configYalgaarInstance == null) {
            configYalgaarInstance = new ConfigYalgaarConnection();
        }
        return configYalgaarInstance;
    }

    /**
     * Fetch Singleton instance. By using this method will return instance of this class.
     */
    public static ConfigYalgaarConnection retrieveInstance() {
        return configYalgaarInstance;
    }


    public void buildConnection() {
//        if(isClientConnected){
//            continueChannelSubscribe();
//            return;
//        }
        yalgaarResponseListener = new ConfigYalgaarConnectionResponseListener(this);

        GeneralFunctions generalFunc = getGeneralFunc();
        try {
            client.connect(getGeneralFunc().retrieveValue(Utils.YALGAAR_CLIENT_KEY), false, (generalFunc.retrieveValue(Utils.DEVICE_SESSION_ID_KEY).equals("") ? generalFunc.getMemberId() : generalFunc.retrieveValue(Utils.DEVICE_SESSION_ID_KEY)), yalgaarResponseListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        startUserStatusUpdateTask();
    }

    /**
     * This function is used to subscribe channels, which are not due to not connected to server.
     */
    private void continueChannelSubscribe() {

        try {
            System.gc();
        } catch (Exception e) {

        }

        if (getGeneralFunc().getMemberId().trim().equals("")) {
            forceDestroy();
            return;
        }

        //Subscribe to private channel
        if (!listOfSubscribedList.contains("PASSENGER_" + getGeneralFunc().getMemberId())) {
            listOfSubscribedList.add("PASSENGER_" + getGeneralFunc().getMemberId());
        }

//        ConfigYalgaarConnection.getInstance().subscribeToChannels("PASSENGER_" + getGeneralFunc().getMemberId());

        // Resubscribe to all previously subscribed channels.
//        for (int i = 1; i < listOfSubscribedList.size(); i++) {
//            if (!listOfSubscribedList.get(i).equals("PASSENGER_" + getGeneralFunc().getMemberId())) {
//                ConfigYalgaarConnection.getInstance().subscribeToChannels(listOfSubscribedList.get(i));
//            }
//        }
        ConfigYalgaarConnection.getInstance().subscribeToChannels(listOfSubscribedList);
    }

    public void subscribeToChannels(ArrayList<String> channelNamesList) {
        // Add any new Channels to subscribed channels list. We will use this list to un subscribe channels.
        ArrayList<String> tmpChannels = new ArrayList<>();
        for (int i = 1; i < channelNamesList.size(); i++) {
            if (!listOfSubscribedList.contains(channelNamesList.get(i))) {
//                ConfigYalgaarConnection.getInstance().subscribeToChannels(listOfSubscribedList.get(i));
                listOfSubscribedList.add(channelNamesList.get(i));
                tmpChannels.add(channelNamesList.get(i));
            }
        }

        if (isClientConnected == false) {
            return;
        }

        try {
            client.subscribe(listOfSubscribedList, yalgaarResponseListener, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribeToChannels(String channelName) {
        // Add any new Channels to subscribed channels list. We will use this list to un subscribe channels.
        if (!listOfSubscribedList.contains(channelName)) {
            listOfSubscribedList.add(channelName);
        }

        if (isClientConnected == false) {
            return;
        }

        try {
            client.subscribe(listOfSubscribedList, yalgaarResponseListener, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unSubscribeFromChannels(String channelName) {
        try {
            client.unSubscribe(channelName, yalgaarResponseListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishMsg(String channelName, String message) {
        try {
            client.publish(channelName, message, yalgaarResponseListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Used to start user's status task. This will initialize frequent task to update device's current location to server and trip status messages.
     */
    private void startUserStatusUpdateTask() {

        stopLocationUpdateTask();

        Context mContext = MyApp.getInstance().getApplicationContext();

        if (mContext != null) {
            getLocUpdates = new GetLocationUpdates(mContext, Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, true, this);

            stopUserStatusUpdateTask();

            updateUserStatusTask = new UpdateFrequentTask(GeneralFunctions.parseIntegerValue(15, GeneralFunctions.retrieveValue(Utils.FETCH_TRIP_STATUS_TIME_INTERVAL_KEY, mContext)) * 1000);
            updateUserStatusTask.setTaskRunListener(this);
            updateUserStatusTask.startRepeatingTask();

        }

    }


    /**
     * Function used to get general function object
     *
     * @return GeneralFunction object is returned.
     */
    private static GeneralFunctions getGeneralFunc() {
        GeneralFunctions generalFunc = MyApp.getInstance().getGeneralFun(MyApp.getInstance().getApplicationContext());

        return generalFunc;
    }

    /**
     * Function used to dispatch message to user when received some event on particular channel.
     */
    private void dispatchMsg(String message) {
        if (!isDispatchThreadLocked) {
            isDispatchThreadLocked = true;

            if (listOfCurrentTripMsg_Map.get(message) == null) {
                listOfCurrentTripMsg_Map.put(message, "Yes");
                (new FireTripStatusMsg()).fireTripMsg(message);
            }

            isDispatchThreadLocked = false;
        }
    }

    /**
     * Function used to un subscribe all channels. Generally this will be done when app is going to be terminate.
     */
    public void releaseAllChannels() {

        isClientKilled = true;

        for (int i = 1; i < listOfSubscribedList.size(); i++) {
            ConfigSCConnection.getInstance().unSubscribeFromChannels(listOfSubscribedList.get(i));
        }

        listOfSubscribedList.clear();
    }

    @Override
    public void onLocationUpdate(Location location) {
        this.userLoc = location;

    }

    @Override
    public void onTaskRun() {
        configUserStatus();
    }

    /**
     * This will dispatch updates regarding trip status OR used to update current driver location to server.
     */
    protected void configUserStatus() {
        Context mContext = MyApp.getInstance().getCurrentAct();
        if (mContext == null) {
            return;
        }
        if (getGeneralFunc().getMemberId().equalsIgnoreCase("")) {
            return;
        }

        String iTripId = "";
        String iDriverId = "";
        String DeliverAll = "";

        if (MyApp.getInstance() != null) {
            if (MyApp.getInstance().onGoingTripDetailsAct != null) {

                if (MyApp.getInstance().onGoingTripDetailsAct.tripDetail == null) {
                    return;
                }
                iTripId = MyApp.getInstance().onGoingTripDetailsAct.tripDetail.get("iTripId");
                iDriverId = MyApp.getInstance().onGoingTripDetailsAct.tripDetail.get("iDriverId");
                DeliverAll = "";
            } else if (MyApp.getInstance().trackOrderActivity != null) {

                iTripId = MyApp.getInstance().trackOrderActivity.iOrderId;

                iDriverId = MyApp.getInstance().trackOrderActivity.iDriverId;

                DeliverAll = "DeliverAll";
            } else if (MyApp.getInstance().mainAct != null) {
                if (MyApp.getInstance().mainAct.isDriverAssigned && MyApp.getInstance().mainAct.driverAssignedData == null) {
                    return;
                }
                if (!MyApp.getInstance().mainAct.isDriverAssigned) {
                    iDriverId = MyApp.getInstance().mainAct.getAvailableDriverIds();
                    iTripId = "";
                    DeliverAll = "";
                } else {
                    DeliverAll = "";
                    iTripId = MyApp.getInstance().mainAct.assignedTripId;
                    iDriverId = MyApp.getInstance().mainAct.assignedDriverId;
                }
            }
        }

        GeneralFunctions generalFunc = getGeneralFunc();
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "configPassengerTripStatus");
        parameters.put("iTripId", iTripId);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);

        if (userLoc != null) {
            parameters.put("vLatitude", "" + userLoc.getLatitude());
            parameters.put("vLongitude", "" + userLoc.getLongitude());
        }

        if (Utils.checkText(iTripId)) {
            parameters.put("CurrentDriverIds", "" + iDriverId);
        } else if (MyApp.getInstance().mainAct != null) {
            if (MyApp.getInstance().mainAct.getAvailableDriverIds() != null) {
                parameters.put("CurrentDriverIds", "" + (MyApp.getInstance().mainAct.getAvailableDriverIds()));
            }
        }

        if (this.currentExeTask != null) {
            this.currentExeTask.cancel(true);
            this.currentExeTask = null;
            Utils.runGC();
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        this.currentExeTask = exeWebServer;
        String finalITripId = iTripId;
        String finalDeliverAll = DeliverAll;
        exeWebServer.setDataResponseListener(responseString -> {
            if (responseString != null && Utils.checkText(responseString)) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    if (!finalITripId.isEmpty()) {
                        String message_str = generalFunc.getJsonValue(Utils.message_str, responseString);

                        dispatchMsg(message_str);
                    } else {
                        JSONArray message_arr = generalFunc.getJsonArray(Utils.message_str, responseString);
                        if (message_arr != null) {
                            for (int i = 0; i < message_arr.length(); i++) {
                                JSONObject obj_tmp = generalFunc.getJsonObject(message_arr, i);
                                dispatchMsg(obj_tmp.toString());
                            }
                        } else {
                            String message_str = generalFunc.getJsonValue(Utils.message_str, responseString);
                            dispatchMsg(message_str);
                        }
                    }

                    JSONArray currentDrivers = generalFunc.getJsonArray("currentDrivers", responseString);
                    if (currentDrivers != null && currentDrivers.length() > 0 && !isClientKilled) {

                        if (generalFunc.retrieveValue(Utils.PUBSUB_TECHNIQUE).equalsIgnoreCase("None")) {
                            for (int i = 0; i < currentDrivers.length(); i++) {

                                try {
                                    String data_temp = currentDrivers.get(i).toString();
                                    JSONObject obj = new JSONObject();
                                    obj.put("MsgType", Utils.checkText(finalITripId) ? "LocationUpdateOnTrip" : "LocationUpdate");
                                    obj.put("iDriverId", generalFunc.getJsonValue("iDriverId", data_temp));
                                    obj.put("vLatitude", generalFunc.getJsonValue("vLatitude", data_temp));
                                    obj.put("vLongitude", generalFunc.getJsonValue("vLongitude", data_temp));
                                    obj.put("ChannelName", Utils.pubNub_Update_Loc_Channel_Prefix + generalFunc.getJsonValue("iDriverId", data_temp));
                                    obj.put("LocTime", System.currentTimeMillis() + "");
                                    obj.put("eSystem", finalDeliverAll);

                                    dispatchMsg(obj.toString());

                                } catch (Exception e) {

                                }

                            }
                        }

                    }

                } else {
                    String message_str = generalFunc.getJsonValue(Utils.message_str, responseString);

                    if (message_str.equalsIgnoreCase("SESSION_OUT")) {
                        forceDestroy();

                        if (MyApp.getInstance().getCurrentAct() != null && !MyApp.getInstance().getCurrentAct().isFinishing()) {
                            MyApp.getInstance().notifySessionTimeOut();
                        }
                    }
                }
            }
        });

        exeWebServer.execute();
    }

    /**
     * Used to stop location updates.
     */
    private void stopLocationUpdateTask() {
        if (getLocUpdates != null) {
            getLocUpdates.stopLocationUpdates();
            getLocUpdates = null;
        }
    }

    /**
     * Used to stop user status update task (A frequent task).
     */
    private void stopUserStatusUpdateTask() {
        if (updateUserStatusTask != null) {
            updateUserStatusTask.stopRepeatingTask();
            updateUserStatusTask = null;
        }
    }


    /**
     * Function used to destroy current instance. Generally this will be done when app is going to be terminate OR instance of this class is no longer required.
     */
    public void forceDestroy() {
        releaseAllChannels();
        ConfigYalgaarConnection.getInstance().client.disConnect();
        stopLocationUpdateTask();
        stopUserStatusUpdateTask();

        configYalgaarInstance = null;
    }

    @Override
    public void onClientConnected() {
        isClientConnected = true;
        continueChannelSubscribe();
    }

    @Override
    public void onClientDisConnected() {
        isClientConnected = false;
    }

    @Override
    public void onDataReceived(String data) {
        dispatchMsg(data);
    }
}
