package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.dialogs.RequestNearestCab;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.general.files.SuccessDialog;
import com.general.files.UpdateFrequentTask;
import com.realmModel.CarWashCartData;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.editBox.MaterialEditText;
import com.view.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class CarWashBookingDetailsActivity extends AppCompatActivity {

    private static final int ADD_ADDRESS_REQ_CODE = 165;
    private static final int SEL_ADDRESS_REQ_CODE = 155;

    ImageView backImgView;
    MTextView titleTxt;
    GeneralFunctions generalFunc;
    LinearLayout itemContainer, itemChargeContainer;
    MTextView subtotalHTxt, subtotalVTxt;
    MTextView serviceTxt, driverNameTxt, bookDateHTxt, bookDateVTxt, booktimeHTxt, booktimeVTxt;
    View couponCodeArea;
    MTextView applyCouponHTxt;
    LinearLayout promocodeArea;
    MTextView promocodeappliedHTxt, promocodeappliedVTxt, appliedPromoHTxtView;
    ImageView couponCodeImgView, couponCodeCloseImgView;
    private String appliedPromoCode = "";
    ArrayList<String> iVehicleTypeIdList;
    ArrayList<String> fVehicleTypeQtyList;
    LinearLayout paymentArea, addressArea, locationArea;
    MTextView paymentHTxt, changePaymentTxt, locationHTxt;
    String userProfileJson;
    ImageView payImgView;
    MTextView payTypeTxt;
    boolean iscash = true;
    public boolean isCardValidated = false;
    public MButton btn_type2_now, btn_type_later;
    MTextView selLocTxt;
    boolean isUserLocation = true;
    String totalAddressCount = "";
    String iUserAddressId = "";
    String iTempUserId = "";
    String vServiceAddress = "";
    MTextView userAddressTxt;
    public RequestNearestCab requestNearestCab;
    GenerateAlertBox reqSentErrorDialog = null;
    String bookingtype = Utils.CabReqType_Now;
    String SelectDate = "", sdate = "", Stime = "";
    boolean eWalletDebitAllow = false;
    androidx.appcompat.app.AlertDialog outstanding_dialog;

    View containerView;
    View mProgressBar;

    boolean clickable = false;
    boolean isPayNow = false;
    LinearLayout btn_type2Area, reschedulearea;

    String provider_away_str = "";
    UpdateFrequentTask allCabRequestTask;
    androidx.appcompat.app.AlertDialog alertDialog_surgeConfirm;
    RealmResults<CarWashCartData> realmCartList;
    public String eWalletIgnore = "No";
    public String ePaymentBy = "Passenger";

    String LBL_CHANGE = "";
    String SYSTEM_PAYMENT_FLOW = "";
    String APP_PAYMENT_METHOD = "";
    String APP_PAYMENT_MODE = "";
    int pos;

    MTextView serviceHTxt, chargeHTxt;
    RadioButton userLocRadioBtn, providerLocRadioBtn;
    View btnView;
    LinearLayout AddAddressArea, EditAddressArea;
    MTextView addAddressHTxt, addAddressBtn, changeBtn, seldriverNameTxt;
    SelectableRoundedImageView driverImg;
    SimpleRatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_booking_details);

        generalFunc = new GeneralFunctions(getActContext());

        getUserProfileJson();

        driverImg = (SelectableRoundedImageView) findViewById(R.id.driverImg);
        ratingBar = (SimpleRatingBar) findViewById(R.id.ratingBar);
        seldriverNameTxt = (MTextView) findViewById(R.id.seldriverNameTxt);
        btnView = (View) findViewById(R.id.btnView);
        userLocRadioBtn = (RadioButton) findViewById(R.id.userLocRadioBtn);
        providerLocRadioBtn = (RadioButton) findViewById(R.id.providerLocRadioBtn);
        serviceHTxt = (MTextView) findViewById(R.id.serviceHTxt);
        chargeHTxt = (MTextView) findViewById(R.id.chargeHTxt);
        itemContainer = (LinearLayout) findViewById(R.id.itemContainer);
        itemChargeContainer = (LinearLayout) findViewById(R.id.itemChargeContainer);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        subtotalHTxt = (MTextView) findViewById(R.id.subtotalHTxt);
        subtotalVTxt = (MTextView) findViewById(R.id.subtotalVTxt);
        serviceTxt = (MTextView) findViewById(R.id.serviceTxt);
        driverNameTxt = (MTextView) findViewById(R.id.driverNameTxt);
        bookDateHTxt = (MTextView) findViewById(R.id.bookDateHTxt);
        bookDateVTxt = (MTextView) findViewById(R.id.bookDateVTxt);
        booktimeHTxt = (MTextView) findViewById(R.id.booktimeHTxt);
        booktimeVTxt = (MTextView) findViewById(R.id.booktimeVTxt);
        applyCouponHTxt = (MTextView) findViewById(R.id.applyCouponHTxt);
        paymentHTxt = (MTextView) findViewById(R.id.paymentHTxt);
        locationHTxt = (MTextView) findViewById(R.id.locationHTxt);
        changePaymentTxt = (MTextView) findViewById(R.id.changePaymentTxt);
        paymentArea = (LinearLayout) findViewById(R.id.paymentArea);
        addressArea = (LinearLayout) findViewById(R.id.addressArea);
        locationArea = (LinearLayout) findViewById(R.id.locationArea);
        couponCodeArea = findViewById(R.id.couponCodeArea);
        couponCodeArea.setOnClickListener(new setOnClickList());
        promocodeArea = (LinearLayout) findViewById(R.id.promocodeArea);
        promocodeappliedHTxt = (MTextView) findViewById(R.id.promocodeappliedHTxt);
        promocodeappliedVTxt = (MTextView) findViewById(R.id.promocodeappliedVTxt);
        appliedPromoHTxtView = (MTextView) findViewById(R.id.appliedPromoHTxtView);
        couponCodeImgView = (ImageView) findViewById(R.id.couponCodeImgView);
        couponCodeCloseImgView = (ImageView) findViewById(R.id.couponCodeCloseImgView);
        userAddressTxt = (MTextView) findViewById(R.id.userAddressTxt);

        btn_type2Area = (LinearLayout) findViewById(R.id.btn_type2Area);
        reschedulearea = (LinearLayout) findViewById(R.id.reschedulearea);
        AddAddressArea = (LinearLayout) findViewById(R.id.AddAddressArea);
        EditAddressArea = (LinearLayout) findViewById(R.id.EditAddressArea);
        addAddressHTxt = (MTextView) findViewById(R.id.addAddressHTxt);
        addAddressBtn = (MTextView) findViewById(R.id.addAddressBtn);
        changeBtn = (MTextView) findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new setOnClickList());

        containerView = findViewById(R.id.containerView);
        mProgressBar = findViewById(R.id.mProgressBar);

        btn_type2Area.setVisibility(View.VISIBLE);
        userAddressTxt.setVisibility(View.VISIBLE);

        payImgView = (ImageView) findViewById(R.id.payImgView);
        payTypeTxt = (MTextView) findViewById(R.id.payTypeTxt);
        selLocTxt = (MTextView) findViewById(R.id.selLocTxt);


        changeBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        addAddressBtn.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_ADDRESS_TXT"));
        addAddressHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_SERVICE_LOCATION"));
        userLocRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_AT_USER_LOCATION"));
        providerLocRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_AT_PROVIDER_LOCATION"));
        serviceHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SERVICES"));
        chargeHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHARGES_TXT"));
        selLocTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SERVICE_LOCATION"));
        btn_type2_now = ((MaterialRippleLayout) findViewById(R.id.btn_type2_now)).getChildView();
        btn_type_later = ((MaterialRippleLayout) findViewById(R.id.btn_type_later)).getChildView();
        btn_type2_now.setOnClickListener(new setOnClickList());
        btn_type_later.setOnClickListener(new setOnClickList());
        addAddressBtn.setOnClickListener(new setOnClickList());

        appliedPromoHTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_APPLIED_COUPON_CODE"));
        btn_type2_now.setText(generalFunc.retrieveLangLBl("", "LBL_BOOK_NOW"));
        btn_type_later.setText(generalFunc.retrieveLangLBl("", "LBL_BOOK_LATER"));
        locationHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_BOOKING_LOCATION"));

        subtotalHTxt.setText(generalFunc.retrieveLangLBl("SubTotal", "LBL_SUBTOTAL_TXT"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BOOKING_DETAILS_TXT"));
        bookDateHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BOOKING_DATE"));
        booktimeHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BOOKING_TIME"));
        applyCouponHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_APPLY_COUPON"));
        paymentHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_PAY_MODE"));
        LBL_CHANGE = generalFunc.retrieveLangLBl("", "LBL_CHANGE");
        changePaymentTxt.setText(LBL_CHANGE);
        payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
        changePaymentTxt.setVisibility(View.GONE);
        changePaymentTxt.setOnClickListener(new setOnClickList());

        driverNameTxt.setText(getIntent().getStringExtra("name"));
        ratingBar.setRating(GeneralFunctions.parseFloatValue(0, getIntent().getStringExtra("average_rating")));


        String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + getIntent().getStringExtra("iDriverId") + "/" + getIntent().getStringExtra("driver_img");


        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_no_pic_user)
                .error(R.mipmap.ic_no_pic_user)
                .into(driverImg);

        seldriverNameTxt.setText(getIntent().getStringExtra("name"));
        serviceTxt.setText(getIntent().getStringExtra("serviceName"));
        booktimeVTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NOW"));

        if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash-Card")) {
            changePaymentTxt.setVisibility(View.VISIBLE);

        } else if (APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
            iscash = false;

            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

                payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));
                payImgView.setImageResource(R.mipmap.ic_card_new);
            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

                payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
                payImgView.setImageResource(R.mipmap.ic_menu_wallet);

            }
        }


        if (isUserLocation) {
            userLocRadioBtn.setChecked(true);
        } else {
            providerLocRadioBtn.setChecked(true);
        }

        userLocRadioBtn.setOnClickListener(view -> {
            isUserLocation = true;
            locationArea.setVisibility(View.VISIBLE);
            setLocationView();
            ((ScrollView) findViewById(R.id.scrollView)).post(() -> ((ScrollView) findViewById(R.id.scrollView)).fullScroll(ScrollView.FOCUS_DOWN));
        });

        providerLocRadioBtn.setOnClickListener(view -> {
            locationArea.setVisibility(View.GONE);
            isUserLocation = false;
            setLocationView();


        });


        bookDateVTxt.setText(generalFunc.getCurrentdate());
        realmCartList = getCartData();
        if (realmCartList != null && realmCartList.size() > 0) {
            for (int i = 0; i < realmCartList.size(); i++) {
                iVehicleTypeIdList.add(realmCartList.get(i).getCategoryListItem().getiVehicleTypeId());
                fVehicleTypeQtyList.add(realmCartList.get(i).getItemCount());
            }
        }

        if (generalFunc.isRTLmode()) {
            couponCodeImgView.setRotation(180);
        }

        double distance = Utils.CalculationByLocation(GeneralFunctions.parseDoubleValue(0.0, getIntent().getStringExtra("latitude")), GeneralFunctions.parseDoubleValue(0.0, getIntent().getStringExtra("longitude")), GeneralFunctions.parseDoubleValue(0.0, getIntent().getStringExtra("vProviderLatitude")), GeneralFunctions.parseDoubleValue(0.0, getIntent().getStringExtra("vProviderLongitude")), "");

        if (generalFunc.getJsonValue("eUnit", userProfileJson).equals("KMs")) {
            provider_away_str = String.format("%.2f", (float) distance) + " " + generalFunc.retrieveLangLBl("", "LBL_KM_DISTANCE_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_AWAY");
        } else {
            provider_away_str = String.format("%.2f", (float) (distance * 0.621371)) + " " + generalFunc.retrieveLangLBl("", "LBL_MILE_DISTANCE_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_AWAY");
        }

        getDetails();
    }

    private void getUserProfileJson() {
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        APP_PAYMENT_MODE = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson);
        SYSTEM_PAYMENT_FLOW = generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson);
        APP_PAYMENT_METHOD = generalFunc.getJsonValue("APP_PAYMENT_METHOD", userProfileJson);
    }

    public void setPromoCode() {
        if (appliedPromoCode.equalsIgnoreCase("")) {
            defaultPromoView();
        } else {
            appliedPromoView();
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
        couponCodeCloseImgView.setOnClickListener(new setOnClickList());
        couponCodeCloseImgView.setVisibility(View.VISIBLE);
        couponCodeImgView.setVisibility(View.GONE);
        appliedPromoHTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_APPLIED_COUPON_CODE"));
    }

    public Context getActContext() {
        return CarWashBookingDetailsActivity.this;
    }

    public void addChargesView(JSONArray chargeArray) {
        if (chargeArray == null || chargeArray.length() == 0) {
            return;
        }
        if (itemChargeContainer.getChildCount() > 0) {
            itemChargeContainer.removeAllViewsInLayout();
        }


        for (int i = 0; i < chargeArray.length(); i++) {
            JSONObject data = generalFunc.getJsonObject(chargeArray, i);
            pos = i;

            LayoutInflater topinginflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View editCartView = topinginflater.inflate(R.layout.item_charge, null);
            MTextView titleTxt = (MTextView) editCartView.findViewById(R.id.titleTxt);
            MTextView amountTxt = (MTextView) editCartView.findViewById(R.id.amountTxt);

            titleTxt.setText(generalFunc.getJsonValueStr("Title", data));
            amountTxt.setText(generalFunc.getJsonValueStr("Amount", data));


            itemChargeContainer.addView(editCartView);

            if (chargeArray.length() > 1 && chargeArray.length() - 1 == i) {
                titleTxt.setTextColor(getResources().getColor(R.color.black));
                titleTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Poppins_SemiBold.ttf");
                titleTxt.setTypeface(face);
                amountTxt.setTypeface(face);
                amountTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                amountTxt.setTextColor(getResources().getColor(R.color.appThemeColor_1));
            }
        }

    }

    public void addItemView(JSONArray itemArray) {


        if (itemArray == null || itemArray.length() == 0) {
            return;
        }


        if (itemContainer.getChildCount() > 0) {
            itemContainer.removeAllViewsInLayout();
        }


        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject data = generalFunc.getJsonObject(itemArray, i);
            pos = i;

            LayoutInflater topinginflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View editCartView = topinginflater.inflate(R.layout.item_uberxcheckout_row, null);
            MTextView itemNameTxtView = (MTextView) editCartView.findViewById(R.id.itemNameTxtView);
            itemNameTxtView.setSelected(true);
            LinearLayout mainView = (LinearLayout) editCartView.findViewById(R.id.mainView);
            MTextView itemMenuNameTxtView = (MTextView) editCartView.findViewById(R.id.itemMenuNameTxtView);
            itemMenuNameTxtView.setSelected(true);
            MTextView itemPriceTxtView = (MTextView) editCartView.findViewById(R.id.itemPriceTxtView);
            MTextView itemstrikePriceTxtView = (MTextView) editCartView.findViewById(R.id.itemstrikePriceTxtView);
            MTextView QTYNumberTxtView = (MTextView) editCartView.findViewById(R.id.QTYNumberTxtView);
            ImageView cancelImg = (ImageView) editCartView.findViewById(R.id.cancelImg);
            MTextView hourTxt = (MTextView) editCartView.findViewById(R.id.hourTxt);
            LinearLayout layoutShape = (LinearLayout) editCartView.findViewById(R.id.layoutShape);
            itemPriceTxtView.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("Amount", data)));
            itemstrikePriceTxtView.setVisibility(View.GONE);
            itemNameTxtView.setText(generalFunc.getJsonValueStr("Title", data));
            itemMenuNameTxtView.setVisibility(View.GONE);
            hourTxt.setText("/" + generalFunc.retrieveLangLBl("", "LBL_HOUR_TXT"));


            if (generalFunc.isRTLmode()) {
                layoutShape.setBackgroundResource(R.drawable.ic_shape_rtl);
            }

            if (generalFunc.getJsonValueStr("Amount", data).equalsIgnoreCase("")) {
                itemPriceTxtView.setVisibility(View.GONE);
                hourTxt.setVisibility(View.GONE);

            }

            if (generalFunc.getJsonValueStr("eFareType", data).equalsIgnoreCase("Hourly")) {
                hourTxt.setVisibility(View.VISIBLE);
            } else {
                hourTxt.setVisibility(View.GONE);
            }

            QTYNumberTxtView.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("Qty", data)));

            cancelImg.setTag(i);

            cancelImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {


                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(btn_id -> {
                            generateAlert.closeAlertBox();
                            if (btn_id == 1) {
                                Realm realm = MyApp.getRealmInstance();
                                realm.beginTransaction();
                                //CarWashCartData categoryListItem = realm.where(CarWashCartData.class).equalTo("iVehicleCategoryId", realmCartList.get((Integer) cancelImg.getTag()).getCategoryListItem().getiVehicleTypeId()).findFirst();
                                CarWashCartData carWashCartData = realmCartList.get((Integer) cancelImg.getTag());
                                carWashCartData.deleteFromRealm();
                                realm.commitTransaction();

                                realmCartList = getCartData();


                                if (realmCartList.size() == 0) {
                                    onBackPressed();
                                }
                                getDetails();

                            }
                        });
                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Are you sure want to delete", "LBL_DELETE_CONFIRM_MSG"));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("cANCEL", "LBL_CANCEL_TXT"));
                        generateAlert.showAlertBox();


                    } catch (Exception e) {
                        Logger.e("TestCrash", "::" + e.toString());
                    }

                }
            });

