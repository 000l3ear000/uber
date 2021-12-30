package com.melevicarbrasil.usuario;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dialogs.OpenListView;
import com.general.files.AppFunctions;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 08-03-18.
 */

public class Help_DetailsActivity extends AppCompatActivity {
    public GeneralFunctions generalFunc;
    MTextView titleTxt;
    ImageView backImgView;
    MTextView headerTitleTxt;
    MTextView descriptionTxt;
    MTextView contact_us_btn;
    //main views//

    String required_str = "";
    //View view;
    String iHelpDetailId = "";
    String iUniqueId = "";
    ArrayList<HashMap<String, String>> reasonsDataList = new ArrayList<>();
    CardView cardView;
    InternetConnection intCheck;
    View contentAreaView;
    View loadingBar;
    //GenerateAlertBox reasonDataAlertBox;
    Dialog FurtherAssistanceDialog;
    String responseStringWeb = "";
    private int filterPosition = -1;
    ImageView closeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        intCheck = new InternetConnection(getActContext());

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        descriptionTxt = (MTextView) findViewById(R.id.descriptionTxt);
        headerTitleTxt = (MTextView) findViewById(R.id.headerTitleTxt);
        contact_us_btn = (MTextView) findViewById(R.id.contact_us_btn);
        contentAreaView = findViewById(R.id.contentAreaView);

        loadingBar = findViewById(R.id.loadingBar);

        //cardView = (CardView) findViewById(R.id.contactCardViewArea);
        // view = (View) findViewById(R.id.view);

        backImgView.setOnClickListener(new setOnClickList());

        contact_us_btn.setOnClickListener(new setOnClickList());

        if (getIntent().getStringExtra("iHelpDetailId") != null) {
            iHelpDetailId = getIntent().getStringExtra("iHelpDetailId");
        }

        if (getIntent().getStringExtra("iUniqueId") != null) {
            iUniqueId = getIntent().getStringExtra("iUniqueId");
        }

