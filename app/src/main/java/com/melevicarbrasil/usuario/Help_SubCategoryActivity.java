package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.files.HelpSubCategoryAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 08-03-18.
 */

public class Help_SubCategoryActivity extends AppCompatActivity implements HelpSubCategoryAdapter.OnItemClickList {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    ProgressBar loading;
    MTextView noHelpTxt;

    RecyclerView helpCategoryRecyclerView;
    HelpSubCategoryAdapter adapter;
    ErrorView errorView;

    ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_subcategory);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        loading = (ProgressBar) findViewById(R.id.loading);
        noHelpTxt = (MTextView) findViewById(R.id.noHelpTxt);
        helpCategoryRecyclerView = (RecyclerView) findViewById(R.id.helpSubCategoryRecyclerView);
        errorView = (ErrorView) findViewById(R.id.errorView);

        list = new ArrayList<>();
        adapter = new HelpSubCategoryAdapter(getActContext(), list, generalFunc);
        helpCategoryRecyclerView.setAdapter(adapter);

        getHelpCategory();
        setLabels();

        backImgView.setOnClickListener(new setOnClickList());

        adapter.setOnItemClickList(this);

    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("Help?", "LBL_HEADER_HELP_TXT"));
    }

    @Override
    public void onItemClick(int position) {
        HashMap<String, String> indexPos = list.get(position);
        Bundle bn = new Bundle();
        bn.putString("iHelpDetailId", indexPos.get("iHelpDetailId"));
        bn.putString("vTitle", indexPos.get("vTitle"));
        bn.putString("tAnswer", indexPos.get("tAnswer"));
        bn.putString("eShowFrom", indexPos.get("eShowFrom"));
        bn.putString("iUniqueId", getIntent().getStringExtra("iUniqueId"));
        if (getIntent().hasExtra("iOrderId")) {
            bn.putString("iOrderId", getIntent().getStringExtra("iOrderId"));
        } else {
            bn.putString("iTripId", getIntent().getStringExtra("iTripId"));
        }
        //generalFunc.storeData(Utils.vTitle, indexPos.get("vTitle"));
        //Logger.d("data_","vvstore::"+indexPos.get("vTitle"));
        new StartActProcess(getActContext()).startActWithData(Help_DetailsActivity.class, bn);
    }

    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    public void getHelpCategory() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        if (list.size() > 0) {
            list.clear();
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getsubHelpdetail");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("appType", Utils.app_type);
        parameters.put("iUniqueId", getIntent().getStringExtra("iUniqueId"));

        if (getIntent().hasExtra("iOrderId")) {
            parameters.put("iOrderId", getIntent().getStringExtra("iOrderId"));
            parameters.put("eSystem", Utils.eSystem_Type);
        } else {
            parameters.put("iTripId", getIntent().getStringExtra("iTripId"));
        }

        noHelpTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noHelpTxt.setVisibility(View.GONE);

            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {

                    JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseString);

                    for (int i = 0; i < obj_arr.length(); i++) {
                        JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("iHelpDetailId", generalFunc.getJsonValueStr("iHelpDetailId", obj_temp));
                        map.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
                        map.put("tAnswer", generalFunc.getJsonValueStr("tAnswer", obj_temp));
                        map.put("eShowFrom", generalFunc.getJsonValueStr("eShowFrom", obj_temp));

                        list.add(map);
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    noHelpTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    noHelpTxt.setVisibility(View.VISIBLE);
                }
            } else {
                generateErrorView();
            }
        });
        exeWebServer.execute();
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getHelpCategory());
    }

    public Context getActContext() {
        return Help_SubCategoryActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    Help_SubCategoryActivity.super.onBackPressed();
                    break;

            }
        }
    }


}
