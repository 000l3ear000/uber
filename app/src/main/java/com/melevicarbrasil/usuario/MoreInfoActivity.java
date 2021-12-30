package com.melevicarbrasil.usuario;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.adapter.files.ViewPagerAdapter;
import com.fragments.GalleryFragment;
import com.fragments.ReviewsFragment;
import com.fragments.ServiceFragment;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.realmModel.CarWashCartData;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.CounterFab;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.carouselview.CarouselView;
import com.view.carouselview.ViewListener;
import com.view.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class MoreInfoActivity extends AppCompatActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    String userProfileJson;
    CharSequence[] titles;
    ImageView rightImgView;
    ArrayList<Fragment> fragmentList = new ArrayList<>();

    ViewPager view_pager;
    RelativeLayout bottomCartView;
    MTextView itemNpricecartTxt, viewCartTxt;
    RealmResults<CarWashCartData> realmCartList;
    CounterFab cartView;

    CarouselView carouselView;
    MTextView closeCarouselTxtView;
    View carouselContainerView;
    ArrayList<HashMap<String, String>> galleryListData = new ArrayList<>();
    ImageView driverStatus;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);


        SimpleRatingBar bottomViewratingBar = (SimpleRatingBar) findViewById(R.id.bottomViewratingBar);
        MTextView nameTxt = (MTextView) findViewById(R.id.bottomViewnameTxt);

        MTextView bottomViewDescTxt = (MTextView) findViewById(R.id.bottomViewDescTxt);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        bottomCartView = (RelativeLayout) findViewById(R.id.bottomCartView);
        bottomCartView.setOnClickListener(new setOnClickList());
        cartView = (CounterFab) findViewById(R.id.cartView);
        itemNpricecartTxt = (MTextView) findViewById(R.id.itemNpricecartTxt);
        viewCartTxt = (MTextView) findViewById(R.id.viewCartTxt);
        viewCartTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHECKOUT"));
        backImgView = (ImageView) findViewById(R.id.backImgView);
        backImgView.setOnClickListener(new setOnClickList());
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        rightImgView = (ImageView) findViewById(R.id.rightImgView);
        driverStatus = (ImageView) findViewById(R.id.driverStatus);

        bottomViewDescTxt.setVisibility(View.VISIBLE);
        bottomViewDescTxt.setText(generalFunc.retrieveLangLBl("", "LBL_VIEW_PROFILE_DESCRIPTION"));
        bottomViewDescTxt.setOnClickListener(new setOnClickList());


        carouselContainerView = findViewById(R.id.carouselContainerView);
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        closeCarouselTxtView = (MTextView) findViewById(R.id.closeCarouselTxtView);

        rightImgView.setOnClickListener(new setOnClickList());
        TabLayout material_tabs = (TabLayout) findViewById(R.id.material_tabs);
        nameTxt.setText(getIntent().getStringExtra("name"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SERVICE_DETAIL"));
        int padding = Utils.dipToPixels(getActContext(), 14);
        rightImgView.setPadding(padding, padding, padding, padding);
        rightImgView.setImageResource(R.drawable.ic_information);

        titles = new CharSequence[]{generalFunc.retrieveLangLBl("", "LBL_SERVICES"), generalFunc.retrieveLangLBl("", "LBL_GALLERY"), generalFunc.retrieveLangLBl("", "LBL_REVIEWS")};
        fragmentList.add(generatServiceFrag());
        fragmentList.add(generatGalleryFrag());
        fragmentList.add(generatReviewsFrag());

        closeCarouselTxtView.setOnClickListener(new setOnClickList());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        view_pager.setAdapter(adapter);
        material_tabs.setupWithViewPager(view_pager);
        bottomViewratingBar.setRating(GeneralFunctions.parseFloatValue(0, getIntent().getStringExtra("average_rating")));

        String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + getIntent().getStringExtra("iDriverId") + "/" + getIntent().getStringExtra("driver_img");

        if (getIntent().getStringExtra("IS_PROVIDER_ONLINE").equalsIgnoreCase("Yes")) {
            driverStatus.setColorFilter(ContextCompat.getColor(getActContext(), R.color.Green));
        } else {
            driverStatus.setColorFilter(ContextCompat.getColor(getActContext(), R.color.Red));
        }

        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_no_pic_user)
                .error(R.mipmap.ic_no_pic_user)
                .into(((SelectableRoundedImageView) findViewById(R.id.bottomViewdriverImgView)));

        closeCarouselTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_CLOSE_TXT"));


    }

    public void onResumeCall() {
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();

        realmCartList = getCartData();
        double finlaTotal = 0;

        if (realmCartList.size() > 0) {
            int cnt = 0;
            for (int i = 0; i < realmCartList.size(); i++) {
                CarWashCartData itemPos = realmCartList.get(i);
                cnt = cnt + GeneralFunctions.parseIntegerValue(0, itemPos.getItemCount());

                double price = GeneralFunctions.parseDoubleValue(0, itemPos.getFinalTotal().replace(itemPos.getvSymbol(), ""));
                finlaTotal = finlaTotal + price;


            }

            bottomCartView.setVisibility(View.VISIBLE);

            itemNpricecartTxt.setText(realmCartList.get(0).getvSymbol() + "" + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(finlaTotal) + ""));

            cartView.setCount(cnt);

        } else {
            bottomCartView.setVisibility(View.GONE);
        }
    }

    public ServiceFragment generatServiceFrag() {
        ServiceFragment frag = new ServiceFragment();
        Bundle bn = new Bundle();
        bn.putString("parentId", getIntent().getStringExtra("parentId"));
        bn.putString("SelectedVehicleTypeId", getIntent().getStringExtra("SelectedVehicleTypeId"));
        frag.setArguments(bn);
        return frag;
    }

    public GalleryFragment generatGalleryFrag() {
        GalleryFragment frag = new GalleryFragment();
        Bundle bn = new Bundle();
        frag.setArguments(bn);
        return frag;
    }

    public ReviewsFragment generatReviewsFrag() {
        ReviewsFragment frag = new ReviewsFragment();
        Bundle bn = new Bundle();
        frag.setArguments(bn);
        return frag;
    }

    public RealmResults<CarWashCartData> getCartData() {
        Realm realm = MyApp.getRealmInstance();
        return realm.where(CarWashCartData.class).findAll();
    }

    ViewListener viewListener = position -> {
        ImageView customView = new ImageView(getActContext());

        CarouselView.LayoutParams layParams = new CarouselView.LayoutParams(CarouselView.LayoutParams.MATCH_PARENT, CarouselView.LayoutParams.MATCH_PARENT);
        customView.setLayoutParams(layParams);

        int padding = Utils.dipToPixels(getActContext(), 15);
        customView.setPadding(padding, 0, padding, 0);
        customView.setImageResource(R.mipmap.ic_no_icon);

        final HashMap<String, String> item = galleryListData.get(position);


        int dip = Utils.dipToPixels(getActContext(), 30);
        Picasso.get()
                .load(Utilities.getResizeImgURL(getActContext(), item.get("vImage"), ((int) Utils.getScreenPixelWidth(getActContext())) - dip, 0, Utils.getScreenPixelHeight(getActContext()) - dip))
                .placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon)
                .into(customView, null);

        return customView;
    };

    public void openCarouselView(ArrayList<HashMap<String, String>> galleryListData, int currentPosition) {
        this.galleryListData = galleryListData;

        carouselContainerView.setVisibility(View.VISIBLE);
        carouselView.setViewListener(viewListener);
        carouselView.setPageCount(galleryListData.size());
        carouselView.setCurrentItem(currentPosition);
    }

    @Override
    public void onBackPressed() {

        Realm realm = MyApp.getRealmInstance();
        realm.beginTransaction();
        realm.delete(CarWashCartData.class);
        realm.commitTransaction();
        super.onBackPressed();
    }

    public Context getActContext() {
        return MoreInfoActivity.this;
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            switch (view.getId()) {
                case R.id.closeCarouselTxtView:
                    if (carouselContainerView.getVisibility() == View.VISIBLE) {
                        carouselContainerView.setVisibility(View.GONE);
                    }
                    break;
                case R.id.backImgView:
                    onBackPressed();
                    break;
                case R.id.bottomCartView:
                    Bundle bn = new Bundle();
                    bn.putString("name", getIntent().getStringExtra("name"));
                    bn.putString("serviceName", getIntent().getStringExtra("serviceName"));
                    bn.putString("iDriverId", getIntent().getStringExtra("iDriverId"));
                    bn.putString("latitude", getIntent().getStringExtra("latitude"));
                    bn.putString("longitude", getIntent().getStringExtra("longitude"));
                    bn.putString("average_rating", getIntent().getStringExtra("average_rating"));
                    bn.putString("driver_img", getIntent().getStringExtra("driver_img"));
                    bn.putString("address", getIntent().getStringExtra("address"));
                    bn.putString("vProviderLatitude", getIntent().getStringExtra("vProviderLatitude"));
                    bn.putString("vProviderLongitude", getIntent().getStringExtra("vProviderLongitude"));
                    new StartActProcess(getActContext()).startActWithData(CarWashBookingDetailsActivity.class, bn);
                    break;
                case R.id.bottomViewDescTxt:
                    Bundle bundle = new Bundle();
                    bundle.putString("iDriverId", getIntent().getStringExtra("iDriverId"));
                    bundle.putString("average_rating", getIntent().getStringExtra("average_rating"));
                    bundle.putString("driver_img", getIntent().getStringExtra("driver_img"));
                    bundle.putString("name", getIntent().getStringExtra("name"));
                    bundle.putString("fname", getIntent().getStringExtra("fname"));

                    new StartActProcess(getActContext()).startActWithData(ProviderInfoActivity.class, bundle);
                    break;
            }
        }
    }


}
