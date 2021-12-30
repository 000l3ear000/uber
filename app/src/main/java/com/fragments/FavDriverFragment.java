package com.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adapter.files.FavoriteDriverAdapter;
import com.melevicarbrasil.usuario.FavouriteDriverActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.like.LikeButton;
import com.utils.Logger;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavDriverFragment extends Fragment implements FavoriteDriverAdapter.OnItemClickListener {


    View view;

    ProgressBar loading_fav_driver;
    MTextView noFavDriversTxt;

    RecyclerView favDriversRecyclerView;
    ErrorView errorView;

    FavoriteDriverAdapter favoriteDriverAdapter;

    ArrayList<HashMap<String, String>> list;

    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;

    String next_page_str = "";
    String APP_TYPE = "";

    GeneralFunctions generalFunc;

    FavouriteDriverActivity favouriteDriverAct;
    ArrayList<HashMap<String, String>> filterlist;
    FavDriverFragment favDriverFragment;
    String previousHeadereType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fav_driver, container, false);

        loading_fav_driver = (ProgressBar) view.findViewById(R.id.loading_fav_driver);
        noFavDriversTxt = (MTextView) view.findViewById(R.id.noFavDriversTxt);
        favDriversRecyclerView = (RecyclerView) view.findViewById(R.id.favDriversRecyclerView);
        errorView = (ErrorView) view.findViewById(R.id.errorView);

        favouriteDriverAct = (FavouriteDriverActivity) getActivity();
        generalFunc = favouriteDriverAct.generalFunc;

        APP_TYPE = generalFunc.getJsonValue("APP_TYPE", generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

        list = new ArrayList<>();
        filterlist = new ArrayList<>();
        favoriteDriverAdapter = new FavoriteDriverAdapter(getActContext(), list, generalFunc, false);
        favDriversRecyclerView.setAdapter(favoriteDriverAdapter);
        favoriteDriverAdapter.setOnItemClickListener(this);
        favDriverFragment = favouriteDriverAct.getFavDriverFrag();


        favDriversRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                int lastInScreen = firstVisibleItemPosition + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable) {

                    mIsLoading = true;
                    favoriteDriverAdapter.addFooterView();

                    getFavDriverList(true);

                } else if (!isNextPageAvailable) {
                    favoriteDriverAdapter.removeFooterView();
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getFavDriverList(false);

    }


    public void onItemClickList(View v, int position) {
        Utils.hideKeyboard(getActContext());

        setDriverAsfav(position, v);

    }


    public void setDriverAsfav(int pos, View view) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "addDriverInFavList");
        parameters.put("iDriverId", list.get(pos).get("iDriverId"));
        parameters.put("eFavDriver", (view instanceof LikeButton && ((LikeButton) view).isLiked()) ? "Yes" : "No");
        parameters.put("eType", list.get(pos).get("eTypeOrig"));
        parameters.put("iUserId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseObj=generalFunc.getJsonObject(responseString);
            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                if (isDataAvail) {
                    favoriteDriverAdapter.notifyDataSetChanged();
                    getFavDriverList(false);


                } else {
                    favoriteDriverAdapter.notifyDataSetChanged();
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseObj)));
                }


            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public void getFavDriverList(final boolean isLoadMore) {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading_fav_driver.getVisibility() != View.VISIBLE && !isLoadMore) {
            loading_fav_driver.setVisibility(View.VISIBLE);
        }

        if (!isLoadMore) {
            removeNextPageConfig();
            list.clear();
            favoriteDriverAdapter.notifyDataSetChanged();
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getFavDriverList");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("contentType", getArguments().getString("TAB_TYPE"));
        parameters.put("vFilterParam", favouriteDriverAct.selFilterType);
        if (isLoadMore) {
            parameters.put("page", next_page_str);
        }

        noFavDriversTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noFavDriversTxt.setVisibility(View.GONE);

            if (responseString != null && !responseString.equals("")) {
                Logger.d("Fav_Driver", "inside responseString::");

                closeLoader();

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {

                    String nextPage = generalFunc.getJsonValue("NextPage", responseString);
                    String eFilterSel = generalFunc.getJsonValue("eFilterSel", responseString);
                    JSONArray arr_fav_drivers = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_fav_drivers != null && arr_fav_drivers.length() > 0) {
                        Logger.d("Fav_Driver", "inside arr_fav_drivers::"+arr_fav_drivers.length());

                        for (int i = 0; i < arr_fav_drivers.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_fav_drivers, i);

                            String eType = generalFunc.getJsonValueStr("eType", obj_temp);

                          /*  if (!previousHeadereType.equalsIgnoreCase(eType)) {

                                HashMap<String, String> mapHeader = new HashMap<String, String>();
                                mapHeader.put("eType", eType);
                                mapHeader.put("TYPE", "" + favoriteDriverAdapter.TYPE_HEADER);
                                list.add(mapHeader);
                                previousHeadereType = eType;
                            }*/


                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("appType", APP_TYPE);
                            map.put("eTypeOrig", eType);



                            if (generalFunc.getJsonValue("APP_TYPE", favouriteDriverAct.userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) ||
                                    generalFunc.getJsonValue("APP_TYPE", favouriteDriverAct.userProfileJson).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
                                if (eType.equalsIgnoreCase("deliver") || eType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                                    map.put("eType", generalFunc.retrieveLangLBl("", "LBL_DELIVERY"));
                                } else if (eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                                    map.put("eType", generalFunc.retrieveLangLBl("", "LBL_RIDE"));
                                } else {
                                    map.put("eType", generalFunc.retrieveLangLBl("", "LBL_SERVICES"));
                                }
                            }

                            if (generalFunc.getJsonValueStr("eFly", obj_temp).equalsIgnoreCase("Yes"))
                            {
                                map.put("eType", generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_FLY_RIDE"));
                            }

                            map.put("iDriverId", generalFunc.getJsonValueStr("iDriverId", obj_temp));
                            map.put("vRating", generalFunc.getJsonValueStr("vAvgRating", obj_temp));
                            map.put("vName", generalFunc.getJsonValueStr("vName", obj_temp));
                            map.put("vImage", generalFunc.getJsonValueStr("vImage", obj_temp));
                            map.put("eFavDriver", generalFunc.getJsonValueStr("eFavDriver", obj_temp));
                            map.put("vService_BG_color", generalFunc.getJsonValueStr("vService_BG_color", obj_temp));
                            map.put("vService_TEXT_color", generalFunc.getJsonValueStr("vService_TEXT_color", obj_temp));
                            map.put("JSON", obj_temp.toString());
                            list.add(map);

                        }


                    }

                    JSONArray arr_type_filter = generalFunc.getJsonArray("AppTypeFilterArr", responseString);

                    if (arr_type_filter != null && arr_type_filter.length() > 0) {
                        filterlist = new ArrayList<>();
                        for (int i = 0; i < arr_type_filter.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_type_filter, i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
                            map.put("vFilterParam", generalFunc.getJsonValueStr("vFilterParam", obj_temp));
                            filterlist.add(map);
                            if (eFilterSel.equalsIgnoreCase(generalFunc.getJsonValueStr("vTitle", obj_temp))){
                                favouriteDriverAct.filterPosition=i;
                            }

                        }
                        favouriteDriverAct.filterManage(filterlist);
                    }


                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }
                    Logger.d("Fav_Driver", "list Data::"+list.size());
                    favoriteDriverAdapter.notifyDataSetChanged();

                }else {
                    Logger.d("Fav_Driver", "responseString blank::"+responseString);

                    if (list.size() == 0) {
                        noFavDriversTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        noFavDriversTxt.setVisibility(View.VISIBLE);
                    }
                    removeNextPageConfig();
                }

            } else {
                Logger.d("Fav_Driver", "removed pagination::");

                if (isLoadMore == false) {
                    removeNextPageConfig();
                    generateErrorView();
                }

            }

            mIsLoading = false;
        });
        exeWebServer.execute();
    }

    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        favoriteDriverAdapter.removeFooterView();
    }

    public void closeLoader() {
        if (loading_fav_driver.getVisibility() == View.VISIBLE) {
            loading_fav_driver.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getFavDriverList(false));
    }


    public Context getActContext() {
        return favouriteDriverAct.getActContext();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult", "::called");
    }

}
