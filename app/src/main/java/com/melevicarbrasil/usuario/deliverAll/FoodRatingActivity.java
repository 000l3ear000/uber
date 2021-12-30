package com.melevicarbrasil.usuario.deliverAll;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;
import com.view.simpleratingbar.SimpleRatingBar;

import java.util.HashMap;

public class FoodRatingActivity extends AppCompatActivity {


    ImageView backImgView;
    MTextView titleTxt, orderMsg;
    MTextView ratingResNameTxt, ratingDriverNameTxt;
    MaterialEditText res_commentBox, driver_commentBox;
    GeneralFunctions generalFun;
    MButton btn_type2;
    int submitBtnId;
    SimpleRatingBar ratingBar_res, ratingBar_driver;

    String userProfileJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_rating);

        generalFun = MyApp.getInstance().getGeneralFun(getActContext());
        userProfileJson = generalFun.retrieveValue(Utils.USER_PROFILE_JSON);
        titleTxt = (MTextView) findViewById(R.id.ordertitleTxt);
        orderMsg = (MTextView) findViewById(R.id.orderMsg);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        ratingResNameTxt = (MTextView) findViewById(R.id.ratingResNameTxt);
        ratingDriverNameTxt = (MTextView) findViewById(R.id.ratingDriverNameTxt);
        res_commentBox = (MaterialEditText) findViewById(R.id.res_commentBox);
        driver_commentBox = (MaterialEditText) findViewById(R.id.driver_commentBox);
        ratingBar_res = (SimpleRatingBar) findViewById(R.id.ratingBar_res);
        ratingBar_driver = (SimpleRatingBar) findViewById(R.id.ratingBar_driver);

        backImgView.setOnClickListener(new setOnClickList());
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        res_commentBox.setHint(generalFun.retrieveLangLBl("Add Special Instruction for provider.", "LBL_RESTAURANT_RATING_NOTE"));
        driver_commentBox.setHint(generalFun.retrieveLangLBl("Add Special Instruction for provider.", "LBL_DRIVER_RATING_NOTE"));
        titleTxt.setText(generalFun.retrieveLangLBl("", "LBL_RATING"));
        orderMsg.setVisibility(View.VISIBLE);
        orderMsg.setText("(" + generalFun.retrieveLangLBl("", "LBL_ORDER") + "#" + generalFun.getJsonValue("LastOrderNo", userProfileJson) + ")");
        btn_type2.setText(generalFun.retrieveLangLBl("", "LBL_ENTER_DELIVERY_RATING"));

        ratingBar_res.setRating(getIntent().getFloatExtra("rating", 0));

        btn_type2.setOnClickListener(new setOnClickList());

        ratingResNameTxt.setText(generalFun.getJsonValue("LastOrderCompanyName", userProfileJson));
        ratingDriverNameTxt.setText(generalFun.retrieveLangLBl("", "LBL_RATE_DELIVERY_BY") + " " + generalFun.getJsonValue("LastOrderDriverName", userProfileJson));


    }


    public void ratingFood() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "submitRating");
        parameters.put("iMemberId", generalFun.getMemberId());
        parameters.put("iOrderId", generalFun.getJsonValue("LastOrderId", userProfileJson));

        parameters.put("rating", ratingBar_res.getRating() + "");
        parameters.put("message", res_commentBox.getText().toString());
        parameters.put("rating1", ratingBar_driver.getRating() + "");
        parameters.put("message1", driver_commentBox.getText().toString());

        parameters.put("eFromUserType", Utils.userType);
        parameters.put("eToUserType", "Company");
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFun);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {


                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail == true) {

                        generalFun.storeData(Utils.USER_PROFILE_JSON, generalFun.getJsonValue(Utils.message_str_one, responseString));

                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
//                    generateAlert.setSystemAlertWindow(true);
                        generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                            @Override
                            public void handleBtnClick(int btn_id) {
                                // MyApp.getInstance().restartWithGetDataApp();
                                finish();

                            }
                        });
                        generateAlert.setContentMessage("", generalFun.retrieveLangLBl("", generalFun.getJsonValue(Utils.message_str, responseString)));
                        generateAlert.setPositiveBtn(generalFun.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.showAlertBox();


                    } else {
                        generalFun.showGeneralMessage("",
                                generalFun.retrieveLangLBl("", generalFun.getJsonValue(Utils.message_str, responseString)));
                    }
                }
            }
        });
        exeWebServer.execute();
    }


    private Activity getActContext() {
        return FoodRatingActivity.this;
    }


    public class setOnClickList implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                FoodRatingActivity.super.onBackPressed();
            } else if (i == submitBtnId) {
                ratingFood();

            }
        }
    }
}
