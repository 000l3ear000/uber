package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.deliverAll.MapDelegate;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MapServiceApi;
import com.general.files.MyApp;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.utils.Utils;
import com.view.MTextView;
import com.view.anim.loader.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import com.google.android.gms.maps.model.MapStyleOptions;
public class FareEstimateActivity extends AppCompatActivity implements OnMapReadyCallback, MapDelegate {

    MTextView titleTxt;
    ImageView backImgView;
    GeneralFunctions generalFunc;
    MTextView searchLocTxt;
    MTextView baseFareHTxt;
    MTextView baseFareVTxt;
    MTextView commisionHTxt;
    MTextView commisionVTxt;
    MTextView distanceTxt;
    MTextView distanceFareTxt;
    MTextView minuteTxt;
    MTextView minuteFareTxt;
    MTextView totalFareHTxt;
    MTextView totalFareVTxt;
    AVLoadingIndicatorView loaderView;
    LinearLayout container;
    String userProfileJson;
    String currency_sign = "";
    SupportMapFragment map;
    GoogleMap gMapView;
    ImageView locPinImg;
    private String TAG = FareEstimateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_estimate);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());


        loaderView = (AVLoadingIndicatorView) findViewById(R.id.loaderView);
        container = (LinearLayout) findViewById(R.id.container);

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        searchLocTxt = (MTextView) findViewById(R.id.searchLocTxt);
        baseFareHTxt = (MTextView) findViewById(R.id.baseFareHTxt);
        baseFareVTxt = (MTextView) findViewById(R.id.baseFareVTxt);
        commisionHTxt = (MTextView) findViewById(R.id.commisionHTxt);
        commisionVTxt = (MTextView) findViewById(R.id.commisionVTxt);
        distanceTxt = (MTextView) findViewById(R.id.distanceTxt);
        distanceFareTxt = (MTextView) findViewById(R.id.distanceFareTxt);
        minuteTxt = (MTextView) findViewById(R.id.minuteTxt);
        minuteFareTxt = (MTextView) findViewById(R.id.minuteFareTxt);
        totalFareHTxt = (MTextView) findViewById(R.id.totalFareHTxt);
        totalFareVTxt = (MTextView) findViewById(R.id.totalFareVTxt);
        locPinImg = (ImageView) findViewById(R.id.locPinImg);

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapV2);
        map.getMapAsync(this);

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        currency_sign = generalFunc.getJsonValue("CurrencySymbol", userProfileJson);

        backImgView.setOnClickListener(new setOnClickAct());
        searchLocTxt.setOnClickListener(new setOnClickAct());

        container.setVisibility(View.GONE);
        setLabels();

        locPinImg.setImageResource(R.mipmap.ic_search);

        if (getIntent().getStringExtra("isDestinationAdded").equals("true")) {
            searchLocTxt.setText(getIntent().getStringExtra("DestLocAddress"));
            findRoute(getIntent().getStringExtra("DestLocLatitude"), getIntent().getStringExtra("DestLocLongitude"));
        }
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
        if (googleMap == null) {
            return;
        }
        this.gMapView = googleMap;
        this.gMapView.getUiSettings().setZoomControlsEnabled(true);

    }

    public Context getActContext() {
        return FareEstimateActivity.this;
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FARE_ESTIMATE_TXT"));
        baseFareHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BASE_FARE_SMALL_TXT"));
        totalFareHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MIN_FARE_TXT"));
        searchLocTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SEARCH_PLACE_HINT_TXT"));
    }

    public void findRoute(String destLocLatitude, String destLocLongitude) {

        loaderView.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

        String originLoc = getIntent().getStringExtra("PickUpLatitude") + "," + getIntent().getStringExtra("PickUpLongitude");
        String destLoc = destLocLatitude + "," + destLocLongitude;

        HashMap<String, String> data = new HashMap<>();
        data.put(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY, "");
        data.put(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, "");
        data = generalFunc.retrieveValue(data);


        String serverKey = data.get(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + originLoc + "&destination=" + destLoc + "&sensor=true&key=" + serverKey + "&language=" + data.get(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY);
        HashMap<String, String> hashMap = new HashMap<>();


        hashMap.put("d_latitude", destLocLatitude + "");
        hashMap.put("d_longitude", destLocLongitude + "");


        hashMap.put("s_latitude", getIntent().getStringExtra("PickUpLatitude"));
        hashMap.put("s_longitude", getIntent().getStringExtra("PickUpLongitude"));

        String parameters = "origin=" + originLoc + "&destination=" + destLoc;

        hashMap.put("parameters", parameters);

        MapServiceApi.getDirectionservice(getActContext(), hashMap, this, null,false);


//        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
//
//        exeWebServer.setDataResponseListener(responseString -> {
//
//            loaderView.setVisibility(View.GONE);
//
//            if (responseString != null && !responseString.equals("")) {
//
//                String status = generalFunc.getJsonValue("status", responseString);
//
//                if (status.equals("OK")) {
//
//                    JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
//                    if (obj_routes != null && obj_routes.length() > 0) {
//                        JSONObject obj_legs = generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0);
//
//                        ((MTextView) findViewById(R.id.sourceLocTxt)).setText(generalFunc.getJsonValueStr("start_address", obj_legs));
//                        ((MTextView) findViewById(R.id.destLocTxt)).setText(generalFunc.getJsonValueStr("end_address", obj_legs));
//
//                        String distance = "" + (generalFunc.parseDoubleValue(0, generalFunc.getJsonValue("value",
//                                generalFunc.getJsonValueStr("distance", obj_legs))) / 1000);
//
//                        String time = "" + (generalFunc.parseDoubleValue(0, generalFunc.getJsonValue("value",
//                                generalFunc.getJsonValueStr("duration", obj_legs))) / 60);
//
//                        LatLng sourceLocation = new LatLng(generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValueStr("start_location", obj_legs))),
//                                generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValueStr("start_location", obj_legs))));
//
//                        LatLng destLocation = new LatLng(generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValueStr("end_location", obj_legs))),
//                                generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValueStr("end_location", obj_legs))));
//
//                        estimateFare(distance, time, responseString, sourceLocation, destLocation);
//                    }
//
//                } else {
//                    generalFunc.showGeneralMessage("",
//                            generalFunc.retrieveLangLBl("", "LBL_GOOGLE_DIR_NO_ROUTE"));
//                }
//
//            } else {
//                generalFunc.showError();
//            }
//        });
//        exeWebServer.execute();
    }

    public void estimateFare(final String distance, final String time, final String directionJSON, final LatLng sourceLocation, final LatLng destLocation) {
        loaderView.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "estimateFare");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("distance", distance);
        parameters.put("time", time);
        parameters.put("SelectedCar", getIntent().getStringExtra("SelectedCarId"));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                if (isDataAvail == true) {

                    String total_fare = generalFunc.getJsonValueStr("total_fare", responseObj);
                    String iBaseFare = generalFunc.getJsonValueStr("iBaseFare", responseObj);
                    String fPricePerMin = generalFunc.getJsonValueStr("fPricePerMin", responseObj);
                    String fPricePerKM = generalFunc.getJsonValueStr("fPricePerKM", responseObj);
                    String fCommision = generalFunc.getJsonValueStr("fCommision", responseObj);
                    String MinFareDiff = generalFunc.getJsonValueStr("MinFareDiff", responseObj);
                    String Distance = generalFunc.getJsonValueStr("Distance", responseObj);
                    String Time = generalFunc.getJsonValueStr("Time", responseObj);

                    baseFareVTxt.setText(getIntent().getStringExtra("SelectedCabType")
                            + " " + currency_sign + " " + iBaseFare);
                    distanceTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DISTANCE_TXT") + "(" + Distance + " " + generalFunc.retrieveLangLBl("", "LBL_KM_DISTANCE_TXT") + ")");
                    distanceFareTxt.setText(currency_sign + " " + fPricePerKM);
                    minuteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TIME_TXT") + "(" + Time + " " + generalFunc.retrieveLangLBl("", "LBL_MINUTES_TXT") + ")");
                    minuteFareTxt.setText(currency_sign + " " + fPricePerMin);
                    totalFareVTxt.setText(currency_sign + " " + total_fare);

                    if (!MinFareDiff.equals("") && !MinFareDiff.equals("0")) {

                        (findViewById(R.id.minFareRow)).setVisibility(View.VISIBLE);
                        ((MTextView) findViewById(R.id.minFareHTxt)).setText(currency_sign + "" + total_fare + " "
                                + generalFunc.retrieveLangLBl("", "LBL_MINIMUM"));
                        ((MTextView) findViewById(R.id.minFareVTxt)).setText(currency_sign + " " + total_fare);
                    }

                    locPinImg.setImageResource(R.mipmap.ic_loc_pin_indicator);

                    loaderView.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);

                    if (gMapView != null) {

                        gMapView.clear();
                        PolylineOptions lineOptions = generalFunc.getGoogleRouteOptions(directionJSON, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black));

                        if (lineOptions != null) {
                            gMapView.addPolyline(lineOptions);
                        }

                        MarkerOptions markerOptions_sourceLocation = new MarkerOptions();
                        markerOptions_sourceLocation.position(sourceLocation);
                        markerOptions_sourceLocation.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_source_marker))
                                .anchor(0.5f, 0.5f);

                        MarkerOptions markerOptions_destinationLocation = new MarkerOptions();
                        markerOptions_destinationLocation
                                .position(destLocation);
                        markerOptions_destinationLocation
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dest_marker)).anchor(0.5f, 0.5f);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(sourceLocation);
                        builder.include(destLocation);

                        LatLngBounds bounds = builder.build();

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                                Utils.dipToPixels(getActContext(), 280), Utils.dipToPixels(getActContext(), 280), 50);
                        gMapView.moveCamera(cu);

                        gMapView.addMarker(markerOptions_sourceLocation);
                        gMapView.addMarker(markerOptions_destinationLocation);

                    }


                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseObj)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                searchLocTxt.setText(place.getAddress());
                LatLng placeLocation = place.getLatLng();

                findRoute("" + placeLocation.latitude, "" + placeLocation.longitude);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                generalFunc.showMessage(generalFunc.getCurrentView(FareEstimateActivity.this),
                        status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void searchResult(ArrayList<HashMap<String, String>> placelist, int selectedPos, String input) {

    }

    @Override
    public void resetOrAddDest(int selPos, String address, double latitude, double longitude, String isSkip) {

    }

    @Override
    public void directionResult(HashMap<String, String> directionlist) {
        String responseString = directionlist.get("routes");


        if (responseString != null && !responseString.equalsIgnoreCase("") && directionlist.get("distance") == null) {

            JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
            if (obj_routes != null && obj_routes.length() > 0) {
                JSONObject obj_legs = generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0);

                ((MTextView) findViewById(R.id.sourceLocTxt)).setText(generalFunc.getJsonValueStr("start_address", obj_legs));
                ((MTextView) findViewById(R.id.destLocTxt)).setText(generalFunc.getJsonValueStr("end_address", obj_legs));

                String distance = "" + (generalFunc.parseDoubleValue(0, generalFunc.getJsonValue("value",
                        generalFunc.getJsonValueStr("distance", obj_legs))) / 1000);

                String time = "" + (generalFunc.parseDoubleValue(0, generalFunc.getJsonValue("value",
                        generalFunc.getJsonValueStr("duration", obj_legs))) / 60);

                LatLng sourceLocation = new LatLng(generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValueStr("start_location", obj_legs))),
                        generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValueStr("start_location", obj_legs))));

                LatLng destLocation = new LatLng(generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValueStr("end_location", obj_legs))),
                        generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValueStr("end_location", obj_legs))));

                estimateFare(distance, time, responseString, sourceLocation, destLocation);
            }

        } else {


            ((MTextView) findViewById(R.id.sourceLocTxt)).setText(directionlist.get("SourceAddress"));
            ((MTextView) findViewById(R.id.destLocTxt)).setText(directionlist.get("DestinationAddress"));
            String distance = directionlist.get("distance");
            String time = directionlist.get("duration");
            LatLng sourceLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, directionlist.get("s_latitude")), GeneralFunctions.parseDoubleValue(0.0, directionlist.get("s_longitude"))
            );

            LatLng destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, directionlist.get("d_latitude")), GeneralFunctions.parseDoubleValue(0.0, directionlist.get("d_longitude"))
            );

            estimateFare(distance, time, responseString, sourceLocation, destLocation);

        }

    }

    @Override
    public void geoCodeAddressFound(String address, double latitude, double longitude, String geocodeobject) {

    }

    public class setOnClickAct implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    FareEstimateActivity.super.onBackPressed();
                    break;
                case R.id.searchLocTxt:
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(FareEstimateActivity.this);
                        startActivityForResult(intent, Utils.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                        generalFunc.showMessage(generalFunc.getCurrentView(FareEstimateActivity.this),
                                generalFunc.retrieveLangLBl("", "LBL_SERVICE_NOT_AVAIL_TXT"));
                    }
                    break;

            }
        }
    }
}
