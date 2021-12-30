package com.melevicarbrasil.usuario.deliverAll;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.files.deliverAll.TrackOrderAdapter;
import com.melevicarbrasil.usuario.BaseActivity;
import com.melevicarbrasil.usuario.Help_MainCategory;
import com.melevicarbrasil.usuario.R;
import com.fragments.ScrollSupportMapFragment;
import com.general.files.ConfigPubNub;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.general.files.UpdateDirections;
import com.general.files.UpdateFrequentTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.SphericalUtil;
import com.utils.AnimateMarker;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.GenerateAlertBox;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import com.google.android.gms.maps.model.MapStyleOptions;
public class TrackOrderActivity extends BaseActivity implements OnMapReadyCallback {

    RecyclerView dataRecyclerView;
    TrackOrderAdapter adapter;
    GoogleMap gMap;
    ScrollSupportMapFragment map;
    ArrayList<HashMap<String, String>> listData = new ArrayList<>();
    private ImageView backImgView;
    GeneralFunctions generalFunctions;
    MTextView titleTxt, ordertitleTxt, orderMsg;
    UpdateFrequentTask updateDriverLocTask;
    boolean isTaskKilled = false;
    public String iDriverId = "";
    Marker driverMarker;
    View marker_view = null;
    LatLng driverLocation;
    AnimateMarker animDriverMarker;
    UpdateDirections updateDirections;
    Polyline polyline = null;

    MTextView pickedUpTimeTxtView;
    MTextView pickedUpTimeAbbrTxtView;
    MTextView pickedUpTxtView;
    MTextView distanceVTxtView;
    MTextView distanceVAbbrTxtView;
    MTextView distanceTxtView;

    public LatLng userLatLng = null;
    public LatLng restLatLng = null;

    String eDisplayDottedLine = "";
    String eDisplayRouteLine = "";
    String CompanyAddress = "";
    String DeliveryAddress = "";
    Marker destMarker;

    String etaVal = "";
    String distanceValue = "";
    String distanceUnit = "";

    LinearLayout finaldelArea, btn_cancelArea, vieDetailsArea;
    MTextView delTitleTxtView, delMsgTxtViewm, btn_help, btn_confirm, btn_help_txt, btn_confirm_txt, btn_cancel;
    LinearLayout btnConfirmarea;
    public static String serviceId = "";

    public String iOrderId = "";
    String userprofileJson = "";
    CardView timeArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        if (getIntent().getStringExtra("iOrderId") != null) {
            iOrderId = getIntent().getStringExtra("iOrderId");
        }

        generalFunctions = MyApp.getInstance().getGeneralFun(getActContext());
        userprofileJson = generalFunctions.retrieveValue(Utils.USER_PROFILE_JSON);

        animDriverMarker = new AnimateMarker();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btnConfirmarea = (LinearLayout) findViewById(R.id.btnConfirmarea);
        btnConfirmarea.setOnClickListener(new setOnClickList());

       // new CreateRoundedView(getActContext().getResources().getColor(R.color.appThemeColor_1), 5, 0, 0, btnConfirmarea);


        setSupportActionBar(mToolbar);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        ordertitleTxt = (MTextView) findViewById(R.id.ordertitleTxt);
        orderMsg = (MTextView) findViewById(R.id.orderMsg);
        titleTxt.setVisibility(View.GONE);
        ordertitleTxt.setVisibility(View.VISIBLE);
        orderMsg.setVisibility(View.VISIBLE);
        dataRecyclerView = (RecyclerView) findViewById(R.id.dataRecyclerView);
        delTitleTxtView = (MTextView) findViewById(R.id.delTitleTxtView);
        finaldelArea = (LinearLayout) findViewById(R.id.finaldelArea);
        btn_cancelArea = (LinearLayout) findViewById(R.id.btn_cancelArea);
        vieDetailsArea = (LinearLayout) findViewById(R.id.vieDetailsArea);
        vieDetailsArea.setOnClickListener(new setOnClickList());
        delMsgTxtViewm = (MTextView) findViewById(R.id.delMsgTxtView);
        btn_help = (MTextView) findViewById(R.id.btn_help);
        btn_cancel = (MTextView) findViewById(R.id.btn_cancel);
        btn_help_txt = (MTextView) findViewById(R.id.btn_help_txt);
        btn_confirm = (MTextView) findViewById(R.id.btn_confirm);
        btn_confirm_txt = (MTextView) findViewById(R.id.btn_confirm_txt);
        btn_confirm.setOnClickListener(new setOnClickList());
        btn_help.setOnClickListener(new setOnClickList());
        btn_cancel.setOnClickListener(new setOnClickList());
        map = (ScrollSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapV2);

