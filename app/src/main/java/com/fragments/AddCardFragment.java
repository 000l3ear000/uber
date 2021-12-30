package com.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.BookingSummaryActivity;
import com.melevicarbrasil.usuario.CardPaymentActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.StartActProcess;
import com.paymaya.sdk.android.payment.PayMayaPayment;
import com.paymaya.sdk.android.payment.models.PaymentToken;
import com.stripe.android.CardUtils;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.StripeEditText;
import com.utils.Logger;
import com.utils.ModelUtils;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MaterialRippleLayout;
import com.view.MyProgressDialog;
import com.view.editBox.MaterialEditText;
import com.xendit.Models.XenditError;
import com.xendit.Xendit;

import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.HashMap;

import co.omise.android.Client;
import co.omise.android.TokenRequest;
import co.omise.android.TokenRequestListener;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCardFragment extends Fragment implements TextWatcher {

    private static final char SPACE_CHAR = ' ';

    GeneralFunctions generalFunc;
    View view;

    CardPaymentActivity cardPayAct;

    BookingSummaryActivity bookingSummaryActivity;

    String userProfileJson;
    MButton btn_type2;
    MaterialEditText nameOfCardBox;
    MaterialEditText creditCardBox;
    MaterialEditText cvvBox;
    MaterialEditText mmBox;
    MaterialEditText yyBox;

    View nameArea;
    String required_str = "";
    public boolean isInProcessMode = false;
    LinearLayout defaultArea, stripearea;
    CardMultilineWidget card_input_widget;
    ImageView stCardImgView;
    ImageView cardImgView;

    String LBL_ADD_CARD = "";
    String LBL_CHANGE_CARD = "";
    String APP_PAYMENT_METHOD = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_card, container, false);

        if (getActivity() instanceof CardPaymentActivity) {
            cardPayAct = (CardPaymentActivity) getActivity();
            generalFunc = cardPayAct.generalFunc;
            userProfileJson = cardPayAct.userProfileJson;
        }
        if (getActivity() instanceof BookingSummaryActivity) {

            bookingSummaryActivity = (BookingSummaryActivity) getActivity();
            generalFunc = bookingSummaryActivity.generalFunc;
            userProfileJson = bookingSummaryActivity.userProfileJson;

        }

        APP_PAYMENT_METHOD = generalFunc.getJsonValue("APP_PAYMENT_METHOD", userProfileJson);
        LBL_ADD_CARD = generalFunc.retrieveLangLBl("", "LBL_ADD_CARD");
        LBL_CHANGE_CARD = generalFunc.retrieveLangLBl("Change Card", "LBL_CHANGE_CARD");

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();
        nameOfCardBox = (MaterialEditText) view.findViewById(R.id.nameOfCardBox);
        creditCardBox = (MaterialEditText) view.findViewById(R.id.creditCardBox);
        cvvBox = (MaterialEditText) view.findViewById(R.id.cvvBox);
        mmBox = (MaterialEditText) view.findViewById(R.id.mmBox);
        yyBox = (MaterialEditText) view.findViewById(R.id.yyBox);
        defaultArea = (LinearLayout) view.findViewById(R.id.defaultArea);
        stripearea = (LinearLayout) view.findViewById(R.id.stripearea);
        card_input_widget = (CardMultilineWidget) view.findViewById(R.id.card_input_widget);
        stCardImgView = (ImageView) view.findViewById(R.id.stCardImgView);
        cardImgView = (ImageView) view.findViewById(R.id.cardImgView);
        nameArea = view.findViewById(R.id.nameArea);

        cardImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_default));
        if (cardPayAct != null) {
            if (getArguments().getString("PAGE_MODE").equals("ADD_CARD")) {
                cardPayAct.changePageTitle(LBL_ADD_CARD);
                btn_type2.setText(LBL_ADD_CARD);
            } else {
                cardPayAct.changePageTitle(LBL_CHANGE_CARD);
                btn_type2.setText(LBL_CHANGE_CARD);
            }
        } else {

            if (getArguments().getString("PAGE_MODE").equals("ADD_CARD")) {
                btn_type2.setText(LBL_ADD_CARD);
            } else {
                btn_type2.setText(LBL_CHANGE_CARD);
            }

        }

        btn_type2.setId(Utils.generateViewId());
        btn_type2.setOnClickListener(new setOnClickList());

        setLabels();

        mmBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        yyBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvvBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        cvvBox.setImeOptions(EditorInfo.IME_ACTION_DONE);
        creditCardBox.setInputType(InputType.TYPE_CLASS_PHONE);

        creditCardBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
        mmBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        cvvBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        yyBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        creditCardBox.addTextChangedListener(this);

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            stripearea.setVisibility(View.VISIBLE);
            defaultArea.setVisibility(View.GONE);
            configureStripeView();
        }  else {
            stripearea.setVisibility(View.GONE);
            defaultArea.setVisibility(View.VISIBLE);

        }
        setCardIOView(true);

        setBrands();

        return view;
    }

    public void setBrands() {
        Card.BRAND_RESOURCE_MAP.clear();
        HashMap<String, Integer> brandMap = new HashMap<String, Integer>();
        brandMap.put(Card.AMERICAN_EXPRESS, R.drawable.ic_amex_system);
        brandMap.put(Card.DINERS_CLUB, R.drawable.ic_diners_system);
        brandMap.put(Card.DISCOVER, R.drawable.ic_discover_system);
        brandMap.put(Card.JCB, R.drawable.ic_jcb_system);
        brandMap.put(Card.MASTERCARD, R.drawable.ic_mastercard_system);
        brandMap.put(Card.VISA, R.drawable.ic_visa_system);
        brandMap.put(Card.UNIONPAY, R.drawable.ic_unionpay_system);
        brandMap.put(Card.UNKNOWN, R.drawable.ic_unknown);
        Card.BRAND_RESOURCE_MAP.putAll(brandMap);
    }

    public void setLabels() {

        nameOfCardBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_CARD_HOLDER_NAME_TXT"), generalFunc.retrieveLangLBl("", "LBL_CARD_HOLDER_NAME_TXT"));
        creditCardBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_CARD_NUMBER_HEADER_TXT"), generalFunc.retrieveLangLBl("", "LBL_CARD_NUMBER_HINT_TXT"));
        cvvBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_CVV_HEADER_TXT"), generalFunc.retrieveLangLBl("", "LBL_CVV_HINT_TXT"));
        mmBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_EXP_MONTH_HINT_TXT"), generalFunc.retrieveLangLBl("", "LBL_EXP_MONTH_HINT_TXT"));
        yyBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_EXP_YEAR_HINT_TXT"), generalFunc.retrieveLangLBl("", "LBL_EXP_YEAR_HINT_TXT"));

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        creditCardBox.addTextChangedListener(new CardTypeTextWatcher());
    }

    public boolean validateExpYear(Calendar now) {
        return yyBox.getText().toString() != null && !ModelUtils.hasYearPassed(GeneralFunctions.parseIntegerValue(0, yyBox.getText().toString()), now);
    }

    private void setCardIOView(boolean isShow) {
        if (Utils.isClassExist("io.card.payment.CardIOActivity")) {
            try {
                View actView = generalFunc.getCurrentView(getActivity());
                if (actView.findViewById(R.id.cardioview) != null) {
                    View cardioview = actView.findViewById(R.id.cardioview);
                    cardioview.setVisibility(isShow ? View.VISIBLE : View.GONE);
                    cardioview.setOnClickListener(new setOnClickList());
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onDetach() {
        setCardIOView(false);
        super.onDetach();
    }

    public class CardTypeTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            getBrands(CardUtils.getPossibleCardType(editable.toString()));
        }
    }


    public Drawable getBrands(String carVal) {

        if (carVal.equalsIgnoreCase(Card.AMERICAN_EXPRESS))
            return getResources().getDrawable(R.drawable.ic_amex_system);
        else if (carVal.equalsIgnoreCase(Card.DINERS_CLUB))
            return getResources().getDrawable(R.drawable.ic_diners_system);
        else if (carVal.equalsIgnoreCase(Card.DISCOVER))
            return getResources().getDrawable(R.drawable.ic_discover_system);
        else if (carVal.equalsIgnoreCase(Card.JCB))
            return getResources().getDrawable(R.drawable.ic_jcb_system);
        else if (carVal.equalsIgnoreCase(Card.MASTERCARD))
            return getResources().getDrawable(R.drawable.ic_mastercard_system);
        else if (carVal.equalsIgnoreCase(Card.VISA))
            return getResources().getDrawable(R.drawable.ic_visa_system);
        else if (carVal.equalsIgnoreCase(Card.UNIONPAY))
            return getResources().getDrawable(R.drawable.ic_unionpay_system);
        else if (carVal.equalsIgnoreCase(Card.UNKNOWN))
            return getResources().getDrawable(R.drawable.ic_card_default);
        else
            return getResources().getDrawable(R.drawable.ic_card_default);

    }

    public void configureStripeView() {
        stCardImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_default));

        View cardNumView = getStripeCardBox(com.stripe.android.R.id.et_add_source_card_number_ml);

        if (cardNumView != null && cardNumView instanceof CardNumberEditText) {

            card_input_widget.setCardNumberTextWatcher(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int charCount = editable.length();

                    if (charCount > 0 && ((CardNumberEditText) cardNumView).getCardBrand() != null) {
                        stCardImgView.setImageDrawable(getBrands(CardUtils.getPossibleCardType(editable.toString())));
                    } else {
                        stCardImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_default));
                    }
                }
            });
        }
    }


    public void checkDetails() {

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            if (card_input_widget.validateAllFields() == false) {
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_INVALID_CARD_DETAILS"));
                return;
            }

            generateStripeToken(card_input_widget.getCard());


            return;
        }
        Card card = new Card(Utils.getText(creditCardBox), generalFunc.parseIntegerValue(0, Utils.getText(mmBox)),
                generalFunc.parseIntegerValue(0, Utils.getText(yyBox)), Utils.getText(cvvBox));

        Logger.d("Card No", ":" + card.validateNumber() + "::num::" + card.getNumber());
        boolean isNameEntered = true;
        if (nameArea.getVisibility() == View.VISIBLE) {
            isNameEntered = Utils.checkText(nameOfCardBox) ? true : Utils.setErrorFields(nameOfCardBox, generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD"));
        }
        boolean cardNoEntered = Utils.checkText(creditCardBox) ? (card.validateNumber() ? true :
                Utils.setErrorFields(creditCardBox, generalFunc.retrieveLangLBl("", "LBL_INVALID")))
                : Utils.setErrorFields(creditCardBox, required_str);
        boolean cvvEntered = Utils.checkText(cvvBox) ? (card.validateCVC() ? true :
                Utils.setErrorFields(cvvBox, generalFunc.retrieveLangLBl("", "LBL_INVALID"))) : Utils.setErrorFields(cvvBox, required_str);
        boolean monthEntered = Utils.checkText(mmBox) ? (card.validateExpMonth() ? true :
                Utils.setErrorFields(mmBox, generalFunc.retrieveLangLBl("", "LBL_INVALID"))) : Utils.setErrorFields(mmBox, required_str);
        boolean yearEntered = Utils.checkText(yyBox) ? (validateExpYear(Calendar.getInstance()) ? true :
                Utils.setErrorFields(yyBox, generalFunc.retrieveLangLBl("", "LBL_INVALID"))) : Utils.setErrorFields(yyBox, required_str);
        boolean yearEntedcount = true;
        if (yearEntered == true) {
            yearEntedcount = yyBox.getText().toString().length() == 4 ? true : Utils.setErrorFields(yyBox, generalFunc.retrieveLangLBl("", "LBL_INVALID"));
        }


        if (isNameEntered == false || cardNoEntered == false || cvvEntered == false || monthEntered == false || yearEntered == false || yearEntedcount == false) {
            return;
        }


        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            if (card.validateCard()) {
                generateStripeToken(card);
            }
        }
    }


    public void generateXenditToken(final com.xendit.Models.Card card) {

        final MyProgressDialog myPDialog = showLoader();
        String XENDIT_PUBLIC_KEY = generalFunc.getJsonValue("XENDIT_PUBLIC_KEY", userProfileJson);

        if (card == null) {
            if (myPDialog != null) {
                myPDialog.close();
            }
            return;
        }


        final Xendit xendit = new Xendit(getActContext(), XENDIT_PUBLIC_KEY);

        xendit.createMultipleUseToken(card, new com.xendit.TokenCallback() {
            @Override
            public void onSuccess(com.xendit.Models.Token token) {

                myPDialog.close();
                CreateCustomer(Utils.maskCardNumber(card.getCreditCardNumber()), token.getId());

            }

            @Override
            public void onError(XenditError error) {
                myPDialog.close();

                generalFunc.showMessage(btn_type2, error.getErrorMessage());


            }
        });


    }

    public void generatePayMayaToken(final com.paymaya.sdk.android.payment.models.Card card) {

        new AsyncTask<String, String, PaymentToken>() {
            MyProgressDialog myPDialog = null;

            @Override
            protected PaymentToken doInBackground(String... strings) {
                String STRIPE_PUBLISH_KEY = generalFunc.getJsonValue("PAYMAYA_PUBLISH_KEY", userProfileJson);

                PayMayaPayment payMayaPayment = new PayMayaPayment(STRIPE_PUBLISH_KEY, card);
                PaymentToken paymentToken = payMayaPayment.getPaymentToken();

                return paymentToken;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                myPDialog = showLoader();
            }

            @Override
            protected void onPostExecute(PaymentToken paymentToken) {

                if (paymentToken != null) {
                    myPDialog.close();
                    CreateCustomer(Utils.maskCardNumber(card.getNumber()), paymentToken.getPaymentTokenId());
                } else {
                    closeProcessingMode();
                    myPDialog.close();
                    generalFunc.showError();


                }
            }

        }.execute();
    }

    public void setProcessingMode() {
        isInProcessMode = true;
        btn_type2.setText(generalFunc.retrieveLangLBl("Processing Payment", "LBL_PROCESS_PAYMENT_TXT"));
        creditCardBox.setEnabled(false);
        mmBox.setEnabled(false);
        yyBox.setEnabled(false);
        cvvBox.setEnabled(false);
        btn_type2.setEnabled(false);
    }


    public void closeProcessingMode() {
        try {
            isInProcessMode = false;
            if (getArguments().getString("PAGE_MODE").equals("ADD_CARD")) {
                btn_type2.setText(LBL_ADD_CARD);
            } else {
                btn_type2.setText(LBL_CHANGE_CARD);
            }
            creditCardBox.setEnabled(true);
            mmBox.setEnabled(true);
            yyBox.setEnabled(true);
            cvvBox.setEnabled(true);
            btn_type2.setEnabled(true);
        } catch (Exception e) {

        }
    }

    public MyProgressDialog showLoader() {
        MyProgressDialog myPDialog = new MyProgressDialog(getActContext(), false, generalFunc.retrieveLangLBl("Loading", "LBL_LOADING_TXT"));
        myPDialog.show();

        return myPDialog;
    }

    public void generateOmiseToken(String cardHolderName, String cardNumber, String expMonth, String expYear, String cvv) throws GeneralSecurityException {
        final MyProgressDialog myPDialog = showLoader();
        String OMISE_PUBLIC_KEY = generalFunc.getJsonValue("OMISE_PUBLIC_KEY", userProfileJson);
        Client client = new Client(OMISE_PUBLIC_KEY);

        TokenRequest request = new TokenRequest();
        request.number = cardNumber.replaceAll("\\s+", "");
        request.name = cardHolderName;
        request.expirationMonth = GeneralFunctions.parseIntegerValue(1, expMonth);
        request.expirationYear = GeneralFunctions.parseIntegerValue(Calendar.getInstance().get(Calendar.YEAR), expYear);
        request.securityCode = cvv;

        client.send(request, new TokenRequestListener() {

            @Override
            public void onTokenRequestSucceed(TokenRequest tokenRequest, co.omise.android.models.Token token) {
                myPDialog.close();
                CreateCustomer(Utils.maskCardNumber(cardNumber), token.id);
            }

            @Override
            public void onTokenRequestFailed(TokenRequest tokenRequest, Throwable throwable) {
                myPDialog.close();
            }
        });

    }

    public void generateStripeToken(final Card card) {

        final MyProgressDialog myPDialog = showLoader();

        String STRIPE_PUBLISH_KEY = generalFunc.getJsonValue("STRIPE_PUBLISH_KEY", userProfileJson);
        Stripe stripe = new Stripe(getActContext());

        stripe.createToken(card, STRIPE_PUBLISH_KEY, new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                myPDialog.close();
//                CreateCustomer(card, null, null, token.getId());
                CreateCustomer(Utils.maskCardNumber(card.getNumber()), token.getId());
            }

            public void onError(Exception error) {
                myPDialog.close();
                generalFunc.showError();
            }
        });
    }


    public void setScanData(CreditCard scanResult) {
        if (scanResult == null) {
            return;
        }

        String cardnumber = scanResult.getFormattedCardNumber();
        String expMonth = "" + scanResult.expiryMonth;
        String expYear = "" + scanResult.expiryYear;
        String cvc = "" + scanResult.cvv;
        String cardHolderName = scanResult.cardholderName == null ? "" : scanResult.cardholderName;

//        String cardnumber, String expMonth, String expYear, String cvc

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {

            String expYear_last = expYear.length() == 4 ? expYear.substring(2, 4) : expYear;

            View cardNumView = getStripeCardBox(com.stripe.android.R.id.et_add_source_card_number_ml);
            if (cardNumView != null && cardNumView instanceof CardNumberEditText) {
                ((CardNumberEditText) cardNumView).setText(cardnumber);
            }

            View expView = getStripeCardBox(com.stripe.android.R.id.et_add_source_expiry_ml);
            if (expView != null && expView instanceof ExpiryDateEditText) {
                ((ExpiryDateEditText) expView).setText((GeneralFunctions.parseIntegerValue(0, expMonth) < 10 ? ("0" + expMonth) : expMonth) + "/" + expYear_last);
            }

            View cvvNumView = getStripeCardBox(com.stripe.android.R.id.et_add_source_cvc_ml);
            if (cvvNumView != null && cvvNumView instanceof StripeEditText) {
                ((StripeEditText) cvvNumView).setText(cvc);
            }

        } else {
            if (nameArea.getVisibility() == View.VISIBLE) {
                nameOfCardBox.setText(cardHolderName);
            }
            creditCardBox.setText(cardnumber);
            mmBox.setText(expMonth);
            yyBox.setText(expYear);
            cvvBox.setText(cvc);
        }
    }

    public View getStripeCardBox(int id) {
        if (stripearea.getVisibility() == View.VISIBLE && card_input_widget.findViewById(id) != null) {

            return card_input_widget.findViewById(id);
        }
        return null;
    }

    public void CreateCustomer(String cardNum, String token) {
        if (cardPayAct.getIntent().hasExtra("isCheckout")) {
            addMoneyToWallet(token);
        } else {
            submitToken(cardNum, token);
        }

    }

    private void addMoneyToWallet(String token) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "addMoneyUserWalletByChargeCard");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("fAmount", cardPayAct.getIntent().getStringExtra("fAmount"));
        parameters.put("UserType", Utils.app_type);
        parameters.put("vStripeToken", token);
        parameters.put("eSystem", Utils.eSystem_Type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                if (responseString != null && !responseString.equals("")) {

                    boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                    if (isDataAvail == true) {

                        generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                        GenerateAlertBox generateAlertBox = new GenerateAlertBox(getActContext());
                        generateAlertBox.setCancelable(false);
                        generateAlertBox.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str_one, responseString)));

                        generateAlertBox.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_OK"));
                        generateAlertBox.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                            @Override
                            public void handleBtnClick(int btn_id) {
                                Intent returnIntent = new Intent();
                                cardPayAct.setResult(Activity.RESULT_OK, returnIntent);
                                cardPayAct.onBackPressed();
                                generateAlertBox.closeAlertBox();

                            }
                        });
                        generateAlertBox.showAlertBox();
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

    private void submitToken(String cardNum, String token) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GenerateCustomer");
        parameters.put("iUserId", generalFunc.getMemberId());

        parameters.put("CardNo", cardNum);

        if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
            parameters.put("vStripeToken", token);
        }

        parameters.put("UserType", Utils.app_type);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {


            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);


                if (isDataAvail == true) {

                    if (APP_PAYMENT_METHOD.equalsIgnoreCase("Stripe")) {
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                        cardPayAct.changeUserProfileJson(generalFunc.getJsonValue(Utils.message_str, responseString));
                    }
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


    public void setdata(int requestCode, int resultCode, Intent data) {

        if (requestCode == Utils.REQ_VERIFY_CARD_PIN_CODE && resultCode == cardPayAct.RESULT_OK && data != null) {

            UpdateCustomerToken((HashMap<String, Object>) data.getSerializableExtra("data"));
        }
    }

    private void UpdateCustomerToken(HashMap<String, Object> data) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "UpdateCustomerToken");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("vPaymayaToken", data.get("vPaymayaToken").toString());
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                    cardPayAct.changeUserProfileJson(generalFunc.getJsonValue(Utils.message_str, responseString));
                } else {
                    closeProcessingMode();
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                closeProcessingMode();
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public Context getActContext() {
        if (cardPayAct != null) {
            return cardPayAct.getActContext();
        }
        if (bookingSummaryActivity != null) {
            return bookingSummaryActivity.getActContext();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActContext());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0 && (s.length() % 5) == 0) {
            final char c = s.charAt(s.length() - 1);
            if (SPACE_CHAR == c) {
                s.delete(s.length() - 1, s.length());
            }
        }
        // Insert char where needed.
        if (s.length() > 0 && (s.length() % 5) == 0) {
            char c = s.charAt(s.length() - 1);
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(SPACE_CHAR)).length <= 3) {
                s.insert(s.length() - 1, String.valueOf(SPACE_CHAR));
            }
        }
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            int i = view.getId();
            if (i == btn_type2.getId()) {
                checkDetails();
            } else if (i == R.id.cardioview) {

                if (generalFunc.isCameraPermissionGranted() && Utils.isClassExist("io.card.payment.CardIOActivity")) {
                    Bundle bn = new Bundle();
                    bn.putBoolean(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
                    bn.putBoolean(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
                    bn.putBoolean(CardIOActivity.EXTRA_REQUIRE_CVV, true);

                    bn.putBoolean(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
                    bn.putBoolean(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
                    bn.putBoolean(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
                    bn.putBoolean(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
                    new StartActProcess(getActContext()).startActForResult(AddCardFragment.this, CardIOActivity.class, Utils.MY_SCAN_REQUEST_CODE, bn);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.MY_SCAN_REQUEST_CODE && data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {

            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            setScanData(scanResult);

            if (scanResult != null) {
                Logger.d("scanResult", "::" + scanResult.toString());
            }
        }
    }
}
