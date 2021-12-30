package com.general.files;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rest.RestClient;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Admin on 05-07-2016.
 */
public class LoadAvailableCab implements UpdateFrequentTask.OnTaskRunCalled {
    public ArrayList<HashMap<String, String>> listOfDrivers;
    public String pickUpAddress = "";
    public String currentGeoCodeResult = "";
    public String sortby = "";
    public boolean isAvailableCab = false;
    public String selectProviderId = "";
    Context mContext;
    GeneralFunctions generalFunc;
    String selectedCabTypeId = "";
    //    Location pickUpLocation;
    GoogleMap gMapView;
    View parentView;
    ExecuteWebServerUrl currentWebTask;
    MainActivity mainAct;
    public ArrayList<Marker> driverMarkerList;
    String userProfileJson;
    int RESTRICTION_KM_NEAREST_TAXI = 4;
    int ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL = 1 * 60 * 1000;
    int DRIVER_ARRIVED_MIN_TIME_PER_MINUTE = 3;
    UpdateFrequentTask updateDriverListTask;
    Dialog dialog = null;
    boolean isTaskKilled = false;
    boolean isSessionOut = false;
    public boolean isMulti = false;

    String SERVICE_PROVIDER_FLOW = "";

    public LoadAvailableCab(Context mContext, GeneralFunctions generalFunc, String selectedCabTypeId, Location pickUpLocation, GoogleMap gMapView, String userProfileJson) {
        this.mContext = mContext;
        this.generalFunc = generalFunc;
        this.selectedCabTypeId = selectedCabTypeId;
//        this.pickUpLocation = pickUpLocation;
        this.gMapView = gMapView;
        this.userProfileJson = userProfileJson;
        isMulti = false;

        if (mContext instanceof MainActivity) {
            mainAct = (MainActivity) mContext;
            parentView = generalFunc.getCurrentView(mainAct);
        }

        listOfDrivers = new ArrayList<>();
        driverMarkerList = new ArrayList<>();

        RESTRICTION_KM_NEAREST_TAXI = generalFunc.parseIntegerValue(4, generalFunc.getJsonValue("RESTRICTION_KM_NEAREST_TAXI", userProfileJson));
        ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL = (generalFunc.parseIntegerValue(1, generalFunc.getJsonValue("ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL", userProfileJson))) * 60 * 1000;
        DRIVER_ARRIVED_MIN_TIME_PER_MINUTE = generalFunc.parseIntegerValue(3, generalFunc.getJsonValue("DRIVER_ARRIVED_MIN_TIME_PER_MINUTE", userProfileJson));
        SERVICE_PROVIDER_FLOW = generalFunc.getJsonValue("SERVICE_PROVIDER_FLOW", userProfileJson);
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public void setPickUpLocation(Location pickUpLocation) {
//        this.pickUpLocation = pickUpLocation;
    }

    public void setCabTypeId(String selectedCabTypeId) {
        this.selectedCabTypeId = selectedCabTypeId;
    }

    public void changeCabs() {

        if (driverMarkerList.size() > 0) {
            filterDrivers(true);
        } else {
            checkAvailableCabs();
        }
    }

    public void checkAvailableCabs() {

        if (mainAct.llFilter != null) {
            mainAct.llFilter.setVisibility(View.GONE);
        }

        if (ConfigPubNub.getInstance().isSessionout) {
            return;
        }

        if (mainAct.pickUpLocation == null) {
            return;
        }

        if (gMapView == null) {
            return;
        }

        if (mainAct != null && mainAct.SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider") && mainAct.getIntent().getBooleanExtra("isCarwash", false)) {
        } else {
            if (updateDriverListTask == null) {
                updateDriverListTask = new UpdateFrequentTask(ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL);
                onResumeCalled();
                updateDriverListTask.setTaskRunListener(this);
            }
        }

        if (currentWebTask != null) {
            currentWebTask.cancel(true);
            currentWebTask = null;
        }

        if (mainAct != null) {
            mainAct.notifyCarSearching();
        }

        if (listOfDrivers != null) {
            if (listOfDrivers.size() > 0) {
                listOfDrivers.clear();
            }
        }

        if (mainAct.cabSelectionFrag != null) {
            mainAct.cabSelectionFrag.showLoader();
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "loadAvailableCab");
        parameters.put("PassengerLat", "" + mainAct.pickUpLocation.getLatitude());
        parameters.put("PassengerLon", "" + mainAct.pickUpLocation.getLongitude());
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("PickUpAddress", pickUpAddress);
        parameters.put("scheduleDate", mainAct.SelectDate);
        parameters.put("SelectedCabType", mainAct.getSelectedCabTypeId());
        parameters.put("sortby", sortby);
        if (!mainAct.destLocLatitude.equalsIgnoreCase("")) {
            parameters.put("DestLat", mainAct.destLocLatitude);
            parameters.put("DestLong", mainAct.destLocLongitude);
        }

        if (!mainAct.eShowOnlyMoto.equalsIgnoreCase("")) {
            parameters.put("eShowOnlyMoto", mainAct.eShowOnlyMoto);
        }

        if (mainAct.eFly) {
            parameters.put("eFly", "Yes");
            parameters.put("iFromStationId", mainAct.iFromStationId);
            parameters.put("iToStationId", mainAct.iToStationId);
        }
//        if (mainAct.isUfx) {
        parameters.put("eType", mainAct.getCurrentCabGeneralType());
//        }
        //  parameters.put("currentGeoCodeResult", Utils.removeWithSpace(currentGeoCodeResult));

        if (mainAct != null) {

            Logger.d("isRental", "::" + mainAct.iscubejekRental + "::" + mainAct.isRental);
            if (mainAct.iscubejekRental || mainAct.isRental) {
                parameters.put("eRental", "Yes");
                parameters.put("eType", "Ride");

            }
            if (mainAct.cabSelectionFrag != null) {
                if (mainAct.isUfx) {
                    parameters.put("iVehicleTypeId", mainAct.getSelectedCabTypeId());
                }
            } else {
                if (mainAct.app_type.equals(Utils.CabGeneralType_UberX) || (mainAct.app_type.equals(Utils.CabGeneralTypeRide_Delivery_UberX) || mainAct.isUfx) || mainAct.getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                    parameters.put("iVehicleTypeId", mainAct.getSelectedCabTypeId());

                }
            }
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        this.currentWebTask = exeWebServer;
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                if (Utils.checkText(responseString) && generalFunc.getJsonValue(Utils.message_str, responseString).equals("SESSION_OUT")) {
                    isSessionOut = true;
                    if (currentWebTask != null) {
                        currentWebTask.cancel(true);
                        currentWebTask = null;
                    }
                    setTaskKilledValue(true);
                    updateDriverListTask.setTaskRunListener(null);
                    MyApp.getInstance().notifySessionTimeOut();
                    Utils.runGC();
                    return;
                }

                JSONArray vehicleTypesArr = generalFunc.getJsonArray("VehicleTypes", responseString);
                ArrayList<HashMap<String, String>> tempCabTypesArrList = new ArrayList<>();

                if (vehicleTypesArr != null) {
                    for (int i = 0; i < vehicleTypesArr.length(); i++) {
                        JSONObject tempObj = generalFunc.getJsonObject(vehicleTypesArr, i);

                        String type = mainAct.isDeliver(mainAct.getCurrentCabGeneralType()) ? "Deliver" : mainAct.getCurrentCabGeneralType();
                        if (type.equalsIgnoreCase("rental")) {
                            type = Utils.CabGeneralType_Ride;
                        }
                        if (generalFunc.getJsonValue("eType", tempObj.toString()).equals(type)) {

                            Gson gson = RestClient.getGSONBuilder();
                            HashMap<String, String> dataMap = gson.fromJson(
                                    tempObj.toString(), new TypeToken<HashMap<String, Object>>() {
                                    }.getType()
                            );
                            tempCabTypesArrList.add(dataMap);
                        }
                    }
                }

                boolean isCarTypeChanged = isCarTypesArrChanged(tempCabTypesArrList);

                if (isCarTypeChanged) {

                    mainAct.cabTypesArrList.clear();
                    mainAct.cabTypesArrList.addAll(tempCabTypesArrList);
                    mainAct.selectedCabTypeId = getFirstCarTypeID();
                    selectedCabTypeId = getFirstCarTypeID();

                    if (mainAct.cabSelectionFrag != null) {
                        mainAct.cabSelectionFrag.generateCarType();
                    }

                    if (mainAct.cabSelectionFrag != null && (!mainAct.cabSelectionFrag.isSkip || mainAct.isRental || mainAct.iscubejekRental)) {
                        if (mainAct.isRental || mainAct.iscubejekRental) {
                            mainAct.cabSelectionFrag.estimateFare("", "");
                        } else {
                            mainAct.cabSelectionFrag.findRoute("--");
                        }
                    }
                }

                if (mainAct.cabTypesArrList.size() > 0) {
                    mainAct.cabTypesArrList.clear();
                    mainAct.cabTypesArrList.addAll(tempCabTypesArrList);
                }

                if (mainAct.cabTypesArrList.size() == 0) {
                    mainAct.cabTypesArrList.addAll(tempCabTypesArrList);
                }

                if (mainAct.cabSelectionFrag != null && mainAct.cabTypesArrList != null && mainAct.cabTypesArrList.size() > 0) {
                    mainAct.cabSelectionFrag.closeLoadernTxt();
                } else {
                    if (mainAct.cabSelectionFrag != null && mainAct.cabTypesArrList != null) {
                        mainAct.cabSelectionFrag.closeLoader();
                    }
                }

                JSONArray availCabArr = generalFunc.getJsonArray("AvailableCabList", responseString);

                if (availCabArr != null) {
                    for (int i = 0; i < availCabArr.length(); i++) {
                        JSONObject obj_temp = generalFunc.getJsonObject(availCabArr, i);

                        JSONObject carDetailsJson = generalFunc.getJsonObject("DriverCarDetails", obj_temp);
                        HashMap<String, String> driverDataMap = new HashMap<String, String>();
                        driverDataMap.put("driver_id", generalFunc.getJsonValueStr("iDriverId", obj_temp));
                        driverDataMap.put("Name", generalFunc.getJsonValueStr("vName", obj_temp));
                        driverDataMap.put("eIsFeatured", generalFunc.getJsonValueStr("eIsFeatured", obj_temp));
                        driverDataMap.put("LastName", generalFunc.getJsonValueStr("vLastName", obj_temp));
                        driverDataMap.put("Latitude", generalFunc.getJsonValueStr("vLatitude", obj_temp));
                        driverDataMap.put("Longitude", generalFunc.getJsonValueStr("vLongitude", obj_temp));
                        driverDataMap.put("GCMID", generalFunc.getJsonValueStr("iGcmRegId", obj_temp));
                        driverDataMap.put("iAppVersion", generalFunc.getJsonValueStr("iAppVersion", obj_temp));
                        driverDataMap.put("driver_img", generalFunc.getJsonValueStr("vImage", obj_temp));
                        driverDataMap.put("average_rating", generalFunc.getJsonValueStr("vAvgRating", obj_temp));
                        driverDataMap.put("DIST_TO_PICKUP_INT", generalFunc.getJsonValueStr("distance", obj_temp));
                        driverDataMap.put("vPhone_driver", generalFunc.getJsonValueStr("vPhone", obj_temp));
                        driverDataMap.put("vPhoneCode_driver", generalFunc.getJsonValueStr("vCode", obj_temp));
                        driverDataMap.put("tProfileDescription", generalFunc.getJsonValueStr("tProfileDescription", obj_temp));
                        driverDataMap.put("ACCEPT_CASH_TRIPS", generalFunc.getJsonValueStr("ACCEPT_CASH_TRIPS", obj_temp));
                        driverDataMap.put("vWorkLocationRadius", generalFunc.getJsonValueStr("vWorkLocationRadius", obj_temp));
                        driverDataMap.put("PROVIDER_RADIUS", generalFunc.getJsonValueStr("vWorkLocationRadius", obj_temp));
                        driverDataMap.put("iGcmRegId", generalFunc.getJsonValueStr("iGcmRegId", obj_temp));

                        driverDataMap.put("DriverGender", generalFunc.getJsonValueStr("eGender", obj_temp));
                        driverDataMap.put("eFemaleOnlyReqAccept", generalFunc.getJsonValueStr("eFemaleOnlyReqAccept", obj_temp));

                        driverDataMap.put("eHandiCapAccessibility", generalFunc.getJsonValueStr("eHandiCapAccessibility", carDetailsJson));
                        driverDataMap.put("eChildSeatAvailable", generalFunc.getJsonValueStr("eChildSeatAvailable", carDetailsJson));
                        driverDataMap.put("eWheelChairAvailable", generalFunc.getJsonValueStr("eWheelChairAvailable", carDetailsJson));
                        driverDataMap.put("vCarType", generalFunc.getJsonValueStr("vCarType", carDetailsJson));
                        driverDataMap.put("vColour", generalFunc.getJsonValueStr("vColour", carDetailsJson));
                        driverDataMap.put("vLicencePlate", generalFunc.getJsonValueStr("vLicencePlate", carDetailsJson));
                        driverDataMap.put("make_title", generalFunc.getJsonValueStr("make_title", carDetailsJson));
                        driverDataMap.put("model_title", generalFunc.getJsonValueStr("model_title", carDetailsJson));
                        driverDataMap.put("fAmount", generalFunc.getJsonValueStr("fAmount", carDetailsJson));
                        driverDataMap.put("eRental", generalFunc.getJsonValueStr("vRentalCarType", carDetailsJson));
                        /*End of the day feature - driver is in destination Mode*/
                        driverDataMap.put("eDestinationMode", generalFunc.getJsonValueStr("eDestinationMode", obj_temp));


                        driverDataMap.put("vCurrencySymbol", generalFunc.getJsonValueStr("vCurrencySymbol", carDetailsJson));

                        driverDataMap.put("PROVIDER_RATING_COUNT", generalFunc.getJsonValueStr("PROVIDER_RATING_COUNT", obj_temp));

                        driverDataMap.put("eFareType", generalFunc.getJsonValueStr("eFareType", carDetailsJson));
                        driverDataMap.put("ePoolRide", generalFunc.getJsonValueStr("ePoolRide", carDetailsJson));
                        driverDataMap.put("fMinHour", generalFunc.getJsonValueStr("fMinHour", carDetailsJson));
                        driverDataMap.put("eTripStatusActive", generalFunc.getJsonValueStr("eTripStatusActive", obj_temp));
                        driverDataMap.put("eFavDriver", generalFunc.getJsonValueStr("eFavDriver", obj_temp));
                        driverDataMap.put("iStopId", generalFunc.getJsonValueStr("iStopId", carDetailsJson));
                        driverDataMap.put("eFly", mainAct.eFly?"Yes":"No");
                        driverDataMap.put("IS_PROVIDER_ONLINE", generalFunc.getJsonValueStr("IS_PROVIDER_ONLINE", obj_temp));
                        listOfDrivers.add(driverDataMap);
                    }

                }


                if (availCabArr == null || availCabArr.length() == 0) {
                    removeDriversFromMap(true);
                    if (mainAct != null) {
                        mainAct.notifyNoCabs();
                    }
                } else {
                    filterDrivers(false);
                }


            } else {
                removeDriversFromMap(true);
                if (parentView != null) {
                    InternetConnection int_conn = new InternetConnection(mContext);
                    String message_str = !int_conn.isNetworkConnected() && !int_conn.check_int() ? generalFunc.retrieveLangLBl("No Internet Connection", "LBL_NO_INTERNET_TXT") : generalFunc.retrieveLangLBl("Please try again.", "LBL_TRY_AGAIN_TXT");
                    generalFunc.showMessage(parentView, message_str);
                }

                if (mainAct != null) {
                    mainAct.notifyNoCabs();
                }
            }

        });
        exeWebServer.execute();
    }

    public boolean isCarTypesArrChanged(ArrayList<HashMap<String, String>> carTypeList) {
        if (mainAct.cabTypesArrList.size() != carTypeList.size()) {
            return true;
        }

        for (int i = 0; i < carTypeList.size(); i++) {
            String iVehicleTypeId = mainAct.cabTypesArrList.get(i).get("iVehicleTypeId");
            String newVehicleTypeId = carTypeList.get(i).get("iVehicleTypeId");

            if (!iVehicleTypeId.equals(newVehicleTypeId)) {
                return true;
            }
        }
        return false;
    }

    public String getFirstCarTypeID() {

        if (mainAct.app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || mainAct.isUfx) {
            return selectedCabTypeId;
        }
        for (int i = 0; i < mainAct.cabTypesArrList.size(); i++) {
            String iVehicleTypeId = mainAct.cabTypesArrList.get(i).get("iVehicleTypeId");

            return iVehicleTypeId;
        }
        return "";
    }

    public void setTaskKilledValue(boolean isTaskKilled) {
        this.isTaskKilled = isTaskKilled;

        if (isTaskKilled) {
            onPauseCalled();
        }
    }

    public void removeDriversFromMap(boolean isUnSubscribeAll) {
        if (driverMarkerList.size() > 0) {
            ArrayList<Marker> tempDriverMarkerList = new ArrayList<>();
            tempDriverMarkerList.addAll(driverMarkerList);
            for (int i = 0; i < tempDriverMarkerList.size(); i++) {
                Marker marker_temp = driverMarkerList.get(0);
                marker_temp.remove();
                driverMarkerList.remove(0);

            }
        }

        if (mainAct != null && isUnSubscribeAll) {
            ConfigPubNub.getInstance().unSubscribeToChannels(mainAct.getDriverLocationChannelList());
        }
    }


    public ArrayList<Marker> getDriverMarkerList() {
        return this.driverMarkerList;
    }

    public void filterDrivers(boolean isCheckAgain) {

        if (mainAct.pickUpLocation == null) {
            generalFunc.restartApp();
            return;
        }

        if (gMapView == null) {
            return;
        }

        double lowestKM = 0.0;
        boolean isFirst_lowestKM = true;

        ArrayList<HashMap<String, String>> currentLoadedDrivers = new ArrayList<>();

        ArrayList<Marker> driverMarkerList_temp = new ArrayList<>();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        if (mainAct.selectedSort.equalsIgnoreCase("eIsFeatured")) {

            Collections.sort(listOfDrivers, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> map1, HashMap<String, String> map2) {

                    if (map1.get("eIsFeatured").equalsIgnoreCase("Yes")) {
                        return  -1;
                    } else if (map2.get("eIsFeatured").equalsIgnoreCase("Yes")) {
                        return  1;
                    }else {
                        return  0;
                    }

                }
            });
        }
        else if (mainAct.selectedSort.equalsIgnoreCase("distance")) {
            Collections.sort(listOfDrivers, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> map1, HashMap<String, String> map2) {
                    return map1.get("DIST_TO_PICKUP_INT").compareTo(map2.get("DIST_TO_PICKUP_INT"));
                }
            });
        }
        else if (mainAct.selectedSort.equalsIgnoreCase("vAvgRating")) {
            Collections.sort(listOfDrivers, Collections.reverseOrder(new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
                    return t1.get("average_rating").compareTo(t2.get("average_rating"));
                }
            }));
        }
        else if (mainAct.selectedSort.equalsIgnoreCase("eFavDriver")) {

            Collections.sort(listOfDrivers, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> map1, HashMap<String, String> map2) {


                    if (map1.get("eFavDriver").equalsIgnoreCase("Yes")) {
                      return  -1;
                    } else  if (map2.get("eFavDriver").equalsIgnoreCase("Yes")) {
                       return  1;
                    }else {
                        return 0;
                    }

                }
            });
        }
        else if(mainAct.selectedSort.equalsIgnoreCase("IS_PROVIDER_ONLINE"))
        {Collections.sort(listOfDrivers, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> map1, HashMap<String, String> map2) {

                if (map1.get("IS_PROVIDER_ONLINE").equalsIgnoreCase("Yes")) {
                   return -1;
                } else if (map2.get("IS_PROVIDER_ONLINE").equalsIgnoreCase("Yes")) {
                    return  1;
                }else {
                    return 0;
                }
            }
        });

        }

        for (int i = 0; i < listOfDrivers.size(); i++) {
            HashMap<String, String> driverData = listOfDrivers.get(i);

            String driverName = driverData.get("Name");
            String[] vCarType = driverData.get("vCarType").split(",");

            boolean isCarSelected = Arrays.asList(vCarType).contains(selectedCabTypeId);

            String eHandiCapAccessibility = driverData.get("eHandiCapAccessibility");
            String eWheelChairAvailable = driverData.get("eWheelChairAvailable");
            String eChildSeatAvailable = driverData.get("eChildSeatAvailable");
            String eFemaleOnlyReqAccept = driverData.get("eFemaleOnlyReqAccept");

            String DriverGender = driverData.get("DriverGender");

            boolean isCarRental = true;

            if (mainAct.isRental || mainAct.iscubejekRental) {
                String[] vRentalCarType = driverData.get("eRental").split(",");
                if (vRentalCarType != null && vRentalCarType.length > 0) {
                    isCarRental = Arrays.asList(vRentalCarType).contains(selectedCabTypeId);
                    isCarSelected = Arrays.asList(vCarType).contains(selectedCabTypeId);
                }
            }
            mainAct.isCashSelected = !isMulti && mainAct.isMultiDelivery() ? false : mainAct.isCashSelected;


            String currentCabGeneralType=mainAct.getCurrentCabGeneralType();
            if (!isCarRental || (!isCarSelected && !(SERVICE_PROVIDER_FLOW.equalsIgnoreCase("PROVIDER") && currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_UberX))) || ((!mainAct.isPoolCabTypeIdSelected) && driverData.get("eTripStatusActive").equals("Yes")) || ((mainAct.isPoolCabTypeIdSelected || !currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) && (driverData.get("eDestinationMode").equalsIgnoreCase("YES"))) || (mainAct.isWheelChair && !eWheelChairAvailable.equalsIgnoreCase("yes")) ||
                    (mainAct.isChildSeat && !eChildSeatAvailable.equalsIgnoreCase("yes")) ||
                    (mainAct.ishandicap && !eHandiCapAccessibility.equalsIgnoreCase("yes") && currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) ||
                    (eFemaleOnlyReqAccept.equalsIgnoreCase("yes") && !generalFunc.getJsonValue("eGender", mainAct.userProfileJson).equalsIgnoreCase("FeMale") && currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) ||
                    (mainAct.isfemale && !DriverGender.equalsIgnoreCase("FeMale") && currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) || (driverData.get("ACCEPT_CASH_TRIPS").equalsIgnoreCase("No") && mainAct.isCashSelected && !currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) ||
                    (currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_UberX) && driverData.get("ACCEPT_CASH_TRIPS").equalsIgnoreCase("No") && mainAct.isCashSelected && generalFunc.getJsonValueStr("APP_PAYMENT_MODE", mainAct.obj_userProfile).equalsIgnoreCase("CASH"))
                    || (!selectProviderId.equals("") && !selectProviderId.equals(driverData.get("driver_id")))) {
                continue;
            }

            double driverLocLatitude = GeneralFunctions.parseDoubleValue(0.0, driverData.get("Latitude"));
            double driverLocLongitude = GeneralFunctions.parseDoubleValue(0.0, driverData.get("Longitude"));

            if (mainAct.pickUpLocation == null) {
                return;
            }

            double distance = Utils.CalculationByLocation(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude(), driverLocLatitude, driverLocLongitude, "");
            if (mainAct.isUfx) {
                if (mainAct.pickUpLocation != null) {
                    distance = Utils.CalculationByLocation(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude(), driverLocLatitude, driverLocLongitude, "");
                }
            }
            if (isFirst_lowestKM) {
                lowestKM = distance;
                isFirst_lowestKM = false;
            } else {
                if (distance < lowestKM) {
                    lowestKM = distance;
                }
            }

            float PROVIDER_RADIUS_int = GeneralFunctions.parseFloatValue(-1, driverData.get("PROVIDER_RADIUS"));

            if ((PROVIDER_RADIUS_int != -1 && distance < PROVIDER_RADIUS_int && currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) || (distance < RESTRICTION_KM_NEAREST_TAXI && !currentCabGeneralType.equalsIgnoreCase(Utils.CabGeneralType_UberX))) {
                driverData.put("DIST_TO_PICKUP", "" + distance);

                String dist = String.format("%.2f", (float) distance);

                driverData.put("DIST_TO_PICKUP_INT", "" + dist);

                String LBL_AWAY=generalFunc.retrieveLangLBl("away", "LBL_AWAY");

                if (generalFunc.getJsonValue("eUnit", userProfileJson).equals("KMs")) {
                    String LBL_KM_DISTANCE_TXT=generalFunc.retrieveLangLBl("", "LBL_KM_DISTANCE_TXT");
                    driverData.put("DIST_TO_PICKUP_INT_ROW", dist + " " + LBL_KM_DISTANCE_TXT + " " + LBL_AWAY);
                    driverData.put("LBL_KM_DISTANCE_TXT", "" + LBL_KM_DISTANCE_TXT);
                } else {
                    driverData.put("DIST_TO_PICKUP_INT_ROW", String.format("%.2f", (float) (distance * 0.621371)) + " " + generalFunc.retrieveLangLBl("", "LBL_MILE_DISTANCE_TXT") + " " + LBL_AWAY);
                    driverData.put("LBL_KM_DISTANCE_TXT", "" + generalFunc.retrieveLangLBl("", "LBL_MILE_DISTANCE_TXT"));
                }


                driverData.put("LBL_BTN_REQUEST_PICKUP_TXT", "" + generalFunc.retrieveLangLBl("", "LBL_BTN_REQUEST_PICKUP_TXT"));
                driverData.put("LBL_SEND_REQUEST", "" + generalFunc.retrieveLangLBl("", "LBL_SEND_REQ"));
                driverData.put("LBL_MORE_INFO_TXT", "" + generalFunc.retrieveLangLBl("More info", "LBL_MORE_DETAILS"));
                driverData.put("LBL_AWAY", "" + LBL_AWAY);
                currentLoadedDrivers.add(driverData);

                Marker driverMarker_temp = mainAct.getDriverMarkerOnPubNubMsg(driverData.get("driver_id"), true);

                if (driverMarker_temp != null) {
                    driverMarker_temp.remove();
                }

                builder.include(new LatLng(driverLocLatitude, driverLocLongitude));
                Marker driverMarker = drawMarker(new LatLng(driverLocLatitude, driverLocLongitude), driverName, driverData);
                driverMarkerList_temp.add(driverMarker);
            }
        }

        if (mainAct.pickUpLocation != null) {
            builder.include(new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude()));
        }

        removeDriversFromMap(false);


        driverMarkerList.addAll(driverMarkerList_temp);


        if (mainAct != null) {

            if (lowestKM > 1.5) {
                if (mainAct.isFirstZoomlevel) {
                    try {
                        mainAct.isFirstZoomlevel = false;
                        gMapView.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));

                    } catch (Exception e) {

                    }
                }

            }

            int lowestTime = ((int) (lowestKM * DRIVER_ARRIVED_MIN_TIME_PER_MINUTE));

            if (lowestTime < 1) {
                lowestTime = 1;
            }

            isAvailableCab = true;
            mainAct.setETA("" + lowestTime + "\n" + generalFunc.retrieveLangLBl("", "LBL_MIN_SMALL_TXT"));
        }

        if (mainAct != null) {

            ArrayList<String> unSubscribeChannelList = new ArrayList<>();
            ArrayList<String> subscribeChannelList = new ArrayList<>();

            ArrayList<String> currentDriverChannelsList = mainAct.getDriverLocationChannelList();
            ArrayList<String> newDriverChannelsList = mainAct.getDriverLocationChannelList(currentLoadedDrivers);

            for (int i = 0; i < currentDriverChannelsList.size(); i++) {
                String channel_name = currentDriverChannelsList.get(i);
                if (!newDriverChannelsList.contains(channel_name)) {
                    unSubscribeChannelList.add(channel_name);
                }
            }

            for (int i = 0; i < newDriverChannelsList.size(); i++) {
                String channel_name = newDriverChannelsList.get(i);
                if (!currentDriverChannelsList.contains(channel_name)) {
                    subscribeChannelList.add(channel_name);
                }
            }

            mainAct.setCurrentLoadedDriverList(currentLoadedDrivers);

            //changes
            ConfigPubNub.getInstance().subscribeToChannels(subscribeChannelList);
            ConfigPubNub.getInstance().unSubscribeToChannels(unSubscribeChannelList);
        }

        if (currentLoadedDrivers.size() == 0) {
            if (mainAct != null) {
                mainAct.notifyNoCabs();
            }

            if (isCheckAgain) {
                checkAvailableCabs();
            }
        } else {

            if (mainAct != null) {
                mainAct.notifyCabsAvailable();
            }
        }
    }

    public Marker drawMarker(LatLng point, String Name, HashMap<String, String> driverData) {

        MarkerOptions markerOptions = new MarkerOptions();
        String eIconType = generalFunc.getSelectedCarTypeData(selectedCabTypeId, mainAct.cabTypesArrList, "eIconType");

        int iconId = R.mipmap.car_driver;
        if (eIconType.equalsIgnoreCase("Bike")) {
            iconId = R.mipmap.car_driver_1;
        } else if (eIconType.equalsIgnoreCase("Cycle")) {
            iconId = R.mipmap.car_driver_2;
        } else if (eIconType.equalsIgnoreCase("Truck")) {
            iconId = R.mipmap.car_driver_4;
        } else if (eIconType.equalsIgnoreCase("Fly")) {
            iconId = R.mipmap.ic_fly_icon;
        }

        SelectableRoundedImageView providerImgView = null;
        View marker_view = null;

        if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("UberX")) {
            marker_view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.uberx_provider_maker_design, null);
            providerImgView = (SelectableRoundedImageView) marker_view
                    .findViewById(R.id.providerImgView);

            providerImgView.setImageResource(R.mipmap.ic_no_pic_user);

            markerOptions.position(point).title("DriverId" + driverData.get("driver_id")).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mContext, marker_view)))
                    .anchor(0.5f, 0.5f).flat(true);
        } else {
            if (mainAct != null) {
                if (mainAct.isUfx) {
                    marker_view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.uberx_provider_maker_design, null);
                    providerImgView = (SelectableRoundedImageView) marker_view
                            .findViewById(R.id.providerImgView);

                    if (driverData.get("eIsFeatured").equals("Yes")) {
                        providerImgView.setBorderColor(mainAct.getResources().getColor(R.color.marker_img_border));
                    } else {
                        providerImgView.setBorderColor(mainAct.getResources().getColor(R.color.appThemeColor_Dark_1));
                    }

                    providerImgView.setImageResource(R.mipmap.ic_no_pic_user);
                    markerOptions.position(point).title("DriverId" + driverData.get("driver_id")).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mContext, marker_view)))
                            .anchor(0.5f, 0.5f).flat(true);
                } else {
                    markerOptions.position(point).title("DriverId" + driverData.get("driver_id")).icon(BitmapDescriptorFactory.fromResource(iconId))
                            .anchor(0.5f, 0.5f).flat(true);
                }
            } else {
                markerOptions.position(point).title("DriverId" + driverData.get("driver_id")).icon(BitmapDescriptorFactory.fromResource(iconId))
                        .anchor(0.5f, 0.5f).flat(true);
            }

        }

        // Adding marker on the Google Map
        final Marker marker = gMapView.addMarker(markerOptions);
        marker.setRotation(0);
        marker.setVisible(true);

        if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("UberX") &&
                !driverData.get("driver_img").equals("") && !driverData.get("driver_img").equals("NONE") && providerImgView != null && marker_view != null) {

            String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + driverData.get("driver_id") + "/"
                    + driverData.get("driver_img");

            final View finalMarker_view = marker_view;
            Picasso.get()
                    .load(image_url/*"http://www.hellocle.com/wp-content/themes/hello/images/hello-logo-stone.png"*/)
                    //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(providerImgView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            try {

                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mContext, finalMarker_view)));
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onError(Exception e){

                        }
                    });
        }

        if (mainAct != null) {
            if (mainAct.isUfx) {
                if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) &&
                        !driverData.get("driver_img").equals("") && !driverData.get("driver_img").equals("NONE") && providerImgView != null && marker_view != null) {

                    String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + driverData.get("driver_id") + "/"
                            + driverData.get("driver_img");

                    final View finalMarker_view = marker_view;
                    Picasso.get()
                            .load(image_url/*"http://www.hellocle.com/wp-content/themes/hello/images/hello-logo-stone.png"*/)
                           // .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(providerImgView, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    try {
                                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mContext, finalMarker_view)));

                                    } catch (Exception e) {

                                    }
                                }

                                @Override
                                public void onError(Exception e){

                                }
                            });
                }
            }
        }

        return marker;
    }

    public void onPauseCalled() {

        if (updateDriverListTask != null) {
            updateDriverListTask.stopRepeatingTask();
        }
    }

    public void onResumeCalled() {
        if (updateDriverListTask != null && !isTaskKilled) {
            updateDriverListTask.startRepeatingTask();
        }
    }

    @Override
    public void onTaskRun() {
        checkAvailableCabs();
    }

    //    ========================================================================================================
