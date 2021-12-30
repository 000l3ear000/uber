package com.general.files;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.melevicarbrasil.usuario.AccountverificationActivity;
import com.melevicarbrasil.usuario.BuildConfig;
import com.melevicarbrasil.usuario.CarWashBookingDetailsActivity;
import com.melevicarbrasil.usuario.CommonDeliveryTypeSelectionActivity;
import com.melevicarbrasil.usuario.LauncherActivity;
import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.MaintenanceActivity;
import com.melevicarbrasil.usuario.NetworkChangeReceiver;
import com.melevicarbrasil.usuario.OnGoingTripDetailsActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.UberXActivity;
import com.melevicarbrasil.usuario.UberXHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.TrackOrderActivity;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.utils.WeViewFontConfig;
import com.view.GenerateAlertBox;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Admin on 28-06-2016.
 */
public class MyApp extends Application {
    private static MyApp mMyApp;

    GeneralFunctions generalFun;
    boolean isAppInBackground = true;

    private GpsReceiver mGpsReceiver;
    private NetworkChangeReceiver mNetWorkReceiver = null;

    private Activity currentAct = null;

    public MainActivity mainAct;
    public UberXActivity uberXAct;
    public UberXHomeActivity uberXHomeAct;
    public CommonDeliveryTypeSelectionActivity commonDeliveryAct;
    public CarWashBookingDetailsActivity carWashAct;
    public OnGoingTripDetailsActivity onGoingTripDetailsAct = null;
    public TrackOrderActivity trackOrderActivity;
    GenerateAlertBox generateSessionAlert;
    GenerateAlertBox drawOverlayAppAlert;

    public static synchronized MyApp getInstance() {
        return mMyApp;
    }

    private  static ArrayList<String> requestPermissions = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //Utils.SERVER_CONNECTION_URL = CommonUtilities.SERVER_URL;
        Utils.SERVER_CONNECTION_URL = "http://192.168.1.141/";
        Utils.IS_APP_IN_DEBUG_MODE = "Yes";
        Utils.userType = BuildConfig.USER_TYPE;
        Utils.app_type = BuildConfig.USER_TYPE;
        Utils.USER_ID_KEY = BuildConfig.USER_ID_KEY;
        Utils.IS_OPTIMIZE_MODE_ENABLE = true;

        WeViewFontConfig.ASSETS_FONT_NAME = getResources().getString(R.string.systemRegular);
        WeViewFontConfig.FONT_FAMILY_NAME = getResources().getString(R.string.systemRegular_name);
        WeViewFontConfig.FONT_COLOR = "#343434";
        WeViewFontConfig.FONT_SIZE = "14px";

        ExecuteWebServerUrl.CUSTOM_APP_TYPE = "Ride-Delivery-UberX";
        ExecuteWebServerUrl.CUSTOM_UBERX_PARENT_CAT_ID = "";
        ExecuteWebServerUrl.DELIVERALL = "";
        ExecuteWebServerUrl.ONLYDELIVERALL = "";
        ExecuteWebServerUrl.FOODONLY = "";

        HashMap<String, String> storeData = new HashMap<>();
        storeData.put("SERVERURL", CommonUtilities.SERVER_URL);
        storeData.put("SERVERWEBSERVICEPATH", CommonUtilities.SERVER_WEBSERVICE_PATH);
        storeData.put("USERTYPE", BuildConfig.USER_TYPE);
        GeneralFunctions.storeData(storeData, this);
        try {
            Picasso.Builder builder = new Picasso.Builder(this);
//            builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(false);
            built.setLoggingEnabled(false);
            Picasso.setSingletonInstance(built);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setScreenOrientation();
        mMyApp = (MyApp) this.getApplicationContext();

        generalFun = new GeneralFunctions(this);

        try {
            AppEventsLogger.activateApp(this);
        } catch (Exception e) {
            Logger.d("FBError", "::" + e.toString());
        }

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("FoodApp.realm").schemaVersion(1).build();
        Realm.setDefaultConfiguration(config);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (mGpsReceiver == null) {
            registerReceiver();
        }

        Fabric.with(this, new Crashlytics());
    }

