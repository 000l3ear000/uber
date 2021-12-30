package com.dialogs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.melevicarbrasil.usuario.CallScreenActivity;
import com.melevicarbrasil.usuario.OnGoingTripDetailsActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.AppFunctions;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.SinchService;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.HashMap;

public class OpenTutorDetailDialog {

    Context mContext;
    HashMap<String, String> data_trip;
    GeneralFunctions generalFunc;
    String vName = "";
    String vImage = "";

    androidx.appcompat.app.AlertDialog alertDialog;

    public OpenTutorDetailDialog(Context mContext, HashMap<String, String> data_trip, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.data_trip = data_trip;
        this.generalFunc = generalFunc;
        show();
    }

    public void show() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builder.setTitle("");

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.design_tutor_detail_dialog, null);
        builder.setView(dialogView);

        ((MTextView) dialogView.findViewById(R.id.rateTxt)).setText(data_trip.get("driverRating"));
        ((MTextView) dialogView.findViewById(R.id.nameTxt)).setText(data_trip.get("driverName"));
        vName = data_trip.get("driverName");

        ((MTextView) dialogView.findViewById(R.id.tutorDTxt)).setText(generalFunc.retrieveLangLBl("Tutor Detail", "LBL_DRIVER_DETAIL"));
        ((MTextView) dialogView.findViewById(R.id.callTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_CALL_TXT"));
        ((MTextView) dialogView.findViewById(R.id.msgTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_MESSAGE_TXT"));


        String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + data_trip.get("iDriverId") + "/"
                + data_trip.get("driverImage");

        if (!data_trip.get("driverImage").equals("")) {
            vImage = data_trip.get("driverImage");

        }

        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_no_pic_user)
                .error(R.mipmap.ic_no_pic_user)
                .into(((SelectableRoundedImageView) dialogView.findViewById(R.id.tutorImgView)));

        (dialogView.findViewById(R.id.callArea)).setOnClickListener(view -> {
            //getMaskNumber();
            String userprofileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

            if (generalFunc.getJsonValue("RIDE_DRIVER_CALLING_METHOD", userprofileJson).equals("Voip")) {
                sinchCall();
            } else {
                getMaskNumber();
            }
        });


        (dialogView.findViewById(R.id.msgArea)).setOnClickListener(view -> {
            try {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "" + data_trip.get("vCode") + "" + data_trip.get("driverMobile"));
                mContext.startActivity(smsIntent);
            } catch (Exception e) {

            }
        });

        (dialogView.findViewById(R.id.closeImg)).setOnClickListener(view -> {

            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog);
        }
        alertDialog.show();
    }

    public void sinchCall() {


        if (!generalFunc.isCallPermissionGranted(false)) {
            generalFunc.isCallPermissionGranted(true);
        } else {

            OnGoingTripDetailsActivity onGoingTripDetailsActivity = (com.melevicarbrasil.usuario.OnGoingTripDetailsActivity) MyApp.getInstance().getCurrentAct();

            if (ContextCompat.checkSelfPermission(onGoingTripDetailsActivity, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(onGoingTripDetailsActivity, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(onGoingTripDetailsActivity,
                        new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                        1);
            }

            String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Id", generalFunc.getMemberId());
            hashMap.put("Name", generalFunc.getJsonValue("vName", userProfileJson));
            hashMap.put("PImage", generalFunc.getJsonValue("vImgName", userProfileJson));
            hashMap.put("type", Utils.userType);
            if (new AppFunctions(mContext).checkSinchInstance(onGoingTripDetailsActivity!=null?onGoingTripDetailsActivity.getSinchServiceInterface():null)) {
                onGoingTripDetailsActivity.getSinchServiceInterface().getSinchClient().setPushNotificationDisplayName(generalFunc.retrieveLangLBl("", "LBL_INCOMING_CALL"));
                com.sinch.android.rtc.calling.Call call = onGoingTripDetailsActivity.getSinchServiceInterface().callUser(Utils.CALLTODRIVER + "_" + data_trip.get("iDriverId"), hashMap);
                String callId = call.getCallId();
                Intent callScreen = new Intent(mContext, CallScreenActivity.class);
                callScreen.putExtra(SinchService.CALL_ID, callId);
                callScreen.putExtra("vImage", vImage);
                callScreen.putExtra("vName", vName);
                callScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mContext.startActivity(callScreen);
            }
        }


    }

    public void call(String phoneNumber) {
        try {

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            mContext.startActivity(callIntent);

        } catch (Exception e) {
        }


    }

    public void getMaskNumber() {

            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("type", "getCallMaskNumber");
            parameters.put("iTripid", data_trip.get("iTripId"));
            parameters.put("UserType", Utils.userType);
            parameters.put("iMemberId", generalFunc.getMemberId());

            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
            exeWebServer.setLoaderConfig(mContext, true, generalFunc);

            exeWebServer.setDataResponseListener(responseString -> {

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail == true) {
                        String message = generalFunc.getJsonValue(Utils.message_str, responseString);

                        call(message);


                    } else {
                        call("+" + data_trip.get("vCode") + "" + data_trip.get("driverMobile"));

                    }
                } else {
                    generalFunc.showError();
                }
            });
            exeWebServer.execute();


    }
}
