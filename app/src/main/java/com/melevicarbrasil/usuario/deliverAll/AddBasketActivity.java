package com.melevicarbrasil.usuario.deliverAll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.autofit.et.lib.AutoFitEditText;
import com.melevicarbrasil.usuario.R;
import com.general.files.AppFunctions;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.realmModel.Cart;
import com.realmModel.Options;
import com.realmModel.Topping;
import com.squareup.picasso.Picasso;
import com.utils.Logger;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AddBasketActivity extends AppCompatActivity {

    ImageView backImgView, productimage;
    MTextView titleTxt;
    GeneralFunctions generalFunc;
    MTextView vItemNameTxt, vItemDescTxt, baseFareHTxt, baseFareVTxt, topingTitleTxt, optionTitleTxt;
    LinearLayout optionContainer, topingContainer;
    LinearLayout topingArea;
    LinearLayout optionArea;
    String MenuItemOptionToppingArr;
    String data;
    //ImageView minuscntImgView, addcntImgView;
    MTextView totalHTxt, totalPriceTxt;
    ImageView vegImage, nonvegImage;
    MTextView vegNonvegTxtView;

    RealmList<Topping> realmToppingList = new RealmList<>();
    RealmList<Options> realmOptionsList = new RealmList<>();

    RealmResults<Options> realmOptionResult;
    RealmResults<Topping> realmToppingResult;

    HashMap<String, String> searchList;
    //MTextView QTYNumberTxtView;
    MTextView addItemCartBtn;
    String[] selToppingarray;
    ArrayList<String> selToppingList;

    String selOptionId = "";

    private RealmResults<Cart> cartRealmList;

    ArrayList<HashMap<String, String>> optionList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> topingList = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> basketList = new ArrayList<>();

    ArrayList<String> toppingListId = new ArrayList<>();
    LinearLayout addarea, minusarea;
    LinearLayout toppingsarea;

    double toppingPrice = 0;

    String toppingOptionsId = "";
    double seloptionPrice = 0;
    String isTooping = "No";
    String isOption = "No";
    String optionId = "";
    String toppingId = "";
    RealmResults<Cart> realmCartList;


    String LBL_SUB_TOTAL = "";

    ImageView minusImageView, addImageView;
    AutoFitEditText autofitEditText;
    MaterialEditText rechargeBox;
    ImageView closeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_basket);
        try {
            searchList = (HashMap<String, String>) getIntent().getSerializableExtra("data");


            initView();

            Realm realm = MyApp.getRealmInstance();
            realmCartList = getCartData();
            Cart cart = realm.where(Cart.class).equalTo("iMenuItemId", searchList.get("iMenuItemId")).findFirst();
            if (cart != null) {
                if (cart.getiToppingId() != null) {
                    selToppingarray = cart.getiToppingId().split(",");
                    selToppingList = new ArrayList<>(Arrays.asList(selToppingarray));
                    selOptionId = cart.getiOptionId();
                    // optionId = selOptionId;
                    // optionId = selOptionId;
                    //toppingListId = selToppingList;
                }
                //QTYNumberTxtView.setText(cart.getQty());
            }
            setData();
            realmOptionResult = getOptionsData();
            realmToppingResult = getToppingData();
            if (realmOptionResult.size() > 0 || realmToppingResult.size() > 0) {
                if (searchList.get("iCompanyId")!=null && !searchList.get("iCompanyId").equalsIgnoreCase(generalFunc.retrieveValue(Utils.COMPANY_ID))) {
                    getOptionsList();
                }
            } else {
                getOptionsList();
            }
        } catch (Exception e) {

        }

    }

    public RealmResults<Cart> getCartData() {
        Realm realm = MyApp.getRealmInstance();
        return realm.where(Cart.class).findAll();
    }


    public void deleteOptionToRealm() {
        Realm realm = MyApp.getRealmInstance();
        realm.beginTransaction();
        realm.delete(Options.class);
        realm.commitTransaction();

        Realm realmTopping = MyApp.getRealmInstance();
        realmTopping.beginTransaction();
        realmTopping.delete(Topping.class);
        realmTopping.commitTransaction();
    }


    public Context getActContext() {
        return AddBasketActivity.this;
    }


    public void initView() {
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        LBL_SUB_TOTAL = generalFunc.retrieveLangLBl("", "LBL_SUB_TOTAL");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent_full));
        toolbar.getBackground().setAlpha(0);
        closeImg = findViewById(R.id.closeImg);
        closeImg.setOnClickListener(new setOnClickList());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        productimage = (ImageView) findViewById(R.id.productimage);
        vegImage = (ImageView) findViewById(R.id.vegImage);
        nonvegImage = (ImageView) findViewById(R.id.nonvegImage);
        vegNonvegTxtView = (MTextView) findViewById(R.id.vegNonvegTxtView);
        vItemNameTxt = (MTextView) findViewById(R.id.vItemNameTxt);
        vItemDescTxt = (MTextView) findViewById(R.id.vItemDescTxt);
        baseFareHTxt = (MTextView) findViewById(R.id.baseFareHTxt);
        baseFareVTxt = (MTextView) findViewById(R.id.baseFareVTxt);
        topingTitleTxt = (MTextView) findViewById(R.id.topingTitleTxt);
        optionTitleTxt = (MTextView) findViewById(R.id.optionTitleTxt);
        toppingsarea = (LinearLayout) findViewById(R.id.toppingsarea);
        optionContainer = (LinearLayout) findViewById(R.id.optionContainer);
        topingContainer = (LinearLayout) findViewById(R.id.topingContainer);
        topingArea = (LinearLayout) findViewById(R.id.topingArea);
        optionArea = (LinearLayout) findViewById(R.id.optionArea);
        // addcntImgView = (ImageView) findViewById(R.id.addImgView);
        // minuscntImgView = (ImageView) findViewById(R.id.minusImgView);
        totalHTxt = (MTextView) findViewById(R.id.totalHTxt);
        totalPriceTxt = (MTextView) findViewById(R.id.totalPriceTxt);
        //  QTYNumberTxtView = (MTextView) findViewById(R.id.QTYNumberTxtView);
        addItemCartBtn = (MTextView) findViewById(R.id.addItemCartBtn);
        addarea = (LinearLayout) findViewById(R.id.addarea);
        //  minusarea = (LinearLayout) findViewById(R.id.minusarea);
        addarea.setOnClickListener(new setOnClickList());
        //  minusarea.setOnClickListener(new setOnClickList());


        minusImageView = (ImageView) findViewById(R.id.minusImageView);
        addImageView = (ImageView) findViewById(R.id.addImageView);
        autofitEditText = (AutoFitEditText) findViewById(R.id.autofitEditText);
        //autofitEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //autofitEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        autofitEditText.setClickable(false);
        rechargeBox = (MaterialEditText) findViewById(R.id.rechargeBox);
        rechargeBox.setBackgroundResource(android.R.color.transparent);
        rechargeBox.setHideUnderline(true);
        rechargeBox.setTextSize(getActContext().getResources().getDimension(R.dimen._18ssp));
        autofitEditText.setText(generalFunc.convertNumberWithRTL("" + 1));
        rechargeBox.setTextColor(getActContext().getResources().getColor(R.color.black));
        rechargeBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addImageView.setOnClickListener(view -> mangePluseView(autofitEditText));
        minusImageView.setOnClickListener(view -> mangeMinusView(autofitEditText));


        // addItemCartBtn.setOnClickListener(new setOnClickList());
        //minuscntImgView.setOnClickListener(new setOnClickList());
        // addcntImgView.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());

        setlabel();
        if (optionContainer.getChildCount() > 0) {
            optionContainer.removeAllViewsInLayout();
        }
        if (topingContainer.getChildCount() > 0) {
            topingContainer.removeAllViewsInLayout();
        }


        if (searchList.get("eFoodType")!=null && searchList.get("eFoodType").equalsIgnoreCase("Veg")) {
            vegImage.setVisibility(View.VISIBLE);
            nonvegImage.setVisibility(View.GONE);
            vegNonvegTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_VEGETARIAN"));
        } else if (searchList.get("eFoodType")!=null && searchList.get("eFoodType").equalsIgnoreCase("NonVeg")) {
            vegImage.setVisibility(View.GONE);
            nonvegImage.setVisibility(View.VISIBLE);
            vegNonvegTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_NONVEGETARIAN"));

        }

        // QTYNumberTxtView.setText(generalFunc.convertNumberWithRTL("" + 1));

    }

    public void setlabel() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_TO_BASKET"));
        baseFareHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BASIC_PRICE"));
        topingTitleTxt.setText(generalFunc.retrieveLangLBl("Select Topping", "LBL_SELECT_TOPPING"));
        optionTitleTxt.setText(generalFunc.retrieveLangLBl("Select Options", "LBL_SELECT_OPTIONS"));
        totalHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TOTAL_TXT"));
        addItemCartBtn.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_ITEM"));
    }

    RadioButton lastCheckedRB = null;

    public void setData() {
        try {

            //  baseFareVTxt.setText(searchList.get("StrikeoutPrice"));
            if (GeneralFunctions.parseDoubleValue(0, searchList.get("fOfferAmt")) > 0) {
                baseFareVTxt.setText(generalFunc.convertNumberWithRTL(searchList.get("StrikeoutPrice")));

                baseFareVTxt.setTextColor(getActContext().getResources().getColor(R.color.gray));
                SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
                SpannableString origSpan = new SpannableString(baseFareVTxt.getText());

                origSpan.setSpan(new StrikethroughSpan(), 0, baseFareVTxt.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                spanBuilder.append(origSpan);

                String priceStr = " " + generalFunc.convertNumberWithRTL(generalFunc.convertNumberWithRTL(searchList.get("fDiscountPricewithsymbol")));

                SpannableString discountSpan = new SpannableString(priceStr);
                discountSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#272727")), 0, priceStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spanBuilder.append(discountSpan);
                baseFareVTxt.setText(spanBuilder);


            } else {
                baseFareVTxt.setText(generalFunc.convertNumberWithRTL(searchList.get("StrikeoutPrice")));
                baseFareVTxt.setPaintFlags(0);
            }
            vItemNameTxt.setText(searchList.get("vItemType"));
            // android.view.animation.Animation marquee = AnimationUtils.loadAnimation(this, R.anim.marquee);
            // vItemNameTxt.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            // vItemNameTxt.setSingleLine(true);
            // vItemNameTxt.startAnimation(marquee);
            // vItemNameTxt.setMarqueeRepeatLimit(-1);
            vItemNameTxt.setSelected(true);


            boolean viewMoreSet = false;

            String vItemDesc = searchList.get("vItemDesc");

            if (Utils.checkText(vItemDesc)) {
                Spanned descdata = AppFunctions.fromHtml(vItemDesc);
                vItemDescTxt.setText(AppFunctions.fromHtml(descdata + ""));
                vItemDescTxt.setVisibility(View.VISIBLE);
                if (viewMoreSet == false) {
                    generalFunc.makeTextViewResizable(vItemDescTxt, 2, "...\n+ " + generalFunc.retrieveLangLBl("View More", "LBL_VIEW_MORE_TXT"), true, R.color.appThemeColor_1, R.dimen.txt_size_10);
                    viewMoreSet = true;
                }
            } else {
                vItemDescTxt.setVisibility(View.GONE);
            }

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            int heightOfImage = (int) getActContext().getResources().getDimension(R.dimen._230sdp);



            Picasso.get()
                    .load(Utilities.getResizeImgURL(getActContext(), searchList.get("vImage"), 0, heightOfImage, width))/*.fit()*/
                    //  .load(Utilities.getResizeImgURL(getActContext(), searchList.get("vImage"), ((int) Utils.getScreenPixelWidth(getActContext())), (int) getResources().getDimension(R.dimen._195sdp)))
                    .placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon)
                    .into(productimage, null);

            // Picasso.with(this).load(searchList.get("vImage")).placeholder(R.mipmap.ic_no_icon).into(productimage);
            MenuItemOptionToppingArr = searchList.get("MenuItemOptionToppingArr");

        JSONObject MainObject = null;
        try {
            MainObject = new JSONObject(convertStandardJSONString(MenuItemOptionToppingArr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

            if (MainObject != null) {

                JSONArray optionArray = generalFunc.getJsonArray("options", MainObject);
                if (optionArray != null && optionArray.length() > 0) {
                    for (int i = 0; i < optionArray.length(); i++) {
                        isOption = "Yes";
                        int pos = i;
                        JSONObject optionObject = generalFunc.getJsonObject(optionArray, i);
                        HashMap<String, String> optionMap = new HashMap<>();
                        LayoutInflater optioninflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View optionview = optioninflater.inflate(R.layout.item_basket_option, null);
                        MTextView optionName = optionview.findViewById(R.id.optionName);
                        MTextView optionPrice = optionview.findViewById(R.id.optionPrice);
                        RadioGroup optionRadioGroup = optionview.findViewById(R.id.optionRadioGroup);
                        RadioButton optionradioBtn = optionview.findViewById(R.id.optionradioBtn);
                        LinearLayout rowArea = optionview.findViewById(R.id.rowArea);
                        optionradioBtn.setTag(pos);
                        optionRadioGroup.setTag(pos);


//                        if (selOptionId != null && !selOptionId.equalsIgnoreCase("")) {
//                            if (selOptionId.equalsIgnoreCase(generalFunc.getJsonValue("iOptionId", optionObject.toString()))) {
//                                optionRadioGroup.check(optionradioBtn.getId());
//                                lastCheckedRB = optionradioBtn;
//                                seloptionPrice = GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("fUserPrice", optionObject.toString()));
//
//                                optionradioBtn.setChecked(true);
//                            }
//                        }

                        rowArea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                optionradioBtn.setChecked(true);
                            }
                        });

                        optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                try {


                                    if (lastCheckedRB != null) {
                                        if (lastCheckedRB == optionradioBtn) {
                                            return;
                                        }
                                    }
                                    if (seloptionPrice == 0) {
                                        seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(pos).get("fUserPrice"));
                                    } else {
                                        // String totalamout = totalPriceTxt.getText().toString().replace(searchList.get("currencySymbol"), "").trim();
                                        Double totalamout = GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice")) * getQty();

                                        totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(Math.abs(toppingPrice) + totalamout - Math.abs(seloptionPrice))));
                                        if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate")) {

                                            totalamout = 0.0;
                                            totalamout = Math.abs(seloptionPrice) * getQty();

                                            totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(totalamout)));

                                        }

                                    }

                                    if (lastCheckedRB != null) {
                                        lastCheckedRB.setChecked(false);
                                    }


                                    optionradioBtn.setChecked(true);


                                    lastCheckedRB = optionradioBtn;


                                    seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(pos).get("fUserPrice"));
                                    optionId = optionList.get(pos).get("iOptionId");
                                    // String totalamout = totalPriceTxt.getText().toString().replace(searchList.get("currencySymbol"), "").trim();
                                    Double totalamout = GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice")) + Math.abs(toppingPrice) + Math.abs(seloptionPrice);
                                    totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(totalamout * getQty())));

                                    if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate")) {

                                        totalamout = 0.0;
                                        totalamout = Math.abs(seloptionPrice) * getQty();

                                        totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(totalamout)));

                                    }
                                } catch (Exception e) {

                                }
                            }


                        });

                        optionMap.put("iOptionId", generalFunc.getJsonValueStr("iOptionId", optionObject));
                        optionMap.put("vOptionName", generalFunc.getJsonValueStr("vOptionName", optionObject));
                        optionMap.put("fPrice", generalFunc.getJsonValueStr("fPrice", optionObject));
                        optionMap.put("eOptionType", generalFunc.getJsonValueStr("eOptionType", optionObject));
                        optionMap.put("fUserPrice", generalFunc.getJsonValueStr("fUserPrice", optionObject));
                        optionMap.put("fUserPriceWithSymbol", generalFunc.getJsonValueStr("fUserPriceWithSymbol", optionObject));
                        optionName.setText(generalFunc.getJsonValueStr("vOptionName", optionObject));
                        optionPrice.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("fUserPriceWithSymbol", optionObject)));

                        optionMap.put("eDefault", generalFunc.getJsonValueStr("eDefault", optionObject));
                        if (generalFunc.getJsonValueStr("eDefault", optionObject)!=null && generalFunc.getJsonValueStr("eDefault", optionObject).equalsIgnoreCase("Yes")) {
                            optionradioBtn.setChecked(true);
                            lastCheckedRB = optionradioBtn;
                            optionId = generalFunc.getJsonValueStr("iOptionId", optionObject);

                        }

                        optionList.add(optionMap);
                        optionContainer.addView(optionview);


                        optionArea.setVisibility(View.VISIBLE);
                    }


                } else {

                    optionTitleTxt.setVisibility(View.GONE);
                }


                JSONArray addOnArray = generalFunc.getJsonArray("addon", MenuItemOptionToppingArr.toString());
                if (addOnArray != null && addOnArray.length() > 0) {

                    for (int i = 0; i < addOnArray.length(); i++) {
                        isTooping = "Yes";
                        int pos = i;
                        JSONObject topingObject = generalFunc.getJsonObject(addOnArray, i);
                        HashMap<String, String> topingMap = new HashMap<>();
                        LayoutInflater topinginflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View topingView = topinginflater.inflate(R.layout.item_basket_toping, null);
                        MTextView topingTxtView = topingView.findViewById(R.id.topingTxtView);
                        MTextView topingPriceTxtView = topingView.findViewById(R.id.topingPriceTxtView);
                        CheckBox topingCheckBox = topingView.findViewById(R.id.topingCheckBox);
                        LinearLayout row_area = topingView.findViewById(R.id.row_area);


                        topingMap.put("iOptionId", generalFunc.getJsonValue("iOptionId", topingObject.toString()));
                        topingMap.put("vOptionName", generalFunc.getJsonValue("vOptionName", topingObject.toString()));
                        topingMap.put("fPrice", generalFunc.getJsonValue("fPrice", topingObject.toString()));
                        topingMap.put("eOptionType", generalFunc.getJsonValue("eOptionType", topingObject.toString()));
                        topingMap.put("fUserPrice", generalFunc.getJsonValue("fUserPrice", topingObject.toString()));
                        topingMap.put("fUserPriceWithSymbol", generalFunc.getJsonValue("fUserPriceWithSymbol", topingObject.toString()));
                        topingTxtView.setText(generalFunc.getJsonValue("vOptionName", topingObject.toString()));
                        topingPriceTxtView.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("fUserPriceWithSymbol", topingObject.toString())));
                        topingList.add(topingMap);
                        topingContainer.addView(topingView);


