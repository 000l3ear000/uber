package com.melevicarbrasil.usuario;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.adapter.files.ViewMultiDeliveryDetailRecyclerAdapter;
import com.fragments.CustomSupportMapFragment;
import com.general.files.AppFunctions;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.MyScrollView;
import com.general.files.StartActProcess;
import com.model.Delivery_Data;
import com.model.Trip_Status;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Admin on 03-11-2017.
 */

public class ViewMultiDeliveryDetailsActivity extends BaseActivity implements ViewMultiDeliveryDetailRecyclerAdapter.OnItemClickList {

    private RecyclerView deliveryDetailSummuryRecyclerView;
    private ViewMultiDeliveryDetailRecyclerAdapter deliveryDetailSummaryAdapter;

    private MTextView paymentDetailsTitleTxt, paymentTypeTitleTxt, payByTitleTxt, totalfareTitleTxt, senderDetailsTitleTxt;
    private MTextView titleTxt;
    private MTextView paymentTypeTxt, payByTxt, totalfareTxt;
    private MTextView senderNameValTxt, senderPhoneValTxt;
    private SelectableRoundedImageView userProfileImgView;
    private ImageView profileimageback, backImgView;

    private RelativeLayout senderDetailArea;
    private ProgressBar loading;
    private ErrorView errorView;

    private GeneralFunctions generalFunc;
    ArrayList<Trip_Status> recipientDetailList = new ArrayList<>();
    String data_message;
    String last_trip_data = "";
    String riderImage = "";
    String iUserId = "";

    String vImage = "";
    String vName = "";

    MyScrollView scrollView;

    private CustomSupportMapFragment.OnTouchListener mListener;
    androidx.appcompat.app.AlertDialog collectPaymentFailedDialog = null;
    private Parcelable recyclerViewState;
    private boolean isIndividualFare = false;
    HashMap<String, String> data_trip;

    //    private android.support.v7.app.AlertDialog alert_showFare_detail;
    Dialog alert_showFare_detail;
    private View convertView = null;
    JSONObject last_trip_fare_data;
    String userprofileJson = "";
    String filePath = "";

