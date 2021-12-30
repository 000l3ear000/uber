package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.adapter.files.ViewPagerAdapter;

import com.fragments.NotiFicationFragment;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    public String userProfileJson;
    CharSequence[] titles;
    ArrayList<Fragment> fragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new NotificationActivity.setOnClickList());
        titleTxt.setText(generalFunc.retrieveLangLBl("Notifications", "LBL_NOTIFICATIONS"));


        ViewPager appLogin_view_pager = (ViewPager) findViewById(R.id.appLogin_view_pager);
        TabLayout material_tabs = (TabLayout) findViewById(R.id.material_tabs);

        if (generalFunc.getJsonValue("ENABLE_NEWS_SECTION", userProfileJson).equalsIgnoreCase("Yes")) {

            if (generalFunc.isRTLmode()) {

                titles = new CharSequence[]{generalFunc.retrieveLangLBl("all", "LBL_NEWS"), generalFunc.retrieveLangLBl("", "LBL_NOTIFICATIONS"), generalFunc.retrieveLangLBl("news", "LBL_ALL")};
                material_tabs.setVisibility(View.VISIBLE);

                fragmentList.add(generateNotificationFrag(Utils.News));
                fragmentList.add(generateNotificationFrag(Utils.Notificatons));
                fragmentList.add(generateNotificationFrag(Utils.All));

            } else {

                titles = new CharSequence[]{generalFunc.retrieveLangLBl("all", "LBL_ALL"), generalFunc.retrieveLangLBl("", "LBL_NOTIFICATIONS"), generalFunc.retrieveLangLBl("news", "LBL_NEWS")};
                material_tabs.setVisibility(View.VISIBLE);
                fragmentList.add(generateNotificationFrag(Utils.All));
                fragmentList.add(generateNotificationFrag(Utils.Notificatons));
                fragmentList.add(generateNotificationFrag(Utils.News));
            }

        } else {
            titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_NOTIFICATIONS")};
            material_tabs.setVisibility(View.GONE);
            fragmentList.add(generateNotificationFrag(Utils.Notificatons));


        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        appLogin_view_pager.setAdapter(adapter);
        material_tabs.setupWithViewPager(appLogin_view_pager);


        appLogin_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                fragmentList.get(position).onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public NotiFicationFragment generateNotificationFrag(String type) {
        NotiFicationFragment frag = new NotiFicationFragment();
        Bundle bn = new Bundle();
        bn.putString("type", type);
        frag.setArguments(bn);
        return frag;
    }

    public Context getActContext() {
        return NotificationActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(NotificationActivity.this);
            switch (view.getId()) {
                case R.id.backImgView:
                    NotificationActivity.super.onBackPressed();
                    break;

            }
        }
    }


}
