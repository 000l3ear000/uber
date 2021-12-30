package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import com.melevicarbrasil.usuario.AccountverificationActivity;
import com.melevicarbrasil.usuario.CommonDeliveryTypeSelectionActivity;
import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.RatingActivity;
import com.melevicarbrasil.usuario.UberXHomeActivity;
import com.melevicarbrasil.usuario.VerifyInfoActivity;
import com.melevicarbrasil.usuario.deliverAll.FoodDeliveryHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.ServiceHomeActivity;
import com.realmModel.CarWashCartData;
import com.utils.Logger;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by Admin on 29-06-2016.
 */
public class OpenMainProfile {
    Context mContext;
    String responseString;
    boolean isCloseOnError;
    GeneralFunctions generalFun;
    String tripId = "";
    String eType = "";
    String eFly = "";
    boolean isnotification = false;
    JSONObject userProfileJsonObj;
    private boolean isFromOngoing;
    HashMap<String,String> storeData=new HashMap<>();
    ArrayList<String> removeData=new ArrayList<>();

    public OpenMainProfile(Context mContext, String responseString, boolean isCloseOnError, GeneralFunctions generalFun) {
        this.mContext = mContext;
        //  this.responseString = responseString;
        this.isCloseOnError = isCloseOnError;
        this.generalFun = generalFun;

        this.responseString = generalFun.retrieveValue(Utils.USER_PROFILE_JSON);

        userProfileJsonObj = generalFun.getJsonObject(this.responseString);


//        storeData=new HashMap<>();
//        storeData.put(Utils.DefaultCountry, generalFun.getJsonValueStr("vDefaultCountry", userProfileJsonObj));
//        storeData.put(Utils.DefaultCountryCode, generalFun.getJsonValueStr("vDefaultCountryCode", userProfileJsonObj));
//        storeData.put(Utils.DefaultPhoneCode, generalFun.getJsonValueStr("vDefaultPhoneCode", userProfileJsonObj));
//        storeData.put(Utils.DefaultCountryImage, generalFun.getJsonValueStr("vDefaultCountryImage", userProfileJsonObj));
//        generalFun.storeData(storeData);

    }

    public OpenMainProfile(Context mContext, String responseString, boolean isCloseOnError, GeneralFunctions generalFun, String tripId) {
        this.mContext = mContext;
        this.responseString = responseString;
        this.isCloseOnError = isCloseOnError;
        this.generalFun = generalFun;
        this.tripId = tripId;

        this.responseString = generalFun.retrieveValue(Utils.USER_PROFILE_JSON);

        userProfileJsonObj = generalFun.getJsonObject(this.responseString);
    }

