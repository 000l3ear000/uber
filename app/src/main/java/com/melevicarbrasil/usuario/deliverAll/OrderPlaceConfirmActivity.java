package com.melevicarbrasil.usuario.deliverAll;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.BookingActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.realmModel.Cart;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import io.realm.Realm;

public class OrderPlaceConfirmActivity extends AppCompatActivity {


    ImageView backImgView;
    MTextView titleTxt;
    GeneralFunctions generalFunc;
    MButton btn_type2;
    MTextView placeOrderNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place_confirm);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        placeOrderNote = (MTextView) findViewById(R.id.placeOrderNote);
        backImgView.setOnClickListener(new setOnClickList());
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ORDER_PLACED"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_TRACK_YOUR_ORDER"));
        placeOrderNote.setText(generalFunc.retrieveLangLBl("", "LBL_ORDER_PLACE_MSG"));


        Realm realm = MyApp.getRealmInstance();
        realm.beginTransaction();
        realm.delete(Cart.class);
        realm.commitTransaction();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (generalFunc.prefHasKey(Utils.iServiceId_KEY) && generalFunc != null && !generalFunc.isDeliverOnlyEnabled()) {
            generalFunc.removeValue(Utils.iServiceId_KEY);
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        MyApp.getInstance().restartWithGetDataApp();
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == R.id.backImgView) {

                MyApp.getInstance().restartWithGetDataApp();
            } else if (i == btn_type2.getId()) {

                Bundle bn = new Bundle();
                bn.putBoolean("isOrder", true);
                bn.putString("iOrderId", getIntent().getStringExtra("iOrderId"));
                // new StartActProcess(getActContext()).startActWithData(TrackOrderActivity.class, bn);
                new StartActProcess(getActContext()).startActWithData(BookingActivity.class, bn);
//                new StartActProcess(getActContext()).startActWithData(ActiveOrderActivity.class, bn);

            }
        }
    }


    public Context getActContext() {
        return OrderPlaceConfirmActivity.this;
    }
}
