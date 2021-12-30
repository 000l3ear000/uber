package com.melevicarbrasil.usuario;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

public class ContactUsActivity extends AppCompatActivity {
    MTextView titleTxt;
    MTextView subheaderTxt;
    MTextView detailTxt;
    MTextView floatingLabel1,floatingLabel2;
    ImageView backImgView;
    GeneralFunctions generalFunc;
    MaterialEditText subjectBox;
    MaterialEditText contentBox;
    MButton btn_type2;
    String required_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        subheaderTxt = (MTextView) findViewById(R.id.subheaderTxt);
        detailTxt = (MTextView) findViewById(R.id.detailTxt);
        floatingLabel1 = (MTextView) findViewById(R.id.floatingLabel1);
        floatingLabel2 = (MTextView) findViewById(R.id.floatingLabel2);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        subjectBox = (MaterialEditText) findViewById(R.id.subjectBox);

        contentBox = (MaterialEditText) findViewById(R.id.contentBox);

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();


        setLabels();


        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());

        backImgView.setOnClickListener(new setOnClickList());
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_HEADER_TXT"));
        subheaderTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_SUBHEADER_TXT"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_QUERY_BTN_TXT"));
        detailTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_DETAIL_TXT"));


        floatingLabel1.setText(generalFunc.retrieveLangLBl("", "LBL_RES_TO_CONTACT"));
        subjectBox.setHint(generalFunc.retrieveLangLBl("", "LBL_ADD_SUBJECT_HINT_CONTACT_TXT"));
        subjectBox.setHideUnderline(true);

        subjectBox.setMetTextColor(Color.parseColor("#757575"));
        contentBox.setMetTextColor(Color.parseColor("#757575"));

        if (generalFunc.isRTLmode()) {
            subjectBox.setPaddings(0,0,(int)getResources().getDimension( R.dimen._10sdp),0);
        }else {
            subjectBox.setPaddings((int)getResources().getDimension( R.dimen._10sdp),0,0,0);
        }



        floatingLabel2.setText(generalFunc.retrieveLangLBl("", "LBL_YOUR_QUERY"));
        contentBox.setHint(generalFunc.retrieveLangLBl("", "LBL_ADD_SUBJECT_HINT_CONTACT_TXT"));
        contentBox.setHideUnderline(true);
        if (generalFunc.isRTLmode()) {
            contentBox.setPaddings(0,0,(int)getResources().getDimension( R.dimen._10sdp),0);
        }else {
            contentBox.setPaddings((int)getResources().getDimension( R.dimen._10sdp),0,0,0);
        }

        contentBox.setSingleLine(false);
        contentBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        contentBox.setGravity(Gravity.TOP);

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
    }

    public void submitQuery() {
        boolean subjectEntered = Utils.checkText(subjectBox);
        boolean contentEntered = Utils.checkText(contentBox);

        if (!subjectEntered || !contentEntered) {
            ((MTextView) findViewById(R.id.subjectBox_error)).setText(required_str);
            findViewById(R.id.subjectBox_error).setVisibility(!subjectEntered ? View.VISIBLE : View.INVISIBLE);

            ((MTextView) findViewById(R.id.contentBox_error)).setText(required_str);
            findViewById(R.id.contentBox_error).setVisibility(!contentEntered ? View.VISIBLE : View.INVISIBLE);
            return;
        }else {
            findViewById(R.id.subjectBox_error).setVisibility(View.INVISIBLE);
            findViewById(R.id.contentBox_error).setVisibility(View.INVISIBLE);
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "sendContactQuery");
        parameters.put("UserType", Utils.app_type);
        parameters.put("UserId", generalFunc.getMemberId());
        parameters.put("message", Utils.getText(contentBox));
        parameters.put("subject", Utils.getText(subjectBox));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    contentBox.setText("");
                    subjectBox.setText("");
                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public Context getActContext() {
        return ContactUsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == R.id.backImgView) {
                ContactUsActivity.super.onBackPressed();
            } else if (i == btn_type2.getId()) {
                submitQuery();
            }
        }
    }

}
