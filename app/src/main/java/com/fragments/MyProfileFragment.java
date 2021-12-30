package com.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.melevicarbrasil.usuario.BookingActivity;
import com.melevicarbrasil.usuario.BusinessProfileActivity;
import com.melevicarbrasil.usuario.CardPaymentActivity;
import com.melevicarbrasil.usuario.ContactUsActivity;
import com.melevicarbrasil.usuario.DonationActivity;
import com.melevicarbrasil.usuario.EmergencyContactActivity;
import com.melevicarbrasil.usuario.FavouriteDriverActivity;
import com.melevicarbrasil.usuario.HelpActivity;
import com.melevicarbrasil.usuario.InviteFriendsActivity;
import com.melevicarbrasil.usuario.MenuSettingActivity;
import com.melevicarbrasil.usuario.MyProfileActivity;
import com.melevicarbrasil.usuario.MyWalletActivity;
import com.melevicarbrasil.usuario.NotificationActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.SearchPickupLocationActivity;
import com.melevicarbrasil.usuario.StaticPageActivity;
import com.melevicarbrasil.usuario.VerifyInfoActivity;
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

public class MyProfileFragment extends Fragment {

    private static final String TAG = "MyProfileFragment";
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
            addMoneyArea, sendMoneyArea, personalDetailsArea, changesPasswordArea, changesCurrancyArea, changeslanguageArea, termsArea, liveChatArea, contactUsArea, verifyEmailArea, verifyMobArea;
    View notificationView, paymentView, rewardView, myBookingView, businessProView, mycartView, favDriverView, addMoneyView, aboutUsView, myWalletView,
            sendMoneyView, personalDetailsView, changePasswordView, changeCurrencyView, changeLangView, termsView, livechatView, verifyEmailView, verifyMobView, donationView;
    LinearLayout bookingArea, inviteArea, topUpArea, logOutArea, workArea;
    LinearLayout myWalletArea, inviteFriendArea, helpArea, aboutusArea, homeArea, headerwalletArea, emeContactArea, donationArea;
    MTextView mywalletHTxt, inviteHTxt, helpHTxt, aboutusHTxt, logoutTxt, otherHTxt, homeHTxt, workHTxt, favHTxt, workaddressTxt, homeaddressTxt, headerwalletTxt, emeContactHTxt, verifyEmailHTxt, verifyMobHTxt, donationHTxt;
    ImageView notificationArrow, paymentArrow, privacyArrow, termsArrow, mywalletArrow, inviteArrow, helpArrow, aboutusArrow,
            mybookingArrow, businessProArrow, mycartArrow, favDriverArrow, addMoneyArrow, sendMoneyArrow, personalDetailsArrow,
            changePasswordArrow, changeCurrencyArrow, changeLangArrow, livechatArrow, contactUsArrow, logoutArrow, homeArrow, workArrow, emeContactArrow, verifyMobsArrow, verifyEmailArrow;

    View view;
    InternetConnection internetConnection;
    String ENABLE_FAVORITE_DRIVER_MODULE_KEY = "";
    boolean isDeliverOnlyEnabled;
    boolean isAnyDeliverOptionEnabled;
    androidx.appcompat.app.AlertDialog alertDialog;
    String SITE_TYPE = "";
    String SITE_TYPE_DEMO_MSG = "";


    ArrayList<HashMap<String, String>> language_list = new ArrayList<>();
    String selected_language_code = "";
    String default_selected_language_code = "";
    ArrayList<HashMap<String, String>> currency_list = new ArrayList<>();

    String selected_currency = "";
    String default_selected_currency = "";
    String selected_currency_symbol = "";

