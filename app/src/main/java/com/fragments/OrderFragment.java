package com.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.adapter.files.deliverAll.ActiveOrderAdapter;
import com.melevicarbrasil.usuario.BookingActivity;
import com.melevicarbrasil.usuario.CommonDeliveryTypeSelectionActivity;
import com.melevicarbrasil.usuario.Help_MainCategory;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.UberXHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.FoodDeliveryHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.OrderDetailsActivity;
import com.melevicarbrasil.usuario.deliverAll.ServiceHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.TrackOrderActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.CommonUtilities;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements ActiveOrderAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    View view;
    ProgressBar loading_ride_history;
    MTextView noRidesTxt;
    RecyclerView activeRecyclerView;
    ErrorView errorView;
    ActiveOrderAdapter activeOrderAdapter;
    ArrayList<HashMap<String, String>> list;
    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;
    String next_page_str = "";
    GeneralFunctions generalFunc;

    String userProfileJson = "";
    int HISTORYDETAILS = 1;
    String APP_TYPE;
    ArrayList<HashMap<String, String>> filterlist = new ArrayList<>();
    ArrayList<HashMap<String, String>> orderFilterlist = new ArrayList<>();
    private LinearLayout filterArea;
    public MTextView filterTxt;

    BookingActivity bookingAct;
    MyBookingFragment myBookingFragment;
    private OrderFragment orderFragment;

    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking, container, false);

        loading_ride_history = (ProgressBar) view.findViewById(R.id.loading_my_bookings);
        noRidesTxt = (MTextView) view.findViewById(R.id.noRidesTxt);
        activeRecyclerView = (RecyclerView) view.findViewById(R.id.myBookingsRecyclerView);
        errorView = (ErrorView) view.findViewById(R.id.errorView);

        if (getActivity() instanceof UberXHomeActivity) {
            myBookingFragment = ((UberXHomeActivity) getActivity()).myBookingFragment;
            orderFragment = ((MyBookingFragment) myBookingFragment).getOrderFrag();
        } else if (getActivity() instanceof CommonDeliveryTypeSelectionActivity) {
            myBookingFragment = ((CommonDeliveryTypeSelectionActivity) getActivity()).myBookingFragment;
            orderFragment = ((MyBookingFragment) myBookingFragment).getOrderFrag();
        } else if (getActivity() instanceof FoodDeliveryHomeActivity) {
            myBookingFragment = ((FoodDeliveryHomeActivity) getActivity()).myBookingFragment;
            orderFragment = ((MyBookingFragment) myBookingFragment).getOrderFrag();
        } else if (getActivity() instanceof ServiceHomeActivity) {
            myBookingFragment = ((ServiceHomeActivity) getActivity()).myBookingFragment;
            orderFragment = ((MyBookingFragment) myBookingFragment).getOrderFrag();
        } else {
            bookingAct = (BookingActivity) getActivity();
            orderFragment = bookingAct.getOrderFrag();
        }

        filterTxt = (MTextView) view.findViewById(R.id.filterTxt);

        filterArea = (LinearLayout) view.findViewById(R.id.filterArea);

        filterArea.setOnClickListener(new setOnClickList());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        generalFunc = MyApp.getInstance().getGeneralFun(getActivity());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        APP_TYPE = generalFunc.getJsonValue("APP_TYPE", userProfileJson);

        list = new ArrayList<>();
        activeOrderAdapter = new ActiveOrderAdapter(getActivity(), list, generalFunc, false);
        activeRecyclerView.setAdapter(activeOrderAdapter);
        activeOrderAdapter.setOnItemClickListener(this);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {


            if (v.getChildAt(v.getChildCount() - 1) != null) {

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {

                    int visibleItemCount = activeRecyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = activeRecyclerView.getLayoutManager().getItemCount();
                    int firstVisibleItemPosition = ((LinearLayoutManager) activeRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    int lastInScreen = firstVisibleItemPosition + visibleItemCount;
//                    Logger.d("SIZEOFLIST", "::" + lastInScreen + "::" + totalItemCount + "::" + isNextPageAvailable);
                    if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable) {
                        mIsLoading = true;
                        activeOrderAdapter.addFooterView();
                        getActiveOrderHistory(true);
                    } else if (!isNextPageAvailable) {
                        activeOrderAdapter.removeFooterView();
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        getActiveOrderHistory(false);
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActivity());
            switch (view.getId()) {

                case R.id.filterArea:
                    if (myBookingFragment != null) {
                        myBookingFragment.BuildType("Order");
                    } else {
                        bookingAct.BuildType("Order");
                    }

                    break;

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (myBookingFragment != null) {
            myBookingFragment.filterImageview.setVisibility(View.GONE);
        } else {
            bookingAct.filterImageview.setVisibility(View.GONE);
        }


        getActiveOrderHistory(false);


    }

    public boolean isDeliver() {
        if (getArguments().getString("HISTORY_TYPE").equals(Utils.CabGeneralType_Deliver)) {
            return true;
        }
        return false;
    }


    public void getActiveOrderHistory(final boolean isLoadMore) {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading_ride_history.getVisibility() != View.VISIBLE && isLoadMore == false) {
            loading_ride_history.setVisibility(View.VISIBLE);
            filterArea.setVisibility(View.GONE);
        }

        if (!isLoadMore) {
            removeNextPageConfig();
            list.clear();
            activeOrderAdapter.notifyDataSetChanged();

        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DisplayActiveOrder");
        parameters.put("UserType", Utils.userType);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eSystem", Utils.eSystem_Type);

        if (myBookingFragment != null) {
            parameters.put("vSubFilterParam", myBookingFragment.selOrderSubFilterType);

        } else {
            parameters.put("vSubFilterParam", bookingAct.selOrderSubFilterType);

        }
        if (isLoadMore) {
            parameters.put("page", next_page_str);
        }

        noRidesTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActivity(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noRidesTxt.setVisibility(View.GONE);
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            swipeRefreshLayout.setRefreshing(false);

            if (responseObj != null && !responseObj.equals("")) {

                closeLoader();

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseObj)) {

                    String nextPage = generalFunc.getJsonValueStr("NextPage", responseObj);

                    JSONArray message_arr = generalFunc.getJsonArray(Utils.message_str, responseObj);

                    if (message_arr != null && message_arr.length() > 0) {
                        int user_profile_icon_size_main = 0;
                        String LBL_HISTORY_REST_DELIVERED = "", LBL_REFUNDED = "", LBL_HISTORY_REST_CANCELLED = "", LBL_TOTAL_TXT = "", LBL_ORDER_AT = "", LBL_HELP = "", LBL_VIEW_DETAILS = "", LBL_TRACK_ORDER = "";
                        if (message_arr.length() > 0) {
                            user_profile_icon_size_main = getActivity().getResources().getDimensionPixelSize(R.dimen.dimen_50);
                            LBL_HISTORY_REST_DELIVERED = generalFunc.retrieveLangLBl("", "LBL_HISTORY_REST_DELIVERED");
                            LBL_REFUNDED = generalFunc.retrieveLangLBl("", "LBL_REFUNDED");
                            LBL_HISTORY_REST_CANCELLED = generalFunc.retrieveLangLBl("", "LBL_HISTORY_REST_CANCELLED");
                            LBL_TOTAL_TXT = generalFunc.retrieveLangLBl("", "LBL_TOTAL_TXT");
                            LBL_ORDER_AT = generalFunc.retrieveLangLBl("", "LBL_ORDER_AT");
                            LBL_HELP = generalFunc.retrieveLangLBl("", "LBL_HELP");
                            LBL_VIEW_DETAILS = generalFunc.retrieveLangLBl("", "LBL_VIEW_DETAILS");
                            LBL_TRACK_ORDER = generalFunc.retrieveLangLBl("", "LBL_TRACK_ORDER");

                        }
                        for (int i = 0; i < message_arr.length(); i++) {
                            JSONObject addr_obj = generalFunc.getJsonObject(message_arr, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("vCompany", generalFunc.getJsonValueStr("vCompany", addr_obj));
                            map.put("vRestuarantLocation", generalFunc.getJsonValueStr("vRestuarantLocation", addr_obj));
                            map.put("iOrderId", generalFunc.getJsonValueStr("iOrderId", addr_obj));
                            map.put("vOrderNo", generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vOrderNo", addr_obj)));

                            String tOrderRequestDate = generalFunc.getJsonValueStr("tOrderRequestDate", addr_obj);


                            try {

                                map.put("ConvertedOrderRequestDate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(tOrderRequestDate, Utils.OriginalDateFormate, CommonUtilities.OriginalDateFormate)));
                                map.put("ConvertedOrderRequestTime", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(tOrderRequestDate, Utils.OriginalDateFormate, CommonUtilities.OriginalTimeFormate)));
                            } catch (Exception e) {
                                e.printStackTrace();
                                map.put("ConvertedOrderRequestDate", "");
                                map.put("ConvertedOrderRequestTime", "");
                            }

                            map.put("vService_BG_color", generalFunc.getJsonValueStr("vService_BG_color", addr_obj));
                            map.put("vService_TEXT_color", generalFunc.getJsonValueStr("vService_TEXT_color", addr_obj));

                            map.put("fNetTotal", generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("fNetTotal", addr_obj)));
                            map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", addr_obj));
                            map.put("vStatus", generalFunc.getJsonValueStr("vStatus", addr_obj));
                            map.put("iStatusCode", generalFunc.getJsonValueStr("iStatusCode", addr_obj));
                            map.put("DisplayLiveTrack", generalFunc.getJsonValueStr("DisplayLiveTrack", addr_obj));
                            map.put("vServiceCategoryName", generalFunc.getJsonValueStr("vServiceCategoryName", addr_obj));
                            map.put("vOrderStatus", generalFunc.getJsonValueStr("vOrderStatus", addr_obj));
                            map.put("vImage", Utilities.getResizeImgURL(getActivity(), generalFunc.getJsonValueStr("vImage", addr_obj), user_profile_icon_size_main, user_profile_icon_size_main));

                            map.put("LBL_HISTORY_REST_DELIVERED", LBL_HISTORY_REST_DELIVERED);
                            map.put("LBL_REFUNDED", LBL_REFUNDED);
                            map.put("LBL_HISTORY_REST_CANCELLED", LBL_HISTORY_REST_CANCELLED);
                            map.put("LBL_TOTAL_TXT", LBL_TOTAL_TXT);
                            map.put("LBL_ORDER_AT", LBL_ORDER_AT);
                            map.put("LBL_HELP", LBL_HELP);
                            map.put("LBL_VIEW_DETAILS", LBL_VIEW_DETAILS);
                            map.put("LBL_TRACK_ORDER", LBL_TRACK_ORDER);

                            list.add(map);
                        }
                    }



                   /* JSONArray arr_type_filter = generalFunc.getJsonArray("AppTypeFilterArr", responseObj);

                    if (arr_type_filter != null && arr_type_filter.length() > 0) {
                        filterlist = new ArrayList<>();
                        for (int i = 0; i < arr_type_filter.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_type_filter, i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            String vTitle = generalFunc.getJsonValueStr("vTitle", obj_temp);
                            map.put("vTitle", vTitle);
                            String vFilterParam = generalFunc.getJsonValueStr("vFilterParam", obj_temp);
                            map.put("vFilterParam", vFilterParam);

                           *//* if (vFilterParam.equalsIgnoreCase(eFilterSel)) {
                                bookingAct.selFilterType = vFilterParam;
                                bookingAct.filterPosition = i;
                            }*//*
                            filterlist.add(map);


                        }

                        bookingAct.filterManage(filterlist);
                    }*/
                    buildFilterTypes(responseObj);


                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }

                    activeOrderAdapter.notifyDataSetChanged();

                } else {
                    buildFilterTypes(responseObj);
                    if (list.size() == 0) {
                        removeNextPageConfig();
                        noRidesTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseObj)));
                        noRidesTxt.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (isLoadMore == false) {
                    buildFilterTypes(responseObj);
                    removeNextPageConfig();
                    generateErrorView();
                }

            }
            filterArea.setVisibility(View.VISIBLE);
            mIsLoading = false;
        });
        exeWebServer.execute();
    }

    private void buildFilterTypes(JSONObject responseObj) {
        if (responseObj == null)
            return;
        JSONArray orderStatusFilterArr = generalFunc.getJsonArray("subFilterOption", responseObj);
        String eFilterSel = generalFunc.getJsonValueStr("eFilterSel", responseObj);

        if (orderStatusFilterArr != null && orderStatusFilterArr.length() > 0) {
            orderFilterlist = new ArrayList<>();
            for (int i = 0; i < orderStatusFilterArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(orderStatusFilterArr, i);
                HashMap<String, String> map = new HashMap<String, String>();
                String vTitle = generalFunc.getJsonValueStr("vTitle", obj_temp);
                map.put("vTitle", vTitle);
                String vSubFilterParam = generalFunc.getJsonValueStr("vSubFilterParam", obj_temp);
                map.put("vSubFilterParam", vSubFilterParam);

                if (vSubFilterParam.equalsIgnoreCase(eFilterSel)) {

                    if (myBookingFragment != null) {
                        myBookingFragment.selOrderSubFilterType = vSubFilterParam;
                        myBookingFragment.orderSubFilterPosition = i;

                    } else {
                        bookingAct.selOrderSubFilterType = vSubFilterParam;
                        bookingAct.orderSubFilterPosition = i;

                    }


                    filterTxt.setText(vTitle);
                }

                orderFilterlist.add(map);
            }

            if (myBookingFragment != null) {
                myBookingFragment.subFilterManage(orderFilterlist, "Order");
            } else {
                bookingAct.subFilterManage(orderFilterlist, "Order");

            }

        }

    }

    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        activeOrderAdapter.removeFooterView();
    }

    public void closeLoader() {
        if (loading_ride_history.getVisibility() == View.VISIBLE) {
            loading_ride_history.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getActiveOrderHistory(false));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            next_page_str = "2";
            userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            list.clear();
            getActiveOrderHistory(true);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActivity());
    }

    @Override
    public void onItemClickList(View v, int position, String isSelect) {
        if (isSelect.equalsIgnoreCase("help")) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", list.get(position).get("iOrderId"));
            new StartActProcess(getActivity()).startActWithData(Help_MainCategory.class, bn);
        } else if (isSelect.equalsIgnoreCase("track")) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", list.get(position).get("iOrderId"));
            new StartActProcess(getActivity()).startActWithData(TrackOrderActivity.class, bn);
        } else if (isSelect.equalsIgnoreCase("view")) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", list.get(position).get("iOrderId"));
            new StartActProcess(getActivity()).startActWithData(OrderDetailsActivity.class, bn);
        }
    }


}
