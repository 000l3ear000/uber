package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.adapter.files.DatesRecyclerAdapter;
import com.adapter.files.TimeSlotAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ScheduleDateSelectActivity extends AppCompatActivity implements TimeSlotAdapter.setRecentTimeSlotClickList, DatesRecyclerAdapter.OnDateSelectListener {


    GeneralFunctions generalFunc;
    ImageView backImgView;
    MTextView titleTxt;
    MTextView monthTxt, timeHtxt;
    MButton btn_type2;
    MTextView AddressTxtView, serviceAddrHederTxtView;
    String address = "";
    String latitude = "";
    String longitude = "";
    String iUserAddressId = "";
    RecyclerView timeslotRecyclerView;
    String seldate = "";
    String seltime = "";
    String Stime = "";
    boolean ismain = false;
    String iCabBookingId = "";

    View containerView;
    View loadingAvailBar;

    ArrayList<HashMap<String, String>> timeSlotList;

    ArrayList<HashMap<String, String>> timeSlotListOrig = new ArrayList<>();

    boolean isRebooking = false;
    ArrayList<HashMap<String, Object>> dateList = new ArrayList<HashMap<String, Object>>();

    RecyclerView datesRecyclerView;

    JSONObject userProfileJsonObj;

    String SERVICE_PROVIDER_FLOW = "";

    HashMap<String, List> driverAvailDataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_date_select);

        timeSlotList = new ArrayList<HashMap<String, String>>();
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJsonObj = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

        SERVICE_PROVIDER_FLOW = generalFunc.getJsonValueStr("SERVICE_PROVIDER_FLOW", userProfileJsonObj);

        if (getIntent().getStringExtra("iCabBookingId") != null) {
            iCabBookingId = getIntent().getStringExtra("iCabBookingId");
        }

        isRebooking = getIntent().getBooleanExtra("isRebooking", false);

        if (getIntent().getStringExtra("Stime") != null) {
            Stime = getIntent().getStringExtra("Stime");
        }

        address = getIntent().getStringExtra("address");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        iUserAddressId = getIntent().getStringExtra("iUserAddressId");
        ismain = getIntent().getBooleanExtra("isMain", false);

        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        monthTxt = (MTextView) findViewById(R.id.monthTxt);
        timeHtxt = (MTextView) findViewById(R.id.timeHtxt);
        AddressTxtView = (MTextView) findViewById(R.id.AddressTxtView);
        datesRecyclerView = (RecyclerView) findViewById(R.id.datesRecyclerView);
        serviceAddrHederTxtView = (MTextView) findViewById(R.id.serviceAddrHederTxtView);
        timeslotRecyclerView = (RecyclerView) findViewById(R.id.timeslotRecyclerView);
        containerView = findViewById(R.id.containerView);
        loadingAvailBar = findViewById(R.id.loadingAvailBar);

        AddressTxtView.setText(address);

        timeslotRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        TimeSlotAdapter adapter = new TimeSlotAdapter(getActContext(), timeSlotList);

        timeslotRecyclerView.setAdapter(adapter);
        adapter.setOnClickList(this);

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setOnClickListener(new setOnClick());
        btn_type2.setText(generalFunc.retrieveLangLBl("Continue", "LBL_CONTINUE_BTN"));

        backImgView.setOnClickListener(new setOnClick());

        /** end after 1 month from now */
        Calendar endDate = Calendar.getInstance(Locale.getDefault());
        endDate.add(Calendar.MONTH, 1);
        // endDate.add(Calendar.MONTH, 0);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance(Locale.getDefault());
        startDate.add(Calendar.MONTH, 0);
        // startDate.add(Calendar.MONTH, 0);

        Date currentTempDate = startDate.getTime();
        int position = 0;

        Locale locale = new Locale(generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        java.text.DateFormat dayNameFormatter = new SimpleDateFormat("EEE", locale);
        java.text.DateFormat dayNumFormatter = new SimpleDateFormat("dd", locale);
        java.text.DateFormat dayMonthFormatter = new SimpleDateFormat("MMM", locale);


        while (currentTempDate.before(endDate.getTime())) {

            HashMap<String, Object> hashMap = new HashMap();
            hashMap.put("dayNameTxt", dayNameFormatter.format(currentTempDate));
            hashMap.put("dayNumTxt", dayNumFormatter.format(currentTempDate) + " " + dayMonthFormatter.format(currentTempDate));
            hashMap.put("currentDate", currentTempDate);

            dateList.add(hashMap);

            position = position + 1;

            Calendar tmpCal = Calendar.getInstance(Locale.getDefault());
            tmpCal.add(Calendar.DATE, position);

            currentTempDate = tmpCal.getTime();
        }

        DatesRecyclerAdapter dateAdapter = new DatesRecyclerAdapter(generalFunc, dateList, getActContext(), startDate.getTime());

        dateAdapter.setOnDateSelectListener(this);
        datesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        datesRecyclerView.setAdapter(dateAdapter);

        dateAdapter.notifyDataSetChanged();


        if (!generalFunc.isRTLmode()) {
            datesRecyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            datesRecyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        settimeSlotData();

        setLabel();

        if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider")) {
            serviceAddrHederTxtView.setVisibility(View.GONE);
            AddressTxtView.setVisibility(View.GONE);
            getDriverAvailability();
        } else {
            onDateSelect(0);
        }
    }

    private void setLabel() {
        titleTxt.setText(generalFunc.retrieveLangLBl("LBL_CHOOSE_BOOKING_DATE", "LBL_CHOOSE_BOOKING_DATE"));
        serviceAddrHederTxtView.setText(generalFunc.retrieveLangLBl("Service address", "LBL_SERVICE_ADDRESS_HINT_INFO"));
    }

    public Context getActContext() {
        return ScheduleDateSelectActivity.this;
    }

    @Override
    public void itemTimeSlotLocClick(int position) {

        seltime = timeSlotListOrig.get(position).get("selname");
        Stime = timeSlotListOrig.get(position).get("name");

    }

    private void CheckDateTimeApi() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "CheckScheduleTimeAvailability");
        parameters.put("scheduleDate", seldate + " " + seltime);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    Bundle bundle = new Bundle();
                    bundle.putString("SelectedVehicleTypeId", getIntent().getStringExtra("SelectedVehicleTypeId"));
                    bundle.putString("Quantity", getIntent().getStringExtra("Quantity"));
                    bundle.putBoolean("isufx", true);
                    bundle.putString("latitude", getIntent().getStringExtra("latitude"));
                    bundle.putString("longitude", getIntent().getStringExtra("longitude"));
                    bundle.putString("address", getIntent().getStringExtra("address"));
                    bundle.putString("SelectDate", seldate + " " + seltime);
                    bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));
                    bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
                    bundle.putString("iUserAddressId", getIntent().getStringExtra("iUserAddressId"));
                    bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
                    bundle.putString("type", Utils.CabReqType_Later);

                    bundle.putString("Sdate", generalFunc.getDateFormatedType(seldate, Utils.DefaultDatefromate, Utils.dateFormateForBooking));
                    bundle.putString("Stime", Stime);
                    bundle.putString("iCabBookingId", iCabBookingId);

                    Logger.e("ClassName", "::" + getCallingActivity().getClassName());

                   boolean isFrom= (getCallingActivity() != null && (getCallingActivity().getClassName().equals(CarWashBookingDetailsActivity.class.getName())) ||
                    (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider") && getCallingActivity().getClassName().equals(BookingActivity.class.getName())) ||
                            (getCallingActivity().getClassName().equals(UberXHomeActivity.class.getName())));

                    Logger.e("ClassName", "::" + getCallingActivity().getClassName());

                    if (ismain || isFrom ) {
                        new StartActProcess(getActContext()).setOkResult(bundle);
                        finish();
                    } else {
                        bundle.putBoolean("isRebooking", isRebooking);
                        bundle.putString("selType", Utils.CabGeneralType_UberX);
                        new StartActProcess(getActContext()).startActWithData(MainActivity.class, bundle);
                    }


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

    public void settimeSlotData() {

        String LBL_AM_TXT = generalFunc.retrieveLangLBl("am", "LBL_AM_TXT");
        String LBL_PM_TXT = generalFunc.retrieveLangLBl("pm", "LBL_PM_TXT");

        for (int i = 0; i <= 23; i++) {
            HashMap<String, String> map = new HashMap<>();
            HashMap<String, String> mapOrig = new HashMap<>();

            map.put("status", "no");
            mapOrig.put("status", "no");

            int fromtime = i;
            int toTime = i + 1;


            String fromtimedisp = "";
            String Totimedisp = "";
            String selfromtime = "";
            String seltoTime = "";

            if (fromtime == 0) {
                fromtime = 12;
            }

            if (fromtime < 10) {
                selfromtime = "0" + fromtime;
            } else {
                selfromtime = fromtime + "";
            }

            if (toTime < 10) {
                seltoTime = "0" + toTime;
            } else {
                seltoTime = toTime + "";
            }

            if (i < 12) {


                if (fromtime < 10) {
                    fromtimedisp = "0" + fromtime;

                } else {
                    fromtimedisp = fromtime + "";

                }

                if (toTime < 10) {
                    Totimedisp = "0" + toTime;

                } else {
                    Totimedisp = toTime + "";
                }


                //   map.put("name", generalFunc.convertNumberWithRTL(fromtimedisp + " - " + Totimedisp + " " + LBL_AM_TXT));
                map.put("name", generalFunc.convertNumberWithRTL(fromtimedisp + " " + LBL_AM_TXT + " - " + Totimedisp + " " + generalFunc.retrieveLangLBl(i == 11 ? "pm" : "am", i == 11 ? "LBL_PM_TXT" : "LBL_AM_TXT")));
                mapOrig.put("name", fromtimedisp + " - " + Totimedisp + " " + LBL_AM_TXT);
                map.put("selname", generalFunc.convertNumberWithRTL(selfromtime + "-" + seltoTime));
                mapOrig.put("selname", selfromtime + "-" + seltoTime);
            } else {

                fromtime = fromtime % 12;
                toTime = toTime % 12;
                if (fromtime == 0) {
                    fromtime = 12;
                }

                if (toTime == 0) {
                    toTime = 12;
                }
                if (fromtime < 10) {
                    fromtimedisp = "0" + fromtime;
                } else {
                    fromtimedisp = fromtime + "";
                }

                if (toTime < 10) {
                    Totimedisp = "0" + toTime;
                } else {
                    Totimedisp = toTime + "";
                }

                //  map.put("name", generalFunc.convertNumberWithRTL(fromtimedisp + " - " + Totimedisp + " " + LBL_PM_TXT));

                map.put("name", generalFunc.convertNumberWithRTL(fromtimedisp + " " + LBL_PM_TXT + " - " + Totimedisp + " " + generalFunc.retrieveLangLBl(i == 23 ? "am" : "pm", i == 23 ? "LBL_AM_TXT" : "LBL_PM_TXT")));
                mapOrig.put("name", fromtimedisp + " - " + Totimedisp + " " + LBL_PM_TXT);
                map.put("selname", generalFunc.convertNumberWithRTL(selfromtime + "-" + seltoTime));
                mapOrig.put("selname", selfromtime + "-" + seltoTime);
            }


            timeSlotList.add(map);
            timeSlotListOrig.add(mapOrig);
        }

    }

    public void getDriverAvailability() {
        driverAvailDataMap.clear();
        containerView.setVisibility(View.INVISIBLE);
        loadingAvailBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDriverAvailability");
        parameters.put("iDriverId", getIntent().getStringExtra("iDriverId"));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    JSONArray message = generalFunc.getJsonArray(Utils.message_str, responseString);
                    if (message != null && message.length() >= 1) {
                        for (int i = 0; i < message.length(); i++) {
                            JSONObject object = generalFunc.getJsonObject(message, i);
                            driverAvailDataMap.put(generalFunc.getJsonValueStr("vDay", object), Arrays.asList(generalFunc.getJsonValueStr("vAvailableTimes", object).split(",")));
                        }
                    }

                    onDateSelect(0);

                    new Handler().postDelayed(() -> {
                        loadingAvailBar.setVisibility(View.GONE);
                        containerView.setVisibility(View.VISIBLE);
                    }, 600);

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

    @Override
    public void onDateSelect(int position) {

        Date date = (Date) dateList.get(position).get("currentDate");

        String tempDate = Utils.convertDateToFormat("yyyy-MM-dd HH:mm:ss", date);
        seldate = generalFunc.getDateFormatedType(tempDate, "yyyy-MM-dd HH:mm:ss", Utils.DefaultDatefromate, new Locale("en"));

        Locale locale = new Locale(generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        DateFormat formatter = new SimpleDateFormat("MMMM", locale);
        DateFormat formatter_day_en = new SimpleDateFormat("EEEE", new Locale("en"));

        String monthname = formatter.format(date);
        String dayName_en = formatter_day_en.format(date);

        String year = (String) android.text.format.DateFormat.format("yyyy", date);

        // monthTxt.setText(monthname + " " + generalFunc.convertNumberWithRTL(year));
        monthTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WHAT_DAY"));
        timeHtxt.setText(generalFunc.retrieveLangLBl("", "LBL_WHAT_TIME"));

        if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider")) {
            seltime = "";
            Stime = "";

            ((TimeSlotAdapter) timeslotRecyclerView.getAdapter()).isSelectedPos = -1;

            List data_availability = new ArrayList();

            if (driverAvailDataMap.get(dayName_en) != null) {
                data_availability.addAll(driverAvailDataMap.get(dayName_en));
            }

//            boolean isNotifyAll = false;

            for (int i = 0; i < timeSlotListOrig.size(); i++) {
                HashMap<String, String> map_tmP_orig = timeSlotListOrig.get(i);
                HashMap<String, String> map_tmP = timeSlotList.get(i);

                String selName = map_tmP_orig.get("selname");

                String isDriverAvailable = map_tmP_orig.get("isDriverAvailable");

//                if (isDriverAvailable == null) {
//                    isNotifyAll = true;
//                }

                map_tmP_orig.put("isDriverAvailable", data_availability.contains(selName) ? "Yes" : "No");
                map_tmP.put("isDriverAvailable", map_tmP_orig.get("isDriverAvailable"));

                if (isDriverAvailable != null && /*!isNotifyAll && */!isDriverAvailable.equalsIgnoreCase(map_tmP_orig.get("isDriverAvailable"))) {
                    timeslotRecyclerView.getAdapter().notifyItemChanged(i);
                }
            }

//            if (isNotifyAll) {
            timeslotRecyclerView.getAdapter().notifyDataSetChanged();
//            }
        }

    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                ScheduleDateSelectActivity.super.onBackPressed();
            } else if (i == btn_type2.getId()) {
                if (seltime.equals("")) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please Select Booking Time.", "LBL_SELECT_SERVICE_BOOKING_TIME"));
                    return;
                }

                CheckDateTimeApi();

            }
        }
    }

}
