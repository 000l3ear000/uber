package com.melevicarbrasil.usuario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.files.DeliveryBannerAdapter;
import com.adapter.files.DeliveryIconAdapter;
import com.adapter.files.SubCategoryItemAdapter;
import com.melevicarbrasil.usuario.deliverAll.FoodDeliveryHomeActivity;
import com.fragments.MyBookingFragment;
import com.fragments.MyProfileFragment;
import com.fragments.MyWalletFragment;
import com.general.files.AddBottomBar;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.OpenAdvertisementDialog;
import com.general.files.OpenCatType;
import com.general.files.StartActProcess;
import com.model.DeliveryIconDetails;
import com.utils.Logger;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CommonDeliveryTypeSelectionActivity extends AppCompatActivity implements SubCategoryItemAdapter.setSubCategoryClickList, DeliveryBannerAdapter.OnBannerItemClickList {

    ImageView backImgView;
    GeneralFunctions generalFunc;
    RecyclerView mainRecyleView;
    ArrayList<DeliveryIconDetails> list = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_item = new ArrayList<>();
    DeliveryIconAdapter deliveryIconAdapter;
    DeliveryBannerAdapter deliveryBannerAdapter;
    MTextView titleTxt;
    String userProfileJson = "";
    ImageView headerLogo;
    public boolean iswallet = false;
    int position = -1;
    boolean isClicked = false;
    LinearLayout bottomMenuArea;
    AddBottomBar addBottomBar;
    FrameLayout container;
    MyProfileFragment myProfileFragment;
    MyWalletFragment myWalletFragment;
    public MyBookingFragment myBookingFragment;

    private static final int SEL_CARD = 004;
    public static final int TRANSFER_MONEY = 87;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type_selection);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        initView();

        backImgView.setOnClickListener(new setOnClickList());

        getDetails();
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        if (Utils.checkText(generalFunc.getMemberId())) {

            addBottomBar = new AddBottomBar(getActContext(), generalFunc.getJsonObject(userProfileJson));
        }
        String app_type = generalFunc.getJsonValue("APP_TYPE", userProfileJson);


        if (app_type.equalsIgnoreCase("Ride-Delivery") || app_type.equalsIgnoreCase("Delivery")) {
            String advertise_banner_data = generalFunc.getJsonValue("advertise_banner_data", userProfileJson);
            if (advertise_banner_data != null && !advertise_banner_data.equalsIgnoreCase("")) {

                String image_url = generalFunc.getJsonValue("image_url", advertise_banner_data);
                if (image_url != null && !image_url.equalsIgnoreCase("")) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("image_url", image_url);
                    map.put("tRedirectUrl", generalFunc.getJsonValue("tRedirectUrl", advertise_banner_data));
                    map.put("vImageWidth", generalFunc.getJsonValue("vImageWidth", advertise_banner_data));
                    map.put("vImageHeight", generalFunc.getJsonValue("vImageHeight", advertise_banner_data));
                    new OpenAdvertisementDialog(getActContext(), map, generalFunc);
                }
            }
        }

        if (app_type.equalsIgnoreCase("Delivery") || app_type.equalsIgnoreCase("Ride-Delivery")) {

            backImgView.setVisibility(View.GONE);
            bottomMenuArea.setVisibility(View.VISIBLE);
            headerLogo.setVisibility(View.VISIBLE);
        } else {

        }
    }

    public void getDetails() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getServiceCategoryDetails");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iVehicleCategoryId", getIntent().getStringExtra("iVehicleCategoryId"));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                int dimensions = Utils.dipToPixels(getActContext(), 110);
                if (isDataAvail) {

                    String eDetailPageView = generalFunc.getJsonValueStr("eDetailPageView", responseObj);

                    if (eDetailPageView.equalsIgnoreCase("Icon")) {
                        JSONArray messagestr = generalFunc.getJsonArray(Utils.message_str, responseObj);

                        if (messagestr != null && messagestr.length() > 0) {

                            for (int i = 0; i < messagestr.length(); i++) {
                                JSONObject obj_temp = generalFunc.getJsonObject(messagestr, i);
                                DeliveryIconDetails map = new DeliveryIconDetails();
                                map.setvCategory(generalFunc.getJsonValueStr("vCategory", obj_temp));
                                map.settCategoryDesc(generalFunc.getJsonValueStr("tCategoryDesc", obj_temp));
                                map.setPos(i);

                                JSONArray subCategoryArray = generalFunc.getJsonArray("SubCategory", obj_temp);
                                if (subCategoryArray != null && subCategoryArray.length() > 0) {
                                    ArrayList<DeliveryIconDetails.SubCatData> subcatdata = new ArrayList<>();
                                    for (int j = 0; j < subCategoryArray.length(); j++) {

                                        JSONObject subobj_temp = generalFunc.getJsonObject(subCategoryArray, j);

                                        DeliveryIconDetails.SubCatData submap = new DeliveryIconDetails.SubCatData();
                                        submap.seteCatType(generalFunc.getJsonValueStr("eCatType", subobj_temp));
                                        submap.setiServiceId(generalFunc.getJsonValueStr("iServiceId", subobj_temp));
                                        submap.setvSubCategory(generalFunc.getJsonValueStr("vCategory", subobj_temp));
                                        submap.settSubCategoryDesc(generalFunc.getJsonValueStr("tCategoryDesc", subobj_temp));
                                        submap.setvImage(generalFunc.getJsonValueStr("vImage", subobj_temp));
                                        submap.seteDeliveryType(generalFunc.getJsonValueStr("eDeliveryType", subobj_temp));

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("vCategory", generalFunc.getJsonValueStr("vCategory", subobj_temp));
                                        hashMap.put("tCategoryDesc", generalFunc.getJsonValueStr("tCategoryDesc", subobj_temp));
                                        hashMap.put("eCatType", generalFunc.getJsonValueStr("eCatType", subobj_temp));
                                        hashMap.put("iServiceId", generalFunc.getJsonValueStr("iServiceId", subobj_temp));
                                        hashMap.put("tBannerButtonText", generalFunc.getJsonValueStr("tBannerButtonText", subobj_temp));
                                        hashMap.put("vImage", generalFunc.getJsonValueStr("vImage", subobj_temp));

                                        String imageUrl = Utilities.getResizeImgURL(getActContext(), hashMap.get("vImage"), dimensions, dimensions);
                                        submap.setvImage(imageUrl);

                                        hashMap.put("vImage", imageUrl);

                                        hashMap.put("eDeliveryType", generalFunc.getJsonValueStr("eDeliveryType", subobj_temp));

                                        submap.setDataMap(hashMap);

                                        subcatdata.add(submap);

                                    }
                                    map.setSubData(subcatdata);
                                }
                                list.add(map);
                            }
                        }
                        deliveryIconAdapter = new DeliveryIconAdapter(generalFunc, list, getActContext(), this::itemSubCategoryClick);
                        mainRecyleView.setAdapter(deliveryIconAdapter);
                    } else {
                        JSONArray messagestr = generalFunc.getJsonArray(Utils.message_str, responseObj);

                        if (messagestr != null && messagestr.length() > 0) {

                            int bannerWidth = Utils.getWidthOfBanner(getActContext(), 0);
                            int bannerHeight = Utils.getHeightOfBanner(getActContext(), Utils.dipToPixels(getActContext(), 10), "16:9");

                            for (int i = 0; i < messagestr.length(); i++) {
                                JSONObject obj_temp = generalFunc.getJsonObject(messagestr, i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("vCategory", generalFunc.getJsonValueStr("vCategory", obj_temp));
                                hashMap.put("tCategoryDesc", generalFunc.getJsonValueStr("tCategoryDesc", obj_temp));
                                hashMap.put("eCatType", generalFunc.getJsonValueStr("eCatType", obj_temp));
                                hashMap.put("iServiceId", generalFunc.getJsonValueStr("iServiceId", obj_temp));
                                hashMap.put("tBannerButtonText", generalFunc.getJsonValueStr("tBannerButtonText", obj_temp));
                                hashMap.put("vImage", generalFunc.getJsonValueStr("vImage", obj_temp));

                                String imageURL = Utilities.getResizeImgURL(getActContext(), hashMap.get("vImage"), bannerWidth, bannerHeight);
                                hashMap.put("vImage", imageURL);

                                hashMap.put("eDeliveryType", generalFunc.getJsonValueStr("eDeliveryType", obj_temp));
                                list_item.add(hashMap);
                            }
                            deliveryBannerAdapter = new DeliveryBannerAdapter(getActContext(), list_item);
                            deliveryBannerAdapter.setOnItemClickList(this::onBannerItemClick);
                            mainRecyleView.setAdapter(deliveryBannerAdapter);
                        }

                    }

                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseObj)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public void openProfileFragment() {
        isProfilefrg = true;
        isWalletfrg = false;
        isBookingfrg = false;
//        if (myProfileFragment != null) {
//            myProfileFragment = null;
//            Utils.runGC();
//        }


        container.setVisibility(View.VISIBLE);
        if (myProfileFragment == null) {
            myProfileFragment = new MyProfileFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myProfileFragment).commit();


    }

    boolean isProfilefrg = false;
    boolean isWalletfrg = false;
    boolean isBookingfrg = false;

    public void openWalletFragment() {
        isProfilefrg = false;
        isWalletfrg = true;
        isBookingfrg = false;

//        if (myProfileFragment != null) {
//            myProfileFragment = null;
//            Utils.runGC();
//        }


        container.setVisibility(View.VISIBLE);
        if (myWalletFragment == null) {
            myWalletFragment = new MyWalletFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myWalletFragment).commit();


    }

    public void manageHome() {
        isProfilefrg = false;
        isWalletfrg = false;
        isBookingfrg = false;
        container.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        isClicked = false;
        position = -1;

        if (myProfileFragment != null && isProfilefrg) {
            myProfileFragment.onResume();
        }

        if (myWalletFragment != null && isWalletfrg) {
            myWalletFragment.onResume();
        }

        if (myBookingFragment != null && isBookingfrg) {
            myBookingFragment.onResume();
        }

        if (generalFunc.retrieveValue(Utils.ISWALLETBALNCECHANGE).equalsIgnoreCase("Yes")) {
            // getWalletBalDetails();
        }

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        //  setUserInfo();


        if (iswallet) {

            iswallet = false;
        }


    }


    public void openHistoryFragment() {

        isProfilefrg = false;
        isWalletfrg = false;
        isBookingfrg = true;
        container.setVisibility(View.VISIBLE);
        if (myBookingFragment == null) {
            myBookingFragment = new MyBookingFragment();
        } else {
            myBookingFragment.onDestroy();
            myBookingFragment = new MyBookingFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myBookingFragment).commit();
    }


    private void initView() {
        container = (FrameLayout) findViewById(R.id.container);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        bottomMenuArea = (LinearLayout) findViewById(R.id.bottomMenuArea);

        headerLogo = (ImageView) findViewById(R.id.headerLogo);
        mainRecyleView = (RecyclerView) findViewById(R.id.mainRecyleView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView.setVisibility(View.VISIBLE);
        titleTxt.setText(getIntent().getStringExtra("vCategory"));
        mainRecyleView.setClipToPadding(false);

    }

    public Activity getActContext() {
        return CommonDeliveryTypeSelectionActivity.this;
    }

    private void getLanguageLabelServiceWise() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getUserLanguagesAsPerServiceType");
        parameters.put("LanguageCode", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    generalFunc.storeData(Utils.languageLabelsKey, generalFunc.getJsonValue(Utils.message_str, responseString));
                    generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vLanguageCode", responseString));
                    generalFunc.storeData(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("langType", responseString));

                    generalFunc.storeData(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", responseString));
                    GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());

                    Utils.setAppLocal(getActContext());

                    Bundle bn = new Bundle();
                    bn.putBoolean("isback", true);


                    new StartActProcess(getActContext()).startActWithData(FoodDeliveryHomeActivity.class, bn);
                } else {
                    errorCallApi();
                }
            } else {
                errorCallApi();
            }

        });
        exeWebServer.execute();
    }

    private void errorCallApi() {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            if (btn_id == 0) {
                generateAlert.closeAlertBox();
            } else {
                getLanguageLabelServiceWise();
                generateAlert.closeAlertBox();
            }
        });
        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_ERROR_TXT"));
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"));
        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        generateAlert.showAlertBox();
    }


    @Override
    public void itemSubCategoryClick(int position, String eDeliveryType, HashMap<String, String> dataMap) {
        Logger.d("CheckData", "::" + getIntent().getStringExtra("latitude") + "::" + getIntent().getStringExtra("longitude") + "::" + getIntent().getStringExtra("address"));
        dataMap.put("latitude", getIntent().getStringExtra("latitude"));
        dataMap.put("longitude", getIntent().getStringExtra("longitude"));
        dataMap.put("address", getIntent().getStringExtra("address"));


        (new OpenCatType(getActContext(), dataMap)).execute();
    }

    @Override
    public void onBannerItemClick(int position) {
        if (!isClicked && this.position != position) {
            isClicked = true;
            this.position = position;
            (new OpenCatType(getActContext(), list_item.get(position))).execute();
        }

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Bundle bn = new Bundle();
            bn.putString("selType", getIntent().getStringExtra("selType"));
            bn.putBoolean("isRestart", getIntent().getBooleanExtra("isRestart", false));
            if (i == R.id.backImgView) {
                onBackPressed();
            }
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


            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    try {
                        generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "No");
                        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        JSONObject object = generalFunc.getJsonObject(userProfileJson);
                        object.put("user_available_balance", generalFunc.getJsonValue("MemberBalance", responseString));
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, object.toString());

                        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);


                        //setUserInfo();
                    } catch (Exception e) {

                    }
                }
            }
        });
        exeWebServer.execute();
    }


