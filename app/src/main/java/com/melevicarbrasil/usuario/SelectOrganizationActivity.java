package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.OnScrollTouchDelegate;
import com.general.files.StartActProcess;
import com.utils.Logger;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;
import com.adapter.files.OrganizationListItem;
import com.adapter.files.OrganizationPinnedSectionListAdapter;
import com.view.pinnedListView.PinnedSectionListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SelectOrganizationActivity extends AppCompatActivity implements OrganizationPinnedSectionListAdapter.OrganizationClick, OnScrollTouchDelegate {


    MTextView titleTxt;
    ImageView backImgView;
    GeneralFunctions generalFunc;
    ProgressBar loading;
    ErrorView errorView;
    MTextView noResTxt;
    PinnedSectionListView organization_list;
    ArrayList<OrganizationListItem> items_list;
    ArrayList<OrganizationListItem> temp_items_list;
    ImageView searchImgView;
    LinearLayout searcharea;
    View toolbarArea;
    MTextView cancelTxt;
    EditText searchTxt;
    ImageView imageCancel;
    MTextView noData;
    OrganizationPinnedSectionListAdapter organizationPinnedSectionListAdapter;
    MTextView headingTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_list);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        noResTxt = (MTextView) findViewById(R.id.noResTxt);
        noData = (MTextView) findViewById(R.id.noData);
        loading = (ProgressBar) findViewById(R.id.loading);
        errorView = (ErrorView) findViewById(R.id.errorView);
        organization_list = (PinnedSectionListView) findViewById(R.id.organization_list);
        organization_list.onTouchDegateListener(this);
        searchImgView = (ImageView) findViewById(R.id.searchImgView);
        searcharea = (LinearLayout) findViewById(R.id.searcharea);
        toolbarArea = (View) findViewById(R.id.toolbarArea);
        cancelTxt = (MTextView) findViewById(R.id.cancelTxt);
        searchTxt = (EditText) findViewById(R.id.searchTxt);
        headingTxt = (MTextView) findViewById(R.id.headingTxt);
        imageCancel = (ImageView) findViewById(R.id.imageCancel);
        searchImgView.setVisibility(View.VISIBLE);
        searchImgView.setOnClickListener(new setOnClickList());
        cancelTxt.setOnClickListener(new setOnClickList());
        imageCancel.setOnClickListener(new setOnClickList());
        headingTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_ORGANIZATION_LINK_TO"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PROFILE_SETUP"));
