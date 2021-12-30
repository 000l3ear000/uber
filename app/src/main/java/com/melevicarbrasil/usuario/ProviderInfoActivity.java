package com.melevicarbrasil.usuario;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;

import java.util.HashMap;

public class ProviderInfoActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    MTextView descTxt, descTitleTxt;
    SelectableRoundedImageView bottomViewdriverImgView;
    MTextView nameTxt;
    SimpleRatingBar bottomViewratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_info);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());
        descTxt = (MTextView) findViewById(R.id.descTxt);
        descTitleTxt = (MTextView) findViewById(R.id.descTitleTxt);
        nameTxt = (MTextView) findViewById(R.id.bottomViewnameTxt);
        bottomViewratingBar = (SimpleRatingBar) findViewById(R.id.bottomViewratingBar);
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SERVICE_DESCRIPTION"));
        bottomViewratingBar.setRating(GeneralFunctions.parseFloatValue(0, getIntent().getStringExtra("average_rating")));
        String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + getIntent().getStringExtra("iDriverId") + "/" + getIntent().getStringExtra("driver_img");
        nameTxt.setText(getIntent().getStringExtra("name"));

        descTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ABOUT") + " " + getIntent().getStringExtra("fname"));


        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_no_pic_user)
                .error(R.mipmap.ic_no_pic_user)
                .into(((SelectableRoundedImageView) findViewById(R.id.bottomViewdriverImgView)));

        getProviderInfo();
    }

    public Context getActContext() {
        return ProviderInfoActivity.this;
    }

    public void getProviderInfo() {
        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getProviderServiceDescription");
        parameters.put("iDriverId", getIntent().getStringExtra("iDriverId"));


        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {


            if (responseString != null && !responseString.equals("")) {

                if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {
                    descTxt.setText(generalFunc.getJsonValue(Utils.message_str, responseString));


                } else {

                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), buttonId -> onBackPressed());

                }


            }

        });
        exeWebServer.execute();
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