        setLabels();
        getCategoryTitleList();
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_HELP_TXT"));
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
    }

    public void getCategoryTitleList() {
        contentAreaView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getHelpDetail");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("appType", Utils.app_type);
        parameters.put("iUniqueId", iUniqueId);

        if (getIntent().hasExtra("iOrderId")) {
            parameters.put("iOrderId", getIntent().getStringExtra("iOrderId"));
            parameters.put("eSystem", Utils.eSystem_Type);
        } else {
            parameters.put("iTripId", getIntent().getStringExtra("iTripId"));
        }

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {

                    responseStringWeb = responseString;
                    generateFurtherAssistantDialog();
                    contentAreaView.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.GONE);

                    //  buildReasonData(generalFunc.getJsonArray(Utils.message_str, responseString));

                  /*  if (showList) {
                        if (reasonDataAlertBox != null) {
                            reasonDataAlertBox.showAlertBox();
                        }
                    }*/

                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), true);
                }

            }
        });
        exeWebServer.execute();
    }

    private void buildReasonData(JSONArray obj_arr) {
        reasonsDataList.clear();

        // GenerateAlertBox generateAlertBox = new GenerateAlertBox(getActContext());
        //  generateAlertBox.setContentMessage(getSelectCategoryText(), null);
        //  generateAlertBox.setCancelable(true);

        for (int i = 0; i < obj_arr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);

            HashMap<String, String> mapData = new HashMap<>();
            mapData.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
            mapData.put("iHelpDetailId", generalFunc.getJsonValueStr("iHelpDetailId", obj_temp));
            mapData.put("tAnswer", generalFunc.getJsonValueStr("tAnswer", obj_temp));
            mapData.put("eShowFrom", generalFunc.getJsonValueStr("eShowFrom", obj_temp));
            mapData.put("vName", generalFunc.getJsonValueStr("vTitle", obj_temp));
            mapData.put("selectedSortValue", "");

            reasonsDataList.add(mapData);

            if (iHelpDetailId.equalsIgnoreCase(mapData.get("iHelpDetailId"))) {
                categoryText.setText(mapData.get("vTitle"));
                headerTitleTxt.setText(AppFunctions.fromHtml(mapData.get("vTitle")));
                descriptionTxt.setText(AppFunctions.fromHtml(mapData.get("tAnswer")));

                if (mapData.get("eShowFrom").equalsIgnoreCase("Yes")) {
                    helpContactslayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.help_card).setVisibility(View.VISIBLE);
                } else {
                    helpContactslayout.setVisibility(View.GONE);
                    findViewById(R.id.help_card).setVisibility(View.GONE);
                }
            }
        }

      /*  generateAlertBox.createList(reasonsDataList, "vTitle", position -> {

            if (reasonDataAlertBox != null) {
                reasonDataAlertBox.closeAlertBox();
            }

            categoryText.setText(reasonsDataList.get(position).get("vTitle"));
            iHelpDetailId = reasonsDataList.get(position).get("iHelpDetailId");

            if (reasonsDataList.get(position).get("eShowFrom").equalsIgnoreCase("Yes")) {
                helpContactslayout.setVisibility(View.VISIBLE);
            } else {
                helpContactslayout.setVisibility(View.GONE);
               // view.setVisibility(View.GONE);
            }

           // headerTitleTxt.setText(AppFunctions.fromHtml(reasonsDataList.get(position).get("vTitle")));
            //descriptionTxt.setText(AppFunctions.fromHtml(reasonsDataList.get(position).get("tAnswer")));
        });*/

        // reasonDataAlertBox = generateAlertBox;
    }

    public void submitQuery() {
        boolean contentEntered = Utils.checkText(contentBox);

        if (!contentEntered) {
            ((MTextView) FurtherAssistanceDialog.findViewById(R.id.subjectBox_error)).setText(required_str);
            FurtherAssistanceDialog.findViewById(R.id.subjectBox_error).setVisibility(!contentEntered ? View.VISIBLE : View.INVISIBLE);

            return;
        } else {
            FurtherAssistanceDialog.findViewById(R.id.subjectBox_error).setVisibility(View.INVISIBLE);

        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "submitTripHelpDetail");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("iHelpDetailId", iHelpDetailId);
        parameters.put("UserType", Utils.app_type);
        parameters.put("UserId", generalFunc.getMemberId());
        parameters.put("vComment", Utils.getText(contentBox));

        if (getIntent().hasExtra("iOrderId")) {
            parameters.put("iOrderId", getIntent().getStringExtra("iOrderId"));
            parameters.put("eSystem", Utils.eSystem_Type);
        } else {
            parameters.put("TripId", getIntent().getStringExtra("iTripId"));
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    contentBox.setText("");
                }

                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), null);
                FurtherAssistanceDialog.dismiss();

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public String getSelectCategoryText() {
        return ("" + generalFunc.retrieveLangLBl("", "LBL_SELECT_TXT"));
    }

    public Context getActContext() {
        return Help_DetailsActivity.this;
    }

    public class setOnClickList implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());
            if (i == R.id.backImgView) {
                Help_DetailsActivity.super.onBackPressed();
            } else if (i == R.id.contact_us_btn) {

                displayFurtherAssistantDialog();
                Utils.hideKeyboard(getActContext());

            } else if (i == btn_type2.getId()) {
                submitQuery();

            } else if (i == R.id.categoryarea) {
                openFilterDilaog();
                // if (reasonDataAlertBox != null) {
                // //     reasonDataAlertBox.showAlertBox();
                // } else {
                //    getCategoryTitleList(true);
                // }
            } else if (i == R.id.closeImg) {
                FurtherAssistanceDialog.dismiss();
            }
        }
    }


    MaterialEditText contentBox;
    MButton btn_type2;
    View helpContactslayout;
    MTextView categoryText;

    private void generateFurtherAssistantDialog() {

        FurtherAssistanceDialog = new Dialog(getActContext(), R.style.ImageSourceDialogStyle);
        FurtherAssistanceDialog.setContentView(R.layout.furtherassistancedialog);

        MTextView contactTxt;
        MTextView reasonContactTxt;
        LinearLayout categoryarea;
        MTextView additionalCommentTxt;
        View pullIndicator;
        RelativeLayout contentBoxBorder;

        closeImg = FurtherAssistanceDialog.findViewById(R.id.closeImg);
        helpContactslayout = FurtherAssistanceDialog.findViewById(R.id.helpContactslayout);
        contactTxt = (MTextView) FurtherAssistanceDialog.findViewById(R.id.contactTxt);
        reasonContactTxt = (MTextView) FurtherAssistanceDialog.findViewById(R.id.reasonContactTxt);
        categoryarea = (LinearLayout) FurtherAssistanceDialog.findViewById(R.id.categoryarea);
        categoryText = (MTextView) FurtherAssistanceDialog.findViewById(R.id.categoryText);
        pullIndicator = (View) FurtherAssistanceDialog.findViewById(R.id.pullIndicator);
        contentBoxBorder = (RelativeLayout) FurtherAssistanceDialog.findViewById(R.id.contentBoxBorder);
        additionalCommentTxt = (MTextView) FurtherAssistanceDialog.findViewById(R.id.additionalCommentTxt);
        contentBox = (MaterialEditText) FurtherAssistanceDialog.findViewById(R.id.contentBox);
        btn_type2 = ((MaterialRippleLayout) FurtherAssistanceDialog.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());
        categoryarea.setOnClickListener(new setOnClickList());
        closeImg.setOnClickListener(new setOnClickList());

        new CreateRoundedView(Color.parseColor("#949494"), 15, 0, Color.parseColor("#949494"), pullIndicator);
        new CreateRoundedView(Color.parseColor("#ffffff"), 1, 1, Color.parseColor("#949494"), contentBoxBorder);

        reasonContactTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RES_TO_CONTACT") + ":");//LBL_SELECT_RES_TO_CONTACT
        contactTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CONTACT_SUPPORT_ASSISTANCE_TXT"));
        additionalCommentTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADDITIONAL_COMMENTS"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_SUBMIT_TXT")); //LBL_SEND_QUERY_BTN_TXT
        contentBox.setHint(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_WRITE_EMAIL_TXT"));
        // contentBox.setFloatingLabelAlwaysShown(true);
        contentBox.setSingleLine(false);
        contentBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        contentBox.setGravity(Gravity.TOP);
        contentBox.setHideUnderline(true);
        Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.systemLightFont));
        contentBox.setTypeface(tf);
        if (!getIntent().hasExtra("iOrderId")) {
            new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getActContext(), 2), Utils.dipToPixels(getActContext(), 1), Color.parseColor("#989898"), categoryarea);
        }

        buildReasonData(generalFunc.getJsonArray(Utils.message_str, responseStringWeb));

    }

    private void displayFurtherAssistantDialog() {
        Window window = FurtherAssistanceDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FurtherAssistanceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FurtherAssistanceDialog.show();
        Utils.hideKeyboard(getActContext());


    }


    public void openFilterDilaog() {
        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", getSelectCategoryText()), reasonsDataList, OpenListView.OpenDirection.CENTER, true, position -> {
            filterPosition = position;
            categoryText.setText(reasonsDataList.get(position).get("vName"));
        }).show(filterPosition, "vName");
    }

}
