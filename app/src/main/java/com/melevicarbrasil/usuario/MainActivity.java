package com.melevicarbrasil.usuario;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.files.SkyPortsRecyclerAdapter;
import com.adapter.files.UberXOnlineDriverListAdapter;
import com.datepicker.files.SlideDateTimeListener;
import com.datepicker.files.SlideDateTimePicker;
import com.dialogs.OpenListView;
import com.dialogs.RequestNearestCab;
import com.fragments.CabSelectionFragment;
import com.fragments.DriverAssignedHeaderFragment;
import com.fragments.DriverDetailFragment;
import com.fragments.MainHeaderFragment;
import com.fragments.PickUpLocSelectedFragment;
import com.general.files.AddDrawer;
import com.general.files.AppFunctions;
import com.general.files.ConfigPubNub;
import com.general.files.CreateAnimation;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.GetLocationUpdates;
import com.general.files.HashMapComparator;
import com.general.files.InternetConnection;
import com.general.files.LoadAvailableCab;
import com.general.files.LocalNotification;
import com.general.files.LockableBottomSheetBehavior;
import com.general.files.MapAnimator;
import com.general.files.MyApp;
import com.general.files.OpenAdvertisementDialog;
import com.general.files.StartActProcess;
import com.general.files.SuccessDialog;
import com.general.files.UpdateFrequentTask;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.like.LikeButton;
import com.model.ContactModel;
import com.model.Multi_Delivery_Data;
import com.model.Stop_Over_Points_Data;
import com.pubnub.api.enums.PNStatusCategory;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.utils.AnimateMarker;
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
import com.view.simpleratingbar.SimpleRatingBar;
import com.view.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GetLocationUpdates.LocationUpdates, SkyPortsRecyclerAdapter.OnSelectListener {
    public static String valorcorrida="";
    public GeneralFunctions generalFunc;
    public static int RENTAL_REQ_CODE = 1234;
    public String userProfileJson = "";
    public String currentGeoCodeObject = "";
    public SlidingUpPanelLayout sliding_layout;
    public ImageView userLocBtnImgView;
    public ImageView userTripBtnImgView;
    public Location userLocation;
    public ArrayList<HashMap<String, String>> currentLoadedDriverList;
    public ImageView emeTapImgView;

    public AddDrawer addDrawer;
    public CabSelectionFragment cabSelectionFrag;
    public LoadAvailableCab loadAvailCabs;
    public Location pickUpLocation;
    public String selectedCabTypeId = "";
    public boolean isDestinationAdded = false;
    public String destLocLatitude = "";
    public String destLocLongitude = "";
    public String destAddress = "";
    public boolean isCashSelected = true;
    public String pickUpLocationAddress = "";
    public ArrayList<Stop_Over_Points_Data> stopOverPointsList = new ArrayList<>();
    public String app_type = "Ride";
    public DrawerLayout mDrawerLayout;
    public AVLoadingIndicatorView loaderView;
    public ImageView pinImgView;
    public ArrayList<HashMap<String, String>> cabTypesArrList = new ArrayList<>();
    public boolean iswallet = false;
    public boolean isUserLocbtnclik = false;
    public String tempPickupGeoCode = "";
    public String tempDestGeoCode = "";
    public boolean isUfx = false;
    public String uberXAddress = "";
    public double uberXlat = 0.0;
    public double uberXlong = 0.0;
    public boolean ishandicap = false;
    public boolean isChildSeat = false;
    public boolean isWheelChair = false;
    public boolean isfemale = false;
    public String timeval = "";
    public DriverAssignedHeaderFragment driverAssignedHeaderFrag;
    public RequestNearestCab requestNearestCab;
    public boolean isDestinationMode = false;
    public LinearLayout ridelaterHandleView;
    public boolean isUfxRideLater = false;
    public String bookingtype = "";
    public String selectedprovidername = "";
    public String vCurrencySymbol = "";
    public String UfxAmount = "";
    public boolean noCabAvail = false;
    public Location destLocation;
    public boolean isDriverAssigned = false;
    public GenerateAlertBox noCabAvailAlertBox;
    public JSONObject obj_userProfile;
    public String SelectDate = "";
    public String sdate = "";
    public String Stime = "";
    public boolean isFirstTime = true;
    public String ACCEPT_CASH_TRIPS = "";
    MTextView titleTxt;
    public SupportMapFragment map;
    GetLocationUpdates getLastLocation;
    GoogleMap gMap;
    boolean isFirstLocation = true;
    RelativeLayout dragView;
    RelativeLayout mainArea;
    View otherArea;
    FrameLayout mainContent;
    RelativeLayout uberXDriverListArea;
    public MainHeaderFragment mainHeaderFrag;
    DriverDetailFragment driverDetailFrag;
    ArrayList<HashMap<String, String>> cabTypeList;
    ArrayList<HashMap<String, String>> uberXDriverList = new ArrayList<>();
    public HashMap<String, String> driverAssignedData;
    public String assignedDriverId = "";
    public String assignedTripId = "";
    String DRIVER_REQUEST_METHOD = "All";
    MTextView uberXNoDriverTxt;
    SelectableRoundedImageView driverImgView;
    UpdateFrequentTask allCabRequestTask;
    SendNotificationsToDriverByDist sendNotificationToDriverByDist;
    String selectedDateTime = "";
    String selectedDateTimeZone = "";
    public String cabRquestType = Utils.CabReqType_Now; // Later OR Now
    View rideArea;
    View deliverArea;

    Intent deliveryData;
    String eTripType = "";
    androidx.appcompat.app.AlertDialog alertDialog_surgeConfirm;
    String required_str = "";
    UberXOnlineDriverListAdapter uberXOnlineDriverListAdapter;
    RecyclerView uberXOnlineDriversRecyclerView;
    LinearLayout driver_detail_bottomView;
    String markerId = "";
    boolean isMarkerClickable = true;
    String currentUberXChoiceType = Utils.Cab_UberX_Type_List;
    String vUberXCategoryName = "";
    Handler ufxFreqTask = null;
    String tripId = "";
    String RideDeliveryType = "";
    SelectableRoundedImageView deliverImgView, deliverImgViewsel, rideImgView, rideImgViewsel, otherImageView, otherImageViewsel;
    PickUpLocSelectedFragment pickUpLocSelectedFrag;
    double tollamount = 0.0;
    String tollcurrancy = "";
    boolean isrideschedule = false;
    boolean isreqnow = false;
    ImageView prefBtnImageView;
    androidx.appcompat.app.AlertDialog pref_dialog;
    androidx.appcompat.app.AlertDialog tolltax_dialog;

    boolean isTollCostdilaogshow = false;
    boolean istollIgnore = false;
    boolean isnotification = false;
    boolean isdelivernow = false;
    boolean isdeliverlater = false;
    LinearLayout ridelaterView;
    MTextView rideLaterTxt;
    MTextView btn_type_ridelater;
    public boolean isTripStarted = false;
    boolean isTripEnded = false;
    boolean isDriverArrived = false;
    InternetConnection intCheck;
    boolean isfirstsearch = true;
    boolean isufxpayment = false;
    String appliedPromoCode = "";
    String userComment = "";
    boolean schedulrefresh = false;
    String iCabBookingId = "";
    boolean isRebooking = false;
    String type = "";
    //Noti
    boolean isufxbackview = false;
    String payableAmount = "";
    private String SelectedDriverId = "";
    private String tripStatus = "";
    private String currentTripId = "";
    private ActionBarDrawerToggle mDrawerToggle;

    public RelativeLayout rootRelView;
    public static String PACKAGE_TYPE_ID_KEY = "PACKAGE_TYPE_ID";

    public boolean isUserTripClick = false;
    boolean isTripActive = false;

    public boolean isFirstZoomlevel = true;

    LinearLayout rduTollbar;
    ImageView backImgView;
    public boolean isMenuImageShow = true;

    public boolean isRental = false;
    public boolean iscubejekRental = false;
    public String eShowOnlyMoto = "";
    public boolean eFly = false;

    public double pickUp_tmpLatitude = 0.0;
    public double pickUp_tmpLongitude = 0.0;
    public String pickUp_tmpAddress = "";

    GenerateAlertBox reqSentErrorDialog = null;
    public String eWalletDebitAllow = "No";
    public String vProfileName = "";
    public String vReasonName = "";
    MTextView filterTxtView;
    public LinearLayout llFilter;
    public boolean isMultiDeliveryTrip = false;
    public String selectedSortValue = "";
    public String selectedSort = "";
    public String IS_PROVIDER_ONLINE = "";

    public String iUserProfileId = "";
    public String iOrganizationId = "";
    public String vProfileEmail = "";
    public String ePaymentBy = "Passenger";
    boolean iswalletShow = true;

    public String selectReasonId = "";
    public String vReasonTitle = "";

    public int selectPos = 0;
    public String vImage = "";
    public boolean isPoolCabTypeIdSelected = false;
    public String eWalletIgnore = "No";
    public String SERVICE_PROVIDER_FLOW = "";

    UpdateFrequentTask allNonFavCabRequestTask;
    boolean requestSentToFavDrivers = false;
    String[] driverIDS = new String[2];

    LinearLayout homeArea, workArea;
    MTextView homeTxt, workTxt;

    CardView cardArea;
    MTextView destSelectTxt;
    LinearLayout destSelectArea;
    private int filterPosition = 0;

    ArrayList<HashMap<String, String>> arrayList;

    private static final String TAG = "MainActivity";

    public ArrayList<Marker> map_SkyPort_MarkerList = new ArrayList<>();
    ArrayList<View> map_SkyPort_ViewList = new ArrayList<>();

    public String iFromStationId = "";
    public String iToStationId = "";
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    int selectedMarkerPos = -1;
    boolean prevSatate;
    public Intent data;
    int height;
    public int staticPanelHeight;
    public int staticFlyPanelHeight;
    public boolean isPickup;

    public RecyclerView skyPortsListRecyclerView;
    SkyPortsRecyclerAdapter dateAdapter;
    private MTextView tvTitle;
    private MTextView tvNoDetails;
    private MTextView tvSelectedAddress, addFlyStationNote;
    private LinearLayout changeArea;
    private MButton btn_type2;
    int submitBtnId;
    public Location finalAddressLocation = null;
    public String finalAddress = "";
    public String finaliLocationId = "";
    private ArrayList<HashMap<String, String>> dateList = new ArrayList<>();
    int pos = -1;
    private CustomLinearLayoutManager customLayoutManager;
    LockableBottomSheetBehavior bottomSheetBehavior;

    int leftMargin = 0;
    int rightMargin = 0;
    boolean isPrefrenceEnable = false;
//
private static final String[] payment = new String[]{
        "Cartão de Crédito", "Cartão de Débito", "Pix", "Dinheiro"
};

    private boolean[] clicked_sosmed = new boolean[payment.length];

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        cabSelectionFrag = null;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        staticPanelHeight = (int) (height * 0.5);


        cardArea = (CardView) findViewById(R.id.cardArea);
        destSelectTxt = (MTextView) findViewById(R.id.destSelectTxt);
        destSelectArea = (LinearLayout) findViewById(R.id.destSelectArea);
        destSelectArea.setOnClickListener(new setOnClickList());
        userLocBtnImgView = (ImageView) findViewById(R.id.userLocBtnImgView);


        rootRelView = (RelativeLayout) findViewById(R.id.rootRelView);
        homeArea = (LinearLayout) findViewById(R.id.homeArea);
        workArea = (LinearLayout) findViewById(R.id.workArea);
        homeTxt = (MTextView) findViewById(R.id.homeTxt);
        workTxt = (MTextView) findViewById(R.id.workTxt);
        homeArea.setOnClickListener(new setOnClickList());
        homeArea.setOnClickListener(new setOnClickList());
        workArea.setOnClickListener(new setOnClickList());

        isTripActive = getIntent().getBooleanExtra("isTripActive", false);
        rduTollbar = (LinearLayout) findViewById(R.id.rduTollbar);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        prefBtnImageView = (ImageView) findViewById(R.id.prefBtnImageView);
        backImgView.setOnClickListener(new setOnClickList());

        filterTxtView = (MTextView) findViewById(R.id.filterTxtView);
        llFilter = (LinearLayout) findViewById(R.id.llFilter);
        filterTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT"));
        filterTxtView.setOnClickListener(new setOnClickList());

        selectedSortValue = generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT");

        if (getIntent().getStringExtra("iCabBookingId") != null) {
            iCabBookingId = getIntent().getStringExtra("iCabBookingId");
        }

        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
            bookingtype = getIntent().getStringExtra("type");
        }
        manageRideArea(true);
        getUserProfileJson();
        flyElementsInit();

        SERVICE_PROVIDER_FLOW = generalFunc.getJsonValue("SERVICE_PROVIDER_FLOW", userProfileJson);

        app_type = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);

        Log.d(TAG, "onCreate: 1");
        if (app_type.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {

            Log.d(TAG, "onCreate: 2");

            String advertise_banner_data = generalFunc.getJsonValue("advertise_banner_data", userProfileJson);
            if (advertise_banner_data != null && !advertise_banner_data.equalsIgnoreCase("")) {

                if (generalFunc.getJsonValue("image_url", advertise_banner_data) != null && !generalFunc.getJsonValue("image_url", advertise_banner_data).equalsIgnoreCase("")) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("image_url", generalFunc.getJsonValue("image_url", advertise_banner_data));
                    map.put("tRedirectUrl", generalFunc.getJsonValue("tRedirectUrl", advertise_banner_data));
                    map.put("vImageWidth", generalFunc.getJsonValue("vImageWidth", advertise_banner_data));
                    map.put("vImageHeight", generalFunc.getJsonValue("vImageHeight", advertise_banner_data));
                    new OpenAdvertisementDialog(getActContext(), map, generalFunc);
                }


            }
        } else {
            userLocBtnImgView.setVisibility(View.GONE);
        }

        isRebooking = getIntent().getBooleanExtra("isRebooking", false);
        intCheck = new InternetConnection(getActContext());
        isufxpayment = getIntent().getBooleanExtra("isufxpayment", false);

        isUfx = getIntent().getBooleanExtra("isufx", false);

        isnotification = getIntent().getBooleanExtra("isnotification", false);


        if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
            Log.d(TAG, "onCreate: 3");
            RideDeliveryType = Utils.CabGeneralType_Ride;
        }

        String selType = getIntent().getStringExtra("selType");
        if (selType != null && !isTripActive) {

            if (getIntent().getBooleanExtra("emoto", false)) {
                eShowOnlyMoto = "Yes";
            }

            if (getIntent().getBooleanExtra("eFly", false)) {
                eFly = true;
            }
            RideDeliveryType = selType;
            rduTollbar.setVisibility(View.GONE);
            //bug_002 start
            if (selType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_RIDE"));

                if (getIntent().getBooleanExtra("emoto", false)) {
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_RIDE"));
                } else if (eFly) {
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_FLY_RIDE"));
                }
                //rduTollbar.setVisibility(View.VISIBLE);
            } else if (selType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SERVICE_PROVIDER_TXT"));


            } else if (selType.equalsIgnoreCase("rental")) {
                isRental = true;
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_RENTAL"));
                if (getIntent().getBooleanExtra("emoto", false)) {
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_RENTAL"));
                }
                RideDeliveryType = Utils.CabGeneralType_Ride;
                iscubejekRental = true;
                // rduTollbar.setVisibility(View.VISIBLE);
            } else {


                if (getIntent().hasExtra("fromMulti")) {

//                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));
//                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));

                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_MULTI_OPTION_TITLE_TXT"));
                    manageRideArea(false);
                    rduTollbar.setVisibility(View.VISIBLE);


                } else {
                    manageRideArea(true);
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY"));
                    reSetButton(false);
                }

                prefBtnImageView.setVisibility(View.GONE);
                //rduTollbar.setVisibility(View.VISIBLE);

                if (getIntent().getBooleanExtra("emoto", false)) {
                    if (getIntent().hasExtra("fromMulti")) {
                        manageRideArea(false);
//                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));
//                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_SEND_TXT") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));
                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_SEND_TXT") + "-" + generalFunc.retrieveLangLBl("", "LBL_MULTI_OPTION_TITLE_TXT"));

                    } else {
                        manageRideArea(true);
                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_DELIVERY"));
                        reSetButton(false);
                    }
                }
            }
            //bug_002 stop
            isMenuImageShow = false;
        }

        if (getIntent().hasExtra("tripId")) {
            tripId = getIntent().getStringExtra("tripId");
        }
        String TripDetails = generalFunc.getJsonValueStr("TripDetails", obj_userProfile);

        if (TripDetails != null && !TripDetails.equals("")) {
            tripId = generalFunc.getJsonValue("iTripId", TripDetails);
        }

        mainContent = (FrameLayout) findViewById(R.id.mainContent);
        userLocBtnImgView = (ImageView) findViewById(R.id.userLocBtnImgView);
        userTripBtnImgView = (ImageView) findViewById(R.id.userTripBtnImgView);

        prefrenceButtonEnable();

        if (!isUfx) {
            mainContent.setVisibility(View.VISIBLE);
            userLocBtnImgView.setVisibility(View.VISIBLE);
        } else {
            prefBtnImageView.setVisibility(View.GONE);
        }

        addDrawer = new AddDrawer(getActContext(), userProfileJson, false);

        if (app_type.equalsIgnoreCase("UberX")) {
            Log.d(TAG, "onCreate: 4");
            addDrawer.configDrawer(true);
            selectedCabTypeId = getIntent().getStringExtra("SelectedVehicleTypeId");
            vUberXCategoryName = getIntent().getStringExtra("vCategoryName");
        } else {
            addDrawer.configDrawer(false);
        }


        if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
            Log.d(TAG, "onCreate: 5");
            if (isUfx) {
                selectedCabTypeId = getIntent().getStringExtra("SelectedVehicleTypeId");
                vUberXCategoryName = getIntent().getStringExtra("vCategoryName");

                setMainHeaderView(true);
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (rduTollbar.getVisibility() == View.VISIBLE) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                1, 2) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getActionBar().setTitle("Closed");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // getActionBar().setTitle("Opened");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        ridelaterView = (LinearLayout) findViewById(R.id.ridelaterView);

        uberXNoDriverTxt = (MTextView) findViewById(R.id.uberXNoDriverTxt);
        deliverImgView = (SelectableRoundedImageView) findViewById(R.id.deliverImgView);
        deliverImgViewsel = (SelectableRoundedImageView) findViewById(R.id.deliverImgViewsel);
        rideImgView = (SelectableRoundedImageView) findViewById(R.id.rideImgView);
        rideImgViewsel = (SelectableRoundedImageView) findViewById(R.id.rideImgViewsel);
        otherImageView = (SelectableRoundedImageView) findViewById(R.id.otherImageView);
        otherImageViewsel = (SelectableRoundedImageView) findViewById(R.id.otherImageViewsel);

        rideLaterTxt = (MTextView) findViewById(R.id.rideLaterTxt);

        ridelaterHandleView = (LinearLayout) findViewById(R.id.ridelaterHandleView);

        btn_type_ridelater = (MTextView) findViewById(R.id.btn_type_ridelater);

        if (type.equals(Utils.CabReqType_Now)) {
            btn_type_ridelater.setText(generalFunc.retrieveLangLBl("", "LBL_BOOK_LATER"));
        } else {
            btn_type_ridelater.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        }


        btn_type_ridelater.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("latitude", getIntent().getStringExtra("latitude"));
            bundle.putString("longitude", getIntent().getStringExtra("longitude"));
            bundle.putString("address", getIntent().getStringExtra("address"));
            bundle.putString("iUserAddressId", getIntent().getStringExtra("iUserAddressId"));
            bundle.putString("SelectedVehicleTypeId", getIntent().getStringExtra("SelectedVehicleTypeId"));
            bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));
            bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));

            bundle.putBoolean("isMain", true);
            new StartActProcess(getActContext()).startActForResult(ScheduleDateSelectActivity.class, bundle, Utils.SCHEDULE_REQUEST_CODE);

            schedulrefresh = true;
        });

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 35), 2,
                getActContext().getResources().getColor(R.color.white), deliverImgViewsel);

        deliverImgViewsel.setColorFilter(getActContext().getResources().getColor(R.color.black));

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 30), 2,
                getActContext().getResources().getColor(R.color.white), deliverImgView);

        deliverImgView.setColorFilter(getActContext().getResources().getColor(R.color.black));

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 35), 2,
                getActContext().getResources().getColor(R.color.white), rideImgViewsel);

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 30), 2,
                getActContext().getResources().getColor(R.color.white), rideImgView);

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 35), 2,
                getActContext().getResources().getColor(R.color.white), otherImageViewsel);

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 30), 2,
                getActContext().getResources().getColor(R.color.white), otherImageView);

        loaderView = (AVLoadingIndicatorView) findViewById(R.id.loaderView);
        uberXOnlineDriversRecyclerView = (RecyclerView) findViewById(R.id.uberXOnlineDriversRecyclerView);


        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapV2);
        sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        sliding_layout.setCoveredFadeColor(Color.TRANSPARENT);

        dragView = (RelativeLayout) findViewById(R.id.dragView);
        mainArea = (RelativeLayout) findViewById(R.id.mainArea);
        otherArea = findViewById(R.id.otherArea);
        mainContent = (FrameLayout) findViewById(R.id.mainContent);
        driver_detail_bottomView = (LinearLayout) findViewById(R.id.driver_detail_bottomView);
        pinImgView = (ImageView) findViewById(R.id.pinImgView);

        uberXDriverListArea = (RelativeLayout) findViewById(R.id.uberXDriverListArea);
        emeTapImgView = (ImageView) findViewById(R.id.emeTapImgView);
        rideArea = findViewById(R.id.rideArea);
        deliverArea = findViewById(R.id.deliverArea);

        prefBtnImageView.setOnClickListener(new setOnClickList());

        map.getMapAsync(MainActivity.this);

        setGeneralData();
        setLabels();

        if (generalFunc.isRTLmode()) {
            ((ImageView) findViewById(R.id.deliverImg)).setRotation(-180);
            ((ImageView) findViewById(R.id.rideImg)).setRotation(-180);
            ((ImageView) findViewById(R.id.rideImg)).setScaleY(-1);
            ((ImageView) findViewById(R.id.deliverImg)).setScaleY(-1);
        }


        new CreateAnimation(dragView, getActContext(), R.anim.design_bottom_sheet_slide_in, 100, true).startAnimation();


        userTripBtnImgView.setOnClickListener(new setOnClickList());
        userLocBtnImgView.setOnClickListener(new setOnClickList());
        emeTapImgView.setOnClickListener(new setOnClickList());
        rideArea.setOnClickListener(new setOnClickList());
        deliverArea.setOnClickListener(new setOnClickList());
        otherArea.setOnClickListener(new setOnClickList());

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            String restratValue_str = savedInstanceState.getString("RESTART_STATE");

            if (restratValue_str != null && !restratValue_str.equals("") && restratValue_str.trim().equals("true")) {
                releaseScheduleNotificationTask();
                generalFunc.restartApp();
            }
        }

        generalFunc.deleteTripStatusMessages();


        String eEmailVerified = generalFunc.getJsonValueStr("eEmailVerified", obj_userProfile);
        String ePhoneVerified = generalFunc.getJsonValueStr("ePhoneVerified", obj_userProfile);
        String RIDER_EMAIL_VERIFICATION = generalFunc.getJsonValueStr("RIDER_EMAIL_VERIFICATION", obj_userProfile);
        String RIDER_PHONE_VERIFICATION = generalFunc.getJsonValueStr("RIDER_PHONE_VERIFICATION", obj_userProfile);

        if (/*(!eEmailVerified.equalsIgnoreCase("YES") && RIDER_EMAIL_VERIFICATION.equalsIgnoreCase("Yes")) ||*/
                (!ePhoneVerified.equalsIgnoreCase("YES") && RIDER_PHONE_VERIFICATION.equalsIgnoreCase("Yes"))) {

            Bundle bn = new Bundle();
            if (!eEmailVerified.equalsIgnoreCase("YES") &&
                    !ePhoneVerified.equalsIgnoreCase("YES")) {
                bn.putString("msg", "DO_EMAIL_PHONE_VERIFY");
            } else if (!eEmailVerified.equalsIgnoreCase("YES")) {
                bn.putString("msg", "DO_EMAIL_VERIFY");
            } else if (!ePhoneVerified.equalsIgnoreCase("YES")) {
                bn.putString("msg", "DO_PHONE_VERIFY");
            }

            showMessageWithAction(mainArea, generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);
        }
        arrayList = populateArrayList();
        String imageGet = generalFunc.getJsonValue("vImgName", userProfileJson);

        String userName = generalFunc.getJsonValue("vName", userProfileJson);

        Log.d("Brasil065_Debug", "onCreate: imageUser " + imageGet);

        if(imageGet.equals(""))

        {

            // showDialogo("Olá  " + userName +
            //        ", \n precisamos de uma selfie \n para identificá-lo. \n \n");
            showDialogo(generalFunc.retrieveLangLBl("", "LBL_ALERT_TEXT"));

        }


    }
    public void showDialogo(String msg) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_design);

        TextView text = (TextView) dialog.findViewById(R.id.text_card2);
        text.setText(msg);


        Button dialog_btn = dialog.findViewById(R.id.btn_dialog);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), MyProfileActivity.class);
                startActivity(i);


                dialog.dismiss();


            }


        });

        dialog.show();


    }
    //

    public void openDialog(View v){

        int id = v.getId();

        if (id == R.id.btn_dialog){
            clicked_sosmed = new boolean[payment.length];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Qual a forma de pagamento");
            builder.setMultiChoiceItems(payment, clicked_sosmed, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    clicked_sosmed[which] = isChecked;
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Selecionado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancelar", null);
            builder.show();
        }
    }


    //
    public void manageRideArea(boolean isShow) {
        if (isShow) {
            homeArea.setVisibility(View.VISIBLE);
            workArea.setVisibility(View.VISIBLE);
            cardArea.setVisibility(View.VISIBLE);

            if (gMap != null) {
                gMap.setPadding(0, 0, 0, Utils.dipToPixels(getActContext(), 62));
            }
        } else {
            homeArea.setVisibility(View.GONE);
            workArea.setVisibility(View.GONE);
            cardArea.setVisibility(View.GONE);

        }

    }

    // Fly changes

    public void showSelectionDialog(Intent data, boolean isPickup) {
        try {

            configDestinationMode(!isPickup);

            String destLoc = isPickup ? pickUpLocation != null ? "" + pickUpLocation.getLatitude() : "" : destLocLatitude;

            if (data == null && Utils.checkText(destLoc)) {
                data = new Intent();
                data.putExtra("Address", isPickup ? pickUpLocationAddress : destAddress);
                data.putExtra("Latitude", isPickup ? pickUpLocation != null ? "" + pickUpLocation.getLatitude() : "" : destLocLatitude);
                data.putExtra("Longitude", isPickup ? pickUpLocation != null ? "" + pickUpLocation.getLongitude() : "" : destLocLongitude);
            } else if (data != null) {
                this.data = data;
            }
            this.isPickup = isPickup;

            if (!eFly) {
                return;
            }


            findViewById(R.id.dragView).setVisibility(View.GONE);
//            findViewById(R.id.dragView1).setVisibility(View.VISIBLE);
            findViewById(R.id.DetailsArea).setVisibility(View.VISIBLE);

            if (data != null) {
                staticFlyPanelHeight = (int) (height * 0.60);
                bottomSheetBehavior.setLocked(false);
                bottomSheetBehavior.setPeekHeight(staticFlyPanelHeight);
                if (mainHeaderFrag != null) {
                    mainHeaderFrag.area_source.setVisibility(View.GONE);
                    mainHeaderFrag.area2.setVisibility(View.GONE);
                }
                findViewById(R.id.stationListArea).setVisibility(View.VISIBLE);
                findViewById(R.id.addFlyStationNote).setVisibility(View.GONE);
                findViewById(R.id.popupView).setVisibility(View.VISIBLE);
                findViewById(R.id.swipeArea).setVisibility(View.VISIBLE);

                enableDisableScroll(false);
                setFlySheetHeight();
                setFlyElements(isPickup);
            } else {
                selectedMarkerPos = -1;
                prevSatate = isPickup;
                findViewById(R.id.stationListArea).setVisibility(View.GONE);
                findViewById(R.id.popupView).setVisibility(View.GONE);
                findViewById(R.id.addFlyStationNote).setVisibility(View.VISIBLE);
                findViewById(R.id.swipeArea).setVisibility(View.GONE);

                tvSelectedAddress.setText(!isPickup ? generalFunc.retrieveLangLBl("Add Your Location", "LBL_ADD_DESTINATION_LOCATION_TXT") : generalFunc.retrieveLangLBl("Detecting your Location", "LBL_DETACTING_YOUR_LOCATION"));
                tvTitle.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_DROP_STATION_TXT" : "LBL_FLY_PICKUP_STATION_TXT"));
                btn_type2.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_CONFIRM_LOCATION_TXT" : "LBL_FLY_CONFIRM_PICKUP_TXT"));
                addFlyStationNote.setText(!isPickup ? generalFunc.retrieveLangLBl("Tap on edit icon to enter or select your destination location.", "LBL_ADD_DESTINATION_LOCATION_NOTE_TEXT") : generalFunc.retrieveLangLBl("", "LBL_FETCHING_LOCATION_NOTE_TEXT"));

                staticFlyPanelHeight = (int) (height * 0.28);
                bottomSheetBehavior.setLocked(true);
                bottomSheetBehavior.setPeekHeight(staticFlyPanelHeight);


                setFlySheetHeight();
            }

           /* if (flyStationSelectionFragment == null) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPickup", isPickup);
                flyStationSelectionFragment = new FlyStationSelectionFragment();
                flyStationSelectionFragment.setArguments(bundle);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
                params.bottomMargin = Utils.dipToPixels(getActContext(), 240);

                if (!isFinishing() && !isDestroyed()) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.dragView1, flyStationSelectionFragment).commitAllowingStateLoss();
                }

            } else if (flyStationSelectionFragment != null) {
                flyStationSelectionFragment.setPickup(isPickup);
            }*/


            try {
                super.onPostResume();
            } catch (Exception e) {
                Logger.d("ExceptionFLyResume", e.toString());
            }
        } catch (Exception e) {
            Logger.d("ExceptionFLy", e.toString());
        }

    }

    private void setFlyElements(boolean isPickup) {
        skyPortsListRecyclerView = (RecyclerView) findViewById(R.id.skyPortsListRecyclerView);

        dateAdapter = new SkyPortsRecyclerAdapter(dateList, getActContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActContext(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        skyPortsListRecyclerView.setLayoutManager(mLayoutManager);
       /* LinearLayoutManager mLayoutManager = new MyCustomLayoutManager(getActContext());
        skyPortsListRecyclerView.setLayoutManager(mLayoutManager);*/
        skyPortsListRecyclerView.setAdapter(dateAdapter);
        dateAdapter.notifyDataSetChanged();
        // skyPortsListRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
        dateAdapter.setSelectedListener(this);
        reSetDetails();


        skyPortsListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Logger.d("isNestedScrollingEnabled::",":111:"+skyPortsListRecyclerView.isNestedScrollingEnabled());

                /*if(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0){
                    skyPortsListRecyclerView.setNestedScrollingEnabled(false);
                    bottomSheetBehavior.(true);
                }else if(!skyPortsListRecyclerView.isNestedScrollingEnabled()){
                    skyPortsListRecyclerView.setNestedScrollingEnabled(true);
                }*/


                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int lastInScreen = firstVisibleItemPosition + visibleItemCount;

             /*   if (firstVisibleItemPosition == 0 && bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setAllowUserDragging(true);
                } else if (lastCompletelyVisibleItemPosition == totalItemCount && (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_COLLAPSED || bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_HALF_EXPANDED)) {
                    bottomSheetBehavior.setAllowUserDragging(true);
                }else
                {
                    bottomSheetBehavior.setAllowUserDragging(false);
                }*/
            }
        });


    }

    private void flyElementsInit() {

        tvTitle = (MTextView) findViewById(R.id.tvTitle);
        tvNoDetails = (MTextView) findViewById(R.id.tvNoDetails);
        tvSelectedAddress = (MTextView) findViewById(R.id.tvSelectedAddress);
        addFlyStationNote = (MTextView) findViewById(R.id.addFlyStationNote);
        tvSelectedAddress.setText(generalFunc.retrieveLangLBl("", "LBL_DETACTING_YOUR_LOCATION"));
        addFlyStationNote.setText(generalFunc.retrieveLangLBl("", "LBL_FETCHING_LOCATION_NOTE_TEXT"));
        changeArea = (LinearLayout) findViewById(R.id.changeArea);
        MTextView tvMoreStations = (MTextView) findViewById(R.id.tvMoreStations);
//        MTextView tvChangeAddress = (MTextView) findViewById(R.id.tvChangeAddress);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        // init the bottom sheet behavior
        bottomSheetBehavior = (LockableBottomSheetBehavior) BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setHideable(false);
//        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_HALF_EXPANDED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                // this part hides the button immediately and waits bottom sheet
                // to collapse to show
             /*   if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    btn_type2.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    btn_type2.animate().scaleX(1).scaleY(1).setDuration(300).start();
                }*/




                String state = "";

                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        state = "DRAGGING";
                        Logger.d("tvMoreStations", ":DRAGGING:");
                        //setFlySheetHeight();

                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        state = "SETTLING";
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        state = "EXPANDED";
//                        skyPortsListRecyclerView.setNestedScrollingEnabled(true);
                        skyPortsListRecyclerView.setPadding(0,0,0,0);
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        int[] location = new int[2];
                        tvMoreStations.getLocationOnScreen(location);
                        int x = location[0];
                        int y = location[1];

                        int[] location_1 = new int[2];
                        (findViewById(R.id.swipeArea)).getLocationOnScreen(location_1);
                        int x_1 = location_1[0];
                        int y_1 = location_1[1];

                        int subY = y - y_1;
                        skyPortsListRecyclerView.setPadding(0,0,0,y-subY);

//                        float padding = Utils.getScreenPixelHeight(getActContext()) - y;

                        state = "COLLAPSED";
//                        skyPortsListRecyclerView.getLayoutManager().smoothScrollToPosition(skyPortsListRecyclerView, new RecyclerView.State(), pos);

//                        skyPortsListRecyclerView.setNestedScrollingEnabled(false);

                        new Handler().postDelayed(() -> skyPortsListRecyclerView.scrollToPosition(pos), 200);

                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        state = "HIDDEN";
                        break;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                btn_type2.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
//                gMap.setPadding(0, 0, 0, (int) (1 - slideOffset));

                int[] location = new int[2];
                tvMoreStations.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];

                int[] location_1 = new int[2];
                (findViewById(R.id.swipeArea)).getLocationOnScreen(location_1);
                int x_1 = location_1[0];
                int y_1 = location_1[1];

                int subY = y - y_1;
                skyPortsListRecyclerView.setPadding(0,0,0,y-subY);

            }
        });

