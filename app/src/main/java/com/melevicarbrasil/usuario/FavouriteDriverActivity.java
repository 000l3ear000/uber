package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.adapter.files.ViewPagerAdapter;
import com.dialogs.OpenListView;
import com.fragments.FavDriverFragment;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteDriverActivity extends AppCompatActivity {

    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    public String userProfileJson;
    CharSequence[] titles;
    boolean isrestart = false;

    String TAB1 = "ALL";
    String TAB2 = "FAV";
    String set = "";

    ImageView filterImageview;
    int selTabPos = 0;
    ArrayList<HashMap<String, String>> filterlist;
    androidx.appcompat.app.AlertDialog list_type;
    public String selFilterType = "";
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ViewPager appLogin_view_pager;
    ArrayList<Integer> selList = new ArrayList();
    boolean checkedETypes[];
    boolean checkedPreETypes[];
    Button pos;
    Button neg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_driver);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        isrestart = getIntent().getBooleanExtra("isrestart", false);


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        filterImageview = (ImageView) findViewById(R.id.filterImageview);
        filterImageview.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        setLabels();


        appLogin_view_pager = (ViewPager) findViewById(R.id.appLogin_view_pager);
        TabLayout material_tabs = (TabLayout) findViewById(R.id.material_tabs);

        String TAB1_LBL = generalFunc.retrieveLangLBl("", "LBL_ALL_FAV_DRIVERS");
        String TAB2_LBL = generalFunc.retrieveLangLBl("", "LBL_FAV_TXT");

        if (!Utils.checkText(set)) {
            titles = new CharSequence[]{TAB1_LBL, TAB2_LBL};
            material_tabs.setVisibility(View.VISIBLE);
            fragmentList.add(generateFavDriverFrag(TAB1));
            fragmentList.add(generateFavDriverFrag(TAB2));
        } else if (set.equalsIgnoreCase(TAB1)) {
            titles = new CharSequence[]{TAB1_LBL};
            fragmentList.add(generateFavDriverFrag(TAB1));
            material_tabs.setVisibility(View.GONE);
        } else if (set.equalsIgnoreCase(TAB2)) {
            titles = new CharSequence[]{TAB2_LBL};
            fragmentList.add(generateFavDriverFrag(TAB1));
            material_tabs.setVisibility(View.GONE);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        appLogin_view_pager.setAdapter(adapter);
        material_tabs.setupWithViewPager(appLogin_view_pager);


        if (isrestart) {
            appLogin_view_pager.setCurrentItem(1);
        }


        appLogin_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logger.d("onPageScrolled", "::" + position);

            }

            @Override
            public void onPageSelected(int position) {
                selTabPos = position;
                Logger.d("onPageScrolled", "::" + "onPageSelected");
                fragmentList.get(position).onResume();
                //selFilterType = "";
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Logger.d("onPageScrolled", "::" + "onPageScrollStateChanged");
            }
        });

    }


    public FavDriverFragment getFavDriverFrag() {

        if (favDriverFragment != null) {
            return favDriverFragment;
        }
        return null;
    }

    FavDriverFragment favDriverFragment = null;

    public FavDriverFragment generateFavDriverFrag(String bookingType) {
        FavDriverFragment frag = new FavDriverFragment();
        Bundle bn = new Bundle();
        bn.putString("TAB_TYPE", bookingType);

        favDriverFragment = frag;

        frag.setArguments(bn);


        return frag;
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("Favorite Drivers", "LBL_FAV_DRIVERS_TITLE_TXT"));


        if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) ||
                generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAV_PROVIDER"));
        } else {
            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAV_DRIVER"));
        }
    }


    public void BuildType() {

//        if (filterlist != null && filterlist.size() > 0) {
//            ArrayList<String> typeNameList = new ArrayList<>();
//            for (int i = 0; i < filterlist.size(); i++) {
//                typeNameList.add((filterlist.get(i).get("vTitle")));
//            }
//
//            CharSequence[] cs_currency_txt = typeNameList.toArray(new CharSequence[typeNameList.size()]);
//
//            // Boolean array for initial selected items
//            checkedETypes = new boolean[cs_currency_txt.length];
//            if (checkedPreETypes != null) {
//                checkedETypes = checkedPreETypes.clone();
//            }
//
//            isContainTrue(checkedETypes);
//
//            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActContext());
//            builder.setTitle(generalFunc.retrieveLangLBl("Select Type", "LBL_SELECT_TYPE"));
//
//            builder.setMultiChoiceItems(cs_currency_txt, checkedETypes, (dialog, which, isChecked) -> {
//                // TODO Auto-generated method stub
//                if (isChecked) {
//                    // If user select a item then add it in selected items
//                    selList.add(which);
//                } else if (selList.contains(which)) {
//                    // if the item is already selected then remove it
//                    selList.remove(Integer.valueOf(which));
//                }
//
//                showButton();
//            });
//
//
//            builder.setPositiveButton(generalFunc.retrieveLangLBl("Apply", "LBL_APPLY"), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    ArrayList<String> selectedStrings = new ArrayList<>();
//
//                    for (int j = 0; j < selList.size(); j++) {
//                        selectedStrings.add(filterlist.get(selList.get(j)).get("vFilterParam"));
//                    }
//
//                    checkedPreETypes = new boolean[cs_currency_txt.length];
//                    checkedPreETypes = checkedETypes.clone();
//                    selFilterType = android.text.TextUtils.join(",", selectedStrings);
//                    fragmentList.get(appLogin_view_pager.getCurrentItem()).onResume();
//
//
//                }
//            });
//
//            builder.setNegativeButton(generalFunc.retrieveLangLBl("Reset", "LBL_RESET"), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    selList.clear();
//                    checkedPreETypes = new boolean[cs_currency_txt.length];
//                    selFilterType = "";
//                    fragmentList.get(appLogin_view_pager.getCurrentItem()).onResume();
//
//
//                }
//            });
//
//
//            list_type = builder.create();
//
//            //2. now setup to change color of the button
//            list_type.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    pos = list_type.getButton(DialogInterface.BUTTON_POSITIVE);
//                    neg = list_type.getButton(DialogInterface.BUTTON_NEGATIVE);
//                    showButton();
//
//                    if (pos != null) {
//
//                        // Change the alert dialog buttons text and background color
//                        pos.setTextColor(getActContext().getResources().getColor(R.color.black));
//
//                        LinearLayout.LayoutParams posParams = (LinearLayout.LayoutParams) pos.getLayoutParams();
//                        posParams.weight = 1;
//                        posParams.width = 0;
//                        posParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                        posParams.gravity = View.TEXT_ALIGNMENT_VIEW_END;
//                        pos.setLayoutParams(posParams);
//                    }
//                    if (neg != null) {
//
//                        // Change the alert dialog buttons text and background color
//                        neg.setTextColor(getActContext().getResources().getColor(R.color.red));
//                        LinearLayout.LayoutParams negParams = (LinearLayout.LayoutParams) neg.getLayoutParams();
//                        negParams.weight = 1;
//                        negParams.width = 0;
//                        negParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                        negParams.gravity = View.TEXT_ALIGNMENT_VIEW_START;
//                        neg.setLayoutParams(negParams);
//                    }
//
//                }
//            });
//
//            if (generalFunc.isRTLmode() == true) {
//                generalFunc.forceRTLIfSupported(list_type);
//            }
//
//            list_type.show();
//        }


        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SELECT_TYPE"), filterlist, OpenListView.OpenDirection.BOTTOM, true, position -> {

            filterPosition = position;
            selFilterType = filterlist.get(position).get("vFilterParam");
            fragmentList.get(appLogin_view_pager.getCurrentItem()).onResume();


        }).show(filterPosition, "vTitle");

    }

  public   int filterPosition = -1;

    private void showButton() {

        if (list_type != null) {
            if (selList.size() > 0) {
                neg.setVisibility(View.VISIBLE);
                pos.setVisibility(View.VISIBLE);
            } else {
                pos.setVisibility(View.GONE);
                neg.setVisibility(View.GONE);
            }

        }
    }


    public boolean isContainTrue(boolean[] checkedETypes) {
        selList = new ArrayList();

        if (checkedETypes != null) {
            for (int i = 0; i < checkedETypes.length; i++) {
                if (checkedETypes[i])
                    selList.add(i);
            }
        }
        return false;
    }


    public void filterManage(ArrayList<HashMap<String, String>> filterlist) {

        this.filterlist = filterlist;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filterImageview.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        if (isrestart) {
            Bundle bn = new Bundle();
            new StartActProcess(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    public Context getActContext() {
        return FavouriteDriverActivity.this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.backImgView:
                    FavouriteDriverActivity.super.onBackPressed();
                    break;
                case R.id.filterImageview:
                    BuildType();
                    break;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.d("OnactivityResult", "::FavDriver called");
    }


}
