package com.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.MyWalletActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.SearchLocationActivity;
import com.melevicarbrasil.usuario.deliverAll.MapDelegate;
import com.general.files.AddDrawer;
import com.general.files.ConfigPubNub;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.LocalNotification;
import com.general.files.MapServiceApi;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.general.files.UpdateFrequentTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.utils.AnimateMarker;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverAssignedHeaderFragment extends Fragment implements UpdateFrequentTask.OnTaskRunCalled, ViewTreeObserver.OnGlobalLayoutListener, MapDelegate {

    public boolean isDriverArrived = false;
    public boolean isDriverArrivedNotGenerated = false;
    public MTextView destLocSelectTxt;
    public boolean isBackVisible = false;
    public LatLng driverLocation;
    public LatLng pickUpLocation;
    public LatLng destLocation;
    public LatLng sourceLocation;
    public boolean isTripStart = false;
    MainActivity mainAct;
    GeneralFunctions generalFunc;
    String userProfileJson;
    View view;
    public ImageView backImgView, menuImgView;
    MTextView titleTxt;
    DriverAssignedHeaderFragment driverAssignedHFrag;
    GoogleMap gMap;
    boolean isGooglemapSet = false;
    UpdateFrequentTask updateDriverLocTask;
    public UpdateFrequentTask updateDestMarkerTask;
    int DRIVER_LOC_FETCH_TIME_INTERVAL;
    int DESTINATION_UPDATE_TIME_INTERVAL;
    boolean isTaskKilled = false;
    String iDriverId = "";
    int notificationCount = 0;
    public HashMap<String, String> driverData;
    long currentNotificationTime = 0;
    public Polyline route_polyLine;
    public MTextView sourceLocSelectTxt;

    ImageView imgAddDestbtn, imgEditDestbtn;

    AnimateMarker animDriverMarker;

    public Marker destinationPointMarker_temp;
    public Marker driverMarker;
    public Marker time_marker;
    public Marker sourceMarker;

    String eType;
    int DRIVER_ARRIVED_MIN_TIME_PER_MINUTE = 3;
    String driverAppVersion = "1";
    AddDrawer addDrawer;

    boolean isMapMoveToDriverLoc = false;

    boolean isload = true;

    LinearLayout destarea;

    public String eConfirmByUser = "No";
    androidx.appcompat.app.AlertDialog alertDialog_surgeConfirm;
    double tollamount = 0.0;
    String tollcurrancy = "";
    boolean istollIgnore = false;
    androidx.appcompat.app.AlertDialog tolltax_dialog;

    String eTollConfirmByUser = "";

    LinearLayout tollbarArea;

    public int fragmentWidth = 0;
    public int fragmentHeight = 0;

    double lowestKM = 0.0;

    float defaultMarkerAnimDuration = 1200;

    String latitude = "";
    String longitirude = "";
    String address = "";
    MTextView pickUpLocHTxt, destLocHTxt;
    public String eWalletIgnore = "No";
    boolean isRouteFail = false;
    public String distance = "";
    public String time = "";
    public boolean isSkip = false;
    public boolean isroutefound = false;
    private final static double DEFAULT_CURVE_ROUTE_CURVATURE = 0.5f;
    private final static int DEFAULT_CURVE_POINTS = 60;
    String LBL_FLY_ARRIVED = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_driver_assigned_header, container, false);

        animDriverMarker = new AnimateMarker();
        mainAct = (MainActivity) getActivity();
        generalFunc = mainAct.generalFunc;
        userProfileJson = mainAct.userProfileJson;
        driverAssignedHFrag = mainAct.getDriverAssignedHeaderFrag();
        gMap = mainAct.getMap();

        if (!isMultiDelivery()) {
            addDrawer = new AddDrawer(getActContext(), userProfileJson, false);
            addDrawer.setIsDriverAssigned(true);
        }
        backImgView = (ImageView) view.findViewById(R.id.backImgView);
        menuImgView = (ImageView) view.findViewById(R.id.menuImgView);
        destarea = (LinearLayout) view.findViewById(R.id.destarea);
        tollbarArea = (LinearLayout) view.findViewById(R.id.tollbarArea);
        pickUpLocHTxt = (MTextView) view.findViewById(R.id.pickUpLocHTxt);
        destLocHTxt = (MTextView) view.findViewById(R.id.destLocHTxt);
        menuImgView.setVisibility(View.VISIBLE);

        String PUBNUB_DISABLED = generalFunc.retrieveValue(Utils.PUBNUB_DISABLED_KEY);

        if (PUBNUB_DISABLED.equalsIgnoreCase("NO")) {
            defaultMarkerAnimDuration = 2400;
        }


        if (!isMultiDelivery()) {
            menuImgView.setVisibility(View.VISIBLE);
            menuImgView.setOnClickListener(new setOnClickList());
        }

        titleTxt = (MTextView) view.findViewById(R.id.titleTxt);
        sourceLocSelectTxt = (MTextView) view.findViewById(R.id.sourceLocSelectTxt);
        destLocSelectTxt = (MTextView) view.findViewById(R.id.destLocSelectTxt);
        imgAddDestbtn = (ImageView) view.findViewById(R.id.imgAddDestbtn);
        imgEditDestbtn = (ImageView) view.findViewById(R.id.imgEditDestbtn);

        if (!isMultiDelivery()) {
            backImgView.setImageResource(R.mipmap.ic_drawer_menu);
        }

        backImgView.setOnClickListener(new setOnClickList());

        sourceLocSelectTxt.setOnClickListener(new setOnClickList());
        destLocSelectTxt.setOnClickListener(new setOnClickList());
        DRIVER_ARRIVED_MIN_TIME_PER_MINUTE = generalFunc.parseIntegerValue(3, generalFunc.getJsonValue("DRIVER_ARRIVED_MIN_TIME_PER_MINUTE", userProfileJson));

        int btnRadius = (int) getActContext().getResources().getDimension(R.dimen._5sdp);
        int nowColor = getResources().getColor(R.color.pickup_req_now_btn);
        int laterColor = getResources().getColor(R.color.pickup_req_later_btn);
        int strokeColor = Color.parseColor("#00000000");

        new CreateRoundedView(nowColor, btnRadius, 0, strokeColor, view.findViewById(R.id.imgsourcearea1));
        new CreateRoundedView(laterColor, btnRadius, 0, strokeColor, view.findViewById(R.id.imagemarkerdest2));

        LBL_FLY_ARRIVED = generalFunc.retrieveLangLBl("", "LBL_FLY_ARRIVED");

        setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_EN_ROUTE_TXT"));

        String DriverTripStatus = driverData != null && driverData.containsKey("DriverTripStatus") ? driverData.get("DriverTripStatus") : "";

        if (isMultiDelivery()) {


            if (DriverTripStatus.equalsIgnoreCase("Arrived")) {
                String eFly = driverData.get("eFly");
                boolean isFly = Utils.checkText(eFly) && eFly.equalsIgnoreCase("Yes");

                if (driverData.get("eFly").equalsIgnoreCase("Yes")) {
                    setDriverStatusTitle(LBL_FLY_ARRIVED);
                } else if (driverData.get("eType").equalsIgnoreCase("Deliver") || isMultiDelivery()) {
                    setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_CARRIER_ARRIVED_TXT"));

                } else {
                    setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVED_TXT"));

                }
                isDriverArrived = true;
                isDriverArrivedNotGenerated = true;
                LocalNotification.dispatchLocalNotification(getActContext(), isFly ? LBL_FLY_ARRIVED : generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVED_NOTIFY_TXT"), true);


            }
        }

        setData();

        if (generalFunc.getJsonValue("vTripStatus", userProfileJson).equals("On Going Trip")) {

            setTripStartValue(true);
        } else {
            configDestinationView();
        }

        if (DriverTripStatus.equalsIgnoreCase("Arrived")) {
            String eFly = driverData.get("eFly");
            boolean isFly = Utils.checkText(eFly) && eFly.equalsIgnoreCase("Yes");
            if (isFly) {
                setDriverStatusTitle(LBL_FLY_ARRIVED);
            } else if (driverData.get("eType").equalsIgnoreCase("Deliver") || isMultiDelivery()) {
                setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_CARRIER_ARRIVED_TXT"));

            } else {
                setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVED_TXT"));

            }
            isDriverArrived = true;
            isDriverArrivedNotGenerated = true;
            LocalNotification.dispatchLocalNotification(getActContext(), isFly ? LBL_FLY_ARRIVED : generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVED_NOTIFY_TXT"), true);


        }

        if (isMultiDelivery() && generalFunc.getJsonValue("vTripStatus", userProfileJson).equals("On Going Trip")) {
            setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_EN_ROUTE_TXT"));
            mainAct.emeTapImgView.setVisibility(View.VISIBLE);
        }

        if (generalFunc.retrieveValue(Utils.APP_TYPE).equalsIgnoreCase("UberX")) {
            destLocSelectTxt.setVisibility(View.GONE);
            if (generalFunc.retrieveValue(Utils.APP_DESTINATION_MODE).equalsIgnoreCase(Utils.STRICT_DESTINATION) || generalFunc.retrieveValue(Utils.APP_DESTINATION_MODE).equalsIgnoreCase(Utils.NON_STRICT_DESTINATION)) {
                if (destLocSelectTxt.getVisibility() == View.GONE) {
                    destLocSelectTxt.setVisibility(View.VISIBLE);
                }
            }
        } else {
            destLocSelectTxt.setVisibility(View.VISIBLE);
        }

        if (mainAct != null && !isMultiDelivery()) {
            mainAct.addDrawer.setMenuImgClick(view, true);
        }

        handleEditDest();
        addGlobalLayoutListner();
        mainAct.setPanelHeight(175); // to reset map data according to frag height
        mainAct.userLocBtnImgView.performClick();
        return view;
    }

    public void removeSurceMarker() {
        if (sourceMarker != null) {
            sourceMarker.remove();
            sourceMarker = null;
        }
    }

    public void addPickupMarker() {

        if (time_marker != null) {
            time_marker.remove();
            time_marker = null;
        }
        removeSurceMarker();
        sourceMarker = gMap.addMarker(new MarkerOptions().position(pickUpLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_source_marker)));
    }

    public void setData() {
        pickUpLocHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PICK_UP_FROM"));
        destLocHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DROP_AT"));

        HashMap<String, String> driverData = (HashMap<String, String>) getArguments().getSerializable("TripData");
        this.driverData = driverData;

        iDriverId = driverData.get("iDriverId");
        driverAppVersion = driverData.get("DriverAppVersion");
        sourceLocSelectTxt.setText(driverData.get("PickUpAddress"));

        driverLocation = new LatLng(generalFunc.parseDoubleValue(0.0, driverData.get("DriverLatitude")),
                generalFunc.parseDoubleValue(0.0, driverData.get("DriverLongitude")));
        pickUpLocation = new LatLng(generalFunc.parseDoubleValue(0.0, driverData.get("PickUpLatitude")),
                generalFunc.parseDoubleValue(0.0, driverData.get("PickUpLongitude")));


        if (driverData.get("destLatitude") != null && !driverData.get("destLatitude").equalsIgnoreCase("")) {
            destLocation = new LatLng(generalFunc.parseDoubleValue(0.0, driverData.get("destLatitude")),
                    generalFunc.parseDoubleValue(0.0, driverData.get("destLongitude")));


        }

        if (isMultiDelivery()) {
            isBackVisible = true;
            backImgView.setImageResource(R.mipmap.ic_back_arrow);
            menuImgView.setVisibility(View.GONE);
        } else {
            isBackVisible = false;
            backImgView.setImageResource(R.mipmap.ic_drawer_menu);
        }

        if (Utils.checkText(mainAct.getDestAddress()) && isMultiDelivery()) {
            destLocSelectTxt.setText(mainAct.getDestAddress());
        } /*else if (mainAct.isDeliver(mainAct.getCurrentCabGeneralType())) {
            destLocSelectTxt.setVisibility(View.GONE);

        }*/ else if (mainAct.getDestinationStatus()) {
            destLocSelectTxt.setText(mainAct.getDestAddress());
            imgAddDestbtn.setVisibility(View.GONE);
            imgEditDestbtn.setVisibility(View.VISIBLE);

            String eFlatTrip = driverData.get("eFlatTrip");
            String ePoolRide = driverData.get("ePoolRide");
            String eFly = driverData.get("eFly");
            String eDestinationMode = driverData.get("eDestinationMode");

            if ((eFlatTrip != null && eFlatTrip.equalsIgnoreCase("Yes")) || (eDestinationMode != null && eDestinationMode.equalsIgnoreCase("Yes")) || isFlatTrip || (ePoolRide != null && ePoolRide.equalsIgnoreCase("Yes")) || (eFly != null && eFly.equalsIgnoreCase("Yes")) || Utils.checkText(driverData.get("iStopId"))) {
                imgEditDestbtn.setVisibility(View.GONE);
                destarea.setOnClickListener(null);
            }
            else {

                destarea.setOnClickListener(new setOnClickList());
            }
        } else {

            if (!driverData.get("ePoolRide").equalsIgnoreCase("Yes") && !Utils.checkText(driverData.get("iStopId")) && !driverData.get("eFly").equalsIgnoreCase("Yes") && !driverData.get("eDestinationMode").equalsIgnoreCase("Yes")) {

                destLocSelectTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_DESTINATION_BTN_TXT"));
                imgAddDestbtn.setVisibility(View.VISIBLE);
                imgEditDestbtn.setVisibility(View.GONE);
                destarea.setOnClickListener(new setOnClickList());
            } else {
                imgEditDestbtn.setVisibility(View.GONE);
                destarea.setOnClickListener(null);

            }
        }

        if (gMap != null && isGooglemapSet == false) {
            isGooglemapSet = true;

            gMap.clear();
            configDriverLoc();
        }


        eType = driverData.get("eType");


        if (!generalFunc.getJsonValue("IS_DEST_ANYTIME_CHANGE", userProfileJson).equalsIgnoreCase("yes") || !eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
            imgEditDestbtn.setVisibility(View.GONE);
            if (mainAct.getDestinationStatus()) {
                destarea.setOnClickListener(null);
            }
        }
        String eFlatTrip = driverData.get("eFlatTrip");
        String ePoolRide = driverData.get("ePoolRide");
        String eDestinationMode = driverData.get("eDestinationMode");

        if (eFlatTrip != null && eFlatTrip.equalsIgnoreCase("Yes") || (eDestinationMode != null && eDestinationMode.equalsIgnoreCase("Yes")) || ePoolRide.equalsIgnoreCase("Yes") || Utils.checkText(driverData.get("iStopId"))) {
            imgEditDestbtn.setVisibility(View.GONE);
            destarea.setOnClickListener(null);
        }


    }

    public boolean isMultiDelivery() {
        if (driverData == null) {
            HashMap<String, String> driverData = (HashMap<String, String>) getArguments().getSerializable("TripData");
            this.driverData = driverData;
        }

        mainAct.isMultiDeliveryTrip = driverData.get("eType").equalsIgnoreCase(Utils.eType_Multi_Delivery);

        return driverData.get("eType").equalsIgnoreCase(Utils.eType_Multi_Delivery);
    }

    public void setGoogleMap(GoogleMap map) {
        this.gMap = map;
        if (isGooglemapSet == false) {
            gMap.clear();
        }
    }

    public void configDriverLoc() {
        if (driverLocation == null) {
            setData();
            return;
        }
        rotateMarkerBasedonDistance(driverLocation, "");


        if (mainAct != null) {
            subscribeToDriverLocChannel();
        }

        notifyDriverArrivedTime("" + driverLocation.latitude, "" + driverLocation.longitude);


    }

    public void subscribeToDriverLocChannel() {
        if (mainAct != null) {
            ArrayList<String> channelName = new ArrayList<>();
            channelName.add(Utils.pubNub_Update_Loc_Channel_Prefix + iDriverId);

            ConfigPubNub.getInstance().subscribeToChannels(channelName);
        }
    }

    public void unSubscribeToDriverLocChannel() {
        if (mainAct != null && ConfigPubNub.getInstance() != null) {
            ArrayList<String> channelName = new ArrayList<>();
            channelName.add(Utils.pubNub_Update_Loc_Channel_Prefix + iDriverId);
            ConfigPubNub.getInstance().unSubscribeToChannels(channelName);
        }
    }

    public void scheduleDriverLocUpdate() {

        DRIVER_LOC_FETCH_TIME_INTERVAL = (generalFunc.parseIntegerValue(1, generalFunc.getJsonValue("DRIVER_LOC_FETCH_TIME_INTERVAL", userProfileJson))) * 1 * 1000;

        if (updateDriverLocTask == null) {
            updateDriverLocTask = new UpdateFrequentTask(DRIVER_LOC_FETCH_TIME_INTERVAL);
            updateDriverLocTask.setTaskRunListener(this);
            onResumeCalled();
        }
    }

    public void setTaskKilledValue(boolean isTaskKilled) {
        this.isTaskKilled = isTaskKilled;

        if (isTaskKilled == true) {
            onPauseCalled();
        }

    }


    public void setTripStartValue(boolean isTripStart) {

        this.isTripStart = isTripStart;
        if (mainAct != null) {
            mainAct.isTripStarted = isTripStart;
        }
        if (generalFunc == null) {
            generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        }

        if (isTripStart == true) {
            setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_EN_ROUTE_TXT"));


            mainAct.emeTapImgView.setVisibility(View.VISIBLE);
        }

        if (time_marker != null) {
            time_marker.remove();
            time_marker = null;
        }
        configDestinationView();
    }

    public void setDriverStatusTitle(String title) {
        try {
            ((MTextView) view.findViewById(R.id.titleTxt)).setText(title);
            if (!isMultiDelivery()) {
                backImgView.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onTaskRun() {
        //   updateDriverLocations();
    }

//    public void updateDriverLocations() {
//        HashMap<String, String> parameters = new HashMap<String, String>();
//        parameters.put("type", "getDriverLocations");
//        parameters.put("iDriverId", iDriverId);
//        parameters.put("UserType", Utils.app_type);
//
//        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
//        exeWebServer.setDataResponseListener(responseString -> {
//
//            if (responseString != null && !responseString.equals("")) {
//
//                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
//
//                if (isDataAvail == true) {
//
//                    String vLatitude = generalFunc.getJsonValue("vLatitude", responseString);
//                    String vLongitude = generalFunc.getJsonValue("vLongitude", responseString);
//                    String vTripStatus = generalFunc.getJsonValue("vTripStatus", responseString);
//
//                    if (vTripStatus.equals("Arrived")) {
//                        String arrivedNotificationMsg = "";
//
//                        if (eType != null) {
//                            if (eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
//                                arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVING_TXT");
//                            } else {
//                                arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_CARRIER_ARRIVING_TXT");
//                            }
//                        } else {
//                            arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVING_TXT");
//                        }
//                        setDriverStatusTitle(arrivedNotificationMsg);
//                        if (isDriverArrivedNotGenerated == false) {
//                            isDriverArrivedNotGenerated = true;
//                            addDrawer.configDrawer(false);
//
//                            LocalNotification.dispatchLocalNotification(getActContext(), arrivedNotificationMsg, true);
//
//                            generalFunc.showGeneralMessage("", arrivedNotificationMsg);
//                        }
//                        isDriverArrived = true;
//
//                    }
//
//                    LatLng driverLocation_update = new LatLng(generalFunc.parseDoubleValue(0.0, vLatitude),
//                            generalFunc.parseDoubleValue(0.0, vLongitude));
//
//                    rotateMarkerBasedonDistance(driverLocation_update, "");
//
//
//                    if (vTripStatus.equals("Active")) {
//                        updateDriverArrivedTime();
//                    }
//                }
//            }
//        });
//        exeWebServer.execute();
//    }

    public void rotateMarkerBasedonDistance(LatLng driverLocation_update, String message_json) {

        if (driverLocation == null) {
            driverLocation = driverLocation_update;
        }

        float rotation = driverMarker == null ? 0 : driverMarker.getRotation();


        if (animDriverMarker.currentLng != null) {
            rotation = (float) animDriverMarker.bearingBetweenLocations(animDriverMarker.currentLng, driverLocation_update);
        } else {
            rotation = (float) animDriverMarker.bearingBetweenLocations(driverLocation, driverLocation_update);
        }

        driverLocation = driverLocation_update;

        if (isMapMoveToDriverLoc == false) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(driverLocation_update)
                    .zoom(Utils.defaultZomLevel).build();
            mainAct.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            isMapMoveToDriverLoc = true;
        }

        Location driver_loc = new Location("gps");
        driver_loc.setLatitude(driverLocation_update.latitude);
        driver_loc.setLongitude(driverLocation_update.longitude);
        if (driverMarker == null) {
            MarkerOptions markerOptions_driver = new MarkerOptions();
            markerOptions_driver.position(driverLocation);

            String eIconType = driverData.get("eIconType");

            int iconId = R.mipmap.car_driver;
            if (eIconType.equalsIgnoreCase("Bike")) {
                iconId = R.mipmap.car_driver_1;
            } else if (eIconType.equalsIgnoreCase("Cycle")) {
                iconId = R.mipmap.car_driver_2;
            } else if (eIconType.equalsIgnoreCase("Truck")) {
//                iconId = R.mipmap.car_driver_5;
                iconId = R.mipmap.car_driver_4;
            } else if (eIconType.equalsIgnoreCase("Fly")) {
                iconId = R.mipmap.ic_fly_icon;
            }

            tempdriverLocation_update = driverLocation;

            markerOptions_driver.icon(BitmapDescriptorFactory.fromResource(iconId)).anchor(0.5f,
                    0.5f).flat(true);
            driverMarker = gMap.addMarker(markerOptions_driver);
            driverMarker.setTitle(iDriverId);
        }


        if (message_json != null && message_json != "" && GeneralFunctions.isJSONValid(message_json) == true) {

            HashMap<String, String> previousItemOfMarker = animDriverMarker.getLastLocationDataOfMarker(driverMarker);
            HashMap<String, String> data_map = new HashMap<>();
            data_map.put("vLatitude", "" + driverLocation_update.latitude);
            data_map.put("vLongitude", "" + driverLocation_update.longitude);
            data_map.put("iDriverId", "" + this.iDriverId);
            data_map.put("RotationAngle", "" + rotation);
            data_map.put("LocTime", "" + generalFunc.getJsonValue("LocTime", message_json));

            if (animDriverMarker.toPositionLat.get("" + driverLocation_update.latitude) == null && animDriverMarker.toPositionLat.get("" + driverLocation_update.longitude) == null) {
                if (previousItemOfMarker.get("LocTime") != null && !previousItemOfMarker.get("LocTime").equals("")) {

                    long previousLocTime = generalFunc.parseLongValue(0, previousItemOfMarker.get("LocTime"));
                    long newLocTime = generalFunc.parseLongValue(0, data_map.get("LocTime"));

                    if (previousLocTime != 0 && newLocTime != 0) {

                        if ((newLocTime - previousLocTime) > 0 && animDriverMarker.driverMarkerAnimFinished == false) {
                            animDriverMarker.addToListAndStartNext(driverMarker, this.gMap, driver_loc, rotation, defaultMarkerAnimDuration, iDriverId, data_map.get("LocTime"));
                        } else if ((newLocTime - previousLocTime) > 0) {
                            animDriverMarker.animateMarker(driverMarker, this.gMap, driver_loc, rotation, defaultMarkerAnimDuration, iDriverId, data_map.get("LocTime"));
                        }

                    } else if ((previousLocTime == 0 || newLocTime == 0) && animDriverMarker.driverMarkerAnimFinished == false) {
                        animDriverMarker.addToListAndStartNext(driverMarker, this.gMap, driver_loc, rotation, defaultMarkerAnimDuration, iDriverId, data_map.get("LocTime"));
                    } else {
                        animDriverMarker.animateMarker(driverMarker, this.gMap, driver_loc, rotation, defaultMarkerAnimDuration, iDriverId, data_map.get("LocTime"));
                    }
                } else if (animDriverMarker.driverMarkerAnimFinished == false) {
                    animDriverMarker.addToListAndStartNext(driverMarker, this.gMap, driver_loc, rotation, defaultMarkerAnimDuration, iDriverId, data_map.get("LocTime"));
                } else {
                    animDriverMarker.animateMarker(driverMarker, this.gMap, driver_loc, rotation, defaultMarkerAnimDuration, iDriverId, data_map.get("LocTime"));
                }
            }

        } else {
            animDriverMarker.animateMarker(driverMarker, this.gMap, driver_loc, rotation, defaultMarkerAnimDuration, iDriverId, "");
        }
    }

    public LatLng tempdriverLocation_update = null;

    public void updateDriverLocation(String message) {
        if (message == null || !Utils.checkText(message)) {
            return;
        }
        String vLatitude = generalFunc.getJsonValue("vLatitude", message);
        String vLongitude = generalFunc.getJsonValue("vLongitude", message);
        String driverId = generalFunc.getJsonValue("iDriverId", message);

        if (!driverId.equalsIgnoreCase(iDriverId)) {
            return;
        }
        LatLng driverLocation_update = new LatLng(generalFunc.parseDoubleValue(0.0, vLatitude),
                generalFunc.parseDoubleValue(0.0, vLongitude));
        tempdriverLocation_update = driverLocation_update;

        rotateMarkerBasedonDistance(driverLocation_update, message);

        if (isTripStart == false) {
            notifyDriverArrivedTime(vLatitude, vLongitude);
        }

        if (mainAct.isUserTripClick) {
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(driverLocation_update)
//                    .zoom(Utils.defaultZomLevel).build();
//            mainAct.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mainAct.animateToLocation(driverLocation_update.latitude, driverLocation_update.longitude, mainAct.getMap().getCameraPosition().zoom);
        }

    }

    public void notifyDriverArrivedTime(String vLatitude, String vLongitude) {
        double distance = Utils.CalculationByLocation(pickUpLocation.latitude, pickUpLocation.longitude,
                generalFunc.parseDoubleValue(0.0, vLatitude), generalFunc.parseDoubleValue(0.0, vLongitude), "");
        int totalTimeInMinParKM = ((int) (distance * DRIVER_ARRIVED_MIN_TIME_PER_MINUTE));

        if (totalTimeInMinParKM == 0) {
            totalTimeInMinParKM = 0;
        } else if (totalTimeInMinParKM < 1) {
            totalTimeInMinParKM = 1;
        }

        if (totalTimeInMinParKM < 3 && isDriverArrived == false) {
            setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_ARRIVING_TXT"));
//            if (addDrawer != null) {
//                addDrawer.configDrawer(false);
//            }

        }

        if ((totalTimeInMinParKM == 1 || totalTimeInMinParKM == 3) && notificationCount < 3 && isDriverArrived == false) {

            if (currentNotificationTime < 1 || (System.currentTimeMillis() - currentNotificationTime) > 1 * 60 * 1000) {

                currentNotificationTime = System.currentTimeMillis();

                notificationCount = notificationCount + 1;

                String arrivedNotificationMsg = "";

                if (eType != null) {
                    if (driverData.get("eFly").equalsIgnoreCase("Yes")) {
                        arrivedNotificationMsg = LBL_FLY_ARRIVED;
                    } else if (eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                        arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVING_TXT");
                    } else {
                        arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_CARRIER_ARRIVING_TXT");
                    }
                } else {
                    if (driverData.get("eFly").equalsIgnoreCase("Yes")) {
                        arrivedNotificationMsg = LBL_FLY_ARRIVED;
                    } else {
                        arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVING_TXT");
                    }
                }
                LocalNotification.dispatchLocalNotification(getActContext(), arrivedNotificationMsg, true);
            }
        }


        Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(getActContext(), R.drawable.driver_time_marker,
                getTimeTxt(totalTimeInMinParKM), true, R.string.defaultFont);

        if (isRouteFail) {

            setMarkerBasedOnTripStatus(marker_time_ic);
        }

    }

    public void setMarkerBasedOnTripStatus(Bitmap marker_time_ic) {
        try {
            if ((isDriverArrived || driverData.get("DriverTripStatus").equalsIgnoreCase("Arrived")) && !isTripStart) {

                if (time_marker != null) {
                    time_marker.remove();
                    time_marker = null;
                }
                return;
            }
            if (time_marker != null) {
                time_marker.remove();
                time_marker = null;
            }

            if (isDriverArrived) {

                if (isTripStart) {
                    time_marker = gMap.addMarker(
                            new MarkerOptions().position(destLocation)
                                    .icon(BitmapDescriptorFactory.fromBitmap(marker_time_ic)));
                } else {
                    time_marker = gMap.addMarker(
                            new MarkerOptions().position(pickUpLocation)
                                    .icon(BitmapDescriptorFactory.fromBitmap(marker_time_ic)));
                }
            } else {
                if (isTripStart) {

                    time_marker = gMap.addMarker(
                            new MarkerOptions().position(destLocation)
                                    .icon(BitmapDescriptorFactory.fromBitmap(marker_time_ic)));
                } else {
                    time_marker = gMap.addMarker(
                            new MarkerOptions().position(pickUpLocation)
                                    .icon(BitmapDescriptorFactory.fromBitmap(marker_time_ic)));
                }


            }
        } catch (Exception e) {

            Logger.d("Excpetion", ":: setMarkerBasedOnTripStatus()");

        }


        //mainAct.userLocBtnImgView.performClick();


    }

//    public void updateDriverArrivedTime() {
//        if (mainAct == null) {
//            return;
//        }
//        if (userProfileJson != null && !generalFunc.getJsonValue("ENABLE_DIRECTION_SOURCE_DESTINATION_USER_APP", userProfileJson).equalsIgnoreCase("Yes")) {
//
//
//            double distance = Utils.CalculationByLocation(pickUpLocation.latitude, pickUpLocation.longitude, driverLocation.latitude, driverLocation.longitude, "");
//
//
//            int lowestTime = ((int) (distance * DRIVER_ARRIVED_MIN_TIME_PER_MINUTE));
//
//            if (lowestTime < 1) {
//                lowestTime = 1;
//            }
//
//            Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(getActContext(), R.drawable.driver_time_marker,
//                    getTimeTxt(lowestTime), true);
//
//            setMarkerBasedOnTripStatus(marker_time_ic);
//
//            return;
//
//        }
//
//        String originLoc = pickUpLocation.latitude + "," + pickUpLocation.longitude;
//        String destLoc = driverLocation.latitude + "," + driverLocation.longitude;
//        String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
//        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + originLoc + "&destination=" + destLoc + "&sensor=true&key=" + serverKey + "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true";
//
//
//        final String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
//
//
//        String trip_data = generalFunc.getJsonValue("TripDetails", userProfileJson);
//
//        String eTollSkipped = generalFunc.getJsonValue("eTollSkipped", trip_data);
//
//        if (eTollSkipped == "Yes") {
//            url = url + "&avoid=tolls";
//        }
//        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
//
//        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
//            @Override
//            public void setResponse(String responseString) {
//
//                if (responseString != null && !responseString.equals("")) {
//
//                    String arrivedNotificationMsg = "";
//
//                    if (eType != null) {
//                        if (eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
//                            arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVING_TXT");
//                        } else {
//                            arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_CARRIER_ARRIVING_TXT");
//                        }
//                    } else {
//                        arrivedNotificationMsg = generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVING_TXT");
//                    }
//
//                    String status = generalFunc.getJsonValue("status", responseString);
//
//                    if (status.equals("OK")) {
//
//                        JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
//                        if (obj_routes != null && obj_routes.length() > 0) {
//
//
//                            int duration = (int) Math.round((generalFunc.parseDoubleValue(0.0,
//                                    generalFunc.getJsonValue("value", generalFunc.getJsonValue("duration",
//                                            generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0).toString())))) / 60);
//
//
//
//                            if (duration < 3 && isDriverArrived == false) {
//
//                                setDriverStatusTitle(arrivedNotificationMsg);
//                                addDrawer.configDrawer(false);
//                            }
//
//                            if (duration < 1) {
//                                duration = 1;
//                            }
//
//                            if ((duration == 1 || duration == 3) && notificationCount < 3) {
//
//
//                                if ((System.currentTimeMillis() - currentNotificationTime) < 60000) {
//                                    currentNotificationTime = System.currentTimeMillis();
//                                    notificationCount = notificationCount + 1;
//                                    LocalNotification.dispatchLocalNotification(getActContext(), arrivedNotificationMsg, true);
//                                }
//                            }
//
//                            Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(getActContext(), R.drawable.driver_time_marker,
//                                    getTimeTxt(duration), true);
//
//                            setMarkerBasedOnTripStatus(marker_time_ic);
//
//                        }
//
//                    }
//
//                }
//            }
//        });
//        exeWebServer.execute();
//    }

    public String getTimeTxt(int duration) {

        if (duration < 1) {
            duration = 1;
        }
        String durationTxt = "";
        String timeToreach = duration == 0 ? "--" : "" + duration;

        timeToreach = duration > 60 ? formatHoursAndMinutes(duration) : timeToreach;


        durationTxt = (duration < 60 ? generalFunc.retrieveLangLBl("", "LBL_MINS_SMALL") : generalFunc.retrieveLangLBl("", "LBL_HOUR_TXT"));

        durationTxt = duration == 1 ? generalFunc.retrieveLangLBl("", "LBL_MIN_SMALL") : durationTxt;
        durationTxt = duration > 120 ? generalFunc.retrieveLangLBl("", "LBL_HOURS_TXT") : durationTxt;

        return timeToreach + "\n" + durationTxt;
    }


    boolean isFindrouteCalled = false;
    boolean isScheduleDestRouteCalled = false;

    LatLng sourceLatlng, destLatLng;

    public void findRoute(final String latitude, final String longitude, final String address) {
        isFindrouteCalled = true;
        isScheduleDestRouteCalled = false;


        try {
            String originLoc = mainAct.getPickUpLocation().getLatitude() + "," + mainAct.getPickUpLocation().getLongitude();

            String destLoc = null;
            destLoc = latitude + "," + longitude;
            sourceLatlng = new LatLng(mainAct.getPickUpLocation().getLatitude(), mainAct.getPickUpLocation().getLongitude());
            destLatLng = new LatLng(GeneralFunctions.parseDoubleValue(0, latitude), GeneralFunctions.parseDoubleValue(0, longitude));
//            mProgressBar.setIndeterminate(true);
//            mProgressBar.setVisibility(View.VISIBLE);
            String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
            //String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + originLoc + "&destination=" + destLoc + "&sensor=true&key=" + serverKey + "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true";

            HashMap<String, String> hashMap = new HashMap<>();


            hashMap.put("s_latitude", mainAct.getPickUpLocation().getLatitude() + "");
            hashMap.put("s_longitude", mainAct.getPickUpLocation().getLongitude() + "");
            hashMap.put("d_latitude", latitude + "");
            hashMap.put("d_longitude", longitude + "");

            String parameters = "origin=" + originLoc + "&destination=" + destLoc;

            hashMap.put("parameters", parameters);


            MapServiceApi.getDirectionservice(getActContext(), hashMap, this, null, true);


//            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
//            exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
//            exeWebServer.setDataResponseListener(responseString -> {
//
////                mProgressBar.setIndeterminate(false);
////                mProgressBar.setVisibility(View.INVISIBLE);
//
//                if (responseString != null && !responseString.equals("")) {
//                    String status = generalFunc.getJsonValue("status", responseString);
//                    if (status.equals("OK")) {
//                        isRouteFail = false;
//
//                        JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
//                        if (obj_routes != null && obj_routes.length() > 0) {
//                            JSONObject obj_legs = generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0);
//
//
//                            distance = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
//                                    generalFunc.getJsonValue("distance", obj_legs.toString()).toString())));
//
//                            time = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
//                                    generalFunc.getJsonValue("duration", obj_legs.toString()).toString())));
//
//                            sourceLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("start_location", obj_legs.toString()))),
//                                    GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("start_location", obj_legs.toString()))));
//
//                            destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("end_location", obj_legs.toString()))),
//                                    GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("end_location", obj_legs.toString()))));
//
//                            if (getActivity() != null) {
//                                addDestination(latitude, longitude, address, distance, time);
//                            }
//
//
//                        }
//
//                    } else {
//
//
//                        isRouteFail = true;
//                        if (!isSkip) {
//                            GenerateAlertBox alertBox = new GenerateAlertBox(getActContext());
//                            alertBox.setContentMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
//                            alertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
//                            alertBox.setBtnClickList(btn_id -> {
//                                alertBox.closeAlertBox();
//                                mainAct.userLocBtnImgView.performClick();
//
//                            });
//                            alertBox.showAlertBox();
//                        }
//
//                        if (isSkip) {
//                            isRouteFail = false;
//
//                        } else {
//                            mainAct.userLocBtnImgView.performClick();
//                        }
//
//                        isSkip = true;
////                        if (getActivity() != null) {
////                            estimateFare(null, null);
////                        }
//                    }
//                }
//            });
//            exeWebServer.execute();
        } catch (Exception e) {

        }
    }


    public void addDestination(final String latitude, final String longitude, final String address, final String vDistance, final String vDuration) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "addDestination");
        parameters.put("eWalletIgnore", eWalletIgnore);
        parameters.put("Latitude", latitude);
        parameters.put("Longitude", longitude);
        parameters.put("Address", address);
        parameters.put("TripId", driverData.get("iTripId"));
        parameters.put("eConfirmByUser", eConfirmByUser);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);
        parameters.put("eTollConfirmByUser", eTollConfirmByUser);
        parameters.put("fTollPrice", tollamount + "");
        parameters.put("vTollPriceCurrencyCode", tollcurrancy);
        parameters.put("vDistance", vDistance);
        parameters.put("vDuration", vDuration);
        String tollskiptxt = "";

        if (istollIgnore) {
            tollamount = 0;
            tollskiptxt = "Yes";
        } else {
            tollskiptxt = "No";
        }
        String TripDetails = generalFunc.getJsonValue("TripDetails", userProfileJson);

        if (generalFunc.getJsonValue("vTripPaymentMode", TripDetails).equalsIgnoreCase("Card") && generalFunc.getJsonValue("ePayWallet", TripDetails).equalsIgnoreCase("Yes") && !generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
            parameters.put("eWalletDebitAllow", "Yes");
            parameters.put("ePayWallet", "Yes");
        }
        parameters.put("eTollSkipped", tollskiptxt);

        destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, latitude),
                GeneralFunctions.parseDoubleValue(0.0, longitude));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(mainAct.getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {

                    if (route_polyLine != null) {
                        route_polyLine.remove();
                    }

                    if (time_marker != null) {
                        time_marker.remove();
                        time_marker = null;
                    }
                    tollamount = 0.0;
                    mainAct.setDestinationPoint(latitude, longitude, address, true);

                    setDestinationAddress("");
                    configDestinationView();

                    if (mainAct.isTripStarted) {
                        mainAct.userLocBtnImgView.performClick();
                    } else {
                        destLocSelectTxt.performClick();
                    }

                    if (eConfirmByUser.equalsIgnoreCase("Yes")) {
                        imgEditDestbtn.setVisibility(View.GONE);
                        destarea.setOnClickListener(null);
                    }

                } else {


                    String msg_str = generalFunc.getJsonValue(Utils.message_str, responseString);


                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {


                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }


                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT ", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_OK") :
                                generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), button_Id -> {
                            if (button_Id == 1) {

                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else {
                                if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                                    eWalletIgnore = "Yes";
                                    addDestination(latitude, longitude, address, vDistance, vDuration);
                                }
                            }
                        });
                        return;
                    }

                    if (msg_str.equalsIgnoreCase("LBL_DROP_LOCATION_NOT_ALLOW")) {
                        tollamount = 0.0;
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DROP_LOCATION_NOT_ALLOW"));
                        return;
                    }

                    if (msg_str.equalsIgnoreCase("Yes")) {
                        if (generalFunc.getJsonValue("SurgePrice", responseString).equalsIgnoreCase("")) {
                            openFixChargeDialog(responseString, false);
                        } else {
                            openFixChargeDialog(responseString, true);
                        }
                        return;
                    }

                    String eTollExist = generalFunc.getJsonValue("eTollExist", responseString);

                    if (eTollExist.equalsIgnoreCase("Yes")) {
                        TollTaxDialog();
                        return;
                    }

                    if (msg_str.equals(Utils.GCM_FAILED_KEY) || msg_str.equals(Utils.APNS_FAILED_KEY)) {
                        generalFunc.restartApp();
                    } else {
                        generalFunc.showGeneralMessage("",
                                generalFunc.retrieveLangLBl("", msg_str));
                    }
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public void getTollcostValue() {

        if (generalFunc.retrieveValue(Utils.ENABLE_TOLL_COST).equalsIgnoreCase("Yes")) {
            eTollConfirmByUser = "No";
            String url = CommonUtilities.TOLLURL + generalFunc.retrieveValue(Utils.TOLL_COST_APP_ID)
                    + "&app_code=" + generalFunc.retrieveValue(Utils.TOLL_COST_APP_CODE) + "&waypoint0=" + pickUpLocation.latitude
                    + "," + pickUpLocation.longitude + "&waypoint1=" + latitude + "," + longitirude + "&mode=fastest;car";

            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
            exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
            exeWebServer.setDataResponseListener(responseString -> {

                if (responseString != null && !responseString.equals("")) {

                    if (generalFunc.getJsonValue("onError", responseString).equalsIgnoreCase("FALSE")) {
                        try {

                            String costs = generalFunc.getJsonValue("costs", responseString);

                            //  String details=generalFunc.getJsonValue("details",c)

                            String currency = generalFunc.getJsonValue("currency", costs);
                            String details = generalFunc.getJsonValue("details", costs);
                            String tollCost = generalFunc.getJsonValue("tollCost", details);
                            if (!currency.equals("") && currency != null) {
                                tollcurrancy = currency;
                            }
                            if (!tollCost.equals("") && tollCost != null && !tollCost.equals("0.0")) {
                                tollamount = generalFunc.parseDoubleValue(0.0, tollCost);
                            }

                            // addDestination(latitude, longitirude, address);
                            findRoute(latitude, longitirude, address);
                        } catch (Exception e) {
                            tollamount = 0.0;
                            tollcurrancy = "";
                            //addDestination(latitude, longitirude, address);
                            findRoute(latitude, longitirude, address);
                        }
                    } else {
                        tollamount = 0.0;
                        tollcurrancy = "";
                        // addDestination(latitude, longitirude, address);
                        findRoute(latitude, longitirude, address);
                    }
                } else {
                    tollamount = 0.0;
                    tollcurrancy = "";
                    //addDestination(latitude, longitirude, address);
                    findRoute(latitude, longitirude, address);
                }

            });
            exeWebServer.execute();
        } else {
            eTollConfirmByUser = "Yes";
            //addDestination(latitude, longitirude, address);
            findRoute(latitude, longitirude, address);
        }

    }


    public void TollTaxDialog() {

        if (tollamount != 0.0 && tollamount != 0 && tollamount != 0.00) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

            LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.dialog_tolltax, null);

            final MTextView tolltaxTitle = (MTextView) dialogView.findViewById(R.id.tolltaxTitle);
            final MTextView tollTaxMsg = (MTextView) dialogView.findViewById(R.id.tollTaxMsg);
            final MTextView tollTaxpriceTxt = (MTextView) dialogView.findViewById(R.id.tollTaxpriceTxt);
            final MTextView cancelTxt = (MTextView) dialogView.findViewById(R.id.cancelTxt);

            final CheckBox checkboxTolltax = (CheckBox) dialogView.findViewById(R.id.checkboxTolltax);

            checkboxTolltax.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (checkboxTolltax.isChecked()) {
                    istollIgnore = true;
                } else {
                    istollIgnore = false;
                }

            });


            MTextView btn_type2 = dialogView.findViewById(R.id.btn_type2);
            int submitBtnId = Utils.generateViewId();
            btn_type2.setId(submitBtnId);
            btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN"));
            btn_type2.setOnClickListener(v -> {
                tolltax_dialog.dismiss();
                eTollConfirmByUser = "Yes";
                //istollIgnore = false;

                // addDestination(latitude, longitirude, address);
                findRoute(latitude, longitirude, address);
            });

            builder.setView(dialogView);
            tolltaxTitle.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_ROUTE"));
            tollTaxMsg.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_PRICE_DESC"));

            tollTaxMsg.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_PRICE_DESC"));

            tollTaxpriceTxt.setText(
                    generalFunc.retrieveLangLBl("Total toll price", "LBL_TOLL_PRICE_TOTAL") + ": " + tollcurrancy + " " + tollamount);

            checkboxTolltax.setText(generalFunc.retrieveLangLBl("", "LBL_IGNORE_TOLL_ROUTE"));
            cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));

            cancelTxt.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 tollamount = 0.0;
                                                 tolltax_dialog.dismiss();
                                             }
                                         }
            );

            tolltax_dialog = builder.create();
            if (generalFunc.isRTLmode() == true) {
                generalFunc.forceRTLIfSupported(tolltax_dialog);
            }
            tolltax_dialog.setCancelable(false);
            tolltax_dialog.show();
        } else {
            //addDestination(latitude, longitirude, address);
            findRoute(latitude, longitirude, address);
        }
    }

    public void handleEditDest() {
        try {

            if (eConfirmByUser.equalsIgnoreCase("Yes")) {
                eConfirmByUser = "yes";
                imgEditDestbtn.setVisibility(View.GONE);
                destarea.setOnClickListener(null);
            }
        } catch (Exception e) {

        }
    }

    String payableAmount = "";
    boolean isFlatTrip = false;

    public void openFixChargeDialog(String responseString, boolean isSurCharge) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.surge_confirm_design, null);
        builder.setView(dialogView);

        MTextView payableAmountTxt;
        MTextView payableTxt;

        ((MTextView) dialogView.findViewById(R.id.headerMsgTxt)).setText(generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_FIX_FARE_HEADER")));


        ((MTextView) dialogView.findViewById(R.id.tryLaterTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRY_LATER"));

        payableTxt = (MTextView) dialogView.findViewById(R.id.payableTxt);
        payableAmountTxt = (MTextView) dialogView.findViewById(R.id.payableAmountTxt);
        if (!generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString).equalsIgnoreCase("")) {
            payableAmountTxt.setVisibility(View.VISIBLE);
            payableTxt.setVisibility(View.GONE);

            if (isSurCharge) {

                payableAmount = generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString) + " " + "(" + generalFunc.retrieveLangLBl("", "LBL_AT_TXT") + " " +
                        generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("SurgePrice", responseString)) + ")";
                ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(payableAmount));
            } else {
                payableAmount = generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString);
                ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(payableAmount));

            }
        } else {
            payableAmountTxt.setVisibility(View.GONE);
            payableTxt.setVisibility(View.VISIBLE);

        }

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_TXT"));
        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(view -> {

            alertDialog_surgeConfirm.dismiss();
            eConfirmByUser = "Yes";

            isFlatTrip = true;
            tollamount = 0;
            // addDestination(latitude, longitirude, address);
            findRoute(latitude, longitirude, address);


        });
        (dialogView.findViewById(R.id.tryLaterTxt)).setOnClickListener(view -> {
            tollamount = 0.0;
            alertDialog_surgeConfirm.dismiss();

        });


        alertDialog_surgeConfirm = builder.create();
        alertDialog_surgeConfirm.setCancelable(false);
        alertDialog_surgeConfirm.setCanceledOnTouchOutside(false);
        if (generalFunc.isRTLmode() == true) {
            generalFunc.forceRTLIfSupported(alertDialog_surgeConfirm);
        }

        alertDialog_surgeConfirm.show();
    }


    public void setDestinationAddress(String eFlatTrip) {
        destLocSelectTxt.setText(mainAct.getDestAddress());

        if (!eFlatTrip.equalsIgnoreCase("") && eFlatTrip.equalsIgnoreCase("Yes")) {
            mainAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    isFlatTrip = true;
                    imgEditDestbtn.setVisibility(View.GONE);
                    imgAddDestbtn.setVisibility(View.GONE);

                }
            });

            destarea.setOnClickListener(null);
            return;
        }

        //destLocTxt.setOnClickListener(null);

        imgAddDestbtn.setVisibility(View.GONE);
        if (generalFunc.getJsonValue("IS_DEST_ANYTIME_CHANGE", userProfileJson).equalsIgnoreCase("yes")
                && eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
            imgEditDestbtn.setVisibility(View.VISIBLE);

            if (eFlatTrip != null && eFlatTrip.equalsIgnoreCase("Yes") || driverData.get("eFly").equalsIgnoreCase("Yes") || driverData.get("eDestinationMode").equalsIgnoreCase("Yes") || driverData.get("ePoolRide").equalsIgnoreCase("Yes") || Utils.checkText(driverData.get("iStopId"))) {
                imgEditDestbtn.setVisibility(View.GONE);
                destarea.setOnClickListener(null);
            }
        } else {
            imgEditDestbtn.setVisibility(View.GONE);
            destarea.setOnClickListener(null);
        }
    }

    public void configDestinationView() {


        if (driverData.get("DriverTripStatus").equalsIgnoreCase("Arrived") && !isTripStart) {
            removeSurceMarker();
            sourceMarker = gMap.addMarker(new MarkerOptions().position(pickUpLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_source_marker)));

            return;
        }

        if (mainAct == null) {
            return;
        }
        final String destLocLatitude = mainAct.getDestLocLatitude();
        final String destLocLongitude = mainAct.getDestLocLongitude();

        DESTINATION_UPDATE_TIME_INTERVAL = (int) ((generalFunc.parseDoubleValue(2, generalFunc.getJsonValue("DESTINATION_UPDATE_TIME_INTERVAL", userProfileJson))) * 60 * 1000);

        if (mainAct.getDestinationStatus()) {
            setDestinationAddress("");
        }

        if (updateDestMarkerTask != null) {
            updateDestMarkerTask.stopRepeatingTask();
            updateDestMarkerTask = null;
        }

        if (updateDestMarkerTask == null) {
            updateDestMarkerTask = new UpdateFrequentTask(DESTINATION_UPDATE_TIME_INTERVAL);
            //  updateDestMarkerTask.startRepeatingTask();
            updateDestMarkerTask.setTaskRunListener(() -> {
                if (gMap != null) {
/*
                    if (destMarker == null) {
                        MarkerOptions markerOptions_destLocation = new MarkerOptions();
                        markerOptions_destLocation.position(new LatLng(generalFunc.parseDoubleValue(0.0, destLocLatitude),
                                generalFunc.parseDoubleValue(0.0, destLocLongitude)));
                        markerOptions_destLocation.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dest_marker)).anchor(0.5f,
                                0.5f);
                        destMarker = gMap.addMarker(markerOptions_destLocation);
                    }*/

                    if (!isFindrouteCalled) {
                        scheduleDestRoute(destLocLatitude, destLocLongitude);
                    }
                }
            });
            onResumeCalled();
        }
    }


    public void scheduleDestRoute(final String destLocLatitude, final String destLocLongitude) {

        if (mainAct == null) {
            return;
        }
        String originLoc = "";
        String destLoc = "";
        LatLng destLatLng = null;
        Location location;
        Location destlocation;
        HashMap<String, String> hashMap = new HashMap<>();

        if (!isTripStart) {
            originLoc = driverLocation.latitude + "," + driverLocation.longitude;
            destLoc = pickUpLocation.latitude + "," + pickUpLocation.longitude;
            destLatLng = new LatLng(pickUpLocation.latitude, pickUpLocation.longitude);
            location = new Location("offline");
            location.setLatitude(driverLocation.latitude);
            location.setLongitude(driverLocation.longitude);

            hashMap.put("s_latitude", driverLocation.latitude + "");
            hashMap.put("s_longitude", driverLocation.longitude + "");
            hashMap.put("d_latitude", pickUpLocation.latitude + "");
            hashMap.put("d_longitude", pickUpLocation.longitude + "");


        } else {
            if (driverLocation == null) {
                originLoc = pickUpLocation.latitude + "," + pickUpLocation.longitude;
                location = new Location("offline");
                location.setLatitude(pickUpLocation.latitude);
                location.setLongitude(pickUpLocation.longitude);
                hashMap.put("s_latitude", pickUpLocation.latitude + "");
                hashMap.put("s_longitude", pickUpLocation.longitude + "");
                sourceLatlng = new LatLng(pickUpLocation.latitude, pickUpLocation.longitude);
            } else {
                originLoc = driverLocation.latitude + "," + driverLocation.longitude;
                hashMap.put("s_latitude", driverLocation.latitude + "");
                hashMap.put("s_longitude", driverLocation.longitude + "");
                location = new Location("offline");
                location.setLatitude(driverLocation.latitude);
                location.setLongitude(driverLocation.longitude);
                sourceLatlng = new LatLng(driverLocation.latitude, driverLocation.longitude);
            }

            destLatLng = new LatLng(GeneralFunctions.parseDoubleValue(0.0, destLocLatitude), GeneralFunctions.parseDoubleValue(0.0, destLocLongitude));

            destLoc = destLocLatitude + "," + destLocLongitude;
            hashMap.put("d_latitude", destLocLatitude + "");
            hashMap.put("d_longitude", destLocLongitude + "");
        }


        this.destLatLng = destLatLng;

        if (userProfileJson != null && !generalFunc.getJsonValue("ENABLE_DIRECTION_SOURCE_DESTINATION_USER_APP", userProfileJson).equalsIgnoreCase("Yes")) {


            if (pickUpLocation != null) {
                double distance = Utils.CalculationByLocation(location.getLatitude(), location.getLongitude(), destLatLng.latitude, destLatLng.longitude, "");

                int temp = (int) distance;


                int lowestTime = temp * DRIVER_ARRIVED_MIN_TIME_PER_MINUTE;

                if (lowestTime < 1) {
                    lowestTime = 1;
                }

                Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(getActContext(), R.drawable.driver_time_marker,
                        getTimeTxt(lowestTime), true, R.string.defaultFont);
                setMarkerBasedOnTripStatus(marker_time_ic);
            }
            return;

        }


        isFindrouteCalled = false;
        isScheduleDestRouteCalled = true;

        String parameters = "origin=" + originLoc + "&destination=" + destLoc;

        hashMap.put("parameters", parameters);


        MapServiceApi.getDirectionservice(getActContext(), hashMap, this, null, false);

        // String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
        // String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + originLoc + "&destination=" + destLoc + "&sensor=true&key=" + serverKey + "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true";

//
//        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
//
//        final LatLng finalDestLatLng = destLatLng;
//        exeWebServer.setDataResponseListener(responseString -> {
//
//            if (responseString != null && !responseString.equals("")) {
//
//                String status = generalFunc.getJsonValue("status", responseString);
//
//                if (status.equals("OK")) {
//
//                    PolylineOptions lineOptions = generalFunc.getGoogleRouteOptions(responseString, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black));
//
//
//                    if (lineOptions != null) {
//                        if (route_polyLine != null) {
//                            route_polyLine.remove();
//                        }
//                        route_polyLine = gMap.addPolyline(lineOptions);
//                    }
//
//
//                    JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
//                    if (obj_routes != null && obj_routes.length() > 0) {
//                        String duration_str =
//                                generalFunc.getJsonValue("text", generalFunc.getJsonValue("duration",
//                                        generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0).toString()));
//
//
//                        int duration = (int) Math.round((generalFunc.parseDoubleValue(0.0,
//                                generalFunc.getJsonValue("value", generalFunc.getJsonValue("duration",
//                                        generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0).toString())))) / 60);
//                        if (duration < 1) {
//                            duration = 1;
//                        }
//
//                        Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(getActContext(), R.drawable.driver_time_marker,
//                                getTimeTxt(duration), true, R.string.defaultFont);
//                        setMarkerBasedOnTripStatus(marker_time_ic);
//
//                    }
//                }
//            }
//        });
//        exeWebServer.execute();
    }


    public static String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }

    public void onPauseCalled() {

        if (updateDriverLocTask != null) {
            updateDriverLocTask.stopRepeatingTask();
            //updateDestMarkerTask = null;
        }
        if (updateDestMarkerTask != null) {
            updateDestMarkerTask.stopRepeatingTask();
            // updateDestMarkerTask = null;
        }

        unSubscribeToDriverLocChannel();


    }


    public void releaseAllTask() {


        if (updateDriverLocTask != null) {
            updateDriverLocTask.stopRepeatingTask();
            //updateDriverLocTask = null;
        }

        if (updateDestMarkerTask != null) {
            updateDestMarkerTask.stopRepeatingTask();
            //updateDestMarkerTask = null;
        }

        if (time_marker != null) {
            time_marker.remove();
            time_marker = null;
        }

        if (route_polyLine != null) {
            route_polyLine.remove();
            route_polyLine = null;
        }
        if (destinationPointMarker_temp != null) {
            destinationPointMarker_temp.remove();
        }
        unSubscribeToDriverLocChannel();
    }

    public void onResumeCalled() {
        if (updateDriverLocTask != null && isTaskKilled == false) {
            updateDriverLocTask.startRepeatingTask();
        }

        if (updateDestMarkerTask != null && isTaskKilled == false) {
            updateDestMarkerTask.startRepeatingTask();
        }

        subscribeToDriverLocChannel();
    }


    @Override
    public void onResume() {
        super.onResume();

        addGlobalLayoutListner();
        String eFlatTrip = driverData.get("eFlatTrip");
        if (eFlatTrip != null && eFlatTrip.equalsIgnoreCase("Yes")) {
            imgEditDestbtn.setVisibility(View.GONE);
        }
    }

    public Context getActContext() {
        return mainAct.getActContext();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.SEARCH_DEST_LOC_REQ_CODE && resultCode == mainAct.RESULT_OK && data != null) {
            latitude = data.getStringExtra("Latitude");
            longitirude = data.getStringExtra("Longitude");
            address = data.getStringExtra("Address");

            //addDestination(latitude, longitirude, address);
            getTollcostValue();

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActContext());
    }

    private void addGlobalLayoutListner() {

        if (getView() != null) {
            getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        if (view != null) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        if (getView() != null) {

            getView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        } else if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }


    @Override
    public void onGlobalLayout() {
        boolean heightChanged = false;
        if (getView() != null || view != null) {
            if (getView() != null) {

                if (getView().getHeight() != 0 && getView().getHeight() != fragmentHeight) {
                    heightChanged = true;
                }
                fragmentWidth = getView().getWidth();
                fragmentHeight = getView().getHeight();
            } else if (view != null) {

                if (view.getHeight() != 0 && view.getHeight() != fragmentHeight) {
                    heightChanged = true;
                }

                fragmentWidth = view.getWidth();
                fragmentHeight = view.getHeight();
            }

            Logger.e("FragHeight", "is :::" + fragmentHeight + "\n" + "Frag Width is :::" + fragmentWidth);

          /*  if (heightChanged && fragmentWidth != 0 && fragmentHeight != 0) {
                mainAct.setPanelHeight(fragmentHeight);
            }*/
        }
    }

    @Override
    public void searchResult(ArrayList<HashMap<String, String>> placelist, int selectedPos, String input) {

    }

    @Override
    public void resetOrAddDest(int selPos, String address, double latitude, double longitude, String isSkip) {

    }

    public PolylineOptions createCurveRoute(LatLng origin, LatLng dest) {

        double distance = SphericalUtil.computeDistanceBetween(origin, dest);
        double heading = SphericalUtil.computeHeading(origin, dest);
        double halfDistance = distance > 0 ? (distance / 2) : (distance * DEFAULT_CURVE_ROUTE_CURVATURE);

        // Calculate midpoint position
        LatLng midPoint = SphericalUtil.computeOffset(origin, halfDistance, heading);

        // Calculate position of the curve center point
        double sqrCurvature = DEFAULT_CURVE_ROUTE_CURVATURE * DEFAULT_CURVE_ROUTE_CURVATURE;
        double extraParam = distance / (4 * DEFAULT_CURVE_ROUTE_CURVATURE);
        double midPerpendicularLength = (1 - sqrCurvature) * extraParam;
        double r = (1 + sqrCurvature) * extraParam;

        LatLng circleCenterPoint = SphericalUtil.computeOffset(midPoint, midPerpendicularLength, heading + 90.0);

        // Calculate heading between circle center and two points
        double headingToOrigin = SphericalUtil.computeHeading(circleCenterPoint, origin);

        // Calculate positions of points on the curve
        double step = Math.toDegrees(Math.atan(halfDistance / midPerpendicularLength)) * 2 / DEFAULT_CURVE_POINTS;
        //Polyline options
        PolylineOptions options = new PolylineOptions();

        for (int i = 0; i < DEFAULT_CURVE_POINTS; ++i) {
            LatLng pi = SphericalUtil.computeOffset(circleCenterPoint, r, headingToOrigin + i * step);
            options.add(pi);
        }


        return options;
    }

    @Override
    public void directionResult(HashMap<String, String> directionlist) {

        String responseString = directionlist.get("routes");

        if (isFindrouteCalled) {

            if (responseString != null && !responseString.equalsIgnoreCase("") && directionlist.get("distance") == null) {
                if (!responseString.equalsIgnoreCase("null")) {
                    isRouteFail = false;

//                    JSONArray obj_routes = generalFunc.getJsonArray(responseString);
                    JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
                    if (obj_routes != null && obj_routes.length() > 0) {
                        JSONObject obj_legs = generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0);


                        distance = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
                                generalFunc.getJsonValue("distance", obj_legs.toString()).toString())));

                        time = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
                                generalFunc.getJsonValue("duration", obj_legs.toString()).toString())));

                        sourceLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("start_location", obj_legs.toString()))),
                                GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("start_location", obj_legs.toString()))));

                        destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("end_location", obj_legs.toString()))),
                                GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("end_location", obj_legs.toString()))));

                        if (getActivity() != null) {
                            addDestination(latitude, longitirude, address, distance, time);
                        }


                    }

                } else {


                    isRouteFail = true;
                    if (!isSkip) {
                        GenerateAlertBox alertBox = new GenerateAlertBox(getActContext());
                        alertBox.setContentMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
                        alertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        alertBox.setBtnClickList(btn_id -> {
                            alertBox.closeAlertBox();
                            mainAct.userLocBtnImgView.performClick();

                        });
                        alertBox.showAlertBox();
                    }

                    if (isSkip) {
                        isRouteFail = false;

                    } else {
                        mainAct.userLocBtnImgView.performClick();
                    }

                    isSkip = true;
//                        if (getActivity() != null) {
//                            estimateFare(null, null);
//                        }
                }
            } else {
                distance = directionlist.get("distance");
                time = directionlist.get("duration");
                sourceLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, directionlist.get("s_latitude")), GeneralFunctions.parseDoubleValue(0.0, directionlist.get("s_longitude"))
                );

                destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, directionlist.get("d_latitude")), GeneralFunctions.parseDoubleValue(0.0, directionlist.get("d_longitude"))
                );

                if (getActivity() != null) {
                    addDestination(latitude, longitirude, address, distance, time);
                }


            }

            isFindrouteCalled = false;
        } else {

            if (responseString != null && !responseString.equalsIgnoreCase("") && directionlist.get("distance") == null) {
                if (directionlist.get("status").equalsIgnoreCase("OK")) {


                    PolylineOptions lineOptions;
                    if (driverData.get("eFly").equalsIgnoreCase("Yes")) {
                        lineOptions = createCurveRoute(sourceLatlng, destLatLng);
                    } else {
//                        lineOptions = generalFunc.getGoogleRouteOptions(directionlist.toString(), Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black));
                        lineOptions = generalFunc.getGoogleRouteOptions(responseString, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black));
                    }

                    if (lineOptions != null) {
                        if (route_polyLine != null) {
                            route_polyLine.remove();
                        }
                        route_polyLine = gMap.addPolyline(lineOptions);
                    }


                    JSONArray obj_routes = generalFunc.getJsonArray("routes",responseString);
                    if (obj_routes != null && obj_routes.length() > 0) {
                        String duration_str =
                                generalFunc.getJsonValue("text", generalFunc.getJsonValue("duration",
                                        generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0).toString()));


                        int duration = (int) Math.round((generalFunc.parseDoubleValue(0.0,
                                generalFunc.getJsonValue("value", generalFunc.getJsonValue("duration",
                                        generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0).toString())))) / 60);
                        if (duration < 1) {
                            duration = 1;
                        }

                        Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(getActContext(), R.drawable.driver_time_marker,
                                getTimeTxt(duration), true, R.string.defaultFont);
                        setMarkerBasedOnTripStatus(marker_time_ic);

                    }
                }
            } else {

                PolylineOptions lineOptions = new PolylineOptions();


                try {
                    JSONArray obj_routes1 = generalFunc.getJsonArray(responseString);

                    ArrayList<LatLng> points = new ArrayList<LatLng>();

                    if (obj_routes1.length() > 0) {
                        // Fetching i-th route
                        // Fetching all the points in i-th route
                        for (int j = 0; j < obj_routes1.length(); j++) {

                            JSONObject point = generalFunc.getJsonObject(obj_routes1, j);

                            LatLng position = new LatLng(GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("latitude", point).toString()), GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("longitude", point).toString()));


                            points.add(position);

                        }


                        lineOptions.addAll(points);

                        if (driverData.get("eFly").equalsIgnoreCase("Yes")) {
                            lineOptions = createCurveRoute(sourceLatlng, destLatLng);

                        } else {
                            lineOptions = generalFunc.getGoogleRouteOptions(directionlist.toString(), Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black));
                        }


                        lineOptions.width(Utils.dipToPixels(getActContext(), 5));
                        lineOptions.color(getActContext().getResources().getColor(R.color.black));


                    } else {

                    }
                } catch (Exception e) {

                }

                if (lineOptions != null) {
                    if (route_polyLine != null) {
                        route_polyLine.remove();
                    }
                    route_polyLine = gMap.addPolyline(lineOptions);
                }


                int duration = (int) Math.round((generalFunc.parseDoubleValue(0.0,
                        directionlist.get("duration")) / 60));
                if (duration < 1) {
                    duration = 1;
                }


                Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(getActContext(), R.drawable.driver_time_marker,
                        getTimeTxt(duration), true, R.string.defaultFont);
                setMarkerBasedOnTripStatus(marker_time_ic);


            }
        }


    }

    @Override
    public void geoCodeAddressFound(String address, double latitude, double longitude, String geocodeobject) {

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == menuImgView.getId()) {
                mainAct.addDrawer.checkDrawerState(true);

            } else if (view.getId() == backImgView.getId()) {

                if (isBackVisible == true) {
                    mainAct.onBackPressed();
                } else {
                    mainAct.checkDrawerState();
                }

            } else if (view.getId() == R.id.destarea) {
                Bundle bn = new Bundle();
                bn.putString("locationArea", "dest");
                if (mainAct.getPickUpLocation() != null) {
                    bn.putDouble("PickUpLatitude", mainAct.getPickUpLocation().getLatitude());
                    bn.putDouble("PickUpLongitude", mainAct.getPickUpLocation().getLongitude());
                    if (destLocation != null) {
                        bn.putDouble("lat", destLocation.latitude);
                        bn.putDouble("long", destLocation.longitude);
                        bn.putString("address", mainAct.destAddress);
                    }
                }
                bn.putBoolean("isDriverAssigned", mainAct.isDriverAssigned);

                new StartActProcess(mainAct.getActContext()).startActForResult(driverAssignedHFrag, SearchLocationActivity.class,
                        Utils.SEARCH_DEST_LOC_REQ_CODE, bn);


            } else if (view.getId() == R.id.sourceLocSelectTxt) {

                removeSurceMarker();


                if (isTripStart) {
                    sourceMarker = gMap.addMarker(new MarkerOptions().position(pickUpLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_source_marker)));

                }

                if (driverData.get("DriverTripStatus").equalsIgnoreCase("Arrived")) {
                    removeSurceMarker();

                    sourceMarker = gMap.addMarker(new MarkerOptions().position(pickUpLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_source_marker)));


                }


                if (!eConfirmByUser.equalsIgnoreCase("Yes")) {
                    if (mainAct.getDestinationStatus() == true) {
                        destLocSelectTxt.setText(mainAct.getDestAddress());
                        imgAddDestbtn.setVisibility(View.GONE);
                        if (generalFunc.getJsonValue("IS_DEST_ANYTIME_CHANGE", userProfileJson).equalsIgnoreCase("yes")
                                && eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                            imgEditDestbtn.setVisibility(View.VISIBLE);
                        }

                        String eFlatTrip = driverData.get("eFlatTrip");
                        String ePoolRide = driverData.get("ePoolRide");
                        String eFly = driverData.get("eFly");
                        String eDestinationMode = driverData.get("eDestinationMode");

                        if (eFlatTrip != null && eFlatTrip.equalsIgnoreCase("Yes") || isFlatTrip || ePoolRide.equalsIgnoreCase("Yes") || Utils.checkText(driverData.get("iStopId"))) {
                            if (eFlatTrip != null && eFlatTrip.equalsIgnoreCase("Yes") || (eDestinationMode != null && eDestinationMode.equalsIgnoreCase("Yes")) || isFlatTrip || eFly.equalsIgnoreCase("Yes") || ePoolRide.equalsIgnoreCase("Yes") || Utils.checkText(driverData.get("iStopId"))) {
                                imgEditDestbtn.setVisibility(View.GONE);
                                destarea.setOnClickListener(null);
                            }
                        }
                    }
                }
                    if (!generalFunc.isLocationEnabled()) {
                        String TripDetails = generalFunc.getJsonValue("TripDetails", userProfileJson);
                        Location tempickuploc = new Location("temppickkup");

                        double startLat = generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("tStartLat", TripDetails));
                        double startLong = generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("tStartLong", TripDetails));

                        if (startLat != 0.0 && startLong != 0.0) {
                            tempickuploc.setLatitude(startLat);
                            tempickuploc.setLongitude(startLong);


                            mainAct.animateToLocation(tempickuploc.getLatitude(), tempickuploc.getLongitude(), gMap.getMaxZoomLevel() - 5);


                        }
                    } else {

                        mainAct.animateToLocation(pickUpLocation.latitude, pickUpLocation.longitude, gMap.getMaxZoomLevel() - 5);
                    }

                    if (destinationPointMarker_temp != null) {
                        destinationPointMarker_temp.remove();
                        destinationPointMarker_temp = null;
                    }
                    mainAct.pinImgView.setVisibility(View.GONE);


                } else if (view.getId() == R.id.destLocSelectTxt) {
                    if (mainAct.getDestinationStatus() == false) {

                        if (sourceMarker != null) {
                            sourceMarker.remove();
                        }


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                destarea.performClick();
                            }
                        }, 250);

                    } else {
                        mainAct.pinImgView.setVisibility(View.GONE);
                        if (isTripStart == false) {

                            if (destinationPointMarker_temp != null) {
                                destinationPointMarker_temp.remove();
                            }
                            destinationPointMarker_temp = gMap.addMarker(
                                    new MarkerOptions().position(new LatLng(generalFunc.parseDoubleValue(0.0, mainAct.getDestLocLatitude()),
                                            generalFunc.parseDoubleValue(0.0, mainAct.getDestLocLongitude())))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dest_marker)));


                        }
                        removeSurceMarker();

                        mainAct.animateToLocation(generalFunc.parseDoubleValue(0.0, mainAct.getDestLocLatitude()),
                                generalFunc.parseDoubleValue(0.0, mainAct.getDestLocLongitude()), gMap.getMaxZoomLevel() - 5);
                    }

                } else if (view == menuImgView) {
                    mainAct.addDrawer.checkDrawerState(true);
                }

        }

        public void removeSurceMarker() {
            if (sourceMarker != null) {
                sourceMarker.remove();
                sourceMarker = null;
            }
        }
    }
}