//        tvChangeAddress.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        tvMoreStations.setText(generalFunc.retrieveLangLBl("", "LBL_FLY_VIEW_MORE_STATIONS"));

//        btn_type2.setOnClickListener(new setOnClickList());
//        changeArea.setOnClickListener(new setOnClickList());

        btn_type2.setOnClickListener(v -> {
            if (pos == -1) {
//                    Logger.d("Fly_CLICK", "no selected");
                generalFunc.showMessage(skyPortsListRecyclerView, generalFunc.retrieveLangLBl("Please select any 1 skyPort.", "LBL_FLY_WARNING_MSG"));
            } else {
//                    Logger.d("Fly_CLICK", " selected");
                String address = dateList.get(pos).get("skyPortAddress");
                finalAddress = dateList.get(pos).get("skyPortTitle") + (Utils.checkText(address) ? (" | " + dateList.get(pos).get("skyPortAddress")) : "");
                finaliLocationId = dateList.get(pos).get("iLocationId");

                if (finalAddressLocation == null) {
                    finalAddressLocation = new Location(!isPickup ? "dest" : "");
                }
                finalAddressLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, dateList.get(pos).get("skyPortLatitude")));
                finalAddressLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, dateList.get(pos).get("skyPortLongitude")));

                if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                setSelectedSkyPortPoint(isPickup, finalAddressLocation, finalAddress, finaliLocationId, isPickup);


                if (destLocation == null && pickUpLocation != null) {
                    showSelectionDialog(null, false);
                }
            }
        });

/*

        // Implement it's on touch listener.
        findViewById(R.id.swipeArea).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                enableDisableScroll(true);
                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });


        // Implement it's on touch listener.

        findViewById(R.id.dataArea).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                enableDisableScroll(false);

                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });

        findViewById(R.id.popupView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                enableDisableScroll(false);
                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });

*/

        changeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                changeAddress(isPickup);
            }
        });
    }

    private void reSetDetails() {
        if (!isPickup && data != null) {
            destAddress = data.getStringExtra("Address");
            if (destLocation == null) {
                destLocation = new Location("dest");
            }
            destLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Latitude")));
            destLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Longitude")));

        }


        tvSelectedAddress.setText(!isPickup ? destAddress : Utils.checkText(pickUpLocationAddress) ? pickUpLocationAddress : generalFunc.retrieveLangLBl("", "LBL_DETACTING_YOUR_LOCATION"));

        tvTitle.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_DROP_STATION_TXT" : "LBL_FLY_PICKUP_STATION_TXT"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_CONFIRM_LOCATION_TXT" : "LBL_FLY_CONFIRM_PICKUP_TXT"));

        getSkyPortsPoints();
    }

    public void getSkyPortsPoints() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getNearestFlyStations");
        parameters.put("lattitude", !isPickup ? "" + destLocation.getLatitude() : "" + pickUpLocation.getLatitude());
        parameters.put("longitude", !isPickup ? "" + destLocation.getLongitude() : "" + pickUpLocation.getLongitude());
        parameters.put("address", Utils.getText(tvSelectedAddress));

        String iLocationId = "";

        iLocationId = iFromStationId;


        if (Utils.checkText(iLocationId)) {
            parameters.put("iLocationId", iLocationId);
        }


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                dateList.clear();
                pos = -1;


                if (isDataAvail == true) {

                    JSONArray arr_sky_ports = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_sky_ports != null && arr_sky_ports.length() > 0) {
                        for (int i = 0; i < arr_sky_ports.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_sky_ports, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("iLocationId", generalFunc.getJsonValueStr("iLocationId", obj_temp));
                            map.put("skyPortTitle", generalFunc.getJsonValueStr("vLocationName", obj_temp));
                            map.put("skyPortKm", generalFunc.getJsonValueStr("distance", obj_temp));
                            map.put("skyPortLatitude", generalFunc.getJsonValueStr("tCentroidLattitude", obj_temp));
                            map.put("skyPortLongitude", generalFunc.getJsonValueStr("tCentroidLongitude", obj_temp));
                            map.put("skyPortAddress", generalFunc.getJsonValueStr("vLocationAddress", obj_temp));
                            map.put("LBL_AWAY_TXT", generalFunc.retrieveLangLBl("", "LBL_AWAY_TXT"));
                            dateList.add(map);
                        }

                        addListOfSkyPortPointMarkers(isPickup, dateList);
                        tvNoDetails.setVisibility(View.GONE);
                    }

                    dateAdapter.pos = 0;
                    onDataSelect(0);
                    dateAdapter.notifyDataSetChanged();
                    loaderView.setVisibility(View.GONE);


                } else {
                    tvNoDetails.setVisibility(View.VISIBLE);

                    tvNoDetails.setText(generalFunc.retrieveLangLBl("No station found nearby to this location.", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    dateAdapter.notifyDataSetChanged();
                    loaderView.setVisibility(View.GONE);
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();


    }

    @Override
    public void onDataSelect(int position) {
      /*  if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
*/
        skyPortsListRecyclerView.scrollToPosition(pos);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        pos = position;
        highlightSelectedSkyPortPointMarkers(isPickup, finalAddressLocation, finalAddress, position);

        skyPortsListRecyclerView.scrollToPosition(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skyPortsListRecyclerView.smoothScrollToPosition(position);
            }
        }, 150);
    }


    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);

        }

        // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                //  Log.d("SET_PICKUP", "onInterceptTouchEvent: click performed");
                rv.findChildViewUnder(e.getX(), e.getY()).performClick();
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            enableDisableScroll(false);
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    };


    public void setSelectedSkyPortPoint(boolean isPickup, Location location, String address, String iLocationId) {
//        Logger.d("Fly_CLICK", "setSelectedSkyPortPoint");

        resetMapView();
        gMap.setPadding(0, 0, 0, 0);
        map.getView().requestLayout();

        removeSkyPortsPointsFromMap();

        Intent data = new Intent();
        data.putExtra("Address", address);
        data.putExtra("Latitude", "" + location.getLatitude());
        data.putExtra("Longitude", "" + location.getLongitude());
        data.putExtra("isSkip", false);
        data.putExtra("iLocationId", iLocationId);

        mainHeaderFrag.addOrResetListOfAddresses(data, !isPickup);

        releaseInstances(false);
    }

    public void setSelectedSkyPortPoint(boolean isPickup, Location location, String address, String iLocationId, boolean showLocationNameArea) {
//        Logger.d("Fly_CLICK", "setSelectedSkyPortPoint" + showLocationNameArea);

        resetMapView();
        gMap.setPadding(0, 0, 0, 0);
        map.getView().requestLayout();

        removeSkyPortsPointsFromMap();

        Intent data = new Intent();
        data.putExtra("Address", address);
        data.putExtra("Latitude", "" + location.getLatitude());
        data.putExtra("Longitude", "" + location.getLongitude());
        data.putExtra("isSkip", false);
        data.putExtra("iLocationId", iLocationId);

        mainHeaderFrag.addOrResetListOfAddresses(data, !isPickup);

        if (showLocationNameArea && cabSelectionFrag == null) {
            mainHeaderFrag.showAddressArea();
        }

        releaseInstances(false);
    }

    public void releaseInstances(boolean showHeader) {
        findViewById(R.id.dragView).setVisibility(View.VISIBLE);
//        findViewById(R.id.dragView1).setVisibility(View.GONE);


        if (showHeader && mainHeaderFrag != null) {
//            Logger.d("Fly_CLICK", "mainHeaderFrag!=null");
            if (isMenuImageShow) {
                mainHeaderFrag.menuImgView.setVisibility(View.VISIBLE);
                mainHeaderFrag.backImgView.setVisibility(View.GONE);
            }

            mainHeaderFrag.setDefaultView();

            pinImgView.setVisibility(View.GONE);
            if (loadAvailCabs != null) {
                selectedCabTypeId = loadAvailCabs.getFirstCarTypeID();
            }
            resetUserLocBtnView();

           /* if (mainHeaderFrag != null) {
                mainHeaderFrag.releaseAddressFinder();
            }*/

            resetMapView();

            if (pickUpLocation != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(this.pickUpLocation.getLatitude(), this.pickUpLocation.getLongitude()))
                        .zoom(Utils.defaultZomLevel).build();
                getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else if (userLocation != null) {
                getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraForUserPosition()));
            }
        }

        if (!/*flyStationSelectionFragment.*/isPickup) {
            mainHeaderFrag.handleDestAddIcon();
            mainHeaderFrag.isclickabledest = false;
            mainHeaderFrag.isclickablesource = false;
            configDestinationMode(false);

            reSetButton(false);
        }

        if (findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE) {
            findViewById(R.id.DetailsArea).setVisibility(View.GONE);
            findViewById(R.id.dragView).setVisibility(View.VISIBLE);
            reSetButton(false);
        }

//        if (flyStationSelectionFragment != null) {
//            getSupportFragmentManager().beginTransaction().remove(flyStationSelectionFragment).commit();
//            flyStationSelectionFragment = null;
//        }

        enableDisableScroll(false);

        removeSkyPortsPointsFromMap();
        setPanelHeight(0);
    }

    public void addListOfSkyPortPointMarkers(boolean isPickup, ArrayList<HashMap<String, String>> skyPortsPointsList) {
        removeSkyPortsPointsFromMap();

        builder = new LatLngBounds.Builder();

        for (int i = 0; i < skyPortsPointsList.size(); i++) {

            View skyport_marker_view = ((LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.custom_skyports_marker, null);

            MarkerOptions temp_option;
            map_SkyPort_ViewList.add(skyport_marker_view);
            double lat = generalFunc.parseDoubleValue(0.00, skyPortsPointsList.get(i).get("skyPortLatitude"));
            double longi = generalFunc.parseDoubleValue(0.00, skyPortsPointsList.get(i).get("skyPortLongitude"));
            temp_option = new MarkerOptions().position(new LatLng(lat, longi)).title("skyPort_" + i).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActContext(), skyport_marker_view))).anchor(0.00f, 0.20f);

                /*
                temp_dest_option = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));*/
            Marker skyPortsTempMarker = getMap().addMarker(temp_option);
            map_SkyPort_MarkerList.add(skyPortsTempMarker);
            builder.include(skyPortsTempMarker.getPosition());
        }

        if (skyPortsPointsList.size() > 0)
            buildSkyPortBuilder();

    }

    public void highlightSelectedSkyPortPointMarkers(boolean isPickup, Location location, String address, int pos) {


        try {
            if (prevSatate == isPickup && selectedMarkerPos != -1) {
//            Logger.d("Fly_CLICK", "skyport_marker_view != null");

                View view = map_SkyPort_ViewList.get(selectedMarkerPos);
                ImageView mImageGreySource = (ImageView) view.findViewById(R.id.image_grey);
                int Size = Utils.dpToPx(getActContext(), 30);
                mImageGreySource.setLayoutParams(new LinearLayout.LayoutParams(Size, Size));
                mImageGreySource.setImageResource(R.drawable.ic_airport_unselected);
                reSetMapIcon(view);

            }
            selectedMarkerPos = pos;
            prevSatate = isPickup;


            View skyport_marker_view = map_SkyPort_ViewList.get(pos);
            Marker skyportMarker = map_SkyPort_MarkerList.get(pos);

            ImageView mImageGreySource = (ImageView) skyport_marker_view.findViewById(R.id.image_grey);
            int Size = Utils.dpToPx(getActContext(), 45);
            mImageGreySource.setLayoutParams(new LinearLayout.LayoutParams(Size, Size));
            mImageGreySource.setImageResource(R.drawable.ic_airport_selected);

            reSetMapIcon(skyport_marker_view);

            CameraPosition cp = CameraPosition.builder().target(skyportMarker.getPosition()).zoom(12.0f).build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        } catch (Exception e) {
            Logger.d("highlightSelectedSkyPortPointMarkers", "::" + e.toString());
        }

//        animateImageView(mImageGreySource);
//        pulseCircleTest(skyportMarker);
    }

    public void reSetMapIcon(View view) {
        map_SkyPort_ViewList.set(selectedMarkerPos, view);

        Marker oldMarker = map_SkyPort_MarkerList.get(selectedMarkerPos);
        oldMarker.remove();

        MarkerOptions temp_option = new MarkerOptions().position(oldMarker.getPosition()).title("skyPort_" + selectedMarkerPos).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActContext(), view))).anchor(0.00f, 0.20f);

        Marker skyPortsTempMarker = getMap().addMarker(temp_option);
        map_SkyPort_MarkerList.set(selectedMarkerPos, skyPortsTempMarker);
    }

    public void buildSkyPortBuilder() {
        if (cabSelectionFrag == null) {
            if (map != null && map.getView().getViewTreeObserver().isAlive()) {
                map.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressLint("NewApi") // We check which build version we are using.
                    @Override
                    public void onGlobalLayout() {

                        boolean isBoundIncluded = true;
                        if (isBoundIncluded) {

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                map.getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                map.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }


                            LatLngBounds bounds = builder.build();


                            LatLng center = bounds.getCenter();

                            LatLng northEast = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), SphericalUtil.computeHeading(center, bounds.northeast));
                            LatLng southWest = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), (180 + (180 + SphericalUtil.computeHeading(center, bounds.southwest))));

                            builder.include(southWest);
                            builder.include(northEast);

                            /*  Method 1 */
//                            getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            int width = metrics.widthPixels;
                            int height = metrics.heightPixels;
                            // Set Padding according to included bounds

                            int padding = (int) (width * 0.25); // offset from edges of the map 10% of screen


                            /*  Method 2 */
                            /*Logger.e("MapHeight","newLatLngZoom");
                            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(builder.build().getCenter(),16));*/

                            int h = (int) (height * 0.5);
