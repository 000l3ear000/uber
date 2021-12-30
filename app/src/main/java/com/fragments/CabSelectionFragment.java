package com.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.melevicarbrasil.usuario.deliverAll.MapDelegate;
import com.general.files.AppFunctions;
import com.general.files.MapServiceApi;
import com.general.files.StopOverComparator;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.adapter.files.CabTypeAdapter;
import com.adapter.files.PoolSeatsSelectionAdapter;
import com.melevicarbrasil.usuario.CouponActivity;
import com.melevicarbrasil.usuario.FareBreakDownActivity;
import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.MyWalletActivity;
import com.melevicarbrasil.usuario.ProfilePaymentActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.RentalDetailsActivity;
import com.drawRoute.DirectionsJSONParser;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MapAnimator;
import com.general.files.StartActProcess;
import com.general.files.StopOverPointsDataParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.model.ContactModel;
import com.model.Stop_Over_Points_Data;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.anim.loader.AVLoadingIndicatorView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CabSelectionFragment extends Fragment implements CabTypeAdapter.OnItemClickList, ViewTreeObserver.OnGlobalLayoutListener, PoolSeatsSelectionAdapter.OnItemClickList, MapDelegate {

    private final static double DEFAULT_CURVE_ROUTE_CURVATURE = 0.5f;
    private final static int DEFAULT_CURVE_POINTS = 60;

    static MainActivity mainAct;
    static GeneralFunctions generalFunc;
    static MTextView payTypeTxt;
    static RadioButton cardRadioBtn;
    static ImageView payImgView;
    public MButton ride_now_btn;
    public int currentPanelDefaultStateHeight = 100;
    public String currentCabGeneralType = "";
    public CabTypeAdapter adapter;
    public ArrayList<HashMap<String, String>> cabTypeList;
    public ArrayList<HashMap<String, String>> rentalTypeList;
    public ArrayList<HashMap<String, String>> tempCabTypeList = new ArrayList<>();
    public String app_type = "Ride";
    public ImageView img_ridelater;
    //    public int isSelcted = -1;
    public boolean isclickableridebtn = false;
    public boolean isroutefound = false;
    public int selpos = 0;
    public View view = null;
    public boolean isCardValidated = true;
    public boolean isSkip = false;
    public LatLng sourceLocation = null;
    public LatLng destLocation = null;
    public Marker sourceMarker, destMarker, sourceDotMarker, destDotMarker;

    LinearLayout imageLaterarea;
    LinearLayout btnArea;
    String userProfileJson = "";
    RecyclerView carTypeRecyclerView;
    String currency_sign = "";
    boolean isKilled = false;
    LinearLayout paymentArea;
    LinearLayout promoArea;
    View payTypeSelectArea;
    String appliedPromoCode = "";
    RadioButton cashRadioBtn;
    LinearLayout casharea;
    LinearLayout cardarea;
    LinearLayout cashcardarea;
    public String distance = "";
    public String time = "";
    AVLoadingIndicatorView loaderView;
    MTextView noServiceTxt;
    boolean isCardnowselcted = false;
    boolean isCardlaterselcted = false;
    String RideDeliveryType = "";
    MTextView promoTxt;
    int i = 0;
    ExecuteWebServerUrl estimateFareTask;
    Polyline route_polyLine;
    boolean isRouteFail = false;
    int height = 0;
    int width = 0;

    MarkerOptions source_dot_option, dest_dot_option;
    String required_str = "";
    ProgressBar mProgressBar;
    androidx.appcompat.app.AlertDialog outstanding_dialog;

    CardView detailArea;

    //#UberPool
    /*UberPool Related Declaration Start*/
    public MButton confirm_seats_btn;
    public ImageView poolBackImage;
    public PoolSeatsSelectionAdapter seatsSelectionAdapter;
    public MTextView poolTxt, poolFareTxt, poolnoseatsTxt, poolnoteTxt;
    RecyclerView poolSeatsRecyclerView;
    LinearLayout cashCardArea, poolArea, mainContentArea;
    public int seatsSelectpos = 0;

    String routeDrawResponse = "";
    public ArrayList<String> poolSeatsList = new ArrayList<>();
    public boolean isPoolCabTypeIdSelected = false;
    /*UberPool Related Declaration End*/

    public ImageView rentalBackImage;
    MTextView rentalPkg;
    SelectableRoundedImageView rentPkgImage, rentBackPkgImage;
    public RelativeLayout rentalarea;
    public MTextView rentalPkgDesc;
    public static int RENTAL_REQ_CODE = 1234;
    public String iRentalPackageId = "";

    View marker_view;
    View skyport_source_marker_view;
    View skyport_dest_marker_view;
    MTextView addressTxt, etaTxt;
    ImageView mImageGreySource, mImageGreyDest;
    boolean isRental = false;
    int lstSelectpos = 0;

    public int fragmentWidth = 0;
    public int fragmentHeight = 0;

    LinearLayout organizationArea;
    MTextView organizationTxt;
    int noOfSeats = 2;
    public boolean isCardSelect = false;
    AppCompatImageView rentIconImage;


    ArrayList<Stop_Over_Points_Data> wayPointslist = new ArrayList<>();  // List of Way Points/ middle points
    ArrayList<Stop_Over_Points_Data> destPointlist = new ArrayList<>();  // destination Points
    ArrayList<Stop_Over_Points_Data> finalPointlist = new ArrayList<>();  // final Points list with time & distance & based on shortest location first algorithm
    ArrayList<Stop_Over_Points_Data> stop_Over_Points_Temp_Array = new ArrayList<Stop_Over_Points_Data>();
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private String APP_PAYMENT_MODE = "";
    private String APP_PAYMENT_METHOD = "";
    private String SYSTEM_PAYMENT_FLOW = "";


    public static void setCardSelection() {
        if (generalFunc == null) {
            generalFunc = mainAct.generalFunc;
        }

        if (generalFunc != null) {
            payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));
        }

        mainAct.setCashSelection(false);
        cardRadioBtn.setChecked(true);
        payImgView.setImageResource(R.mipmap.ic_card_new);
    }

    public static void setWalletSelection() {
        if (generalFunc == null) {
            generalFunc = mainAct.generalFunc;
        }

        payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
        mainAct.setCashSelection(false);
        cardRadioBtn.setChecked(true);
        payImgView.setImageResource(R.mipmap.ic_menu_wallet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            return view;
        }


        view = inflater.inflate(R.layout.fragment_new_cab_selection, container, false);
        mainAct = (MainActivity) getActivity();
        generalFunc = mainAct.generalFunc;

        //#UberPool
        /* Pool related views declaration started */
        rentIconImage = (AppCompatImageView) view.findViewById(R.id.rentIconImage);
        poolBackImage = (ImageView) view.findViewById(R.id.poolBackImage);
        poolFareTxt = (MTextView) view.findViewById(R.id.poolFareTxt);
        poolTxt = (MTextView) view.findViewById(R.id.poolTxt);
        poolnoseatsTxt = (MTextView) view.findViewById(R.id.poolnoseatsTxt);
        poolnoteTxt = (MTextView) view.findViewById(R.id.poolnoteTxt);
        poolSeatsRecyclerView = (RecyclerView) view.findViewById(R.id.poolSeatsRecyclerView);
        cashCardArea = (LinearLayout) view.findViewById(R.id.cashcardarea);
        poolArea = (LinearLayout) view.findViewById(R.id.poolArea);
        mainContentArea = (LinearLayout) view.findViewById(R.id.mainContentArea);
        poolBackImage.setOnClickListener(new setOnClickList());
        /* Pool related views declaration ended */

        detailArea = (CardView) view.findViewById(R.id.detailArea);

        rentalBackImage = (ImageView) view.findViewById(R.id.rentalBackImage);
        rentalarea = (RelativeLayout) view.findViewById(R.id.rentalarea);
        organizationArea = (LinearLayout) view.findViewById(R.id.organizationArea);
        rentPkgImage = (SelectableRoundedImageView) view.findViewById(R.id.rentPkgImage);
        rentBackPkgImage = (SelectableRoundedImageView) view.findViewById(R.id.rentBackPkgImage);
        rentalPkg = (MTextView) view.findViewById(R.id.rentalPkg);
        rentalPkgDesc = (MTextView) view.findViewById(R.id.rentalPkgDesc);
        organizationTxt = (MTextView) view.findViewById(R.id.organizationTxt);
        rentalBackImage.setOnClickListener(new setOnClickList());
        rentalPkg.setOnClickListener(new setOnClickList());
        rentPkgImage.setOnClickListener(new setOnClickList());
        img_ridelater = (ImageView) view.findViewById(R.id.img_ridelater);
        organizationArea.setOnClickListener(new setOnClickList());
        if (generalFunc.isRTLmode()) {
            rentalBackImage.setRotation(180);
            img_ridelater.setRotationY(180);
            poolBackImage.setRotation(180);
        }

        for (int i = 0; i < noOfSeats; i++) {
            poolSeatsList.add("" + (i + 1));
        }

        poolnoseatsTxt.setText(generalFunc.retrieveLangLBl("How Many seats do you need?", "LBL_POOL_SEATS"));
        poolnoteTxt.setText(generalFunc.retrieveLangLBl("This fare is based on our estimation. This may vary during trip and final fare.", "LBL_GENERAL_NOTE_FARE_EST"));
        poolTxt.setText(generalFunc.retrieveLangLBl("Pool", "LBL_POOL"));
        if (mainAct.eShowOnlyMoto != null && mainAct.eShowOnlyMoto.equalsIgnoreCase("Yes")) {
            rentalPkg.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_MOTO_TITLE_TXT"));
            rentalPkgDesc.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_MOTO_PKG_INFO"));
        } else if (mainAct.eFly) {
            rentalPkg.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_AIRCRAFT_TITLE_TXT"));
            rentalPkgDesc.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_AIRCRAFT_PKG_INFO"));
            rentIconImage.setImageResource(R.drawable.ic_air_freight_new);

        } else {
            rentIconImage.setImageResource(R.drawable.ic_sedan_car_front);
            rentalPkg.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_A_CAR"));
            rentalPkgDesc.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_PKG_INFO"));
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        height = displayMetrics.heightPixels - Utils.dpToPx(getActContext(), 300);

        ride_now_btn = ((MaterialRippleLayout) view.findViewById(R.id.ride_now_btn)).getChildView();

        confirm_seats_btn = ((MaterialRippleLayout) view.findViewById(R.id.confirm_seats_btn)).getChildView();
        ride_now_btn.setId(Utils.generateViewId());
        confirm_seats_btn.setId(Utils.generateViewId());
        confirm_seats_btn.setAllCaps(true);

        mProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                getActContext().getResources().getColor(R.color.appThemeColor_2), android.graphics.PorterDuff.Mode.SRC_IN);
        findRoute("--");
        RideDeliveryType = getArguments().getString("RideDeliveryType");

        carTypeRecyclerView = (RecyclerView) view.findViewById(R.id.carTypeRecyclerView);
        loaderView = (AVLoadingIndicatorView) view.findViewById(R.id.loaderView);
        payTypeSelectArea = view.findViewById(R.id.payTypeSelectArea);
        payTypeTxt = (MTextView) view.findViewById(R.id.payTypeTxt);
        promoTxt = (MTextView) view.findViewById(R.id.promoTxt);
        promoTxt.setText(generalFunc.retrieveLangLBl("", "LBL_COUPON"));


        imageLaterarea = (LinearLayout) view.findViewById(R.id.imageLaterarea);
        btnArea = (LinearLayout) view.findViewById(R.id.btnArea);
        noServiceTxt = (MTextView) view.findViewById(R.id.noServiceTxt);


        casharea = (LinearLayout) view.findViewById(R.id.casharea);
        cardarea = (LinearLayout) view.findViewById(R.id.cardarea);

        casharea.setOnClickListener(new setOnClickList());
        cardarea.setOnClickListener(new setOnClickList());

        img_ridelater.setOnClickListener(new setOnClickList());

        if (generalFunc.getJsonValueStr("ENABLE_CORPORATE_PROFILE", mainAct.obj_userProfile).equalsIgnoreCase("Yes") && mainAct.getCurrentCabGeneralType().equalsIgnoreCase("Ride")) {
            organizationArea.setVisibility(View.VISIBLE);
            organizationTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PERSONAL"));
            LinearLayout.LayoutParams organizationLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            organizationLayoutParams.setMargins(0, 0, 0, -Utils.dpToPx(getActContext(), 5));
            organizationArea.setLayoutParams(organizationLayoutParams);
        }


        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 14), 1,
                getActContext().getResources().getColor(R.color.gray), rentBackPkgImage);
        new CreateRoundedView(getActContext().getResources().getColor(R.color.appThemeColor_1), Utils.dipToPixels(getActContext(), 12), 0,
                getActContext().getResources().getColor(R.color.appThemeColor_2), rentPkgImage);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_car);
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        d.setColorFilter(getActContext().getResources().getColor(R.color.appThemeColor_TXT_1), PorterDuff.Mode.MULTIPLY);
        rentPkgImage.setImageDrawable(d);

        paymentArea = (LinearLayout) view.findViewById(R.id.paymentArea);
        promoArea = (LinearLayout) view.findViewById(R.id.promoArea);
        promoArea.setOnClickListener(new setOnClickList());
        paymentArea.setOnClickListener(new setOnClickList());
        cashRadioBtn = (RadioButton) view.findViewById(R.id.cashRadioBtn);
        cardRadioBtn = (RadioButton) view.findViewById(R.id.cardRadioBtn);

        payImgView = (ImageView) view.findViewById(R.id.payImgView);

        cashcardarea = (LinearLayout) view.findViewById(R.id.cashcardarea);

        getUserProfileJson(mainAct.userProfileJson);

        currency_sign = generalFunc.getJsonValue("CurrencySymbol", userProfileJson);
        app_type = generalFunc.getJsonValue("APP_TYPE", userProfileJson);

        showBookingLaterArea();

        if (mainAct.isDeliver(mainAct.getCurrentCabGeneralType())) {
            img_ridelater.setImageResource(R.drawable.ic_delivery_later);
        }

        if (mainAct.eFly) {
            img_ridelater.setImageResource(R.drawable.ic_calendar_later);
        }

        if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
            app_type = "Ride";
        }

        if (app_type.equals(Utils.CabGeneralType_UberX)) {
            view.setVisibility(View.GONE);
            return view;
        }

        isKilled = false;

        if (mainAct.isMultiDelivery()) {

         /*  FrameLayout.LayoutParams params = new
                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // Set the height by params

//            params.height = Utils.dpToPx(getActContext(), 118);
            params.height = (int) getActContext().getResources().getDimension(R.dimen._100sdp);
            carTypeRecyclerView.setLayoutParams(params);*/
            // set height of RecyclerView*/

            cashcardarea.setVisibility(View.GONE);
            detailArea.setCardBackgroundColor(Color.parseColor("#ffffff"));
            // set margin top for higher devices

            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    getActContext().getResources().getDimension(R.dimen._10sdp),
                    getActContext().getResources().getDisplayMetrics()
            );
            int px1 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    getActContext().getResources().getDimension(R.dimen._3sdp),
                    getActContext().getResources().getDisplayMetrics()
            );

            ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) view.findViewById(R.id.sendRequestArea).getLayoutParams();
            // params1.bottomMargin = px;
            params1.topMargin = px1;

          /*  LinearLayout.LayoutParams  params1= (LinearLayout.LayoutParams) view.findViewById(R.id.sendRequestArea).getLayoutParams();
            params1.bottomMargin = px;
            params1.topMargin = px;*/

        } else {
            detailArea.setCardBackgroundColor(Color.parseColor("#f1f1f1"));

            cashRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
            cardRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));
            setCashSelection();
            if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash")) {
                cashRadioBtn.setVisibility(View.VISIBLE);
                cardRadioBtn.setVisibility(View.GONE);
            } else if (APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
                cashRadioBtn.setVisibility(View.GONE);
                cardRadioBtn.setVisibility(View.VISIBLE);
                isCardValidated = true;
                if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    setCardSelection();
                } else {
                    setWalletSelection();
                }
                isCardValidated = false;
            } else if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                cardRadioBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CARD"));
                cashRadioBtn.setVisibility(View.VISIBLE);
                cardRadioBtn.setVisibility(View.VISIBLE);
            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                cardRadioBtn.setText(generalFunc.retrieveLangLBl("Pay by Wallet", "LBL_PAY_BY_WALLET_TXT") + "(" + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson) + ")"));
                cashRadioBtn.setVisibility(View.VISIBLE);
                cardRadioBtn.setVisibility(View.VISIBLE);
            }
        }


        setLabels(true);

        ride_now_btn.setOnClickListener(new setOnClickList());
        confirm_seats_btn.setOnClickListener(new setOnClickList());


        configRideLaterBtnArea(false);

        addGlobalLayoutListner();

        seatsSelectionAdapter = new PoolSeatsSelectionAdapter(getActContext(), poolSeatsList, generalFunc);
        seatsSelectionAdapter.setSelectedSeat(seatsSelectpos);
        poolSeatsRecyclerView.setAdapter(seatsSelectionAdapter);
        seatsSelectionAdapter.notifyDataSetChanged();
        seatsSelectionAdapter.setOnItemClickList(this);

        return view;
    }

    private void showBookingLaterArea() {
        if (generalFunc.getJsonValue("RIDE_LATER_BOOKING_ENABLED", userProfileJson).equalsIgnoreCase("Yes") && !mainAct.isMultiDelivery()) {

            if (isPoolCabTypeIdSelected) {
                showRideLaterBtn(false);
            } else if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) && !mainAct.iscubejekRental) {
                showRideLaterBtn(true);
            } else if (mainAct.iscubejekRental || mainAct.isRental) {
                showRideLaterBtn(false);
            } else {
                showRideLaterBtn(true);
            }

        } else {
            showRideLaterBtn(false);

        }

    }

    private void showRideLaterBtn(boolean show) {
        imageLaterarea.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        addGlobalLayoutListner();
    }

    private void addGlobalLayoutListner() {

        if (getView() != null) {
            getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        if (view != null) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        if (getView() != null) {

            getView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        } else if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }

    public void showLoader() {
        try {
            loaderView.setVisibility(View.VISIBLE);
            closeNoServiceText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNoServiceText() {
        noServiceTxt.setVisibility(View.VISIBLE);
    }

    public void closeNoServiceText() {
        noServiceTxt.setVisibility(View.GONE);
    }

    public void closeLoader() {
        try {
            loaderView.setVisibility(View.GONE);
            if (mainAct.cabTypesArrList.size() == 0) {
                showNoServiceText();
            } else {
                closeNoServiceText();
            }
        } catch (Exception e) {

        }
    }

    public void setUserProfileJson() {
        getUserProfileJson(mainAct.userProfileJson);
    }

    public void getUserProfileJson(String userProfileJsonStr) {
        userProfileJson = userProfileJsonStr;
        APP_PAYMENT_MODE = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson);
        APP_PAYMENT_METHOD = generalFunc.getJsonValue("APP_PAYMENT_METHOD", userProfileJson);
        SYSTEM_PAYMENT_FLOW = generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson);
    }

    public void checkCardConfig() {
        setUserProfileJson();


        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                mainAct.OpenCardPaymentAct(true);
            } else {
                showPaymentBox(false, false, "", new Intent());
            }
        }

    }

    public void checkCardConfig(boolean isOutstanding, boolean isReqNow, String responseString, Intent data) {
        setUserProfileJson();

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                mainAct.OpenCardPaymentAct(true);
            } else {
                showPaymentBox(isOutstanding, isReqNow, responseString, data);
            }
        }

    }

    public void showPaymentBox(boolean isOutstanding, boolean isReqNow, String responseString, Intent data) {
        androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.input_box_view, null);
        builder.setView(dialogView);

        final MaterialEditText input = (MaterialEditText) dialogView.findViewById(R.id.editBox);
        final MTextView subTitleTxt = (MTextView) dialogView.findViewById(R.id.subTitleTxt);

        Utils.removeInput(input);

        subTitleTxt.setVisibility(View.VISIBLE);
        subTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TITLE_PAYMENT_ALERT"));
        input.setText(generalFunc.getJsonValue("vCreditCard", userProfileJson));

        builder.setPositiveButton(generalFunc.retrieveLangLBl("Confirm", "LBL_BTN_TRIP_CANCEL_CONFIRM_TXT"), (dialog, which) -> {
            dialog.cancel();
            if (isOutstanding) {
                callOutStandingPayAmout(responseString, data);
            } else {
                checkPaymentCard();
            }
        });
        builder.setNeutralButton(generalFunc.retrieveLangLBl("Change", "LBL_CHANGE"), (dialog, which) -> {
            dialog.cancel();
            mainAct.OpenCardPaymentAct(true);
            //ridelaterclick = false;

        });
        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> {
            dialog.cancel();
            //ridelaterclick = false;

        });


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void setCashSelection() {
        isCardSelect = false;

        payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));

        isCardValidated = false;
        mainAct.setCashSelection(true);
        cashRadioBtn.setChecked(true);

        payImgView.setImageResource(R.drawable.ic_money_cash);
    }

    public void checkPaymentCard() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "CheckCard");
        parameters.put("iUserId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);
                if (action.equals("1")) {

                    if (mainAct.pickUpLocation == null) {
                        return;
                    }
                    isCardValidated = true;
                    setCardSelection();

                    if (isCardnowselcted) {
                        isCardnowselcted = false;


                        if (mainAct.isDeliver(mainAct.getCurrentCabGeneralType())) {
                            if (!mainAct.getDestinationStatus() && !mainAct.isMultiDelivery()) {
                                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please add your destination location " +
                                        "to deliver your package.", "LBL_ADD_DEST_MSG_DELIVER_ITEM"));
                                return;
                            }
                            mainAct.continueDeliveryProcess();
                            return;
                        } else {
                            if (!mainAct.getCabReqType().equals(Utils.CabReqType_Later)) {

                                if (cabTypeList.get(selpos).get("eRental") != null && !cabTypeList.get(selpos).get("eRental").equalsIgnoreCase("") &&
                                        cabTypeList.get(selpos).get("eRental").equalsIgnoreCase("Yes")) {

                                    Bundle bn = new Bundle();
                                    bn.putString("address", mainAct.pickUpLocationAddress);
                                    bn.putString("vVehicleType", cabTypeList.get(selpos).get("vRentalVehicleTypeName"));
                                    bn.putString("iVehicleTypeId", cabTypeList.get(selpos).get("iVehicleTypeId"));
                                    bn.putString("vLogo", cabTypeList.get(selpos).get("vLogo1"));
                                    bn.putString("eta", etaTxt.getText().toString());
                                    bn.putString("eMoto", mainAct.eShowOnlyMoto);
                                    bn.putString("PromoCode", appliedPromoCode);
                                    bn.putBoolean("eFly", mainAct.eFly);


                                    new StartActProcess(getActContext()).startActForResult(
                                            RentalDetailsActivity.class, bn, RENTAL_REQ_CODE);
                                    return;

                                }
                                mainAct.continuePickUpProcess();
                            } else {
                                mainAct.setRideSchedule();
                            }

                        }
                    }

                    if (isCardlaterselcted) {
                        isCardlaterselcted = false;
                        mainAct.chooseDateTime();
                    }

                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void setLabels(boolean isCallGenerateType) {

        if (mainAct == null) {
            return;
        }

        if (generalFunc == null) {
            generalFunc = mainAct.generalFunc;
        }

        if (generalFunc == null) {
            return;
        }

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        noServiceTxt.setText(mainAct.eFly ? generalFunc.retrieveLangLBl("No rides for these locations.", "LBL_FLY_NO_VEHICLES") : generalFunc.retrieveLangLBl("service not available in this location", "LBL_NO_SERVICE_AVAILABLE_TXT"));

        if (mainAct.currentLoadedDriverList == null || mainAct.currentLoadedDriverList.size() < 1) {
            ride_now_btn.setText(generalFunc.retrieveLangLBl("No Car available.", "LBL_NO_CARS"));
            if (isCallGenerateType) {
                generateCarType();
            }
            return;
        }


        if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
            currentCabGeneralType = Utils.CabGeneralType_UberX;
        } else {
            String type = mainAct.isDeliver(app_type) || mainAct.isDeliver(RideDeliveryType) ? "Deliver" : Utils.CabGeneralType_Ride;
            if (type.equals("Deliver")) {
                if (mainAct.getCabReqType().equals(Utils.CabReqType_Now)) {
                    ride_now_btn.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
                } else if (mainAct.getCabReqType().equals(Utils.CabReqType_Later)) {
                    ride_now_btn.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
                }
            } else {
                ride_now_btn.setText(generalFunc.retrieveLangLBl("Request Now", "LBL_REQUEST_NOW"));
            }


            if (generalFunc.getSelectedCarTypeData(mainAct.getSelectedCabTypeId(), mainAct.cabTypesArrList, "ePoolStatus").equalsIgnoreCase("Yes")) {
                ride_now_btn.setText(generalFunc.retrieveLangLBl("", "LBL_CONFIRM_SEATS"));

            }

        }
        confirm_seats_btn.setText(generalFunc.retrieveLangLBl("Confirm Seats", "LBL_CONFIRM_SEATS"));
        if (isCallGenerateType) {
            generateCarType();
        }

    }

    public void releaseResources() {
        isKilled = true;
    }

    public void changeCabGeneralType(String currentCabGeneralType) {
        this.currentCabGeneralType = currentCabGeneralType;
    }

    public String getCurrentCabGeneralType() {
        return this.currentCabGeneralType;
    }

    public void configRideLaterBtnArea(boolean isGone) {
        if (mainAct.isMultiDelivery()) {
            mainAct.setPanelHeight(185);
            currentPanelDefaultStateHeight = 185;
        } else {
            if (isGone || app_type.equalsIgnoreCase("Ride-Delivery")) {
                mainAct.setPanelHeight(237);
                if (!app_type.equalsIgnoreCase("Ride-Delivery")) {
                    mainAct.setUserLocImgBtnMargin(105);
                }
                return;
            }
            if (!generalFunc.getJsonValue("RIIDE_LATER", userProfileJson).equalsIgnoreCase("YES") && !app_type.equalsIgnoreCase("Ride-Delivery")) {
                mainAct.setUserLocImgBtnMargin(105);
                mainAct.setPanelHeight(237);
            } else {


                mainAct.setPanelHeight(237);
                currentPanelDefaultStateHeight = 237;
                mainAct.setUserLocImgBtnMargin(164);
            }
        }
    }

    public void generateCarType() {
        if (getActContext() == null) {
            return;
        }

        if (cabTypeList == null) {
            cabTypeList = new ArrayList<>();
            rentalTypeList = new ArrayList<>();
            if (adapter == null) {
                adapter = new CabTypeAdapter(getActContext(), cabTypeList, generalFunc);
                adapter.setSelectedVehicleTypeId(mainAct.getSelectedCabTypeId());
                adapter.isMultiDelivery(mainAct.isMultiDelivery());
                carTypeRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickList(this);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            cabTypeList.clear();
            rentalTypeList.clear();
        }

        if (mainAct.isDeliver(currentCabGeneralType)) {
            this.currentCabGeneralType = "Deliver";
        }

        String APP_TYPE = generalFunc.getJsonValue("APP_TYPE", userProfileJson);
        for (int i = 0; i < mainAct.cabTypesArrList.size(); i++) {

            HashMap<String, String> map = new HashMap<>();
            String iVehicleTypeId = mainAct.cabTypesArrList.get(i).get("iVehicleTypeId");

            String vVehicleType = mainAct.cabTypesArrList.get(i).get("vVehicleType");
            String vRentalVehicleTypeName = mainAct.cabTypesArrList.get(i).get("vRentalVehicleTypeName");
            String fPricePerKM = mainAct.cabTypesArrList.get(i).get("fPricePerKM");
            String fPricePerMin = mainAct.cabTypesArrList.get(i).get("fPricePerMin");
            String iBaseFare = mainAct.cabTypesArrList.get(i).get("iBaseFare");
            String fCommision = mainAct.cabTypesArrList.get(i).get("fCommision");
            String iPersonSize = mainAct.cabTypesArrList.get(i).get("iPersonSize");
            String vLogo = mainAct.cabTypesArrList.get(i).get("vLogo");
            String vLogo1 = mainAct.cabTypesArrList.get(i).get("vLogo1");
            String eType = mainAct.cabTypesArrList.get(i).get("eType");
            String fPoolPercentage = mainAct.cabTypesArrList.get(i).get("fPoolPercentage");

            String eRental = mainAct.cabTypesArrList.get(i).get("eRental");
            String ePoolStatus = mainAct.cabTypesArrList.get(i).get("ePoolStatus");

            if (!eType.equalsIgnoreCase(currentCabGeneralType)) {
                continue;
            }

            map.put("iVehicleTypeId", iVehicleTypeId);
            map.put("vVehicleType", vVehicleType);
            map.put("vRentalVehicleTypeName", vRentalVehicleTypeName);
            map.put("fPricePerKM", fPricePerKM);
            map.put("fPricePerMin", fPricePerMin);
            map.put("iBaseFare", iBaseFare);
            map.put("fCommision", fCommision);
            map.put("iPersonSize", iPersonSize);
            map.put("vLogo", vLogo);
            map.put("vLogo1", vLogo1);
            map.put("fPoolPercentage", fPoolPercentage);
            //#UberPool Change
            map.put("ePoolStatus", ePoolStatus);

            if (((APP_TYPE.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) || (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) || (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralType_Ride))) && (mainAct.iscubejekRental || mainAct.isRental)) {
                map.put("eRental", eRental);
            } else {
                map.put("eRental", "No");
            }

            if (i == 0) {
                adapter.setSelectedVehicleTypeId(iVehicleTypeId);
            }

            cabTypeList.add(map);

            if (eRental != null && eRental.equalsIgnoreCase("Yes")) {
                HashMap<String, String> rentalmap = (HashMap<String, String>) map.clone();
                rentalmap.put("eRental", "Yes");
                rentalTypeList.add(rentalmap);
            }
        }


        manageRentalArea();

        mainAct.setCabTypeList(cabTypeList);
        adapter.notifyDataSetChanged();

        if (cabTypeList.size() > 0) {
            onItemClick(0);
        }
    }

    public void manageRentalArea() {
        if (rentalarea != null && rentalBackImage != null && mainAct.isMultiStopOverEnabled()) {
            // Show or Hide Rental - if MultiStop Over Added
            int rentalArea = rentalarea.getVisibility();
            int rentalBackImgArea = rentalBackImage.getVisibility();

            if (mainAct.stopOverPointsList.size() > 2) {
                hideRentalArea();
            } else if ((rentalArea == View.GONE && rentalBackImgArea == View.GONE)) {
                showRentalArea();
            }
        } else {
            showRentalArea();
        }

    }

    public void showRentalArea() {
        if (mainAct != null && !mainAct.iscubejekRental) {
            if (rentalTypeList != null && rentalTypeList.size() > 0) {
                String APP_TYPE = generalFunc.getJsonValue("APP_TYPE", userProfileJson);

                if (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralType_Ride) || APP_TYPE.equalsIgnoreCase("Ride-Delivery") || (RideDeliveryType.equalsIgnoreCase(Utils.CabGeneralType_Ride) && !mainAct.iscubejekRental)) {
                    rentalPkg.setVisibility(View.VISIBLE);
                    rentalarea.setVisibility(View.VISIBLE);
                    //rentPkgImage.setVisibility(View.VISIBLE);
                    //rentBackPkgImage.setVisibility(View.VISIBLE);


                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                mainAct.setPanelHeight(280);
                                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (mainAct.userLocBtnImgView).getLayoutParams();
                                params.bottomMargin = Utils.dipToPixels(getActContext(), 300);
*/
                            } catch (Exception e2) {

                                new Handler().postDelayed(this, 20);
                            }
                        }
                    };
                    new Handler().postDelayed(r, 20);
                    setShadow();
                }

            }
        }
    }

    public void closeLoadernTxt() {
        loaderView.setVisibility(View.GONE);
        closeNoServiceText();

    }

    public void setShadow() {
        (view.findViewById(R.id.shadowView)).setVisibility(View.GONE);
    }

    public Context getActContext() {
        return mainAct!=null?mainAct.getActContext():getActivity();
    }

    @Override
    public void onItemClick(int position) {

        String iVehicleTypeId = cabTypeList.get(position).get("iVehicleTypeId");
        String ePoolStatus = cabTypeList.get(position).get("ePoolStatus");

        if (ePoolStatus.equalsIgnoreCase("Yes") && mainAct.stopOverPointsList.size() > 2) {
            generalFunc.showMessage(carTypeRecyclerView, generalFunc.retrieveLangLBl("", "LBL_REMOVE_MULTI_STOP_OVER_TXT"));
            return;
        }


        selpos = position;

        isPoolCabTypeIdSelected = ePoolStatus.equalsIgnoreCase("Yes");
        mainAct.isPoolCabTypeIdSelected = isPoolCabTypeIdSelected;

        showBookingLaterArea();

        if (!iVehicleTypeId.equals(mainAct.getSelectedCabTypeId())) {
            mainAct.selectedCabTypeId = iVehicleTypeId;
            adapter.setSelectedVehicleTypeId(iVehicleTypeId);
            adapter.notifyDataSetChanged();
            mainAct.changeCabType(iVehicleTypeId);

            if (cabTypeList.get(position).get("eFlatTrip") != null &&
                    (!cabTypeList.get(position).get("eFlatTrip").equalsIgnoreCase(""))
                    && cabTypeList.get(position).get("eFlatTrip").equalsIgnoreCase("Yes")) {
                mainAct.isFixFare = true;
            } else {
                mainAct.isFixFare = false;
            }
            //#UberPool Change
            if (ePoolStatus.equalsIgnoreCase("Yes")) {
                mainAct.loadAvailCabs.checkAvailableCabs();
                ride_now_btn.setText(generalFunc.retrieveLangLBl("", "LBL_CONFIRM_SEATS"));
            } else {
                String type = mainAct.isDeliver(app_type) || mainAct.isDeliver(RideDeliveryType) ? "Deliver" : Utils.CabGeneralType_Ride;
                if (type.equals("Deliver")) {
                    if (mainAct.getCabReqType().equals(Utils.CabReqType_Now)) {
                        ride_now_btn.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
                    } else if (mainAct.getCabReqType().equals(Utils.CabReqType_Later)) {
                        ride_now_btn.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
                    }
                } else {
                    ride_now_btn.setText(generalFunc.retrieveLangLBl("Request Now", "LBL_REQUEST_NOW"));
                }

            }
        } else {
            openFareDetailsDilaog(position);
        }

        poolFareTxt.setText(cabTypeList.get(position).get("total_fare"));

        if (isPoolCabTypeIdSelected) {
            if (Utils.checkText(routeDrawResponse)) {
                routeDraw();
            } else {
                findRoute("--");
            }
        } else {
            if (routeDrawResponse != null && !routeDrawResponse.equalsIgnoreCase("")) {
                PolylineOptions lineOptions = null;

                if ((isPoolCabTypeIdSelected || mainAct.eFly) && sourceLocation != null && destLocation != null) {
                    lineOptions = createCurveRoute(new LatLng(sourceLocation.latitude, sourceLocation.longitude), new LatLng(destLocation.latitude, destLocation.longitude));

                } else if (!isPoolCabTypeIdSelected && Utils.checkText(routeDrawResponse)) {


                    if (mainAct.stopOverPointsList.size() > 2) {
                        lineOptions = getGoogleRouteOptions(routeDrawResponse, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black), getActContext(), mainAct.stopOverPointsList, wayPointslist, destPointlist, finalPointlist, mainAct.getMap(), builder, isGoogle);

                    } else {
                        lineOptions = getGoogleRouteOptions(routeDrawResponse, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(android.R.color.black), isGoogle);
                    }

                }


                if (lineOptions != null) {
                    if (route_polyLine != null) {
                        route_polyLine.remove();
                        route_polyLine = null;

                    }
                    route_polyLine = mainAct.getMap().addPolyline(lineOptions);
                    route_polyLine.remove();
                }


                DisplayMetrics metrics = new DisplayMetrics();
                mainAct.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                if (isSkip) {
                    MapAnimator.getInstance().stopRouteAnim();
                    if (route_polyLine != null) {
                        route_polyLine.remove();
                        route_polyLine = null;
                    }
                    return;
                }
                if (route_polyLine != null && route_polyLine.getPoints().size() > 1) {
                    MapAnimator.getInstance().animateRoute(mainAct.getMap(), route_polyLine.getPoints(), getActContext());
                }
            }
        }


    }

    public void routeDraw() {
        if (isSkip) {
            if (route_polyLine != null) {
                route_polyLine.remove();
                route_polyLine = null;
            }
            return;
        }
        PolylineOptions lineOptions = null;

        if ((isPoolCabTypeIdSelected || mainAct.eFly) && sourceLocation != null && destLocation != null) {
            lineOptions = createCurveRoute(new LatLng(sourceLocation.latitude, sourceLocation.longitude), new LatLng(destLocation.latitude, destLocation.longitude));

        } else if (!isPoolCabTypeIdSelected && Utils.checkText(routeDrawResponse)) {

            if (mainAct.stopOverPointsList.size() > 2) {
                lineOptions = getGoogleRouteOptions(routeDrawResponse, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black), getActContext(), mainAct.stopOverPointsList, wayPointslist, destPointlist, finalPointlist, mainAct.getMap(), builder, isGoogle);

            } else {
                lineOptions = getGoogleRouteOptions(routeDrawResponse, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(android.R.color.black), isGoogle);
            }


        }

        if (lineOptions != null) {
            if (route_polyLine != null) {
                route_polyLine.remove();
                route_polyLine = null;
            }

            if (MapAnimator.getInstance() != null) {
                MapAnimator.getInstance().stopRouteAnim();
            }

            //Draw polyline
            route_polyLine = mainAct.getMap().addPolyline(lineOptions);

            if (isPoolCabTypeIdSelected) {
                route_polyLine.setColor(Color.parseColor("#cecece"));
                route_polyLine.setStartCap(new RoundCap());
                route_polyLine.setEndCap(new RoundCap());
            }


            if (route_polyLine != null && route_polyLine.getPoints().size() > 1) {
                MapAnimator.getInstance().animateRoute(mainAct.getMap(), route_polyLine.getPoints(), getActContext());
            }

            route_polyLine.remove();
        }

    }

    public void openFareEstimateDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");

        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.fare_detail_design, null);
        builder.setView(dialogView);

        ((MTextView) dialogView.findViewById(R.id.fareDetailHTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_FARE_DETAIL_TXT"));
        ((MTextView) dialogView.findViewById(R.id.baseFareHTxt)).setText(" " + generalFunc.retrieveLangLBl("", "LBL_BASE_FARE_TXT"));
        ((MTextView) dialogView.findViewById(R.id.parMinHTxt)).setText(" / " + generalFunc.retrieveLangLBl("", "LBL_MIN_TXT"));
        ((MTextView) dialogView.findViewById(R.id.parMinHTxt)).setVisibility(View.GONE);
        ((MTextView) dialogView.findViewById(R.id.andTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_AND_TXT"));
        ((MTextView) dialogView.findViewById(R.id.parKmHTxt)).setText(" / " + generalFunc.retrieveLangLBl("", "LBL_KM_TXT"));
        ((MTextView) dialogView.findViewById(R.id.parKmHTxt)).setVisibility(View.GONE);

        ((MTextView) dialogView.findViewById(R.id.baseFareVTxt)).setText(currency_sign + " " +
                generalFunc.getSelectedCarTypeData(mainAct.getSelectedCabTypeId(), mainAct.cabTypesArrList, "iBaseFare"));

        ((MTextView) dialogView.findViewById(R.id.parMinVTxt)).setText(currency_sign + " " +
                generalFunc.getSelectedCarTypeData(mainAct.getSelectedCabTypeId(), mainAct.cabTypesArrList, "fPricePerMin") + " / " + generalFunc.retrieveLangLBl("", "LBL_MIN_TXT"));

        ((MTextView) dialogView.findViewById(R.id.parKmVTxt)).setText(currency_sign + " " +
                generalFunc.getSelectedCarTypeData(mainAct.getSelectedCabTypeId(), mainAct.cabTypesArrList, "fPricePerKM") + " / " + generalFunc.retrieveLangLBl("", "LBL_KM_TXT"));

        builder.show();
    }

    public void hidePayTypeSelectionArea() {
        payTypeSelectArea.setVisibility(View.GONE);
        cashcardarea.setVisibility(View.VISIBLE);
        mainAct.setPanelHeight(240 - 10);

        if (!mainAct.iscubejekRental) {

            if (rentalTypeList.size() > 0) {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            mainAct.setPanelHeight(290 - 10);

                        } catch (Exception e2) {
                            new Handler().postDelayed(this, 20);
                        }
                    }
                };
                new Handler().postDelayed(r, 20);
            }
        }
    }

    public void buildNoCabMessage(String message, String positiveBtn) {

        if (mainAct.noCabAvailAlertBox != null) {
            mainAct.noCabAvailAlertBox.closeAlertBox();
            mainAct.noCabAvailAlertBox = null;
        }

        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(true);
        generateAlert.setBtnClickList(btn_id -> generateAlert.closeAlertBox());
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(positiveBtn);
        generateAlert.showAlertBox();

        mainAct.noCabAvailAlertBox = generateAlert;
    }

    public String getAppliedPromoCode() {
        return this.appliedPromoCode;
    }

    public void findRoute(String etaVal) {

        if (mainAct != null && mainAct.isMultiDelivery()) {
            if (mainAct.getMap() != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude()))
                        .zoom(Utils.defaultZomLevel).build();
                mainAct.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            if (mainAct.loadAvailCabs != null) {
                mainAct.loadAvailCabs.changeCabs();
            }

            return;
        }


        try {

            String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
            String parameters = "";
            ArrayList<String> data_waypoints = new ArrayList<>();
            HashMap<String, String> hashMap = new HashMap<>();

            if (mainAct.stopOverPointsList.size() > 2 && mainAct.isMultiStopOverEnabled()) {
                // Origin of route
                String str_origin = "origin=" + mainAct.stopOverPointsList.get(0).getDestLat() + "," + mainAct.stopOverPointsList.get(0).getDestLong();


                String str_dest = "";
                String waypoints = "";

                wayPointslist = new ArrayList<>();      // List of Way Points
                destPointlist = new ArrayList<>();      // destination Points
                finalPointlist = new ArrayList<>();     // final Points list with time & distance & based on shortest location first algorithm
                stop_Over_Points_Temp_Array = new ArrayList<>(); // temp list of all points

                stop_Over_Points_Temp_Array = new ArrayList<Stop_Over_Points_Data>(mainAct.stopOverPointsList.subList(1, mainAct.stopOverPointsList.size()));

                // Retrive middle & destination points

                if (stop_Over_Points_Temp_Array.size() > 0) {
                    String lastAddress = "";
                    for (int i = 0; i < stop_Over_Points_Temp_Array.size(); i++) {

                        if (i == stop_Over_Points_Temp_Array.size() - 1) {
                            str_dest = "destination=" + stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLat() + "," + stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLong();
                            hashMap.put("d_latitude", stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLat() + "");
                            hashMap.put("d_longitude", stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLong() + "");
                            stop_Over_Points_Temp_Array.get(i).setDestination(true);
                            destPointlist.add(stop_Over_Points_Temp_Array.get(i));
                        } else if (i == stop_Over_Points_Temp_Array.size() - 2) {
                            wayPointslist.add(stop_Over_Points_Temp_Array.get(i));
                            lastAddress = stop_Over_Points_Temp_Array.get(i).getDestLat() + "," + stop_Over_Points_Temp_Array.get(i).getDestLong();
                            data_waypoints.add(stop_Over_Points_Temp_Array.get(i).getDestLat() + "," + stop_Over_Points_Temp_Array.get(i).getDestLong());

                        } else {
                            wayPointslist.add(stop_Over_Points_Temp_Array.get(i));
                            waypoints = waypoints + stop_Over_Points_Temp_Array.get(i).getDestLat() + "," + stop_Over_Points_Temp_Array.get(i).getDestLong() + "|";

                            data_waypoints.add(stop_Over_Points_Temp_Array.get(i).getDestLat() + "," + stop_Over_Points_Temp_Array.get(i).getDestLong());
                        }

                    }
                    waypoints = "waypoints=optimize:true|" + waypoints + lastAddress;

                } else {
                    str_dest = "destination=" + stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLat() + "," + stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLong();
                    hashMap.put("d_latitude", stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLat() + "");
                    hashMap.put("d_longitude", stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLong() + "");
                    destPointlist.add(stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1));
                }

                // Building the parameters to the web service
                if (stop_Over_Points_Temp_Array.size() > 1) {
                    parameters = str_origin + "&" + str_dest + "&" + waypoints;
                    // data_waypoints.add(stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLat() + "," + stop_Over_Points_Temp_Array.get(stop_Over_Points_Temp_Array.size() - 1).getDestLong());

                    hashMap.put("s_latitude", mainAct.stopOverPointsList.get(0).getDestLat() + "");
                    hashMap.put("s_longitude", mainAct.stopOverPointsList.get(0).getDestLong() + "");


                } else {
                    parameters = str_origin + "&" + str_dest;

                    hashMap.put("s_latitude", mainAct.stopOverPointsList.get(0).getDestLat() + "");
                    hashMap.put("s_longitude", mainAct.stopOverPointsList.get(0).getDestLong() + "");
                    hashMap.put("d_latitude", mainAct.stopOverPointsList.get(0).getDestLat() + "");
                    hashMap.put("d_longitude", mainAct.stopOverPointsList.get(0).getDestLong() + "");

                }

            } else {

                String originLoc = mainAct.getPickUpLocation().getLatitude() + "," + mainAct.getPickUpLocation().getLongitude();
                String destLoc = null;
                if (mainAct.destLocation != null) {
                    destLoc = mainAct.destLocation.getLatitude() + "," + mainAct.destLocation.getLongitude();
                    hashMap.put("d_latitude", mainAct.destLocation.getLatitude() + "");
                    hashMap.put("d_longitude", mainAct.destLocation.getLongitude() + "");

                    if (mainAct.destLocation.getLatitude() == 0.0) {
                        hashMap.put("d_latitude", mainAct.getPickUpLocation().getLatitude() + "");
                        hashMap.put("d_longitude", mainAct.getPickUpLocation().getLongitude() + "");

                    }


                } else {
                    destLoc = mainAct.getPickUpLocation().getLatitude() + "," + mainAct.getPickUpLocation().getLongitude();
                    hashMap.put("d_latitude", mainAct.getPickUpLocation().getLatitude() + "");
                    hashMap.put("d_longitude", mainAct.getPickUpLocation().getLongitude() + "");

                }

                hashMap.put("s_latitude", mainAct.getPickUpLocation().getLatitude() + "");
                hashMap.put("s_longitude", mainAct.getPickUpLocation().getLongitude() + "");


                parameters = "origin=" + originLoc + "&destination=" + destLoc;

            }

            String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=" + serverKey + "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true";


            hashMap.put("parameters", parameters);
            hashMap.put("waypoints", data_waypoints.toString());

            mProgressBar.setIndeterminate(true);
            mProgressBar.setVisibility(View.VISIBLE);
            MapServiceApi.getDirectionservice(getActContext(), hashMap, this, data_waypoints,false);


            //  mProgressBar.setIndeterminate(true);
            //  mProgressBar.setVisibility(View.VISIBLE);