        timeArea = (CardView) findViewById(R.id.timeArea);

        new CreateRoundedView(getActContext().getResources().getColor(R.color.appThemeColor_1), 5, 0, 0, btn_cancel);

        pickedUpTimeTxtView = (MTextView) findViewById(R.id.pickedUpTimeTxtView);
        pickedUpTimeAbbrTxtView = (MTextView) findViewById(R.id.pickedUpTimeAbbrTxtView);
        pickedUpTxtView = (MTextView) findViewById(R.id.pickedUpTxtView);
        distanceVTxtView = (MTextView) findViewById(R.id.distanceVTxtView);
        distanceVAbbrTxtView = (MTextView) findViewById(R.id.distanceVAbbrTxtView);
        distanceTxtView = (MTextView) findViewById(R.id.distanceTxtView);

        map.getMapAsync(this);

        map.setListener(() -> ((NestedScrollView) findViewById(R.id.mScrollView)).requestDisallowInterceptTouchEvent(true));

        delTitleTxtView.setText(generalFunctions.retrieveLangLBl("", "LBL_ORDER_DELIVERED"));
        delMsgTxtViewm.setText(generalFunctions.retrieveLangLBl("", "LBL_ORDER_DELIVER_MSG"));
        btn_confirm.setText(generalFunctions.retrieveLangLBl("", "LBL_OK_GOT_IT"));
        btn_cancel.setText(generalFunctions.retrieveLangLBl("", "LBL_OK_GOT_IT"));
        btn_help.setText(generalFunctions.retrieveLangLBl("", "LBL_NOT_DELIVERD"));
        btn_confirm_txt.setText(generalFunctions.retrieveLangLBl("", "LBL_DELIVERD_NOTE"));
        btn_help_txt.setText(generalFunctions.retrieveLangLBl("", "LBL_NOTDELIVERD_NOTE"));
        pickedUpTxtView.setText(generalFunctions.retrieveLangLBl("", "LBL_PICKED_UP"));
        distanceTxtView.setText(generalFunctions.retrieveLangLBl("", "LBL_ETA_TXT"));

        distanceVTxtView.setText("--");
        distanceVAbbrTxtView.setText("");

