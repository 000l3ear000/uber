package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.adapter.files.DrawerAdapter;
import com.melevicarbrasil.usuario.BookingActivity;
import com.melevicarbrasil.usuario.BusinessProfileActivity;
import com.melevicarbrasil.usuario.CardPaymentActivity;
import com.melevicarbrasil.usuario.ContactUsActivity;
import com.melevicarbrasil.usuario.DonationActivity;
import com.melevicarbrasil.usuario.EmergencyContactActivity;
import com.melevicarbrasil.usuario.FavouriteDriverActivity;
import com.melevicarbrasil.usuario.HelpActivity;
import com.melevicarbrasil.usuario.InviteFriendsActivity;
import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.MenuSettingActivity;
import com.melevicarbrasil.usuario.MyProfileActivity;
import com.melevicarbrasil.usuario.MyWalletActivity;
import com.melevicarbrasil.usuario.NotificationActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.StaticPageActivity;
import com.melevicarbrasil.usuario.SupportActivity;
import com.melevicarbrasil.usuario.UberXActivity;
import com.melevicarbrasil.usuario.VerifyInfoActivity;
import com.melevicarbrasil.usuario.deliverAll.ActiveOrderActivity;
import com.melevicarbrasil.usuario.deliverAll.EditCartActivity;
import com.melevicarbrasil.usuario.deliverAll.LoginActivity;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class AddDrawer implements AdapterView.OnItemClickListener {

    public String userProfileJson;
    public MTextView walletbalncetxt;
    Context mContext;
    View view;
    DrawerLayout mDrawerLayout;
    ListView menuListView;
    DrawerAdapter drawerAdapter;
    ArrayList<String[]> list_menu_items;
    GeneralFunctions generalFunc;
    DrawerClickListener drawerClickListener;
    boolean isMenuState = true;
    boolean isDriverAssigned = false;
    LinearLayout logoutarea;
    ImageView logoutimage;
    MTextView logoutTxt;
    ImageView imgSetting;
    LinearLayout left_linear;
    MainActivity mainActivity;
    public JSONObject obj_userProfile;
    boolean isback = false;

    /*Deliver All*/
    RelativeLayout header_area;
    InternetConnection internetConnection;
    boolean isDeliverAll = false;
    ImageView backimageView;

    public static final int FAVDRIVER = -1;

    public AddDrawer(Context mContext, String userProfileJson, boolean isback) {
        this.mContext = mContext;
        this.userProfileJson = userProfileJson;
        this.isback = isback;
        internetConnection = new InternetConnection(mContext);
        view = ((Activity) mContext).findViewById(android.R.id.content);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        menuListView = (ListView) view.findViewById(R.id.menuListView);
        logoutarea = (LinearLayout) view.findViewById(R.id.logoutarea);
        logoutimage = (ImageView) view.findViewById(R.id.logoutimage);
        logoutTxt = (MTextView) view.findViewById(R.id.logoutTxt);
        imgSetting = (ImageView) view.findViewById(R.id.imgSetting);
        left_linear = (LinearLayout) view.findViewById(R.id.left_linear);
        imgSetting.setOnClickListener(new setOnClickLst());
        logoutarea.setOnClickListener(new setOnClickLst());
        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        logoutimage.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_menu_logout));
        logoutTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGNOUT_TXT"));
        walletbalncetxt = (MTextView) view.findViewById(R.id.walletbalncetxt);
        backimageView = (ImageView) view.findViewById(R.id.backimageView);

        if (generalFunc.isRTLmode()) {
            backimageView.setRotationY(180);
        }


        android.view.Display display = ((android.view.WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        left_linear.getLayoutParams().width = display.getWidth() * 75 / 100;
        left_linear.requestLayout();

        if (mContext instanceof MainActivity) {
            mainActivity = (MainActivity) mContext;
        }
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);


        if (isback) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


        buildDrawer();
        setUserInfo();

        if (mContext instanceof UberXActivity/* || mContext instanceof MainActivity*/) {
            (view.findViewById(R.id.menuImgView)).setOnClickListener(new setOnClickLst());
        }
    }

    public void setMenuImgClick(View view, boolean isDriverAssigned) {
//        isMenuState = true;
        if (isDriverAssigned) {
            (view.findViewById(R.id.backImgView)).setOnClickListener(new setOnClickLst());

        } else {
            (view.findViewById(R.id.menuImgView)).setOnClickListener(new setOnClickLst());

        }
    }


    /* Deliver All */

    public AddDrawer(Context mContext, String userProfileJson) {
        this.mContext = mContext;
        this.userProfileJson = userProfileJson;
        internetConnection = new InternetConnection(mContext);
        view = ((Activity) mContext).findViewById(android.R.id.content);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        menuListView = (ListView) view.findViewById(R.id.menuListView);
        logoutarea = (LinearLayout) view.findViewById(R.id.logoutarea);
        logoutimage = (ImageView) view.findViewById(R.id.logoutimage);
        logoutTxt = (MTextView) view.findViewById(R.id.logoutTxt);
        imgSetting = (ImageView) view.findViewById(R.id.imgSetting);
        left_linear = (LinearLayout) view.findViewById(R.id.left_linear);
        header_area = (RelativeLayout) view.findViewById(R.id.header_area);
        imgSetting.setOnClickListener(new setOnClickLst());
        logoutarea.setOnClickListener(new setOnClickLst());
        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        logoutimage.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_menu_logout));
        logoutTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGNOUT_TXT"));
        walletbalncetxt = (MTextView) view.findViewById(R.id.walletbalncetxt);


        android.view.Display display = ((android.view.WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        left_linear.getLayoutParams().width = display.getWidth() * 75 / 100;
        left_linear.requestLayout();


        obj_userProfile = generalFunc.getJsonObject(userProfileJson);


        buildDrawer();
        //setUserInfo();

        // if (mContext instanceof UberXActivity/* || mContext instanceof MainActivity*/) {
        (view.findViewById(R.id.menuImgView)).setOnClickListener(new setOnClickLst());
        // }

        Managemenu();
    }

    public void Managemenu() {
        if (generalFunc.getMemberId().equalsIgnoreCase("")) {
            logoutarea.setVisibility(View.GONE);
            walletbalncetxt.setVisibility(View.GONE);
        } else {
            userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            obj_userProfile = generalFunc.getJsonObject(userProfileJson);
            walletbalncetxt.setVisibility(View.VISIBLE);
            logoutarea.setVisibility(View.VISIBLE);
        }
        buildDrawer();
    }

    public void setwalletText(String msg) {
        walletbalncetxt = (MTextView) view.findViewById(R.id.walletbalncetxt);
        walletbalncetxt.setText(msg);
    }

    public void changeUserProfileJson(String userProfileJson) {
        this.userProfileJson = userProfileJson;
        setUserInfo();
    }

    public void configDrawer(boolean isHide) {

        (view.findViewById(R.id.left_linear)).setVisibility(View.GONE);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) (view.findViewById(R.id.left_linear)).getLayoutParams();
        params.gravity = isHide ? Gravity.NO_GRAVITY : GravityCompat.START;
        (view.findViewById(R.id.left_linear)).setLayoutParams(params);
    }

    public void setMenuState(boolean isMenuState) {
        this.isMenuState = isMenuState;

        if (!this.isMenuState) {
            ((ImageView) view.findViewById(R.id.menuImgView)).setImageResource(R.mipmap.ic_back_arrow);

            configDrawer(true);

        } else {
            ((ImageView) view.findViewById(R.id.menuImgView)).setImageResource(R.mipmap.ic_drawer_menu);

            configDrawer(false);
        }
    }

    public void setIsDriverAssigned(boolean isDriverAssigned) {
        this.isDriverAssigned = isDriverAssigned;
    }

    public void buildDrawer() {
        list_menu_items = new ArrayList();
        drawerAdapter = new DrawerAdapter(list_menu_items, mContext);

        menuListView.setAdapter(drawerAdapter);
        menuListView.setOnItemClickListener(this);

        String APP_TYPE = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);
        String ENABLE_FAVORITE_DRIVER_MODULE_KEY = generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY);
        boolean isDeliverOnlyEnabled = generalFunc.isDeliverOnlyEnabled();
        boolean isAnyDeliverOptionEnabled = generalFunc.isAnyDeliverOptionEnabled();


        if (isDeliverOnlyEnabled) {
            //  list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_order, generalFunc.retrieveLangLBl("", "LBL_ORDERS"), "" + Utils.MENU_ORDER});

            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_cart, generalFunc.retrieveLangLBl("", "LBL_CART"), "" + Utils.MENU_CART});

            if (!generalFunc.getMemberId().equalsIgnoreCase("")) {
                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_profile, generalFunc.retrieveLangLBl("", "LBL_MY_PROFILE_HEADER_TXT"), "" + Utils.MENU_PROFILE});
            } else {
                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_profile, generalFunc.retrieveLangLBl("", "LBL_LOGIN_SIGNUP"), "" + Utils.MENU_PROFILE});
            }

        }

        if (isAnyDeliverOptionEnabled && !isDeliverOnlyEnabled) {

            if (!generalFunc.getMemberId().equalsIgnoreCase("")) {
                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_profile, generalFunc.retrieveLangLBl("", "LBL_MY_PROFILE_HEADER_TXT"), "" + Utils.MENU_PROFILE});
            } else {
                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_profile, generalFunc.retrieveLangLBl("", "LBL_LOGIN_SIGNUP"), "" + Utils.MENU_PROFILE});
            }


            if (ENABLE_FAVORITE_DRIVER_MODULE_KEY.equalsIgnoreCase("Yes") && !isDeliverOnlyEnabled) {
                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_fav_driver, generalFunc.retrieveLangLBl("Favorite Driver", "LBL_MENU_FAV_DRIVER"), "" + FAVDRIVER});
            }


            // list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_order, generalFunc.retrieveLangLBl("", "LBL_ORDERS"), "" + Utils.MENU_ORDER});

            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_cart, generalFunc.retrieveLangLBl("", "LBL_CART"), "" + Utils.MENU_CART});

        }

        if (!isAnyDeliverOptionEnabled) {
            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_profile, generalFunc.retrieveLangLBl("", "LBL_MY_PROFILE_HEADER_TXT"), "" + Utils.MENU_PROFILE});

            if (ENABLE_FAVORITE_DRIVER_MODULE_KEY.equalsIgnoreCase("Yes") && !isDeliverOnlyEnabled) {
                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_fav_driver, generalFunc.retrieveLangLBl("Favorite Driver", "LBL_MENU_FAV_DRIVER"), "" + FAVDRIVER});
            }
        }


        if (!isDeliverOnlyEnabled) {

            if (generalFunc.getJsonValueStr("ENABLE_CORPORATE_PROFILE", obj_userProfile).equalsIgnoreCase("Yes")) {
                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_business_profile, generalFunc.retrieveLangLBl("", "LBL_BUSINESS_PROFILE"), "" + Utils.MENU_BUSINESS_PROFILE});
            }

            String menuMsgYourTrips = "";
            if (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_TRIPS");
            } else if (APP_TYPE.equalsIgnoreCase("Delivery")) {
                menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_DELIVERY");
            } else if (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_BOOKING");
            } else {
                menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_BOOKING");
            }

            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_yourtrip, menuMsgYourTrips, "" + Utils.MENU_YOUR_TRIPS});

            if (generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
                if (!generalFunc.getJsonValueStr("APP_PAYMENT_MODE", obj_userProfile).equalsIgnoreCase("Cash")) {
                    list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_card, generalFunc.retrieveLangLBl("Payment", "LBL_PAYMENT"), "" + Utils.MENU_PAYMENT});
                }
            }
        }

        if (!generalFunc.getJsonValueStr(Utils.WALLET_ENABLE, obj_userProfile).equals("") &&
                generalFunc.getJsonValueStr(Utils.WALLET_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")) {
            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_wallet, generalFunc.retrieveLangLBl("", "LBL_LEFT_MENU_WALLET"), "" + Utils.MENU_WALLET});
        }

        if (!isDeliverOnlyEnabled) {
//            if (APP_TYPE.equals(Utils.CabGeneralType_UberX)) {
//                list_menu_items.add(new String[]{"" + R.mipmap.menu_ongoing, generalFunc.retrieveLangLBl("On Going trip", "LBL_ONGOING_JOB"), "" + Utils.MENU_ONGOING_TRIPS});
//            }
//            if ((APP_TYPE.equals(Utils.CabGeneralTypeRide_Delivery_UberX) || generalFunc.isMultiDelivery()) && !APP_TYPE.equals(Utils.CabGeneralType_Ride)) {
//                list_menu_items.add(new String[]{"" + R.mipmap.menu_ongoing, generalFunc.retrieveLangLBl("On Going trip", "LBL_ONGOING_JOB"), "" + Utils.MENU_ONGOING_TRIPS});
//            }
//
//            if (!generalFunc.getJsonValueStr("eEmailVerified", obj_userProfile).equalsIgnoreCase("YES") ||
//                    !generalFunc.getJsonValueStr("ePhoneVerified", obj_userProfile).equalsIgnoreCase("YES")) {
//                list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_privacy, generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_TXT"), "" + Utils.MENU_ACCOUNT_VERIFY});
//            }

            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_emergency, generalFunc.retrieveLangLBl("Emergency Contact", "LBL_EMERGENCY_CONTACT"), "" + Utils.MENU_EMERGENCY_CONTACT});
        }

        if (!generalFunc.getJsonValueStr(Utils.REFERRAL_SCHEME_ENABLE, obj_userProfile).equals("") && generalFunc.getJsonValueStr(Utils.REFERRAL_SCHEME_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")) {
            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_invite, generalFunc.retrieveLangLBl("Invite Friends", "LBL_INVITE_FRIEND_TXT"), "" + Utils.MENU_INVITE_FRIEND});
        }


        if (generalFunc.getJsonValue("ENABLE_NEWS_SECTION", userProfileJson) != null && generalFunc.getJsonValue("ENABLE_NEWS_SECTION", userProfileJson).equalsIgnoreCase("yes")) {
            list_menu_items.add(new String[]{"" + R.drawable.ic_notification_menu, generalFunc.retrieveLangLBl("Notifications", "LBL_NOTIFICATIONS"), "" + Utils.MENU_NOTIFICATION});
        }

        if (generalFunc.getJsonValueStr(Utils.DONATION_ENABLE, obj_userProfile).equalsIgnoreCase("Yes") && APP_TYPE.equals(Utils.CabGeneralType_Ride)) {
            list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_donation, generalFunc.retrieveLangLBl("Donation", "LBL_DONATE"), "" + Utils.MENU_DONATION});
        }

        list_menu_items.add(new String[]{"" + R.mipmap.ic_menu_support, generalFunc.retrieveLangLBl("Support", "LBL_SUPPORT_HEADER_TXT"), "" + Utils.MENU_SUPPORT});

        drawerAdapter.notifyDataSetChanged();
    }

    public void setUserInfo() {
        ((MTextView) view.findViewById(R.id.userNameTxt)).setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        //((MTextView) view.findViewById(R.id.walletbalncetxt)).setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));
        setWalletInfo();

        (new AppFunctions(mContext)).checkProfileImage((SelectableRoundedImageView) view.findViewById(R.id.userImgView), userProfileJson, "vImgName");
    }

    public void setWalletInfo() {

        String text1 = generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson));
        String text2 = "\n" + generalFunc.retrieveLangLBl("wallet Balance", "LBL_WALLET_BALANCE");
        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(18, mContext)), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(12, mContext)), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), 0, text2.length(), 0);
        CharSequence finalText = TextUtils.concat(span1, "", span2);

        walletbalncetxt.setText(finalText);
    }

    public void openMenuProfile() {
        Bundle bn = new Bundle();
        bn.putString("isDriverAssigned", "" + isDriverAssigned);
        new StartActProcess(mContext).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
        // new StartActProcess(mContext).startActForResult(MyProfileNewActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        int itemId = GeneralFunctions.parseIntegerValue(0, list_menu_items.get(position)[2]);
        Bundle bn = new Bundle();
        switch (itemId) {
            // deliver all
            case Utils.MENU_ORDER:
                if (generalFunc.getMemberId().equals("")) {
                    new StartActProcess(mContext).startAct(LoginActivity.class);
                } else {
                    new StartActProcess(mContext).startAct(ActiveOrderActivity.class);
                }

                break;
            case Utils.MENU_CART:
                new StartActProcess(mContext).startAct(EditCartActivity.class);

                break;

            case Utils.MENU_PROFILE:
                if (generalFunc.isDeliverOnlyEnabled()) {
                    if (generalFunc.getMemberId().equals("")) {
                        new StartActProcess(mContext).startAct(LoginActivity.class);
                    } else {
                        openMenuProfile();
                    }
                } else {
                    openMenuProfile();
                }

                break;
            case Utils.MENU_BUSINESS_PROFILE:
                new StartActProcess(mContext).startActWithData(BusinessProfileActivity.class, bn);
                break;
            case Utils.MENU_VEHICLE:

                break;
            case Utils.MENU_RIDE_HISTORY:
                new StartActProcess(mContext).startActWithData(BookingActivity.class, bn);
                break;
            case Utils.MENU_BOOKINGS:
                new StartActProcess(mContext).startActWithData(BookingActivity.class, bn);
                break;
            case Utils.MENU_ONGOING_TRIPS:
                if (mainActivity != null && mainActivity.driverAssignedHeaderFrag != null) {
                    bn.putString("isTripRunning", "yes");
                }
                new StartActProcess(mContext).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                break;
            case Utils.MENU_ABOUT_US:
                new StartActProcess(mContext).startAct(StaticPageActivity.class);
                break;
            case Utils.MENU_POLICY:
                (new StartActProcess(mContext)).openURL(CommonUtilities.SERVER_URL + "privacy-policy");
                break;
            case Utils.MENU_PAYMENT:

                bn.putBoolean("fromcabselection", false);
                new StartActProcess(mContext).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
                break;
            case Utils.MENU_CONTACT_US:
                new StartActProcess(mContext).startAct(ContactUsActivity.class);
                break;
            case Utils.MENU_HELP:
                new StartActProcess(mContext).startAct(HelpActivity.class);
                break;
            case Utils.MENU_EMERGENCY_CONTACT:
                new StartActProcess(mContext).startAct(EmergencyContactActivity.class);
                break;
            case Utils.MENU_NOTIFICATION:
                new StartActProcess(mContext).startAct(NotificationActivity.class);
                break;

            case Utils.MENU_DONATION:
                new StartActProcess(mContext).startActWithData(DonationActivity.class, bn);
                break;

            case Utils.MENU_SUPPORT:
                new StartActProcess(mContext).startAct(SupportActivity.class);
                break;

            case Utils.MENU_WALLET:
                new StartActProcess(mContext).startActWithData(MyWalletActivity.class, bn);
                break;

            case FAVDRIVER:
                new StartActProcess(mContext).startActWithData(FavouriteDriverActivity.class, bn);
                break;


            case Utils.MENU_ACCOUNT_VERIFY:
                if (!generalFunc.getJsonValue("eEmailVerified", userProfileJson).equalsIgnoreCase("YES") ||
                        !generalFunc.getJsonValue("ePhoneVerified", userProfileJson).equalsIgnoreCase("YES")) {

                    Bundle bn1 = new Bundle();
                    if (!generalFunc.getJsonValue("eEmailVerified", userProfileJson).equalsIgnoreCase("YES") &&
                            !generalFunc.getJsonValue("ePhoneVerified", userProfileJson).equalsIgnoreCase("YES")) {
                        bn1.putString("msg", "DO_EMAIL_PHONE_VERIFY");
                    } else if (!generalFunc.getJsonValue("eEmailVerified", userProfileJson).equalsIgnoreCase("YES")) {
                        bn1.putString("msg", "DO_EMAIL_VERIFY");
                    } else if (!generalFunc.getJsonValue("ePhoneVerified", userProfileJson).equalsIgnoreCase("YES")) {
                        bn1.putString("msg", "DO_PHONE_VERIFY");
                    }

                    new StartActProcess(mContext).startActForResult(VerifyInfoActivity.class, bn1, Utils.VERIFY_INFO_REQ_CODE);
                }
                break;
            case Utils.MENU_YOUR_TRIPS:
                bn.putBoolean("isrestart", false);
                new StartActProcess(mContext).startActWithData(BookingActivity.class, bn);
                break;
            case Utils.MENU_INVITE_FRIEND:
                if (generalFunc.isDeliverOnlyEnabled()) {
                    if (generalFunc.getMemberId().equals("")) {
                        new StartActProcess(mContext).startAct(LoginActivity.class);
                    } else {
                        new StartActProcess(mContext).startActWithData(InviteFriendsActivity.class, bn);
                    }
                } else {
                    new StartActProcess(mContext).startActWithData(InviteFriendsActivity.class, bn);
                }


                break;
            case Utils.MENU_SIGN_OUT:

                final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                generateAlert.setCancelable(false);
                generateAlert.setBtnClickList(btn_id -> {
                    if (btn_id == 0) {
                        generateAlert.closeAlertBox();
                    } else {
                        MyApp.getInstance().logOutFromDevice(false);
                    }

                });

                generateAlert.setContentMessage(generalFunc.retrieveLangLBl("Logout", "LBL_LOGOUT"), generalFunc.retrieveLangLBl("Are you sure you want to logout?", "LBL_WANT_LOGOUT_APP_TXT"));
                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
                generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
                generateAlert.showAlertBox();
                break;
        }
        closeDrawer();
    }

    public void closeDrawer() {
        if (isback) {
            return;
        }
        (view.findViewById(R.id.left_linear)).setVisibility(View.GONE);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public boolean checkDrawerState(boolean isOpenDrawer) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
            return true;
        } else if (isOpenDrawer) {
            openDrawer();
        }
        return false;
    }

    public void openDrawer() {

        (view.findViewById(R.id.left_linear)).setVisibility(View.VISIBLE);
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void setMenuAction() {
        if (isMenuState) {
            openDrawer();
        } else {
            setMenuState(true);
            if (drawerClickListener != null) {
                drawerClickListener.onClick();
            }
        }
    }

    public void setItemClickList(DrawerClickListener itemClickList) {
        this.drawerClickListener = itemClickList;
    }

    public interface DrawerClickListener {
        void onClick();
    }

    public class setOnClickLst implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.menuImgView:
                    setMenuAction();
                    break;
                case R.id.backImgView:
                    setMenuAction();
                    break;

                case R.id.imgSetting:

                    if (generalFunc.isDeliverOnlyEnabled()) {
                        if (generalFunc.getMemberId().equals("")) {
                            new StartActProcess(mContext).startAct(LoginActivity.class);
                        } else {
                          //  menuListView.performItemClick(view, 2, Utils.MENU_PROFILE);
                            new StartActProcess(mContext).startAct(MenuSettingActivity.class);
                            closeDrawer();
                        }

                    } else {
                       // menuListView.performItemClick(view, 0, Utils.MENU_PROFILE);
                        new StartActProcess(mContext).startAct(MenuSettingActivity.class);
                        closeDrawer();
                    }
                    break;

                case R.id.logoutarea:
                    final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {
                        if (btn_id == 0) {
                            generateAlert.closeAlertBox();
                        } else {
                            if (internetConnection.isNetworkConnected()) {
                                MyApp.getInstance().logOutFromDevice(false);
                            } else {
                                generalFunc.showMessage(logoutarea, generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
                            }
                        }

                    });
                    generateAlert.setContentMessage(generalFunc.retrieveLangLBl("Logout", "LBL_LOGOUT"), generalFunc.retrieveLangLBl("Are you sure you want to logout?", "LBL_WANT_LOGOUT_APP_TXT"));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
                    generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
                    generateAlert.showAlertBox();

                    break;
            }
        }
    }
}