//            Logger.d("Api", "directUrl_value True");
//
//            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
//            exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
//                @Override
//                public void setResponse(String responseString) {
//
//                    mProgressBar.setIndeterminate(false);
//                    mProgressBar.setVisibility(mainAct.isMultiDelivery() ? View.GONE : View.INVISIBLE);
//
//                    if (responseString != null && !responseString.equals("")) {
//
//                        String status = generalFunc.getJsonValue("status", responseString);
//
//                        if (status.equals("OK")) {
//                            isRouteFail = false;
//                            JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
//                            if (obj_routes != null && obj_routes.length() > 0) {
//
//
//                                if (mainAct.stopOverPointsList.size() > 2 && mainAct.isMultiStopOverEnabled()) {
//
//                                    if (finalPointlist.size() > 0) {
//                                        ArrayList<Stop_Over_Points_Data> finalAllPointlist = new ArrayList<>();
//                                        finalAllPointlist = new ArrayList<>();
//                                        finalAllPointlist.add(mainAct.stopOverPointsList.get(0));
//                                        finalAllPointlist.addAll(finalPointlist);
//                                        mainAct.stopOverPointsList.clear();
//                                        mainAct.stopOverPointsList.addAll(finalAllPointlist);
//                                    }
//
//
//                                    sourceLocation = mainAct.stopOverPointsList.get(0).getDestLatLong();
//                                    destLocation = mainAct.stopOverPointsList.get(mainAct.stopOverPointsList.size() - 1).getDestLatLong();
//
//                                    StopOverPointsDataParser parser = new StopOverPointsDataParser(getActContext(), mainAct.stopOverPointsList, wayPointslist, destPointlist, finalPointlist, mainAct.getMap(), builder);
//                                    parser.getDistanceArray(generalFunc.getJsonObject(responseString));
//                                    List<List<HashMap<String, String>>> routes_list = parser.parse(generalFunc.getJsonObject(responseString));
//
//                                    distance = parser.distance;
//                                    time = parser.time;
//
//                                } else {
//
//                                    JSONObject obj_legs = generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0);
//
//
//                                    distance = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
//                                            generalFunc.getJsonValue("distance", obj_legs.toString()).toString())));
//
//                                    time = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
//                                            generalFunc.getJsonValue("duration", obj_legs.toString()).toString())));
//
//                                    sourceLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("start_location", obj_legs.toString()))),
//                                            GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("start_location", obj_legs.toString()))));
//
//                                    destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("end_location", obj_legs.toString()))),
//                                            GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("end_location", obj_legs.toString()))));
//
//                                }
//
//                                if (getActivity() != null) {
//                                    estimateFare(distance, time);
//                                }
//
//
//                                //temp animation test
//                                handleMapAnimation(responseString, sourceLocation, destLocation, etaVal);
//
//                            }
//
//
//                        } else {
//
//
//                            isRouteFail = true;
//                            if (!isSkip) {
//                                GenerateAlertBox alertBox = new GenerateAlertBox(getActContext());
//                                alertBox.setContentMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
//                                alertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
//                                alertBox.setBtnClickList(btn_id -> {
//                                    alertBox.closeAlertBox();
//                                    mainAct.userLocBtnImgView.performClick();
//
//                                });
//                                alertBox.showAlertBox();
//                            }
//
//                            if (isSkip) {
//                                isRouteFail = false;
//                                if (mainAct.destLocation != null && mainAct.pickUpLocation != null) {
//                                    handleMapAnimation(responseString, new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude()), new LatLng(mainAct.destLocation.getLatitude(), mainAct.destLocation.getLongitude()), "--");
//                                }
//                            } else {
//                                mainAct.userLocBtnImgView.performClick();
//                            }
//
//                            isSkip = true;
//                            if (getActivity() != null) {
//                                estimateFare(null, null);
//                            }
//                        }
//
//                    }
//                    else if (mainAct.eFly && mainAct.stopOverPointsList.size() < 2)
//                    {
//                        Location loc2=null;
//                        Location loc1 = new Location("");
//                        loc1.setLatitude(mainAct.getPickUpLocation().getLatitude());
//                        loc1.setLongitude(mainAct.getPickUpLocation().getLongitude());
//
//                        String destLoc = null;
//                        if (mainAct.destLocation != null) {
//                            loc2 = new Location("");
//                            loc2.setLatitude(generalFunc.parseDoubleValue(0.00,mainAct.getDestLocLatitude()));
//                            loc2.setLongitude(generalFunc.parseDoubleValue(0.00,mainAct.getDestLocLongitude()));
//                        } else {
//                            loc2 = loc1;
//                        }
//
//
//
//
//                        int DRIVER_ARRIVED_MIN_TIME_PER_MINUTE = generalFunc.parseIntegerValue(3, generalFunc.getJsonValue("DRIVER_ARRIVED_MIN_TIME_PER_MINUTE", generalFunc.retrieveValue(Utils.USER_PROFILE_JSON)));
//
//                        Random r = new Random();
//                        int randomNo = r.nextInt(1000 + 1);
//                        long  dis = (long) loc1.distanceTo(loc2);  // direct distance
//                        distance = ""+dis;  // direct distance
//                        time = ""+((int) (((dis / 1000) * DRIVER_ARRIVED_MIN_TIME_PER_MINUTE) * 60)); // estimated Time
//
//
//                        sourceLocation = new LatLng(loc1.getLatitude(),loc1.getLongitude());
//                        destLocation = new LatLng(loc2.getLatitude(),loc2.getLongitude());
//
//                        if (getActivity() != null) {
//                        //    Logger.d("SET_PICKUP", "in 1");
//                            estimateFare(distance, time);
//                        }
//
//
//                        //temp animation test
//                        handleMapAnimation(responseString, sourceLocation, destLocation, etaVal);
//                    }
//                }
//            });
            // exeWebServer.execute();

        } catch (
                Exception e) {


        }
    }


    public void setEta(String time) {
        if (etaTxt != null) {
            etaTxt.setText(time);
        }


    }

    public void mangeMrakerPostion() {
        try {

            if (mainAct.pickUpLocation != null) {
                Point PickupPoint = mainAct.getMap().getProjection().toScreenLocation(new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude()));
                if (sourceMarker != null) {
                    sourceMarker.setAnchor(PickupPoint.x < Utils.dpToPx(getActContext(), 200) ? 0.00f : 1.00f, PickupPoint.y < Utils.dpToPx(getActContext(), 100) ? 0.20f : 1.20f);
                }
            }
            if (destLocation != null) {
                Point DestinationPoint = mainAct.getMap().getProjection().toScreenLocation(destLocation);
                //dest
                if (destMarker != null) {
                    destMarker.setAnchor(DestinationPoint.x < Utils.dpToPx(getActContext(), 200) ? 0.00f : 1.00f, DestinationPoint.y < Utils.dpToPx(getActContext(), 100) ? 0.20f : 1.20f);
                }
            }
        } catch (Exception e) {

        }


    }

    public void handleSourceMarker(String etaVal) {
        try {
            if (!isSkip) {
                if (mainAct.pickUpLocation == null) {
                    return;
                }
            }

            if (marker_view == null) {
                marker_view = ((LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.custom_marker, null);
                addressTxt = (MTextView) marker_view
                        .findViewById(R.id.addressTxt);
                etaTxt = (MTextView) marker_view.findViewById(R.id.etaTxt);
            }

            if (marker_view != null) {
                etaTxt = (MTextView) marker_view.findViewById(R.id.etaTxt);
            }

            addressTxt.setTextColor(getActContext().getResources().getColor(R.color.sourceAddressTxt));

            LatLng fromLnt;
            if (isSkip) {
                estimateFare(null, null);
                if (destMarker != null) {
                    destMarker.remove();
                }
                if (destDotMarker != null) {
                    destDotMarker.remove();
                }
                if (route_polyLine != null) {
                    route_polyLine.remove();
                }

                destLocation = null;
                mainAct.destLocation = null;

                fromLnt = new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude());

            } else {
                fromLnt = new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude());

                if (sourceLocation != null) {
                    fromLnt = sourceLocation;
                }


            }


            etaTxt.setVisibility(View.VISIBLE);
            etaTxt.setText(etaVal);

            if (sourceMarker != null) {
                sourceMarker.remove();
                sourceMarker = null;
            }

            if (source_dot_option != null) {
                if(sourceDotMarker!=null) {
                    sourceDotMarker.remove();
                    sourceDotMarker = null;
                }
                source_dot_option = null;
            }

            source_dot_option = new MarkerOptions().position(fromLnt).icon(BitmapDescriptorFactory.fromResource(R.mipmap.dot));

            if (mainAct.getMap() != null) {
                sourceDotMarker = mainAct.getMap().addMarker(source_dot_option);
            }

            String name = "";
            if (generalFunc.retrieveValue(Utils.BOOK_FOR_ELSE_ENABLE_KEY).equalsIgnoreCase("yes") && getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride)) {

                if (generalFunc.containsKey(Utils.BFSE_SELECTED_CONTACT_KEY) && Utils.checkText(generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY))) {
                    Gson gson = new Gson();
                    String data1 = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                    ContactModel contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                            }.getType()
                    );


                    if (Utils.checkText(contactdetails.name) && !contactdetails.name.equalsIgnoreCase("ME")) {
                        int n = 5;
                        String upToNCharacters = contactdetails.name.substring(0, Math.min(contactdetails.name.length(), n)) + (contactdetails.name.length() > n ? "..." : "");
                        name = "<b><font color=" + getActContext().getResources().getColor(R.color.black) + ">" + "@" + upToNCharacters + "</font><b>" + " - ";
                    }
                }
            }

            addressTxt.setText(AppFunctions.fromHtml(name + mainAct.pickUpLocationAddress));
            MarkerOptions marker_opt_source = new MarkerOptions().position(fromLnt).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActContext(), marker_view))).anchor(0.00f, 0.20f);
            if (mainAct.getMap() != null) {
                sourceMarker = mainAct.getMap().addMarker(marker_opt_source);
                sourceMarker.setTag("1");
            }

            buildBuilder();

            if (isSkip) {
         /*   if (mainAct.getMap() != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude()))
                        .zoom(Utils.defaultZomLevel).build();
                mainAct.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }*/
            }

        } catch (Exception e) {
            // Backpress done by user then app crashes
            e.printStackTrace();
        }
    }

    public void handleMapAnimation(String responseString, LatLng sourceLocation, LatLng destLocation, String etaVal) {

        try {
            if (mainAct == null) {
                return;
            }

            //    mainAct.getMap().clear();
            if (mainAct.cabSelectionFrag == null) {
                return;
            }

            if (isSkip) {
                MapAnimator.getInstance().stopRouteAnim();
                if (route_polyLine != null) {
                    route_polyLine.remove();
                    route_polyLine = null;
                }
                return;
            }

            MapAnimator.getInstance().stopRouteAnim();

            LatLng fromLnt = new LatLng(sourceLocation.latitude, sourceLocation.longitude);
            LatLng toLnt = new LatLng(destLocation.latitude, destLocation.longitude);


            if (marker_view == null) {

                marker_view = ((LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.custom_marker, null);
                addressTxt = (MTextView) marker_view
                        .findViewById(R.id.addressTxt);
                etaTxt = (MTextView) marker_view.findViewById(R.id.etaTxt);
            }

            addressTxt.setTextColor(getActContext().getResources().getColor(R.color.destAddressTxt));


            addressTxt.setText(mainAct.destAddress + " " + (mainAct.stopOverPointsList.size() >= 3 ? ">" : ""));

            MarkerOptions marker_opt_dest = new MarkerOptions().position(toLnt);
            etaTxt.setVisibility(View.GONE);

            marker_opt_dest.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActContext(), marker_view))).anchor(0.00f, 0.20f);
            if (dest_dot_option != null) {
                destDotMarker.remove();
            }
            dest_dot_option = new MarkerOptions().position(toLnt).icon(BitmapDescriptorFactory.fromResource(R.mipmap.dot));
            destDotMarker = mainAct.getMap().addMarker(dest_dot_option);

            if (destMarker != null) {
                destMarker.remove();
            }
            destMarker = mainAct.getMap().addMarker(marker_opt_dest);
            destMarker.setTag("2");
       /* LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(fromLnt);
        builder.include(toLnt);*/

            handleSourceMarker(etaVal);

            JSONArray obj_routes1 = generalFunc.getJsonArray("routes", responseString);


            if (obj_routes1 != null && obj_routes1.length() > 0) {
                routeDrawResponse = responseString;
                Logger.d("routeDrawResponse", "::" + routeDrawResponse);
                PolylineOptions lineOptions = null;

                if ((isPoolCabTypeIdSelected || mainAct.eFly) && sourceLocation != null && destLocation != null) {
                    lineOptions = createCurveRoute(new LatLng(sourceLocation.latitude, sourceLocation.longitude), new LatLng(destLocation.latitude, destLocation.longitude));

                } else if (!isPoolCabTypeIdSelected && Utils.checkText(routeDrawResponse)) {
                    if (mainAct.stopOverPointsList.size() > 2) {
                        lineOptions = getGoogleRouteOptions(routeDrawResponse, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(R.color.black), getActContext(), mainAct.stopOverPointsList, wayPointslist, destPointlist, finalPointlist, mainAct.getMap(), builder, isGoogle);

                    } else {
                        lineOptions = getGoogleRouteOptions(routeDrawResponse, Utils.dipToPixels(getActContext(), 5), getActContext().getResources().getColor(android.R.color.black), isGoogle);
                    }

                }

                if (lineOptions != null) {
                    if (route_polyLine != null) {
                        route_polyLine.remove();
                        route_polyLine = null;

                    }
                    route_polyLine = mainAct.getMap().addPolyline(lineOptions);
                    route_polyLine.remove();
                }
            }

            DisplayMetrics metrics = new DisplayMetrics();
            mainAct.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