//    public void setUserInfo() {
//        Logger.d("TRACK_FLOW", "setUserInfo");
//
//        View view = ((Activity) getActContext()).findViewById(android.R.id.content);
//        ((MTextView) view.findViewById(R.id.userNameTxt)).setText(generalFunc.getJsonValue("vName", userProfileJson) + " "
//                + generalFunc.getJsonValue("vLastName", userProfileJson));
//        ((MTextView) view.findViewById(R.id.walletbalncetxt)).setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson)));
//
//        (new AppFunctions(getActContext())).checkProfileImage((SelectableRoundedImageView) view.findViewById(R.id.userImgView), userProfileJson, "vImgName");
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.d("TRACK_FLOW", "onActivityResult");
        Logger.d("TRACK_FLOW", "requestCode" + requestCode + "\n" + "resultCode" + resultCode + "\n" + "data" + data);

        if (requestCode == Utils.MY_PROFILE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            if (myProfileFragment != null) {
                myProfileFragment.onActivityResult(requestCode, resultCode, data);
            }

        } else if (requestCode == Utils.VERIFY_INFO_REQ_CODE && resultCode == RESULT_OK && data != null) {

            String msgType = data.getStringExtra("MSG_TYPE");


            this.userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
//            obj_userProfile = generalFunc.getJsonObject(userProfileJson);

        } else if (requestCode == Utils.VERIFY_INFO_REQ_CODE) {

            this.userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
//            obj_userProfile = generalFunc.getJsonObject(userProfileJson);

        } else if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            iswallet = true;

            String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
