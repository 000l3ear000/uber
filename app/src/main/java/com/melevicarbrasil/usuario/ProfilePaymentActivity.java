package com.melevicarbrasil.usuario;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dialogs.OpenListView;
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

public class ProfilePaymentActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    public ImageView backImgView;
    MTextView titleTxt, walletLblTxt, walletBalanceTxt, walletBalTxt, addWalletTxt, cashTxt, cardTxt;
    String userProfileJson;
    LinearLayout cashArea, cardArea;
    CardView cashCardViewarea, cardViewarea;
    boolean isCashSelect = true;
    String APP_PAYMENT_MODE = "";
    String SYSTEM_PAYMENT_FLOW = "";
    String LBL_PERSONAL = "";
    String LBL_OTHER_TXT = "";
    ImageView cardImg, cashImg;
    LinearLayout userWalletArea;
    AppCompatCheckBox checkboxWallet;
    MTextView selProfileTxt, reasonLblTxt;
    MButton btn_type2;
    MTextView selProfileBoxTxt, selreasonBoxTxt;
    ArrayList<HashMap<String, String>> listdata = new ArrayList<>();
    ArrayList<HashMap<String, String>> reasonlistdata = new ArrayList<>();
    ArrayList<HashMap<String, String>> filterReasonlistdata = new ArrayList<>();
    String ePaymentBy = "";
    String selectedMethod = "";
    LinearLayout commentarea;
    MaterialEditText commentBox;
    int selectPos = 0;
    String selectReasonId = "";
    String vReasonName = "";
    String iUserProfileId = "";
    String iOrganizationId = "";
    String vProfileEmail = "";
    LinearLayout reasonArea;
    ImageView profileView;
    String APP_PAYMENT_METHOD = "";
    LinearLayout cardValArea;
    MTextView cardValTxt, selectCardTxt;
    ImageView profileImg;
    LinearLayout paymentArea;
    MTextView paymentTitleTxt, commentHname, codNotAllowTxt;
    LinearLayout organizeArea;
    View walletView;
    MTextView organizationNoteTxt;
    FrameLayout mainPaymentArea;
    String DisplayCardPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_payment);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        getUserProfileJson();
        DisplayCardPayment = getIntent().getStringExtra("DisplayCardPayment");

        mainPaymentArea = (FrameLayout) findViewById(R.id.mainPaymentArea);
        organizationNoteTxt = (MTextView) findViewById(R.id.organizationNoteTxt);
        walletView = (View) findViewById(R.id.walletView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        profileImg = (ImageView) findViewById(R.id.profileImg);
        walletLblTxt = (MTextView) findViewById(R.id.walletLblTxt);
        walletBalTxt = (MTextView) findViewById(R.id.walletBalTxt);
        walletBalanceTxt = (MTextView) findViewById(R.id.walletBalanceTxt);
        addWalletTxt = (MTextView) findViewById(R.id.addWalletTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        cashTxt = (MTextView) findViewById(R.id.cashTxt);
        cardTxt = (MTextView) findViewById(R.id.cardTxt);
        cashArea = (LinearLayout) findViewById(R.id.cashArea);
        cashCardViewarea = (CardView) findViewById(R.id.cashCardViewarea);
        cardArea = (LinearLayout) findViewById(R.id.cardArea);
        cardViewarea = (CardView) findViewById(R.id.cardViewarea);
        cardImg = (ImageView) findViewById(R.id.cardImg);
        cashImg = (ImageView) findViewById(R.id.cashImg);
        userWalletArea = (LinearLayout) findViewById(R.id.userWalletArea);
        checkboxWallet = (AppCompatCheckBox) findViewById(R.id.checkboxWallet);
        selProfileTxt = (MTextView) findViewById(R.id.selProfileTxt);
        selProfileBoxTxt = (MTextView) findViewById(R.id.selProfileBoxTxt);
        selreasonBoxTxt = (MTextView) findViewById(R.id.selreasonBoxTxt);
        reasonLblTxt = (MTextView) findViewById(R.id.reasonLblTxt);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        commentarea = (LinearLayout) findViewById(R.id.commentarea);
        reasonArea = (LinearLayout) findViewById(R.id.reasonArea);
        profileView = (ImageView) findViewById(R.id.profileView);
        cardValArea = (LinearLayout) findViewById(R.id.cardValArea);
        cardValTxt = (MTextView) findViewById(R.id.cardValTxt);
        selectCardTxt = (MTextView) findViewById(R.id.selectCardTxt);
        commentBox = (MaterialEditText) findViewById(R.id.commentBox);
        paymentTitleTxt = (MTextView) findViewById(R.id.paymentTitleTxt);
        commentHname = (MTextView) findViewById(R.id.commentHname);
        codNotAllowTxt = (MTextView) findViewById(R.id.codNotAllowTxt);
        paymentArea = (LinearLayout) findViewById(R.id.paymentArea);
        organizeArea = (LinearLayout) findViewById(R.id.organizeArea);

        commentBox.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        commentBox.setSingleLine(false);
        commentBox.setHideUnderline(true);
        commentBox.setGravity(Gravity.START | Gravity.TOP);
        commentBox.setLines(5);
        commentBox.setPaddings(10, 5, 0, 5);
        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClick());
        profileView.setOnClickListener(new setOnClick());
        cashArea.setOnClickListener(new setOnClick());
        cardArea.setOnClickListener(new setOnClick());
        backImgView.setOnClickListener(new setOnClick());
        selProfileBoxTxt.setOnClickListener(new setOnClick());
        selreasonBoxTxt.setOnClickListener(new setOnClick());
        selectCardTxt.setOnClickListener(new setOnClick());
        addWalletTxt.setOnClickListener(new setOnClick());


        setLabel();
        manageWalletView();
        manageCashCardView(true);
        displayProfileList();

        manageView();





        ViewGroup layout =  findViewById(R.id.bottomArea);
        LayoutTransition layoutTransition = layout.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);


    }

    private void manageWalletView() {
        if (!generalFunc.getJsonValue(Utils.WALLET_ENABLE, userProfileJson).equals("") &&
                generalFunc.getJsonValue(Utils.WALLET_ENABLE, userProfileJson).equalsIgnoreCase("Yes")) {
            userWalletArea.setVisibility(View.VISIBLE);
            walletView.setVisibility(View.VISIBLE);
        } else {
            userWalletArea.setVisibility(View.GONE);
            walletView.setVisibility(View.GONE);
        }


        if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("method-1")) {
            walletBalanceTxt.setVisibility(View.VISIBLE);
            cardTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
            userWalletArea.setVisibility(View.GONE);
            walletView.setVisibility(View.GONE);
            cardImg.setImageResource(R.drawable.ic_wallet_method);

        }

    }


    String vReasonTitle_ = "";
    String vReasonName_ = "";

    public void manageView() {

        if (getIntent().hasExtra("ePaymentBy")) {
            ePaymentBy = getIntent().getStringExtra("ePaymentBy");
        }
        if (getIntent().hasExtra("selectedMethod")) {
            selectedMethod = getIntent().getStringExtra("selectedMethod");
        }

        if (getIntent().getBooleanExtra("isRide", false)) {
            String vProfileName = getIntent().getStringExtra("vProfileName");
            String selectReasonId_ = getIntent().getStringExtra("selectReasonId");
            vReasonName_ = getIntent().getStringExtra("vReasonName");
            vReasonTitle_ = getIntent().getStringExtra("vReasonTitle");
            Log.d("manageView", "manageView: vProfileName:" + vProfileName + ",vReasonName_:" + vReasonName_ + ",vReasonTitle_:" + vReasonTitle_);

            if (ePaymentBy.equalsIgnoreCase("Passenger") || !ePaymentBy.equalsIgnoreCase("Organization")) {
                if (getIntent().getStringExtra("isWallet") != null && getIntent().getStringExtra("isWallet").equalsIgnoreCase("Yes")) {
                    checkboxWallet.setChecked(true);
                }


                if (getIntent().getBooleanExtra("isCash", false)) {
                    manageCashCardView(true);
                } else {
                    manageCashCardView(false);
                }


                if (!vProfileName.equalsIgnoreCase("")) {
                    selProfileBoxTxt.setText(vProfileName);
                }
                if (!selectReasonId_.equalsIgnoreCase("")) {
                    selectReasonId = selectReasonId_;
                    // reasonArea.setVisibility(View.VISIBLE);

                    selreasonBoxTxt.setText(vReasonName_);


                }

                if (!vReasonName_.equalsIgnoreCase("")) {
                    reasonArea.setVisibility(View.VISIBLE);
                    selreasonBoxTxt.setText(vReasonName_);
                    vReasonName = vReasonName_;
                    selCurrentPosition = 1;
                }
                if (!vReasonTitle_.equalsIgnoreCase("")) {
                    //commentarea.setVisibility(View.VISIBLE);
                    reasonArea.setVisibility(View.GONE);
                    profileView.setVisibility(View.VISIBLE);
                    profileView.animate().setDuration(150).rotation(-180);
                    commentBox.setText(vReasonTitle_);
                }
                String vImage = getIntent().getStringExtra("vImage");
                if (vImage != null && !vImage.equalsIgnoreCase("")) {
                    if (vImage.equalsIgnoreCase(generalFunc.retrieveLangLBl("", "Personal"))) {
                        profileImg.setImageDrawable(getResources().getDrawable(R.drawable.personal));
                    } else {
                        reasonArea.setVisibility(View.VISIBLE);

                        Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImg);
                    }
                }


                manageOrganizeNote(false);
            } else {

                if (!vProfileName.equalsIgnoreCase("")) {
                    selProfileBoxTxt.setText(vProfileName);
                }

                String vImage = getIntent().getStringExtra("vImage");
                if (vImage != null && !vImage.equalsIgnoreCase("")) {
                    Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImg);

                }
                if (!selectReasonId_.equalsIgnoreCase("")) {
                    selectReasonId = selectReasonId_;
                    // reasonArea.setVisibility(View.VISIBLE);
                    selreasonBoxTxt.setText(vReasonName_);


                }

                if (!vReasonName_.equalsIgnoreCase("")) {
                    reasonArea.setVisibility(View.VISIBLE);
                    selreasonBoxTxt.setText(vReasonName_);
                    vReasonName = vReasonName_;
                    selCurrentPosition = 1;
                }
                if (!vReasonTitle_.equalsIgnoreCase("")) {
                    //commentarea.setVisibility(View.VISIBLE);
//                    reasonArea.setVisibility(View.GONE);
                    profileView.setVisibility(View.VISIBLE);
                    profileView.animate().setDuration(150).rotation(-180);
                    commentBox.setText(vReasonTitle_);
                }

//                paymentArea.setVisibility(View.GONE);
//                paymentTitleTxt.setVisibility(View.GONE);
//                cardValArea.setVisibility(View.GONE);


                manageOrganizeNote(true);


            }
            selectPos = getIntent().getIntExtra("selectPos", 0);
        } else {
            organizeArea.setVisibility(View.GONE);

            if (getIntent().getStringExtra("isWallet") != null && getIntent().getStringExtra("isWallet").equalsIgnoreCase("Yes")) {
                checkboxWallet.setChecked(true);
            }
            if (getIntent().getBooleanExtra("isCash", false)) {
                manageCashCardView(true);
            } else {
                manageCashCardView(false);
            }

            if (!getIntent().getBooleanExtra("isCODAllow", true)) {
                codNotAllowTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_COD_NOT_AVAILABLE_TXT")) + " " + getIntent().getStringExtra("outStandingAmount"));
                cashArea.setEnabled(false);
                codNotAllowTxt.setVisibility(View.VISIBLE);
                cashArea.setBackgroundColor(getActContext().getResources().getColor(R.color.gray));
                manageCashCardView(false);
                manageGravity();
            }


            if (getIntent().getBooleanExtra("isRecipient", false) || (DisplayCardPayment != null && DisplayCardPayment.equalsIgnoreCase("No"))) {
                ((LinearLayout) findViewById(R.id.paymentBtnArea)).setGravity(generalFunc.isRTLmode() ? Gravity.RIGHT : Gravity.LEFT);
                cardArea.setVisibility(View.GONE);
                cardViewarea.setVisibility(View.GONE);
                userWalletArea.setVisibility(View.GONE);
                walletView.setVisibility(View.GONE);
                manageCashCardView(true);
                manageGravity();
            }
        }

    }

    public void manageGravity() {
        paymentArea.setGravity(Gravity.START);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) (paymentArea).getLayoutParams();
        if (generalFunc.isRTLmode()) {
            params.rightMargin = Utils.dipToPixels(getActContext(), 15);
        } else {
            params.leftMargin = Utils.dipToPixels(getActContext(), 15);
        }


    }

    public void manageCashCardView(boolean isCash) {
        if (isCash) {
            isCashSelect = true;
            cashTxt.setTextColor(getActContext().getResources().getColor(R.color.white));
            cashArea.setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
            cardTxt.setTextColor(getActContext().getResources().getColor(R.color.black));
            cardArea.setBackgroundColor(getActContext().getResources().getColor(R.color.white));
            walletBalanceTxt.setTextColor(getActContext().getResources().getColor(R.color.black));
            cashImg.setColorFilter(ContextCompat.getColor(getActContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            cardImg.setColorFilter(ContextCompat.getColor(getActContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            cardValArea.setVisibility(View.GONE);
            cashCardViewarea.setCardBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
            cardViewarea.setCardBackgroundColor(getActContext().getResources().getColor(R.color.white));
        } else {
            cardViewarea.setCardBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
            cashCardViewarea.setCardBackgroundColor(getActContext().getResources().getColor(R.color.white));
            isCashSelect = false;
            cashTxt.setTextColor(getActContext().getResources().getColor(R.color.black));
            cashArea.setBackgroundColor(getActContext().getResources().getColor(R.color.white));
            cardArea.setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
            cardTxt.setTextColor(getActContext().getResources().getColor(R.color.white));
            walletBalanceTxt.setTextColor(getActContext().getResources().getColor(R.color.white));
            cashImg.setColorFilter(ContextCompat.getColor(getActContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            cardImg.setColorFilter(ContextCompat.getColor(getActContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

            if (!getIntent().getBooleanExtra("isCODAllow", true)) {
                cashArea.setBackgroundColor(getActContext().getResources().getColor(R.color.gray));
            }

            if ((SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("method-1") && !selectedMethod.equalsIgnoreCase("Instant")) || (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("method-1") && Utils.checkText(selectedMethod) && selectedMethod.equalsIgnoreCase("Manual"))) {

                String vCreditCard = generalFunc.getJsonValue("vCreditCard", userProfileJson);
                if (!vCreditCard.equalsIgnoreCase("")) {
                    cardValArea.setVisibility(View.VISIBLE);
                    cardValTxt.setText(vCreditCard);
                    //showPaymentBox(false, false);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        getUserProfileJson();
        getWalletBalDetails();

        if ((APP_PAYMENT_MODE.equalsIgnoreCase("Cash") || (DisplayCardPayment != null && DisplayCardPayment.equalsIgnoreCase("No")))) {
            cashCardViewarea.setVisibility(View.VISIBLE);
            cardArea.setVisibility(View.GONE);
            cardViewarea.setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.paymentBtnArea)).setGravity(generalFunc.isRTLmode() ? Gravity.RIGHT : Gravity.LEFT);
            addWalletTxt.setVisibility(View.GONE);
            manageCashCardView(true);
            manageGravity();
        } else if (APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
            cardArea.setVisibility(View.VISIBLE);
            cardViewarea.setVisibility(View.VISIBLE);
            cashCardViewarea.setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.paymentBtnArea)).setGravity(generalFunc.isRTLmode() ? Gravity.RIGHT : Gravity.LEFT);
            addWalletTxt.setVisibility(View.VISIBLE);
            manageCashCardView(false);
            manageGravity();
        }


        if (codNotAllowTxt.getVisibility() == View.VISIBLE) {
            codNotAllowTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_COD_NOT_AVAILABLE_TXT")) + " " + getIntent().getStringExtra("outStandingAmount"));
            cashArea.setEnabled(false);
            codNotAllowTxt.setVisibility(View.VISIBLE);
            cashArea.setBackgroundColor(getActContext().getResources().getColor(R.color.gray));
            manageCashCardView(false);
            manageGravity();
        }

    }

    private void setUserBalance() {
        String userBal = generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson));
        walletBalTxt.setText(userBal);
        walletBalanceTxt.setText("(" + userBal + ")");
    }

    public void setLabel() {

        organizationNoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ORGANIZATION_NOTE"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PROFILE_PAYMENT"));
        walletLblTxt.setText(generalFunc.retrieveLangLBl("", "LBL_USE_WALLET_BALANCE"));
        setUserBalance();
        addWalletTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ACTION_ADD"));
        cashTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
        cardTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_ONLINE_TXT"));
        selProfileTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_PROFILE"));
        reasonLblTxt.setText(generalFunc.retrieveLangLBl("", "LBL_REASON"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_DONE"));
        LBL_PERSONAL = generalFunc.retrieveLangLBl("", "LBL_PERSONAL");
        LBL_OTHER_TXT = generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT");
        selectCardTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHOOSE_ANOTEHR_CARD"));
        selreasonBoxTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_REASON"));
        paymentTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_PAYMENT_METHOD_TXT"));
        commentHname.setText(generalFunc.retrieveLangLBl("", "LBL_WRITE_REASON_BELOW"));

        selProfileBoxTxt.setText(LBL_PERSONAL);


    }

    public Context getActContext() {
        return ProfilePaymentActivity.this;
    }

    int selCurrentPosition = 0;
    int selCurrentPositionorg = -1;

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == R.id.cardArea) {
                manageCashCardView(false);
                if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("method-1")) {

                    checkboxWallet.setChecked(true);

                } else if (!Utils.checkText(selectedMethod) || ((Utils.checkText(selectedMethod) && selectedMethod.equalsIgnoreCase("Manual")))) {
                    checkCardConfig();
                }

            } else if (i == R.id.cashArea) {
                manageCashCardView(true);
                if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("method-1")) {
                    checkboxWallet.setChecked(false);
                }
            } else if (i == R.id.selProfileBoxTxt) {


/*
                ArrayList<String> items = new ArrayList<String>();

                for (int j = 0; j < listdata.size(); j++) {
                    items.add(listdata.get(j).get("vProfileName"));

                }

                CharSequence[] cs_currency_txt = items.toArray(new CharSequence[items.size()]);


                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
                builder.setTitle(generalFunc.retrieveLangLBl("", "LBL_SELECT_PROFILE"));

                builder.setItems(cs_currency_txt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        selectPos = item;
                        HashMap<String, String> mapData = listdata.get(item);
                        if (list_organization != null) {
                            list_organization.dismiss();
                        }

                        if (ePaymentBy.equalsIgnoreCase(ePaymentBy)) {
                            return;
                        }
                        commentBox.setText("");
                        selectReasonId = "";
                        vReasonName = "";

                        ePaymentBy = mapData.get("ePaymentBy");
                        if (ePaymentBy.equalsIgnoreCase("Passenger") || !ePaymentBy.equalsIgnoreCase("Organization")) {
                            paymentArea.setVisibility(View.VISIBLE);


                            paymentTitleTxt.setVisibility(View.VISIBLE);

                            manageOrganizeNote(false);

                            manageCashCardView(isCashSelect);
                        } else {
                            //  paymentArea.setVisibility(View.GONE);
                            // paymentTitleTxt.setVisibility(View.GONE);
                            // cardValArea.setVisibility(View.GONE);


                            manageOrganizeNote(true);


                        }

                        String tripreasons = mapData.get("tripreasons");
                        if ((tripreasons != null && tripreasons.equalsIgnoreCase("Yes"))) {
                            commentarea.setVisibility(View.VISIBLE);
                            profileView.setVisibility(View.VISIBLE);
                        } else {
                            commentarea.setVisibility(View.GONE);
                            profileView.setVisibility(View.GONE);

                            commentBox.setText("");
                        }

                        String vProfileName = mapData.get("vProfileName");

                        if (vProfileName.equalsIgnoreCase(LBL_PERSONAL)) {
                            iUserProfileId = "";
                            vProfileEmail = "";
                            iOrganizationId = "";
                            selectReasonId = "";
                            profileImg.setImageDrawable(getResources().getDrawable(R.drawable.personal));

                            reasonArea.setVisibility(View.GONE);


                        } else {
                            iUserProfileId = mapData.get("iUserProfileId");
                            vProfileEmail = mapData.get("vProfileEmail");
                            iOrganizationId = mapData.get("iOrganizationId");
                            String vImage = mapData.get("vImage");
                            if (vImage != null && !vImage.isEmpty()) {
                                Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImg);

                            }
                        }


                        selProfileBoxTxt.setText(vProfileName);


                        if (Utils.checkText(iUserProfileId) && reasonlistdata.size() > 0) {

                            reasonArea.setVisibility(View.VISIBLE);

                            //profileView.setVisibility(View.VISIBLE);

                            ArrayList<String> items = new ArrayList<String>();

                            filterReasonlistdata.clear();

                            for (int i = 0; i < reasonlistdata.size(); i++) {


                                HashMap<String, String> posData = reasonlistdata.get(i);

                                String vProfileName1 = posData.get("vProfileName");

                                if (vProfileName1.equalsIgnoreCase(selProfileBoxTxt.getText().toString().trim()) ||
                                        vProfileName1.equalsIgnoreCase(LBL_OTHER_TXT)) {
                                    items.add(posData.get("vReasonTitle"));
                                    filterReasonlistdata.add(posData);
                                }

                            }

                            if (filterReasonlistdata.size() == 1) {
                                setSelectedReason(0);
                            } else {
                                commentarea.setVisibility(View.GONE);
                                profileView.setVisibility(View.GONE);

                                //reasonNameTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_REASON"));

                            }

                        }

                    }
                });

                list_organization = builder.create();
                list_organization.show();*/


                OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SELECT_PROFILE"), listdata, OpenListView.OpenDirection.CENTER, true, position -> {

                    HashMap<String, String> mapData = listdata.get(position);

                    if (!ePaymentBy.equalsIgnoreCase(mapData.get("ePaymentBy"))) {
                        commentBox.setText("");
                        selectReasonId = "";
                        vReasonName = "";
                    } else {
                        //  return;
                    }

                    selCurrentPosition = position;

                    selectPos = position;


                    ePaymentBy = mapData.get("ePaymentBy");
                    if (ePaymentBy.equalsIgnoreCase("Passenger") || !ePaymentBy.equalsIgnoreCase("Organization")) {
                        paymentArea.setVisibility(View.VISIBLE);


                        paymentTitleTxt.setVisibility(View.VISIBLE);

                        manageOrganizeNote(false);

                        manageCashCardView(isCashSelect);
                    } else {
                        //  paymentArea.setVisibility(View.GONE);
                        // paymentTitleTxt.setVisibility(View.GONE);
                        // cardValArea.setVisibility(View.GONE);


                        manageOrganizeNote(true);


                    }

                    String tripreasons = mapData.get("tripreasons");
                    if ((tripreasons != null && tripreasons.equalsIgnoreCase("Yes"))) {
                        commentarea.setVisibility(View.VISIBLE);
                        profileView.setVisibility(View.VISIBLE);
                    } else {
                        commentarea.setVisibility(View.GONE);
                        profileView.setVisibility(View.GONE);

                        commentBox.setText("");
                    }

                    String vProfileName = mapData.get("vProfileName");

                    if (vProfileName.equalsIgnoreCase(LBL_PERSONAL)) {
                        iUserProfileId = "";
                        vProfileEmail = "";
                        iOrganizationId = "";
                        selectReasonId = "";
                        profileImg.setImageDrawable(getResources().getDrawable(R.drawable.personal));

                        reasonArea.setVisibility(View.GONE);


                    } else {
                        iUserProfileId = mapData.get("iUserProfileId");
                        vProfileEmail = mapData.get("vProfileEmail");
                        iOrganizationId = mapData.get("iOrganizationId");
                        String vImage = mapData.get("vImage");
                        if (vImage != null && !vImage.isEmpty()) {
                            Picasso.get().load(vImage).placeholder(R.mipmap.ic_no_icon).into(profileImg);

                        }
                        reasonArea.setVisibility(View.GONE);
                    }

                    selProfileBoxTxt.setText(listdata.get(position).get("vProfileName"));


                    if (Utils.checkText(iUserProfileId) && reasonlistdata.size() > 0) {

                        reasonArea.setVisibility(View.VISIBLE);

                        //profileView.setVisibility(View.VISIBLE);

                        ArrayList<String> items = new ArrayList<String>();

                        filterReasonlistdata.clear();

                        for (int j = 0; j < reasonlistdata.size(); j++) {


                            HashMap<String, String> posData = reasonlistdata.get(j);

                            String vProfileName1 = posData.get("vProfileName");

                            if (vProfileName1.equalsIgnoreCase(selProfileBoxTxt.getText().toString().trim()) ||
                                    vProfileName1.equalsIgnoreCase(LBL_OTHER_TXT)) {
                                items.add(posData.get("vReasonTitle"));
                                filterReasonlistdata.add(posData);
                            }

                        }

                        if (filterReasonlistdata.size() == 1) {
                            setSelectedReason(0);
                        } else {
                            commentarea.setVisibility(View.GONE);
                            profileView.setVisibility(View.GONE);
                            //reasonNameTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_REASON"));

                        }
                    }


                }).show(selCurrentPosition, "vProfileName");

            } else if (i == R.id.profileView) {
                if (reasonArea.getVisibility() == View.VISIBLE && commentarea.getVisibility()==View.GONE) {
                    profileView.animate().setDuration(150).rotation(0);
                    commentarea.setVisibility(View.VISIBLE);
                } else if (reasonArea.getVisibility() == View.GONE && commentarea.getVisibility() == View.GONE) {
                    profileView.animate().setDuration(150).rotation(0);
                    reasonArea.setVisibility(View.VISIBLE);
                    commentarea.setVisibility(View.VISIBLE);
                }
                else if (commentarea.getVisibility() == View.GONE) {
                    profileView.animate().setDuration(150).rotation(0);
                    reasonArea.setVisibility(View.VISIBLE);
                    commentarea.setVisibility(View.VISIBLE);
                } else if (commentarea.getVisibility() == View.VISIBLE) {
                    profileView.animate().setDuration(150).rotation(-180);
                    reasonArea.setVisibility(View.GONE);
                    commentarea.setVisibility(View.GONE);
                /*    if (!vReasonTitle_.equalsIgnoreCase("")) {
                        commentarea.setVisibility(View.VISIBLE);
                    }*/
                }
            } else if (i == R.id.selreasonBoxTxt) {

                // ArrayList<String> items = new ArrayList<String>();

                filterReasonlistdata.clear();

                for (int j = 0; j < reasonlistdata.size(); j++) {

                    HashMap<String, String> posData = reasonlistdata.get(j);
                    String vProfileName = posData.get("vProfileName");
                    if (vProfileName.equalsIgnoreCase(selProfileBoxTxt.getText().toString().trim()) ||
                            vProfileName.equalsIgnoreCase(LBL_OTHER_TXT)) {
                        // items.add(posData.get("vReasonTitle"));
                        filterReasonlistdata.add(posData);
                    }


                }
 /*

                CharSequence[] cs_currency_txt = items.toArray(new CharSequence[items.size()]);


                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
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
                }*/


                OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SELECT_TXT"), reasonlistdata, OpenListView.OpenDirection.CENTER, true, position -> {


                    selCurrentPositionorg = position;
                    setSelectedReason(position);

                }).show(selCurrentPositionorg, "vReasonTitle");


            } else if (i == btn_type2.getId()) {
                if (commentarea.getVisibility() == View.VISIBLE && commentBox.getText().toString().length() == 0) {
                    generalFunc.showMessage(commentarea, generalFunc.retrieveLangLBl("", "LBL_RESTRICT_ADD_REASON"));
                    return;
                }

                if (reasonArea.getVisibility() == View.VISIBLE && selectReasonId.equalsIgnoreCase("") && commentarea.getVisibility() == View.GONE) {
                    generalFunc.showMessage(commentarea, generalFunc.retrieveLangLBl("", "LBL_RESTRICT_SEL_REASON"));
                    return;
                }


                if (APP_PAYMENT_MODE.equalsIgnoreCase("Card") && !ePaymentBy.equalsIgnoreCase("Organization")) {


                    String vCreditCard = generalFunc.getJsonValue("vCreditCard", userProfileJson);

                    if (vCreditCard.equals("")) {
                        OpenCardPaymentAct(true);
                        return;
                    }


                }

                handleButton();


            } else if (i == selectCardTxt.getId()) {
                OpenCardPaymentAct(true);
            } else if (i == addWalletTxt.getId()) {
                Bundle bn = new Bundle();
                bn.putString("iServiceId", generalFunc.getServiceId());
                if (getIntent().hasExtra("isCheckout")) {
                    bn.putString("isCheckout", "");
                }
                new StartActProcess(getActContext()).startActWithData(MyWalletActivity.class, bn);
            }
        }
    }

    public void manageOrganizeNote(boolean isShow) {
        if (isShow) {
            organizationNoteTxt.setVisibility(View.VISIBLE);
            int size10 = (int) getResources().getDimension(R.dimen._10sdp);
            int size15 = (int) getResources().getDimension(R.dimen._15sdp);
            FrameLayout.LayoutParams prams = (FrameLayout.LayoutParams) mainPaymentArea.getLayoutParams();
            prams.setMargins(size10, size15, size10, 0);
            mainPaymentArea.requestLayout();
            cashArea.setClickable(false);
            cardArea.setClickable(false);
            selectCardTxt.setClickable(false);
            mainPaymentArea.setClickable(false);
            userWalletArea.setVisibility(View.GONE);
            walletView.setVisibility(View.GONE);
            mainPaymentArea.setForeground(getResources().getDrawable(R.drawable.disableview));
        } else {
            organizationNoteTxt.setVisibility(View.GONE);
            FrameLayout.LayoutParams prams = (FrameLayout.LayoutParams) mainPaymentArea.getLayoutParams();
            prams.setMargins(0, 0, 0, 0);
            mainPaymentArea.requestLayout();
            mainPaymentArea.setBackgroundResource(0);
            final int color = 0xFFFFFF;
            manageWalletView();
            final Drawable drawable = new ColorDrawable(color);
            mainPaymentArea.setForeground(drawable);
            mainPaymentArea.setBackgroundResource(0);
            cashArea.setClickable(true);
            cardArea.setClickable(true);
            selectCardTxt.setClickable(true);
            mainPaymentArea.setClickable(true);
        }

    }

    public void checkCardConfig() {

        String vCreditCard = generalFunc.getJsonValue("vCreditCard", userProfileJson);
        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {

            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                OpenCardPaymentAct(true);
            } else {
                cardValArea.setVisibility(View.VISIBLE);

                cardValTxt.setText(vCreditCard);
                //showPaymentBox(false, false);

            }
        }

    }

    public void OpenCardPaymentAct(boolean fromcabselection) {
        Bundle bn = new Bundle();
        // bn.putString("UserProfileJson", userProfileJson);
//        bn.putBoolean("fromcabselection", fromcabselection);
        new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
    }

    private void setSelectedReason(int item) {
        // Do something with the selection
        commentBox.setText("");

        selectReasonId = filterReasonlistdata.get(item).get("iTripReasonId");
        vReasonName = filterReasonlistdata.get(item).get("vReasonTitle");
        selreasonBoxTxt.setText(filterReasonlistdata.get(item).get("vReasonTitle"));
        if (selectReasonId.equalsIgnoreCase("-1")) {
//            selectReasonId = "";
            commentarea.setVisibility(View.VISIBLE);
            profileView.setVisibility(View.VISIBLE);
        } else {
            commentarea.setVisibility(View.GONE);
            profileView.setVisibility(View.GONE);

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

                manageCashCardView(true);
            }


        });


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
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
                JSONObject responseObj = generalFunc.getJsonObject(responseString);
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


                                    if (!generalFunc.getJsonValueStr("vReasonTitle", reasonobj_temp).isEmpty()) {
                                        tempreasonlistdata.add(reasonMap);
                                        if (vReasonName_.equalsIgnoreCase(generalFunc.getJsonValueStr("vReasonTitle", reasonobj_temp))) {
                                            selCurrentPositionorg = j;
                                        }
                                    }

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

                        if (vReasonName_.equalsIgnoreCase(LBL_OTHER_TXT)) {
                            selCurrentPositionorg = reasonlistdata.size();
                        }

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

    public void handleButton() {
        Bundle bundle = new Bundle();
        if (selectPos == 0) {
            bundle.putSerializable("data", "");


            if (isCashSelect) {
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

                if (isCashSelect) {
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
        bundle.putString("vProfileName", selProfileBoxTxt.getText().toString());
        new StartActProcess(getActContext()).setOkResult(bundle);
        commentBox.setText("");
        finish();
    }

    private void getUserProfileJson() {
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        APP_PAYMENT_MODE = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson);
        APP_PAYMENT_METHOD = generalFunc.getJsonValue("APP_PAYMENT_METHOD", userProfileJson);
        SYSTEM_PAYMENT_FLOW = generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            getUserProfileJson();
        }

        boolean isCardNotAdded = true;
        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            isCardNotAdded = vStripeCusId.equals("");

        }

        if (isCardNotAdded) {
            manageCashCardView(true);
        } else {
            manageCashCardView(false);
        }
    }

    public void getWalletBalDetails() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GetMemberWalletBalance");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseStringObject = generalFunc.getJsonObject(responseString);

            if (responseStringObject != null && !responseStringObject.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseStringObject);

                if (isDataAvail) {
                    try {
                        String userProfileJsonStr = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        JSONObject object = generalFunc.getJsonObject(userProfileJsonStr);
                        String MemberBalance = generalFunc.getJsonValueStr("MemberBalance", responseStringObject);
                        object.put("user_available_balance", MemberBalance);
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, object.toString());

                        getUserProfileJson();
                        setUserBalance();

                    } catch (Exception e) {

                    }
                } else {
                    setUserBalance();
                }
            }
        });
        exeWebServer.execute();
    }

}