//        mainAct.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dpToPx(getActContext(), 80), metrics.heightPixels - Utils.dipToPixels(getActContext(), 300), 0));

            if (route_polyLine != null && route_polyLine.getPoints().size() > 1) {
                MapAnimator.getInstance().animateRoute(mainAct.getMap(), route_polyLine.getPoints(), getActContext());
            }

            mainAct.getMap().setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {

                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    mainAct.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int height = displaymetrics.heightPixels;
                    int width = displaymetrics.widthPixels;


                }
            });


//        mainAct.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                if (marker == null) {
//                    return false;
//                }
//
//                if (marker.getTag().equals("1")) {
//                    if (mainAct.mainHeaderFrag != null) {
//                        mainAct.mainHeaderFrag.pickupLocArea1.performClick();
//                    }
//
//                } else if (marker.getTag().equals("2")) {
//                    if (mainAct.mainHeaderFrag != null) {
//                        mainAct.mainHeaderFrag.destarea.performClick();
//                    }
//
//                }
//
//                return false;
//            }
//        });


            if (mainAct.loadAvailCabs != null) {
                mainAct.loadAvailCabs.changeCabs();
            }


        } catch (Exception e) {
            // Backpress done by user then app crashes

            e.printStackTrace();
        }

    }

    public void buildBuilder() {
        if (mainAct == null) {
            return;
        }
        if (sourceMarker != null && (destMarker == null || isSkip)) {

            builder = new LatLngBounds.Builder();

            builder.include(sourceMarker.getPosition());

            DisplayMetrics metrics = new DisplayMetrics();
            mainAct.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            int padding = (mainAct != null && mainAct.isMultiDelivery()) ? (width != 0 ? (int) (width * 0.35) : 0) : 0; // offset from edges of the map in pixels

            LatLngBounds bounds = builder.build();
            LatLng center = bounds.getCenter();
            LatLng northEast = SphericalUtil.computeOffset(center, 30 * Math.sqrt(2.0), SphericalUtil.computeHeading(center, bounds.northeast));
            LatLng southWest = SphericalUtil.computeOffset(center, 30 * Math.sqrt(2.0), (180 + (180 + SphericalUtil.computeHeading(center, bounds.southwest))));
            builder.include(southWest);
            builder.include(northEast);


            Logger.d("MapHeight", "padding 1>>" + padding);
            Logger.d("MapHeight", "Main height 1>>" + height);
            Logger.d("MapHeight", "fragmentHeight 1>>" + fragmentHeight);

            int tabHeight = 0;
            if (mainAct.isMultiDelivery()) {
                tabHeight = (int) getActContext().getResources().getDimension(R.dimen._45sdp);
            }

            mainAct.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - tabHeight - ((fragmentHeight + 5) + Utils.dipToPixels(getActContext(), 60)), padding));

        } else if (mainAct.map != null && mainAct.map.getView().getViewTreeObserver().isAlive()) {
            mainAct.map.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {

                    boolean isBoundIncluded = false;

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    if (sourceMarker != null) {
                        isBoundIncluded = true;
                        builder.include(sourceMarker.getPosition());
                    }


                    if (destMarker != null) {
                        isBoundIncluded = true;
                        builder.include(destMarker.getPosition());
                    }


                    if (isBoundIncluded) {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mainAct.map.getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            mainAct.map.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }


                        LatLngBounds bounds = builder.build();


                        LatLng center = bounds.getCenter();

                        LatLng northEast = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), SphericalUtil.computeHeading(center, bounds.northeast));
                        LatLng southWest = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), (180 + (180 + SphericalUtil.computeHeading(center, bounds.southwest))));

                        builder.include(southWest);
                        builder.include(northEast);

                        /*  Method 1 */
