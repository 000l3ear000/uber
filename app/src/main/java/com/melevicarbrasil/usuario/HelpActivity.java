package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adapter.files.HelpCategoryRecycleAdapter;
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

public class HelpActivity extends AppCompatActivity implements HelpCategoryRecycleAdapter.OnItemClickList {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    ProgressBar loading;
    MTextView noHelpTxt;

    RecyclerView helpCategoryRecyclerView;
    HelpCategoryRecycleAdapter adapter;
    ErrorView errorView;

    ArrayList<HashMap<String, String>> list;
    ArrayList<String> answer = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        loading = (ProgressBar) findViewById(R.id.loading);
        noHelpTxt = (MTextView) findViewById(R.id.noHelpTxt);
        helpCategoryRecyclerView = (RecyclerView) findViewById(R.id.helpCategoryRecyclerView);
        errorView = (ErrorView) findViewById(R.id.errorView);

        list = new ArrayList<>();
        adapter = new HelpCategoryRecycleAdapter(getActContext(), list, generalFunc);
        helpCategoryRecyclerView.setAdapter(adapter);

        getHelpCategory();
        setLabels();

        backImgView.setOnClickListener(new setOnClickList());

        adapter.setOnItemClickList(this);
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAQ_TXT"));
    }

    @Override
    public void onItemClick(int position,String question,String answer) {
        Bundle bn = new Bundle();
       // bn.putString("QUESTION_LIST", list.get(position).get("QUESTION_LIST"));
        bn.putString("QUESTION", question);
        bn.putString("ANSWER", answer);
        new StartActProcess(getActContext()).startActWithData(QuestionAnswerActivity.class, bn);
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
        parameters.put("type", "getFAQ");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("appType", Utils.app_type);

        noHelpTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                noHelpTxt.setVisibility(View.GONE);

                if (responseString != null && !responseString.equals("")) {

                    closeLoader();

                    if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {

                        JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseString);

                        for (int i = 0; i < obj_arr.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
                            //map.put("QUESTION_LIST", obj_temp.toString());
                            list.add(map);

                            answer.add( generalFunc.getJsonValueStr("vTitle", obj_temp));
                            JSONArray obj_ques = generalFunc.getJsonArray("Questions", obj_temp.toString());
                            for (int i1 = 0; i1 < obj_ques.length(); i1++) {
                                JSONObject obj_temp1 = generalFunc.getJsonObject(obj_ques, i1);


                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("vSubTitle",generalFunc.getJsonValueStr("vTitle", obj_temp1));
                                map1.put("tAnswer",generalFunc.getJsonValueStr("tAnswer", obj_temp1));

                                list.add(map1);

                                answer.add(generalFunc.getJsonValueStr("vTitle", obj_temp1)+","+generalFunc.getJsonValueStr("tAnswer", obj_temp1));

                                //list.put(listDataHeader.get(i), answer);
                            }



                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        noHelpTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        noHelpTxt.setVisibility(View.VISIBLE);
                    }
                } else {
                    generateErrorView();
                }
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
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                getHelpCategory();
            }
        });
    }

    public Context getActContext() {
        return HelpActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    HelpActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
