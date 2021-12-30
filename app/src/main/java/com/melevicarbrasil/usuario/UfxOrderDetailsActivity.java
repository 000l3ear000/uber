package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

public class UfxOrderDetailsActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    MTextView serviceItemname, servicepriceTxtView;
    MaterialEditText commentBox;
    MButton continueBtn;
    View couponCodeArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ufx_order_details);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        couponCodeArea = findViewById(R.id.couponCodeArea);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        serviceItemname = (MTextView) findViewById(R.id.serviceItemname);
        servicepriceTxtView = (MTextView) findViewById(R.id.servicepriceTxtView);
        backImgView.setOnClickListener(new setOnClickList());
        commentBox = (MaterialEditText) findViewById(R.id.commentBox);
        commentBox.setHint(generalFunc.retrieveLangLBl("Add Special Instruction for provider.", "LBL_COMMENT_BOX_TXT"));


        couponCodeArea.setOnClickListener(new setOnClickList());

        continueBtn = ((MaterialRippleLayout) findViewById(R.id.proceedToCheckOutBtn)).getChildView();
        continueBtn.setId(Utils.generateViewId());
        continueBtn.setOnClickListener(new setOnClickList());


        serviceItemname.setText(getIntent().getStringExtra("SelectvVehicleType"));
        servicepriceTxtView.setText(getIntent().getStringExtra("SelectvVehiclePrice"));
    }

    public Context getActContext() {
        return UfxOrderDetailsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(UfxOrderDetailsActivity.this);
            Bundle bn = new Bundle();
            switch (view.getId()) {

                case R.id.backImgView:
                    UfxOrderDetailsActivity.super.onBackPressed();
                    break;
                case R.id.couponCodeArea:
//                    new StartActProcess(getActContext()).startActForResult(CouponActivity.class, RES_PROMOCODE);
                    break;
            }

            if (view.getId() == continueBtn.getId()) {
                new StartActProcess(getActContext()).startActWithData(UfxPaymentActivity.class, bn);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
