package com.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.files.ViewPagerAdapter;
import com.melevicarbrasil.usuario.R;
import com.dialogs.OpenListView;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyBookingFragment extends Fragment {

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

    View view;
    ViewPagerAdapter adapter;
    TabLayout material_tabs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.acitity_booking, container, false);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        titleTxt = (MTextView) view.findViewById(R.id.titleTxt);
        backImgView = (ImageView) view.findViewById(R.id.backImgView);
        backImgView.setVisibility(View.GONE);
        filterImageview = (ImageView) view.findViewById(R.id.filterImageview);
        filterImageview.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        app_type = generalFunc.getJsonValue("APP_TYPE", userProfileJson);
        setLabels();

        appLogin_view_pager = (ViewPager) view.findViewById(R.id.appLogin_view_pager);
        material_tabs = (TabLayout) view.findViewById(R.id.material_tabs);
        LinearLayout tabArea = (LinearLayout) view.findViewById(R.id.tabArea);


        boolean isDeliverOnlyEnabled = generalFunc.isDeliverOnlyEnabled();
        boolean isAnyDeliverOptionEnabled = generalFunc.isAnyDeliverOptionEnabled();
        if (isDeliverOnlyEnabled) {
            titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_ORDERS_TXT"),};
            tabArea.setVisibility(View.GONE);
            fragmentList.add(generateOrderFrag(Utils.Past));
        } else if (isAnyDeliverOptionEnabled) {
            titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_BOOKING"), generalFunc.retrieveLangLBl("", "LBL_ORDERS_TXT")};
            tabArea.setVisibility(View.VISIBLE);
            fragmentList.add(generateBookingFrag(Utils.Upcoming));
            fragmentList.add(generateOrderFrag(Utils.Past));
        } else {
            titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_BOOKING"),};
            tabArea.setVisibility(View.GONE);
            fragmentList.add(generateBookingFrag(Utils.Past));
        }

        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), titles, fragmentList);
        appLogin_view_pager.setAdapter(adapter);
        material_tabs.setupWithViewPager(appLogin_view_pager);


        appLogin_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Logger.d("onPageScrolled", "::" + position);

            }

            @Override
            public void onPageSelected(int position) {
                selTabPos = position;
//                Logger.d("onPageScrolled", "::" + "onPageSelected" + position);
                fragmentList.get(position).onResume();
                selFilterType = "";

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Logger.d("onPageScrolled", "::" + "onPageScrollStateChanged");
            }

        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("bookingfrag", "onResume: ");
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
        if (getActivity() != null && filterlist.size() > 0 && (appLogin_view_pager!=null &&appLogin_view_pager.getCurrentItem()==0)) {
            filterImageview.setVisibility(View.VISIBLE);
        } else {
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


    public Context getActContext() {
        return getActivity();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.filterImageview:
                    BuildType("Normal");
                    break;
                case R.id.backImgView:
                    getActivity().onBackPressed();
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
