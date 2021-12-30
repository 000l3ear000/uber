package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.melevicarbrasil.usuario.BuildConfig;
import com.rest.RestClient;
import com.utils.CommonUtilities;
import com.utils.DeviceData;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MyProgressDialog;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 22-02-2016.
 */
public class ExecuteWebServerUrl {

    public static String CUSTOM_APP_TYPE = "";
    public static String CUSTOM_UBERX_PARENT_CAT_ID = "";
    public static String DELIVERALL = "";
    public static String ONLYDELIVERALL = "";
    public static String MAPS_API_REPLACEMENT_STRATEGY = "None";
    public static String FOODONLY = "";
    SetDataResponse setDataRes;

    HashMap<String, String> parameters;

    GeneralFunctions generalFunc;

    String responseString = "";

    boolean directUrl_value = false;
    String directUrl = "";

    boolean isLoaderShown = false;
    Context mContext;

    MyProgressDialog myPDialog;

    boolean isGenerateDeviceToken = false;
    String key_DeviceToken_param;
    InternetConnection intCheck;
    boolean isSetCancelable = true;

    boolean isTaskKilled = false;

    Call<Object> currentCall;
    Call<ResponseBody> currentCall1;

    /*Multi*/
    HashMap<String, Object> parametersObj;
    boolean isObjectTypeParam = false;
    boolean isStringResponse = false;


    public ExecuteWebServerUrl(Context mContext, HashMap<String, String> parameters) {
        this.parameters = parameters;
        this.mContext = mContext;
    }

    public ExecuteWebServerUrl(Context mContext, HashMap<String, Object> parametersObj, boolean isObjectTypeParam) {
        this.parametersObj = parametersObj;
        this.mContext = mContext;
        this.isObjectTypeParam = isObjectTypeParam;
    }

    public ExecuteWebServerUrl(Context mContext, boolean isStringResponse, HashMap<String, String> parameters) {
        this.parameters = parameters;
        this.mContext = mContext;
        this.isStringResponse = isStringResponse;
    }

    public ExecuteWebServerUrl(Context mContext, String directUrl, boolean directUrl_value) {
        this.directUrl = directUrl;
        this.directUrl_value = directUrl_value;
        this.mContext = mContext;

        if (directUrl.startsWith("https://maps.googleapis.com") && MAPS_API_REPLACEMENT_STRATEGY.equalsIgnoreCase("Normal")) {
            isObjectTypeParam = false;
            parameters = new HashMap<String, String>();
            parameters.put("type", "fetchAPIDetails");
            parameters.put("API_URL", directUrl);
        }
    }

    public void setLoaderConfig(Context mContext, boolean isLoaderShown, GeneralFunctions generalFunc) {
        this.isLoaderShown = isLoaderShown;
        this.generalFunc = generalFunc;
        this.mContext = mContext;
    }

    public void setIsDeviceTokenGenerate(boolean isGenerateDeviceToken, String key_DeviceToken_param, GeneralFunctions generalFunc) {
        this.isGenerateDeviceToken = isGenerateDeviceToken;
        this.key_DeviceToken_param = key_DeviceToken_param;
        this.generalFunc = generalFunc;
    }

    public void setCancelAble(boolean isSetCancelable) {
        this.isSetCancelable = isSetCancelable;
    }

    public void execute() {
        Utils.runGC();
        intCheck = new InternetConnection(mContext);

        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
            fireResponse();
            return;
        }

        if (isLoaderShown == true) {
            myPDialog = new MyProgressDialog(mContext, isSetCancelable, generalFunc.retrieveLangLBl("Loading", "LBL_LOADING_TXT"));
            //  isSetCancelable = true;
            try {
                myPDialog.show();
            } catch (Exception e) {

            }
        }