    public static Realm getRealmInstance() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        return Realm.getInstance(config);
    }

    public GeneralFunctions getGeneralFun(Context mContext) {
        return new GeneralFunctions(mContext, R.id.backImgView);
    }

    public GeneralFunctions getAppLevelGeneralFunc() {
        if (generalFun == null) {
            generalFun = new GeneralFunctions(this);
        }
        return generalFun;
    }

    public boolean isMyAppInBackGround() {
        return this.isAppInBackground;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

        Logger.d("Api", "Object Destroyed >> MYAPP onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        Logger.d("Api", "Object Destroyed >> MYAPP onTrimMemory");

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        Logger.d("Api", "Object Destroyed >> MYAPP onTerminate");
        removePubSub();

        if (generalFun.prefHasKey(Utils.iServiceId_KEY)) {
            generalFun.removeValue(Utils.iServiceId_KEY);
        }

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();

        removeVoIpSettings();
    }


    public void removePubSub() {
        releaseGpsReceiver();
        removeAllRunningInstances();
        terminatePuSubInstance();
    }

    public void refreshWithConfigData() {
        GetUserData objRefresh = new GetUserData(generalFun, MyApp.getInstance().getCurrentAct());
        objRefresh.GetConfigData();
    }


    private void releaseGpsReceiver() {
        if (mGpsReceiver != null)
            this.unregisterReceiver(mGpsReceiver);
        this.mGpsReceiver = null;
    }


    private void registerReceiver() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {

            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);

            this.mGpsReceiver = new GpsReceiver();
            this.registerReceiver(this.mGpsReceiver, mIntentFilter);
        }
    }

    private void removeAllRunningInstances() {
        Logger.e("NetWorkDEMO", "removeAllRunningInstances called");
        connectReceiver(false);
    }

    private void registerNetWorkReceiver() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO && mNetWorkReceiver == null) {
            try {
                Logger.e("NetWorkDemo", "Network connectivity registered");
                IntentFilter mIntentFilter = new IntentFilter();
                mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                mIntentFilter.addAction(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
                /*Extra Filter Started */
                mIntentFilter.addAction(ConnectivityManager.EXTRA_IS_FAILOVER);
                mIntentFilter.addAction(ConnectivityManager.EXTRA_REASON);
                mIntentFilter.addAction(ConnectivityManager.EXTRA_EXTRA_INFO);
                /*Extra Filter Ended */
//                mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//                mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

                this.mNetWorkReceiver = new NetworkChangeReceiver();
                this.registerReceiver(this.mNetWorkReceiver, mIntentFilter);
            } catch (Exception e) {
                Logger.e("NetWorkDemo", "Network connectivity register error occurred");
            }
        }
    }

    private void unregisterNetWorkReceiver() {

        if (mNetWorkReceiver != null)
            try {
                Logger.e("NetWorkDemo", "Network connectivity unregistered");
                this.unregisterReceiver(mNetWorkReceiver);
                this.mNetWorkReceiver = null;
            } catch (Exception e) {
                Logger.e("NetWorkDemo", "Network connectivity register error occurred");
                e.printStackTrace();
            }

    }


    public void setScreenOrientation() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Utils.runGC();
                // new activity created; force its orientation to portrait
                try {
                    activity.setRequestedOrientation(
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } catch (Exception e) {

                }
                activity.setTitle(getResources().getString(R.string.app_name));
                setCurrentAct(activity);
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                Log.d("ActOnTop", ":Class:" + activity.getClass().getSimpleName() + "::" + activity.isTaskRoot() + "::LogIn::" + generalFun.isUserLoggedIn());

                if (!(activity instanceof LauncherActivity) && !(activity instanceof MaintenanceActivity) && !(activity instanceof AccountverificationActivity) && generalFun.isUserLoggedIn() && activity.isTaskRoot()) {
                    configPuSubInstance();
                    checkForOverlay(activity);
                }

                /*if (activity instanceof MainActivity || activity instanceof UberXActivity || activity instanceof CarWashBookingDetailsActivity || (activity instanceof FoodDeliveryHomeActivity) || activity instanceof CommonDeliveryTypeSelectionActivity || activity instanceof ServiceHomeActivity) {
                    //Reset PubNub instance
                    configPuSubInstance();
                }*/

//                if (activity instanceof RatingActivity) {
//                    terminatePuSubInstance();
//                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Utils.runGC();
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Utils.runGC();
                setCurrentAct(activity);

                Log.d("CheckAppBackGround", "::" + isAppInBackground);
                isAppInBackground = false;

                LocalNotification.clearAllNotifications();

                if (currentAct instanceof MainActivity || currentAct instanceof UberXActivity || currentAct instanceof UberXHomeActivity) {
                    ViewGroup viewGroup = (ViewGroup) currentAct.findViewById(android.R.id.content);
                    new Handler().postDelayed(() -> {
                        OpenNoLocationView.getInstance(currentAct, viewGroup).configView(false);
                    }, 1000);
                }

                if (generalFun.isUserLoggedIn()){

                    JSONObject userProfileJsonObj = generalFun.getJsonObject(generalFun.retrieveValue(Utils.USER_PROFILE_JSON));

                    if (!requestPermissions.contains(android.Manifest.permission.ACCESS_FINE_LOCATION))
                        requestPermissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                    if (!requestPermissions.contains(android.Manifest.permission.ACCESS_COARSE_LOCATION))
                        requestPermissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //    if (!requestPermissions.contains(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                         //   requestPermissions.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                    }

                    String Packagetype = generalFun.getJsonValueStr("PACKAGE_TYPE", userProfileJsonObj);

                    if (!Packagetype.equalsIgnoreCase("STANDARD")){
                        if (!requestPermissions.contains(android.Manifest.permission.RECORD_AUDIO))
                            requestPermissions.add(android.Manifest.permission.RECORD_AUDIO);
                        if (!requestPermissions.contains(android.Manifest.permission.READ_PHONE_STATE))
                            requestPermissions.add(android.Manifest.permission.READ_PHONE_STATE);
                    }
                    if(!generalFun.isAllPermissionGranted(false,requestPermissions))
                    {
                        if (activity instanceof LauncherActivity){

                        }else {
                            new StartActProcess(activity).startAct(LauncherActivity.class);
                            activity.finish();

                        }

                    }
                }

            }

            @Override
            public void onActivityPaused(Activity activity) {
                Utils.runGC();
                isAppInBackground = true;
                Log.d("CheckAppBackGround", "::" + isAppInBackground);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Utils.runGC();
            }


            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                /*Called to retrieve per-instance state from an activity before being killed so that the state can be restored in onCreate(Bundle) or onRestoreInstanceState(Bundle) (the Bundle populated by this method will be passed to both).*/
                //  removeAllRunningInstances();
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Utils.runGC();
                Utils.hideKeyboard(activity);
                if (activity instanceof UberXActivity && uberXAct == activity) {
                    uberXAct = null;
                }

                if (activity instanceof UberXHomeActivity && uberXHomeAct == activity) {
                    uberXHomeAct = null;
                }

                if (activity instanceof CommonDeliveryTypeSelectionActivity && commonDeliveryAct == activity) {
                    commonDeliveryAct = null;
                }

                if (activity instanceof TrackOrderActivity && trackOrderActivity == activity) {
                    trackOrderActivity = null;
                }
                if (currentAct instanceof CarWashBookingDetailsActivity && currentAct == activity) {
                    carWashAct = null;
                }

                if (activity instanceof MainActivity && mainAct == activity) {
                    Logger.d("CheckMainAct", ":: onActivityDestroyed");
                    mainAct = null;
                }

                if (activity instanceof OnGoingTripDetailsActivity && onGoingTripDetailsAct == activity) {
                    onGoingTripDetailsAct = null;
                }

