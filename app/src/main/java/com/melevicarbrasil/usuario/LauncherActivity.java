package com.melevicarbrasil.usuario;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.widget.RelativeLayout;
import com.melevicarbrasil.usuario.deliverAll.FoodDeliveryHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.ServiceHomeActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.GetDeviceToken;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.OpenMainProfile;
import com.general.files.SetGeneralData;
import com.general.files.SetUserData;
import com.general.files.StartActProcess;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.anim.loader.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class LauncherActivity extends BaseActivity implements ProviderInstaller.ProviderInstallListener {

    AVLoadingIndicatorView loaderView;
    InternetConnection intCheck;
    GeneralFunctions generalFunc;

   // GetLocationUpdates getLastLocation;

    long autoLoginStartTime = 0;

    /*4.4 lower Device SSl CERTIFICATE ISSUE*/

    private static final int ERROR_DIALOG_REQUEST_CODE = 1;
    private boolean mRetryProviderInstall;
    RelativeLayout rlContentArea;
    private MTextView drawOverMsgTxtView;
    GenerateAlertBox currentAlertBox;

    /////
    boolean isPermissionShown_general;
    String response_str_generalConfigData = "";
    String response_str_autologin = "";
    private  static ArrayList<String> requestPermissions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        requestPermissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        requestPermissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
           // requestPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }



        loaderView = (AVLoadingIndicatorView) findViewById(R.id.loaderView);
        rlContentArea = (RelativeLayout) findViewById(R.id.rlContentArea);
      drawOverMsgTxtView = (MTextView) findViewById(R.id.drawOverMsgTxtView);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
       // getLastLocation = new GetLocationUpdates(getActContext(), 2, false, null);

        intCheck = new InternetConnection(this);

        generalFunc.storeData("isInLauncher", "true");
        drawOverMsgTxtView.setText(generalFunc.retrieveLangLBl("Please wait while we are checking app's configuration. This will take few seconds.", "LBL_DRAW_OVER_APP_NOTE"));


//        checkConfigurations(true);

        ProviderInstaller.installIfNeededAsync(this, this);


        //   new StartActProcess(getActContext()).startService(MyBackGroundService.class);

    }

    public void checkConfigurations(boolean isPermissionShown) {
        drawOverMsgTxtView.setVisibility(View.GONE);
        closeAlert();

        isPermissionShown_general = isPermissionShown;

        int status = (GoogleApiAvailability.getInstance()).isGooglePlayServicesAvailable(getActContext());

        if (status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            showErrorOnPlayServiceDialog(generalFunc.retrieveLangLBl("This application requires updated google play service. " +
                    "Please install Or update it from play store", "LBL_UPDATE_PLAY_SERVICE_NOTE"));
            return;
        } else if (status != ConnectionResult.SUCCESS) {
            showErrorOnPlayServiceDialog(generalFunc.retrieveLangLBl("This application requires updated google play service. " +
                    "Please install Or update it from play store", "LBL_UPDATE_PLAY_SERVICE_NOTE"));
            return;
        }

     /*   if (!generalFunc.isAllPermissionGranted(isPermissionShown)) {
            showNoPermission();
            return;
        }*/

        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
            showNoInternetDialog();
        } /*else {*/
           /* Location mLastLocation = getLastLocation.getLastLocation();
            if (mLastLocation == null) {
                getLastLocation.startLocationUpdates(false);
            }*/

        if (!generalFunc.canDrawOverlayViews(getActContext())) {
            GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
            generateAlert.setContentMessage("COLETA DE DADOS E PERMISSÕES",
                    generalFunc.retrieveLangLBl("Este aplicativo coleta dados de recursos como \"localização\", \"câmera\", \"contatos\", \"memória\", \"chamada telefônica\", \"gravação de áudio\" quando o aplicativo está em uso. Esses dados também são usados para fornecer anúncios/publicidade/serviços/suporte. Para aprimorar sua experiência com ofertas ou produtos e serviços mais relevantes para seu perfil. Para habilitar todas opções acima permita.", "LBL_ENABLE_DRWA_OVER_APP"));
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Permitir", "LBL_ALLOW"));
            generateAlert.showAlertBox();
            generateAlert.setCancelable(false);
            generateAlert.setBtnClickList(btn_id -> {
                if (btn_id == 1) {
                    (new StartActProcess(getActContext())).requestOverlayPermission(Utils.OVERLAY_PERMISSION_REQ_CODE);
                }
            });

            return;
        }

            continueProcess();