    public void startProcess() {

        if (generalFun == null) {
            return;
        }

        generalFun.sendHeartBeat();
        generalFun.storeData("DEFAULT_SERVICE_CATEGORY_ID", "");
        setGeneralData();

        //remove carwash cart data
        Realm realm = MyApp.getRealmInstance();
        realm.beginTransaction();
        realm.delete(CarWashCartData.class);
        realm.commitTransaction();

        String vTripStatus = generalFun.getJsonValueStr("vTripStatus", userProfileJsonObj);
        String PaymentStatus_From_Passenger_str = "";
        String Ratings_From_Passenger_str = "";
        String vTripPaymentMode_str = "";
        String eVerified_str = "";
        String eSystem = "";

        JSONObject Last_trip_data = generalFun.getJsonObject("TripDetails", userProfileJsonObj);
        eType = generalFun.getJsonValueStr("eType", Last_trip_data);
        eFly = generalFun.getJsonValueStr("eFly", Last_trip_data);
        eSystem = generalFun.getJsonValueStr("eSystem", Last_trip_data);

        PaymentStatus_From_Passenger_str = generalFun.getJsonValueStr("PaymentStatus_From_Passenger", userProfileJsonObj);
        Ratings_From_Passenger_str = generalFun.getJsonValueStr("Ratings_From_Passenger", userProfileJsonObj);
        eVerified_str = generalFun.getJsonValueStr("eVerified", Last_trip_data);
        vTripPaymentMode_str = generalFun.getJsonValueStr("vTripPaymentMode", Last_trip_data);

        vTripPaymentMode_str = "Cash";// to remove paypal
        PaymentStatus_From_Passenger_str = "Approved"; // to remove paypal

        Bundle bn = new Bundle();
        if (generalFun.isDeliverOnlyEnabled()) {
            if (userProfileJsonObj != null && !userProfileJsonObj.equals(""))
                if (generalFun.getJsonValue("vPhone", userProfileJsonObj).equals("") || generalFun.getJsonValue("vEmail", userProfileJsonObj).equals("")) {
                    //open account verification screen
                    if (generalFun.getMemberId() != null && !generalFun.getMemberId().equals("")) {
                        bn.putBoolean("isRestart", true);
                        new StartActProcess(mContext).startActForResult(AccountverificationActivity.class, bn, Utils.SOCIAL_LOGIN_REQ_CODE);
                        return;
                    }
                }


            String ePhoneVerified = generalFun.getJsonValueStr("ePhoneVerified", userProfileJsonObj);
            String vPhoneCode = generalFun.getJsonValueStr("vPhoneCode", userProfileJsonObj);
            String vPhone = generalFun.getJsonValueStr("vPhone", userProfileJsonObj);

            if (!ePhoneVerified.equals("Yes")) {
                bn.putString("MOBILE", vPhoneCode + vPhone);
                bn.putString("msg", "DO_PHONE_VERIFY");
                bn.putBoolean("isrestart", true);
                bn.putString("isbackshow", "No");
                new StartActProcess(mContext).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
                return;
            }

            JSONArray serviceArray = generalFun.getJsonArray("ServiceCategories", userProfileJsonObj.toString());

            if (serviceArray != null && serviceArray.length() > 1 && generalFun.isAnyDeliverOptionEnabled()) {

                ArrayList<HashMap<String, String>> list_item = new ArrayList<>();
                for (int i = 0; i < serviceArray.length(); i++) {
                    JSONObject serviceObj = generalFun.getJsonObject(serviceArray, i);
                    HashMap<String, String> servicemap = new HashMap<>();
                    servicemap.put("iServiceId", generalFun.getJsonValue("iServiceId", serviceObj.toString()));
                    servicemap.put("vServiceName", generalFun.getJsonValue("vServiceName", serviceObj.toString()));
                    servicemap.put("vImage", generalFun.getJsonValue("vImage", serviceObj.toString()));
                    list_item.add(servicemap);
                }
                bn.putSerializable("servicedata", list_item);
                new StartActProcess(mContext).startActWithData(ServiceHomeActivity.class, bn);


            } else {

                if (!vTripStatus.equals("Not Active") || ((PaymentStatus_From_Passenger_str.equals("Approved")
                        || vTripPaymentMode_str.equals("Cash")) && Ratings_From_Passenger_str.equals("Done")
                        /*&& eVerified_str.equals("Verified")*/)) {
                    bn.putBoolean("isnotification", isnotification);
                    new StartActProcess(mContext).startActWithData(FoodDeliveryHomeActivity.class, bn);

                } else {
                    new StartActProcess(mContext).startActWithData(FoodDeliveryHomeActivity.class, bn);
                }
            }

        } else {
            if (Utils.checkText(tripId)) {
                bn.putString("tripId", tripId);
            }

            if (generalFun.getJsonValue("vPhone", userProfileJsonObj).equals("") || generalFun.getJsonValue("vEmail", userProfileJsonObj).equals("")) {
                //open account verification screen
                if (generalFun.getMemberId() != null && !generalFun.getMemberId().equals("")) {
                    if (!generalFun.getMemberId().equals("")) {
                        bn.putBoolean("isRestart", true);
                        new StartActProcess(mContext).startActForResult(AccountverificationActivity.class, bn, Utils.SOCIAL_LOGIN_REQ_CODE);
                    } else {
                        generalFun.restartApp();
                    }
                }
            } else if (!vTripStatus.equals("Not Active") || (PaymentStatus_From_Passenger_str.equals("Approved")
                    || vTripPaymentMode_str.equals("Cash")) && Ratings_From_Passenger_str.equals("Done")
                /*&& eVerified_str.equals("Verified")*/) {
                isFromOngoing = true;
                if (generalFun.getJsonValue("APP_TYPE", responseString).equalsIgnoreCase("UberX") ||
                        generalFun.getJsonValue("APP_TYPE", responseString).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {

                    if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))
                            && ((!eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) && !eType.equalsIgnoreCase(Utils.eType_Multi_Delivery))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new StartActProcess(mContext).startActWithData(MainActivity.class, bn);

                    } else if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))
                            && ((!eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) && eType.equalsIgnoreCase(Utils.eType_Multi_Delivery) && Utils.checkText(tripId))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new StartActProcess(mContext).startActWithData(MainActivity.class, bn);

                    } else if (eFly.equalsIgnoreCase("Yes") && vTripStatus.equalsIgnoreCase("Arrived")) {

                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new StartActProcess(mContext).startActWithData(MainActivity.class, bn);
                    } else {
                       // new StartActProcess(mContext).startActWithData(UberXActivity.class, bn);
                        new StartActProcess(mContext).startActWithData(UberXHomeActivity.class, bn);
                    }
                } else if (generalFun.getJsonValue("APP_TYPE", responseString).equalsIgnoreCase("Delivery")) {

                    if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))
                            && ((eType.equalsIgnoreCase("Deliver")) || (eType.equalsIgnoreCase(Utils.eType_Multi_Delivery) && Utils.checkText(tripId)))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new StartActProcess(mContext).startActWithData(MainActivity.class, bn);
                    } else {
                        bn.putString("iVehicleCategoryId", generalFun.getJsonValueStr("DELIVERY_CATEGORY_ID", userProfileJsonObj));
                        if (generalFun.getJsonValueStr("PACKAGE_TYPE", userProfileJsonObj).equalsIgnoreCase("STANDARD")) {
                            new StartActProcess(mContext).startActWithData(MainActivity.class, bn);
                        } else {
                            new StartActProcess(mContext).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                        }
                    }

                } else if (generalFun.getJsonValue("APP_TYPE", responseString).equalsIgnoreCase("Ride-Delivery")) {

                    if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new StartActProcess(mContext).startActWithData(MainActivity.class, bn);
                    } else {
                        bn.putString("iVehicleCategoryId", generalFun.getJsonValueStr("DELIVERY_CATEGORY_ID", userProfileJsonObj));

                        new StartActProcess(mContext).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);

                    }

                } else {
                    bn.putBoolean("isnotification", isnotification);
                    new StartActProcess(mContext).startActWithData(MainActivity.class, bn);
                }
            } else {
                if (!eType.equals("")) {
                    if (generalFun.getJsonValue("APP_TYPE", responseString).equalsIgnoreCase("UberX")) {
                        //new StartActProcess(mContext).startActWithData(UberXActivity.class, bn);
                        new StartActProcess(mContext).startActWithData(UberXHomeActivity.class, bn);
                    } else if (generalFun.getJsonValue("APP_TYPE", responseString).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                        if (eType.equals(Utils.CabGeneralType_UberX) || eType.equals(Utils.eType_Multi_Delivery)) {
                            if (eType.equals(Utils.eType_Multi_Delivery)) {
                                bn.putString("tripId", "");
                                bn.putString("isMulti", "");
                            }
                            new StartActProcess(mContext).startActWithData(MainActivity.class, bn);
                        } else {
                            new StartActProcess(mContext).startActWithData(RatingActivity.class, bn);
                        }
                    } else {
                        new StartActProcess(mContext).startActWithData(RatingActivity.class, bn);
                    }
                }
            }
        }

        if (Utils.checkText(tripId) && isFromOngoing) {

        } else {
            try {
                ActivityCompat.finishAffinity((Activity) mContext);
            } catch (Exception e) {
            }
        }
    }

    public void setGeneralData() {
        //  Utils.SKIP_MOCK_LOCATION_CHECK = generalFun.getJsonValueStr("eAllowFakeGPS", userProfileJsonObj).equalsIgnoreCase("Yes");

        storeData = new HashMap<>();
        // storeData.put(Utils.PUBNUB_PUB_KEY, generalFun.getJsonValueStr("PUBNUB_PUBLISH_KEY", userProfileJsonObj));
        // storeData.put(Utils.PUBNUB_SUB_KEY, generalFun.getJsonValueStr("PUBNUB_SUBSCRIBE_KEY", userProfileJsonObj));
        // storeData.put(Utils.PUBNUB_SEC_KEY, generalFun.getJsonValueStr("PUBNUB_SECRET_KEY", userProfileJsonObj));
        // storeData.put(Utils.SESSION_ID_KEY, generalFun.getJsonValueStr("tSessionId", userProfileJsonObj));
        //storeData.put(Utils.RIDER_REQUEST_ACCEPT_TIME_KEY, generalFun.getJsonValueStr("RIDER_REQUEST_ACCEPT_TIME", userProfileJsonObj));
        // storeData.put(Utils.DEVICE_SESSION_ID_KEY, generalFun.getJsonValueStr("tDeviceSessionId", userProfileJsonObj));

        // storeData.put(Utils.SMS_BODY_KEY, generalFun.getJsonValueStr(Utils.SMS_BODY_KEY, userProfileJsonObj));
        //storeData.put("DESTINATION_UPDATE_TIME_INTERVAL", generalFun.getJsonValueStr("DESTINATION_UPDATE_TIME_INTERVAL", userProfileJsonObj));
        //  storeData.put("showCountryList", generalFun.getJsonValueStr("showCountryList", userProfileJsonObj));

        // storeData.put(Utils.FETCH_TRIP_STATUS_TIME_INTERVAL_KEY, generalFun.getJsonValueStr("FETCH_TRIP_STATUS_TIME_INTERVAL", userProfileJsonObj));

//        storeData.put(Utils.VERIFICATION_CODE_RESEND_TIME_IN_SECONDS_KEY, generalFun.getJsonValueStr(Utils.VERIFICATION_CODE_RESEND_TIME_IN_SECONDS_KEY, userProfileJsonObj));
//        storeData.put(Utils.VERIFICATION_CODE_RESEND_COUNT_KEY, generalFun.getJsonValueStr(Utils.VERIFICATION_CODE_RESEND_COUNT_KEY, userProfileJsonObj));
//        storeData.put(Utils.VERIFICATION_CODE_RESEND_COUNT_RESTRICTION_KEY, generalFun.getJsonValueStr(Utils.VERIFICATION_CODE_RESEND_COUNT_RESTRICTION_KEY, userProfileJsonObj));

        //  storeData.put(Utils.APP_DESTINATION_MODE, generalFun.getJsonValueStr("APP_DESTINATION_MODE", userProfileJsonObj));
        // storeData.put(Utils.APP_TYPE, generalFun.getJsonValueStr("APP_TYPE", userProfileJsonObj));
        //   storeData.put(Utils.SITE_TYPE_KEY, generalFun.getJsonValueStr("SITE_TYPE", userProfileJsonObj));
        // storeData.put(Utils.ENABLE_TOLL_COST, generalFun.getJsonValueStr("ENABLE_TOLL_COST", userProfileJsonObj));
        // storeData.put(Utils.TOLL_COST_APP_ID, generalFun.getJsonValueStr("TOLL_COST_APP_ID", userProfileJsonObj));
        // storeData.put(Utils.TOLL_COST_APP_CODE, generalFun.getJsonValueStr("TOLL_COST_APP_CODE", userProfileJsonObj));
        // storeData.put(Utils.HANDICAP_ACCESSIBILITY_OPTION, generalFun.getJsonValueStr("HANDICAP_ACCESSIBILITY_OPTION", userProfileJsonObj));
        //  storeData.put(Utils.CHILD_SEAT_ACCESSIBILITY_OPTION, generalFun.getJsonValueStr("CHILD_SEAT_ACCESSIBILITY_OPTION", userProfileJsonObj));
        // storeData.put(Utils.WHEEL_CHAIR_ACCESSIBILITY_OPTION, generalFun.getJsonValueStr("WHEEL_CHAIR_ACCESSIBILITY_OPTION", userProfileJsonObj));
        // storeData.put(Utils.FEMALE_RIDE_REQ_ENABLE, generalFun.getJsonValueStr("FEMALE_RIDE_REQ_ENABLE", userProfileJsonObj));
        // storeData.put(Utils.PUBNUB_DISABLED_KEY, generalFun.getJsonValueStr("PUBNUB_DISABLED", userProfileJsonObj));
        //  storeData.put(Utils.ENABLE_SOCKET_CLUSTER_KEY, generalFun.getJsonValueStr("ENABLE_SOCKET_CLUSTER", userProfileJsonObj));

        // storeData.put(Utils.PUBSUB_TECHNIQUE, generalFun.getJsonValueStr("PUBSUB_TECHNIQUE", userProfileJsonObj));
        //storeData.put(Utils.SC_CONNECT_URL_KEY, generalFun.getJsonValueStr("SC_CONNECT_URL", userProfileJsonObj));
        //   storeData.put(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY, generalFun.getJsonValueStr("GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY", userProfileJsonObj));

        //  storeData.put(Utils.ISWALLETBALNCECHANGE, "No");

        // storeData.put(Utils.DELIVERALL_KEY, generalFun.getJsonValueStr(Utils.DELIVERALL_KEY, userProfileJsonObj));
        // storeData.put(Utils.ONLYDELIVERALL_KEY, generalFun.getJsonValueStr(Utils.ONLYDELIVERALL_KEY, userProfileJsonObj));

        // storeData.put(Utils.LINKDIN_LOGIN, generalFun.getJsonValue("LINKEDIN_LOGIN", responseString));

        /*Multi Delivery Enabled*/
//        storeData.put(Utils.ENABLE_MULTI_DELIVERY_KEY, generalFun.getJsonValueStr(Utils.ENABLE_MULTI_DELIVERY_KEY, userProfileJsonObj));
//        storeData.put(Utils.ALLOW_MULTIPLE_DEST_ADD_KEY, generalFun.getJsonValueStr(Utils.ALLOW_MULTIPLE_DEST_ADD_KEY, userProfileJsonObj));
//
//        storeData.put(Utils.MAX_ALLOW_NUM_DESTINATION_MULTI_KEY, generalFun.getJsonValueStr(Utils.MAX_ALLOW_NUM_DESTINATION_MULTI_KEY, userProfileJsonObj));
//        storeData.put(Utils.ENABLE_ROUTE_CALCULATION_MULTI_KEY, generalFun.getJsonValueStr(Utils.ENABLE_ROUTE_CALCULATION_MULTI_KEY, userProfileJsonObj));
//        storeData.put(Utils.ENABLE_ROUTE_OPTIMIZE_MULTI_KEY, generalFun.getJsonValueStr(Utils.ENABLE_ROUTE_OPTIMIZE_MULTI_KEY, userProfileJsonObj));
//
//        storeData.put(Utils.SINCH_APP_KEY, generalFun.getJsonValueStr(Utils.SINCH_APP_KEY, userProfileJsonObj));
//        storeData.put(Utils.SINCH_APP_SECRET_KEY, generalFun.getJsonValueStr(Utils.SINCH_APP_SECRET_KEY, userProfileJsonObj));
//        storeData.put(Utils.SINCH_APP_ENVIRONMENT_HOST, generalFun.getJsonValueStr(Utils.SINCH_APP_ENVIRONMENT_HOST, userProfileJsonObj));

        /*Enable fav driver feature*/
        // storeData.put(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY, generalFun.getJsonValueStr(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY, userProfileJsonObj));

        /*BFSE */
//        storeData.put(Utils.BOOK_FOR_ELSE_SHOW_NO_CONTACT_KEY, generalFun.getJsonValueStr(Utils.BOOK_FOR_ELSE_SHOW_NO_CONTACT_KEY, userProfileJsonObj));
//        storeData.put(Utils.BOOK_FOR_ELSE_SHOW_NO_CONTACT_KEY, generalFun.getJsonValueStr(Utils.BOOK_FOR_ELSE_SHOW_NO_CONTACT_KEY, userProfileJsonObj));
//        storeData.put(Utils.BOOK_FOR_ELSE_ENABLE_KEY, generalFun.getJsonValueStr(Utils.BOOK_FOR_ELSE_ENABLE_KEY, userProfileJsonObj));

        /*Enable end of the day feature*/
        // storeData.put(Utils.ENABLE_DRIVER_DESTINATIONS_KEY, generalFun.getJsonValueStr(Utils.ENABLE_DRIVER_DESTINATIONS_KEY, userProfileJsonObj));

        /*Multi StopOver*/
        // storeData.put(Utils.ENABLE_STOPOVER_POINT_KEY, generalFun.getJsonValueStr(Utils.ENABLE_STOPOVER_POINT_KEY, userProfileJsonObj));
        // storeData.put(Utils.MAX_NUMBER_STOP_OVER_POINTS_KEY, generalFun.getJsonValueStr(Utils.MAX_NUMBER_STOP_OVER_POINTS_KEY, userProfileJsonObj));

        // GoJek - go Pay
        //  storeData.put(Utils.ENABLE_GOPAY_KEY, generalFun.getJsonValueStr(Utils.ENABLE_GOPAY_KEY, userProfileJsonObj));

        Logger.d("CheckTSITE_DBC","::"+userProfileJsonObj);
        new SetGeneralData(generalFun, userProfileJsonObj);

        removeData = new ArrayList<>();
        removeData.add("userHomeLocationLatitude");
        removeData.add("userHomeLocationLongitude");
        removeData.add("userHomeLocationAddress");
        removeData.add("userWorkLocationLatitude");
        removeData.add("userWorkLocationLongitude");
        removeData.add("userWorkLocationAddress");
        generalFun.removeValue(removeData);

        JSONArray userFavouriteAddressArr = generalFun.getJsonArray("UserFavouriteAddress", responseString);
        if (userFavouriteAddressArr != null && userFavouriteAddressArr.length() > 0) {

            for (int i = 0; i < userFavouriteAddressArr.length(); i++) {
                JSONObject dataItem = generalFun.getJsonObject(userFavouriteAddressArr, i);

                if (generalFun.getJsonValueStr("eType", dataItem).equalsIgnoreCase("HOME")) {
                    storeData.put("userHomeLocationLatitude", generalFun.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userHomeLocationLongitude", generalFun.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userHomeLocationAddress", generalFun.getJsonValueStr("vAddress", dataItem));
                } else if (generalFun.getJsonValueStr("eType", dataItem).equalsIgnoreCase("WORK")) {
                    storeData.put("userWorkLocationLatitude", generalFun.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userWorkLocationLongitude", generalFun.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userWorkLocationAddress", generalFun.getJsonValueStr("vAddress", dataItem));
                }
            }
        }

        generalFun.storeData(storeData);
    }
}