//                            int h=flyStationSelectionFragment.fragmentHeight;
                            try {
                                /*  Method 3 */
                                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                                padding = (height - ((h + 5) + Utils.dipToPixels(getActContext(), 60))) / 3;
//                                Logger.e("MapHeight", "cameraUpdate" + padding);
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(),
                                        screenWidth, screenHeight, padding);
                                getMap().animateCamera(cameraUpdate);
                            } catch (Exception e) {
                                e.printStackTrace();

                                /*  Method 1 */
                                getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - ((h + 5) + Utils.dipToPixels(getActContext(), 60)), padding));
                            }


                        }

                    }
                });
            }
        }
    }

    public void removeSkyPortsPointsFromMap() {
        if (map_SkyPort_MarkerList.size() > 0) {
            ArrayList<Marker> tempDriverMarkerList = new ArrayList<>();
            tempDriverMarkerList.addAll(map_SkyPort_MarkerList);
            for (int i = 0; i < tempDriverMarkerList.size(); i++) {
                Marker marker_temp = map_SkyPort_MarkerList.get(0);
                marker_temp.remove();
                map_SkyPort_MarkerList.remove(0);
                map_SkyPort_ViewList.remove(0);
            }
        }

        reSetButton(findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE ? true : false);
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

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public void pulseCircleTest(Marker skyportMarker) {

        // move map to point of interest
        CameraPosition cp = CameraPosition.builder().target(skyportMarker.getPosition()).zoom(8.0F).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));


        // create a circle and start animation
        int color = Color.BLACK;
        float initialRadius = 10;
        float maxRadius = 20000f;
        CircleOptions co = new CircleOptions().center(skyportMarker.getPosition()).radius(initialRadius).strokeColor(color).fillColor(color).strokeWidth(1.0f);
        Circle c = gMap.addCircle(co);
        Circle c2 = gMap.addCircle(co);

        final Handler h = new Handler();
        h.postDelayed(new Fader(h, c, initialRadius, maxRadius, Color.BLUE, co), 300);
        h.postDelayed(new Fader(h, c2, initialRadius, maxRadius, Color.BLUE, co), 750);
    }

    public void changeAddress(boolean isPickup) {

        if (mainHeaderFrag != null) {
            // Logger.d("SET_PICKUP", "isPickup-->" + isPickup);
            if (isPickup) {
                mainHeaderFrag.isclickablesource = false;
                mainHeaderFrag.pickupLocArea1.performClick();
            } else {
                mainHeaderFrag.isclickabledest = false;
                mainHeaderFrag.destarea.performClick();
            }
        }

    }

    public void enableDisableScroll(boolean b) {
        sliding_layout.setTouchEnabled(b);
        /*
        if (b) {
            sliding_layout.requestFocus();
        }
        else
        {
            sliding_layout.clearFocus();
        }*/
      /*  sliding_layout.setDragView(b?R.id.dragView1:null);
        sliding_layout.setScrollableView(b?(flyStationSelectionFragment!=null?flyStationSelectionFragment.skyPortsListRecyclerView:null):null);*/
    }


    private class Fader implements Runnable {
        private float radius, initialRadius, maxRadius;
        private int baseColor, color, initialColor;
        private Handler h;
        private Circle c;
        private float radiusJump = 400;
        int numIncrements, alphaIncrement;
        private CircleOptions co;

        public Fader(Handler h, Circle c, float initialRadius, float maxRadius, int initialColor, CircleOptions co) {
            this.initialRadius = initialRadius;
            this.initialColor = initialColor;
            this.maxRadius = maxRadius;
            this.h = h;
            this.c = c;
            this.co = co;
            reset();
        }

        private void reset() {
            radius = initialRadius;
            this.color = initialColor;
            this.baseColor = initialColor;
            numIncrements = (int) ((maxRadius - initialRadius) / radiusJump);
            alphaIncrement = 0x100 / numIncrements;
            if (alphaIncrement <= 0) alphaIncrement = 1;
        }

        public void run() {
            int alpha = Color.alpha(color);
            radius = radius + radiusJump;
            c.setRadius(radius);
            alpha -= alphaIncrement;
            color = Color.argb(alpha, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor));
            c.setFillColor(color);
            c.setStrokeColor(color);

            if (radius < maxRadius) {
                h.postDelayed(this, 25);
            } else {
                c.remove();
                reset();
                c = gMap.addCircle(co);
                h.postDelayed(this, 2000);
            }

            //done
        }
    }

    public void animateImageView(final ImageView v) {

        int colorFrom = getResources().getColor(R.color.gray);
        int colorTo = getResources().getColor(R.color.btnnavtripselcolor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
//                v.setBackgroundColor((int) animator.getAnimatedValue());
                v.setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.SRC_ATOP);
            }

        });
        colorAnimation.start();


        Animator animatorScaleX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1.5f, 1.f);
        Animator animatorScaleY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1.5f, 1.f);
        Animator animator = ObjectAnimator.ofFloat(v, View.ALPHA, 0.f, 1.f).setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                v.setVisibility(View.VISIBLE);
                //invalidate();
            }
        });
        AnimatorSet showAnimatorSet = new AnimatorSet();
        showAnimatorSet.playTogether(animator, animatorScaleX, animatorScaleY);

       /* Animation scaleAnimation = AnimationUtils.loadAnimation(getActContext(), R.anim.pulse);
        scaleAnimation.setDuration(100);*/

        /*
        final int black = getResources().getColor(R.color.btnnavtripselcolor);

        final ValueAnimator colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = adjustAlpha(black, mul);
                v.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
                if (mul == 0.0) {
                    v.setColorFilter(null);
                }
            }
        });

        colorAnim.setDuration(1500);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();*/

    }


    public void addcabselectionfragment() {
        setRiderDefaultView();

        // Map Height resetting n Backpress done by user then app crashes
        if (isMultiDelivery() && isFinishing() || isDestroyed()) {
            return;
        }


       /* //handle map height
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        ViewGroup.LayoutParams params = map.getView().getLayoutParams();
        params.height = height - Utils.dpToPx(getActContext(), 280);
        Logger.d("height", "::" + params.height);
        map.getView().setLayoutParams(params);
*/

        resetMapView();
        gMap.setPadding(0, 0, 0, Utils.dipToPixels(getActContext(), 280));

        map.getView().requestLayout();
    }

    public void setSelectedDriverId(String driver_id) {
        SelectedDriverId = driver_id;
    }

    public void setLabels() {
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        ((MTextView) findViewById(R.id.rideTxt)).setText(generalFunc.retrieveLangLBl("Ride", "LBL_RIDE"));
        ((MTextView) findViewById(R.id.selrideTxt)).setText(generalFunc.retrieveLangLBl("Ride", "LBL_RIDE"));
        ((MTextView) findViewById(R.id.deliverTxt)).setText(generalFunc.retrieveLangLBl("Deliver", "LBL_DELIVER"));
        ((MTextView) findViewById(R.id.otherTxt)).setText(generalFunc.retrieveLangLBl("Other", "LBL_SERVICES"));

        homeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HOME"));
        workTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OFFICE"));
        destSelectTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WHERE_TO"));

        if (type.equals(Utils.CabReqType_Now)) {
            if (generalFunc.getJsonValue("RIDE_LATER_BOOKING_ENABLED", userProfileJson).equalsIgnoreCase("Yes")) {
                rideLaterTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_PROVIDERS_AVAIL_NOW"));


                btn_type_ridelater.setVisibility(View.VISIBLE);
            } else {

                rideLaterTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_PROVIDERS_AVAIL"));


                btn_type_ridelater.setVisibility(View.GONE);
            }
        } else {

            rideLaterTxt.setText(generalFunc.retrieveLangLBl("", SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider") ? "LBL_NO_PROVIDER_AVA_AT_LOCATION" : "LBL_NO_PROVIDERS_AVAIL_LATER"));


            btn_type_ridelater.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        try {
            outState.putString("RESTART_STATE", "true");
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGeneralData() {
        HashMap<String, String> storeData = new HashMap<>();
        storeData.put(Utils.MOBILE_VERIFICATION_ENABLE_KEY, generalFunc.getJsonValueStr("MOBILE_VERIFICATION_ENABLE", obj_userProfile));
        String DRIVER_REQUEST_METHOD = generalFunc.getJsonValueStr("DRIVER_REQUEST_METHOD", obj_userProfile);

        this.DRIVER_REQUEST_METHOD = DRIVER_REQUEST_METHOD.equals("") ? "All" : DRIVER_REQUEST_METHOD;

        storeData.put(Utils.REFERRAL_SCHEME_ENABLE, generalFunc.getJsonValueStr("REFERRAL_SCHEME_ENABLE", obj_userProfile));
        storeData.put(Utils.WALLET_ENABLE, generalFunc.getJsonValueStr("WALLET_ENABLE", obj_userProfile));
        storeData.put(Utils.SMS_BODY_KEY, generalFunc.getJsonValueStr(Utils.SMS_BODY_KEY, obj_userProfile));
        generalFunc.storeData(storeData);
    }

    public MainHeaderFragment getMainHeaderFrag() {
        return mainHeaderFrag;
    }

    private void openBottomView() {
        if (driver_detail_bottomView == null) {
            return;
        }
        Animation bottomUp = AnimationUtils.loadAnimation(getActContext(),
                R.anim.slide_up_anim);
        driver_detail_bottomView.startAnimation(bottomUp);
        driver_detail_bottomView.setVisibility(View.VISIBLE);
    }

    boolean isFirst = true;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        (findViewById(R.id.LoadingMapProgressBar)).setVisibility(View.GONE);

        // Maps dias e noches inicio by gabriel
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if(hourOfDay >= 06 && hourOfDay < 18){
            // googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        } else {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.ub__map_style_noite_pedro));
        }
// Maps dias e noches fim by gabriel

        if (googleMap == null) {
            return;
        }

        this.gMap = googleMap;

        if (isUfx) {
            if (getIntent().getStringExtra("SelectDate") != null) {
                SelectDate = getIntent().getStringExtra("SelectDate");
            }
            if (pickUpLocation == null) {
                Location temploc = new Location("PickupLoc");
                if (getIntent().getStringExtra("latitude") != null) {
                    temploc.setLatitude(generalFunc.parseDoubleValue(0.0, getIntent().getStringExtra("latitude")));
                    temploc.setLongitude(generalFunc.parseDoubleValue(0.0, getIntent().getStringExtra("longitude")));
                    onLocationUpdate(temploc);
//                    pickUpLocation = temploc;
//                    pickUpLocationAddress = getIntent().getStringExtra("address");
                }
            }
        }


        if (generalFunc.checkLocationPermission(true) == true) {
            getMap().setMyLocationEnabled(true);
            getMap().getUiSettings().setTiltGesturesEnabled(false);
            getMap().getUiSettings().setCompassEnabled(false);
            getMap().getUiSettings().setMyLocationButtonEnabled(false);

            getMap().setOnMarkerClickListener(marker -> {
                marker.hideInfoWindow();

                if (isUfx) {
                    if (isMarkerClickable == true) {
                        openBottomView();
                        markerId = marker.getId();
                        setBottomView(marker);
                    }
                } else {
                    try {

                        getMap().getUiSettings().setMapToolbarEnabled(false);
                        if (marker.getTag().equals("1")) {
                            if (mainHeaderFrag != null) {
                                mainHeaderFrag.pickupLocArea1.performClick();
                            }

                        } else if (marker.getTag().equals("2")) {
                            if (mainHeaderFrag != null) {
                                mainHeaderFrag.destarea.performClick();
                            }
                        }
                    } catch (Exception e) {

                    }

                }
                return true;

            });


            getMap().setOnMapClickListener(this);


        }

        if (isUfx) {
            if (isFirst) {
                isFirst = false;
//                initializeLoadCab();
                Logger.d("dd123","pheragaya");
            }
        }

        String vTripStatus = generalFunc.getJsonValueStr("vTripStatus", obj_userProfile);

        if (vTripStatus != null && (vTripStatus.equals("Active") || vTripStatus.equals("On Going Trip"))) {
            getMap().setMyLocationEnabled(false);
            String tripDetailJson = generalFunc.getJsonValueStr("TripDetails", obj_userProfile);

            if (tripDetailJson != null && !tripDetailJson.trim().equals("")) {
                double latitude = generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("tStartLat", tripDetailJson));
                double longitude = generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("tStartLong", tripDetailJson));
                Location loc = new Location("gps");
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                onLocationUpdate(loc);
            }
        }

        initializeViews();

        if (getLastLocation != null) {
            getLastLocation.stopLocationUpdates();
            getLastLocation = null;
        }

        GetLocationUpdates.locationResolutionAsked = false;
        getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, true, this);

        if (workArea.getVisibility() == View.VISIBLE) {
            if (gMap != null) {
                gMap.setPadding(0, 0, 0, Utils.dipToPixels(getActContext(), 65));
            }
        }

    }

    public void checkDrawerState() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START) == true) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public GoogleMap getMap() {
        return this.gMap;
    }

    public void setShadow() {
        if (cabSelectionFrag != null) {
            cabSelectionFrag.setShadow();
        }
    }

    public void setUserLocImgBtnMargin(int margin) {
    }

    public void initializeLoadCab() {
        if (isDriverAssigned == true) {
            return;
        }

        loadAvailCabs = new LoadAvailableCab(getActContext(), generalFunc, selectedCabTypeId, userLocation,
                getMap(), userProfileJson);

        loadAvailCabs.pickUpAddress = pickUpLocationAddress;
        loadAvailCabs.currentGeoCodeResult = currentGeoCodeObject;
        loadAvailCabs.checkAvailableCabs();
    }

    public void getWalletBalDetails() {


        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GetMemberWalletBalance");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {


            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    try {
                        generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "No");
                        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        JSONObject object = generalFunc.getJsonObject(userProfileJson);
                        object.put("user_available_balance", generalFunc.getJsonValue("MemberBalance", responseString));
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, object.toString());

                        getUserProfileJson();


                        setUserInfo();
                    } catch (Exception e) {

                    }
                }
            }
        });
        exeWebServer.execute();
    }


    public void showMessageWithAction(View view, String message, final Bundle bn) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(generalFunc.retrieveLangLBl("", "LBL_BTN_VERIFY_TXT"), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StartActProcess(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_INFO_REQ_CODE);

                    }
                });
        snackbar.setActionTextColor(getActContext().getResources().getColor(R.color.verfiybtncolor));
        snackbar.setDuration(10000);
        snackbar.show();
    }

    boolean isIinitializeViewsCall = false;

    public void initializeViews() {
        if (isIinitializeViewsCall) {
            if (pickUpLocation != null && mainHeaderFrag != null) {
                mainHeaderFrag.setSourceAddress(pickUpLocation.getLatitude(), pickUpLocation.getLongitude());
                return;
            }
            return;
        }

        if (pickUpLocation != null && mainHeaderFrag != null) {
            mainHeaderFrag.setSourceAddress(pickUpLocation.getLatitude(), pickUpLocation.getLongitude());
            return;
        }

        isIinitializeViewsCall = true;

        String vTripStatus = generalFunc.getJsonValueStr("vTripStatus", obj_userProfile);


        if (vTripStatus != null && (vTripStatus.equals("Active") || vTripStatus.equals("On Going Trip") || vTripStatus.equals("Arrived"))) {

            JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);

            if (tripDetailJson != null) {
                String tripId = generalFunc.getJsonValueStr("iTripId", tripDetailJson);
                String eFly = generalFunc.getJsonValueStr("eFly", tripDetailJson);

                if (vTripStatus.equals("Arrived") && !eFly.equalsIgnoreCase("Yes")) {
                    setMainHeaderView(isMultiDelivery() ? false : isUfx);
                    return;
                }

                eTripType = generalFunc.getJsonValueStr("eType", tripDetailJson);

                this.tripId = tripId;

                if (eTripType.equals("Deliver")) {
                    eTripType = Utils.CabGeneralType_Deliver;
                }

                if (eTripType.equalsIgnoreCase(Utils.eType_Multi_Delivery) && !TextUtils.isEmpty(tripId)) {
                    configureAssignedDriver(true);
                    configureDeliveryView(true);

                    return;
                } else if (!eTripType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                    configureAssignedDriver(true);
                    configureDeliveryView(true);

                    return;
                }
            }
        }

        if (eFly) {
            showSelectionDialog(null, true);
        }

        setMainHeaderView(isMultiDelivery() ? false : isUfx);

        Utils.runGC();
    }

    private void setMainHeaderView(boolean isUfx) {
        try {
            if (mainHeaderFrag == null) {

                mainHeaderFrag = new MainHeaderFragment();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isUfx", isUfx);
                bundle.putBoolean("isRedirectMenu", true);
                mainHeaderFrag.setArguments(bundle);
                if (getMap() != null) {
                    mainHeaderFrag.setGoogleMapInstance(getMap());
                }
            }
            if (mainHeaderFrag != null) {
                if (getMap() != null) {
                    mainHeaderFrag.releaseAddressFinder();
                }
            }
            try {
                super.onPostResume();
            } catch (Exception e) {
                Logger.d("Exception", e.toString());
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.headerContainer, mainHeaderFrag).commit();

            configureDeliveryView(false);


        } catch (Exception e) {
            Logger.d("Exception", e.toString());

        }

    }

    private void setRiderDefaultView() {
        if (cabSelectionFrag == null) {
            Bundle bundle = new Bundle();
            bundle.putString("RideDeliveryType", RideDeliveryType);
            cabSelectionFrag = new CabSelectionFragment();
            cabSelectionFrag.setArguments(bundle);
            pinImgView.setVisibility(View.GONE);

//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
//            params.bottomMargin = Utils.dipToPixels(getActContext(), 240);


            if (driverAssignedHeaderFrag != null) {
                userTripBtnImgView.setVisibility(View.VISIBLE);
            }
        }

        if (mainHeaderFrag != null) {
            mainHeaderFrag.addAddressFinder();
        }

        if (driverAssignedHeaderFrag != null) {
            pinImgView.setVisibility(View.GONE);
            if (!driverAssignedHeaderFrag.isMultiDelivery()) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
                params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
                userTripBtnImgView.setVisibility(View.VISIBLE);
            }

        }

        setCurrentType();

        if (!isFinishing() && !isDestroyed() && isMultiDelivery()) {

            if (app_type.equalsIgnoreCase("Ride-Delivery") && generalFunc.isMultiDelivery() && loadAvailCabs != null) {
                Log.d(TAG, "setRiderDefaultView: 6");
                loadAvailCabs.onPauseCalled();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dragView, cabSelectionFrag).commitAllowingStateLoss();

            if (app_type.equalsIgnoreCase("Ride-Delivery") && generalFunc.isMultiDelivery() && loadAvailCabs != null) {
                Log.d(TAG, "setRiderDefaultView: 7");
                loadAvailCabs.onResumeCalled();
            }

            configureDeliveryView(false);


        }


        try {
            super.onPostResume();
        } catch (Exception e) {
        }

        if (!isMultiDelivery()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.dragView, cabSelectionFrag).commit();

            configureDeliveryView(false);
        }

        if (mainHeaderFrag != null && !isMultiDelivery()) {
            mainHeaderFrag.menuBtn.setVisibility(View.GONE);
            mainHeaderFrag.backBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setCurrentType() {

        if (cabSelectionFrag == null) {
            return;
        }
        if (app_type.equalsIgnoreCase("Delivery")) {
            Log.d(TAG, "setCurrentType: 8");
            cabSelectionFrag.currentCabGeneralType = "Deliver";
        } else if (app_type.equalsIgnoreCase("UberX")) {
            Log.d(TAG, "setCurrentType: 9");
            cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_UberX;
        } else if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) || app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
            Log.d(TAG, "setCurrentType: 10");
            if (isDeliver(RideDeliveryType)) {
                cabSelectionFrag.currentCabGeneralType = "Deliver";
            } else {
                cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Ride;
            }
        } else {
            cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Ride;
        }
    }

    public void configureDeliveryView(boolean isHidden) {
        if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {

        }
//        else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("Ride-Delivery") && isHidden == false) {
//            (findViewById(R.id.deliveryArea)).setVisibility(View.VISIBLE);
//            setUserLocImgBtnMargin(190);
//        }
        else {
            (findViewById(R.id.deliveryArea)).setVisibility(View.GONE);
            setUserLocImgBtnMargin(105);
        }

    }

    public void configDestinationMode(boolean isDestinationMode) {
        this.isDestinationMode = isDestinationMode;
        try {
            if (isDestinationMode == false) {
                if (loadAvailCabs != null) {
                    loadAvailCabs.filterDrivers(false);
                }
                animateToLocation(getPickUpLocation().getLatitude(), getPickUpLocation().getLongitude());
                if (cabSelectionFrag != null) {
                    noCabAvail = false;
                    changeLable();
                }
            } else {
                pinImgView.setImageResource(R.drawable.pin_dest_select);
                if (cabSelectionFrag != null) {
                    if (loadAvailCabs != null) {
                        if (loadAvailCabs.isAvailableCab) {
                            changeLable();
                            noCabAvail = true;
                        }
                    }
                }

                if (timeval.equalsIgnoreCase("\n" + "--")) {
                    noCabAvail = false;
                } else {
                    noCabAvail = true;
                }
                changeLable();
                pinImgView.setImageResource(R.drawable.pin_dest_select);
                if (isDestinationAdded == true && !getDestLocLatitude().trim().equals("") && !getDestLocLongitude().trim().equals("")) {
                    animateToLocation(generalFunc.parseDoubleValue(0.0, getDestLocLatitude()), generalFunc.parseDoubleValue(0.0, getDestLocLongitude()));
                }

            }
            changeLable();

            if (mainHeaderFrag != null) {
                mainHeaderFrag.configDestinationMode(isDestinationMode);
            }
        } catch (Exception e) {

        }
    }

    private void changeLable() {
        if (cabSelectionFrag != null) {
            cabSelectionFrag.setLabels(false);
        }
    }

    public void animateToLocation(double latitude, double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude))
                    .zoom(gMap.getCameraPosition().zoom).build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void animateToLocation(double latitude, double longitude, float zoom) {
        try {
            if (latitude != 0.0 && longitude != 0.0) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(latitude, longitude))
                        .zoom(zoom).build();
                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } catch (Exception e) {

        }
    }

    public void configureAssignedDriver(boolean isAppRestarted) {
        isDriverAssigned = true;
        addDrawer.setIsDriverAssigned(isDriverAssigned);

        if (driverAssignedHeaderFrag != null) {
            driverAssignedHeaderFrag.releaseAllTask();
            driverAssignedHeaderFrag = null;
        }

        manageRideArea(false);

        driverDetailFrag = new DriverDetailFragment();
        driverAssignedHeaderFrag = new DriverAssignedHeaderFragment();

        Bundle bn = new Bundle();
        bn.putString("isAppRestarted", "" + isAppRestarted);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
        //params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
        params.bottomMargin = (int) getResources().getDimension(R.dimen._150sdp);

        if (driverAssignedHeaderFrag != null) {
            userTripBtnImgView.setVisibility(View.VISIBLE);
        }

        driverAssignedData = new HashMap<>();
        releaseScheduleNotificationTask();
        if (isAppRestarted == true) {

            JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);
            JSONObject driverDetailJson = generalFunc.getJsonObject("DriverDetails", userProfileJson);
            JSONObject driverCarDetailJson = generalFunc.getJsonObject("DriverCarDetails", userProfileJson);
            driverAssignedData.put("ePoolRide", generalFunc.getJsonValueStr("ePoolRide", tripDetailJson));
            driverAssignedData.put("eBookingFrom", generalFunc.getJsonValueStr("eBookingFrom", tripDetailJson));

            String vTripPaymentMode = generalFunc.getJsonValueStr("vTripPaymentMode", tripDetailJson);
            String tEndLat = generalFunc.getJsonValueStr("tEndLat", tripDetailJson);
            String tEndLong = generalFunc.getJsonValueStr("tEndLong", tripDetailJson);
            String tDaddress = generalFunc.getJsonValueStr("tDaddress", tripDetailJson);

            if (vTripPaymentMode.equals("Cash")) {
                isCashSelected = true;
            } else {
                isCashSelected = false;
            }

            assignedDriverId = generalFunc.getJsonValueStr("iDriverId", tripDetailJson);
            assignedTripId = generalFunc.getJsonValueStr("iTripId", tripDetailJson);
            eTripType = generalFunc.getJsonValueStr("eType", tripDetailJson);

            if (!tEndLat.equals("0.0") && !tEndLong.equals("0.0")
                    && !tDaddress.equals("Not Set") && !tEndLat.equals("") && !tEndLong.equals("")
                    && !tDaddress.equals("")) {
                isDestinationAdded = true;
                destAddress = tDaddress;
                destLocLatitude = tEndLat;
                destLocLongitude = tEndLong;
            }

            driverAssignedData.put("destLatitude", generalFunc.getJsonValueStr("tEndLat", tripDetailJson));
            driverAssignedData.put("eRental", generalFunc.getJsonValueStr("eRental", tripDetailJson));
            driverAssignedData.put("destLongitude", generalFunc.getJsonValueStr("tEndLong", tripDetailJson));
            driverAssignedData.put("PickUpLatitude", generalFunc.getJsonValueStr("tStartLat", tripDetailJson));
            driverAssignedData.put("PickUpLongitude", generalFunc.getJsonValueStr("tStartLong", tripDetailJson));
            driverAssignedData.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", tripDetailJson));
            driverAssignedData.put("vDeliveryConfirmCode", generalFunc.getJsonValueStr("vDeliveryConfirmCode", tripDetailJson));
            driverAssignedData.put("recipientNameTxt", generalFunc.getJsonValueStr("Running_Receipent_Detail", tripDetailJson));
            driverAssignedData.put("PickUpAddress", generalFunc.getJsonValueStr("tSaddress", tripDetailJson));
            driverAssignedData.put("vVehicleType", generalFunc.getJsonValueStr("vVehicleType", tripDetailJson));
            driverAssignedData.put("eIconType", generalFunc.getJsonValueStr("eIconType", tripDetailJson));
            driverAssignedData.put("eType", generalFunc.getJsonValueStr("eType", tripDetailJson));
            driverAssignedData.put("DriverTripStatus", generalFunc.getJsonValueStr("vTripStatus", driverDetailJson));
            driverAssignedData.put("DriverPhone", generalFunc.getJsonValueStr("vPhone", driverDetailJson));
            driverAssignedData.put("DriverPhoneCode", generalFunc.getJsonValueStr("vCode", driverDetailJson));
            driverAssignedData.put("DriverRating", generalFunc.getJsonValueStr("vAvgRating", driverDetailJson));
            driverAssignedData.put("DriverAppVersion", generalFunc.getJsonValueStr("iAppVersion", driverDetailJson));
            driverAssignedData.put("DriverLatitude", generalFunc.getJsonValueStr("vLatitude", driverDetailJson));
            driverAssignedData.put("DriverLongitude", generalFunc.getJsonValueStr("vLongitude", driverDetailJson));
            driverAssignedData.put("DriverImage", generalFunc.getJsonValueStr("vImage", driverDetailJson));
            driverAssignedData.put("DriverName", generalFunc.getJsonValueStr("vName", driverDetailJson));
            driverAssignedData.put("iGcmRegId_D", generalFunc.getJsonValueStr("iGcmRegId", driverDetailJson));
            driverAssignedData.put("DriverCarPlateNum", generalFunc.getJsonValueStr("vLicencePlate", driverCarDetailJson));
            driverAssignedData.put("DriverCarName", generalFunc.getJsonValueStr("make_title", driverCarDetailJson));
            driverAssignedData.put("DriverCarModelName", generalFunc.getJsonValueStr("model_title", driverCarDetailJson));
            driverAssignedData.put("DriverCarColour", generalFunc.getJsonValueStr("vColour", driverCarDetailJson));
            driverAssignedData.put("vCode", generalFunc.getJsonValueStr("vCode", driverDetailJson));
            driverAssignedData.put("ePoolRide", generalFunc.getJsonValueStr("ePoolRide", tripDetailJson));
            driverAssignedData.put("iStopId", generalFunc.getJsonValueStr("iStopId", tripDetailJson));
            driverAssignedData.put("eFly", generalFunc.getJsonValueStr("eFly", tripDetailJson));
            driverAssignedData.put("eDestinationMode", generalFunc.getJsonValueStr("eDestinationMode", tripDetailJson));


        } else {

            if (currentLoadedDriverList == null) {
                generalFunc.restartApp();
                return;
            }

            boolean isDriverIdMatch = false;
            for (int i = 0; i < currentLoadedDriverList.size(); i++) {
                HashMap<String, String> driverDataMap = currentLoadedDriverList.get(i);
                String iDriverId = driverDataMap.get("driver_id");

                if (iDriverId.equals(assignedDriverId)) {
                    isDriverIdMatch = true;

                    if (destLocation != null) {

                        driverAssignedData.put("destLatitude", destLocation.getLatitude() + "");
                        driverAssignedData.put("destLongitude", destLocation.getLongitude() + "");
                    }
                    driverAssignedData.put("PickUpLatitude", "" + getPickUpLocation().getLatitude());
                    driverAssignedData.put("PickUpLongitude", "" + getPickUpLocation().getLongitude());

                    if (mainHeaderFrag != null) {
                        driverAssignedData.put("PickUpAddress", mainHeaderFrag.getPickUpAddress());
                    } else {
                        driverAssignedData.put("PickUpAddress", pickUpLocationAddress);
                    }

                    driverAssignedData.put("vVehicleType", generalFunc.getSelectedCarTypeData(selectedCabTypeId, cabTypesArrList, "vVehicleType"));
                    driverAssignedData.put("eIconType", generalFunc.getSelectedCarTypeData(selectedCabTypeId, cabTypesArrList, "eIconType"));
                    driverAssignedData.put("vDeliveryConfirmCode", "");
                    driverAssignedData.put("recipientNameTxt", "");
                    driverAssignedData.put("DriverTripStatus", "");
                    driverAssignedData.put("DriverPhone", driverDataMap.get("vPhone_driver"));
                    driverAssignedData.put("DriverPhoneCode", driverDataMap.get("vPhoneCode_driver"));
                    driverAssignedData.put("DriverRating", driverDataMap.get("average_rating"));
                    driverAssignedData.put("DriverAppVersion", driverDataMap.get("iAppVersion"));
                    driverAssignedData.put("DriverLatitude", driverDataMap.get("Latitude"));
                    driverAssignedData.put("DriverLongitude", driverDataMap.get("Longitude"));
                    driverAssignedData.put("DriverImage", driverDataMap.get("driver_img"));
                    driverAssignedData.put("iGcmRegId_D", driverDataMap.get("iGcmRegId"));

                    driverAssignedData.put("DriverName", driverDataMap.get("Name"));
                    driverAssignedData.put("DriverCarPlateNum", driverDataMap.get("vLicencePlate"));
                    driverAssignedData.put("DriverCarName", driverDataMap.get("make_title"));
                    driverAssignedData.put("DriverCarModelName", driverDataMap.get("model_title"));
                    driverAssignedData.put("DriverCarColour", driverDataMap.get("vColour"));
                    driverAssignedData.put("eType", getCurrentCabGeneralType());
                    driverAssignedData.put("ePoolRide", driverDataMap.get("ePoolRide"));
                    driverAssignedData.put("iStopId", driverDataMap.get("iStopId"));
                    driverAssignedData.put("eFly", driverDataMap.get("eFly"));
                    driverAssignedData.put("eDestinationMode", driverDataMap.get("eDestinationMode"));

                    break;
                }
            }

            if (isDriverIdMatch == false) {
                generalFunc.restartApp();
                return;
            }
        }

        driverAssignedData.put("iDriverId", assignedDriverId);
        driverAssignedData.put("iTripId", assignedTripId);

        driverAssignedData.put("PassengerName", generalFunc.getJsonValueStr("vName", obj_userProfile));
        driverAssignedData.put("PassengerImageName", generalFunc.getJsonValueStr("vImgName", obj_userProfile));

        bn.putSerializable("TripData", driverAssignedData);
        driverAssignedHeaderFrag.setArguments(bn);
        driverAssignedHeaderFrag.setGoogleMap(getMap());
        if (!TextUtils.isEmpty(tripId)) {
            driverAssignedHeaderFrag.isBackVisible = true;
        }

        driverDetailFrag.setArguments(bn);


        Location pickUpLoc = new Location("");
        pickUpLoc.setLatitude(generalFunc.parseDoubleValue(0.0, driverAssignedData.get("PickUpLatitude")));
        pickUpLoc.setLongitude(generalFunc.parseDoubleValue(0.0, driverAssignedData.get("PickUpLongitude")));
        this.pickUpLocation = pickUpLoc;

        if (mainHeaderFrag != null) {
            mainHeaderFrag.releaseResources();
            mainHeaderFrag = null;
        }

        if (cabSelectionFrag != null) {
            stopOverPointsList.clear();
            cabSelectionFrag.releaseResources();
            cabSelectionFrag = null;
        }

        Utils.runGC();

        if (isnotification) {
            chatMsg();
        }

        setPanelHeight(175);

        try {
            super.onPostResume();
        } catch (Exception e) {

        }

        if (driverDetailFrag != null) {
            deliverArea.setVisibility(View.GONE);
            otherArea.setEnabled(false);
            deliverArea.setEnabled(false);
            rideArea.setEnabled(false);
        }

        if (!isFinishing()) {
            gMap.clear();

            getMap().setMyLocationEnabled(false);

            resetMapView();
            getMap().setPadding(0, 0, 0, Utils.dpToPx(getActContext(), 232));
            map.getView().requestLayout();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.headerContainer, driverAssignedHeaderFrag).commit();

            if (!isAppRestarted) {
                if (isFixFare) {
                    if (driverAssignedHeaderFrag != null) {
                        driverAssignedHeaderFrag.eConfirmByUser = "Yes";
                        driverAssignedHeaderFrag.handleEditDest();
                    }
                }
            }


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dragView, driverDetailFrag).commit();


            if (driverAssignedHeaderFrag != null) {
                userTripBtnImgView.setVisibility(View.VISIBLE);
            }

            String OPEN_CHAT = generalFunc.retrieveValue("OPEN_CHAT");
            if (Utils.checkText(OPEN_CHAT)) {
                JSONObject OPEN_CHAT_DATA_OBJ = generalFunc.getJsonObject(OPEN_CHAT);
                generalFunc.removeValue("OPEN_CHAT");
               /*
                Bundle bnChat = new Bundle();

                bnChat.putString("iFromMemberId", driverAssignedData.get("iDriverId"));
                bnChat.putString("FromMemberImageName", driverAssignedData.get("DriverImage"));
                bnChat.putString("iTripId", driverAssignedData.get("iTripId"));
                bnChat.putString("FromMemberName", driverAssignedData.get("DriverName"));
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);
                bnChat.putString("vBookingNo", tripDetailJson != null ? generalFunc.getJsonValueStr("vRideNo", tripDetailJson) : "");
*/
                if (OPEN_CHAT_DATA_OBJ != null)
                    new StartActProcess(getActContext()).startActWithData(ChatActivity.class, generalFunc.createChatBundle(OPEN_CHAT_DATA_OBJ));
            }

        } else {
            generalFunc.restartApp();
        }


    }

    private void resetMapView() {
        map.getView().invalidate();
        if (gMap != null) {
            gMap.setPadding(0, 0, 0, 0);
        }
        map.getView().requestLayout();
    }

    private void resetUserLocBtnView() {
        userLocBtnImgView.invalidate();
        userLocBtnImgView.requestLayout();
    }

    @Override
    public void onLocationUpdate(Location location) {

        if (location == null) {
            return;
        }

        if (getIntent().getStringExtra("latitude") != null && getIntent().getStringExtra("longitude") != null) {
            Location loc_ufx = new Location("gps");
            loc_ufx.setLatitude(GeneralFunctions.parseDoubleValue(0.0, getIntent().getStringExtra("latitude")));
            loc_ufx.setLongitude(GeneralFunctions.parseDoubleValue(0.0, getIntent().getStringExtra("longitude")));
            this.userLocation = loc_ufx;
        } else {
            this.userLocation = location;
        }

        if (isFirstLocation == true) {

            double currentZoomLevel = Utils.defaultZomLevel;

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude()))
                    .zoom((float) currentZoomLevel).build();

            if (cameraPosition != null && getMap() != null) {
                getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            if (pickUpLocation == null) {
                pickUpLocation = this.userLocation;
                initializeViews();
                if (dialog != null && isFromSourceDialog) {
                    dialog.dismiss();
                    checkForSourceLocation(selectedViewID);
                }
            }
            isFirstLocation = false;
        }
    }


    public void setETA(String time) {

        timeval = time;

        if (cabSelectionFrag != null) {
            cabSelectionFrag.handleSourceMarker(time);
            if (!(isMultiDelivery())) {
                cabSelectionFrag.mangeMrakerPostion();
            }

        }
    }

    public CameraPosition cameraForUserPosition() {

        try {
            if (cabSelectionFrag != null) {
                return null;
            }

            double currentZoomLevel = getMap() == null ? Utils.defaultZomLevel : getMap().getCameraPosition().zoom;
            // if (Utils.defaultZomLevel > currentZoomLevel) {
            currentZoomLevel = Utils.defaultZomLevel;
            // }
            String TripDetails = generalFunc.getJsonValue("TripDetails", userProfileJson);

            String vTripStatus = generalFunc.getJsonValue("vTripStatus", userProfileJson);
            if (generalFunc.isLocationEnabled()) {

                double startLat = 0.0;
                double startLong = 0.0;

                if (vTripStatus != null && startLat != 0.0 && startLong != 0.0 && ((vTripStatus.equals("Active") || vTripStatus.equals("On Going Trip")))) {

                    Location tempickuploc = new Location("temppickkup");

                    tempickuploc.setLatitude(startLat);
                    tempickuploc.setLongitude(startLong);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(tempickuploc.getLatitude(), tempickuploc.getLongitude()))
                            .zoom((float) currentZoomLevel).build();


                    return cameraPosition;


                } else {
//
                    // if (Utils.defaultZomLevel > currentZoomLevel) {
                    currentZoomLevel = Utils.defaultZomLevel;
                    //}
                    if (userLocation != null) {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude()))
                                .zoom((float) currentZoomLevel).build();

//                        pickUpLocation = userLocation;

                        return cameraPosition;
                    } else {
                        return null;
                    }
                }
            } else if (userLocation != null) {
                if (Utils.defaultZomLevel > currentZoomLevel) {
                    currentZoomLevel = Utils.defaultZomLevel;
                }
                if (userLocation != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude()))
                            .zoom((float) currentZoomLevel).build();

//                    pickUpLocation = userLocation;

                    return cameraPosition;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return null;

    }

    public void redirectToMapOrList(String choiceType, boolean autoLoad) {

        manageRideArea(false);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
        if (generalFunc.isRTLmode()) {
            params.leftMargin = Utils.dipToPixels(getActContext(), 10);
        } else {
            params.rightMargin = Utils.dipToPixels(getActContext(), 10);
        }

        if (autoLoad == true && currentUberXChoiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map)) {
            return;
        }

        this.currentUberXChoiceType = choiceType;

        mainHeaderFrag.listTxt.setBackgroundColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ?
                Color.parseColor("#FFFFFF") : getResources().getColor(R.color.appThemeColor_1));
        mainHeaderFrag.mapTxt.setBackgroundColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ?
                getResources().getColor(R.color.appThemeColor_1) : Color.parseColor(
                "#FFFFFF"));


        mainHeaderFrag.listImage.setBackground(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ?
                getResources().getDrawable(R.drawable.roundcolorect) : getResources().getDrawable(R.drawable.roundrect));
        mainHeaderFrag.mapImage.setBackground(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map) ?
                getResources().getDrawable(R.drawable.roundcolorect) : getResources().getDrawable(R.drawable.roundrect));
        mainHeaderFrag.filterImage.setBackground(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Filter) ?
                getResources().getDrawable(R.drawable.roundcolorect) : getResources().getDrawable(R.drawable.roundrect));

        if (choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map)) {
            mainHeaderFrag.uberXMainHeaderLayout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            mainHeaderFrag.uberXMainHeaderLayout.setBackgroundColor(Color.parseColor("#E6E8E7"));
        }

        mainHeaderFrag.listImage.setColorFilter(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ? ContextCompat.getColor(getActContext(), R.color.white) : ContextCompat.getColor(getActContext(), R.color.black));
        mainHeaderFrag.mapImage.setColorFilter(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map) ? ContextCompat.getColor(getActContext(), R.color.white) : ContextCompat.getColor(getActContext(), R.color.black));
        mainHeaderFrag.filterImage.setColorFilter(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Filter) ? ContextCompat.getColor(getActContext(), R.color.white) : ContextCompat.getColor(getActContext(), R.color.black));


        mainHeaderFrag.mapTxt.setTextColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ?
                Color.parseColor("#FFFFFF") : Color.parseColor("#1C1C1C"));
        mainHeaderFrag.listTxt.setTextColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ?
                Color.parseColor("#1C1C1C") : Color.parseColor("#FFFFFF"));


        if (driver_detail_bottomView != null || driver_detail_bottomView.getVisibility() == View.VISIBLE) {

            driver_detail_bottomView.setVisibility(View.GONE);
        }
        if (choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List)) {
            if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider")) {
                SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        //  getFilterList();
                        if (loadAvailCabs != null) {
                            loadAvailCabs.checkAvailableCabs();
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            uberXNoDriverTxt.setText(generalFunc.retrieveLangLBl("No Provider Available", "LBL_NO_PROVIDER_AVAIL_TXT"));

            if (!isUfxRideLater) {

                uberXDriverListArea.setVisibility(View.VISIBLE);
                uberXNoDriverTxt.setVisibility(View.GONE);
                ridelaterView.setVisibility(View.GONE);

                uberXDriverList.clear();
                if (uberXOnlineDriverListAdapter != null) {
                    uberXOnlineDriverListAdapter.notifyDataSetChanged();
                }
            }

            configDriverListForUfx();

        } else {
            (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
            uberXDriverListArea.setVisibility(View.GONE);
        }
    }

    public void configDriverListForUfx() {

        if (ufxFreqTask != null) {
            return;
        }

        if (isufxbackview) {
            return;
        }

        (findViewById(R.id.llFilter)).setVisibility(View.GONE);
        (findViewById(R.id.driverListAreaLoader)).setVisibility(View.VISIBLE);
        (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);
        ((MTextView) findViewById(R.id.searchingDriverTxt)).setText(generalFunc.retrieveLangLBl("Searching Provider", "LBL_SEARCH_PROVIDER_WAIT_TXT"));
        uberXNoDriverTxt.setVisibility(View.GONE);
        ridelaterView.setVisibility(View.GONE);

        if (currentLoadedDriverList != null) {
            uberXDriverList.addAll(currentLoadedDriverList);

            if (currentLoadedDriverList.size() > 0) {
                llFilter.setVisibility(View.VISIBLE);
            } else {
                llFilter.setVisibility(View.GONE);
            }
        }

        if (uberXOnlineDriverListAdapter == null) {
            uberXOnlineDriverListAdapter = new UberXOnlineDriverListAdapter(getActContext(), uberXDriverList, generalFunc, 0.0, 0.0);
            uberXOnlineDriversRecyclerView.setAdapter(uberXOnlineDriverListAdapter);
            uberXOnlineDriversRecyclerView.setLayoutManager(new LinearLayoutManager(getActContext()));

            uberXOnlineDriverListAdapter.setOnItemClickListener((v, position) -> {
                Utils.hideKeyboard(getActContext());

                if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider")) {
                    SelectedDriverId = currentLoadedDriverList.get(position).get("driver_id");
                    generalFunc.storeData(Utils.SELECTEDRIVERID, SelectedDriverId);
                    Bundle bn = new Bundle();
                    bn.putString("iDriverId", SelectedDriverId);
                    bn.putString("name", currentLoadedDriverList.get(position).get("Name") + " " + currentLoadedDriverList.get(position).get("LastName"));
                    bn.putString("vProviderLatitude", currentLoadedDriverList.get(position).get("Latitude"));
                    bn.putString("vProviderLongitude", currentLoadedDriverList.get(position).get("Longitude"));
                    bn.putString("serviceName", getIntent().getStringExtra("SelectvVehicleType"));
                    bn.putString("parentId", getIntent().getStringExtra("parentId"));
                    bn.putString("SelectedVehicleTypeId", getIntent().getStringExtra("SelectedVehicleTypeId"));
                    bn.putString("latitude", getIntent().getStringExtra("latitude"));
                    bn.putString("longitude", getIntent().getStringExtra("longitude"));
                    bn.putString("address", getIntent().getStringExtra("address"));
                    bn.putString("average_rating", currentLoadedDriverList.get(position).get("average_rating"));
                    bn.putString("driver_img", currentLoadedDriverList.get(position).get("driver_img"));
                    bn.putString("tProfileDescription", currentLoadedDriverList.get(position).get("tProfileDescription"));
                    bn.putString("IS_PROVIDER_ONLINE", currentLoadedDriverList.get(position).get("IS_PROVIDER_ONLINE"));
                    bn.putString("LBL_FEATURED_TXT", generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT"));
                    bn.putString("fname", currentLoadedDriverList.get(position).get("Name"));
                    new StartActProcess(getActContext()).startActWithData(MoreInfoActivity.class, bn);
                } else {
                    if (currentLoadedDriverList.size() > 0) {
                        SelectedDriverId = currentLoadedDriverList.get(position).get("driver_id");
                        generalFunc.storeData(Utils.SELECTEDRIVERID, SelectedDriverId);
                        loadAvailCabs.getMarkerDetails(SelectedDriverId);
                    }
                }
            });
        }

        if (uberXDriverList.size() > 0) {
            uberXNoDriverTxt.setVisibility(View.GONE);
            ridelaterView.setVisibility(View.GONE);
            (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
            (findViewById(R.id.searchingDriverTxt)).setVisibility(View.GONE);
        } else {
            if (!isUfxRideLater) {

                if (isfirstsearch) {
                    isfirstsearch = false;
                    (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);
                    ((MTextView) findViewById(R.id.searchingDriverTxt)).setText(generalFunc.retrieveLangLBl("Searching Provider", "LBL_SEARCH_PROVIDER_WAIT_TXT"));
                } else {
                    (findViewById(R.id.searchingDriverTxt)).setVisibility(View.GONE);
                    uberXNoDriverTxt.setVisibility(View.GONE);
                    (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
                    (findViewById(R.id.searchingDriverTxt)).setVisibility(View.GONE);
                    uberXNoDriverTxt.setVisibility(View.GONE);
                    ridelaterView.setVisibility(View.VISIBLE);
                    uberXNoDriverTxt.setVisibility(View.GONE);

                    (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
                }
            }


        }

        uberXOnlineDriverListAdapter.notifyDataSetChanged();
        ufxFreqTask = null;
    }

    private void closeBottomView() {

        if (driver_detail_bottomView == null) {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
        params.bottomMargin = Utils.dipToPixels(getActContext(), Utils.dpToPx(getActContext(), 10));
        userLocBtnImgView.requestLayout();
        Animation bottomUp = AnimationUtils.loadAnimation(getActContext(),
                R.anim.slide_out_down_anim);
        driver_detail_bottomView.startAnimation(bottomUp);
        driver_detail_bottomView.setVisibility(View.GONE);
    }


    private void setBottomView(final Marker marker) {

        if (loadAvailCabs == null) {
            return;
        }
        HashMap<String, String> map = loadAvailCabs.getMarkerDetails(marker);

        if (isPickUpLocationCorrect() == false || map.size() == 0) {
            closeBottomView();
            return;
        } else {


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            params.bottomMargin = Utils.dipToPixels(getActContext(), 150);
            if (generalFunc.isRTLmode()) {
                params.leftMargin = Utils.dipToPixels(getActContext(), 10);
            } else {
                params.rightMargin = Utils.dipToPixels(getActContext(), 10);
            }

            userLocBtnImgView.requestLayout();

            SimpleRatingBar bottomViewratingBar = (SimpleRatingBar) findViewById(R.id.bottomratingBar);
            MTextView nameTxt = (MTextView) findViewById(R.id.bottomdriverNameTxt);
            MTextView milesTxt = (MTextView) findViewById(R.id.bottommilesTxt);
            MTextView btnTxt = (MTextView) findViewById(R.id.bottombtnTxt);
            ImageView btnImg = (ImageView) findViewById(R.id.bottombtnImg);
            MTextView priceTxt = (MTextView) findViewById(R.id.bottompriceTxt);

            ImageView driverbottomStatus = (ImageView) findViewById(R.id.bottomdriverStatus);
            TextView eIsFeatured = (TextView) findViewById(R.id.bottomlabelFeatured);
            LinearLayout btnArea = (LinearLayout) findViewById(R.id.bottombtnArea);
            ImageView cancelImg = (ImageView) findViewById(R.id.cancelImg);
            CardView cardView = findViewById(R.id.cardView);
            cancelImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeBottomView();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setCardElevation(getResources().getDimension(R.dimen._8sdp));
            }

            if (generalFunc.isRTLmode()) {
                btnImg.setRotation(180);
                btnArea.setBackground(getActContext().getResources().getDrawable(R.drawable.login_border_rtl));
            }

            Logger.e("IS_PROVIDER_ONLINE", "::" + map.get("IS_PROVIDER_ONLINE"));


            if (map.get("fAmount") != null && !map.get("fAmount").trim().equals("")) {
                priceTxt.setText(map.get("fAmount"));
            } else {
                priceTxt.setVisibility(View.GONE);
            }

            String LBL_FEATURED_TXT = generalFunc.retrieveLangLBl("Featured", "LBL_FEATURED_TXT");

            if (map.get("eIsFeatured").equalsIgnoreCase("Yes")) {
                String LANGUAGE_IS_RTL_KEY = generalFunc.retrieveValue(Utils.LANGUAGE_IS_RTL_KEY);
                if (!LANGUAGE_IS_RTL_KEY.equals("") && LANGUAGE_IS_RTL_KEY.equals(Utils.DATABASE_RTL_STR)) {
                    eIsFeatured.setText(LBL_FEATURED_TXT);
                    eIsFeatured.setVisibility(View.VISIBLE);

                } else {
                    eIsFeatured.setText(LBL_FEATURED_TXT);
                    eIsFeatured.setVisibility(View.VISIBLE);

                }
            } else if (map.get("eIsFeatured").equalsIgnoreCase("No")) {
                eIsFeatured.setVisibility(View.GONE);

            }

            bottomViewratingBar.setRating(generalFunc.parseFloatValue(0, map.get("average_rating")));
            btnTxt.setText(generalFunc.retrieveLangLBl("More Info", "LBL_MORE_DETAILS"));


            nameTxt.setText(map.get("Name") + " " + map.get("LastName"));
            LikeButton likeButton = (LikeButton) findViewById(R.id.likeButtonbottom);
            if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes") && map.get("eFavDriver").equalsIgnoreCase("Yes")) {
                Logger.d("eFavDriver", "::" + map.get("Name") + " " + map.get("LastName"));
                likeButton.setVisibility(View.VISIBLE);
                likeButton.setLiked(true);
                likeButton.setEnabled(false);
            } else {
                Logger.d("noeFavDriver", "::" + map.get("Name") + " " + map.get("LastName"));
                likeButton.setVisibility(View.GONE);

            }

            double SourceLat = pickUpLocation.getLatitude();
            double SourceLong = pickUpLocation.getLongitude();
            double DesLat = generalFunc.parseDoubleValue(0.0, map.get("Latitude"));
            double DesLong = generalFunc.parseDoubleValue(0.0, map.get("Longitude"));
            //   milesTxt.setText(generalFunc.CalculationByLocationKm(SourceLat, SourceLong, DesLat, DesLong) + " km away");

            /*if (userProfileJson != null && !generalFunc.getJsonValue("eUnit", userProfileJson).equalsIgnoreCase("KMs")) {
                milesTxt.setText(GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "Miles") + generalFunc.retrieveLangLBl("km away", "LBL_MILE_DISTANCE_TXT"));
            } else {
                milesTxt.setText(GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "KM") + generalFunc.retrieveLangLBl("km away", "LBL_KM_AWAY"));

            }*/

            if (generalFunc.getJsonValue("eUnit", userProfileJson).equals("KMs")) {
                milesTxt.setText(String.format("%.2f", (float) GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "KM")) + " " + generalFunc.retrieveLangLBl("", "LBL_KM_DISTANCE_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_AWAY"));
            } else {
                milesTxt.setText(String.format("%.2f", (float) (GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "KM") * 0.621371)) + " " + generalFunc.retrieveLangLBl("", "LBL_MILE_DISTANCE_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_AWAY"));

            }

            String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + map.get("driver_id") + "/" + map.get("driver_img");

            Picasso.get()
                    .load(image_url)
                    .placeholder(R.mipmap.ic_no_pic_user)
                    .error(R.mipmap.ic_no_pic_user)
                    .into(((SelectableRoundedImageView) findViewById(R.id.bottomdriverImgView)));

            if (map.get("IS_PROVIDER_ONLINE").equalsIgnoreCase("Yes")) {

                driverbottomStatus.setColorFilter(ContextCompat.getColor(getActContext(), R.color.Green));
            } else {
                driverbottomStatus.setColorFilter(ContextCompat.getColor(getActContext(), R.color.Red));
            }

            btnArea.setOnClickListener(view -> {
                closeBottomView();
                loadAvailCabs.closeDialog();

                if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider")) {

                    HashMap<String, String> mapData = loadAvailCabs.getMarkerDetails(marker);
                    SelectedDriverId = mapData.get("driver_id");
                    Bundle bn = new Bundle();
                    bn.putString("iDriverId", SelectedDriverId);
                    bn.putString("name", mapData.get("Name") + " " + mapData.get("LastName"));
                    bn.putString("fname", mapData.get("Name"));
                    bn.putString("vProviderLatitude", mapData.get("Latitude"));
                    bn.putString("vProviderLongitude", mapData.get("Longitude"));
                    bn.putString("serviceName", getIntent().getStringExtra("SelectvVehicleType"));
                    bn.putString("parentId", getIntent().getStringExtra("parentId"));
                    bn.putString("SelectedVehicleTypeId", getIntent().getStringExtra("SelectedVehicleTypeId"));
                    bn.putString("latitude", getIntent().getStringExtra("latitude"));
                    bn.putString("longitude", getIntent().getStringExtra("longitude"));
                    bn.putString("address", getIntent().getStringExtra("address"));
                    bn.putString("average_rating", mapData.get("average_rating"));
                    bn.putString("driver_img", mapData.get("driver_img"));
                    bn.putString("IS_PROVIDER_ONLINE", mapData.get("IS_PROVIDER_ONLINE"));
                    bn.putString("LBL_FEATURED_TXT", generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT"));
                    new StartActProcess(getActContext()).startActWithData(MoreInfoActivity.class, bn);

                } else {
                    if (loadAvailCabs.getMarkerDetails(marker).size() > 0) {
                        loadAvailCabs.selectProviderId = "";
                    }
                    if (loadAvailCabs.getMarkerDetails(marker).isEmpty()) {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_PROVIDER_NOT_AVAILABLE"));
                    } else {
                        loadAvailCabs.loadDriverDetails(loadAvailCabs.getMarkerDetails(marker));
                    }
                }

            });

            //ll_bottom.setOnClickListener(view -> closeBottomView());
        }
    }


    public void OpenCardPaymentAct(boolean fromcabselection) {
        iswallet = true;
        Bundle bn = new Bundle();
        // bn.putString("UserProfileJson", userProfileJson);
        bn.putBoolean("fromcabselection", fromcabselection);
        new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
    }

    public boolean isPickUpLocationCorrect() {
        String pickUpLocAdd = mainHeaderFrag != null ? (mainHeaderFrag.getPickUpAddress().equals(
                generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT")) ? "" : mainHeaderFrag.getPickUpAddress()) : "";

        if (isUfx) {
            return true;
        }

        if (!pickUpLocAdd.equals("")) {
            return true;
        }
        return false;
    }

    public void continuePickUpProcess() {
        String pickUpLocAdd = mainHeaderFrag != null ? (mainHeaderFrag.getPickUpAddress().equals(
                generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT")) ? "" : mainHeaderFrag.getPickUpAddress()) : "";

        if (!pickUpLocAdd.equals("")) {
            if (isUfx) {
                checkSurgePrice("", null);
            } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("UberX")) {
                checkSurgePrice("", null);
            } else {
                setCabReqType(Utils.CabReqType_Now);
                checkSurgePrice("", null);
            }
        } else {
            if (isUfx) {
                checkSurgePrice("", null);
            } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("UberX")) {
                checkSurgePrice("", null);
            }
        }
    }

    public String getCurrentCabGeneralType() {

        if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) || app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
            Log.d(TAG, "setCurrentType: 11");
            if (!RideDeliveryType.equals("")) {
                if (isUfx) {
                    return Utils.CabGeneralType_UberX;
                }


                if (isDeliver(RideDeliveryType)) {
                    return "Deliver";
                } else {
                    return RideDeliveryType;
                }

            } else {
                if (isUfx) {
                    return Utils.CabGeneralType_UberX;
                }

                return Utils.CabGeneralType_Ride;
            }
        }


        if (cabSelectionFrag != null) {
            return cabSelectionFrag.getCurrentCabGeneralType();
        } else if (!eTripType.trim().equals("")) {
            return eTripType;
        }

        if (isUfx) {
            return Utils.CabGeneralType_UberX;
        }
        return app_type;
    }

    String selectedTime = "";

    public void chooseDateTime() {


        if (!isPickUpLocationCorrect()) {
            return;
        }

        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date) {

                        selectedDateTime = Utils.convertDateToFormat("yyyy-MM-dd HH:mm:ss", date);
                        selectedDateTimeZone = Calendar.getInstance().getTimeZone().getID();

                        if (!Utils.isValidTimeSelect(date, TimeUnit.HOURS.toMillis(1))) {
                            generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("Invalid pickup time", "LBL_INVALID_PICKUP_TIME"),
                                    generalFunc.retrieveLangLBl("Please make sure that pickup time is after atleast an hour from now.", "LBL_INVALID_PICKUP_NOTE_MSG"));
                            return;
                        }

                        if (!Utils.isValidTimeSelectForLater(date, TimeUnit.DAYS.toMillis(30))) {
                            generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("Invalid pickup time", "LBL_INVALID_PICKUP_TIME"),
                                    generalFunc.retrieveLangLBl("Please make sure that pickup time is after atleast an 1 month from now.", "LBL_INVALID_PICKUP_NOTE_MONTH_MSG"));
                            return;
                        }


                        setCabReqType(Utils.CabReqType_Later);

                        selectedTime = Utils.convertDateToFormat("yyyy-MM-dd HH:mm:ss", date);

                        if (isDeliver(getCurrentCabGeneralType())) {
                            setDeliverySchedule();

                        } else {

                            if (!cabSelectionFrag.handleRnetalView(Utils.convertDateToFormat(Utils.getDetailDateFormat(getActContext()), date))) {
                                checkSurgePrice(selectedTime, deliveryData);
                            }
                        }
                    }

                    @Override
                    public void onDateTimeCancel() {

                        if (cabSelectionFrag != null) {
                            //cabSelectionFrag.ride_now_btn.setClickable(true);
                            // cabSelectionFrag.ride_now_btn.setEnabled(true);

                        }

                    }

                })

                .setInitialDate(new Date())
                .setMinDate(Calendar.getInstance().getTime())
