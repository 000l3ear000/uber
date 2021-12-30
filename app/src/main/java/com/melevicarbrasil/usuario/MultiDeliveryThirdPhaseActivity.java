package com.melevicarbrasil.usuario;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.adapter.files.MultiPaymentTypeRecyclerAdapter;
import com.melevicarbrasil.usuario.deliverAll.MapDelegate;
import com.general.files.DataParser;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MapServiceApi;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.Delivery_Data;
import com.model.Multi_Delivery_Data;
import com.model.Multi_Dest_Info_Detail_Data;
import com.squareup.picasso.Picasso;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.ErrorView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Esite on 10-04-2018.
 */

public class MultiDeliveryThirdPhaseActivity extends AppCompatActivity implements MultiPaymentTypeRecyclerAdapter.OnTypeSelectListener, MapDelegate {
    HashMap<String, String> mapData = new HashMap<>();

    MTextView titleTxt;
    ImageView backImgView;
    GeneralFunctions generalFunc;
    MTextView paymentTitle, payementModeTitle, codeTitle;
    LinearLayout fareDetailDisplayArea;

    String selectedcar = "";
    String vVehicleType = "";

    LinearLayout paymentArea, payTypeSelectArea;
    private String userProfileJson;
    private ArrayList<Multi_Delivery_Data> list = new ArrayList<>();
    View convertView = null;


    MButton btn_type2;

    // payment
    RadioButton cashRadioBtn;
    RadioButton cardRadioBtn;
    boolean isCardValidated = true;

    // promo code
    private String appliedPromoCode = "";
    private boolean checkResponsiblePerson = false;

    public JSONArray ja = new JSONArray();
    ArrayList<Multi_Dest_Info_Detail_Data> dest_details_Array = new ArrayList<Multi_Dest_Info_Detail_Data>();

    RecyclerView paymentMethodRecyclerView;
    RecyclerView mRecyclerViewUploadFile;
    String selectedRecipientName = "";
    String selectedPayTypeName = "";
    MultiPaymentTypeRecyclerAdapter payTypeAdapter;
    ArrayList<HashMap<String, String>> paymentTypeList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> recipientList = new ArrayList<>();
    boolean recipientNotSelected = false;
    public double totalFare = 0.0;
    public String individualFare = "0.0";

    //PaymentBy

    String Payment_Type_1 = "Sender";
    public String Payment_Type_2 = "Receiver";
    public String Payment_Type_3 = "Individual";

    public int selectedPos = -1;
    public int selectedRecipientPos = -1;
    public String currencySymbol = "";

    public String PAYMENT_DONE_BY = "PaymentDoneBy";
    private String PAYMENT_PERSON_NAME = "name";
    private String RECIPIENT_NAME = "recipientName";
    private String ITEM_ID_KEY = "ItemID";

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Temp";
    private static final int SELECT_PICTURE = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri;

    boolean isOutStandingDailogShow = false;
    androidx.appcompat.app.AlertDialog outstanding_dialog;


    View couponCodeArea;
    LinearLayout promocodeArea;
    MTextView promocodeappliedHTxt;
    MTextView promocodeappliedVTxt;

    ImageView couponCodeImgView, couponCodeCloseImgView;
    MTextView applyCouponHTxt;
    MTextView appliedPromoHTxtView;
    String responseStr = "";

    ArrayList<Multi_Delivery_Data> templistofViews = new ArrayList<>();  // temp list
    ArrayList<Multi_Delivery_Data> wayPointslist = new ArrayList<>();  // List of Way Points/ middle points
    ArrayList<Multi_Delivery_Data> destPointlist = new ArrayList<>();  // destination Points
    ArrayList<Multi_Delivery_Data> finalPointlist = new ArrayList<>();  // final Points list with time & distance & based on shortest location first algorithm


    private String distance = "";
    private String time = "";
    LinearLayout errorViewArea;
    ErrorView errorView;

