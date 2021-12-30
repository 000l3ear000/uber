package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.dialogs.OpenListView;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.anim.loader.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AppLoginActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;

    MTextView languageText, currancyText, loginbtn, registerbtn;

    LinearLayout languagearea, currencyarea;

    LinearLayout languageCurrancyArea;

    GenerateAlertBox languageListAlertBox;

    String selected_language_code = "";

    ArrayList<HashMap<String, String>> languageDataList = new ArrayList<>();
    ArrayList<HashMap<String, String>> currencyDataList = new ArrayList<>();

    String selected_currency = "";
    String selected_currency_symbol = "";

    GenerateAlertBox currencyListAlertBox;

    String type = "";

    AVLoadingIndicatorView loaderView;
    InternetConnection intCheck;
    LinearLayout btnArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_app_login);


        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        intCheck = new InternetConnection(getActContext());
        generalFunc.getHasKey(getActContext());
        initview();
        setLabel();
        buildLanguageList();


    }

    private void initview() {


        btnArea=(LinearLayout)findViewById(R.id.btnArea);
       // introductondetailstext = (MTextView) findViewById(R.id.introductondetailstext);
        languageText = (MTextView) findViewById(R.id.languageText);
        currancyText = (MTextView) findViewById(R.id.currancyText);

        languagearea = (LinearLayout) findViewById(R.id.languagearea);
        currencyarea = (LinearLayout) findViewById(R.id.currencyarea);
        loginbtn = (MTextView) findViewById(R.id.loginbtn);
        registerbtn = (MTextView) findViewById(R.id.registerbtn);

        loaderView = (AVLoadingIndicatorView) findViewById(R.id.loaderView);
        loaderView.setVisibility(View.GONE);

        languageCurrancyArea = (LinearLayout) findViewById(R.id.languageCurrancyArea);

        loginbtn.setOnClickListener(new setOnClickAct());
        registerbtn.setOnClickListener(new setOnClickAct());
        languagearea.setOnClickListener(new setOnClickAct());
        currencyarea.setOnClickListener(new setOnClickAct());


    }


    private void setLabel() {
       // introductondetailstext.setText(generalFunc.retrieveLangLBl("", "LBL_HOME_PASSENGER_INTRO_DETAILS"));
        loginbtn.setText(generalFunc.retrieveLangLBl("", "LBL_LOGIN"));
        registerbtn.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_REGISTER_TXT"));

        languageText.setText(generalFunc.retrieveValue(Utils.DEFAULT_LANGUAGE_VALUE));
        currancyText.setText(generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));


    }
    int selCurrancyPosition = -1;
    int selLanguagePosition = -1;

    public void buildLanguageList() {

        JSONArray languageList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.LANGUAGE_LIST_KEY));
        languageDataList.clear();
        for (int i = 0; i < languageList_arr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(languageList_arr, i);

            HashMap<String, String> mapData = new HashMap<>();
            mapData.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
            mapData.put("vCode", generalFunc.getJsonValueStr("vCode", obj_temp));

            if (Utils.getText(languageText).equalsIgnoreCase(generalFunc.getJsonValueStr("vTitle", obj_temp))) {
                selLanguagePosition = i;
            }

            languageDataList.add(mapData);

            if ((generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY)).equals(generalFunc.getJsonValue("vCode", obj_temp))) {
                selected_language_code = generalFunc.getJsonValueStr("vCode", obj_temp);

            }
        }


        setLanguageArea();

        buildCurrencyList();

    }

    public void setLanguageArea() {
        if (languageDataList.size() < 2 || generalFunc.retrieveValue("LANGUAGE_OPTIONAL").equalsIgnoreCase("Yes")) {
            languagearea.setVisibility(View.GONE);
        }
    }

    public void buildCurrencyList() {

        JSONArray currencyList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.CURRENCY_LIST_KEY));
        currencyDataList.clear();
        for (int i = 0; i < currencyList_arr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(currencyList_arr, i);

            HashMap<String, String> mapData = new HashMap<>();
            mapData.put("vName", generalFunc.getJsonValueStr("vName", obj_temp));
            mapData.put("vSymbol", generalFunc.getJsonValueStr("vSymbol", obj_temp));

            if (Utils.getText(currancyText).equalsIgnoreCase(generalFunc.getJsonValueStr("vName", obj_temp))) {
                selCurrancyPosition = i;
            }

            currencyDataList.add(mapData);

        }

        if (currencyDataList.size() < 2 || generalFunc.retrieveValue("CURRENCY_OPTIONAL").equalsIgnoreCase("Yes")) {
            currencyarea.setVisibility(View.GONE);

            setLanguageArea();
        }
    }

    public Context getActContext() {
        return AppLoginActivity.this;
    }


    public String getSelectLangText() {
        return ("" + generalFunc.retrieveLangLBl("Select", "LBL_SELECT_LANGUAGE_HINT_TXT"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void showLanguageList() {

        OpenListView.getInstance(getActContext(), getSelectLangText(), languageDataList, OpenListView.OpenDirection.CENTER, true, position -> {


            selLanguagePosition = position;
            HashMap<String, String> mapData = languageDataList.get(position);

            selected_language_code = mapData.get("vCode");

            if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
                generalFunc.showGeneralMessage("",
                        generalFunc.retrieveLangLBl("No Internet Connection", "LBL_NO_INTERNET_TXT"));
            } else {
                if (!generalFunc.retrieveValue(Utils.DEFAULT_LANGUAGE_VALUE).equals(mapData.get("vTitle"))) {
                    languageText.setText(mapData.get("vTitle"));
                    generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                    generalFunc.storeData(Utils.DEFAULT_LANGUAGE_VALUE, mapData.get("vTitle"));

                    changeLanguagedata(selected_language_code);
                }
            }
        }).show(selLanguagePosition, "vTitle");
    }

    public void showCurrencyList() {
        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SELECT_CURRENCY"), currencyDataList, OpenListView.OpenDirection.CENTER, true, position -> {


            selCurrancyPosition = position;
            HashMap<String, String> mapData = currencyDataList.get(position);

            selected_currency_symbol = mapData.get("vSymbol");

            selected_currency = mapData.get("vName");
            currancyText.setText(mapData.get("vName"));

            generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
        }).show(selCurrancyPosition, "vName");
    }

    public void changeLanguagedata(String langcode) {
        loaderView.setVisibility(View.VISIBLE);
        btnArea.setVisibility(View.GONE);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "changelanguagelabel");
        parameters.put("vLang", langcode);
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {


                    generalFunc.storeData(Utils.languageLabelsKey, generalFunc.getJsonValue(Utils.message_str, responseString));
                    generalFunc.storeData(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", responseString));

                    GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            loaderView.setVisibility(View.GONE);
                            btnArea.setVisibility(View.VISIBLE);
                           // generalFunc.restartApp();

                            MyApp.getInstance().refreshWithConfigData();
                            setLabel();
                        }
                    }, 2000);


                } else {
                    loaderView.setVisibility(View.GONE);
                    btnArea.setVisibility(View.VISIBLE);

                }
            } else {
                loaderView.setVisibility(View.GONE);
                btnArea.setVisibility(View.VISIBLE);

            }

        });
        exeWebServer.execute();
    }

    public class setOnClickAct implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == R.id.languagearea) {

                if (loaderView.getVisibility() == View.GONE) {
                    showLanguageList();
                }

            } else if (i == R.id.currencyarea) {
                if (loaderView.getVisibility() == View.GONE) {
                    showCurrencyList();
                }
            } else if (i == R.id.loginbtn) {
                if (loaderView.getVisibility() == View.GONE) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "login");

                    new StartActProcess(getActContext()).startActWithData(AppLoignRegisterActivity.class, bundle);
                }


            } else if (i == R.id.registerbtn) {
                if (loaderView.getVisibility() == View.GONE) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "register");
                    new StartActProcess(getActContext()).startActWithData(AppLoignRegisterActivity.class, bundle);
                }

            }
        }


    }

}