//                .setMinDate(getCurrentDate1hrAfter())
                //.setMaxDate(maxDate)
//                .setIs24HourTime(true)
                .setIs24HourTime(false)
                //.setTheme(SlideDateTimePicker.HOLO_DARK)
                .setIndicatorColor(getResources().getColor(R.color.appThemeColor_2))
                .build()
                .show();
    }

    public void setCabTypeList(ArrayList<HashMap<String, String>> cabTypeList) {
        this.cabTypeList = cabTypeList;
    }

    public void changeCabType(String selectedCabTypeId) {
        this.selectedCabTypeId = selectedCabTypeId;
        if (loadAvailCabs != null) {
            loadAvailCabs.setCabTypeId(this.selectedCabTypeId);
            loadAvailCabs.setPickUpLocation(pickUpLocation);
            loadAvailCabs.changeCabs();
        }
    }

    public String getSelectedCabTypeId() {

        return this.selectedCabTypeId;

    }

    public boolean isFixFare = false;

    public void checkSurgePrice(final String selectedTime, final Intent data) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "checkSurgePrice");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);
        parameters.put("SelectedCarTypeID", "" + getSelectedCabTypeId());

        parameters.put("iUserProfileId", iUserProfileId);
        parameters.put("iOrganizationId", iOrganizationId);
        parameters.put("vProfileEmail", vProfileEmail);
        parameters.put("ePaymentBy", ePaymentBy);

        if (cabSelectionFrag != null && cabSelectionFrag.isPoolCabTypeIdSelected) {
            parameters.put("ePool", "Yes");
        } else {
            parameters.put("ePool", "No");
        }

        if (cabSelectionFrag != null && !cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
            parameters.put("iRentalPackageId", cabSelectionFrag.iRentalPackageId);
        }
        if (!selectedTime.trim().equals("")) {
            parameters.put("SelectedTime", selectedTime);
        }

        if (getPickUpLocation() != null) {
            parameters.put("PickUpLatitude", "" + getPickUpLocation().getLatitude());
            parameters.put("PickUpLongitude", "" + getPickUpLocation().getLongitude());
        }

        if (getDestLocLatitude() != null && !getDestLocLatitude().equalsIgnoreCase("")) {
            parameters.put("DestLatitude", "" + getDestLocLatitude());
            parameters.put("DestLongitude", "" + getDestLocLongitude());
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                generalFunc.sendHeartBeat();

                String fOutStandingAmount = generalFunc.getJsonValue("fOutStandingAmount", responseString);

                if (GeneralFunctions.parseDoubleValue(0.0, fOutStandingAmount) > 0) {
                    if (data != null && data.hasExtra("isMulti")) // Skip this OutStanding handling for Multi Delivery
                    {
                        continueSurgeChargeExecution(responseString, data);
                    } else if (cabSelectionFrag != null) {
                        cabSelectionFrag.outstandingDialog(responseString, data);
                    } else {
                        continueSurgeChargeExecution(responseString, data);
                    }
                } else {
                    continueSurgeChargeExecution(responseString, data);
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void continueSurgeChargeExecution(String responseString, final Intent data) {
        boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

        if (isDataAvail == true) {


            if (data != null && data.hasExtra("isufxpayment")) {
                isUfxRequest(data);
            } else if (!selectedTime.trim().equals("")) {

                if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || isUfx) {
                    Log.d(TAG, "setCurrentType: 12");
                    ridelaterView.setVisibility(View.GONE);
                    uberXDriverListArea.setVisibility(View.GONE);
                    pickUpLocClicked();
                } else {

                    if (generalFunc.getJsonValue("eFlatTrip", responseString).equalsIgnoreCase("Yes")) {
                        isFixFare = true;
                        openFixChargeDialog(responseString, false, data);
                    } else {
                        handleRequest(data);
                    }

                }
            } else {
                if (generalFunc.getJsonValue("eFlatTrip", responseString).equalsIgnoreCase("Yes")) {
                    isFixFare = true;
                    openFixChargeDialog(responseString, false, data);
                } else {
                    if (!isUfx) {
                        handleRequest(data);
                    }
                }

                if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || isUfx) {
                    Log.d(TAG, "setCurrentType: 13");
                    ridelaterView.setVisibility(View.GONE);
                    uberXDriverListArea.setVisibility(View.GONE);
                    pickUpLocClicked();
                }
            }

        } else {

            if (data != null && data.hasExtra("isufxpayment")) {
//                openSurgeConfirmDialog(responseString, selectedTime, data); // enable this condition to enable Surge for Uberx
                isUfxRequest(data);
            } else if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || isUfx) {
                Log.d(TAG, "setCurrentType: 14");
                ridelaterView.setVisibility(View.GONE);
                uberXDriverListArea.setVisibility(View.GONE);
                pickUpLocClicked();
            }

            if (!isUfx) {
                if (generalFunc.getJsonValue("eFlatTrip", responseString).equalsIgnoreCase("Yes")) {
                    isFixFare = true;
                    openFixChargeDialog(responseString, true, data);

                } else {
                    openSurgeConfirmDialog(responseString, selectedTime, data);
                }
            }
        }
    }

    private void isUfxRequest(Intent data) {

        if (data.getStringExtra("paymenttype").equalsIgnoreCase("cash")) {
            isCashSelected = true;

        } else {
            isCashSelected = false;

        }
        if (bookingtype.equals(Utils.CabReqType_Now)) {
            requestPickUp();
        } else {
            setRideSchedule();
            bookRide();
        }
    }


    private void handleRequest(Intent data) {


        String driverIds = getAvailableDriverIds();

        JSONObject cabRequestedJson = new JSONObject();
        try {
            cabRequestedJson.put("Message", "CabRequested");
            cabRequestedJson.put("sourceLatitude", "" + getPickUpLocation().getLatitude());
            cabRequestedJson.put("sourceLongitude", "" + getPickUpLocation().getLongitude());
            cabRequestedJson.put("PassengerId", generalFunc.getMemberId());
            cabRequestedJson.put("PName", generalFunc.getJsonValue("vName", userProfileJson) + " "
                    + generalFunc.getJsonValue("vLastName", userProfileJson));
            cabRequestedJson.put("PPicName", generalFunc.getJsonValue("vImgName", userProfileJson));
            cabRequestedJson.put("PFId", generalFunc.getJsonValue("vFbId", userProfileJson));
            cabRequestedJson.put("PRating", generalFunc.getJsonValue("vAvgRating", userProfileJson));
            cabRequestedJson.put("PPhone", generalFunc.getJsonValue("vPhone", userProfileJson));
            cabRequestedJson.put("PPhoneC", generalFunc.getJsonValue("vPhoneCode", userProfileJson));
            cabRequestedJson.put("REQUEST_TYPE", getCurrentCabGeneralType());
            cabRequestedJson.put("eFly", eFly? "Yes" : "No");

            cabRequestedJson.put("selectedCatType", vUberXCategoryName);
            if (getDestinationStatus() == true) {
                cabRequestedJson.put("destLatitude", "" + getDestLocLatitude());
                cabRequestedJson.put("destLongitude", "" + getDestLocLongitude());
            } else {
                cabRequestedJson.put("destLatitude", "");
                cabRequestedJson.put("destLongitude", "");
            }

            if (deliveryData != null && !isMultiDelivery()) {
                cabRequestedJson.put("PACKAGE_TYPE", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.PACKAGE_TYPE_NAME_KEY));
            }


            getTollcostValue(driverIds, cabRequestedJson.toString(), data);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void openFixChargeDialog(String responseString, boolean isSurCharge, Intent data) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.surge_confirm_design, null);
        builder.setView(dialogView);

        MTextView payableAmountTxt;
        MTextView payableTxt;

        ((MTextView) dialogView.findViewById(R.id.headerMsgTxt)).setText(generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_FIX_FARE_HEADER")));


        ((MTextView) dialogView.findViewById(R.id.tryLaterTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRY_LATER"));

        payableTxt = (MTextView) dialogView.findViewById(R.id.payableTxt);
        payableAmountTxt = (MTextView) dialogView.findViewById(R.id.payableAmountTxt);
        if (!generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString).equalsIgnoreCase("")) {
            payableAmountTxt.setVisibility(View.VISIBLE);
            payableTxt.setVisibility(View.GONE);

            if (isSurCharge) {

                payableAmount = generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString) + " " + "(" + generalFunc.retrieveLangLBl("", "LBL_AT_TXT") + " " +
                        generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("SurgePrice", responseString)) + ")";
                ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(payableAmount));
            } else {
                payableAmount = generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString);
                ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(payableAmount));
            }
        } else {
            payableAmountTxt.setVisibility(View.GONE);
            payableTxt.setVisibility(View.VISIBLE);
        }

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_TXT"));
        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(view -> {
            alertDialog_surgeConfirm.dismiss();
            handleRequest(data);
        });

        (dialogView.findViewById(R.id.tryLaterTxt)).setOnClickListener(view -> {
            isFixFare = false;
            alertDialog_surgeConfirm.dismiss();
            closeRequestDialog(false);
        });


        alertDialog_surgeConfirm = builder.create();
        alertDialog_surgeConfirm.setCancelable(false);
        alertDialog_surgeConfirm.setCanceledOnTouchOutside(false);
        if (generalFunc.isRTLmode() == true) {
            generalFunc.forceRTLIfSupported(alertDialog_surgeConfirm);
        }
        alertDialog_surgeConfirm.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
        alertDialog_surgeConfirm.show();
    }

    public void openSurgeConfirmDialog(String responseString, final String selectedTime, Intent data) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.surge_confirm_design, null);
        builder.setView(dialogView);

        MTextView payableAmountTxt;
        MTextView payableTxt;

        ((MTextView) dialogView.findViewById(R.id.headerMsgTxt)).setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
        ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("SurgePrice", responseString)));

        ((MTextView) dialogView.findViewById(R.id.tryLaterTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRY_LATER"));

        payableTxt = (MTextView) dialogView.findViewById(R.id.payableTxt);
        payableAmountTxt = (MTextView) dialogView.findViewById(R.id.payableAmountTxt);
        payableTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYABLE_AMOUNT"));


        if (cabSelectionFrag != null && cabTypeList != null && cabTypeList.size() > 0 && cabTypeList.get(cabSelectionFrag.selpos).get("total_fare") != null && !cabTypeList.get(cabSelectionFrag.selpos).get("total_fare").equals("") && !cabTypeList.get(cabSelectionFrag.selpos).get("eRental").equals("Yes")) {

            payableAmountTxt.setVisibility(View.VISIBLE);
            payableTxt.setVisibility(View.GONE);
            if (isMultiDelivery() && data != null) {
                payableAmount = generalFunc.convertNumberWithRTL(data.getStringExtra("totalFare"));

            } else {
                payableAmount = generalFunc.convertNumberWithRTL(cabTypeList.get(cabSelectionFrag.selpos).get("total_fare"));

            }

            // payableAmountTxt.setText(generalFunc.retrieveLangLBl("Approx payable amount", "LBL_APPROX_PAY_AMOUNT") + ": " + payableAmount);
            if (isPoolCabTypeIdSelected) {

                payableAmountTxt.setText(generalFunc.retrieveLangLBl("Approx payable amount", "LBL_APPROX_PAY_AMOUNT") + ": " + Utils.getText(cabSelectionFrag.poolFareTxt));
            } else {
                payableAmountTxt.setText(generalFunc.retrieveLangLBl("Approx payable amount", "LBL_APPROX_PAY_AMOUNT") + ": " + payableAmount);
            }
            if (cabRquestType == Utils.CabReqType_Later) {
                payableAmountTxt.setVisibility(View.GONE);
            }
        } else {
            payableAmountTxt.setVisibility(View.GONE);
            payableTxt.setVisibility(View.VISIBLE);

        }

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_SURGE"));
        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(view -> {

            alertDialog_surgeConfirm.dismiss();

            if (data != null && data.hasExtra("isufxpayment")) {
                isUfxRequest(data);
            } else {
                handleRequest(data);
            }
        });

        (dialogView.findViewById(R.id.tryLaterTxt)).setOnClickListener(view -> {
            alertDialog_surgeConfirm.dismiss();
            closeRequestDialog(false);
            cabSelectionFrag.ride_now_btn.setClickable(true);
            isdelivernow = false;
            isdeliverlater = false;

        });


        alertDialog_surgeConfirm = builder.create();
        alertDialog_surgeConfirm.setCancelable(false);
        alertDialog_surgeConfirm.setCanceledOnTouchOutside(false);
        if (generalFunc.isRTLmode() == true) {
            generalFunc.forceRTLIfSupported(alertDialog_surgeConfirm);
        }

        alertDialog_surgeConfirm.show();
    }

    public void pickUpLocClicked() {

        configureDeliveryView(true);
        redirectToMapOrList(Utils.Cab_UberX_Type_List, false);

        Bundle bundle = new Bundle();
        bundle.putString("latitude", getIntent().getStringExtra("latitude"));
        bundle.putString("longitude", getIntent().getStringExtra("longitude"));
        bundle.putString("address", getIntent().getStringExtra("address"));
        bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));

        bundle.putString("type", bookingtype);
        bundle.putString("Quantity", getIntent().getStringExtra("Quantity"));

        bundle.putString("Pname", selectedprovidername);
        if (sdate.equals("")) {
            sdate = getIntent().getStringExtra("Sdate");

        }
        if (Stime.equals("")) {
            Stime = getIntent().getStringExtra("Stime");

        }
        bundle.putString("Sdate", sdate);
        bundle.putString("Stime", Stime);

        if (UfxAmount.equals("")) {
            bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
            bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
        } else {

            bundle.putString("SelectvVehiclePrice", UfxAmount + "");


            if (!getIntent().getStringExtra("Quantity").equals("0")) {
                UfxAmount = UfxAmount.replace(vCurrencySymbol, "");
                int qty = GeneralFunctions.parseIntegerValue(0, getIntent().getStringExtra("Quantity"));
                float amount = GeneralFunctions.parseFloatValue(0, UfxAmount);
                bundle.putString("Quantityprice", vCurrencySymbol + (qty * amount) + "");
            } else {
                bundle.putString("Quantityprice", UfxAmount + "");
            }


            UfxAmount = "";
        }

        bundle.putString("ACCEPT_CASH_TRIPS", ACCEPT_CASH_TRIPS);
        new StartActProcess(getActContext()).startActForResult(BookingSummaryActivity.class, bundle, Utils.UFX_REQUEST_CODE);
    }

    public void setDefaultView() {

        try {
            super.onPostResume();
        } catch (Exception e) {

        }


        try {


            cabRquestType = Utils.CabReqType_Now;


            if (mainHeaderFrag != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.headerContainer, mainHeaderFrag).commit();
            }


            if (!app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                Log.d(TAG, "setCurrentType: 15");
                if (mainHeaderFrag != null) {
                    mainHeaderFrag.releaseAddressFinder();
                }

            } else if (app_type.equalsIgnoreCase("UberX")) {
                Log.d(TAG, "setCurrentType: 16");
                (findViewById(R.id.dragView)).setVisibility(View.GONE);
                setUserLocImgBtnMargin(5);
            }


            configDestinationMode(false);
            userLocBtnImgView.performClick();
            Utils.runGC();

            if (!app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                Log.d(TAG, "setCurrentType: 17");
                configureDeliveryView(false);
            }

            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            try {
                new CreateAnimation(dragView, getActContext(), R.anim.design_bottom_sheet_slide_in, 600, true).startAnimation();
            } catch (Exception e) {

            }


            if (loadAvailCabs != null) {
                loadAvailCabs.setTaskKilledValue(false);
                loadAvailCabs.onResumeCalled();
            }
        } catch (Exception e) {

        }
    }

    public void setPanelHeight(int value) {

        int FragHeight = (cabSelectionFrag != null && cabSelectionFrag.fragmentHeight != 0) ? cabSelectionFrag.fragmentHeight : (driverDetailFrag != null ? driverDetailFrag.fragmentHeight : Utils.dipToPixels(getActContext(), value));
        sliding_layout.setPanelHeight(FragHeight);

        Log.d("SCROLLPANEL", "setPanelHeight: ");

      //  sliding_layout.setPanelHeight((cabSelectionFrag != null && cabSelectionFrag.fragmentHeight != 0) ? cabSelectionFrag.fragmentHeight : driverDetailFrag != null ? value : Utils.dipToPixels(getActContext(), value));

        //resize map padding/height according panel height

        resetMapView();


        if (isMultiDelivery() && cabSelectionFrag != null)
        {
            map.getView().setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);
            map.getView().requestLayout();
        }else {
            gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);
            map.getView().requestLayout();
        }

        if (userLocBtnImgView != null && (cabSelectionFrag != null || driverDetailFrag != null || driverAssignedHeaderFrag != null)) {
            resetUserLocBtnView();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) (userTripBtnImgView).getLayoutParams();
            if (isMultiDelivery()) {
                params.bottomMargin = sliding_layout.getPanelHeight() + Utils.dipToPixels(getActContext(), 5);
            } else {
                if (driverDetailFrag != null) {
                    // userTripBtnImgView.setX(getResources().getDimension(R.dimen._6sdp));
                    // userLocBtnImgView.setX(getResources().getDimension(R.dimen._6sdp));
//                    params.bottomMargin = sliding_layout.getPanelHeight();
//                    params1.bottomMargin = sliding_layout.getPanelHeight();
                    gMap.setPadding(0, 0, 0, (sliding_layout.getPanelHeight() + (int) getResources().getDimension(R.dimen._25sdp)));
                } else {
                    params.bottomMargin = sliding_layout.getPanelHeight() - Utils.dipToPixels(getActContext(), 50);
                }

            }

            if (generalFunc.isRTLmode()) {
                params.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
                params1.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
            } else {
                params.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
                params1.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
            }

            userLocBtnImgView.requestLayout();
        }
    }


    public void setFlyPanelHeight(int value) {
        sliding_layout.setPanelHeight(staticPanelHeight);
        Log.d("SCROLLPANEL", "setFlyPanelHeight: " + staticPanelHeight);
        if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

//        sliding_layout.setPanelHeight((flyStationSelectionFragment != null && flyStationSelectionFragment.fragmentHeight != 0) ? flyStationSelectionFragment.fragmentHeight : Utils.dipToPixels(getActContext(), value));

        //resize map padding/height according panel height

        Logger.d("SCROLLPANEL", "sliding_layout.getPanelHeight()" + sliding_layout.getPanelHeight());
        resetMapView();
        gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);
        map.getView().requestLayout();

        if (userLocBtnImgView != null /*&& flyStationSelectionFragment != null*/) {
            resetUserLocBtnView();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) (userTripBtnImgView).getLayoutParams();
            if (isMultiDelivery()) {
                //params.bottomMargin = sliding_layout.getPanelHeight() + Utils.dipToPixels(getActContext(), 5);
                params.bottomMargin = (int) (sliding_layout.getPanelHeight() + getResources().getDimension(R.dimen._5sdp));
            } else {
                if (driverDetailFrag != null) {
                    // userTripBtnImgView.setX(getResources().getDimension(R.dimen._6sdp));
                    // userLocBtnImgView.setX(getResources().getDimension(R.dimen._6sdp));
//                    params.bottomMargin = sliding_layout.getPanelHeight();
//                    params1.bottomMargin = sliding_layout.getPanelHeight();
                    gMap.setPadding(0, 0, 0, (sliding_layout.getPanelHeight() + (int) getResources().getDimension(R.dimen._25sdp)));
                } else {
                    //params.bottomMargin = sliding_layout.getPanelHeight() - Utils.dipToPixels(getActContext(), 50);
                    params.bottomMargin = (int) (sliding_layout.getPanelHeight() - getResources().getDimension(R.dimen._50sdp));
                }

            }

            if (generalFunc.isRTLmode()) {
                params.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
                params1.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
            } else {
                params.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
                params1.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
            }

            userLocBtnImgView.requestLayout();
        }
    }


    public void setFlySheetHeight() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        }

        //resize map padding/height according panel height

        resetMapView();
        gMap.setPadding(0, 0, 0, staticFlyPanelHeight + 5);
        map.getView().requestLayout();


        reSetButton(true);
    }


    public Location getPickUpLocation() {
        return this.pickUpLocation;
    }

    public String getPickUpLocationAddress() {
        return this.pickUpLocationAddress;
    }

    public void notifyCarSearching() {
        setETA("\n" + "--");

        if (getCurrentCabGeneralType().equals(Utils.CabGeneralType_UberX)) {
            ridelaterView.setVisibility(View.GONE);

            if (currentUberXChoiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List)) {

                (findViewById(R.id.driverListAreaLoader)).setVisibility(View.VISIBLE);
                (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);
                ((MTextView) findViewById(R.id.searchingDriverTxt)).setText(generalFunc.retrieveLangLBl("Searching Provider", "LBL_SEARCH_PROVIDER_WAIT_TXT"));
                uberXNoDriverTxt.setVisibility(View.GONE);

                uberXDriverList.clear();
                if (uberXOnlineDriverListAdapter != null) {
                    uberXOnlineDriverListAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    public void notifyNoCabs() {

        if (isufxbackview) {
            return;
        }

        setETA("\n" + "--");
        setCurrentLoadedDriverList(new ArrayList<HashMap<String, String>>());

        if (cabSelectionFrag != null) {
            noCabAvail = false;
            changeLable();
        }

        changeLable();

    }


    public void notifyCabsAvailable() {
        if (cabSelectionFrag != null && loadAvailCabs != null && loadAvailCabs.listOfDrivers != null && loadAvailCabs.listOfDrivers.size() > 0) {
            if (cabSelectionFrag.isroutefound) {
                if (loadAvailCabs.isAvailableCab) {
                    if (!timeval.equalsIgnoreCase("\n" + "--")) {
                        noCabAvail = true;
                    }
                }
            }
        }


        if (cabSelectionFrag != null) {
            cabSelectionFrag.setLabels(false);
        }
    }

    public void onMapCameraChanged() {
        if (cabSelectionFrag != null) {

            if (loadAvailCabs != null) {
                loadAvailCabs.filterDrivers(true);
            }

            if (mainHeaderFrag != null) {
                //notifyCarSearching();
//                if(cabSelectionFrag!=null) {
//                    cabSelectionFrag.img_ridelater.setEnabled(false);
//                }


                if (isDestinationMode == true) {
                    mainHeaderFrag.setDestinationAddress(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT"));
                } else {
                    mainHeaderFrag.setPickUpAddress(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT"));
                }
            }
        }
    }

    public void onAddressFound(String address) {
        try {


            if (cabSelectionFrag != null) {
                notifyCabsAvailable();
                if (cabSelectionFrag.img_ridelater != null) {
                    cabSelectionFrag.img_ridelater.setEnabled(true);
                }
                if (mainHeaderFrag != null) {

                    if (isDestinationMode == true) {
                        mainHeaderFrag.setDestinationAddress(address);
                    } else {
                        mainHeaderFrag.setPickUpAddress(address);
                    }
                }

                // cabSelectionFrag.findRoute("--");
            } else {
                if (isUserLocbtnclik) {
                    isUserLocbtnclik = false;

                    if (mainHeaderFrag != null && eFly) {

                        if (isDestinationMode == true) {
                            mainHeaderFrag.setDestinationAddress(address);
                        } else {
                            mainHeaderFrag.setPickUpAddress(address);
                        }
                    } else {
                        mainHeaderFrag.setPickUpAddress(address);
                    }

                }
            }
        } catch (Exception e) {
            Logger.d("onAddressFound2222", "::" + e.toString());
        }


    }

    public void setDestinationPoint(String destLocLatitude, String destLocLongitude, String destAddress, boolean isDestinationAdded) {

        if (destLocation == null) {
            destLocation = new Location("dest");
        }
        destLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, destLocLatitude));
        destLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, destLocLongitude));

        this.isDestinationAdded = isDestinationAdded;
        this.destLocLatitude = destLocLatitude;
        this.destLocLongitude = destLocLongitude;
        this.destAddress = destAddress;

        if (isMultiStopOverEnabled()) {
            handleMultiStopOverData();
        }

    }

    private void handleMultiStopOverData() {
        // reSet or add addresses
        if (mainHeaderFrag != null && stopOverPointsList.size() < 3 && isDestinationAdded) {
            mainHeaderFrag.addOrResetStopOverPoints(destLocation.getLatitude(), destLocation.getLongitude(), destAddress, false);
        }

        // Manage pool & rental for multi stop Over
        if (cabSelectionFrag != null) {
            // if MultiStop Over Added & pool selected then restrict from do pool trip
            if (stopOverPointsList.size() > 2 && cabSelectionFrag.isPoolCabTypeIdSelected && cabSelectionFrag.cabTypeList.size() > 0) {
                cabSelectionFrag.onItemClick(0);
            }

            cabSelectionFrag.manageRentalArea();
        }
    }

    public boolean getDestinationStatus() {
        return this.isDestinationAdded;
    }

    public String getDestLocLatitude() {
        return this.destLocLatitude;
    }

    public String getDestLocLongitude() {
        return this.destLocLongitude;
    }

    public String getDestAddress() {
        return this.destAddress;
    }

    public void setCashSelection(boolean isCashSelected) {
        this.isCashSelected = isCashSelected;
        if (loadAvailCabs != null) {
            loadAvailCabs.changeCabs();
        }
    }

    public String getCabReqType() {
        return this.cabRquestType;
    }

    public void setCabReqType(String cabRquestType) {
        this.cabRquestType = cabRquestType;
    }

    public Bundle getFareEstimateBundle() {
        Bundle bn = new Bundle();
        bn.putString("PickUpLatitude", "" + getPickUpLocation().getLatitude());
        bn.putString("PickUpLongitude", "" + getPickUpLocation().getLongitude());
        bn.putString("isDestinationAdded", "" + getDestinationStatus());
        bn.putString("DestLocLatitude", "" + getDestLocLatitude());
        bn.putString("DestLocLongitude", "" + getDestLocLongitude());
        bn.putString("DestLocAddress", "" + getDestAddress());
        bn.putString("SelectedCarId", "" + getSelectedCabTypeId());
        bn.putString("SelectedCabType", "" + generalFunc.getSelectedCarTypeData(getSelectedCabTypeId(), cabTypesArrList, "vVehicleType"));
//        bn.putString("UserProfileJson", "" + userProfileJson);

        return bn;
    }

    public void continueDeliveryProcess() {
        String pickUpLocAdd = mainHeaderFrag != null ? (mainHeaderFrag.getPickUpAddress().equals(
                generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT")) ? "" : mainHeaderFrag.getPickUpAddress()) : "";

        if (!pickUpLocAdd.equals("")) {

            if (isDeliver(getCurrentCabGeneralType())) {
                setDeliverySchedule();
            } else {
                checkSurgePrice("", null);
            }
        }
    }

    public void setRideSchedule() {
        isrideschedule = true;


        if (getDestinationStatus() == false && generalFunc.retrieveValue(Utils.APP_DESTINATION_MODE).equalsIgnoreCase(Utils.STRICT_DESTINATION)) {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_ADD_DEST_MSG_BOOK_RIDE"));
        }
    }

    public void setDeliverySchedule() {


        boolean skipDestCheck = false;
        if (isMultiDelivery()) {
            skipDestCheck = true;
        }
        if (!skipDestCheck && getDestinationStatus() == false) {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please add your destination location " +
                    "to deliver your package.", "LBL_ADD_DEST_MSG_DELIVER_ITEM"));
        } else {

            if (skipDestCheck) {
                if (getSelectedCabTypeId().equals("-1") || TextUtils.isEmpty(getSelectedCabTypeId())) {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", "LBL_NO_CAR_AVAIL_TXT"));
                    return;
                }

                Bundle bn = new Bundle();

                HashMap<String, String> map = new HashMap<>();
                map.put("isDeliverNow", "" + getCabReqType().equals(Utils.CabReqType_Now));
                map.put("vVehicleType", generalFunc.getSelectedCarTypeData(selectedCabTypeId, cabTypesArrList, "vVehicleType"));
                map.put("SelectedCar", "" + getSelectedCabTypeId());
                map.put("CashPayment", "" + isCashSelected);
                map.put("pickUpLocLattitude", "" + pickUpLocation.getLatitude());
                map.put("pickUpLocLongitude", "" + pickUpLocation.getLongitude());
                map.put("pickUpLocAddress", "" + pickUpLocationAddress);

                bn.putSerializable("selectedData", map);
                new StartActProcess(getActContext()).startActForResult(MultiDeliverySecondPhaseActivity.class, bn, Utils.MULTI_DELIVERY_DETAILS_REQ_CODE);

            } else {
                Bundle bn = new Bundle();
                bn.putString("isDeliverNow", "" + getCabReqType().equals(Utils.CabReqType_Now));
                new StartActProcess(getActContext()).startActForResult(EnterDeliveryDetailsActivity.class, bn, Utils.DELIVERY_DETAILS_REQ_CODE);

            }

        }
    }

    public void bookRide() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("type", "ScheduleARide");

        parameters.put("iUserProfileId", iUserProfileId);
        parameters.put("iOrganizationId", iOrganizationId);
        parameters.put("vProfileEmail", vProfileEmail);
        parameters.put("ePaymentBy", ePaymentBy);
        parameters.put("eWalletIgnore", eWalletIgnore);


        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && generalFunc.retrieveValue(Utils.BOOK_FOR_ELSE_ENABLE_KEY).equalsIgnoreCase("yes")) {

            if (generalFunc.containsKey(Utils.BFSE_SELECTED_CONTACT_KEY) && Utils.checkText(generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY))) {
                Gson gson = new Gson();
                String data1 = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                ContactModel contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                        }.getType()
                );


                if (contactdetails != null && Utils.checkText(contactdetails.name) && !contactdetails.name.equalsIgnoreCase("ME")) {
                    if (Utils.checkText(contactdetails.mobileNumber)) {
                        parameters.put("eBookSomeElseNumber", contactdetails.mobileNumber);
                        parameters.put("eBookForSomeOneElse", "Yes");
                    }
                    if (Utils.checkText(contactdetails.name)) {
                        parameters.put("eBookSomeElseName", contactdetails.name);
                    }
                }
            }

        }

        if (!selectReasonId.equalsIgnoreCase("")) {
            parameters.put("iTripReasonId", selectReasonId);
        }
        if (!vReasonTitle.equalsIgnoreCase("")) {
            parameters.put("vReasonTitle", vReasonTitle);
        }


        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                parameters.put("vDistance", cabSelectionFrag.distance);
                parameters.put("vDuration", cabSelectionFrag.time);
            }
        }

        if (mainHeaderFrag != null) {
            if (!app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                Log.d(TAG, "setCurrentType: 18");
                if (isUfx) {
                    parameters.put("pickUpLocAdd", pickUpLocationAddress);
                } else {
                    parameters.put("pickUpLocAdd", mainHeaderFrag != null ? mainHeaderFrag.getPickUpAddress() : "");
                }

            } else {
                parameters.put("pickUpLocAdd", pickUpLocationAddress);
            }
        }

        if (cabSelectionFrag != null && !cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
            parameters.put("iRentalPackageId", cabSelectionFrag.iRentalPackageId);

        }
        parameters.put("iUserId", generalFunc.getMemberId());
        if (isUfx) {
            parameters.put("pickUpLatitude", getIntent().getStringExtra("latitude"));
            parameters.put("pickUpLongitude", getIntent().getStringExtra("longitude"));
        } else {
            parameters.put("pickUpLatitude", "" + getPickUpLocation().getLatitude());
            parameters.put("pickUpLongitude", "" + getPickUpLocation().getLongitude());
            if (eFly)
                parameters.put("iFromStationId", iFromStationId);

        }
        parameters.put("destLocAdd", getDestAddress());
        if (eFly) {
            parameters.put("iToStationId", iToStationId);
        }

        parameters.put("destLatitude", getDestLocLatitude());
        parameters.put("destLongitude", getDestLocLongitude());
        parameters.put("iCabBookingId", iCabBookingId);
        parameters.put("scheduleDate", selectedDateTime);
        parameters.put("iVehicleTypeId", getSelectedCabTypeId());
        parameters.put("SelectedDriverId", SelectedDriverId);
        // parameters.put("TimeZone", selectedDateTimeZone);
        parameters.put("CashPayment", "" + isCashSelected);
        parameters.put("eWalletDebitAllow", eWalletDebitAllow);
        parameters.put("ePayWallet", "No");

        if (!isCashSelected && !generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
            parameters.put("eWalletDebitAllow", "Yes");
            parameters.put("ePayWallet", "Yes");
        }


        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                parameters.put("vDistance", cabSelectionFrag.distance);
                parameters.put("vDuration", cabSelectionFrag.time);
            }
        }
        // parameters.put("PickUpAddGeoCodeResult", tempPickupGeoCode);
        // parameters.put("DestAddGeoCodeResult", tempDestGeoCode);


        parameters.put("HandicapPrefEnabled", ishandicap ? "Yes" : "No");
        parameters.put("PreferFemaleDriverEnable", isfemale ? "Yes" : "No");
        parameters.put("ChildPrefEnabled", isChildSeat ? "Yes" : "No");
        parameters.put("WheelChairPrefEnabled", isWheelChair ? "Yes" : "No");
        parameters.put("vTollPriceCurrencyCode", tollcurrancy);
        String tollskiptxt = "";

        if (istollIgnore) {
            tollamount = 0;
            tollskiptxt = "Yes";

        } else {
            tollskiptxt = "No";
        }
        parameters.put("fTollPrice", tollamount + "");
        parameters.put("eTollSkipped", tollskiptxt);


        parameters.put("eType", getCurrentCabGeneralType());

        if (cabSelectionFrag != null) {
            parameters.put("PromoCode", cabSelectionFrag.getAppliedPromoCode());
        }
        if (app_type.equalsIgnoreCase("UberX") || isUfx) {
            Log.d(TAG, "setCurrentType: 19");
            parameters.put("PromoCode", appliedPromoCode);
            parameters.put("eType", Utils.CabGeneralType_UberX);
            if (getIntent().getStringExtra("Quantity").equals("0")) {
                parameters.put("Quantity", "1");
            } else {
                parameters.put("Quantity", getIntent().getStringExtra("Quantity"));
            }

            parameters.put("iUserAddressId", getIntent().getStringExtra("iUserAddressId"));
            parameters.put("tUserComment", userComment);
            parameters.put("scheduleDate", SelectDate);
        } else {
            parameters.put("scheduleDate", selectedDateTime);
        }

        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && stopOverPointsList.size() > 2) {
            JSONArray jaStore = new JSONArray();

            for (int j = 0; j < stopOverPointsList.size(); j++) {
                Stop_Over_Points_Data data1 = stopOverPointsList.get(j);
                if (data1.isDestination()) {
                    try {
                        JSONObject stopOverPointsObj = new JSONObject();
                        stopOverPointsObj.put("tDAddress", "" + data1.getDestAddress());
                        stopOverPointsObj.put("tDestLatitude", "" + data1.getDestLat());
                        stopOverPointsObj.put("tDestLongitude", "" + data1.getDestLong());
                        jaStore.put(stopOverPointsObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            parameters.put("stopoverpoint_arr", jaStore);

        }


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters, true);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setCancelAble(false);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {


                if (generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_PHONE_VERIFY") ||
                        generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_PHONE_VERIFY") ||
                        generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_VERIFY")) {
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + generalFunc.getJsonValue(Utils.message_str, responseString));
                    //  bn.putString("UserProfileJson", userProfileJson);
                    accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);

                    return;
                }

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);

                if (action.equals("1")) {
                    setDestinationPoint("", "", "", false);
                    setDefaultView();
                    isrideschedule = false;

                    if (isRebooking) {


                        showBookingAlert();
                    } else {

                        reSetFields();
                        showBookingAlert(generalFunc.retrieveLangLBl("",
                                generalFunc.getJsonValue(Utils.message_str, responseString)), false);
                    }

                } else {


                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);
                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }

                        boolean isCancelShow = false;
                        if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") && generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-2")) {
                            isCancelShow = true;
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_OK") :
                                generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), isCancelShow ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : "", button_Id -> {
                            if (button_Id == 1) {

                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                                    eWalletIgnore = "Yes";
                                    bookRide();
                                }
                            }
                        });