        pickedUpTimeTxtView.setText("--");
        pickedUpTimeAbbrTxtView.setText("");
        if (getIntent().getStringExtra("iOrderId") != null) {
            iOrderId = getIntent().getStringExtra("iOrderId");
        }
    }


    public void setEta(String val, String distanceValue, String distanceUnit) {
        if (eDisplayRouteLine.equalsIgnoreCase("yes")) {
            runOnUiThread(() -> {
                if (restLatLng != null && userLatLng != null) {
                    etaVal = val;
                    this.distanceValue = distanceValue;
                    this.distanceUnit = distanceUnit;

                    distanceVTxtView.setText(generalFunctions.convertNumberWithRTL(etaVal));

                    generateMapLocations(restLatLng.latitude, restLatLng.longitude, userLatLng.latitude, userLatLng.longitude);
                    if (driverLocation != null) {
                        updateDriverLocation(driverLocation);
                    }
                }
            });
        }

    }

    public void setTaskKilledValue(boolean isTaskKilled) {
        this.isTaskKilled = isTaskKilled;

        if (isTaskKilled == true) {
            onPauseCalled();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setTaskKilledValue(true);

        unSubscribeToDriverLocChannel();

    }


    public void onPauseCalled() {

        if (updateDriverLocTask != null) {
            updateDriverLocTask.stopRepeatingTask();
        }
        updateDriverLocTask = null;

        unSubscribeToDriverLocChannel();
    }


    public void subscribeToDriverLocChannel() {

        if (!iDriverId.equalsIgnoreCase("")) {

            ArrayList<String> channelName = new ArrayList<>();
            channelName.add(Utils.pubNub_Update_Loc_Channel_Prefix + iDriverId);
            ConfigPubNub.getInstance().subscribeToChannels(channelName);
        }


    }


    public void pubnubMessage(JSONObject obj_msg) {

        String messageStr = generalFunctions.getJsonValueStr("Message", obj_msg);
        String iOrderId_ = generalFunctions.getJsonValueStr("iOrderId", obj_msg);
        if (!messageStr.equals("")) {
            String vTitle = generalFunctions.getJsonValueStr("vTitle", obj_msg);
            Logger.d("messageStr", "::" + messageStr);
            if (messageStr.equalsIgnoreCase("OrderConfirmByRestaurant") || messageStr.equalsIgnoreCase("OrderPickedup")) {

                if (messageStr.equalsIgnoreCase("OrderPickedup")) {
                    timeArea.setVisibility(View.VISIBLE);
                }
                handleDailog(false, vTitle);

            } else if (messageStr.equalsIgnoreCase("OrderDeclineByRestaurant") || (messageStr.equalsIgnoreCase("OrderCancelByAdmin")) && iOrderId_.equalsIgnoreCase(iOrderId)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finaldelArea.setVisibility(View.VISIBLE);
                        btn_help_txt.setVisibility(View.GONE);
                        btn_confirm_txt.setVisibility(View.GONE);
                        btnConfirmarea.setVisibility(View.GONE);
                        btn_cancelArea.setVisibility(View.VISIBLE);
                        vieDetailsArea.setVisibility(View.GONE);
                        findViewById(R.id.btnMainConfirmarea).setVisibility(View.GONE);
                        delTitleTxtView.setText(generalFunctions.retrieveLangLBl("", "LBL_ORDER_CANCELLED"));
                        delTitleTxtView.setTextColor(getActContext().getResources().getColor(R.color.red));
                        delMsgTxtViewm.setText(vTitle);
                    }
                });


            } else if (messageStr.equalsIgnoreCase("OrderDelivered") && iOrderId_.equalsIgnoreCase(iOrderId)) {
                try {

                    getTrackDetails();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finaldelArea.setVisibility(View.VISIBLE);
                        }
                    });


                } catch (Exception e) {
                    Logger.d("OrderDeliveredException", "::" + e.toString());

                }

            } else if (messageStr.equalsIgnoreCase("CabRequestAccepted")) {
                handleDailog(false, vTitle);
            }


        } else if (generalFunctions.getJsonValueStr("MsgType", obj_msg) != null && !generalFunctions.getJsonValueStr("MsgType", obj_msg).equalsIgnoreCase("")) {

            pubNubMsgArrived(obj_msg.toString(), true);

        }

    }

    public void handleDailog(boolean isfinish, String vTitle) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                generateAlert.setCancelable(false);
                generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                    @Override
                    public void handleBtnClick(int btn_id) {
                        if (isfinish) {
                            finish();
                        } else {
                            getTrackDetails();
                        }
                    }
                });
                generateAlert.setContentMessage("", vTitle);
                generateAlert.setPositiveBtn(generalFunctions.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                generateAlert.showAlertBox();


            }
        });

    }

    public void unSubscribeToDriverLocChannel() {

        if (!iDriverId.equalsIgnoreCase("")) {
            ArrayList<String> channelName = new ArrayList<>();
            channelName.add(Utils.pubNub_Update_Loc_Channel_Prefix + iDriverId);
            ConfigPubNub.getInstance().unSubscribeToChannels(channelName);
        }

    }


    public void pubNubMsgArrived(final String message, final Boolean ishow) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String msgType = generalFunctions.getJsonValue("MsgType", message);
                String DriverId = generalFunctions.getJsonValue("iDriverId", message);

                if (msgType.equals("LocationUpdateOnTrip") && DriverId.equalsIgnoreCase(iDriverId)) {
                    String vLatitude = generalFunctions.getJsonValue("vLatitude", message);
                    String vLongitude = generalFunctions.getJsonValue("vLongitude", message);

                    Location driverLoc = new Location("Driverloc");
                    driverLoc.setLatitude(generalFunctions.parseDoubleValue(0.0, vLatitude));
                    driverLoc.setLongitude(generalFunctions.parseDoubleValue(0.0, vLongitude));


                    if (updateDirections != null) {
                        updateDirections.changeUserLocation(driverLoc);
                    }

                    if (eDisplayRouteLine.equalsIgnoreCase("Yes")) {
                        callUpdateDeirection(driverLoc);
                    }

                    LatLng driverLocation_update = new LatLng(generalFunctions.parseDoubleValue(0.0, vLatitude),
                            generalFunctions.parseDoubleValue(0.0, vLongitude));

                    driverLocation = driverLocation_update;
                    updateDriverLocation(driverLocation_update);

                }
            }
        });
    }


    public void callUpdateDeirection(Location driverlocation) {
        if (userLatLng == null) {
            return;

        }
        if (updateDirections == null) {

            Location location = new Location("userLocation");
            location.setLatitude(userLatLng.latitude);
            location.setLongitude(userLatLng.longitude);
            updateDirections = new UpdateDirections(getActContext(), gMap, driverlocation, location);
            updateDirections.scheduleDirectionUpdate();
        }

    }


    public void updateDriverLocation(LatLng latlog) {
        if (latlog == null) {
            return;
        }

        if (driverMarker == null) {
            try {
                if (gMap != null) {
                    MarkerOptions markerOptions = new MarkerOptions();

                    int iconId = R.mipmap.car_driver_6;

                    markerOptions.position(latlog).title("DriverId" + iDriverId).icon(BitmapDescriptorFactory.fromResource(iconId))
                            .anchor(0.5f, 0.5f).flat(true);

                    driverMarker = gMap.addMarker(markerOptions);

                    driverLocation = latlog;
                    CameraPosition cameraPosition = cameraForDriverPosition();
                    gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }
            } catch (Exception e) {
                Logger.d("markerException", e.toString());
            }
        } else {

            if (driverMarker != null) {
                driverMarker.remove();
            }
            if (gMap != null) {
                MarkerOptions markerOptions = new MarkerOptions();

                int iconId = R.mipmap.car_driver_6;

                markerOptions.position(latlog).title("DriverId" + iDriverId).icon(BitmapDescriptorFactory.fromResource(iconId))
                        .anchor(0.5f, 0.5f).flat(true);

                driverMarker = gMap.addMarker(markerOptions);

                driverLocation = latlog;

            }


            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(driverLocation);
            if (eDisplayDottedLine.equalsIgnoreCase("No") && eDisplayRouteLine.equalsIgnoreCase("No")) {
                //driver  to resturant bounding
                builder.include(restLatLng);

            } else if (eDisplayRouteLine.equalsIgnoreCase("Yes")) {
                //driver to user bounding
                builder.include(userLatLng);

            }

            LatLngBounds bounds = builder.build();

            if (map != null) {
                int width = map.getView().getMeasuredWidth();
                int height = map.getView().getMeasuredHeight();
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, Utils.dpToPx(getActContext(), 150), padding);

                gMap.animateCamera(cu);
            }

        }


    }

    public CameraPosition cameraForDriverPosition() {

        double currentZoomLevel = gMap == null ? Utils.defaultZomLevel : gMap.getCameraPosition().zoom;

        if (Utils.defaultZomLevel > currentZoomLevel) {
            currentZoomLevel = Utils.defaultZomLevel;
        }
        if (driverLocation != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(this.driverLocation.latitude, this.driverLocation.longitude))
                    .zoom((float) currentZoomLevel).build();


            return cameraPosition;
        } else {
            return null;
        }
    }

    public Context getActContext() {
        return TrackOrderActivity.this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Maps dias e noches inicio by gabriel
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if(hourOfDay >= 06 && hourOfDay < 18){
//googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.ub__map_style_pedro));
        } else {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.ub__map_style_noite_pedro));
        }
