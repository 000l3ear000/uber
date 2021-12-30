package com.melevicarbrasil.usuario;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.adapter.files.UberXCategoryAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UberxFilterActivity extends AppCompatActivity implements UberXCategoryAdapter.OnItemClickList {


    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    String userProfileJson = "";
    MButton btn_type2;
    int submitBtnId;
    RecyclerView dataListRecyclerView;
    UberXCategoryAdapter ufxCatAdapter;
    ArrayList<HashMap<String, String>> generalCategoryList = new ArrayList<>();

    MTextView selectServiceTxt;
    String SelectedVehicleTypeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uberx_filter);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        selectServiceTxt = (MTextView) findViewById(R.id.selectServiceTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());
        dataListRecyclerView = (RecyclerView) findViewById(R.id.dataListRecyclerView);
        dataListRecyclerView.setLayoutManager(new LinearLayoutManager(getActContext()));
        ufxCatAdapter = new UberXCategoryAdapter(getActContext(), generalCategoryList, generalFunc);
        dataListRecyclerView.setAdapter(ufxCatAdapter);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_FILTER_TXT"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_SERVICE_TXT"));
        selectServiceTxt.setText(getIntent().getStringExtra("SelectvVehicleType"));

        SelectedVehicleTypeId = getIntent().getStringExtra("SelectedVehicleTypeId");

        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);

        btn_type2.setOnClickListener(new setOnClickList());
        getCategory();
    }


    public void getCategory() {
        generalCategoryList.clear();

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getServiceCategories");
        parameters.put("parentId", getIntent().getStringExtra("parentId"));
        parameters.put("userId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setCancelAble(false);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);
                if (isDataAvail) {

                    JSONArray mainCataArray = generalFunc.getJsonArray("message", responseObj);

                    if (mainCataArray != null) {
                        String LBL_BOOK_NOW = generalFunc.retrieveLangLBl("", "LBL_BOOK_NOW");
                        for (int i = 0; i < mainCataArray.length(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject categoryObj = generalFunc.getJsonObject(mainCataArray, i);
                            map.put("eCatType", generalFunc.getJsonValueStr("eCatType", categoryObj));
                            map.put("iServiceId", generalFunc.getJsonValueStr("iServiceId", categoryObj));
                            map.put("vCategory", generalFunc.getJsonValueStr("vCategory", categoryObj));
                            map.put("vLogo_image", generalFunc.getJsonValueStr("vLogo_image", categoryObj));
                            map.put("iVehicleCategoryId", generalFunc.getJsonValueStr("iVehicleCategoryId", categoryObj));
                            map.put("vCategoryBanner", generalFunc.getJsonValueStr("vCategoryBanner", categoryObj));
                            map.put("vBannerImage", generalFunc.getJsonValueStr("vBannerImage", categoryObj));
                            map.put("tBannerButtonText", generalFunc.getJsonValueStr("tBannerButtonText", categoryObj));
                            map.put("LBL_BOOK_NOW", LBL_BOOK_NOW);


                            if (SelectedVehicleTypeId.contains(generalFunc.getJsonValueStr("iVehicleCategoryId", categoryObj))) {
                                map.put("isCheck", "Yes");
                            } else {
                                map.put("isCheck", "No");
                            }
                            generalCategoryList.add((HashMap<String, String>) map.clone());

                        }
                    }

                    ufxCatAdapter = null;
                    ufxCatAdapter = new UberXCategoryAdapter(getActContext(), generalCategoryList, generalFunc);


                    ufxCatAdapter.setCategoryMode("1");
                    dataListRecyclerView.setAdapter(ufxCatAdapter);
                    ufxCatAdapter.notifyDataSetChanged();
                    ufxCatAdapter.setOnItemClickList(UberxFilterActivity.this);

                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr("message", responseObj)));
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    ArrayList<String> multiServiceSelect = new ArrayList<>();

    public Context getActContext() {
        return UberxFilterActivity.this;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onMultiItem(String id, boolean b) {

        if (multiServiceSelect.contains(id)) {
            if (!b) {
                while (multiServiceSelect.remove(id)) {
                }
            }

        } else {
            multiServiceSelect.add(id);
        }

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(UberxFilterActivity.this);
            int i = view.getId();


            if (i == backImgView.getId()) {
                onBackPressed();
            } else if (i == submitBtnId) {
                Bundle bn = new Bundle();

                String SelectedVehicleTypeId = "";
                if (multiServiceSelect.size() > 0) {

                    SelectedVehicleTypeId = android.text.TextUtils.join(",", multiServiceSelect);
                } else {
                    generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("Please Select Service", "LBL_SELECT_SERVICE_TXT"));
                    return;
                }
                bn.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                (new StartActProcess(getActContext())).setOkResult(bn);
                finish();

            }


        }
    }

    @Override
    public void onBackPressed() {
        Bundle bn = new Bundle();
        bn.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
        (new StartActProcess(getActContext())).setOkResult(bn);
        finish();
        super.onBackPressed();
    }
}