//                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
//                        generateAlert.setCancelable(false);
//                        generateAlert.setBtnClickList(btn_id -> {
//
//                            if (btn_id == 1) {
////                                isSomeOneDailog = false;
////                                eBookSomeElseNumber = "";
////                                eBookSomeElseName = "";
////                                isrideschedule = false;
//                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
//                            } else {
//                                eWalletIgnore = "Yes";
//                                bookRide();
//
//                            }
//
//                        });
//
//
//                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Low Wallet Balance. Please Pay Remaining Amount In Cash.", "LBL_WALLET_LOW_AMOUNT_MSG_TXT"));
//
//                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"));
//                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_OK"));
//                        generateAlert.showAlertBox();

                        return;

                    }
                    if (generalFunc.getJsonValue("isShowContactUs", responseString) != null && generalFunc.getJsonValue("isShowContactUs", responseString).equalsIgnoreCase("Yes")) {
                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(btn_id -> {
                            if (btn_id == 0) {


                            } else if (btn_id == 1) {
                                Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                // finish();

                            }
                        });

                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));

                        generateAlert.showAlertBox();
                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("",
                                generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    private void reSetFields() {
        // Logger.d("SET_PICKUP", "in 7-1 -> reSetFields");
        iFromStationId = "";
        iToStationId = "";
        generalFunc.resetStoredDetails();
    }


    public void chatMsg() {
        Bundle bn = new Bundle();
        bn.putString("iFromMemberId", driverDetailFrag.getTripData().get("iDriverId"));
        bn.putString("FromMemberImageName", driverDetailFrag.getTripData().get("DriverImage"));
        bn.putString("iTripId", driverDetailFrag.getTripData().get("iTripId"));
        bn.putString("FromMemberName", driverDetailFrag.getTripData().get("DriverName"));

        JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);
        bn.putString("vBookingNo", tripDetailJson != null ? generalFunc.getJsonValueStr("vRideNo", tripDetailJson) : "");


        JSONObject driverDetailsJson = generalFunc.getJsonObject("DriverDetails", tripDetailJson);

        bn.putString("DisplayCat", generalFunc.getJsonValue("eType", tripDetailJson) + "");
        bn.putString("vAvgRating", generalFunc.getJsonValueStr("vAvgRating", driverDetailsJson));
        bn.putString("ConvertedTripRequestDateDate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(generalFunc.getJsonValueStr("tTripRequestDateOrig", tripDetailJson), Utils.OriginalDateFormate, Utils.dateFormateInList)));


        new StartActProcess(getActContext()).startActWithData(ChatActivity.class, bn);
    }


    public void showBookingAlert() {
        reSetFields();
        SuccessDialog.showSuccessDialog(getActContext(), "", generalFunc.retrieveLangLBl("Your selected booking has been updated.", "LBL_BOOKING_UPDATED"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), false, () -> {
            reSetFields();
            Bundle bn = new Bundle();
            bn.putBoolean("isrestart", true);
            new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);

            finish();
        });

        /*
         generalFunc.resetStoredDetails();

        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());

        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            generalFunc.resetStoredDetails();

            generateAlert.closeAlertBox();
            Bundle bn = new Bundle();
            bn.putBoolean("isrestart", true);
            new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);

            finish();
        });
        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Your selected booking has been updated.", "LBL_BOOKING_UPDATED"));
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));

        generateAlert.showAlertBox();
        */
    }

    public void showBookingAlert(String message, boolean isongoing) {
        reSetFields();
        SuccessDialog.showSuccessDialog(getActContext(), generalFunc.retrieveLangLBl("Booking Successful", "LBL_BOOKING_ACCEPTED"), message, isongoing ? generalFunc.retrieveLangLBl("", "LBL_VIEW_ON_GOING_TRIPS") : generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS"), generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), false, () -> {
            reSetFields();
            if (isongoing) {
                generalFunc.resetStoredDetails();
                Bundle bn = new Bundle();
                if (driverAssignedHeaderFrag != null) {
                    bn.putString("isTripRunning", "yes");
                }
                bn.putBoolean("isRestart", true);
//                new StartActProcess(getActContext()).startActForResult(OnGoingTripsActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                new StartActProcess(getActContext()).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                finishAffinity();
            } else {
                Bundle bn = new Bundle();
                bn.putBoolean("isrestart", true);
                String selType = getIntent().getStringExtra("selType");
                if (selType != null) {
                    bn.putString("selType", selType);
                }
                new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);
                finish();
            }

        }, () -> {
            reSetFields();
            Bundle bn = new Bundle();
            if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equals(Utils.CabGeneralType_UberX)) {
                new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            } else {
                if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                    new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("Delivery")) {
                    bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                    bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));
                    new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);

                } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
                    bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                    bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));
                    new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                } else {
                    new StartActProcess(getActContext()).startActWithData(MainActivity.class, bn);
                }
            }
            finishAffinity();
        });

        /*
         android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_booking_view, null);
        builder.setView(dialogView);

        final MTextView titleTxt = (MTextView) dialogView.findViewById(R.id.titleTxt);
        final MTextView mesasgeTxt = (MTextView) dialogView.findViewById(R.id.mesasgeTxt);

        titleTxt.setText(generalFunc.retrieveLangLBl("Booking Successful", "LBL_BOOKING_ACCEPTED"));

        mesasgeTxt.setText(message);

        generalFunc.resetStoredDetails();

        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> {
            dialog.cancel();
            Bundle bn = new Bundle();
            if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equals(Utils.CabGeneralType_UberX)) {
                new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            } else {
                if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                    new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("Delivery")) {
                    bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                    bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));
                    new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);

                } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
                    bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                    bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));
                    new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                } else {
                    new StartActProcess(getActContext()).startActWithData(MainActivity.class, bn);
                }
            }
            finishAffinity();
        });

        if (isongoing) {

            builder.setPositiveButton(generalFunc.retrieveLangLBl("", "LBL_VIEW_ON_GOING_TRIPS"), (dialog, which) -> {

                generalFunc.resetStoredDetails();
                dialog.cancel();
                Bundle bn = new Bundle();
                if (driverAssignedHeaderFrag != null) {
                    bn.putString("isTripRunning", "yes");
                }
                bn.putBoolean("isRestart", true);
//                new StartActProcess(getActContext()).startActForResult(OnGoingTripsActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                new StartActProcess(getActContext()).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                finishAffinity();

            });

        } else {

            builder.setPositiveButton(generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS"), (dialog, which) -> {
                dialog.cancel();
                Bundle bn = new Bundle();
                bn.putBoolean("isrestart", true);
                String selType = getIntent().getStringExtra("selType");
                if (selType != null) {
                    bn.putString("selType", selType);
                }
                new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);
                finish();
            });
        }


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        */
    }

    public void scheduleDelivery(Intent data) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("type", "ScheduleARide");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eWalletIgnore", eWalletIgnore);
        parameters.put("pickUpLocAdd", mainHeaderFrag != null ? mainHeaderFrag.getPickUpAddress() : "");
        if (eFly) {
            parameters.put("iFromStationId", iFromStationId);
            parameters.put("iToStationId", iToStationId);
        }
        parameters.put("pickUpLatitude", "" + getPickUpLocation().getLatitude());
        parameters.put("pickUpLongitude", "" + getPickUpLocation().getLongitude());
        parameters.put("destLocAdd", getDestAddress());
        parameters.put("destLatitude", getDestLocLatitude());
        parameters.put("destLongitude", getDestLocLongitude());
        parameters.put("scheduleDate", selectedDateTime);
        parameters.put("iVehicleTypeId", getSelectedCabTypeId());
        //  parameters.put("TimeZone", selectedDateTimeZone);
        parameters.put("CashPayment", "" + isCashSelected);
        parameters.put("eType", "Deliver");
        parameters.put("eWalletDebitAllow", eWalletDebitAllow);
        parameters.put("ePayWallet", "No");

        if (!isCashSelected && !generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
            parameters.put("eWalletDebitAllow", "Yes");
            parameters.put("ePayWallet", "Yes");
        }

        parameters.put("iUserProfileId", iUserProfileId);
        parameters.put("iOrganizationId", iOrganizationId);
        parameters.put("vProfileEmail", vProfileEmail);
        parameters.put("ePaymentBy", ePaymentBy);
        if (!selectReasonId.equalsIgnoreCase("")) {
            parameters.put("iTripReasonId", selectReasonId);
        }
        if (!vReasonTitle.equalsIgnoreCase("")) {
            parameters.put("vReasonTitle", vReasonTitle);
        }

        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                parameters.put("vDistance", cabSelectionFrag.distance);
                parameters.put("vDuration", cabSelectionFrag.time);
            }
        }

        if (data != null && isMultiDelivery()) {
            String deliveryData = generalFunc.retrieveValue(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY);

            if (data.hasExtra("isMulti")) {
                parameters.put("CashPayment", "" + data.getStringExtra("isCashPayment"));
                parameters.put("PromoCode", data.getStringExtra("promocode"));
                parameters.put("ePaymentBy", data.getStringExtra("paymentMethod"));
            }

            if (data.hasExtra("total_del_dist")) {
                parameters.put("total_del_dist", "" + data.getStringExtra("total_del_dist"));
            }

            if (data.hasExtra("total_del_time")) {
                parameters.put("total_del_time", "" + data.getStringExtra("total_del_time"));
            }

            JSONArray array = generalFunc.getJsonArray(deliveryData);
            parameters.put("delivery_arr", array);
        } else {
            String data1 = generalFunc.retrieveValue(Utils.DELIVERY_DETAILS_KEY);
            JSONArray deliveriesArr = generalFunc.getJsonArray("deliveries", data1);
            if (deliveriesArr != null) {
                for (int j = 0; j < deliveriesArr.length(); j++) {
                    JSONObject ja = generalFunc.getJsonObject(deliveriesArr, j);
                    parameters.put("iPackageTypeId", generalFunc.getJsonValue(PACKAGE_TYPE_ID_KEY, ja.toString()));
                    parameters.put("vReceiverName", data.getStringExtra(EnterDeliveryDetailsActivity.RECEIVER_NAME_KEY));
                    parameters.put("vReceiverMobile", data.getStringExtra(EnterDeliveryDetailsActivity.RECEIVER_MOBILE_KEY));
                    parameters.put("tPickUpIns", data.getStringExtra(EnterDeliveryDetailsActivity.PICKUP_INS_KEY));
                    parameters.put("tDeliveryIns", data.getStringExtra(EnterDeliveryDetailsActivity.DELIVERY_INS_KEY));
                    parameters.put("tPackageDetails", data.getStringExtra(EnterDeliveryDetailsActivity.PACKAGE_DETAILS_KEY));
                }
            }
        }


        String tollskiptxt = "";

        if (istollIgnore) {
            tollskiptxt = "Yes";
            tollamount = 0;
        } else {
            tollskiptxt = "No";
        }
        parameters.put("fTollPrice", tollamount + "");
        parameters.put("vTollPriceCurrencyCode", tollcurrancy);
        parameters.put("eTollSkipped", tollskiptxt);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters, true);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setCancelAble(false);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                if (generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_PHONE_VERIFY") ||
                        generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_PHONE_VERIFY") ||
                        generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_VERIFY")) {
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + generalFunc.getJsonValue(Utils.message_str, responseString));
                    accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);
                    return;
                }

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);

                if (action.equals("1")) {

                    generalFunc.removeValue(Utils.DELIVERY_DETAILS_KEY);
                    setDestinationPoint("", "", "", false);
                    setDefaultView();

                    if (isRebooking) {
                        showBookingAlert();
                    } else {
                        reSetFields();

                        showBookingAlert(generalFunc.retrieveLangLBl("",
                                generalFunc.getJsonValue(Utils.message_str, responseString)), false);
                    }
                } else {


                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);

                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }


                        boolean isCancelShow = false;
                        if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") && generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-2")) {
                            isCancelShow = true;
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_OK") :
                                generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), isCancelShow ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : "", button_Id -> {
                            if (button_Id == 1) {
                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                                    eWalletIgnore = "Yes";
                                    scheduleDelivery(data);
                                }
                            }
                        });


