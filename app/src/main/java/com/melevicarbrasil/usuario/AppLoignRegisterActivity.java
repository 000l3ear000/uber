package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.WebDialog;
import com.facebook.login.widget.LoginButton;
import com.fragments.SignInFragment;
import com.fragments.SignUpFragment;
import com.general.files.GeneralFunctions;
import com.general.files.GetDeviceToken;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.RegisterFbLoginResCallBack;
import com.general.files.RegisterGoogleLoginResCallBack;
import com.general.files.RegisterLinkedinLoginResCallBack;
import com.general.files.RegisterTwitterLoginResCallBack;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import java.util.Arrays;

public class AppLoignRegisterActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 001;
    public MTextView titleTxt;
    public GeneralFunctions generalFunc;
    public MTextView signheaderHint, orTxt;
    ImageView backImgView;
    FrameLayout container;
    String type = "";
    ImageView imagefacebook, imagetwitter, imageGoogle, imagelinkedin;
    CallbackManager callbackManager;
    LoginButton loginButton;
    GoogleApiClient mGoogleApiClient;
    SignUpFragment signUpFragment;
    boolean isGoogleLogin = true;
    boolean isFacebookLogin = true;
    boolean isTwitterLogin = true;
    boolean isLinkdinLogin = true;
    LinearLayout socialarea;
    private TwitterLoginButton twitterloginButton;
    private InternetConnection intCheck;
    boolean isBack = false;

    private final static String LOGIN_TAG_FRAGMENT = "LOGIN_TAG_FRAGMENT";
    private final static String SIGNUP_TAG_FRAGMENT = "SIGNUP_TAG_FRAGMENT";
    public   boolean isSignInFirst=false;
    public   boolean isSignUpFirst=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("activityCreated", ":1:" + getClass().getSimpleName());

        try {
//            FacebookSdk.sdkInitialize(getActContext());
            WebDialog.setWebDialogTheme(R.style.FBDialogtheme);
            setContentView(R.layout.activity_app_loign_register);

            type = getIntent().getStringExtra("type");
            intCheck = new InternetConnection(this);


            TwitterAuthConfig authConfig = new TwitterAuthConfig(getActContext().getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getActContext().getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));

            TwitterConfig config = new TwitterConfig.Builder(this)
                    .logger(new DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(authConfig)
                    .debug(true)
                    .build();
            Twitter.initialize(config);


            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActContext())
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


            callbackManager = CallbackManager.Factory.create();
            initview();
            setLabel();
        } catch (Exception e) {

            Logger.d("exception oncreate()", e.toString());
        }

    }


    private void initview() {

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        FacebookSdk.setApplicationId(generalFunc.retrieveValue(Utils.FACEBOOK_APPID_KEY));
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());
        container = (FrameLayout) findViewById(R.id.container);
        socialarea = (LinearLayout) findViewById(R.id.socialarea);

        signheaderHint = (MTextView) findViewById(R.id.signheaderHint);
        orTxt = (MTextView) findViewById(R.id.orTxt);


        imagefacebook = (ImageView) findViewById(R.id.imagefacebook);
        imagetwitter = (ImageView) findViewById(R.id.imagetwitter);
        imageGoogle = (ImageView) findViewById(R.id.imageGoogle);
        imagelinkedin = (ImageView) findViewById(R.id.imagelinkedin);

        imagefacebook.setOnClickListener(new setOnClickList());
        imagetwitter.setOnClickListener(new setOnClickList());
        imageGoogle.setOnClickListener(new setOnClickList());
        imagelinkedin.setOnClickListener(new setOnClickList());

        if (generalFunc.retrieveValue(Utils.FACEBOOK_LOGIN).equalsIgnoreCase("NO")) {
            isFacebookLogin = false;
            imagefacebook.setVisibility(View.GONE);
        }

        if (generalFunc.retrieveValue(Utils.GOOGLE_LOGIN).equalsIgnoreCase("NO")) {
            isGoogleLogin = false;
            imageGoogle.setVisibility(View.GONE);
        }
        if (generalFunc.retrieveValue(Utils.TWITTER_LOGIN).equalsIgnoreCase("NO")) {
            isTwitterLogin = false;
            imagetwitter.setVisibility(View.GONE);
        }

        if (generalFunc.retrieveValue(Utils.LINKDIN_LOGIN).equalsIgnoreCase("NO")) {
            isLinkdinLogin = false;
            imagelinkedin.setVisibility(View.GONE);
        }

        if (!isTwitterLogin && !isGoogleLogin & !isFacebookLogin && !isLinkdinLogin) {
            socialarea.setVisibility(View.GONE);
            signheaderHint.setVisibility(View.GONE);

        }
        loginButton = new LoginButton(getActContext());

        twitterloginButton = new TwitterLoginButton(getActContext());
        twitterloginButton.setCallback(new RegisterTwitterLoginResCallBack(getActContext()));


        callbackManager = CallbackManager.Factory.create();

