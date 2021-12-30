package com.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.melevicarbrasil.usuario.CarWashBookingDetailsActivity;
import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.realmModel.CarWashCartData;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by Admin on 11-07-2016.
 */
public class RequestNearestCab implements Runnable, GenerateAlertBox.HandleAlertBtnClick {

    Context mContext;
    GeneralFunctions generalFunc;
    public Dialog dialogRequestNearestCab;
    GenerateAlertBox generateAlert;
    String driverIds;
    String cabRequestedJson;
    boolean isCancelBtnClick = false;

    RadarView mRadarView = null;

    public RequestNearestCab(Context mContext, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.generalFunc = generalFunc;
    }

    public void setRequestData(String driverIds, String cabRequestedJson) {
        this.driverIds = driverIds;
        this.cabRequestedJson = cabRequestedJson;
    }

    @Override
    public void run() {
        dialogRequestNearestCab = new Dialog(mContext,/* R.style.Theme_Dialog*/android.R.style.Theme_Translucent_NoTitleBar);
        dialogRequestNearestCab.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRequestNearestCab.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogRequestNearestCab.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialogRequestNearestCab.setContentView(R.layout.design_request_nearest_cab_dialog);

        MButton btn_type2 = ((MaterialRippleLayout) dialogRequestNearestCab.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"));
        //((RippleBackground) dialogRequestNearestCab.findViewById(R.id.rippleBgView)).startRippleAnimation();
        //(dialogRequestNearestCab.findViewById(R.id.backImgView)).setVisibility(View.GONE);
        //((MTextView) dialogRequestNearestCab.findViewById(R.id.titleTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_REQUESTING_TXT"));


        if (generalFunc.isRTLmode()) {
            //            ((LinearLayout) dialogRequestNearestCab.findViewById(R.id.retryBtnArea)).setRotationY(180);
            ((LinearLayout) dialogRequestNearestCab.findViewById(R.id.retryBtnArea)).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            ((LinearLayout) dialogRequestNearestCab.findViewById(R.id.retryArea)).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        String retryMsg = generalFunc.retrieveLangLBl("Driver is not able to take your request. Please cancel request and try again OR retry.", "LBL_NOTE_NO_DRIVER_REQUEST");

        String headrMsg = "";

        ((MTextView) dialogRequestNearestCab.findViewById(R.id.noDriverNotifyTxt)).setText(retryMsg);
        if (mContext != null) {
            if (mContext instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) mContext;
                if (mainActivity.getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                    headrMsg = generalFunc.retrieveLangLBl("Finding A Provider", "LBL_FINDING_PROVIDER_HEADER_TXT");
                    retryMsg = generalFunc.retrieveLangLBl("Driver is not able to take your request. Please cancel request and try again OR retry.", "LBL_NOTE_NO_PROVIDER_REQUEST");
                    ((MTextView) dialogRequestNearestCab.findViewById(R.id.noDriverNotifyTxt)).setText(retryMsg);
                } else if (mainActivity.getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                    headrMsg = generalFunc.retrieveLangLBl("Finding A Driver", "LBL_FINDING_DRIVER_HEADER_TXT");
                    retryMsg = generalFunc.retrieveLangLBl("Driver is not able to take your request. Please cancel request and try again OR retry.", "LBL_NOTE_NO_DRIVER_REQUEST");
                    ((MTextView) dialogRequestNearestCab.findViewById(R.id.noDriverNotifyTxt)).setText(retryMsg);
                } else {
                    headrMsg = generalFunc.retrieveLangLBl("Finding A Carrier", "LBL_FINDING_CARRIER_HEADER_TXT");
                    retryMsg = generalFunc.retrieveLangLBl("Driver is not able to take your request. Please cancel request and try again OR retry.", "LBL_NOTE_NO_CARRIER_REQUEST");
                    ((MTextView) dialogRequestNearestCab.findViewById(R.id.noDriverNotifyTxt)).setText(retryMsg);
                }
            } else if (mContext instanceof CarWashBookingDetailsActivity) {
                headrMsg = generalFunc.retrieveLangLBl("Finding A Provider", "LBL_FINDING_PROVIDER_HEADER_TXT");
                retryMsg = generalFunc.retrieveLangLBl("Driver is not able to take your request. Please cancel request and try again OR retry.",
                        "LBL_NOTE_NO_PROVIDER_REQUEST");
                ((MTextView) dialogRequestNearestCab.findViewById(R.id.noDriverNotifyTxt)).setText(retryMsg);
            }
        }

        mRadarView = dialogRequestNearestCab.findViewById(R.id.radarView);
        mRadarView.setShowCircles(true);
        mRadarView.startAnimation();

        TextView noDriverText = dialogRequestNearestCab.findViewById(R.id.noDriverText);
        TextView headerText = dialogRequestNearestCab.findViewById(R.id.headerText);
        TextView reqNoteText = dialogRequestNearestCab.findViewById(R.id.reqNoteText);
        TextView retryText = dialogRequestNearestCab.findViewById(R.id.retryText);
        TextView cancelText = dialogRequestNearestCab.findViewById(R.id.cancelText);
        TextView reqcancelText = dialogRequestNearestCab.findViewById(R.id.reqcancelText);

        cancelText.setText(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"));
        reqcancelText.setText(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"));
        retryText.setText(generalFunc.retrieveLangLBl("Retry", "LBL_RETRY_TXT"));
        headerText.setText(generalFunc.retrieveLangLBl("", "LBL_REQUESTING_TXT"));
        reqNoteText.setText(headrMsg);
        noDriverText.setText(retryMsg);

        retryText.setOnClickListener(v -> {
            if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).retryReqBtnPressed(driverIds, cabRequestedJson);
            } else if (mContext instanceof CarWashBookingDetailsActivity) {
                ((CarWashBookingDetailsActivity) mContext).retryReqBtnPressed(driverIds, cabRequestedJson);
            }
        });
        cancelText.setOnClickListener(v -> {
            isCancelBtnClick = true;
            cancelRequestConfirm();
        });
        reqcancelText.setOnClickListener(v -> {
            isCancelBtnClick = true;
            cancelRequestConfirm();
        });

        ((ProgressBar) dialogRequestNearestCab.findViewById(R.id.mProgressBar)).setIndeterminate(true);
        dialogRequestNearestCab.setCancelable(false);
        dialogRequestNearestCab.setCanceledOnTouchOutside(false);
        try {
            dialogRequestNearestCab.show();
        } catch (Exception e) {
        }
        (dialogRequestNearestCab.findViewById(R.id.cancelImgView)).setOnClickListener(view -> {
            if (!isCancelBtnClick) {
                isCancelBtnClick = true;
                cancelRequestConfirm();
            }
        });

        ((ProgressBar) dialogRequestNearestCab.findViewById(R.id.mProgressBar)).getIndeterminateDrawable().setColorFilter(
                mContext.getResources().getColor(R.color.appThemeColor_2), android.graphics.PorterDuff.Mode.SRC_IN);

        //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) (dialogRequestNearestCab.findViewById(R.id.titleTxt)).getLayoutParams();

        //layoutParams.setMargins(Utils.dipToPixels(mContext, 25), 0, 0, 0);
        // (dialogRequestNearestCab.findViewById(R.id.titleTxt)).setLayoutParams(layoutParams);

        btn_type2.setOnClickListener(view -> {
            if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).retryReqBtnPressed(driverIds, cabRequestedJson);
            } else if (mContext instanceof CarWashBookingDetailsActivity) {
                ((CarWashBookingDetailsActivity) mContext).retryReqBtnPressed(driverIds, cabRequestedJson);
            }

        });

    }

    public void setVisibilityOfRetryArea(int visibility) {
        //(dialogRequestNearestCab.findViewById(R.id.retryBtnArea)).setVisibility(visibility);
        //setVisibleBottomArea(visibility);
    }

    public void setVisibleBottomArea(int visibility) {
       // Animation bottomUp = AnimationUtils.loadAnimation(mContext, R.anim.bottom_up);
        LinearLayout layout = dialogRequestNearestCab.findViewById(R.id.retryArea);
       // layout.startAnimation(bottomUp);
        layout.setVisibility(visibility);

        if (layout.getVisibility() == View.VISIBLE) {
            LinearLayout reqLayout = dialogRequestNearestCab.findViewById(R.id.reqNoteArea);
            reqLayout.setVisibility(View.GONE);
        } else {
            LinearLayout reqLayout = dialogRequestNearestCab.findViewById(R.id.reqNoteArea);
            reqLayout.setVisibility(View.VISIBLE);
        }


    }

    public void setInVisibleBottomArea(int visibility) {
      //  Animation bottomUp = AnimationUtils.loadAnimation(mContext, R.anim.bottom_up);
        LinearLayout layout = dialogRequestNearestCab.findViewById(R.id.retryArea);
       // layout.startAnimation(bottomUp);
        layout.setVisibility(visibility);

        if (layout.getVisibility() == View.VISIBLE) {
            LinearLayout reqLayout = dialogRequestNearestCab.findViewById(R.id.reqNoteArea);
            reqLayout.setVisibility(View.GONE);
        } else {
            LinearLayout reqLayout = dialogRequestNearestCab.findViewById(R.id.reqNoteArea);
            reqLayout.setVisibility(View.VISIBLE);
        }

    }

    public int getRetryVisibility() {
        return (dialogRequestNearestCab.findViewById(R.id.retryBtnArea)).getVisibility();
    }

    public void dismissDialog() {
        if (dialogRequestNearestCab != null && dialogRequestNearestCab.isShowing()) {
            dialogRequestNearestCab.dismiss();
        }
    }

    public void releaseMainTask() {
        if (mContext != null && mContext instanceof MainActivity) {
            ((MainActivity) mContext).releaseScheduleNotificationTask();
        }
    }

    public void cancelRequestConfirm() {
        if (generateAlert != null) {
            generateAlert.closeAlertBox();
            generateAlert = null;
        }
        generateAlert = new GenerateAlertBox(mContext);
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(this);
        generateAlert.setContentMessage("",
                generalFunc.retrieveLangLBl("", "LBL_CONFIRM_REQUEST_CANCEL_TXT"));
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_TRIP_CANCEL_CONFIRM_TXT"));
        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"));
        generateAlert.showAlertBox();
    }

    @Override
    public void handleBtnClick(int btn_id) {
        if (btn_id == 0) {
            isCancelBtnClick = false;
            if (generateAlert != null) {
                generateAlert.closeAlertBox();
                generateAlert = null;
            }
        } else {
            if (generateAlert != null) {
                generateAlert.closeAlertBox();
                generateAlert = null;
            }

            //((MTextView) dialogRequestNearestCab.findViewById(R.id.titleTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_CANCELING_TXT"));

            cancelRequest();
        }
    }

    public void cancelRequest() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "cancelCabRequest");
        parameters.put("iUserId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mContext, parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {
                    try {
                        Realm realm = MyApp.getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(CarWashCartData.class);
                        realm.commitTransaction();
                    } catch (Exception e) {

                    }

                    dismissDialog();
                    releaseMainTask();
                    MyApp.getInstance().restartWithGetDataApp();
                } else {
                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);

                    if (message.equals("DO_RESTART") || message.equals(Utils.GCM_FAILED_KEY) || message.equals(Utils.APNS_FAILED_KEY) || message.equals("LBL_SERVER_COMM_ERROR")) {
                        dismissDialog();
                        releaseMainTask();
                        generalFunc.restartApp();
                    } else {
                        generalFunc.showGeneralMessage("",
                                generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }

                    /*((MTextView) dialogRequestNearestCab.findViewById(R.id.titleTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_REQUESTING_TXT"));*/
                }
            } else {
                /* ((MTextView) dialogRequestNearestCab.findViewById(R.id.titleTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_REQUESTING_TXT"));*/
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }
}
