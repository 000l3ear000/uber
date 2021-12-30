package com.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.files.WalletHistoryRecycleAdapter;
import com.autofit.et.lib.AutoFitEditText;

import com.melevicarbrasil.usuario.CardPaymentActivity;
import com.melevicarbrasil.usuario.MyWalletHistoryActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.CustomDialog;
import com.general.files.DecimalDigitsInputFilter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.anim.loader.AVLoadingIndicatorView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyWalletFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    View view;

    private final long DELAY = 1000; // in ms
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    ProgressBar loading_wallet_history;
    MTextView viewTransactionsTxt;
    ErrorView errorView;
    String required_str = "";
    String error_money_str = "";

    String userProfileJson = "";
    boolean mIsLoading = false;

    String next_page_str = "0";


    //    private MTextView policyTxt;
//    private MTextView termsTxt;
    private MTextView yourBalTxt;
    private MButton btn_type1;


    //private MTextView addMoneyTagTxt;
    private MTextView addMoneyTxt;

    private Timer timer = new Timer();
    private static final int SEL_CARD = 004;
    public static final int TRANSFER_MONEY = 87;

    AppCompatCheckBox useBalChkBox;
    MTextView useBalanceTxt;

    InternetConnection intCheck;
    AVLoadingIndicatorView loaderView;
    WebView paymentWebview;

    // Go Pay view declaration start
    LinearLayout addTransferArea, ProfileImageArea;
    String transferState = "SEARCH";
    MTextView sendMoneyTxt, transferMoneyTagTxt;
    RadioButton driverRadioBtn, userRadioBtn;
    RadioGroup rg_whomType;
    MaterialEditText detailBox, otpverificationCodeBox;
    FrameLayout verificationArea;
    LinearLayout infoArea;
    ImageView ic_back_arrow;
    MTextView whomTxt, userNameTxt, moneyTitleTxt;
    MButton btn_type3, btn_type4, btn_otp;
    SelectableRoundedImageView toUserImgView;
    CardView moneyDetailArea;
    LinearLayout transferMoneyAddDetailArea;
    String error_email_str = "";
    String error_verification_code = "";
    LinearLayout toWhomTransferArea;
    String isRegenerate = "No";
    boolean isClicked = false;
    // Go Pay view declaration end


    LinearLayout addMoneyArea, transerArea, TransactionArea;
    MTextView transferTxt, transactionTxt, recentTransHTxt, noTransactionTxt;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    boolean isNextPageAvailable = false;
    RecyclerView recentTransactionRecyclerView;
    private WalletHistoryRecycleAdapter wallethistoryRecyclerAdapter;

    LinearLayout transferMoneyToWallet;

    String detailBoxVal = "";
    LinearLayout resendOtpArea, otpArea, moneyArea;
    String iUserId = "";
    String eUserType = "";
    String verificationCode = "";
    String username = "";
    String userImage = "";
    String userEmail = "";
    String userPhone = "";
    String amount = "";
    String transactionDate = "";
    CardView transerCardArea, addMoneyCardArea;

    LinearLayout WalletContentArea;
    private AppBarLayout app_bar_layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mywallet, container, false);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        intCheck = new InternetConnection(getActContext());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        WalletContentArea = (LinearLayout) view.findViewById(R.id.WalletContentArea);
        transerCardArea = (CardView) view.findViewById(R.id.transerCardArea);
        addMoneyCardArea = (CardView) view.findViewById(R.id.addMoneyCardArea);
        titleTxt = (MTextView) view.findViewById(R.id.titleTxt);
        backImgView = (ImageView) view.findViewById(R.id.backImgView);
        backImgView.setVisibility(View.GONE);

        loading_wallet_history = (ProgressBar) view.findViewById(R.id.loading_wallet_history);
        viewTransactionsTxt = (MTextView) view.findViewById(R.id.viewTransactionsTxt);
        errorView = (ErrorView) view.findViewById(R.id.errorView);

        addMoneyTxt = (MTextView) view.findViewById(R.id.addMoneyTxt);
        //addMoneyTagTxt = (MTextView) view.findViewById(R.id.addMoneyTagTxt);
        addMoneyTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY_TXT"));
        errorView = (ErrorView) view.findViewById(R.id.errorView);

        // termsTxt = (MTextView) view.findViewById(R.id.termsTxt);
        yourBalTxt = (MTextView) view.findViewById(R.id.yourBalTxt);
        //policyTxt = (MTextView) view.findViewById(R.id.policyTxt);

        app_bar_layout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        //app_bar_layout.setOutlineProvider(null);

        btn_type1 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type1)).getChildView();

        addMoneyArea = (LinearLayout) view.findViewById(R.id.addMoneyArea);
        transerArea = (LinearLayout) view.findViewById(R.id.transerArea);
        TransactionArea = (LinearLayout) view.findViewById(R.id.TransactionArea);
        transferTxt = (MTextView) view.findViewById(R.id.transferTxt);
        transactionTxt = (MTextView) view.findViewById(R.id.transactionTxt);
        recentTransHTxt = (MTextView) view.findViewById(R.id.recentTransHTxt);
        noTransactionTxt = (MTextView) view.findViewById(R.id.noTransactionTxt);
        recentTransactionRecyclerView = (RecyclerView) view.findViewById(R.id.recentTransactionRecyclerView);

        addMoneyArea.setOnClickListener(new setOnClickList());
        transerArea.setOnClickListener(new setOnClickList());
        TransactionArea.setOnClickListener(new setOnClickList());


        useBalanceTxt = (MTextView) view.findViewById(R.id.useBalanceTxt);
        useBalChkBox = (AppCompatCheckBox) view.findViewById(R.id.useBalChkBox);


        paymentWebview = (WebView) view.findViewById(R.id.paymentWebview);
        loaderView = (AVLoadingIndicatorView) view.findViewById(R.id.loaderView);


        backImgView.setOnClickListener(new setOnClickList());
        viewTransactionsTxt.setOnClickListener(new setOnClickList());

        btn_type1.setId(Utils.generateViewId());
        btn_type1.setOnClickListener(new setOnClickList());
        //termsTxt.setOnClickListener(new setOnClickList());


        setLabels();


        useBalChkBox.setOnCheckedChangeListener(this);


        //  getWalletBalDetails();

        wallethistoryRecyclerAdapter = new WalletHistoryRecycleAdapter(getActContext(), list, generalFunc, false);
        recentTransactionRecyclerView.setAdapter(wallethistoryRecyclerAdapter);

        recentTransactionRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                int lastInScreen = firstVisibleItemPosition + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable == true) {

                    mIsLoading = true;
                    wallethistoryRecyclerAdapter.addFooterView();

                    getRecentTransction(true);

                } else if (isNextPageAvailable == false) {
                    wallethistoryRecyclerAdapter.removeFooterView();
                }
            }
        });

        // getRecentTransction(false);
        showHideButton("");

        return view;
    }


    private void showHideButton(String setView) {

        boolean isOnlyCashEnabled = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson).equalsIgnoreCase("Cash");
        /*Go Pay Enabled Or Not - Delete Start if you don't want gopay */
        boolean isTransferMoneyEnabled = generalFunc.retrieveValue(Utils.ENABLE_GOPAY_KEY).equalsIgnoreCase("Yes");

        /*Go Pay Enabled Or Not - Delete End if you don't want gopay */

        if (TextUtils.isEmpty(setView)) {
            if (isOnlyCashEnabled) {
                transerCardArea.setVisibility(isTransferMoneyEnabled ? View.VISIBLE : View.GONE);
                addMoneyCardArea.setVisibility(View.GONE);

            } else if (!isOnlyCashEnabled) {
                transerCardArea.setVisibility(isTransferMoneyEnabled ? View.VISIBLE : View.GONE);

            }
        }
        /*Go Pay Enabled Or Not - Delete Start if you don't want gopay */
        else if (setView.equalsIgnoreCase("add")) {
            removeValues(true);
            //rechargeBox.setText("");

            btn_type1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            transferState = "SEARCH";
            configureView();
            transferMoneyToWallet.setVisibility(View.GONE);
            addTransferArea.setVisibility(View.VISIBLE);
            ProfileImageArea.setVisibility(View.VISIBLE);
            btn_type4.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_TO") + " " + username);
        } else if (setView.equalsIgnoreCase("transfer")) {
            removeValues(true);

            btn_type1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            addTransferArea.setVisibility(View.GONE);
            ProfileImageArea.setVisibility(View.GONE);
            transferMoneyToWallet.setVisibility(View.VISIBLE);
            transferState = "SEARCH";
            configureView();
            btn_type4.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY_TXT"));
        }
        /*Go Pay Enabled Or Not - Delete End if you don't want gopay */

    }

    public Context getActContext() {
        return getActivity();
    }

    public void setLabels() {

        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LEFT_MENU_WALLET"));
        yourBalTxt.setText(generalFunc.retrieveLangLBl("", "LBL_USER_BALANCE"));
        viewTransactionsTxt.setText(generalFunc.retrieveLangLBl("", "LBL_VIEW_TRANS_HISTORY"));
        btn_type1.setText(generalFunc.retrieveLangLBl("", "LBL_VIEW_TRANS_HISTORY"));


        useBalanceTxt.setText(generalFunc.retrieveLangLBl("", "LBL_USE_WALLET_BALANCE_NOTE"));


        // policyTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRIVACY_POLICY"));
        //termsTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRIVACY_POLICY1"));

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        error_money_str = generalFunc.retrieveLangLBl("", "LBL_ADD_CORRECT_DETAIL_TXT");


        if (generalFunc.getJsonValue("eWalletAdjustment", userProfileJson).equals("No")) {
            useBalChkBox.setChecked(false);
        } else {
            useBalChkBox.setChecked(true);
        }


        transferTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TRANSFER"));
        transactionTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TRANSACTIONS"));
        recentTransHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RECENT_TRANSACTION"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == SEL_CARD) {
            if (generalFunc.isDeliverOnlyEnabled())
            {
                if (dialog_add_money != null) {
                    dialog_add_money.dismiss();
                }

            }
            getTransactionHistory(false);


        } else if (resultCode == getActivity().RESULT_OK && requestCode == TRANSFER_MONEY) {
            list.clear();
            getRecentTransction(false);
            // getWalletBalDetails();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // getWalletBalDetails();
        list.clear();
        getRecentTransction(false);

    }

    public void getWalletBalDetails() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GetMemberWalletBalance");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {


            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    try {

                        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        JSONObject object = generalFunc.getJsonObject(userProfileJson);

                        ((MTextView) view.findViewById(R.id.walletamountTxt)).setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("MemberBalance", responseString)));

                        if (!generalFunc.getJsonValue("user_available_balance", userProfileJson).equalsIgnoreCase(generalFunc.getJsonValue("MemberBalance", responseString))) {
                            generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "Yes");
                        }


                    } catch (Exception e) {

                    }

                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
        UpdateUserWalletAdjustment(isCheck);
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            if (view.getId() == btn_type1.getId()) {
                new StartActProcess(getActContext()).startAct(MyWalletHistoryActivity.class);
            }


            switch (view.getId()) {

                case R.id.viewTransactionsTxt:
                    new StartActProcess(getActContext()).startAct(MyWalletHistoryActivity.class);
                    break;


//                case R.id.termsTxt:
//                    Bundle bn = new Bundle();
//                    bn.putString("staticpage", "4");
//                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
//                    break;


//                case R.id.addTransferBtnArea:
//                    btn_type4.performClick();
//                    break;

                /*Go Pay view Click handling start*/

                case R.id.viewTransactionsBtnArea:
                    btn_type1.performClick();
                    break;

                case R.id.infoArea:
                    animateDialog(infoArea);
                    break;
                case R.id.resendOtpArea:

                    if (!isClicked) {
                        isClicked = true;
                        isRegenerate = "Yes";
                        transferState = "ENTER_AMOUNT";
                        transferMoneyToWallet();
                    }
                    break;

                case R.id.addMoneyArea:
                    openAddMoneyDialog();
                    break;
                case R.id.transerArea:
                    openTransferDialog();
                    break;
                case R.id.TransactionArea:
                    new StartActProcess(getActContext()).startAct(MyWalletHistoryActivity.class);
                    break;

                /*Go Pay view Click handling end*/
            }
        }

    }

    private void animateDialog(LinearLayout infoArea) {

        CustomDialog customDialog = new CustomDialog(getActContext());
        customDialog.setDetails(""/*generalFunc.retrieveLangLBl("","LBL_RETRIVE_OTP_TITLE_TXT")*/, generalFunc.retrieveLangLBl("", "LBL_TRANSFER_WALLET_OTP_INFO_TXT"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), "", false, R.drawable.ic_normal_info, false, 2);
        customDialog.setRoundedViewBackgroundColor(R.color.appThemeColor_1);
        customDialog.setRoundedViewBorderColor(R.color.white);
        customDialog.setImgStrokWidth(15);
        customDialog.setBtnRadius(10);
        customDialog.setIconTintColor(R.color.white);
        customDialog.setPositiveBtnBackColor(R.color.appThemeColor_2);
        customDialog.setPositiveBtnTextColor(R.color.white);
        customDialog.createDialog();
        customDialog.setPositiveButtonClick(new com.general.files.Closure() {
            @Override
            public void exec() {

            }
        });
        customDialog.setNegativeButtonClick(new com.general.files.Closure() {
            @Override
            public void exec() {

            }
        });
        customDialog.show();
    }

    public void getRecentTransction(final boolean isLoadMore) {

        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading_wallet_history.getVisibility() != View.VISIBLE && isLoadMore == false) {
            if (list.size() == 0) {
                loading_wallet_history.setVisibility(View.VISIBLE);
            }
            WalletContentArea.setVisibility(View.GONE);
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getTransactionHistory");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("ListType", Utils.Wallet_all);
        if (isLoadMore == true) {
            parameters.put("page", next_page_str);
        }

        noTransactionTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noTransactionTxt.setVisibility(View.GONE);
            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                if(!isLoadMore)
                {
                    list.clear();
                }

                if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {

                    String nextPage = generalFunc.getJsonValue("NextPage", responseString);
                    JSONArray arr_transhistory = generalFunc.getJsonArray(Utils.message_str, responseString);
                    ((MTextView) view.findViewById(R.id.walletamountTxt)).setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("MemberBalance", responseString)));

                    if (!generalFunc.getJsonValue("user_available_balance", userProfileJson).equalsIgnoreCase(generalFunc.getJsonValue("MemberBalance", responseString))) {
                        generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "Yes");
                    }

                    if (arr_transhistory != null && arr_transhistory.length() > 0) {
                        for (int i = 0; i < arr_transhistory.length(); i++) {
                            //   for (int i = 0; i < 10; i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_transhistory, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("iUserWalletId", generalFunc.getJsonValueStr("iUserWalletId", obj_temp));
                            map.put("iUserId", generalFunc.getJsonValueStr("iUserId", obj_temp));
                            map.put("eUserType", generalFunc.getJsonValueStr("eUserType", obj_temp));
                            map.put("eType", generalFunc.getJsonValueStr("eType", obj_temp));
                            map.put("iTripId", generalFunc.getJsonValueStr("iTripId", obj_temp));
                            map.put("eFor", generalFunc.getJsonValueStr("eFor", obj_temp));
                            String tDescription = generalFunc.getJsonValueStr("tDescription", obj_temp);
                            map.put("tDescription", tDescription);
                            map.put("tDescriptionConverted", generalFunc.convertNumberWithRTL(tDescription));
                            map.put("ePaymentStatus", generalFunc.getJsonValueStr("ePaymentStatus", obj_temp));
                            map.put("currentbal", generalFunc.getJsonValueStr("currentbal", obj_temp));
                            map.put("LBL_Status", generalFunc.retrieveLangLBl("", "LBL_Status"));
                            map.put("LBL_TRIP_NO", generalFunc.retrieveLangLBl("", "LBL_TRIP_NO"));
                            map.put("LBL_BALANCE_TYPE", generalFunc.retrieveLangLBl("", "LBL_BALANCE_TYPE"));
                            map.put("LBL_DESCRIPTION", generalFunc.retrieveLangLBl("", "LBL_DESCRIPTION"));
                            map.put("LBL_AMOUNT", generalFunc.retrieveLangLBl("", "LBL_AMOUNT"));

                            String dDateOrig = generalFunc.getJsonValueStr("dDateOrig", obj_temp);
                            map.put("dDateOrig", dDateOrig);
                            map.put("listingFormattedDate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(dDateOrig, Utils.OriginalDateFormate, CommonUtilities.OriginalDateFormate)));

                            String iBalance = generalFunc.getJsonValueStr("iBalance", obj_temp);
                            map.put("iBalance", iBalance);
                            map.put("FormattediBalance", generalFunc.convertNumberWithRTL(iBalance));


                            list.add(map);
                        }
                    }


                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }

                    wallethistoryRecyclerAdapter.notifyDataSetChanged();
                } else {
                    if (list.size() == 0) {
                        removeNextPageConfig();
                        noTransactionTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        noTransactionTxt.setVisibility(View.VISIBLE);
                    }
                }

                wallethistoryRecyclerAdapter.notifyDataSetChanged();


            } else {
                if (isLoadMore == false) {
                    removeNextPageConfig();
                    generateErrorView();
                }

            }

            mIsLoading = false;
            WalletContentArea.setVisibility(View.VISIBLE);
        });

        if (!isLoadMore) {
            if (list.size() == 0) {
                exeWebServer.execute();
            }

        } else {
            exeWebServer.execute();
        }

    }


    public void UpdateUserWalletAdjustment(boolean value) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "UpdateUserWalletAdjustment");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("eWalletAdjustment", value == true ? "Yes" : "No");

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setCancelAble(false);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                if (isDataAvail == true) {

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                    userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

                    generalFunc.showMessage(generalFunc.getCurrentView((Activity) getActContext()), generalFunc.retrieveLangLBl("", "LBL_INFO_UPDATED_TXT"));

                } else {

                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));

                    useBalChkBox.setOnCheckedChangeListener(null);
                    useBalChkBox.setChecked(value == true ? false : true);
                    useBalChkBox.setOnCheckedChangeListener(this);
                }
            } else {
                generalFunc.showError();
                useBalChkBox.setOnCheckedChangeListener(null);
                useBalChkBox.setChecked(value == true ? false : true);
                useBalChkBox.setOnCheckedChangeListener(this);
            }
        });
        exeWebServer.execute();
    }

    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        wallethistoryRecyclerAdapter.removeFooterView();
    }

    Dialog dialog_add_money, dialog_transfer, dialog_sucess;

    MTextView otpInfoTxt;

    public void openTransferDialog() {

        dialog_transfer = new Dialog(getActContext(), R.style.ImageSourceDialogStyle);
        dialog_transfer.setContentView(R.layout.design_transfer_money);


        /*Go Pay view initialization start*/
        sendMoneyTxt = (MTextView) dialog_transfer.findViewById(R.id.sendMoneyTxt);
        resendOtpArea = (LinearLayout) dialog_transfer.findViewById(R.id.resendOtpArea);
        otpArea = (LinearLayout) dialog_transfer.findViewById(R.id.otpArea);
        moneyArea = (LinearLayout) dialog_transfer.findViewById(R.id.moneyArea);


        whomTxt = (MTextView) dialog_transfer.findViewById(R.id.whomTxt);
        transferMoneyTagTxt = (MTextView) dialog_transfer.findViewById(R.id.transferMoneyTagTxt);
        driverRadioBtn = (RadioButton) dialog_transfer.findViewById(R.id.driverRadioBtn);
        userRadioBtn = (RadioButton) dialog_transfer.findViewById(R.id.userRadioBtn);

        rg_whomType = (RadioGroup) dialog_transfer.findViewById(R.id.rg_whomType);
        detailBox = (MaterialEditText) dialog_transfer.findViewById(R.id.detailBox);
        verificationArea = (FrameLayout) dialog_transfer.findViewById(R.id.verificationArea);
        infoArea = (LinearLayout) dialog_transfer.findViewById(R.id.infoArea);
        otpverificationCodeBox = (MaterialEditText) dialog_transfer.findViewById(R.id.otpverificationCodeBox);
        moneyTitleTxt = (MTextView) dialog_transfer.findViewById(R.id.moneyTitleTxt);
        userNameTxt = (MTextView) dialog_transfer.findViewById(R.id.userNameTxt);
        toWhomTransferArea = (LinearLayout) dialog_transfer.findViewById(R.id.toWhomTransferArea);
        moneyDetailArea = (CardView) dialog_transfer.findViewById(R.id.moneyDetailArea);

        transferMoneyAddDetailArea = (LinearLayout) dialog_transfer.findViewById(R.id.transferMoneyAddDetailArea);
        toUserImgView = (SelectableRoundedImageView) dialog_transfer.findViewById(R.id.toUserImgView);
        btn_type3 = ((MaterialRippleLayout) dialog_transfer.findViewById(R.id.btn_type3)).getChildView();
        btn_type4 = ((MaterialRippleLayout) dialog_transfer.findViewById(R.id.btn_type4)).getChildView();
        btn_otp = ((MaterialRippleLayout) dialog_transfer.findViewById(R.id.btn_otp)).getChildView();
        MTextView cancelTxt = (MTextView) dialog_transfer.findViewById(R.id.cancelTxt);
        MTextView cancelTransTxt = (MTextView) dialog_transfer.findViewById(R.id.cancelTransTxt);
        MTextView cancelOtpTxt = (MTextView) dialog_transfer.findViewById(R.id.cancelOtpTxt);
        MTextView addMoneyNote = (MTextView) dialog_transfer.findViewById(R.id.addMoneyNote);
        transferMoneyToWallet = (LinearLayout) dialog_transfer.findViewById(R.id.transferMoneyToWallet);
        autofitEditText = (AutoFitEditText) dialog_transfer.findViewById(R.id.autofitEditText);
        ImageView backTansImage = (ImageView) dialog_transfer.findViewById(R.id.backTansImage);
        otpInfoTxt = (MTextView) dialog_transfer.findViewById(R.id.otpInfoTxt);
        MTextView currencyTxt = (MTextView) dialog_transfer.findViewById(R.id.currencyTxt);

        if (generalFunc.isRTLmode()) {
            backTansImage.setRotation(180);
        }


        currencyTxt.setText(generalFunc.getJsonValue("vCurrencyDriver", userProfileJson));
        cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        cancelTransTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        cancelOtpTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));

        btn_type3.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
        btn_otp.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_SUBMIT_TXT"));
        MTextView resendOtpTxt = (MTextView) dialog_transfer.findViewById(R.id.resendOtpTxt);
        resendOtpTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RESEND_OTP_TXT"));


        addMoneyNote.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY_MSG"));


        ic_back_arrow = (ImageView) dialog_transfer.findViewById(R.id.ic_back_arrow);


        addTransferArea = (LinearLayout) dialog_transfer.findViewById(R.id.addTransferArea);
        ProfileImageArea = (LinearLayout) dialog_transfer.findViewById(R.id.ProfileImageArea);

        infoArea.setOnClickListener(new setOnClickList());
        // dialog_transfer.findViewById(R.id.viewTransactionsBtnArea).setOnClickListener(new setOnClickList());


        addTransferArea.setOnClickListener(new setOnClickList());

        rechargeBox = (MaterialEditText) dialog_transfer.findViewById(R.id.rechargeBox);


        //rechargeBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_RECHARGE_AMOUNT_TXT"), generalFunc.retrieveLangLBl("", "LBL_RECHARGE_AMOUNT_TXT"));
        //  rechargeBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        autofitEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        autofitEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        rechargeBox.setBackgroundResource(android.R.color.transparent);
        rechargeBox.setHideUnderline(true);
        rechargeBox.setTextSize(getActContext().getResources().getDimension(R.dimen._18ssp));
        autofitEditText.setText(defaultAmountVal);
        btn_type4.setEnabled(false);
        autofitEditText.setTextColor(getActContext().getResources().getColor(R.color.black));
        rechargeBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ImageView minusImageView = (ImageView) dialog_transfer.findViewById(R.id.minusImageView);
        ImageView addImageView = (ImageView) dialog_transfer.findViewById(R.id.addImageView);

        addImageView.setOnClickListener(view -> mangePluseView(autofitEditText));
        minusImageView.setOnClickListener(view -> mangeMinusView(autofitEditText));



        /*Go Pay Label Start*/
        sendMoneyTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_MONEY"));
        whomTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TRANSFER_TO_WHOM"));
        transferMoneyTagTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_MONEY_TXT1"));

        otpInfoTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TRANSFER_WALLET_OTP_INFO_TXT"));
        otpInfoTxt.setVisibility(View.VISIBLE);

        String lblDriver = "LBL_DRIVER";
        if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) || generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
            lblDriver = "LBL_PROVIDER";
        }
        driverRadioBtn.setText(generalFunc.retrieveLangLBl("", lblDriver));
        userRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_RIDER"));

        detailBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_GO_PAY_EMAIL_OR_PHONE_TXT"));

        otpverificationCodeBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_ENTER_GOPAY_VERIFICATION_CODE"));
        moneyTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TRANSFER_MONEY_TXT"));


        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR_TXT");
        error_verification_code = generalFunc.retrieveLangLBl("", "LBL_VERIFICATION_CODE_INVALID");

        btn_type4.setId(Utils.generateViewId());
        /*Go Pay Label End*/





        /*Go Pay view initialization end*/



        /*Go Pay view Click handling Start*/

        btn_type3.setOnClickListener(v -> {

            transferState = "Search";


            if (rg_whomType.getCheckedRadioButtonId() != driverRadioBtn.getId() && rg_whomType.getCheckedRadioButtonId() != userRadioBtn.getId()) {
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_SELECT_ANY_MEMBER_OPTION_TXT"));
                return;
            }

            boolean detailEntered = Utils.checkText(detailBox) ? true : Utils.setErrorFields(detailBox, required_str);
            if (detailEntered == false) {
                return;
            }
            String regexStr = "^[0-9]*$";

            if (detailBox.getText().toString().trim().replace("+", "").matches(regexStr)) {
                if (detailEntered) {
                    detailEntered = detailBox.length() >= 3 ? true : Utils.setErrorFields(detailBox, generalFunc.retrieveLangLBl("", "LBL_INVALID_MOBILE_NO"));
                }

            } else {
                detailEntered = Utils.checkText(detailBox) ?
                        (generalFunc.isEmailValid(Utils.getText(detailBox)) ? true : Utils.setErrorFields(detailBox, error_email_str))
                        : Utils.setErrorFields(detailBox, required_str);


            }
            if (detailEntered == false) {
                return;
            }


            detailBoxVal = Utils.getText(detailBox);

            transferMoneyToWallet();

        });

        resendOtpArea.setOnClickListener(v -> {

            if (!isClicked) {
                isClicked = true;
                isRegenerate = "Yes";
                transferState = "ENTER_AMOUNT";
                transferMoneyToWallet();
            }
        });
        btn_type4.setOnClickListener(v -> {


            if (Utils.checkText(autofitEditText) == true && GeneralFunctions.parseDoubleValue(0, autofitEditText.getText().toString()) > 0) {
            } else {
                return;

            }


            Double moneyAdded = 0.0;

            if (Utils.checkText(autofitEditText) == true) {

                moneyAdded = generalFunc.parseDoubleValue(0, Utils.getText(autofitEditText));
            }
            boolean addMoneyAmountEntered = Utils.checkText(autofitEditText) ? (moneyAdded > 0 ? true : Utils.setErrorFields(autofitEditText, error_money_str))
                    : Utils.setErrorFields(autofitEditText, required_str);

            if (addMoneyAmountEntered == false) {
                return;
            }

            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
            generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_CONFIRM_TRANSFER_TO_WALLET_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_CONFIRM_TRANSFER_TO_WALLET_TXT1") + " " + username);
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
            generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
            generateAlert.setBtnClickList(btn_id -> {

                if (btn_id == 1) {
                    transferMoneyToWallet();

                } else {
                    generateAlert.closeAlertBox();
                }

            });

            generateAlert.showAlertBox();


        });

        btn_otp.setOnClickListener(v -> {

            boolean isCodeEntered = Utils.checkText(otpverificationCodeBox) ?
                    ((verificationCode.equalsIgnoreCase(Utils.getText(otpverificationCodeBox))) ? true
                            : Utils.setErrorFields(otpverificationCodeBox, error_verification_code)) : Utils.setErrorFields(otpverificationCodeBox, required_str);
            if (isCodeEntered == false) {
                return;
            }
            transferMoneyToWallet();
        });

        autofitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.d("onTextChanged", "::called");
                manageButton(btn_type4, autofitEditText);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (autofitEditText.getText().length() == 1) {
                    if (autofitEditText.getText().toString().contains(".")) {
                        autofitEditText.setText("0.");
                        autofitEditText.setSelection(autofitEditText.length());
                    }
                }

            }
        });


        /*Go Pay view Click handling End*/

        cancelTxt.setOnClickListener(view -> dialog_transfer.dismiss());
        cancelTransTxt.setOnClickListener(view -> {
//            rechargeBox.setText("0.0");
//            transferMoneyToWallet.setVisibility(View.VISIBLE);
//            addTransferArea.setVisibility(View.GONE);
//            ProfileImageArea.setVisibility(View.GONE);
//            transferState = "Search";

            dialog_transfer.dismiss();
            //goback();
        });

        backTansImage.setOnClickListener(view -> {

            if (otpArea.getVisibility() == View.VISIBLE) {

                moneyArea.setVisibility(View.VISIBLE);
                otpArea.setVisibility(View.GONE);
                transferState = "ENTER_AMOUNT";
            } else {
                autofitEditText.setText(defaultAmountVal);
                transferMoneyToWallet.setVisibility(View.VISIBLE);
                addTransferArea.setVisibility(View.GONE);
                ProfileImageArea.setVisibility(View.GONE);
                transferState = "Search";

            }
        });

        cancelOtpTxt.setOnClickListener(view -> {
//            moneyArea.setVisibility(View.VISIBLE);
//            otpArea.setVisibility(View.GONE);
//            transferState = "ENTER_AMOUNT";
            //goback();
            dialog_transfer.dismiss();
        });

        dialog_transfer.setCanceledOnTouchOutside(true);
        Window window = dialog_transfer.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog_transfer.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (generalFunc.isRTLmode()) {
            dialog_transfer.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        dialog_transfer.show();

    }

    private void transferMoneyToWallet() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("fromUserId", generalFunc.getMemberId());
        parameters.put("fromUserType", Utils.userType);
        //parameters.put("transferType", emailRadioBtn.isChecked() ? "Email" : "Phone");
        parameters.put("searchUserType", userRadioBtn.isChecked() ? Utils.userType : "Driver");
        parameters.put("UserType", Utils.userType);
        if (transferState.equalsIgnoreCase("SEARCH")) {
            parameters.put("type", "GopayCheckPhoneEmail");
            parameters.put("vPhoneOrEmailTxt", detailBoxVal);
        } else if (transferState.equalsIgnoreCase("ENTER_AMOUNT")) {
            parameters.put("type", "GoPayVerifyAmount");
            parameters.put("isRegenerate", isRegenerate);
            parameters.put("fAmount", Utils.getText(autofitEditText));
            parameters.put("toUserId", iUserId);
            parameters.put("toUserType", eUserType);
        } else if (transferState.equalsIgnoreCase("VERIFY")) {
            parameters.put("type", "GoPayTransferAmount");
            parameters.put("toUserId", iUserId);
            parameters.put("toUserType", eUserType);
            parameters.put("fAmount", Utils.getText(autofitEditText));
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (isRegenerate.equalsIgnoreCase("Yes")) {
                isClicked = false;
            }

            if (responseString != null && !responseString.equals("")) {
                String action = generalFunc.getJsonValue(Utils.action_str, responseString);
                if (action.equals("1")) {
                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                    if (transferState.equalsIgnoreCase("SEARCH")) {

                        iUserId = generalFunc.getJsonValue("iUserId", message);
                        eUserType = generalFunc.getJsonValue("eUserType", message);

                        username = generalFunc.getJsonValue("vName", message);
                        userImage = generalFunc.getJsonValue("vImgName", message);
                        userEmail = generalFunc.getJsonValue("vEmail", message);
                        userPhone = generalFunc.getJsonValue("vPhone", message);

                        //  transferMap.put("eUserTypeLBl", eUserType.equalsIgnoreCase("driver") ? generalFunc.retrieveLangLBl("", "LBL_DRIVER") : generalFunc.retrieveLangLBl("", "LBL_RIDER"));
                        if (btn_type4 != null) {
                            btn_type4.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_TO") + " " + username);
                        }
                        transferState = "ENTER_AMOUNT";
                        configureView();
                    } else if (transferState.equalsIgnoreCase("ENTER_AMOUNT")) {
                        if (isRegenerate.equalsIgnoreCase("Yes")) {
                            otpverificationCodeBox.setText("");
                            isRegenerate = "No";
                            resendOtpArea.setVisibility(View.GONE);
                            resendOtpArea.setOnClickListener(null);
                        }
                        transferState = "VERIFY";

                        verificationCode = generalFunc.getJsonValue("verificationCode", message);
                        String amount = String.format("%.2f", (double) generalFunc.parseDoubleValue(0.00, Utils.getText(autofitEditText)));
                        this.amount = generalFunc.getJsonValue("CurrencySymbol", userProfileJson) + "" + generalFunc.convertNumberWithRTL(amount);
                        ;
                        //transferMap.put("fAmount", generalFunc.getJsonValue("CurrencySymbol", userProfileJson) + "" + generalFunc.convertNumberWithRTL(amount));
                        configureView();
                    } else if (transferState.equalsIgnoreCase("VERIFY")) {
                        if (isRegenerate.equalsIgnoreCase("Yes")) {
                            isRegenerate = "No";
                            resendOtpArea.setVisibility(View.GONE);
                            resendOtpArea.setOnClickListener(null);
                        }


                        successDialog(generalFunc.retrieveLangLBl("", message), generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
                    }
                } else {

                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                    String showAddMoney = generalFunc.getJsonValue("showAddMoney", responseString);

                    if (transferState.equalsIgnoreCase("ENTER_AMOUNT") && (message.equalsIgnoreCase("LBL_WALLET_AMOUNT_GREATER_THAN_ZERO") || showAddMoney.equalsIgnoreCase("Yes"))) {
                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", message));

                        boolean isOnlyCashEnabled = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson).equalsIgnoreCase("Cash");

                        if (!isOnlyCashEnabled) {
                            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"));
                        }
                        generateAlert.setNegativeBtn(!isOnlyCashEnabled ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : generalFunc.retrieveLangLBl("", "LBL_OK"));

                        generateAlert.setBtnClickList(btn_id -> {

                            if (btn_id == 1) {
                                generateAlert.closeAlertBox();
                                //must change


                                openAddMoneyDialog();
                                dialog_transfer.dismiss();
                            } else {
                                generateAlert.closeAlertBox();
                            }

                        });

                        generateAlert.showAlertBox();
                        return;
                    } else if (transferState.equalsIgnoreCase("VERIFY")) {

                        if (message.equalsIgnoreCase("LBL_OTP_EXPIRED")) {
                            isRegenerate = "Yes";
                            resendOtpArea.setVisibility(View.VISIBLE);

                            return;
                        }
                        //manage new for sucess dialog

                        // removeValues(true);
                        if (dialog_transfer != null) {
                            dialog_transfer.dismiss();
                        }
                        // successDialog(action.equalsIgnoreCase("2") ? message : generalFunc.retrieveLangLBl("", message), generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue("message_profile_data", responseString));

                        transactionDate = generalFunc.getJsonValue("transactionDate", responseString);

                        openSucessDialog();
                    } else {

                        generalFunc.showGeneralMessage("", action.equalsIgnoreCase("2") ? message : generalFunc.retrieveLangLBl("", message));
                    }


                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();

    }

    private void successDialog(String message, String positiveBtnTxt) {
        if (isRegenerate.equalsIgnoreCase("yes")) {
            CustomDialog customDialog = new CustomDialog(getActContext());
            customDialog.setDetails(""/*generalFunc.retrieveLangLBl("","LBL_OTP_EXPIRED_TXT")*/, message, positiveBtnTxt, "", false, R.drawable.ic_hand_gesture, false, 2);
            customDialog.setRoundedViewBackgroundColor(R.color.appThemeColor_1);
            customDialog.setRoundedViewBorderColor(R.color.white);
            customDialog.setImgStrokWidth(15);
            customDialog.setBtnRadius(10);
            customDialog.setIconTintColor(R.color.white);
            customDialog.setPositiveBtnBackColor(R.color.appThemeColor_2);
            customDialog.setPositiveBtnTextColor(R.color.white);
            customDialog.createDialog();
            customDialog.setPositiveButtonClick(new com.general.files.Closure() {
                @Override
                public void exec() {
                    otpverificationCodeBox.setText("");
                    resendOtpArea.setVisibility(View.VISIBLE);
                    resendOtpArea.setOnClickListener(new setOnClickList());
                }
            });
            customDialog.setNegativeButtonClick(new com.general.files.Closure() {
                @Override
                public void exec() {

                }
            });
            customDialog.show();

        } else {
            CustomDialog customDialog = new CustomDialog(getActContext());
            customDialog.setDetails(""/*generalFunc.retrieveLangLBl("","LBL_MONEY_TRANSFER_CONFIRMATION_TITLE_TXT")*/, message, positiveBtnTxt, "", false, R.drawable.ic_correct, false, 2);
            customDialog.setRoundedViewBackgroundColor(R.color.appThemeColor_1);
            customDialog.setRoundedViewBorderColor(R.color.white);
            customDialog.setImgStrokWidth(15);
            customDialog.setBtnRadius(10);
            customDialog.setIconTintColor(R.color.white);
            customDialog.setPositiveBtnBackColor(R.color.appThemeColor_2);
            customDialog.setPositiveBtnTextColor(R.color.white);
            customDialog.createDialog();
            customDialog.setPositiveButtonClick(new com.general.files.Closure() {
                @Override
                public void exec() {
                    transferState = "SEARCH";
                    configureView();
                    generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "Yes");
                    list.clear();
                    getRecentTransction(false);
                    //getWalletBalDetails();
                }
            });
            customDialog.setNegativeButtonClick(new com.general.files.Closure() {
                @Override
                public void exec() {

                }
            });
            customDialog.show();
        }
    }

    public void getTransactionHistory(final boolean isLoadMore) {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);

        }
        if (loading_wallet_history.getVisibility() != View.VISIBLE && isLoadMore == false) {
            loading_wallet_history.setVisibility(View.VISIBLE);

        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getTransactionHistory");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        //parameters.put("TimeZone", generalFunc.getTimezone());
        if (isLoadMore == true) {
            parameters.put("page", next_page_str);
        }

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                closeLoader();


                String LBL_BALANCE = generalFunc.getJsonValue("user_available_balance", responseString);

                ((MTextView) view.findViewById(R.id.yourBalTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_USER_BALANCE"));
                ((MTextView) view.findViewById(R.id.walletamountTxt)).setText(generalFunc.convertNumberWithRTL(LBL_BALANCE));


            } else {
                if (isLoadMore == false) {
                    generateErrorView();
                }

            }

            mIsLoading = false;
        });
        exeWebServer.execute();

    }

    public void closeLoader() {
        if (loading_wallet_history.getVisibility() == View.VISIBLE) {
            loading_wallet_history.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);

        }
        errorView.setOnRetryListener(() -> getTransactionHistory(false));
    }

    private void configureView() {


        if (transferState.equalsIgnoreCase("SEARCH")) {
            btn_type3.setText(generalFunc.retrieveLangLBl("", "LBL_Search"));
        } else if (transferState.equalsIgnoreCase("ENTER_AMOUNT")) {

            userNameTxt.setText(username);


            Picasso.get().load(userImage).placeholder(R.mipmap.ic_no_pic_user).into(toUserImgView);

            // btn_type3.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_PAYMENT_TXT"));

            transferMoneyToWallet.setVisibility(View.GONE);
            addTransferArea.setVisibility(View.VISIBLE);
            ProfileImageArea.setVisibility(View.VISIBLE);


        } else if (transferState.equalsIgnoreCase("VERIFY")) {
            // ((MTextView) findViewById(R.id.moneyAmountTxt)).setText(transferMap.containsKey("fAmount") ? transferMap.get("fAmount") : "");
            btn_type3.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_SUBMIT_TXT"));
            otpArea.setVisibility(View.VISIBLE);
            moneyArea.setVisibility(View.GONE);
        }

    }

    MaterialEditText rechargeBox;
    AutoFitEditText autofitEditText;
    //Valor mínimo by Victor Nogueira
    String defaultAmountVal = "10.00";

    public void openAddMoneyDialog() {
        dialog_add_money = new Dialog(getActContext(), R.style.ImageSourceDialogStyle);
        dialog_add_money.setContentView(R.layout.add_money_layout);
        MTextView titleTxt = (MTextView) dialog_add_money.findViewById(R.id.titleTxt);
        MTextView addMoneyNote = (MTextView) dialog_add_money.findViewById(R.id.addMoneyNote);
        ImageView minusImageView = (ImageView) dialog_add_money.findViewById(R.id.minusImageView);
        ImageView addImageView = (ImageView) dialog_add_money.findViewById(R.id.addImageView);
        MTextView addMoneybtn1 = (MTextView) dialog_add_money.findViewById(R.id.addMoneybtn1);
        MTextView addMoneybtn2 = (MTextView) dialog_add_money.findViewById(R.id.addMoneybtn2);
        MTextView addMoneybtn3 = (MTextView) dialog_add_money.findViewById(R.id.addMoneybtn3);
        MTextView cancelTxt = (MTextView) dialog_add_money.findViewById(R.id.cancelTxt);
        MTextView currencyTxt = (MTextView) dialog_add_money.findViewById(R.id.currencyTxt);
        autofitEditText = (AutoFitEditText) dialog_add_money.findViewById(R.id.autofitEditText);


        rechargeBox = (MaterialEditText) dialog_add_money.findViewById(R.id.rechargeBox);


        //rechargeBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_RECHARGE_AMOUNT_TXT"), generalFunc.retrieveLangLBl("", "LBL_RECHARGE_AMOUNT_TXT"));
        //  rechargeBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        autofitEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        currencyTxt.setText(generalFunc.getJsonValue("vCurrencyPassenger", userProfileJson));

        autofitEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        rechargeBox.setBackgroundResource(android.R.color.transparent);
        rechargeBox.setHideUnderline(true);
        rechargeBox.setTextSize(getActContext().getResources().getDimension(R.dimen._18ssp));
        autofitEditText.setText(defaultAmountVal);

        rechargeBox.setTextColor(getActContext().getResources().getColor(R.color.black));
        rechargeBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"));
        addMoneyNote.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY_MSG"));


        addMoneybtn1.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("WALLET_FIXED_AMOUNT_1", userProfileJson)));
        addMoneybtn2.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("WALLET_FIXED_AMOUNT_2", userProfileJson)));
        addMoneybtn3.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("WALLET_FIXED_AMOUNT_3", userProfileJson)));


        MButton btn_type2 = ((MaterialRippleLayout) dialog_add_money.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setEnabled(false);
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_DONE"));

        cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));


        autofitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                manageButton(btn_type2, autofitEditText);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (autofitEditText.getText().length() == 1) {
                    if (autofitEditText.getText().toString().contains(".")) {
                        autofitEditText.setText("0.");
                        autofitEditText.setSelection(autofitEditText.length());
                    }
                }

            }
        });

        btn_type2.setOnClickListener(view -> manageButtonView(autofitEditText));
        cancelTxt.setOnClickListener(view -> dialog_add_money.dismiss());


        addImageView.setOnClickListener(view -> mangePluseView(autofitEditText));
        minusImageView.setOnClickListener(view -> mangeMinusView(autofitEditText));


        addMoneybtn1.setOnClickListener(v -> {

            autofitEditText.setText(String.format("%.2f", (double) (GeneralFunctions.parseDoubleValue(0.00, generalFunc.getJsonValue("WALLET_FIXED_AMOUNT_1", userProfileJson)))));
        });

        addMoneybtn2.setOnClickListener(v -> {


            autofitEditText.setText(String.format("%.2f", (double) (GeneralFunctions.parseDoubleValue(0.00, generalFunc.getJsonValue("WALLET_FIXED_AMOUNT_2", userProfileJson)))));
        });
        addMoneybtn3.setOnClickListener(v -> {

            autofitEditText.setText(String.format("%.2f", (double) (GeneralFunctions.parseDoubleValue(0.00, generalFunc.getJsonValue("WALLET_FIXED_AMOUNT_3", userProfileJson)))));
        });


        //dialog_add_money.setCanceledOnTouchOutside(true);
        Window window = dialog_add_money.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog_add_money.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (generalFunc.isRTLmode()) {
            dialog_add_money.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        dialog_add_money.show();
    }

    public void manageButton(MButton btn, AutoFitEditText editText) {
        if (Utils.checkText(editText)) {
            if (generalFunc.parseDoubleValue(0, Utils.getText(editText)) > 0) {
                btn.setEnabled(true);
            } else {
                btn.setEnabled(false);
            }
        } else {
            btn.setEnabled(false);
        }

    }


    public void openSucessDialog() {

        dialog_sucess = new Dialog(getActContext(), R.style.ImageSourceDialogStyle);
        dialog_sucess.setContentView(R.layout.sucess_layout);
        MTextView titleTxt = (MTextView) dialog_sucess.findViewById(R.id.titleTxt);
        MTextView msgTxt = (MTextView) dialog_sucess.findViewById(R.id.msgTxt);
        MTextView priceTxt = (MTextView) dialog_sucess.findViewById(R.id.priceTxt);
        MTextView nametxt = (MTextView) dialog_sucess.findViewById(R.id.nametxt);
        MTextView transDateTxt = (MTextView) dialog_sucess.findViewById(R.id.transDateTxt);
        MTextView transDateValTxt = (MTextView) dialog_sucess.findViewById(R.id.transDateValTxt);
        SelectableRoundedImageView UserImgView = (SelectableRoundedImageView) dialog_sucess.findViewById(R.id.UserImgView);

        transDateTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TRANSACTION_DONE"));
        transDateValTxt.setText(generalFunc.convertNumberWithRTL(transactionDate));

        msgTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_MONEY_TO") + " " + username);
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SUCCESSFULLY"));

        nametxt.setText(username);
        priceTxt.setText(amount);
        Picasso.get().load(userImage).placeholder(R.mipmap.ic_no_pic_user).into(UserImgView);

        MButton btn_type2 = ((MaterialRippleLayout) dialog_sucess.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_DONE"));


        btn_type2.setOnClickListener(view -> {
            removeValues(true);
            // getWalletBalDetails();
            list.clear();
            getRecentTransction(false);
            dialog_sucess.dismiss();
        });


        dialog_sucess.show();

    }

    private void removeValues(boolean removeValues) {
        if (removeValues) {
            //detailBox.setText("");
            //rechargeBox.setText("");
            otpverificationCodeBox.setText("");

            iUserId = "";
            eUserType = "";
            verificationCode = "";
            rg_whomType.clearCheck();
        }
    }

    public void manageButtonView(AutoFitEditText rechargeBox) {
        if (Utils.checkText(rechargeBox) == true && GeneralFunctions.parseDoubleValue(10.00, rechargeBox.getText().toString()) > 10.00) {

            checkValues(rechargeBox);
            rechargeBox.setText(defaultAmountVal);

        }
    }

    public void mangeMinusView(AutoFitEditText rechargeBox) {
        if (Utils.checkText(rechargeBox) == true && GeneralFunctions.parseDoubleValue(10.00, rechargeBox.getText().toString()) > 10.00) {
            Locale localeBR = new Locale("pt","BR");

            Double valor = Double.valueOf(rechargeBox.getText().toString());
            valor --;                        generalFunc.showGeneralMessage("",
                    //Corrigida a mensagem quando não se tem cartão cadastrado by Victor Nogueira
                    //generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    generalFunc.retrieveLangLBl("", "LBL_CADASTRE_UM_CARTAO"));
            NumberFormat dinheiro = NumberFormat.getCurrencyInstance(localeBR);
            NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);
            // rechargeBox.setText(numberFormat.format(valor));
            rechargeBox.setText(numberFormat.format(valor));
            // rechargeBox.setText(String.format("%.2f", (double) (GeneralFunctions.parseDoubleValue(0.0, rechargeBox.getText().toString()) - 1)));
            if (Utils.checkText(rechargeBox) == true && GeneralFunctions.parseDoubleValue(10.00, rechargeBox.getText().toString()) == 10.00) {
                //Mensagem de valor mínimo by Victor Nogueira
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(getActContext(), "" + generalFunc.retrieveLangLBl("", "LBL_VALOR_MINIMO"), Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(15);
                toastMessage.setTextColor(Color.WHITE);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toastView.setBackgroundColor(Color.RED);
                toast.show();
            }

        } else {
            rechargeBox.setText(defaultAmountVal);

        }
    }


    public void mangePluseView(AutoFitEditText rechargeBox) {
        if (Utils.checkText(rechargeBox) == true) {
            Locale localeBR = new Locale("pt","BR");

            Double valor = Double.valueOf(rechargeBox.getText().toString());
            valor ++;
            NumberFormat dinheiro = NumberFormat.getCurrencyInstance(localeBR);
            NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);
            // rechargeBox.setText(numberFormat.format(valor));
            rechargeBox.setText(numberFormat.format(valor));
            //rechargeBox.setText(String.format("%.2f", (double) (GeneralFunctions.parseDoubleValue(0.0, rechargeBox.getText().toString()) + 1)));


        } else {
            rechargeBox.setText("1.00");


        }
    }

    public void checkValues(AutoFitEditText rechargeBox) {

        Double moneyAdded = 0.0;

        if (Utils.checkText(rechargeBox) == true) {

            moneyAdded = generalFunc.parseDoubleValue(0, Utils.getText(rechargeBox));
        }
        boolean addMoneyAmountEntered = Utils.checkText(rechargeBox) ? (moneyAdded > 0 ? true : Utils.setErrorFields(rechargeBox, error_money_str))
                : Utils.setErrorFields(rechargeBox, required_str);

        if (addMoneyAmountEntered == false) {
            return;
        }

        /*if (generalFunc.isDeliverOnlyEnabled()) {
            Bundle bn = new Bundle();
            bn.putBoolean("isWallet", true);
            bn.putString("fAmount", Utils.getText(rechargeBox));
            new StartActProcess(getActContext()).startActForResult(PaymentCardActivity.class, bn, SEL_CARD);
        } else {*/

        if (generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
            if (generalFunc.isDeliverOnlyEnabled())
            {
                Bundle bn = new Bundle();
                bn.putString("fAmount", Utils.getText(rechargeBox));
                bn.putString("isCheckout", "");
                new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, SEL_CARD);

            }else {
                addMoneyToWallet(rechargeBox);
            }

        } else if (!generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
            dialog_add_money.dismiss();

            String url = CommonUtilities.PAYMENTLINK + "amount=" + Utils.getText(rechargeBox) + "&iUserId=" + generalFunc.getMemberId() + "&UserType=" + Utils.app_type + "&vUserDeviceCountry=" +
                    generalFunc.retrieveValue(Utils.DefaultCountryCode) + "&ccode=" + generalFunc.getJsonValue("vCurrencyPassenger", userProfileJson) + "&UniqueCode=" + System.currentTimeMillis();

            paymentWebview.setWebViewClient(new myWebClient());
            paymentWebview.getSettings().setJavaScriptEnabled(true);
            paymentWebview.loadUrl(url);
            paymentWebview.setFocusable(true);
            paymentWebview.setVisibility(View.VISIBLE);
            loaderView.setVisibility(View.VISIBLE);

        }
        // }
    }

    public class myWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            String data = url;
            Logger.d("WebData", "::" + data);
            data = data.substring(data.indexOf("data") + 5, data.length());
            try {

                String datajson = URLDecoder.decode(data, "UTF-8");
                Logger.d("WebData", "::2222222::" + datajson);
                loaderView.setVisibility(View.VISIBLE);

                view.setOnTouchListener(null);


                if (url.contains("success=1")) {

                    paymentWebview.setVisibility(View.GONE);
                    //rechargeBox.setText("");

                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_WALLET_MONEY_CREDITED")), "", generalFunc.retrieveLangLBl("", "LBL_OK"), i -> {
//                        isFinish = true;
                        list.clear();
                        getRecentTransction(false);
                        // getWalletBalDetails();
                    });
                }

                if (url.contains("success=0")) {

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            generalFunc.showError();
            loaderView.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loaderView.setVisibility(View.GONE);

            view.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            });

        }
    }

    private void addMoneyToWallet(AutoFitEditText rechargeBox) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "addMoneyUserWallet");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("fAmount", Utils.getText(rechargeBox));
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {

                    if (dialog_add_money != null) {
                        dialog_add_money.dismiss();
                    }

                    String memberBalance = generalFunc.getJsonValue("MemberBalance", responseString);
                    ((MTextView) view.findViewById(R.id.walletamountTxt)).setText(generalFunc.convertNumberWithRTL(memberBalance));

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));

                    list.clear();
                    getRecentTransction(false);
                } else {
                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                    if (message.equalsIgnoreCase("LBL_NO_CARD_AVAIL_NOTE")) {

                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_ADD_CARD"));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
                        generateAlert.setBtnClickList(btn_id -> {

                            if (btn_id == 1) {
                                if (dialog_add_money != null) {
                                    dialog_add_money.dismiss();
                                }
                                generateAlert.closeAlertBox();
                                Bundle bn = new Bundle();
                                new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);

                            } else {
                                generateAlert.closeAlertBox();
                            }

                        });

                        generateAlert.showAlertBox();

                    } else {
                        generalFunc.showGeneralMessage("",
                                //Corrigida a mensagem quando não se tem cartão cadastrado by Victor Nogueira
                                //generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                                generalFunc.retrieveLangLBl("", "LBL_CADASTRE_UM_CARTAO"));


                    }

                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();

    }


}