// Maps dias e noches fim by gabriel
        this.gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.setPadding(0, 0, 0, 70);
    }

    Marker sourceMarker;
    Polyline linePoly;

    public void generateMapLocations(double resLat, double resLong, double userLat, double userLong) {
        LatLng sourceLnt = new LatLng(resLat, resLong);
        restLatLng = sourceLnt;

        if (sourceMarker != null) {
            sourceMarker.remove();
        }
        if (destMarker != null) {
            destMarker.remove();
        }
        if (!iDriverId.equalsIgnoreCase("")) {
            if (linePoly != null) {
                linePoly.remove();
            }

        }

        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View restaurantMarkerView = inflater.inflate(R.layout.marker_view, null);


        ImageView restaurantPinImgView = (ImageView) restaurantMarkerView.findViewById(R.id.pinImgView);
        restaurantPinImgView.setImageResource(R.mipmap.ic_track_restaurant);

        MTextView restaurantMarkerTxtView = (MTextView) restaurantMarkerView.findViewById(R.id.addressTxtView);
        restaurantMarkerTxtView.setText(CompanyAddress);

        View userMarkerView = inflater.inflate(R.layout.marker_view, null);
        ImageView userPinImgView = (ImageView) userMarkerView.findViewById(R.id.pinImgView);
        userPinImgView.setImageResource(R.mipmap.ic_track_user);


        MTextView userMarkerTxtView = (MTextView) userMarkerView.findViewById(R.id.addressTxtView);
        userMarkerTxtView.setText(DeliveryAddress);
        userMarkerTxtView.setTextColor(getResources().getColor(R.color.white));
        userMarkerTxtView.setBackgroundColor(getResources().getColor(R.color.black));

        sourceMarker = gMap.addMarker(new MarkerOptions().position(sourceLnt).icon(BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromView(restaurantMarkerView))));


        LatLng destLnt = new LatLng(userLat, userLong);
        userLatLng = destLnt;
        destMarker = gMap.addMarker(new MarkerOptions().position(destLnt).icon(BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromView(userMarkerView))));


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(sourceMarker.getPosition());
        builder.include(destMarker.getPosition());

        LatLngBounds bounds = builder.build();


        if (map != null && map.getView() != null) {
            int width = map.getView().getMeasuredWidth();
            int height = map.getView().getMeasuredHeight();
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, Utils.dpToPx(getActContext(), 150), padding);
            gMap.animateCamera(cu);

        }

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions
                .jointType(JointType.ROUND)
                .pattern(Arrays.asList(new Gap(20), new Dash(20)))
                .add(sourceMarker.getPosition())
                .add(destMarker.getPosition());
        polylineOptions.width(Utils.dipToPixels(getActContext(), 4));

        if (iDriverId.equalsIgnoreCase("")) {
            linePoly = gMap.addPolyline(polylineOptions);
            linePoly.setColor(Color.parseColor("#cecece"));
            linePoly.setStartCap(new RoundCap());
            linePoly.setEndCap(new RoundCap());
        }


        buildArcLine(sourceLnt, destLnt, 0.050);


    }

    private void buildArcLine(LatLng p1, LatLng p2, double arcCurvature) {
        //Calculate distance and heading between two points
        double d = SphericalUtil.computeDistanceBetween(p1, p2);
        double h = SphericalUtil.computeHeading(p1, p2);

        if (h < 0) {
            LatLng tmpP1 = p1;
            p1 = p2;
            p2 = tmpP1;

            d = SphericalUtil.computeDistanceBetween(p1, p2);
            h = SphericalUtil.computeHeading(p1, p2);
        }

        //Midpoint position
        LatLng midPointLnt = SphericalUtil.computeOffset(p1, d * 0.5, h);

        //Apply some mathematics to calculate position of the circle center
        double x = (1 - arcCurvature * arcCurvature) * d * 0.5 / (2 * arcCurvature);
        double r = (1 + arcCurvature * arcCurvature) * d * 0.5 / (2 * arcCurvature);

        LatLng centerLnt = SphericalUtil.computeOffset(midPointLnt, x, h + 90.0);

        //Polyline options
        PolylineOptions options = new PolylineOptions();
        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));

        //Calculate heading between circle center and two points
        double h1 = SphericalUtil.computeHeading(centerLnt, p1);
        double h2 = SphericalUtil.computeHeading(centerLnt, p2);

        //Calculate positions of points on circle border and add them to polyline options
        int numPoints = 100;
        double step = (h2 - h1) / numPoints;

        for (int i = 0; i < numPoints; i++) {
            LatLng middlePointTemp = SphericalUtil.computeOffset(centerLnt, r, h1 + i * step);
            options.add(middlePointTemp);
        }


        if (!eDisplayDottedLine.equalsIgnoreCase("") && eDisplayDottedLine.equalsIgnoreCase("Yes")) {
            //Draw polyline
            if (polyline != null) {
                polyline.remove();
                polyline = null;


            }
            polyline = gMap.addPolyline(options.width(10).color(Color.BLACK).geodesic(false).pattern(pattern));
        } else {
            if (polyline != null) {
                polyline.remove();
                polyline = null;


            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrackDetails();
    }

    public void getTrackDetails() {
        try {
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("type", "getOrderDeliveryLog");
            parameters.put("iOrderId", iOrderId);
            parameters.put("iUserId", generalFunctions.getMemberId());
            parameters.put("UserType", Utils.userType);
            parameters.put("eSystem", Utils.eSystem_Type);

            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);

            exeWebServer.setLoaderConfig(getActContext(), true, generalFunctions);
            exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
                @Override
                public void setResponse(String responseString) {

                    if (responseString != null && !responseString.equals("")) {
                        String action = generalFunctions.getJsonValue(Utils.action_str, responseString);
                        if (action.equals("1")) {
                            listData.clear();

                            if (generalFunctions.getJsonValue("iServiceId", responseString) != null && !generalFunctions.getJsonValue("iServiceId", responseString).equalsIgnoreCase("")) {
                                serviceId = generalFunctions.getJsonValue("iServiceId", responseString);
                            }

                            eDisplayDottedLine = generalFunctions.getJsonValue("eDisplayDottedLine", responseString);
                            eDisplayRouteLine = generalFunctions.getJsonValue("eDisplayRouteLine", responseString);

                            CompanyAddress = generalFunctions.getJsonValue("CompanyAddress", responseString);
                            DeliveryAddress = generalFunctions.getJsonValue("DeliveryAddress", responseString);
                            String DriverPhone = generalFunctions.getJsonValue("DriverPhone", responseString);
                            String OrderPickedUpDate = generalFunctions.getJsonValue("OrderPickedUpDate", responseString);


                            if (OrderPickedUpDate.trim().equalsIgnoreCase("")) {
                                pickedUpTimeTxtView.setText("--");
                                pickedUpTimeAbbrTxtView.setText("");
                            } else {
                                String pickUpDate = generalFunctions.getDateFormatedType(generalFunctions.getJsonValue("OrderPickedUpDate", responseString), Utils.OriginalDateFormate, "hh:mm aa");
                                String[] pickUpDateArr = pickUpDate.split(" ");

                                pickedUpTimeTxtView.setText(generalFunctions.convertNumberWithRTL(pickUpDateArr[0]));
                                pickedUpTimeAbbrTxtView.setText(pickUpDateArr[1]);
                            }


                            if (generalFunctions.getJsonValue("iDriverId", responseString) != null && !generalFunctions.getJsonValue("iDriverId", responseString).equalsIgnoreCase("")) {
                                iDriverId = generalFunctions.getJsonValue("iDriverId", responseString);


                            }
                            generateMapLocations(GeneralFunctions.parseDoubleValue(0, generalFunctions.getJsonValue("CompanyLat", responseString)), GeneralFunctions.parseDoubleValue(0, generalFunctions.getJsonValue("CompanyLong", responseString)), GeneralFunctions.parseDoubleValue(0, generalFunctions.getJsonValue("PassengerLat", responseString)), GeneralFunctions.parseDoubleValue(0, generalFunctions.getJsonValue("PassengerLong", responseString)));

                            ordertitleTxt.setText("#" + generalFunctions.convertNumberWithRTL(generalFunctions.getJsonValue("vOrderNo", responseString)));

                            String tOrderRequestDate=generalFunctions.getJsonValue("tOrderRequestDate", responseString);

                            String formattedDate=generalFunctions.getDateFormatedType(tOrderRequestDate, Utils.OriginalDateFormate, CommonUtilities.OriginalDateFormate)+", "+generalFunctions.getDateFormatedType(tOrderRequestDate, Utils.OriginalDateFormate, CommonUtilities.OriginalTimeFormate);

                            orderMsg.setText(generalFunctions.convertNumberWithRTL(formattedDate) + " | " +
                                    generalFunctions.convertNumberWithRTL(generalFunctions.getJsonValue("TotalOrderItems", responseString)) + " | " + generalFunctions.convertNumberWithRTL(generalFunctions.getJsonValue("fNetTotal", responseString)));


                            JSONArray messageArr = null;
                            messageArr = generalFunctions.getJsonArray(Utils.message_str, responseString);

                            if (generalFunctions.getJsonValue("iDriverId", responseString) != null && !generalFunctions.getJsonValue("iDriverId", responseString).equalsIgnoreCase("")
                                    && !generalFunctions.getJsonValue("iDriverId", responseString).equalsIgnoreCase("0")) {
                                iDriverId = generalFunctions.getJsonValue("iDriverId", responseString);

                                //  unSubscribeToDriverLocChannel();

                                LatLng driverLocation_update = new LatLng(generalFunctions.parseDoubleValue(0.0, generalFunctions.getJsonValue("DriverLat", responseString)),
                                        generalFunctions.parseDoubleValue(0.0, generalFunctions.getJsonValue("DriverLong", responseString)));

                                driverLocation = driverLocation_update;
                                //timeArea.setVisibility(View.GONE);
                                if (eDisplayRouteLine.equalsIgnoreCase("Yes")) {
                                    Location driverLoc = new Location("Driverloc");
                                    driverLoc.setLatitude(driverLocation_update.latitude);
                                    driverLoc.setLongitude(driverLocation_update.longitude);
                                    callUpdateDeirection(driverLoc);
                                    timeArea.setVisibility(View.VISIBLE);
                                }

                                updateDriverLocation(driverLocation);
                                subscribeToDriverLocChannel();

                            }

                            String RIDE_DRIVER_CALLING_METHOD = generalFunctions.getJsonValue("RIDE_DRIVER_CALLING_METHOD", userprofileJson);
                            String CALLMASKING_ENABLED = generalFunctions.getJsonValue("CALLMASKING_ENABLED", userprofileJson);
                            String vName = generalFunctions.getJsonValue("vName", userprofileJson);
                            String vImgName = generalFunctions.getJsonValue("vImgName", userprofileJson);
                            String LBL_INCOMING_CALL = generalFunctions.retrieveLangLBl("", "LBL_INCOMING_CALL");

                            for (int i = 0; i < messageArr.length(); i++) {
                                JSONObject rowObject = generalFunctions.getJsonObject(messageArr, i);
                                HashMap<String, String> mapData = new HashMap<>();
                                mapData.put("vStatus", generalFunctions.getJsonValueStr("vStatus_Track", rowObject));
                                mapData.put("vStatusTitle", generalFunctions.getJsonValueStr("vStatus", rowObject));
                                mapData.put("eShowCallImg", generalFunctions.getJsonValueStr("eShowCallImg", rowObject));
                                mapData.put("iStatusCode", generalFunctions.getJsonValueStr("iStatusCode", rowObject));
                                mapData.put("OrderCurrentStatusCode", generalFunctions.getJsonValueStr("OrderCurrentStatusCode", rowObject));
                                mapData.put("eCompleted", generalFunctions.getJsonValueStr("eCompleted", rowObject));
                                mapData.put("driverImage", generalFunctions.getJsonValueStr("driverImage", rowObject));
                                mapData.put("driverName", generalFunctions.getJsonValueStr("driverName", rowObject));
                                mapData.put("iDriverId", generalFunctions.getJsonValueStr("iDriverId", rowObject));
                                mapData.put("driverImageUrl", CommonUtilities.PROVIDER_PHOTO_PATH + mapData.get("iDriverId") + "/"
                                        + mapData.get("driverImage"));
                                mapData.put("DriverPhone", DriverPhone);
                                mapData.put("RIDE_DRIVER_CALLING_METHOD", RIDE_DRIVER_CALLING_METHOD);
                                mapData.put("CALLMASKING_ENABLED", CALLMASKING_ENABLED);
                                mapData.put("vName", vName);
                                mapData.put("vImgName", vImgName);
                                mapData.put("LBL_INCOMING_CALL", LBL_INCOMING_CALL);

                                Object iorderId = generalFunctions.getJsonValue("iOrderId", rowObject);
                                if (iorderId != null && Utils.checkText(iorderId.toString())) {
                                    mapData.put("iOrderId", iorderId.toString());
                                    iOrderId = iorderId.toString();
                                }


                                String dDate = generalFunctions.getJsonValue("dDate", rowObject).toString();

                                if (!dDate.trim().equalsIgnoreCase("")) {
                                    String dLogDate = generalFunctions.getDateFormatedType(dDate, Utils.OriginalDateFormate, "hh:mm aa");
                                    String[] dLogDateArr = dLogDate.split(" ");
                                    String date = dLogDateArr[0];
                                    mapData.put("dDate", date);
                                    mapData.put("dDateConverted", generalFunctions.convertNumberWithRTL(date));
                                    mapData.put("dDateAMPM", dLogDateArr[1]);
                                } else {
                                    mapData.put("dDate", "");
                                    mapData.put("dDateConverted", "");
                                    mapData.put("dDateAMPM", "");
                                }

                                listData.add(mapData);
                            }
                            adapter = new TrackOrderAdapter(getActContext(), listData);

                            dataRecyclerView.setAdapter(adapter);


                        } else {
                        }
                    } else {
                        generalFunctions.showError();
                    }
                }
            });
            exeWebServer.execute();
        } catch (Exception e) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.track_order_activity, menu);
        menu.findItem(R.id.menu_help).setTitle(generalFunctions.retrieveLangLBl("", "LBL_HELP"));
        menu.findItem(R.id.menu_order_details).setTitle(generalFunctions.retrieveLangLBl("", "LBL_VIEW_ORDER_DETAILS"));
        Utils.setMenuTextColor(menu.findItem(R.id.menu_order_details), getResources().getColor(R.color.appThemeColor_TXT_1));
        Utils.setMenuTextColor(menu.findItem(R.id.menu_help), getResources().getColor(R.color.appThemeColor_TXT_1));


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bn = new Bundle();
        switch (item.getItemId()) {

            case R.id.menu_order_details:
                bn.putString("iOrderId", iOrderId);
                new StartActProcess(getActContext()).startActWithData(OrderDetailsActivity.class, bn);
                return true;

            case R.id.menu_help:

                bn.putString("iOrderId", iOrderId);
                new StartActProcess(getActContext()).startActWithData(Help_MainCategory.class, bn);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {


        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (getIntent().getBooleanExtra("isRestart", false)) {
            MyApp.getInstance().restartWithGetDataApp();
        } else {
            super.onBackPressed();
        }
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backImgView:
                    onBackPressed();
                    break;

                case R.id.btn_cancel:
                case R.id.btnConfirmarea:
                    finaldelArea.setVisibility(View.GONE);
                    MyApp.getInstance().restartWithGetDataApp();
                    break;
                case R.id.vieDetailsArea:
                    finaldelArea.setVisibility(View.GONE);
                    Bundle bn = new Bundle();
                    bn.putString("iOrderId", iOrderId);
                    new StartActProcess(getActContext()).startActWithData(Help_MainCategory.class, bn);
                    break;
            }
        }
    }
}
