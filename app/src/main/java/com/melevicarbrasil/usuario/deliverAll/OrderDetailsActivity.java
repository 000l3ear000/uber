package com.melevicarbrasil.usuario.deliverAll;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.melevicarbrasil.usuario.Help_MainCategory;
import com.melevicarbrasil.usuario.PrescriptionActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.AppFunctions;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.realmModel.Cart;
import com.realmModel.Options;
import com.realmModel.Topping;
import com.squareup.picasso.Picasso;
import com.utils.Logger;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class OrderDetailsActivity extends AppCompatActivity {

    LinearLayout billDetails;
    GeneralFunctions generalFunc;

    ImageView backImgView;
    ImageView receiptImgView;
    MTextView titleTxt;
    String iOrderId = "";
    String iServiceId = "";
    String vImage = "";
    String vAvgRating = "";
    MTextView billTitleTxt;
    View convertView = null;
    MTextView resturantAddressTxt, deliveryaddressTxt, resturantAddressHTxt, destAddressHTxt;
    MTextView orderNoHTxt, orderNoVTxt, orderDateVTxt;
    MTextView paidviaTextH;
    MTextView deliverystatusTxt;
    LinearLayout chargeDetailArea;
    ImageView helpTxt;

    boolean ePaid = false;
    String ePaymentOption = "";
    MButton btn_type2;
    private RealmResults<Cart> cartRealmList;
    String iCompanyId = "";
    String vCompany = "";
    String toppingId = "";
    String iMenuItemId = "";
    String optionId = "";
    ArrayList<HashMap<String, String>> cartList;
    String currencySymbol = "";
    String fMinOrderValue = "";
    RealmList<Topping> realmToppingList = new RealmList<>();
    RealmList<Options> realmOptionsList = new RealmList<>();
    String isOption = "No";
    String isTooping = "No";
    LinearLayout deliveryCancelDetails;
    MTextView deliverycanclestatusTxt;
    MTextView oredrstatusTxt;
    LinearLayout cancelArea;
    private String SYSTEM_PAYMENT_FLOW = "";

    ProgressBar mProgressBar;
    View contentView;

    JSONObject userProfileJsonObj;
    MTextView viewPrescTxtView;
    ArrayList<HashMap<String, String>> imageList = new ArrayList<>();
    private SelectableRoundedImageView restaurantImgView;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        size = (int) this.getResources().getDimension(R.dimen._55sdp);
        userProfileJsonObj = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
        SYSTEM_PAYMENT_FLOW = generalFunc.getJsonValueStr("SYSTEM_PAYMENT_FLOW", userProfileJsonObj);
        cartList = new ArrayList<>();
        billDetails = (LinearLayout) findViewById(R.id.billDetails);
        viewPrescTxtView = (MTextView) findViewById(R.id.viewPrescTxtView);
        viewPrescTxtView.setOnClickListener(new setOnClickList());
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        receiptImgView = (ImageView) findViewById(R.id.receiptImgView);

        backImgView = (ImageView) findViewById(R.id.backImgView);
        billTitleTxt = (MTextView) findViewById(R.id.billTitleTxt);
        resturantAddressTxt = (MTextView) findViewById(R.id.resturantAddressTxt);
        resturantAddressHTxt = (MTextView) findViewById(R.id.resturantAddressHTxt);
        deliveryaddressTxt = (MTextView) findViewById(R.id.deliveryaddressTxt);

        orderNoHTxt = (MTextView) findViewById(R.id.orderNoHTxt);
        orderNoVTxt = (MTextView) findViewById(R.id.orderNoVTxt);
        orderDateVTxt = (MTextView) findViewById(R.id.orderDateVTxt);

        destAddressHTxt = (MTextView) findViewById(R.id.destAddressHTxt);
        paidviaTextH = (MTextView) findViewById(R.id.paidviaTextH);
        deliverystatusTxt = (MTextView) findViewById(R.id.deliverystatusTxt);
        chargeDetailArea = (LinearLayout) findViewById(R.id.chargeDetailArea);
        helpTxt = (ImageView) findViewById(R.id.helpTxt);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        deliveryCancelDetails = (LinearLayout) findViewById(R.id.deliveryCancelDetails);
        deliverycanclestatusTxt = (MTextView) findViewById(R.id.deliverycanclestatusTxt);
        restaurantImgView = (SelectableRoundedImageView) findViewById(R.id.restaurantImgView);
        oredrstatusTxt = (MTextView) findViewById(R.id.oredrstatusTxt);
        cancelArea = (LinearLayout) findViewById(R.id.cancelArea);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        contentView = findViewById(R.id.contentView);


        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());
        backImgView.setOnClickListener(new setOnClickList());
        receiptImgView.setOnClickListener(new setOnClickList());
        helpTxt.setOnClickListener(new setOnClickList());
        iOrderId = getIntent().getStringExtra("iOrderId");

        setLabel();

        getOrderDetails();

    }

    public void setLabel() {
        billTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BILL_DETAILS"));
        titleTxt.setText(generalFunc.retrieveLangLBl("RECEIPT", "LBL_RECEIPT_HEADER_TXT"));
        destAddressHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DELIVERY_ADDRESS"));
        Logger.d("BTN_DISPLAY", "LBL_REORDER::" + generalFunc.retrieveLangLBl("", "LBL_REORDER"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_REORDER"));
        viewPrescTxtView.setText(generalFunc.retrieveLangLBl("View Prescription", "LBL_VIEW_PRESCRIPTION"));
    }


    public void getOrderDetails() {

        mProgressBar.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        // helpTxt.setVisibility(View.GONE);

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", " GetOrderDetailsRestaurant");
        parameters.put("iOrderId", iOrderId);
        parameters.put("UserType", Utils.userType);
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    JSONObject message = generalFunc.getJsonObject(Utils.message_str, responseString);

                    iCompanyId = generalFunc.getJsonValueStr("iCompanyId", message);
                    currencySymbol = generalFunc.getJsonValueStr("currencySymbol", message);
                    fMinOrderValue = generalFunc.getJsonValueStr("fMinOrderValue", message);
                    vCompany = generalFunc.getJsonValueStr("vCompany", message);
                    iOrderId = generalFunc.getJsonValueStr("iOrderId", message);
                    iServiceId = generalFunc.getJsonValueStr("iServiceId", message);
                    vImage = generalFunc.getJsonValueStr("companyImage", message);
                    vAvgRating = generalFunc.getJsonValueStr("vAvgRating", message);
                    if (vAvgRating.isEmpty()) vAvgRating = "0.0";
                    ((SimpleRatingBar) findViewById(R.id.ratingBar)).setRating(Float.parseFloat(vAvgRating));
                    setImage();


                    if (generalFunc.getJsonValueStr("DisplayReorder", message).equalsIgnoreCase("Yes")) {
                        Logger.d("BTN_DISPLAY", "Make VISIBLE::");
                        findViewById(R.id.btnArea).setVisibility(View.VISIBLE);
                        helpTxt.setVisibility(View.VISIBLE);
                        receiptImgView.setVisibility(View.VISIBLE);
//                        btn_type2.setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.btnArea).setVisibility(View.GONE);
                        helpTxt.setVisibility(View.GONE);
                        receiptImgView.setVisibility(View.GONE);
//                        btn_type2.setVisibility(View.GONE);
                    }


                    resturantAddressTxt.setText(generalFunc.getJsonValueStr("vRestuarantLocation", message));
                    deliveryaddressTxt.setText(generalFunc.getJsonValueStr("DeliveryAddress", message));

                    orderNoHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ORDER_NO_TXT"));
                    orderNoVTxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vOrderNo", message)));
                    orderDateVTxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(generalFunc.getJsonValueStr("tOrderRequestDate_Org", message), Utils.OriginalDateFormate, Utils.getDetailDateFormat(getActContext()))));


                    resturantAddressHTxt.setText(generalFunc.getJsonValueStr("vCompany", message));
                    JSONArray itemListArr = null;
                    itemListArr = generalFunc.getJsonArray("itemlist", message);
                    if (billDetails.getChildCount() > 0) {
                        billDetails.removeAllViewsInLayout();
                    }
                    addItemDetailLayout(itemListArr);

                    JSONArray PrescriptionImages = generalFunc.getJsonArray("PrescriptionImages", message);

                    if (PrescriptionImages != null && !PrescriptionImages.equals("")) {
                        findViewById(R.id.viewPrescriptionArea).setVisibility(View.VISIBLE);
                        findViewById(R.id.viewPrescriptionArea).setOnClickListener(new setOnClickList());

                        for (int i = 0; i < PrescriptionImages.length(); i++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("vImage", generalFunc.getJsonValue(PrescriptionImages, i).toString());
                            imageList.add(map);

                        }

                    }

                    String LBL_PAID_VIA = generalFunc.retrieveLangLBl("", "LBL_PAID_VIA");
                    if (generalFunc.getJsonValueStr("ePaymentOption", message).equalsIgnoreCase("Cash")) {
                        ((ImageView) findViewById(R.id.paymentTypeImgeView)).setImageResource(R.drawable.ic_cash_payment);
                        paidviaTextH.setText(LBL_PAID_VIA + " " + generalFunc.retrieveLangLBl("", "LBL_CASH_TXT"));
                    } else if (generalFunc.getJsonValueStr("ePaymentOption", message).equalsIgnoreCase("Card")) {
                        if (Utils.checkText(SYSTEM_PAYMENT_FLOW) && !SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                            ((ImageView) findViewById(R.id.paymentTypeImgeView)).setImageResource(R.mipmap.ic_menu_wallet);
                            paidviaTextH.setText(generalFunc.retrieveLangLBl("", "LBL_PAID_VIA_WALLET"));
                        } else {
                            ((ImageView) findViewById(R.id.paymentTypeImgeView)).setImageResource(R.mipmap.ic_card_new);
                            paidviaTextH.setText(LBL_PAID_VIA + " " + generalFunc.retrieveLangLBl("", "LBL_CARD"));
                        }
                    }


                    JSONArray FareDetailsArr = null;
                    FareDetailsArr = generalFunc.getJsonArray("FareDetailsArr", message);

                    if (generalFunc.getJsonValueStr("iStatusCode", message).equalsIgnoreCase("6") && generalFunc.getJsonValueStr("ePaid", message).equals("Yes")) {
                        ePaid = true;
                        ePaymentOption = generalFunc.getJsonValueStr("ePaymentOption", message);
                        deliverystatusTxt.setVisibility(View.VISIBLE);
                        deliverystatusTxt.setText(AppFunctions.fromHtml(generalFunc.getJsonValueStr("vStatusNew", message)));
                        findViewById(R.id.PayTypeArea).setVisibility(View.VISIBLE);

                        JSONArray OrderDataArray = generalFunc.getJsonArray("DataReorder", message);

                        if (OrderDataArray != null) {
                            for (int i = 0; i < OrderDataArray.length(); i++) {
                                JSONObject orderObj = generalFunc.getJsonObject(OrderDataArray, i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("Qty", generalFunc.getJsonValueStr("iQty", orderObj));
                                map.put("vItemType", generalFunc.getJsonValueStr("MenuItem", orderObj));
                                map.put("vImage", generalFunc.getJsonValueStr("vImage", orderObj));
                                map.put("fDiscountPrice", generalFunc.getJsonValueStr("fPrice", orderObj));

                                map.put("eFoodType", generalFunc.getJsonValueStr("eFoodType", orderObj));
                                map.put("iFoodMenuId", generalFunc.getJsonValueStr("iFoodMenuId", orderObj));
                                map.put("iCompanyId", iCompanyId);
                                map.put("vCompany", vCompany);
                                optionId = generalFunc.getJsonValueStr("vOptionId", orderObj);
                                iMenuItemId = generalFunc.getJsonValueStr("iMenuItemId", orderObj);
                                map.put("iMenuItemId", iMenuItemId);
                                map.put("optionId", optionId);
                                map.put("ispriceshow", generalFunc.getJsonValue("ispriceshow", responseString));

                                JSONArray toppingArray = generalFunc.getJsonArray("AddOnItemArr", orderObj);

                                if (toppingArray != null) {
                                    for (int j = 0; j < toppingArray.length(); j++) {

                                        JSONObject toppingObject = generalFunc.getJsonObject(toppingArray, j);

                                        if (toppingId.equals("")) {
                                            toppingId = generalFunc.getJsonValueStr("vAddonId", toppingObject);
                                        } else {
                                            toppingId = toppingId + "," + generalFunc.getJsonValueStr("vAddonId", toppingObject);
                                        }


                                    }
                                }

                                map.put("iToppingId", toppingId);
                                cartList.add(map);
                            }

                            JSONArray optionArray = generalFunc.getJsonArray("options", message);
                            for (int i = 0; i < optionArray.length(); i++) {
                                isOption = "Yes";
                                JSONObject optionObject = generalFunc.getJsonObject(optionArray, i);

                                Options optionsObj = new Options();
                                optionsObj.setfPrice(generalFunc.getJsonValueStr("fPrice", optionObject));
                                optionsObj.setfUserPrice(generalFunc.getJsonValueStr("fUserPrice", optionObject));
                                optionsObj.setfUserPriceWithSymbol(generalFunc.getJsonValueStr("fUserPriceWithSymbol", optionObject));
                                optionsObj.setiFoodMenuId(generalFunc.getJsonValueStr("iFoodMenuId", optionObject));
                                optionsObj.setiMenuItemId(generalFunc.getJsonValueStr("iMenuItemId", optionObject));
                                optionsObj.setvOptionName(generalFunc.getJsonValueStr("vOptionName", optionObject));
                                optionsObj.setiOptionId(generalFunc.getJsonValueStr("iOptionId", optionObject));
                                optionsObj.seteDefault(generalFunc.getJsonValueStr("eDefault", optionObject));
                                realmOptionsList.add(optionsObj);
                            }

                            JSONArray addOnArray = generalFunc.getJsonArray("addon", message);
                            for (int i = 0; i < addOnArray.length(); i++) {
                                isTooping = "Yes";
                                JSONObject topingObject = generalFunc.getJsonObject(addOnArray, i);
                                Topping topppingObj = new Topping();
                                topppingObj.setfPrice(generalFunc.getJsonValueStr("fPrice", topingObject));
                                topppingObj.setfUserPrice(generalFunc.getJsonValueStr("fUserPrice", topingObject));
                                topppingObj.setfUserPriceWithSymbol(generalFunc.getJsonValueStr("fUserPriceWithSymbol", topingObject));
                                topppingObj.setiFoodMenuId(generalFunc.getJsonValueStr("iFoodMenuId", topingObject));
                                topppingObj.setiMenuItemId(generalFunc.getJsonValueStr("iMenuItemId", topingObject));
                                topppingObj.setvOptionName(generalFunc.getJsonValueStr("vOptionName", topingObject));
                                topppingObj.setiOptionId(generalFunc.getJsonValueStr("iOptionId", topingObject));
                                realmToppingList.add(topppingObj);
                            }
                        }

                    } else if (generalFunc.getJsonValueStr("iStatusCode", message).equalsIgnoreCase("8")) {
                        deliveryCancelDetails.setVisibility(View.GONE);
                        deliverycanclestatusTxt.setText(generalFunc.getJsonValueStr("OrderStatustext", message));
                        if (!generalFunc.getJsonValueStr("OrderMessage", message).equals("") && generalFunc.getJsonValueStr("CancelOrderMessage", message) != null) {
                            deliveryCancelDetails.setVisibility(View.VISIBLE);
                            deliverycanclestatusTxt.setVisibility(View.GONE);
                            oredrstatusTxt.setVisibility(View.VISIBLE);
                            oredrstatusTxt.setText(generalFunc.getJsonValueStr("CancelOrderMessage", message));
                        }
                    } else if (generalFunc.getJsonValueStr("iStatusCode", message).equalsIgnoreCase("7")) {
                        deliveryCancelDetails.setVisibility(View.VISIBLE);
                        cancelArea.setVisibility(View.GONE);
                        if (!generalFunc.getJsonValueStr("CancelOrderMessage", message).equals("") && generalFunc.getJsonValueStr("CancelOrderMessage", message) != null) {
                            oredrstatusTxt.setVisibility(View.VISIBLE);
                            oredrstatusTxt.setText(generalFunc.getJsonValueStr("CancelOrderMessage", message));
                        }

                    } else {
                       // deliverystatusTxt.setVisibility(View.GONE);

                        if (findViewById(R.id.btnArea).getVisibility() == View.GONE) {
                            findViewById(R.id.paymentMainArea).setVisibility(View.GONE);
                        } else {
                            //  helpTxt.setVisibility(View.GONE);
                            findViewById(R.id.PayTypeArea).setVisibility(View.GONE);
                        }
                    }

                    deliverystatusTxt.setText(generalFunc.getJsonValueStr("vStatusNew", message));

                    addFareDetailLayout(FareDetailsArr);

                    contentView.setVisibility(View.VISIBLE);
                } else {
                    generalFunc.showError(true);
                }
            } else {
                generalFunc.showError(true);
            }

            mProgressBar.setVisibility(View.GONE);

        });
        exeWebServer.execute();
    }

    private void setImage() {
        if (Utils.checkText(vImage)) {

            Picasso.get()
                    .load(vImage)
                    .placeholder(R.mipmap.ic_no_pic_user)
                    .error(R.mipmap.ic_no_pic_user)
                    .into(restaurantImgView);
        }
    }

    private void addItemDetailLayout(JSONArray jobjArray) {


        for (int i = 0; i < jobjArray.length(); i++) {
            JSONObject jobject = generalFunc.getJsonObject(jobjArray, i);
            try {
                additemDetailRow(jobject.getString("vImage"), jobject.getString("MenuItem"), jobject.getString("SubTitle"), jobject.getString("fTotPrice"), /*" x " + */"" + jobject.get("iQty"), jobject.getString("TotalDiscountPrice"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addFareDetailLayout(JSONArray jobjArray) {

        if (chargeDetailArea.getChildCount() > 0) {
            chargeDetailArea.removeAllViewsInLayout();
        }

        for (int i = 0; i < jobjArray.length(); i++) {
            JSONObject jobject = generalFunc.getJsonObject(jobjArray, i);
            try {
                addFareDetailRow(jobject.names().getString(0), jobject.get(jobject.names().getString(0)).toString(), (jobjArray.length() - 1) == i ? true : false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addFareDetailRow(String row_name, String row_value, boolean isLast) {
        View convertView = null;
        if (row_name.equalsIgnoreCase("eDisplaySeperator")) {
            convertView = new View(getActContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(getActContext(), 1));
            params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen._5sdp));
            convertView.setBackgroundColor(Color.parseColor("#dedede"));
            convertView.setLayoutParams(params);
        } else {
            LayoutInflater infalInflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.design_fare_deatil_row, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, (int) getResources().getDimension(R.dimen._10sdp), 0, isLast ? (int) getResources().getDimension(R.dimen._10sdp) : 0);
            convertView.setLayoutParams(params);

            MTextView titleHTxt = (MTextView) convertView.findViewById(R.id.titleHTxt);
            MTextView titleVTxt = (MTextView) convertView.findViewById(R.id.titleVTxt);

            titleHTxt.setText(generalFunc.convertNumberWithRTL(row_name));
            titleVTxt.setText(generalFunc.convertNumberWithRTL(row_value));

            if (isLast) {
                // CALCULATE individual fare & show
                titleHTxt.setTextColor(getResources().getColor(R.color.black));
                titleHTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Poppins_SemiBold.ttf");
                titleHTxt.setTypeface(face);
                titleVTxt.setTypeface(face);
                titleVTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                titleVTxt.setTextColor(getResources().getColor(R.color.appThemeColor_1));

            }

        }

        chargeDetailArea.addView(convertView);
    }

    private void additemDetailRow(String itemImage, String menuitemName, String subMenuName, String itemPrice, String qty, String discountprice) {
        final LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_select_bill_design, null);

        MTextView billItems = (MTextView) view.findViewById(R.id.billItems);
        MTextView billItemsQty = (MTextView) view.findViewById(R.id.billItemsQty);
        ImageView imageFoodType = (ImageView) view.findViewById(R.id.imageFoodType);

        MTextView serviceTypeNameTxtView = (MTextView) view.findViewById(R.id.serviceTypeNameTxtView);
        MTextView strikeoutbillAmount = (MTextView) view.findViewById(R.id.strikeoutbillAmount);

        final MTextView billAmount = (MTextView) view.findViewById(R.id.billAmount);


        Picasso.get().load(Utilities.getResizeImgURL(getActContext(), itemImage, size, size)).placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon).into(imageFoodType);


        billAmount.setText(generalFunc.convertNumberWithRTL(itemPrice));
        billItemsQty.setText(generalFunc.convertNumberWithRTL(qty));

        billItems.setText(menuitemName);
        if (!subMenuName.equalsIgnoreCase("")) {
            serviceTypeNameTxtView.setVisibility(View.VISIBLE);
            serviceTypeNameTxtView.setText(subMenuName);
        } else {
            serviceTypeNameTxtView.setVisibility(View.GONE);
        }



        if (discountprice != null && !discountprice.equals("")) {
            SpannableStringBuilder spanBuilder = new SpannableStringBuilder();

            SpannableString origSpan = new SpannableString(billAmount.getText());

            origSpan.setSpan(new StrikethroughSpan(), 0, billAmount.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            spanBuilder.append(origSpan);

            strikeoutbillAmount.setVisibility(View.VISIBLE);
            strikeoutbillAmount.setText(spanBuilder);
            billAmount.setText(discountprice);
            billAmount.setTextColor(getResources().getColor(R.color.appThemeColor_1));
        } else {
            strikeoutbillAmount.setVisibility(View.GONE);
            billAmount.setTextColor(getResources().getColor(R.color.appThemeColor_1));
            billAmount.setPaintFlags(billAmount.getPaintFlags());
        }


        billDetails.addView(view);
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            Logger.d("BTN_DISPLAY", "i::" + (i == btn_type2.getId()));

            if (i == R.id.backImgView) {
                onBackPressed();
            } else if (i == helpTxt.getId()) {
                Bundle bn = new Bundle();
                bn.putString("iOrderId", iOrderId);
                new StartActProcess(getActContext()).startActWithData(Help_MainCategory.class, bn);
            } else if (i == btn_type2.getId()) {
                manageReorder();
            } else if (view.getId() == R.id.viewPrescriptionArea|| view.getId()==R.id.viewPrescTxtView) {
                Bundle bn = new Bundle();
                bn.putSerializable("imageList", imageList);
                bn.putBoolean("isOrder", true);
                (new StartActProcess(getActContext())).startActWithData(PrescriptionActivity.class, bn);

            } else if (view.getId() == R.id.receiptImgView) {
                sendReceipt();
            }

        }
    }

    public void sendReceipt() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getReceiptOrder");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iOrderId", iOrderId);
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public void addDataToList(Realm realm) {
        ArrayList<String> removeData = new ArrayList<>();
        removeData.add(Utils.COMPANY_ID);
        removeData.add(Utils.COMPANY_MINIMUM_ORDER);
        generalFunc.removeValue(removeData);
        generalFunc.removeAllRealmData(realm);

        if ((realmOptionsList != null && realmOptionsList.size() > 0) || (realmToppingList != null && realmToppingList.size() > 0)) {
            storeAllOptionsToRealm();
        }

        setRealmdData();
    }

    public void storeAllOptionsToRealm() {
        Realm realm = MyApp.getRealmInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmToppingList);
        realm.insertOrUpdate(realmOptionsList);
        realm.commitTransaction();
    }


    boolean isCartNull;

    public void setRealmdData() {

        Realm realm = MyApp.getRealmInstance();

        for (int i = 0; i < cartList.size(); i++) {

            HashMap<String, String> cartData = cartList.get(i);
            realm.beginTransaction();
            //if (cart == null) {
            isCartNull = true;
            Cart cart = new Cart();
            cart.setvItemType(cartData.get("vItemType"));
            cart.setvImage(cartData.get("vImage"));
            cart.setfDiscountPrice(cartData.get("fDiscountPrice"));
            cart.setiMenuItemId(cartData.get("iMenuItemId"));
            cart.seteFoodType(cartData.get("eFoodType"));
            cart.setiServiceId(iServiceId);
            cart.setiFoodMenuId(cartData.get("iFoodMenuId"));
            cart.setiCompanyId(cartData.get("iCompanyId"));
            cart.setvCompany(cartData.get("vCompany"));
            cart.setCurrencySymbol(currencySymbol);
            cart.setQty(cartData.get("Qty"));
            cart.setIsOption(isOption);
            cart.setIsTooping(isTooping);
            cart.setIspriceshow(cartData.get("ispriceshow"));
            cart.setMilliseconds(Calendar.getInstance().getTimeInMillis());


            cart.setiOptionId(cartData.get("optionId"));
            cart.setiToppingId(cartData.get("iToppingId"));
            if (isCartNull) {
                realm.insert(cart);
            } else {
                realm.insertOrUpdate(cart);
            }

            realm.commitTransaction();
        }

        generalFunc.storeData(Utils.COMPANY_MINIMUM_ORDER, fMinOrderValue);
        generalFunc.storeData(Utils.COMPANY_ID, iCompanyId);

        Bundle bn = new Bundle();
        bn.putBoolean("isRestart", true);
        new StartActProcess(getActContext()).startActWithData(EditCartActivity.class, bn);
    }


    public void manageReorder() {
        Realm realm = MyApp.getRealmInstance();
        cartRealmList = realm.where(Cart.class).findAll();

        if (cartRealmList != null && cartRealmList.size() > 0) {

            GenerateAlertBox generateAlertBox = new GenerateAlertBox(getActContext());
            generateAlertBox.setCancelable(false);
            generateAlertBox.setContentMessage(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CART"), generalFunc.retrieveLangLBl("Are you sure you'd like to change restaurants ? Your order will be lost.", "LBL_ORDER_LOST_ALERT_TXT"));
            generateAlertBox.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
            generateAlertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_PROCEED"));
            generateAlertBox.setBtnClickList(btn_id -> {
                if (btn_id == 1) {
                    deleteOptionToRealm();

                    addDataToList(realm);

                } else {
                    generateAlertBox.closeAlertBox();
                }
            });
            generateAlertBox.showAlertBox();

        } else {
            deleteOptionToRealm();
            addDataToList(realm);
        }
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
        return OrderDetailsActivity.this;
    }
}
