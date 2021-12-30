package com.melevicarbrasil.usuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.adapter.files.AddressListAdapter;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAddressActivity extends AppCompatActivity implements AddressListAdapter.ItemClickListener {


    GeneralFunctions generalFunc;
    ImageView backImgView;


    ProgressBar addressListPageLoader;
    MTextView titleTxt;
    MTextView noAddrTxt;
    MTextView chooseAddrTxtView;
    RecyclerView AddrListRecyclerView;
    AddressListAdapter addressListAdapterobj;
    LinearLayout addDeliveryArea;

    ImageView rightImgView;

    ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();
    String type = "";
    String SelectedVehicleTypeId = "";
    String quantity = "0";
    boolean isDeleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_address);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        type = getIntent().getStringExtra("type");
        SelectedVehicleTypeId = getIntent().getStringExtra("SelectedVehicleTypeId");
        quantity = getIntent().getStringExtra("Quantity");


        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        noAddrTxt = (MTextView) findViewById(R.id.noAddrTxt);
        chooseAddrTxtView = (MTextView) findViewById(R.id.chooseAddrTxtView);
        addDeliveryArea = (LinearLayout) findViewById(R.id.addDeliveryArea);
        addressListPageLoader = (ProgressBar) findViewById(R.id.addressListPageLoader);
        AddrListRecyclerView = (RecyclerView) findViewById(R.id.AddrListRecyclerView);
        rightImgView = (ImageView) findViewById(R.id.rightImgView);

        addressListAdapterobj = new AddressListAdapter(getActContext(), addrList, generalFunc);
        AddrListRecyclerView.setAdapter(addressListAdapterobj);

        addressListAdapterobj.onClickListener(this);

        backImgView.setOnClickListener(new setOnClick());
        addDeliveryArea.setOnClickListener(new setOnClick());
        rightImgView.setOnClickListener(new setOnClick());

        rightImgView.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra("iCompanyId")) {
            rightImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_circle));
        }
        setLabel();
    }

    private void setLabel() {
        titleTxt.setText(generalFunc.retrieveLangLBl("Select Address", "LBL_SELECT_ADDRESS_TITLE_TXT"));
        chooseAddrTxtView.setText(generalFunc.retrieveLangLBl("Choose Address", "LBL_CHOOSE_ADDRESS_HINT_INFO"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddrDetail();
    }

    private void getAddrDetail() {
        addressListPageLoader.setVisibility(View.VISIBLE);
        noAddrTxt.setVisibility(View.GONE);
        addrList.clear();
        addressListAdapterobj.notifyDataSetChanged();


        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DisplayUserAddress");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eUserType", Utils.app_type);

        if (getIntent().hasExtra("iDriverId")) {
            parameters.put("iDriverId", getIntent().getStringExtra("iDriverId"));
        }

        if (getIntent().hasExtra("eSystem")) {
            parameters.put("eSystem", getIntent().getStringExtra("eSystem"));
        }

        if (getIntent().hasExtra("iCompanyId")) {
            if (!getIntent().getStringExtra("iCompanyId").equalsIgnoreCase("-1")) {
                HashMap<String, String> data = new HashMap<>();
                data.put(Utils.SELECT_LATITUDE, "");
                data.put(Utils.SELECT_LONGITUDE, "");
                data = generalFunc.retrieveValue(data);


                parameters.put("PassengerLat", data.get(Utils.SELECT_LATITUDE));
                parameters.put("PassengerLon", data.get(Utils.SELECT_LONGITUDE));
                parameters.put("iCompanyId", getIntent().getStringExtra("iCompanyId"));
            }
        }


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            noAddrTxt.setVisibility(View.GONE);
            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    JSONArray message_arr = generalFunc.getJsonArray("message", responseString);

                    if (message_arr != null && message_arr.length() > 0) {
                        addrList.clear();

                        String iSelectedUserAddressId = "";

                        String iUserAddressId = getIntent().getStringExtra("iUserAddressId");
                        String addressid = getIntent().getStringExtra("addressid");

                        if (iUserAddressId != null && !iUserAddressId.equalsIgnoreCase("") && !iUserAddressId.equalsIgnoreCase("0") && !iUserAddressId.equalsIgnoreCase("-1")) {
                            iSelectedUserAddressId = iUserAddressId;
                        } else if (addressid != null && !addressid.equalsIgnoreCase("") && !addressid.equalsIgnoreCase("0") && !addressid.equalsIgnoreCase("-1")) {
                            iSelectedUserAddressId = addressid;
                        }

                        for (int i = 0; i < message_arr.length(); i++) {
                            JSONObject addr_obj = generalFunc.getJsonObject(message_arr, i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("vServiceAddress", generalFunc.getJsonValueStr("vServiceAddress", addr_obj));
                            map.put("iUserAddressId", generalFunc.getJsonValueStr("iUserAddressId", addr_obj));
                            map.put("vBuildingNo", generalFunc.getJsonValueStr("vBuildingNo", addr_obj));
                            map.put("vLandmark", generalFunc.getJsonValueStr("vLandmark", addr_obj));
                            map.put("vAddressType", generalFunc.getJsonValueStr("vAddressType", addr_obj));
                            map.put("vLatitude", generalFunc.getJsonValueStr("vLatitude", addr_obj));
                            map.put("vLongitude", generalFunc.getJsonValueStr("vLongitude", addr_obj));
                            map.put("eStatus", generalFunc.getJsonValueStr("eStatus", addr_obj));
                            map.put("eLocationAvailable", generalFunc.getJsonValueStr("eLocationAvailable", addr_obj));


                            if (iSelectedUserAddressId.equalsIgnoreCase(map.get("iUserAddressId"))) {
                                map.put("isSelected", "true");
                            } else {
                                map.put("isSelected", "false");
                            }

                            map.put("LBL_SERVICE_NOT_AVAIL_ADDRESS_RESTRICT", generalFunc.retrieveLangLBl("", "LBL_SERVICE_NOT_AVAIL_ADDRESS_RESTRICT"));
                            addrList.add(map);
                        }
                        addressListAdapterobj.notifyDataSetChanged();
                        addressListPageLoader.setVisibility(View.GONE);

                    }
                } else {

                    if (getIntent().hasExtra("iCompanyId")) {
                        addrList.clear();
                        if (addrList.size() == 0) {
                            noAddrTxt.setVisibility(View.VISIBLE);
                            noAddrTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_USER_ADDRESS_FOUND"));
                            addressListPageLoader.setVisibility(View.GONE);
                        }
                        // finish();
                    } else {
                        if (addrList.size() == 0) {
                            noAddrTxt.setVisibility(View.VISIBLE);
                            noAddrTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_ADDRESS_TXT"));
                            addressListPageLoader.setVisibility(View.GONE);
                        }
                        finish();
                    }

                }
            } else {


            }
        });
        exeWebServer.execute();
    }

    public void Checkuseraddressrestriction(String iAddressId, String selectedVehicleTypeId, final String type, final int position) {
        final Bundle bundle = new Bundle();

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "Checkuseraddressrestriction");
        parameters.put("iUserAddressId", iAddressId);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("iSelectVehicalId", selectedVehicleTypeId);
        parameters.put("eUserType", Utils.app_type);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    HashMap<String, String> data = addrList.get(position);
                    if (type.equalsIgnoreCase(Utils.CabReqType_Later)) {
                        bundle.putString("latitude", data.get("vLatitude"));
                        bundle.putString("longitude", data.get("vLongitude"));
                        bundle.putString("address", data.get("vServiceAddress"));
                        bundle.putString("iUserAddressId", data.get("iUserAddressId"));
                        bundle.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                        bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));
                        bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
                        String vAddressType = data.get("vAddressType");
                        if (vAddressType != null && !vAddressType.equals("")) {
                            String tempadd = vAddressType + "\n" + data.get("vBuildingNo") + ", " + data.get("vLandmark") + "\n" + data.get("vServiceAddress");
                            bundle.putString("address", tempadd);
                        } else {
                            String tempadd = data.get("vBuildingNo") + ", " + data.get("vLandmark") + "\n" + data.get("vServiceAddress");
                            bundle.putString("address", tempadd);
                        }
                        bundle.putString("Quantity", quantity);
                        bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
                        bundle.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                        bundle.putBoolean("isWalletShow", getIntent().getBooleanExtra("isWalletShow", false));
                        new StartActProcess(getActContext()).startActWithData(ScheduleDateSelectActivity.class, bundle);

                    } else {
                        bundle.putBoolean("isufx", true);
                        bundle.putString("latitude", data.get("vLatitude"));
                        bundle.putString("longitude", data.get("vLongitude"));
                        bundle.putString("address", data.get("vServiceAddress"));
                        bundle.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                        bundle.putString("iUserAddressId", data.get("iUserAddressId"));
                        bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));
                        bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
                        bundle.putString("Quantity", quantity);
                        bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
                        bundle.putString("type", Utils.CabReqType_Now);
                        bundle.putString("Sdate", "");
                        bundle.putString("Stime", "");
                        bundle.putBoolean("isWalletShow", getIntent().getBooleanExtra("isWalletShow", false));
                        new StartActProcess(getActContext()).startActWithData(MainActivity.class, bundle);
                    }
                } else {

                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {
                        generateAlert.closeAlertBox();
                        finish();
                    });
                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));

                    generateAlert.showAlertBox();
                }
            }
        });
        exeWebServer.execute();


    }

    public void removeAddressApi(String iAddressId) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "DeleteUserAddressDetail");
        parameters.put("iUserAddressId", iAddressId);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eUserType", Utils.app_type);
        if (getIntent().hasExtra("iCompanyId")) {

            HashMap<String, String> data = new HashMap<>();
            data.put(Utils.SELECT_LATITUDE, "");
            data.put(Utils.SELECT_LONGITUDE, "");
            data = generalFunc.retrieveValue(data);

            parameters.put("PassengerLat", data.get(Utils.SELECT_LATITUDE));
            parameters.put("PassengerLon", data.get(Utils.SELECT_LONGITUDE));
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));

                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {
                        generateAlert.closeAlertBox();

                        if (getIntent().hasExtra("iCompanyId") && !getIntent().getStringExtra("iCompanyId").equalsIgnoreCase("-1")) {
                            String addressid = getIntent().getStringExtra("addressid");
                            if (addressid != null) {
                                if (addressid.equalsIgnoreCase(iAddressId)) {
                                    isDeleted = true;
                                }
                            }

                            String ToTalAddress = getIntent().hasExtra("ToTalAddress") ? getIntent().getStringExtra("ToTalAddress") : generalFunc.getJsonValue("ToTalAddress", responseString);
                            if ((ToTalAddress != null && ToTalAddress.equalsIgnoreCase("0"))) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("ToTalAddress", ToTalAddress);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            } else {
                                getAddrDetail();
                            }
                        } else {

                            String userprofileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                            if (generalFunc.getJsonValue("ToTalAddress", userprofileJson).equalsIgnoreCase("0")) {
                                finish();
                            } else {
                                getAddrDetail();
                            }


                        }
                    });
                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));

                    generateAlert.showAlertBox();


                } else {

                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));

                }
            } else {

            }
        });
        exeWebServer.execute();
    }

    public Context getActContext() {
        return ListAddressActivity.this;
    }

    @Override
    public void setOnClick(int position) {
        HashMap<String, String> data = addrList.get(position);

        if (getIntent().hasExtra("iCompanyId")) {
            Intent returnIntent = new Intent();

            String address = "";
            String vAddressType = data.get("vAddressType");

            if (vAddressType != null && !vAddressType.equalsIgnoreCase("")) {
                address = vAddressType + "," + data.get("vBuildingNo") + ", " + data.get("vLandmark") + ", " + data.get("vServiceAddress");
            } else {
                address = data.get("vBuildingNo") + ", " + data.get("vLandmark") + ", " + data.get("vServiceAddress");
            }

            returnIntent.putExtra("address", address);
            returnIntent.putExtra("addressId", data.get("iUserAddressId"));
            returnIntent.putExtra("vLatitude", data.get("vLatitude"));
            returnIntent.putExtra("vLongitude", data.get("vLongitude"));
            returnIntent.putExtra("ToTalAddress", "1");
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            if (type.equals(Utils.CabReqType_Later)) {
                Checkuseraddressrestriction(data.get("iUserAddressId"), SelectedVehicleTypeId, Utils.CabReqType_Later, position);
            } else {
                Checkuseraddressrestriction(data.get("iUserAddressId"), SelectedVehicleTypeId, Utils.CabReqType_Now, position);
            }
        }
    }

    @Override
    public void setOnDeleteClick(final int position) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            generateAlert.closeAlertBox();
            if (btn_id == 1) {
                removeAddressApi(addrList.get(position).get("iUserAddressId"));
            } else {
            }
        });
        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Are you sure want to delete", "LBL_DELETE_CONFIRM_MSG"));
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("cANCEL", "LBL_CANCEL_TXT"));
        generateAlert.showAlertBox();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("iCompanyId")) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("isDeleted", isDeleted);
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public class setOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.backImgView) {
                ListAddressActivity.super.onBackPressed();
            } else if (i == R.id.addDeliveryArea) {

                Bundle bundle = new Bundle();
                bundle.putBoolean("isufx", true);
                bundle.putString("latitude", getIntent().getStringExtra("latitude"));
                bundle.putString("longitude", getIntent().getStringExtra("longitude"));
                bundle.putString("address", getIntent().getStringExtra("address"));
                bundle.putString("type", type);

                bundle.putString("Quantity", quantity);
                bundle.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
                if (getIntent().hasExtra("iCompanyId")) {
                    bundle.putString("iCompanyId", getIntent().getStringExtra("iCompanyId"));

                }
                bundle.putBoolean("isWalletShow", getIntent().getBooleanExtra("isWalletShow", false));
                new StartActProcess(getActContext()).startActWithData(AddAddressActivity.class, bundle);
            } else if (i == R.id.rightImgView) {
                addDeliveryArea.performClick();
            }
        }
    }
}
