package com.melevicarbrasil.usuario.deliverAll;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.files.deliverAll.RestaurantSearchAdapter;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.MTextView;
import com.view.anim.loader.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantsSearchActivity extends AppCompatActivity implements RestaurantSearchAdapter.RestaurantOnClickListener {

    GeneralFunctions generalFunc;
    EditText searchTxtView;
    ImageView imageCancel;
    MTextView cancelTxt;
    MTextView cusineTitleTxt;
    LinearLayout cusinecontainArea;
    Double latitude;
    Double longitude;
    RestaurantSearchAdapter restaurantAdapter;

    ArrayList<HashMap<String, String>> cuisineList = new ArrayList<>();

    ArrayList<HashMap<String, String>> restaurantArr_List = new ArrayList<>();
    RecyclerView restaurantSearchRecycView;

    LinearLayout cusineArea, restaurantsArea;
    ImageView noSearchImage;
    String userProfileJson;
    MTextView norecordTxt;
    AVLoadingIndicatorView loaderView;
    boolean isFavChange=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_search);


        initView();


    }

    public void initView() {
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        searchTxtView = (EditText) findViewById(R.id.searchTxtView);
        imageCancel = (ImageView) findViewById(R.id.imageCancel);
        cancelTxt = (MTextView) findViewById(R.id.cancelTxt);
        cusineTitleTxt = (MTextView) findViewById(R.id.cusineTitleTxt);
        cusinecontainArea = (LinearLayout) findViewById(R.id.cusinecontainArea);
        restaurantSearchRecycView = (RecyclerView) findViewById(R.id.restaurantSearchRecycView);
        cusineArea = (LinearLayout) findViewById(R.id.cusineArea);
        restaurantsArea = (LinearLayout) findViewById(R.id.restaurantsArea);
        noSearchImage = (ImageView) findViewById(R.id.noSearchImage);
        norecordTxt = (MTextView) findViewById(R.id.norecordTxt);
        loaderView = (AVLoadingIndicatorView) findViewById(R.id.loaderView);

        restaurantSearchRecycView.setLayoutManager(new LinearLayoutManager(getActContext()));
        cancelTxt.setOnClickListener(new setOnClickList());
        imageCancel.setOnClickListener(new setOnClickList());

        latitude = getIntent().getDoubleExtra("lat", 0);
        longitude = getIntent().getDoubleExtra("long", 0);

        restaurantAdapter = new RestaurantSearchAdapter(getActContext(), restaurantArr_List);
//        restaurantListRecycView.setAdapter(restaurantAdapter);
        restaurantSearchRecycView.setAdapter(restaurantAdapter);
        restaurantAdapter.setOnRestaurantItemClick(this);
        cusineTitleTxt.setText(generalFunc.retrieveLangLBl("","LBL_CUISINES"));

        setLabel();


        searchTxtView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 2) {

                    getRestaurantList(searchTxtView.getText().toString().trim());

                } else {
                    noSearchImage.setVisibility(View.VISIBLE);
                    cuisineList.clear();
                    addCusineView();
                    restaurantArr_List.clear();
                    restaurantAdapter.notifyDataSetChanged();
                    restaurantsArea.setVisibility(View.GONE);
                    cusineArea.setVisibility(View.GONE);

                }

            }
        });
    }

    public void setLabel() {
        searchTxtView.setHint(generalFunc.retrieveLangLBl("", "LBL_SEARCH_RESTAURANT"));
        cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        norecordTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_RECORD_FOUND"));


    }

    public Context getActContext() {
        return RestaurantsSearchActivity.this;
    }

    @Override
    public void setOnRestaurantclick(int position) {
        HashMap<String, String> posData = restaurantArr_List.get(position);
        Bundle bn = new Bundle();
        bn.putString("iCompanyId", posData.get("iCompanyId"));
        bn.putString("Restaurant_Status", posData.get("Restaurant_Status"));
        bn.putString("ispriceshow", posData.get("ispriceshow"));
        bn.putString("lat", latitude + "");
        bn.putString("long", longitude + "");
        new StartActProcess(getActContext()).startActForResult(RestaurantAllDetailsNewActivity.class, bn,111);

    }

    @Override
    public void setOnRestaurantclick(int position, boolean liked) {

    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());

            if (i == R.id.imageCancel) {
                loaderView.setVisibility(View.GONE);
                norecordTxt.setVisibility(View.GONE);
                searchTxtView.setText("");
            } else if (i == R.id.cancelTxt) {
                onBackPressed();
            }


        }
    }


    @Override
    public void onBackPressed() {

        if (isFavChange) {
            Bundle bn = new Bundle();
            (new StartActProcess(getActContext())).setOkResult(bn);
            finish();
            return;
        }

        super.onBackPressed();
    }

    private void getRestaurantList(String searchword) {
        loaderView.setVisibility(View.VISIBLE);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "loadSearchRestaurants");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("PassengerLat", "" + latitude);
        parameters.put("PassengerLon", "" + longitude);
        parameters.put("searchword", searchword.trim());
        if (getIntent().getStringExtra("address") != null && !getIntent().getStringExtra("address").equals("")) {
            parameters.put("vAddress", getIntent().getStringExtra("address"));
        }
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {
                JSONObject responseObj=generalFunc.getJsonObject(responseString);

                if (responseObj != null && !responseObj.equals("")) {

                    loaderView.setVisibility(View.GONE);

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                    if (isDataAvail == true) {

                        if (cusinecontainArea.getChildCount() > 0) {
                            cusinecontainArea.removeAllViewsInLayout();
                        }
                        restaurantArr_List.clear();


                        JSONArray restaurant_Arr = generalFunc.getJsonArray("message", responseObj);
                        norecordTxt.setVisibility(View.GONE);

                        if (restaurant_Arr != null)
                        {
                            String LBL_OPEN_NOW="",LBL_CLOSED_TXT="",LBL_NOT_ACCEPT_ORDERS_TXT="";

                            if (restaurant_Arr.length()>0)
                            {
                                LBL_OPEN_NOW=generalFunc.retrieveLangLBl("Open Now", "LBL_OPEN_NOW");
                                LBL_CLOSED_TXT=generalFunc.retrieveLangLBl("close", "LBL_CLOSED_TXT");
                                LBL_NOT_ACCEPT_ORDERS_TXT= generalFunc.retrieveLangLBl("", "LBL_NOT_ACCEPT_ORDERS_TXT");
                            }
                            for (int i = 0; i < restaurant_Arr.length(); i++) {

                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject restaurant_Obj = generalFunc.getJsonObject(restaurant_Arr, i);
                                map.put("vCompany", generalFunc.getJsonValueStr("vCompany", restaurant_Obj));
                                map.put("tClocation", generalFunc.getJsonValueStr("tClocation", restaurant_Obj));
                                map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", restaurant_Obj));
                                map.put("vPhone", generalFunc.getJsonValueStr("vPhone", restaurant_Obj));
                                map.put("vImage", generalFunc.getJsonValueStr("vImage", restaurant_Obj));

                                map.put("vLatitude", generalFunc.getJsonValueStr("vLatitude", restaurant_Obj));
                                map.put("vLongitude", generalFunc.getJsonValueStr("vLongitude", restaurant_Obj));

                                map.put("vFromTimeSlot1", generalFunc.getJsonValueStr("vFromTimeSlot1", restaurant_Obj));
                                map.put("vToTimeSlot1", generalFunc.getJsonValueStr("vToTimeSlot1", restaurant_Obj));
                                map.put("vFromTimeSlot2", generalFunc.getJsonValueStr("vFromTimeSlot2", restaurant_Obj));
                                map.put("vToTimeSlot2", generalFunc.getJsonValueStr("vToTimeSlot2", restaurant_Obj));
                                map.put("fMinOrderValue", generalFunc.getJsonValueStr("fMinOrderValue", restaurant_Obj));
                                map.put("Restaurant_Cuisine", generalFunc.getJsonValueStr("Restaurant_Cuisine", restaurant_Obj));
                                map.put("fPrepareTime", generalFunc.getJsonValueStr("fPrepareTime", restaurant_Obj));
                                map.put("Restaurant_Status", generalFunc.getJsonValueStr("restaurantstatus", restaurant_Obj));

                                String Restaurant_Opentime = generalFunc.getJsonValueStr("Restaurant_Opentime", restaurant_Obj);
                                map.put("Restaurant_Opentime", Restaurant_Opentime);
                                map.put("Restaurant_OpentimeConverted", generalFunc.convertNumberWithRTL(Restaurant_Opentime));

                                map.put("Restaurant_Closetime", generalFunc.getJsonValueStr("Restaurant_Closetime", restaurant_Obj));
                                map.put("Restaurant_OfferMessage", generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj));
                                String vAvgRating = generalFunc.getJsonValueStr("vAvgRating", restaurant_Obj);
                                map.put("vAvgRating", vAvgRating);
                                map.put("vAvgRatingConverted", generalFunc.convertNumberWithRTL(vAvgRating));

                                String Restaurant_PricePerPerson = generalFunc.getJsonValueStr("Restaurant_PricePerPerson", restaurant_Obj);
                                map.put("Restaurant_PricePerPerson", Restaurant_PricePerPerson);
                                map.put("Restaurant_PricePerPersonConverted", generalFunc.convertNumberWithRTL(Restaurant_PricePerPerson));

                                String Restaurant_OrderPrepareTime = generalFunc.getJsonValueStr("Restaurant_OrderPrepareTime", restaurant_Obj);
                                map.put("Restaurant_OrderPrepareTime", Restaurant_OrderPrepareTime);
                                map.put("Restaurant_OrderPrepareTimeConverted", generalFunc.convertNumberWithRTL(Restaurant_OrderPrepareTime));


                               // String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage_short", restaurant_Obj);
                                String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj);
                                map.put("Restaurant_OfferMessage_short", Restaurant_OfferMessage_short);
                                map.put("Restaurant_OfferMessage_shortConverted", generalFunc.convertNumberWithRTL(Restaurant_OfferMessage_short));

                                map.put("Restaurant_MinOrderValue", generalFunc.getJsonValueStr("Restaurant_MinOrderValue_Orig", restaurant_Obj));
                                map.put("eAvailable", generalFunc.getJsonValueStr("eAvailable", restaurant_Obj));
                                map.put("eFavStore", generalFunc.getJsonValueStr("eFavStore", restaurant_Obj));

                                map.put("LBL_OPEN_NOW", LBL_OPEN_NOW);
                                map.put("LBL_CLOSED_TXT",LBL_CLOSED_TXT);
                                map.put("LBL_NOT_ACCEPT_ORDERS_TXT",LBL_NOT_ACCEPT_ORDERS_TXT);

                                map.put("ispriceshow", generalFunc.getJsonValueStr("ispriceshow", responseObj));
                                restaurantArr_List.add(map);
                            }
                    }

                        cuisineList.clear();
                        JSONArray cusine_array = generalFunc.getJsonArray("message_cusine", responseObj);

                        for (int i = 0; i < cusine_array.length(); i++) {

                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject cusine_Obj = generalFunc.getJsonObject(cusine_array, i);
                            map.put("cuisineId", generalFunc.getJsonValueStr("cuisineId", cusine_Obj));
                            map.put("cuisineName", generalFunc.getJsonValueStr("cuisineName", cusine_Obj));
                            map.put("TotalRestaurant", generalFunc.getJsonValueStr("TotalRestaurant", cusine_Obj));
                            map.put("TotalRestaurantWithLabel", generalFunc.getJsonValueStr("TotalRestaurantWithLabel", cusine_Obj));
                            cuisineList.add(map);
                        }

                        if (restaurant_Arr != null && restaurant_Arr.length() > 0) {
                            restaurantsArea.setVisibility(View.VISIBLE);
                        } else {
                            restaurantsArea.setVisibility(View.GONE);
                        }

                        if (cuisineList != null && cuisineList.size() > 0) {
                            cusineArea.setVisibility(View.VISIBLE);
                        } else {
                            cusineArea.setVisibility(View.GONE);
                        }
                        if (restaurant_Arr.length() == 0 && cuisineList.size() == 0) {
                            noSearchImage.setVisibility(View.VISIBLE);
                        } else {
                            noSearchImage.setVisibility(View.GONE);
                        }


                        addCusineView();

                        restaurantAdapter.notifyDataSetChanged();

                    } else {
                        if (restaurantArr_List != null && restaurantArr_List.size() == 0) {
                            norecordTxt.setVisibility(View.VISIBLE);
                            restaurantsArea.setVisibility(View.GONE);
                            cusineArea.setVisibility(View.GONE);
                            noSearchImage.setVisibility(View.VISIBLE);

                        }
                        restaurantArr_List.clear();
                        restaurantAdapter.notifyDataSetChanged();
                    }
                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    public void addCusineView() {

        if (cuisineList != null && cuisineList.size() > 0) {

            for (int i = 0; i < cuisineList.size(); i++) {
                int pos = i;
                HashMap<String, String> posData = cuisineList.get(i);

                LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_cusines, null);
                MTextView cusineName = (MTextView) view.findViewById(R.id.cusineName);
                MTextView totalRest = (MTextView) view.findViewById(R.id.totalRest);
                LinearLayout rowArea = (LinearLayout) view.findViewById(R.id.rowArea);
                cusineName.setText(posData.get("cuisineName"));
                totalRest.setText(posData.get("TotalRestaurantWithLabel"));
                rowArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bn = new Bundle();
                        bn.putString("cid", cuisineList.get(pos).get("cuisineId"));
                        bn.putString("cname", cuisineList.get(pos).get("cuisineName"));
                        bn.putDouble("lat", latitude);
                        bn.putDouble("long", longitude);
                        new StartActProcess(getActContext()).startActWithData(SearchRestaurantListActivity.class, bn);
                    }
                });

                cusinecontainArea.addView(view);
            }
        } else {
            if (cusinecontainArea.getChildCount() > 0) {
                cusinecontainArea.removeAllViewsInLayout();
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


          if (requestCode == 111 && resultCode == RESULT_OK) {
              isFavChange=data.getBooleanExtra("isFavChange",false);
              getRestaurantList(searchTxtView.getText().toString().trim());

        }
    }
}
