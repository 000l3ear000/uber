package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.melevicarbrasil.usuario.AccountverificationActivity;
import com.melevicarbrasil.usuario.AppLoignRegisterActivity;
import com.melevicarbrasil.usuario.ContactUsActivity;
import com.melevicarbrasil.usuario.deliverAll.LoginActivity;
import com.melevicarbrasil.usuario.deliverAll.SignUpActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 29-06-2016.
 */
public class RegisterFbLoginResCallBack implements FacebookCallback<LoginResult> {
    private final InternetConnection intCheck;
    Context mContext;
    GeneralFunctions generalFunc;
    MyProgressDialog myPDialog;
    AppLoignRegisterActivity appMainLoginAct;
    LoginActivity appLoginAct;
    SignUpActivity appSignUpAct;
    private CallbackManager callbackManager;
    public boolean isrestart;

    public RegisterFbLoginResCallBack(Context mContext, CallbackManager callbackManager) {
        this.mContext = mContext;

        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        this.callbackManager = callbackManager;
        appMainLoginAct = (AppLoignRegisterActivity) mContext;
        intCheck = new InternetConnection(mContext);
    }

    public RegisterFbLoginResCallBack(Context mContext, CallbackManager callbackManager, boolean isrestart) {
        this.mContext = mContext;
        this.isrestart = isrestart;

        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        this.callbackManager = callbackManager;
        if (mContext instanceof LoginActivity) {
            appLoginAct = (LoginActivity) mContext;
        } else if (mContext instanceof SignUpActivity) {
            appSignUpAct = (SignUpActivity) mContext;
        }
        intCheck = new InternetConnection(mContext);
    }


    @Override
    public void onSuccess(LoginResult loginResult) {

        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
            closeDialog();
            return;
        }

        myPDialog = new MyProgressDialog(mContext, false, generalFunc.retrieveLangLBl("", "LBL_LOADING_TXT"));
        myPDialog.show();

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                (me, response) -> {
                    // Application code
                    myPDialog.close();
                    if (response.getError() != null) {
                        // handle error
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_ERROR"), generalFunc.retrieveLangLBl("", "LBL_TRY_AGAIN"));
                    } else {
                        try {

                            String email_str = generalFunc.getJsonValue("email", me.toString());
                            String name_str = generalFunc.getJsonValue("name", me.toString());
                            String first_name_str = generalFunc.getJsonValue("first_name", me.toString());
                            String last_name_str = generalFunc.getJsonValue("last_name", me.toString());
                            String fb_id_str = generalFunc.getJsonValue("id", me.toString());

                            // URL imageURL = "https://graph.facebook.com/" + fb_id_str + "/picture?type=large";
                            URL imageURL = new URL("https://graph.facebook.com/" + fb_id_str + "/picture?type=large");

                            registerFbUser(email_str, first_name_str, last_name_str, fb_id_str, imageURL + "");

                            generalFunc.logOUTFrmFB();
                        } catch (Exception e) {

                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();

        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
            closeDialog();
        }

    }

    @Override
    public void onCancel() {
        closeDialog();
    }

    @Override
    public void onError(FacebookException error) {
        closeDialog();
    }


    public void registerFbUser(final String email, final String fName, final String lName, final String fbId, final String imageURL) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "LoginWithFB");
        parameters.put("vFirstName", fName);
        parameters.put("vLastName", lName);
        parameters.put("vEmail", email);
        parameters.put("iFBId", fbId);
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("UserType", Utils.userType);
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("vImageURL", imageURL);
        parameters.put("eLoginType", "Facebook");