//    UBER X DRIVERS MARKERS AND DRIVER DETAILS
    public HashMap<String, String> getMarkerDetails(Marker marker) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < listOfDrivers.size(); i++) {
            if (marker.getTitle().replace("DriverId", "").trim().equalsIgnoreCase(listOfDrivers.get(i).get("driver_id"))) {
//                loadDriverDetails(listOfDrivers.get(i));
                map = listOfDrivers.get(i);
            }
        }
        return map;
    }

    public void getMarkerDetails(String driverId) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < listOfDrivers.size(); i++) {
            if (driverId.trim().equalsIgnoreCase(listOfDrivers.get(i).get("driver_id"))) {
                selectProviderId = "";
                loadDriverDetails(listOfDrivers.get(i));
            }
        }
    }

    public void loadDriverDetails(final HashMap<String, String> map) {
        //  PROVIDER_RATING_COUNT
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.uber_x_driver_detail_dialog);
        ((SimpleRatingBar) dialog.findViewById(R.id.ratingBar)).setRating(GeneralFunctions.parseFloatValue(0, map.get("average_rating")));
        ((MTextView) dialog.findViewById(R.id.nameTxt)).setText(map.get("Name") + " " + map.get("LastName"));

        ((MTextView) dialog.findViewById(R.id.milesTxt)).setText(map.get("DIST_TO_PICKUP_INT") + " " + map.get("LBL_KM_DISTANCE_TXT"));


        ((MTextView) dialog.findViewById(R.id.milesawayTxt)).setText(map.get("LBL_AWAY"));

        ((MTextView) dialog.findViewById(R.id.reviewTxt)).setText(map.get("PROVIDER_RATING_COUNT") + " " + generalFunc.retrieveLangLBl("Reviews", "LBL_REVIEWS"));


