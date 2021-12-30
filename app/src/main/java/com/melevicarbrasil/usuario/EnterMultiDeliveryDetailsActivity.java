package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dialogs.OpenListView;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.StartActProcess;
import com.general.files.TextWatcherExtendedListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.Delivery_Data;
import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EnterMultiDeliveryDetailsActivity extends AppCompatActivity implements TextWatcher, TextWatcherExtendedListener, Serializable {
    MTextView titleTxt;
    ImageView backImgView;

    GeneralFunctions generalFunc;

    MaterialEditText receiverNameEditBox;
    MaterialEditText receiverMobileEditBox;
    MaterialEditText pickUpInstructionEditBox;
    MaterialEditText deliveryInstructionEditBox;
    MaterialEditText packageTypeBox;
    MaterialEditText packageDetailsEditBox;

    MTextView btn_type2;
    MTextView btn_reset;

    String required_str = "";

    androidx.appcompat.app.AlertDialog alert_packageTypes;

    ArrayList<String[]> list_packageType_items = new ArrayList<>();
    ArrayList<HashMap<String, String>> packageTypeList = new ArrayList<>();

    String currentPackageTypeId = "";
    // Store Delivery Details
    LinearLayout dynamicArea;
    ArrayList<Delivery_Data> elementArrayList = new ArrayList<Delivery_Data>();
    ArrayList<Object> dataArrayList = new ArrayList<>();
    List<String> enteredTextArray = new ArrayList<>();
    private String userProfileJson;
    MTextView pickUpInstructionTitle,deliveryInstructionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_delivery_detail);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        dynamicArea = (LinearLayout) findViewById(R.id.dynamicArea);

        pickUpInstructionTitle=(MTextView)findViewById(R.id.pickUpInstructionTitle);
        deliveryInstructionTitle=(MTextView)findViewById(R.id.deliveryInstructionTitle);
        receiverNameEditBox = (MaterialEditText) findViewById(R.id.receiverNameEditBox);
        receiverMobileEditBox = (MaterialEditText) findViewById(R.id.receiverMobileEditBox);
        pickUpInstructionEditBox = (MaterialEditText) findViewById(R.id.pickUpInstructionEditBox);
        deliveryInstructionEditBox = (MaterialEditText) findViewById(R.id.deliveryInstructionEditBox);
        packageTypeBox = (MaterialEditText) findViewById(R.id.packageTypeBox);
        packageDetailsEditBox = (MaterialEditText) findViewById(R.id.packageDetailsEditBox);

        receiverNameEditBox.addTextChangedListener((TextWatcher) this);
        receiverMobileEditBox.addTextChangedListener((TextWatcher) this);
        pickUpInstructionEditBox.addTextChangedListener((TextWatcher) this);
        deliveryInstructionEditBox.addTextChangedListener((TextWatcher) this);
        packageTypeBox.addTextChangedListener((TextWatcher) this);
        packageDetailsEditBox.addTextChangedListener((TextWatcher) this);

        btn_type2 = ((MTextView) findViewById(R.id.btn_type2));
        btn_reset = ((MTextView) findViewById(R.id.btn_reset));


        receiverMobileEditBox.setInputType(InputType.TYPE_CLASS_NUMBER);

        receiverNameEditBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        receiverMobileEditBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        pickUpInstructionEditBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        deliveryInstructionEditBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        packageDetailsEditBox.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Utils.removeInput(packageTypeBox);




        pickUpInstructionEditBox.setHideUnderline(true);
        if (generalFunc.isRTLmode()) {
            pickUpInstructionEditBox.setPaddings(0, 0, (int) getResources().getDimension(R.dimen._10sdp), 0);
        } else {
            pickUpInstructionEditBox.setPaddings((int) getResources().getDimension(R.dimen._10sdp), 0, 0, 0);
        }

        pickUpInstructionEditBox.setSingleLine(false);
        pickUpInstructionEditBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        pickUpInstructionEditBox.setGravity(Gravity.TOP);



        deliveryInstructionEditBox.setHideUnderline(true);
        if (generalFunc.isRTLmode()) {
            deliveryInstructionEditBox.setPaddings(0, 0, (int) getResources().getDimension(R.dimen._10sdp),0 );
        } else {
            deliveryInstructionEditBox.setPaddings((int) getResources().getDimension(R.dimen._10sdp), 0, 0, 0);
        }

        deliveryInstructionEditBox.setSingleLine(false);
        deliveryInstructionEditBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        deliveryInstructionEditBox.setGravity(Gravity.TOP);

        backImgView.setOnClickListener(new setOnClickList());
        btn_type2.setOnClickListener(new setOnClickList());
        packageTypeBox.setOnTouchListener(new setOnTouchList());
        packageTypeBox.setOnClickListener(new setOnClickList());

        setLabels();


        getSetDynamicView();

        if (getIntent().hasExtra("isFromMulti")) {
            configureResetButton(false);
        } else {
            handleResetButton();
        }

        configureResetButton(false);

    }

    private void getSetDynamicView() {

        InternetConnection intCheck = new InternetConnection(getActContext());

        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
            generalFunc.showMessage(findViewById(R.id.contentArea), generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
            return;
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDeliveryFormFields");
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                JSONObject responseObj=generalFunc.getJsonObject(responseString);

                if (responseObj != null && !responseObj.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                    if (isDataAvail == true) {

                        findViewById(R.id.insidecontentArea).setVisibility(View.VISIBLE);

                        JSONArray message_arr = generalFunc.getJsonArray("message", responseObj);

                        findViewById(R.id.loaderView).setVisibility(View.GONE);

                        if (message_arr != null && message_arr.length() > 0) {
                            elementArrayList.clear();
                            dataArrayList.clear();
                            enteredTextArray.clear();
                            addDynamicContentLayout(message_arr);

                        }
                    } else {
                        findViewById(R.id.loaderView).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.loaderView).setVisibility(View.VISIBLE);

                }
            }
        });
        exeWebServer.execute();

    }

    private void addDynamicContentLayout(JSONArray jobjArray) {

        if (dynamicArea.getChildCount() > 0) {
            dynamicArea.removeAllViewsInLayout();
        }

        if (jobjArray != null && jobjArray.length() > 0) {
            String LBL_DEST_ADD_TXT=generalFunc.retrieveLangLBl("", "LBL_DEST_ADD_TXT");
            for (int i = 0; i < jobjArray.length(); i++) {
                JSONObject jobject = generalFunc.getJsonObject(jobjArray, i);

                Delivery_Data dt = new Delivery_Data();
                dt.setvFieldName(generalFunc.getJsonValueStr("vFieldName", jobject));
                dt.seteInputType(generalFunc.getJsonValueStr("eInputType", jobject));
                dt.setOptions(generalFunc.getJsonArray("Options", jobject));
                dt.setiDeliveryFieldId(generalFunc.getJsonValueStr("iDeliveryFieldId", jobject));
                dt.settDesc(generalFunc.getJsonValueStr("tDesc", jobject));
                dt.seteAllowFloat(generalFunc.getJsonValueStr("eAllowFloat", jobject));
                dt.seteRequired(generalFunc.getJsonValueStr("eRequired", jobject));
                dt.setItemID(i);
            /*HashMap<Object, Object> map = new HashMap<Object, Object>();
            map.put("vFieldName", generalFunc.getJsonValue("vFieldName", jobject.toString()));
            map.put("eInputType", generalFunc.getJsonValue("eInputType", jobject.toString()));
            map.put("Options", generalFunc.getJsonArray("Options", jobject.toString()));
            map.put("iDeliveryFieldId", generalFunc.getJsonValue("iDeliveryFieldId", jobject.toString()));*/
                elementArrayList.add(dt);
                addDetailRow(i, (jobjArray.length() - 1) == i ? true : false);

            }

            if (!getIntent().hasExtra("isDestAdded")) {
                setStoredDeliveryDetails();
            } else {
                Delivery_Data dt = new Delivery_Data();
                dt.setvFieldName("SelectAddress");
                dt.seteInputType("SelectAddress");
                dt.setOptions(generalFunc.getJsonArray("Options", ""));
                dt.setiDeliveryFieldId(generalFunc.getJsonValue("iDeliveryFieldId", ""));
                dt.settDesc(generalFunc.getJsonValue("tDesc", ""));
                dt.setItemID(elementArrayList.size() - 1);
                dt.seteAllowFloat(generalFunc.getJsonValue("eAllowFloat", ""));
                dt.seteRequired("Yes");
//                dt.setAddressHintTxt(generalFunc.retrieveLangLBl("", "LBL_SELECT_ADDRESS_TITLE_TXT"));
                dt.setAddressHintTxt(LBL_DEST_ADD_TXT);
                elementArrayList.add(dt);
                addDetailRow(elementArrayList.size() - 1, true);
            }

        }

        if (getIntent().hasExtra("isDestAdded")) {
            setStoredDeliveryDetails();
        }
    }

    private void addDetailRow(int pos, boolean b) {

        Delivery_Data posData = elementArrayList.get(pos);
        View convertView;
        LayoutInflater infalInflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.dynamic_layout_android, null);
        LinearLayout singleLineTextArea = (LinearLayout) convertView.findViewById(R.id.singleLineTextArea);
        MaterialEditText textField = (MaterialEditText) convertView.findViewById(R.id.textField);
        FrameLayout selectArea = (FrameLayout) convertView.findViewById(R.id.selectArea);
        MaterialEditText selectBox = (MaterialEditText) convertView.findViewById(R.id.selectBox);
        ImageView arrowImageView = (ImageView) convertView.findViewById(R.id.arrowImageView);
        RelativeLayout multiLineTextArea = (RelativeLayout) convertView.findViewById(R.id.multiLineTextArea);
        MaterialEditText multiLineText = (MaterialEditText) convertView.findViewById(R.id.multiLineText);
        MTextView multiLineTitle = (MTextView) convertView.findViewById(R.id.multiLineTitle);



        multiLineText.setHideUnderline(true);
        if (generalFunc.isRTLmode()) {
            multiLineText.setPaddings(0, 0, (int) getResources().getDimension(R.dimen._10sdp), 0);
        } else {
            multiLineText.setPaddings((int) getResources().getDimension(R.dimen._10sdp), 0, 0, 0);
        }

        multiLineText.setSingleLine(false);
        multiLineText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        multiLineText.setGravity(Gravity.TOP);


        multiLineTextArea.setTag(pos);
        singleLineTextArea.setTag(pos);
        textField.setTag(pos);
        selectArea.setTag(pos);
        selectBox.setTag(pos);
        arrowImageView.setTag(pos);
        multiLineText.setTag(pos);
        textField.addTextChangedListener(this); //Let implement TextWatcherExtendedListener methods

        if (posData.geteInputType().equals("Select")) {
            selectArea.setVisibility(View.VISIBLE);
//            selectBox.setHint(posData.geteInputType() + " " + posData.getvFieldName());
//            selectBox.setHint(posData.getvFieldName());
            selectBox.setBothText(posData.getvFieldName(),posData.getvFieldName());
            Utils.removeInput(selectBox);
            dataArrayList.add(selectBox);
            enteredTextArray.add("");
            selectBox.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP && !view.hasFocus()) {
                    selectBox.performClick();
                }
                return true;
            });

            selectBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int viewId = generalFunc.parseIntegerValue(0, view.getTag().toString());
                    buildPackageTypes(selectBox, elementArrayList.get(viewId).getOptions().toString(), viewId);
                }
            });


        } else if (posData.geteInputType().equals("Number")) {
            singleLineTextArea.setVisibility(View.VISIBLE);
            textField.setBothText(posData.getvFieldName(), posData.gettDesc());
            textField.setInputType(InputType.TYPE_CLASS_NUMBER);
            dataArrayList.add(textField);
            enteredTextArray.add("");
        } else if (posData.geteInputType().equals("Text")) {
            singleLineTextArea.setVisibility(View.VISIBLE);
            textField.setBothText(posData.getvFieldName(), posData.gettDesc());
            if (posData.geteAllowFloat().equalsIgnoreCase("Yes")) {
                textField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            }
            dataArrayList.add(textField);
            enteredTextArray.add("");
        } else if (posData.geteInputType().equals("Textarea")) {

            multiLineTextArea.setVisibility(View.VISIBLE);
            multiLineTitle.setText(posData.getvFieldName());
            multiLineText.setHint( generalFunc.retrieveLangLBl("","LBL_ADD_SUBJECT_HINT_CONTACT_TXT"));
            dataArrayList.add(multiLineText);
            enteredTextArray.add("");
        } else if (posData.geteInputType().equals("SelectAddress")) {
            singleLineTextArea.setVisibility(View.VISIBLE);
            textField.setBothText(posData.getAddressHintTxt(), posData.getAddressHintTxt());

            dataArrayList.add(textField);
            enteredTextArray.add("");
            Utils.removeInput(textField);
            textField.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP && !view.hasFocus()) {
                        int viewId = generalFunc.parseIntegerValue(0, view.getTag().toString());

                        Bundle bn = new Bundle();
                        bn.putInt("pos", viewId);
                        bn.putBoolean("isFromMulti", true);
                        bn.putString("locationArea", "dest");

                        if (elementArrayList.get(viewId).getDestLat() != 0.0 && elementArrayList.get(viewId).getDestLong() != 0.0) {
                            bn.putDouble("lat", elementArrayList.get(viewId).getDestLat());
                            bn.putDouble("long", elementArrayList.get(viewId).getDestLong());
                            bn.putString("address", "" + elementArrayList.get(viewId).getDestAddress());
                        } else if (getIntent().getDoubleExtra("lat", 0.0) != 0.0 && getIntent().getDoubleExtra("long", 0.0) != 0.0) {
                            bn.putDouble("lat", getIntent().getDoubleExtra("lat", 0.0));
                            bn.putDouble("long", getIntent().getDoubleExtra("long", 0.0));
                            bn.putString("address", getIntent().getStringExtra("address"));
                        }

                        new StartActProcess(getActContext()).startActForResult(SearchLocationActivity.class, bn, Utils.ADD_LOC_REQ_CODE);

                    }
                    return true;
                }
            });

            textField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int viewId = generalFunc.parseIntegerValue(0, view.getTag().toString());

                    Bundle bn = new Bundle();
                    bn.putInt("pos", viewId);
                    bn.putBoolean("isFromMulti", true);
                    bn.putString("locationArea", "dest");

                    if (elementArrayList.get(viewId).getDestLat() != 0.0 && elementArrayList.get(viewId).getDestLong() != 0.0) {
                        bn.putDouble("lat", elementArrayList.get(viewId).getDestLat());
                        bn.putDouble("long", elementArrayList.get(viewId).getDestLong());
                        bn.putString("address", "" + elementArrayList.get(viewId).getDestAddress());
                    } else if (getIntent().getDoubleExtra("lat", 0.0) != 0.0 && getIntent().getDoubleExtra("long", 0.0) != 0.0) {
                        bn.putDouble("lat", getIntent().getDoubleExtra("lat", 0.0));
                        bn.putDouble("long", getIntent().getDoubleExtra("long", 0.0));
                        bn.putString("address", getIntent().getStringExtra("address"));
                    }

                    new StartActProcess(getActContext()).startActForResult(SearchLocationActivity.class, bn, Utils.ADD_LOC_REQ_CODE);

                }
            });

        }

        if (convertView != null)
            dynamicArea.addView(convertView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.ADD_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {

            if (data.hasExtra("pos")) {
                Logger.d("Api", "Selected Data" + data.toString());
                int pos = data.getIntExtra("pos", -1);
                if (pos != -1) {
                    Delivery_Data posData = elementArrayList.get(pos);
                    double latt = generalFunc.parseDoubleValue(0.0, data.getStringExtra("Latitude"));
                    double longi = generalFunc.parseDoubleValue(0.0, data.getStringExtra("Longitude"));
                    LatLng latlng = new LatLng(latt, longi);
                    posData.setDestAddress(data.getStringExtra("Address"));
                    posData.setDestLatLong(latlng);
                    posData.setDestLat(latt);
                    posData.setDestLong(longi);
                    ((MaterialEditText) dataArrayList.get(pos)).setText(data.getStringExtra("Address"));
                    enteredTextArray.add(pos, data.getStringExtra("Address"));
                    handleResetButton();
                }
            }
        }
    }

    private void setStoredDeliveryDetails() {
        boolean isAnyValueEntered=false;

        if (getIntent().hasExtra("isDestAdded")) {

            if (getIntent().hasExtra("selectedDetails") && getIntent().hasExtra("isEdit")) {

                Gson gson = new Gson();
                String data1 = getIntent().getStringExtra("selectedDetails");

                ArrayList<Delivery_Data> dataMap = gson.fromJson(data1, new TypeToken<ArrayList<Delivery_Data>>() {
                        }.getType()
                );


                if (dataMap != null && dataMap.size() > 0) {
                    for (int j = 0; j < elementArrayList.size(); j++) {
                        isAnyValueEntered=true;
                        setData(dataMap, j);
                    }
                }

            }
        } else {

            Gson gson = new Gson();
            if (Utils.checkText(generalFunc.retrieveValue(Utils.DELIVERY_ALL_DETAILS_KEY))) {
                String data = generalFunc.retrieveValue(Utils.DELIVERY_ALL_DETAILS_KEY);
                ArrayList<Delivery_Data> lstArrayList = gson.fromJson(data,
                        new TypeToken<List<Delivery_Data>>() {
                        }.getType());

                if (lstArrayList != null && lstArrayList.size() > 0) {
                    for (int j = 0; j < lstArrayList.size(); j++) {
                        isAnyValueEntered=true;
                        setData(lstArrayList.get(j), j);
                    }
                }
            }
        }

        configureResetButton(isAnyValueEntered);

    }

    private void setData(ArrayList<Delivery_Data> data, int pos) {
        for (int i = 0; i < data.size(); i++) {

            Delivery_Data posDats = elementArrayList.get(pos);
            if (posDats.geteInputType().equalsIgnoreCase(data.get(i).geteInputType())) {

                if (data.get(i).geteInputType().equalsIgnoreCase("SelectAddress")) {

                    LatLng latlng = new LatLng(data.get(i).getDestLat(), data.get(i).getDestLong());
                    posDats.setDestAddress(data.get(i).getDestAddress());
                    posDats.setvFieldValue(data.get(i).getDestAddress());
                    ((MaterialEditText) dataArrayList.get(pos)).setText(data.get(i).getDestAddress());
                    posDats.setDestLatLong(latlng);
                    posDats.setDestLat(data.get(i).getDestLat());
                    posDats.setDestLong(data.get(i).getDestLong());
                }


                if (data.get(i).geteInputType().equalsIgnoreCase("Select")) {
                    if (posDats.getiDeliveryFieldId().equalsIgnoreCase(data.get(i).getiDeliveryFieldId())) {
                        posDats.setSelectedOptionID(data.get(i).getSelectedOptionID());
                        posDats.setvFieldValue(data.get(i).getvFieldValue());
                        ((MaterialEditText) dataArrayList.get(pos)).setText(data.get(i).getvFieldValue());
                    }
                } else {

                    if (elementArrayList.get(i).getItemID() == pos) {

                        ((MaterialEditText) dataArrayList.get(pos)).setText(data.get(i).getvFieldValue());
                        posDats.setvFieldValue(data.get(i).getvFieldValue());
                    }

                }

            }

        }
    }

    private void setData(Delivery_Data data, int pos) {

        if (data != null) {
            Delivery_Data posData = elementArrayList.get(pos);

            if (data.geteInputType().equalsIgnoreCase("SelectAddress")) {

                LatLng latlng = new LatLng(data.getDestLat(), data.getDestLong());
                posData.setDestAddress(data.getDestAddress());
                ((MaterialEditText) dataArrayList.get(pos)).setText(data.getDestAddress());
                posData.setvFieldValue(data.getDestAddress());
                posData.setDestLatLong(latlng);
                posData.setDestLat(data.getDestLat());
                posData.setDestLong(data.getDestLong());
            }

            if (data.geteInputType().equalsIgnoreCase("Select")) {
                if (posData.getiDeliveryFieldId().equalsIgnoreCase(data.getiDeliveryFieldId())) {
                    posData.setSelectedOptionID(data.getSelectedOptionID());
                    posData.setvFieldValue(data.getvFieldValue());
                    ((MaterialEditText) dataArrayList.get(pos)).setText(data.getvFieldValue());
                }
            } else {
                if (posData.getItemID() == data.getItemID()) {
                    ((MaterialEditText) dataArrayList.get(pos)).setText(data.getvFieldValue());
                    posData.setvFieldValue(data.getvFieldValue());
                }
            }
        }
    }


    private void setData(JSONObject ja, int pos) {
        Delivery_Data posData = elementArrayList.get(pos);

        if (generalFunc.getJsonValueStr("eInputType", ja).equalsIgnoreCase("Select")) {

            if (posData.getiDeliveryFieldId().equalsIgnoreCase(generalFunc.getJsonValueStr("iDeliveryFieldId", ja))) {
                posData.setSelectedOptionID(generalFunc.getJsonValueStr("selectedOptionID", ja));
                ((MaterialEditText) dataArrayList.get(pos)).setText(generalFunc.getJsonValueStr("vFieldValue", ja));
                posData.setvFieldValue(generalFunc.getJsonValueStr("vFieldValue", ja));
            }
        } else {
            ((MaterialEditText) dataArrayList.get(pos)).setText(generalFunc.getJsonValueStr("vFieldValue", ja));
            posData.setvFieldValue(generalFunc.getJsonValueStr("vFieldValue", ja));

        }

    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("Delivery Details", "LBL_DELIVERY_DETAILS"));

        receiverNameEditBox.setBothText(generalFunc.retrieveLangLBl("Receiver Name", "LBL_RECEIVER_NAME"));
        receiverMobileEditBox.setBothText(generalFunc.retrieveLangLBl("Receiver Mobile", "LBL_RECEIVER_MOBILE"));
        pickUpInstructionEditBox.setHint(generalFunc.retrieveLangLBl("Pickup instruction", "LBL_ADD_SUBJECT_HINT_CONTACT_TXT"));
        pickUpInstructionTitle.setText(generalFunc.retrieveLangLBl("Pickup instruction", "LBL_PICK_UP_INS"));
        deliveryInstructionTitle.setText(generalFunc.retrieveLangLBl("Pickup instruction", "LBL_DELIVERY_INS"));
        deliveryInstructionEditBox.setHint(generalFunc.retrieveLangLBl("Delivery instruction", "LBL_ADD_SUBJECT_HINT_CONTACT_TXT"));
        packageTypeBox.setBothText(generalFunc.retrieveLangLBl("Package Type", "LBL_PACKAGE_TYPE"),
                generalFunc.retrieveLangLBl("Select package type", "LBL_SELECT_PACKAGE_TYPE"));
        packageDetailsEditBox.setBothText(generalFunc.retrieveLangLBl("Package Details", "LBL_PACKAGE_DETAILS"));

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");


        if (getIntent().hasExtra("isDestAdded")) {
            btn_type2.setText(generalFunc.retrieveLangLBl("Save", "LBL_SAVE_ADDRESS_TXT"));
        } else {
            if (getIntent().getStringExtra("isDeliverNow") != null && getIntent().getStringExtra("isDeliverNow").equals("true")) {
                btn_type2.setText(generalFunc.retrieveLangLBl("Deliver Now", "LBL_DELIVER_NOW"));
            } else {
                btn_type2.setText(generalFunc.retrieveLangLBl("Send Request", "LBL_CONFIRM_BOOKING"));
            }
        }
        btn_type2.setText(generalFunc.retrieveLangLBl("Save", "LBL_SUBMIT_TXT"));

        btn_reset.setText(generalFunc.retrieveLangLBl("", "LBL_RESET"));

    }

    public Context getActContext() {
        return EnterMultiDeliveryDetailsActivity.this;
    }

    public void loadPackageTypes() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "loadPackageTypes");

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);

        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail == true) {

                        buildPackageTypes(generalFunc.getJsonValue(Utils.message_str, responseString));

                    } else {
                        generalFunc.showGeneralMessage("",
                                generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }
                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    public void buildPackageTypes(String message) {
        ArrayList<String> items = new ArrayList<>();

        JSONArray arr_data = generalFunc.getJsonArray(message);
        if (arr_data != null) {
            for (int i = 0; i < arr_data.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(arr_data, i);

                String[] arr_str_data = new String[2];
                String vName=generalFunc.getJsonValueStr("vName", obj_temp);
                arr_str_data[0] = generalFunc.getJsonValueStr("iPackageTypeId", obj_temp);
                arr_str_data[1] = vName;

                list_packageType_items.add(arr_str_data);
                items.add(vName);
            }


            CharSequence[] cs_currency_txt = items.toArray(new CharSequence[items.size()]);

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
            builder.setTitle(generalFunc.retrieveLangLBl("Select package type", "LBL_SELECT_PACKAGE_TYPE"));

            builder.setItems(cs_currency_txt, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection

                    if (alert_packageTypes != null) {
                        alert_packageTypes.dismiss();
                    }

                    String selectedPackageTypeId = list_packageType_items.get(item)[0];

                    currentPackageTypeId = selectedPackageTypeId;

                    packageTypeBox.setText(list_packageType_items.get(item)[1]);

                }
            });


            alert_packageTypes = builder.create();
            if (generalFunc.isRTLmode() == true) {
                generalFunc.forceRTLIfSupported(alert_packageTypes);
            }

           // showPackageTypes(selectBox, viewId);
        }
    }

    public void buildPackageTypes(MaterialEditText selectBox, String message, int viewId) {
        packageTypeList.clear();
        String selectedOptionPos= elementArrayList.get(viewId).getSelectedOptionID();
        int pos=-1;
        JSONArray arr_data = generalFunc.getJsonArray(message);
        if (arr_data != null) {
            for (int i = 0; i < arr_data.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(arr_data, i);

                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("vName",generalFunc.getJsonValueStr("vName", obj_temp));
                String id=generalFunc.getJsonValueStr("iPackageTypeId", obj_temp);
                hashMap.put("iPackageTypeId",id);

                if (id.equalsIgnoreCase(selectedOptionPos))
                {
                    pos=i;
                }
                packageTypeList.add(hashMap);
            }


            showPackageTypes(selectBox,viewId,pos);
        }
    }

    public void showPackageTypes(MaterialEditText selectBox, int viewId,int selectedOptionPos) {

        OpenListView.getInstance(getActContext(), elementArrayList.get(viewId).getvFieldName(), packageTypeList, OpenListView.OpenDirection.CENTER, true, position -> {
            HashMap<String, String> mapData = packageTypeList.get(position);
            String selectedVal = "" + mapData.get("vName");
            elementArrayList.get(viewId).setSelectedOptionID(mapData.get("iPackageTypeId"));
            elementArrayList.get(viewId).setvFieldValue(selectedVal);
            selectBox.setText(selectedVal);
            enteredTextArray.add(viewId, selectedVal);
            handleResetButton();

        }).show(selectedOptionPos, "vName");
    }

    public void checkDetails() {

        boolean allDetailsEntered = true;
        boolean any1DetailEntered = false;
        for (int i = 0; i < dataArrayList.size(); i++) {
            if (dataArrayList.get(i) instanceof MaterialEditText) {

                if (Utils.checkText((MaterialEditText) dataArrayList.get(i))) {
                    any1DetailEntered = true;
                }

                if (elementArrayList.get(i).geteRequired().equalsIgnoreCase("Yes")) {
                    boolean detailEntered = Utils.checkText((MaterialEditText) dataArrayList.get(i)) ? true : Utils.setErrorFields((MaterialEditText) dataArrayList.get(i), required_str);

                    if (detailEntered == false) {
                        allDetailsEntered = false;
                    }
                }
            }
        }

        configureResetButton(any1DetailEntered);

        if (allDetailsEntered) {
            Checkpickupdropoffrestriction();

        }
    }

    private void configureResetButton(boolean enable) {
        if (enable) {
            btn_reset.setEnabled(true);
            btn_reset.setOnClickListener(new setOnClickList());
            btn_reset.setTextColor(Color.parseColor("#FFFFFF"));

        } else {

            btn_reset.setEnabled(false);
            btn_reset.setOnClickListener(null);
            btn_reset.setTextColor(Color.parseColor("#BABABA"));
        }
    }


    public void Checkpickupdropoffrestriction() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "Checkpickupdropoffrestriction");
        parameters.put("iUserId", generalFunc.getMemberId());

        for (int i = 0; i < elementArrayList.size(); i++) {
            Delivery_Data details = elementArrayList.get(i);
            try {

                if (details.geteInputType().equalsIgnoreCase("SelectAddress")) {

                    parameters.put("DestLatitude", "" + details.getDestLat());
                    parameters.put("DestLongitude", "" + details.getDestLong());
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        parameters.put("eType", "Deliver");


        parameters.put("UserType", Utils.userType);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                if (responseString != null && !responseString.equals("")) {
                    if (generalFunc.getJsonValue("Action", responseString).equalsIgnoreCase("0")) {
                        if (message.equalsIgnoreCase("LBL_DROP_LOCATION_NOT_ALLOW")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_DROP_LOCATION_NOT_ALLOW"));
                        } else if (message.equalsIgnoreCase("LBL_PICKUP_LOCATION_NOT_ALLOW")) {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_PICKUP_LOCATION_NOT_ALLOW"));
                        }
                    } else if (generalFunc.getJsonValue("Action", responseString).equalsIgnoreCase("1")) {
                        storeDetails();
                    }

                } else {
                    generalFunc.showError();
                }
            }
        });
        exeWebServer.execute();
    }

    private void storeDetails() {
        ArrayList<Delivery_Data> finalAllDetailsArray = new ArrayList<Delivery_Data>();

        JSONArray jaStore = new JSONArray();
        JSONArray jaStore1 = new JSONArray();
        JSONArray mainJaStore = new JSONArray();
        JSONArray mainAllJaStore = new JSONArray();

        for (int i = 0; i < elementArrayList.size(); i++) {
            jaStore.put(storeDetails(i, jaStore1, finalAllDetailsArray));
        }


        try {

            mainJaStore.put(0, jaStore);
            mainAllJaStore.put(0, jaStore1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle bn = new Bundle();
        if (getIntent().hasExtra("isDestAdded")) {
            Gson gson = new Gson();
            int pos = getIntent().getIntExtra("selectedPos", -1);
            String json = gson.toJson(finalAllDetailsArray);
            bn.putInt("selectedPos", pos);
            bn.putString("selectedDetails", json);
        } else {
            generalFunc.removeValue(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY);
            generalFunc.storeData(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY, mainJaStore.toString());

            Gson gson = new Gson();
            String json = gson.toJson(finalAllDetailsArray);

            generalFunc.removeValue(Utils.DELIVERY_ALL_DETAILS_KEY);
            generalFunc.storeData(Utils.DELIVERY_ALL_DETAILS_KEY, json);
            bn.putSerializable("deliveries", mainJaStore.toString());
        }
        (new StartActProcess(getActContext())).setOkResult(bn);

        backImgView.performClick();
    }


    private JSONObject storeDetails(int pos, JSONArray jsonArray, ArrayList<Delivery_Data> finalAllDetailsArray1) {
        JSONObject deliveriesObj = new JSONObject();
        JSONObject deliveriesObjall = new JSONObject();
        Delivery_Data details = elementArrayList.get(pos);
        try {

            if (details.geteInputType().equalsIgnoreCase("SelectAddress")) {
                deliveriesObjall.put("vReceiverAddress", details.getDestAddress());
                deliveriesObjall.put("vReceiverLatitude", details.getDestLat());
                deliveriesObjall.put("vReceiverLongitude", details.getDestLong());
            } else {
                deliveriesObjall.put("vFieldValue", details.getvFieldValue());
                deliveriesObjall.put("iDeliveryFieldId", details.getiDeliveryFieldId());
                deliveriesObjall.put("eInputType", details.geteInputType());
                deliveriesObjall.put("selectedOptionID", details.getSelectedOptionID());
                deliveriesObjall.put("itemID", details.getItemID());
            }

            jsonArray.put(deliveriesObjall);
            if (dataArrayList.get(pos) instanceof MaterialEditText) {
                if (details.geteInputType().equals("Select")) {
                    details.setvFieldValue(((MaterialEditText) dataArrayList.get(pos)).getText().toString());
                    deliveriesObj.put(details.getiDeliveryFieldId(), details.getSelectedOptionID());
                } else {
                    details.setvFieldValue(((MaterialEditText) dataArrayList.get(pos)).getText().toString());
                    deliveriesObj.put(details.getiDeliveryFieldId(), details.getvFieldValue());
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        finalAllDetailsArray1.add(details);
        return deliveriesObj;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {
        handleResetButton();
    }

    @Override
    public void afterTextChanged(View v, Editable s, boolean isTextEntered) {

        if (isTextEntered) {
            int viewId = generalFunc.parseIntegerValue(0, v.getTag().toString());
            enteredTextArray.add(viewId, s.toString());
        }
        handleResetButton();
    }

    @Override
    public void onTextChanged(View v, CharSequence s, int start, int before, int count) {


    }

    @Override
    public void beforeTextChanged(View v, CharSequence s, int start, int count, int after) {

    }

    public void handleResetButton() {

        if (getIntent().hasExtra("isFromMulti")) {
            boolean allEmpty = true;

            for (String value : enteredTextArray) {
                if (Utils.checkText(value)) {
                    allEmpty = false;
                    break;
                }
            }
            configureResetButton(allEmpty ? false : true);
        } else {
            try {

                boolean receiverNameEntered = Utils.checkText(receiverNameEditBox) ? true : false;
                boolean receiverMobileEntered = Utils.checkText(receiverMobileEditBox) ? true : false;
                boolean pickUpInsEntered = Utils.checkText(pickUpInstructionEditBox) ? true : false;
                boolean deliveryInsEntered = Utils.checkText(deliveryInstructionEditBox) ? true : false;
                boolean packageDetailsEntered = Utils.checkText(packageDetailsEditBox) ? true : false;
                boolean packageTypeSelected = !currentPackageTypeId.trim().equals("") ? true : false;

                if (receiverNameEntered == true || receiverMobileEntered == true || pickUpInsEntered == true || deliveryInsEntered == true
                        || packageDetailsEntered == true || packageTypeSelected == true) {

                    configureResetButton(true);
                } else {
                    configureResetButton(false);
                }
            } catch (Exception e) {

            }
        }

    }

    public void resetAllView() {
        for (int i = 0; i < dataArrayList.size(); i++) {
            if (dataArrayList.get(i) instanceof MaterialEditText) {
                ((MaterialEditText) dataArrayList.get(i)).setText("");
            }
        }
    }

    public class setOnTouchList implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && !view.hasFocus()) {
                view.performClick();
            }
            return true;
        }
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == R.id.backImgView) {
                EnterMultiDeliveryDetailsActivity.super.onBackPressed();
            } else if (i == btn_type2.getId()) {
                checkDetails();
            } else if (i == R.id.packageTypeBox) {

                /*if (alert_packageTypes != null) {
                    showPackageTypes(selectBox, viewId);
                } else {
                    loadPackageTypes();
                }*/
            } else if (view == btn_reset) {

                final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                generateAlert.setCancelable(false);
                generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                    @Override
                    public void handleBtnClick(int btn_id) {
                        if (btn_id == 0) {
                            generateAlert.closeAlertBox();
                        } else {
                            generateAlert.closeAlertBox();
                            resetAllView();
                        }
                    }
                });
                generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", "LBL_ALL_DATA_CLEAR"));
                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CLEAR"));
                generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
                generateAlert.showAlertBox();

            }
        }
    }

}
