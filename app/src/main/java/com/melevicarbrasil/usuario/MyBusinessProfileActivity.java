package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

public class MyBusinessProfileActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    public ImageView backImgView;
    MTextView titleTxt;
    MTextView statusTxt, noteTxt;
    MaterialEditText emailBox, organizationBox;
    MButton btn_type2;
    int btnId;
    MTextView selprofilenoteTxt;

    HashMap<String, String> map;

    ImageView imageView;
    SelectableRoundedImageView editIconImgView;
    MTextView deletebtn;
    String eStatus = "";
    boolean isupdate = false;

    String error_email_str = "";
    String required_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_profile);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        statusTxt = (MTextView) findViewById(R.id.statusTxt);
        noteTxt = (MTextView) findViewById(R.id.noteTxt);
        selprofilenoteTxt = (MTextView) findViewById(R.id.selprofilenoteTxt);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        imageView = (ImageView) findViewById(R.id.imageView);
        deletebtn = (MTextView) findViewById(R.id.deletebtn);
        deletebtn.setText(generalFunc.retrieveLangLBl("", "LBL_DELETE_BUSINESS_PROFILE"));
        // noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_INACTIVE_BUSINESS_PROFILE"));
        deletebtn.setOnClickListener(new setOnClick());
        editIconImgView = (SelectableRoundedImageView) findViewById(R.id.editIconImgView);
        editIconImgView.setOnClickListener(new setOnClick());

        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_Dark_1), Utils.dipToPixels(getActContext(), 15), 0,
                getResources().getColor(R.color.appThemeColor_Dark_1), editIconImgView);

        editIconImgView.setColorFilter(getResources().getColor(R.color.appThemeColor_TXT_1));
        new CreateRoundedView(getResources().getColor(R.color.appThemeColor_hover_1), Utils.dipToPixels(getActContext(), 65), 2,
                getResources().getColor(R.color.appThemeColor_hover_1), imageView);

        btnId = Utils.generateViewId();
        btn_type2.setId(btnId);
        map = (HashMap<String, String>) getIntent().getSerializableExtra("data");
        selprofilenoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FINAL_BUSINESS_PROFILE_SET_NOTE"));

        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR_TXT");
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");

        String ProfileStatus=map.get("ProfileStatus");
        if (ProfileStatus != null) {
            if (ProfileStatus.equalsIgnoreCase("Pending")) {
                noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PENDING_BUSINESS_PROFILE"));

            } else if (ProfileStatus.equalsIgnoreCase("Inactive")) {
                noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_INACTIVE_BUSINESS_PROFILE"));
            } else if (ProfileStatus.equalsIgnoreCase("Terminate")) {
                noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TERMINATED_BUSINESS_PROFILE"));

            } else if (ProfileStatus.equalsIgnoreCase("Reject")) {
                noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_REJECTED_BUSINESS_PROFILE"));

            } else {
                if (ProfileStatus.equalsIgnoreCase("Active")) {
                    noteTxt.setText("");
                }
                editIconImgView.setVisibility(View.VISIBLE);
                deletebtn.setVisibility(View.VISIBLE);
                btn_type2.setVisibility(View.GONE);
            }
        } else {
            selprofilenoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BUSINESS_PROFILE_SET_NOTE"));
        }


        btn_type2.setOnClickListener(new setOnClick());
        organizationBox = (MaterialEditText) findViewById(R.id.organizationBox);

        String eProfileAdded=map.get("eProfileAdded");
        if (eProfileAdded != null && eProfileAdded.equalsIgnoreCase("Yes")) {
            statusTxt.setText(map.get("vProfileName"));
            btn_type2.setVisibility(View.GONE);
            deletebtn.setVisibility(View.VISIBLE);
        } else {
            statusTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ALL_SET"));
            btn_type2.setVisibility(View.VISIBLE);
            deletebtn.setVisibility(View.GONE);
        }


        emailBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_FOR_RECEIPT"));
        organizationBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_YOUR_ORGANIZATION"));
        emailBox.setText(map.get("email"));
        organizationBox.setText(map.get("vCompany"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_DONE"));


        Utils.removeInput(emailBox);
        Utils.removeInput(organizationBox);

        organizationBox.setHideUnderline(true);
        emailBox.setHideUnderline(true);
        backImgView.setOnClickListener(new setOnClick());
    }

    public void updateProfileData() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "UpdateUserOrganizationProfile");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        if (!eStatus.equalsIgnoreCase("")) {
            parameters.put("eStatus", eStatus);
        }
        if (isupdate) {
            parameters.put("vProfileEmail", emailBox.getText().toString().trim());
        } else {
            parameters.put("vProfileEmail", map.get("email"));
        }
        if (map.get("iUserProfileId") != null && !map.get("iUserProfileId").equalsIgnoreCase("")) {
            parameters.put("iUserProfileId", map.get("iUserProfileId"));
        }

        parameters.put("iUserProfileMasterId", map.get("iUserProfileMasterId"));
        parameters.put("iOrganizationId", map.get("iOrganizationId"));


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                if (isDataAvail == true) {

                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), null, generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), buttonId -> {


//                        MyApp.getInstance().restartWithGetDataApp();

                        new StartActProcess(getActContext()).startActClearTop(BusinessProfileActivity.class);
                        finish();
                    });
                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
            }
        });
        exeWebServer.execute();
    }


    public Context getActContext() {
        return MyBusinessProfileActivity.this;
    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == btn_type2.getId()) {

                boolean emailEntered = Utils.checkText(emailBox) ?
                        (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, error_email_str))
                        : Utils.setErrorFields(emailBox, required_str);
                if (emailEntered == false) {
                    return;
                }

                updateProfileData();
            } else if (i == R.id.editIconImgView) {
                emailBox.setHideUnderline(false);
                emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                emailBox.setFocusableInTouchMode(true);
                emailBox.setFocusable(true);
                emailBox.setOnTouchListener((v, event) -> {

                    return false;
                });
                btn_type2.setVisibility(View.VISIBLE);
                deletebtn.setVisibility(View.GONE);
                isupdate = true;
                btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_PROFILE_UPDATE_PAGE_TXT"));

            } else if (i == R.id.deletebtn) {

                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_CONFIRM_DELETE_BUSINESS_PROFILE")), generalFunc.retrieveLangLBl("", "LBL_NO"), generalFunc.retrieveLangLBl("", "LBL_YES"), buttonId -> {
                    if (buttonId == 1) {
                        eStatus = "Deleted";
                        updateProfileData();
                    }
                });
            }
        }

    }
}
