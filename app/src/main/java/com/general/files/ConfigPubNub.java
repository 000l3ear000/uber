package com.general.files;

import android.content.Context;
import android.location.Location;
import android.os.Handler;

import com.melevicarbrasil.usuario.MainActivity;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.utils.Logger;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Admin on 05-10-2016.
 */
public class ConfigPubNub extends SubscribeCallback implements GetLocationUpdates.LocationUpdates, UpdateFrequentTask.OnTaskRunCalled {
    private static ConfigPubNub instance = null;
    public boolean isSessionout = false;
    Context mContext;
    PubNub pubnub;
    GeneralFunctions generalFunc;
    private Location userLoc;
    private ExecuteWebServerUrl currentExeTask;
    private UpdateFrequentTask updatepassengerStatustask;
    private InternetConnection intCheck;
    private GetLocationUpdates getLocUpdates;
    private boolean isPubnubInstKilled = false;
    int FETCH_TRIP_STATUS_TIME_INTERVAL = 15;

    public static ConfigPubNub getInstance() {
        if (instance == null) {
            instance = new ConfigPubNub(MyApp.getInstance().getCurrentAct());
        }
        return instance;
    }

    public static ConfigPubNub getInstance(boolean isForceReset) {

        if (instance != null) {
            instance.releaseInstances();
        }

        instance = new ConfigPubNub(MyApp.getInstance().getCurrentAct());

        return instance;
    }

    public static ConfigPubNub retrieveInstance() {
        return instance;
    }

    public ConfigPubNub(Context mContext) {
        this.mContext = mContext;
    }

    public void buildPubSub() {

        releaseInstances();

        if (mContext == null) {
            return;
        }

        generalFunc = MyApp.getInstance().getAppLevelGeneralFunc();

        FETCH_TRIP_STATUS_TIME_INTERVAL = generalFunc.parseIntegerValue(15, generalFunc.retrieveValue(Utils.FETCH_TRIP_STATUS_TIME_INTERVAL_KEY));
        intCheck = new InternetConnection(mContext);


        Logger.d("PUBNUBTECH", "::" + generalFunc.retrieveValue(Utils.PUBSUB_TECHNIQUE));
        if (generalFunc.retrieveValue(Utils.PUBSUB_TECHNIQUE).equalsIgnoreCase("SocketCluster")) {
            ConfigSCConnection.getInstance().buildConnection();
            return;
        } else if (generalFunc.retrieveValue(Utils.PUBSUB_TECHNIQUE).equalsIgnoreCase("PubNub")) {
            PNConfiguration pnConfiguration = new PNConfiguration();

            pnConfiguration.setUuid(generalFunc.retrieveValue(Utils.DEVICE_SESSION_ID_KEY).equals("") ? generalFunc.getMemberId() : generalFunc.retrieveValue(Utils.DEVICE_SESSION_ID_KEY));

            pnConfiguration.setSubscribeKey(generalFunc.retrieveValue(Utils.PUBNUB_SUB_KEY));
            pnConfiguration.setPublishKey(generalFunc.retrieveValue(Utils.PUBNUB_PUB_KEY));
            pnConfiguration.setSecretKey(generalFunc.retrieveValue(Utils.PUBNUB_SEC_KEY));

            if (pubnub != null) {
                try {
                    pubnub.forceDestroy();
                } catch (Exception e) {
                    Logger.d("pubnub", "::" + e.toString());

                }
            }
            pubnub = new PubNub(pnConfiguration);

            pubnub.addListener(this);
        } else if (generalFunc.retrieveValue(Utils.PUBSUB_TECHNIQUE).equalsIgnoreCase("Yalgaar")) {
            //yalgaar
            ConfigYalgaarConnection.getInstance().buildConnection();
            return;

        }


        if (getLocUpdates != null) {
            getLocUpdates.stopLocationUpdates();
            getLocUpdates = null;
        }
        getLocUpdates = new GetLocationUpdates(mContext, Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, true, true, this);

        if (updatepassengerStatustask != null) {
            updatepassengerStatustask.stopRepeatingTask();
            updatepassengerStatustask = null;
        }
        updatepassengerStatustask = new UpdateFrequentTask(generalFunc.parseIntegerValue(15, generalFunc.retrieveValue(Utils.FETCH_TRIP_STATUS_TIME_INTERVAL_KEY)) * 1000);
        updatepassengerStatustask.setTaskRunListener(this);
        updatepassengerStatustask.startRepeatingTask();

        subscribeToPrivateChannel();

        connectToPubNub(10000);
        connectToPubNub(20000);
        connectToPubNub(30000);
    }

