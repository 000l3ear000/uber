package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.deliverAll.EditCartActivity;
import com.melevicarbrasil.usuario.deliverAll.LoginActivity;
import com.dialogs.OpenListView;
import com.general.files.AppFunctions;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.SetUserData;
import com.general.files.StartActProcess;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.livechatinc.inappchat.ChatWindowActivity;
import com.model.ContactModel;
import com.realmModel.Cart;
import com.realmModel.Options;
import com.realmModel.Topping;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

public class MyProfileNewActivity extends AppCompatActivity {
    public String userProfileJson = "";
    public JSONObject obj_userProfile;
    GeneralFunctions generalFunc;

    ImageView backImg, editProfileImage;
    SelectableRoundedImageView userImgView, userImgView_toolbar;
    MTextView userNameTxt, userNameTxt_toolbar, userEmailTxt, walletHTxt, walletVxt, generalSettingHTxt, accountHTxt;
    MTextView bookingTxt, inviteTxt, topupTxt;
    MTextView notificationHTxt, paymentHTxt, privacyHTxt, termsHTxt, myPaymentHTxt, mybookingHTxt, businessProfileHTxt, myCartHTxt, favDriverHTxt,
            addMoneyHTxt, sendMoneyHTxt, personalDetailsHTxt, changePasswordHTxt, changeCurrencyHTxt, changeLanguageHTxt, supportHTxt, livechatHTxt, contactUsHTxt;
    LinearLayout notificationArea, paymentMethodArea, privacyArea, myBookingArea, businessProfileArea, myCartArea, favDriverArea,
            addMoneyArea, sendMoneyArea, personalDetailsArea, changesPasswordArea, changesCurrancyArea, changeslanguageArea, termsArea, liveChatArea, contactUsArea;
    View notificationView, paymentView, rewardView, myBookingView, businessProView, mycartView, favDriverView, addMoneyView, aboutUsView, myWalletView,
            sendMoneyView, personalDetailsView, changePasswordView, changeCurrencyView, changeLangView, termsView, livechatView;
    LinearLayout bookingArea, inviteArea, topUpArea, logOutArea, workArea;
    LinearLayout myWalletArea, inviteFriendArea, helpArea, aboutusArea, homeArea, headerwalletArea, emeContactArea;
    MTextView mywalletHTxt, inviteHTxt, helpHTxt, aboutusHTxt, logoutTxt, otherHTxt, homeHTxt, workHTxt, favHTxt, workaddressTxt, homeaddressTxt, headerwalletTxt, emeContactHTxt;
    ImageView notificationArrow, paymentArrow, privacyArrow, termsArrow, mywalletArrow, inviteArrow, helpArrow, aboutusArrow,
            mybookingArrow, businessProArrow, mycartArrow, favDriverArrow, addMoneyArrow, sendMoneyArrow, personalDetailsArrow,
            changePasswordArrow, changeCurrencyArrow, changeLangArrow, livechatArrow, contactUsArrow, logoutArrow, homeArrow, workArrow, emeContactArrow;
    InternetConnection internetConnection;
    String selected_currency = "";
    String default_selected_currency = "";
    String selected_currency_symbol = "";
    ArrayList<HashMap<String, String>> language_list = new ArrayList<>();
    String selected_language_code = "";
    String default_selected_language_code = "";
    ArrayList<HashMap<String, String>> currency_list = new ArrayList<>();
    androidx.appcompat.app.AlertDialog alertDialog;
    String SITE_TYPE = "";
    String SITE_TYPE_DEMO_MSG = "";
    LinearLayout settingArea, walletArea, favArea, otherArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_new);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
        internetConnection = new InternetConnection(getActContext());
        initViews();
        setuserInfo();
        setLabel();
        buildLanguageList();
    }

    public void initViews() {
        backImg = findViewById(R.id.backImg);

        backImg = findViewById(R.id.backImg);

        settingArea = findViewById(R.id.settingArea);
        walletArea = findViewById(R.id.walletArea);
        otherArea = findViewById(R.id.otherArea);
        favArea = findViewById(R.id.favArea);
        editProfileImage = findViewById(R.id.editProfileImage);
        userImgView = findViewById(R.id.userImgView);
        userImgView_toolbar = findViewById(R.id.userImgView_toolbar);
        userNameTxt = findViewById(R.id.userNameTxt);
        userNameTxt_toolbar = findViewById(R.id.userNameTxt_toolbar);
        userEmailTxt = findViewById(R.id.userEmailTxt);
        walletHTxt = findViewById(R.id.walletHTxt);
        walletVxt = findViewById(R.id.walletVxt);
        bookingTxt = findViewById(R.id.bookingTxt);
        inviteTxt = findViewById(R.id.inviteTxt);
        topupTxt = findViewById(R.id.topupTxt);
        headerwalletTxt = findViewById(R.id.headerwalletTxt);
        emeContactHTxt = findViewById(R.id.emeContactHTxt);
        generalSettingHTxt = findViewById(R.id.generalSettingHTxt);
        accountHTxt = findViewById(R.id.accountHTxt);
        notificationHTxt = findViewById(R.id.notificationHTxt);
        paymentHTxt = findViewById(R.id.paymentHTxt);
        privacyHTxt = findViewById(R.id.privacyHTxt);
        termsHTxt = findViewById(R.id.termsHTxt);
        logoutTxt = findViewById(R.id.logoutTxt);
        otherHTxt = findViewById(R.id.otherHTxt);
        homeHTxt = findViewById(R.id.homeHTxt);
        workHTxt = findViewById(R.id.workHTxt);
        favHTxt = findViewById(R.id.favHTxt);
        workaddressTxt = findViewById(R.id.workaddressTxt);
        homeaddressTxt = findViewById(R.id.homeaddressTxt);
        notificationArea = findViewById(R.id.notificationArea);
        paymentMethodArea = findViewById(R.id.paymentMethodArea);
        privacyArea = findViewById(R.id.privacyArea);
        logoutTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LOGOUT"));
        otherHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT"));

        myBookingArea = findViewById(R.id.myBookingArea);
        businessProfileArea = findViewById(R.id.businessProfileArea);
        myCartArea = findViewById(R.id.myCartArea);
        favDriverArea = findViewById(R.id.favDriverArea);
        addMoneyArea = findViewById(R.id.addMoneyArea);
        sendMoneyArea = findViewById(R.id.sendMoneyArea);
        personalDetailsArea = findViewById(R.id.personalDetailsArea);
        changesPasswordArea = findViewById(R.id.changesPasswordArea);
        changesCurrancyArea = findViewById(R.id.changesCurrancyArea);
        changeslanguageArea = findViewById(R.id.changeslanguageArea);
        termsArea = findViewById(R.id.termsArea);
        liveChatArea = findViewById(R.id.liveChatArea);
        contactUsArea = findViewById(R.id.contactUsArea);
        notificationView = findViewById(R.id.notificationView);
        paymentView = findViewById(R.id.paymentView);
        rewardView = findViewById(R.id.rewardView);
        myBookingView = findViewById(R.id.myBookingView);
        businessProView = findViewById(R.id.businessProView);
        mycartView = findViewById(R.id.mycartView);
        favDriverView = findViewById(R.id.favDriverView);
        addMoneyView = findViewById(R.id.addMoneyView);
        aboutUsView = findViewById(R.id.aboutUsView);
        myWalletView = findViewById(R.id.myWalletView);
        sendMoneyView = findViewById(R.id.sendMoneyView);
        personalDetailsView = findViewById(R.id.personalDetailsView);
        changePasswordView = findViewById(R.id.personalDetailsView);
        changeCurrencyView = findViewById(R.id.changeCurrencyView);
        changeLangView = findViewById(R.id.changeLangView);
        termsView = findViewById(R.id.termsView);
        livechatView = findViewById(R.id.livechatView);
        bookingArea = findViewById(R.id.bookingArea);
        inviteArea = findViewById(R.id.inviteArea);
        topUpArea = findViewById(R.id.topUpArea);

        logOutArea = findViewById(R.id.logOutArea);
        workArea = findViewById(R.id.workArea);
        myPaymentHTxt = findViewById(R.id.myPaymentHTxt);
        mybookingHTxt = findViewById(R.id.mybookingHTxt);
        businessProfileHTxt = findViewById(R.id.businessProfileHTxt);
        myCartHTxt = findViewById(R.id.myCartHTxt);
        favDriverHTxt = findViewById(R.id.favDriverHTxt);
        addMoneyHTxt = findViewById(R.id.addMoneyHTxt);
        sendMoneyHTxt = findViewById(R.id.sendMoneyHTxt);
        personalDetailsHTxt = findViewById(R.id.personalDetailsHTxt);
        changePasswordHTxt = findViewById(R.id.changePasswordHTxt);
        changeCurrencyHTxt = findViewById(R.id.changeCurrencyHTxt);
        changeLanguageHTxt = findViewById(R.id.changeLanguageHTxt);
        supportHTxt = findViewById(R.id.supportHTxt);
        livechatHTxt = findViewById(R.id.livechatHTxt);
        contactUsHTxt = findViewById(R.id.contactUsHTxt);
        myWalletArea = findViewById(R.id.myWalletArea);
        headerwalletArea = findViewById(R.id.headerwalletArea);
        emeContactArea = findViewById(R.id.emeContactArea);
        inviteFriendArea = findViewById(R.id.inviteFriendArea);
        helpArea = findViewById(R.id.helpArea);
        aboutusArea = findViewById(R.id.aboutusArea);
        homeArea = findViewById(R.id.homeArea);
        notificationArrow = findViewById(R.id.notificationArrow);
        paymentArrow = findViewById(R.id.paymentArrow);
        privacyArrow = findViewById(R.id.privacyArrow);
        termsArrow = findViewById(R.id.termsArrow);
        mywalletArrow = findViewById(R.id.mywalletArrow);
        inviteArrow = findViewById(R.id.inviteArrow);
        helpArrow = findViewById(R.id.helpArrow);
        aboutusArrow = findViewById(R.id.aboutusArrow);
        mybookingArrow = findViewById(R.id.mybookingArrow);
        businessProArrow = findViewById(R.id.businessProArrow);
        mycartArrow = findViewById(R.id.mycartArrow);
        favDriverArrow = findViewById(R.id.favDriverArrow);
        addMoneyArrow = findViewById(R.id.addMoneyArrow);
        sendMoneyArrow = findViewById(R.id.sendMoneyArrow);
        personalDetailsArrow = findViewById(R.id.personalDetailsArrow);
        changePasswordArrow = findViewById(R.id.changePasswordArrow);
        changeCurrencyArrow = findViewById(R.id.changeCurrencyArrow);
        changeLangArrow = findViewById(R.id.changeLangArrow);
        livechatArrow = findViewById(R.id.livechatArrow);
        contactUsArrow = findViewById(R.id.contactUsArrow);
        logoutArrow = findViewById(R.id.logoutArrow);
        homeArrow = findViewById(R.id.homeArrow);
        workArrow = findViewById(R.id.workArrow);
        emeContactArrow = findViewById(R.id.emeContactArrow);


        mywalletHTxt = findViewById(R.id.mywalletHTxt);
        inviteHTxt = findViewById(R.id.inviteHTxt);
        helpHTxt = findViewById(R.id.helpHTxt);
        aboutusHTxt = findViewById(R.id.aboutusHTxt);


        notificationArea.setOnClickListener(new setOnClickList());
        paymentMethodArea.setOnClickListener(new setOnClickList());
        privacyArea.setOnClickListener(new setOnClickList());
        emeContactArea.setOnClickListener(new setOnClickList());

        myBookingArea.setOnClickListener(new setOnClickList());
        businessProfileArea.setOnClickListener(new setOnClickList());
        bookingArea.setOnClickListener(new setOnClickList());
        inviteArea.setOnClickListener(new setOnClickList());
        topUpArea.setOnClickListener(new setOnClickList());

        logOutArea.setOnClickListener(new setOnClickList());
        workArea.setOnClickListener(new setOnClickList());
        myWalletArea.setOnClickListener(new setOnClickList());
        headerwalletArea.setOnClickListener(new setOnClickList());
        inviteFriendArea.setOnClickListener(new setOnClickList());
        helpArea.setOnClickListener(new setOnClickList());
        aboutusArea.setOnClickListener(new setOnClickList());
        homeArea.setOnClickListener(new setOnClickList());
        backImg.setOnClickListener(new setOnClickList());
        myCartArea.setOnClickListener(new setOnClickList());
        favDriverArea.setOnClickListener(new setOnClickList());
        addMoneyArea.setOnClickListener(new setOnClickList());
        sendMoneyArea.setOnClickListener(new setOnClickList());
        personalDetailsArea.setOnClickListener(new setOnClickList());
        changesPasswordArea.setOnClickListener(new setOnClickList());
        changesCurrancyArea.setOnClickListener(new setOnClickList());
        changeslanguageArea.setOnClickListener(new setOnClickList());
        termsArea.setOnClickListener(new setOnClickList());
        liveChatArea.setOnClickListener(new setOnClickList());
        contactUsArea.setOnClickListener(new setOnClickList());
        editProfileImage.setOnClickListener(new setOnClickList());


        if (generalFunc.isRTLmode()) {
            backImg.setRotation(0);
            notificationArrow.setRotation(180);
            paymentArrow.setRotation(180);
            privacyArrow.setRotation(180);
            termsArrow.setRotation(180);
            mywalletArrow.setRotation(180);
            inviteArrow.setRotation(180);
            helpArrow.setRotation(180);
            aboutusArrow.setRotation(180);
            mybookingArrow.setRotation(180);
            businessProArrow.setRotation(180);
            mycartArrow.setRotation(180);
            favDriverArrow.setRotation(180);
            addMoneyArrow.setRotation(180);
            sendMoneyArrow.setRotation(180);
            personalDetailsArrow.setRotation(180);
            changePasswordArrow.setRotation(180);
            changeCurrencyArrow.setRotation(180);
            changeLangArrow.setRotation(180);
            livechatArrow.setRotation(180);
            contactUsArrow.setRotation(180);
            logoutArrow.setRotation(180);
            homeArrow.setRotation(180);
            workArrow.setRotation(180);
            emeContactArrow.setRotation(180);
        }


    }

    public void setLabel() {
        walletHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE"));
        emeContactHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_EMERGENCY_CONTACT"));
        topupTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TOP_UP"));
        headerwalletTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_TXT"));
        inviteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_INVITE"));
        bookingTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BOOKING"));
        generalSettingHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GENERAL_SETTING"));
        notificationHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NOTIFICATIONS"));
        paymentHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYMENT_METHOD"));
        privacyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRIVACY_POLICY_TEXT"));
        termsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION"));
        myPaymentHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYMENT"));
        mywalletHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_WALLET"));
        inviteHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_INVITE_FRIEND_TXT"));
        // helpHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HELP_CENTER"));
        helpHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAQ_TXT"));
        aboutusHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ABOUT_US_TXT"));
        mybookingHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_BOOKINGS"));
        businessProfileHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BUSINESS_PROFILE"));
        myCartHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_CART"));
        favDriverHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAV_DRIVER"));
        addMoneyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"));
        sendMoneyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_MONEY"));
        accountHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_SETTING"));
        personalDetailsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PERSONAL_DETAILS"));
        changePasswordHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_PASSWORD_TXT"));
        changeCurrencyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_CURRENCY"));
        changeLanguageHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_LANGUAGE"));
        supportHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SUPPORT"));
        livechatHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LIVE_CHAT"));
        contactUsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));
        logoutTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LOGOUT"));
        otherHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT"));
        favHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAV_LOCATIONS"));
        homeHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_HOME_PLACE_TXT"));
        workHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_WORK_PLACE_TXT"));


    }


    public void setuserInfo() {


        userNameTxt.setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        userEmailTxt.setText(generalFunc.getJsonValueStr("vEmail", obj_userProfile));

        (new AppFunctions(getActContext())).checkProfileImage(userImgView, userProfileJson, "vImgName");
        walletVxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));

    }

    public void buildLanguageList() {
        JSONArray languageList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.LANGUAGE_LIST_KEY));

        for (int i = 0; i < languageList_arr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(languageList_arr, i);


            if ((generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY)).equals(generalFunc.getJsonValueStr("vCode", obj_temp))) {
                selected_language_code = generalFunc.getJsonValueStr("vCode", obj_temp);

                default_selected_language_code = selected_language_code;
                selLanguagePosition = i;
            }

            HashMap<String, String> data = new HashMap<>();
            data.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
            data.put("vCode", generalFunc.getJsonValueStr("vCode", obj_temp));


            language_list.add(data);
        }
        if (language_list.size() < 2 /*|| generalFunc.retrieveValue("LANGUAGE_OPTIONAL").equalsIgnoreCase("Yes")*/) {
            changeslanguageArea.setVisibility(View.GONE);
        } else {
            changeslanguageArea.setVisibility(View.VISIBLE);

        }

        buildCurrencyList();

    }




    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            Bundle bn = new Bundle();
            switch (view.getId()) {
                case R.id.backImg:
                    onBackPressed();
                    break;
                case R.id.personalDetailsArea:
                case R.id.editProfileImage:
                    if (generalFunc.getMemberId().equals("")) {
                        new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    } else {
                        new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    }
                    break;
                case R.id.bookingArea:
                case R.id.myBookingArea:
                    if (generalFunc.isDeliverOnlyEnabled() && generalFunc.getMemberId().equals("")) {
                        new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    } else {
                        new StartActProcess(getActContext()).startAct(BookingActivity.class);
                    }
                    /*if (generalFunc.getMemberId().equals("")) {
                        new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    } else {
                        new StartActProcess(getActContext()).startAct(ActiveOrderActivity.class);
                    }*/
                    break;
                case R.id.businessProfileArea:
                    new StartActProcess(getActContext()).startActWithData(BusinessProfileActivity.class, bn);
                    break;

                case R.id.myCartArea:
                    new StartActProcess(getActContext()).startAct(EditCartActivity.class);
                    break;

                case R.id.emeContactArea:
                    new StartActProcess(getActContext()).startAct(EmergencyContactActivity.class);

                    break;

                case R.id.notificationArea:
                    new StartActProcess(getActContext()).startAct(NotificationActivity.class);
                    break;
                case R.id.favDriverArea:
                    new StartActProcess(getActContext()).startActWithData(FavouriteDriverActivity.class, bn);
                    break;

                case R.id.paymentMethodArea:
                    bn.putBoolean("fromcabselection", false);
                    new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
                    break;
                case R.id.privacyArea:
                    bn.putString("staticpage", "33");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;

                case R.id.promocodeArea:
                    break;
                case R.id.changesPasswordArea:
                    showPasswordBox();
                    break;
                case R.id.changesCurrancyArea:
                    showCurrencyList();
                    break;
                case R.id.changeslanguageArea:
                    showLanguageList();
                    break;
                case R.id.termsArea:
                    bn.putString("staticpage", "4");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;

                case R.id.topUpArea:
                    break;

                case R.id.headerwalletArea:
                case R.id.myWalletArea:
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.addMoneyArea:
                    bn.putBoolean("isAddMoney", true);
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.sendMoneyArea:
                    bn.putBoolean("isSendMoney", true);
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.inviteArea:
                case R.id.inviteFriendArea:
                    if (generalFunc.isDeliverOnlyEnabled()) {
                        if (generalFunc.getMemberId().equals("")) {
                            new StartActProcess(getActContext()).startAct(LoginActivity.class);
                        } else {
                            new StartActProcess(getActContext()).startActWithData(InviteFriendsActivity.class, bn);
                        }
                    } else {
                        new StartActProcess(getActContext()).startActWithData(InviteFriendsActivity.class, bn);
                    }

                    break;
                case R.id.helpArea:
                    new StartActProcess(getActContext()).startAct(HelpActivity.class);
                    break;
                case R.id.liveChatArea:
                    startChatActivity();
                    break;
                case R.id.aboutusArea:
                    bn.putString("staticpage", "1");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;
                case R.id.contactUsArea:
                    new StartActProcess(getActContext()).startAct(ContactUsActivity.class);
                    break;
                case R.id.logOutArea:

                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {
                        if (btn_id == 0) {
                            generateAlert.closeAlertBox();
                        } else {
                            if (internetConnection.isNetworkConnected()) {
                                MyApp.getInstance().logOutFromDevice(false);
                            } else {
                                generalFunc.showMessage(logOutArea, generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
                            }
                        }

                    });
                    generateAlert.setContentMessage(generalFunc.retrieveLangLBl("Logout", "LBL_LOGOUT"), generalFunc.retrieveLangLBl("Are you sure you want to logout?", "LBL_WANT_LOGOUT_APP_TXT"));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
                    generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
                    generateAlert.showAlertBox();
                    break;
                case R.id.homeArea:

                    if (generalFunc.getMemberId().equals("")) {
                        new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    } else {
                        bn.putString("isHome", "true");
                        new StartActProcess(getActContext()).startActForResult(SearchPickupLocationActivity.class, bn,
                                Utils.ADD_HOME_LOC_REQ_CODE);
                    }

                    break;
                case R.id.workArea:
                    if (generalFunc.getMemberId().equals("")) {
                        new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    } else {
                        bn.putString("isWork", "true");
                        new StartActProcess(getActContext()).startActForResult(SearchPickupLocationActivity.class, bn,
                                Utils.ADD_WORK_LOC_REQ_CODE);
                    }

                    break;
                case R.id.signSignUpTxt:
                    new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    break;

            }
        }
    }

    int selCurrancyPosition = -1;
    int selLanguagePosition = -1;

    public String getSelectLangText() {
        return ("" + generalFunc.retrieveLangLBl("Select", "LBL_SELECT_LANGUAGE_HINT_TXT"));
    }

    public void showLanguageList() {
        // list_language.show();

        OpenListView.getInstance(getActContext(), getSelectLangText(), language_list, OpenListView.OpenDirection.CENTER, true, position -> {


            selLanguagePosition = position;
            selected_language_code = language_list.get(selLanguagePosition).get("vCode");
            generalFunc.storeData(Utils.DEFAULT_LANGUAGE_VALUE, language_list.get(selLanguagePosition).get("vTitle"));

            if (generalFunc.getMemberId().equalsIgnoreCase("")) {
                generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
                changeLanguagedata(selected_language_code);
            } else {
                updateProfile();
            }
        }).show(selLanguagePosition, "vTitle");
    }

    public void showCurrencyList() {
        //  list_currency.show();


        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SELECT_CURRENCY"), currency_list, OpenListView.OpenDirection.CENTER, true, position -> {


            selCurrancyPosition = position;
            selected_currency_symbol = currency_list.get(selCurrancyPosition).get("vSymbol");
            selected_currency = currency_list.get(selCurrancyPosition).get("vName");
            if (generalFunc.getMemberId().equalsIgnoreCase("")) {
                generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
                changeLanguagedata(selected_language_code);
            } else {
                updateProfile();
            }
        }).show(selCurrancyPosition, "vName");
    }

    private void startChatActivity() {

        String vName = generalFunc.getJsonValue("vName", userProfileJson);
        String vLastName = generalFunc.getJsonValue("vLastName", userProfileJson);

        String driverName = vName + " " + vLastName;
        String driverEmail = generalFunc.getJsonValue("vEmail", userProfileJson);

        Utils.LIVE_CHAT_LICENCE_NUMBER = generalFunc.getJsonValue("LIVE_CHAT_LICENCE_NUMBER", userProfileJson);
        HashMap<String, String> map = new HashMap<>();
        map.put("FNAME", vName);
        map.put("LNAME", vLastName);
        map.put("EMAIL", driverEmail);
        map.put("USERTYPE", Utils.userType);

        Intent intent = new Intent(getActContext(), ChatWindowActivity.class);
        intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, Utils.LIVE_CHAT_LICENCE_NUMBER);
        intent.putExtra(ChatWindowActivity.KEY_VISITOR_NAME, driverName);
        intent.putExtra(ChatWindowActivity.KEY_VISITOR_EMAIL, driverEmail);
        intent.putExtra(ChatWindowActivity.KEY_GROUP_ID, Utils.userType + "_" + generalFunc.getMemberId());

        intent.putExtra("myParam", map);
        startActivity(intent);
    }


    public void showPasswordBox() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());


        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.change_passoword_layout, null);

        final String required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        final String noWhiteSpace = generalFunc.retrieveLangLBl("Password should not contain whitespace.", "LBL_ERROR_NO_SPACE_IN_PASS");
        final String pass_length = generalFunc.retrieveLangLBl("Password must be", "LBL_ERROR_PASS_LENGTH_PREFIX")
                + " " + Utils.minPasswordLength + " " + generalFunc.retrieveLangLBl("or more character long.", "LBL_ERROR_PASS_LENGTH_SUFFIX");
        final String vPassword = generalFunc.getJsonValueStr("vPassword", obj_userProfile);

        final MaterialEditText previous_passwordBox = (MaterialEditText) dialogView.findViewById(R.id.editBox);
        previous_passwordBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_CURR_PASS_HEADER"));
        previous_passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        previous_passwordBox.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        if (vPassword.equals("")) {
            previous_passwordBox.setVisibility(View.GONE);
        }

        final MaterialEditText newPasswordBox = (MaterialEditText) dialogView.findViewById(R.id.newPasswordBox);

        ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelImg);
        MTextView submitTxt = (MTextView) dialogView.findViewById(R.id.submitTxt);
        MTextView cancelTxt = (MTextView) dialogView.findViewById(R.id.cancelTxt);
        MTextView subTitleTxt = (MTextView) dialogView.findViewById(R.id.subTitleTxt);
        subTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_PASSWORD_TXT"));


        newPasswordBox.setFloatingLabelText(generalFunc.retrieveLangLBl("", "LBL_UPDATE_PASSWORD_HEADER_TXT"));
        newPasswordBox.setHint(generalFunc.retrieveLangLBl("", "LBL_UPDATE_PASSWORD_HINT_TXT"));
        newPasswordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        newPasswordBox.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        final MaterialEditText reNewPasswordBox = (MaterialEditText) dialogView.findViewById(R.id.reNewPasswordBox);

        reNewPasswordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        reNewPasswordBox.setFloatingLabelText(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT"));
        reNewPasswordBox.setHint(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CONFIRM_PASSWORD_HINT_TXT"));

        reNewPasswordBox.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        builder.setView(dialogView);


        cancelImg.setOnClickListener(v -> alertDialog.dismiss());
        cancelTxt.setOnClickListener(v -> alertDialog.dismiss());
        submitTxt.setOnClickListener(v -> {

            boolean isCurrentPasswordEnter = Utils.checkText(previous_passwordBox) ?
                    (Utils.getText(previous_passwordBox).contains(" ") ? Utils.setErrorFields(previous_passwordBox, noWhiteSpace)
                            : (Utils.getText(previous_passwordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(previous_passwordBox, pass_length)))
                    : Utils.setErrorFields(previous_passwordBox, required_str);

            boolean isNewPasswordEnter = Utils.checkText(newPasswordBox) ?
                    (Utils.getText(newPasswordBox).contains(" ") ? Utils.setErrorFields(newPasswordBox, noWhiteSpace)
                            : (Utils.getText(newPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(newPasswordBox, pass_length)))
                    : Utils.setErrorFields(newPasswordBox, required_str);

            boolean isReNewPasswordEnter = Utils.checkText(reNewPasswordBox) ?
                    (Utils.getText(reNewPasswordBox).contains(" ") ? Utils.setErrorFields(reNewPasswordBox, noWhiteSpace)
                            : (Utils.getText(reNewPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(reNewPasswordBox, pass_length)))
                    : Utils.setErrorFields(reNewPasswordBox, required_str);

            if ((!vPassword.equals("") && isCurrentPasswordEnter == false) || isNewPasswordEnter == false || isReNewPasswordEnter == false) {
                return;
            }

            if (!Utils.getText(newPasswordBox).equals(Utils.getText(reNewPasswordBox))) {
                Utils.setErrorFields(reNewPasswordBox, generalFunc.retrieveLangLBl("", "LBL_VERIFY_PASSWORD_ERROR_TXT"));
                return;
            }

            changePassword(Utils.getText(previous_passwordBox), Utils.getText(newPasswordBox), previous_passwordBox);

        });

        builder.setView(dialogView);
        alertDialog = builder.create();


        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog);
        }

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
        alertDialog.show();

//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                boolean isCurrentPasswordEnter = Utils.checkText(previous_passwordBox) ?
//                        (Utils.getText(previous_passwordBox).contains(" ") ? Utils.setErrorFields(previous_passwordBox, noWhiteSpace)
//                                : (Utils.getText(previous_passwordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(previous_passwordBox, pass_length)))
//                        : Utils.setErrorFields(previous_passwordBox, required_str);
//
//                boolean isNewPasswordEnter = Utils.checkText(newPasswordBox) ?
//                        (Utils.getText(newPasswordBox).contains(" ") ? Utils.setErrorFields(newPasswordBox, noWhiteSpace)
//                                : (Utils.getText(newPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(newPasswordBox, pass_length)))
//                        : Utils.setErrorFields(newPasswordBox, required_str);
//
//                boolean isReNewPasswordEnter = Utils.checkText(reNewPasswordBox) ?
//                        (Utils.getText(reNewPasswordBox).contains(" ") ? Utils.setErrorFields(reNewPasswordBox, noWhiteSpace)
//                                : (Utils.getText(reNewPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(reNewPasswordBox, pass_length)))
//                        : Utils.setErrorFields(reNewPasswordBox, required_str);
//
//                if ((!vPassword.equals("") && isCurrentPasswordEnter == false) || isNewPasswordEnter == false || isReNewPasswordEnter == false) {
//                    return;
//                }
//
//                if (!Utils.getText(newPasswordBox).equals(Utils.getText(reNewPasswordBox))) {
//                    Utils.setErrorFields(reNewPasswordBox, generalFunc.retrieveLangLBl("", "LBL_VERIFY_PASSWORD_ERROR_TXT"));
//                    return;
//                }
//
//                changePassword(Utils.getText(previous_passwordBox), Utils.getText(newPasswordBox), previous_passwordBox);
//            }
//        });
//
//        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });

    }

    public void updateProfile() {


        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "updateUserProfileDetail");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vName", generalFunc.getJsonValue("vName", userProfileJson));
        parameters.put("vLastName", generalFunc.getJsonValue("vLastName", userProfileJson));
        parameters.put("vPhone", generalFunc.getJsonValue("vPhone", userProfileJson));
        parameters.put("vPhoneCode", generalFunc.getJsonValue("vPhoneCode", userProfileJson));
        parameters.put("vCountry", generalFunc.getJsonValue("vCountry", userProfileJson));
        parameters.put("vEmail", generalFunc.getJsonValue("vEmail", userProfileJson));
        parameters.put("CurrencyCode", selected_currency);
        parameters.put("LanguageCode", selected_language_code);
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    String currentLangCode = generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY);
                    String vCurrencyPassenger = generalFunc.getJsonValue("vCurrencyPassenger", userProfileJson);

                    String messgeJson = generalFunc.getJsonValue(Utils.message_str, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, messgeJson);
                    responseString = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);




                    if (!currentLangCode.equals(selected_language_code) || !selected_currency.equals(vCurrencyPassenger)) {
                        new SetUserData(responseString, generalFunc, getActContext(), false);

                        GenerateAlertBox alertBox = generalFunc.notifyRestartApp();
                        alertBox.setCancelable(false);
                        alertBox.setBtnClickList(btn_id -> {

                            if (btn_id == 1) {
                                //  generalFunc.restartApp();
                                generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                                generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
                                changeLanguagedata(selected_language_code);
                            }
                        });
                    }

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

    public void changeLanguagedata(String langcode) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "changelanguagelabel");
        parameters.put("vLang", langcode);
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    Realm realm = MyApp.getRealmInstance();
                    realm.beginTransaction();
                    realm.delete(Cart.class);
                    realm.delete(Topping.class);
                    realm.delete(Options.class);
                    realm.commitTransaction();

                    generalFunc.storeData(Utils.languageLabelsKey, generalFunc.getJsonValue(Utils.message_str, responseString));
                    generalFunc.storeData(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", responseString));
                    generalFunc.storeData(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", responseString));
                    GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());


                    Gson gson = new Gson();
                    String data1 = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                    ContactModel contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                            }.getType()
                    );
                    if (contactdetails!=null && contactdetails.shouldremove){
                        generalFunc.removeValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                    }

                    new Handler().postDelayed(() -> generalFunc.restartApp(), 100);

                }
            }
        });
        exeWebServer.execute();
    }


    public void buildCurrencyList() {

        JSONArray currencyList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.CURRENCY_LIST_KEY));

        for (int i = 0; i < currencyList_arr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(currencyList_arr, i);

            HashMap<String, String> data = new HashMap<>();
            data.put("vName", generalFunc.getJsonValueStr("vName", obj_temp));
            data.put("vSymbol", generalFunc.getJsonValueStr("vSymbol", obj_temp));
            if (!selected_currency.equalsIgnoreCase("") && selected_currency.equalsIgnoreCase(generalFunc.getJsonValueStr("vName", obj_temp))) {
                selCurrancyPosition = i;
            }
            currency_list.add(data);
        }

        if (currency_list.size() < 2 /*|| generalFunc.retrieveValue("CURRENCY_OPTIONAL").equalsIgnoreCase("Yes")*/) {
            changeCurrencyView.setVisibility(View.GONE);
            changesCurrancyArea.setVisibility(View.GONE);
        } else {
            changeCurrencyView.setVisibility(View.VISIBLE);
            changesCurrancyArea.setVisibility(View.VISIBLE);

        }
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    public void changePassword(String currentPassword, String password, MaterialEditText previous_passwordBox) {

        if (SITE_TYPE.equals("Demo")) {
            generalFunc.showGeneralMessage("", SITE_TYPE_DEMO_MSG);
            return;
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "updatePassword");
        parameters.put("UserID", generalFunc.getMemberId());
        parameters.put("pass", password);
        parameters.put("CurrentPassword", currentPassword);
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseStringObject = generalFunc.getJsonObject(responseString);

            if (responseStringObject != null && !responseStringObject.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseStringObject);

                if (isDataAvail == true) {
                    alertDialog.dismiss();
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValueStr(Utils.message_str, responseStringObject));
                    userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                    obj_userProfile = generalFunc.getJsonObject(userProfileJson);
                } else {
                    previous_passwordBox.setText("");

                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseStringObject)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.MY_PROFILE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            setuserInfo();

            //addDrawer.changeUserProfileJson(userProfileJson);
        }
    }

    public Context getActContext() {
        return MyProfileNewActivity.this;
    }
}