        if (parametersObj != null) {
            GeneralFunctions generalFunc = MyApp.getInstance().getAppLevelGeneralFunc();
            parametersObj.put("tSessionId", generalFunc.getMemberId().equals("") ? "" : generalFunc.retrieveValue(Utils.SESSION_ID_KEY));
            parametersObj.put("deviceHeight", Utils.getScreenPixelHeight(mContext) + "");
            parametersObj.put("deviceWidth", Utils.getScreenPixelWidth(mContext) + "");
            parametersObj.put("GeneralUserType", Utils.app_type);
            parametersObj.put("GeneralMemberId", generalFunc.getMemberId());
            parametersObj.put("GeneralDeviceType", "" + Utils.deviceType);
            parametersObj.put("GeneralAppVersion", BuildConfig.VERSION_NAME);
            parametersObj.put("GeneralAppVersionCode", "" + BuildConfig.VERSION_CODE);
            parametersObj.put("vTimeZone", generalFunc.getTimezone());
            parametersObj.put("vUserDeviceCountry", Utils.getUserDeviceCountryCode(mContext));
            parametersObj.put("vCurrentTime", generalFunc.getCurrentDateHourMin());
            parametersObj.put("APP_TYPE", CUSTOM_APP_TYPE);
            parametersObj.put("UBERX_PARENT_CAT_ID", CUSTOM_UBERX_PARENT_CAT_ID);
            parametersObj.put("DELIVERALL", DELIVERALL);
            parametersObj.put("ONLYDELIVERALL", ONLYDELIVERALL);
            parametersObj.put("FOOD_ONLY", FOODONLY);
            parametersObj.put("vGeneralLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
            parametersObj.put("vGeneralCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));
            /*Deliver all related keys added */
            parametersObj.put("iServiceId", generalFunc.getServiceId());
            parametersObj.put("DEFAULT_SERVICE_CATEGORY_ID", generalFunc.retrieveValue("DEFAULT_SERVICE_CATEGORY_ID"));

            try {
                if (parametersObj.get("type") != null && (parametersObj.get("type").toString().equalsIgnoreCase("getDetail") || parametersObj.get("type").toString().equalsIgnoreCase("signIn") || parametersObj.get("parametersObj").toString().equalsIgnoreCase("signup") || parametersObj.get("type").toString().equalsIgnoreCase("LoginWithFB"))) {
                    parametersObj.put("DEVICE_DATA", DeviceData.getDeviceData());
                }
            } catch (Exception e) {

            }

            try {
                if (parametersObj.get("type") != null && parametersObj.get("type").toString().equalsIgnoreCase("generalConfigData")) {
                    parametersObj.putAll(GetFeatureClassList.getAllGeneralClasses());
                }
            } catch (Exception e) {

            }
        } else if (parameters != null) {
            GeneralFunctions generalFunc = MyApp.getInstance().getAppLevelGeneralFunc();
            parameters.put("tSessionId", generalFunc.getMemberId().equals("") ? "" : generalFunc.retrieveValue(Utils.SESSION_ID_KEY));
            parameters.put("deviceHeight", Utils.getScreenPixelHeight(mContext) + "");
            parameters.put("deviceWidth", Utils.getScreenPixelWidth(mContext) + "");
            parameters.put("GeneralUserType", Utils.app_type);
            parameters.put("GeneralMemberId", generalFunc.getMemberId());
            parameters.put("GeneralDeviceType", "" + Utils.deviceType);
            parameters.put("GeneralAppVersion", BuildConfig.VERSION_NAME);
            parameters.put("GeneralAppVersionCode", "" + BuildConfig.VERSION_CODE);
            parameters.put("vTimeZone", generalFunc.getTimezone());
            parameters.put("vUserDeviceCountry", Utils.getUserDeviceCountryCode(mContext));
            parameters.put("vCurrentTime", generalFunc.getCurrentDateHourMin());
            parameters.put("APP_TYPE", CUSTOM_APP_TYPE);
            parameters.put("UBERX_PARENT_CAT_ID", CUSTOM_UBERX_PARENT_CAT_ID);
            parameters.put("DELIVERALL", DELIVERALL);
            parameters.put("ONLYDELIVERALL", ONLYDELIVERALL);
            parameters.put("FOOD_ONLY", FOODONLY);
            parameters.put("vGeneralLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
            parameters.put("vGeneralCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));

            /*Deliver all related keys added */
            parameters.put("iServiceId", generalFunc.getServiceId());
            parameters.put("DEFAULT_SERVICE_CATEGORY_ID", generalFunc.retrieveValue("DEFAULT_SERVICE_CATEGORY_ID"));

            try {
                if (parameters.get("type") != null && (parameters.get("type").equalsIgnoreCase("getDetail") || parameters.get("type").equalsIgnoreCase("signIn") || parameters.get("type").equalsIgnoreCase("signup") || parameters.get("type").equalsIgnoreCase("LoginWithFB"))) {
                    parameters.put("DEVICE_DATA", DeviceData.getDeviceData());
                }
            } catch (Exception e) {

            }

            try {
                if (parameters.get("type") != null && parameters.get("type").equalsIgnoreCase("generalConfigData")) {
                    parameters.putAll(GetFeatureClassList.getAllGeneralClasses());
                }
            } catch (Exception e) {

            }
        }

        if (generalFunc != null) {
            GetDeviceToken getDeviceToken = new GetDeviceToken(generalFunc);

            getDeviceToken.setDataResponseListener(vDeviceToken -> {

                if (isGenerateDeviceToken) {
                    if (!vDeviceToken.equals("")) {

                        if (parameters != null) {
                            parameters.put(key_DeviceToken_param, "" + vDeviceToken);
                            parameters.put("vFirebaseDeviceToken", vDeviceToken);
                        }
                        performPostCall();
                    } else {
                        responseString = "";
                        fireResponse();
                    }
                } else {

                    if (parameters != null) {
                        parameters.put("vFirebaseDeviceToken", vDeviceToken);
                    }
                    performPostCall();

                }

            });
            getDeviceToken.execute();

        } else {
            performPostCall();
        }
    }

    public void performGetCall(String directUrl) {
//        Call<Object> call = RestClient.getClient("GET", CommonUtilities.SERVER).getResponse(directUrl);

            Call<Object> call = RestClient.getClient("GET", CommonUtilities.SERVER, mContext).getResponse(directUrl);
            Logger.d("Url", "::" + directUrl);
            currentCall = call;
            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        responseString = RestClient.getGSONBuilder().toJson(response.body());
                        fireResponse();
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                responseString = RestClient.getGSONBuilder().toJson(response.errorBody().string());
                            } catch (Exception e) {
                                responseString = "";
                            }
                        } else {
                            responseString = "";
                        }
                        fireResponse();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Logger.d("DataError", "::" + t.getMessage());
                    responseString = "";
                    fireResponse();
                }
            });

    }

    public void performPostCall() {


        if (directUrl_value && (!directUrl.startsWith("https://maps.googleapis.com") || (directUrl.startsWith("https://maps.googleapis.com") && !MAPS_API_REPLACEMENT_STRATEGY.equalsIgnoreCase("Normal")))) {
            performGetCall(directUrl);
            return;
        }


        if (isStringResponse)
        {
            Call<ResponseBody> call = null;

            Logger.d("Api", "Parameters::" + CommonUtilities.SERVER_WEBSERVICE_PATH + "?" + parameters.toString());

            if (isObjectTypeParam && parametersObj != null) {
               /* Logger.d("Api", "DATA::" + CommonUtilities.SERVER_WEBSERVICE_PATH + "?" + parametersObj.toString());

                call = RestClient.getClient("POST", CommonUtilities.SERVER, mContext, new ToStringConverterFactory()).getStringResponse(CommonUtilities.SERVER_WEBSERVICE_PATH, parametersObj);*/
            }else {
                call = RestClient.getClient("POST", CommonUtilities.SERVER, mContext, new ToStringConverterFactory()).getStringResponse(CommonUtilities.SERVER_WEBSERVICE_PATH, parameters);

            }

            currentCall1 = call;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            responseString = response.body().string();
                            Logger.d("Api", "Response ::" + responseString);
                            fireResponse();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (response.errorBody() != null) {
                            try {
                                responseString = response.body().string();
                            } catch (Exception e) {
                                responseString = "";
                            }
                        } else {
                            responseString = "";
                        }
                        fireResponse();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Logger.d("DataError", "::" + t.getMessage());
                    responseString = "";
                    fireResponse();
                }
            });
        }

        else {

            Call<Object> call;

            if (isObjectTypeParam && parametersObj != null) {
                Logger.d("Api", "DATA::" + CommonUtilities.SERVER_WEBSERVICE_PATH + "?" + parametersObj.toString());

                call = RestClient.getClient("POST", CommonUtilities.SERVER).getResponseObj(CommonUtilities.SERVER_WEBSERVICE_PATH, parametersObj);
            } else {
                Logger.d("Parameters", "::" + parameters.toString());

                call = RestClient.getClient("POST", CommonUtilities.SERVER).getResponse(CommonUtilities.SERVER_WEBSERVICE_PATH, parameters);
            }

            currentCall = call;
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        responseString = RestClient.getGSONBuilder().toJson(response.body());
                        fireResponse();
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                responseString = RestClient.getGSONBuilder().toJson(response.errorBody().string());
                            } catch (Exception e) {
                                responseString = "";
                            }
                        } else {
                            responseString = "";
                        }
                        fireResponse();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Logger.d("DataError", "::" + t.getMessage());
                    responseString = "";
                    fireResponse();
                }
            });
        }
    }

    public void fireResponse() {
        if (myPDialog != null) {
            myPDialog.close();
        }

        if (setDataRes != null && !isTaskKilled) {
            GeneralFunctions generalFunc = MyApp.getInstance().getAppLevelGeneralFunc();
            String message = Utils.checkText(responseString) ? generalFunc.getJsonValue(Utils.message_str, responseString) : null;

            if (message != null && message.equals("DO_RESTART")) {
                generalFunc.restartApp();
                Utils.runGC();
                return;
            } else {
                try {
                    if (mContext != null && mContext instanceof Activity) {
                        Activity act = (Activity) mContext;
                        if (!act.isFinishing()) {
                            if (message != null && message.equals("SESSION_OUT")) {
                                MyApp.getInstance().notifySessionTimeOut();
                                Utils.runGC();
                                return;
                            }

                        }
                    }
                } catch (Exception e) {

                }

                if (directUrl.startsWith("https://maps.googleapis.com") && MAPS_API_REPLACEMENT_STRATEGY.equalsIgnoreCase("Normal")) {

                    responseString = generalFunc.getJsonValue("DATA_RESULT", responseString);
                }

                setDataRes.setResponse(responseString);
            }
        }
    }

    public void cancel(boolean value) {

        this.isTaskKilled = value;
        if (currentCall != null) {

            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {
                    currentCall.cancel();
                    return "";
                }
            }.execute();
        }else if (currentCall1 != null) {

            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {
                    currentCall1.cancel();
                    return "";
                }
            }.execute();
        }
    }


    public void setDataResponseListener(SetDataResponse setDataRes) {
        this.setDataRes = setDataRes;
    }

    public interface SetDataResponse {
        void setResponse(String responseString);
    }


}
