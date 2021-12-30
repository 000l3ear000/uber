package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fragments.AddCardFragment;
import com.fragments.ViewCardFragment;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

public class UfxPaymentActivity extends AppCompatActivity {


    public GeneralFunctions generalFunc;
    public String userProfileJson = "";
    MTextView titleTxt;
    ImageView backImgView;
    MButton goToOrderSummaryBtn;
    RadioGroup radioGroup;
    RadioButton radioPayOnline, radioCashPayment;
    RadioButton seleted;
    String finalOrderPrice = "";
    String CurrencySymbol = "";
    Bundle bundle = new Bundle();
    AddCardFragment addCardFrag;
    LinearLayout cardarea;
    ViewCardFragment viewCardFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ufx_payment);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        radioGroup = (RadioGroup) findViewById(R.id.radioGrp);
        radioPayOnline = (RadioButton) findViewById(R.id.radioPayOnline);
        radioCashPayment = (RadioButton) findViewById(R.id.radioCashPayment);


        radioCashPayment.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_PAYMENT_TXT"));
        radioPayOnline.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_ONLINE_TXT"));
        radioCashPayment.setChecked(true);

        cardarea = (LinearLayout) findViewById(R.id.cardarea);

        goToOrderSummaryBtn = ((MaterialRippleLayout) findViewById(R.id.goToOrderSummaryBtn)).getChildView();
        goToOrderSummaryBtn.setId(Utils.generateViewId());
        goToOrderSummaryBtn.setText(generalFunc.retrieveLangLBl("", "LBL_GOTO_ORDER_SUMMARY_TXT"));
        goToOrderSummaryBtn.setOnClickListener(new setOnClick());

        goToOrderSummaryBtn.setText(generalFunc.retrieveLangLBl("", "LBL_BOOK_NOW"));


        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHECKOUT_TXT"));
        ((MTextView) findViewById(R.id.howToPayTxt)).setText(generalFunc.retrieveLangLBl("How would you like to pay?", "LBL_HOW_TO_PAY_TXT"));

        CurrencySymbol = getIntent().getStringExtra("CurrencySymbol");

        backImgView.setOnClickListener(new setOnClick());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == radioPayOnline.getId()) {
                    cardarea.setVisibility(View.VISIBLE);
                    openViewCardFrag();

                } else {
                    cardarea.setVisibility(View.GONE);

                }

            }
        });
    }

    private void viewcard() {
        Bundle bundle = new Bundle();
        bundle.putString("PAGE_MODE", "ADD_CARD");
        addCardFrag = new AddCardFragment();
        addCardFrag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cardarea, addCardFrag).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Logger.d("Methods", "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("Methods", "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d("Methods", "onStart");
    }


    public Context getActContext() {
        return UfxPaymentActivity.this;
    }

    public void changeUserProfileJson(String userProfileJson) {
        this.userProfileJson = userProfileJson;

        Bundle bn = new Bundle();
        bn.putString("UserProfileJson", userProfileJson);
        new StartActProcess(getActContext()).setOkResult(bn);

        openViewCardFrag();

        generalFunc.showMessage(getCurrView(), generalFunc.retrieveLangLBl("", "LBL_INFO_UPDATED_TXT"));
    }

    public View getCurrView() {
        return generalFunc.getCurrentView(UfxPaymentActivity.this);
    }

    public void openViewCardFrag() {
//
        if (viewCardFrag != null) {
            viewCardFrag = null;
            addCardFrag = null;
            Utils.runGC();
        }
        viewCardFrag = new ViewCardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cardarea, viewCardFrag).commit();
    }

    public void openAddCardFrag(String mode) {

        if (addCardFrag != null) {
            addCardFrag = null;
            viewCardFrag = null;
            Utils.runGC();
        }

        Bundle bundle = new Bundle();
        bundle.putString("PAGE_MODE", mode);
        addCardFrag = new AddCardFragment();
        addCardFrag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cardarea, addCardFrag).commit();
    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int i = view.getId();
            if (i == R.id.backImgView) {
                new StartActProcess(getActContext()).setOkResult(bundle);
                UfxPaymentActivity.super.onBackPressed();
            } else if (i == goToOrderSummaryBtn.getId()) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                seleted = (RadioButton) findViewById(selectedId);

                bundle.putBoolean("isufxpayment", true);

                if (radioGroup.getCheckedRadioButtonId() != -1) {


                } else {
                    Toast.makeText(getActContext(), "" + generalFunc.retrieveLangLBl("", "LBL_PLEASE_SELECT_AT_LEAST_ONE_TXT"), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
