package com.melevicarbrasil.usuario.deliverAll;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

public class ViewCardActivity extends AppCompatActivity {


    ImageView backImgView;
    MTextView titleTxt;
    GeneralFunctions generalFunc;

    MaterialEditText creditCardBox;
    String userProfileJson = "";

    MButton btn_change, btn_pay;
    private static final int SEL_CARD = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        creditCardBox = (MaterialEditText) findViewById(R.id.creditCardBox);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        btn_change = ((MaterialRippleLayout) findViewById(R.id.btn_change)).getChildView();
        btn_change.setId(Utils.generateViewId());
        btn_pay = ((MaterialRippleLayout) findViewById(R.id.btn_pay)).getChildView();
        btn_pay.setId(Utils.generateViewId());
        backImgView.setOnClickListener(new setOnClickList());
        btn_change.setOnClickListener(new setOnClickList());
        btn_pay.setOnClickListener(new setOnClickList());

        creditCardBox.setPaddings(10, 0, 0, 0);

        btn_pay.setText(generalFunc.retrieveLangLBl("", "LBL_PAY"));
        btn_change.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        creditCardBox.setClickable(false);
        creditCardBox.setFocusable(false);
        creditCardBox.setText(generalFunc.getJsonValue("vCreditCard", userProfileJson));

        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MYEARNING_PAYMENT_TXT"));
    }


    public void setLabel() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEL_CARD) {
            if (resultCode == RESULT_OK) {

            } else {

            }
        }

    }

    public Context getActContext() {
        return ViewCardActivity.this;
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == btn_pay.getId()) {

            } else if (i == btn_change.getId()) {
                new StartActProcess(getActContext()).startActForResult(PaymentCardActivity.class, SEL_CARD);
            }
        }
    }

}