    public void connectToPubNub(int interval) {
//        isPubnubInstKilled = false;
        new Handler().postDelayed(() -> {
            if (pubnub != null) {
                pubnub.reconnect();
            }
        }, interval);
    }

    public void connectToPubNub() {
//        isPubnubInstKilled = false;
        new Handler().postDelayed(() -> {
            if (pubnub != null) {
                pubnub.reconnect();
            }
        }, 10000);
    }

    public void connectToPubNub(final PubNub pubNub) {

        new Handler().postDelayed(() -> {
            if (pubNub != null) {
                pubNub.reconnect();
            }
        }, 10000);
    }

    public void subscribeToPrivateChannel() {
        try {
            if (pubnub != null) {
                pubnub.subscribe()
                        .channels(Arrays.asList("PASSENGER_" + generalFunc.getMemberId())) // subscribe to channels
                        .execute();
            }
            if (ConfigSCConnection.retrieveInstance() != null) {
                ConfigSCConnection.getInstance().subscribeToChannels("PASSENGER_" + generalFunc.getMemberId());
            }

            if (ConfigYalgaarConnection.retrieveInstance() != null) {
                ConfigYalgaarConnection.getInstance().subscribeToChannels("PASSENGER_" + generalFunc.getMemberId());
            }
        } catch (Exception e) {
            Logger.d("subscribeToPrivateChannel", "::" + e.toString());
        }
    }

    public void unSubscribeToPrivateChannel() {
        try {
            if (pubnub != null) {
                pubnub.unsubscribe()
                        .channels(Arrays.asList("PASSENGER_" + generalFunc.getMemberId())) // subscribe to channels
                        .execute();
            }
            if (ConfigSCConnection.retrieveInstance() != null) {
                ConfigSCConnection.getInstance().unSubscribeFromChannels("PASSENGER_" + generalFunc.getMemberId());
            }

            if (ConfigYalgaarConnection.retrieveInstance() != null) {
                ConfigYalgaarConnection.getInstance().unSubscribeFromChannels("PASSENGER_" + generalFunc.getMemberId());
            }
        } catch (Exception e) {

        }
    }

    public void releasePubSubInstance() {
        releaseInstances();
    }

    private void releaseInstances() {
        Logger.e("releaseInstances", "Called::" + MyApp.getInstance().getCurrentAct().getClass().getSimpleName());
        try {

            if (pubnub != null) {
                pubnub.removeListener(this);
                pubnub.unsubscribeAll();
                pubnub.forceDestroy();
            }

            if (ConfigSCConnection.retrieveInstance() != null) {
                ConfigSCConnection.getInstance().forceDestroy();
            }

            if (ConfigYalgaarConnection.retrieveInstance() != null) {
                ConfigYalgaarConnection.getInstance().forceDestroy();
            }


            //isPubnubInstKilled = true;
            if (updatepassengerStatustask != null) {
                updatepassengerStatustask.stopRepeatingTask();
                updatepassengerStatustask = null;
            }

            if (getLocUpdates != null) {
                getLocUpdates.stopLocationUpdates();
                getLocUpdates = null;
            }

        } catch (Exception e) {
            Logger.e("releaseInstances", "::" + e.toString());
        }

    }

    public void subscribeToChannels(ArrayList<String> channels) {
        Logger.e("SubscribdChannels", ":::" + channels.toString());
        if (pubnub != null) {
            pubnub.subscribe()
                    .channels(channels) // subscribe to channels
                    .execute();
        }
        if (ConfigSCConnection.retrieveInstance() != null) {
            for (int i = 0; i < channels.size(); i++) {
                ConfigSCConnection.getInstance().subscribeToChannels(channels.get(i));
            }
        }
        if (ConfigYalgaarConnection.retrieveInstance() != null) {
            for (int i = 0; i < channels.size(); i++) {
                ConfigYalgaarConnection.getInstance().subscribeToChannels(channels.get(i));
            }
        }
    }