//                if (activity.isTaskRoot()) {
//                    terminatePuSubInstance();
//                }

                if ((activity instanceof TrackOrderActivity && trackOrderActivity == activity) || (activity instanceof UberXActivity && uberXAct == activity) || (uberXAct == null && commonDeliveryAct == null && activity instanceof MainActivity && mainAct == activity) || (activity instanceof CommonDeliveryTypeSelectionActivity && commonDeliveryAct == activity)) {
                    terminatePuSubInstance();
                }
            }
        });
    }

    private void connectReceiver(boolean isConnect) {
        if (isConnect && mNetWorkReceiver == null) {
            registerNetWorkReceiver();
        } else if (!isConnect && mNetWorkReceiver != null) {
            unregisterNetWorkReceiver();
        }
    }


    public Activity getCurrentAct() {
        return currentAct;
    }

    private void setCurrentAct(Activity currentAct) {
        this.currentAct = currentAct;

        if (currentAct instanceof LauncherActivity) {
            mainAct = null;
            uberXAct = null;
            uberXHomeAct = null;
            onGoingTripDetailsAct = null;
        }

        if (currentAct instanceof MainActivity) {
            mainAct = (MainActivity) currentAct;
        }

        if (currentAct instanceof CarWashBookingDetailsActivity) {
            carWashAct = (CarWashBookingDetailsActivity) currentAct;
        }

        if (currentAct instanceof TrackOrderActivity) {
            trackOrderActivity = (TrackOrderActivity) currentAct;
        }

        if (currentAct instanceof UberXActivity) {
            uberXAct = (UberXActivity) currentAct;
            mainAct = null;
        }
        if (currentAct instanceof UberXHomeActivity) {
            uberXHomeAct = (UberXHomeActivity) currentAct;
            mainAct = null;
        }

        if (currentAct instanceof OnGoingTripDetailsActivity) {
            onGoingTripDetailsAct = (OnGoingTripDetailsActivity) currentAct;
        }

        connectReceiver(true);
    }

    private void configPuSubInstance() {
        Log.d("TerminatingCalled", ":Building:");
        ConfigPubNub.getInstance(true).buildPubSub();
    }

    private void terminatePuSubInstance() {
        if (ConfigPubNub.retrieveInstance() != null) {
            Log.d("TerminatingCalled", ":1:");

            ConfigPubNub.getInstance().releasePubSubInstance();
        }
    }

    private void removeVoIpSettings() {
        try {
            if (MyApp.getInstance().getCurrentAct() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) MyApp.getInstance().getCurrentAct();
                if (new AppFunctions(mainActivity.getActContext()).checkSinchInstance(mainActivity != null ? mainActivity.getSinchServiceInterface() : null)) {
                    mainActivity.getSinchServiceInterface().getSinchClient().setSupportPushNotifications(false);
                    mainActivity.getSinchServiceInterface().getSinchClient().setSupportManagedPush(false);
                    mainActivity.getSinchServiceInterface().getSinchClient().unregisterManagedPush();
                    mainActivity.getSinchServiceInterface().getSinchClient().unregisterPushNotificationData();
                }
            }
            if (MyApp.getInstance().getCurrentAct() instanceof UberXActivity) {
                UberXActivity uberXActivity = (UberXActivity) MyApp.getInstance().getCurrentAct();
                if (new AppFunctions(uberXActivity.getActContext()).checkSinchInstance(uberXActivity != null ? uberXActivity.getSinchServiceInterface() : null)) {
                    uberXActivity.getSinchServiceInterface().getSinchClient().setSupportPushNotifications(false);
                    uberXActivity.getSinchServiceInterface().getSinchClient().setSupportManagedPush(false);
                    uberXActivity.getSinchServiceInterface().getSinchClient().unregisterManagedPush();
                    uberXActivity.getSinchServiceInterface().getSinchClient().unregisterPushNotificationData();
                }
            }

            if (MyApp.getInstance().getCurrentAct() instanceof UberXHomeActivity) {
                UberXHomeActivity uberXActivity = (UberXHomeActivity) MyApp.getInstance().getCurrentAct();
                if (new AppFunctions(uberXActivity.getActContext()).checkSinchInstance(uberXActivity != null ? uberXActivity.getSinchServiceInterface() : null)) {
                    uberXActivity.getSinchServiceInterface().getSinchClient().setSupportPushNotifications(false);
                    uberXActivity.getSinchServiceInterface().getSinchClient().setSupportManagedPush(false);
                    uberXActivity.getSinchServiceInterface().getSinchClient().unregisterManagedPush();
                    uberXActivity.getSinchServiceInterface().getSinchClient().unregisterPushNotificationData();
                }
            }
        } catch (Exception e) {

        }
    }

    public void restartWithGetDataApp() {
        GetUserData objRefresh = new GetUserData(generalFun, MyApp.getInstance().getCurrentAct());
        objRefresh.getData();
    }

    public void restartWithGetDataApp(String tripId) {
        GetUserData objRefresh = new GetUserData(generalFun, MyApp.getInstance().getCurrentAct(), tripId);
        objRefresh.getData();
    }

    public void notifySessionTimeOut() {
        if (generateSessionAlert != null) {
            return;
        }


        generateSessionAlert = new GenerateAlertBox(MyApp.getInstance().getCurrentAct());


        generateSessionAlert.setContentMessage(generalFun.retrieveLangLBl("", "LBL_BTN_TRIP_CANCEL_CONFIRM_TXT"),
                generalFun.retrieveLangLBl("Your session is expired. Please login again.", "LBL_SESSION_TIME_OUT"));
        generateSessionAlert.setPositiveBtn(generalFun.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
        generateSessionAlert.setCancelable(false);
        generateSessionAlert.setBtnClickList(btn_id -> {

            if (btn_id == 1) {
                onTerminate();
                generalFun.logOutUser(MyApp.this);
                try {
                    generateSessionAlert = null;
                } catch (Exception e) {

                }
                (MyApp.getInstance().getGeneralFun(getCurrentAct())).restartApp();
            }
        });

        generateSessionAlert.showSessionOutAlertBox();
    }

    public void logOutFromDevice(boolean isForceLogout) {

        if (generalFun != null) {
            final HashMap<String, String> parameters = new HashMap<String, String>();

            parameters.put("type", "callOnLogout");
            parameters.put("iMemberId", generalFun.getMemberId());
            parameters.put("UserType", Utils.userType);

            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getCurrentAct(), parameters);
            exeWebServer.setLoaderConfig(getCurrentAct(), true, generalFun);

            exeWebServer.setDataResponseListener(responseString -> {

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail == true) {

                        if (getCurrentAct() instanceof MainActivity) {
                            ((MainActivity) getCurrentAct()).releaseScheduleNotificationTask();
                        }

                        onTerminate();
                        generalFun.logOutUser(MyApp.this);

                        (new GeneralFunctions(getCurrentAct())).restartApp();

                    } else {
                        if (isForceLogout) {
                            generalFun.showGeneralMessage("",
                                    generalFun.retrieveLangLBl("", generalFun.getJsonValue(Utils.message_str, responseString)), buttonId -> (MyApp.getInstance().getGeneralFun(getCurrentAct())).restartApp());
                        } else {
                            generalFun.showGeneralMessage("",
                                    generalFun.retrieveLangLBl("", generalFun.getJsonValue(Utils.message_str, responseString)));
                        }
                    }
                } else {
                    if (isForceLogout) {
                        generalFun.showError(buttonId -> (MyApp.getInstance().getGeneralFun(getCurrentAct())).restartApp());
                    } else {
                        generalFun.showError();
                    }
                }
            });
            exeWebServer.execute();
        }
    }

    private void checkForOverlay(Activity act) {
        if (!generalFun.canDrawOverlayViews(act)) {
            if (drawOverlayAppAlert != null) {
                drawOverlayAppAlert.closeAlertBox();
                drawOverlayAppAlert = null;
            }

            GenerateAlertBox alertBox = new GenerateAlertBox(getCurrentAct(), false);
            drawOverlayAppAlert = alertBox;
            alertBox.setContentMessage(null, generalFun.retrieveLangLBl("Please enable draw over app permission.", "LBL_ENABLE_DRWA_OVER_APP"));
            alertBox.setPositiveBtn(generalFun.retrieveLangLBl("Allow", "LBL_ALLOW"));
            alertBox.setNegativeBtn(generalFun.retrieveLangLBl("Retry", "LBL_RETRY_TXT"));
            alertBox.setCancelable(false);
            alertBox.setBtnClickList(btn_id -> {
                if (btn_id == 1) {
                    (new StartActProcess(act)).requestOverlayPermission(Utils.OVERLAY_PERMISSION_REQ_CODE);
                } else {
                    checkForOverlay(act);
                }

            });
            alertBox.showAlertBox();
        }
    }

    /*public void handleBtnClick(int buttonId, String alertType) {
        Utils.hideKeyboard(getCurrentAct());
        if (buttonId == 0) {
            if (alertType.equals("NO_PERMISSION")) {
                getCurrentAct().finish();
            }
        }
    }*/


}
