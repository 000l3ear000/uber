package com.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.adapter.files.AllHistoryRecycleAdapter;
import com.melevicarbrasil.usuario.BookingActivity;
import com.melevicarbrasil.usuario.CommonDeliveryTypeSelectionActivity;
import com.melevicarbrasil.usuario.HistoryDetailActivity;
import com.melevicarbrasil.usuario.MoreServiceInfoActivity;
import com.melevicarbrasil.usuario.OnGoingTripDetailsActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.ScheduleDateSelectActivity;
import com.melevicarbrasil.usuario.UberXHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.FoodDeliveryHomeActivity;
import com.melevicarbrasil.usuario.deliverAll.ServiceHomeActivity;
import com.datepicker.files.SlideDateTimeListener;
import com.datepicker.files.SlideDateTimePicker;
import com.dialogs.OpenListView;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.ErrorView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewBookingFragment extends Fragment implements AllHistoryRecycleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    View view;
    ProgressBar loading_ride_history;
    MTextView noRidesTxt;
    public MTextView filterTxt;
    RecyclerView historyRecyclerView;
    ErrorView errorView;
    AllHistoryRecycleAdapter historyRecyclerAdapter;
    ArrayList<HashMap<String, String>> list;
    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;
    String next_page_str = "";
    GeneralFunctions generalFunc;
    String userProfileJson = "";
    int HISTORYDETAILS = 1;
    String APP_TYPE;
    ArrayList<HashMap<String, String>> filterlist = new ArrayList<>();
    ArrayList<HashMap<String, String>> subFilterlist = new ArrayList<>();
    private LinearLayout filterArea;

    String selectedDateTime = "";
    String selectedDateTimeZone = "";
    int pos = 0;

    androidx.appcompat.app.AlertDialog alertDialog_surgeConfirm;
    AlertDialog dialog_declineOrder;
    String titleDailog = "";
    private NewBookingFragment newBookingFragment;
    String eFilterSel = "";
    BookingActivity bookingAct;
    MyBookingFragment myBookingFragment;

    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    String tripdataPage = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking, container, false);

        loading_ride_history = (ProgressBar) view.findViewById(R.id.loading_my_bookings);
        noRidesTxt = (MTextView) view.findViewById(R.id.noRidesTxt);
        filterTxt = (MTextView) view.findViewById(R.id.filterTxt);
        historyRecyclerView = (RecyclerView) view.findViewById(R.id.myBookingsRecyclerView);
        errorView = (ErrorView) view.findViewById(R.id.errorView);

        if (getActivity() instanceof BookingActivity) {
            bookingAct = (BookingActivity) getActivity();
            newBookingFragment = bookingAct.getNewBookingFrag();
        } else {
            if (getActivity() instanceof UberXHomeActivity) {
                myBookingFragment = ((UberXHomeActivity) getActivity()).myBookingFragment;
                newBookingFragment = myBookingFragment.getNewBookingFrag();
            } else if (getActivity() instanceof ServiceHomeActivity) {
                myBookingFragment = ((ServiceHomeActivity) getActivity()).myBookingFragment;
                newBookingFragment = myBookingFragment.getNewBookingFrag();
            } else if (getActivity() instanceof CommonDeliveryTypeSelectionActivity) {
                myBookingFragment = ((CommonDeliveryTypeSelectionActivity) getActivity()).myBookingFragment;
            } else if (getActivity() instanceof CommonDeliveryTypeSelectionActivity) {
                myBookingFragment = ((CommonDeliveryTypeSelectionActivity) getActivity()).myBookingFragment;
                newBookingFragment = myBookingFragment.getNewBookingFrag();
            } else if (getActivity() instanceof FoodDeliveryHomeActivity) {
                myBookingFragment = ((FoodDeliveryHomeActivity) getActivity()).myBookingFragment;
                newBookingFragment = myBookingFragment.getNewBookingFrag();
            } else if (getActivity() instanceof ServiceHomeActivity) {
                myBookingFragment = ((ServiceHomeActivity) getActivity()).myBookingFragment;
                newBookingFragment = myBookingFragment.getNewBookingFrag();
            }
        }


        generalFunc = MyApp.getInstance().getGeneralFun(getActivity());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        APP_TYPE = generalFunc.getJsonValue("APP_TYPE", userProfileJson);

        filterArea = (LinearLayout) view.findViewById(R.id.filterArea);
        filterArea.setOnClickListener(new setOnClickList());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        list = new ArrayList<>();
        historyRecyclerAdapter = new AllHistoryRecycleAdapter(getActivity(), list, generalFunc, false);
        historyRecyclerView.setAdapter(historyRecyclerAdapter);
        historyRecyclerAdapter.setOnItemClickListener(this);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (v.getChildAt(v.getChildCount() - 1) != null) {

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {

                    int visibleItemCount = historyRecyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = historyRecyclerView.getLayoutManager().getItemCount();
                    int firstVisibleItemPosition = ((LinearLayoutManager) historyRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    int lastInScreen = firstVisibleItemPosition + visibleItemCount;
                    Logger.d("SIZEOFLIST", "::" + lastInScreen + "::" + totalItemCount + "::" + isNextPageAvailable);
                    if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable) {
                        mIsLoading = true;
                        historyRecyclerAdapter.addFooterView();
                        historyRecyclerView.stopScroll();
                        getBookingsHistory(true);

                    } else if (!isNextPageAvailable) {
                        historyRecyclerAdapter.removeFooterView();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myBookingFragment != null) {
            myBookingFragment.filterImageview.setVisibility(View.VISIBLE);
        } else {
            bookingAct.filterImageview.setVisibility(View.VISIBLE);
        }


        getBookingsHistory(false);

    }

    @Override
    public void onItemClickList(View v, int position, String type) {

        if (type.equalsIgnoreCase("ShowDetail")) {
            Utils.hideKeyboard(getActivity());
            Bundle bn = new Bundle();
            bn.putString("iTripId", list.get(position).get("iTripId"));
            bn.putString("iCabBookingId", list.get(position).get("iCabBookingId"));
            new StartActProcess(getActivity()).startActForResult(HistoryDetailActivity.class, bn, HISTORYDETAILS);
        } else if (type.equalsIgnoreCase("ReSchedule")) {
            chooseDateTime(list.get(position).get("iCabBookingId"));
        } else if (type.equalsIgnoreCase("ViewRequestedServices")) {
            Bundle bundle = new Bundle();
            bundle.putString("iCabBookingId", list.get(position).get("iCabBookingId"));
            bundle.putString("iDriverId", list.get(position).get("iDriverId"));
            new StartActProcess(getActivity()).startActWithData(MoreServiceInfoActivity.class, bundle);
        } else if (type.equalsIgnoreCase("ReBooking")) {
            rescheduleBooking(position);
        } else if (type.equalsIgnoreCase("CancelBooking")) {
            pos = position;
            getDeclineReasonsList();
        } else if (type.equalsIgnoreCase("ViewCancelReason")) {
            generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_BOOKING_CANCEL_REASON"), list.get(position).get("vCancelReason"));
        } else if (type.equalsIgnoreCase("ViewDetail")) {
            Bundle bn = new Bundle();
            bn.putSerializable("TripDetail", list.get(position));
            bn.putSerializable("iTripId", list.get(position).get("iTripId"));
            bn.putSerializable("driverStatus", list.get(position).get("driverStatus"));
            new StartActProcess(getActivity()).startActForResult(OnGoingTripDetailsActivity.class, bn, Utils.LIVE_TRACK_REQUEST_CODE);
        } else if (type.equalsIgnoreCase("LiveTrack")) {
            MyApp.getInstance().restartWithGetDataApp(list.get(position).get("iTripId"));
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        getBookingsHistory(false);

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActivity());
            switch (view.getId()) {

                case R.id.filterArea:

                    if (myBookingFragment != null) {
                        myBookingFragment.BuildType("History");
                    } else {
                        bookingAct.BuildType("History");
                    }

                    break;

            }
        }
    }


    public void rescheduleBooking(int position) {
        HashMap<String, String> posData = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("SelectedVehicleTypeId", posData.get("SelectedCategoryId"));
        bundle.putBoolean("isufx", true);
        bundle.putString("latitude", posData.get("vLatitude"));
        bundle.putString("longitude", posData.get("vLongitude"));
//        bundle.putString("address", posData.get("vSourceAddresss"));
        bundle.putString("address", posData.get("tSaddress"));
        bundle.putString("SelectDate", posData.get("selecteddatetime"));
        bundle.putString("SelectvVehicleType", posData.get("SelectedVehicle"));
        String SelectedPrice = posData.get("SelectedPrice");

        bundle.putString("SelectvVehiclePrice", SelectedPrice);
        bundle.putString("iUserAddressId", posData.get("iUserAddressId"));
        bundle.putString("type", Utils.CabReqType_Later);
        bundle.putString("Sdate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(posData.get("dBooking_dateOrig"), Utils.OriginalDateFormate, Utils.dateFormateForBooking)));
        bundle.putString("Stime", posData.get("selectedtime"));


        if (posData.get("SelectedAllowQty").equalsIgnoreCase("yes")) {

            bundle.putString("Quantity", posData.get("iQty"));
            bundle.putString("Quantityprice", posData.get("SelectedCurrencySymbol") + "" + (GeneralFunctions.parseIntegerValue(1, posData.get("iQty"))) * (GeneralFunctions.parseIntegerValue(1, SelectedPrice)) + "");
        } else {
            bundle.putString("Quantityprice", posData.get("SelectedCurrencySymbol") + "" + SelectedPrice);
            bundle.putString("Quantity", "0");
        }

        bundle.putString("iCabBookingId", posData.get("iCabBookingId"));
        bundle.putBoolean("isRebooking", true);
        bundle.putString("iDriverId", posData.get("iDriverId"));
        bundle.putBoolean("isList", true);
        new StartActProcess(getActivity()).startActForResult(newBookingFragment, ScheduleDateSelectActivity.class, Utils.SCHEDULE_REQUEST_CODE, bundle);
    }


    public void getDeclineReasonsList() {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("type", "GetCancelReasons");
        parameters.put("iCabBookingId", list.get(pos).get("iCabBookingId"));
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("eUserType", Utils.app_type);

        ExecuteWebServerUrl exeServerTask = new ExecuteWebServerUrl(getActivity(), parameters);
        exeServerTask.setLoaderConfig(getActivity(), true, generalFunc);
        exeServerTask.setDataResponseListener(responseString -> {

            if (!responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    showDeclineReasonsAlert(responseString);
                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }

            } else {
                generalFunc.showError();
            }

        });
        exeServerTask.execute();
    }
    int selCurrentPosition= -1;
    public void showDeclineReasonsAlert(String responseString) {
        selCurrentPosition= -1;
        String eType = list.get(pos).get("eType");

        if (eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
            titleDailog = generalFunc.retrieveLangLBl("", "LBL_CANCEL_TRIP");
        } else if (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
            titleDailog = generalFunc.retrieveLangLBl("", "LBL_CANCEL_BOOKING");
        } else {
            titleDailog = generalFunc.retrieveLangLBl("", "LBL_CANCEL_DELIVERY");
        }


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        // builder.setTitle(titleDailog);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.decline_order_dialog_design, null);
        builder.setView(dialogView);

        MaterialEditText reasonBox = (MaterialEditText) dialogView.findViewById(R.id.inputBox);
        RelativeLayout commentArea = (RelativeLayout) dialogView.findViewById(R.id.commentArea);
        reasonBox.setHideUnderline(true);
        if (generalFunc.isRTLmode()) {
            reasonBox.setPaddings(0, 0, (int) getResources().getDimension(R.dimen._10sdp), 0);
        } else {
            reasonBox.setPaddings((int) getResources().getDimension(R.dimen._10sdp), 0, 0, 0);
        }

        reasonBox.setSingleLine(false);
        reasonBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        reasonBox.setGravity(Gravity.TOP);
        if (generalFunc.isRTLmode()) {
            reasonBox.setPaddings(0, 0, (int) getResources().getDimension(R.dimen._10sdp),0 );
        } else {
            reasonBox.setPaddings((int) getResources().getDimension(R.dimen._10sdp), 0, 0, 0);
        }
        reasonBox.setVisibility(View.GONE);
        commentArea.setVisibility(View.GONE);
        new CreateRoundedView(Color.parseColor("#ffffff"), 5, 1, Color.parseColor("#C5C3C3"), commentArea);
        reasonBox.setBothText("", generalFunc.retrieveLangLBl("", "LBL_ENTER_REASON"));


        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
      //  HashMap<String, String> map = new HashMap<>();
       // map.put("title", "-- " + generalFunc.retrieveLangLBl("Select Reason", "LBL_SELECT_CANCEL_REASON") + " --");
       // map.put("id", "");
      //  list.add(map);
        JSONArray arr_msg = generalFunc.getJsonArray(Utils.message_str, responseString);
        if (arr_msg != null) {

            for (int i = 0; i < arr_msg.length(); i++) {

                JSONObject obj_tmp = generalFunc.getJsonObject(arr_msg, i);


                HashMap<String, String> datamap = new HashMap<>();
                datamap.put("title", generalFunc.getJsonValueStr("vTitle", obj_tmp));
                datamap.put("id", generalFunc.getJsonValueStr("iCancelReasonId", obj_tmp));
                list.add(datamap);
            }

            HashMap<String, String> othermap = new HashMap<>();
            othermap.put("title", generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT"));
            othermap.put("id", "");
            list.add(othermap);

          //  AppCompatSpinner spinner = (AppCompatSpinner) dialogView.findViewById(R.id.declineReasonsSpinner);

            MTextView cancelTxt = (MTextView) dialogView.findViewById(R.id.cancelTxt);
            MTextView submitTxt = (MTextView) dialogView.findViewById(R.id.submitTxt);
            MTextView subTitleTxt = (MTextView) dialogView.findViewById(R.id.subTitleTxt);
            ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelImg);
            subTitleTxt.setText(titleDailog);

            submitTxt.setText(generalFunc.retrieveLangLBl("", "LBL_YES"));
            cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO"));
            MTextView declinereasonBox = (MTextView)dialogView.findViewById(R.id.declinereasonBox) ;
            declinereasonBox.setText(generalFunc.retrieveLangLBl("Select Reason", "LBL_SELECT_CANCEL_REASON"));
            submitTxt.setClickable(false);
            submitTxt.setTextColor(getResources().getColor(R.color.gray_holo_light));


            submitTxt.setOnClickListener(v -> {


                if (selCurrentPosition == -1) {
                    return;
                }

                if (!Utils.checkText(reasonBox) && selCurrentPosition == (list.size() - 1)) {
                    reasonBox.setError(generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD_ERROR_TXT"));
                    return;
                }

                cancelBooking(this.list.get(pos).get("iCabBookingId"), list.get(selCurrentPosition).get("id"), reasonBox.getText().toString().trim());

                dialog_declineOrder.dismiss();

            });
            cancelTxt.setOnClickListener(v -> {
                Utils.hideKeyboard(getContext());
                dialog_declineOrder.dismiss();
            });

            cancelImg.setOnClickListener(v -> {
                Utils.hideKeyboard(getContext());
                dialog_declineOrder.dismiss();
            });


         /*   CustSpinnerAdapter adapter = new CustSpinnerAdapter(getActivity(), list);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinner.getSelectedItemPosition() == (list.size() - 1)) {
                        reasonBox.setVisibility(View.VISIBLE);
                        commentArea.setVisibility(View.VISIBLE);
                    } else if (spinner.getSelectedItemPosition() == 0) {
                        commentArea.setVisibility(View.GONE);
                        reasonBox.setVisibility(View.GONE);
                    } else {
                        commentArea.setVisibility(View.GONE);
                        reasonBox.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

            declinereasonBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenListView.getInstance(getActivity(), generalFunc.retrieveLangLBl("", "LBL_SELECT_REASON"), list, OpenListView.OpenDirection.CENTER, true, position -> {


                        selCurrentPosition = position;
                        HashMap<String, String> mapData = list.get(position);
                        declinereasonBox.setText(mapData.get("title"));
                        if (selCurrentPosition == (list.size() - 1)) {
                            reasonBox.setVisibility(View.VISIBLE);
                            commentArea.setVisibility(View.VISIBLE);
                        } else {
                            commentArea.setVisibility(View.GONE);
                            reasonBox.setVisibility(View.GONE);
                        }

                        submitTxt.setClickable(true);
                        submitTxt.setTextColor(getResources().getColor(R.color.white));



                    }).show(selCurrentPosition, "title");
                }
            });



            dialog_declineOrder = builder.create();
            dialog_declineOrder.setCancelable(false);
            dialog_declineOrder.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
            dialog_declineOrder.show();
          /*  dialog_declineOrder = builder.create();
            dialog_declineOrder.show();
            // dialog_declineOrder.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            dialog_declineOrder.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
            dialog_declineOrder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.gray));

            dialog_declineOrder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {


                if (spinner.getSelectedItemPosition() == 0) {
                    return;
                }

                if (!Utils.checkText(reasonBox) && spinner.getSelectedItemPosition() == (list.size() - 1)) {
                    reasonBox.setError(generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD_ERROR_TXT"));
                    return;
                }

                cancelBooking(this.list.get(pos).get("iCabBookingId"), list.get(spinner.getSelectedItemPosition()).get("id"), reasonBox.getText().toString().trim());

                dialog_declineOrder.dismiss();
            });

            dialog_declineOrder.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> dialog_declineOrder.dismiss());*/
        } else {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_NO_DATA_AVAIL"));
        }
    }

    public void cancelBooking(String iCabBookingId, String iCancelReasonId, String reason) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "cancelBooking");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("iCabBookingId", iCabBookingId);
        parameters.put("iCancelReasonId", iCancelReasonId);
        parameters.put("Reason", reason);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActivity(), parameters);
        exeWebServer.setLoaderConfig(getActivity(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    list.clear();
                    historyRecyclerAdapter.notifyDataSetChanged();
                    getBookingsHistory(false);
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void chooseDateTime(String iCabBookingId) {

        new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
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

                        String selectedTime = Utils.convertDateToFormat("yyyy-MM-dd HH:mm:ss", date);

                        updateBookingDate(iCabBookingId, selectedTime, "No");
                    }

                    @Override
                    public void onDateTimeCancel() {

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

    public void updateBookingDate(String iCabBookingId, String selDate, String eConfirmByUser) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "UpdateBookingDateRideDelivery");
        parameters.put("iCabBookingId", iCabBookingId);
        parameters.put("scheduleDate", selDate);
        parameters.put("eConfirmByUser", eConfirmByUser);
        parameters.put("iUserId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActivity(), parameters);
        exeWebServer.setLoaderConfig(getActivity(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            JSONObject responseObj = generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                if (isDataAvail) {
                    list.clear();
                    historyRecyclerAdapter.notifyDataSetChanged();
                    getBookingsHistory(false);


                } else {

                    if (generalFunc.getJsonValueStr("SurgePrice", responseObj) != null && !generalFunc.getJsonValueStr("SurgePrice", responseObj).equalsIgnoreCase("")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("");
                        builder.setCancelable(false);
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.surge_confirm_design, null);
                        builder.setView(dialogView);

                        MTextView payableAmountTxt;
                        MTextView payableTxt;

                        ((MTextView) dialogView.findViewById(R.id.headerMsgTxt)).setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseObj)));
                        ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("SurgePrice", responseObj)));

                        ((MTextView) dialogView.findViewById(R.id.tryLaterTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRY_LATER"));

                        payableTxt = (MTextView) dialogView.findViewById(R.id.payableTxt);
                        payableAmountTxt = (MTextView) dialogView.findViewById(R.id.payableAmountTxt);
                        payableTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYABLE_AMOUNT"));
                        payableAmountTxt.setVisibility(View.GONE);
                        payableTxt.setVisibility(View.VISIBLE);

                        String total_fare = generalFunc.getJsonValueStr("total_fare", responseObj);
                        if (total_fare != null && !total_fare.equalsIgnoreCase("")) {
                            payableAmountTxt.setVisibility(View.VISIBLE);
                            payableTxt.setVisibility(View.GONE);
                            payableAmountTxt.setText(generalFunc.retrieveLangLBl("Approx payable amount", "LBL_APPROX_PAY_AMOUNT") + ": " + total_fare);

                        }


                        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
                        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_SURGE"));
                        btn_type2.setId(Utils.generateViewId());

                        btn_type2.setOnClickListener(view -> {
                            updateBookingDate(iCabBookingId, selDate, "Yes");
                            alertDialog_surgeConfirm.dismiss();
                        });
                        (dialogView.findViewById(R.id.tryLaterTxt)).setOnClickListener(view -> alertDialog_surgeConfirm.dismiss());
                        alertDialog_surgeConfirm = builder.create();
                        alertDialog_surgeConfirm.setCancelable(false);
                        alertDialog_surgeConfirm.setCanceledOnTouchOutside(false);
                        if (generalFunc.isRTLmode()) {
                            generalFunc.forceRTLIfSupported(alertDialog_surgeConfirm);
                        }

                        alertDialog_surgeConfirm.show();


                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseObj)));
                    }
                }


            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public boolean isDeliver(String eType) {
        if (eType.equals(Utils.CabGeneralType_Deliver) || eType.equalsIgnoreCase("Deliver")) {
            return true;
        }
        return false;
    }


    public void getBookingsHistory(final boolean isLoadMore) {
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
            historyRecyclerAdapter.notifyDataSetChanged();

        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getMemberBookings");
        parameters.put("memberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        if (myBookingFragment != null) {
            parameters.put("vFilterParam", myBookingFragment.selFilterType);
            parameters.put("vSubFilterParam", myBookingFragment.selSubFilterType);
        } else {
            parameters.put("vFilterParam", bookingAct.selFilterType);
            parameters.put("vSubFilterParam", bookingAct.selSubFilterType);
        }

        if (isLoadMore) {
            parameters.put("page", next_page_str);
        }
        parameters.put("tripdataPage", tripdataPage);

        noRidesTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActivity(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noRidesTxt.setVisibility(View.GONE);
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            tripdataPage = generalFunc.getJsonValueStr("tripdataPage", responseObj);

            swipeRefreshLayout.setRefreshing(false);

            if (responseObj != null && !responseObj.equals("")) {

                closeLoader();

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseObj)) {

                    String nextPage = generalFunc.getJsonValueStr("NextPage", responseObj);


                    JSONArray arr_rides = generalFunc.getJsonArray(Utils.message_str, responseObj);
                    if (arr_rides != null && arr_rides.length() > 0) {
                        String LBL_JOB_LOCATION_TXT = "", LBL_RENTAL_CATEGORY_TXT = "";
                        String LBL_DELIVERY_NO = "", LBL_CANCEL_BOOKING = "", LBL_BOOKING = "", LBL_PICK_UP_LOCATION = "", LBL_DEST_LOCATION = "", LBL_SENDER_LOCATION = "", LBL_RECEIVER_LOCATION = "";
                        String LBL_Status = "", LBL_VIEW_REASON = "", LBL_REBOOKING = "", LBL_RESCHEDULE = "", LBL_VIEW_REQUESTED_SERVICES = "";
                        String LBL_MULTI_LIVE_TRACK_TEXT = "", LBL_VIEW_DETAILS = "";
                        if (arr_rides.length() > 0) {
                            LBL_Status = generalFunc.retrieveLangLBl("", "LBL_Status");
                            LBL_RENTAL_CATEGORY_TXT = generalFunc.retrieveLangLBl("", "LBL_RENTAL_CATEGORY_TXT");
                            LBL_DELIVERY_NO = generalFunc.retrieveLangLBl("Delivery No", "LBL_DELIVERY_NO");
                            LBL_CANCEL_BOOKING = generalFunc.retrieveLangLBl("", "LBL_CANCEL_BOOKING");
                            LBL_BOOKING = generalFunc.retrieveLangLBl("", "LBL_BOOKING");

                            LBL_PICK_UP_LOCATION = generalFunc.retrieveLangLBl("", "LBL_PICK_UP_LOCATION");
                            LBL_DEST_LOCATION = generalFunc.retrieveLangLBl("", "LBL_DEST_LOCATION");
                            LBL_JOB_LOCATION_TXT = generalFunc.retrieveLangLBl("", "LBL_JOB_LOCATION_TXT");
                            LBL_SENDER_LOCATION = generalFunc.retrieveLangLBl("", "LBL_SENDER_LOCATION");
                            LBL_RECEIVER_LOCATION = generalFunc.retrieveLangLBl("", "LBL_RECEIVER_LOCATION");

                            LBL_MULTI_LIVE_TRACK_TEXT = generalFunc.retrieveLangLBl("", "LBL_MULTI_LIVE_TRACK_TEXT");
                            LBL_VIEW_DETAILS = generalFunc.retrieveLangLBl("", "LBL_VIEW_DETAILS");
                            LBL_VIEW_REASON = generalFunc.retrieveLangLBl("", "LBL_VIEW_REASON");
                            LBL_RESCHEDULE = generalFunc.retrieveLangLBl("", "LBL_RESCHEDULE");
                            LBL_REBOOKING = generalFunc.retrieveLangLBl("", "LBL_REBOOKING");
                            LBL_VIEW_REQUESTED_SERVICES = generalFunc.retrieveLangLBl("", "LBL_VIEW_REQUESTED_SERVICES");


                        }

                        for (int i = 0; i < arr_rides.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_rides, i);
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("vBookingType", generalFunc.getJsonValueStr("vBookingType", obj_temp));
                            map.put("vPhone", generalFunc.getJsonValueStr("vPhone", obj_temp));
                            map.put("vImage", generalFunc.getJsonValueStr("vImage", obj_temp));
                            map.put("vAvgRating", "" + generalFunc.parseFloatValue(0, generalFunc.getJsonValueStr("vAvgRating", obj_temp)));
                            map.put("vTimeZone", generalFunc.getJsonValueStr("vTimeZone", obj_temp));
                            map.put("tSaddress", generalFunc.getJsonValueStr("tSaddress", obj_temp));
                            map.put("tDaddress", generalFunc.getJsonValueStr("tDaddress", obj_temp));
                            map.put("vRideNo", generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vRideNo", obj_temp)));
                            map.put("vName", generalFunc.getJsonValueStr("vName", obj_temp));
                            map.put("iTripId", generalFunc.getJsonValueStr("iTripId", obj_temp));
                            map.put("iCabBookingId", generalFunc.getJsonValueStr("iCabBookingId", obj_temp));
                            map.put("vServiceTitle", generalFunc.getJsonValueStr("vServiceTitle", obj_temp));
                            map.put("vVehicleType", generalFunc.getJsonValueStr("vVehicleType", obj_temp));
                            map.put("eShowHistory", generalFunc.getJsonValueStr("eShowHistory", obj_temp));

                            map.put("vTripStatus", generalFunc.getJsonValueStr("vTripStatus", obj_temp));
                            map.put("iDriverId", generalFunc.getJsonValueStr("iDriverId", obj_temp));
                            map.put("driverRating", generalFunc.getJsonValueStr("driverRating", obj_temp));
                            map.put("driverLatitude", generalFunc.getJsonValueStr("driverLatitude", obj_temp));
                            map.put("driverLongitude", generalFunc.getJsonValueStr("driverLongitude", obj_temp));
                            map.put("driverImage", generalFunc.getJsonValueStr("driverImage", obj_temp));

                            map.put("eServiceLocation", generalFunc.getJsonValueStr("eServiceLocation", obj_temp));
                            map.put("eFareType", generalFunc.getJsonValueStr("eFareType", obj_temp));

                            String eType = generalFunc.getJsonValueStr("eType", obj_temp);
                            String iActive = generalFunc.getJsonValueStr("iActive", obj_temp);
                            String dBooking_dateOrig = generalFunc.getJsonValueStr("dBooking_dateOrig", obj_temp);
                            String tTripRequestDate = generalFunc.getJsonValueStr("tTripRequestDate", obj_temp);

                            map.put("appType", APP_TYPE);
                            map.put("currenteType", eType);
                            map.put("eType", eType);
                            map.put("iActive", iActive);

                            map.put("iRentalPackageId", generalFunc.getJsonValueStr("iRentalPackageId", obj_temp));
                            map.put("iVehicleTypeId", generalFunc.getJsonValueStr("iVehicleTypeId", obj_temp));
                            map.put("vLatitude", generalFunc.getJsonValueStr("vLatitude", obj_temp));
                            map.put("vLongitude", generalFunc.getJsonValueStr("vLongitude", obj_temp));
                            map.put("vPhone", generalFunc.getJsonValueStr("vPhone", obj_temp));
                            map.put("vCode", generalFunc.getJsonValueStr("vCode", obj_temp));
                            map.put("vPackageName", generalFunc.getJsonValueStr("vPackageName", obj_temp));
                            map.put("moreServices", generalFunc.getJsonValueStr("moreServices", obj_temp));
                            map.put("is_rating", generalFunc.getJsonValueStr("is_rating", obj_temp));
                            map.put("eFavDriver", generalFunc.getJsonValueStr("eFavDriver", obj_temp));
                            map.put("vCancelReason", generalFunc.getJsonValueStr("vCancelReason", obj_temp));

                            if (eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                                map.put("tDaddress", "");
                            }

                            map.put("vService_BG_color", generalFunc.getJsonValueStr("vService_BG_color", obj_temp));
                            map.put("vService_TEXT_color", generalFunc.getJsonValueStr("vService_TEXT_color", obj_temp));

                            map.put("showViewRequestedServicesBtn", generalFunc.getJsonValueStr("showViewRequestedServicesBtn", obj_temp));
                            map.put("showCancelBookingBtn", generalFunc.getJsonValueStr("showCancelBookingBtn", obj_temp));
                            map.put("showReScheduleBtn", generalFunc.getJsonValueStr("showReScheduleBtn", obj_temp));
                            map.put("showReBookingBtn", generalFunc.getJsonValueStr("showReBookingBtn", obj_temp));
                            map.put("showViewCancelReasonBtn", generalFunc.getJsonValueStr("showViewCancelReasonBtn", obj_temp));
                            map.put("showViewDetailBtn", generalFunc.getJsonValueStr("showViewDetailBtn", obj_temp));
                            map.put("showLiveTrackBtn", generalFunc.getJsonValueStr("showLiveTrackBtn", obj_temp));
                            map.put("showAcceptBtn", generalFunc.getJsonValueStr("showAcceptBtn", obj_temp));
                            map.put("showDeclineBtn", generalFunc.getJsonValueStr("showDeclineBtn", obj_temp));
                            map.put("showStartBtn", generalFunc.getJsonValueStr("showStartBtn", obj_temp));
                            map.put("showCancelBtn", generalFunc.getJsonValueStr("showCancelBtn", obj_temp));

                            try {
                                map.put("ConvertedTripRequestDate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(dBooking_dateOrig, Utils.OriginalDateFormate, CommonUtilities.OriginalDateFormate)));
                                map.put("ConvertedTripRequestTime", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(dBooking_dateOrig, Utils.OriginalDateFormate, CommonUtilities.OriginalTimeFormate)));
                            } catch (Exception e) {
                                e.printStackTrace();
                                map.put("ConvertedTripRequestDate", "");
                                map.put("ConvertedTripRequestTime", "");
                            }

                            if (eType.equalsIgnoreCase("deliver") || eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                                map.put("LBL_PICK_UP_LOCATION", LBL_SENDER_LOCATION);
                                map.put("LBL_DEST_LOCATION", LBL_RECEIVER_LOCATION);
                            } else {
                                map.put("LBL_PICK_UP_LOCATION", LBL_PICK_UP_LOCATION);
                                map.put("LBL_DEST_LOCATION", LBL_DEST_LOCATION);
                            }

                            map.put("LBL_BOOKING_NO", LBL_BOOKING);
                            map.put("LBL_Status", LBL_Status);

                            if (isDeliver(eType) || eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                                map.put("LBL_BOOKING_NO", LBL_DELIVERY_NO);
                            } else {
                                map.put("LBL_BOOKING_NO", LBL_BOOKING);
                            }
                            map.put("LBL_JOB_LOCATION_TXT", LBL_JOB_LOCATION_TXT);
                            map.put("LBL_CANCEL_BOOKING", LBL_CANCEL_BOOKING);
                            map.put("LBL_RENTAL_CATEGORY_TXT", LBL_RENTAL_CATEGORY_TXT);
                            map.put("liveTrackLBL", LBL_MULTI_LIVE_TRACK_TEXT);
                            map.put("viewDetailLBL", LBL_VIEW_DETAILS);
                            map.put("LBL_VIEW_REASON", LBL_VIEW_REASON);
                            map.put("LBL_RESCHEDULE", LBL_RESCHEDULE);
                            map.put("LBL_REBOOKING", LBL_REBOOKING);
                            map.put("LBL_VIEW_REQUESTED_SERVICES", LBL_VIEW_REQUESTED_SERVICES);

                            map.put("JSON", obj_temp.toString());
                            map.put("APP_TYPE", APP_TYPE);


                            if (eType.equals(Utils.CabGeneralType_UberX) /*&&
                                    !generalFunc.getJsonValueStr("eFareType", obj_temp).equalsIgnoreCase(Utils.CabFaretypeRegular)*/) {
                                map.put("selectedtime", generalFunc.getJsonValueStr("selectedtime", obj_temp));
                                map.put("SelectedCategoryId", generalFunc.getJsonValueStr("SelectedCategoryId", obj_temp));
                                map.put("iQty", generalFunc.getJsonValueStr("iQty", obj_temp));
                                map.put("iUserAddressId", generalFunc.getJsonValueStr("iUserAddressId", obj_temp));
                                map.put("dBooking_dateOrig", dBooking_dateOrig);
                                map.put("selecteddatetime", generalFunc.getJsonValueStr("selecteddatetime", obj_temp));
                                map.put("SelectedCurrencySymbol", generalFunc.getJsonValueStr("SelectedCurrencySymbol", obj_temp));
                                map.put("SelectedAllowQty", generalFunc.getJsonValueStr("SelectedAllowQty", obj_temp));
                                map.put("SelectedPrice", generalFunc.getJsonValueStr("SelectedPrice", obj_temp));
                                map.put("SelectedVehicle", generalFunc.getJsonValueStr("SelectedVehicle", obj_temp));
                                map.put("SelectedCategory", generalFunc.getJsonValueStr("SelectedCategory", obj_temp));
                            } else {
                                map.put("SelectedCategory", generalFunc.getJsonValueStr("vVehicleType", obj_temp));
                            }

                            list.add(map);

                        }
                    }

                    buildFilterTypes(responseObj);

                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }

                    historyRecyclerAdapter.notifyDataSetChanged();

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
        if (responseObj==null) return;
        String eFilterSel = generalFunc.getJsonValueStr("eFilterSel", responseObj);

        JSONArray arr_type_filter = generalFunc.getJsonArray("AppTypeFilterArr", responseObj);

        filterlist = new ArrayList<>();
        if (arr_type_filter != null && arr_type_filter.length() > 0) {
            for (int i = 0; i < arr_type_filter.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(arr_type_filter, i);
                HashMap<String, String> map = new HashMap<String, String>();
                String vTitle = generalFunc.getJsonValueStr("vTitle", obj_temp);
                map.put("vTitle", vTitle);
                String vFilterParam = generalFunc.getJsonValueStr("vFilterParam", obj_temp);
                map.put("vFilterParam", vFilterParam);

                filterlist.add(map);
            }
        }

        if (myBookingFragment != null) {
            myBookingFragment.filterManage(filterlist);
        } else {
            bookingAct.filterManage(filterlist);
        }


        JSONArray subFilterOptionArr = generalFunc.getJsonArray("subFilterOption", responseObj);

        subFilterlist = new ArrayList<>();
        if (subFilterOptionArr != null && subFilterOptionArr.length() > 0) {
            for (int i = 0; i < subFilterOptionArr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(subFilterOptionArr, i);
                HashMap<String, String> map = new HashMap<String, String>();
                String vTitle = generalFunc.getJsonValueStr("vTitle", obj_temp);
                map.put("vTitle", vTitle);
                String vSubFilterParam = generalFunc.getJsonValueStr("vSubFilterParam", obj_temp);
                map.put("vSubFilterParam", vSubFilterParam);

                if (vSubFilterParam.equalsIgnoreCase(eFilterSel)) {


                    if (myBookingFragment != null) {
                        myBookingFragment.selSubFilterType = vSubFilterParam;
                        myBookingFragment.subFilterPosition = i;
                    } else {
                        bookingAct.selSubFilterType = vSubFilterParam;
                        bookingAct.subFilterPosition = i;
                    }

                    filterTxt.setText(vTitle);
                }
                subFilterlist.add(map);
            }
        }

        if (myBookingFragment != null) {
            myBookingFragment.subFilterManage(subFilterlist, "History");
        } else {
            bookingAct.subFilterManage(subFilterlist, "History");
        }

    }

    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        historyRecyclerAdapter.removeFooterView();
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
        errorView.setOnRetryListener(() -> getBookingsHistory(false));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == Utils.SCHEDULE_REQUEST_CODE) {
            userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            updateBookingDate(data.getStringExtra("iCabBookingId"), data.getStringExtra("SelectDate"), "Yes");

        } else if (resultCode == Activity.RESULT_OK) {
            userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            list.clear();
            getBookingsHistory(true);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActivity());
    }
}
