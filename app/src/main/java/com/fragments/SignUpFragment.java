package com.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.AppLoignRegisterActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.SelectCountryActivity;
import com.melevicarbrasil.usuario.SupportActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.OpenMainProfile;
import com.general.files.PasswordViewHideManager;
import com.general.files.SetOnTouchList;
import com.general.files.SetUserData;
import com.general.files.StartActProcess;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    static MaterialEditText countryBox;
    static String vCountryCode = "";
    static String vPhoneCode = "";
    static String vSImage = "";
    static boolean isCountrySelected = false;
    View view;
    GenerateAlertBox generateAlert;
    AppLoignRegisterActivity appLoginAct;
    GeneralFunctions generalFunc;
    MaterialEditText fNameBox;
    MaterialEditText lNameBox;
    MaterialEditText emailBox;
    MaterialEditText passwordBox;
    MaterialEditText invitecodeBox;
    MaterialEditText mobileBox;

    // SignUpFragment signUpFrag;
    ImageView inviteQueryImg;
    LinearLayout inviteCodeArea;
    String required_str = "";
    String error_email_str = "";

    MTextView signbootomHint;

    ImageView countrydropimage, countrydropimagerror;

    CheckBox checkboxTermsCond;
    MTextView txtTermsCond;

    MTextView btnTxt, titleTxt;
    LinearLayout btnArea;
    ImageView btnImg;
    LinearLayout imgClose;
    static ImageView countryimage;

    public static void setdata(int requestCode, int resultCode, Intent data) {

        if (requestCode == Utils.SELECT_COUNTRY_REQ_CODE && data != null) {

            vCountryCode = data.getStringExtra("vCountryCode");
            vPhoneCode = data.getStringExtra("vPhoneCode");
            isCountrySelected = true;
            vSImage = data.getStringExtra("vSImage");

            Picasso.get().load(vSImage).into(countryimage);

            GeneralFunctions generalFunctions = new GeneralFunctions(MyApp.getInstance().getCurrentAct());
            countryBox.setText("+" + generalFunctions.convertNumberWithRTL(vPhoneCode));
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        appLoginAct = (AppLoignRegisterActivity) getActivity();
        generalFunc = appLoginAct.generalFunc;
        generateAlert = new GenerateAlertBox(getActContext());

        fNameBox = (MaterialEditText) view.findViewById(R.id.fNameBox);
        lNameBox = (MaterialEditText) view.findViewById(R.id.lNameBox);
        emailBox = (MaterialEditText) view.findViewById(R.id.emailBox);
        countryBox = (MaterialEditText) view.findViewById(R.id.countryBox);
        mobileBox = (MaterialEditText) view.findViewById(R.id.mobileBox);
        passwordBox = (MaterialEditText) view.findViewById(R.id.passwordBox);
        invitecodeBox = (MaterialEditText) view.findViewById(R.id.invitecodeBox);
        signbootomHint = (MTextView) view.findViewById(R.id.signbootomHint);
        countryimage = view.findViewById(R.id.countryimage);

        countrydropimage = (ImageView) view.findViewById(R.id.countrydropimage);
        countrydropimagerror = (ImageView) view.findViewById(R.id.countrydropimagerror);
        checkboxTermsCond = (CheckBox) view.findViewById(R.id.checkboxTermsCond);
        txtTermsCond = (MTextView) view.findViewById(R.id.txtTermsCond);
        txtTermsCond.setOnClickListener(new setOnClickList());

        vCountryCode = generalFunc.retrieveValue(Utils.DefaultCountryCode);
        vPhoneCode = generalFunc.retrieveValue(Utils.DefaultPhoneCode);
        vSImage = generalFunc.retrieveValue(Utils.DefaultCountryImage);
        Picasso.get().load(vSImage).into(countryimage);

        int paddingValStart = (int) getResources().getDimension(R.dimen._35sdp);
        int paddingValEnd = (int) getResources().getDimension(R.dimen._12sdp);
        if (generalFunc.isRTLmode()){
            countryBox.setPaddings(paddingValEnd, 0, paddingValStart, 0);
            invitecodeBox.setPaddings(paddingValStart, 0, 0, 0);
        }else {
            countryBox.setPaddings(paddingValStart, 0, paddingValEnd, 0);
            invitecodeBox.setPaddings(0, 0, paddingValStart, 0);
        }


        if (!vPhoneCode.equalsIgnoreCase("")) {
            countryBox.setText("+" + generalFunc.convertNumberWithRTL(vPhoneCode));
            isCountrySelected = true;
        }
        inviteQueryImg = (ImageView) view.findViewById(R.id.inviteQueryImg);
        inviteCodeArea = (LinearLayout) view.findViewById(R.id.inviteCodeArea);



        inviteQueryImg.setOnClickListener(new setOnClickList());

        inviteCodeArea.setVisibility(View.GONE);

        if (generalFunc.isReferralSchemeEnable()) {
            inviteCodeArea.setVisibility(View.VISIBLE);
        }

        btnArea = (LinearLayout) view.findViewById(R.id.btnArea);
        btnTxt = (MTextView) view.findViewById(R.id.btnTxt);
        titleTxt = (MTextView) view.findViewById(R.id.titleTxt);

        btnImg = (ImageView) view.findViewById(R.id.btnImg);
        if (generalFunc.isRTLmode()) {
            btnImg.setRotation(180);
            btnArea.setBackground(getActContext().getResources().getDrawable(R.drawable.login_border_rtl));
        }
        btnArea.setOnClickListener(new setOnClickList());

        removeInput();
        setLabels();


        passwordBox.setTypeface(Typeface.DEFAULT);
        passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordBox.setTypeface(generalFunc.getDefaultFont(getActContext()));
        mobileBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
        fNameBox.setInputType(InputType.TYPE_CLASS_TEXT);
        lNameBox.setInputType(InputType.TYPE_CLASS_TEXT);

        fNameBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        lNameBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        emailBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        passwordBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mobileBox.setImeOptions(EditorInfo.IME_ACTION_DONE);

        passwordBox.setTypeface(generalFunc.getDefaultFont(getActContext()));

        new PasswordViewHideManager(getActContext(), passwordBox, generalFunc);
        countryBox.setShowClearButton(false);
        imgClose = (LinearLayout) view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new setOnClickList());
        return view;
    }

    public void removeInput() {
        Utils.removeInput(countryBox);

        if (generalFunc.retrieveValue("showCountryList").equalsIgnoreCase("Yes")) {
            // countrydropimage.setVisibility(View.GONE);
            countryBox.setOnTouchListener(new SetOnTouchList());

            countryBox.setOnClickListener(new setOnClickList());
        }
    }

    public void setLabels() {


        btnTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGNUP_SIGNUP"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGNUP"));

        fNameBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_FIRST_NAME_HEADER_TXT"));
        lNameBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_LAST_NAME_HEADER_TXT"));
        emailBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_LBL_TXT"));
        countryBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_COUNTRY_TXT"));
        mobileBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_MOBILE_NUMBER_HEADER_TXT"));
        passwordBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_PASSWORD_LBL_TXT"));

        signbootomHint.setText(generalFunc.retrieveLangLBl("", "LBL_ALREADY_HAVE_ACC"));


