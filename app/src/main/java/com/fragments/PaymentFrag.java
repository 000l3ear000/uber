package com.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.melevicarbrasil.usuario.BookingSummaryActivity;
import com.melevicarbrasil.usuario.CardPaymentActivity;
import com.melevicarbrasil.usuario.MyWalletActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import java.util.HashMap;

public class PaymentFrag extends Fragment {


    public GeneralFunctions generalFunc;

    public String userProfileJson = "";
    MButton goToOrderSummaryBtn;
    RadioGroup radioGroup;
    RadioButton radioPayOnline, radioCashPayment;
    RadioButton seleted;
    Bundle bundle = new Bundle();
    AddCardFragment addCardFrag;
    LinearLayout cardarea;
    ViewCardFragment viewCardFrag;
    View v;
    BookingSummaryActivity bookingSummaryActivity;
    androidx.appcompat.app.AlertDialog outstanding_dialog;

    boolean clickable = false;
    boolean isPayNow = false;

    String SYSTEM_PAYMENT_FLOW="";
    String APP_PAYMENT_METHOD="";
    String APP_PAYMENT_MODE="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.activity_ufx_payment, container, false);

        bookingSummaryActivity = (BookingSummaryActivity) getActivity();

        generalFunc = bookingSummaryActivity.generalFunc;

        getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGrp);
        radioPayOnline = (RadioButton) v.findViewById(R.id.radioPayOnline);
        radioCashPayment = (RadioButton) v.findViewById(R.id.radioCashPayment);
        radioCashPayment.setText(generalFunc.retrieveLangLBl("", "LBL_CASH_PAYMENT_TXT"));
        radioPayOnline.setText(generalFunc.retrieveLangLBl("Pay Online", "LBL_PAY_ONLINE_TXT"));
        radioCashPayment.setChecked(true);


        if (bookingSummaryActivity.ACCEPT_CASH_TRIPS.equalsIgnoreCase("NO")) {
            radioPayOnline.setChecked(true);
            radioCashPayment.setChecked(false);
        }


        if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash")) {
            radioCashPayment.setVisibility(View.VISIBLE);
            radioPayOnline.setVisibility(View.GONE);

        } else if (APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
            radioCashPayment.setVisibility(View.GONE);
            radioPayOnline.setVisibility(View.VISIBLE);
            radioPayOnline.setChecked(true);

        } else {
            radioCashPayment.setVisibility(View.VISIBLE);
            radioPayOnline.setVisibility(View.VISIBLE);
        }

        if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            radioPayOnline.setText(generalFunc.retrieveLangLBl("Pay by Wallet", "LBL_PAY_BY_WALLET_TXT") + "(" + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson) + ")"));

        }

        cardarea = (LinearLayout) v.findViewById(R.id.cardarea);

        goToOrderSummaryBtn = ((MaterialRippleLayout) v.findViewById(R.id.goToOrderSummaryBtn)).getChildView();
        goToOrderSummaryBtn.setId(Utils.generateViewId());
        goToOrderSummaryBtn.setText(generalFunc.retrieveLangLBl("", "LBL_GOTO_ORDER_SUMMARY_TXT"));
        goToOrderSummaryBtn.setOnClickListener(new setOnClick());

        if (bookingSummaryActivity.bookingtype.equals(Utils.CabReqType_Now)) {
            goToOrderSummaryBtn.setText(generalFunc.retrieveLangLBl("", "LBL_BOOK_NOW"));
        } else {
            goToOrderSummaryBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CONFIRM_BOOKING"));
        }

        ((MTextView) v.findViewById(R.id.howToPayTxt)).setText(generalFunc.retrieveLangLBl("How would you like to pay?", "LBL_HOW_TO_PAY_TXT"));

        radioPayOnline.setOnClickListener(view -> {
            radioPayOnline.setChecked(true);
            radioCashPayment.setChecked(false);

            cardarea.setVisibility(View.VISIBLE);
            //   openViewCardFrag();

            int selectedId = radioGroup.getCheckedRadioButtonId();
            seleted = (RadioButton) v.findViewById(selectedId);

            String paymenttype = "";

            if (selectedId == radioCashPayment.getId()) {
                paymenttype = "cash";
            } else {
                paymenttype = "card";

                getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

                if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

                    if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {

                        String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);

                        if (vStripeCusId.equals("")) {
                            Bundle bn = new Bundle();
                            bn.putBoolean("isufxbook", true);
                            new StartActProcess(bookingSummaryActivity).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
                            return;
                        }
                    }
                }
            }

            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                String strCardAdd = generalFunc.retrieveValue(Utils.isCardAdded);
                if (strCardAdd.equals("true")) {
                    checkPaymentCard(paymenttype);
                } else {
                    getOutStandingAmout("card", paymenttype);
                }
            }

        });


        radioCashPayment.setOnClickListener(view -> {
            if (bookingSummaryActivity.ACCEPT_CASH_TRIPS.equalsIgnoreCase("NO")) {

                radioCashPayment.setChecked(false);
                radioPayOnline.setChecked(true);
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Your selected provider can't accept cash payment", "LBL_CASH_DISABLE_PROVIDER"));

            } else {
                radioPayOnline.setChecked(true);
                radioCashPayment.setChecked(true);
            }

        });

        return v;

    }

    private void getUserProfileJson(String userProfileJson_) {
        userProfileJson = userProfileJson_;
        SYSTEM_PAYMENT_FLOW=generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson);
        APP_PAYMENT_METHOD=generalFunc.getJsonValue("APP_PAYMENT_METHOD", userProfileJson);
        APP_PAYMENT_MODE=generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1"))
            {
                if (generalFunc.getJsonValue("vCreditCard", userProfileJson).equalsIgnoreCase("")) {
                    radioCashPayment.setChecked(true);
                    radioPayOnline.setChecked(false);
                }
        }
    } catch(Exception e)
    {

    }
}

    private void viewcard() {
        Bundle bundle = new Bundle();
        bundle.putString("PAGE_MODE", "ADD_CARD");
        addCardFrag = new AddCardFragment();
        addCardFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.cardarea, addCardFrag).commit();
    }

    public void changeUserProfileJson(String userProfileJson) {
        this.userProfileJson = userProfileJson;
        getUserProfileJson(userProfileJson);

        Bundle bn = new Bundle();
        bn.putString("UserProfileJson", userProfileJson);
        new StartActProcess(bookingSummaryActivity.getActContext()).setOkResult(bn);

        openViewCardFrag();

        generalFunc.showMessage(v, generalFunc.retrieveLangLBl("", "LBL_INFO_UPDATED_TXT"));
    }

    public void openViewCardFrag() {

        if (viewCardFrag != null) {
            viewCardFrag = null;
            addCardFrag = null;
            Utils.runGC();
        }
        viewCardFrag = new ViewCardFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.cardarea, viewCardFrag).commit();
    }

    public void openAddCardFrag(String mode) {

        if (addCardFrag != null) {
            addCardFrag = null;
            viewCardFrag = null;
            Utils.runGC();
        }

        Bundle bundle = new Bundle();
        bundle.putString("PAGE_MODE", mode);
        addCardFrag = new AddCardFragment();
        addCardFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.cardarea, addCardFrag).commit();
    }

    public void checkCardConfig() {

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);
            if (vStripeCusId.equals("")) {
                OpenCardPaymentAct(false);
            } else {
                showPaymentBox(true, "card");
            }
        }
    }

    public void OpenCardPaymentAct(boolean fromcabselection) {
        Bundle bn = new Bundle();
        // bn.putString("UserProfileJson", userProfileJson);
        bn.putBoolean("fromcabselection", fromcabselection);
        new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
    }

    public void outstandingDialog(String data) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dailog_outstanding, null);

        final MTextView outStandingTitle = (MTextView) dialogView.findViewById(R.id.outStandingTitle);
        final MTextView outStandingValue = (MTextView) dialogView.findViewById(R.id.outStandingValue);
        final MTextView cardtitleTxt = (MTextView) dialogView.findViewById(R.id.cardtitleTxt);
        final MTextView adjustTitleTxt = (MTextView) dialogView.findViewById(R.id.adjustTitleTxt);
        final LinearLayout cardArea = (LinearLayout) dialogView.findViewById(R.id.cardArea);
        final LinearLayout adjustarea = (LinearLayout) dialogView.findViewById(R.id.adjustarea);


        outStandingTitle.setText(generalFunc.retrieveLangLBl("", "LBL_OUTSTANDING_AMOUNT_TXT"));
        outStandingValue.setText(generalFunc.getJsonValue("fOutStandingAmountWithSymbol", data));
        cardtitleTxt.setText(generalFunc.retrieveLangLBl("Pay Now", "LBL_PAY_NOW"));

        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-3")) {
            adjustarea.setVisibility(View.GONE);
        }
        adjustTitleTxt.setText(generalFunc.retrieveLangLBl("Adjust in Your trip", "LBL_ADJUST_OUT_AMT_JOB_TXT"));

        if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
            if (APP_PAYMENT_MODE.equalsIgnoreCase("Cash-Card") ||
                    APP_PAYMENT_MODE.equalsIgnoreCase("Card")) {
                cardArea.setVisibility(View.VISIBLE);

            }
        } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

            cardtitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAY_BY_WALLET_TXT"));
            cardArea.setVisibility(View.VISIBLE);
        }


        cardArea.setOnClickListener(v -> {
            outstanding_dialog.dismiss();
            clickable = false;

            isPayNow = true;


            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                checkCardConfig();
            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                callOutStandingPayAmout();


            }

        });

        adjustarea.setOnClickListener(v -> {
            outstanding_dialog.dismiss();
            clickable = false;
            handleOrderSunnaryBtn();
        });

        if (generalFunc.isRTLmode()) {
            ((ImageView) dialogView.findViewById(R.id.cardimagearrow)).setRotationY(180);
            ((ImageView) dialogView.findViewById(R.id.adjustimagearrow)).setRotationY(180);
        }

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        int submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        btn_type2.setOnClickListener(v -> {
            clickable = false;
            outstanding_dialog.dismiss();
        });

        builder.setView(dialogView);
        outstanding_dialog = builder.create();
        if (generalFunc.isRTLmode() == true) {
            generalFunc.forceRTLIfSupported(outstanding_dialog);
        }
        outstanding_dialog.setCancelable(false);
        outstanding_dialog.show();
    }

    public void callOutStandingPayAmout() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "ChargePassengerOutstandingAmount");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setCancelAble(false);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                    getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

                    clickable = false;
                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {

                    });

                    generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    generateAlert.showAlertBox();
                } else {
                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {
                        String walletMsg = "";
                        String low_balance_content_msg=generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), button_Id -> {
                            if (button_Id == 1) {

                                new StartActProcess(getActContext()).startAct(MyWalletActivity.class);
                            } else {

                            }
                        });
                    }
                }
            } else {

                if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_OUTSTANDING_AMOUT_ALREADY_PAID_TXT")) {
                    String message = generalFunc.getJsonValue(Utils.message_str_one, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                    getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            }
        });
        exeWebServer.execute();

    }


