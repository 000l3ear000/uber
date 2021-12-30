package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessSelectPaymentActivity extends AppCompatActivity {
    public GeneralFunctions generalFunc;
    public ImageView backImgView;
    MTextView titleTxt, profileheadingTxt, paymentheadingTxt, orgnizationNameTxt;
    RadioGroup radioGroup;
    RadioButton radioPayOnline, radioCashPayment;
    RadioButton seleted;
    MButton btn_type2;
    CheckBox checkboxWallet;
    LinearLayout orgnizationArea;
    ArrayList<HashMap<String, String>> listdata = new ArrayList<>();
    ArrayList<HashMap<String, String>> reasonlistdata = new ArrayList<>();
    ArrayList<HashMap<String, String>> filterReasonlistdata = new ArrayList<>();
    androidx.appcompat.app.AlertDialog list_organization;
    androidx.appcompat.app.AlertDialog reasonlist_organization;
    String iUserProfileId = "";
    String iOrganizationId = "";
    String vProfileEmail = "";
    String ePaymentBy = "";
    String userProfileJson = "";
    int selectPos = 0;
    LinearLayout paymentArea;
    MTextView addWalletTxt;
    LinearLayout reasonArea;
    MTextView reasonNameTxt;
    String selectReasonId = "";
    String vReasonName = "";
    LinearLayout reasonLayout;
    LinearLayout commentarea;
    MTextView commentHname;
    MaterialEditText commentBox;
    MTextView reasonheadingTxt;
    ImageView profileImage, imagearrow, reasonarrow;
    boolean isCardConfirmDialogShow = true;
    String vImage = "";
    String APP_PAYMENT_MODE = "";
    String LBL_PERSONAL = "";
    String LBL_OTHER_TXT = "";
    String APP_PAYMENT_METHOD="";
    String SYSTEM_PAYMENT_FLOW="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_select_payment);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        getUserProfileJson();

        LBL_PERSONAL=generalFunc.retrieveLangLBl("", "LBL_PERSONAL");
        LBL_OTHER_TXT=generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT");

        backImgView = (ImageView) findViewById(R.id.backImgView);
        orgnizationArea = (LinearLayout) findViewById(R.id.orgnizationArea);
        orgnizationArea.setOnClickListener(new setOnClick());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        orgnizationNameTxt = (MTextView) findViewById(R.id.orgnizationNameTxt);
        profileheadingTxt = (MTextView) findViewById(R.id.profileheadingTxt);
        paymentheadingTxt = (MTextView) findViewById(R.id.paymentheadingTxt);
        radioGroup = (RadioGroup) findViewById(R.id.radioGrp);
        radioPayOnline = (RadioButton) findViewById(R.id.radioPayOnline);
        radioCashPayment = (RadioButton) findViewById(R.id.radioCashPayment);
        checkboxWallet = (CheckBox) findViewById(R.id.checkboxWallet);
        addWalletTxt = (MTextView) findViewById(R.id.addWalletTxt);
        addWalletTxt.setOnClickListener(new setOnClick());
        paymentArea = (LinearLayout) findViewById(R.id.paymentArea);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        reasonArea = (LinearLayout) findViewById(R.id.reasonArea);
        reasonNameTxt = (MTextView) findViewById(R.id.reasonNameTxt);
        reasonLayout = (LinearLayout) findViewById(R.id.reasonLayout);
        commentarea = (LinearLayout) findViewById(R.id.commentarea);
        commentHname = (MTextView) findViewById(R.id.commentHname);
        reasonheadingTxt = (MTextView) findViewById(R.id.reasonheadingTxt);
        commentBox = (MaterialEditText) findViewById(R.id.commentBox);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        imagearrow = (ImageView) findViewById(R.id.imagearrow);
        reasonarrow = (ImageView) findViewById(R.id.reasonarrow);
        commentBox.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        commentBox.setSingleLine(false);
        commentBox.setHideUnderline(true);
        commentBox.setGravity(Gravity.START | Gravity.TOP);
        commentBox.setLines(5);
        commentBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_fb_border));
        commentBox.setPaddings(10, 5, 0, 5);

        if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash")) {
            addWalletTxt.setVisibility(View.GONE);
        }

        reasonArea.setOnClickListener(new setOnClick()); btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClick());
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_DONE"));

        radioCashPayment.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_PAYMENT_TXT"));
        radioPayOnline.setText(generalFunc.retrieveLangLBl("Pay Online", "LBL_PAY_ONLINE_TXT"));
        addWalletTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ACTION_ADD"));
        radioCashPayment.setChecked(true);
        backImgView.setOnClickListener(new setOnClick());
        profileheadingTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_PROFILE"));
        paymentheadingTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_PAY_MODE"));
        checkboxWallet.setText(generalFunc.retrieveLangLBl("", "LBL_USE_WALLET_BALANCE") + "\n" + "( " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson)) + " )");
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PROFILE_PAYMENT"));
        orgnizationNameTxt.setText(LBL_PERSONAL);
        reasonNameTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_REASON"));
        reasonheadingTxt.setText(generalFunc.retrieveLangLBl("", "LBL_REASON"));
        commentHname.setText(generalFunc.retrieveLangLBl("", "LBL_WRITE_REASON_BELOW"));
        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.personal));
        displayProfileList();


        if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash")) {
            radioCashPayment.setVisibility(View.VISIBLE);
            radioPayOnline.setVisibility(View.GONE);

        } else if (APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
            radioCashPayment.setVisibility(View.GONE);
            radioPayOnline.setVisibility(View.VISIBLE);
            radioPayOnline.setChecked(true);

        } else {
            radioCashPayment.setVisibility(View.VISIBLE);
            radioPayOnline.setVisibility(View.VISIBLE);
        }


        if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            checkboxWallet.setVisibility(View.GONE);
            addWalletTxt.setVisibility(View.GONE);

            radioPayOnline.setText(generalFunc.retrieveLangLBl("Pay by Wallet", "LBL_PAY_BY_WALLET_TXT") + "(" + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson) + ")"));

        }


        radioPayOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                        if (isCardConfirmDialogShow) {
                            checkCardConfig();
                        }
                        isCardConfirmDialogShow = true;
                    }
                }
            }
        });

        manageView();


    }

    private void getUserProfileJson() {
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        APP_PAYMENT_MODE=generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson);
        APP_PAYMENT_METHOD=generalFunc.getJsonValue("APP_PAYMENT_METHOD", userProfileJson);
        SYSTEM_PAYMENT_FLOW=generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson);
    }


    public void displayProfileList() {
        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DisplayUserOrganizationProfile");
        parameters.put("UserType", Utils.userType);
        parameters.put("iUserId", generalFunc.getMemberId());

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {
                JSONObject responseObj=generalFunc.getJsonObject(responseString);
                if (responseObj != null && !responseObj.equals("")) {

                    if (generalFunc.checkDataAvail(Utils.action_str, responseObj) == true) {

                        JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseObj);
                        HashMap<String, String> map;
                        map = new HashMap<String, String>();
                        map.put("vShortProfileName", LBL_PERSONAL);
                        map.put("vProfileName", LBL_PERSONAL);
                        map.put("ePaymentBy", generalFunc.retrieveLangLBl("", "Passenger"));
                        listdata.add(map);
                        for (int i = 0; i < obj_arr.length(); i++) {
                            map = new HashMap<String, String>();
                            JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);
                            map.put("iUserProfileMasterId", generalFunc.getJsonValueStr("iUserProfileMasterId", obj_temp));
                            map.put("vImage", generalFunc.getJsonValueStr("vImage", obj_temp));
                            map.put("eStatus", generalFunc.getJsonValueStr("eStatus", obj_temp));
                            map.put("vProfileName", generalFunc.getJsonValueStr("vProfileName", obj_temp));
                            map.put("vProfileEmail", generalFunc.getJsonValueStr("vProfileEmail", obj_temp));
                            map.put("iUserProfileId", generalFunc.getJsonValueStr("iUserProfileId", obj_temp));
                            map.put("iOrganizationId", generalFunc.getJsonValueStr("iOrganizationId", obj_temp));
                            map.put("vCompany", generalFunc.getJsonValueStr("vCompany", obj_temp));
                            map.put("ePaymentBy", generalFunc.getJsonValueStr("ePaymentBy", obj_temp));
                            map.put("vShortProfileName", generalFunc.getJsonValueStr("vShortProfileName", obj_temp));

                            JSONArray tripReason_arr = generalFunc.getJsonArray("tripreasons", obj_temp);
                            HashMap<String, String> reasonMap;
                            String iOrganizationId = "";
                            ArrayList<HashMap<String, String>> tempreasonlistdata = new ArrayList<>();
                            if (tripReason_arr != null && tripReason_arr.length() > 0) {
                                for (int j = 0; j < tripReason_arr.length(); j++) {
                                    reasonMap = new HashMap<String, String>();
                                    JSONObject reasonobj_temp = generalFunc.getJsonObject(tripReason_arr, j);
                                    reasonMap.put("iTripReasonId", generalFunc.getJsonValueStr("iTripReasonId", reasonobj_temp));
                                    reasonMap.put("vReasonTitle", generalFunc.getJsonValueStr("vReasonTitle", reasonobj_temp));
                                    reasonMap.put("vShortProfileName", generalFunc.getJsonValueStr("vShortProfileName", obj_temp));
                                    reasonMap.put("vProfileName", generalFunc.getJsonValueStr("vProfileName", obj_temp));
                                    tempreasonlistdata.add(reasonMap);
                                }

                                map.put("tripreasons", "Yes");
                            } else {
                                map.put("tripreasons", "No");
                            }
                            listdata.add(map);

                            reasonlistdata.addAll(tempreasonlistdata);


                        }

//                        if (reasonlistdata.size() > 0) {
                        HashMap otherMap = new HashMap<String, String>();
                        otherMap.put("iTripReasonId", "-1");
                        otherMap.put("vReasonTitle", LBL_OTHER_TXT);
                        otherMap.put("vShortProfileName", LBL_OTHER_TXT);
                        otherMap.put("vProfileName", LBL_OTHER_TXT);

                        reasonlistdata.add(otherMap);
//                        }

                    } else {
                        HashMap<String, String> map;
                        map = new HashMap<String, String>();
                        map.put("vShortProfileName", LBL_PERSONAL);
                        map.put("vProfileName", LBL_PERSONAL);
                        map.put("ePaymentBy", generalFunc.retrieveLangLBl("", "Passenger"));
                        listdata.add(map);

                        (findViewById(R.id.btn_type2)).setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        exeWebServer.execute();


    }


    public Context getActContext() {
        return BusinessSelectPaymentActivity.this;
    }


    public void checkCardConfig() {


        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                OpenCardPaymentAct(true);
            } else {
                showPaymentBox(false, false);
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

        builder.setPositiveButton(generalFunc.retrieveLangLBl("Confirm", "LBL_BTN_TRIP_CANCEL_CONFIRM_TXT"), (dialog, which) -> {
            dialog.cancel();

            if (APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
                handleButton();
            }
//            if (isOutstanding) {
//                callOutStandingPayAmout(isReqNow);
//
//            } else {
//                checkPaymentCard();
//            }
        });
        builder.setNeutralButton(generalFunc.retrieveLangLBl("Change", "LBL_CHANGE"), (dialog, which) -> {
            dialog.cancel();
            OpenCardPaymentAct(true);
            //ridelaterclick = false;

        });
        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> {
            dialog.cancel();
            //ridelaterclick = false;
            if (!APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
                radioPayOnline.setChecked(false);
                radioCashPayment.setChecked(true);
            }


        });


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void OpenCardPaymentAct(boolean fromcabselection) {
        Bundle bn = new Bundle();
        // bn.putString("UserProfileJson", userProfileJson);
        bn.putBoolean("fromcabselection", fromcabselection);
        new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserProfileJson();
        setData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            getUserProfileJson();
        }

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                radioPayOnline.setChecked(false);
                radioCashPayment.setChecked(true);
            }

        }
    }

    public void manageView() {

        if (getIntent().hasExtra("ePaymentBy")) {
            ePaymentBy = getIntent().getStringExtra("ePaymentBy");
        }

        String vProfileName=getIntent().getStringExtra("vProfileName");
        String selectReasonId_=getIntent().getStringExtra("selectReasonId");
        String vReasonName_=getIntent().getStringExtra("vReasonName");
        String vReasonTitle_=getIntent().getStringExtra("vReasonTitle");

        if (ePaymentBy.equalsIgnoreCase("Passenger") || !ePaymentBy.equalsIgnoreCase("Organization")) {
            if (getIntent().getStringExtra("isWallet").equalsIgnoreCase("Yes")) {
                checkboxWallet.setChecked(true);
            }


            if (getIntent().getBooleanExtra("isCash", false)) {
                radioCashPayment.setChecked(true);
            } else {
                isCardConfirmDialogShow = false;
                radioPayOnline.setChecked(true);
            }


            if (!vProfileName.equalsIgnoreCase("")) {
                orgnizationNameTxt.setText(vProfileName);
            }
            if (!selectReasonId_.equalsIgnoreCase("")) {
                selectReasonId = selectReasonId_;
                reasonLayout.setVisibility(View.VISIBLE);
                reasonNameTxt.setText(vReasonName_);


            }

            if (!vReasonName_.equalsIgnoreCase("")) {
                reasonLayout.setVisibility(View.VISIBLE);
                reasonNameTxt.setText(vReasonName_);
                vReasonName = vReasonName_;
            }
            if (!vReasonTitle_.equalsIgnoreCase("")) {
                commentarea.setVisibility(View.VISIBLE);
                reasonLayout.setVisibility(View.VISIBLE);
                commentBox.setText(vReasonTitle_);
            }
            vImage = getIntent().getStringExtra("vImage");
            if (vImage != null && !vImage.equalsIgnoreCase("")) {
                if (vImage.equalsIgnoreCase(generalFunc.retrieveLangLBl("", "Personal"))) {
                    profileImage.setImageDrawable(getResources().getDrawable(R.drawable.personal));
                } else {
                    Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImage);
                }
            }
        } else {

            if (!vProfileName.equalsIgnoreCase("")) {
                orgnizationNameTxt.setText(vProfileName);
            }

            vImage = getIntent().getStringExtra("vImage");
            if (vImage != null && !vImage.equalsIgnoreCase("")) {
                Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImage);

            }
            if (!selectReasonId_.equalsIgnoreCase("")) {
                selectReasonId = selectReasonId_;
                reasonLayout.setVisibility(View.VISIBLE);
                reasonNameTxt.setText(vReasonName_);


            }

            if (!vReasonName_.equalsIgnoreCase("")) {
                reasonLayout.setVisibility(View.VISIBLE);
                reasonNameTxt.setText(vReasonName_);
                vReasonName = vReasonName_;
            }
            if (!vReasonTitle_.equalsIgnoreCase("")) {
                commentarea.setVisibility(View.VISIBLE);
                reasonLayout.setVisibility(View.VISIBLE);
                commentBox.setText(vReasonTitle_);
            }

            paymentArea.setVisibility(View.GONE);


        }
        selectPos = getIntent().getIntExtra("selectPos", 0);
    }

    public void setData() {
        checkboxWallet.setText(generalFunc.retrieveLangLBl("", "LBL_USE_WALLET_BALANCE") + "\n" + "( " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson)) + " )");
    }


    public void handleButton() {
        Bundle bundle = new Bundle();
        if (selectPos == 0) {
            bundle.putSerializable("data", "");
            boolean iscash = radioCashPayment.isChecked();
            Logger.d("iscash", "::" + iscash);
            if (iscash) {
                bundle.putBoolean("isCash", true);
            }
            boolean isWallet = checkboxWallet.isChecked();
            Logger.d("isWallet", "::" + isWallet);
            if (isWallet) {
                bundle.putBoolean("isWallet", true);
            }
        } else {
            ePaymentBy = listdata.get(selectPos).get("ePaymentBy");
            if (ePaymentBy.equalsIgnoreCase("Passenger") || !ePaymentBy.equalsIgnoreCase("Organization")) {
                boolean iscash = radioCashPayment.isChecked();
                Logger.d("iscash", "::" + iscash);
                if (iscash) {
                    bundle.putBoolean("isCash", true);
                }
                boolean isWallet = checkboxWallet.isChecked();
                Logger.d("isWallet", "::" + isWallet);
                if (isWallet) {
                    bundle.putBoolean("isWallet", true);
                }
            }

            bundle.putSerializable("data", listdata.get(selectPos));
        }


        bundle.putString("iTripReasonId", selectReasonId);
        bundle.putString("vReasonTitle", commentBox.getText().toString().trim());
        bundle.putString("vReasonName", vReasonName);
        bundle.putInt("selectPos", selectPos);
        new StartActProcess(getActContext()).setOkResult(bundle);
        commentBox.setText("");
        finish();
    }

    private void setSelectedReason(int item) {
        // Do something with the selection
        if (reasonlist_organization != null) {
            reasonlist_organization.dismiss();
        }
        commentBox.setText("");

        selectReasonId = filterReasonlistdata.get(item).get("iTripReasonId");
        vReasonName = filterReasonlistdata.get(item).get("vReasonTitle");
        reasonNameTxt.setText(filterReasonlistdata.get(item).get("vReasonTitle"));
        if (selectReasonId.equalsIgnoreCase("-1")) {
            selectReasonId = "";
            commentarea.setVisibility(View.VISIBLE);
        } else {
            commentarea.setVisibility(View.GONE);
        }
    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.backImgView) { onBackPressed();

            } else if (id == addWalletTxt.getId()) {
                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
            } else if (id == btn_type2.getId()) {
                if (commentarea.getVisibility() == View.VISIBLE && commentBox.getText().toString().length() == 0) {
                    generalFunc.showMessage(commentarea, generalFunc.retrieveLangLBl("", "LBL_RESTRICT_ADD_REASON"));
                    return;
                }

                if (reasonLayout.getVisibility() == View.VISIBLE && selectReasonId.equalsIgnoreCase("") && commentarea.getVisibility() == View.GONE) {
                    generalFunc.showMessage(commentarea, generalFunc.retrieveLangLBl("", "LBL_RESTRICT_SEL_REASON"));
                    return;
                }


                if (APP_PAYMENT_MODE.equalsIgnoreCase("Card") && !ePaymentBy.equalsIgnoreCase("Organization")) {
                    checkCardConfig();
                    return;
                }

                handleButton();


            } else if (id == orgnizationArea.getId()) {
                ArrayList<String> items = new ArrayList<String>();

                for (int i = 0; i < listdata.size(); i++) {
                    items.add(listdata.get(i).get("vProfileName"));

                }

                CharSequence[] cs_currency_txt = items.toArray(new CharSequence[items.size()]);


                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
                builder.setTitle(generalFunc.retrieveLangLBl("", "LBL_SELECT_TXT"));

                builder.setItems(cs_currency_txt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        selectPos = item;
                        HashMap<String, String> mapData = listdata.get(item);
                        if (list_organization != null) {
                            list_organization.dismiss();
                        }

                        commentBox.setText("");
                        selectReasonId = "";
                        vReasonName = "";

                        ePaymentBy = mapData.get("ePaymentBy");
                        if (ePaymentBy.equalsIgnoreCase("Passenger") || !ePaymentBy.equalsIgnoreCase("Organization")) {
                            paymentArea.setVisibility(View.VISIBLE);
                        } else {
                            paymentArea.setVisibility(View.GONE);
                        }

                        String  tripreasons = mapData.get("tripreasons");
                        if ((tripreasons != null && tripreasons.equalsIgnoreCase("Yes"))) {
                            reasonLayout.setVisibility(View.VISIBLE);
                        } else {
                            reasonLayout.setVisibility(View.GONE);
                            commentarea.setVisibility(View.GONE);
                            commentBox.setText("");
                        }

                        String  vProfileName = mapData.get("vProfileName");

                        if (vProfileName.equalsIgnoreCase(LBL_PERSONAL)) {
                            iUserProfileId = "";
                            vProfileEmail = "";
                            iOrganizationId = "";
                            selectReasonId = "";
                            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.personal));

                            reasonLayout.setVisibility(View.GONE);
                        } else {
                            iUserProfileId = mapData.get("iUserProfileId");
                            vProfileEmail = mapData.get("vProfileEmail");
                            iOrganizationId = mapData.get("iOrganizationId");
                            String vImage=mapData.get("vImage");
                            if (vImage != null&& !vImage.isEmpty()) {
                                Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImage);
                            }
                        }


                        orgnizationNameTxt.setText(vProfileName);


                        if (Utils.checkText(iUserProfileId) && reasonlistdata.size() > 0) {

                            reasonLayout.setVisibility(View.VISIBLE);

                            ArrayList<String> items = new ArrayList<String>();

                            filterReasonlistdata.clear();

                            for (int i = 0; i < reasonlistdata.size(); i++) {


                                HashMap<String, String> posData = reasonlistdata.get(i);

                                String vProfileName1=posData.get("vProfileName");

                                if (vProfileName1.equalsIgnoreCase(orgnizationNameTxt.getText().toString().trim()) ||
                                        vProfileName1.equalsIgnoreCase(LBL_OTHER_TXT)) {
                                    items.add(posData.get("vReasonTitle"));
                                    filterReasonlistdata.add(posData);
                                }

                            }

                            if (filterReasonlistdata.size() == 1) {
                                setSelectedReason(0);
                            } else {
                                commentarea.setVisibility(View.GONE);
                                reasonNameTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_REASON"));

                            }

                        }

                    }
                });

                list_organization = builder.create();
                list_organization.show();

                if (generalFunc.isRTLmode() == true) {
                    generalFunc.forceRTLIfSupported(list_organization);
                }
            } else if (id == reasonArea.getId()) {
                ArrayList<String> items = new ArrayList<String>();

                filterReasonlistdata.clear();

                for (int i = 0; i < reasonlistdata.size(); i++) {

                    HashMap<String, String> posData = reasonlistdata.get(i);
                    String vProfileName=posData.get("vProfileName");
                    if (vProfileName.equalsIgnoreCase(orgnizationNameTxt.getText().toString().trim()) ||
                            vProfileName.equalsIgnoreCase(LBL_OTHER_TXT)) {
                        items.add(posData.get("vReasonTitle"));
                        filterReasonlistdata.add(posData);
                    }


                }


                CharSequence[] cs_currency_txt = items.toArray(new CharSequence[items.size()]);


                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
                builder.setTitle(generalFunc.retrieveLangLBl("", "LBL_SELECT_TXT"));

                builder.setItems(cs_currency_txt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        setSelectedReason(item);

                    }
                });

                reasonlist_organization = builder.create();
                reasonlist_organization.show();

                if (generalFunc.isRTLmode() == true) {
                    generalFunc.forceRTLIfSupported(reasonlist_organization);
                }
            }


        }
    }
}