//        searchTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_SEARCH_ORGANIZATION"));
        searchTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_Search"));


        organization_list.setShadowVisible(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            organization_list.setFastScrollEnabled(true);
            organization_list.setFastScrollAlwaysVisible(true);
        }
        items_list = new ArrayList<>();
        temp_items_list = new ArrayList<>();


        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                filterOrganization(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        backImgView.setOnClickListener(new setOnClickList());
        getOrganizationList();


    }


    public void getOrganizationList() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DisplayOrganizationList");
        parameters.put("iUserProfileMasterId", getIntent().getStringExtra("iUserProfileMasterId"));

        noResTxt.setVisibility(View.GONE);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noResTxt.setVisibility(View.GONE);

            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {
                    items_list.clear();
                    organizationPinnedSectionListAdapter = new OrganizationPinnedSectionListAdapter(getActContext(), items_list);
                    organization_list.setAdapter(organizationPinnedSectionListAdapter);

                    organizationPinnedSectionListAdapter.setOrganizationClickListener(SelectOrganizationActivity.this);
                    items_list.clear();
                    organizationPinnedSectionListAdapter.notifyDataSetChanged();
                    JSONArray subListArr = generalFunc.getJsonArray(Utils.message_str, responseString);
                    for (int j = 0; j < subListArr.length(); j++) {
                        JSONObject subTempJson = generalFunc.getJsonObject(subListArr, j);
                        String vCompany = generalFunc.getJsonValueStr("vCompany", subTempJson);
                        OrganizationListItem organizationListItem = new OrganizationListItem(OrganizationListItem.ITEM, vCompany);
                        organizationListItem.setiOrganizationId(generalFunc.getJsonValueStr("iOrganizationId", subTempJson));
                        organizationListItem.setiUserProfileMasterId(generalFunc.getJsonValueStr("iUserProfileMasterId", subTempJson));
                        items_list.add(organizationListItem);
                        temp_items_list.add(organizationListItem);
                    }
                    if (subListArr == null || subListArr.length() == 0) {
                        noData.setText(generalFunc.retrieveLangLBl("", "LBL_NO_COUNTRY_AVAIL"));
                        noData.setVisibility(View.VISIBLE);
                    } else {
                        noData.setVisibility(View.GONE);
                    }
                    organizationPinnedSectionListAdapter.notifyDataSetChanged();
                } else {
                    noResTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ERROR_TXT"));
                    noResTxt.setVisibility(View.VISIBLE);
                }
            } else {
                generateErrorView();
            }
        });
        exeWebServer.execute();
    }

    protected void onSectionAdded(OrganizationListItem section, int sectionPosition) {
        //sections[sectionPosition] = section;
    }


    public Context getActContext() {
        return SelectOrganizationActivity.this;
    }

    @Override
    public void onTouchDelegate() {

    }

    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }


    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getOrganizationList());
    }


    @Override
    public void OrganizationClickList(OrganizationListItem organizationListItem) {
        Bundle bundle = new Bundle();
        bundle.putString("iOrganizationId", organizationListItem.getiOrganizationId());
        bundle.putString("vCompany", organizationListItem.text);
        new StartActProcess(getActContext()).setOkResult(bundle);


        finish();

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    SelectOrganizationActivity.super.onBackPressed();
                    break;
                case R.id.cancelTxt:
                    toolbarArea.setVisibility(View.VISIBLE);
                    searcharea.setVisibility(View.GONE);
                    filterOrganization("");
                    Utils.hideKeyboard(getActContext());
                    break;
                case R.id.searchImgView:
                    searchTxt.setText("");
                    if (items_list != null) {
                        searcharea.setVisibility(View.VISIBLE);
                        toolbarArea.setVisibility(View.GONE);

                        searchTxt.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.showSoftInput(searchTxt, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                    break;
                case R.id.imageCancel:
                    searchTxt.setText("");
                    break;
            }
        }
    }

    private void filterOrganization(String searchText) {
        Locale locale = Locale.US;
        String keypad_locale = "";
        try {
            // get current keypade locale
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
            keypad_locale = ims.getLocale();
            Logger.d("Locale", "KeyPadLocale" + keypad_locale);
            locale = new Locale(keypad_locale);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<OrganizationListItem> items_list_tmp = new ArrayList<>();
        HashMap<Integer, OrganizationListItem> sectionMapData = new HashMap<>();
        ArrayList<OrganizationListItem> items_list_sub_tmp = new ArrayList<>();
        for (int j = 0; j < temp_items_list.size(); j++) {
            String vCompany = temp_items_list.get(j).text;
            String iOrganizationId = temp_items_list.get(j).getiOrganizationId();

            if (searchText.trim().equals("") || vCompany.toUpperCase().startsWith(searchText.trim().toUpperCase(locale))) {
                OrganizationListItem organizationListItem = new OrganizationListItem(OrganizationListItem.ITEM, vCompany);
                organizationListItem.setiOrganizationId(iOrganizationId);
                items_list_sub_tmp.add(organizationListItem);
            }
        }
        if (items_list_sub_tmp.size() > 0) {
            items_list_tmp.addAll(items_list_sub_tmp);
        }
        for (Integer currentKey : sectionMapData.keySet()) {
            onSectionAdded(sectionMapData.get(currentKey), currentKey);
        }
        items_list.clear();
        items_list.addAll(items_list_tmp);
        organizationPinnedSectionListAdapter = new OrganizationPinnedSectionListAdapter(getActContext(), items_list);
        organization_list.setAdapter(organizationPinnedSectionListAdapter);
        organizationPinnedSectionListAdapter.setOrganizationClickListener(SelectOrganizationActivity.this);
        organizationPinnedSectionListAdapter.notifyDataSetChanged();
    }

}
