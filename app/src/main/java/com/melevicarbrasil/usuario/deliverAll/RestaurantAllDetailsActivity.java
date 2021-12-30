package com.melevicarbrasil.usuario.deliverAll;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.files.deliverAll.RatingDialogRecycAdapter;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.kyleduo.switchbutton.SwitchButton;
import com.model.RestaurantCataChildModel;
import com.model.RestaurantCataParentModel;
import com.realmModel.Cart;
import com.squareup.picasso.Picasso;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CounterFab;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.viewholder.BiodataExpandable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RestaurantAllDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, BiodataExpandable.ChildItemClickListener {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.7f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    MTextView ratetxt;
    LinearLayout ratingView, restaurantAdptrLayout, addviewsLayout, header, ratingArea;
    View ratingline;
    SwitchButton vegNonVegSwitch;
    MTextView titleFoodTxt, foodAddresstxt;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    CardView card_view_left1;
    TextView titleTxtView;
    private Toolbar toolbar;
    private AppBarLayout appbar;
    ImageView backarrorImgView, searchImgView, cartImgView;
    CounterFab cartFoodImgView;


    LinearLayout infoInnerLayout, dialogsLayout, resHeaderViewInfoLayout, VegNovegFilterArea;
    RelativeLayout dialogsLayoutArea;
    RelativeLayout DialogBGView;
    MButton closeBtn;
    ImageView ratingDialogCloseBtn;
    RecyclerView expandRecyclerview;
    private BiodataExpandable mBiodataExapandable;
    public List<RestaurantCataParentModel> listModelBiodata = new ArrayList<>();
    GeneralFunctions generalFunc;

    RecyclerView ratingDialogRecycView;
    RatingDialogRecycAdapter ratingDialogRecycAdapter;
    ArrayList<HashMap<String, String>> ratingDialogArrList = new ArrayList<>();

    CardView ratingDesignCardView, informationDesignCardView;

    LinearLayout restaurantViewFloatingBtn, menuContainer;
    CoordinatorLayout mainContaner;
    LinearLayout ratingViewww;
    RelativeLayout fabMenuLayout;
    RelativeLayout fabMenuArea;
    MTextView miniOrderTtxt, miniOrderVtxt, deliverinTtxt, deliverinVtxt, selectServiceTxt;

    String eNonVegToggleDisplay = "";
    MTextView openingHourTxt, timeHTxt, timeVTxt, titleDailogTxt, addressDailogTxt;
    ImageView coverImage;
    LinearLayout offerArea;

    MTextView offerMsgTxt;
    View viewofferLine;
    String vCompany = "";
    String fMinOrderValue = "0";
    String iMaxItemQty = "0";

    LinearLayoutManager linearLayoutManager;
    RealmResults<Cart> realmCartList;

    String isSearchVeg = "No";
    MTextView menutxt;
    NestedScrollView nestedscroll;
    int selMenupos = 0;
    String iCompanyId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_all_details);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        iCompanyId=getIntent().getStringExtra("iCompanyId");
        mainContaner = findViewById(R.id.mainContaner);
        nestedscroll = findViewById(R.id.nestedscroll);
        ratingViewww = findViewById(R.id.ratingViewww);
        fabMenuLayout = findViewById(R.id.fabMenuLayout);
        fabMenuArea = findViewById(R.id.fabMenuArea);
        offerArea = findViewById(R.id.offerArea);
        offerMsgTxt = findViewById(R.id.offerMsgTxt);
        viewofferLine = findViewById(R.id.viewofferLine);
        expandRecyclerview = findViewById(R.id.expandRecyclerview);
        ratingDialogRecycView = findViewById(R.id.ratingDialogRecycView);
        miniOrderTtxt = findViewById(R.id.miniOrderTtxt);
        miniOrderVtxt = findViewById(R.id.miniOrderVtxt);
        deliverinTtxt = findViewById(R.id.deliverinTtxt);
        deliverinVtxt = findViewById(R.id.deliverinVtxt);
        selectServiceTxt = findViewById(R.id.selectServiceTxt);
        openingHourTxt = findViewById(R.id.openingHourTxt);
        titleDailogTxt = findViewById(R.id.titleDailogTxt);
        addressDailogTxt = findViewById(R.id.addressDailogTxt);
        timeHTxt = findViewById(R.id.timeHTxt);
        timeVTxt = findViewById(R.id.timeVTxt);
        coverImage = findViewById(R.id.coverImage);
        restaurantViewFloatingBtn = findViewById(R.id.restaurantViewFloatingBtn);
        menuContainer = findViewById(R.id.menuContainer);
        ratingDesignCardView = findViewById(R.id.ratingDesignCardView);
        informationDesignCardView = findViewById(R.id.informationDesignCardView);
        ratingDialogCloseBtn = findViewById(R.id.ratingDialogCloseBtn);
        VegNovegFilterArea = findViewById(R.id.VegNovegFilterArea);
        DialogBGView = findViewById(R.id.DialogBGView);
        resHeaderViewInfoLayout = findViewById(R.id.resHeaderViewInfoLayout);
        infoInnerLayout = findViewById(R.id.infoInnerLayout);
        dialogsLayout = findViewById(R.id.dialogsLayout);
        dialogsLayoutArea = findViewById(R.id.dialogsLayoutArea);
