package com.melevicarbrasil.usuario;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.adapter.files.DonationBannerAdapter;
import com.adapter.files.DonationBannerAdapter.OnBannerItemClickList;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DonationActivity extends AppCompatActivity implements OnBannerItemClickList {

    MTextView titleTxt;
    ImageView backImgView;
    GeneralFunctions generalFunc;
    RecyclerView donateListRecyclerView;
    DonationBannerAdapter donationBannerAdapter;
    ArrayList<HashMap<String, String>> donationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        generalFunc = new GeneralFunctions(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        donateListRecyclerView = (RecyclerView) findViewById(R.id.donateListRecyclerView);
        backImgView.setOnClickListener(new setOnClickList());
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DONATE"));

        donationBannerAdapter = new DonationBannerAdapter(getActContext(), donationList);
        donationBannerAdapter.setOnItemClickList(this);
        donateListRecyclerView.setAdapter(donationBannerAdapter);
        getDonationDetails();
    }

    public void getDonationDetails() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDonation");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    JSONArray arr = generalFunc.getJsonArray(Utils.message_str, responseString);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj_temp = generalFunc.getJsonObject(arr, i);


                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("tTitle", generalFunc.getJsonValue("tTitle", obj_temp.toString()));
                        hashMap.put("tDescription", generalFunc.getJsonValue("tDescription", obj_temp.toString()));
                        hashMap.put("tDescription", generalFunc.getJsonValue("tDescription", obj_temp.toString()));
                        hashMap.put("vImage", generalFunc.getJsonValue("vImage", obj_temp.toString()));
                        hashMap.put("tLink", generalFunc.getJsonValue("tLink", obj_temp.toString()));
                        donationList.add(hashMap);
                    }
                    donationBannerAdapter.notifyDataSetChanged();
                } else {

                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), buttonId -> onBackPressed());
                }
            }
        });
        exeWebServer.execute();
    }

    public Context getActContext() {
        return DonationActivity.this;
    }

    @Override
    public void onBannerItemClick(int position) {
        Logger.d("URL", "::" + donationList.get(position).get("tLink"));
        (new StartActProcess(getActContext())).openURL(donationList.get(position).get("tLink"), BuildConfig.APPLICATION_ID, this.getLocalClassName());

    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());


            switch (view.getId()) {
                case R.id.backImgView:
                    onBackPressed();
                    break;


            }
        }
    }
}