//            if (itemArray.length() > 1 && itemArray.length() - 1 == i) {
//                itemNameTxtView.setTextColor(getResources().getColor(R.color.appThemeColor_1));
//                itemPriceTxtView.setTextColor(getResources().getColor(R.color.appThemeColor_1));
//            }

            itemContainer.addView(editCartView);
        }
    }


    public void getDetails() {

        containerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        JSONArray orderedItemArr = new JSONArray();
        if (realmCartList != null && realmCartList.size() > 0) {
            try {
                for (int i = 0; i < realmCartList.size(); i++) {
                    CarWashCartData posData = realmCartList.get(i);
                    JSONObject object = new JSONObject();
                    object.put("iVehicleTypeId", posData.getCategoryListItem().getiVehicleTypeId());
                    object.put("fVehicleTypeQty", posData.getItemCount());
                    object.put("tUserComment", URLEncoder.encode(posData.getSpecialInstruction()/*.replace(" ", "%20")*/, "utf8"));
                    orderedItemArr.put(object);
                }
            } catch (Exception e) {

            }
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getVehicleTypeFareDetails");
        parameters.put("SelectedCabType", Utils.CabGeneralType_UberX);
        parameters.put("iVehicleTypeId", android.text.TextUtils.join(",", iVehicleTypeIdList));
        parameters.put("fVehicleTypeQty", android.text.TextUtils.join(",", fVehicleTypeQtyList));
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("vCouponCode", appliedPromoCode.trim());
        parameters.put("OrderDetails", orderedItemArr.toString());
        parameters.put("iDriverId", getIntent().getStringExtra("iDriverId"));
        parameters.put("vSelectedLatitude", getIntent().getStringExtra("latitude"));
        parameters.put("vSelectedLongitude", getIntent().getStringExtra("longitude"));
        parameters.put("vSelectedAddress", getIntent().getStringExtra("address"));
        parameters.put("iUserAddressId", iUserAddressId);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setCancelAble(false);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                if (isDataAvail) {

                    if (generalFunc.getJsonValue("eEnableServiceAtProviderLocation", responseString).equalsIgnoreCase("Yes")) {
                        addressArea.setVisibility(View.VISIBLE);
                    } else {
                        addressArea.setVisibility(View.GONE);
                    }

                    if (generalFunc.getJsonValue("vAvailability", responseString).equalsIgnoreCase("No")) {
                        btn_type2Area.setVisibility(View.GONE);

                    } else {
                        btn_type2Area.setVisibility(View.VISIBLE);

                    }
                    if (generalFunc.getJsonValue("vScheduleAvailability", responseString).equalsIgnoreCase("No")) {

                        reschedulearea.setVisibility(View.GONE);

                    } else {
                        reschedulearea.setVisibility(View.VISIBLE);
                    }

                    if (btn_type2Area.getVisibility() == View.VISIBLE && btnView.getVisibility() == View.VISIBLE) {
                        btnView.setVisibility(View.VISIBLE);
                    } else {
                        btnView.setVisibility(View.GONE);
                    }

                    totalAddressCount = generalFunc.getJsonValue("totalAddressCount", responseString);
                    vServiceAddress = generalFunc.getJsonValue("vServiceAddress", responseString);
                    iUserAddressId = generalFunc.getJsonValue("iUserAddressId", responseString);
                    iTempUserId = generalFunc.getJsonValue("iUserAddressId", responseString);

                    userAddressTxt.setText(vServiceAddress);

                    if (GeneralFunctions.parseIntegerValue(0, totalAddressCount) >= 1) {


                        AddAddressArea.setVisibility(View.GONE);
                        EditAddressArea.setVisibility(View.VISIBLE);

                    } else {

                        EditAddressArea.setVisibility(View.GONE);
                        AddAddressArea.setVisibility(View.VISIBLE);
                        int padding = Utils.dipToPixels(getActContext(), 8);


                    }

                    addItemView(generalFunc.getJsonArray("items", responseString));
                    addChargesView(generalFunc.getJsonArray("vehiclePriceTypeArrCubex", responseString));

                    containerView.setVisibility(View.VISIBLE);
                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue("message", responseString)), true);
                }

                mProgressBar.setVisibility(View.GONE);

            } else {
                generalFunc.showError(true);
            }
        });
        exeWebServer.execute();
    }

    public RealmResults<CarWashCartData> getCartData() {
        try {
            iVehicleTypeIdList = new ArrayList<>();
            fVehicleTypeQtyList = new ArrayList<>();
            Realm realm = MyApp.getRealmInstance();
            return realm.where(CarWashCartData.class).findAll();
        } catch (Exception e) {
            Logger.d("RealmException", "::" + e.toString());
        }
        return null;
    }

    public void setCashCardView() {
        if (iscash) {
            isCardValidated = false;
            payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
            payImgView.setImageResource(R.mipmap.ic_cash_new);
        } else {
            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));
                payImgView.setImageResource(R.mipmap.ic_card_new);
            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
                payImgView.setImageResource(R.mipmap.ic_menu_wallet);
            }
        }

    }

    public void setLocationView() {
        if (isUserLocation) {
            userAddressTxt.setVisibility(View.VISIBLE);
            selLocTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SERVICE_LOCATION"));
            userAddressTxt.setText(vServiceAddress);
            iUserAddressId = iTempUserId;
        } else {
            iUserAddressId = "";
            userAddressTxt.setVisibility(View.VISIBLE);

            selLocTxt.setText(generalFunc.retrieveLangLBl("", "LBL_AT_PROVIDER_LOCATION"));
            userAddressTxt.setText(provider_away_str);
        }
    }

    public void openPaymentDailog() {
        final BottomSheetDialog addAddressDailog = new BottomSheetDialog(getActContext());
        View contentView = View.inflate(getActContext(), R.layout.dailog_payment_option, null);

        addAddressDailog.setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                Utils.dpToPx(150, getActContext())));
        addAddressDailog.setCancelable(false);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        mBehavior.setPeekHeight(Utils.dpToPx(150, getActContext()));
        View bottomSheetView = addAddressDailog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);

        MTextView paymentHTxt = (MTextView) bottomSheetView.findViewById(R.id.paymentHTxt);
        MTextView cancelTxt = (MTextView) bottomSheetView.findViewById(R.id.cancelTxt);
        RadioButton cashRadioBtn = (RadioButton) bottomSheetView.findViewById(R.id.cashRadioBtn);
        RadioButton cardRadioBtn = (RadioButton) bottomSheetView.findViewById(R.id.cardRadioBtn);
        if (iscash) {
            cashRadioBtn.setChecked(true);
        } else {
            cardRadioBtn.setChecked(true);
        }

        cashRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
        cardRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));

        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            cardRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));
        } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            cardRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
        }


        cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        paymentHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_PAY_MODE"));


        cashRadioBtn.setOnClickListener(view -> {
            if (cashRadioBtn.isChecked()) {
                iscash = true;
                addAddressDailog.dismiss();
                setCashCardView();
            }
        });

        cardRadioBtn.setOnClickListener(view -> {

            if (cardRadioBtn.isChecked()) {

                if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

                    if (!isCardValidated) {
                        getUserProfileJson();

                        checkCardConfig("");
                        addAddressDailog.dismiss();
                        return;
                    }

                    iscash = false;
                    addAddressDailog.dismiss();
                    setCashCardView();
                } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    getUserProfileJson();
                    iscash = false;
                    addAddressDailog.dismiss();
                    setCashCardView();

                }
            }
        });


        cancelTxt.setOnClickListener(v -> {
            addAddressDailog.dismiss();
        });

        addAddressDailog.show();
    }


    public void checkCardConfig(String paymentType) {
        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                OpenCardPaymentAct();
            } else {
                showPaymentBox(new Intent(), false, paymentType);
            }
        }

    }

    public void OpenCardPaymentAct() {
        Bundle bn = new Bundle();
        new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
    }

    public void showPaymentBox(Intent data, boolean isOutStanding, String paymentType) {
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

        builder.setPositiveButton(generalFunc.retrieveLangLBl("Confirm", "LBL_BTN_TRIP_CANCEL_CONFIRM_TXT"), (dialog, which) -> {
            dialog.cancel();

            if (isOutStanding) {
                callOutStandingPayAmout();
            } else {
                checkPaymentCard(paymentType);
            }
        });

        builder.setNeutralButton(LBL_CHANGE, (dialog, which) -> {
            dialog.cancel();
            OpenCardPaymentAct();
            //ridelaterclick = false;
        });

        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> {
            dialog.cancel();
            //ridelaterclick = false;
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void checkPaymentCard(String paymentType) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "CheckCard");
        parameters.put("iUserId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);
                if (action.equals("1")) {
                    iscash = false;
                    isCardValidated = true;
                    setCashCardView();

                    if (paymentType.equalsIgnoreCase(Utils.CabReqType_Now)) {
                        btn_type2_now.performClick();
                    } else if (paymentType.equalsIgnoreCase(Utils.CabReqType_Later)) {
                        btn_type_later.performClick();
                    }
                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == couponCodeArea.getId()) {
                if (generalFunc.getMemberId().equalsIgnoreCase("")) {
                    generalFunc.showMessage(applyCouponHTxt, generalFunc.retrieveLangLBl("", "LBL_PROMO_CODE_LOGIN_HINT"));
                } else {
                    Bundle bn = new Bundle();
                    bn.putString("CouponCode", appliedPromoCode);
                    bn.putString("eType", Utils.CabGeneralType_UberX);
                    new StartActProcess(getActContext()).startActForResult(CouponActivity.class, bn, Utils.SELECT_COUPON_REQ_CODE);
                }
            } else if (i == R.id.couponCodeCloseImgView) {
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DELETE_CONFIRM_COUPON_MSG"), generalFunc.retrieveLangLBl("", "LBL_NO"), generalFunc.retrieveLangLBl("", "LBL_YES"), buttonId -> {
                    if (buttonId == 1) {
                        appliedPromoCode = "";
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_COUPON_REMOVE_SUCCESS"));
                        getDetails();
                        setPromoCode();
                    }
                });
            } else if (i == R.id.changePaymentTxt) {
                openPaymentDailog();
            } else if (i == R.id.addAddressBtn) {
                Bundle bn = new Bundle();
                bn.putString("iCompanyId", "-1");
                bn.putString("iUserAddressId", iUserAddressId);
                bn.putString("latitude", getIntent().getStringExtra("latitude"));
                bn.putString("longitude", getIntent().getStringExtra("longitude"));
                bn.putString("address", getIntent().getStringExtra("address"));
                new StartActProcess(getActContext()).startActForResult(AddAddressActivity.class, bn, ADD_ADDRESS_REQ_CODE);
            } else if (view == btn_type2_now) {
                if (isUserLocation) {
                    if (iUserAddressId.equals("")) {
                        generalFunc.showMessage(addressArea, generalFunc.retrieveLangLBl("", "LBL_SELECT_ADDRESS_TITLE_TXT"));
                        return;
                    }
                }

                bookingtype = Utils.CabReqType_Now;
                Bundle bn = new Bundle();
                //  new StartActProcess(getActContext()).startActForResult(BusinessSelectPaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);
                bn.putString("isWallet", eWalletDebitAllow ? "Yes" : "No");
                bn.putBoolean("isCash", isCashSelected);
                new StartActProcess(getActContext()).startActForResult(ProfilePaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);

                // getOutStandingAmout("", bookingtype);
            } else if (view == btn_type_later) {
                if (isUserLocation) {
                    if (iUserAddressId.equals("")) {
                        generalFunc.showMessage(addressArea, generalFunc.retrieveLangLBl("", "LBL_SELECT_ADDRESS_TITLE_TXT"));
                        return;
                    }
                }

                if (!iscash && !isCardValidated && SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    if (!isCardValidated) {
                        getUserProfileJson();
                        checkCardConfig(Utils.CabReqType_Later);
                        return;
                    }
                }

                //isRideLater = true;
                bookingtype = Utils.CabReqType_Later;
                Bundle bn = new Bundle();
                //  new StartActProcess(getActContext()).startActForResult(BusinessSelectPaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);
                bn.putString("isWallet", eWalletDebitAllow ? "Yes" : "No");
                bn.putBoolean("isCash", isCashSelected);
                new StartActProcess(getActContext()).startActForResult(ProfilePaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);


//                Bundle bundle = new Bundle();
//                bundle.putString("iDriverId", getIntent().getStringExtra("iDriverId"));
//                new StartActProcess(getActContext()).startActForResult(ScheduleDateSelectActivity.class, bundle, Utils.SCHEDULE_REQUEST_CODE);
            } else if (view == changeBtn) {
                Bundle bn = new Bundle();
                bn.putString("iCompanyId", "-1");
                bn.putString("iUserAddressId", iUserAddressId);
                bn.putString("latitude", getIntent().getStringExtra("latitude"));
                bn.putString("longitude", getIntent().getStringExtra("longitude"));
                bn.putString("address", getIntent().getStringExtra("address"));
                if (GeneralFunctions.parseIntegerValue(0, totalAddressCount) >= 1) {
                    bn.putString("iDriverId", getIntent().getStringExtra("iDriverId"));
                    new StartActProcess(getActContext()).startActForResult(ListAddressActivity.class, bn, SEL_ADDRESS_REQ_CODE);
                } else {
                    new StartActProcess(getActContext()).startActForResult(AddAddressActivity.class, bn, ADD_ADDRESS_REQ_CODE);
                }

            }
        }

    }


    public void getOutStandingAmout(String clicked, String paymentType) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "checkSurgePrice");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);
        parameters.put("SelectedCarTypeID", android.text.TextUtils.join(",", iVehicleTypeIdList));

        if (!SelectDate.trim().equals("")) {
            parameters.put("SelectedTime", SelectDate);
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equals("")) {
                openSurgeConfirmDialog(responseString, paymentType);
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void openSurgeConfirmDialog(String message, String paymentType) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.surge_confirm_design, null);
        builder.setView(dialogView);

        MTextView payableAmountTxt;
        MTextView payableTxt;

        if (generalFunc.getJsonValue("SurgePrice", message) != null && generalFunc.getJsonValue("SurgePrice", message).equalsIgnoreCase("")) {
            checkOutStandingAmount(message, paymentType);
            return;
        }

        ((MTextView) dialogView.findViewById(R.id.headerMsgTxt)).setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, message)));
        ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("SurgePrice", message)));

        ((MTextView) dialogView.findViewById(R.id.tryLaterTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRY_LATER"));

        payableTxt = (MTextView) dialogView.findViewById(R.id.payableTxt);
        payableAmountTxt = (MTextView) dialogView.findViewById(R.id.payableAmountTxt);
        payableTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYABLE_AMOUNT"));

        payableAmountTxt.setVisibility(View.GONE);
        payableTxt.setVisibility(View.VISIBLE);

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_SURGE"));
        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(view -> {
            alertDialog_surgeConfirm.dismiss();
            checkOutStandingAmount(message, paymentType);
        });

        (dialogView.findViewById(R.id.tryLaterTxt)).setOnClickListener(view -> {
            alertDialog_surgeConfirm.dismiss();
        });

        alertDialog_surgeConfirm = builder.create();
        alertDialog_surgeConfirm.setCancelable(false);
        alertDialog_surgeConfirm.setCanceledOnTouchOutside(false);
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog_surgeConfirm);
        }
        alertDialog_surgeConfirm.show();
    }

    public void checkOutStandingAmount(String message, String paymentType) {
        String fOutStandingAmount = "";
        if (Utils.checkText(message)) {
            fOutStandingAmount = generalFunc.getJsonValue("fOutStandingAmount", message);
        }

        boolean isDataAvail = GeneralFunctions.parseDoubleValue(0.0, fOutStandingAmount) > 0;

        if (iscash && isDataAvail) {
            outstandingDialog(message, paymentType);
        } else if (!iscash && isDataAvail) {

            outstandingDialog(message, paymentType);


        } else {

            if (paymentType.equals(Utils.CabReqType_Now)) {
                sendRequestToDrivers();
            } else {
                bookScheduleRide();
            }
        }

    }

    public void outstandingDialog(String data, String paymentType) {
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
        adjustTitleTxt.setText(generalFunc.retrieveLangLBl("Adjust in Your trip", "LBL_ADJUST_OUT_AMT_JOB_TXT"));


        if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash-Card") ||
                APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
            cardArea.setVisibility(View.VISIBLE);

        }


        if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

            cardtitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
            cardArea.setVisibility(View.VISIBLE);
        }

        cardArea.setOnClickListener(v -> {
            outstanding_dialog.dismiss();
            clickable = false;

            isPayNow = true;
            getUserProfileJson();


            // showPaymentBox(new Intent(), true);


            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                showPaymentBox(new Intent(), true, paymentType);
            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                getUserProfileJson();

                callOutStandingPayAmout();


            }


            //}
        });

        adjustarea.setOnClickListener(v -> {
            outstanding_dialog.dismiss();
            clickable = false;
            if (generalFunc.getJsonValue("eWalletBalanceAvailable", userProfileJson).equalsIgnoreCase("Yes")) {
                final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                generateAlert.setCancelable(false);
                generateAlert.setBtnClickList(btn_id -> {
                    if (btn_id == 1) {
                        eWalletDebitAllow = true;

                        if (paymentType.equals(Utils.CabReqType_Now)) {
                            sendRequestToDrivers();
                        } else {
                            bookScheduleRide();
                        }

                    } else {
                        eWalletDebitAllow = false;
                        if (paymentType.equals(Utils.CabReqType_Now)) {
                            sendRequestToDrivers();
                        } else {
                            bookScheduleRide();
                        }
                    }
                });

                generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE_EXIST_JOB").replace("####", generalFunc.getJsonValue("user_available_balance", userProfileJson)));

                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
                generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
                generateAlert.showAlertBox();

            } else {
                eWalletDebitAllow = false;
                if (paymentType.equalsIgnoreCase(Utils.CabReqType_Now)) {
                    sendRequestToDrivers();
                } else {
                    bookScheduleRide();
                }
            }
        });

        if (generalFunc.isRTLmode()) {
            ((ImageView) dialogView.findViewById(R.id.cardimagearrow)).setRotationY(180);
            ((ImageView) dialogView.findViewById(R.id.adjustimagearrow)).setRotationY(180);
        }

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        int submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        btn_type2.setOnClickListener(v -> {
            clickable = false;
            outstanding_dialog.dismiss();
        });

        builder.setView(dialogView);
        outstanding_dialog = builder.create();
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(outstanding_dialog);
        }
        outstanding_dialog.setCancelable(false);
        outstanding_dialog.show();
    }

    public void callOutStandingPayAmout() {

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
                    getUserProfileJson();

                    clickable = false;
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));
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
                    getUserProfileJson();
                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            }

        });
        exeWebServer.execute();

    }


    public void sendRequestToDrivers() {

//        if (!iscash && !isCardValidated && SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
//            if (!isCardValidated) {
//                getUserProfileJson();
//                checkCardConfig(Utils.CabReqType_Now);
//                return;
//            }
//        }

        try {
            if (requestNearestCab == null) {
                requestNearestCab = new RequestNearestCab(getActContext(), generalFunc);
                requestNearestCab.run();
            }
            if (allCabRequestTask != null) {
                allCabRequestTask.stopRepeatingTask();
                allCabRequestTask = null;
            }
            int interval = GeneralFunctions.parseIntegerValue(30, generalFunc.getJsonValue("RIDER_REQUEST_ACCEPT_TIME", generalFunc.retrieveValue(Utils.USER_PROFILE_JSON)));
            allCabRequestTask = new UpdateFrequentTask((interval + 5) * 1000);
            allCabRequestTask.startRepeatingTask();
            allCabRequestTask.setTaskRunListener(() -> {
                setRetryReqBtn(true);
                allCabRequestTask.stopRepeatingTask();
            });
        } catch (Exception e) {

        }

        JSONArray orderedItemArr = new JSONArray();
        if (realmCartList != null && realmCartList.size() > 0) {
            try {
                for (int i = 0; i < realmCartList.size(); i++) {
                    JSONObject object = new JSONObject();
                    object.put("iVehicleTypeId", realmCartList.get(i).getCategoryListItem().getiVehicleTypeId());
                    object.put("fVehicleTypeQty", realmCartList.get(i).getItemCount());
                    object.put("tUserComment", realmCartList.get(i).getSpecialInstruction());
                    orderedItemArr.put(object);
                }
            } catch (Exception e) {

            }
        }

        HashMap<String, Object> requestCabData = new HashMap<String, Object>();
        requestCabData.put("type", "sendRequestToDrivers");
        requestCabData.put("userId", generalFunc.getMemberId());
        requestCabData.put("CashPayment", "" + iscash);
        requestCabData.put("PickUpAddress", vServiceAddress);
        requestCabData.put("eType", Utils.CabGeneralType_UberX);
        requestCabData.put("driverIds", getIntent().getStringExtra("iDriverId"));
        requestCabData.put("eWalletDebitAllow", eWalletDebitAllow ? "Yes" : "No");
        requestCabData.put("eWalletIgnore", eWalletIgnore);
        requestCabData.put("ePaymentBy", ePaymentBy);

        requestCabData.put("PromoCode", appliedPromoCode.trim());

        requestCabData.put("OrderDetails", orderedItemArr.toString());

        if (!iscash && !SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            requestCabData.put("eWalletDebitAllow", "Yes");
            requestCabData.put("ePayWallet", "Yes");
        }

        if (isUserLocation) {
            requestCabData.put("eServiceLocation", "Passenger");
            requestCabData.put("iUserAddressId", iUserAddressId);
        } else {
            requestCabData.put("eServiceLocation", "Driver");
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), requestCabData, true);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken", generalFunc);
        exeWebServer.setCancelAble(false);

        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                generalFunc.sendHeartBeat();

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (!isDataAvail) {
                    String message_str = generalFunc.getJsonValue(Utils.message_str, responseString);
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + message_str);

                    String message = message_str;

                    if (message.equals("SESSION_OUT")) {
                        closeRequestDialog(false);
                        MyApp.getInstance().notifySessionTimeOut();
                        Utils.runGC();
                        return;
                    }
                    if (message.equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);


                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_OK") :
                                generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("NO") ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") :
                                "", button_Id -> {
                            if (button_Id == 1) {

                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                requestNearestCab = null;
                                eWalletDebitAllow = true;
                                eWalletIgnore = "Yes";
                                sendRequestToDrivers();
                            }
                        });

                        return;

                    }


                    if (message_str.equals("DO_EMAIL_PHONE_VERIFY") ||
                            message_str.equals("DO_PHONE_VERIFY") ||
                            message_str.equals("DO_EMAIL_VERIFY")) {
                        closeRequestDialog(true);
                        accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);
                    } else if (!message_str.equalsIgnoreCase("")) {
                        if (generalFunc.getJsonValue("isShowContactUs", responseString) != null && generalFunc.getJsonValue("isShowContactUs", responseString).equalsIgnoreCase("Yes")) {
                            closeRequestDialog(false);
                            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                            generateAlert.setCancelable(false);
                            generateAlert.setBtnClickList(btn_id -> {
                                if (btn_id == 1) {
                                    Intent intent = new Intent(CarWashBookingDetailsActivity.this, ContactUsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                            generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", message_str));
                            generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));
                            generateAlert.showAlertBox();
                        } else {
                            if (message.equals("NO_CARS")) {
                                return;
                            }

                            closeRequestDialog(false);

                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", message_str), "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), null);
                        }
                    } else {
                        closeRequestDialog(false);
                        buildMessage(generalFunc.retrieveLangLBl("", "LBL_REQUEST_FAILED_PROCESS"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), true);
                    }

                }
            } else {
                if (reqSentErrorDialog != null) {
                    reqSentErrorDialog.closeAlertBox();
                    reqSentErrorDialog = null;
                }

                InternetConnection intConnection = new InternetConnection(getActContext());

                reqSentErrorDialog = generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", intConnection.isNetworkConnected() ? "LBL_TRY_AGAIN_TXT" : "LBL_NO_INTERNET_TXT"), generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"), buttonId -> {
                    if (buttonId == 1) {
                        sendRequestToDrivers();
                    } else {
                        closeRequestDialog(true);
                        MyApp.getInstance().restartWithGetDataApp();
                    }
                });
            }
        });
        exeWebServer.execute();

        generalFunc.sendHeartBeat();
    }

    public void showBookingAlert(String message, boolean isongoing) {

        SuccessDialog.showSuccessDialog(getActContext(), generalFunc.retrieveLangLBl("Booking Successful", "LBL_BOOKING_ACCEPTED"), message, isongoing?generalFunc.retrieveLangLBl("", "LBL_VIEW_ON_GOING_TRIPS"):generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS"), generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), false, () -> {
            if (isongoing)
            {
                Bundle bn = new Bundle();
                bn.putBoolean("isRestart", true);
                new StartActProcess(getActContext()).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
            }else
            {
                Bundle bn = new Bundle();
                bn.putBoolean("isrestart", true);
                if (getIntent().getStringExtra("selType") != null) {
                    bn.putString("selType", getIntent().getStringExtra("selType"));
                }
                new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);
            }

        }, () -> {
            Bundle bn = new Bundle();
            new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            finishAffinity();
        });

       /* android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_booking_view, null);
        builder.setView(dialogView);

        final MTextView titleTxt = (MTextView) dialogView.findViewById(R.id.titleTxt);
        final MTextView mesasgeTxt = (MTextView) dialogView.findViewById(R.id.mesasgeTxt);

        titleTxt.setText(generalFunc.retrieveLangLBl("Booking Successful", "LBL_BOOKING_ACCEPTED"));

        mesasgeTxt.setText(message);

        //generalFunc.resetMultiStoredDetails();

        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> {
            dialog.cancel();
            Bundle bn = new Bundle();
            new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            finishAffinity();
        });

        if (isongoing) {
            builder.setPositiveButton(generalFunc.retrieveLangLBl("", "LBL_VIEW_ON_GOING_TRIPS"), (dialog, which) -> {
                //generalFunc.resetMultiStoredDetails();
                dialog.cancel();
                Bundle bn = new Bundle();
                bn.putBoolean("isRestart", true);
                new StartActProcess(getActContext()).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
            });

        } else {
            builder.setPositiveButton(generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS"), (dialog, which) -> {
                dialog.cancel();
                Bundle bn = new Bundle();
                bn.putBoolean("isrestart", true);
                if (getIntent().getStringExtra("selType") != null) {
                    bn.putString("selType", getIntent().getStringExtra("selType"));
                }
                new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);
                finish();
            });
        }


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();*/

    }

    public void pubNubMsgArrived(final String message) {
        try {
            String driverMsg = generalFunc.getJsonValue("Message", message);
            if (driverMsg.equals("CabRequestAccepted")) {
                closeRequestDialog(false);
                Realm realm = MyApp.getRealmInstance();
                realm.beginTransaction();
                realm.delete(CarWashCartData.class);
                realm.commitTransaction();
                showBookingAlert(generalFunc.retrieveLangLBl("", "LBL_ONGOING_TRIP_TXT"), true);
            }
        } catch (Exception e) {

        }
    }


    public void retryReqBtnPressed(String driverIds, String cabRequestedJson) {
        sendRequestToDrivers();
        setRetryReqBtn(false);
    }

    public void setRetryReqBtn(boolean isVisible) {
        if (isVisible) {
            if (requestNearestCab != null) {
                //requestNearestCab.setVisibilityOfRetryArea(View.VISIBLE);
                requestNearestCab.setVisibleBottomArea(View.VISIBLE);
            }
        } else {
            if (requestNearestCab != null) {
                //requestNearestCab.setVisibilityOfRetryArea(View.GONE);
                requestNearestCab.setInVisibleBottomArea(View.GONE);
            }
        }
    }

    public void accountVerificationAlert(String message, final Bundle bn) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            if (btn_id == 1) {
                generateAlert.closeAlertBox();
//                bn.putString("msg", "DO_PHONE_VERIFY");
                (new StartActProcess(getActContext())).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_INFO_REQ_CODE);
            } else if (btn_id == 0) {
                generateAlert.closeAlertBox();
            }
        });
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_CANCEL_TRIP_TXT"));
        generateAlert.showAlertBox();

    }

    public void buildMessage(String message, String positiveBtn, final boolean isRestart) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            generateAlert.closeAlertBox();
            if (isRestart) {
                generalFunc.restartApp();
            }
        });
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(positiveBtn);
        generateAlert.showAlertBox();
    }


    public void closeRequestDialog(boolean isSetDefault) {
        if (requestNearestCab != null) {
            requestNearestCab.dismissDialog();
        }
    }

    public void bookScheduleRide() {

        JSONArray orderedItemArr = new JSONArray();
        if (realmCartList != null && realmCartList.size() > 0) {
            try {
                for (int i = 0; i < realmCartList.size(); i++) {
                    JSONObject object = new JSONObject();
                    object.put("iVehicleTypeId", realmCartList.get(i).getCategoryListItem().getiVehicleTypeId());
                    object.put("fVehicleTypeQty", realmCartList.get(i).getItemCount());
                    object.put("tUserComment", realmCartList.get(i).getSpecialInstruction());
                    orderedItemArr.put(object);
                }
            } catch (Exception e) {

            }
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "ScheduleARide");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("CashPayment", "" + iscash);
        parameters.put("PickUpAddress", vServiceAddress);
        parameters.put("eType", Utils.CabGeneralType_UberX);
        parameters.put("driverIds", getIntent().getStringExtra("iDriverId"));
        parameters.put("eWalletDebitAllow", eWalletDebitAllow ? "Yes" : "No");
        parameters.put("ePaymentBy", ePaymentBy);

        parameters.put("PromoCode", appliedPromoCode.trim());

        parameters.put("OrderDetails", orderedItemArr.toString());

        if (isUserLocation) {
            parameters.put("eServiceLocation", "Passenger");
            parameters.put("iUserAddressId", iUserAddressId);
        } else {
            parameters.put("eServiceLocation", "Driver");
        }

        parameters.put("SelectedDriverId", getIntent().getStringExtra("iDriverId"));
        parameters.put("CashPayment", "" + iscash);
        parameters.put("PromoCode", appliedPromoCode);
        parameters.put("eType", Utils.CabGeneralType_UberX);
        parameters.put("scheduleDate", SelectDate);
        parameters.put("eWalletIgnore", eWalletIgnore);
        if (!iscash && !SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            parameters.put("eWalletDebitAllow", "Yes");
            parameters.put("ePayWallet", "Yes");
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setCancelAble(false);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                String message_str = generalFunc.getJsonValue(Utils.message_str, responseString);

                if (message_str.equals("DO_EMAIL_PHONE_VERIFY") || message_str.equals("DO_PHONE_VERIFY") || message_str.equals("DO_EMAIL_VERIFY")) {
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + message_str);
                    accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);
                    return;
                }

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);

                if (action.equals("1")) {
                    showBookingAlert(generalFunc.retrieveLangLBl("", message_str));
                } else {
                    String message = message_str;
                    if (message.equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);

                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }


                        String LBL_CANCEL_TXT = generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT");
                        String IS_RESTRICT_TO_WALLET_AMOUNT = generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString);

                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, IS_RESTRICT_TO_WALLET_AMOUNT.equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_OK") :
                                LBL_CANCEL_TXT, generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), IS_RESTRICT_TO_WALLET_AMOUNT.equalsIgnoreCase("NO") ? LBL_CANCEL_TXT :
                                "", button_Id -> {
                            if (button_Id == 1) {

                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                if (IS_RESTRICT_TO_WALLET_AMOUNT.equalsIgnoreCase("No")) {
                                    requestNearestCab = null;
                                    eWalletDebitAllow = true;
                                    eWalletIgnore = "Yes";
                                    bookScheduleRide();
                                }
                            }
                        });

                        return;
                    }

                    String isShowContactUs = generalFunc.getJsonValue("isShowContactUs", responseString);
                    if (isShowContactUs != null && isShowContactUs.equalsIgnoreCase("Yes")) {
                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(btn_id -> {
                            if (btn_id == 1) {
                                Intent intent = new Intent(CarWashBookingDetailsActivity.this, ContactUsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", message_str));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));

                        generateAlert.showAlertBox();
                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", message_str));
                    }
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public void showBookingAlert(String message) {

        SuccessDialog.showSuccessDialog(getActContext(), generalFunc.retrieveLangLBl("Booking Successful", "LBL_BOOKING_ACCEPTED"), message,generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS"), generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), false, () -> {

            clearReleamData();

            Bundle bn = new Bundle();
            bn.putBoolean("isrestart", true);
            bn.putString("selType", Utils.CabGeneralType_UberX);
            new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);
            finish();

        }, () -> {

            clearReleamData();

            Bundle bn = new Bundle();
            new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            finishAffinity();
        });

        /*
        android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_booking_view, null);
        builder.setView(dialogView);

        final MTextView titleTxt = (MTextView) dialogView.findViewById(R.id.titleTxt);
        final MTextView mesasgeTxt = (MTextView) dialogView.findViewById(R.id.mesasgeTxt);

        titleTxt.setText(generalFunc.retrieveLangLBl("Booking Successful", "LBL_BOOKING_ACCEPTED"));

        mesasgeTxt.setText(message);

        //generalFunc.resetMultiStoredDetails();

        try {
            Realm realm = MyApp.getRealmInstance();
            realm.beginTransaction();
            realm.delete(CarWashCartData.class);
            realm.commitTransaction();
        } catch (Exception e) {

        }

        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> {
            dialog.cancel();
            Bundle bn = new Bundle();
            new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            finishAffinity();
        });

        builder.setPositiveButton(generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS"), (dialog, which) -> {
            dialog.cancel();
            Bundle bn = new Bundle();
            bn.putBoolean("isrestart", true);

            bn.putString("selType", Utils.CabGeneralType_UberX);

            new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);
            finish();
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        */
    }

    private void clearReleamData() {
        try {
            Realm realm = MyApp.getRealmInstance();
            realm.beginTransaction();
            realm.delete(CarWashCartData.class);
            realm.commitTransaction();
        } catch (Exception e) {

        }
    }

    boolean isCashSelected = true;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.SELECT_COUPON_REQ_CODE && resultCode == RESULT_OK) {

            String couponCode = data.getStringExtra("CouponCode");

            if (couponCode == null) {
                couponCode = "";
            }

            appliedPromoCode = couponCode;
            setPromoCode();
            getDetails();
        } else if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK) {
            iscash = false;
            setCashCardView();
        } else if (requestCode == SEL_ADDRESS_REQ_CODE) {

            if (resultCode == RESULT_OK) {
                iUserAddressId = data.getStringExtra("addressId");
                iTempUserId = data.getStringExtra("addressId");
                vServiceAddress = data.getStringExtra("address");
                userAddressTxt.setText(vServiceAddress);
                ((ScrollView) findViewById(R.id.scrollView)).post(() -> ((ScrollView) findViewById(R.id.scrollView)).fullScroll(ScrollView.FOCUS_DOWN));
            }

            getDetails();

        } else if (requestCode == ADD_ADDRESS_REQ_CODE && resultCode == RESULT_OK) {
            getUserProfileJson();
            getDetails();
        } else if (requestCode == Utils.SCHEDULE_REQUEST_CODE && resultCode == RESULT_OK) {

            SelectDate = data.getStringExtra("SelectDate");
            sdate = data.getStringExtra("Sdate");
            Stime = data.getStringExtra("Stime");

            booktimeVTxt.setText(Stime);
            bookDateVTxt.setText(sdate);
            bookingtype = Utils.CabReqType_Later;

            getOutStandingAmout("", Utils.CabReqType_Later);
        } else if (requestCode == Utils.SELECT_ORGANIZATION_PAYMENT_CODE) {

            if (resultCode == RESULT_OK) {
                if (data.getSerializableExtra("data").equals("")) {


                    if (data.getBooleanExtra("isCash", false)) {
                        isCashSelected = true;
                        iscash = true;
                    } else {
                        isCashSelected = false;
                        iscash = false;
                    }
                    if (data.getBooleanExtra("isWallet", false)) {
                        eWalletDebitAllow = true;

                    } else {

                        eWalletDebitAllow = false;
                    }

                    if (bookingtype.equalsIgnoreCase(Utils.CabReqType_Now)) {

                        getOutStandingAmout("", bookingtype);

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("iDriverId", getIntent().getStringExtra("iDriverId"));
                        new StartActProcess(getActContext()).startActForResult(ScheduleDateSelectActivity.class, bundle, Utils.SCHEDULE_REQUEST_CODE);
                    }


                }

            }
        }
    }
}
