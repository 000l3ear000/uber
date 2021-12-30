package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

/**
 * Created by Admin on 03-11-2016.
 */
public class InviteFriendsActivity extends AppCompatActivity {

    MTextView titleTxt, shareTxtLbl, invitecodeTxt, shareTxt;
    ImageView backImgView;
    String userProfileJson = "";
    String Refreal_code = "";
    GeneralFunctions generalFunc;
    private MButton btn_type3;
    String LBL_INVITE_FRIEND_TXT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        init();
    }

    private void init() {


        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        shareTxtLbl = (MTextView) findViewById(R.id.shareTxtLbl);
        invitecodeTxt = (MTextView) findViewById(R.id.invitecodeTxt);
        shareTxt = (MTextView) findViewById(R.id.shareTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        btn_type3 = ((MaterialRippleLayout) findViewById(R.id.btn_type3)).getChildView();
        btn_type3.setId(Utils.generateViewId());

        setLabels();

        btn_type3.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
    }


    public void setLabels() {
        LBL_INVITE_FRIEND_TXT = generalFunc.retrieveLangLBl("", "LBL_INVITE_FRIEND_TXT");
        titleTxt.setText(LBL_INVITE_FRIEND_TXT);
        btn_type3.setText(LBL_INVITE_FRIEND_TXT);
        shareTxtLbl.setText(generalFunc.retrieveLangLBl("", "LBL_INVITE_FRIEND_SHARE"));
//        shareTxt.setText(generalFunc.retrieveLangLBl("", "LBL_INVITE_FRIEND_SHARE_TXT"));
        shareTxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("INVITE_DESCRIPTION_CONTENT", userProfileJson)));
        Refreal_code = generalFunc.getJsonValue("vRefCode", userProfileJson);


        invitecodeTxt.setText(Refreal_code.trim());


    }

    public Context getActContext() {
        return InviteFriendsActivity.this;
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == R.id.backImgView) {
                InviteFriendsActivity.super.onBackPressed();
            } else if (i == btn_type3.getId()) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, LBL_INVITE_FRIEND_TXT);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, generalFunc.getJsonValue("INVITE_SHARE_CONTENT", userProfileJson));
                startActivity(Intent.createChooser(sharingIntent, generalFunc.retrieveLangLBl("", "LBL_SHARE_USING")));
            }
        }
    }


}

