package com.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adapter.files.NotificationAdapter;

import com.melevicarbrasil.usuario.NotificationActivity;
import com.melevicarbrasil.usuario.NotificationDetailsActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NotiFicationFragment extends Fragment implements NotificationAdapter.OnItemClickListener {

    View view;
    RecyclerView notificationRecyclerView;
    NotificationAdapter notificationAdapter;
    NotificationActivity notificationActivity;
    GeneralFunctions generalFunc;
    String type = "";
    ArrayList<HashMap<String, String>> list;
    String readMoreLbl = "";
    String next_page_str = "";
    boolean isNextPageAvailable = false;
    MTextView noDataTxt;
    boolean mIsLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationActivity = (NotificationActivity) getActivity();
        generalFunc = notificationActivity.generalFunc;
        type = getArguments().getString("type");
        list = new ArrayList<>();
        notificationRecyclerView = (RecyclerView) view.findViewById(R.id.notificationRecyclerView);
        noDataTxt = (MTextView) view.findViewById(R.id.noDataTxt);
        notificationAdapter = new NotificationAdapter(getActContext(), list, type, generalFunc, false);
        notificationRecyclerView.setAdapter(notificationAdapter);
        notificationAdapter.setOnItemClickListener(this);

//        notificationRecyclerView.addItemDecoration(new DividerItemDecoration(getActContext(), DividerItemDecoration.VERTICAL_LIST));

        readMoreLbl = generalFunc.retrieveLangLBl("", "LBL_READ_MORE");

        list.clear();
        getNotificationDetails(false);


        notificationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                int lastInScreen = firstVisibleItemPosition + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable == true) {
                    mIsLoading = true;

                    getNotificationDetails(true);

                } else if (isNextPageAvailable == false) {
                    notificationAdapter.removeFooterView();
                }
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean isDeliver() {
        if (getArguments().getString("BOOKING_TYPE").equals(Utils.CabGeneralType_Deliver)) {
            return true;
        }
        return false;
    }

    public Context getActContext() {
        return notificationActivity.getActContext();
    }

    @Override
    public void onReadMoreItemClick(View v, int position) {

        Bundle bn = new Bundle();
        bn.putSerializable("data", list.get(position));
        new StartActProcess(getActContext()).startActWithData(NotificationDetailsActivity.class, bn);

    }


    public void getNotificationDetails(boolean isLoadMore) {


        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getNewsNotification");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("eType", type);

        if (isLoadMore) {
            parameters.put("page", next_page_str);
        }


        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {

                    String nextPage = generalFunc.getJsonValue("NextPage", responseString);
                    JSONArray arr_notifications = generalFunc.getJsonArray(Utils.message_str, responseString);
                    if (arr_notifications != null && arr_notifications.length() > 0) {
                        noDataTxt.setVisibility(View.GONE);

                        for (int i = 0; i < arr_notifications.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_notifications, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("iNewsfeedId", generalFunc.getJsonValueStr("iNewsfeedId", obj_temp));
                            map.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
                            map.put("vImage", generalFunc.getJsonValueStr("vImage", obj_temp));
                            map.put("tDescription", generalFunc.getJsonValueStr("tDescription", obj_temp));
                            map.put("eStatus", generalFunc.getJsonValueStr("eStatus", obj_temp));
                            map.put("eType", generalFunc.getJsonValueStr("eType", obj_temp));
                            map.put("readMoreLbl", readMoreLbl);

                            map.put("dDateTime", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(generalFunc.getJsonValueStr("dDateTime", obj_temp), Utils.OriginalDateFormate,CommonUtilities.OriginalDateFormate)));
                            list.add(map);
                        }
                    }

                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }

                    notificationAdapter.notifyDataSetChanged();

                } else {
                    if (list.size() == 0) {
                        removeNextPageConfig();
                        noDataTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        noDataTxt.setVisibility(View.VISIBLE);
                        notificationAdapter.notifyDataSetChanged();
                    }

                }
            } else {
                list.clear();
                if (list.size() == 0) {
                    removeNextPageConfig();
                    noDataTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    noDataTxt.setVisibility(View.VISIBLE);
                    notificationAdapter.notifyDataSetChanged();
                }

            }
            mIsLoading = false;

        });
        exeWebServer.execute();
    }

    public void removeNextPageConfig() {
        next_page_str = "";
        mIsLoading = false;
        isNextPageAvailable = false;
    }
}
