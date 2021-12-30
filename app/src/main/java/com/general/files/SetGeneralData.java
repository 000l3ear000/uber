package com.general.files;


import com.utils.Logger;
import com.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class SetGeneralData {


    public SetGeneralData(GeneralFunctions generalFunc, JSONObject responseObj) {

        HashMap<String, String> storeData = new HashMap<>();



        //open main profile missing key add for make common
        if (!generalFunc.getMemberId().equalsIgnoreCase("")) {
            storeData.put(Utils.SESSION_ID_KEY, generalFunc.getJsonValueStr("tSessionId", responseObj));
            storeData.put(Utils.DEVICE_SESSION_ID_KEY, generalFunc.getJsonValueStr("tDeviceSessionId", responseObj));
            storeData.put(Utils.RIDER_REQUEST_ACCEPT_TIME_KEY, generalFunc.getJsonValueStr("RIDER_REQUEST_ACCEPT_TIME", responseObj));

            storeData.put(Utils.SMS_BODY_KEY, generalFunc.getJsonValueStr(Utils.SMS_BODY_KEY, responseObj));
            storeData.put(Utils.FETCH_TRIP_STATUS_TIME_INTERVAL_KEY, generalFunc.getJsonValueStr("FETCH_TRIP_STATUS_TIME_INTERVAL", responseObj));
            storeData.put(Utils.VERIFICATION_CODE_RESEND_TIME_IN_SECONDS_KEY, generalFunc.getJsonValueStr(Utils.VERIFICATION_CODE_RESEND_TIME_IN_SECONDS_KEY, responseObj));
            storeData.put(Utils.VERIFICATION_CODE_RESEND_COUNT_KEY, generalFunc.getJsonValueStr(Utils.VERIFICATION_CODE_RESEND_COUNT_KEY, responseObj));
            storeData.put(Utils.VERIFICATION_CODE_RESEND_COUNT_RESTRICTION_KEY, generalFunc.getJsonValueStr(Utils.VERIFICATION_CODE_RESEND_COUNT_RESTRICTION_KEY, responseObj));
            storeData.put("DESTINATION_UPDATE_TIME_INTERVAL", generalFunc.getJsonValueStr("DESTINATION_UPDATE_TIME_INTERVAL", responseObj));
            storeData.put(Utils.PUBNUB_PUB_KEY, generalFunc.getJsonValueStr("PUBNUB_PUBLISH_KEY", responseObj));
            storeData.put(Utils.PUBNUB_SUB_KEY, generalFunc.getJsonValueStr("PUBNUB_SUBSCRIBE_KEY", responseObj));
            storeData.put(Utils.PUBNUB_SEC_KEY, generalFunc.getJsonValueStr("PUBNUB_SECRET_KEY", responseObj));
            storeData.put(Utils.SC_CONNECT_URL_KEY, generalFunc.getJsonValueStr("SC_CONNECT_URL", responseObj));
            storeData.put("RIDER_REQUEST_ACCEPT_TIME", generalFunc.getJsonValueStr("RIDER_REQUEST_ACCEPT_TIME", responseObj));

            storeData.put(Utils.ENABLE_TOLL_COST, generalFunc.getJsonValueStr("ENABLE_TOLL_COST", responseObj));
            storeData.put(Utils.TOLL_COST_APP_ID, generalFunc.getJsonValueStr("TOLL_COST_APP_ID", responseObj));
            storeData.put(Utils.TOLL_COST_APP_CODE, generalFunc.getJsonValueStr("TOLL_COST_APP_CODE", responseObj));
            storeData.put(Utils.APP_DESTINATION_MODE, generalFunc.getJsonValueStr("APP_DESTINATION_MODE", responseObj));
            storeData.put(Utils.APP_TYPE, generalFunc.getJsonValueStr("APP_TYPE", responseObj));
            storeData.put(Utils.HANDICAP_ACCESSIBILITY_OPTION, generalFunc.getJsonValueStr("HANDICAP_ACCESSIBILITY_OPTION", responseObj));
            storeData.put(Utils.FEMALE_RIDE_REQ_ENABLE, generalFunc.getJsonValueStr("FEMALE_RIDE_REQ_ENABLE", responseObj));
            storeData.put(Utils.ENABLE_GOPAY_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_GOPAY_KEY, responseObj));
            storeData.put(Utils.CHILD_SEAT_ACCESSIBILITY_OPTION, generalFunc.getJsonValueStr("CHILD_SEAT_ACCESSIBILITY_OPTION", responseObj));
            storeData.put(Utils.WHEEL_CHAIR_ACCESSIBILITY_OPTION, generalFunc.getJsonValueStr("WHEEL_CHAIR_ACCESSIBILITY_OPTION", responseObj));
            storeData.put(Utils.PUBNUB_DISABLED_KEY, generalFunc.getJsonValueStr("PUBNUB_DISABLED", responseObj));
            storeData.put(Utils.ENABLE_SOCKET_CLUSTER_KEY, generalFunc.getJsonValueStr("ENABLE_SOCKET_CLUSTER", responseObj));
            storeData.put(Utils.ISWALLETBALNCECHANGE, "No");

            storeData.put(Utils.ENABLE_MULTI_DELIVERY_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_MULTI_DELIVERY_KEY, responseObj));
            storeData.put(Utils.ALLOW_MULTIPLE_DEST_ADD_KEY, generalFunc.getJsonValueStr(Utils.ALLOW_MULTIPLE_DEST_ADD_KEY, responseObj));

            storeData.put(Utils.MAX_ALLOW_NUM_DESTINATION_MULTI_KEY, generalFunc.getJsonValueStr(Utils.MAX_ALLOW_NUM_DESTINATION_MULTI_KEY, responseObj));
            storeData.put(Utils.ENABLE_ROUTE_CALCULATION_MULTI_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_ROUTE_CALCULATION_MULTI_KEY, responseObj));
            storeData.put(Utils.ENABLE_ROUTE_OPTIMIZE_MULTI_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_ROUTE_OPTIMIZE_MULTI_KEY, responseObj));
            storeData.put(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY, responseObj));

            storeData.put(Utils.BOOK_FOR_ELSE_SHOW_NO_CONTACT_KEY, generalFunc.getJsonValueStr(Utils.BOOK_FOR_ELSE_SHOW_NO_CONTACT_KEY, responseObj));
            storeData.put(Utils.BOOK_FOR_ELSE_ENABLE_KEY, generalFunc.getJsonValueStr(Utils.BOOK_FOR_ELSE_ENABLE_KEY, responseObj));

            storeData.put(Utils.ENABLE_DRIVER_DESTINATIONS_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_DRIVER_DESTINATIONS_KEY, responseObj));
            storeData.put(Utils.ENABLE_STOPOVER_POINT_KEY, generalFunc.getJsonValueStr(Utils.ENABLE_STOPOVER_POINT_KEY, responseObj));
            storeData.put(Utils.MAX_NUMBER_STOP_OVER_POINTS_KEY, generalFunc.getJsonValueStr(Utils.MAX_NUMBER_STOP_OVER_POINTS_KEY, responseObj));

            Utils.SKIP_MOCK_LOCATION_CHECK = generalFunc.getJsonValueStr("eAllowFakeGPS", responseObj).equalsIgnoreCase("Yes");
        }
        else
        {



            storeData.put(Utils.FACEBOOK_APPID_KEY, generalFunc.getJsonValueStr("FACEBOOK_APP_ID", responseObj));
            storeData.put(Utils.LINK_FORGET_PASS_KEY, generalFunc.getJsonValueStr("LINK_FORGET_PASS_PAGE_PASSENGER", responseObj));
            storeData.put(Utils.APP_GCM_SENDER_ID_KEY, generalFunc.getJsonValueStr("GOOGLE_SENDER_ID", responseObj));
            storeData.put(Utils.MOBILE_VERIFICATION_ENABLE_KEY, generalFunc.getJsonValueStr("MOBILE_VERIFICATION_ENABLE", responseObj));




            storeData.put("CURRENCY_OPTIONAL", generalFunc.getJsonValueStr("CURRENCY_OPTIONAL", responseObj));
            storeData.put("LANGUAGE_OPTIONAL", generalFunc.getJsonValueStr("LANGUAGE_OPTIONAL", responseObj));

            storeData.put(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", generalFunc.getJsonValueStr("DefaultLanguageValues", responseObj)));



            storeData.put(Utils.languageLabelsKey, generalFunc.getJsonValueStr("LanguageLabels", responseObj));
            storeData.put(Utils.LANGUAGE_LIST_KEY, generalFunc.getJsonValueStr("LIST_LANGUAGES", responseObj));
            storeData.put(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", generalFunc.getJsonValueStr("DefaultLanguageValues", responseObj)));
            storeData.put(Utils.LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vCode", generalFunc.getJsonValueStr("DefaultLanguageValues", responseObj)));
            storeData.put(Utils.DEFAULT_LANGUAGE_VALUE, generalFunc.getJsonValue("vTitle", generalFunc.getJsonValueStr("DefaultLanguageValues", responseObj)));

            String UPDATE_TO_DEFAULT = generalFunc.getJsonValueStr("UPDATE_TO_DEFAULT", responseObj);
            storeData.put("UPDATE_TO_DEFAULT", UPDATE_TO_DEFAULT);

            if (generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE).equalsIgnoreCase("") || UPDATE_TO_DEFAULT.equalsIgnoreCase("Yes")) {
                storeData.put(Utils.DEFAULT_CURRENCY_VALUE, generalFunc.getJsonValue("vName", generalFunc.getJsonValueStr("DefaultCurrencyValues", responseObj)));
            }

            storeData.put(Utils.FACEBOOK_LOGIN, generalFunc.getJsonValueStr("FACEBOOK_LOGIN", responseObj));
            storeData.put(Utils.GOOGLE_LOGIN, generalFunc.getJsonValueStr("GOOGLE_LOGIN", responseObj));
            storeData.put(Utils.TWITTER_LOGIN, generalFunc.getJsonValueStr("TWITTER_LOGIN", responseObj));


            storeData.put(Utils.DefaultCountry, generalFunc.getJsonValueStr("vDefaultCountry", responseObj));
            storeData.put(Utils.DefaultCountryCode, generalFunc.getJsonValueStr("vDefaultCountryCode", responseObj));

            storeData.put(Utils.DefaultPhoneCode, generalFunc.getJsonValueStr("vDefaultPhoneCode", responseObj));
            storeData.put(Utils.DefaultCountryImage, generalFunc.getJsonValueStr("vDefaultCountryImage", responseObj));



            storeData.put(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY, generalFunc.getJsonValueStr("GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY", responseObj));




            generalFunc.storeData(storeData);



        }
        Logger.d("CheckTSITE_DB",generalFunc.getJsonValueStr("TSITE_DB", responseObj));

        storeData.put(Utils.DELIVERALL_KEY, generalFunc.getJsonValueStr(Utils.DELIVERALL_KEY, responseObj));
        storeData.put(Utils.ONLYDELIVERALL_KEY, generalFunc.getJsonValueStr(Utils.ONLYDELIVERALL_KEY, responseObj));
        storeData.put(Utils.SINCH_APP_KEY, generalFunc.getJsonValueStr(Utils.SINCH_APP_KEY, responseObj));
        storeData.put(Utils.SINCH_APP_SECRET_KEY, generalFunc.getJsonValueStr(Utils.SINCH_APP_SECRET_KEY, responseObj));
        storeData.put(Utils.SINCH_APP_ENVIRONMENT_HOST, generalFunc.getJsonValueStr(Utils.SINCH_APP_ENVIRONMENT_HOST, responseObj));
        storeData.put(Utils.DefaultCountry, generalFunc.getJsonValueStr("vDefaultCountry", responseObj));
        storeData.put(Utils.DefaultCountryCode, generalFunc.getJsonValueStr("vDefaultCountryCode", responseObj));
        storeData.put(Utils.DefaultPhoneCode, generalFunc.getJsonValueStr("vDefaultPhoneCode", responseObj));
        storeData.put(Utils.DefaultCountryImage, generalFunc.getJsonValueStr("vDefaultCountryImage", responseObj));
        storeData.put(Utils.REFERRAL_SCHEME_ENABLE, generalFunc.getJsonValueStr("REFERRAL_SCHEME_ENABLE", responseObj));
        storeData.put(Utils.MOBILE_VERIFICATION_ENABLE_KEY, generalFunc.getJsonValueStr("MOBILE_VERIFICATION_ENABLE", responseObj));
        storeData.put(Utils.YALGAAR_CLIENT_KEY, generalFunc.getJsonValueStr("YALGAAR_CLIENT_KEY", responseObj));
        storeData.put(Utils.PUBSUB_TECHNIQUE, generalFunc.getJsonValueStr("PUBSUB_TECHNIQUE", responseObj));
        storeData.put(Utils.SITE_TYPE_KEY, generalFunc.getJsonValueStr("SITE_TYPE", responseObj));
        storeData.put("showCountryList", generalFunc.getJsonValueStr("showCountryList", responseObj));
        storeData.put(Utils.LINKDIN_LOGIN, generalFunc.getJsonValueStr("LINKEDIN_LOGIN", responseObj));
        storeData.put(Utils.CURRENCY_LIST_KEY, generalFunc.getJsonValueStr("LIST_CURRENCY", responseObj));
        storeData.put("ENABLE_OPTION_UPDATE_CURRENCY", generalFunc.getJsonValueStr("ENABLE_OPTION_UPDATE_CURRENCY", responseObj));


        generalFunc.storeData(storeData);
    }


}