public class setOnClick implements View.OnClickListener {

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.backImgView) {
            new StartActProcess(bookingSummaryActivity.getActContext()).setOkResult(bundle);
            bookingSummaryActivity.onBackPressed();
        } else if (radioPayOnline.isChecked() && SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

            checkPaymentCard("card");

        } else if (i == goToOrderSummaryBtn.getId()) {
            if (!clickable) {
                clickable = true;

                getOutStandingAmout("goToOrderSummaryBtn", radioPayOnline.isChecked() ? "card" : (radioCashPayment.isChecked() ? "Cash" : ""));
            }
        }
    }

}


    public void getOutStandingAmout(String clicked, String paymentType) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "checkSurgePrice");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            boolean isDataAvail = false;

            if (responseString != null && !responseString.equals("")) {
                checkOutStandingAmount(clicked, responseString, paymentType);
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }

    public void checkOutStandingAmount(String clicked, String message, String paymentType) {
        String fOutStandingAmount = "";
        if (Utils.checkText(message)) {
            fOutStandingAmount = generalFunc.getJsonValue("fOutStandingAmount", message);
        }
        boolean isDataAvail = GeneralFunctions.parseDoubleValue(0.0, fOutStandingAmount) > 0;

        if (!paymentType.equalsIgnoreCase("card") && isDataAvail) {
            outstandingDialog(isDataAvail ? message : userProfileJson);
        } else if (clicked.equalsIgnoreCase("goToOrderSummaryBtn")) {
            if (paymentType.equalsIgnoreCase("card")) {
                if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    showPaymentBox(false, paymentType);
                } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                    handleOrderSunnaryBtn();
                }

            } else {
                handleOrderSunnaryBtn();
            }
        } else if (clicked.equalsIgnoreCase("card")) {
            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                showPaymentBox(false, paymentType);
            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                handleOrderSunnaryBtn();
            }
        }
    }


    public void handleOrderSunnaryBtn() {
        clickable = false;

        int selectedId = radioGroup.getCheckedRadioButtonId();
        seleted = (RadioButton) v.findViewById(selectedId);
        String paymenttype = "";

        if (selectedId == radioCashPayment.getId()) {
            paymenttype = "cash";

            bundle.putBoolean("isufxpayment", true);
            bundle.putString("comment", bookingSummaryActivity.comment);
            bundle.putString("promocode", bookingSummaryActivity.promocode);
            bundle.putString("paymenttype", paymenttype);
            new StartActProcess(bookingSummaryActivity.getActContext()).setOkResult(bundle);
            bookingSummaryActivity.finish();
            if (radioGroup.getCheckedRadioButtonId() != -1) {

            } else {
                Toast.makeText(bookingSummaryActivity.getActContext(), "" + generalFunc.retrieveLangLBl("", "LBL_PLEASE_SELECT_AT_LEAST_ONE_TXT"), Toast.LENGTH_SHORT).show();
            }

        } else {


            paymenttype = "card";

            if (SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {

                getUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

                if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {

                    String vStripeCusId = generalFunc.getJsonValue("vStripeCusId", userProfileJson);

                    if (vStripeCusId.equals("")) {
                        Bundle bn = new Bundle();
                        bn.putBoolean("isufxbook", true);
                        new StartActProcess(bookingSummaryActivity).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
                        return;
                    }
                }

                String strCardAdd = generalFunc.retrieveValue(Utils.isCardAdded);
                if (strCardAdd.equals("true")) {
                    checkPaymentCard(paymenttype);
                } else {
                    showPaymentBox(false, paymenttype);
                }

            } else if (!SYSTEM_PAYMENT_FLOW.equalsIgnoreCase("Method-1")) {
                bundle.putBoolean("isufxpayment", true);
                bundle.putString("comment", bookingSummaryActivity.comment);
                bundle.putString("promocode", bookingSummaryActivity.promocode);
                bundle.putString("paymenttype", paymenttype);
                new StartActProcess(bookingSummaryActivity.getActContext()).setOkResult(bundle);
                bookingSummaryActivity.finish();
            }


        }
    }


    public void showPaymentBox(boolean isOutstanding, String paymenttype) {
        androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.input_box_view, null);
        builder.setView(dialogView);

        final MaterialEditText input = (MaterialEditText) dialogView.findViewById(R.id.editBox);
        final MTextView subTitleTxt = (MTextView) dialogView.findViewById(R.id.subTitleTxt);

        Utils.removeInput(input);

        subTitleTxt.setVisibility(View.VISIBLE);
        subTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TITLE_PAYMENT_ALERT"));
        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {

            input.setText(generalFunc.getJsonValue("vCreditCard", userProfileJson));
        }

        builder.setPositiveButton(generalFunc.retrieveLangLBl("Confirm", "LBL_BTN_TRIP_CANCEL_CONFIRM_TXT"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (isPayNow) {
                    callOutStandingPayAmout();
                    isPayNow = false;
                } /*else {
                    checkPaymentCard(paymenttype);
                }*/
            }
        });
        builder.setNeutralButton(generalFunc.retrieveLangLBl("Change", "LBL_CHANGE"), (dialog, which) -> {
            dialog.cancel();

            Bundle bn = new Bundle();
            bn.putBoolean("isufxbook", true);
            new StartActProcess(bookingSummaryActivity).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
        });
        builder.setNegativeButton(generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), (dialog, which) -> {

            radioCashPayment.setChecked(true);
            radioPayOnline.setChecked(false);
            dialog.cancel();
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void checkPaymentCard(String paymenttype) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "CheckCard");
        parameters.put("iUserId", generalFunc.getMemberId());

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);
                if (action.equals("1")) {

                    generalFunc.storeData(Utils.isCardAdded, "false");

                    ///setCardSelection();

                    bundle.putBoolean("isufxpayment", true);
                    bundle.putString("comment", bookingSummaryActivity.comment);
                    bundle.putString("promocode", bookingSummaryActivity.promocode);
                    bundle.putString("paymenttype", paymenttype);
                    new StartActProcess(bookingSummaryActivity.getActContext()).setOkResult(bundle);
                    bookingSummaryActivity.finish();

                    if (radioGroup.getCheckedRadioButtonId() != -1) {

                    } else {
                        Toast.makeText(bookingSummaryActivity.getActContext(),
                                "" + generalFunc.retrieveLangLBl("", "LBL_PLEASE_SELECT_AT_LEAST_ONE_TXT"),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public Context getActContext() {
        return getActivity();
    }

}
