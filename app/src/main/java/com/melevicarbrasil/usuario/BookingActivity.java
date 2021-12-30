package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.files.ViewPagerAdapter;
import com.melevicarbrasil.usuario.deliverAll.TrackOrderActivity;
import com.dialogs.OpenListView;
import com.fragments.NewBookingFragment;
import com.fragments.OrderFragment;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    String userProfileJson;
    public ImageView filterImageview;

    int selTabPos = 0;
    private String app_type;

    ArrayList<HashMap<String, String>> filterlist;
    ArrayList<HashMap<String, String>> subFilterlist;
    ArrayList<HashMap<String, String>> orderSubFilterlist;

    public String selFilterType = "";
    public String selSubFilterType = "";
    public String selOrderSubFilterType = "";

    public int filterPosition = 0;
    public int subFilterPosition = 0;
    public int orderSubFilterPosition = 0;

    ViewPager appLogin_view_pager;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    CharSequence[] titles;
    NewBookingFragment frag;
    OrderFragment orderFrag;

    boolean isrestart = false;  // For Bookings
    boolean isRestart = false; // for OnGoing trips
    private boolean isOrder=false; // for Order

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitity_booking);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        isrestart = getIntent().getBooleanExtra("isrestart", false);
        isOrder = getIntent().getBooleanExtra("isOrder", false);
        isRestart = getIntent().getBooleanExtra("isRestart", false);

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        filterImageview = (ImageView) findViewById(R.id.filterImageview);
        filterImageview.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        app_type = generalFunc.getJsonValue("APP_TYPE", userProfileJson);
        setLabels();

        appLogin_view_pager = (ViewPager) findViewById(R.id.appLogin_view_pager);
        TabLayout material_tabs = (TabLayout) findViewById(R.id.material_tabs);
        LinearLayout tabArea = (LinearLayout) findViewById(R.id.tabArea);


        boolean isDeliverOnlyEnabled = generalFunc.isDeliverOnlyEnabled();
        boolean isAnyDeliverOptionEnabled = generalFunc.isAnyDeliverOptionEnabled();

        if (isDeliverOnlyEnabled) {
            titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_ORDERS_TXT"),};
            tabArea.setVisibility(View.GONE);
            fragmentList.add(generateOrderFrag(Utils.Past));
        }
        else if (isAnyDeliverOptionEnabled) {
            titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_BOOKING"), generalFunc.retrieveLangLBl("", "LBL_ORDERS_TXT")};
            tabArea.setVisibility(View.VISIBLE);
            fragmentList.add(generateBookingFrag(Utils.Upcoming));
            fragmentList.add(generateOrderFrag(Utils.Past));
        } else {
            titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_BOOKING"),};
            tabArea.setVisibility(View.GONE);
            fragmentList.add(generateBookingFrag(Utils.Past));
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        appLogin_view_pager.setAdapter(adapter);
        material_tabs.setupWithViewPager(appLogin_view_pager);

        if (isOrder) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", getIntent().getStringExtra("iOrderId"));
            new StartActProcess(getActContext()).startActWithData(TrackOrderActivity.class, bn);
        }


        appLogin_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logger.d("onPageScrolled", "::" + position);

            }

            @Override
            public void onPageSelected(int position) {
                selTabPos = position;
                Logger.d("onPageScrolled", "::" + "onPageSelected");
                fragmentList.get(position).onResume();
                selFilterType = "";
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Logger.d("onPageScrolled", "::" + "onPageScrollStateChanged");
            }
        });
    }

    public void focusFragment(int pos)
    {
        appLogin_view_pager.setCurrentItem(1);
    }
    private Fragment generateOrderFrag(String past) {
        orderFrag = new OrderFragment();
        Bundle bn = new Bundle();
        bn.putString("HISTORY_TYPE", "DisplayActiveOrder");
        orderFrag.setArguments(bn);
        return orderFrag;
    }

    private Fragment generateBookingFrag(String upcoming) {
        frag = new NewBookingFragment();
        Bundle bn = new Bundle();
        bn.putString("HISTORY_TYPE", "getMemberBookings");
        frag.setArguments(bn);

        return frag;
    }

    public NewBookingFragment getNewBookingFrag() {

        if (frag != null) {
            return frag;
        }
        return null;
    }


    public OrderFragment getOrderFrag() {

        if (orderFrag != null) {
            return orderFrag;
        }
        return null;
    }


    public void filterManage(ArrayList<HashMap<String, String>> filterlist) {
        this.filterlist = filterlist;
        if (filterlist.size()>0 && filterlist.size() > 0 && (appLogin_view_pager!=null &&appLogin_view_pager.getCurrentItem()==0))
        {
            filterImageview.setVisibility(View.VISIBLE);
        }else
        {
            filterImageview.setVisibility(View.GONE);
        }
    }


    public void subFilterManage(ArrayList<HashMap<String, String>> filterlist, String type) {

        if (type.equalsIgnoreCase("Order")) {
            this.orderSubFilterlist = filterlist;
        } else {
            this.subFilterlist = filterlist;
        }
    }

    private void setLabels() {
        String menuMsgYourTrips = "";
        if (app_type.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
            menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_TRIPS");
        } else if (app_type.equalsIgnoreCase("Delivery")) {
            menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_DELIVERY");
        } else if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
            menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_BOOKING");
        } else {
            menuMsgYourTrips = generalFunc.retrieveLangLBl("", "LBL_YOUR_BOOKING");
        }
        titleTxt.setText(menuMsgYourTrips);
    }

    @Override
    protected void onResume() {

        app_type = generalFunc.getJsonValue("APP_TYPE", userProfileJson);
        super.onResume();


    }

    public Context getActContext() {
        return BookingActivity.this;
    }

    @Override
    public void onBackPressed() {


        if (isOrder)
        {
            MyApp.getInstance().restartWithGetDataApp();
        }
        else if (isRestart) {
            if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {


                if ((generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("Active") ||
                        generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValue("eType", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

//                OnGoingTripsActivity.super.onBackPressed();
                    if (getIntent().hasExtra("isTripRunning")) {
                        MyApp.getInstance().restartWithGetDataApp();
                    }

                } else if (generalFunc.prefHasKey(Utils.isMultiTrackRunning) && generalFunc.retrieveValue(Utils.isMultiTrackRunning).equalsIgnoreCase("Yes")) {
                    MyApp.getInstance().restartWithGetDataApp();
                } else if (getIntent().getBooleanExtra("isRestart", false)) {

                    Bundle bn = new Bundle();
                    new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                    finishAffinity();
                } else {

                    super.onBackPressed();
                }
            } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery) || generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_Deliver)) {


                if ((generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("Active") ||
                        generalFunc.getJsonValue("vTripStatus", userProfileJson).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValue("eType", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

//                OnGoingTripsActivity.super.onBackPressed();
                    if (getIntent().hasExtra("isTripRunning")) {
                        MyApp.getInstance().restartWithGetDataApp();
                    }

                } else if (generalFunc.prefHasKey(Utils.isMultiTrackRunning) && generalFunc.retrieveValue(Utils.isMultiTrackRunning).equalsIgnoreCase("Yes")) {
                    MyApp.getInstance().restartWithGetDataApp();
                } else if (getIntent().getBooleanExtra("isRestart", false)) {

                    Bundle bn = new Bundle();
                    bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                    bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));
                    new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                    finishAffinity();
                } else {

                    super.onBackPressed();
                }
            }
            //  MyApp.getInstance().restartWithGetDataApp();
        }else  if (isrestart) {
            Bundle bn = new Bundle();

            if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equals(Utils.CabGeneralType_UberX)) {
                new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("Delivery")) {
                bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));


                if (generalFunc.getJsonValue("PACKAGE_TYPE", userProfileJson).equalsIgnoreCase("STANDARD")) {
                    new StartActProcess(getActContext()).startActWithData(MainActivity.class, bn);
                } else {
                    new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                }

            } else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
                bn.putString("iVehicleCategoryId", generalFunc.getJsonValue("DELIVERY_CATEGORY_ID", userProfileJson));
                bn.putString("vCategory", generalFunc.getJsonValue("DELIVERY_CATEGORY_NAME", userProfileJson));
                new StartActProcess(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
            } else {
                if (getIntent().getStringExtra("selType") != null) {
                    bn.putString("selType", getIntent().getStringExtra("selType"));
                    new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                } else {
                    new StartActProcess(getActContext()).startActWithData(MainActivity.class, bn);
                }

            }
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    onBackPressed();
                    break;

                case R.id.filterImageview:
                    BuildType("Normal");
                    break;


            }
        }
    }


    public void BuildType(String type) {
        ArrayList<String> arrayList = populateSubArrayList(type);

        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("Select Type", "LBL_SELECT_TYPE"), arrayList, OpenListView.OpenDirection.BOTTOM, true, true, position -> {
            if (type.equalsIgnoreCase("Order")) {
                orderSubFilterPosition = position;
                selOrderSubFilterType = orderSubFilterlist.get(position).get("vSubFilterParam");
                getOrderFrag().filterTxt.setText(orderSubFilterlist.get(position).get("vTitle"));
            } else if (type.equalsIgnoreCase("History")) {
                subFilterPosition = position;
                selSubFilterType = subFilterlist.get(position).get("vSubFilterParam");
                getNewBookingFrag().filterTxt.setText(subFilterlist.get(position).get("vTitle"));
            } else {
                filterPosition = position;
                selFilterType = filterlist.get(position).get("vFilterParam");
            }
            fragmentList.get(appLogin_view_pager.getCurrentItem()).onResume();

        }).show(populatePos(type), "vTitle");
    }

    private ArrayList<String> populateSubArrayList(String BuildType) {
        ArrayList<String> typeNameList = new ArrayList<>();
        ArrayList<HashMap<String, String>> filterArrayList = BuildType.equalsIgnoreCase("Order") ? orderSubFilterlist : (BuildType.equalsIgnoreCase("History") ? subFilterlist : filterlist);

        if (filterArrayList != null && filterArrayList.size() > 0) {
            for (int i = 0; i < filterArrayList.size(); i++) {
                typeNameList.add((filterArrayList.get(i).get("vTitle")));
            }
        }

        return typeNameList;
    }

    private int populatePos(String BuildType) {
        return BuildType.equalsIgnoreCase("Order") ? orderSubFilterPosition : (BuildType.equalsIgnoreCase("History") ? subFilterPosition : filterPosition);
    }

}