//                            mainAct.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                        DisplayMetrics metrics = new DisplayMetrics();
                        mainAct.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;
                        // Set Padding according to included bounds

                        int padding = (int) (width * 0.25); // offset from edges of the map 25% of screen

                        /*  Method 2 */
                            /*Logger.e("MapHeight","newLatLngZoom");
                            mainAct.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(builder.build().getCenter(),16));*/
                        int height_ = 0;
                        if (mainAct != null && mainAct.mainHeaderFrag != null && mainAct.isMultiDelivery()) {
                            height_ = mainAct.mainHeaderFrag.fragmentHeight;
                        } else {
                            height_ = Utils.dipToPixels(getActContext(), 60);
                        }


                        try {
                            /*  Method 3 */
                            int screenWidth = getResources().getDisplayMetrics().widthPixels;
                            int screenHeight = getResources().getDisplayMetrics().heightPixels;
                            padding = ((height - ((fragmentHeight + 5) + height_)) / 3);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(),
                                    screenWidth, screenHeight, padding);
//                            CameraUpdateFactory.scrollBy(float, float);
                            mainAct.getMap().animateCamera(cameraUpdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                            /*  Method 1 */
                            mainAct.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - ((fragmentHeight + 5) + height_), padding));
                        }


                    }

                }
            });
        }
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    // add route polyline line
    public PolylineOptions getGoogleRouteOptions(String directionJson, int width, int color, Context mContext, ArrayList<Stop_Over_Points_Data> list, ArrayList<Stop_Over_Points_Data> wayPointslist, ArrayList<Stop_Over_Points_Data> destPointlist, ArrayList<Stop_Over_Points_Data> finalPointlist, GoogleMap gMap, LatLngBounds.Builder builder, Boolean isGoogle) {
        PolylineOptions lineOptions = new PolylineOptions();

        Logger.d("isGoogleVal", "::" + isGoogle);
        if (isGoogle) {


            StopOverPointsDataParser parser = new StopOverPointsDataParser(mContext, list, wayPointslist, destPointlist, finalPointlist, gMap, builder);
            // JSONObject jsonObject=new JSONObject();
//            try {
//                jsonObject.put("routes",generalFunc.getJsonValue("routes",directionJson));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            List<List<HashMap<String, String>>> routes_list = null;
            try {
                routes_list = parser.parse(new JSONObject(directionJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<LatLng> points = new ArrayList<LatLng>();

            if (routes_list.size() > 0) {
                // Fetching i-th route
                List<HashMap<String, String>> path = routes_list.get(0);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }

                lineOptions.addAll(points);
                lineOptions.width(width);
                lineOptions.color(color);

                return lineOptions;
            } else {
                return null;
            }
        } else {

            try {
                JSONArray obj_routes1 = generalFunc.getJsonArray("routes", directionJson);

                ArrayList<LatLng> points = new ArrayList<LatLng>();

                if (obj_routes1.length() > 0) {
                    // Fetching i-th route
                    // Fetching all the points in i-th route
                    for (int j = 0; j < obj_routes1.length(); j++) {

                        JSONObject point = generalFunc.getJsonObject(obj_routes1, j);

                        LatLng position = new LatLng(GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("latitude", point).toString()), GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("longitude", point).toString()));


                        points.add(position);

                    }


                    lineOptions.addAll(points);
                    lineOptions.width(width);
                    lineOptions.color(color);

                    return lineOptions;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }

        }
    }

//    public PolylineOptions getGoogleRouteOptions(String directionJson, int width, int color) {
//        PolylineOptions lineOptions = new PolylineOptions();
//
//        try {
//            DirectionsJSONParser parser = new DirectionsJSONParser();
//            List<List<HashMap<String, String>>> routes_list = parser.parse(new JSONObject(directionJson));
//
//            ArrayList<LatLng> points = new ArrayList<LatLng>();
//
//            if (routes_list.size() > 0) {
//                // Fetching i-th route
//                List<HashMap<String, String>> path = routes_list.get(0);
//
//                // Fetching all the points in i-th route
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String, String> point = path.get(j);
//
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    LatLng position = new LatLng(lat, lng);
//
//                    points.add(position);
//
//                }
//
//                lineOptions.addAll(points);
//                lineOptions.width(width);
//                lineOptions.color(color);
//
//                return lineOptions;
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public PolylineOptions getGoogleRouteOptions(String directionJson, int width, int color, Boolean isGoogle) {
        PolylineOptions lineOptions = new PolylineOptions();

        if (isGoogle) {

            try {
                DirectionsJSONParser parser = new DirectionsJSONParser();
                List<List<HashMap<String, String>>> routes_list = parser.parse(new JSONObject(directionJson));

                ArrayList<LatLng> points = new ArrayList<LatLng>();

                if (routes_list.size() > 0) {
                    // Fetching i-th route
                    List<HashMap<String, String>> path = routes_list.get(0);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);

                    }

                    lineOptions.addAll(points);
                    lineOptions.width(width);
                    lineOptions.color(color);

                    return lineOptions;
                } else {
                    Logger.d("getGoogleRouteOptionsEx", ":: null");
                    return null;

                }
            } catch (Exception e) {
                Logger.d("getGoogleRouteOptionsEx", "::" + e.toString());
                return null;
            }
        } else {

            try {
                JSONArray obj_routes1 = generalFunc.getJsonArray("routes", directionJson);

                ArrayList<LatLng> points = new ArrayList<LatLng>();

                if (obj_routes1.length() > 0) {
                    // Fetching i-th route
                    // Fetching all the points in i-th route
                    for (int j = 0; j < obj_routes1.length(); j++) {

                        JSONObject point = generalFunc.getJsonObject(obj_routes1, j);

                        LatLng position = new LatLng(GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("latitude", point).toString()), GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("longitude", point).toString()));


                        points.add(position);

                    }


                    lineOptions.addAll(points);
                    lineOptions.width(width);
                    lineOptions.color(color);

                    return lineOptions;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    public String getAvailableCarTypesIds() {
        String carTypesIds = "";
        for (int i = 0; i < mainAct.cabTypesArrList.size(); i++) {
            String iVehicleTypeId = mainAct.cabTypesArrList.get(i).get("iVehicleTypeId");

            carTypesIds = carTypesIds.equals("") ? iVehicleTypeId : (carTypesIds + "," + iVehicleTypeId);
        }
        return carTypesIds;
    }

    public void estimateFare(final String distance, String time) {

        //  loaderView.setVisibility(View.VISIBLE);

        if (estimateFareTask != null) {
            estimateFareTask.cancel(true);
            estimateFareTask = null;
        }
        if (distance == null && time == null) {
            //  mainAct.noCabAvail = false;
            // isroutefound = false;

        } else {
            if (mainAct.loadAvailCabs != null) {
                if (mainAct.loadAvailCabs.isAvailableCab) {
                    isroutefound = true;
                    if (!mainAct.timeval.equalsIgnoreCase("\n" + "--")) {
                        mainAct.noCabAvail = false;
                    }
                }
            }

        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "estimateFareNew");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("SelectedCarTypeID", getAvailableCarTypesIds());
        if (distance != null && time != null) {
            parameters.put("distance", distance);
            parameters.put("time", time);
        }
        parameters.put("SelectedCar", mainAct.getSelectedCabTypeId());
        parameters.put("PromoCode", getAppliedPromoCode());

        if (mainAct.getPickUpLocation() != null) {
            parameters.put("StartLatitude", "" + mainAct.getPickUpLocation().getLatitude());
            parameters.put("EndLongitude", "" + mainAct.getPickUpLocation().getLongitude());
        }

        if (mainAct.getDestLocLatitude() != null && !mainAct.getDestLocLatitude().equalsIgnoreCase("")) {
            parameters.put("DestLatitude", "" + mainAct.getDestLocLatitude());
            parameters.put("DestLongitude", "" + mainAct.getDestLocLongitude());
        }

        if (mainAct.eFly) {
            parameters.put("iFromStationId", mainAct.iFromStationId);
            parameters.put("iToStationId", mainAct.iToStationId);
            parameters.put("eFly", "Yes");
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        estimateFareTask = exeWebServer;
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {

                    JSONArray vehicleTypesArr = generalFunc.getJsonArray(Utils.message_str, responseString);
                    String APP_TYPE = generalFunc.getJsonValue("APP_TYPE", userProfileJson);

                    for (int i = 0; i < vehicleTypesArr.length(); i++) {

                        JSONObject obj_temp = generalFunc.getJsonObject(vehicleTypesArr, i);

                        if (distance != null) {

                            String type = mainAct.getCurrentCabGeneralType();
                            if (type.equalsIgnoreCase("rental")) {
                                type = Utils.CabGeneralType_Ride;
                            }

                            if (generalFunc.getJsonValueStr("eType", obj_temp).contains(type)) {

                                if (cabTypeList != null) {
                                    for (int k = 0; k < cabTypeList.size(); k++) {
                                        HashMap<String, String> map = cabTypeList.get(k);

                                        if (map.get("iVehicleTypeId").equalsIgnoreCase(generalFunc.getJsonValueStr("iVehicleTypeId", obj_temp))) {

                                            String totalfare = "";

                                            if (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) ||
                                                    (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery) ||
                                                            (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralType_Ride)))) {
                                                if (map.get("eRental").equalsIgnoreCase("Yes") && (mainAct.iscubejekRental || mainAct.isRental)) {
                                                    totalfare = generalFunc.getJsonValueStr("eRental_total_fare", obj_temp);
                                                } else {
                                                    totalfare = generalFunc.getJsonValueStr("total_fare", obj_temp);
                                                }
                                            } else {
                                                totalfare = generalFunc.getJsonValueStr("total_fare", obj_temp);
                                            }

                                            if (totalfare != null && !totalfare.equals("")) {
                                                map.put("total_fare", totalfare);
                                                map.put("FinalFare", generalFunc.getJsonValueStr("FinalFare", obj_temp));
                                                map.put("currencySymbol", generalFunc.getJsonValueStr("currencySymbol", obj_temp));
                                                map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                Logger.e("FinalFare", "::" + generalFunc.getJsonValueStr("FinalFare", obj_temp));
                                                cabTypeList.set(k, map);

                                            } else {
                                                map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                cabTypeList.set(k, map);
                                            }
                                        }

                                    }
                                }

                                if (rentalTypeList != null) {
                                    for (int k = 0; k < rentalTypeList.size(); k++) {
                                        HashMap<String, String> map = rentalTypeList.get(k);

                                        if (/*map.get("vVehicleType").equalsIgnoreCase(generalFunc.getJsonValueStr()("vVehicleType", obj_temp))
                                                && */map.get("iVehicleTypeId").equalsIgnoreCase(generalFunc.getJsonValueStr("iVehicleTypeId", obj_temp))) {

                                            String totalfare = generalFunc.getJsonValueStr("eRental_total_fare", obj_temp);
                                            if (totalfare != null && !totalfare.equals("")) {
                                                map.put("total_fare", totalfare);
                                                map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                rentalTypeList.set(k, map);
                                            } else {
                                                map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                rentalTypeList.set(k, map);
                                            }
                                        }
                                    }
                                }


                            }
                        } else {


                            if (generalFunc.getJsonValueStr("eType", obj_temp).equalsIgnoreCase(mainAct.getCurrentCabGeneralType())) {

                                if (cabTypeList != null) {


                                    for (int k = 0; k < cabTypeList.size(); k++) {
                                        HashMap<String, String> map = cabTypeList.get(k);

                                        if (mainAct.iscubejekRental || mainAct.isRental) {
                                            if (/*map.get("vVehicleType").equalsIgnoreCase(generalFunc.getJsonValueStr()("vVehicleType", obj_temp))
                                            &&*/ map.get("iVehicleTypeId").equalsIgnoreCase(generalFunc.getJsonValueStr("iVehicleTypeId", obj_temp))) {
                                                String totalfare = generalFunc.getJsonValueStr("eRental_total_fare", obj_temp);
                                                if (totalfare != null && !totalfare.equals("")) {
                                                    map.put("total_fare", totalfare);
                                                    map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                    rentalTypeList.set(k, map);
                                                } else {
                                                    map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                    rentalTypeList.set(k, map);
                                                }

                                            }

                                        } else {

                                            if (/*map.get("vVehicleType").equalsIgnoreCase(generalFunc.getJsonValueStr()("vVehicleType", obj_temp))
                                            &&*/ map.get("iVehicleTypeId").equalsIgnoreCase(generalFunc.getJsonValueStr("iVehicleTypeId", obj_temp))) {
                                                map.put("total_fare", "");
                                                cabTypeList.set(k, map);
                                            }
                                        }
                                    }
                                }

                                if (rentalTypeList != null) {
                                    for (int k = 0; k < rentalTypeList.size(); k++) {
                                        HashMap<String, String> map = rentalTypeList.get(k);

                                        if (/*map.get("vVehicleType").equalsIgnoreCase(generalFunc.getJsonValueStr()("vVehicleType", obj_temp))
                                                && */map.get("iVehicleTypeId").equalsIgnoreCase(generalFunc.getJsonValueStr("iVehicleTypeId", obj_temp))) {

                                            String totalfare = generalFunc.getJsonValueStr("eRental_total_fare", obj_temp);
                                            if (totalfare != null && !totalfare.equals("")) {
                                                map.put("total_fare", totalfare);
                                                map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                rentalTypeList.set(k, map);
                                            } else {
                                                map.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", obj_temp));
                                                rentalTypeList.set(k, map);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        exeWebServer.execute();
    }

    public void openFareDetailsDilaog(final int pos) {

        if (mainAct.isMultiDelivery()) {
            return;
        }


        // if (cabTypeList.get(pos).get("total_fare") != null && !cabTypeList.get(pos).get("total_fare").equalsIgnoreCase("")) {
        if (cabTypeList.get(pos).get("total_fare") != null) {
            String vehicleIconPath = CommonUtilities.SERVER_URL + "webimages/icons/VehicleType/";
            String vehicleDefaultIconPath = CommonUtilities.SERVER_URL + "webimages/icons/DefaultImg/";
            final BottomSheetDialog faredialog = new BottomSheetDialog(getActContext());

            View contentView = View.inflate(getContext(), R.layout.dailog_faredetails, null);
            if (generalFunc.isRTLmode()) {
                contentView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            faredialog.setContentView(contentView);
            BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
            mBehavior.setPeekHeight(1500);
            View bottomSheetView = faredialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheetView).setHideable(false);
            setCancelable(faredialog, false);

            ImageView imagecar;
            LinearLayout capacityArea;
            final MTextView carTypeTitle, capacityHTxt, capacityVTxt, fareHTxt, fareVTxt, mordetailsTxt, farenoteTxt, pkgMsgTxt;
            MButton btn_type2;
            ImageView morwArrow;
            int submitBtnId;
            imagecar = (ImageView) faredialog.findViewById(R.id.imagecar);
            carTypeTitle = (MTextView) faredialog.findViewById(R.id.carTypeTitle);
            capacityHTxt = (MTextView) faredialog.findViewById(R.id.capacityHTxt);
            capacityVTxt = (MTextView) faredialog.findViewById(R.id.capacityVTxt);
            fareHTxt = (MTextView) faredialog.findViewById(R.id.fareHTxt);
            fareVTxt = (MTextView) faredialog.findViewById(R.id.fareVTxt);
            capacityArea = (LinearLayout) faredialog.findViewById(R.id.capacityArea);
            mordetailsTxt = (MTextView) faredialog.findViewById(R.id.mordetailsTxt);
            morwArrow = (ImageView) faredialog.findViewById(R.id.morwArrow);
            farenoteTxt = (MTextView) faredialog.findViewById(R.id.farenoteTxt);
            pkgMsgTxt = (MTextView) faredialog.findViewById(R.id.pkgMsgTxt);

            btn_type2 = ((MaterialRippleLayout) faredialog.findViewById(R.id.btn_type2)).getChildView();
            submitBtnId = Utils.generateViewId();
            btn_type2.setId(submitBtnId);


            capacityHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CAPACITY"));
            fareHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FARE_TXT"));
            mordetailsTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MORE_DETAILS"));

            if (mainAct.isFixFare || mainAct.eFly) {
                farenoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GENERAL_NOTE_FLAT_FARE_EST"));
            } else {
                farenoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GENERAL_NOTE_FARE_EST"));
            }
            btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_DONE"));

            if (cabTypeList.get(pos).get("eRental") != null && cabTypeList.get(pos).get("eRental").equalsIgnoreCase("Yes")) {
                mordetailsTxt.setVisibility(View.GONE);
                morwArrow.setVisibility(View.GONE);
                pkgMsgTxt.setVisibility(View.VISIBLE);
                fareHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PKG_STARTING_AT"));

                if (mainAct.eShowOnlyMoto != null && mainAct.eShowOnlyMoto.equalsIgnoreCase("Yes")) {
                    pkgMsgTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_MOTO_PKG_MSG"));
                } else if (mainAct.eFly) {
                    pkgMsgTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_AIRCRAFT_PKG_MSG"));
                } else {
                    pkgMsgTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_PKG_MSG"));
                }
                farenoteTxt.setText(generalFunc.retrieveLangLBl("", mainAct.eFly ? "LBL_RENT_AIRCRAFT_PKG_DETAILS" : "LBL_RENT_PKG_DETAILS"));
            }


            if (!cabTypeList.get(pos).get("eRental").equals("") && cabTypeList.get(pos).get("eRental").equals("Yes")) {
                carTypeTitle.setText(cabTypeList.get(pos).get("vRentalVehicleTypeName"));
            } else {
                carTypeTitle.setText(cabTypeList.get(pos).get("vVehicleType"));
            }
            if (cabTypeList.get(pos).get("total_fare") != null && !cabTypeList.get(pos).get("total_fare").equalsIgnoreCase("")) {
                fareVTxt.setText(generalFunc.convertNumberWithRTL(cabTypeList.get(pos).get("total_fare")));
            } else {
                fareVTxt.setText("--");
            }
            if (mainAct.getCurrentCabGeneralType().equals(Utils.CabGeneralType_Ride)) {
                capacityVTxt.setText(generalFunc.convertNumberWithRTL(cabTypeList.get(pos).get("iPersonSize")) + " " + generalFunc.retrieveLangLBl("", "LBL_PEOPLE_TXT"));
                capacityArea.setVisibility(View.VISIBLE);

            } else {
                capacityVTxt.setText("---");
                capacityArea.setVisibility(View.GONE);
            }

            String imgName = cabTypeList.get(pos).get("vLogo1");
            if (imgName.equals("")) {
                imgName = vehicleDefaultIconPath + "hover_ic_car.png";
            } else {
                imgName = vehicleIconPath + cabTypeList.get(pos).get("iVehicleTypeId") + "/android/" + "xxxhdpi_" +
                        cabTypeList.get(pos).get("vLogo1");


            }

            Picasso.get()
                    .load(imgName)
                    .into(imagecar, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });


            btn_type2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    faredialog.dismiss();

                }
            });

            morwArrow.setOnClickListener(v -> mordetailsTxt.performClick());

            mordetailsTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialogShowOnce = true;
                    Bundle bn = new Bundle();
                    bn.putString("SelectedCar", cabTypeList.get(pos).get("iVehicleTypeId"));
                    bn.putString("iUserId", generalFunc.getMemberId());
                    bn.putString("distance", distance);
                    bn.putString("time", time);
                    bn.putString("PromoCode", appliedPromoCode);
                    if (cabTypeList.get(pos).get("eRental").equals("yes")) {
                        bn.putString("vVehicleType", cabTypeList.get(pos).get("vRentalVehicleTypeName"));
                    } else {
                        bn.putString("vVehicleType", cabTypeList.get(pos).get("vVehicleType"));
                    }
                    bn.putBoolean("isSkip", isSkip);
                    if (mainAct.getPickUpLocation() != null) {
                        bn.putString("picupLat", mainAct.getPickUpLocation().getLatitude() + "");
                        bn.putString("pickUpLong", mainAct.getPickUpLocation().getLongitude() + "");
                    }
                    if (mainAct.destLocation != null) {
                        bn.putString("destLat", mainAct.destLocLatitude + "");
                        bn.putString("destLong", mainAct.destLocLongitude + "");
                    }
                    if (mainAct.isFixFare) {
                        bn.putBoolean("isFixFare", true);
                    } else {
                        bn.putBoolean("isFixFare", false);
                    }

                    if (mainAct.eFly) {
                        bn.putString("iFromStationId", mainAct.iFromStationId);
                        bn.putString("iToStationId", mainAct.iToStationId);
                        bn.putString("eFly", "Yes");
                    }
                    new StartActProcess(getActContext()).startActWithData(FareBreakDownActivity.class, bn);
                    faredialog.dismiss();
                }
            });


            faredialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            faredialog.show();
        }


    }

    public void setCancelable(Dialog dialogview, boolean cancelable) {
        final Dialog dialog = dialogview;
        View touchOutsideView = dialog.getWindow().getDecorView().findViewById(R.id.touch_outside);
        View bottomSheetView = dialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);

        if (cancelable) {
            touchOutsideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                }
            });
            BottomSheetBehavior.from(bottomSheetView).setHideable(true);
        } else {
            touchOutsideView.setOnClickListener(null);
            BottomSheetBehavior.from(bottomSheetView).setHideable(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        releseInstances();
    }

    private void releseInstances() {
        Utils.hideKeyboard(getActContext());
        if (estimateFareTask != null) {
            estimateFareTask.cancel(true);
            estimateFareTask = null;
        }
    }

    public void Checkpickupdropoffrestriction() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "Checkpickupdropoffrestriction");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("PickUpLatitude", "" + mainAct.getPickUpLocation().getLatitude());
        parameters.put("PickUpLongitude", "" + mainAct.getPickUpLocation().getLongitude());
        parameters.put("DestLatitude", mainAct.getDestLocLatitude());
        parameters.put("DestLongitude", mainAct.getDestLocLongitude());
        parameters.put("UserType", Utils.userType);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                if (responseString != null && !responseString.equals("")) {
                    if (generalFunc.getJsonValue("Action", responseString).equalsIgnoreCase("0")) {
                        if (message.equalsIgnoreCase("LBL_DROP_LOCATION_NOT_ALLOW")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DROP_LOCATION_NOT_ALLOW"));
                        } else if (message.equalsIgnoreCase("LBL_PICKUP_LOCATION_NOT_ALLOW")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_PICKUP_LOCATION_NOT_ALLOW"));
                        }
                    } else if (generalFunc.getJsonValue("Action", responseString).equalsIgnoreCase("1")) {
                        mainAct.continueDeliveryProcess();
                    }

                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releseInstances();
    }

    public void outstandingDialog(/*boolean isReqNow*/String responseString, Intent data) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dailog_outstanding, null);

        final MTextView outStandingTitle = (MTextView) dialogView.findViewById(R.id.outStandingTitle);
        final MTextView outStandingValue = (MTextView) dialogView.findViewById(R.id.outStandingValue);
        final MTextView cardtitleTxt = (MTextView) dialogView.findViewById(R.id.cardtitleTxt);
        final MTextView adjustTitleTxt = (MTextView) dialogView.findViewById(R.id.adjustTitleTxt);
        final LinearLayout cardArea = (LinearLayout) dialogView.findViewById(R.id.cardArea);
        final LinearLayout adjustarea = (LinearLayout) dialogView.findViewById(R.id.adjustarea);
        outStandingTitle.setText(generalFunc.retrieveLangLBl("", "LBL_OUTSTANDING_AMOUNT_TXT"));
        String type = mainAct.getCurrentCabGeneralType();
        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-3")) {
            adjustarea.setVisibility(View.GONE);
        }
        adjustTitleTxt.setText(generalFunc.retrieveLangLBl("Adjust in Your trip", "LBL_ADJUST_OUT_AMT_RIDE_TXT"));
        if (type.equalsIgnoreCase("Ride")) {
            adjustTitleTxt.setText(generalFunc.retrieveLangLBl("Adjust in Your trip", "LBL_ADJUST_OUT_AMT_RIDE_TXT"));
        } else if (type.equalsIgnoreCase("Deliver")) {
            adjustTitleTxt.setText(generalFunc.retrieveLangLBl("Adjust in Your trip", "LBL_ADJUST_OUT_AMT_DELIVERY_TXT"));
        }