//        if (generalFunc.retrieveValue(Utils.MOBILE_VERIFICATION_ENABLE_KEY).equals("Yes")) {
//            btnTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
//        } else {
//            btnTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_REGISTER_TXT"));
//        }

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR");

        invitecodeBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_REFERRAL_CODE_HINT"), generalFunc.retrieveLangLBl("", "LBL_REFERRAL_CODE_HINT"));


        String termsfirstVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_PREFIX_TXT");
        String termsSecondVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION");
        String termsThirdVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_POSTFIX_TXT");
        String termsVal = termsfirstVal + " " + termsSecondVal + " " + termsThirdVal;
        manageSpanView(termsVal, termsSecondVal, txtTermsCond);

        String signupFirstVal = generalFunc.retrieveLangLBl("", "LBL_ALREADY_HAVE_ACC");
//        String signupSecondVal = generalFunc.retrieveLangLBl("", "LBL_SIGN_IN");
        String signupSecondVal = generalFunc.retrieveLangLBl("", "LBL_HEADER_TOPBAR_SIGN_IN_TXT");
        String signupThirdVal = generalFunc.retrieveLangLBl("", "LBL_NOW");
        String signupfinalVal = signupFirstVal + " " + signupSecondVal + " " + signupThirdVal;

        manageSpanView(signupfinalVal, signupSecondVal, signbootomHint);

    }


    ClickableSpan ClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {


            if (view == txtTermsCond) {
                Bundle bn = new Bundle();
                bn.putBoolean("islogin", true);
                new StartActProcess(getActContext()).startActWithData(SupportActivity.class, bn);
            } else if (view == signbootomHint) {
                if (!appLoginAct.isSignInFirst) {
                    appLoginAct.hadnleFragment(new SignInFragment(), true, false);
                }else {
                    appLoginAct.hadnleFragment(new SignInFragment(), false, false);
                }
                appLoginAct.signheaderHint.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_IN_WITH_SOC_ACC_HINT"));

            }

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.appThemeColor_1));    // you can use custom color
            ds.setUnderlineText(false);    // this remove the underline
        }
    };

    public void manageSpanView(String finalString, String clickAbleString, MTextView txtView) {
        String firstVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_PREFIX_TXT");
        String secondVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION");
        String thirdVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_POSTFIX_TXT");
        String finalVal = firstVal + " " + secondVal + " " + thirdVal;
        SpannableString termsSpan = new SpannableString(finalString);


        termsSpan.setSpan(
                ClickableSpan, // Span to add
                finalString.indexOf(clickAbleString), // Start of the span (inclusive)
                finalString.indexOf(clickAbleString) + String.valueOf(clickAbleString).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        );
        txtView.setText(termsSpan);
        txtView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void checkData() {
        Utils.hideKeyboard(getActContext());

        String noWhiteSpace = generalFunc.retrieveLangLBl("Password should not contain whitespace.", "LBL_ERROR_NO_SPACE_IN_PASS");
        String pass_length = generalFunc.retrieveLangLBl("Password must be", "LBL_ERROR_PASS_LENGTH_PREFIX")
                + " " + Utils.minPasswordLength + " " + generalFunc.retrieveLangLBl("or more character long.", "LBL_ERROR_PASS_LENGTH_SUFFIX");

        boolean fNameEntered = Utils.checkText(fNameBox) ? true : Utils.setErrorFields(fNameBox, required_str);
        boolean lNameEntered = Utils.checkText(lNameBox) ? true : Utils.setErrorFields(lNameBox, required_str);
        boolean emailEntered = Utils.checkText(emailBox) ?
                (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, error_email_str))
                : Utils.setErrorFields(emailBox, required_str);
        boolean mobileEntered = Utils.checkText(mobileBox) ? true : Utils.setErrorFields(mobileBox, required_str);
        boolean countryEntered = isCountrySelected ? true : false;
        boolean passwordEntered = Utils.checkText(passwordBox) ?
                (Utils.getText(passwordBox).contains(" ") ? Utils.setErrorFields(passwordBox, noWhiteSpace)
                        : (Utils.getText(passwordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(passwordBox, pass_length)))
                : Utils.setErrorFields(passwordBox, required_str);

        if (countryBox.getText().length() == 0) {
            countryEntered = false;
        }

        if (generalFunc.retrieveValue("showCountryList").equalsIgnoreCase("Yes")) {

            if (!countryEntered) {

                Utils.setErrorFields(countryBox, required_str);
                countrydropimagerror.setVisibility(View.VISIBLE);
                countrydropimage.setVisibility(View.GONE);
            } else {
                countrydropimage.setVisibility(View.VISIBLE);
                countrydropimagerror.setVisibility(View.GONE);

            }
        }

        if (mobileEntered) {
            mobileEntered = mobileBox.length() >= 3 ? true : Utils.setErrorFields(mobileBox, generalFunc.retrieveLangLBl("", "LBL_INVALID_MOBILE_NO"));
        }

        if (fNameEntered == false || lNameEntered == false || emailEntered == false || mobileEntered == false
                || countryEntered == false || passwordEntered == false) {
            return;
        }

//        if (!checkboxTermsCond.isChecked()) {
//            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_ACCEPT_TERMS_PRIVACY_ALERT"));
//            return;
//        }

        btnArea.setEnabled(false);
        if (generalFunc.retrieveValue(Utils.MOBILE_VERIFICATION_ENABLE_KEY).equals("Yes")) {

            checkUserExist();
        } else {
            registerUser();
        }
    }

    public void registerUser() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "signup");
        parameters.put("vFirstName", Utils.getText(fNameBox));
        parameters.put("vLastName", Utils.getText(lNameBox));
        parameters.put("vEmail", Utils.getText(emailBox));
        parameters.put("vPhone", Utils.getText(mobileBox));
        parameters.put("vPassword", Utils.getText(passwordBox));
        parameters.put("PhoneCode", vPhoneCode);
        parameters.put("CountryCode", vCountryCode);
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("vInviteCode", Utils.getText(invitecodeBox));
        parameters.put("UserType", Utils.userType);
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setIsDeviceTokenGenerate(true, "vDeviceToken", generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            btnArea.setEnabled(true);
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    new SetUserData(responseString, generalFunc, getActContext(), true);

                    appLoginAct.manageSinchClient();
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                    new OpenMainProfile(getActContext(),
                            generalFunc.getJsonValue(Utils.message_str, responseString), false, generalFunc).startProcess();
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

    public void checkUserExist() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "isUserExist");
        parameters.put("Email", Utils.getText(emailBox));
        parameters.put("Phone", Utils.getText(mobileBox));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            btnArea.setEnabled(true);

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    notifyVerifyMobile();
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

    public void notifyVerifyMobile() {
        Bundle bn = new Bundle();
        bn.putString("MOBILE", vPhoneCode + Utils.getText(mobileBox));
        bn.putString("msg", "DO_PHONE_VERIFY");
    }

    public Context getActContext() {
        return appLoginAct.getActContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.SELECT_COUNTRY_REQ_CODE && resultCode == appLoginAct.RESULT_OK && data != null) {

            vCountryCode = data.getStringExtra("vCountryCode");
            vPhoneCode = data.getStringExtra("vPhoneCode");
            vSImage = data.getStringExtra("vSImage");
            Log.d("TestData", "::" + vSImage);
            Picasso.get().load(vSImage).into(countryimage);
            isCountrySelected = true;
            countryBox.setTextColor(getResources().getColor(R.color.black));
        } else if (requestCode == Utils.VERIFY_MOBILE_REQ_CODE && resultCode == appLoginAct.RESULT_OK) {
            String MSG_TYPE = data == null ? "" : (data.getStringExtra("MSG_TYPE") == null ? "" : data.getStringExtra("MSG_TYPE"));
            if (!MSG_TYPE.equals("EDIT_PROFILE")) {
                registerUser();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActivity());
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (!isAdded()) {
                return;
            }
            Utils.hideKeyboard(getActivity());
            int i = view.getId();
            if (i == btnArea.getId()) {
                checkData();
            } else if (i == R.id.countryBox) {
                new StartActProcess(getActivity()).startActForResult(SelectCountryActivity.class, Utils.SELECT_COUNTRY_REQ_CODE);
            } else if (i == inviteQueryImg.getId()) {
                generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl(" What is Referral / Invite Code ?", "LBL_REFERAL_SCHEME_TXT"),
                        generalFunc.retrieveLangLBl("", "LBL_REFERAL_SCHEME"));

            } else if (i == imgClose.getId()) {
                appLoginAct.onBackPressed();
            }


        }
    }
}