    LinearLayout toolbar_profile;
    CardView headerArea;
    LinearLayout settingArea, walletArea, favArea, otherArea;
    MTextView signSignUpTxt;
    RelativeLayout profileArea;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        if (view != null) {
//            return view;
//        }
        view = inflater.inflate(R.layout.activity_my_profile_new, container, false);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        ENABLE_FAVORITE_DRIVER_MODULE_KEY = generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY);
        isDeliverOnlyEnabled = generalFunc.isDeliverOnlyEnabled();
        isAnyDeliverOptionEnabled = generalFunc.isAnyDeliverOptionEnabled();
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
        SITE_TYPE = generalFunc.getJsonValueStr("SITE_TYPE", obj_userProfile);
        SITE_TYPE_DEMO_MSG = generalFunc.getJsonValueStr("SITE_TYPE_DEMO_MSG", obj_userProfile);
        selected_currency = generalFunc.getJsonValue("vCurrencyPassenger", userProfileJson);
        default_selected_currency = selected_currency;
        internetConnection = new InternetConnection(getActContext());
        initViews();
        setLabel();
        setuserInfo();
        manageView();
        buildLanguageList();
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (getArguments().getBoolean("isback", false)) {
                backImg.setVisibility(View.VISIBLE);

            }
        }


        return view;

    }

    public void manageView() {
        if (generalFunc.getJsonValue("ENABLE_LIVE_CHAT", userProfileJson).equalsIgnoreCase("Yes")) {
            liveChatArea.setVisibility(View.VISIBLE);
            livechatView.setVisibility(View.VISIBLE);
        } else {
            liveChatArea.setVisibility(View.GONE);
            livechatView.setVisibility(View.GONE);
        }

        if (ENABLE_FAVORITE_DRIVER_MODULE_KEY.equalsIgnoreCase("Yes")) {
            favDriverArea.setVisibility(View.VISIBLE);
            favDriverView.setVisibility(View.VISIBLE);

        } else {
            favDriverArea.setVisibility(View.GONE);
            favDriverView.setVisibility(View.GONE);

        }

        String APP_TYPE = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);