//        outStandingValue.setText(generalFunc.getJsonValue("fOutStandingAmountWithSymbol", userProfileJson));
        outStandingValue.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("fOutStandingAmountWithSymbol", responseString)));


        cardtitleTxt.setText(generalFunc.retrieveLangLBl("Pay Now", "LBL_PAY_NOW"));

        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash-Card") ||
                    APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
                cardArea.setVisibility(View.VISIBLE);

            }


            if (!generalFunc.getJsonValue("ShowPayNow", responseString).equalsIgnoreCase("") && generalFunc.getJsonValue("ShowPayNow", responseString).equalsIgnoreCase("NO")) {
                cardArea.setVisibility(View.GONE);
            }
        } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

            cardtitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
            cardArea.setVisibility(View.VISIBLE);
        }

        cardArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outstanding_dialog.dismiss();

                if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    checkCardConfig(true, mainAct.getCabReqType().equals(Utils.CabReqType_Now), responseString, data);
                } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                    callOutStandingPayAmout(responseString, data);


                }

            }
        });

        adjustarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outstanding_dialog.dismiss();

//                if (isReqNow) {
//                    isOutStandingDailogShow = true;
//                    ride_now_btn.performClick();
//                } else {
//                    isOutStandingDailogShow = true;
//                    img_ridelater.performClick();
//
//                }

                mainAct.continueSurgeChargeExecution(responseString, data);
            }
        });

        if (generalFunc.isRTLmode()) {
            ((ImageView) dialogView.findViewById(R.id.cardimagearrow)).setRotationY(180);
            ((ImageView) dialogView.findViewById(R.id.adjustimagearrow)).setRotationY(180);
        }


        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        int submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        btn_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outstanding_dialog.dismiss();
            }
        });

        builder.setView(dialogView);
        outstanding_dialog = builder.create();
        if (generalFunc.isRTLmode() == true) {
            generalFunc.forceRTLIfSupported(outstanding_dialog);
        }
        outstanding_dialog.setCancelable(false);
        outstanding_dialog.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
        outstanding_dialog.show();
    }

    public void callOutStandingPayAmout(/*boolean isReqNow*/ String responseStr, Intent data) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "ChargePassengerOutstandingAmount");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("ePaymentBy", mainAct.ePaymentBy);
        parameters.put("iUserProfileId", mainAct.iUserProfileId);
        parameters.put("iOrganizationId", mainAct.iOrganizationId);
        parameters.put("vProfileEmail", mainAct.vProfileEmail);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setCancelAble(false);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                    getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                    mainAct.userProfileJson = userProfileJson;
                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                        @Override
                        public void handleBtnClick(int btn_id) {

                            /*if (isReqNow) {
                                isOutStandingDailogShow = true;
                                ride_now_btn.performClick();
                            } else {
                                isOutStandingDailogShow = true;
                                img_ridelater.performClick();
                            }*/

                            mainAct.continueSurgeChargeExecution(responseString, data);
                        }
                    });
                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    generateAlert.showAlertBox();
                } else {

                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {
                        String walletMsg = "";

                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), button_Id -> {
                            if (button_Id == 1) {

                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else {

                            }
                        });
                    }
                }
            } else {
                generalFunc.showGeneralMessage("",
                        generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
            }
        });
        exeWebServer.execute();

    }

    @Override
    public void onGlobalLayout() {
        boolean heightChanged = false;
        if (getView() != null || view != null) {
            if (getView() != null) {

                if (getView().getHeight() != 0 && getView().getHeight() != fragmentHeight) {
                    heightChanged = true;
                }
                fragmentWidth = getView().getWidth();
                fragmentHeight = getView().getHeight();
            } else if (view != null) {

                if (view.getHeight() != 0 && view.getHeight() != fragmentHeight) {
                    heightChanged = true;
                }

                fragmentWidth = view.getWidth();
                fragmentHeight = view.getHeight();
            }

            Logger.e("FragHeight", "is :::" + fragmentHeight + "\n" + "Frag Width is :::" + fragmentWidth);
            Logger.d("MapHeight", "fragmentHeight got>>" + fragmentHeight);

            if (heightChanged && fragmentWidth != 0 && fragmentHeight != 0) {
                mainAct.setPanelHeight(fragmentHeight);
            }
        }
    }

    @Override
    public void onItemClick(int position, String selectedType) {

        seatsSelectpos = position;
        double totalFare = GeneralFunctions.parseDoubleValue(0, cabTypeList.get(selpos).get("FinalFare"));
        double seatVal = GeneralFunctions.parseDoubleValue(1, poolSeatsList.get(position));

        if (seatVal > 1) {
            double res = (totalFare / 100.0f) * GeneralFunctions.parseDoubleValue(0, mainAct.cabTypesArrList.get(selpos).get("fPoolPercentage"));
            res = res + totalFare;
            // poolFareTxt.setText(cabTypeList.get(selpos).get("currencySymbol") + " " + String.format("%.2f", (float) res));
            DecimalFormat formatter = new DecimalFormat("#,###,###.00");
            poolFareTxt.setText(cabTypeList.get(selpos).get("currencySymbol") + " " + formatter.format(res));
        } else {
            poolFareTxt.setText(cabTypeList.get(selpos).get("total_fare"));
        }

        if (seatsSelectionAdapter != null) {
            seatsSelectionAdapter.setSelectedSeat(seatsSelectpos);
            seatsSelectionAdapter.notifyDataSetChanged();
        }
    }

    public void hideRentalArea() {
        rentalPkg.setVisibility(View.GONE);
        rentalarea.setVisibility(View.GONE);
        rentPkgImage.setVisibility(View.GONE);
        rentBackPkgImage.setVisibility(View.GONE);
    }

    @Override
    public void searchResult(ArrayList<HashMap<String, String>> placelist, int selectedPos, String input) {

    }

    @Override
    public void resetOrAddDest(int selPos, String address, double latitude, double longitude, String isSkip) {

    }

    boolean isGoogle = false;

    @Override
    public void directionResult(HashMap<String, String> directionlist) {

        {


            mProgressBar.setIndeterminate(false);
            mProgressBar.setVisibility(mainAct.isMultiDelivery() ? View.GONE : View.INVISIBLE);

            String responseString = directionlist.get("routes");
            if (responseString.equalsIgnoreCase("")) {

                Logger.d("directionResult", "::NULL##");

                isRouteFail = true;
                if (!isSkip) {
                    GenerateAlertBox alertBox = new GenerateAlertBox(getActContext());
                    alertBox.setContentMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
                    alertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    alertBox.setBtnClickList(btn_id -> {
                        alertBox.closeAlertBox();
                        mainAct.userLocBtnImgView.performClick();

                    });
                    alertBox.showAlertBox();
                }

                if (isSkip) {
                    Logger.d("directionResult", "::NULLSKIP##");
                    isRouteFail = false;
                    if (mainAct.destLocation != null && mainAct.pickUpLocation != null) {
                        handleMapAnimation(responseString, new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude()), new LatLng(mainAct.destLocation.getLatitude(), mainAct.destLocation.getLongitude()), "--");
                    }
                } else {
                    mainAct.userLocBtnImgView.performClick();
                }

                isSkip = true;
                if (getActivity() != null) {
                    estimateFare(null, null);
                }
                return;
            }
            if (responseString.equalsIgnoreCase("null")) {
                responseString = null;
            }

            if (responseString != null && !responseString.equalsIgnoreCase("") && directionlist.get("distance") == null) {
                isGoogle = true;
                isRouteFail = false;
//                JSONArray obj_routes = generalFunc.getJsonArray(responseString);
                JSONArray obj_routes = generalFunc.getJsonArray("routes", responseString);
                if (obj_routes != null && obj_routes.length() > 0) {


                    if (mainAct.stopOverPointsList.size() > 2 && mainAct.isMultiStopOverEnabled()) {

                        if (finalPointlist.size() > 0) {
                            ArrayList<Stop_Over_Points_Data> finalAllPointlist = new ArrayList<>();
                            finalAllPointlist = new ArrayList<>();
                            finalAllPointlist.add(mainAct.stopOverPointsList.get(0));
                            finalAllPointlist.addAll(finalPointlist);
                            mainAct.stopOverPointsList.clear();
                            mainAct.stopOverPointsList.addAll(finalAllPointlist);
                        }


                        sourceLocation = mainAct.stopOverPointsList.get(0).getDestLatLong();
                        destLocation = mainAct.stopOverPointsList.get(mainAct.stopOverPointsList.size() - 1).getDestLatLong();

                        StopOverPointsDataParser parser = new StopOverPointsDataParser(getActContext(), mainAct.stopOverPointsList, wayPointslist, destPointlist, finalPointlist, mainAct.getMap(), builder);
                        HashMap<String, String> routeMap = new HashMap<>();
                        routeMap.put("routes", directionlist.get("routes"));

                        String jsonrotas =directionlist.get("routes");
                        responseString = routeMap.toString();
                        parser.getDistanceArray(generalFunc.getJsonObject(responseString));


                        //  List<List<HashMap<String, String>>> routes_list = parser.parse(generalFunc.getJsonObject(responseString));

                        distance = parser.distance;
                        time = parser.time;

                    } else {

                        JSONObject obj_legs = generalFunc.getJsonObject(generalFunc.getJsonArray("legs", generalFunc.getJsonObject(obj_routes, 0).toString()), 0);


                        distance = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
                                generalFunc.getJsonValue("distance", obj_legs.toString()).toString())));

                        time = "" + (GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("value",
                                generalFunc.getJsonValue("duration", obj_legs.toString()).toString())));

                        sourceLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("start_location", obj_legs.toString()))),
                                GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("start_location", obj_legs.toString()))));

                        destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", generalFunc.getJsonValue("end_location", obj_legs.toString()))),
                                GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("lng", generalFunc.getJsonValue("end_location", obj_legs.toString()))));

                    }

                    if (getActivity() != null) {
                        estimateFare(distance, time);
                    }


                    //temp animation test
                    responseString = directionlist.get("routes");
                    handleMapAnimation(responseString, sourceLocation, destLocation, "--");

                }
            } else if (responseString == null) {
                Logger.d("directionResult", "::NULL##");

                isRouteFail = true;
                if (!isSkip) {
                    GenerateAlertBox alertBox = new GenerateAlertBox(getActContext());
                    alertBox.setContentMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
                    alertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    alertBox.setBtnClickList(btn_id -> {
                        alertBox.closeAlertBox();
                        mainAct.userLocBtnImgView.performClick();

                    });
                    alertBox.showAlertBox();
                }

                if (isSkip) {
                    Logger.d("directionResult", "::NULLSKIP##");
                    isRouteFail = false;
                    if (mainAct.destLocation != null && mainAct.pickUpLocation != null) {
                        handleMapAnimation(responseString, new LatLng(mainAct.pickUpLocation.getLatitude(), mainAct.pickUpLocation.getLongitude()), new LatLng(mainAct.destLocation.getLatitude(), mainAct.destLocation.getLongitude()), "--");
                    }
                } else {
                    mainAct.userLocBtnImgView.performClick();
                }

                isSkip = true;
                if (getActivity() != null) {
                    estimateFare(null, null);
                }

            } else {
                isGoogle = false;

                if (mainAct.stopOverPointsList.size() > 2 && mainAct.isMultiStopOverEnabled()) {

                    if (finalPointlist.size() > 0) {
                        ArrayList<Stop_Over_Points_Data> finalAllPointlist = new ArrayList<>();
                        finalAllPointlist = new ArrayList<>();
                        finalAllPointlist.add(mainAct.stopOverPointsList.get(0));
                        finalAllPointlist.addAll(finalPointlist);
                        mainAct.stopOverPointsList.clear();
                        mainAct.stopOverPointsList.addAll(finalAllPointlist);
                    }


                    sourceLocation = mainAct.stopOverPointsList.get(0).getDestLatLong();
                    destLocation = mainAct.stopOverPointsList.get(mainAct.stopOverPointsList.size() - 1).getDestLatLong();


                    setWaypoints(generalFunc.getJsonArray(directionlist.get("waypoint_order")));

                    distance = directionlist.get("distance");
                    time = directionlist.get("duration");


                } else {

                    distance = directionlist.get("distance");
                    time = directionlist.get("duration");
                    sourceLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, directionlist.get("s_latitude")), GeneralFunctions.parseDoubleValue(0.0, directionlist.get("s_longitude"))
                    );

                    destLocation = new LatLng(GeneralFunctions.parseDoubleValue(0.0, directionlist.get("d_latitude")), GeneralFunctions.parseDoubleValue(0.0, directionlist.get("d_longitude"))
                    );
                }

                Logger.d("sourceLocation", "::" + sourceLocation + "::destLocation::" + destLocation);

                if (getActivity() != null) {
                    estimateFare(distance, time);
                }


                //temp animation test


                //routesjsonObject.p("routes", directionlist.get("routes"));
                HashMap<String, String> routeMap = new HashMap<>();
                routeMap.put("routes", directionlist.get("routes"));
                responseString = routeMap.toString();

                handleMapAnimation(responseString, sourceLocation, destLocation, "--");
            }


        }

    }

    @Override
    public void geoCodeAddressFound(String address, double latitude, double longitude, String geocodeobject) {

    }

    public void setWaypoints(JSONArray waypoint_order) {


        for (int l = 0; l < waypoint_order.length(); l++) {

            Logger.d("WayPointsArray", "::" + generalFunc.getJsonValue(waypoint_order, l));
            int ordering = generalFunc.parseIntegerValue(0, generalFunc.getJsonValue(waypoint_order, l).toString());
            Logger.d("Api", "waypoint_order sequence : ordering" + ordering);
            wayPointslist.get(l).setSequenceId(ordering);
            destPointlist.get(0).setSequenceId(waypoint_order.length());


            LatLng latLng = wayPointslist.get(l).getDestLatLong();

            Logger.d("Route_Parser", "else");

            MarkerOptions dest_dot_option = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.dot_filled));
            Marker dest_marker = mainAct.getMap().addMarker(dest_dot_option);
            builder.include(dest_marker.getPosition());

        }

        finalPointlist.addAll(wayPointslist);
        finalPointlist.addAll(destPointlist);

        if (finalPointlist.size() > 0) {
            Collections.sort(finalPointlist, new StopOverComparator("SequenceId"));
        }

    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == R.id.minFareArea) {
                openFareEstimateDialog();
            } else if (i == ride_now_btn.getId()) {

                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
                    return;
                }
                if ((mainAct.currentLoadedDriverList != null && mainAct.currentLoadedDriverList.size() < 1) || mainAct.currentLoadedDriverList == null || (cabTypeList != null && cabTypeList.size() < 1) || cabTypeList == null) {

                    buildNoCabMessage(generalFunc.retrieveLangLBl("", "LBL_NO_CARS_AVAIL_IN_TYPE"),
                            generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    return;
                }

                if (cabTypeList.get(selpos).get("ePoolStatus").equalsIgnoreCase("Yes") && !mainAct.isDestinationAdded) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DESTINATION_REQUIRED_POOL"));
                    return;

                }


                if (isRouteFail && !mainAct.isMultiDelivery()) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
                    return;
                }

                // if (!ridenowclick) {