    Toolbar toolbar;
    private LinearLayout payementDetailArea;
    private Dialog signatureImageDialog;
    private String senderImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_delivery_details);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        userprofileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        if (getIntent().hasExtra("TRIP_DATA")) {
            HashMap<String, String> data = (HashMap<String, String>) getIntent().getSerializableExtra("TRIP_DATA");
            this.data_trip = data;
            vName = data_trip.get("PName");
        }


        init();
        setLables();
        setView();
        getTripDeliveryLocations();

        String OPEN_CHAT = generalFunc.retrieveValue("OPEN_CHAT");

        if (Utils.checkText(OPEN_CHAT)) {
            JSONObject OPEN_CHAT_DATA_OBJ = generalFunc.getJsonObject(OPEN_CHAT);
            generalFunc.removeValue("OPEN_CHAT");
            if (OPEN_CHAT_DATA_OBJ != null)
                new StartActProcess(getActContext()).startActWithData(ChatActivity.class, generalFunc.createChatBundle(OPEN_CHAT_DATA_OBJ));
        }

    }

    private void setData(String sender_message, JSONObject responseString) {
        data_message = sender_message;

        senderNameValTxt.setText(generalFunc.getJsonValue("vName", sender_message));

        String vTripPaymentMode = generalFunc.getJsonValue("vTripPaymentMode", sender_message);
        String payType = Utils.checkText(vTripPaymentMode) ? vTripPaymentMode : generalFunc.getJsonValue("ePayType", sender_message);
        paymentTypeTxt.setText(payType);

//        paymentTypeTxt.setText(generalFunc.getJsonValue("vTripPaymentMode", sender_message));
        payByTxt.setText("" + generalFunc.getJsonValue("PaymentPerson", responseString));
        totalfareTxt.setText("" + generalFunc.getJsonValue("DriverPaymentAmount", responseString));

        /*String ePaymentBy = generalFunc.getJsonValueStr("ePaymentBy", responseString);
        if (ePaymentBy.equalsIgnoreCase("Individual")) {
            isIndividualFare = true;
            ((MTextView) findViewById(R.id.indifareTxt)).setText("" + generalFunc.getJsonValue("Fare_Payable", responseString));
            ((MTextView) findViewById(R.id.indifareTitleTxt)).setText("" + generalFunc.retrieveLangLBl("Payable amount", "LBL_MULTI_PAYBALE_AMOUNT") + ":");
            ((LinearLayout) findViewById(R.id.totalFareArea)).setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_bg_parent_1));
        }*/

        totalfareTxt.setText("" + generalFunc.getJsonValue("DriverPaymentAmount", responseString));


        senderPhoneValTxt.setText("+" + generalFunc.getJsonValue("vCode", data_message) + " " + generalFunc.getJsonValue("vMobile", sender_message));
        riderImage = generalFunc.getJsonValue("vImage", sender_message);
        iUserId = generalFunc.getJsonValue("iUserId", sender_message);
        vName = generalFunc.getJsonValue("vName", sender_message);
        String image_url = CommonUtilities.SERVER_URL_PHOTOS + "upload/Driver/" + generalFunc.getJsonValue("iDriverId", sender_message) + "/"
                + riderImage;


        vImage = image_url;


        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (profileimageback != null) {
                    Utils.setBlurImage(bitmap, profileimageback);
                }

                userProfileImgView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                userProfileImgView.setImageResource(R.mipmap.ic_no_pic_user);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                userProfileImgView.setImageResource(R.mipmap.ic_no_pic_user);

            }
        };

        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_no_pic_user)
                .error(R.mipmap.ic_no_pic_user)
                .into(target);


    }


    private void setLables() {
        ((MTextView) findViewById(R.id.passengerSignTxt)).setText(generalFunc.retrieveLangLBl("View Signature", "LBL_VIEW_MULTI_SENDER_SIGN"));
        titleTxt.setText(generalFunc.retrieveLangLBl("Delivery Details", "LBL_DELIVERY_DETAILS"));
        paymentDetailsTitleTxt.setText(generalFunc.retrieveLangLBl("PAYMENT DETAIL", "LBL_PAYMENT_HEADER_TXT"));
        paymentTypeTitleTxt.setText(generalFunc.retrieveLangLBl("Payment Type", "LBL_PAYMENT_TYPE_TXT") + ":");
        payByTitleTxt.setText(generalFunc.retrieveLangLBl("Pay By", "LBL_MULTI_PAY_BY_TXT") + ":");
        totalfareTitleTxt.setText(generalFunc.retrieveLangLBl("Total Fare", "LBL_Total_Fare") + ":");
        senderDetailsTitleTxt.setText(generalFunc.retrieveLangLBl("Sender Details", "LBL_MULTI_SENDER_DETAILS_TXT"));
    }

    public Context getActContext() {
        return ViewMultiDeliveryDetailsActivity.this;
    }

    private void init() {
        scrollView = (MyScrollView) findViewById(R.id.mainScroll);
        deliveryDetailSummuryRecyclerView = (RecyclerView) findViewById(R.id.deliveryDetailSummuryRecyclerView);
        payementDetailArea = (LinearLayout) findViewById(R.id.payementDetailArea);
        senderDetailArea = (RelativeLayout) findViewById(R.id.senderDetailArea);
        paymentDetailsTitleTxt = (MTextView) findViewById(R.id.paymentDetailsTitleTxt);
        paymentTypeTitleTxt = (MTextView) findViewById(R.id.paymentTypeTitleTxt);
        paymentTypeTxt = (MTextView) findViewById(R.id.paymentTypeTxt);
        payByTitleTxt = (MTextView) findViewById(R.id.payByTitleTxt);
        payByTxt = (MTextView) findViewById(R.id.payByTxt);
        totalfareTitleTxt = (MTextView) findViewById(R.id.totalfareTitleTxt);
        totalfareTxt = (MTextView) findViewById(R.id.totalfareTxt);
        senderDetailsTitleTxt = (MTextView) findViewById(R.id.senderDetailsTitleTxt);
        senderNameValTxt = (MTextView) findViewById(R.id.senderNameValTxt);
        senderPhoneValTxt = (MTextView) findViewById(R.id.senderPhoneValTxt);
        profileimageback = (ImageView) findViewById(R.id.profileimageback);
        userProfileImgView = (SelectableRoundedImageView) findViewById(R.id.userProfileImgView);
        loading = (ProgressBar) findViewById(R.id.loading);
        errorView = (ErrorView) findViewById(R.id.errorView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        senderDetailArea.setVisibility(View.VISIBLE);
        backImgView.setVisibility(View.VISIBLE);
        backImgView.setOnClickListener(new setOnClickList());

    }


    public void getTripDeliveryLocations() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (deliveryDetailSummuryRecyclerView.getVisibility() == View.VISIBLE) {
            deliveryDetailSummuryRecyclerView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }
        recipientDetailList.clear();
        deliveryDetailSummaryAdapter.notifyDataSetChanged();

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getTripDeliveryDetails");
        parameters.put("iCabBookingId", "");

        String iCabBookingId = getIntent().hasExtra("iCabBookingId") ? getIntent().getStringExtra("iCabBookingId") : "";
        if (Utils.checkText(iCabBookingId)) {
            parameters.put("iCabBookingId", iCabBookingId);
        }

        String iCabRequestId = getIntent().hasExtra("iCabRequestId") ? getIntent().getStringExtra("iCabRequestId") : "";
        if (Utils.checkText(iCabRequestId)) {
            parameters.put("iCabRequestId", iCabRequestId);
        }
        parameters.put("iTripId", getIntent().getStringExtra("iTripId"));
        parameters.put("userType", Utils.userType);
        parameters.put("iDriverId", generalFunc.getMemberId());

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseStringObj = generalFunc.getJsonObject(responseString);

            if (responseStringObj != null && !responseStringObj.equals("")) {
                recipientDetailList.clear();
                closeLoader();
                String msg_str = generalFunc.getJsonValueStr(Utils.message_str, responseStringObj);

                if (generalFunc.checkDataAvail(Utils.action_str, responseStringObj)) {

                    if (Utils.checkText(msg_str)) {
                        JSONObject jobject = generalFunc.getJsonObject("MemberDetails", msg_str);
                        last_trip_data = generalFunc.getJsonValueStr("TripDetails", responseStringObj);
                        last_trip_fare_data = responseStringObj;

                        if (jobject != null) {
                            setData(jobject.toString(), responseStringObj);
                        }


                        JSONArray deliveries = generalFunc.getJsonArray("Deliveries", msg_str);
                        if (deliveries != null) {

                            String LBL_RECIPIENT = "", LBL_Status = "", LBL_CANCELLED="",LBL_CANCELED_TRIP_TXT = "", LBL_FINISHED_TXT = "", LBL_MULTI_AMOUNT_COLLECT_TXT = "", LBL_PICK_UP_INS = "", LBL_DELIVERY_INS = "", LBL_PACKAGE_DETAILS = "", LBL_CALL_TXT = "", LBL_VIEW_SIGN_TXT = "", LBL_MESSAGE_ACTIVE_TRIP = "", LBL_MULTI_RESPONSIBLE_FOR_PAYMENT_TXT = "";

                            if (deliveries.length() > 0) {
                                LBL_RECIPIENT = generalFunc.retrieveLangLBl("", "LBL_RECIPIENT");
                                LBL_Status = generalFunc.retrieveLangLBl("", "LBL_Status");
//                                LBL_CANCELED_TRIP_TXT = generalFunc.retrieveLangLBl("", "LBL_CANCELED_TRIP_TXT");
                                LBL_CANCELLED = generalFunc.retrieveLangLBl("", "LBL_CANCELLED");
                                LBL_FINISHED_TXT = generalFunc.retrieveLangLBl("", "LBL_FINISHED_TXT");
                                LBL_MULTI_AMOUNT_COLLECT_TXT = generalFunc.retrieveLangLBl("", "LBL_MULTI_AMOUNT_COLLECT_TXT");
                                LBL_PICK_UP_INS = generalFunc.retrieveLangLBl("", "LBL_PICK_UP_INS");
                                LBL_DELIVERY_INS = generalFunc.retrieveLangLBl("", "LBL_DELIVERY_INS");
                                LBL_PACKAGE_DETAILS = generalFunc.retrieveLangLBl("", "LBL_PACKAGE_DETAILS");
                                LBL_CALL_TXT = generalFunc.retrieveLangLBl("", "LBL_CALL_TXT");
                                LBL_VIEW_SIGN_TXT = generalFunc.retrieveLangLBl("", "LBL_VIEW_SIGN_TXT");
                                LBL_MESSAGE_ACTIVE_TRIP = generalFunc.retrieveLangLBl("", "LBL_MESSAGE_ACTIVE_TRIP");
                                LBL_MULTI_RESPONSIBLE_FOR_PAYMENT_TXT = generalFunc.retrieveLangLBl("Responsible for payment", "LBL_MULTI_RESPONSIBLE_FOR_PAYMENT_TXT");
                            }

//                                senderImage = generalFunc.getJsonValue("Sender_Signature", jobject.toString());
//                            if (Utils.checkText(senderImage)) {
//                                ((MTextView) findViewById(R.id.passengerSignTxt)).setVisibility(View.VISIBLE);
//                                ((MTextView) findViewById(R.id.passengerSignTxt)).setOnClickListener(new setOnClickList());
//                            }


                            for (int i = 0; i < deliveries.length(); i++) {
                                Trip_Status recipientDetailMap1 = new Trip_Status();
                                JSONArray deliveriesArray = generalFunc.getJsonArray(deliveries, i);

                                if (deliveriesArray != null && deliveriesArray.length() > 0) {


                                    ArrayList<Delivery_Data> subrecipientDetailList = new ArrayList<>();

                                    for (int j = 0; j < deliveriesArray.length(); j++) {

                                        JSONObject jobject1 = generalFunc.getJsonObject(deliveriesArray, j);
                                        Delivery_Data recipientDetailMap = new Delivery_Data();

                                        String vValue = generalFunc.getJsonValueStr("vValue", jobject1);
                                        String vFieldName = generalFunc.getJsonValueStr("vFieldName", jobject1);



                                        recipientDetailMap.setvValue(vValue);

                                        if (vFieldName.equalsIgnoreCase("Recepient Name") || (generalFunc.getJsonValueStr("iDeliveryFieldId", jobject1).equalsIgnoreCase("2"))) {
                                            recipientDetailMap1.setRecepientName(vValue);
                                        } else if (vFieldName.equalsIgnoreCase("Mobile Number") || (generalFunc.getJsonValueStr("iDeliveryFieldId", jobject1).equalsIgnoreCase("3"))) {
                                            recipientDetailMap1.setRecepientNum(vValue);
                                            recipientDetailMap1.setRecepientMaskNum(generalFunc.getJsonValueStr("vMaskValue", jobject1));
                                        } else if (vFieldName.equalsIgnoreCase("Address")) {
                                            recipientDetailMap1.setePaymentByReceiver(generalFunc.getJsonValueStr("ePaymentByReceiver", jobject1));
                                            recipientDetailMap1.setRecepientAddress(AppFunctions.fromHtml(generalFunc.getJsonValue("tDaddress", jobject1.toString())).toString());
                                            recipientDetailMap.setiTripDeliveryLocationId(AppFunctions.fromHtml(generalFunc.getJsonValue("iTripDeliveryLocationId", jobject1.toString())).toString());
                                            recipientDetailMap1.setReceipent_Signature(generalFunc.getJsonValueStr("Receipent_Signature", jobject1));

                                            recipientDetailMap.setvValue(generalFunc.getJsonValueStr("tDaddress", jobject1));

                                            recipientDetailMap1.setiTripDeliveryLocationId(generalFunc.getJsonValueStr("iTripDeliveryLocationId", jobject1));

                                            recipientDetailMap1.setiActive(generalFunc.getJsonValueStr("iActive", jobject1));
                                        }

                                        recipientDetailMap.setvFieldName(vFieldName);

                                        recipientDetailMap.setiDeliveryFieldId(generalFunc.getJsonValueStr("iDeliveryFieldId", jobject1));

                                        recipientDetailMap.settSaddress(generalFunc.getJsonValueStr("tSaddress", jobject1));

                                        recipientDetailMap.settStartLat(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValueStr("tStartLat", jobject1)));

                                        recipientDetailMap.settStartLong(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValueStr("tStartLong", jobject1)));


                                        recipientDetailMap.settDaddress(generalFunc.getJsonValueStr("tDaddress", jobject1));


                                        recipientDetailMap.settDestLat(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValueStr("tEndLat", jobject1)));

                                        recipientDetailMap.settDestLong(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValueStr("tEndLong", jobject1)));

                                        recipientDetailMap.setePaymentByReceiver(generalFunc.getJsonValueStr("ePaymentByReceiver", jobject1));
                                        if (!vFieldName.equalsIgnoreCase("Address") && (!vFieldName.equalsIgnoreCase("Mobile Number") && !(generalFunc.getJsonValueStr("iDeliveryFieldId", jobject1).equalsIgnoreCase("3"))) && (!vFieldName.equalsIgnoreCase("Recepient Name") && !(generalFunc.getJsonValueStr("iDeliveryFieldId", jobject1).equalsIgnoreCase("2"))) /*&& Utils.checkText(generalFunc.getJsonValue("vValue", jobject1))*/) {
                                            subrecipientDetailList.add(recipientDetailMap);

                                        }
                                    }

                                    String status = getIntent().hasExtra("Status") ? getIntent().getStringExtra("Status") : "";
                                    if (status.equalsIgnoreCase("activeTrip")) {
                                        recipientDetailMap1.setShowUpcomingLocArea("Yes");
                                    } else {
                                        recipientDetailMap1.setShowUpcomingLocArea("No");
                                    }
                                    if (status.equalsIgnoreCase("cabRequestScreen")) {
                                        recipientDetailMap1.setShowMobile("No");
                                    } else {
                                        recipientDetailMap1.setShowMobile("Yes");
                                    }

                                    recipientDetailMap1.setLBL_RECIPIENT(LBL_RECIPIENT);
                                    recipientDetailMap1.setLBL_Status(LBL_Status);
                                    recipientDetailMap1.setLBL_CANCELED_TRIP_TXT(LBL_CANCELLED);
                                    recipientDetailMap1.setLBL_FINISHED_TRIP_TXT(LBL_FINISHED_TXT);

                                    recipientDetailMap1.setLBL_PACKAGE_DETAILS(LBL_PICK_UP_INS);
                                    recipientDetailMap1.setLBL_DELIVERY_INS(LBL_DELIVERY_INS);
                                    recipientDetailMap1.setLBL_PACKAGE_DETAILS(LBL_PACKAGE_DETAILS);
                                    recipientDetailMap1.setLBL_CALL_TXT(LBL_CALL_TXT);
                                    recipientDetailMap1.setLBL_MESSAGE_ACTIVE_TRIP(LBL_MESSAGE_ACTIVE_TRIP);
                                    recipientDetailMap1.setLBL_VIEW_SIGN_TXT(LBL_VIEW_SIGN_TXT);

                                    recipientDetailMap1.setLBL_RESPONSIBLE_FOR_PAYMENT_TXT(LBL_MULTI_RESPONSIBLE_FOR_PAYMENT_TXT);

                                    recipientDetailMap1.setListOfDeliveryItems(subrecipientDetailList);
                                    recipientDetailList.add(recipientDetailMap1);
                                }
                            }

                        }

                    }

                    resetView();


                    deliveryDetailSummaryAdapter.notifyDataSetChanged();


                } else {
                    generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("Error", "LBL_ERROR_TXT"),
                            generalFunc.retrieveLangLBl("", msg_str));
                    deliveryDetailSummaryAdapter.notifyDataSetChanged();

                }
            } else {
                generateErrorView();
                deliveryDetailSummaryAdapter.notifyDataSetChanged();
            }
        });
        exeWebServer.execute();
    }

    private void resetView() {
        deliveryDetailSummuryRecyclerView.setVisibility(View.VISIBLE);
        setView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        recyclerViewState = deliveryDetailSummuryRecyclerView.getLayoutManager().onSaveInstanceState();


    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        deliveryDetailSummuryRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                getTripDeliveryLocations();
            }
        });
    }


    public void setView() {
        deliveryDetailSummuryRecyclerView.setVisibility(View.VISIBLE);
        deliveryDetailSummaryAdapter = new ViewMultiDeliveryDetailRecyclerAdapter(getActContext(), ViewMultiDeliveryDetailsActivity.this, recipientDetailList, generalFunc);
        deliveryDetailSummuryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        deliveryDetailSummuryRecyclerView.setAdapter(deliveryDetailSummaryAdapter);
        deliveryDetailSummaryAdapter.notifyDataSetChanged();
        deliveryDetailSummaryAdapter.setOnItemClickList(this);
    }

    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (backImgView.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(String data, String type, int position) {

    }

    @Override
    public void onItemClick(String type, int position) {
        showSignatureImage(generalFunc.retrieveLangLBl("", "LBL_RECIPIENT_NAME_HEADER_TXT") + " : " + recipientDetailList.get(position).getRecepientName(), recipientDetailList.get(position).getReceipent_Signature(), false);
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backImgView:
                    ViewMultiDeliveryDetailsActivity.super.onBackPressed();
                    break;
                case R.id.cancelArea:
                    Log.v("log_tag", "Panel Canceled");
                    // Calling the same class
                    recreate();
                    break;
                case R.id.passengerSignTxt:
                    //new StartActProcess(getActContext()).startActWithData(UberXSelectServiceActivity.class, bundle);
                    showSignatureImage(data_trip.get("vName")+ " " +
                            data_trip.get("vLastName"), senderImage, true);
                    break;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void showSignatureImage(String Name, String image_url, boolean isSender) {
        signatureImageDialog = new Dialog(getActContext(), R.style.Theme_Dialog);
        signatureImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signatureImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        signatureImageDialog.setContentView(R.layout.multi_show_sign_design);

        final ProgressBar LoadingProgressBar = ((ProgressBar) signatureImageDialog.findViewById(R.id.LoadingProgressBar));

        ((MTextView) signatureImageDialog.findViewById(R.id.nameTxt)).setText(" " + Name);

        if (isSender) {
            ((MTextView) signatureImageDialog.findViewById(R.id.passengerDTxt)).setText(generalFunc.retrieveLangLBl("Sender Signature", "LBL_SENDER_SIGN"));
            ((MTextView) signatureImageDialog.findViewById(R.id.nameTxt)).setVisibility(View.GONE);

        } else {
            ((MTextView) signatureImageDialog.findViewById(R.id.passengerDTxt)).setText(generalFunc.retrieveLangLBl("Receiver Signature", "LBL_RECEIVER_SIGN"));
            ((MTextView) signatureImageDialog.findViewById(R.id.nameTxt)).setVisibility(View.VISIBLE);

        }

        if (Utils.checkText(image_url)) {

            Picasso.get()
                    .load(image_url)
                    .into(((ImageView) signatureImageDialog.findViewById(R.id.passengerImgView)), new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            LoadingProgressBar.setVisibility(View.GONE);
                            ((ImageView) signatureImageDialog.findViewById(R.id.passengerImgView)).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e){
                            LoadingProgressBar.setVisibility(View.VISIBLE);
                            ((ImageView) signatureImageDialog.findViewById(R.id.passengerImgView)).setVisibility(View.GONE);

                        }
                    });
        } else {
            LoadingProgressBar.setVisibility(View.VISIBLE);
            ((ImageView) signatureImageDialog.findViewById(R.id.passengerImgView)).setVisibility(View.GONE);

        }
        (signatureImageDialog.findViewById(R.id.cancelArea)).setOnClickListener(view -> {

            if (signatureImageDialog != null) {
                signatureImageDialog.dismiss();
            }
        });

        signatureImageDialog.setCancelable(false);
        signatureImageDialog.setCanceledOnTouchOutside(false);

        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(signatureImageDialog);
        }
        signatureImageDialog.show();

    }

}
