package com.melevicarbrasil.usuario;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fragments.BusinessProfileIntroFragment;
import com.fragments.BusinessProfileListFragment;
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

public class BusinessProfileActivity extends AppCompatActivity implements GeneralFunctions.OnAlertButtonClickListener {

    public GeneralFunctions generalFunc;
    public ImageView backImgView;
    MTextView titleTxt;
    BusinessProfileIntroFragment businessProfileIntroFragment;
    BusinessProfileListFragment businessProfileListFragment;
    ArrayList<HashMap<String, String>> listdata = new ArrayList<>();
    ErrorView errorView;

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        errorView = (ErrorView) findViewById(R.id.errorView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        backImgView.setOnClickListener(new setOnClick());
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PROFILE_SETUP"));
    }

    public void generateErrorView() {
        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");
        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> displayProfileList());
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayProfileList();
    }

    @Override
    public void onBackPressed() {
        if (businessProfileIntroFragment != null) {
            businessProfileIntroFragment = null;
            if (listdata.size() > 1) {
                OpenSetUpScreen(listdata);
            }
            else
            {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public Context getActContext() {
        return BusinessProfileActivity.this;
    }

    public void displayProfileList() {
        listdata = new ArrayList<>();
        mProgressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DisplayProfileList");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            mProgressBar.setVisibility(View.GONE);

            JSONObject responseObj=generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {
                if (generalFunc.checkDataAvail(Utils.action_str, responseObj) == true) {
                    JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseObj);
                    HashMap<String, String> map;
                    if (obj_arr != null && obj_arr.length() == 1) {
                        map = new HashMap<String, String>();
                        JSONObject intro_obj_temp = generalFunc.getJsonObject(obj_arr, 0);
                        map.put("vTitle", generalFunc.getJsonValueStr("vTitle", intro_obj_temp));
                        map.put("iUserProfileMasterId", generalFunc.getJsonValueStr("iUserProfileMasterId", intro_obj_temp));
                        map.put("vSubTitle", generalFunc.getJsonValueStr("vSubTitle", intro_obj_temp));
                        map.put("vScreenHeading", generalFunc.getJsonValueStr("vScreenHeading", intro_obj_temp));
                        map.put("vScreenTitle", generalFunc.getJsonValueStr("vScreenTitle", intro_obj_temp));
                        map.put("tDescription", generalFunc.getJsonValueStr("tDescription", intro_obj_temp));
                        map.put("vScreenButtonText", generalFunc.getJsonValueStr("vScreenButtonText", intro_obj_temp));
                        map.put("vImage", generalFunc.getJsonValueStr("vImage", intro_obj_temp));
                        map.put("vWelcomeImage", generalFunc.getJsonValueStr("vWelcomeImage", intro_obj_temp));
                        map.put("eStatus", generalFunc.getJsonValueStr("eStatus", intro_obj_temp));
                        map.put("vProfileName", generalFunc.getJsonValueStr("vProfileName", intro_obj_temp));
                        map.put("eProfileAdded", generalFunc.getJsonValueStr("eProfileAdded", intro_obj_temp));
                        map.put("email", generalFunc.getJsonValueStr("vProfileEmail", intro_obj_temp));
                        map.put("ProfileStatus", generalFunc.getJsonValueStr("ProfileStatus", intro_obj_temp));
                        map.put("vCompany", generalFunc.getJsonValueStr("vCompany", intro_obj_temp));
                        map.put("iOrganizationId", generalFunc.getJsonValueStr("iOrganizationId", intro_obj_temp));
                        map.put("iUserProfileId", generalFunc.getJsonValueStr("iUserProfileId", intro_obj_temp));

                        OpenProfileIntroScreen(map, false);

                    } else {
                        for (int i = 0; i < obj_arr.length(); i++) {
                            map = new HashMap<String, String>();
                            JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);
                            map.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
                            map.put("iUserProfileMasterId", generalFunc.getJsonValueStr("iUserProfileMasterId", obj_temp));
                            map.put("vSubTitle", generalFunc.getJsonValueStr("vSubTitle", obj_temp));
                            map.put("vScreenHeading", generalFunc.getJsonValueStr("vScreenHeading", obj_temp));
                            map.put("vScreenTitle", generalFunc.getJsonValueStr("vScreenTitle", obj_temp));
                            map.put("tDescription", generalFunc.getJsonValueStr("tDescription", obj_temp));
                            map.put("vScreenButtonText", generalFunc.getJsonValueStr("vScreenButtonText", obj_temp));
                            map.put("vImage", generalFunc.getJsonValueStr("vImage", obj_temp));
                            map.put("vWelcomeImage", generalFunc.getJsonValueStr("vWelcomeImage", obj_temp));
                            map.put("eStatus", generalFunc.getJsonValueStr("eStatus", obj_temp));
                            map.put("vProfileName", generalFunc.getJsonValueStr("vProfileName", obj_temp));
                            map.put("eProfileAdded", generalFunc.getJsonValueStr("eProfileAdded", obj_temp));
                            map.put("email", generalFunc.getJsonValueStr("vProfileEmail", obj_temp));
                            map.put("ProfileStatus", generalFunc.getJsonValueStr("ProfileStatus", obj_temp));
                            map.put("vCompany", generalFunc.getJsonValueStr("vCompany", obj_temp));
                            map.put("iOrganizationId", generalFunc.getJsonValueStr("iOrganizationId", obj_temp));
                            map.put("iUserProfileId", generalFunc.getJsonValueStr("iUserProfileId", obj_temp));

                            listdata.add(map);
                        }
                        OpenSetUpScreen(listdata);
                    }

                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseObj)), "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), buttonId -> onBackPressed());
                }
            } else {
                generateErrorView();
            }
        });
        exeWebServer.execute();

    }

    public void setupTitleTxt(String title) {
        titleTxt.setText(title);
    }

    public void OpenProfileIntroScreen(HashMap<String, String> map, boolean isMultipleProfileAdded) {
        if (map.get("eProfileAdded").equalsIgnoreCase("Yes")) {
            Bundle bn = new Bundle();
            bn.putSerializable("data", map);
            new StartActProcess(getActContext()).startActWithData(MyBusinessProfileActivity.class, bn);
            if (!isMultipleProfileAdded) {
                finish();
            }
        } else {
            if (businessProfileIntroFragment != null) {
                businessProfileIntroFragment = null;
                Utils.runGC();
            }
            setupTitleTxt(map.get("vScreenHeading"));

            businessProfileIntroFragment = new BusinessProfileIntroFragment();
            Bundle bn = new Bundle();
            bn.putSerializable("Data", map);

            businessProfileIntroFragment.setArguments(bn);
            try {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContent, businessProfileIntroFragment).commit();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void OpenSetUpScreen(ArrayList<HashMap<String, String>> map) {
        if (businessProfileListFragment != null) {
            businessProfileListFragment = null;

            Utils.runGC();
        }
        setupTitleTxt(generalFunc.retrieveLangLBl("", "LBL_PROFILE_SETUP"));
        businessProfileListFragment = new BusinessProfileListFragment();
        Bundle bn = new Bundle();
        bn.putSerializable("Data", map);

        businessProfileListFragment.setArguments(bn);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContent, businessProfileListFragment).commit();
    }

    @Override
    public void onAlertButtonClick(int buttonId) {

    }

    public class setOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            }
        }
    }


}