//                if (!isOutStandingDailogShow) {
////
////                    if (generalFunc.getJsonValue("fOutStandingAmount", userProfileJson) != null &&
////                            GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("fOutStandingAmount", userProfileJson)) > 0) {
////                        outstandingDialog(true);
////                        //  ridenowclick = true;
////                        return;
////
////                    }
////                }
                //  }


                // isOutStandingDailogShow = false;


                // if (!ridenowclick) {

                mainAct.setCabReqType(Utils.CabReqType_Now);


//                    if (mainAct.getDestinationStatus()) {
//                        String destLocAdd = mainAct != null ? (mainAct.getDestAddress().equals(
//                                generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT")) ? "" : mainAct.getDestAddress()) : "";
//                        if (destLocAdd.equals("")) {
//                            return;
//                        }
//                    }

                if (!isCardValidated && APP_PAYMENT_MODE.equalsIgnoreCase("Card") &&
                        SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    isCardnowselcted = true;
                    isCardlaterselcted = false;
                    checkCardConfig();
                    return;
                }


                if (mainAct.isDeliver(mainAct.getCurrentCabGeneralType())) {
                    if (!mainAct.getDestinationStatus() && !mainAct.isMultiDelivery()) {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please add your destination location " +
                                "to deliver your package.", "LBL_ADD_DEST_MSG_DELIVER_ITEM"));
                        return;
                    }
                    Checkpickupdropoffrestriction();
                    // mainAct.setDeliverySchedule();
                    return;
                }

                // ridenowclick = true;

                if (cabTypeList.get(selpos).get("ePoolStatus").equalsIgnoreCase("Yes")) {
                    rentalarea.setVisibility(View.GONE);
                    poolArea.setVisibility(View.VISIBLE);
                    mainContentArea.setVisibility(View.GONE);


                    double totalFare = GeneralFunctions.parseDoubleValue(0, cabTypeList.get(selpos).get("FinalFare"));
                    double seatVal = GeneralFunctions.parseDoubleValue(1, poolSeatsList.get(seatsSelectpos));
                    if (seatVal > 1) {
                        double res = (totalFare / 100.0f) * GeneralFunctions.parseDoubleValue(0, mainAct.cabTypesArrList.get(selpos).get("fPoolPercentage"));
                        res = res + totalFare;
                        poolFareTxt.setText(cabTypeList.get(selpos).get("currencySymbol") + " " + String.format("%.2f", (float) res));
                    } else {
                        poolFareTxt.setText(cabTypeList.get(selpos).get("total_fare"));
                    }

                    return;
                }

                if (!mainAct.getCabReqType().equals(Utils.CabReqType_Later)) {
                    //  mainAct.requestPickUp();


                    if (cabTypeList.get(selpos).get("eRental") != null && !cabTypeList.get(selpos).get("eRental").equalsIgnoreCase("") &&
                            cabTypeList.get(selpos).get("eRental").equalsIgnoreCase("Yes")) {

                        Bundle bn = new Bundle();
                        bn.putString("address", mainAct.pickUpLocationAddress);
                        bn.putString("vVehicleType", cabTypeList.get(selpos).get("vRentalVehicleTypeName"));
                        bn.putString("iVehicleTypeId", cabTypeList.get(selpos).get("iVehicleTypeId"));
                        bn.putString("vLogo", cabTypeList.get(selpos).get("vLogo1"));
                        bn.putString("eta", etaTxt.getText().toString());
                        bn.putString("eMoto", mainAct.eShowOnlyMoto);
                        bn.putString("PromoCode", appliedPromoCode);
                        bn.putBoolean("eFly", mainAct.eFly);


                        new StartActProcess(getActContext()).startActForResult(RentalDetailsActivity.class, bn, RENTAL_REQ_CODE);
                        return;

                    }

                    // ride_now_btn.setEnabled(false);
                    //  ride_now_btn.setClickable(false);


                    mainAct.continuePickUpProcess();
                } else {
                    //   ride_now_btn.setEnabled(false);
                    // ride_now_btn.setClickable(false);


                    mainAct.setRideSchedule();
                }

