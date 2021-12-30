package com.melevicarbrasil.usuario;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MTextView;
import com.view.editBox.MaterialEditText;

public class BusinessSetupActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    public ImageView backImgView;
    MTextView titleTxt, emailNoteTxt;
    MaterialEditText emailBox;
    MTextView skipbtn, nextbtn;
    String userProfileJson;
    boolean emailEntered;
    String required_str = "";
    String error_email_str = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_setup);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR_TXT");
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        emailNoteTxt = (MTextView) findViewById(R.id.emailNoteTxt);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        skipbtn = (MTextView) findViewById(R.id.skipbtn);
        nextbtn = (MTextView) findViewById(R.id.nextbtn);
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        backImgView.setOnClickListener(new setOnClick());
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PROFILE_SETUP"));
        emailNoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BUSINESS_EMAIL_FOR_BILL"));
        emailBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_TEXT"));
        skipbtn.setText(generalFunc.retrieveLangLBl("", "LBL_SKIP"));
        nextbtn.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
        skipbtn.setOnClickListener(new setOnClick());
        nextbtn.setOnClickListener(new setOnClick());
        emailBox.setText(generalFunc.getJsonValue("vEmail", userProfileJson));

    }

    public Context getActContext() {
        return BusinessSetupActivity.this;
    }


    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == R.id.skipbtn) {
                Bundle bn = new Bundle();
                bn.putString("iUserProfileMasterId", getIntent().getStringExtra("iUserProfileMasterId"));
                bn.putString("email", generalFunc.getJsonValue("vEmail", userProfileJson));
                new StartActProcess(getActContext()).startActWithData(OrganizationActivity.class, bn);
            } else if (i == R.id.nextbtn) {
                emailEntered = Utils.checkText(emailBox) ?
                        (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, error_email_str))
                        : Utils.setErrorFields(emailBox, required_str);

                if (emailEntered == false) {
                    return;
                }

                Bundle bn = new Bundle();
                bn.putString("iUserProfileMasterId", getIntent().getStringExtra("iUserProfileMasterId"));
                bn.putString("email", emailBox.getText().toString().trim());
                new StartActProcess(getActContext()).startActWithData(OrganizationActivity.class, bn);
            }
        }

    }
}