//                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
//                        generateAlert.setCancelable(false);
//                        generateAlert.setBtnClickList(btn_id -> {
//
//                            if (btn_id == 1) {
////                                isSomeOneDailog = false;
////                                eBookSomeElseNumber = "";
////                                eBookSomeElseName = "";
////                                isrideschedule = false;
//                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
//                            } else {
//                                eWalletIgnore = "Yes";
//                                scheduleDelivery(data);
//
//                            }
//
//                        });
//
//
//                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Low Wallet Balance. Please Pay Remaining Amount In Cash.", "LBL_WALLET_LOW_AMOUNT_MSG_TXT"));
//
//                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"));
//                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_OK"));
//                        generateAlert.showAlertBox();

                        return;

                    }

                    if (generalFunc.getJsonValue("isShowContactUs", responseString) != null && generalFunc.getJsonValue("isShowContactUs", responseString).equalsIgnoreCase("Yes")) {
                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(btn_id -> {
                            if (btn_id == 0) {


                            } else if (btn_id == 1) {
                                Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                // finish();

                            }
                        });

                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));

                        generateAlert.showAlertBox();
                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("",
                                generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();

    }

    public void deliverNow(Intent data) {

        this.deliveryData = data;


        requestPickUp();
    }

    public void requestPickUp() {
        setLoadAvailCabTaskValue(true);

        try {
            requestNearestCab = new RequestNearestCab(getActContext(), generalFunc);
            requestNearestCab.run();
        } catch (Exception e) {

        }

        String driverIds = getAvailableDriverIds();

        JSONObject cabRequestedJson = new JSONObject();
        try {
            cabRequestedJson.put("Message", "CabRequested");
            cabRequestedJson.put("sourceLatitude", "" + getPickUpLocation().getLatitude());
            cabRequestedJson.put("sourceLongitude", "" + getPickUpLocation().getLongitude());
            cabRequestedJson.put("PassengerId", generalFunc.getMemberId());
            cabRequestedJson.put("PName", generalFunc.getJsonValue("vName", userProfileJson) + " "
                    + generalFunc.getJsonValue("vLastName", userProfileJson));
            cabRequestedJson.put("PPicName", generalFunc.getJsonValue("vImgName", userProfileJson));
            cabRequestedJson.put("PFId", generalFunc.getJsonValue("vFbId", userProfileJson));
            cabRequestedJson.put("PRating", generalFunc.getJsonValue("vAvgRating", userProfileJson));
            cabRequestedJson.put("PPhone", generalFunc.getJsonValue("vPhone", userProfileJson));
            cabRequestedJson.put("PPhoneC", generalFunc.getJsonValue("vPhoneCode", userProfileJson));
            cabRequestedJson.put("REQUEST_TYPE", getCurrentCabGeneralType());
            cabRequestedJson.put("eFly", eFly == true ? "Yes" : "No");

            cabRequestedJson.put("selectedCatType", vUberXCategoryName);
            if (getDestinationStatus() == true) {
                cabRequestedJson.put("destLatitude", "" + getDestLocLatitude());
                cabRequestedJson.put("destLongitude", "" + getDestLocLongitude());
            } else {
                cabRequestedJson.put("destLatitude", "");
                cabRequestedJson.put("destLongitude", "");
            }

            if (deliveryData != null && !isMultiDelivery()) {
                cabRequestedJson.put("PACKAGE_TYPE", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.PACKAGE_TYPE_NAME_KEY));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!generalFunc.getJsonValue("Message", cabRequestedJson.toString()).equals("")) {
            requestNearestCab.setRequestData(driverIds, cabRequestedJson.toString());

            if (DRIVER_REQUEST_METHOD.equals("All")) {
                driverIds = sendRequestAsPerFav(driverIds);
                sendReqToAll(driverIds, cabRequestedJson.toString());
            } else if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
                Logger.e("reqSentErrorDialog", "::sendReqByDist___");
                sendReqByDist(driverIds, cabRequestedJson.toString());

            } else {
                driverIds = sendRequestAsPerFav(driverIds);
                sendReqToAll(driverIds, cabRequestedJson.toString());
            }
        }


    }

    private String sendRequestAsPerFav(String oriDriverIds) {
        Logger.e("reqSentErrorDialog", ":: sendRequestAsPerFav");
        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
            String favDriverIds = getDriverID(true);
            String nonfavDriverIds = getDriverID(false);

            String driverIds = "";

            if (Utils.checkText(favDriverIds) && Utils.checkText(nonfavDriverIds)) {
                driverIds = favDriverIds + "-" + nonfavDriverIds;
            } else if (Utils.checkText(favDriverIds)) {
                driverIds = favDriverIds;
            } else if (Utils.checkText(nonfavDriverIds)) {
                driverIds = nonfavDriverIds;
            } else {
                driverIds = oriDriverIds;
            }

            return driverIds;
        } else {
            return oriDriverIds;
        }

    }

    public void sendReqToAll(String driverIds, String cabRequestedJson) {
        isreqnow = true;
        Logger.e("reqSentErrorDialog", ":: sendReqToAll");
        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {

            requestSentToFavDrivers = false;

            if (driverIds.contains("-")) {
                driverIDS = driverIds.split("-");
                sendRequestToDrivers(driverIDS[0], cabRequestedJson);
            } else {
                requestSentToFavDrivers = true;
                sendRequestToDrivers(driverIds, cabRequestedJson);
            }
        } else {
            sendRequestToDrivers(driverIds, cabRequestedJson);
        }

        if (allCabRequestTask != null) {
            allCabRequestTask.stopRepeatingTask();
            allCabRequestTask = null;
        }

        int interval = generalFunc.parseIntegerValue(30, generalFunc.getJsonValue("RIDER_REQUEST_ACCEPT_TIME", generalFunc.retrieveValue(Utils.USER_PROFILE_JSON)));

        allCabRequestTask = new UpdateFrequentTask((interval + 5) * 1000);
        Logger.e("UpdateFrequentTask", ":: sendReqToAll");
        allCabRequestTask.startRepeatingTask();
        allCabRequestTask.setTaskRunListener(() -> {

            if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
                if (!requestSentToFavDrivers && Utils.checkText(driverIDS[1])) {
                    requestSentToFavDrivers = true;
                    sendRequestToDrivers(driverIDS[1], cabRequestedJson);
                    Logger.e("reqSentErrorDialog", ":: Test2 setRetryReqBtn");
                    sendReqToNonFav(interval);
                } else {
                    Logger.e("reqSentErrorDialog", ":: Test2 setRetryReqBtn");
                    setRetryReqBtn(true);
                    allCabRequestTask.stopRepeatingTask();
                }

            } else {
                Logger.e("reqSentErrorDialog", ":: Test1 setRetryReqBtn");
                setRetryReqBtn(true);
                allCabRequestTask.stopRepeatingTask();
            }
        });

    }


    public void sendReqToNonFav(int interval) {
        Logger.e("reqSentErrorDialog", ":: sendReqToNonFav");

        if (allNonFavCabRequestTask != null) {
            allNonFavCabRequestTask.stopRepeatingTask();
            allNonFavCabRequestTask = null;
        }

        allNonFavCabRequestTask = new UpdateFrequentTask((interval + 5) * 1000);
        Logger.e("UpdateFrequentTask", ":: sendReqToNonFav");
        allNonFavCabRequestTask.startRepeatingTask();
        allNonFavCabRequestTask.setTaskRunListener(() -> {
            Logger.e("reqSentErrorDialog", ":: Test3 setRetryReqBtn");
            setRetryReqBtn(true);
            allNonFavCabRequestTask.stopRepeatingTask();
            if (allCabRequestTask != null)
                allCabRequestTask.stopRepeatingTask();

        });
    }


    public void sendReqByDist(String driverIds, String cabRequestedJson) {
        if (sendNotificationToDriverByDist == null) {
            sendNotificationToDriverByDist = new SendNotificationsToDriverByDist(driverIds, cabRequestedJson);
        } else {
            sendNotificationToDriverByDist.startRepeatingTask();
        }
    }

    public void setRetryReqBtn(boolean isVisible) {
        if (isVisible) {
            if (requestNearestCab != null) {
                //requestNearestCab.setVisibilityOfRetryArea(View.VISIBLE);
                requestNearestCab.setVisibleBottomArea(View.VISIBLE);
            }
        } else {
            if (requestNearestCab != null) {
//                requestNearestCab.setVisibilityOfRetryArea(View.GONE);
                requestNearestCab.setInVisibleBottomArea(View.GONE);
            }
        }
    }

    public void retryReqBtnPressed(String driverIds, String cabRequestedJson) {

        if (DRIVER_REQUEST_METHOD.equals("All")) {
            driverIds = sendRequestAsPerFav(driverIds);
            sendReqToAll(driverIds, cabRequestedJson.toString());
        } else if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
            Logger.e("reqSentErrorDialog", ":: sendReqByDist___2");
            sendReqByDist(driverIds, cabRequestedJson.toString());
        } else {
            driverIds = sendRequestAsPerFav(driverIds);
            sendReqToAll(driverIds, cabRequestedJson.toString());
        }

        setRetryReqBtn(false);
    }

    public String getDriverID(boolean getFavId) {

        String driverIds = "";

        ArrayList<HashMap<String, String>> favLoadedDriverList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> nonFavLoadedDriverList = new ArrayList<HashMap<String, String>>();
        String fav_driverIds = "";
        String noFav_driverIds = "";


        if (currentLoadedDriverList == null) {

            return driverIds;
        }

        ArrayList<HashMap<String, String>> finalLoadedDriverList = new ArrayList<HashMap<String, String>>();
        finalLoadedDriverList.addAll(currentLoadedDriverList);

        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes") && DRIVER_REQUEST_METHOD.equalsIgnoreCase("ALL")) {


            for (int j = 0; j < finalLoadedDriverList.size(); j++) {
                HashMap<String, String> driversDataMap = finalLoadedDriverList.get(j);
                String eFavDriver = driversDataMap.get("eFavDriver");

                if (eFavDriver.equalsIgnoreCase("Yes")) {
                    favLoadedDriverList.add(driversDataMap);
                } else {
                    nonFavLoadedDriverList.add(driversDataMap);
                }
            }

            Logger.d("driverID", "favDriver " + favLoadedDriverList.toString());
            Logger.d("driverID", "noFavDriver" + nonFavLoadedDriverList.toString());

            if (favLoadedDriverList.size() > 0 && getFavId) {

                for (int fd = 0; fd < favLoadedDriverList.size(); fd++) {
                    String iDriverId = favLoadedDriverList.get(fd).get("driver_id");

                    fav_driverIds = fav_driverIds.equals("") ? iDriverId : (fav_driverIds + "," + iDriverId);
                }


                Logger.d("driverID", "_fav " + fav_driverIds);
            }

            if (nonFavLoadedDriverList.size() > 0 && !getFavId) {
                for (int nfd = 0; nfd < nonFavLoadedDriverList.size(); nfd++) {
                    String iDriverId = nonFavLoadedDriverList.get(nfd).get("driver_id");

                    noFav_driverIds = noFav_driverIds.equals("") ? iDriverId : (noFav_driverIds + "," + iDriverId);
                }


                Logger.d("driverID", "no_fav " + noFav_driverIds);
            }
        }

        return getFavId ? fav_driverIds : noFav_driverIds;

    }


    public void setLoadAvailCabTaskValue(boolean value) {
        if (loadAvailCabs != null) {
            loadAvailCabs.setTaskKilledValue(value);
        }
    }

    public void setCurrentLoadedDriverList(ArrayList<HashMap<String, String>> currentLoadedDriverList) {
        this.currentLoadedDriverList = currentLoadedDriverList;
        if (app_type.equalsIgnoreCase("UberX") || isUfx) {
            Log.d(TAG, "setCurrentType: 20");
            // load list here but wait for 5 seconds
            redirectToMapOrList(Utils.Cab_UberX_Type_List, true);
            findViewById(R.id.driverListAreaLoader).setVisibility(View.GONE);
        }
    }

    public ArrayList<String> getDriverLocationChannelList() {

        ArrayList<String> channels_update_loc = new ArrayList<>();

        if (currentLoadedDriverList != null) {

            for (int i = 0; i < currentLoadedDriverList.size(); i++) {
                channels_update_loc.add(Utils.pubNub_Update_Loc_Channel_Prefix + "" + (currentLoadedDriverList.get(i).get("driver_id")));
            }

        }
        return channels_update_loc;
    }

    public ArrayList<String> getDriverLocationChannelList(ArrayList<HashMap<String, String>> listData) {

        ArrayList<String> channels_update_loc = new ArrayList<>();

        if (listData != null) {

            for (int i = 0; i < listData.size(); i++) {
                channels_update_loc.add(Utils.pubNub_Update_Loc_Channel_Prefix + "" + (listData.get(i).get("driver_id")));
            }

        }
        return channels_update_loc;
    }

    public String getAvailableDriverIds() {
        String driverIds = "";

        if (currentLoadedDriverList == null) {
            return driverIds;
        }

        ArrayList<HashMap<String, String>> finalLoadedDriverList = new ArrayList<HashMap<String, String>>();
        finalLoadedDriverList.addAll(currentLoadedDriverList);

        if (DRIVER_REQUEST_METHOD.equals("Distance")) {
            Collections.sort(finalLoadedDriverList, new HashMapComparator("DIST_TO_PICKUP"));
        }


        if (!DRIVER_REQUEST_METHOD.equals("All") && generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
            ArrayList<HashMap<String, String>> favDriverLoadedDriverList = new ArrayList<HashMap<String, String>>();
            ArrayList<HashMap<String, String>> sequeceLoadedDriverList = new ArrayList<HashMap<String, String>>();

            for (HashMap<String, String> item : finalLoadedDriverList) {
                if (item.get("eFavDriver").equalsIgnoreCase("Yes")) {
                    favDriverLoadedDriverList.add(item);
                } else {
                    sequeceLoadedDriverList.add(item);
                }
            }
            if (DRIVER_REQUEST_METHOD.equals("Distance")) {
                Collections.sort(sequeceLoadedDriverList, new HashMapComparator("DIST_TO_PICKUP"));
            }

            finalLoadedDriverList.clear();
            finalLoadedDriverList.addAll(favDriverLoadedDriverList);
            finalLoadedDriverList.addAll(sequeceLoadedDriverList);
        }
       /* else if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {

            Collections.sort(finalLoadedDriverList, new favDriverComparator("eFavDriver", 0.0));
        }*/


        Logger.d("finalLoadedDriverList", "After Filter" + finalLoadedDriverList.size());

        for (int i = 0; i < finalLoadedDriverList.size(); i++) {
            String iDriverId = finalLoadedDriverList.get(i).get("driver_id");

            driverIds = driverIds.equals("") ? iDriverId : (driverIds + "," + iDriverId);
        }

        return driverIds;
    }

    boolean isSomeOneDailog = true;

    public void sendRequestToDrivers(String driverIds, String cabRequestedJson) {


        HashMap<String, Object> requestCabData = new HashMap<String, Object>();
        requestCabData.put("type", "sendRequestToDrivers");
        requestCabData.put("message", cabRequestedJson);
        requestCabData.put("userId", generalFunc.getMemberId());
        requestCabData.put("CashPayment", "" + isCashSelected);

        requestCabData.put("PickUpAddress", getPickUpLocationAddress());
        requestCabData.put("iFromStationId", iFromStationId);
        requestCabData.put("eWalletIgnore", eWalletIgnore);


        requestCabData.put("vTollPriceCurrencyCode", tollcurrancy);
        requestCabData.put("eWalletDebitAllow", eWalletDebitAllow);
        requestCabData.put("ePayWallet", "No");

        if (!isCashSelected && !generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
            requestCabData.put("eWalletDebitAllow", "Yes");
            requestCabData.put("ePayWallet", "Yes");
        }


        if (!selectReasonId.equalsIgnoreCase("")) {
            requestCabData.put("iTripReasonId", selectReasonId);
        }
        if (!vReasonTitle.equalsIgnoreCase("")) {
            requestCabData.put("vReasonTitle", vReasonTitle);
        }

        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && generalFunc.retrieveValue(Utils.BOOK_FOR_ELSE_ENABLE_KEY).equalsIgnoreCase("yes")) {

            String BFSE_SELECTED_CONTACT_KEY = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
            if (generalFunc.containsKey(Utils.BFSE_SELECTED_CONTACT_KEY) && Utils.checkText(BFSE_SELECTED_CONTACT_KEY)) {
                Gson gson = new Gson();
                String data1 = BFSE_SELECTED_CONTACT_KEY;
                ContactModel contactdetails = null;

                contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                        }.getType()
                );


                if (contactdetails != null && Utils.checkText(contactdetails.name) && !contactdetails.name.equalsIgnoreCase("ME")) {
                    if (Utils.checkText(contactdetails.mobileNumber)) {
                        requestCabData.put("eBookSomeElseNumber", contactdetails.mobileNumber);
                        requestCabData.put("eBookForSomeOneElse", "Yes");
                    }
                    if (Utils.checkText(contactdetails.name)) {
                        requestCabData.put("eBookSomeElseName", contactdetails.name);
                    }
                }
            }
        }


        requestCabData.put("iUserProfileId", iUserProfileId);
        requestCabData.put("iOrganizationId", iOrganizationId);
        requestCabData.put("vProfileEmail", vProfileEmail);
        requestCabData.put("ePaymentBy", ePaymentBy);


        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                requestCabData.put("vDistance", cabSelectionFrag.distance);
                requestCabData.put("vDuration", cabSelectionFrag.time);
            }

            if (cabSelectionFrag.isPoolCabTypeIdSelected) {
                requestCabData.put("ePoolRequest", "Yes");
                requestCabData.put("iPersonSize", cabSelectionFrag.poolSeatsList.get(cabSelectionFrag.seatsSelectpos));
            }
        }


        String tollskiptxt = "";

        if (istollIgnore) {
            tollamount = 0;
            tollskiptxt = "Yes";

        } else {
            tollskiptxt = "No";
        }
        requestCabData.put("valorcorrida", String.valueOf(MainActivity.valorcorrida) );
        requestCabData.put("fTollPrice", tollamount + "");
        requestCabData.put("eTollSkipped", tollskiptxt);

        requestCabData.put("eType", getCurrentCabGeneralType());

        if ((app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX))) {
            Log.d(TAG, "setCurrentType: 21");
            if (isUfx) {
                requestCabData.put("eType", Utils.CabGeneralType_UberX);
                requestCabData.put("driverIds", generalFunc.retrieveValue(Utils.SELECTEDRIVERID));

            } else {
                requestCabData.put("driverIds", driverIds);
            }

        }
        if ((app_type.equalsIgnoreCase("UberX") || isUfx)) {
            Log.d(TAG, "setCurrentType: 22");
            requestCabData.put("driverIds", generalFunc.retrieveValue(Utils.SELECTEDRIVERID));
        } else {

            requestCabData.put("driverIds", driverIds);

        }
        requestCabData.put("SelectedCarTypeID", "" + selectedCabTypeId);

        if (!isMultiDelivery()) {
            requestCabData.put("DestLatitude", getDestLocLatitude());
            requestCabData.put("DestLongitude", getDestLocLongitude());
            requestCabData.put("DestAddress", getDestAddress());
            requestCabData.put("iToStationId", iToStationId);

        }

        if (isUfx) {
            requestCabData.put("PickUpLatitude", getIntent().getStringExtra("latitude"));
            requestCabData.put("PickUpLongitude", getIntent().getStringExtra("longitude"));
        } else {
            requestCabData.put("PickUpLatitude", "" + getPickUpLocation().getLatitude());
            requestCabData.put("PickUpLongitude", "" + getPickUpLocation().getLongitude());
        }


        if (deliveryData != null) {
            if (isMultiDelivery()) {
                String data = generalFunc.retrieveValue(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY);

                if (deliveryData.hasExtra("isMulti")) {
                    requestCabData.put("CashPayment", "" + deliveryData.getStringExtra("isCashPayment"));
                    requestCabData.put("PromoCode", deliveryData.getStringExtra("promocode"));
                    requestCabData.put("ePaymentBy", deliveryData.getStringExtra("paymentMethod"));

                    if (deliveryData.getStringExtra("isCashPayment").equalsIgnoreCase("false") && !generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
                        requestCabData.put("eWalletDebitAllow", "Yes");
                        requestCabData.put("ePayWallet", "Yes");
                    }
                    requestCabData.put("eType", Utils.eType_Multi_Delivery);
                }

                if (deliveryData.hasExtra("total_del_dist")) {
                    requestCabData.put("total_del_dist", "" + deliveryData.getStringExtra("total_del_dist"));
                }

                if (deliveryData.hasExtra("total_del_time")) {
                    requestCabData.put("total_del_time", "" + deliveryData.getStringExtra("total_del_time"));
                }

                JSONArray array = generalFunc.getJsonArray(data);
                requestCabData.put("delivery_arr", array);
            } else {
                requestCabData.put("iPackageTypeId", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.PACKAGE_TYPE_ID_KEY));
                requestCabData.put("vReceiverName", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.RECEIVER_NAME_KEY));
                requestCabData.put("vReceiverMobile", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.RECEIVER_MOBILE_KEY));
                requestCabData.put("tPickUpIns", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.PICKUP_INS_KEY));
                requestCabData.put("tDeliveryIns", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.DELIVERY_INS_KEY));
                requestCabData.put("tPackageDetails", deliveryData.getStringExtra(EnterDeliveryDetailsActivity.PACKAGE_DETAILS_KEY));
            }
        }

        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && stopOverPointsList.size() > 2) {
            JSONArray jaStore = new JSONArray();

            for (int j = 0; j < stopOverPointsList.size(); j++) {
                Stop_Over_Points_Data data = stopOverPointsList.get(j);

                if (data.isDestination()) {
                    try {
                        JSONObject stopOverPointsObj = new JSONObject();
                        stopOverPointsObj.put("tDAddress", "" + data.getDestAddress());
                        stopOverPointsObj.put("tDestLatitude", "" + data.getDestLat());
                        stopOverPointsObj.put("tDestLongitude", "" + data.getDestLong());
                        jaStore.put(stopOverPointsObj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
            requestCabData.put("stopoverpoint_arr", jaStore);

        }

        if ((app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX))) {
            Log.d(TAG, "setCurrentType: 23");
            if (isUfx) {
                requestCabData.put("Quantity", getIntent().getStringExtra("Quantity"));
            }
        }

        if (!isMultiDelivery()) {
            if (app_type.equalsIgnoreCase("UberX") || isUfx) {
                Log.d(TAG, "setCurrentType: 24");
                requestCabData.put("PromoCode", appliedPromoCode);
                requestCabData.put("iUserAddressId", getIntent().getStringExtra("iUserAddressId"));
                requestCabData.put("tUserComment", userComment);

                if (getIntent().getStringExtra("Quantity").equals("0")) {
                    requestCabData.put("Quantity", "1");
                } else {
                    requestCabData.put("Quantity", getIntent().getStringExtra("Quantity"));
                }
            }

            if (cabSelectionFrag != null) {
                requestCabData.put("PromoCode", cabSelectionFrag.getAppliedPromoCode());

                if (!cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
                    requestCabData.put("iRentalPackageId", cabSelectionFrag.iRentalPackageId);
                }
            }
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), requestCabData, true);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken", generalFunc);
        exeWebServer.setCancelAble(false);

        exeWebServer.setDataResponseListener(responseString -> {

            if (cabSelectionFrag != null) {
                cabSelectionFrag.isclickableridebtn = false;
            }

            if (responseString != null && !responseString.equals("")) {

                generalFunc.sendHeartBeat();

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (!isDataAvail) {
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + generalFunc.getJsonValue(Utils.message_str, responseString));

                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);

                    if (message.equals("SESSION_OUT")) {
                        closeRequestDialog(false);
                        MyApp.getInstance().notifySessionTimeOut();
                        Utils.runGC();
                        return;
                    }
                    if (message.equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);


                        String walletMsg = "";

                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }


                        boolean isCancelShow = false;
                        if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") && generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-2")) {
                            isCancelShow = true;
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_OK") :
                                generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), isCancelShow ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : "", button_Id -> {
                            if (button_Id == 1) {
                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                                    eWalletIgnore = "Yes";
                                    requestPickUp();
                                }
                            }
                        });


//                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
//                        generateAlert.setCancelable(false);
//                        generateAlert.setBtnClickList(btn_id -> {
//
//                            if (btn_id == 1) {
//                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
//                            } else {
//                                eWalletIgnore = "Yes";
//                                requestPickUp();
//                            }
//                        });
//
//                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Low Wallet Balance. Please Pay Remaining Amount In Cash.", "LBL_WALLET_LOW_AMOUNT_MSG_TXT"));
//
//                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"));
//                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_OK"));
//                        generateAlert.showAlertBox();

                        return;

                    }

                    if (message.equals("NO_CARS") && !DRIVER_REQUEST_METHOD.equalsIgnoreCase("ALL") && sendNotificationToDriverByDist != null) {
                        sendNotificationToDriverByDist.incTask();
                        return;

                    }
                    if (message.equals("NO_CARS") || message.equals("LBL_PICK_DROP_LOCATION_NOT_ALLOW")
                            || message.equals("LBL_DROP_LOCATION_NOT_ALLOW") || message.equals("LBL_PICKUP_LOCATION_NOT_ALLOW")) {
                        closeRequestDialog(false);
                        String messageLabel = "";

                        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                            messageLabel = "LBL_NO_CAR_AVAIL_TXT";

                        } else if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                            messageLabel = "LBL_NO_PROVIDERS_AVAIL_TXT";
                        } else {
                            messageLabel = "LBL_NO_CARRIERS_AVAIL_TXT";
                        }
                        buildMessage(generalFunc.retrieveLangLBl("", message.equals("NO_CARS") ? messageLabel : message),
                                generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), false);
                        if (loadAvailCabs != null) {
                            isufxbackview = false;
                            loadAvailCabs.onResumeCalled();
                        }

                    } else if (message.equals(Utils.GCM_FAILED_KEY) || message.equals(Utils.APNS_FAILED_KEY)) {
                        releaseScheduleNotificationTask();
                        generalFunc.restartApp();
                    } else if (generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_PHONE_VERIFY") ||
                            generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_PHONE_VERIFY") ||
                            generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_VERIFY")) {
                        closeRequestDialog(true);
                        isFixFare = false;
                        isTollCostdilaogshow = false;
                        accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);

                        if (loadAvailCabs != null) {
                            isufxbackview = false;
                            loadAvailCabs.onResumeCalled();
                        }

                    } else if (!generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("")) {
                        closeRequestDialog(false);

                        if (generalFunc.getJsonValue("isShowContactUs", responseString) != null && generalFunc.getJsonValue("isShowContactUs", responseString).equalsIgnoreCase("Yes")) {
                            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                            generateAlert.setCancelable(false);
                            generateAlert.setBtnClickList(btn_id -> {
                                if (btn_id == 0) {

                                } else if (btn_id == 1) {
                                    Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                            generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                            generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));

                            generateAlert.showAlertBox();
                        } else {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), null);
                        }
                    } else {
                        closeRequestDialog(false);
                        buildMessage(generalFunc.retrieveLangLBl("", "LBL_REQUEST_FAILED_PROCESS"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), true);
                    }

                }
            } else {
                if (reqSentErrorDialog != null) {
                    reqSentErrorDialog.closeAlertBox();
                    reqSentErrorDialog = null;
                }

                InternetConnection intConnection = new InternetConnection(getActContext());

                reqSentErrorDialog = generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", intConnection.isNetworkConnected() ? "LBL_TRY_AGAIN_TXT" : "LBL_NO_INTERNET_TXT"), generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"), buttonId -> {
                    if (buttonId == 1) {
                        sendRequestToDrivers(driverIds, cabRequestedJson);
                    } else {
                        closeRequestDialog(true);
                        MyApp.getInstance().restartWithGetDataApp();
                    }
                });
            }
        });
        exeWebServer.execute();

        generalFunc.sendHeartBeat();
    }

    public void accountVerificationAlert(String message, final Bundle bn) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            if (btn_id == 1) {
                generateAlert.closeAlertBox();
//                bn.putString("msg", "DO_PHONE_VERIFY");
                (new StartActProcess(getActContext())).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_INFO_REQ_CODE);
            } else if (btn_id == 0) {
                generateAlert.closeAlertBox();
            }
        });
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_CANCEL_TRIP_TXT"));
        generateAlert.showAlertBox();

    }

    public void closeRequestDialog(boolean isSetDefault) {
        if (requestNearestCab != null) {
            requestNearestCab.dismissDialog();
        }

        if (loadAvailCabs != null) {
            loadAvailCabs.selectProviderId = "";

        }

        if (!isDriverAssigned) {
            setLoadAvailCabTaskValue(false);
        }

        releaseScheduleNotificationTask();

        if (isSetDefault) {
            setDefaultView();
        }

    }

    public void releaseScheduleNotificationTask() {
        if (allCabRequestTask != null) {
            allCabRequestTask.stopRepeatingTask();
            allCabRequestTask = null;
        }
        if (allNonFavCabRequestTask != null) {
            allNonFavCabRequestTask.stopRepeatingTask();
            allNonFavCabRequestTask = null;
        }

        if (sendNotificationToDriverByDist != null) {
            sendNotificationToDriverByDist.stopRepeatingTask();
            sendNotificationToDriverByDist = null;
        }
    }

    public DriverDetailFragment getDriverDetailFragment() {
        return driverDetailFrag;
    }

    public void buildMessage(String message, String positiveBtn, final boolean isRestart) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            generateAlert.closeAlertBox();
            if (isRestart) {
                generalFunc.restartApp();
            } else if (!TextUtils.isEmpty(tripId) && eTripType.equals(Utils.eType_Multi_Delivery)) {
                MyApp.getInstance().restartWithGetDataApp(tripId);
            }
        });
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(positiveBtn);
        generateAlert.showAlertBox();
    }


    public void onGcmMessageArrived(String message) {

        String driverMsg = generalFunc.getJsonValue("Message", message);
        String eType = generalFunc.getJsonValue("eType", message);

        if (!eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
            if (!assignedTripId.equals("") && !generalFunc.getJsonValue("iTripId", message).equalsIgnoreCase("") && !generalFunc.getJsonValue("iTripId", message).equalsIgnoreCase(assignedTripId)) {
                return;
            }
        }
        currentTripId = generalFunc.getJsonValue("iTripId", message);

        if (driverMsg.equals("CabRequestAccepted")) {
            if (isDriverAssigned) {
                return;
            }

            if (generalFunc.getJsonValue("eSystem", message) != null && generalFunc.getJsonValue("eSystem", message).equalsIgnoreCase("DeliverAll")) {
                generalFunc.showGeneralMessage("", generalFunc.getJsonValue("vTitle", message));
                return;
            }

            isDriverAssigned = true;
            addDrawer.setIsDriverAssigned(isDriverAssigned);
            userLocBtnImgView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
            assignedDriverId = generalFunc.getJsonValue("iDriverId", message);
            assignedTripId = generalFunc.getJsonValue("iTripId", message);

            generalFunc.removeValue(Utils.DELIVERY_DETAILS_KEY);

            reSetFields();


            if (generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                pinImgView.setVisibility(View.GONE);
                setDestinationPoint("", "", "", false);
                closeRequestDialog(true);
                showBookingAlert(generalFunc.retrieveLangLBl("", "LBL_ONGOING_TRIP_TXT"), true);
                return;
            } else if (app_type != null && app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                Log.d(TAG, "setCurrentType: 25");
                if (!generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

                    closeRequestDialog(false);

                    MyApp.getInstance().restartWithGetDataApp();
                    return;
                }
            }

            if (generalFunc.isJSONkeyAvail("iCabBookingId", message) && !generalFunc.getJsonValue("iCabBookingId", message).trim().equals("")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else {
                if (generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.CabGeneralType_UberX) || generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                    isDriverAssigned = false;
                    pinImgView.setVisibility(View.GONE);
                    setDestinationPoint("", "", "", false);
                    closeRequestDialog(true);
                    showBookingAlert(generalFunc.retrieveLangLBl("", "LBL_ONGOING_TRIP_TXT"), true);
                    return;
                } else {
                    MyApp.getInstance().restartWithGetDataApp();
                    //    configureAssignedDriver(false);
                    pinImgView.setVisibility(View.GONE);
                    closeRequestDialog(false);

                    if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
                        Log.d(TAG, "setCurrentType: 25");
                        rduTollbar.setVisibility(View.GONE);
                    }
                    configureDeliveryView(true);
                }
            }


            tripStatus = "Assigned";

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (userLocBtnImgView.getVisibility() == View.VISIBLE) {
                    userLocBtnImgView.performClick();
                }
            }, 1500);


        } else if (driverMsg.equals("TripEnd")) {
            if (!isDriverAssigned) {
                return;
            }

            if (isTripEnded && !isDriverAssigned) {
                generalFunc.restartApp();
                return;
            }

            if (isTripEnded) {
                return;
            }

            tripStatus = "TripEnd";
            if (driverAssignedHeaderFrag != null) {

                if ((!TextUtils.isEmpty(tripId) && (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Deliver)))) {
                    isTripEnded = true;
                } else {
                    isTripEnded = true;
                }

                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.setTaskKilledValue(true);
                }
            }

        } else if (driverMsg.equals("TripStarted")) {
            try {
                if (!isDriverAssigned) {
                    return;
                }

                if (!isDriverAssigned && isTripStarted) {
                    generalFunc.restartApp();
                    return;
                }

                if (isTripStarted) {
                    return;
                }


                // Change Status as per trip
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);

                if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message)) && eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                    return;
                }

                tripStatus = "TripStarted";


                isTripStarted = true;
                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.setTripStartValue(true);
                }
                if (driverAssignedHeaderFrag.sourceMarker != null) {
                    driverAssignedHeaderFrag.sourceMarker.remove();
                }

                if (driverDetailFrag != null) {
                    driverDetailFrag.configTripStartView(generalFunc.getJsonValue("VerificationCode", message));
                }
                userLocBtnImgView.performClick();
            } catch (Exception e) {

            }


        } else if (driverMsg.equals("DestinationAdded")) {
            if (isDriverAssigned == false) {
                return;
            }

            // Change Status as per trip
            JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);

            if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message))) {
                return;
            }

            LocalNotification.dispatchLocalNotification(getActContext(), generalFunc.retrieveLangLBl("Destination is added by driver.", "LBL_DEST_ADD_BY_DRIVER"), true);

            buildMessage(generalFunc.retrieveLangLBl("Destination is added by driver.", "LBL_DEST_ADD_BY_DRIVER"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), false);

            String destLatitude = generalFunc.getJsonValue("DLatitude", message);
            String destLongitude = generalFunc.getJsonValue("DLongitude", message);
            String destAddress = generalFunc.getJsonValue("DAddress", message);
            String eFlatTrip = generalFunc.getJsonValue("eFlatTrip", message);

            setDestinationPoint(destLatitude, destLongitude, destAddress, true);
            if (driverAssignedHeaderFrag != null) {
                driverAssignedHeaderFrag.setDestinationAddress(eFlatTrip);
                driverAssignedHeaderFrag.configDestinationView();
            }
        } else if (driverMsg.equals("TripCancelledByDriver") || driverMsg.equals("TripCancelled")) {

            if (!generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                if (isDriverAssigned == false) {
                    generalFunc.restartApp();
                    return;
                }
            }

            if (tripStatus.equals("TripCanelled")) {
                return;
            }

            tripStatus = "TripCanelled";
            if (driverAssignedHeaderFrag != null) {
                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.setTaskKilledValue(true);
                }
            }
        }
    }

    public DriverAssignedHeaderFragment getDriverAssignedHeaderFrag() {
        return driverAssignedHeaderFrag;
    }

    public void unSubscribeCurrentDriverChannels() {
        if (currentLoadedDriverList != null) {
            ConfigPubNub.getInstance().unSubscribeToChannels(getDriverLocationChannelList());
        }
    }

    public boolean isDeliver(String selctedType) {
        return (selctedType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || selctedType.equalsIgnoreCase("Deliver"));
    }

    public boolean isMultiDelivery() {
        if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
            Log.d(TAG, "setCurrentType: 26");
            return (getIntent().hasExtra("fromMulti") && generalFunc.isMultiDelivery() && !isDeliver(app_type) && isDeliver(getCurrentCabGeneralType()));
        } else if (isDeliver(app_type) && getIntent().hasExtra("fromMulti")) {
            Log.d(TAG, "setCurrentType: 27");
            return generalFunc.isMultiDelivery();
        } else {
            return generalFunc.isMultiDelivery() && !isDeliver(app_type) && isDeliver(getCurrentCabGeneralType()) && getIntent().hasExtra("fromMulti");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (loadAvailCabs != null) {
            loadAvailCabs.onPauseCalled();
        }

        if (driverAssignedHeaderFrag != null) {
            driverAssignedHeaderFrag.onPauseCalled();
        }

        unSubscribeCurrentDriverChannels();
    }


    public void setWalletInfo() {

        String text1 = generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson));
        String text2 = "\n" + generalFunc.retrieveLangLBl("wallet Balance", "LBL_WALLET_BALANCE");
        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(18, getActContext())), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(12, getActContext())), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), 0, text2.length(), 0);
        CharSequence finalText = TextUtils.concat(span1, "", span2);

        addDrawer.walletbalncetxt.setText(finalText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (generalFunc.retrieveValue(Utils.ISWALLETBALNCECHANGE).equalsIgnoreCase("Yes")) {
            getWalletBalDetails();
        }

        getUserProfileJson();

        setUserInfo();


        if (addDrawer != null) {
            addDrawer.userProfileJson = userProfileJson;
        }


        if (iswallet) {
            obj_userProfile = generalFunc.getJsonObject(userProfileJson);
            if (addDrawer != null) {
                addDrawer.changeUserProfileJson(userProfileJson);
            }
            iswallet = false;
        }

        if (addDrawer != null) {
            //  addDrawer.walletbalncetxt.setText(generalFunc.retrieveLangLBl("wallet Balance", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson)));

            setWalletInfo();

//            String text1 = generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson));
//            String text2 = "/n" +generalFunc.retrieveLangLBl("wallet Balance", "LBL_WALLET_BALANCE");
//            SpannableString span1 = new SpannableString(text1);
//            span1.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(18, getActContext())), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);
//
//            SpannableString span2 = new SpannableString(text2);
//            span2.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(12, getActContext())), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
//            span2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray)), 0, text2.length(), 0);
//            CharSequence finalText = TextUtils.concat(span1, "", span2);
//
//            addDrawer.walletbalncetxt.setText(finalText);

        }

        if (!schedulrefresh) {
            if (loadAvailCabs != null) {
                loadAvailCabs.onResumeCalled();
            }
        }

        app_type = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);


        if (driverAssignedHeaderFrag != null) {
            driverAssignedHeaderFrag.onResumeCalled();
            pinImgView.setVisibility(View.GONE);
            if (!driverAssignedHeaderFrag.isMultiDelivery()) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
                //params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
                params.bottomMargin = (int) getResources().getDimension(R.dimen._150sdp);
            }
        }

        if (!isufxbackview) {

            if (currentLoadedDriverList != null) {
                ConfigPubNub.getInstance().subscribeToChannels(getDriverLocationChannelList());
            }
        }
    }

    private void getUserProfileJson() {
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
    }

    public void setUserInfo() {
        View view = ((Activity) getActContext()).findViewById(android.R.id.content);
        ((MTextView) view.findViewById(R.id.userNameTxt)).setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));

        ((MTextView) view.findViewById(R.id.userNameTxt)).setSelected(true);
        //  ((MTextView) view.findViewById(R.id.walletbalncetxt)).setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));
        setWalletInfo();

        (new AppFunctions(getActContext())).checkProfileImage((SelectableRoundedImageView) view.findViewById(R.id.userImgView), userProfileJson, "vImgName");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            releaseScheduleNotificationTask();
            if (getLastLocation != null) {
                getLastLocation.stopLocationUpdates();
                getLastLocation = null;
            }

            if (gMap != null) {
                gMap.clear();
                gMap = null;
            }

            Utils.runGC();

        } catch (Exception e) {

        }

    }

    public void setDriverImgView(SelectableRoundedImageView driverImgView) {
        this.driverImgView = driverImgView;
    }

    public void pubNubStatus(PNStatusCategory status) {

    }

    public void pubNubMsgArrived(final String message) {

        currentTripId = generalFunc.getJsonValue("iTripId", message);
        runOnUiThread(() -> {

            String msgType = generalFunc.getJsonValue("MsgType", message);

            if (msgType.equals("TripEnd")) {

                if (!isDriverAssigned) {
                    generalFunc.restartApp();
                    return;
                }
            }
            if (msgType.equals("LocationUpdate")) {
                if (loadAvailCabs == null) {
                    return;
                }

                String iDriverId = generalFunc.getJsonValue("iDriverId", message);
                String vLatitude = generalFunc.getJsonValue("vLatitude", message);
                String vLongitude = generalFunc.getJsonValue("vLongitude", message);

                Marker driverMarker = getDriverMarkerOnPubNubMsg(iDriverId, false);

                LatLng driverLocation_update = new LatLng(GeneralFunctions.parseDoubleValue(0.0, vLatitude),
                        GeneralFunctions.parseDoubleValue(0.0, vLongitude));
                Location driver_loc = new Location("gps");
                driver_loc.setLatitude(driverLocation_update.latitude);
                driver_loc.setLongitude(driverLocation_update.longitude);

                if (driverMarker != null) {
                    float rotation = (float) SphericalUtil.computeHeading(driverMarker.getPosition(), driverLocation_update);

                    if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("UberX") || isUfx) {
                        rotation = 0;
                    }

                    AnimateMarker.animateMarker(driverMarker, gMap, driver_loc, rotation, 1200);
                }

            } else if (msgType.equals("TripRequestCancel")) {

                tripStatus = "TripCanelled";
                if (TextUtils.isEmpty(tripId) && eTripType.equals(Utils.CabGeneralType_Deliver) && getCurrentCabGeneralType().equals(Utils.CabGeneralType_Deliver)) {
                    if (tripId.equalsIgnoreCase(currentTripId)) {
                        if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
                            if (sendNotificationToDriverByDist != null) {
                                sendNotificationToDriverByDist.incTask();
                            }
                        }
                    }
                } else {
                    if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
                        if (sendNotificationToDriverByDist != null) {
                            sendNotificationToDriverByDist.incTask();
                        }
                    }
                }
            } else if (msgType.equals("LocationUpdateOnTrip")) {

                if (!isDriverAssigned) {
                    return;
                }

                if (generalFunc.checkLocationPermission(true)) {
                    getMap().setMyLocationEnabled(false);
                }
                // Change Status as per trip
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);

                if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message))) {
                    return;
                }
                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.updateDriverLocation(message);
                }

            } else if (msgType.equals("DriverArrived")) {

                if (isDriverAssigned == false) {
                    generalFunc.restartApp();
                    return;
                }

                // Change Status as per trip
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);

                if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message))) {
                    return;
                }

                tripStatus = "DriverArrived";
                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.isDriverArrived = true;
                    if (generalFunc.getJsonValue("eFly", message).equalsIgnoreCase("Yes")) {
                        driverAssignedHeaderFrag.setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_FLY_ARRIVED"));
                    } else if (generalFunc.getJsonValue("eType", message).equalsIgnoreCase("Deliver") || generalFunc.getJsonValue("eType", message).equals(Utils.eType_Multi_Delivery)) {
                        driverAssignedHeaderFrag.setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_CARRIER_ARRIVED_TXT"));
                    } else {
                        driverAssignedHeaderFrag.setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVED_TXT"));
                    }
                    gMap.clear();


                    if (driverAssignedHeaderFrag.updateDestMarkerTask != null) {
                        driverAssignedHeaderFrag.updateDestMarkerTask.stopRepeatingTask();
                        driverAssignedHeaderFrag.updateDestMarkerTask = null;
                        if (driverAssignedHeaderFrag.time_marker != null) {
                            driverAssignedHeaderFrag.time_marker.remove();
                            driverAssignedHeaderFrag.time_marker = null;
                        }
                        if (driverAssignedHeaderFrag.route_polyLine != null) {
                            driverAssignedHeaderFrag.route_polyLine.remove();
                        }
                    }
                    if (driverAssignedHeaderFrag.driverMarker != null) {
                        driverAssignedHeaderFrag.driverMarker.remove();
                        driverAssignedHeaderFrag.driverMarker = null;
                    }
                    if (driverAssignedHeaderFrag.driverData != null) {
                        driverAssignedHeaderFrag.driverData.get("DriverTripStatus");
                        driverAssignedHeaderFrag.driverData.put("DriverTripStatus", "Arrived");
                    }


                    driverAssignedHeaderFrag.configDriverLoc();
                    driverAssignedHeaderFrag.addPickupMarker();

                }

                userLocBtnImgView.performClick();

                if (driverAssignedHeaderFrag != null) {
                    if (driverAssignedHeaderFrag.isDriverArrived || driverAssignedHeaderFrag.isDriverArrivedNotGenerated) {
                        return;
                    }
                }

            } else {

                onGcmMessageArrived(message);

            }

        });

    }

    public Marker getDriverMarkerOnPubNubMsg(String iDriverId, boolean isRemoveFromList) {

        if (loadAvailCabs != null) {
            ArrayList<Marker> currentDriverMarkerList = loadAvailCabs.getDriverMarkerList();

            if (currentDriverMarkerList != null) {
                for (int i = 0; i < currentDriverMarkerList.size(); i++) {
                    Marker marker = currentDriverMarkerList.get(i);

                    String driver_id = marker.getTitle().replace("DriverId", "");

                    if (driver_id.equals(iDriverId)) {

                        if (isRemoveFromList) {
                            loadAvailCabs.getDriverMarkerList().remove(i);
                        }

                        return marker;
                    }

                }
            }
        }


        return null;
    }

    @Override
    public void onBackPressed() {
        callBackEvent(false);
    }

    public void callBackEvent(boolean status) {
        try {

            if (pickUpLocSelectedFrag != null) {
                pickUpLocSelectedFrag = null;

                if (loadAvailCabs != null) {
                    loadAvailCabs.selectProviderId = "";
                    loadAvailCabs.changeCabs();
                }


                if (mainHeaderFrag != null) {
                    mainHeaderFrag = null;
                }

                setMainHeaderView(false);
                // setDefaultView();
                return;

            }
            if (status) {
                if (requestNearestCab != null) {
                    requestNearestCab.dismissDialog();
                }

                releaseScheduleNotificationTask();
            }

            if (addDrawer.checkDrawerState(false) && !isMultiDelivery()) {
                return;
            }

            // Logger.d("SET_PICKUP", "no selected -> " + (flyStationSelectionFragment != null && flyStationSelectionFragment.isPickup));

          /*  if (eFly && flyStationSelectionFragment != null && flyStationSelectionFragment.isPickup) {
                releaseCabSelectionInstances();
                return;
            }

            if (eFly && flyStationSelectionFragment != null) {
                releaseInstances(cabSelectionFrag != null ? false : true);
                return;
            }*/

            if (eFly && findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE) {
                releaseCabSelectionInstances();
                return;
            } else if (eFly && !isPickup && cabSelectionFrag != null) {
                releaseCabSelectionInstances();
                return;
            }

           /* if (eFly && findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE) {
                releaseInstances(cabSelectionFrag != null ? false : true);
                return;
            }*/


            if (isMultiDelivery() && !app_type.equalsIgnoreCase("Ride-Delivery")) {
                Log.d(TAG, "setCurrentType: 28");
                releaseCabSelectionInstances();
                return;
            }
            if (cabSelectionFrag == null) {


            } else if (isDeliver(app_type) || (!app_type.equalsIgnoreCase("Ride-Delivery") && generalFunc.isMultiDelivery() && getIntent().hasExtra("fromMulti")) ||
                    (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery) && getIntent().hasExtra("fromMulti"))) {
                releaseCabSelectionInstances();
                Log.d(TAG, "setCurrentType: 29");
            } else {


                //  MapAnimator.getInstance().stopRouteAnim();
//                if (cabSelectionFrag != null) {
//                    cabSelectionFrag.manageisRentalValue();
//                }
//                getSupportFragmentManager().beginTransaction().remove(cabSelectionFrag).commit();

                if (MapAnimator.getInstance() != null) {
                    MapAnimator.getInstance().stopRouteAnim();
                }

                if (cabSelectionFrag != null) {
                    cabSelectionFrag.manageisRentalValue();
                    cabSelectionFrag.releaseResources();
                    getSupportFragmentManager().beginTransaction().remove(cabSelectionFrag).commit();
                    cabSelectionFrag = null;
                    if (loadAvailCabs != null) {
                        loadAvailCabs.checkAvailableCabs();
                    }

                }

                if (stopOverPointsList.size() > 1) {
                    ArrayList<Stop_Over_Points_Data> tempStopOverPointsList = new ArrayList<>();
                    tempStopOverPointsList.add(stopOverPointsList.get(0));
                    stopOverPointsList.clear();
                    stopOverPointsList.addAll(tempStopOverPointsList);
                }


                eWalletDebitAllow = "";
                selectReasonId = "";
                vReasonTitle = "";
                iUserProfileId = "";
                iOrganizationId = "";
                vProfileEmail = "";
                vProfileName = "";
                ePaymentBy = "";
                vReasonName = "";
                selectPos = 0;
                vImage = "";
                gMap.clear();


                configDestinationMode(false);

                isRental = false;
                if (loadAvailCabs != null) {
                    loadAvailCabs.changeCabs();
                }

                if (isMenuImageShow) {
                    mainHeaderFrag.menuBtn.setVisibility(View.VISIBLE);
                    mainHeaderFrag.backBtn.setVisibility(View.GONE);
                }

                mainHeaderFrag.handleDestAddIcon();
                cabTypesArrList.clear();

                if (generalFunc.isMultiDelivery() && getIntent().hasExtra("fromMulti")) {
                    rideArea.performClick();
                }

                //  mainHeaderFrag.setDestinationAddress(generalFunc.retrieveLangLBl("", "LBL_ADD_DESTINATION_BTN_TXT"));
                mainHeaderFrag.setDefaultView();

                pinImgView.setVisibility(View.GONE);
                if (loadAvailCabs != null) {
                    selectedCabTypeId = loadAvailCabs.getFirstCarTypeID();
                }


                reSetButton(false);
                /*
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
                params.bottomMargin = Utils.dipToPixels(getActContext(), 22);
                if (prefBtnImageView.getVisibility() == View.VISIBLE) {
                    if (generalFunc.isRTLmode()) {
                        params.leftMargin = Utils.dipToPixels(getActContext(), 70);
                    } else {
                        params.rightMargin = Utils.dipToPixels(getActContext(), 70);
                    }
                }
                userLocBtnImgView.requestLayout();*/

                if (mainHeaderFrag != null) {
                    mainHeaderFrag.releaseAddressFinder();
                }


                resetMapView();

                if (pickUpLocation != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(this.pickUpLocation.getLatitude(), this.pickUpLocation.getLongitude()))
                            .zoom(Utils.defaultZomLevel).build();
                    getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else if (userLocation != null) {
                    getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraForUserPosition()));
                }
