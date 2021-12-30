package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import androidx.core.app.ActivityCompat;

import com.melevicarbrasil.usuario.AppLoginActivity;
import com.melevicarbrasil.usuario.BuildConfig;
import com.utils.Utils;
import com.view.GenerateAlertBox;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Admin on 19-06-2017.
 */

public class GetUserData {

    GeneralFunctions generalFunc;
    Context mContext;
    /*Multi*/
    String tripId = "";
    boolean releaseCurrActInstance = true;

    public GetUserData(GeneralFunctions generalFunc, Context mContext) {
        this.generalFunc = generalFunc;
        this.mContext = mContext;
        this.releaseCurrActInstance = true;
    }

    /*Track Particular Trip Data*/
    public GetUserData(GeneralFunctions generalFunc, Context mContext, String tripID) {
        this.generalFunc = generalFunc;
        this.mContext = mContext;
        this.tripId = tripID;

        if (Utils.checkText(tripID)) {
            this.releaseCurrActInstance = false;
        }
    }


    public void getData() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDetail");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        if (Utils.checkText(tripId)) {
            generalFunc.storeData(Utils.isMultiTrackRunning, "Yes");
            parameters.put("LiveTripId", tripId);
        } else {
            generalFunc.storeData(Utils.isMultiTrackRunning, "No");
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        exeWebServer.setLoaderConfig(mContext, true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken", generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {


                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                String message = generalFunc.getJsonValue(Utils.message_str, responseString);

                if (Utils.checkText(responseString) && message.equals("SESSION_OUT")) {
                    if (ConfigPubNub.retrieveInstance() != null) {
                        ConfigPubNub.getInstance().releasePubSubInstance();
                    }
                    MyApp.getInstance().notifySessionTimeOut();
                    Utils.runGC();
                    return;
                }

                if (isDataAvail == true) {

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                    new OpenMainProfile(mContext,
                            generalFunc.getJsonValue(Utils.message_str, responseString), true, generalFunc, tripId).startProcess();

                    if (releaseCurrActInstance) {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            try {

                                ActivityCompat.finishAffinity((Activity) mContext);

                            } catch (Exception e) {
                            }
                            Utils.runGC();
                        }, 300);

                    }


                } else {
                    if (!generalFunc.getJsonValue("isAppUpdate", responseString).trim().equals("")
                            && generalFunc.getJsonValue("isAppUpdate", responseString).equals("true")) {

                    } else {

                        if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_CONTACT_US_STATUS_NOTACTIVE_COMPANY") ||
                                generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_ACC_DELETE_TXT") ||
                                generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_CONTACT_US_STATUS_NOTACTIVE_DRIVER")) {

                            GenerateAlertBox alertBox = generalFunc.notifyRestartApp("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                            alertBox.setCancelable(false);
                            alertBox.setBtnClickList(btn_id -> {

                                if (btn_id == 1) {
                                    MyApp.getInstance().logOutFromDevice(true);
                                }
                            });
                            return;
                        }
                    }

                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_TRY_AGAIN_TXT"), "", generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"), buttonId -> generalFunc.restartApp());
                }
            } else {

                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_TRY_AGAIN_TXT"), "", generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"), buttonId -> generalFunc.restartApp());
            }
        });
        exeWebServer.execute();
    }


    public void  GetConfigData()
    {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "generalConfigData");
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            JSONObject responseObj=generalFunc.getJsonObject(responseString);


            if (responseObj != null && !responseObj.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);
                if (isDataAvail) {


                    new SetGeneralData(generalFunc,responseObj);

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        try {
                            new StartActProcess(mContext).startAct(AppLoginActivity.class);
                            ActivityCompat.finishAffinity((Activity) mContext);

                        } catch (Exception e) {
                        }
                        Utils.runGC();
                    }, 300);


                }
            } else {
                //  showError();
            }

        });
        exeWebServer.execute();
    }

}