        //   parameters.put("eSystem", Utils.eSystem_Type);
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        exeWebServer.setLoaderConfig(mContext, true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken", generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    new SetUserData(responseString, generalFunc, mContext, true);
                    if (appMainLoginAct != null) {
                        appMainLoginAct.manageSinchClient();
                    }

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));

                    if (appMainLoginAct == null) {
                        if (isrestart) {
                            new OpenMainProfile(mContext,
                                    generalFunc.getJsonValue(Utils.message_str, responseString), false, generalFunc).startProcess();
                        } else {
                            //((Activity)mContext).finish();
                            String userProfileJsonObj = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                            setGeneralData(userProfileJsonObj);

                            if (generalFunc.getJsonValue("vPhone", userProfileJsonObj).equals("") || generalFunc.getJsonValue("vEmail", userProfileJsonObj).equals("")) {
                                //open account verification screen
                                if (generalFunc.getMemberId() != null && !generalFunc.getMemberId().equals("")) {
                                    if (!generalFunc.getMemberId().equals("")) {
                                        Bundle bn = new Bundle();
                                        bn.putBoolean("isRestart", isrestart);
                                        new StartActProcess(mContext).startActForResult(AccountverificationActivity.class, bn, Utils.SOCIAL_LOGIN_REQ_CODE);
                                    } else {
                                        generalFunc.restartApp();
                                    }
                                }
                            } else {
                                Intent returnIntent = new Intent();
                                ((Activity) mContext).setResult(Activity.RESULT_OK, returnIntent);
                                ((Activity) mContext).finish();
                            }

                        }
                    } else {
                        new OpenMainProfile(mContext,
                                generalFunc.getJsonValue(Utils.message_str, responseString), false, generalFunc).startProcess();
                    }
                } else {

                    if (generalFunc.getJsonValue("eStatus", responseString).equalsIgnoreCase("Deleted")) {
                        openContactUsDialog(responseString);
                    } else if (generalFunc.getJsonValue("eStatus", responseString).equalsIgnoreCase("Inactive")) {
                        openContactUsDialog(responseString);
                    } else {
                        if (!generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_REGISTER")) {
                            generalFunc.showGeneralMessage("",
                                    generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        } else {

                            signupUser(email, fName, lName, fbId, imageURL);

                        }
                    }


                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void openContactUsDialog(String responseString) {
        GenerateAlertBox alertBox = new GenerateAlertBox(mContext);
        alertBox.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
        alertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
        alertBox.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));
        alertBox.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
            @Override
            public void handleBtnClick(int btn_id) {

                alertBox.closeAlertBox();
                if (btn_id == 0) {
                    new StartActProcess(mContext).startAct(ContactUsActivity.class);
                }
            }
        });
        alertBox.showAlertBox();
    }

    public void signupUser(final String email, final String fName, final String lName, final String fbId, String imageURL) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "signup");
        parameters.put("vFirstName", fName);
        parameters.put("vLastName", lName);
        parameters.put("vEmail", email);
        parameters.put("vFbId", fbId);
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("UserType", Utils.userType);
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("eSignUpType", "Facebook");
        parameters.put("vImageURL", imageURL);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        exeWebServer.setLoaderConfig(mContext, true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken", generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    new SetUserData(responseString, generalFunc, mContext, true);
                    if (appMainLoginAct != null) {
                        appMainLoginAct.manageSinchClient();
                    }

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));

                    if (appMainLoginAct == null) {
                        String userProfileJsonObj = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        setGeneralData(userProfileJsonObj);
                        if (generalFunc.getJsonValue("vPhone", userProfileJsonObj).equals("") || generalFunc.getJsonValue("vEmail", userProfileJsonObj).equals("")) {
                            //open account verification screen
                            if (generalFunc.getMemberId() != null && !generalFunc.getMemberId().equals("")) {
                                if (!generalFunc.getMemberId().equals("")) {

                                    Bundle bn = new Bundle();
                                    bn.putBoolean("isRestart", isrestart);
                                    new StartActProcess(mContext).startActForResult(AccountverificationActivity.class, bn, Utils.SOCIAL_LOGIN_REQ_CODE);
                                } else {
                                    generalFunc.restartApp();
                                }
                            }
                        } else {
                            Intent returnIntent = new Intent();
                            ((Activity) mContext).setResult(Activity.RESULT_OK, returnIntent);
                            ((Activity) mContext).finish();
                        }
                    } else {
                        new OpenMainProfile(mContext,
                                generalFunc.getJsonValue(Utils.message_str, responseString), false, generalFunc).startProcess();
                    }
                } else {


                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void setGeneralData(String userProfileJsonObjData) {

        HashMap<String, String> storeData = new HashMap<>();
        ArrayList<String> removeData = new ArrayList<>();

        JSONObject userProfileJsonObj = generalFunc.getJsonObject(userProfileJsonObjData);
        new SetGeneralData(generalFunc, userProfileJsonObj);
//
//        storeData.put(Utils.PUBNUB_PUB_KEY, generalFunc.getJsonValueStr("PUBNUB_PUBLISH_KEY", userProfileJsonObj));
//        storeData.put(Utils.PUBNUB_SUB_KEY, generalFunc.getJsonValueStr("PUBNUB_SUBSCRIBE_KEY", userProfileJsonObj));
//        storeData.put(Utils.PUBNUB_SEC_KEY, generalFunc.getJsonValueStr("PUBNUB_SECRET_KEY", userProfileJsonObj));
//        storeData.put(Utils.SESSION_ID_KEY, generalFunc.getJsonValueStr("tSessionId", userProfileJsonObj));
//        storeData.put(Utils.RIDER_REQUEST_ACCEPT_TIME_KEY, generalFunc.getJsonValueStr("RIDER_REQUEST_ACCEPT_TIME", userProfileJsonObj));
//        storeData.put(Utils.DEVICE_SESSION_ID_KEY, generalFunc.getJsonValueStr("tDeviceSessionId", userProfileJsonObj));
//
//        storeData.put(Utils.FETCH_TRIP_STATUS_TIME_INTERVAL_KEY, generalFunc.getJsonValueStr("FETCH_TRIP_STATUS_TIME_INTERVAL", userProfileJsonObj));
//
//        storeData.put(Utils.APP_DESTINATION_MODE, generalFunc.getJsonValueStr("APP_DESTINATION_MODE", userProfileJsonObj));
//        storeData.put(Utils.APP_TYPE, generalFunc.getJsonValueStr("APP_TYPE", userProfileJsonObj));
//        storeData.put(Utils.SITE_TYPE_KEY, generalFunc.getJsonValueStr("SITE_TYPE", userProfileJsonObj));
//        storeData.put(Utils.ENABLE_TOLL_COST, generalFunc.getJsonValueStr("ENABLE_TOLL_COST", userProfileJsonObj));
//        storeData.put(Utils.TOLL_COST_APP_ID, generalFunc.getJsonValueStr("TOLL_COST_APP_ID", userProfileJsonObj));
//        storeData.put(Utils.TOLL_COST_APP_CODE, generalFunc.getJsonValueStr("TOLL_COST_APP_CODE", userProfileJsonObj));
//        storeData.put(Utils.HANDICAP_ACCESSIBILITY_OPTION, generalFunc.getJsonValueStr("HANDICAP_ACCESSIBILITY_OPTION", userProfileJsonObj));
//        storeData.put(Utils.CHILD_SEAT_ACCESSIBILITY_OPTION, generalFunc.getJsonValueStr("CHILD_SEAT_ACCESSIBILITY_OPTION", userProfileJsonObj));
//        storeData.put(Utils.WHEEL_CHAIR_ACCESSIBILITY_OPTION, generalFunc.getJsonValueStr("WHEEL_CHAIR_ACCESSIBILITY_OPTION", userProfileJsonObj));
//        storeData.put(Utils.FEMALE_RIDE_REQ_ENABLE, generalFunc.getJsonValueStr("FEMALE_RIDE_REQ_ENABLE", userProfileJsonObj));
//        storeData.put(Utils.PUBNUB_DISABLED_KEY, generalFunc.getJsonValueStr("PUBNUB_DISABLED", userProfileJsonObj));
//
//        storeData.put(Utils.ENABLE_SOCKET_CLUSTER_KEY, generalFunc.getJsonValueStr("ENABLE_SOCKET_CLUSTER", userProfileJsonObj));
//        storeData.put(Utils.SC_CONNECT_URL_KEY, generalFunc.getJsonValueStr("SC_CONNECT_URL", userProfileJsonObj));
//        storeData.put(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY, generalFunc.getJsonValueStr("GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY", userProfileJsonObj));
//        storeData.put("DESTINATION_UPDATE_TIME_INTERVAL", generalFunc.getJsonValueStr("DESTINATION_UPDATE_TIME_INTERVAL", userProfileJsonObj));
//        storeData.put(Utils.DELIVERALL_KEY, generalFunc.getJsonValueStr(Utils.DELIVERALL_KEY, userProfileJsonObj));
//        storeData.put(Utils.ONLYDELIVERALL_KEY, generalFunc.getJsonValueStr(Utils.ONLYDELIVERALL_KEY, userProfileJsonObj));
//
//        storeData.put(Utils.ISWALLETBALNCECHANGE, "No");
//        storeData.put("showCountryList", generalFunc.getJsonValueStr("showCountryList", userProfileJsonObj));
//
//
//        /*Multi Delivery Enabled*/
//        storeData.put(Utils.ENABLE_MULTI_DELIVERY_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_MULTI_DELIVERY_KEY, userProfileJsonObj));
//        storeData.put(Utils.ALLOW_MULTIPLE_DEST_ADD_KEY, generalFunc.getJsonValueStr(Utils.ALLOW_MULTIPLE_DEST_ADD_KEY, userProfileJsonObj));

        removeData.add("userHomeLocationLatitude");
        removeData.add("userHomeLocationLongitude");
        removeData.add("userHomeLocationAddress");
        removeData.add("userWorkLocationLatitude");
        removeData.add("userWorkLocationLongitude");
        removeData.add("userWorkLocationAddress");

        generalFunc.removeValue(removeData);

        if (generalFunc.getJsonArray("UserFavouriteAddress", userProfileJsonObjData) == null) {
            return;
        }

        JSONArray userFavouriteAddressArr = generalFunc.getJsonArray("UserFavouriteAddress", userProfileJsonObjData);
        if (userFavouriteAddressArr.length() > 0) {

            for (int i = 0; i < userFavouriteAddressArr.length(); i++) {
                JSONObject dataItem = generalFunc.getJsonObject(userFavouriteAddressArr, i);

                if (generalFunc.getJsonValueStr("eType", dataItem).equalsIgnoreCase("HOME")) {

                    storeData.put("userHomeLocationLatitude", generalFunc.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userHomeLocationLongitude", generalFunc.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userHomeLocationAddress", generalFunc.getJsonValueStr("vAddress", dataItem));

                } else if (generalFunc.getJsonValueStr("eType", dataItem).equalsIgnoreCase("WORK")) {
                    storeData.put("userWorkLocationLatitude", generalFunc.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userWorkLocationLongitude", generalFunc.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userWorkLocationAddress", generalFunc.getJsonValueStr("vAddress", dataItem));

                }

            }
        }
        generalFunc.storeData(storeData);

    }


    public void closeDialog() {
        if (myPDialog != null) {
            myPDialog.close();
        }
    }
}
