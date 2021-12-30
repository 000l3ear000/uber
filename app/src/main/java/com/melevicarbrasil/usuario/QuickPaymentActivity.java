package com.melevicarbrasil.usuario;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.anim.loader.AVLoadingIndicatorView;

import java.util.HashMap;

/**
 * Created by Esite on 01-05-2018.
 */

public class QuickPaymentActivity extends AppCompatActivity {

    WebView mWebView;
    boolean loadingFinished = true;
    boolean redirect = false;

    private int webViewPreviousState;
    private final int PAGE_STARTED = 0x1;
    private final int PAGE_REDIRECTED = 0x2;

    GeneralFunctions generalFunc;
    private AVLoadingIndicatorView loaderView;

    MTextView titleTxt;
    ImageView backImgView;
    private String OldUrl;


    HashMap<String, Object> mapData = new HashMap<>();
    public boolean isInProcessMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        mapData = (HashMap<String, Object>) getIntent().getSerializableExtra("data");

        init();

        titleTxt.setText(mapData.get("vPageTitle").toString());

        loaderView = findViewById(R.id.loaderView);
        mWebView.setWebViewClient(new myWebClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mapData.get("URL").toString());
        clearCookies(getApplication());
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("Api", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d("Api", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public class myWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            OldUrl = url;
            view.loadUrl(url);
            return true;

        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadingFinished = false;

            //SHOW LOADING IF IT ISN'T ALREADY VISIBLE
            super.onPageStarted(view, url, favicon);

            webViewPreviousState = PAGE_STARTED;
            loaderView.setVisibility(View.VISIBLE);


            if (url.contains("paymentprocess=yes")) {
                isInProcessMode = true;
            }

            if (!loadingFinished) {
                redirect = true;
            }


            if (url.contains("result.php?success=1")) {
                isInProcessMode = false;
                view.stopLoading();
                loaderView.setVisibility(View.GONE);

                Bundle bn = new Bundle();
                bn.putSerializable("data", mapData);
                (new StartActProcess(getActContext())).setOkResult(bn);

                finish();

            } else if (url.contains("result.php?success=0")) {
                isInProcessMode = false;
                view.stopLoading();
                loaderView.setVisibility(View.GONE);

                final GenerateAlertBox generateAlert = new GenerateAlertBox(QuickPaymentActivity.this);
                generateAlert.setCancelable(false);
                generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                    @Override
                    public void handleBtnClick(int btn_id) {
                        generateAlert.closeAlertBox();
                        finish();
                    }
                });
                generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_REQUEST_FAILED_PROCESS"));
                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
                generateAlert.showAlertBox();
            }


            OldUrl = url;
            loadingFinished = false;

        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            generalFunc.showError();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }

            if (webViewPreviousState == PAGE_STARTED) {
                loaderView.setVisibility(View.GONE);
            }

            if (loadingFinished && !redirect) {
                //HIDE LOADING IT HAS FINISHED
            } else {
                redirect = false;
            }
        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
            return true;
        }
    }

    private void init() {
        mWebView = findViewById(R.id.webView);
        titleTxt = findViewById(R.id.titleTxt);
        backImgView = findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());


    }

    public Context getActContext() {
        return QuickPaymentActivity.this;
    }

    @Override
    public void onBackPressed() {
        backImgView.performClick();
        return;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == R.id.backImgView) {

                if (isInProcessMode) {
                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("You cannot go back while your transaction is being processed. Please wait for transaction being completed.", "LBL_TRANSACTION_IN_PROCESS_TXT")));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_ANYWAY_TXT"));
                    generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                        @Override
                        public void handleBtnClick(int btn_id) {

                            generateAlert.closeAlertBox();
                            if (btn_id == 0) {
                                QuickPaymentActivity.super.onBackPressed();
                            }

                        }
                    });

                    generateAlert.showAlertBox();
                } else {
                    QuickPaymentActivity.super.onBackPressed();
                }
            }
        }
    }
}