//                userLocBtnImgView.performClick();

                if (workArea.getVisibility() == View.VISIBLE) {
                    if (gMap != null) {
                        gMap.setPadding(0, 0, 0, Utils.dipToPixels(getActContext(), 65));
                    }
                }
                return;
            }


            super.onBackPressed();
        } catch (Exception e) {
            Log.e("Exception", "::" + e.toString());
        }
    }

    private void reSetButton(boolean changeDefaultBottomMargin) {
        resetUserLocBtnView();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) userLocBtnImgView.getLayoutParams();
        int bottomMargin = changeDefaultBottomMargin ? staticFlyPanelHeight - 110 : (int) getResources().getDimension(R.dimen._20sdp);

        if (isPrefrenceEnable) {

            if (isPickup) {
                prefBtnImageView.invalidate();
                prefBtnImageView.requestLayout();
                prefBtnImageView.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) prefBtnImageView.getLayoutParams();
                int bottomMargin1 = changeDefaultBottomMargin ? staticFlyPanelHeight - 110 : (((int) getResources().getDimension(R.dimen._20sdp)));
                params1.setMargins(params1.leftMargin, params1.topMargin, params1.rightMargin, bottomMargin1);
                prefBtnImageView.requestLayout();
            } else if (!isPickup) {
                prefBtnImageView.setVisibility(View.GONE);
            } else {
                prefBtnImageView.setVisibility(View.VISIBLE);
            }

            if (!isPickup) {
                params.bottomMargin = bottomMargin;
                int margin = (int) getResources().getDimension(R.dimen._8sdp);
                params.setMarginStart(margin);
                params.setMarginEnd(margin);
                Logger.d("SET_VIEW", "in main activity");
            } else {
                if (leftMargin == 0 && rightMargin == 0) {
                    leftMargin = params.leftMargin;
                    rightMargin = params.rightMargin;
                }
                params.setMargins(leftMargin, params.topMargin, rightMargin, bottomMargin);
            }

            userLocBtnImgView.requestLayout();
        } else {
            params.bottomMargin = bottomMargin;
            int margin = (int) getResources().getDimension(R.dimen._8sdp);
            params.setMarginStart(margin);
            params.setMarginEnd(margin);
            userLocBtnImgView.requestLayout();
        }
    }

    public void releaseCabSelectionInstances() {
        Logger.d("releaseCabSelectionInstances", "::");

        if (mainHeaderFrag != null) {
            mainHeaderFrag.releaseAddressFinder();
        }

        if (loadAvailCabs != null) {
            loadAvailCabs.setTaskKilledValue(true);
        }

        if (eFly && findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE/*flyStationSelectionFragment != null*/) {
            if (!/*flyStationSelectionFragment.*/isPickup) {
                mainHeaderFrag.handleDestAddIcon();
                mainHeaderFrag.isclickabledest = false;
                mainHeaderFrag.isclickablesource = false;
                configDestinationMode(false);
            }


          /*  getSupportFragmentManager().beginTransaction().remove(flyStationSelectionFragment).commit();
            flyStationSelectionFragment = null;*/
            reSetButton(false);

            findViewById(R.id.DetailsArea).setVisibility(View.GONE);
            findViewById(R.id.dragView).setVisibility(View.VISIBLE);

        }

        boolean isCabSelFragRemoved = false;

        if (cabSelectionFrag != null) {
            cabSelectionFrag.manageisRentalValue();
            cabSelectionFrag.releaseResources();
            getSupportFragmentManager().beginTransaction().remove(cabSelectionFrag).commit();
            cabSelectionFrag = null;
            isCabSelFragRemoved = true;
            Logger.d("isRental", ":: called");
        }

        if (MapAnimator.getInstance() != null) {
            MapAnimator.getInstance().stopRouteAnim();
        }

        if (stopOverPointsList.size() > 1) {
            ArrayList<Stop_Over_Points_Data> tempStopOverPointsList = new ArrayList<>();
            tempStopOverPointsList.add(stopOverPointsList.get(0));
            stopOverPointsList.clear();
            stopOverPointsList.addAll(tempStopOverPointsList);
        }

        eWalletDebitAllow = "";
        selectReasonId = "";
        vReasonTitle = "";
        iUserProfileId = "";
        iOrganizationId = "";
        vProfileEmail = "";
        vProfileName = "";
        vReasonName = "";
        selectPos = 0;


        gMap.clear();

        if (eFly && !isPickup && isCabSelFragRemoved) {
            destAddress = "";
            destLocLatitude = "";
            destLocLongitude = "";
            showSelectionDialog(null, false);
            return;
        }

        super.onBackPressed();
    }

    public Context getActContext() {
        return MainActivity.this;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 1, 0, "" + generalFunc.retrieveLangLBl("", "LBL_CALL_TXT"));
        menu.add(0, 2, 0, "" + generalFunc.retrieveLangLBl("", "LBL_MESSAGE_TXT"));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == 1) {

            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + driverDetailFrag.getDriverPhone()));
                startActivity(callIntent);
            } catch (Exception e) {
                // TODO: handle exception
            }

        } else if (item.getItemId() == 2) {

            try {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "" + driverDetailFrag.getDriverPhone());
                startActivity(smsIntent);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.MY_PROFILE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            getUserProfileJson();
            addDrawer.changeUserProfileJson(this.userProfileJson);
        } else if (requestCode == Utils.VERIFY_INFO_REQ_CODE && resultCode == RESULT_OK && data != null) {

            String msgType = data.getStringExtra("MSG_TYPE");

            if (msgType.equalsIgnoreCase("EDIT_PROFILE")) {
                addDrawer.openMenuProfile();
            }
            getUserProfileJson();
            addDrawer.userProfileJson = this.userProfileJson;
            addDrawer.obj_userProfile = generalFunc.getJsonObject(this.userProfileJson);
            addDrawer.buildDrawer();
        } else if (requestCode == Utils.VERIFY_INFO_REQ_CODE) {

            getUserProfileJson();
            addDrawer.userProfileJson = this.userProfileJson;
            addDrawer.obj_userProfile = generalFunc.getJsonObject(this.userProfileJson);
            addDrawer.buildDrawer();
        } else if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {

            iswallet = true;
            getUserProfileJson();

            if (cabSelectionFrag != null) {
                cabSelectionFrag.isCardValidated = true;
            }

            addDrawer.changeUserProfileJson(this.userProfileJson);
        } else if (requestCode == Utils.DELIVERY_DETAILS_REQ_CODE && resultCode == RESULT_OK && data != null) {
            try {
                if (!getCabReqType().equals(Utils.CabReqType_Later)) {
                    isdelivernow = true;
                } else {
                    isdeliverlater = true;
                }

                deliveryData = data;
                checkSurgePrice("", data);

            } catch (Exception e) {

            }
        } else if (requestCode == Utils.MULTI_DELIVERY_DETAILS_REQ_CODE && resultCode == RESULT_OK && data != null) {
            try {

                isCashSelected = data.getStringExtra("isCashPayment").equalsIgnoreCase("true");
                if (isCashSelected) {
                    if (loadAvailCabs != null) {
                        loadAvailCabs.isMulti = true;
                        loadAvailCabs.filterDrivers(true);
                    }
                }


                if (!getCabReqType().equals(Utils.CabReqType_Later)) {
                    isdelivernow = true;
                } else {
                    isdeliverlater = true;
                }

/*
                if (data.getBooleanExtra("isCash", false)) {
                    isCashSelected = true;
                } else {
                    isCashSelected = false;
                }*/

                if (data.getStringExtra("isWallet").equalsIgnoreCase("true")) {
                    eWalletDebitAllow = "Yes";
                    iswalletShow = false;
                } else {
                    iswalletShow = true;
                    eWalletDebitAllow = "No";
                }

                checkSurgePrice("", data);

            } catch (Exception e) {

            }
        } else if (requestCode == Utils.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                LatLng placeLocation = place.getLatLng();

                setDestinationPoint(placeLocation.latitude + "", placeLocation.longitude + "", place.getAddress().toString(), true);
                mainHeaderFrag.setDestinationAddress(place.getAddress().toString());

                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(placeLocation, Utils.defaultZomLevel);

                if (gMap != null) {
                    gMap.clear();
                    gMap.moveCamera(cu);
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);


                generalFunc.showMessage(generalFunc.getCurrentView(MainActivity.this),
                        status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }


        } else if (requestCode == Utils.ASSIGN_DRIVER_CODE) {

            if (data != null && data.hasExtra("callGetDetail")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else {
                if (app_type.equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                    Log.d(TAG, "setCurrentType: 30");
                    if (!isUfx) {

                        if ((generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("Active") ||
                                generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValue("eType", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {


                        } else {

                            Bundle bn = new Bundle();
                            new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                            finishAffinity();
                        }

                    } else {
                        isUfx = false;
                        Bundle bn = new Bundle();
                        new StartActProcess(getActContext()).startActWithData(MainActivity.class, bn);
                        finishAffinity();
                    }
                } else {

                    if ((generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("Active") ||
                            generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValue("eType", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {


                    } else if ((isDeliver(app_type) || isMultiDeliveryTrip || app_type.equals("Ride-Delivery")) && generalFunc.isMultiDelivery()) {
                        Log.d(TAG, "setCurrentType: 31");
                        if (isMultiDeliveryTrip && Utils.checkText(tripId) && driverAssignedHeaderFrag != null) {
                            MyApp.getInstance().restartWithGetDataApp();
                        }
                    } else {

                        Bundle bn = new Bundle();
                        new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                        finishAffinity();
                    }
                }
            }
        } else if (requestCode == Utils.MULTIDELIVERY_HISTORY_RATE_CODE) {
            MyApp.getInstance().restartWithGetDataApp();
        } else if (requestCode == Utils.REQUEST_CODE_GPS_ON) {

//            gpsEnabled();

        } else if (requestCode == Utils.SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null && gMap != null) {

            if (data.getStringExtra("Address") != null) {
                pickUp_tmpAddress = data.getStringExtra("Address");
                pickUpLocationAddress = pickUp_tmpAddress;
            }


            pickUp_tmpLatitude = GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Latitude"));
            pickUp_tmpLongitude = GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Longitude"));

//            final Location location = new Location("gps");
//            location.setLatitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Latitude")));
//            location.setLongitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Longitude")));
//            onLocationUpdate(location);

            //  mainHeaderFrag.setPickUpAddress(pickUp_tmpAddress);

            Location pickUpLoc = new Location("");
            pickUpLoc.setLatitude(pickUp_tmpLatitude);
            pickUpLoc.setLongitude(pickUp_tmpLongitude);
            this.pickUpLocation = pickUpLoc;

            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(pickUp_tmpLatitude, pickUp_tmpLongitude), Utils.defaultZomLevel);


            if (gMap != null) {
                gMap.moveCamera(cu);
            }

            if (isMultiStopOverEnabled()) {
                mainHeaderFrag.addOrResetStopOverPoints(pickUp_tmpLatitude, pickUp_tmpLongitude, pickUp_tmpAddress, true);

            }
//            getAvailableDriverIds();
//            initializeLoadCab();

//            destSelectArea.performClick();
            Logger.d("obj2","normal");

        } else if (requestCode == Utils.UFX_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                schedulrefresh = true;
                isufxbackview = true;
                ridelaterView.setVisibility(View.GONE);

                if (loadAvailCabs != null) {
                    loadAvailCabs.setTaskKilledValue(true);
                }

                appliedPromoCode = data.getStringExtra("promocode");
                userComment = data.getStringExtra("comment");

                checkSurgePrice("", data);
            } else {
                loadAvailCabs.selectProviderId = "";
            }
        } else if (requestCode == Utils.SCHEDULE_REQUEST_CODE && resultCode == RESULT_OK) {

            SelectDate = data.getStringExtra("SelectDate");
            sdate = data.getStringExtra("Sdate");
            Stime = data.getStringExtra("Stime");
//
            bookingtype = Utils.CabReqType_Later;

            uberXDriverListArea.setVisibility(View.VISIBLE);
            uberXNoDriverTxt.setVisibility(View.GONE);
            ridelaterView.setVisibility(View.GONE);
            (findViewById(R.id.driverListAreaLoader)).setVisibility(View.VISIBLE);
            (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);

            if (loadAvailCabs != null) {
                loadAvailCabs.changeCabs();
            }
            schedulrefresh = false;

        } else if (requestCode == Utils.OTHER_AREA_CLICKED_CODE) {

            rideArea.performClick();
        } else if (requestCode == RENTAL_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && !data.getStringExtra("iRentalPackageId").equalsIgnoreCase("")) {
                    cabSelectionFrag.iRentalPackageId = data.getStringExtra("iRentalPackageId");
                }

                if (cabRquestType.equalsIgnoreCase(Utils.CabReqType_Now)) {
                    continuePickUpProcess();
                } else {
                    checkSurgePrice(selectedTime, deliveryData);

                }
            }
        } else if (requestCode == Utils.SELECT_ORGANIZATION_PAYMENT_CODE) {

            if (resultCode == RESULT_OK) {
                if (data.getSerializableExtra("data").equals("")) {
                    iUserProfileId = "";
                    iOrganizationId = "";
                    vProfileEmail = "";
                    ePaymentBy = "Passenger";


                    if (data.getBooleanExtra("isCash", false)) {
                        isCashSelected = true;
                    } else {
                        isCashSelected = false;
                    }
                    if (data.getBooleanExtra("isWallet", false)) {
                        eWalletDebitAllow = "Yes";
                        iswalletShow = false;
                    } else {
                        iswalletShow = true;
                        eWalletDebitAllow = "No";
                    }

                    if (cabSelectionFrag != null) {
                        cabSelectionFrag.setOrganizationName(generalFunc.retrieveLangLBl("", "LBL_PERSONAL"), false);
                        cabSelectionFrag.setPaymentType(isCashSelected ? "Cash" : "Card");
                        vProfileName = generalFunc.retrieveLangLBl("", "LBL_PERSONAL");


                    }
                    selectPos = data.getIntExtra("selectPos", 0);
                    vReasonName = data.getStringExtra("vReasonName");
                    selectReasonId = data.getStringExtra("iTripReasonId");
                    vReasonTitle = data.getStringExtra("vReasonTitle");
                    vProfileName = data.getStringExtra("vProfileName");
                    vImage = "Personal";

                } else {
                    HashMap<String, String> map = (HashMap<String, String>) data.getSerializableExtra("data");
                    iUserProfileId = map.get("iUserProfileId");
                    iOrganizationId = map.get("iOrganizationId");
                    vProfileEmail = map.get("vProfileEmail");
                    ePaymentBy = map.get("ePaymentBy");
                    vImage = map.get("vImage");
                    iswalletShow = false;


                    selectReasonId = data.getStringExtra("iTripReasonId");
                    vReasonTitle = data.getStringExtra("vReasonTitle");
                    selectPos = data.getIntExtra("selectPos", 0);

                    if (!ePaymentBy.equalsIgnoreCase("Organization")) {
                        if (data.getBooleanExtra("isCash", false)) {
                            isCashSelected = true;
                        } else {
                            isCashSelected = false;
                        }
                        if (data.getBooleanExtra("isWallet", false)) {
                            eWalletDebitAllow = "Yes";
                            iswalletShow = false;
                        } else {
                            iswalletShow = true;
                            eWalletDebitAllow = "No";
                        }

                        cabSelectionFrag.setPaymentType(isCashSelected ? "Cash" : "Card");
                        if (cabSelectionFrag != null) {
                            cabSelectionFrag.setOrganizationName(map.get("vShortProfileName"), false);
                        }

                        vProfileName = map.get("vProfileName");
                        vReasonName = data.getStringExtra("vReasonName");

                    } else {
                        if (cabSelectionFrag != null) {
                            cabSelectionFrag.setOrganizationName(map.get("vShortProfileName"), true);
                        }
                        vProfileName = map.get("vProfileName");
                        vReasonName = data.getStringExtra("vReasonName");
                        eWalletDebitAllow = "No";
                        iswalletShow = false;
                    }


                }

            }
        } else if (requestCode == Utils.FILTER_REQ_CODE && resultCode == RESULT_OK) {
            selectedCabTypeId = data.getStringExtra("SelectedVehicleTypeId");
            loadAvailCabs.changeCabs();

        } else if (requestCode == Utils.ADD_HOME_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {

            String Latitude = data.getStringExtra("Latitude");
            String Longitude = data.getStringExtra("Longitude");
            String Address = data.getStringExtra("Address");

            Intent data1 = new Intent();
            data1.putExtra("Address", Address);
            data1.putExtra("Latitude", Latitude);
            data1.putExtra("Longitude", Longitude);

            generalFunc.storeData("userHomeLocationLatitude", "" + Latitude);
            generalFunc.storeData("userHomeLocationLongitude", "" + Longitude);
            generalFunc.storeData("userHomeLocationAddress", "" + Address);

            setDestinationPoint(Latitude, Longitude, Address, true);

            if (eFly) {
                //     Logger.d("SET_PICKUP", "in 7");
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }


        } else if (requestCode == Utils.ADD_WORK_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {
            String Latitude = data.getStringExtra("Latitude");
            String Longitude = data.getStringExtra("Longitude");
            String Address = data.getStringExtra("Address");

            Intent data1 = new Intent();
            data1.putExtra("Address", Address);
            data1.putExtra("Latitude", Latitude);
            data1.putExtra("Longitude", Longitude);


            generalFunc.storeData("userWorkLocationLatitude", "" + Latitude);
            generalFunc.storeData("userWorkLocationLongitude", "" + Longitude);
            generalFunc.storeData("userWorkLocationAddress", "" + Address);


            setDestinationPoint(Latitude, Longitude, Address, true);
            if (eFly) {
                //     Logger.d("SET_PICKUP", "in 7");
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }
        }
    }

    public void openPrefrancedailog() {


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.activity_prefrance, null);

        final MTextView TitleTxt = (MTextView) dialogView.findViewById(R.id.TitleTxt);
        final MTextView noteText = (MTextView) dialogView.findViewById(R.id.noteText);
        noteText.setText(generalFunc.retrieveLangLBl("", "LBL_NOTE") + ": " + generalFunc.retrieveLangLBl("", "LBL_OPTION_FOR_FEMALE_USERS"));

        final CheckBox checkboxHandicap = (CheckBox) dialogView.findViewById(R.id.checkboxHandicap);
        final CheckBox checkboxChildseat = (CheckBox) dialogView.findViewById(R.id.checkboxChildseat);
        final CheckBox checkboxWheelChair = (CheckBox) dialogView.findViewById(R.id.checkboxWheelChair);
        final CheckBox checkboxFemale = (CheckBox) dialogView.findViewById(R.id.checkboxFemale);
        final ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelImg);
        checkboxFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes") && generalFunc.getJsonValue("eGender", userProfileJson).equals("")) {
                    checkboxFemale.setChecked(false);
                    genderDailog();
                    return;
                }


            }
        });

        cancelImg.setOnClickListener(v -> pref_dialog.dismiss());


        if (generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("yes")) {
            checkboxHandicap.setVisibility(View.VISIBLE);
        } else {
            checkboxHandicap.setVisibility(View.GONE);
        }
        if (generalFunc.retrieveValue(Utils.CHILD_SEAT_ACCESSIBILITY_OPTION).equalsIgnoreCase("yes")) {
            checkboxChildseat.setVisibility(View.VISIBLE);
        } else {
            checkboxChildseat.setVisibility(View.GONE);
        }
        if (generalFunc.retrieveValue(Utils.WHEEL_CHAIR_ACCESSIBILITY_OPTION).equalsIgnoreCase("yes")) {
            checkboxWheelChair.setVisibility(View.VISIBLE);
        } else {
            checkboxWheelChair.setVisibility(View.GONE);
        }

        if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("yes")) {
            if (!generalFunc.getJsonValue("eGender", userProfileJson).equalsIgnoreCase("") && !generalFunc.getJsonValue("eGender", userProfileJson).equalsIgnoreCase("Male")) {
                checkboxFemale.setVisibility(View.VISIBLE);

                noteText.setVisibility(View.GONE);

            } else if (generalFunc.getJsonValue("eGender", userProfileJson).equalsIgnoreCase("")) {
                checkboxFemale.setVisibility(View.VISIBLE);
            } else {
                checkboxFemale.setVisibility(View.GONE);
                noteText.setVisibility(View.GONE);
            }
        } else {
            checkboxFemale.setVisibility(View.GONE);
            noteText.setVisibility(View.GONE);
        }
        if (isfemale) {
            checkboxFemale.setChecked(true);
        }

        if (ishandicap) {
            checkboxHandicap.setChecked(true);
        }
        if (isChildSeat) {
            checkboxChildseat.setChecked(true);
        }
        if (isWheelChair) {
            checkboxWheelChair.setChecked(true);
        }
        MButton btn_type2 = btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        int submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        btn_type2.setText(generalFunc.retrieveLangLBl("Update", "LBL_UPDATE"));
        btn_type2.setOnClickListener(v -> {
            pref_dialog.dismiss();
            if (checkboxFemale.isChecked()) {
                isfemale = true;

            } else {
                isfemale = false;

            }
            if (checkboxHandicap.isChecked()) {
                ishandicap = true;

            } else {
                ishandicap = false;
            }
            if (checkboxChildseat.isChecked()) {
                isChildSeat = true;

            } else {
                isChildSeat = false;
            }
            if (checkboxWheelChair.isChecked()) {
                isWheelChair = true;

            } else {
                isWheelChair = false;
            }


            if (loadAvailCabs != null) {
                loadAvailCabs.changeCabs();
            }

        });


        builder.setView(dialogView);
        TitleTxt.setText(generalFunc.retrieveLangLBl("Prefrance", "LBL_PREFRANCE_TXT"));
        checkboxHandicap.setText(generalFunc.retrieveLangLBl("Filter handicap accessibility drivers only", "LBL_MUST_HAVE_HANDICAP_ASS_CAR"));
        checkboxFemale.setText(generalFunc.retrieveLangLBl("Accept Female Only trip request", "LBL_ACCEPT_FEMALE_REQ_ONLY_PASSENGER"));
        checkboxChildseat.setText(generalFunc.retrieveLangLBl("", "LBL_MUST_HAVE_CHILD_SEAT_ASS_CAR"));
        checkboxWheelChair.setText(generalFunc.retrieveLangLBl("", "LBL_MUST_HAVE_WHEEL_CHAIR_ASS_CAR"));


        pref_dialog = builder.create();
        pref_dialog.setCancelable(false);
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(pref_dialog);
        }
        pref_dialog.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
        pref_dialog.show();

    }

    public void getTollcostValue(final String driverIds, final String cabRequestedJson, final Intent data) {

        if (isFixFare && !isMultiDelivery()) {
            setDeliverOrRideReq(driverIds, cabRequestedJson, data);
            return;
        }


        if (cabSelectionFrag != null && !isMultiDelivery()) {
            if (cabSelectionFrag.isSkip) {
                setDeliverOrRideReq(driverIds, cabRequestedJson, data);
                return;
            }
        }

        // Toll Disabled for MultiDelivery

        if (generalFunc.retrieveValue(Utils.ENABLE_TOLL_COST).equalsIgnoreCase("Yes") && !isMultiDelivery() && !eFly) {

            String wayPoints = "";


            String MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY = generalFunc.retrieveValue(Utils.MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY);
            if (isMultiDelivery() && Utils.checkText(MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY)) {

                Gson gson = new Gson();
                String data1 = MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY;
                ArrayList<Multi_Delivery_Data> listofViews = gson.fromJson(data1, new TypeToken<ArrayList<Multi_Delivery_Data>>() {
                        }.getType()
                );

                for (int i = 0; i < listofViews.size(); i++) {

                    for (int j = 0; j < listofViews.get(i).getDt().size(); j++) {

                        if (listofViews.get(i).getDt().get(j).geteInputType().equalsIgnoreCase("SelectAddress")) {
                            wayPoints = wayPoints + "&waypoint" + (i + 1) + "=" + listofViews.get(i).getDt().get(j).getDestLat() + "," + listofViews.get(i).getDt().get(j).getDestLong();
                            break;
                        }
                    }
                }

            } else {
                wayPoints = "&waypoint1=" + getDestLocLatitude() + "," + getDestLocLongitude();
            }

            String url = CommonUtilities.TOLLURL + generalFunc.retrieveValue(Utils.TOLL_COST_APP_ID)
                    + "&app_code=" + generalFunc.retrieveValue(Utils.TOLL_COST_APP_CODE) + "&waypoint0=" + getPickUpLocation().getLatitude()
                    + "," + getPickUpLocation().getLongitude() + wayPoints + "&mode=fastest;car";

            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), url, true);
            exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
            exeWebServer.setDataResponseListener(responseString -> {


                if (responseString != null && !responseString.equals("")) {

                    if (generalFunc.getJsonValue("onError", responseString).equalsIgnoreCase("FALSE")) {
                        try {

                            String costs = generalFunc.getJsonValue("costs", responseString);

                            String currency = generalFunc.getJsonValue("currency", costs);
                            String details = generalFunc.getJsonValue("details", costs);
                            String tollCost = generalFunc.getJsonValue("tollCost", details);
                            if (currency != null && !currency.equals("")) {
                                tollcurrancy = currency;
                            }
                            tollamount = 0.0;
                            if (tollCost != null && !tollCost.equals("") && !tollCost.equals("0.0")) {
                                tollamount = GeneralFunctions.parseDoubleValue(0.0, tollCost);
                            }


                            TollTaxDialog(driverIds, cabRequestedJson, data);


                        } catch (Exception e) {

                            TollTaxDialog(driverIds, cabRequestedJson, data);
                        }

                    } else {
                        TollTaxDialog(driverIds, cabRequestedJson, data);
                    }


                } else {
                    generalFunc.showError();
                }

            });
            exeWebServer.execute();


        } else {
            setDeliverOrRideReq(driverIds, cabRequestedJson, data);
        }

    }

    public void setCancelable(Dialog dialogview, boolean cancelable) {
        final Dialog dialog = dialogview;
        View touchOutsideView = dialog.getWindow().getDecorView().findViewById(R.id.touch_outside);
        View bottomSheetView = dialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);

        if (cancelable) {
            touchOutsideView.setOnClickListener(v -> {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            });
            BottomSheetBehavior.from(bottomSheetView).setHideable(true);
        } else {
            touchOutsideView.setOnClickListener(null);
            BottomSheetBehavior.from(bottomSheetView).setHideable(false);
        }
    }

    private void setDeliverOrRideReq(String driverIds, String cabRequestedJson, Intent data) {

        if (isDeliver(getCurrentCabGeneralType()) && isDeliver(app_type)) {
            // setDeliverySchedule();
        } else {

            if (app_type.equals(Utils.CabGeneralType_UberX)) {
                Log.d(TAG, "setCurrentType: 32");
                pickUpLocClicked();
            } else {

                if (getCabReqType().equals(Utils.CabReqType_Later)) {
                    isrideschedule = true;

                } else {
                    isreqnow = true;

                }
                // requestPickUp();
            }
        }


        if (data != null) {
            if (isdelivernow) {
                isdelivernow = false;
                deliverNow(data);
            } else if (isdeliverlater) {
                isdeliverlater = false;
                scheduleDelivery(data);
            }


        } else {
            if (isrideschedule) {
                isrideschedule = false;
                bookRide();
            } else if (isreqnow) {
                isreqnow = false;
                //sendRequestToDrivers(driverIds, cabRequestedJson);
                requestPickUp();
            }

        }
    }


    public void TollTaxDialog(final String driverIds, final String cabRequestedJson, final Intent data) {

        if (!isTollCostdilaogshow) {
            if (tollamount != 0.0 && tollamount != 0 && tollamount != 0.00) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

                LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_tolltax, null);

                final MTextView tolltaxTitle = (MTextView) dialogView.findViewById(R.id.tolltaxTitle);
                final MTextView tollTaxMsg = (MTextView) dialogView.findViewById(R.id.tollTaxMsg);
                final MTextView tollTaxpriceTxt = (MTextView) dialogView.findViewById(R.id.tollTaxpriceTxt);
                final MTextView cancelTxt = (MTextView) dialogView.findViewById(R.id.cancelTxt);

                final CheckBox checkboxTolltax = (CheckBox) dialogView.findViewById(R.id.checkboxTolltax);

                checkboxTolltax.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (checkboxTolltax.isChecked()) {
                        istollIgnore = true;
                    } else {
                        istollIgnore = false;
                    }

                });


                MTextView btn_type2 = ((MTextView) dialogView.findViewById(R.id.btn_type2));
                int submitBtnId = Utils.generateViewId();
                btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN"));
                btn_type2.setOnClickListener(v -> {
                    tolltax_dialog.dismiss();
                    isTollCostdilaogshow = true;
                    setDeliverOrRideReq(driverIds, cabRequestedJson, data);


                });


                builder.setView(dialogView);
                tolltaxTitle.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_ROUTE"));
                tollTaxMsg.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_PRICE_DESC"));

                tollTaxMsg.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_PRICE_DESC"));

                String payAmount = payableAmount;
                if (isMultiDelivery() && data != null) {
                    payableAmount = generalFunc.convertNumberWithRTL(data.getStringExtra("totalFare"));
                } else if (cabSelectionFrag != null && cabTypeList != null && cabTypeList.size() > 0 && cabTypeList.get(cabSelectionFrag.selpos).get("total_fare") != null && !cabTypeList.get(cabSelectionFrag.selpos).get("total_fare").equals("") && !cabTypeList.get(cabSelectionFrag.selpos).get("eRental").equals("Yes")) {

                    try {
                        payAmount = generalFunc.convertNumberWithRTL(cabTypeList.get(cabSelectionFrag.selpos).get("total_fare"));
                    } catch (Exception e) {

                    }


                }

                if (payAmount.equalsIgnoreCase("")) {
                    tollTaxpriceTxt.setText(generalFunc.retrieveLangLBl("Total toll price", "LBL_TOLL_PRICE_TOTAL") + ": " + tollcurrancy + " " + generalFunc.convertNumberWithRTL(tollamount + ""));
                } else {
                    tollTaxpriceTxt.setText(generalFunc.retrieveLangLBl(
                            "Current Fare", "LBL_CURRENT_FARE") + ": " + payAmount + "\n" + "+" + "\n" +
                            generalFunc.retrieveLangLBl("Total toll price", "LBL_TOLL_PRICE_TOTAL") + ": " + tollcurrancy + " " + generalFunc.convertNumberWithRTL(tollamount + ""));
                }

                checkboxTolltax.setText(generalFunc.retrieveLangLBl("", "LBL_IGNORE_TOLL_ROUTE"));
                cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));

                cancelTxt.setOnClickListener(v -> {
                    tolltax_dialog.dismiss();
                    isreqnow = false;
                    // cabSelectionFrag.ride_now_btn.setEnabled(true);
                    // cabSelectionFrag.ride_now_btn.setClickable(true);

                    // closeRequestDialog(true);
                });


                tolltax_dialog = builder.create();
                if (generalFunc.isRTLmode() == true) {
                    generalFunc.forceRTLIfSupported(tolltax_dialog);
                }
                tolltax_dialog.setCancelable(false);

                tolltax_dialog.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
                tolltax_dialog.show();
            } else {
                setDeliverOrRideReq(driverIds, cabRequestedJson, data);
            }
        } else {
            setDeliverOrRideReq(driverIds, cabRequestedJson, data);

        }
    }

    public void callgederApi(String egender) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateUserGender");
        parameters.put("UserType", Utils.userType);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("eGender", egender);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);


            String message = generalFunc.getJsonValue(Utils.message_str, responseString);
            if (isDataAvail) {
                generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                getUserProfileJson();
                prefBtnImageView.performClick();
            }


        });
        exeWebServer.execute();
    }

    public void genderDailog() {
        closeDrawer();
        final Dialog builder = new Dialog(getActContext(), R.style.Theme_Dialog);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setContentView(R.layout.gender_view);
        builder.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        final MTextView genderTitleTxt = (MTextView) builder.findViewById(R.id.genderTitleTxt);
        final MTextView maleTxt = (MTextView) builder.findViewById(R.id.maleTxt);
        final MTextView femaleTxt = (MTextView) builder.findViewById(R.id.femaleTxt);
        final ImageView gendercancel = (ImageView) builder.findViewById(R.id.gendercancel);
        final ImageView gendermale = (ImageView) builder.findViewById(R.id.gendermale);
        final ImageView genderfemale = (ImageView) builder.findViewById(R.id.genderfemale);
        final LinearLayout male_area = (LinearLayout) builder.findViewById(R.id.male_area);
        final LinearLayout female_area = (LinearLayout) builder.findViewById(R.id.female_area);


        if (generalFunc.isRTLmode()) {

            //            ((LinearLayout)builder.findViewById(R.id.llCancelButton)).setRotation(180);
                                /*RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.ALIGN_PARENT_START);
            gendercancel.setLayoutParams(params1);*/

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.START;
            gendercancel.setLayoutParams(params1);
        }

        genderTitleTxt.setText(generalFunc.retrieveLangLBl("Select your gender to continue", "LBL_SELECT_GENDER"));
        maleTxt.setText(generalFunc.retrieveLangLBl("Male", "LBL_MALE_TXT"));
        femaleTxt.setText(generalFunc.retrieveLangLBl("FeMale", "LBL_FEMALE_TXT"));

        gendercancel.setOnClickListener(v -> builder.dismiss());

        male_area.setOnClickListener(v -> {
            if (pref_dialog != null) {
                pref_dialog.dismiss();
            }

            callgederApi("Male");
            builder.dismiss();

        });
        female_area.setOnClickListener(v -> {

            if (pref_dialog != null) {
                pref_dialog.dismiss();
            }
            callgederApi("Female");
            builder.dismiss();

        });

        builder.show();

    }


    public void homeClick() {
        Bundle bn = new Bundle();
        HashMap<String, String> data = new HashMap<>();
        data.put("userHomeLocationAddress", "");
        data.put("userHomeLocationLatitude", "");
        data.put("userHomeLocationLongitude", "");
        data = GeneralFunctions.retrieveValue(data, getActContext());

        final String home_address_str = data.get("userHomeLocationAddress");
        final String home_addr_latitude = data.get("userHomeLocationLatitude");
        final String home_addr_longitude = data.get("userHomeLocationLongitude");

        Intent data1 = new Intent();
        data1.putExtra("Address", home_address_str);
        data1.putExtra("Latitude", home_addr_latitude);
        data1.putExtra("Longitude", home_addr_longitude);
        if (home_address_str != null && !home_address_str.equalsIgnoreCase("")) {

            setDestinationPoint(home_addr_latitude, home_addr_longitude, home_address_str, true);

            if (eFly) {
                //     Logger.d("SET_PICKUP", "in 7");
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }

        } else {
            if (intCheck.isNetworkConnected()) {
                bn.putString("isHome", "true");
                new StartActProcess(getActContext()).startActForResult(SearchPickupLocationActivity.class,
                        bn, Utils.ADD_HOME_LOC_REQ_CODE);
            } else {
                generalFunc.showMessage(btn_type_ridelater, generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
            }
        }

    }

    public void workClick() {
        Bundle bndl = new Bundle();

        HashMap<String, String> data = new HashMap<>();
        data.put("userWorkLocationAddress", "");
        data.put("userWorkLocationLatitude", "");
        data.put("userWorkLocationLongitude", "");
        data = generalFunc.retrieveValue(data);


        String work_address_str = data.get("userWorkLocationAddress");
        String work_addr_latitude = data.get("userWorkLocationLatitude");
        String work_addr_longitude = data.get("userWorkLocationLongitude");

        Intent data1 = new Intent();
        data1.putExtra("Address", work_address_str);
        data1.putExtra("Latitude", work_addr_latitude);
        data1.putExtra("Longitude", work_addr_longitude);

        if (work_address_str != null && !work_address_str.equalsIgnoreCase("")) {

            setDestinationPoint(work_addr_latitude, work_addr_longitude, work_address_str, true);
            if (eFly) {
                //     Logger.d("SET_PICKUP", "in 7");
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }
        } else {
            if (intCheck.isNetworkConnected()) {
                bndl.putString("isWork", "true");
                if (getIntent().hasExtra("isFromMulti")) {
                    bndl.putBoolean("isFromMulti", true);
                    bndl.putInt("pos", getIntent().getIntExtra("pos", -1));
                }

                new StartActProcess(getActContext()).startActForResult(SearchPickupLocationActivity.class,
                        bndl, Utils.ADD_WORK_LOC_REQ_CODE);
            } else {
                generalFunc.showMessage(btn_type_ridelater, generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
            }
        }
    }

    private void prefrenceButtonEnable() {

        String currentCabType = getCurrentCabGeneralType();
        String HANDICAP_ACCESSIBILITY_OPTION = generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION);
        String FEMALE_RIDE_REQ_ENABLE = generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE);
        String CHILD_SEAT_ACCESSIBILITY_OPTION = generalFunc.retrieveValue(Utils.CHILD_SEAT_ACCESSIBILITY_OPTION);
        String WHEEL_CHAIR_ACCESSIBILITY_OPTION = generalFunc.retrieveValue(Utils.WHEEL_CHAIR_ACCESSIBILITY_OPTION);

        if ((!HANDICAP_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !FEMALE_RIDE_REQ_ENABLE.equalsIgnoreCase("YES") && !CHILD_SEAT_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !WHEEL_CHAIR_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES")) || (FEMALE_RIDE_REQ_ENABLE.equalsIgnoreCase("YES") && generalFunc.getJsonValue("eGender", userProfileJson).equals("Male") && !HANDICAP_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !CHILD_SEAT_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !WHEEL_CHAIR_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES")) || ((currentCabType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || currentCabType.equalsIgnoreCase("Deliver") || currentCabType.equalsIgnoreCase(Utils.CabGeneralType_UberX)))) {
            prefBtnImageView.setVisibility(View.GONE);
            isPrefrenceEnable = false;
            reSetButton(false);
        } else {
            prefBtnImageView.setVisibility(View.VISIBLE);
            isPrefrenceEnable = true;
        }

      /*  if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("No") &&
                generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("No")) {
            prefBtnImageView.setVisibility(View.GONE);

        } else if (generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("No") &&
                !generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes")
                || (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes") &&
                generalFunc.getJsonValue("eGender", userProfileJson).equals("Male")
                && !generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("Yes"))) {
            prefBtnImageView.setVisibility(View.GONE);
        }*/
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == userLocBtnImgView.getId()) {
                moveToCurrentLoc();

            } else if (i == userTripBtnImgView.getId()) {

                if (!isUserTripClick) {
                    isUserTripClick = true;
                    userTripBtnImgView.setColorFilter(getActContext().getResources().getColor(R.color.btnnavtripselcolor));
                    if (driverAssignedHeaderFrag != null && driverAssignedHeaderFrag.tempdriverLocation_update != null) {
                        animateToLocation(driverAssignedHeaderFrag.tempdriverLocation_update.latitude, driverAssignedHeaderFrag.tempdriverLocation_update.longitude, Utils.defaultZomLevel);
                    }
                } else {
                    isUserTripClick = false;
                    userTripBtnImgView.setColorFilter(getActContext().getResources().getColor(R.color.black));
                }
            } else if (i == emeTapImgView.getId()) {
                Bundle bn = new Bundle();
                bn.putString("UserProfileJson", userProfileJson);
                bn.putString("TripId", assignedTripId);
                new StartActProcess(getActContext()).startActWithData(ConfirmEmergencyTapActivity.class, bn);
            } else if (i == rideArea.getId()) {
                ((ImageView) findViewById(R.id.rideImg)).setImageResource(R.mipmap.ride_on);
                rideImgViewsel.setVisibility(View.VISIBLE);
                ((MTextView) findViewById(R.id.selrideTxt)).setVisibility(View.VISIBLE);
                ((MTextView) findViewById(R.id.rideTxt)).setVisibility(View.GONE);
                rideImgView.setVisibility(View.GONE);
                deliverImgView.setVisibility(View.VISIBLE);
                deliverImgViewsel.setVisibility(View.GONE);
                otherImageView.setVisibility(View.VISIBLE);
                otherImageViewsel.setVisibility(View.GONE);

                ((ImageView) findViewById(R.id.deliverImg)).setImageResource(R.mipmap.delivery_off);
                ((MTextView) findViewById(R.id.rideTxt)).setTextColor(Color.parseColor("#000000"));
                ((MTextView) findViewById(R.id.deliverTxt)).setTextColor(Color.parseColor("#000000"));

                RideDeliveryType = Utils.CabGeneralType_Ride;

                if (mainHeaderFrag != null && generalFunc.isMultiDelivery() && app_type.equalsIgnoreCase("Ride-Delivery")) {
                    mainHeaderFrag.setDestinationViewEnableOrDisabled(RideDeliveryType, false);
                    Log.d(TAG, "setCurrentType: 32");
                }

                prefBtnImageView.setVisibility(View.VISIBLE);
                prefrenceButtonEnable();

                if (cabSelectionFrag != null) {
                    cabSelectionFrag.changeCabGeneralType(Utils.CabGeneralType_Ride);
                    cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Ride;

                    if (cabSelectionFrag.cabTypeList != null) {
                        cabSelectionFrag.cabTypeList.clear();
                        cabSelectionFrag.adapter.notifyDataSetChanged();
                    }
                }

                if (loadAvailCabs != null) {
                    loadAvailCabs.checkAvailableCabs();
                }

            } else if (i == deliverArea.getId()) {

                rideImgViewsel.setVisibility(View.GONE);
                ((MTextView) findViewById(R.id.selrideTxt)).setVisibility(View.GONE);
                ((MTextView) findViewById(R.id.rideTxt)).setVisibility(View.VISIBLE);
                rideImgView.setVisibility(View.VISIBLE);
                deliverImgView.setVisibility(View.GONE);
                deliverImgViewsel.setVisibility(View.VISIBLE);
                otherImageView.setVisibility(View.VISIBLE);
                otherImageViewsel.setVisibility(View.GONE);

                ((ImageView) findViewById(R.id.rideImg)).setImageResource(R.mipmap.ride_off);
                ((ImageView) findViewById(R.id.deliverImg)).setImageResource(R.mipmap.delivery_on);

                ((MTextView) findViewById(R.id.rideTxt)).setTextColor(Color.parseColor("#000000"));

                ((MTextView) findViewById(R.id.deliverTxt)).setTextColor(Color.parseColor("#000000"));

                RideDeliveryType = Utils.CabGeneralType_Deliver;

                if (mainHeaderFrag != null && generalFunc.isMultiDelivery() && app_type.equalsIgnoreCase("Ride-Delivery")) {
                    mainHeaderFrag.setDestinationViewEnableOrDisabled(RideDeliveryType, true);
                    Log.d(TAG, "setCurrentType: 33");
                }


                isfemale = false;
                ishandicap = false;
                isChildSeat = false;
                isWheelChair = false;
                prefBtnImageView.setVisibility(View.GONE);

                if (cabSelectionFrag != null) {
                    cabSelectionFrag.changeCabGeneralType(Utils.CabGeneralType_Deliver);
                    cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Deliver;

                    if (cabSelectionFrag.cabTypeList != null) {
                        cabSelectionFrag.cabTypeList.clear();
                        cabSelectionFrag.adapter.notifyDataSetChanged();
                    }
                }

                if (loadAvailCabs != null) {
                    loadAvailCabs.checkAvailableCabs();
                }

            } else if (i == otherArea.getId()) {
                rideImgViewsel.setVisibility(View.GONE);
                ((MTextView) findViewById(R.id.selrideTxt)).setVisibility(View.GONE);
                ((MTextView) findViewById(R.id.rideTxt)).setVisibility(View.VISIBLE);
                rideImgView.setVisibility(View.VISIBLE);
                deliverImgView.setVisibility(View.VISIBLE);
                deliverImgViewsel.setVisibility(View.GONE);
                otherImageView.setVisibility(View.GONE);
                otherImageViewsel.setVisibility(View.VISIBLE);


                RideDeliveryType = Utils.CabGeneralType_UberX;
                if (cabSelectionFrag != null) {
                    cabSelectionFrag.changeCabGeneralType(Utils.CabGeneralType_UberX);
                    cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_UberX;

                }
                Bundle bn = new Bundle();
                bn.putBoolean("isback", true);
                if (pickUpLocation != null) {
                    bn.putString("lat", pickUpLocation.getLatitude() + "");
                    bn.putString("long", pickUpLocation.getLongitude() + "");
                    bn.putString("address", pickUpLocationAddress);
                }
                new StartActProcess(getActContext()).startActForResult(UberXActivity.class, bn, Utils.OTHER_AREA_CLICKED_CODE);
            } else if (i == prefBtnImageView.getId()) {

                getUserProfileJson();
                openPrefrancedailog();
            } else if (i == backImgView.getId()) {
                onBackPressed();
            } else if (i == filterTxtView.getId()) {
                openFilterDilaog();
            } else if (i == workArea.getId()) {
                checkForSourceLocation(i);/*workClick();*/
            } else if (i == homeArea.getId()) {
                checkForSourceLocation(i);/*homeClick();*/
            } else if (i == destSelectArea.getId()) {
//                checkForSourceLocation(i);
                Intent intent=new Intent(getBaseContext(),DestinationsList.class);
                startActivity(intent);
                Logger.d("obj1","He121");

                /*if (mainHeaderFrag != null) {
                    mainHeaderFrag.destarea.performClick();
                }*/
            }
        }
    }

    private void checkForSourceLocation(int viewID) {
        selectedViewID = viewID;

        if (pickUpLocation != null) {
            if (viewID == workArea.getId()) {
                workClick();
            } else if (viewID == homeArea.getId()) {
                homeClick();
            } else if (viewID == destSelectArea.getId()) {
                if (mainHeaderFrag != null) {
                    mainHeaderFrag.destarea.performClick();
                }
            }
        } else {
            openSourceLocationView();
        }
    }

    boolean isFromSourceDialog = false;
    AppCompatDialog dialog;
    int selectedViewID;

    private void openSourceLocationView() {
        dialog = new AppCompatDialog(getActContext(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.no_source_location_design);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);

        ((RippleBackground) dialog.findViewById(R.id.rippleBgView)).startRippleAnimation();

        ImageView closeImage = dialog.findViewById(R.id.closeImage);
        closeImage.setOnClickListener(v -> {
            isFromSourceDialog = false;
            isFirstLocation = false;
            dialog.dismiss();
        });
        MTextView locationHintText = dialog.findViewById(R.id.locationHintText);
        MTextView locationDescText = dialog.findViewById(R.id.locationDescText);
        MTextView btnTxt = dialog.findViewById(R.id.btnTxt);
        ImageView btnImg = dialog.findViewById(R.id.btnImg);
        LinearLayout btnArea = dialog.findViewById(R.id.btnArea);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        RippleBackground.LayoutParams buttonLayoutParams = new RippleBackground.LayoutParams(RippleBackground.LayoutParams.MATCH_PARENT, RippleBackground.LayoutParams.MATCH_PARENT);
        buttonLayoutParams.setMargins(0, 0, 0, -(height / 2));
        ((RippleBackground) dialog.findViewById(R.id.rippleBgView)).setLayoutParams(buttonLayoutParams);


        if (generalFunc.isRTLmode()) {
            btnImg.setRotation(180);
            btnArea.setBackground(getActContext().getResources().getDrawable(R.drawable.login_border_rtl));
        }

        btnTxt.setText(generalFunc.retrieveLangLBl("ENTER", "LBL_ADD_ADDRESS_TXT"));
        locationDescText.setText(generalFunc.retrieveLangLBl("Please wait while we are trying to access your location. meanwhile you can enter your source location.", "LBL_FETCHING_LOCATION_NOTE_TEXT"));
        locationHintText.setText(generalFunc.retrieveLangLBl("Location", "LBL_LOCATION_FOR_FRONT"));

        btnArea.setOnClickListener(v -> {
            dialog.dismiss();
            isFirstLocation = false;
            isFromSourceDialog = false;

            Bundle bn = new Bundle();
            bn.putString("locationArea", "source");
            bn.putDouble("lat", 0.0);
            bn.putDouble("long", 0.0);
            new StartActProcess(getActContext()).startActForResult(SearchLocationActivity.class, bn, Utils.SEARCH_PICKUP_LOC_REQ_CODE);
        });

        isFirstLocation = true;
        isFromSourceDialog = true;
        if (getLastLocation != null) {
            getLastLocation.stopLocationUpdates();
            getLastLocation = null;
        }
        new Handler().postDelayed(() -> getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, true, this), 1500);
        dialog.show();
    }

    private ArrayList<HashMap<String, String>> populateArrayList() {
        ArrayList<HashMap<String, String>> mapArrayList = new ArrayList<>();

        ArrayList<String> sortby_List = new ArrayList<String>();
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT"));
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_NEAR_BY_TXT"));
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_RATING"));
        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
            sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_FAV_DRIVERS_FILTER_TXT"));
        }
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_AVAILABILITY"));
        for (int i = 0; i < sortby_List.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("vName", "" + sortby_List.get(i));
            map.put("selectedSortValue", "" + selectedSortValue);
            mapArrayList.add(map);
        }
        return mapArrayList;
    }

    public void openFilterDilaog() {


        String enablefvdriverkey = generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY);
        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SORT_BY_TXT"), arrayList, OpenListView.OpenDirection.BOTTOM, true, position -> {

            filterTxtView.setText(arrayList.get(position).get("vName"));

            filterPosition = position;
            if (position == 0) {
                selectedSort = "eIsFeatured";
                selectedSortValue = arrayList.get(0).get("vName");
            } else if (position == 1) {
                selectedSort = "distance";
                selectedSortValue = arrayList.get(1).get("vName");
            } else if (position == 2) {
                selectedSort = "vAvgRating";
                selectedSortValue = arrayList.get(2).get("vName");
            } else if (position == 3 && enablefvdriverkey.equalsIgnoreCase("Yes")) {
                selectedSort = "eFavDriver";
                selectedSortValue = arrayList.get(3).get("vName");
            } else if ((position == 4 && enablefvdriverkey.equalsIgnoreCase("Yes")) || position == 3) {
                if (enablefvdriverkey.equalsIgnoreCase("Yes")) {
                    selectedSort = "IS_PROVIDER_ONLINE";
                    selectedSortValue = arrayList.get(4).get("vName");
                } else {
                    selectedSort = "IS_PROVIDER_ONLINE";
                    selectedSortValue = arrayList.get(3).get("vName");
                }
            }
            findViewById(R.id.driverListAreaLoader).setVisibility(View.VISIBLE);

            if (loadAvailCabs != null) {

                loadAvailCabs.sortby = selectedSort;
                loadAvailCabs.changeCabs();
                ////////
                loadAvailCabs.checkAvailableCabs();

            }

        }).show(filterPosition, "vName");
    }

    private void moveToCurrentLoc() {
        if (!generalFunc.isLocationEnabled()) {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please enable you GPS location service", "LBL_GPSENABLE_TXT"));
            return;
        }

        isUserLocbtnclik = true;

        if (cabSelectionFrag == null) {

            if (driverAssignedHeaderFrag != null) {
                if (driverAssignedHeaderFrag.sourceMarker != null) {
                    driverAssignedHeaderFrag.sourceMarker.remove();
                    driverAssignedHeaderFrag.sourceMarker = null;
                }

                if (driverAssignedHeaderFrag.destinationPointMarker_temp != null) {
                    driverAssignedHeaderFrag.destinationPointMarker_temp.remove();
                    driverAssignedHeaderFrag.destinationPointMarker_temp = null;
                }
            }

            if (isDriverAssigned && !isTripStarted && driverAssignedHeaderFrag != null) {
                //driver topickup
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if (driverAssignedHeaderFrag.driverMarker != null) {
                    builder.include(driverAssignedHeaderFrag.driverMarker.getPosition());
                }
                if (driverAssignedHeaderFrag.time_marker != null) {
                    builder.include(driverAssignedHeaderFrag.time_marker.getPosition());
                } else {
                    driverAssignedHeaderFrag.addPickupMarker();
                    if (driverAssignedHeaderFrag.sourceMarker != null) {
                        builder.include(driverAssignedHeaderFrag.sourceMarker.getPosition());
                    }
                }

                if (driverAssignedHeaderFrag.driverMarker != null) {
                    LatLngBounds bounds = builder.build();

                    LatLng center = bounds.getCenter();
                    LatLng northEast = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), SphericalUtil.computeHeading(center, bounds.northeast));
                    LatLng southWest = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), (180 + (180 + SphericalUtil.computeHeading(center, bounds.southwest))));
                    builder.include(southWest);
                    builder.include(northEast);


                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;
                    int padding = (int) (width * 0.25); // offset from edges of the map 10% of screen

                    /*  Method 3 */
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
                    padding = (int) ((height - ((driverAssignedHeaderFrag.fragmentHeight + 5) + Utils.dipToPixels(getActContext(), 170))) / (4.5));
                    Logger.e("MapHeight", "cameraUpdate" + padding);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,
                            screenWidth, screenHeight, padding);

                    float maxZoomLevel = gMap.getMaxZoomLevel();

                    try {
                        gMap.setPadding(0, 320, 0, sliding_layout.getPanelHeight() + 5);

                        gMap.setMaxZoomPreference(maxZoomLevel - 5);
                        int finalPadding = padding;
                        gMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                try {

                                    // gMap.animateCamera(CameraUpdateFactory.scrollBy(0, Utils.dpToPx(getActContext(), -200)));
                                    gMap.setMaxZoomPreference(maxZoomLevel);
                                    gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);

                                } catch (Exception e) {
                                    Logger.d("ExceptionGMapAnim", ":onFinish:IF:" + e.getMessage().toString());

                                }

                            }

                            @Override
                            public void onCancel() {

                                try {
                                    gMap.setMaxZoomPreference(maxZoomLevel);
                                    gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);


                                    gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - ((driverAssignedHeaderFrag.fragmentHeight + 5) + Utils.dipToPixels(getActContext(), 170)), finalPadding));

                                } catch (Exception e) {
                                    Logger.d("ExceptionGMapAnim", ":OnCancel:IF:" + e.getMessage().toString());

                                }
                            }
                        });
                    } catch (Exception e) {
                        Logger.d("ExceptionGMapAnim", ":MainSubCatch:IF:" + e.getMessage().toString());

                        try {
                            gMap.setMaxZoomPreference(maxZoomLevel);
                            gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);


                            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - ((driverAssignedHeaderFrag.fragmentHeight + 5) + Utils.dipToPixels(getActContext(), 170)), padding));

                        } catch (Exception e1) {
                            Logger.d("ExceptionGMapAnim", ":MainSubCatch:IF:" + e1.getMessage().toString());

                        }
                    }
                }


            } else if (isDriverAssigned && isTripStarted && driverAssignedHeaderFrag != null) {
                //driver to dest;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if (driverAssignedHeaderFrag.driverMarker != null) {
                    builder.include(driverAssignedHeaderFrag.driverMarker.getPosition());
                }
                if (driverAssignedHeaderFrag.destLocation != null) {
                    builder.include(driverAssignedHeaderFrag.destLocation);
                }
                if (driverAssignedHeaderFrag.driverMarker != null) {
                    LatLngBounds bounds = builder.build();

                    LatLng center = bounds.getCenter();
                    LatLng northEast = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), SphericalUtil.computeHeading(center, bounds.northeast));
                    LatLng southWest = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), (180 + (180 + SphericalUtil.computeHeading(center, bounds.southwest))));
                    builder.include(southWest);
                    builder.include(northEast);


                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;
                    int padding = (int) (width * 0.25); // offset from edges of the map 10% of screen

                    /*  Method 3 */
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
                    padding = (int) ((height - ((driverAssignedHeaderFrag.fragmentHeight + 5) + Utils.dipToPixels(getActContext(), 170))) / (4.5));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,
                            screenWidth, screenHeight, padding);

                    float maxZoomLevel = gMap.getMaxZoomLevel();

                    try {

                        gMap.setPadding(0, 320, 0, sliding_layout.getPanelHeight() + 5);


                        gMap.setMaxZoomPreference(maxZoomLevel - 5);
                        int finalPadding = padding;
                        gMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                try {

//                                        gMap.animateCamera(CameraUpdateFactory.scrollBy(0, Utils.dpToPx(getActContext(), -200)));
                                    gMap.setMaxZoomPreference(maxZoomLevel);
                                    gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);

                                } catch (Exception e) {
                                    Logger.d("ExceptionGMapAnim", ":OnFinish:" + e.getMessage().toString());

                                }
                            }

                            @Override
                            public void onCancel() {
                                try {

                                    gMap.setMaxZoomPreference(maxZoomLevel);
                                    gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);

                                    gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - ((driverAssignedHeaderFrag.fragmentHeight + 5) + Utils.dipToPixels(getActContext(), 170)), finalPadding));
                                    //  gMap.animateCamera(CameraUpdateFactory.scrollBy(0, Utils.dpToPx(getActContext(), -200)));

                                } catch (Exception e) {

                                    Logger.d("ExceptionGMapAnim", ":OnCancel:" + e.getMessage().toString());
                                }

                            }
                        });
                    } catch (Exception e) {
                        Logger.d("ExceptionGMapAnim", ":MainCatch:" + e.getMessage().toString());


                        try {

                            gMap.setMaxZoomPreference(maxZoomLevel);
                            gMap.setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);

                            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - ((driverAssignedHeaderFrag.fragmentHeight + 5) + Utils.dipToPixels(getActContext(), 170)), padding));