//            obj_userProfile = generalFunc.getJsonObject(userProfileJson);
            this.userProfileJson = userProfileJson;


        } else if (requestCode == Utils.ASSIGN_DRIVER_CODE) {

            if (data != null && data.hasExtra("callGetDetail")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else {
                if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {

                    Bundle bn = new Bundle();
                    bn.putString("latitude", getIntent().getStringExtra("latitude"));
                    bn.putString("longitude", getIntent().getStringExtra("longitude"));
                    bn.putString("address", getIntent().getStringExtra("address"));
                    Logger.d("CheckData1111", "::" + getIntent().getStringExtra("latitude") + "::" + getIntent().getStringExtra("longitude") + "::" + getIntent().getStringExtra("address"));
                    new StartActProcess(getActContext()).startActWithData(MainActivity.class, bn);
                    finishAffinity();
                } else {

                    if ((generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("Active") ||
                            generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValue("eType", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {


                    } else {

                        Bundle bn = new Bundle();

                    }
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == SEL_CARD) {

            if (myWalletFragment != null) {
                myWalletFragment.onActivityResult(requestCode, resultCode, data);
            }


        } else if (resultCode == RESULT_OK && requestCode == TRANSFER_MONEY) {
            if (myWalletFragment != null) {
                myWalletFragment.onActivityResult(requestCode, resultCode, data);
            }


        }

    }


}