    boolean cardSelectedOnStart = false;
    String SYSTEM_PAYMENT_FLOW = "";
    String APP_PAYMENT_METHOD = "";
    String APP_PAYMENT_MODE = "";
    boolean isCashSelected = true;
    boolean iswalletSelect = false;
    ImageView carTypeImg;
    ProgressBar progress_bar;
    LinearLayout PromoCodearea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_third_phase_multi);
        mapData = (HashMap<String, String>) getIntent().getSerializableExtra("selectedData");

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        retriverUserProfileJson();

        if (getIntent().hasExtra("selectedDetails")) {

            Gson gson = new Gson();
            String data1 = getIntent().getStringExtra("selectedDetails");
            String data2 = getIntent().getStringExtra("timeAndDistArr");
            Logger.d("timeAndDistArr", "::" + data2);

            list = gson.fromJson(data1, new TypeToken<ArrayList<Multi_Delivery_Data>>() {
                    }.getType()
            );

            dest_details_Array = new Gson().fromJson(data2, new TypeToken<ArrayList<Multi_Dest_Info_Detail_Data>>() {
                    }.getType()
            );

        }


        init();
        setLables();

        setSelected("3");

        paymentTypeList = new ArrayList<>();
        recipientList = new ArrayList<>();

        // add Payment Details

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getIsFromLoc().equalsIgnoreCase("Yes")) {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(PAYMENT_DONE_BY, Payment_Type_1);
                map.put(PAYMENT_PERSON_NAME, generalFunc.retrieveLangLBl("Sender", "LBL_MULTI_SENDER_TXT"));
                paymentTypeList.add(map);

                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put(PAYMENT_DONE_BY, Payment_Type_2);
                map1.put(PAYMENT_PERSON_NAME, generalFunc.retrieveLangLBl("Any One Receiver", "LBL_MULTI_ANY_RECIPIENT"));
                paymentTypeList.add(map1);


                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put(PAYMENT_DONE_BY, Payment_Type_3);
                map2.put(PAYMENT_PERSON_NAME, generalFunc.retrieveLangLBl("Each Receiver", "LBL_MULTI_EACH_RECIPIENT"));
                paymentTypeList.add(map2);


            } else {

                ArrayList<Delivery_Data> arrayList = list.get(i).getDt();
                if (arrayList != null && arrayList.size() > 0) {
                    for (int j = 0; j < arrayList.size(); j++) {
                        if (arrayList.get(j) != null && (arrayList.get(j).getvFieldName().equalsIgnoreCase("Recepient Name") || arrayList.get(j).getiDeliveryFieldId().equalsIgnoreCase("2"))) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(PAYMENT_DONE_BY, Payment_Type_2);
                            map.put(RECIPIENT_NAME, arrayList.get(j).getvFieldValue());
                            map.put(ITEM_ID_KEY, "" + arrayList.get(j).getItemID());
                            map.put(PAYMENT_PERSON_NAME, generalFunc.retrieveLangLBl("", "LBL_RECIPIENT") + " " + (i) + " (" + arrayList.get(j).getvFieldValue() + ")");
                            recipientList.add(map);
                            break;
                        }
                    }

                }
            }


        }

        // if route calculation enable && manual calculation then call findRoute

        if (generalFunc.retrieveValue(Utils.ENABLE_ROUTE_CALCULATION_MULTI_KEY).equalsIgnoreCase("Yes") && getIntent().hasExtra("isManualCalculation")) {
            findRoute("call");
            return;
        }

        // or only call fare estimate
        callFareEstimate(dest_details_Array);

    }

    private void retriverUserProfileJson() {
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        SYSTEM_PAYMENT_FLOW = generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson);
        APP_PAYMENT_MODE = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson);
        APP_PAYMENT_METHOD = generalFunc.getJsonValue("APP_PAYMENT_METHOD", userProfileJson);

    }

    public void generateErrorView(String url, String type, boolean removePromo) {

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");


        if (errorViewArea.getVisibility() != View.VISIBLE) {
            errorViewArea.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {

                if (type.equalsIgnoreCase("callFareDetailsRequest")) {
                    callFareDetailsRequest(removePromo);
                } else {
                    findRoute(url);
                }

            }
        });
    }

    private void callFareEstimate(ArrayList<Multi_Dest_Info_Detail_Data> dest_details_Array) {
        // generate all locations time & distance array to pass on type=getEstimateFareDetailsArr
        if (dest_details_Array.size() > 0) {

            for (int i = 0; i < dest_details_Array.size(); i++) {
                try {
                    JSONObject deliveriesObjall = new JSONObject();
                    deliveriesObjall.put("time", dest_details_Array.get(i).getTime());
                    deliveriesObjall.put("distance", dest_details_Array.get(i).getDistance());
                    ja.put(deliveriesObjall);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        if ((ja != null && ja.length() > 0) || (!distance.equalsIgnoreCase(""))) {
            setPaymentOptions();
            callFareDetailsRequest(false);
        }
    }

    private void findRoute(String directionsUrl) {
        btn_type2.setEnabled(false);
        PromoCodearea.setVisibility(View.GONE);
        String url = directionsUrl;
        HashMap<String, String> hashMap = new HashMap<>();
        ArrayList<String> data_waypoints = new ArrayList<>();
        if (Utils.checkText(url)) {
            // Origin of route
            String str_origin = "origin=" + list.get(0).getDestLat() + "," + list.get(0).getDestLong();


            String str_dest = "";
            String waypoints = "";
            wayPointslist = new ArrayList<>();      // List of Way Points
            destPointlist = new ArrayList<>();      // destination Points
            finalPointlist = new ArrayList<>();     // final Points list with time & distance & based on shortest location first algorithm
            dest_details_Array = new ArrayList<>(); // time & distance array of shortest location first

            templistofViews = new ArrayList<>();    // temp list

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isDetailsAdded() == true) {
                    templistofViews.add(list.get(i));
                }
            }
            // Retrive middle & destination points

            if (templistofViews.size() > 0) {
                String lastAddress = "";
                for (int i = 0; i < templistofViews.size(); i++) {

                    if (i == templistofViews.size() - 1) {
                        str_dest = "destination=" + templistofViews.get(templistofViews.size() - 1).getDestLat() + "," + templistofViews.get(templistofViews.size() - 1).getDestLong();
                        hashMap.put("d_latitude", templistofViews.get(templistofViews.size() - 1).getDestLat() + "");
                        hashMap.put("d_longitude", templistofViews.get(templistofViews.size() - 1).getDestLong() + "");
                        templistofViews.get(i).setDestination(true);
                        destPointlist.add(templistofViews.get(i));
                    } else if (i == templistofViews.size() - 2) {
                        templistofViews.get(i).setDestination(true);
                        wayPointslist.add(templistofViews.get(i));
                        lastAddress = templistofViews.get(i).getDestLat() + "," + templistofViews.get(i).getDestLong();
                        data_waypoints.add(templistofViews.get(i).getDestLat() + "," + templistofViews.get(i).getDestLong());

                    } else {
                        templistofViews.get(i).setDestination(true);
                        wayPointslist.add(templistofViews.get(i));
                        waypoints = waypoints + templistofViews.get(i).getDestLat() + "," + templistofViews.get(i).getDestLong() + "|";
                        data_waypoints.add(templistofViews.get(i).getDestLat() + "," + templistofViews.get(i).getDestLong());
                    }

                }
                waypoints = "waypoints=optimize:true|" + waypoints + lastAddress;
            } else {
                str_dest = "destination=" + templistofViews.get(templistofViews.size() - 1).getDestLat() + "," + templistofViews.get(templistofViews.size() - 1).getDestLong();
                hashMap.put("d_latitude", templistofViews.get(templistofViews.size() - 1).getDestLat() + "");
                hashMap.put("d_longitude", templistofViews.get(templistofViews.size() - 1).getDestLong() + "");
                destPointlist.add(templistofViews.get(templistofViews.size() - 1));
            }

            String parameters = "";
            // Building the parameters to the web service
            if (templistofViews.size() > 1) {
                parameters = str_origin + "&" + str_dest + "&" + waypoints;

            } else {
                parameters = str_origin + "&" + str_dest;

            }
            String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
            url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=" + serverKey + "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true";


            hashMap.put("s_latitude", list.get(0).getDestLat() + "");
            hashMap.put("s_longitude", list.get(0).getDestLong() + "");


            hashMap.put("parameters", parameters);
            hashMap.put("waypoints", data_waypoints.toString());

        }
        Logger.d("Api", "directUrl_value True");

        progress_bar.setVisibility(View.VISIBLE);

        MapServiceApi.getDirectionservice(getActContext(), hashMap, this, data_waypoints,false);

//        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
//
//        String finalUrl = url;
//        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
//            @Override
//            public void setResponse(String responseString) {
//                btn_type2.setEnabled(true);
//                if (responseString != null && !responseString.equals("")) {
//
//                    String status = generalFunc.getJsonValue("status", responseString);
//
//                    if (status.equals("OK")) {
//
//                        JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
//                        if (obj_routes != null && obj_routes.length() > 0) {
//
//                            DataParser parser = new DataParser(getActContext(), list, wayPointslist, destPointlist, finalPointlist, dest_details_Array);
//                            parser.getDistanceArray(generalFunc.getJsonObject(responseString));
//                            distance = parser.distance;
//                            time = parser.time;
//
//                            if (finalPointlist.size() > 0) {
//                                ArrayList<Multi_Delivery_Data> finalAllPointlist = new ArrayList<>();
//                                finalAllPointlist = new ArrayList<>();
//                                finalAllPointlist.add(list.get(0));
//                                finalAllPointlist.addAll(finalPointlist);
//                               /*
//                               // Re-add blank added destinations
//                               if ((listofViews.size()) > finalAllPointlist.size()) {
//                                    for (int i = 0; i < (listofViews.size() - finalAllPointlist.size()); i++) {
//                                        Multi_Delivery_Data mt = new Multi_Delivery_Data();
//                                        mt.setHintLable(generalFunc.retrieveLangLBl("", "LBL_MULTI_ADD_NEW_DESTINATION"));
//                                        mt.setFRLable(LBL_MULTI_FR_TXT);
//                                        mt.setTOLable(LBL_MULTI_TO_TXT);
//                                        finalAllPointlist.add(mt);
//                                    }
//                                }
//                               */
//
//                                list.clear();
//                                list.addAll(finalAllPointlist);
//                            }
//
//                            storeDetails(list, true);
//
//                            callFareEstimate(dest_details_Array);
//
//                        }
//
//
//                    } else {
//                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_ERROR_TXT"),
//                                generalFunc.retrieveLangLBl("", "LBL_GOOGLE_DIR_NO_ROUTE"));
//                    }
//
//                } else {
//                    btn_type2.setEnabled(true);
//                    generateErrorView(finalUrl, "", false);
//                }
//            }
//        });
//        exeWebServer.execute();
    }

    // Store Details
    private void storeDetails(ArrayList<Multi_Delivery_Data> array, boolean setFocusable) {

        ArrayList<Multi_Delivery_Data> finalAllDetailsArray = new ArrayList<Multi_Delivery_Data>();

        JSONArray jaStore = new JSONArray();
        JSONArray jaStore1 = new JSONArray();
        JSONArray mainJaStore = new JSONArray();
        JSONArray mainAllJaStore = new JSONArray();

        if (setFocusable) {
            for (int i = 0; i < array.size(); i++) {
                storeDetails(i, jaStore, jaStore1, finalAllDetailsArray, array, setFocusable);
            }
        } else {
            for (int i = 1; i < array.size(); i++) {
                storeDetails(i, jaStore, jaStore1, finalAllDetailsArray, array, setFocusable);
            }
        }

        try {

            mainJaStore.put(0, jaStore1);
            mainAllJaStore.put(0, jaStore);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        generalFunc.removeValue(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY);
        generalFunc.storeData(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY, jaStore1.toString());

        Gson gson = new Gson();
        String json = gson.toJson(finalAllDetailsArray);

        generalFunc.removeValue(Utils.MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY);
        generalFunc.storeData(Utils.MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY, json);


    }

    private JSONObject storeDetails(int pos, JSONArray jaStore, JSONArray jaStore1, ArrayList<Multi_Delivery_Data> finalAllDetailsArray, ArrayList<Multi_Delivery_Data> mainArray, boolean setFocusable) {
        JSONObject deliveriesObj = new JSONObject();
        JSONObject deliveriesObjall = new JSONObject();
        ArrayList<Delivery_Data> dt = mainArray.get(pos).getDt();
        Multi_Delivery_Data details = mainArray.get(pos);


        for (int i = 0; i < dt.size(); i++) {
            try {

                if (dt.get(i).geteInputType().equalsIgnoreCase("SelectAddress")) {
                    deliveriesObjall.put("DestAddress", dt.get(i).getDestAddress());
                    deliveriesObjall.put("DestLat", dt.get(i).getDestLat());
                    deliveriesObjall.put("DestLong", dt.get(i).getDestLong());
                } else {
                    deliveriesObjall.put("vFieldValue", dt.get(i).getvFieldValue());
                    deliveriesObjall.put("iDeliveryFieldId", dt.get(i).getiDeliveryFieldId());
                    deliveriesObjall.put("eInputType", dt.get(i).geteInputType());
                    deliveriesObjall.put("selectedOptionID", dt.get(i).getSelectedOptionID());
                    deliveriesObjall.put("itemID", dt.get(i).getItemID());
                    deliveriesObjall.put("ePaymentByReceiver", dt.get(i).getPaymentDoneBy());
                }

                if (dt.get(i).geteInputType().equals("Select")) {
                    deliveriesObj.put(dt.get(i).getiDeliveryFieldId(), dt.get(i).getSelectedOptionID());
                } else if (dt.get(i).geteInputType().equalsIgnoreCase("SelectAddress")) {
                    deliveriesObj.put("vReceiverAddress", dt.get(i).getDestAddress());
                    deliveriesObj.put("vReceiverLatitude", dt.get(i).getDestLat());
                    deliveriesObj.put("vReceiverLongitude", dt.get(i).getDestLong());
                } else {
                    deliveriesObj.put(dt.get(i).getiDeliveryFieldId(), dt.get(i).getvFieldValue());
                }


                deliveriesObj.put("ePaymentByReceiver", mainArray.get(pos).getePaymentByReceiver());

                jaStore.put(i, deliveriesObjall);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (setFocusable) {
            finalAllDetailsArray.add(pos, details);
            try {
                jaStore1.put(pos, deliveriesObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            finalAllDetailsArray.add(details);
            jaStore1.put(deliveriesObj);
        }


        return deliveriesObj;
    }


    private void setLables() {
        promocodeappliedHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_APPLIED_COUPON_CODE"));
        titleTxt.setText(generalFunc.retrieveLangLBl("New Booking", "LBL_MULTI_NEW_BOOKING_TXT"));

        paymentTitle.setText(generalFunc.retrieveLangLBl("Responsible for payment", "LBL_MULTI_RESPONSIBLE_FOR_PAYMENT_TXT"));
        payementModeTitle.setText(generalFunc.retrieveLangLBl("Payment", "LBL_PAYMENT_MENU_SCREEN"));
        cashRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
        cardRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));
        ((MTextView) findViewById(R.id.chargeTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_CHARGES_TXT"));

        if (mapData.containsKey("isDeliverNow") && mapData.get("isDeliverNow").equals("true")) {
            btn_type2.setText(generalFunc.retrieveLangLBl("Deliver Now", "LBL_DELIVER_NOW"));
        } else {
            btn_type2.setText(generalFunc.retrieveLangLBl("Confirm Booking", "LBL_CONFIRM_BOOKING"));
        }
    }

    private void init() {

        PromoCodearea=(LinearLayout)findViewById(R.id.PromoCodearea);
        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        carTypeImg = (ImageView) findViewById(R.id.carTypeImg);
        paymentMethodRecyclerView = (RecyclerView) findViewById(R.id.paymentMethodRecyclerView);
        mRecyclerViewUploadFile = (RecyclerView) findViewById(R.id.view_recycler_view_upload_file);
        this.mRecyclerViewUploadFile.setHasFixedSize(true);
        paymentTitle = (MTextView) findViewById(R.id.paymentTitle);
        payementModeTitle = (MTextView) findViewById(R.id.payementModeTitle);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickAct());
        fareDetailDisplayArea = (LinearLayout) findViewById(R.id.fareDetailDisplayArea);
        paymentArea = (LinearLayout) findViewById(R.id.paymentArea);
        payTypeSelectArea = (LinearLayout) findViewById(R.id.payTypeSelectArea);
        cashRadioBtn = (RadioButton) findViewById(R.id.cashRadioBtn);
        cardRadioBtn = (RadioButton) findViewById(R.id.cardRadioBtn);

        couponCodeArea = findViewById(R.id.couponCodeArea);
        promocodeArea = (LinearLayout) findViewById(R.id.promocodeArea);
        promocodeappliedHTxt = (MTextView) findViewById(R.id.promocodeappliedHTxt);
        promocodeappliedVTxt = (MTextView) findViewById(R.id.promocodeappliedVTxt);
        couponCodeImgView = (ImageView) findViewById(R.id.couponCodeImgView);
        couponCodeCloseImgView = (ImageView) findViewById(R.id.couponCodeCloseImgView);
        if (generalFunc.isRTLmode()) {
            couponCodeImgView.setRotationY(180);
        }
        applyCouponHTxt = (MTextView) findViewById(R.id.applyCouponHTxt);
        appliedPromoHTxtView = (MTextView) findViewById(R.id.appliedPromoHTxtView);

        errorViewArea = (LinearLayout) findViewById(R.id.errorViewArea);
        errorView = (ErrorView) findViewById(R.id.errorView);

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());
        findViewById(R.id.imgAddPicture).setOnClickListener(new setOnClickList());

        cashRadioBtn.setOnClickListener(new setOnClickList());
        cardRadioBtn.setOnClickListener(new setOnClickList());
        couponCodeArea.setOnClickListener(new setOnClickList());

        selectedcar = mapData.get("SelectedCar");
        vVehicleType = mapData.get("vVehicleType");
    }

    @Override
    public void onTypeSelect(int position, int recipientPos) {

        selectedRecipientName = "";
        selectedPayTypeName = paymentTypeList.get(position).get(PAYMENT_DONE_BY);
        selectedPos = position;
        selectedRecipientPos = recipientPos;


        if ((selectedRecipientPos == -1 || !Utils.checkText(recipientList.get(recipientPos).get(RECIPIENT_NAME))) && paymentTypeList.get(position).get(PAYMENT_DONE_BY).equalsIgnoreCase(Payment_Type_2)) {
            recipientNotSelected = true;
            return;
        } else {
            recipientNotSelected = false;
            if (paymentTypeList.get(position).get(PAYMENT_DONE_BY).equalsIgnoreCase(Payment_Type_2)) {
                selectedRecipientName = recipientList.get(recipientPos).get(RECIPIENT_NAME);
            }
        }
    }


    public void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void chooseFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the rider's current state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    public View getCurrView() {
        return generalFunc.getCurrentView(MultiDeliveryThirdPhaseActivity.this);
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
        progress_bar.setVisibility(View.GONE);
        if (responseString != null && !responseString.equalsIgnoreCase("") && directionlist.get("distance") == null) {
            btn_type2.setEnabled(true);
            if (responseString != null && !responseString.equals("")) {


                if (directionlist.get("status").equalsIgnoreCase("OK")) {

//                    JSONArray obj_routes = generalFunc.getJsonArray(directionlist.get("routes"));
                    JSONArray obj_routes = generalFunc.getJsonArray("routes",responseString);
                    if (obj_routes != null && obj_routes.length() > 0) {

                        DataParser parser = new DataParser(getActContext(), list, wayPointslist, destPointlist, finalPointlist, dest_details_Array);
                        HashMap<String, String> routeMap = new HashMap<>();
                        routeMap.put("routes", obj_routes.toString());
                        try {
                            parser.getDistanceArray(new JSONObject(routeMap.toString()));
                            distance = parser.distance;
                            time = parser.time;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (finalPointlist.size() > 0) {
                            ArrayList<Multi_Delivery_Data> finalAllPointlist = new ArrayList<>();
                            finalAllPointlist = new ArrayList<>();
                            finalAllPointlist.add(list.get(0));
                            finalAllPointlist.addAll(finalPointlist);
                               /*
                               // Re-add blank added destinations
                               if ((listofViews.size()) > finalAllPointlist.size()) {
                                    for (int i = 0; i < (listofViews.size() - finalAllPointlist.size()); i++) {
                                        Multi_Delivery_Data mt = new Multi_Delivery_Data();
                                        mt.setHintLable(generalFunc.retrieveLangLBl("", "LBL_MULTI_ADD_NEW_DESTINATION"));
                                        mt.setFRLable(LBL_MULTI_FR_TXT);
                                        mt.setTOLable(LBL_MULTI_TO_TXT);
                                        finalAllPointlist.add(mt);
                                    }
                                }
                               */

                            list.clear();
                            list.addAll(finalAllPointlist);
                        }

                        storeDetails(list, true);

                        callFareEstimate(dest_details_Array);

                    }


                } else {
                    generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_ERROR_TXT"),
                            generalFunc.retrieveLangLBl("", "LBL_GOOGLE_DIR_NO_ROUTE"));
                }

            }
        } else {

            JSONArray obj_routes = generalFunc.getJsonArray(responseString);
            if (obj_routes != null && obj_routes.length() > 0) {

//                DataParser parser = new DataParser(getActContext(), list, wayPointslist, destPointlist, finalPointlist, dest_details_Array);
//                parser.getDistanceArray(generalFunc.getJsonObject(responseString));
                distance = directionlist.get("distance");
                time = directionlist.get("duration");

                if (finalPointlist.size() > 0) {
                    ArrayList<Multi_Delivery_Data> finalAllPointlist = new ArrayList<>();
                    finalAllPointlist = new ArrayList<>();
                    finalAllPointlist.add(list.get(0));
                    finalAllPointlist.addAll(finalPointlist);
                               /*
                               // Re-add blank added destinations
                               if ((listofViews.size()) > finalAllPointlist.size()) {
                                    for (int i = 0; i < (listofViews.size() - finalAllPointlist.size()); i++) {
                                        Multi_Delivery_Data mt = new Multi_Delivery_Data();
                                        mt.setHintLable(generalFunc.retrieveLangLBl("", "LBL_MULTI_ADD_NEW_DESTINATION"));
                                        mt.setFRLable(LBL_MULTI_FR_TXT);
                                        mt.setTOLable(LBL_MULTI_TO_TXT);
                                        finalAllPointlist.add(mt);
                                    }
                                }
                               */

                    list.clear();
                    list.addAll(finalAllPointlist);
                }

                storeDetails(list, true);
                Log.d("dest_details_Array", "::" + dest_details_Array.size());

                callFareEstimate(dest_details_Array);

            }

        }

    }

    @Override
    public void geoCodeAddressFound(String address, double latitude, double longitude, String geocodeobject) {

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == btn_type2.getId()) {
                //checkDetails();


                if (selectedPos == -1) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Payment Method", "LBL_CHOOSE_PAYMENT_METHOD"));
                    return;
                }

                if (selectedPayTypeName.equalsIgnoreCase(Payment_Type_2) && !Utils.checkText(selectedRecipientName.trim())) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Select", "LBL_SELECT_TXT") + " " + generalFunc.retrieveLangLBl("Recipient", "LBL_RECIPIENT"));
                    return;
                }

                Bundle bn = new Bundle();
                bn.putString("isWallet", iswalletSelect ? "Yes" : "No");
                bn.putBoolean("isCash", isCashSelected);
                if (selectedPayTypeName.equalsIgnoreCase(Payment_Type_2) || selectedPayTypeName.equalsIgnoreCase(Payment_Type_3)) {
                    bn.putBoolean("isRecipient", true);
                }
                // bn.putBoolean("isCODAllow", isCODAllow);
                // bn.putString("outStandingAmount", outStandingAmount);
                new StartActProcess(getActContext()).startActForResult(ProfilePaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);

            } else if (i == R.id.couponCodeArea) {
                // showPromoBox();
                Bundle bn = new Bundle();
                bn.putString("CouponCode", appliedPromoCode);
                bn.putString("eType", Utils.CabGeneralType_Deliver);
                new StartActProcess(getActContext()).startActForResult(CouponActivity.class, bn, Utils.SELECT_COUPON_REQ_CODE);
            } else if (i == R.id.couponCodeCloseImgView) {
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DELETE_CONFIRM_COUPON_MSG"), generalFunc.retrieveLangLBl("", "LBL_NO"), generalFunc.retrieveLangLBl("", "LBL_YES"), buttonId -> {
                    if (buttonId == 1) {
                        appliedPromoCode = "";
                        callFareDetailsRequest(true);
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_COUPON_REMOVE_SUCCESS"));
                    }
                });
            } else if (i == R.id.imgAddPicture) {
                if (generalFunc.isCameraStoragePermissionGranted()) {
                    new ImageSourceDialog().run();
                } else {
                    generalFunc.showMessage(getCurrView(), "Allow this app to use camera.");
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case GeneralFunctions.MY_PERMISSIONS_REQUEST: {
                if (generalFunc.isPermisionGranted()) {
                    new ImageSourceDialog().run();
                }
                break;

            }
        }
    }

    class ImageSourceDialog implements Runnable {

        @Override
        public void run() {


            final Dialog dialog_img_update = new Dialog(getActContext(), R.style.ImageSourceDialogStyle);

            dialog_img_update.setContentView(R.layout.design_image_source_select);

            MTextView chooseImgHTxt = (MTextView) dialog_img_update.findViewById(R.id.chooseImgHTxt);
            chooseImgHTxt.setText(generalFunc.retrieveLangLBl("Choose Category", "LBL_CHOOSE_CATEGORY"));

            SelectableRoundedImageView cameraIconImgView = (SelectableRoundedImageView) dialog_img_update.findViewById(R.id.cameraIconImgView);
            SelectableRoundedImageView galleryIconImgView = (SelectableRoundedImageView) dialog_img_update.findViewById(R.id.galleryIconImgView);

            ImageView closeDialogImgView = (ImageView) dialog_img_update.findViewById(R.id.closeDialogImgView);

            closeDialogImgView.setOnClickListener(v -> {

                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }
            });

            int btnRadius = Utils.dipToPixels(getActContext(), 25);
            int backColor = getResources().getColor(R.color.appThemeColor_Dark_1);
            int strokeColor = Color.parseColor("#00000000");
            int filterColor = getResources().getColor(R.color.appThemeColor_TXT_1);

            new CreateRoundedView(backColor, btnRadius, 0, strokeColor, cameraIconImgView);
            cameraIconImgView.setColorFilter(filterColor);

            new CreateRoundedView(backColor, btnRadius, 0, strokeColor, galleryIconImgView);
            galleryIconImgView.setColorFilter(filterColor);


            cameraIconImgView.setOnClickListener(v -> {

                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }

                if (!isDeviceSupportCamera()) {
                    generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_NOT_SUPPORT_CAMERA_TXT"));
                } else {
                    chooseFromCamera();
                }

            });

            galleryIconImgView.setOnClickListener(v -> {

                if (dialog_img_update != null) {
                    dialog_img_update.cancel();
                }

                chooseFromGallery();


            });

            dialog_img_update.setCanceledOnTouchOutside(true);

            Window window = dialog_img_update.getWindow();
            window.setGravity(Gravity.BOTTOM);

            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog_img_update.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (generalFunc.isRTLmode()) {
                dialog_img_update.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

            dialog_img_update.show();

        }

    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    public void setCancelable(Dialog dialogview, boolean cancelable) {
        final Dialog dialog = dialogview;
        View touchOutsideView = dialog.getWindow().getDecorView().findViewById(R.id.touch_outside);
        View bottomSheetView = dialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);

        if (cancelable) {
            touchOutsideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                }
            });
            BottomSheetBehavior.from(bottomSheetView).setHideable(true);
        } else {
            touchOutsideView.setOnClickListener(null);
            BottomSheetBehavior.from(bottomSheetView).setHideable(false);
        }
    }


    public class setOnClickAct implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    MultiDeliveryThirdPhaseActivity.super.onBackPressed();
                    break;

            }
        }
    }

    // set Payment Options

    private void setPaymentOptions() {
        cashRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCashSelection();
            }

        });

        cardRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cardSelectedOnStart) {
                cardSelectedOnStart = false;
                return;
            }
            if (isChecked) {
                if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

                    setCardSelection(false);
                    checkCardConfig();

                } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    isCardValidated = true;
                    setCardSelection(false);
                }
            }
        });


        if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash")) {
            cashRadioBtn.setVisibility(View.VISIBLE);
            cardRadioBtn.setVisibility(View.GONE);
            cashRadioBtn.performClick();
        } else if (APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
            cashRadioBtn.setVisibility(View.GONE);
            cardRadioBtn.setVisibility(View.VISIBLE);
            cardSelectedOnStart = true;
//            cardRadioBtn.performClick();
            setCardSelection(false);
            isCardValidated = false;
        } else {
        }
        if (!APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
            setCashSelection();
        }
        if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            cardRadioBtn.setText(generalFunc.retrieveLangLBl("Pay by Wallet", "LBL_PAY_BY_WALLET_TXT"));


        }
    }


    // payement handling

    public void setCashSelection() {
        isCardValidated = false;
        checkResponsiblePerson = true;
        cashRadioBtn.setChecked(true);
        cardRadioBtn.setChecked(false);
        paymentArea.setVisibility(View.VISIBLE);
    }

    public void setCardSelection(boolean b) {
        checkResponsiblePerson = false;
        reSetAdapter(true);
        cardRadioBtn.setChecked(true);
        cashRadioBtn.setChecked(false);
        paymentArea.setVisibility(View.GONE);
//        if (b) {
//            btn_type2.performClick();
//        }
    }

    private void reSetAdapter(boolean setSender) {

        selectedRecipientName = "";
        selectedPayTypeName = "";
        selectedPos = -1;
        selectedRecipientPos = -1;

        payTypeAdapter = new MultiPaymentTypeRecyclerAdapter(MultiDeliveryThirdPhaseActivity.this, generalFunc, recipientList, paymentTypeList, getActContext());
        payTypeAdapter.setOnTypeSelectListener(this);
        if (setSender) {
            selectedRecipientName = "";
            selectedPos = 0;
            selectedPayTypeName = Payment_Type_1;
            selectedRecipientPos = -1;
            payTypeAdapter.setlastCheckedPosition(0);
        }
        paymentMethodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentMethodRecyclerView.setAdapter(payTypeAdapter);
        payTypeAdapter.notifyDataSetChanged();
    }


    public void checkPaymentCard() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "CheckCard");
        parameters.put("iUserId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);
                if (action.equals("1")) {

                    isCardValidated = true;
                    setCardSelection(true);
//                    setCardSelection(false);


                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    private void checkDetails() {

//        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
//            if (!isCardValidated && APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
//                checkCardConfig();
//                return;
//            }
//        }
//
//        if (!isCardValidated && cardRadioBtn.isChecked()) {
//            checkCardConfig();
//            return;
//        }


        // Update Selected values

//        if (selectedPos == -1) {
//            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Payment Method", "LBL_CHOOSE_PAYMENT_METHOD"));
//            return;
//        }


        if (!isOutStandingDailogShow) {
            checkOutStandingAmount(true);
            return;
        }

        isOutStandingDailogShow = false;

        // add responsible for payment based on selected payment method

        if (selectedPos != -1) {
            for (int j = 1; j < list.size(); j++) {

                list.get(j).setPaymentType(paymentTypeList.get(selectedPos).get(PAYMENT_DONE_BY));
                list.get(j).setePaymentByReceiver("No");
                for (int k = 0; k < list.get(j).getDt().size(); k++) {

                    list.get(j).getDt().get(k).setPaymentDoneBy("No");
                    list.get(j).getDt().get(k).setePaymentByReceiver("No");

                    if (selectedRecipientPos != -1 && selectedRecipientPos + 1 == j && paymentTypeList.get(selectedPos).get(PAYMENT_DONE_BY).equalsIgnoreCase(Payment_Type_2)) {
                        list.get(j).setePaymentByReceiver("Yes");
                        list.get(j).getDt().get(k).setPaymentDoneBy("Yes");
                        list.get(j).getDt().get(k).setePaymentByReceiver("Yes");

                    } else if (paymentTypeList.get(selectedPos).get(PAYMENT_DONE_BY).equalsIgnoreCase(Payment_Type_3)) {
                        list.get(j).setePaymentByReceiver("Yes");
                        list.get(j).getDt().get(k).setPaymentDoneBy("Yes");
                        list.get(j).getDt().get(k).setePaymentByReceiver("Yes");
                    }
                }
            }
        }


        Bundle bn = new Bundle();
        if (list.size() > 0) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            bn.putString("list", json);
        }

//        bn.putString("isCashPayment", "" + cashRadioBtn.isChecked());
        bn.putString("isCashPayment", "" + isCashSelected);
        bn.putString("paymentMethod", selectedPayTypeName);
        bn.putString("isWallet", "" + iswalletSelect);
        bn.putString("promocode", appliedPromoCode);
        bn.putString("totalFare", currencySymbol + "" + totalFare);

        if (mapData.containsKey("time_multi")) {
            bn.putString("total_del_dist", "" + mapData.get("distance_multi"));
            bn.putString("total_del_time", "" + mapData.get("time_multi"));
        } else {
            bn.putString("total_del_dist", "" + mapData.get("distance"));
            bn.putString("total_del_time", "" + mapData.get("time"));
        }

        (new StartActProcess(getActContext())).setOkResult(bn);
        finish();

    }

    public void checkCardConfig() {
        retriverUserProfileJson();

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                OpenCardPaymentAct(true);
            } else {
                showPaymentBox(false, false);
            }
        }
    }

    public void outstandingDialog(boolean isReqNow, String data) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dailog_outstanding, null);

        final MTextView outStandingTitle = (MTextView) dialogView.findViewById(R.id.outStandingTitle);
        final MTextView outStandingValue = (MTextView) dialogView.findViewById(R.id.outStandingValue);
        final MTextView cardtitleTxt = (MTextView) dialogView.findViewById(R.id.cardtitleTxt);
        final MTextView adjustTitleTxt = (MTextView) dialogView.findViewById(R.id.adjustTitleTxt);
        final LinearLayout cardArea = (LinearLayout) dialogView.findViewById(R.id.cardArea);
        final LinearLayout adjustarea = (LinearLayout) dialogView.findViewById(R.id.adjustarea);


        outStandingTitle.setText(generalFunc.retrieveLangLBl("", "LBL_OUTSTANDING_AMOUNT_TXT"));
        outStandingValue.setText(generalFunc.getJsonValue("fOutStandingAmountWithSymbol", data));
        cardtitleTxt.setText(generalFunc.retrieveLangLBl("Pay Now", "LBL_PAY_NOW"));

        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-3")) {
            adjustarea.setVisibility(View.GONE);
        }
        adjustTitleTxt.setText(generalFunc.retrieveLangLBl("Adjust in Your trip", "LBL_ADJUST_OUT_AMT_DELIVERY_TXT"));


        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash-Card") ||
                    APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
                cardArea.setVisibility(View.VISIBLE);

            }
        } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

            cardtitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
            cardArea.setVisibility(View.VISIBLE);
        }

        if (generalFunc.isRTLmode()) {
            ((ImageView) dialogView.findViewById(R.id.cardimagearrow)).setRotationY(180);
            ((ImageView) dialogView.findViewById(R.id.adjustimagearrow)).setRotationY(180);
        }

        cardArea.setOnClickListener(v -> {
            outstanding_dialog.dismiss();

            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                checkCardConfig(true, isReqNow);
            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

                callOutStandingPayAmout(isReqNow);

            }
        });

        adjustarea.setOnClickListener(v -> {

            if (cardArea.getVisibility() == View.GONE) {
                outstanding_dialog.dismiss();
            }

            if (!selectedPayTypeName.equalsIgnoreCase(Payment_Type_1) && !cardRadioBtn.isChecked()) {
                generalFunc.showGeneralMessage("",
                        generalFunc.retrieveLangLBl("", "LBL_MULTI_OUTSTANDING_DECILENED_MSG"));
                return;
            }

            if (isReqNow) {
                isOutStandingDailogShow = true;
                //btn_type2.performClick();
                checkDetails();
            }
        });


        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        int submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        btn_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ridenowclick = false;
                outstanding_dialog.dismiss();
            }
        });

        builder.setView(dialogView);
        outstanding_dialog = builder.create();
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(outstanding_dialog);
        }
        outstanding_dialog.setCancelable(false);
        outstanding_dialog.show();
    }

    public void checkCardConfig(boolean isOutstanding, boolean isReqNow) {
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                OpenCardPaymentAct(true);
            } else {
                showPaymentBox(isOutstanding, isReqNow);
            }
        }
    }

    public void showPaymentBox(boolean isOutstanding, boolean isReqNow) {
        androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.input_box_view, null);
        builder.setView(dialogView);

        final MaterialEditText input = (MaterialEditText) dialogView.findViewById(R.id.editBox);
        final MTextView subTitleTxt = (MTextView) dialogView.findViewById(R.id.subTitleTxt);

        Utils.removeInput(input);

        subTitleTxt.setVisibility(View.VISIBLE);
        subTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TITLE_PAYMENT_ALERT"));
        input.setText(generalFunc.getJsonValue("vCreditCard", userProfileJson));

        builder.setPositiveButton(generalFunc.retrieveLangLBl("Confirm", "LBL_BTN_TRIP_CANCEL_CONFIRM_TXT"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (isOutstanding) {
                    callOutStandingPayAmout(isReqNow);
                } else {
                    checkPaymentCard();
                }
            }
        });

        builder.setNeutralButton(generalFunc.retrieveLangLBl("Change", "LBL_CHANGE"), (dialog, which) -> {
            dialog.cancel();
            OpenCardPaymentAct(true);
        });
        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> dialog.cancel());


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void getOutStandingAmout(boolean isReqNow) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getOutstandingAmount");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setCancelAble(false);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            boolean isDataAvail = false;
            String message = "";

            if (responseString != null && !responseString.equals("")) {
                isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                message = generalFunc.getJsonValue(Utils.message_str, responseString);

                checkOutStandingAmount(isReqNow);

            } else {
                checkOutStandingAmount(isReqNow);
            }
        });
        exeWebServer.execute();

    }

    public void checkOutStandingAmount(boolean isReqNow) {
        String fOutStandingAmount = "";
        if (Utils.checkText(responseStr)) {
            fOutStandingAmount = generalFunc.getJsonValue("fOutStandingAmount", responseStr);
        }
        boolean isDataAvail = GeneralFunctions.parseDoubleValue(0.0, fOutStandingAmount) > 0;

        if (!isOutStandingDailogShow && isDataAvail) {
            outstandingDialog(isReqNow, responseStr);
        } else {
            isOutStandingDailogShow = true;
            checkDetails();
        }
    }


    public void callOutStandingPayAmout(boolean isReqNow) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "ChargePassengerOutstandingAmount");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setCancelAble(false);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                    userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {

                        isOutStandingDailogShow = true;
                        checkDetails();
                    });
                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    generateAlert.showAlertBox();
                } else {
                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {
                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), button_Id -> {
                            if (button_Id == 1) {

                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else {

                            }
                        });


                    }
                }
            } else {
                if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_OUTSTANDING_AMOUT_ALREADY_PAID_TXT")) {
                    String message = generalFunc.getJsonValue(Utils.message_str_one, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                    userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

                    if (generalFunc.getJsonValue("fOutStandingAmount", userProfileJson) != null &&
                            GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("fOutStandingAmount", userProfileJson)) > 0) {
                        isOutStandingDailogShow = false;
                    } else {
                        isOutStandingDailogShow = true;
                    }

                    // btn_type2.performClick();
                    checkDetails();

                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            }
        });
        exeWebServer.execute();

    }

    public void OpenCardPaymentAct(boolean fromcabselection) {
        Bundle bn = new Bundle();
        bn.putBoolean("fromFareSummery", fromcabselection);
        new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
    }

    // Add Payment Layout
    private void buildPaymentAdapter() {

        if (!cashRadioBtn.isChecked()) {
            paymentArea.setVisibility(View.GONE);
        } else {
            paymentArea.setVisibility(View.VISIBLE);

        }
    }

    //get fare estimate
    private void callFareDetailsRequest(boolean removePromo) {
        btn_type2.setEnabled(false);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("type", "getEstimateFareDetailsArr");
        parameters.put("iUserId", "" + generalFunc.getMemberId());
        parameters.put("distance", "" + mapData.get("distance"));
        parameters.put("time", "" + mapData.get("time"));
        parameters.put("SelectedCar", "" + selectedcar);

        if (removePromo) {
            parameters.put("PromoCode", "");
        } else {
            parameters.put("PromoCode", "" + appliedPromoCode);
        }

        parameters.put("details_arr", ja);
        parameters.put("userType", Utils.userType);
        parameters.put("eType", Utils.eType_Multi_Delivery);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters, true);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            responseStr = responseString;
            if (responseString != null && !responseString.equals("")) {

                btn_type2.setEnabled(true);
                PromoCodearea.setVisibility(View.VISIBLE);
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    if (removePromo) {
                        appliedPromoCode = "";
                        defaultPromoView();
                    }

                    reSetAdapter(cardRadioBtn.isChecked());

                    (findViewById(R.id.fareDetailArea)).setVisibility(View.VISIBLE);
                    JSONArray FareDetailsArrNewObj = null;

                    FareDetailsArrNewObj = generalFunc.getJsonArray(Utils.message_str, responseString);

                    // Store Rounded Distance and Time
                    mapData.put("distance_multi", generalFunc.getJsonValue("distance_multi", responseString));
                    mapData.put("time_multi", generalFunc.getJsonValue("time_multi", responseString));


                    currencySymbol = generalFunc.getJsonValue("vSymbol", responseString);
                    totalFare = generalFunc.parseDoubleValue(0.00, generalFunc.getJsonValue("total_fare_amount", responseString));

                    carTypeImg.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(generalFunc.getJsonValue("vehicleImage", responseString))
                            .placeholder(R.mipmap.ic_no_pic_user)
                            .error(R.mipmap.ic_no_pic_user)
                            .into(carTypeImg);

                    addFareDetailLayout(FareDetailsArrNewObj);
                } else {
                    btn_type2.setEnabled(false);
                    PromoCodearea.setVisibility(View.GONE);
                    (findViewById(R.id.fareDetailArea)).setVisibility(View.GONE);
                }

            } else {
                generateErrorView("", "callFareDetailsRequest", removePromo);
            }
        });
        exeWebServer.execute();

    }

    // Add Fare Detail Layout
    private void addFareDetailLayout(JSONArray jobjArray) {

        if (fareDetailDisplayArea.getChildCount() > 0) {
            fareDetailDisplayArea.removeAllViewsInLayout();
        }


        for (int i = 0; i < jobjArray.length(); i++) {
            JSONObject jobject = generalFunc.getJsonObject(jobjArray, i);
            try {
                String data = jobject.names().getString(0);

                if (i == 0) {
                    ((MTextView) findViewById(R.id.cartypeTxt)).setText(jobject.get(data).toString());
                    ((MTextView) findViewById(R.id.carTypeName)).setText(jobject.get(data).toString());
                    ((MTextView) findViewById(R.id.pickupAddressTxtView)).setText(getIntent().getStringExtra("pickUpLocAddress"));


                } else {
                    addFareDetailRow(data, jobject.get(data).toString(), (jobjArray.length() - 1) == i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // Add Fare Detail Layout
    private void addFareDetailRow(String row_name, String row_value, boolean isLast) {


        View convertView = null;
        if (row_name.equalsIgnoreCase("eDisplaySeperator")) {
            convertView = new View(getActContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(getActContext(), 1));
            int dip = Utils.dipToPixels(getActContext(), 10);
            params.setMarginStart(dip);
            params.setMarginEnd(dip);
            convertView.setBackgroundColor(Color.parseColor("#dedede"));
            convertView.setLayoutParams(params);
        } else {
            LayoutInflater infalInflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.design_fare_breakdown_row, null);

            convertView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        convertView.setMinimumHeight(Utils.dipToPixels(getActContext(), 40));

            MTextView titleHTxt = (MTextView) convertView.findViewById(R.id.titleHTxt);
            MTextView titleVTxt = (MTextView) convertView.findViewById(R.id.titleVTxt);

            titleHTxt.setText(generalFunc.convertNumberWithRTL(row_name));
            titleVTxt.setText(generalFunc.convertNumberWithRTL(row_value));


            if (isLast) {
                // CALCULATE individual fare & show
                try {
                    double indiFare = (totalFare / recipientList.size());
                    individualFare = "" + new BigDecimal(indiFare).setScale(2,
                            BigDecimal.ROUND_HALF_UP);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                convertView.setMinimumHeight(Utils.dipToPixels(getActContext(), 40));

                titleHTxt.setTextColor(getResources().getColor(R.color.black));
                titleHTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Poppins_SemiBold.ttf");
                titleHTxt.setTypeface(face);
                titleVTxt.setTypeface(face);
                titleVTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                titleVTxt.setTextColor(getResources().getColor(R.color.appThemeColor_1));


                buildPaymentAdapter();

                payTypeAdapter = new MultiPaymentTypeRecyclerAdapter(MultiDeliveryThirdPhaseActivity.this, generalFunc, recipientList, paymentTypeList, getActContext());
                payTypeAdapter.setOnTypeSelectListener(this);
                paymentMethodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                paymentMethodRecyclerView.setAdapter(payTypeAdapter);
                payTypeAdapter.notifyDataSetChanged();
            }


        }

        if (convertView != null)
            fareDetailDisplayArea.addView(convertView);
    }


    private Context getActContext() {
        return MultiDeliveryThirdPhaseActivity.this;
    }

    private void setSelected(String selected) {

        if (selected.equalsIgnoreCase("3")) {
//            Typeface selectedFont = Typeface.createFromAsset(getActContext().getAssets(), "fonts/Poppins_SemiBold.ttf");
//            Typeface normalFont = Typeface.createFromAsset(getActContext().getAssets(), "fonts/Poppins_Medium.ttf");


            ((MTextView) findViewById(R.id.tv1)).setTextColor(getResources().getColor(R.color.black));
            ((LinearLayout) findViewById(R.id.tabArea1)).setBackground(getResources().getDrawable(R.drawable.tab_background));
            ((MTextView) findViewById(R.id.tv1)).setText(generalFunc.retrieveLangLBl("", "LBL_MULTI_VEHICLE_TYPE"));
//            ((MTextView) findViewById(R.id.tv1)).setTypeface(normalFont);

            ((LinearLayout) findViewById(R.id.tabArea2)).setBackground(getResources().getDrawable(R.drawable.tab_background));
            ((MTextView) findViewById(R.id.tv2)).setTextColor(getResources().getColor(R.color.black));
            ((MTextView) findViewById(R.id.tv2)).setText(generalFunc.retrieveLangLBl("", "LBL_MULTI_ROUTE"));
//            ((MTextView) findViewById(R.id.tv2)).setTypeface(normalFont);

            ((LinearLayout) findViewById(R.id.tabArea3)).setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
            ((MTextView) findViewById(R.id.tv3)).setTextColor(getResources().getColor(R.color.appThemeColor_TXT_1));
            ((MTextView) findViewById(R.id.tv3)).setText(generalFunc.retrieveLangLBl("", "LBL_MULTI_PRICE"));
//            ((MTextView) findViewById(R.id.tv3)).setTypeface(selectedFont);

            findViewById(R.id.headerArea).setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            JSONObject obj_userProfile = generalFunc.getJsonObject(userProfileJson);
            this.userProfileJson = userProfileJson;
//            isCardValidated = true;
//            btn_type2.performClick();
        } else if (requestCode == Utils.SELECT_COUPON_REQ_CODE && resultCode == RESULT_OK) {
            String couponCode = data.getStringExtra("CouponCode");
            if (couponCode == null) {
                couponCode = "";
            }
            appliedPromoCode = couponCode;
            setPromoCode();
        } else if (requestCode == Utils.SELECT_ORGANIZATION_PAYMENT_CODE) {

            if (resultCode == RESULT_OK) {
                if (data.getSerializableExtra("data").equals("")) {


                    if (data.getBooleanExtra("isCash", false)) {
                        isCashSelected = true;
                    } else {
                        isCashSelected = false;
                    }
                    if (data.getBooleanExtra("isWallet", false)) {
                        iswalletSelect = true;

                    } else {
                        iswalletSelect = false;

                    }

                    checkDetails();


                }

            }
        }
    }


    public void setPromoCode() {
        if (Utils.checkText(appliedPromoCode)) {
            appliedPromoView();
        } else {
            callFareDetailsRequest(false);
        }
    }

    public void defaultPromoView() {
        promocodeArea.setVisibility(View.GONE);
        appliedPromoHTxtView.setVisibility(View.GONE);

        couponCodeCloseImgView.setVisibility(View.GONE);
        couponCodeImgView.setVisibility(View.VISIBLE);
        applyCouponHTxt.setTextColor(Color.parseColor("#333333"));
        applyCouponHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_APPLY_COUPON"));
        promocodeappliedHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_APPLIED_COUPON_CODE"));
    }

    public void appliedPromoView() {
        appliedPromoHTxtView.setVisibility(View.VISIBLE);
        applyCouponHTxt.setText(appliedPromoCode);
        applyCouponHTxt.setTextColor(getResources().getColor(R.color.appThemeColor_1));
        couponCodeCloseImgView.setVisibility(View.VISIBLE);
        couponCodeImgView.setVisibility(View.GONE);
        couponCodeCloseImgView.setOnClickListener(new setOnClickList());
        appliedPromoHTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_APPLIED_COUPON_CODE"));
        callFareDetailsRequest(false);
    }
}