//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ridenowclick = false;
//                        }
//                    }, 500);
                // }
            } else if (i == img_ridelater.getId()) {
                try {

                    if (mainAct.stopOverPointsList.size() > 2) {
                        generalFunc.showMessage(carTypeRecyclerView, generalFunc.retrieveLangLBl("", "LBL_REMOVE_MULTI_STOP_OVER_TXT"));
                        return;
                    }


                    if (mProgressBar.getVisibility() == View.VISIBLE) {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
                        return;
                    }


                    if (!cabTypeList.get(selpos).get("eRental").equalsIgnoreCase("Yes")) {
                        if (mainAct.destAddress == null || mainAct.destAddress.equalsIgnoreCase("")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Destination is required to create scheduled booking.", "LBL_DEST_REQ_FOR_LATER"));

                            return;
                        }
                    }

                    if (isRouteFail && !mainAct.isMultiDelivery()) {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Route not found", "LBL_DEST_ROUTE_NOT_FOUND"));
                        return;
                    }
//                    if (!isOutStandingDailogShow) {
//                        if (generalFunc.getJsonValue("fOutStandingAmount", userProfileJson) != null &&
//                                GeneralFunctions.parseIntegerValue(0, generalFunc.getJsonValue("fOutStandingAmount", userProfileJson)) > 0) {
//                            outstandingDialog(false);
//                            return;
//
//                        }
//                    }


//                if (!ridelaterclick) {
//                    ridelaterclick = true;
                    if (cabTypeList.size() > 0) {
                        if (!isCardValidated && APP_PAYMENT_MODE.equalsIgnoreCase("Card") && !mainAct.isMultiDelivery()) {
                            isCardlaterselcted = true;
                            isCardnowselcted = false;
                            checkCardConfig();
                            return;
                        }
                        //  ride_now_btn.setEnabled(false);
                        // ride_now_btn.setTextColor(Color.parseColor("#BABABA"));
                        //  ride_now_btn.setClickable(false);
                        mainAct.chooseDateTime();
                        //      }
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ridelaterclick = false;
//                        }
//                    }, 200);
                    }
                } catch (Exception e) {

                }
            } else if (i == R.id.organizationArea) {
                paymentArea.performClick();
            } else if (i == R.id.paymentArea) {

                if (generalFunc.getJsonValueStr("ENABLE_CORPORATE_PROFILE", mainAct.obj_userProfile).equalsIgnoreCase("Yes") && mainAct.getCurrentCabGeneralType().equalsIgnoreCase("Ride")) {

                    Bundle bn = new Bundle();
                    bn.putString("isWallet", mainAct.eWalletDebitAllow);
                    bn.putBoolean("isCash", mainAct.isCashSelected);
                    bn.putString("ePaymentBy", mainAct.ePaymentBy);
                    bn.putString("selectReasonId", mainAct.selectReasonId);
                    bn.putString("vReasonTitle", mainAct.vReasonTitle);
                    bn.putString("iUserProfileId", mainAct.iUserProfileId);
                    bn.putString("iOrganizationId", mainAct.iOrganizationId);
                    bn.putString("vProfileEmail", mainAct.vProfileEmail);
                    bn.putString("vProfileName", mainAct.vProfileName);
                    bn.putString("vReasonName", mainAct.vReasonName);
                    bn.putInt("selectPos", mainAct.selectPos);
                    bn.putString("vImage", mainAct.vImage);
                    bn.putString("vProfileName", mainAct.vProfileName);
                    bn.putBoolean("isRide", true);

                    //  new StartActProcess(getActContext()).startActForResult(BusinessSelectPaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);
                    new StartActProcess(getActContext()).startActForResult(ProfilePaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);
                    return;
                }

                if (payTypeSelectArea.getVisibility() == View.VISIBLE) {
                    hidePayTypeSelectionArea();
                } else if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash-Card")) {

                    Bundle bn = new Bundle();
                    bn.putString("isWallet", mainAct.eWalletDebitAllow);
                    bn.putBoolean("isCash", mainAct.isCashSelected);
                    //  new StartActProcess(getActContext()).startActForResult(BusinessSelectPaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);
                    new StartActProcess(getActContext()).startActForResult(ProfilePaymentActivity.class, bn, Utils.SELECT_ORGANIZATION_PAYMENT_CODE);
                }


