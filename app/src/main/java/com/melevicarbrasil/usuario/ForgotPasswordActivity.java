package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements GenerateAlertBox.HandleAlertBtnClick {


    public String userProfileJson = "";

    MaterialEditText emailBox;
    GeneralFunctions generalFunc;
    String required_str = "";
    String error_email_str = "";
    MTextView forgotpasswordHint, forgotpasswordNote;

    int submitBtnId;
    InternetConnection intCheck;
    LinearLayout imgClose;
    MTextView btnTxt;
    LinearLayout btnArea;
    ImageView btnImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        setLabel();

    }

    public Context getActContext() {
        return ForgotPasswordActivity.this;
    }

    private void initView() {
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        intCheck = new InternetConnection(this);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        forgotpasswordHint = (MTextView) findViewById(R.id.forgotpasswordHint);
        forgotpasswordNote = (MTextView) findViewById(R.id.forgotpasswordNote);
        btnArea = (LinearLayout) findViewById(R.id.btnArea);
        btnTxt = (MTextView) findViewById(R.id.btnTxt);

        imgClose = (LinearLayout) findViewById(R.id.imgClose);
        btnImg = (ImageView) findViewById(R.id.btnImg);
        imgClose.setOnClickListener(new setOnClickList());


        submitBtnId = Utils.generateViewId();


        btnArea.setOnClickListener(new setOnClickList());

        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
        emailBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        if (generalFunc.isRTLmode()) {
            btnImg.setRotation(180);
            btnArea.setBackground(getActContext().getResources().getDrawable(R.drawable.login_border_rtl));
        }
    }

    private void setLabel() {
        emailBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_LBL_TXT"));
      //  required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD_ERROR_TXT");
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
     //   required_str = generalFunc.retrieveLangLBl("", "LBL_LOGIN");
        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR");
        forgotpasswordHint.setText(generalFunc.retrieveLangLBl("", "LBL_FORGET_YOUR_PASS_TXT"));
        forgotpasswordNote.setText(generalFunc.retrieveLangLBl("", "LBL_FORGET_PASS_NOTE"));
        btnTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SUBMIT_TXT"));

        emailBox.getLabelFocusAnimator().start();
    }

    @Override
    public void handleBtnClick(int btn_id) {
        Utils.hideKeyboard(getActContext());
        if (btn_id == 1) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void checkValues() {
        boolean emailEntered = Utils.checkText(emailBox) ?
                (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, error_email_str))
                : Utils.setErrorFields(emailBox, required_str);

        if (emailEntered == false) {
            return;
        }

        forgptPasswordCall();
    }

    public void forgptPasswordCall() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "requestResetPassword");
        parameters.put("vEmail", Utils.getText(emailBox));
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    emailBox.setText("");
                    GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
                    generateAlert.setBtnClickList(ForgotPasswordActivity.this);
                    generateAlert.showAlertBox();
                }else {
                    generalFunc.showGeneralMessage("",generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }


            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();

            if (i == btnArea.getId()) {

                if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                    generalFunc.showMessage(emailBox, generalFunc.retrieveLangLBl("No Internet Connection", "LBL_NO_INTERNET_TXT"));
                } else {
                    checkValues();

                }

            } else if (i == imgClose.getId()) {
                onBackPressed();

            }

        }
    }


}
