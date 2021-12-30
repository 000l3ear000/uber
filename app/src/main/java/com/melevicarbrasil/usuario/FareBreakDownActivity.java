package com.melevicarbrasil.usuario;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FareBreakDownActivity extends AppCompatActivity {


    MTextView titleTxt;
    ImageView backImgView;
    GeneralFunctions generalFunc;
    MTextView fareBreakdownNoteTxt;
    MTextView carTypeTitle;
    LinearLayout fareDetailDisplayArea;

    String selectedcar = "";
    String iUserId = "";
    String distance = "";
    String time = "";
    String PromoCode = "";
    String vVehicleType = "";

    boolean isSkip;
    boolean isFixFare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_break_down);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        fareBreakdownNoteTxt = (MTextView) findViewById(R.id.fareBreakdownNoteTxt);
        carTypeTitle = (MTextView) findViewById(R.id.carTypeTitle);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickAct());
        fareDetailDisplayArea = (LinearLayout) findViewById(R.id.fareDetailDisplayArea);
        selectedcar = getIntent().getStringExtra("SelectedCar");
        iUserId = getIntent().getStringExtra("iUserId");
        distance = getIntent().getStringExtra("distance");
        time = getIntent().getStringExtra("time");
        PromoCode = getIntent().getStringExtra("PromoCode");
        vVehicleType = getIntent().getStringExtra("vVehicleType");
        isFixFare = getIntent().getBooleanExtra("isFixFare", false);
        setLabels();
        isSkip = getIntent().getBooleanExtra("isSkip", false);
        callBreakdownRequest();

    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FARE_BREAKDOWN_TXT"));
        if (isFixFare || getIntent().hasExtra("eFly")) {
            fareBreakdownNoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GENERAL_NOTE_FLAT_FARE_EST"));
        } else {
            fareBreakdownNoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GENERAL_NOTE_FARE_EST"));
        }
        carTypeTitle.setText(vVehicleType);
    }

    public Context getActContext() {
        return FareBreakDownActivity.this;
    }

    public void callBreakdownRequest() {


        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getEstimateFareDetailsArr");
        parameters.put("iUserId", generalFunc.getMemberId());

        parameters.put("distance", distance);
        parameters.put("time", time);
        parameters.put("SelectedCar", selectedcar);
        parameters.put("PromoCode", PromoCode);

        if (!isSkip) {
            parameters.put("isDestinationAdded", "Yes");

            String destLat=getIntent().getStringExtra("destLat");

            if (destLat != null && !destLat.equalsIgnoreCase("")) {
                parameters.put("DestLatitude", destLat);
                parameters.put("DestLongitude", getIntent().getStringExtra("destLong"));
            }

            String picupLat=getIntent().getStringExtra("picupLat");

            if (picupLat != null && !picupLat.equalsIgnoreCase("")) {
                parameters.put("StartLatitude", picupLat);
                parameters.put("EndLongitude", getIntent().getStringExtra("pickUpLong"));
            }

            if (getIntent().hasExtra("eFly")) {

                parameters.put("iFromStationId", getIntent().getStringExtra("iFromStationId"));
                parameters.put("iToStationId",  getIntent().getStringExtra("iToStationId"));
                parameters.put("eFly", getIntent().getStringExtra("eFly"));
            }
        } else {
            parameters.put("isDestinationAdded", "No");


        }


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseObj=generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                if (isDataAvail == true) {


                    JSONArray FareDetailsArrNewObj = null;
                    FareDetailsArrNewObj = generalFunc.getJsonArray(Utils.message_str, responseObj);
                    addFareDetailLayout(FareDetailsArrNewObj);


                } else {

                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();

    }

    private void addFareDetailLayout(JSONArray jobjArray) {

        if (fareDetailDisplayArea.getChildCount() > 0) {
            fareDetailDisplayArea.removeAllViewsInLayout();
        }

        for (int i = 0; i < jobjArray.length(); i++) {
            JSONObject jobject = generalFunc.getJsonObject(jobjArray, i);
            try {
                String data = jobject.names().getString(0);
                addFareDetailRow(data, jobject.get(data).toString(), (jobjArray.length() - 1) == i ? true : false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addFareDetailRow(String row_name, String row_value, boolean isLast) {
        View convertView = null;
        if (row_name.equalsIgnoreCase("eDisplaySeperator")) {
            convertView = new View(getActContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(getActContext(), 1));
            int dip=Utils.dipToPixels(getActContext(), 10);
            params.setMarginStart(dip);
            params.setMarginEnd(dip);
            convertView.setBackgroundColor(Color.parseColor("#dedede"));
            convertView.setLayoutParams(params);
        } else {
            LayoutInflater infalInflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.design_fare_breakdown_row, null);

            convertView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            MTextView titleHTxt = (MTextView) convertView.findViewById(R.id.titleHTxt);
            MTextView titleVTxt = (MTextView) convertView.findViewById(R.id.titleVTxt);

            titleHTxt.setText(generalFunc.convertNumberWithRTL(row_name));
            titleVTxt.setText(generalFunc.convertNumberWithRTL(row_value));


            if(isLast)
            {
                convertView.setMinimumHeight(Utils.dipToPixels(getActContext(), 40));

                titleHTxt.setTextColor(getResources().getColor(R.color.black));
                titleHTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Poppins_SemiBold.ttf");
                titleHTxt.setTypeface(face);
                titleVTxt.setTypeface(face);
                titleVTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                titleVTxt.setTextColor(getResources().getColor(R.color.appThemeColor_1));
            }
        }



        if (convertView != null)
            fareDetailDisplayArea.addView(convertView);
    }

    public class setOnClickAct implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    FareBreakDownActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