//                if (payTypeSelectArea.getVisibility() == View.VISIBLE) {
//                    hidePayTypeSelectionArea();
//                } else {
//                    if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash-Card")) {
//
//                        if (rentalTypeList.size() > 0 && !mainAct.iscubejekRental) {
//                            Runnable r = new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        if (rentPkgImage.getVisibility() == View.VISIBLE) {
//                                            mainAct.setPanelHeight(335);
//                                        } else {
//                                            mainAct.setPanelHeight(280);
//
//                                        }
//                                    } catch (Exception e2) {
//                                        new Handler().postDelayed(this, 20);
//                                    }
//                                }
//                            };
//                            new Handler().postDelayed(r, 20);
//                        } else {
//                            mainAct.setPanelHeight(283);
//                        }
//                        payTypeSelectArea.setVisibility(View.VISIBLE);
//                        cashcardarea.setVisibility(View.GONE);
//                    } else {
//
//
//                        if (rentalTypeList.size() > 0 && !mainAct.iscubejekRental) {
//                            Runnable r = new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    try {
//                                        mainAct.setPanelHeight(335 - 55);
//                                    } catch (Exception e2) {
//                                        new Handler().postDelayed(this, 20);
//                                    }
//                                }
//                            };
//                            new Handler().postDelayed(r, 20);
//                        } else {
//                            mainAct.setPanelHeight(283 - 48);
//                        }
//                    }
//                }

            } else if (i == R.id.promoArea) {
                // showPromoBox();
                Bundle bn = new Bundle();
                bn.putString("CouponCode", appliedPromoCode);
                bn.putString("eType", mainAct.getCurrentCabGeneralType());
                bn.putBoolean("eFly", mainAct.eFly);
                new StartActProcess(getActContext()).startActForResult(CabSelectionFragment.this, CouponActivity.class, Utils.SELECT_COUPON_REQ_CODE, bn);
            } else if (i == R.id.cardarea) {
                hidePayTypeSelectionArea();
                if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    setCashSelection();
                    checkCardConfig();
                } else {
                    isCardSelect = true;
                    setWalletSelection();

                }
                //   }

            } else if (i == R.id.casharea) {
                isCardSelect = false;
                hidePayTypeSelectionArea();
                setCashSelection();
            } else if (i == R.id.rentalBackImage) {

                mainAct.isRental = false;
                mainAct.iscubejekRental = false;

                if (mainAct.loadAvailCabs != null) {
                    mainAct.loadAvailCabs.checkAvailableCabs();
                }
                selpos = 0;
                iRentalPackageId = "";
                lstSelectpos = 0;
                cabTypeList = (ArrayList<HashMap<String, String>>) tempCabTypeList.clone();
                mainAct.setCabTypeList(cabTypeList);
                tempCabTypeList.clear();
                tempCabTypeList = (ArrayList<HashMap<String, String>>) cabTypeList.clone();
                isRental = false;
                if (cabTypeList.size() > 0) {
                    adapter.setSelectedVehicleTypeId(cabTypeList.get(0).get("iVehicleTypeId"));
                    mainAct.selectedCabTypeId = cabTypeList.get(0).get("iVehicleTypeId");
                    adapter.setRentalItem(cabTypeList);
                    mainAct.changeCabType(mainAct.selectedCabTypeId);
                    adapter.notifyDataSetChanged();
                }
                rentalBackImage.setVisibility(View.GONE);
                rentalPkgDesc.setVisibility(View.GONE);


                rentalPkg.setVisibility(View.VISIBLE);
                rentalarea.setVisibility(View.VISIBLE);
                //rentPkgImage.setVisibility(View.VISIBLE);
                //rentBackPkgImage.setVisibility(View.VISIBLE);
                android.view.animation.Animation bottomUp = AnimationUtils.loadAnimation(getActContext(),
                        R.anim.slide_up_anim);
                carTypeRecyclerView.startAnimation(bottomUp);

                showBookingLaterArea();

                if (!mainAct.iscubejekRental) {
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                mainAct.setPanelHeight(280);

                               /* RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (mainAct.userLocBtnImgView).getLayoutParams();
                                params.bottomMargin = Utils.dipToPixels(getActContext(), 300);*/
                            } catch (Exception e2) {
                                new Handler().postDelayed(this, 20);
                            }
                        }
                    };
                    new Handler().postDelayed(r, 20);
                }

            } else if (i == R.id.rentalPkg) {


                mainAct.isRental = true;
                mainAct.iscubejekRental = true;

                if (mainAct.loadAvailCabs != null) {
                    mainAct.loadAvailCabs.checkAvailableCabs();
                }

                selpos = 0;
                iRentalPackageId = "";
                lstSelectpos = 1;
                tempCabTypeList.clear();
                tempCabTypeList = (ArrayList<HashMap<String, String>>) cabTypeList.clone();
                cabTypeList.clear();
                cabTypeList = (ArrayList<HashMap<String, String>>) rentalTypeList.clone();
                adapter.setRentalItem(cabTypeList);
                isRental = true;
                if (cabTypeList.size() > 0) {
                    adapter.setSelectedVehicleTypeId(cabTypeList.get(0).get("iVehicleTypeId"));
                    mainAct.selectedCabTypeId = cabTypeList.get(0).get("iVehicleTypeId");
                    mainAct.changeCabType(mainAct.selectedCabTypeId);
                    adapter.notifyDataSetChanged();
                }
                rentalPkgDesc.setVisibility(View.VISIBLE);

                rentalBackImage.setVisibility(View.VISIBLE);
                rentalPkg.setVisibility(View.GONE);
                rentalarea.setVisibility(View.GONE);
                rentPkgImage.setVisibility(View.GONE);
                rentBackPkgImage.setVisibility(View.GONE);

                android.view.animation.Animation bottomUp = AnimationUtils.loadAnimation(getActContext(),
                        R.anim.slide_up_anim);
                carTypeRecyclerView.startAnimation(bottomUp);

                showRideLaterBtn(false);


                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            mainAct.setPanelHeight(270);
                        } catch (Exception e2) {
                            new Handler().postDelayed(this, 20);
                        }
                    }
                };
                new Handler().postDelayed(r, 20);
            } else if (i == R.id.rentPkgImage) {
                rentalPkg.performClick();
            } else if (i == R.id.poolBackImage) {
                if (rentalTypeList != null && rentalTypeList.size() > 0 && !mainAct.iscubejekRental) {
                    rentalarea.setVisibility(View.VISIBLE);

                }
                poolArea.setVisibility(View.GONE);
                mainContentArea.setVisibility(View.VISIBLE);
                if (seatsSelectionAdapter != null) {
                    seatsSelectpos = 0;

                    seatsSelectionAdapter.setSelectedSeat(seatsSelectpos);
                    seatsSelectionAdapter.notifyDataSetChanged();

                    if (cabTypeList != null && cabTypeList.get(selpos).get("total_fare") != null && !cabTypeList.get(selpos).get("total_fare").equalsIgnoreCase("")) {
                        poolFareTxt.setText(cabTypeList.get(selpos).get("total_fare"));
                    }

                }
                /*cashCardArea.setVisibility(View.VISIBLE);
                carTypeRecyclerView.setVisibility(View.VISIBLE);*/
            } else if (i == confirm_seats_btn.getId()) {


                mainAct.continuePickUpProcess();
            }

        }

    }

    public void setOrganizationName(String name, boolean isOrganization) {
        organizationTxt.setText(name);
        if (!isOrganization) {
            LinearLayout.LayoutParams organizationLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            organizationLayoutParams.setMargins(0, 0, 0, -Utils.dpToPx(getActContext(), 5));
            Typeface face = Typeface.createFromAsset(getActContext().getAssets(), "fonts/Poppins_Regular.ttf");
            organizationArea.setLayoutParams(organizationLayoutParams);
            organizationTxt.setTypeface(face);
            organizationTxt.setTextColor(Color.parseColor("#6d6d6d"));
            organizationTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        } else {
            LinearLayout.LayoutParams organizationLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            organizationLayoutParams.setMargins(0, 0, 0, 0);
            organizationArea.setLayoutParams(organizationLayoutParams);

            Typeface face = Typeface.createFromAsset(getActContext().getAssets(), "fonts/Poppins_Medium.ttf");
            organizationTxt.setTypeface(face);
            organizationTxt.setTextColor(Color.parseColor("#2f2f2f"));
            organizationTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            cashRadioBtn.setVisibility(View.GONE);
            payTypeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
            cardRadioBtn.setVisibility(View.GONE);
            payTypeTxt.setVisibility(View.GONE);
            cashRadioBtn.setVisibility(View.GONE);
            isCardValidated = true;
            payImgView.setImageResource(R.drawable.ic_business_pay);
//            payImgView.setColorFilter(getResources().getColor(R.color.businesspay), PorterDuff.Mode.SRC_IN);

        }
    }

    public void setPaymentType(String type) {
        payTypeTxt.setVisibility(View.VISIBLE);
        if (type.equalsIgnoreCase("Cash")) {
            cashRadioBtn.setVisibility(View.VISIBLE);
            cardRadioBtn.setVisibility(View.GONE);
            setCashSelection();
        } else if (type.equalsIgnoreCase("Card")) {

            if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                cashRadioBtn.setVisibility(View.GONE);
                cardRadioBtn.setVisibility(View.VISIBLE);
                setWalletSelection();
                isCardValidated = true;
            } else {
                cashRadioBtn.setVisibility(View.GONE);
                cardRadioBtn.setVisibility(View.VISIBLE);
                setCardSelection();
                isCardValidated = true;
            }
        }
    }


    public boolean handleRnetalView(String selectedTime) {
        if (cabTypeList.get(selpos).get("eRental") != null && !cabTypeList.get(selpos).get("eRental").equalsIgnoreCase("") &&
                cabTypeList.get(selpos).get("eRental").equalsIgnoreCase("Yes")) {

            Bundle bn = new Bundle();
            bn.putString("address", mainAct.pickUpLocationAddress);
            bn.putString("vVehicleType", cabTypeList.get(selpos).get("vRentalVehicleTypeName"));
            bn.putString("iVehicleTypeId", cabTypeList.get(selpos).get("iVehicleTypeId"));
            bn.putString("vLogo", cabTypeList.get(selpos).get("vLogo1"));
            bn.putString("eta", etaTxt.getText().toString());
            bn.putString("selectedTime", selectedTime);
            bn.putString("eMoto", mainAct.eShowOnlyMoto);
            bn.putString("PromoCode", appliedPromoCode);
            bn.putBoolean("eFly", mainAct.eFly);

            new StartActProcess(getActContext()).startActForResult(
                    RentalDetailsActivity.class, bn, RENTAL_REQ_CODE);
            return true;


        }
        return false;
    }

    public PolylineOptions createCurveRoute(LatLng origin, LatLng dest) {

        double distance = SphericalUtil.computeDistanceBetween(origin, dest);
        double heading = SphericalUtil.computeHeading(origin, dest);
        double halfDistance = distance > 0 ? (distance / 2) : (distance * DEFAULT_CURVE_ROUTE_CURVATURE);

        // Calculate midpoint position
        LatLng midPoint = SphericalUtil.computeOffset(origin, halfDistance, heading);

        // Calculate position of the curve center point
        double sqrCurvature = DEFAULT_CURVE_ROUTE_CURVATURE * DEFAULT_CURVE_ROUTE_CURVATURE;
        double extraParam = distance / (4 * DEFAULT_CURVE_ROUTE_CURVATURE);
        double midPerpendicularLength = (1 - sqrCurvature) * extraParam;
        double r = (1 + sqrCurvature) * extraParam;

        LatLng circleCenterPoint = SphericalUtil.computeOffset(midPoint, midPerpendicularLength, heading + 90.0);

        // Calculate heading between circle center and two points
        double headingToOrigin = SphericalUtil.computeHeading(circleCenterPoint, origin);

        // Calculate positions of points on the curve
        double step = Math.toDegrees(Math.atan(halfDistance / midPerpendicularLength)) * 2 / DEFAULT_CURVE_POINTS;
        //Polyline options
        PolylineOptions options = new PolylineOptions();

        for (int i = 0; i < DEFAULT_CURVE_POINTS; ++i) {
            LatLng pi = SphericalUtil.computeOffset(circleCenterPoint, r, headingToOrigin + i * step);
            options.add(pi);
        }
        return options;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.SELECT_COUPON_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String couponCode = data.getStringExtra("CouponCode");
            if (couponCode == null) {
                couponCode = "";
            }
            appliedPromoCode = couponCode;

            findRoute("--");
        }


    }

    public void manageisRentalValue() {
        if (rentalarea.getVisibility() == View.VISIBLE || rentalBackImage.getVisibility() == View.VISIBLE) {
            mainAct.isRental = false;
            mainAct.iscubejekRental = false;

        }
    }
}


