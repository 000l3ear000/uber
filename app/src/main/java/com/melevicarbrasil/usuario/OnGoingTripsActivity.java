package com.melevicarbrasil.usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.files.OngoingTripAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 21-02-2017.
 */
public class OnGoingTripsActivity extends AppCompatActivity implements OngoingTripAdapter.OnItemClickListener {

    OngoingTripAdapter ongoingTripAdapter;
    String userProfileJson = "";
    String iTripId = "";
    String driverStatus = "";
    private MTextView titleTxt, noOngoingTripsTxt;
    private RecyclerView onGoingTripsListRecyclerView;
    private ImageView backImgView;
    private ProgressBar loading_ongoing_trips;
    private ErrorView errorView;
    private GeneralFunctions generalFunc;
    private ArrayList<HashMap<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoingtrips_layout);
        init();
        setLables();
    }


    private void init() {
        noOngoingTripsTxt = (MTextView) findViewById(R.id.noOngoingTripsTxt);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        onGoingTripsListRecyclerView = (RecyclerView) findViewById(R.id.onGoingTripsListRecyclerView);
        loading_ongoing_trips = (ProgressBar) findViewById(R.id.loading_ongoing_trips);

        errorView = (ErrorView) findViewById(R.id.errorView);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        //userProfileJson=getIntent().getStringExtra("UserProfileJson");
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        String Last_trip_data = generalFunc.getJsonValue("TripDetails", userProfileJson);
        iTripId = generalFunc.getJsonValue("iTripId", Last_trip_data);

        backImgView.setOnClickListener(new setOnClickList());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOngoingUserTrips();
    }


    private void setViews() {

        ongoingTripAdapter = new OngoingTripAdapter(getActContext(), list, generalFunc, false);
        onGoingTripsListRecyclerView.setAdapter(ongoingTripAdapter);
        ongoingTripAdapter.setOnItemClickListener(this);
        ongoingTripAdapter.notifyDataSetChanged();

        if (list.size() > 0) {
            onGoingTripsListRecyclerView.setVisibility(View.VISIBLE);
            noOngoingTripsTxt.setVisibility(View.GONE);
        } else {
            onGoingTripsListRecyclerView.setVisibility(View.GONE);
            noOngoingTripsTxt.setVisibility(View.VISIBLE);
        }
    }


    public void getOngoingUserTrips() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }

        loading_ongoing_trips.setVisibility(View.VISIBLE);

        if (ongoingTripAdapter != null && list.size() > 0) {
            list.clear();
            ongoingTripAdapter.notifyDataSetChanged();
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getOngoingUserTrips");
        parameters.put("iUserId", generalFunc.getMemberId());

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                list = new ArrayList<>();
                closeLoader();

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {

                    JSONArray message = generalFunc.getJsonArray(Utils.message_str, responseString);

                    String senderName = generalFunc.getJsonValue("vName", userProfileJson) + " " + generalFunc.getJsonValue("vLastName", userProfileJson);
                    String APP_TYPE = generalFunc.getJsonValue("APP_TYPE", userProfileJson);
                    if (message != null && message.length() > 0) {
                        for (int i = 0; i < message.length(); i++) {
                            JSONObject jobject1 = generalFunc.getJsonObject(message, i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("iDriverId", generalFunc.getJsonValueStr("iDriverId", jobject1));
                            map.put("driverImage", generalFunc.getJsonValueStr("driverImage", jobject1));
                            map.put("driverName", generalFunc.getJsonValueStr("driverName", jobject1));
                            map.put("vCode", generalFunc.getJsonValueStr("vCode", jobject1));
                            map.put("driverMobile", generalFunc.getJsonValueStr("driverMobile", jobject1));
                            map.put("driverStatus", generalFunc.getJsonValueStr("driverStatus", jobject1));
                            map.put("driverRating", generalFunc.getJsonValueStr("driverRating", jobject1));
                            map.put("vRideNo", generalFunc.getJsonValueStr("vRideNo", jobject1));
                            map.put("tSaddress", generalFunc.getJsonValueStr("tSaddress", jobject1));
                            map.put("iTripId", generalFunc.getJsonValueStr("iTripId", jobject1));
                            map.put("senderName", senderName);
                            map.put("Booking_LBL", generalFunc.retrieveLangLBl("Booking No", "LBL_BOOKING"));
                            map.put("dDateOrig", generalFunc.getJsonValueStr("dDateOrig", jobject1));
                            map.put("SelectedTypeName", generalFunc.getJsonValueStr("SelectedTypeName", jobject1));
                            driverStatus = generalFunc.getJsonValueStr("driverStatus", jobject1);
                            map.put("driverLatitude", generalFunc.getJsonValueStr("driverLatitude", jobject1));
                            map.put("driverLongitude", generalFunc.getJsonValueStr("driverLongitude", jobject1));
                            map.put("eServiceLocation", generalFunc.getJsonValueStr("eServiceLocation", jobject1));

                            map.put("tStratLat", generalFunc.getJsonValueStr("driverLatitude", jobject1));
                            map.put("tStartLong", generalFunc.getJsonValueStr("driverLongitude", jobject1));
                            map.put("eFareType", generalFunc.getJsonValueStr("eFareType", jobject1));
                            map.put("moreServices", generalFunc.getJsonValueStr("moreServices", jobject1));

                            map.put("vServiceTitle", generalFunc.getJsonValueStr("vServiceTitle", jobject1));

                            String eType = generalFunc.getJsonValueStr("eType", jobject1);
                            map.put("eType", eType);
                            map.put("liveTrackLBL", generalFunc.retrieveLangLBl("", "LBL_MULTI_LIVE_TRACK_TEXT"));
                            map.put("viewDetailLBL", generalFunc.retrieveLangLBl("View Details", "LBL_VIEW_DETAILS"));

                            if (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                                if (eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                                    map.put("eTypeName", generalFunc.retrieveLangLBl("Delivery", "LBL_DELIVERY"));
                                } else {
                                    map.put("eTypeName", generalFunc.retrieveLangLBl("", "LBL_SERVICES"));
                                }
                            } else {
                                map.put("eTypeName", "");
                            }

                            list.add(map);
                        }
                    } else {
                        noOngoingTripsTxt.setVisibility(View.VISIBLE);
                        noOngoingTripsTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }
                } else {
                    if (!GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {
                        noOngoingTripsTxt.setVisibility(View.VISIBLE);
                        noOngoingTripsTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    } else {
                        generateErrorView();
                    }
                }
                setViews();

            }
        });
        exeWebServer.execute();
    }

    public void closeLoader() {
        if (loading_ongoing_trips.getVisibility() == View.VISIBLE) {
            loading_ongoing_trips.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
            noOngoingTripsTxt.setVisibility(View.GONE);
        }
        errorView.setOnRetryListener(() -> getOngoingUserTrips());
    }


    @Override
    public void onItemClickList(String type, int position) {
        Utils.hideKeyboard(getActContext());
        if (type.equalsIgnoreCase("View Detail")) {
            Bundle bn = new Bundle();
            bn.putSerializable("TripDetail", list.get(position));
            bn.putSerializable("driverStatus", driverStatus);

            new StartActProcess(getActContext()).startActForResult(OnGoingTripDetailsActivity.class, bn, Utils.LIVE_TRACK_REQUEST_CODE);
        } else if (type.equalsIgnoreCase("Live Track")) {
            MyApp.getInstance().restartWithGetDataApp(list.get(position).get("iTripId"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.LIVE_TRACK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
           /* Bundle bn = new Bundle();
            bn.putString("callGetDetail", "true");
            new StartActProcess(getActContext()).setOkResult(bn);
            (getActContext()).setResult(Activity.RESULT_OK);*/

            MyApp.getInstance().restartWithGetDataApp();
            onBackPressed();
        } else if (requestCode == Utils.MULTIDELIVERY_HISTORY_RATE_CODE) {
            onBackPressed();
        }
    }

    private void setLables() {


        titleTxt.setText(generalFunc.retrieveLangLBl("My Ongoing Trips", "LBL_MY_ON_GOING_JOB"));
        noOngoingTripsTxt.setText(generalFunc.retrieveLangLBl("No Ongoing Trips Available.", "LBL_NO_ONGOING_TRIPS_AVAIL"));
    }

    private Activity getActContext() {
        return OnGoingTripsActivity.this;
    }

    @Override
    public void onBackPressed() {

        //bug_004 start
        if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {


            if ((generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("Active") ||
                    generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValue("eType", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

//                OnGoingTripsActivity.super.onBackPressed();
                if (getIntent().hasExtra("isTripRunning")) {
                    MyApp.getInstance().restartWithGetDataApp();
                }

            } else if (generalFunc.prefHasKey(Utils.isMultiTrackRunning) && generalFunc.retrieveValue(Utils.isMultiTrackRunning).equalsIgnoreCase("Yes")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else if (getIntent().getBooleanExtra("isRestart", false)) {

                Bundle bn = new Bundle();
                new StartActProcess(getActContext()).startActWithData(UberXActivity.class, bn);
                finishAffinity();
            } else {

                OnGoingTripsActivity.super.onBackPressed();
            }
        }
        else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery) || generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_Deliver)) {


            if ((generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("Active") ||
                    generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValue("eType", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

//                OnGoingTripsActivity.super.onBackPressed();
                if (getIntent().hasExtra("isTripRunning")) {
                    MyApp.getInstance().restartWithGetDataApp();
                }

            } else if (generalFunc.prefHasKey(Utils.isMultiTrackRunning) && generalFunc.retrieveValue(Utils.isMultiTrackRunning).equalsIgnoreCase("Yes")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else if (getIntent().getBooleanExtra("isRestart", false)) {

                Bundle bn = new Bundle();
                bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));
                new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                finishAffinity();
            } else {

                OnGoingTripsActivity.super.onBackPressed();
            }
        }

        else {
            OnGoingTripsActivity.super.onBackPressed();
        }
        //bug_004 end

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            if (view.getId() == R.id.backImgView) {

                onBackPressed();

            }
        }
    }


}