    public void unSubscribeToChannels(ArrayList<String> channels) {
        Logger.e("UnSubscribdChannels", ":::" + channels.toString());
        if (pubnub != null) {
            pubnub.unsubscribe()
                    .channels(channels)
                    .execute();
        }
        if (ConfigSCConnection.retrieveInstance() != null) {
            for (int i = 0; i < channels.size(); i++) {
                ConfigSCConnection.getInstance().unSubscribeFromChannels(channels.get(i));
            }
        }
        if (ConfigYalgaarConnection.retrieveInstance() != null) {
            for (int i = 0; i < channels.size(); i++) {
                ConfigYalgaarConnection.getInstance().unSubscribeFromChannels(channels.get(i));
            }
        }
    }

    public void publishMsg(String channel, String message) {
//        .message(Arrays.asList("hello", "there"))
        if (pubnub != null) {
            pubnub.publish()
                    .message(message)
                    .channel(channel)
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            // handle publish result, status always present, result if successful
                            // status.isError to see if error happened
                            Logger.d("Publish Res", "::::" + result.getTimetoken());
                        }
                    });
        }
        if (ConfigSCConnection.retrieveInstance() != null) {
            ConfigSCConnection.getInstance().publishMsg(channel, message);
        }

        if (ConfigYalgaarConnection.retrieveInstance() != null) {
            ConfigYalgaarConnection.getInstance().publishMsg(channel, message);
        }
    }


    private void getUpdatedPassengerStatus() {
        Logger.e("USERID", "::" + generalFunc.getMemberId());
        if (generalFunc.getMemberId().equalsIgnoreCase("")) {
            return;
        }

        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
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
        } else if (mContext != null) {
            if (mContext instanceof MainActivity) {
                if (((MainActivity) mContext).getAvailableDriverIds() != null)
                    parameters.put("CurrentDriverIds", "" + ((MainActivity) mContext).getAvailableDriverIds());
            }
        }

        if (this.currentExeTask != null) {
            this.currentExeTask.cancel(true);
            this.currentExeTask = null;
            Utils.runGC();
        }

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minutesOfDay = calendar.get(Calendar.MINUTE);
        int seondsOfDay = calendar.get(Calendar.SECOND);
        NotificationScheduler.setReminder(mContext, AlarmReceiver.class, hourOfDay, minutesOfDay, seondsOfDay + FETCH_TRIP_STATUS_TIME_INTERVAL + 5);

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
                    if (currentDrivers != null && currentDrivers.length() > 0 && !isPubnubInstKilled) {


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
                        releaseInstances();

                        if (MyApp.getInstance().getCurrentAct() != null && !MyApp.getInstance().getCurrentAct().isFinishing()) {
                            MyApp.getInstance().notifySessionTimeOut();
                        }
                    }
                }
            }
        });
        exeWebServer.execute();
    }

    @Override
    public void onLocationUpdate(Location location) {
        if (location == null) {
            return;
        }
        this.userLoc = location;
    }

    @Override
    public void onTaskRun() {

        if (!isSessionout) {
            generalFunc.sendHeartBeat();

            getUpdatedPassengerStatus();
        }
    }


    private void dispatchMsg(final String message) {
        (new FireTripStatusMsg()).fireTripMsg(message);
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {
        if (pubnub == null || status == null || status.getCategory() == null) {
            connectToPubNub();
            return;
        }

        if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).pubNubStatus(status.getCategory());
        }
        switch (status.getCategory()) {
            case PNMalformedResponseCategory:
            case PNUnexpectedDisconnectCategory:
            case PNTimeoutCategory:
            case PNNetworkIssuesCategory:
            case PNDisconnectedCategory:
                connectToPubNub(pubnub);
                break;
            case PNConnectedCategory:
                // Connect event. You can do stuff like publish, and know you'll get it.
                // Or just use the connected event to confirm you are subscribed for
                // UI / internal notifications, etc
                break;

            default:
                break;

        }
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

        Logger.e("PubNubMsg", ":GOT:" + message.getMessage().toString());
        dispatchMsg(message.getMessage().toString());
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }

}
