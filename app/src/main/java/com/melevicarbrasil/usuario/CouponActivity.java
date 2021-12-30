package com.melevicarbrasil.usuario;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.files.ApplyCouponAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 03-07-18.
 */

public class CouponActivity extends AppCompatActivity implements ApplyCouponAdapter.OnItemClickListener {

    MTextView titleTxt;
    ImageView backImgView;
    EditText inputCouponCode;

    GeneralFunctions generalFunc;

    MButton btn_type2;
    MTextView couponHTxt;
    ProgressBar loading_apply_coupon;
    MTextView noCouponTxt;

    RecyclerView applyCouponRecyclerView;
    ErrorView errorView;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    ApplyCouponAdapter applyCouponAdapter;

    String required_str = "";

    String appliedCouponCode = "";

    int selpos = -1;
    AppBarLayout app_bar_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout_coupon);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(i) / (float) maxScroll;
                if (percentage > 0.70f) {

                    couponHTxt.setTextColor(getResources().getColor(R.color.black));
                    couponHTxt.setText(generalFunc.retrieveLangLBl("Choose Coupons", "LBL_CHOOSE_COUPONS"));
                    couponHTxt.setGravity(Gravity.START);
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Poppins_SemiBold.ttf");
                    couponHTxt.setTypeface(face);
                } else {
                    couponHTxt.setTextColor(Color.parseColor("#999999"));
                    couponHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_COUPON_FROM_LIST"));
                    couponHTxt.setGravity(Gravity.CENTER);
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Poppins_Light.ttf");
                    couponHTxt.setTypeface(face);
                }

            }
        });
        loading_apply_coupon = (ProgressBar) findViewById(R.id.loading_apply_coupon);
        noCouponTxt = (MTextView) findViewById(R.id.noCouponTxt);
        applyCouponRecyclerView = (RecyclerView) findViewById(R.id.applyCouponRecyclerView);

        errorView = (ErrorView) findViewById(R.id.errorView);
        inputCouponCode = (EditText) findViewById(R.id.inputCouponCode);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        couponHTxt = (MTextView) findViewById(R.id.couponHTxt);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActContext(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        applyCouponRecyclerView.setLayoutManager(mLayoutManager);

        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());

        if (getIntent().getStringExtra("CouponCode") != null) {
            appliedCouponCode = getIntent().getStringExtra("CouponCode");
        }

        setAdapter();
        setLabels();
        getCouponList();
    }

    public void setAdapter() {
        applyCouponAdapter = new ApplyCouponAdapter(getActContext(), list, generalFunc, appliedCouponCode, applyCouponRecyclerView);
        applyCouponRecyclerView.setAdapter(applyCouponAdapter);
        applyCouponAdapter.setOnItemClickListener(this);
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_APPLY_COUPON"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_APPLY"));
        couponHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_COUPON_FROM_LIST"));
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");

        inputCouponCode.setHint(generalFunc.retrieveLangLBl("", "LBL_ENTER_COUPON_CODE"));
    }

    public Context getActContext() {
        return CouponActivity.this;
    }

    public void getCouponList() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading_apply_coupon.getVisibility() != View.VISIBLE) {
            loading_apply_coupon.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.topContainerView).setVisibility(View.GONE);

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DisplayCouponList");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        if (getIntent().hasExtra("eSystem")) {
            parameters.put("eSystem", Utils.eSystem_Type);
        }
        parameters.put("eFly", getIntent().getBooleanExtra("eFly", false) ? "Yes" : "No");

        if (getIntent().hasExtra("eType")) {
            parameters.put("eType", getIntent().getStringExtra("eType"));
        }
        noCouponTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noCouponTxt.setVisibility(View.GONE);
            findViewById(R.id.topContainerView).setVisibility(View.VISIBLE);
            JSONObject responseObj = generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                list.clear();
                closeLoader();

                if (generalFunc.checkDataAvail(Utils.action_str, responseObj) == true) {

                    String vCurrency = generalFunc.getJsonValueStr("vCurrency", responseObj);
                    String vSymbol = generalFunc.getJsonValueStr("vSymbol", responseObj);

                    JSONArray arr_rides = generalFunc.getJsonArray(Utils.message_str, responseObj);

                    if (arr_rides != null && arr_rides.length() > 0) {
                        for (int i = 0; i < arr_rides.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_rides, i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            String eValidityType = generalFunc.getJsonValueStr("eValidityType", obj_temp);
                            String dExpiryDate = generalFunc.getJsonValueStr("dExpiryDate", obj_temp);
                            map.put("iCouponId", generalFunc.getJsonValueStr("iCouponId", obj_temp));
                            map.put("vCouponCode", generalFunc.getJsonValueStr("vCouponCode", obj_temp));
                            map.put("tDescription", generalFunc.getJsonValueStr("tDescription", obj_temp));
                            map.put("fDiscount", generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("fDiscount", obj_temp)));
                            map.put("eType", generalFunc.getJsonValueStr("eType", obj_temp));
                            map.put("eValidityType", eValidityType);
                            map.put("dActiveDate", generalFunc.getJsonValueStr("dActiveDate", obj_temp));
                            map.put("dExpiryDate", dExpiryDate + " 00:00:00");
                            map.put("iUsageLimit", generalFunc.getJsonValueStr("iUsageLimit", obj_temp));
                            map.put("iUsed", generalFunc.getJsonValueStr("iUsed", obj_temp));
                            map.put("eStatus", generalFunc.getJsonValueStr("eStatus", obj_temp));
                            if (!eValidityType.equalsIgnoreCase("PERMANENT")) {
                                map.put("dExpiryDate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(map.get("dExpiryDate"), Utils.OriginalDateFormate, CommonUtilities.OriginalDateFormate)));
                            }

                            map.put("vSymbol", vSymbol);
                            map.put("vCurrency", vCurrency);
                            map.put("isdetailsView", "No");
                            map.put("LBL_USE_AND_SAVE_LBL", generalFunc.retrieveLangLBl("", "LBL_USE_AND_SAVE_LBL"));
                            map.put("LBL_REMOVE_TEXT", generalFunc.retrieveLangLBl("", "LBL_REMOVE_TEXT"));
                            map.put("LBL_APPLY", generalFunc.retrieveLangLBl("", "LBL_APPLY"));
                            map.put("LBL_VALID_TILL_TXT", generalFunc.retrieveLangLBl("", "LBL_VALID_TILL_TXT"));
                            map.put("LBL_USE_CODE_TXT", generalFunc.retrieveLangLBl("", "LBL_USE_CODE_TXT"));

                            if (generalFunc.getJsonValueStr("vCouponCode", obj_temp).equals(appliedCouponCode)) {
                                selpos = i;
                            }
                            list.add(map);
                        }
                    }

                    if (list.size() == 0) {
                        couponHTxt.setVisibility(View.GONE);
                        (findViewById(R.id.listCouponArea)).setVisibility(View.GONE);
                        Utils.showSoftKeyboard((Activity) getActContext(), inputCouponCode);
                    }


                    if (selpos == -1 && !appliedCouponCode.equalsIgnoreCase("")) {
                        inputCouponCode.setText(appliedCouponCode);

                        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_REMOVE_TEXT"));
                    }

                    applyCouponAdapter.notifyDataSetChanged();
                    applyCouponRecyclerView.scrollToPosition(selpos == -1 ? 0 : selpos);
                } else {
                    if (list.size() == 0) {
                        couponHTxt.setVisibility(View.GONE);
                        (findViewById(R.id.listCouponArea)).setVisibility(View.GONE);
                        Utils.showSoftKeyboard((Activity) getActContext(), inputCouponCode);
                    }
                }
            } else {
                generateErrorView();
            }
        });
        exeWebServer.execute();
    }

    public void closeLoader() {
        if (loading_apply_coupon.getVisibility() == View.VISIBLE) {
            loading_apply_coupon.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();
        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getCouponList());
    }

    @Override
    public void onItemClickList(View v, int position) {
        Utils.hideKeyboard(getActContext());
        checkPromoCode(list.get(position).get("vCouponCode"), "");
    }


    @Override
    public void onItemClickListRemoveCode(View v, int position, String string) {
        Utils.hideKeyboard(getActContext());
        if (string.equals("useCode")) {
//            removePromoCode(list.get(position).get("vCouponCode"), string);
            removePromoCode();
        }
    }

    public void checkCouponCode() {

        if (!appliedCouponCode.equalsIgnoreCase("") && appliedCouponCode.equalsIgnoreCase(Utils.getText(inputCouponCode))) {
            removePromoCode();
            return;
        }

        boolean couponCodeEntered = Utils.checkText(inputCouponCode) ? true : setErrorData();
        if (couponCodeEntered == false) {
            return;
        }
        checkPromoCode(inputCouponCode.getText().toString().trim(), "");
    }

    public boolean setErrorData() {
       // inputCouponCode.setError(AppFunctions.fromHtml("<font color='white'>" + required_str + "</font>"));

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getActContext().getResources().getColor(R.color.white));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(required_str);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, required_str.length(), 0);
        inputCouponCode.setError(spannableStringBuilder);
        return false;
    }

    public void checkPromoCode(final String promoCode, String string) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "CheckPromoCode");
        parameters.put("PromoCode", promoCode);
        parameters.put("iUserId", generalFunc.getMemberId());
        if (getIntent().hasExtra("eSystem")) {
            parameters.put("eType", Utils.eSystem_Type);
            parameters.put("eSystemType", "");
        }

            parameters.put("eFly", getIntent().getBooleanExtra("eFly", false) ? "Yes" : "No");

        if (getIntent().hasExtra("eType")) {
            parameters.put("eType", getIntent().getStringExtra("eType"));
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);
                if (action.equals("1")) {

                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {
                        generateAlert.closeAlertBox();

                        Bundle bundle = new Bundle();
                        bundle.putString("CouponCode", promoCode);
                        new StartActProcess(getActContext()).setOkResult(bundle);

                        finish();
                    });
                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
                    generateAlert.showAlertBox();

                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void removePromoCode() {

        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DELETE_CONFIRM_COUPON_MSG"), generalFunc.retrieveLangLBl("", "LBL_NO"), generalFunc.retrieveLangLBl("", "LBL_YES"), buttonId -> {
            if (buttonId == 1) {

                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_COUPON_REMOVE_SUCCESS"), "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), button_Id -> {
                    if (button_Id == 1) {

                        appliedCouponCode = "";

                        new StartActProcess(getActContext()).setOkResult();
                        finish();
                    }
                });

            }
        });

        /*final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            if (btn_id == 1) {
                generateAlert.closeAlertBox();
                new StartActProcess(getActContext()).setOkResult();
                finish();
            }
        });
        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_COUPON_REMOVE_SUCCESS"));
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
//        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("Cancel", "LBL_BTN_CANCEL_TXT"));
        generateAlert.showAlertBox();*/

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == R.id.backImgView) {
                CouponActivity.super.onBackPressed();
            } else if (i == btn_type2.getId()) {
                checkCouponCode();
            }
        }
    }

}