//        closeBtn = findViewById(R.id.closeBtn);
        closeBtn = ((MaterialRippleLayout) findViewById(R.id.closeBtn)).getChildView();
        ratetxt = findViewById(R.id.restaurantRateTxt);
        titleTxtView = findViewById(R.id.titleTxtView);
        titleFoodTxt = findViewById(R.id.titleFoodTxt);
        foodAddresstxt = findViewById(R.id.foodAddresstxt);
        ratingView = findViewById(R.id.ratingView);
        ratingArea = findViewById(R.id.ratingArea);
        ratingline = findViewById(R.id.ratingline);
        card_view_left1 = (CardView) findViewById(R.id.card_view_left1);
        appbar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        header = findViewById(R.id.header);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        restaurantAdptrLayout = findViewById(R.id.restaurantAdptrLayout);
        addviewsLayout = findViewById(R.id.addviewsLayout);
        vegNonVegSwitch = findViewById(R.id.vegNonVegSwitch);
        menutxt = findViewById(R.id.menutxt);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        backarrorImgView = findViewById(R.id.backArrowImgView);
        backarrorImgView.setOnClickListener(new setOnClickList());
        searchImgView = findViewById(R.id.rightFoodImgView);


        cartFoodImgView = findViewById(R.id.cartFoodImgView);
        cartImgView = findViewById(R.id.cartImgView);

        int backColor=Color.parseColor("#a7d24f");
        int strokeColor=getActContext().getResources().getColor(android.R.color.transparent);

        new CreateRoundedView(backColor, 6, 0, strokeColor, ratingView);
        new CreateRoundedView(backColor, 6, 0, strokeColor, ratingViewww);

        //ratingView.setOnClickListener(new setOnClickList());
        fabMenuLayout.setOnClickListener(new setOnClickList());
        searchImgView.setOnClickListener(new setOnClickList());
        cartFoodImgView.setOnClickListener(new setOnClickList());


        if (generalFunc.isRTLmode()) {
            restaurantViewFloatingBtn.setRotation(180);
        }


        restaurantViewFloatingBtn.setOnClickListener(new setOnClickList());
        resHeaderViewInfoLayout.setOnClickListener(new setOnClickList());
        DialogBGView.setOnClickListener(new setOnClickList());
        closeBtn.setOnClickListener(new setOnClickList());
        ratingDialogCloseBtn.setOnClickListener(new setOnClickList());


        linearLayoutManager = new LinearLayoutManager(this);
        expandRecyclerview.setLayoutManager(linearLayoutManager);

        findViews();

        ratingDialogRecycAdapter = new RatingDialogRecycAdapter(getActContext(), ratingDialogArrList);
        ratingDialogRecycView.setLayoutManager(new LinearLayoutManager(getActContext()));
        ratingDialogRecycView.setAdapter(ratingDialogRecycAdapter);
        findDataOfRatingDialog();

        setLabel();
        getRestaurantDetails("");

        vegNonVegSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String isVeg = "";
                if (isChecked) {
                    isVeg = "Yes";
                    isSearchVeg = isVeg;
                    // vegNonVegSwitch.setThumbColorRes(android.R.color.holo_green_dark);
                    // vegNonVegSwitch.setBackColorRes(android.R.color.holo_green_light);
                } else {
                    isVeg = "No";
                    isSearchVeg = isVeg;
                    //  vegNonVegSwitch.setThumbColorRes(R.color.switchgray);
                    //  vegNonVegSwitch.setBackColorRes(R.color.gray);
                }


                getRestaurantDetails(isVeg);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            realmCartList = getCartData();


            if (realmCartList.size() > 0) {

                int cnt = 0;
                for (int i = 0; i < realmCartList.size(); i++) {
                    cnt = cnt + GeneralFunctions.parseIntegerValue(0, realmCartList.get(i).getQty());
                }

                cartFoodImgView.setCount(cnt);


                cartImgView.setImageResource(R.drawable.ic_fill_cart);
            } else {
                cartFoodImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_cart));
                cartImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_cart));
                cartFoodImgView.setCount(0);
            }
        } catch (Exception e) {

        }
    }

    public RealmResults<Cart> getCartData() {
        Realm realm = MyApp.getRealmInstance();
        return realm.where(Cart.class).findAll();
    }

    public void setLabel() {
        miniOrderTtxt.setText(generalFunc.retrieveLangLBl("", "LBL_MIN_ORDER_TXT"));
        deliverinTtxt.setText(generalFunc.retrieveLangLBl("", "LBL_DELIVER_IN"));
        openingHourTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OPENING_HOURS"));
        closeBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CLOSE_TXT"));
        menutxt.setText(generalFunc.retrieveLangLBl("MENU", "LBL_MENU"));
    }

    public void getRestaurantDetails(String isVeg) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GetRestaurantDetails");
        parameters.put("iCompanyId", iCompanyId);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("CheckNonVegFoodType", isVeg);
        parameters.put("PassengerLat", getIntent().getStringExtra("lat"));
        parameters.put("PassengerLon", getIntent().getStringExtra("long"));
        parameters.put("eSystem", Utils.eSystem_Type);
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setCancelAble(false);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {
                JSONObject responseObj=generalFunc.getJsonObject(responseString);

                if (responseObj != null && !responseObj.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                    if (isDataAvail == true) {
                        listModelBiodata.clear();
                        String message = generalFunc.getJsonValueStr("message", responseObj);

                        eNonVegToggleDisplay = generalFunc.getJsonValue("eNonVegToggleDisplay", message);
                        if (eNonVegToggleDisplay.equalsIgnoreCase("Yes")) {
                            VegNovegFilterArea.setVisibility(View.VISIBLE);
                            selectServiceTxt.setText(generalFunc.retrieveLangLBl("", "LBL_VEG_ONLY"));
                        }


                        JSONObject companyDetails = generalFunc.getJsonObject("CompanyDetails", message);
                        JSONArray restaurant_Arr = generalFunc.getJsonArray("CompanyFoodData", companyDetails);
                        for (int i = 0; i < restaurant_Arr.length(); i++) {
                            JSONObject headerObj = generalFunc.getJsonObject(restaurant_Arr, i);
                            JSONArray categoryListObj = generalFunc.getJsonArray("menu_items", headerObj);
                            List<RestaurantCataChildModel> mChilddata = new ArrayList<>();
                            RestaurantCataParentModel mHeaderData = new RestaurantCataParentModel();
                            if (categoryListObj != null) {
                                for (int j = 0; j < categoryListObj.length(); j++) {
                                    JSONObject category_obj = generalFunc.getJsonObject(categoryListObj, j);
                                    RestaurantCataChildModel childObj = new RestaurantCataChildModel();
                                    childObj.setfPrice(generalFunc.getJsonValueStr("fPrice", category_obj));
                                    childObj.setiServiceId(generalFunc.getServiceId());
                                    childObj.setiDisplayOrder(generalFunc.getJsonValueStr("iDisplayOrder", category_obj));
                                    childObj.setiFoodMenuId(generalFunc.getJsonValueStr("iFoodMenuId", category_obj));
                                    childObj.setiMenuItemId(generalFunc.getJsonValueStr("iMenuItemId", category_obj));
                                    childObj.setvImage(generalFunc.getJsonValueStr("vImage", category_obj));
                                    childObj.setvItemType(generalFunc.getJsonValueStr("vItemType", category_obj));
                                    childObj.setvItemDesc(generalFunc.getJsonValueStr("vItemDesc", category_obj));
                                    childObj.setfOfferAmt(generalFunc.getJsonValueStr("fOfferAmt", category_obj));
                                    childObj.setStrikeoutPrice(generalFunc.getJsonValueStr("StrikeoutPrice", category_obj));
                                    childObj.setfDiscountPrice(generalFunc.getJsonValueStr("fDiscountPrice", category_obj));
                                    childObj.seteFoodType(generalFunc.getJsonValueStr("eFoodType", category_obj));
                                    childObj.setfDiscountPricewithsymbol(generalFunc.getJsonValueStr("fDiscountPricewithsymbol", category_obj));
                                    childObj.setCurrencySymbol(generalFunc.getJsonValueStr("currencySymbol", category_obj));
                                    childObj.setMenuItemOptionToppingArr(generalFunc.getJsonValueStr("MenuItemOptionToppingArr", category_obj));
                                    mChilddata.add(childObj);
                                }
                            }

                            mHeaderData.setmListChild(mChilddata);
                            mHeaderData.setName(generalFunc.getJsonValueStr("vMenu", headerObj));
                            mHeaderData.setNameCnt(generalFunc.getJsonValueStr("vMenuItemCount", headerObj));
                            mHeaderData.setId(generalFunc.getJsonValueStr("iFoodMenuId", headerObj));
                            listModelBiodata.add(mHeaderData);
                        }
                        setAdappter();
                        setData(message);


                        if (menuContainer.getChildCount() > 0) {
                            menuContainer.removeAllViewsInLayout();
                        }
                        addMenuViewdata();


                    } else {
                        mBiodataExapandable.notifyDataSetChanged();
                    }
                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    public void setData(String message) {
        fMinOrderValue = generalFunc.getJsonValue("fMinOrderValue", message);
        iMaxItemQty = generalFunc.getJsonValue("iMaxItemQty", message);
        vCompany = generalFunc.getJsonValue("vCompany", message);

        String vAvgRating=generalFunc.getJsonValue("vAvgRating", message);
        if (vAvgRating != null && !vAvgRating.equalsIgnoreCase("") && !vAvgRating.equalsIgnoreCase("0")) {
            ratetxt.setText(vAvgRating);
            ratingline.setVisibility(View.VISIBLE);
            ratingArea.setVisibility(View.VISIBLE);
        } else {
            ratingline.setVisibility(View.GONE);
            ratingArea.setVisibility(View.GONE);
        }

        String fMinOrderValueDisplay=generalFunc.getJsonValue("fMinOrderValueDisplay", message);
        if (fMinOrderValueDisplay != null && !fMinOrderValueDisplay.equalsIgnoreCase("")) {
            miniOrderVtxt.setText(fMinOrderValueDisplay);
            miniOrderVtxt.setVisibility(View.VISIBLE);
            miniOrderTtxt.setVisibility(View.VISIBLE);
        } else {
            miniOrderVtxt.setVisibility(View.GONE);
            miniOrderTtxt.setVisibility(View.GONE);
        }
        titleFoodTxt.setText(vCompany);
        titleTxtView.setText(vCompany);
        foodAddresstxt.setText(generalFunc.getJsonValue("vCaddress", message));

        String Restaurant_OrderPrepareTime=generalFunc.getJsonValue("Restaurant_OrderPrepareTime", message);
        if (Restaurant_OrderPrepareTime != null && !Restaurant_OrderPrepareTime.equalsIgnoreCase("")) {
            deliverinVtxt.setText(Restaurant_OrderPrepareTime);
            deliverinVtxt.setVisibility(View.VISIBLE);
            deliverinTtxt.setVisibility(View.VISIBLE);
        } else {
            deliverinVtxt.setVisibility(View.GONE);
            deliverinTtxt.setVisibility(View.GONE);
        }
        titleDailogTxt.setText(vCompany);
        addressDailogTxt.setText(generalFunc.getJsonValue("vCaddress", message));

        String Restaurant_OfferMessage=generalFunc.getJsonValue("Restaurant_OfferMessage", message);
        if (!Restaurant_OfferMessage.equalsIgnoreCase("")) {
            offerArea.setVisibility(View.VISIBLE);
            offerMsgTxt.setText(Restaurant_OfferMessage);
        } else {
            offerArea.setVisibility(View.GONE);
        }

        if (offerArea.getVisibility() == View.VISIBLE && VegNovegFilterArea.getVisibility() == View.VISIBLE) {
            viewofferLine.setVisibility(View.VISIBLE);
        }

        timeHTxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("monfritimeslot_TXT", message)) + "\n" + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("satsuntimeslot_TXT", message)));
        timeVTxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("monfritimeslot_Time", message)) + "\n" + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("satsuntimeslot_Time", message)));
        findViews();

        String vCoverImage=generalFunc.getJsonValue("vCoverImage", message);
        if (vCoverImage != null && !vCoverImage.equals("")) {
            Picasso.get()
                    .load(vCoverImage)
                    .into(coverImage);
        } else {
            //coverImage.setImageDrawable(getActContext().getResources().getDrawable(R.mipmap.ic_no_icon));
        }


    }

    public void setAdappter() {
        mBiodataExapandable = new BiodataExpandable(listModelBiodata, this);
        expandRecyclerview.setItemAnimator(new DefaultItemAnimator());
        expandRecyclerview.setAdapter(mBiodataExapandable);
        mBiodataExapandable.onChildClickListener(this);

    }

    private void findDataOfRatingDialog() {
        for (int i = 0; i <= 10; i++) {

            HashMap<String, String> map = new HashMap<>();
            map.put("data", "textdata");
            ratingDialogArrList.add(map);
        }
        ratingDialogRecycAdapter.notifyDataSetChanged();
    }


    private void findViews() {
        toolbar.setTitle("");

        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        startAlphaAnimation(titleTxtView, 0, View.INVISIBLE);
    }

    public Context getActContext() {
        return RestaurantAllDetailsActivity.this;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(titleTxtView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(backarrorImgView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(searchImgView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(cartFoodImgView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);


                mIsTheTitleVisible = true;
            }
        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(titleTxtView, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }


    @Override
    public void setOnClick(int position, RestaurantCataChildModel restaurantCataChildModel) {
        Bundle bn = new Bundle();

        HashMap<String, String> map = new HashMap<>();
        map.put("iMenuItemId", restaurantCataChildModel.getiMenuItemId());
        map.put("iFoodMenuId", restaurantCataChildModel.getiFoodMenuId());
        map.put("vItemType", restaurantCataChildModel.getvItemType());
        map.put("vItemDesc", restaurantCataChildModel.getvItemDesc());
        map.put("fPrice", restaurantCataChildModel.getfPrice());
        map.put("eFoodType", restaurantCataChildModel.geteFoodType());
        map.put("fOfferAmt", restaurantCataChildModel.getfOfferAmt());
        map.put("vImage", restaurantCataChildModel.getvImage());
        map.put("iDisplayOrder", restaurantCataChildModel.getiDisplayOrder());
        map.put("StrikeoutPrice", restaurantCataChildModel.getStrikeoutPrice());
        map.put("fDiscountPrice", restaurantCataChildModel.getfDiscountPrice());
        map.put("fDiscountPricewithsymbol", restaurantCataChildModel.getfDiscountPricewithsymbol());
        map.put("MenuItemOptionToppingArr", restaurantCataChildModel.getMenuItemOptionToppingArr());
        map.put("currencySymbol", restaurantCataChildModel.getCurrencySymbol());
        map.put("iCompanyId", iCompanyId);
        map.put("vCompany", vCompany);
        map.put("fMinOrderValue", fMinOrderValue);
        map.put("iMaxItemQty", iMaxItemQty);
        map.put("Restaurant_Status", getIntent().getStringExtra("Restaurant_Status"));
        map.put("ispriceshow", getIntent().getStringExtra("ispriceshow"));
        bn.putSerializable("data", map);

        new StartActProcess(getActContext()).startActForResult(AddBasketActivity.class, bn, Utils.ADD_TO_BASKET);
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.resHeaderViewInfoLayout) {

                if (dialogsLayout.getVisibility() == View.GONE) {
                    dialogsLayout.setVisibility(View.VISIBLE);
                    dialogsLayoutArea.setVisibility(View.VISIBLE);
                    informationDesignCardView.setVisibility(View.VISIBLE);
                    ratingDesignCardView.setVisibility(View.GONE);
                } else {
                    dialogsLayout.setVisibility(View.GONE);
                    dialogsLayoutArea.setVisibility(View.GONE);
                }
            } else if (i == R.id.DialogBGView) {

            } else if (i == closeBtn.getId()) {
                Logger.d("performclickk", "call");
                Logger.d("performclickk", "callBG");
                if (dialogsLayout.getVisibility() == View.VISIBLE) {
                    dialogsLayout.setVisibility(View.GONE);
                    dialogsLayoutArea.setVisibility(View.GONE);
                }
            } else if (i == R.id.ratingView) {
                if (dialogsLayout.getVisibility() == View.GONE) {
                    dialogsLayout.setVisibility(View.VISIBLE);
                    dialogsLayoutArea.setVisibility(View.VISIBLE);
                    ratingDesignCardView.setVisibility(View.VISIBLE);
                    informationDesignCardView.setVisibility(View.GONE);
                } else {
                    dialogsLayout.setVisibility(View.GONE);
                    dialogsLayoutArea.setVisibility(View.GONE);
                }
            } else if (i == R.id.ratingDialogCloseBtn) {
                closeBtn.performClick();
            } else if (i == R.id.restaurantViewFloatingBtn) {
                if (fabMenuLayout.getVisibility() == View.GONE) {
                    fabMenuLayout.setVisibility(View.VISIBLE);
                    fabMenuArea.setVisibility(View.VISIBLE);
                } else {
                    fabMenuLayout.setVisibility(View.GONE);
                    fabMenuArea.setVisibility(View.GONE);
                }
            } else if (i == R.id.rightFoodImgView) {
                Bundle bn = new Bundle();
                bn.putString("iCompanyId", iCompanyId);
                bn.putString("vCompany", vCompany);
                bn.putString("Restaurant_Status", getIntent().getStringExtra("Restaurant_Status"));
                bn.putString("ispriceshow", getIntent().getStringExtra("ispriceshow"));
                bn.putString("CheckNonVegFoodType", isSearchVeg);
                new StartActProcess(getActContext()).startActWithData(SearchFoodActivity.class, bn);
            } else if (i == R.id.cartFoodImgView) {

                new StartActProcess(getActContext()).startAct(EditCartActivity.class);
            } else if (i == R.id.backArrowImgView) {
                onBackPressed();
            }
        }
    }


    public void addMenuViewdata() {
        if (listModelBiodata != null && listModelBiodata.size() > 0) {
            for (int i = 0; i < listModelBiodata.size(); i++) {
                int pos = i;
                LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_menu_design, null);
                MTextView rowName = (MTextView) view.findViewById(R.id.rowName);
                MTextView rowCnt = (MTextView) view.findViewById(R.id.rowCnt);
                View rowview = (View) view.findViewById(R.id.rowview);
                rowview.setVisibility(View.VISIBLE);
                LinearLayout rowArea = (LinearLayout) view.findViewById(R.id.rowArea);


                rowArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabMenuLayout.setVisibility(View.GONE);
                        fabMenuArea.setVisibility(View.GONE);
                        mBiodataExapandable.collapseAllParents();
                        mBiodataExapandable.onParentListItemExpanded(pos);
                        float y = expandRecyclerview.getY() + expandRecyclerview.getChildAt(pos).getY();
                        nestedscroll.smoothScrollTo(0, (int) y);
                    }
                });
                rowName.setText(listModelBiodata.get(i).getName());
                rowCnt.setText(listModelBiodata.get(i).getNameCnt());
                menuContainer.addView(view);
            }

            LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_menu_design, null);
            MTextView rowName = (MTextView) view.findViewById(R.id.rowName);
            MTextView rowCnt = (MTextView) view.findViewById(R.id.rowCnt);
            View rowview = (View) view.findViewById(R.id.rowview);
            rowview.setVisibility(View.GONE);
            menuContainer.addView(view);
        } else {
            restaurantViewFloatingBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.ADD_TO_BASKET && resultCode == RESULT_OK) {
            generalFunc.showMessage(titleTxtView, generalFunc.retrieveLangLBl("", "LBL_ADD_CART_MSG"));
        }
    }

    @Override
    public void onBackPressed() {
        if (fabMenuLayout.getVisibility() == View.VISIBLE) {
            fabMenuLayout.setVisibility(View.GONE);
            fabMenuArea.setVisibility(View.GONE);
            return;
        }
        if (dialogsLayout.getVisibility() == View.VISIBLE) {
            closeBtn.performClick();
            return;
        } else {
            RestaurantAllDetailsActivity.super.onBackPressed();
        }
    }
}