//        ImageView backCoverImage = (ImageView) dialog.findViewById(R.id.backCoverImage);

        if (map.get("fAmount") != null && !map.get("fAmount").trim().equals("") && !map.get("fAmount").trim().equals("0")) {
            ((MTextView) dialog.findViewById(R.id.priceTxt)).setText(map.get("fAmount"));
        } else {
            (dialog.findViewById(R.id.priceTxt)).setVisibility(View.GONE);
        }

        MTextView minHourTxtView = ((MTextView) dialog.findViewById(R.id.minHourTxtView));
        String fMinHour = map.get("fMinHour");
        String eFareType = map.get("eFareType");

        if (eFareType.equalsIgnoreCase("Hourly") && !fMinHour.equalsIgnoreCase("") && Integer.parseInt(fMinHour) > 1) {
            minHourTxtView.setVisibility(View.VISIBLE);
            minHourTxtView.setText("" + "(" + generalFunc.retrieveLangLBl("", "LBL_MINIMUM_TXT") + " " + fMinHour + " " + generalFunc.retrieveLangLBl("", "LBL_HOURS_TXT") + ")");
        } else {
            minHourTxtView.setVisibility(View.GONE);
        }


        if (map.get("tProfileDescription") != null && !map.get("tProfileDescription").equals("")) {
            ((MTextView) dialog.findViewById(R.id.descTxt)).setText(map.get("tProfileDescription"));
        } else {
            ((MTextView) dialog.findViewById(R.id.descTxt)).setText("----------");

        }
        ((MTextView) dialog.findViewById(R.id.descHTxt)).setText(generalFunc.retrieveLangLBl("ABOUT EXPERT", "LBL_ABOUT_EXPERT"));
        MButton btn_type2 = ((MaterialRippleLayout) dialog.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
        ((MTextView) dialog.findViewById(R.id.driverDTxt)).setText(generalFunc.retrieveLangLBl("Washer Detail", "LBL_DRIVER_DETAIL"));

        String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + map.get("driver_id") + "/" + map.get("driver_img");

        SelectableRoundedImageView userProfileImgView = (SelectableRoundedImageView) dialog.findViewById(R.id.driverImgView);

        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_no_pic_user)
                .error(R.mipmap.ic_no_pic_user)
                .into(((SelectableRoundedImageView) dialog.findViewById(R.id.driverImgView)));

//        new DownloadProfileImg(mContext, userProfileImgView,
//                image_url,
//                "", backCoverImage).execute();


        btn_type2.setOnClickListener(view -> {

            closeDialog();

            mainAct.redirectToMapOrList(Utils.Cab_UberX_Type_List, true);
            mainAct.setSelectedDriverId(map.get("driver_id"));
            selectProviderId = map.get("driver_id");
//                mainAct.selectSourceLocArea.performClick();

            mainAct.selectedprovidername = map.get("Name") + " " + map.get("LastName");
            mainAct.ACCEPT_CASH_TRIPS = map.get("ACCEPT_CASH_TRIPS");


            if (map.get("fAmount") != null && !map.get("fAmount").trim().equals("")) {
                mainAct.UfxAmount = map.get("fAmount");
                mainAct.vCurrencySymbol = map.get("vCurrencySymbol");


            }

            mainAct.continuePickUpProcess();
        });

        (dialog.findViewById(R.id.closeImg)).setOnClickListener(view -> closeDialog());


        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(dialog);
        }
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

//                              OVER MARKERS AND DRIVER DETAILS
//    ======================================================================================================

}