//        }

    }

    public void continueProcess() {
        closeAlert();
        showLoader();

        Utils.setAppLocal(getActContext());
        if (generalFunc.isUserLoggedIn() == true && Utils.checkText(generalFunc.getMemberId())) {

            if (getSinchServiceInterface() == null && !generalFunc.retrieveValue(Utils.SINCH_APP_KEY).equalsIgnoreCase("")) {
                new Handler().postDelayed(() -> continueProcess(), 1500);
            } else if (getSinchServiceInterface() != null) {
                autoLogin();
                if (!getSinchServiceInterface().isStarted()) {
                    getSinchServiceInterface().startClient(Utils.userType + "_" + generalFunc.getMemberId());
                    GetDeviceToken getDeviceToken = new GetDeviceToken(generalFunc);
                    getDeviceToken.setDataResponseListener(vDeviceToken -> {
                        if (!vDeviceToken.equals("")) {
                            try {
                                getSinchServiceInterface().getSinchClient().registerPushNotificationData(vDeviceToken.getBytes());
                            } catch (Exception ignored) {

                            }
                        }
                    });
                    getDeviceToken.execute();
                }
            } else {
                if (this.response_str_autologin.trim().equalsIgnoreCase("")) {
                    autoLogin();
                } else {

                    continueAutoLogin(this.response_str_autologin);
                }
            }


        } else {
            if (this.response_str_generalConfigData.trim().equalsIgnoreCase("")) {
                downloadGeneralData();
            } else {

                continueDownloadGeneralData(this.response_str_generalConfigData);
            }
        }

    }

    public void restartAppDailog() {
        closeAlert();
        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please try again.", "LBL_TRY_AGAIN_TXT"), generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"), "", buttonId -> generalFunc.restartApp());
    }


    public void downloadGeneralData() {
        closeAlert();
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "generalConfigData");
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (isFinishing()) {
                restartAppDailog();
                return;
            }


            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    String PACKAGE_TYPE = generalFunc.getJsonValue("PACKAGE_TYPE", responseString);
                    if (!PACKAGE_TYPE.equalsIgnoreCase("STANDARD")){
                        requestPermissions.add(Manifest.permission.RECORD_AUDIO);
                        requestPermissions.add(Manifest.permission.READ_PHONE_STATE);
                    }


                    String[] strings =   (String[])requestPermissions.toArray(new String[requestPermissions.size()]);
                    Logger.d("permissiossss", Arrays.toString(strings));
                    if (!generalFunc.isAllPermissionGranted(isPermissionShown_general,requestPermissions)) {
                        response_str_generalConfigData = responseString;
                        showNoPermission();
                        return;
                    }

                    continueDownloadGeneralData(responseString);

                    Logger.d("PACKAGE_TYPEGET_GC",""+PACKAGE_TYPE);

                } else {
                    if (!generalFunc.getJsonValue("isAppUpdate", responseString).trim().equals("")
                            && generalFunc.getJsonValue("isAppUpdate", responseString).equals("true")) {
                        showAppUpdateDialog(generalFunc.retrieveLangLBl("New update is available to download. " +
                                        "Downloading the latest update, you will get latest features, improvements and bug fixes.",
                                generalFunc.getJsonValue(Utils.message_str, responseString)));
                    } else {
                        showError();
                    }
                }
            } else {
                showError();
            }
        });
        exeWebServer.execute();
    }


    public void continueDownloadGeneralData(String responseString) {
        JSONObject responseObj=generalFunc.getJsonObject(responseString);

        Utils.setAppLocal(getActContext());
        closeLoader();

                    generalFunc.storeData("TSITE_DB", generalFunc.getJsonValue("TSITE_DB", responseString));
                    generalFunc.storeData("GOOGLE_API_REPLACEMENT_URL", generalFunc.getJsonValue("GOOGLE_API_REPLACEMENT_URL", responseString));
                    new SetGeneralData(generalFunc, responseObj);

        if (generalFunc.getJsonValue("SERVER_MAINTENANCE_ENABLE", responseString).equalsIgnoreCase("Yes")) {

            new StartActProcess(getActContext()).startAct(MaintenanceActivity.class);
            finish();
            return;
        }


        if (generalFunc.isDeliverOnlyEnabled()) {
            if(!generalFunc.isAllPermissionGranted(true,requestPermissions))
            {
                showNoPermission();
                return;
            }

            JSONArray serviceArray = generalFunc.getJsonArray("ServiceCategories", responseString);

            if (serviceArray.length() > 1) {

                ArrayList<HashMap<String, String>> list_item = new ArrayList<>();
                for (int i = 0; i < serviceArray.length(); i++) {
                    JSONObject serviceObj = generalFunc.getJsonObject(serviceArray, i);
                    HashMap<String, String> servicemap = new HashMap<>();
                    servicemap.put("iServiceId", generalFunc.getJsonValue("iServiceId", serviceObj.toString()));
                    servicemap.put("vServiceName", generalFunc.getJsonValue("vServiceName", serviceObj.toString()));
                    servicemap.put("vImage", generalFunc.getJsonValue("vImage", serviceObj.toString()));
                    list_item.add(servicemap);
                }
                Bundle bn = new Bundle();
                bn.putSerializable("servicedata", list_item);
                new StartActProcess(getActContext()).startActWithData(ServiceHomeActivity.class, bn);

            } else {
                new StartActProcess(getActContext()).startAct(FoodDeliveryHomeActivity.class);
            }
        } else {
            if(!generalFunc.isAllPermissionGranted(true,requestPermissions))
            {
                showNoPermission();
                return;
            }
            new StartActProcess(getActContext()).startAct(AppLoginActivity.class);
        }
        try {
            ActivityCompat.finishAffinity(LauncherActivity.this);
        } catch (Exception e) {

        }
    }


    public void autoLogin() {
        closeAlert();
        autoLoginStartTime = Calendar.getInstance().getTimeInMillis();

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDetail");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        parameters.put("UserType", Utils.app_type);
        if (!generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY).equalsIgnoreCase("")) {
            parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        }


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken", generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            closeLoader();

            if (isFinishing()) {
                return;
            }

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (generalFunc.getJsonValue("changeLangCode", responseString).equalsIgnoreCase("Yes")) {
                    //here to manage code
                    new SetUserData(responseString, generalFunc, getActContext(), false);
                }

                String message = generalFunc.getJsonValue(Utils.message_str, responseString);

                if (message.equals("SESSION_OUT")) {
                    autoLoginStartTime = 0;
                    MyApp.getInstance().notifySessionTimeOut();
                    Utils.runGC();
                    return;
                }

                if (isDataAvail) {

                    String PACKAGE_TYPE = generalFunc.getJsonValue("PACKAGE_TYPE", message);
                    if (!PACKAGE_TYPE.equalsIgnoreCase("STANDARD")){
                        requestPermissions.add(Manifest.permission.RECORD_AUDIO);
                        requestPermissions.add(Manifest.permission.READ_PHONE_STATE);
                    }


                    if (!generalFunc.isAllPermissionGranted(isPermissionShown_general,requestPermissions)) {
                        response_str_autologin = responseString;
                        showNoPermission();
                        return;
                    }

                    Logger.d("PACKAGE_TYPEGET_GD",""+PACKAGE_TYPE);
                    continueAutoLogin(responseString);

                } else {
                    autoLoginStartTime = 0;
                    if (!generalFunc.getJsonValue("isAppUpdate", responseString).trim().equals("")
                            && generalFunc.getJsonValue("isAppUpdate", responseString).equals("true")) {

                        showAppUpdateDialog(generalFunc.retrieveLangLBl("New update is available to download. " +
                                        "Downloading the latest update, you will get latest features, improvements and bug fixes.",
                                generalFunc.getJsonValue(Utils.message_str, responseString)));
                    } else {

                        if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER") ||
                                generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_ACC_DELETE_TXT")) {

                            showContactUs(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));

                            return;
                        }
                        showError("",
                                generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }
                }
            } else {
                autoLoginStartTime = 0;
                showError();
            }
        });
        exeWebServer.execute();
    }

    public void continueAutoLogin(String responseString) {
        String message = generalFunc.getJsonValue(Utils.message_str, responseString);
        if (generalFunc.getJsonValue("SERVER_MAINTENANCE_ENABLE", message).equalsIgnoreCase("Yes")) {
            new StartActProcess(getActContext()).startAct(MaintenanceActivity.class);
            finish();
            return;
        }

        generalFunc.storeData(Utils.USER_PROFILE_JSON, message);

                    generalFunc.storeData("TSITE_DB", generalFunc.getJsonValue("TSITE_DB", responseString));
                    generalFunc.storeData("GOOGLE_API_REPLACEMENT_URL", generalFunc.getJsonValue("GOOGLE_API_REPLACEMENT_URL", responseString));

                    generalFunc.storeData(Utils.DELIVERALL_KEY, generalFunc.getJsonValue("DELIVERALL", message));
                    generalFunc.storeData(Utils.ONLYDELIVERALL_KEY, generalFunc.getJsonValue("ONLYDELIVERALL", message));

        if (generalFunc.isDeliverOnlyEnabled()) {

            if(!generalFunc.isAllPermissionGranted(true,requestPermissions))
            {
                showNoPermission();
                return;
            }

            if (generalFunc.getJsonValue("vPhone", message).equals("") || generalFunc.getJsonValue("vEmail", message).equals("")) {
                //open account verification screen
                Bundle bn = new Bundle();
                bn.putBoolean("isRestart", true);
                new StartActProcess(getActContext()).startActForResult(AccountverificationActivity.class, bn, Utils.SOCIAL_LOGIN_REQ_CODE);
                return;

            }


            String ePhoneVerified = generalFunc.getJsonValue("ePhoneVerified", message);
            String vPhoneCode = generalFunc.getJsonValue("vPhoneCode", message);
            String vPhone = generalFunc.getJsonValue("vPhone", message);

            if (!ePhoneVerified.equals("Yes")) {
                Bundle bn = new Bundle();
                bn.putString("MOBILE", vPhoneCode + vPhone);
                bn.putString("msg", "DO_PHONE_VERIFY");
                bn.putBoolean("isrestart", true);
                bn.putString("isbackshow", "No");
                new StartActProcess(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
                return;
            }


            JSONArray serviceArray = generalFunc.getJsonArray("ServiceCategories", message);

            if (serviceArray != null && serviceArray.length() > 1 && generalFunc.isAnyDeliverOptionEnabled()) {

                ArrayList<HashMap<String, String>> list_item = new ArrayList<>();
                for (int i = 0; i < serviceArray.length(); i++) {
                    JSONObject serviceObj = generalFunc.getJsonObject(serviceArray, i);
                    HashMap<String, String> servicemap = new HashMap<>();
                    servicemap.put("iServiceId", generalFunc.getJsonValue("iServiceId", serviceObj.toString()));
                    servicemap.put("vServiceName", generalFunc.getJsonValue("vServiceName", serviceObj.toString()));
                    servicemap.put("vImage", generalFunc.getJsonValue("vImage", serviceObj.toString()));
                    list_item.add(servicemap);
                }
                Bundle bn = new Bundle();
                bn.putSerializable("servicedata", list_item);
                new StartActProcess(getActContext()).startActWithData(ServiceHomeActivity.class, bn);
                try {
                    ActivityCompat.finishAffinity(LauncherActivity.this);
                } catch (Exception e) {

                }

            } else {
                new StartActProcess(getActContext()).startAct(FoodDeliveryHomeActivity.class);
                try {
                    ActivityCompat.finishAffinity(LauncherActivity.this);
                } catch (Exception e) {

                }
            }
        } else {
            if (Calendar.getInstance().getTimeInMillis() - autoLoginStartTime < 2000) {
                new Handler().postDelayed(() -> new OpenMainProfile(getActContext(),
                        generalFunc.getJsonValue(Utils.message_str, responseString), true, generalFunc).startProcess(), 2000);
            } else {
                new OpenMainProfile(getActContext(),
                        generalFunc.getJsonValue(Utils.message_str, responseString), true, generalFunc).startProcess();
            }
        }

    }

    public void showLoader() {
        if (loaderView != null) {
            loaderView.setVisibility(View.VISIBLE);
        }
    }

    public void closeLoader() {
        if (loaderView != null) {
            loaderView.setVisibility(View.GONE);
        }
    }

    private void closeAlert() {
        try {
            if (currentAlertBox != null) {
                currentAlertBox.closeAlertBox();
                currentAlertBox = null;
            }
        } catch (Exception e) {

        }
    }

    public void showContactUs(String content) {
        closeAlert();
        currentAlertBox = generalFunc.showGeneralMessage("", content, generalFunc.retrieveLangLBl("Contact Us", "LBL_CONTACT_US_TXT"), generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"), buttonId -> {
            if (buttonId == 0) {
                new StartActProcess(getActContext()).startAct(ContactUsActivity.class);
                showContactUs(content);
            } else if (buttonId == 1) {
                MyApp.getInstance().logOutFromDevice(true);
            }
        });
    }

    public void showError() {
        closeAlert();
        currentAlertBox = generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please try again.", "LBL_TRY_AGAIN_TXT"), generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("Retry", "LBL_RETRY_TXT"), buttonId -> handleBtnClick(buttonId, "ERROR"));
    }

    public void showError(String title, String contentMsg) {
        closeAlert();
        currentAlertBox = generalFunc.showGeneralMessage(title, contentMsg, generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("Retry", "LBL_RETRY_TXT"), buttonId -> handleBtnClick(buttonId, "ERROR"));
    }

    public void showNoInternetDialog() {
        closeAlert();
        currentAlertBox = generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Sem internet para conectar", "LBL_NO_INTERNET_TXT"), generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("Retry", "LBL_RETRY_TXT"), buttonId -> handleBtnClick(buttonId, "NO_INTERNET"));
    }


    public void showNoPermission() {
        closeAlert();
        currentAlertBox = generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("O aplicativo requer permissão para funcionar. Por favor permita.",
                "LBL_ALLOW_PERMISSIONS_APP"), generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("Allow All", "LBL_ALLOW_ALL_TXT"), buttonId -> handleBtnClick(buttonId, "NO_PERMISSION"));
    }

    public void showErrorOnPlayServiceDialog(String content) {
        closeAlert();
        currentAlertBox = generalFunc.showGeneralMessage("", content, generalFunc.retrieveLangLBl("Retry", "LBL_RETRY_TXT"), generalFunc.retrieveLangLBl("Update", "LBL_UPDATE"), buttonId -> handleBtnClick(buttonId, "NO_PLAY_SERVICE"));
    }

    public void showAppUpdateDialog(String content) {
        closeAlert();
        currentAlertBox = generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("New update available", "LBL_NEW_UPDATE_AVAIL"), content, generalFunc.retrieveLangLBl("Retry", "LBL_RETRY_TXT"), generalFunc.retrieveLangLBl("Update", "LBL_UPDATE"), buttonId -> handleBtnClick(buttonId, "APP_UPDATE"));
    }


    public Context getActContext() {
        return LauncherActivity.this;
    }

    public boolean shouldShowPermission(){
        boolean permisssion_show_final = true;
        for(String item : requestPermissions){
            boolean permisssion_show = ActivityCompat.shouldShowRequestPermissionRationale(this, item);
            if(!permisssion_show){
                permisssion_show_final = false;
                break;
            }
        }
        return permisssion_show_final;
    }
    public void handleBtnClick(int buttonId, String alertType) {
        Logger.d("alertType",alertType);
        Utils.hideKeyboard(getActContext());
        if (buttonId == 0) {
            if (!alertType.equals("NO_PLAY_SERVICE") && !alertType.equals("APP_UPDATE")) {
                finish();
            } else {
                checkConfigurations(false);
            }
        } else {
            if (alertType.equals("NO_PLAY_SERVICE")) {

                boolean isSuccessfulOpen = new StartActProcess(getActContext()).openURL("market://details?id=com.google.android.gms");
                if (!isSuccessfulOpen) {
                    new StartActProcess(getActContext()).openURL("http://play.google.com/store/apps/details?id=com.google.android.gms");
                }
                checkConfigurations(false);
            } else if (alertType.equals("NO_PERMISSION")) {

                generalFunc.openSettings();
/*
                boolean b = shouldShowPermission();
                if (shouldShowPermission() == false) {
                    generalFunc.openSettings();
                } else if (!generalFunc.isAllPermissionGranted(false,requestPermissions)) {
                    generalFunc.isAllPermissionGranted(true,requestPermissions);
                    checkConfigurations(false);
                } else {
                    checkConfigurations(true);
                }*/

            } else if (alertType.equals("APP_UPDATE")) {
                boolean isSuccessfulOpen = new StartActProcess(getActContext()).openURL("market://details?id=" + BuildConfig.APPLICATION_ID);
                if (!isSuccessfulOpen) {
                    new StartActProcess(getActContext()).openURL("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                }
                checkConfigurations(false);
            } else if (!alertType.equals("NO_GPS")) {
                checkConfigurations(false);
            } else {
                new StartActProcess(getActContext()).
                        startActForResult(Settings.ACTION_LOCATION_SOURCE_SETTINGS, Utils.REQUEST_CODE_GPS_ON);
            }
        }
    }

    @Override
    public void onResume() {

        if (generalFunc.prefHasKey(Utils.iServiceId_KEY) && generalFunc != null /*&& !generalFunc.isDeliverOnlyEnabled()*/) {
            generalFunc.removeValue(Utils.iServiceId_KEY);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Utils.REQUEST_CODE_GPS_ON:
                checkConfigurations(false);
                break;
            case GeneralFunctions.MY_SETTINGS_REQUEST:
                checkConfigurations(false);
                break;
            case ERROR_DIALOG_REQUEST_CODE:
                mRetryProviderInstall = true;
                break;
            case Utils.OVERLAY_PERMISSION_REQ_CODE:
                drawOverMsgTxtView.setVisibility(View.GONE);
                if (!generalFunc.canDrawOverlayViews(getActContext())) {
                    drawOverMsgTxtView.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        checkConfigurations(true);
                    }, 15000);
                } else {
                    checkConfigurations(true);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GeneralFunctions.MY_PERMISSIONS_REQUEST: {

                if(!generalFunc.isAllPermissionGranted(false,requestPermissions))
                {
                   // showNoPermission();
                    return;
                }
                checkConfigurations(false);
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        generalFunc.storeData("isInLauncher", "false");
         // if (getLastLocation != null) {
        //     getLastLocation.stopLocationUpdates();
        // }
    }

    @Override
    public void onProviderInstalled() {
        checkConfigurations(true);
    }

    @Override
    public void onProviderInstallFailed(int errorCode, Intent intent) {
        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Recoverable error. Show a dialog prompting the user to
            // install/update/enable Google Play services.
            GooglePlayServicesUtil.showErrorDialogFragment(
                    errorCode,
                    this,
                    ERROR_DIALOG_REQUEST_CODE,
                    dialog -> {
                        // The user chose not to take the recovery action
                        onProviderInstallerNotAvailable();
                    });
        } else {
            // Google Play services is not available.
            onProviderInstallerNotAvailable();
        }
    }

    private void onProviderInstallerNotAvailable() {
        // This is reached if the provider cannot be updated for some reason.
        // App should consider all HTTP communication to be vulnerable, and take
        // appropriate action.
        checkConfigurations(true);
        showMessageWithAction(rlContentArea, generalFunc.retrieveLangLBl("provider cannot be updated for some reason.", "LBL_PROVIDER_NOT_AVALIABLE_TXT"));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mRetryProviderInstall) {
            ProviderInstaller.installIfNeededAsync(this, this);
        }
        mRetryProviderInstall = false;
    }

    public void showMessageWithAction(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(10000);
        snackbar.show();
    }

}