//        if (generalFunc.getJsonValueStr(Utils.DONATION_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")/* && !APP_TYPE.equals(Utils.CabGeneralTypeRide_Delivery_UberX)*/) {
        if (generalFunc.getJsonValueStr(Utils.DONATION_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")&& APP_TYPE.equals(Utils.CabGeneralType_Ride)) {
            donationView.setVisibility(View.VISIBLE);
            donationArea.setVisibility(View.VISIBLE);
        } else {
            donationView.setVisibility(View.GONE);
            donationArea.setVisibility(View.GONE);
        }


        if (!generalFunc.getJsonValueStr(Utils.WALLET_ENABLE, obj_userProfile).equals("") &&
                generalFunc.getJsonValueStr(Utils.WALLET_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")) {
            myWalletArea.setVisibility(View.VISIBLE);
            headerwalletArea.setVisibility(View.VISIBLE);
            myWalletView.setVisibility(View.VISIBLE);
            addMoneyArea.setVisibility(View.VISIBLE);
            sendMoneyArea.setVisibility(View.VISIBLE);
            addMoneyView.setVisibility(View.VISIBLE);
            sendMoneyView.setVisibility(View.VISIBLE);
            topUpArea.setVisibility(View.VISIBLE);


        } else {
            myWalletArea.setVisibility(View.GONE);
            headerwalletArea.setVisibility(View.GONE);
            myWalletView.setVisibility(View.GONE);
            addMoneyArea.setVisibility(View.GONE);
            sendMoneyArea.setVisibility(View.GONE);
            addMoneyView.setVisibility(View.GONE);
            sendMoneyView.setVisibility(View.GONE);
            topUpArea.setVisibility(View.GONE);
        }

        if (generalFunc.getJsonValueStr("ENABLE_CORPORATE_PROFILE", obj_userProfile).equalsIgnoreCase("Yes")) {
            businessProfileArea.setVisibility(View.VISIBLE);
            businessProView.setVisibility(View.VISIBLE);
        } else {
            businessProfileArea.setVisibility(View.GONE);
            businessProView.setVisibility(View.GONE);

        }

        if (isDeliverOnlyEnabled) {
            generalSettingHTxt.setVisibility(View.VISIBLE);
            settingArea.setVisibility(View.VISIBLE);
            myCartArea.setVisibility(View.VISIBLE);
            mycartView.setVisibility(View.VISIBLE);
        } else {
            myCartArea.setVisibility(View.GONE);
            mycartView.setVisibility(View.GONE);
        }

        if (generalFunc.getJsonValue("ENABLE_NEWS_SECTION", userProfileJson) != null && generalFunc.getJsonValue("ENABLE_NEWS_SECTION", userProfileJson).equalsIgnoreCase("yes")) {
            notificationArea.setVisibility(View.VISIBLE);
            notificationView.setVisibility(View.VISIBLE);

        } else {
            notificationArea.setVisibility(View.GONE);
            notificationView.setVisibility(View.GONE);

        }
        if (!generalFunc.getJsonValueStr(Utils.REFERRAL_SCHEME_ENABLE, obj_userProfile).equals("") && generalFunc.getJsonValueStr(Utils.REFERRAL_SCHEME_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")) {
            inviteFriendArea.setVisibility(View.VISIBLE);
            inviteArea.setVisibility(View.VISIBLE);
        } else {
            inviteFriendArea.setVisibility(View.GONE);
            inviteArea.setVisibility(View.GONE);
        }

        if (generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1") && !generalFunc.getJsonValueStr("APP_PAYMENT_MODE", obj_userProfile).equalsIgnoreCase("Cash")) {

            paymentMethodArea.setVisibility(View.VISIBLE);
            paymentView.setVisibility(View.VISIBLE);

        } else {
            paymentMethodArea.setVisibility(View.GONE);
            paymentView.setVisibility(View.GONE);

        }


        JSONArray currencyList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.CURRENCY_LIST_KEY));

        if (currencyList_arr!=null){
            if (currencyList_arr.length() < 2) {
                changesCurrancyArea.setVisibility(View.GONE);
                changeCurrencyView.setVisibility(View.GONE);
            } else {
                changesCurrancyArea.setVisibility(View.VISIBLE);
                changeCurrencyView.setVisibility(View.VISIBLE);
            }
        }


        HashMap<String, String> data = new HashMap<>();
        data.put(Utils.LANGUAGE_LIST_KEY, "");
        data.put(Utils.LANGUAGE_CODE_KEY, "");
        data = generalFunc.retrieveValue(data);

        JSONArray languageList_arr = generalFunc.getJsonArray(data.get(Utils.LANGUAGE_LIST_KEY));

        if (languageList_arr.length() < 2) {
            changeslanguageArea.setVisibility(View.GONE);
        } else {
            changeslanguageArea.setVisibility(View.VISIBLE);
        }


        if (generalFunc.retrieveValue(Utils.ONLYDELIVERALL_KEY).equalsIgnoreCase("Yes")) {

            if (!generalFunc.getMemberId().equalsIgnoreCase("")) {
                signSignUpTxt.setVisibility(View.GONE);
                otherArea.setVisibility(View.VISIBLE);
                otherHTxt.setVisibility(View.VISIBLE);
                headerArea.setVisibility(View.VISIBLE);
                profileArea.getLayoutParams().height = Utils.dpToPx(160, getActContext());
                profileArea.requestLayout();
                changesPasswordArea.setVisibility(View.VISIBLE);
                personalDetailsView.setVisibility(View.VISIBLE);
                personalDetailsArea.setVisibility(View.VISIBLE);
                changePasswordView.setVisibility(View.VISIBLE);
                emeContactArea.setVisibility(View.VISIBLE);
            }
            favDriverArea.setVisibility(View.GONE);
            favDriverView.setVisibility(View.GONE);
            businessProfileArea.setVisibility(View.GONE);
            businessProView.setVisibility(View.GONE);

        }


    }

    public void initViews() {
        profileArea = view.findViewById(R.id.profileArea);
        backImg = view.findViewById(R.id.backImg);
        signSignUpTxt = view.findViewById(R.id.signSignUpTxt);
        headerArea = view.findViewById(R.id.headerArea);
        settingArea = view.findViewById(R.id.settingArea);
        walletArea = view.findViewById(R.id.walletArea);
        otherArea = view.findViewById(R.id.otherArea);
        favArea = view.findViewById(R.id.favArea);
        editProfileImage = view.findViewById(R.id.editProfileImage);
        userImgView = view.findViewById(R.id.userImgView);
        userImgView_toolbar = view.findViewById(R.id.userImgView_toolbar);
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userNameTxt_toolbar = view.findViewById(R.id.userNameTxt_toolbar);
        userEmailTxt = view.findViewById(R.id.userEmailTxt);
        walletHTxt = view.findViewById(R.id.walletHTxt);
        walletVxt = view.findViewById(R.id.walletVxt);
        bookingTxt = view.findViewById(R.id.bookingTxt);
        inviteTxt = view.findViewById(R.id.inviteTxt);
        topupTxt = view.findViewById(R.id.topupTxt);
        headerwalletTxt = view.findViewById(R.id.headerwalletTxt);
        emeContactHTxt = view.findViewById(R.id.emeContactHTxt);
        verifyEmailHTxt = view.findViewById(R.id.verifyEmailHTxt);
        verifyMobHTxt = view.findViewById(R.id.verifyMobHTxt);
        donationHTxt = view.findViewById(R.id.donationHTxt);
        generalSettingHTxt = view.findViewById(R.id.generalSettingHTxt);
        accountHTxt = view.findViewById(R.id.accountHTxt);
        notificationHTxt = view.findViewById(R.id.notificationHTxt);
        paymentHTxt = view.findViewById(R.id.paymentHTxt);
        privacyHTxt = view.findViewById(R.id.privacyHTxt);
        termsHTxt = view.findViewById(R.id.termsHTxt);
        logoutTxt = view.findViewById(R.id.logoutTxt);
        otherHTxt = view.findViewById(R.id.otherHTxt);
        homeHTxt = view.findViewById(R.id.homeHTxt);
        workHTxt = view.findViewById(R.id.workHTxt);
        favHTxt = view.findViewById(R.id.favHTxt);
        workaddressTxt = view.findViewById(R.id.workaddressTxt);
        homeaddressTxt = view.findViewById(R.id.homeaddressTxt);
        notificationArea = view.findViewById(R.id.notificationArea);
        paymentMethodArea = view.findViewById(R.id.paymentMethodArea);
        privacyArea = view.findViewById(R.id.privacyArea);
        logoutTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LOGOUT"));
        otherHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT"));

        myBookingArea = view.findViewById(R.id.myBookingArea);
        businessProfileArea = view.findViewById(R.id.businessProfileArea);
        myCartArea = view.findViewById(R.id.myCartArea);
        favDriverArea = view.findViewById(R.id.favDriverArea);
        addMoneyArea = view.findViewById(R.id.addMoneyArea);
        sendMoneyArea = view.findViewById(R.id.sendMoneyArea);
        personalDetailsArea = view.findViewById(R.id.personalDetailsArea);
        changesPasswordArea = view.findViewById(R.id.changesPasswordArea);
        changesCurrancyArea = view.findViewById(R.id.changesCurrancyArea);
        changeslanguageArea = view.findViewById(R.id.changeslanguageArea);
        termsArea = view.findViewById(R.id.termsArea);
        liveChatArea = view.findViewById(R.id.liveChatArea);
        contactUsArea = view.findViewById(R.id.contactUsArea);
        verifyEmailArea = view.findViewById(R.id.verifyEmailArea);
        verifyMobArea = view.findViewById(R.id.verifyMobArea);
        notificationView = view.findViewById(R.id.notificationView);
        paymentView = view.findViewById(R.id.paymentView);
        rewardView = view.findViewById(R.id.rewardView);
        myBookingView = view.findViewById(R.id.myBookingView);
        businessProView = view.findViewById(R.id.businessProView);
        mycartView = view.findViewById(R.id.mycartView);
        favDriverView = view.findViewById(R.id.favDriverView);
        addMoneyView = view.findViewById(R.id.addMoneyView);
        aboutUsView = view.findViewById(R.id.aboutUsView);
        myWalletView = view.findViewById(R.id.myWalletView);
        sendMoneyView = view.findViewById(R.id.sendMoneyView);
        personalDetailsView = view.findViewById(R.id.personalDetailsView);
        changePasswordView = view.findViewById(R.id.personalDetailsView);
        changeCurrencyView = view.findViewById(R.id.changeCurrencyView);
        changeLangView = view.findViewById(R.id.changeLangView);
        termsView = view.findViewById(R.id.termsView);
        livechatView = view.findViewById(R.id.livechatView);
        verifyEmailView = view.findViewById(R.id.verifyEmailView);
        verifyMobView = view.findViewById(R.id.verifyMobView);
        donationView = view.findViewById(R.id.donationView);
        bookingArea = view.findViewById(R.id.bookingArea);
        inviteArea = view.findViewById(R.id.inviteArea);
        topUpArea = view.findViewById(R.id.topUpArea);

        logOutArea = view.findViewById(R.id.logOutArea);
        workArea = view.findViewById(R.id.workArea);
        myPaymentHTxt = view.findViewById(R.id.myPaymentHTxt);
        mybookingHTxt = view.findViewById(R.id.mybookingHTxt);
        businessProfileHTxt = view.findViewById(R.id.businessProfileHTxt);
        myCartHTxt = view.findViewById(R.id.myCartHTxt);
        favDriverHTxt = view.findViewById(R.id.favDriverHTxt);
        addMoneyHTxt = view.findViewById(R.id.addMoneyHTxt);
        sendMoneyHTxt = view.findViewById(R.id.sendMoneyHTxt);
        personalDetailsHTxt = view.findViewById(R.id.personalDetailsHTxt);
        changePasswordHTxt = view.findViewById(R.id.changePasswordHTxt);
        changeCurrencyHTxt = view.findViewById(R.id.changeCurrencyHTxt);
        changeLanguageHTxt = view.findViewById(R.id.changeLanguageHTxt);
        supportHTxt = view.findViewById(R.id.supportHTxt);
        livechatHTxt = view.findViewById(R.id.livechatHTxt);
        contactUsHTxt = view.findViewById(R.id.contactUsHTxt);
        myWalletArea = view.findViewById(R.id.myWalletArea);
        headerwalletArea = view.findViewById(R.id.headerwalletArea);
        emeContactArea = view.findViewById(R.id.emeContactArea);
        donationArea = view.findViewById(R.id.donationArea);
        inviteFriendArea = view.findViewById(R.id.inviteFriendArea);
        helpArea = view.findViewById(R.id.helpArea);
        aboutusArea = view.findViewById(R.id.aboutusArea);
        homeArea = view.findViewById(R.id.homeArea);
        notificationArrow = view.findViewById(R.id.notificationArrow);
        paymentArrow = view.findViewById(R.id.paymentArrow);
        privacyArrow = view.findViewById(R.id.privacyArrow);
        termsArrow = view.findViewById(R.id.termsArrow);
        mywalletArrow = view.findViewById(R.id.mywalletArrow);
        inviteArrow = view.findViewById(R.id.inviteArrow);
        helpArrow = view.findViewById(R.id.helpArrow);
        aboutusArrow = view.findViewById(R.id.aboutusArrow);
        mybookingArrow = view.findViewById(R.id.mybookingArrow);
        businessProArrow = view.findViewById(R.id.businessProArrow);
        mycartArrow = view.findViewById(R.id.mycartArrow);
        favDriverArrow = view.findViewById(R.id.favDriverArrow);
        addMoneyArrow = view.findViewById(R.id.addMoneyArrow);
        sendMoneyArrow = view.findViewById(R.id.sendMoneyArrow);
        personalDetailsArrow = view.findViewById(R.id.personalDetailsArrow);
        changePasswordArrow = view.findViewById(R.id.changePasswordArrow);
        changeCurrencyArrow = view.findViewById(R.id.changeCurrencyArrow);
        changeLangArrow = view.findViewById(R.id.changeLangArrow);
        livechatArrow = view.findViewById(R.id.livechatArrow);
        contactUsArrow = view.findViewById(R.id.contactUsArrow);
        logoutArrow = view.findViewById(R.id.logoutArrow);
        homeArrow = view.findViewById(R.id.homeArrow);
        workArrow = view.findViewById(R.id.workArrow);
        emeContactArrow = view.findViewById(R.id.emeContactArrow);
        verifyMobsArrow = view.findViewById(R.id.verifyMobsArrow);
        verifyEmailArrow = view.findViewById(R.id.verifyEmailArrow);


        mywalletHTxt = view.findViewById(R.id.mywalletHTxt);
        inviteHTxt = view.findViewById(R.id.inviteHTxt);
        helpHTxt = view.findViewById(R.id.helpHTxt);
        aboutusHTxt = view.findViewById(R.id.aboutusHTxt);
        toolbar_profile = view.findViewById(R.id.toolbar_profile);


        notificationArea.setOnClickListener(new setOnClickList());
        paymentMethodArea.setOnClickListener(new setOnClickList());
        privacyArea.setOnClickListener(new setOnClickList());
        emeContactArea.setOnClickListener(new setOnClickList());
        donationArea.setOnClickListener(new setOnClickList());

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
        verifyEmailArea.setOnClickListener(new setOnClickList());
        verifyMobArea.setOnClickListener(new setOnClickList());
        editProfileImage.setOnClickListener(new setOnClickList());
        signSignUpTxt.setOnClickListener(new setOnClickList());

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
            verifyMobsArrow.setRotation(180);
            verifyEmailArrow.setRotation(180);
        }


        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.scroll);
        scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {

                    Log.i(TAG, "Scroll DOWN");

                    if (scrollY > getResources().getDimension(R.dimen._75sdp)) {
                        toolbar_profile.setVisibility(View.VISIBLE);
                    }
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");

                    if (scrollY < getResources().getDimension(R.dimen._75sdp)) {
                        toolbar_profile.setVisibility(View.GONE);
                    }

                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");

                }

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");
                }
            }
        });


        if (generalFunc.getMemberId().equalsIgnoreCase("")) {
            headerArea.setVisibility(View.GONE);
            generalSettingHTxt.setVisibility(View.GONE);
            settingArea.setVisibility(View.GONE);
            myPaymentHTxt.setVisibility(View.GONE);
            walletArea.setVisibility(View.GONE);
            favHTxt.setVisibility(View.GONE);
            favArea.setVisibility(View.GONE);
            otherHTxt.setVisibility(View.GONE);
            otherArea.setVisibility(View.GONE);
            changesPasswordArea.setVisibility(View.GONE);
            personalDetailsView.setVisibility(View.GONE);
            personalDetailsArea.setVisibility(View.GONE);
            changePasswordView.setVisibility(View.GONE);
            editProfileImage.setVisibility(View.GONE);
            verifyEmailArea.setVisibility(View.GONE);
            verifyEmailView.setVisibility(View.GONE);
            verifyMobArea.setVisibility(View.GONE);
            verifyMobView.setVisibility(View.GONE);
            emeContactArea.setVisibility(View.GONE);

        }


    }


    public void setLabel() {
        verifyEmailHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_VERIFY"));
        verifyMobHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MOBILE_VERIFY"));
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
        donationHTxt.setText(generalFunc.retrieveLangLBl("Donation", "LBL_DONATE"));
        signSignUpTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_IN_SIGN_IN_TXT") + " / " + generalFunc.retrieveLangLBl("", "LBL_SIGNUP_SIGNUP"));

        String APP_TYPE = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);

        if (APP_TYPE.equals(Utils.CabGeneralTypeRide_Delivery_UberX) || APP_TYPE.equals(Utils.CabGeneralType_UberX)) {
            favDriverHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAV_PROVIDER"));
        } else {
            favDriverHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAV_DRIVER"));
        }


    }


    public void setuserInfo() {

        userNameTxt.setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        userNameTxt_toolbar.setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        userEmailTxt.setText(generalFunc.getJsonValueStr("vEmail", obj_userProfile));


        (new AppFunctions(getActContext())).checkProfileImage(userImgView, userProfileJson, "vImgName");
        (new AppFunctions(getActContext())).checkProfileImage(userImgView_toolbar, userProfileJson, "vImgName");
        walletVxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));

        String work_address_str = generalFunc.retrieveValue("userWorkLocationAddress");
        String home_address_str = generalFunc.retrieveValue("userHomeLocationAddress");

        if (work_address_str != null && !work_address_str.equalsIgnoreCase("")) {
            workaddressTxt.setVisibility(View.VISIBLE);

            int padding = (int) getResources().getDimension(R.dimen._10sdp);
            workArea.setPadding(0, padding, 0, padding);
            workaddressTxt.setText(work_address_str);
            workHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WORK_PLACE"));

        } else {
            workaddressTxt.setVisibility(View.GONE);
            workArea.setPadding(0, 0, 0, 0);
            workHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_WORK_PLACE_TXT"));
        }
        if (home_address_str != null && !home_address_str.equalsIgnoreCase("")) {
            int padding = (int) getResources().getDimension(R.dimen._10sdp);
            homeArea.setPadding(0, padding, 0, padding);
            homeaddressTxt.setVisibility(View.VISIBLE);
            homeaddressTxt.setText(home_address_str);
            homeHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HOME_PLACE"));
        } else {
            homeaddressTxt.setVisibility(View.GONE);
            homeArea.setPadding(0, 0, 0, 0);
            homeHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_HOME_PLACE_TXT"));
        }

        if (generalFunc.getMemberId().equalsIgnoreCase("")) {
            userNameTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WELCOME_BACK"));
            userNameTxt_toolbar.setText(generalFunc.retrieveLangLBl("", "LBL_WELCOME_BACK"));
            signSignUpTxt.setVisibility(View.VISIBLE);

            profileArea.getLayoutParams().height = Utils.dpToPx(110, getActContext());
            profileArea.requestLayout();

        }

        if (!generalFunc.getJsonValueStr("eEmailVerified", obj_userProfile).equalsIgnoreCase("YES") && !generalFunc.getMemberId().equalsIgnoreCase("")) {
            verifyEmailArea.setVisibility(View.VISIBLE);
            verifyEmailView.setVisibility(View.VISIBLE);
        } else {
            verifyEmailArea.setVisibility(View.GONE);
            verifyEmailView.setVisibility(View.GONE);
        }


        if (!generalFunc.getJsonValueStr("ePhoneVerified", obj_userProfile).equalsIgnoreCase("YES") && !generalFunc.getMemberId().equalsIgnoreCase("")) {
            verifyMobArea.setVisibility(View.VISIBLE);
            verifyMobView.setVisibility(View.VISIBLE);
        } else {
            verifyMobArea.setVisibility(View.GONE);
            verifyMobView.setVisibility(View.GONE);
        }

        boolean isTransferMoneyEnabled = generalFunc.retrieveValue(Utils.ENABLE_GOPAY_KEY).equalsIgnoreCase("Yes");

        if (!isTransferMoneyEnabled) {
            sendMoneyArea.setVisibility(View.GONE);
            sendMoneyView.setVisibility(View.GONE);

        }

        boolean isOnlyCashEnabled = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson).equalsIgnoreCase("Cash");
        if (isOnlyCashEnabled) {
            addMoneyArea.setVisibility(View.GONE);
            topUpArea.setVisibility(View.GONE);
            addMoneyView.setVisibility(View.GONE);
        }


        if (generalFunc.getMemberId().equals("")) {
            editProfileImage.setImageResource(R.drawable.ic_edit);
        } else {
            if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes") && generalFunc.getJsonValue("eGender", obj_userProfile).equals("") && generalFunc.retrieveValue(Utils.ONLYDELIVERALL_KEY).equalsIgnoreCase("No")) {
                editProfileImage.setImageResource(R.drawable.ic_settings_new);
            } else {
                editProfileImage.setImageResource(R.drawable.ic_edit);
            }
        }


    }


    public void buildLanguageList() {
        JSONArray languageList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.LANGUAGE_LIST_KEY));

        language_list.clear();
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


                    new SetUserData(responseString, generalFunc, getActContext(), false);

                    if (!currentLangCode.equals(selected_language_code) || !selected_currency.equals(vCurrencyPassenger)) {

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


    public void buildCurrencyList() {

        currency_list.clear();
        JSONArray currencyList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.CURRENCY_LIST_KEY));

        if (currencyList_arr!=null){
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

            if (currency_list.size() < 2 /*|| generalFunc.retrieveValue("CURRENCY_OPTIONAL").equalsIgnoreCase("Yes") */) {
                changeCurrencyView.setVisibility(View.GONE);
                changesCurrancyArea.setVisibility(View.GONE);
            } else {
                changeCurrencyView.setVisibility(View.VISIBLE);
                changesCurrancyArea.setVisibility(View.VISIBLE);

            }
        }else {
            changeCurrencyView.setVisibility(View.GONE);
            changesCurrancyArea.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
        //  Logger.d("Onresume", ":: fragment called" + "::" + generalFunc.getJsonValueStr("user_available_balance", obj_userProfile));

        setuserInfo();
        manageView();


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

    int selCurrancyPosition = -1;
    int selLanguagePosition = -1;

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
        reNewPasswordBox.setHint(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT"));

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

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
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


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            Bundle bn = new Bundle();
            switch (view.getId()) {
                case R.id.backImg:

                    try {
                        MenuSettingActivity menuSettingActivity = (MenuSettingActivity) MyApp.getInstance().getCurrentAct();
                        menuSettingActivity.onBackPressed();
                    } catch (Exception e) {

                    }

                    break;
                case R.id.personalDetailsArea:
                case R.id.editProfileImage:
                    if (generalFunc.getMemberId().equals("")) {
                        new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    } else {

                        if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes") && generalFunc.getJsonValue("eGender", obj_userProfile).equals("") && generalFunc.retrieveValue(Utils.ONLYDELIVERALL_KEY).equalsIgnoreCase("No")) {
                            genderDailog();
                        } else {
                            new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                        }
                    }
                    break;
                case R.id.bookingArea:
                case R.id.myBookingArea:
                    if (isDeliverOnlyEnabled && generalFunc.getMemberId().equals("")) {
                        new StartActProcess(getActContext()).startAct(LoginActivity.class);
                    } else {
                        new StartActProcess(getActContext()).startAct(BookingActivity.class);
                    }
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
                case R.id.donationArea:
                    new StartActProcess(getActContext()).startActWithData(DonationActivity.class, bn);

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
                case R.id.headerwalletArea:
                case R.id.myWalletArea:
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.topUpArea:
                case R.id.addMoneyArea:
                    bn.putBoolean("isAddMoney", true);
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.sendMoneyArea:
                    bn.putBoolean("isSendMoney", true);
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.inviteArea:
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

                case R.id.verifyEmailArea:
                    bn.putString("msg", "DO_EMAIL_VERIFY");
                    new StartActProcess(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
                    break;
                case R.id.verifyMobArea:
                    bn.putString("msg", "DO_PHONE_VERIFY");
                    new StartActProcess(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
                    break;

            }
        }
    }

    public void genderDailog() {

        final Dialog builder = new Dialog(getActContext(), R.style.Theme_Dialog);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setContentView(R.layout.gender_view);
        builder.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        final MTextView genderTitleTxt = (MTextView) builder.findViewById(R.id.genderTitleTxt);
        final MTextView maleTxt = (MTextView) builder.findViewById(R.id.maleTxt);
        final MTextView femaleTxt = (MTextView) builder.findViewById(R.id.femaleTxt);
        final ImageView gendercancel = (ImageView) builder.findViewById(R.id.gendercancel);
        final ImageView gendermale = (ImageView) builder.findViewById(R.id.gendermale);
        final ImageView genderfemale = (ImageView) builder.findViewById(R.id.genderfemale);
        final LinearLayout male_area = (LinearLayout) builder.findViewById(R.id.male_area);
        final LinearLayout female_area = (LinearLayout) builder.findViewById(R.id.female_area);


        if (generalFunc.isRTLmode()) {

            //            ((LinearLayout)builder.findViewById(R.id.llCancelButton)).setRotation(180);
                                /*RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.ALIGN_PARENT_START);
            gendercancel.setLayoutParams(params1);*/

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.START;
            gendercancel.setLayoutParams(params1);
        }

        genderTitleTxt.setText(generalFunc.retrieveLangLBl("Select your gender to continue", "LBL_SELECT_GENDER"));
        maleTxt.setText(generalFunc.retrieveLangLBl("Male", "LBL_MALE_TXT"));
        femaleTxt.setText(generalFunc.retrieveLangLBl("FeMale", "LBL_FEMALE_TXT"));

        gendercancel.setOnClickListener(v -> builder.dismiss());

        male_area.setOnClickListener(v -> {


            callgederApi("Male");
            builder.dismiss();

        });
        female_area.setOnClickListener(v -> {


            callgederApi("Female");
            builder.dismiss();

        });

        builder.show();

    }

    public void callgederApi(String egender) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateUserGender");
        parameters.put("UserType", Utils.userType);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("eGender", egender);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);


            String message = generalFunc.getJsonValue(Utils.message_str, responseString);
            if (isDataAvail) {
                generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                obj_userProfile = generalFunc.getJsonObject(userProfileJson);
                Bundle bn = new Bundle();
                new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);

            }


        });
        exeWebServer.execute();
    }


    public String getSelectLangText() {
        return ("" + generalFunc.retrieveLangLBl("Select", "LBL_SELECT_LANGUAGE_HINT_TXT"));
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
                    if (contactdetails!=null && contactdetails.shouldremove) {
                        generalFunc.removeValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                    }

                    new Handler().postDelayed(() -> generalFunc.restartApp(), 100);

                }
            }
        });
        exeWebServer.execute();
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

        Intent intent = new Intent(getActivity(), ChatWindowActivity.class);
        intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, Utils.LIVE_CHAT_LICENCE_NUMBER);
        intent.putExtra(ChatWindowActivity.KEY_VISITOR_NAME, driverName);
        intent.putExtra(ChatWindowActivity.KEY_VISITOR_EMAIL, driverEmail);
        intent.putExtra(ChatWindowActivity.KEY_GROUP_ID, Utils.userType + "_" + generalFunc.getMemberId());

        intent.putExtra("myParam", map);
        startActivity(intent);
    }


    public Context getActContext() {
        return getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
        setuserInfo();
    }
}
