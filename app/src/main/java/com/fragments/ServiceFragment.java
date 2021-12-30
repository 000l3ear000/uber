package com.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adapter.files.CategoryListItem;
import com.adapter.files.PinnedCategorySectionListAdapter;
import com.melevicarbrasil.usuario.MoreInfoActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.UberxCartActivity;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.realmModel.CarWashCartData;
import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.pinnedListView.PinnedSectionListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class ServiceFragment extends Fragment implements PinnedCategorySectionListAdapter.ServiceClick {

    View view;
    GeneralFunctions generalFunc;
    MoreInfoActivity moreInfoAct;
    PinnedSectionListView service_list;
    PinnedCategorySectionListAdapter pinnedSectionListAdapter;

    ArrayList<CategoryListItem> categoryitems_list;

    ProgressBar loadingBar;

    int hourCnt = 0, regCnt = 0, fixCnt = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_services, container, false);


        moreInfoAct = (MoreInfoActivity) getActivity();
        generalFunc = moreInfoAct.generalFunc;
        service_list = (PinnedSectionListView) view.findViewById(R.id.service_list);
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);

        service_list.setShadowVisible(true);

        service_list.setFastScrollEnabled(false);
        service_list.setFastScrollAlwaysVisible(false);

        categoryitems_list = new ArrayList<>();

        if (getArguments() != null) {
            getServiceInfo();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (categoryitems_list.size() > 0 && pinnedSectionListAdapter != null) {
            markSelectedItems();
        }

    }

    private void markSelectedItems() {
        for (int i = 0; i < categoryitems_list.size(); i++) {
            CategoryListItem tmpItem = categoryitems_list.get(i);
            if (tmpItem.type == CategoryListItem.ITEM) {
                tmpItem.setAdd(checkSameRecordExist(MyApp.getRealmInstance(), tmpItem.getiVehicleTypeId()));
            }
        }

        pinnedSectionListAdapter.notifyDataSetChanged();
    }

    private Context getActContext() {
        return moreInfoAct.getActContext();
    }


    public void getServiceInfo() {
        loadingBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDriverServiceCategories");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iDriverId", moreInfoAct.getIntent().getStringExtra("iDriverId"));
        parameters.put("SelectedCabType", Utils.CabGeneralType_UberX);
        parameters.put("parentId", getArguments().getString("parentId") != null ? getArguments().getString("parentId") : "");
        parameters.put("SelectedVehicleTypeId", getArguments().getString("SelectedVehicleTypeId") != null ? getArguments().getString("SelectedVehicleTypeId") : "");
        parameters.put("vSelectedLatitude", moreInfoAct.getIntent().getStringExtra("latitude"));
        parameters.put("vSelectedLongitude", moreInfoAct.getIntent().getStringExtra("longitude"));
        parameters.put("vSelectedAddress", moreInfoAct.getIntent().getStringExtra("address"));

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), false, generalFunc);

        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {

                    categoryitems_list.clear();

                    JSONArray mainListArr = generalFunc.getJsonArray(Utils.message_str, responseString);
                    CategoryListItem[] sections = null;

                    int sectionPosition = 0, listPosition = 0;
                    int lastItemPosition = 0;

                    if (pinnedSectionListAdapter != null) {
                        sectionPosition = pinnedSectionListAdapter.getSections().length - 1;
                        listPosition = pinnedSectionListAdapter.getSections().length - 1;

                        sections = new CategoryListItem[pinnedSectionListAdapter.getSections().length + mainListArr.length()];

                        lastItemPosition = pinnedSectionListAdapter.getSections().length - 1;

                        for (int i = 0; i < pinnedSectionListAdapter.getSections().length; i++) {
                            sections[i] = pinnedSectionListAdapter.getSections()[i];
                        }

                    } else {
                        sections = new CategoryListItem[mainListArr.length()];
                    }

                    Realm realm = MyApp.getRealmInstance();

                    for (int i = 0; i < mainListArr.length(); i++) {
                        JSONObject tempJson = generalFunc.getJsonObject(mainListArr, i);

                        String vCategory = generalFunc.getJsonValueStr("vCategory", tempJson);

                        CategoryListItem section = new CategoryListItem(CategoryListItem.SECTION, vCategory);
                        section.sectionPosition = sectionPosition;
                        section.listPosition = listPosition++;
                        section.CountSubItems = GeneralFunctions.parseIntegerValue(0, vCategory);

                        sections[sectionPosition] = section;

                        categoryitems_list.add(section);

                        JSONArray subListArr = generalFunc.getJsonArray("SubCategories", tempJson);

                        for (int j = 0; j < subListArr.length(); j++) {
                            JSONObject subTempJson = generalFunc.getJsonObject(subListArr, j);

                            CategoryListItem categoryListItem = new CategoryListItem(CategoryListItem.ITEM, generalFunc.getJsonValueStr("vCategory", tempJson));
                            categoryListItem.sectionPosition = sectionPosition;
                            categoryListItem.listPosition = listPosition++;
                            categoryListItem.setvTitle(generalFunc.getJsonValueStr("vVehicleType", subTempJson));
                            categoryListItem.setiVehicleCategoryId(generalFunc.getJsonValueStr("iVehicleCategoryId", subTempJson));
                            categoryListItem.setvDesc(generalFunc.getJsonValueStr("vCategoryDesc", subTempJson));
                            categoryListItem.seteFareType(generalFunc.getJsonValueStr("eFareType", subTempJson));
                            categoryListItem.setfFixedFare(generalFunc.getJsonValueStr("fFixedFare", subTempJson));
                            categoryListItem.setfPricePerHour(generalFunc.getJsonValueStr("fPricePerHour", subTempJson));
                            categoryListItem.setfMinHour(generalFunc.getJsonValueStr("fMinHour", subTempJson));
                            categoryListItem.setiVehicleTypeId(generalFunc.getJsonValueStr("iVehicleTypeId", subTempJson));
                            if (checkSameRecordExist(realm, generalFunc.getJsonValueStr("iVehicleTypeId", subTempJson))) {
                                categoryListItem.setAdd(true);
                            } else {
                                categoryListItem.setAdd(false);
                            }
                            categoryitems_list.add(categoryListItem);
                        }

                        sectionPosition++;
                    }

                    pinnedSectionListAdapter = new PinnedCategorySectionListAdapter(getActContext(), categoryitems_list, sections);
                    service_list.setAdapter(pinnedSectionListAdapter);
                    pinnedSectionListAdapter.setserviceClickListener(this);

                    if(!moreInfoAct.isFinishing()) {
                        moreInfoAct.onResumeCall();
                    }
                }
            } else {
                generalFunc.showError(true);
            }

            loadingBar.setVisibility(View.GONE);

        });
        exeWebServer.execute();
    }

    public RealmResults<CarWashCartData> getHourData() {
        try {
            hourCnt = 0;
            regCnt = 0;
            fixCnt = 0;
            Realm realm = MyApp.getRealmInstance();
            RealmResults<CarWashCartData> data = realm.where(CarWashCartData.class).findAll();

            for (int i = 0; i < data.size(); i++) {
                CategoryListItem categoryListItem = data.get(i).getCategoryListItem();
                if (categoryListItem.geteFareType().equals("Hourly")) {
                    hourCnt = hourCnt + 1;
                } else if (categoryListItem.geteFareType().equals("Regular")) {
                    regCnt = regCnt + 1;
                } else {
                    fixCnt = fixCnt + 1;
                }

            }
        } catch (Exception e) {
            Logger.d("RealmException", "::" + e.toString());
        }

        return null;
    }


    public CarWashCartData checkSameRecordExist(Realm realm, CategoryListItem serviceListItem) {
        CarWashCartData cart = null;

        RealmResults<CarWashCartData> cartlist = realm.where(CarWashCartData.class).findAll();

        if (cartlist != null && cartlist.size() > 0)

            for (int i = 0; i < cartlist.size(); i++) {

                if (serviceListItem.getiVehicleTypeId().equalsIgnoreCase(cartlist.get(i).getCategoryListItem().getiVehicleTypeId())) {
                    return cartlist.get(i);
                }
            }


        return cart;
    }

    public boolean checkSameRecordExist(Realm realm, String iVehicleTypeId) {
        RealmResults<CarWashCartData> cartlist = realm.where(CarWashCartData.class).findAll();

        if (cartlist != null && cartlist.size() > 0)

            for (int i = 0; i < cartlist.size(); i++) {

                if (iVehicleTypeId.equalsIgnoreCase(cartlist.get(i).getCategoryListItem().getiVehicleTypeId())) {
                    return true;
                }
            }
        return false;
    }

    @Override
    public void serviceClickList(CategoryListItem serviceListItem) {
        getHourData();
        Realm realm = MyApp.getRealmInstance();

        CarWashCartData carWashCartData = checkSameRecordExist(realm, serviceListItem);

        if ((serviceListItem.geteFareType().equals("Hourly") && fixCnt >= 1) || (serviceListItem.geteFareType().equals("Regular") && fixCnt >= 1)) {
            generalFunc.showMessage(view, generalFunc.retrieveLangLBl("", "LBL_RESTRICT_FIXED_SERVICE"));

        } else if ((serviceListItem.geteFareType().equals("Hourly") && hourCnt > 1) || (serviceListItem.geteFareType().equals("Fixed") && hourCnt == 1)
                || ((serviceListItem.geteFareType().equals("Hourly") && hourCnt >= 1 && carWashCartData == null)) || (serviceListItem.geteFareType().equals("Regular") && hourCnt >= 1)) {
            generalFunc.showMessage(view, generalFunc.retrieveLangLBl("", "LBL_RESTRICT_HOURLY_SERVICE"));

        } else if ((serviceListItem.geteFareType().equals("Regular") && regCnt > 1) || (serviceListItem.geteFareType().equals("Fixed") && regCnt >= 1) || (serviceListItem.geteFareType().equals("Hourly") && regCnt >= 1) || (serviceListItem.geteFareType().equals("Regular") && regCnt >= 1 && carWashCartData == null)) {
            generalFunc.showMessage(view, generalFunc.retrieveLangLBl("", "LBL_RESTRICT_REGULAR_SERVICE"));

        } else {
            Bundle bn = new Bundle();
            bn.putSerializable("data", (Serializable) serviceListItem);
            bn.putString("iDriverId", moreInfoAct.getIntent().getStringExtra("iDriverId"));
            new StartActProcess(getActContext()).startActWithData(UberxCartActivity.class, bn);
        }
    }

    @Override
    public void serviceRemoveClickList(CategoryListItem serviceListItem) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getContext());
        generateAlert.setCancelable(false);

        generateAlert.setBtnClickList(btn_id -> {
            if (btn_id == 0) {
                generateAlert.closeAlertBox();
            } else {
                Realm realm = MyApp.getRealmInstance();
                realm.beginTransaction();
                CarWashCartData carWashCartData = checkSameRecordExist(realm, serviceListItem);
                carWashCartData.deleteFromRealm();
                realm.commitTransaction();
                onResume();

                moreInfoAct.onResumeCall();
            }
        });

        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_REMOVE_SERVICE_NOTE"));
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
        generateAlert.showAlertBox();
    }
}
