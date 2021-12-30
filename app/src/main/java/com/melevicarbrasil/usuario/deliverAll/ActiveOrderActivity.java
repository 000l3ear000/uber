package com.melevicarbrasil.usuario.deliverAll;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.files.deliverAll.ActiveOrderAdapter;
import com.melevicarbrasil.usuario.Help_MainCategory;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActiveOrderActivity extends AppCompatActivity implements ActiveOrderAdapter.OnItemClickListener {

    ImageView backImgView;
    MTextView titleTxt;
    GeneralFunctions generalFunc;

    ProgressBar mProgressBar;

    boolean isRestart = false;

    RecyclerView activeRecyclerView;
    ErrorView errorView;
    MTextView noDataTxt;

    ActiveOrderAdapter activeOrderAdapter;

    ArrayList<HashMap<String, String>> list;
    InternetConnection internetConnection;

    View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);

        isRestart = getIntent().getBooleanExtra("isRestart", false);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        internetConnection = new InternetConnection(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        errorView = (ErrorView) findViewById(R.id.errorView);
        noDataTxt = (MTextView) findViewById(R.id.noDataTxt);
        backImgView.setOnClickListener(new setOnClickList());
        activeRecyclerView = (RecyclerView) findViewById(R.id.activeRecyclerView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        contentView = findViewById(R.id.contentView);

        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ORDERS_TXT"));
        noDataTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_DATA_AVAIL"));

        list = new ArrayList<>();

        activeOrderAdapter = new ActiveOrderAdapter(getActContext(), list, generalFunc, false);
        activeRecyclerView.setAdapter(activeOrderAdapter);
        activeOrderAdapter.setOnItemClickListener(this);

        generateErrorView();

        if (isRestart) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", getIntent().getStringExtra("iOrderId"));
            new StartActProcess(getActContext()).startActWithData(TrackOrderActivity.class, bn);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getActiveOrderDetails();
    }

    public void getActiveOrderDetails() {

        contentView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        list.clear();
        activeOrderAdapter.notifyDataSetChanged();

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DisplayActiveOrder");
        parameters.put("UserType", Utils.userType);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseObj=generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                String action = generalFunc.getJsonValueStr(Utils.action_str, responseObj);

                generateErrorView();
                if (action.equals("1")) {

                    noDataTxt.setVisibility(View.GONE);

                    JSONArray message_arr = generalFunc.getJsonArray("message", responseObj);

                    if(message_arr != null){
                        list.clear();

                        int user_profile_icon_size_main=0;
                        String LBL_HISTORY_REST_DELIVERED="",LBL_REFUNDED="",LBL_HISTORY_REST_CANCELLED="",LBL_TOTAL_TXT="",LBL_ORDER_AT="",LBL_HELP="",LBL_VIEW_DETAILS="",LBL_TRACK_ORDER="";
                        if (message_arr.length()>0)
                        {
                            user_profile_icon_size_main = getActContext().getResources().getDimensionPixelSize(R.dimen.dimen_50);
                            LBL_HISTORY_REST_DELIVERED=generalFunc.retrieveLangLBl("", "LBL_HISTORY_REST_DELIVERED");
                            LBL_REFUNDED=generalFunc.retrieveLangLBl("", "LBL_REFUNDED");
                            LBL_HISTORY_REST_CANCELLED=generalFunc.retrieveLangLBl("", "LBL_HISTORY_REST_CANCELLED");
                            LBL_TOTAL_TXT=generalFunc.retrieveLangLBl("", "LBL_TOTAL_TXT");
                            LBL_ORDER_AT=generalFunc.retrieveLangLBl("", "LBL_ORDER_AT");
                            LBL_HELP=generalFunc.retrieveLangLBl("", "LBL_HELP");
                            LBL_VIEW_DETAILS=generalFunc.retrieveLangLBl("", "LBL_VIEW_DETAILS");
                            LBL_TRACK_ORDER=generalFunc.retrieveLangLBl("", "LBL_TRACK_ORDER");

                        }
                        for (int i = 0; i < message_arr.length(); i++) {
                            JSONObject addr_obj = generalFunc.getJsonObject(message_arr, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("vCompany", generalFunc.getJsonValueStr("vCompany", addr_obj));
                            map.put("vRestuarantLocation", generalFunc.getJsonValueStr("vRestuarantLocation", addr_obj));
                            map.put("iOrderId", generalFunc.getJsonValueStr("iOrderId", addr_obj));
                            map.put("vOrderNo", generalFunc.getJsonValueStr("vOrderNo", addr_obj));
                            map.put("tOrderRequestDate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(generalFunc.getJsonValueStr("tOrderRequestDate", addr_obj), Utils.OriginalDateFormate, Utils.getDetailDateFormat(getActContext()))));
                            map.put("fNetTotal", generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("fNetTotal", addr_obj)));
                            map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", addr_obj));
                            map.put("vStatus", generalFunc.getJsonValueStr("vStatus", addr_obj));
                            map.put("iStatusCode", generalFunc.getJsonValueStr("iStatusCode", addr_obj));
                            map.put("DisplayLiveTrack", generalFunc.getJsonValueStr("DisplayLiveTrack", addr_obj));
                            map.put("vServiceCategoryName", generalFunc.getJsonValueStr("vServiceCategoryName", addr_obj));
                            map.put("vImage", Utilities.getResizeImgURL(getActContext(), generalFunc.getJsonValueStr("vImage", addr_obj), user_profile_icon_size_main, user_profile_icon_size_main));

                            map.put("LBL_HISTORY_REST_DELIVERED",LBL_HISTORY_REST_DELIVERED);
                            map.put("LBL_REFUNDED",LBL_REFUNDED);
                            map.put("LBL_HISTORY_REST_CANCELLED",LBL_HISTORY_REST_CANCELLED);
                            map.put("LBL_TOTAL_TXT", LBL_TOTAL_TXT);
                            map.put("LBL_ORDER_AT",LBL_ORDER_AT);
                            map.put("LBL_HELP", LBL_HELP);
                            map.put("LBL_VIEW_DETAILS",LBL_VIEW_DETAILS);
                            map.put("LBL_TRACK_ORDER",LBL_TRACK_ORDER );

                            list.add(map);
                        }
                    }

                    activeOrderAdapter.notifyDataSetChanged();

                } else {
                    noDataTxt.setVisibility(View.VISIBLE);
                }
                contentView.setVisibility(View.VISIBLE);
            } else {
                generalFunc.showError(true);
            }
            mProgressBar.setVisibility(View.GONE);
        });
        exeWebServer.execute();
    }


    public void generateErrorView() {
        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (internetConnection.isNetworkConnected()) {
            errorView.setVisibility(View.GONE);
        } else {
            if (errorView.getVisibility() != View.VISIBLE) {
                errorView.setVisibility(View.VISIBLE);
            }
            errorView.setOnRetryListener(this::getActiveOrderDetails);
        }
    }


    public Context getActContext() {
        return ActiveOrderActivity.this;
    }


    @Override
    public void onBackPressed() {
        if (!isRestart) {
            super.onBackPressed();
        } else {
            MyApp.getInstance().restartWithGetDataApp();
        }
    }


    @Override
    public void onItemClickList(View v, int position, String isSelect) {
        if (isSelect.equalsIgnoreCase("help")) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", list.get(position).get("iOrderId"));
            new StartActProcess(getActContext()).startActWithData(Help_MainCategory.class, bn);
        } else if (isSelect.equalsIgnoreCase("track")) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", list.get(position).get("iOrderId"));
            new StartActProcess(getActContext()).startActWithData(TrackOrderActivity.class, bn);
        } else if (isSelect.equalsIgnoreCase("view")) {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", list.get(position).get("iOrderId"));
            new StartActProcess(getActContext()).startActWithData(OrderDetailsActivity.class, bn);
        }
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            }
        }
    }

}
