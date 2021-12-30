package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.squareup.picasso.Picasso;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.MTextView;

import java.util.HashMap;

public class NotificationDetailsActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    MTextView notificationtitleTxt, dateTxt, detailsTxt;
    ImageView newsImage;
    HashMap<String, String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        list = (HashMap<String, String>) getIntent().getSerializableExtra("data");
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());
        notificationtitleTxt = (MTextView) findViewById(R.id.notificationtitleTxt);
        dateTxt = (MTextView) findViewById(R.id.dateTxt);
        detailsTxt = (MTextView) findViewById(R.id.detailsTxt);
        newsImage = (ImageView) findViewById(R.id.newsImage);

        String vImage=list.get("vImage");
        if (vImage != null && !vImage.isEmpty()) {
            findViewById(R.id.imagArea).setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(Utilities.getResizeImgURL(getActContext(), vImage, (int) (Utils.getScreenPixelWidth(getActContext()) - Utils.dipToPixels(getActContext(), 20)), 0))
                    .into(newsImage);
        }
        titleTxt.setText(list.get("eType"));

        notificationtitleTxt.setText(list.get("vTitle"));
        dateTxt.setText(list.get("dDateTime"));
        detailsTxt.setText(list.get("tDescription"));


    }


    public Context getActContext() {
        return NotificationDetailsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(NotificationDetailsActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    NotificationDetailsActivity.super.onBackPressed();
                    break;

            }
        }
    }
}