//                                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), metrics.heightPixels - Utils.dpToPx(getActContext(), 200), 100));
                            //  gMap.animateCamera(CameraUpdateFactory.scrollBy(0, Utils.dpToPx(getActContext(), -200)));

                        } catch (Exception e1) {
                            Logger.d("ExceptionGMapAnim", ":MainSubCatch:" + e1.getMessage().toString());

                        }
                    }
                }
            } else {
                try {
                    CameraPosition cameraPosition = cameraForUserPosition();
                    if (cameraPosition != null) {
                        getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        if (mainHeaderFrag != null && mainHeaderFrag.getAddressFromLocation != null && userLocation != null) {
                            mainHeaderFrag.getAddressFromLocation.setLocation(userLocation.getLatitude(), userLocation.getLongitude());
                            if (!getIntent().getStringExtra("address").equalsIgnoreCase("")) {
                                mainHeaderFrag.setPickUpAddress(getIntent().getStringExtra("address"));
                            } else {
                                mainHeaderFrag.getAddressFromLocation.execute();
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }


        } else if (cabSelectionFrag != null) {

            if (cabSelectionFrag.isSkip) {
                cabSelectionFrag.handleSourceMarker(timeval);
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if (cabSelectionFrag.sourceMarker != null) {
                builder.include(cabSelectionFrag.sourceMarker.getPosition());
            }
            if (cabSelectionFrag.destDotMarker != null) {
                builder.include(cabSelectionFrag.destDotMarker.getPosition());
            }
            float maxZoomLevel = gMap.getMaxZoomLevel();
            if (cabSelectionFrag.sourceDotMarker != null && cabSelectionFrag.destDotMarker != null && gMap != null) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                gMap.setMaxZoomPreference(maxZoomLevel);
                gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width - Utils.dipToPixels(getActContext(), 80), (metrics.heightPixels - Utils.dipToPixels(getActContext(), 300)), 0));

            }


        }


    }

    public class SendNotificationsToDriverByDist implements Runnable {

        String[] list_drivers_ids;
        String cabRequestedJson;

        int interval = GeneralFunctions.parseIntegerValue(30, generalFunc.getJsonValue("RIDER_REQUEST_ACCEPT_TIME", userProfileJson));

        int mInterval = (interval + 5) * 1000;

        int current_position_driver_id = 0;
        private Handler mHandler_sendNotification;

        public SendNotificationsToDriverByDist(String list_drivers_ids, String cabRequestedJson) {
            this.list_drivers_ids = list_drivers_ids.split(",");
            this.cabRequestedJson = cabRequestedJson;
            mHandler_sendNotification = new Handler();

            startRepeatingTask();
        }

        @Override
        public void run() {
            if (current_position_driver_id == -1) {
                return;
            }
            setRetryReqBtn(false);

            if ((current_position_driver_id + 1) <= list_drivers_ids.length) {
                sendRequestToDrivers(list_drivers_ids[current_position_driver_id], cabRequestedJson);
                current_position_driver_id = current_position_driver_id + 1;
                mHandler_sendNotification.postDelayed(this, mInterval);
            } else {
                setRetryReqBtn(true);
                stopRepeatingTask();
            }

        }


        public void stopRepeatingTask() {
            mHandler_sendNotification.removeCallbacks(this);
            mHandler_sendNotification.removeCallbacksAndMessages(null);
            current_position_driver_id = -1;
        }

        public void incTask() {
            mHandler_sendNotification.removeCallbacks(this);
            mHandler_sendNotification.removeCallbacksAndMessages(null);
            if (current_position_driver_id != -1) {
                this.run();
            }
        }

        public void startRepeatingTask() {
            stopRepeatingTask();
            current_position_driver_id = 0;
            this.run();
        }
    }

    public boolean isMultiStopOverEnabled() {
        boolean isStopOverEnabled = generalFunc.retrieveValue(Utils.ENABLE_STOPOVER_POINT_KEY).equalsIgnoreCase("Yes") && getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride);
        boolean isRental = false;
        if (cabSelectionFrag != null) {
            if (!cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
                isRental = true;
            }
        }

        return isStopOverEnabled && !isRental && !eFly;

    }
}