//                        if (selToppingList != null) {
//                            if (selToppingList.contains(topingMap.get("iOptionId"))) {
//                                topingCheckBox.setChecked(true);
//                                toppingPrice = toppingPrice + GeneralFunctions.parseDoubleValue(0, topingList.get(pos).get("fUserPrice"));
//                            }
//                        }

                        row_area.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (topingCheckBox.isChecked()) {
                                    topingCheckBox.setChecked(false);
                                } else {
                                    topingCheckBox.setChecked(true);
                                }

                            }
                        });

                        topingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                //   String totalamout = totalPriceTxt.getText().toString().replace(searchList.get("currencySymbol"), "").trim();

                                Double totalamout = GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice"));
                                //Double totalValAmount =totalamout - Math.abs(seloptionPrice);
                                if (isChecked) {
                                    toppingListId.add(topingList.get(pos).get("iOptionId"));
                                    toppingPrice = toppingPrice + GeneralFunctions.parseDoubleValue(0, topingList.get(pos).get("fUserPrice"));


                                    totalamout = totalamout + Math.abs(seloptionPrice) + Math.abs(toppingPrice);
                                    totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(totalamout * getQty())));
                                } else {
                                    if (toppingListId.size() > 0) {
                                        if (toppingListId.contains(topingList.get(pos).get("iOptionId"))) {
                                            toppingListId.remove(topingList.get(pos).get("iOptionId"));
                                        }
                                    }
                                    toppingPrice = toppingPrice - GeneralFunctions.parseDoubleValue(0, topingList.get(pos).get("fUserPrice"));


                                    totalamout = totalamout + Math.abs(seloptionPrice) + Math.abs(toppingPrice);

                                    totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(totalamout * getQty())));
                                }
                            }
                        });
                        topingArea.setVisibility(View.VISIBLE);
                    }
                } else {
                    topingTitleTxt.setVisibility(View.GONE);
                }
                totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(((GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice")) * (getQty())) + Math.abs(toppingPrice) + Math.abs(seloptionPrice)))));

                if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate") && optionList.size() > 0) {

                    if (seloptionPrice == 0) {
                        seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(0).get("fUserPrice"));
                    }


                    totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay((Math.abs(toppingPrice) + Math.abs(seloptionPrice) * (getQty())) + Math.abs(toppingPrice))));

                }
            }else
            {
                toppingsarea.setVisibility(View.GONE);
                totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(((GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice")) * (getQty())) + Math.abs(toppingPrice) + Math.abs(seloptionPrice)))));

                if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate") && optionList.size() > 0) {

                    if (seloptionPrice == 0) {
                        seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(0).get("fUserPrice"));
                    }


                    totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay((Math.abs(toppingPrice) + Math.abs(seloptionPrice) * (getQty())) + Math.abs(toppingPrice))));

                }
            }
        } catch (Exception e) {
            Logger.d("ProducetImages", "::" + e.toString());
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            int heightOfImage = (int) getActContext().getResources().getDimension(R.dimen._230sdp);
            Picasso.get()
                    .load(Utilities.getResizeImgURL(getActContext(), searchList.get("vImage"), 0, heightOfImage, width))/*.fit()*/
                    //  .load(Utilities.getResizeImgURL(getActContext(), searchList.get("vImage"), ((int) Utils.getScreenPixelWidth(getActContext())), (int) getResources().getDimension(R.dimen._195sdp)))
                    .placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon)
                    .into(productimage, null);
            toppingsarea.setVisibility(View.GONE);
            totalPriceTxt.setText(searchList.get("currencySymbol") + " " +
                    generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(((GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice")) * (getQty())) + Math.abs(toppingPrice) + Math.abs(seloptionPrice)))));

            if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate") && optionList.size() > 0) {

                if (seloptionPrice == 0) {
                    seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(0).get("fUserPrice"));
                }


                totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay((Math.abs(toppingPrice) + Math.abs(seloptionPrice) * (getQty())) + Math.abs(toppingPrice))));

            }

        }


    }

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        return data_json;
    }

    private static DecimalFormat df2 = new DecimalFormat(".##");

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == R.id.backImgView || i == R.id.closeImg) {
                onBackPressed();
            } else if (i == R.id.addItemCartBtn) {


               /* if (searchList.get("Restaurant_Status") != null && searchList.get("Restaurant_Status").equalsIgnoreCase("closed")) {

                    generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("", "LBL_RESTAURANTS_CLOSE_NOTE"));
                    return;
                }


                Realm realm = MyApp.getRealmInstance();
                cartRealmList = realm.where(Cart.class).findAll();

                if (cartRealmList != null && cartRealmList.size() > 0 && !searchList.get("iCompanyId").equalsIgnoreCase(generalFunc.retrieveValue(Utils.COMPANY_ID))) {


                    if (optionArea.getVisibility() == View.VISIBLE) {
                        if (optionId.equalsIgnoreCase("")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_OPTIONS_REQUIRED"));
                            return;
                        }
                    }


                    GenerateAlertBox generateAlertBox = new GenerateAlertBox(getActContext());
                    generateAlertBox.setCancelable(false);
                    generateAlertBox.setContentMessage(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CART"), generalFunc.retrieveLangLBl("Are you sure you'd like to change restaurants ? Your order will be lost.", "LBL_ORDER_LOST_ALERT_TXT"));
                    generateAlertBox.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
                    generateAlertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_PROCEED"));
                    generateAlertBox.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                        @Override
                        public void handleBtnClick(int btn_id) {
                            if (btn_id == 1) {
                                deleteOptionToRealm();
                                ArrayList<String> removeData=new ArrayList<>();
                                removeData.add(Utils.COMPANY_ID);
                                removeData.add(Utils.COMPANY_MINIMUM_ORDER);
                                removeData.add(Utils.COMPANY_MAX_QTY);
                                generalFunc.removeValue(removeData);

                                generalFunc.removeAllRealmData(realm);
                                addDataToList();

                            } else {
                                generateAlertBox.closeAlertBox();
                            }
                        }
                    });
                    generateAlertBox.showAlertBox();
                } else {

                    if (optionArea.getVisibility() == View.VISIBLE) {
                        if (optionId.equalsIgnoreCase("")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_OPTIONS_REQUIRED"));
                            return;
                        }
                    }
                    addDataToList();
                }*/


            } else if (i == R.id.addarea) {


                if (searchList.get("Restaurant_Status") != null && searchList.get("Restaurant_Status").equalsIgnoreCase("closed")) {

                    generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("", "LBL_RESTAURANTS_CLOSE_NOTE"));
                    return;
                }


                Realm realm = MyApp.getRealmInstance();
                cartRealmList = realm.where(Cart.class).findAll();

                if (cartRealmList != null && cartRealmList.size() > 0 && !searchList.get("iCompanyId").equalsIgnoreCase(generalFunc.retrieveValue(Utils.COMPANY_ID))) {


                    if (optionArea.getVisibility() == View.VISIBLE) {
                        if (optionId.equalsIgnoreCase("")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_OPTIONS_REQUIRED"));
                            return;
                        }
                    }


                    GenerateAlertBox generateAlertBox = new GenerateAlertBox(getActContext());
                    generateAlertBox.setCancelable(false);
                    generateAlertBox.setContentMessage(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CART"), generalFunc.retrieveLangLBl("Are you sure you'd like to change restaurants ? Your order will be lost.", "LBL_ORDER_LOST_ALERT_TXT"));
                    generateAlertBox.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
                    generateAlertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_PROCEED"));
                    generateAlertBox.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                        @Override
                        public void handleBtnClick(int btn_id) {
                            if (btn_id == 1) {
                                deleteOptionToRealm();
                                ArrayList<String> removeData = new ArrayList<>();
                                removeData.add(Utils.COMPANY_ID);
                                removeData.add(Utils.COMPANY_MINIMUM_ORDER);
                                removeData.add(Utils.COMPANY_MAX_QTY);
                                generalFunc.removeValue(removeData);

                                generalFunc.removeAllRealmData(realm);
                                addDataToList();

                            } else {
                                generateAlertBox.closeAlertBox();
                            }
                        }
                    });
                    generateAlertBox.showAlertBox();
                } else {

                    if (optionArea.getVisibility() == View.VISIBLE) {
                        if (optionId.equalsIgnoreCase("")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_OPTIONS_REQUIRED"));
                            return;
                        }
                    }
                    addDataToList();
                }



               /* if (searchList.get("Restaurant_Status") != null && searchList.get("Restaurant_Status").equalsIgnoreCase("closed")) {

                    generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("", "LBL_RESTAURANTS_CLOSE_NOTE"));
                    return;
                }
//                int QUANTITY = Integer.parseInt(QTYNumberTxtView.getText().toString());
                int QUANTITY = getQty();


                if (QUANTITY >= 1) {
                    QUANTITY = QUANTITY + 1;
                    Double itemTotal = GeneralFunctions.parseDoubleValue(0, searchList.get("fDiscountPrice")) + Math.abs(toppingPrice) + Math.abs(seloptionPrice);

                    if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate") && optionList.size() > 0) {
                        if (seloptionPrice == 0) {
                            seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(0).get("fUserPrice"));
                        }

                        itemTotal = 0.0;
                        itemTotal = Math.abs(toppingPrice) + Math.abs(seloptionPrice);

                    }
                    totalPriceTxt.setText(LBL_SUB_TOTAL + " " + searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(QUANTITY * itemTotal)));
                    rechargeBox.setText(generalFunc.convertNumberWithRTL("" + QUANTITY));
                    //minusarea.setEnabled(true);
                }*/

            } else if (i == R.id.minusarea) {
              /*  if (searchList.get("Restaurant_Status") != null && searchList.get("Restaurant_Status").equalsIgnoreCase("closed")) {

                    generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("", "LBL_RESTAURANTS_CLOSE_NOTE"));
                    return;
                }


//                int QUANTITY = Integer.parseInt(QTYNumberTxtView.getText().toString());
                int QUANTITY = getQty();
                if (QUANTITY > 1) {
                    QUANTITY = QUANTITY - 1;

                    Double itemTotal = GeneralFunctions.parseDoubleValue(0, searchList.get("fDiscountPrice")) + Math.abs(toppingPrice) + Math.abs(seloptionPrice);
                    if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate") && optionList.size() > 0) {
                        if (seloptionPrice == 0) {
                            seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(0).get("fUserPrice"));
                        }

                        itemTotal = 0.0;
                        itemTotal = Math.abs(toppingPrice) + Math.abs(seloptionPrice);

                    }

                    totalPriceTxt.setText(LBL_SUB_TOTAL + " " + searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(QUANTITY * itemTotal)));
                    QTYNumberTxtView.setText(generalFunc.convertNumberWithRTL("" + QUANTITY));
                } else {

                }*/

            }

        }
    }

    public void addDataToList() {
        try {

            if ((realmOptionsList != null && realmOptionsList.size() > 0) || (realmToppingList != null && realmToppingList.size() > 0)) {
                storeAllOptionsToRealm();
            }

            HashMap<String, String> map = new HashMap<>();
//        map.put("Qty", QTYNumberTxtView.getText().toString().trim());
            map.put("Qty", "" + getQty());
            map.put("vItemType", searchList.get("vItemType"));
            map.put("vImage", searchList.get("vImage"));
            map.put("fDiscountPrice", searchList.get("fPrice"));
            map.put("iMenuItemId", searchList.get("iMenuItemId"));
            map.put("eFoodType", searchList.get("eFoodType"));
            map.put("iFoodMenuId", searchList.get("iFoodMenuId"));
            map.put("iCompanyId", searchList.get("iCompanyId"));
            map.put("vCompany", searchList.get("vCompany"));
            if (toppingListId.size() > 0) {
                for (int i = 0; i < toppingListId.size(); i++) {
                    if (toppingId.equals("")) {
                        toppingId = toppingListId.get(i).toString();
                    } else {
                        toppingId = toppingId + "," + toppingListId.get(i).toString();
                    }
                }
            }
            map.put("iToppingId", toppingId);


            //  basketList.add(map);
            //  generalFunc.setBasketData(basketList);
            HashMap<String, String> storeData = new HashMap<>();
            storeData.put(Utils.COMPANY_MINIMUM_ORDER, searchList.get("fMinOrderValue"));
            storeData.put(Utils.COMPANY_MAX_QTY, searchList.get("iMaxItemQty"));
            storeData.put(Utils.COMPANY_ID, searchList.get("iCompanyId"));
            generalFunc.storeData(storeData);
            setRealmData();
            // onBackPressed();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } catch (Exception e) {
            Log.e("AddToCartException", "::" + e.toString());
        }

    }


    boolean isCartNull;

    public Cart checksameRecordExist(Realm realm, String toppingId, String optionId, String iFoodMenuId, String iMenuItemId) {
        Cart cart = null;
        String[] list_topping_ids = toppingId.split(",");
        List<String> list_topping_ids_list = Arrays.asList(list_topping_ids);
        Collections.sort(list_topping_ids_list);


        RealmResults<Cart> cartlist = realm.where(Cart.class).findAll();

        if (cartlist != null && cartlist.size() > 0)

            for (int i = 0; i < realmCartList.size(); i++) {
                String[] topping_ids = realmCartList.get(i).getiToppingId().split(",");
                List<String> topping_idsList = Arrays.asList(topping_ids);
                Collections.sort(topping_idsList);
                if (topping_idsList.equals(list_topping_ids_list) &&
                        realmCartList.get(i).getiOptionId().equalsIgnoreCase(optionId) && realmCartList.get(i).getiFoodMenuId().equalsIgnoreCase(iFoodMenuId) && realmCartList.get(i).getiMenuItemId().equalsIgnoreCase(iMenuItemId)) {
                    return realmCartList.get(i);
                }
            }


        // cart = realm.where(Cart.class).("iToppingId", list_topping_ids).equalTo("iOptionId", optionId).findFirst();

        return cart;
    }

    public void setRealmData() {
        String toppingId = "";
        Realm realm = MyApp.getRealmInstance();
        //    Cart cart = realm.where(Cart.class).equalTo("iMenuItemId", searchList.get("iMenuItemId")).findFirst();

        if (toppingListId.size() > 0) {
            for (int i = 0; i < toppingListId.size(); i++) {
                if (toppingId.equals("")) {
                    toppingId = toppingListId.get(i).toString();
                } else {
                    toppingId = toppingId + "," + toppingListId.get(i).toString();
                }
            }
        }

        Cart cart = checksameRecordExist(realm, toppingId, optionId, searchList.get("iFoodMenuId"), searchList.get("iMenuItemId"));

        realm.beginTransaction();
        if (cart == null) {
            isCartNull = true;
            cart = new Cart();
            cart.setvItemType(searchList.get("vItemType"));
            cart.setvImage(searchList.get("vImage"));
            cart.setfDiscountPrice(searchList.get("fDiscountPrice"));
            cart.setiMenuItemId(searchList.get("iMenuItemId"));
            cart.seteFoodType(searchList.get("eFoodType"));
            cart.setiFoodMenuId(searchList.get("iFoodMenuId"));
            cart.setiCompanyId(searchList.get("iCompanyId"));
            cart.setvCompany(searchList.get("vCompany"));
            cart.setCurrencySymbol(searchList.get("currencySymbol"));
            if (Utils.checkText(generalFunc.getServiceId())) {
                cart.setiServiceId(generalFunc.getServiceId());
            }
            //cart.setQty(QTYNumberTxtView.getText().toString().trim());
            cart.setQty("" + getQty());
            cart.setIsOption(isOption);
            cart.setIsTooping(isTooping);
            cart.setiOptionId(optionId);
            cart.setiToppingId(toppingId);
            cart.setMilliseconds(Calendar.getInstance().getTimeInMillis());
            cart.setIspriceshow(searchList.get("ispriceshow"));
            if (isCartNull) {
                realm.insert(cart);
            } else {
                realm.insertOrUpdate(cart);
            }
        } else {

            int qty = GeneralFunctions.parseIntegerValue(0, cart.getQty());
//            int newqty = GeneralFunctions.parseIntegerValue(0, QTYNumberTxtView.getText().toString().trim());
            int newqty = getQty();

            cart.setQty((qty + newqty) + "");
            realm.insertOrUpdate(cart);

        }
        realm.commitTransaction();
    }

    public RealmResults<Options> getOptionsData() {
        Realm realm = MyApp.getRealmInstance();
        return realm.where(Options.class).findAll();
    }

    public RealmResults<Topping> getToppingData() {
        Realm realm = MyApp.getRealmInstance();
        return realm.where(Topping.class).findAll();
    }


    public void storeAllOptionsToRealm() {
        Realm realm = MyApp.getRealmInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmToppingList);
        realm.insertOrUpdate(realmOptionsList);
        realm.commitTransaction();
    }

    public void getOptionsList() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GetItemOptionAddonDetails");
        parameters.put("iCompanyId", searchList.get("iCompanyId"));
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {
                if (responseString != null && !responseString.equals("")) {
                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail == true) {
                        String message = generalFunc.getJsonValue("message", responseString);

                        JSONArray optionArray = generalFunc.getJsonArray("options", message);
                        for (int i = 0; i < optionArray.length(); i++) {
                            JSONObject optionObject = generalFunc.getJsonObject(optionArray, i);

                            Options optionsObj = new Options();
                            optionsObj.setfPrice(generalFunc.getJsonValue("fPrice", optionObject.toString()));
                            optionsObj.setfUserPrice(generalFunc.getJsonValue("fUserPrice", optionObject.toString()));
                            optionsObj.setfUserPriceWithSymbol(generalFunc.getJsonValue("fUserPriceWithSymbol", optionObject.toString()));
                            optionsObj.setiFoodMenuId(generalFunc.getJsonValue("iFoodMenuId", optionObject.toString()));
                            optionsObj.setiMenuItemId(generalFunc.getJsonValue("iMenuItemId", optionObject.toString()));
                            optionsObj.setvOptionName(generalFunc.getJsonValue("vOptionName", optionObject.toString()));
                            optionsObj.setiOptionId(generalFunc.getJsonValue("iOptionId", optionObject.toString()));
                            optionsObj.seteDefault(generalFunc.getJsonValue("eDefault", optionObject.toString()));
                            realmOptionsList.add(optionsObj);


                        }
                        JSONArray addOnArray = generalFunc.getJsonArray("addon", message);
                        for (int i = 0; i < addOnArray.length(); i++) {
                            JSONObject topingObject = generalFunc.getJsonObject(addOnArray, i);
                            Topping topppingObj = new Topping();
                            topppingObj.setfPrice(generalFunc.getJsonValue("fPrice", topingObject.toString()));
                            topppingObj.setfUserPrice(generalFunc.getJsonValue("fUserPrice", topingObject.toString()));
                            topppingObj.setfUserPriceWithSymbol(generalFunc.getJsonValue("fUserPriceWithSymbol", topingObject.toString()));
                            topppingObj.setiFoodMenuId(generalFunc.getJsonValue("iFoodMenuId", topingObject.toString()));
                            topppingObj.setiMenuItemId(generalFunc.getJsonValue("iMenuItemId", topingObject.toString()));
                            topppingObj.setvOptionName(generalFunc.getJsonValue("vOptionName", topingObject.toString()));
                            topppingObj.setiOptionId(generalFunc.getJsonValue("iOptionId", topingObject.toString()));
                            realmToppingList.add(topppingObj);
                        }

                    } else {
                    }
                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    public int getQty() {
        int qty = 1;


        String strVal = autofitEditText.getText().toString().trim().toUpperCase(Locale.US);

        qty = GeneralFunctions.parseIntegerValue(1, strVal);

        return qty;


    }


    public void mangeMinusView(AutoFitEditText rechargeBox) {
        if (Utils.checkText(rechargeBox) == true && GeneralFunctions.parseDoubleValue(0, rechargeBox.getText().toString()) > 0) {


            if (searchList.get("Restaurant_Status") != null && searchList.get("Restaurant_Status").equalsIgnoreCase("closed")) {

                generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("", "LBL_RESTAURANTS_CLOSE_NOTE"));
                return;
            }


//                int QUANTITY = Integer.parseInt(QTYNumberTxtView.getText().toString());
            int QUANTITY = getQty();
            if (QUANTITY > 1) {
                QUANTITY = QUANTITY - 1;

                Double itemTotal = GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice")) + Math.abs(toppingPrice) + Math.abs(seloptionPrice);
                if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate") && optionList.size() > 0) {
                    if (seloptionPrice == 0) {
                        seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(0).get("fUserPrice"));
                    }

                    itemTotal = 0.0;
                    itemTotal = Math.abs(toppingPrice) + Math.abs(seloptionPrice);

                }

                totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(QUANTITY * itemTotal)));
                rechargeBox.setText(generalFunc.convertNumberWithRTL("" + QUANTITY));
                rechargeBox.setEnabled(false);
            } else {

            }

        } else {
            // rechargeBox.setText(generalFunc.convertNumberWithRTL("" + 1));
        }
    }


    public void mangePluseView(AutoFitEditText rechargeBox) {
        if (Utils.checkText(rechargeBox) == true) {


            if (searchList.get("Restaurant_Status") != null && searchList.get("Restaurant_Status").equalsIgnoreCase("closed")) {

                generalFunc.showMessage(backImgView, generalFunc.retrieveLangLBl("", "LBL_RESTAURANTS_CLOSE_NOTE"));
                return;
            }
//                int QUANTITY = Integer.parseInt(QTYNumberTxtView.getText().toString());
            int QUANTITY = getQty();


            if (QUANTITY >= 1) {
                QUANTITY = QUANTITY + 1;
                Double itemTotal = GeneralFunctions.parseDoubleValue(0, searchList.get("fPrice")) + Math.abs(toppingPrice) + Math.abs(seloptionPrice);

                if (searchList.get("ispriceshow") != null && searchList.get("ispriceshow").equalsIgnoreCase("separate") && optionList.size() > 0) {
                    if (seloptionPrice == 0) {
                        seloptionPrice = GeneralFunctions.parseDoubleValue(0, optionList.get(0).get("fUserPrice"));
                    }

                    itemTotal = 0.0;
                    itemTotal = Math.abs(toppingPrice) + Math.abs(seloptionPrice);

                }
                totalPriceTxt.setText(searchList.get("currencySymbol") + " " + generalFunc.convertNumberWithRTL(GeneralFunctions.convertDecimalPlaceDisplay(QUANTITY * itemTotal)));

                // rechargeBox.setText(""+QUANTITY);

                rechargeBox.setText(generalFunc.convertNumberWithRTL("" + QUANTITY));
                rechargeBox.setEnabled(false);
            }

        } else {
            //  rechargeBox.setText("1");

        }
    }

}