//        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_about_me"));
        // , "user_about_me" deprecated and not working
        //loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new RegisterFbLoginResCallBack(getActContext(), callbackManager));


    }

    public Context getActContext() {
        return AppLoignRegisterActivity.this;
    }


    private void setLabel() {

        if (type != null) {
            if (type.equals("login")) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_IN_TXT"));
                signheaderHint.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_IN_WITH_SOC_ACC_HINT"));
                hadnleFragment(new SignInFragment(), false, true);
                isSignInFirst=true;
            } else {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_UP"));
                signheaderHint.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_UP_WITH_SOC_ACC_HINT"));
                hadnleFragment(new SignUpFragment(), false, false);
                isSignUpFirst = true;
            }
        }
        orTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OR_TXT"));
    }

    public void hadnleFragment(Fragment fragment, boolean isback, boolean isLogin) {
        this.isBack = isback;
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, isLogin ? LOGIN_TAG_FRAGMENT : SIGNUP_TAG_FRAGMENT);
        if (isback) {
            transaction.addToBackStack(backStateName);
        } else {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            new RegisterGoogleLoginResCallBack(getActContext()).handleSignInResult(result);
        } else if (requestCode == Utils.SELECT_COUNTRY_REQ_CODE) {
            SignUpFragment.setdata(requestCode, resultCode, data);

        } else if (requestCode == 140) {
            // twitterloginButton.setCallback(new RegisterTwitterLoginResCallBack(getActContext()));
            twitterloginButton.onActivityResult(requestCode, resultCode, data);


        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {

        if (isBack) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            super.onBackPressed();
            isBack = false;
            return;
        }
        finish();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == backImgView.getId()) {
                Utils.hideKeyboard(AppLoignRegisterActivity.this);
                finish();

            } else if (i == imagefacebook.getId()) {
                if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                    generalFunc.showError();
                } else {
                    loginButton.performClick();
                }

            } else if (i == imagetwitter.getId()) {

                if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                    generalFunc.showError();
                } else {
                    twitterloginButton.performClick();
                }


            } else if (i == imageGoogle.getId()) {
                if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                    generalFunc.showError();
                } else {

                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            } else if (i == imagelinkedin.getId()) {
                if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
                    generalFunc.showError();
                } else {
                    RegisterLinkedinLoginResCallBack registerLinkedinLoginResCallBack = new RegisterLinkedinLoginResCallBack(getActContext());
                    registerLinkedinLoginResCallBack.continueLogin();


                }

            }
        }
    }


    public void manageSinchClient() {
        if (getSinchServiceInterface() != null && !getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient("Passenger" + "_" + generalFunc.getMemberId());

            GetDeviceToken getDeviceToken = new GetDeviceToken(generalFunc);

            getDeviceToken.setDataResponseListener(vDeviceToken -> {

                if (!vDeviceToken.equals("")) {
                    try {
                        getSinchServiceInterface().getSinchClient().registerPushNotificationData(vDeviceToken.getBytes());

                    } catch (Exception e) {

                    }
                }


            });
            getDeviceToken.execute();
        }
    }
}
