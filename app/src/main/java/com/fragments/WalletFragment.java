package com.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adapter.files.WalletHistoryRecycleAdapter;
import com.melevicarbrasil.usuario.MyWalletHistoryActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 27-05-2017.
 */

public class WalletFragment extends Fragment {

    View view;
    GeneralFunctions generalFunc;

    MyWalletHistoryActivity myWalletAct;

    ProgressBar loading_transaction_history;
    MTextView noTransactionTxt;
    MTextView transactionsTxt;

    RecyclerView walletHistoryRecyclerView;
    ErrorView errorView;

    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;

    String next_page_str = "";

    private WalletHistoryRecycleAdapter wallethistoryRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wallet, container, false);

        myWalletAct = (MyWalletHistoryActivity) getActivity();
        generalFunc = myWalletAct.generalFunc;
        loading_transaction_history = (ProgressBar) view.findViewById(R.id.loading_transaction_history);
        noTransactionTxt = (MTextView) view.findViewById(R.id.noTransactionTxt);
        transactionsTxt = (MTextView) view.findViewById(R.id.transactionsTxt);
        walletHistoryRecyclerView = (RecyclerView) view.findViewById(R.id.walletTransactionRecyclerView);
        errorView = (ErrorView) view.findViewById(R.id.errorView);

        list = new ArrayList<>();
        wallethistoryRecyclerAdapter = new WalletHistoryRecycleAdapter(getActContext(), list, generalFunc, false);
        walletHistoryRecyclerView.setAdapter(wallethistoryRecyclerAdapter);

        walletHistoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                int lastInScreen = firstVisibleItemPosition + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable == true) {

                    mIsLoading = true;
                    wallethistoryRecyclerAdapter.addFooterView();

                    getTransactionHistory(true);

                } else if (isNextPageAvailable == false) {
                    wallethistoryRecyclerAdapter.removeFooterView();
                }
            }
        });

        getTransactionHistory(false);
        return view;
    }


    public Context getActContext() {
        return myWalletAct.getActContext();
    }


    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        wallethistoryRecyclerAdapter.removeFooterView();

    }

    public void closeLoader() {
        if (loading_transaction_history.getVisibility() == View.VISIBLE) {
            loading_transaction_history.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getTransactionHistory(false));
    }

    public void getTransactionHistory(final boolean isLoadMore) {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading_transaction_history.getVisibility() != View.VISIBLE && isLoadMore == false) {
            if (list.size() == 0) {
                loading_transaction_history.setVisibility(View.VISIBLE);
            }
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getTransactionHistory");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("ListType", getArguments().getString("ListType"));
        if (isLoadMore == true) {
            parameters.put("page", next_page_str);
        }

        noTransactionTxt.setVisibility(View.GONE);

        final ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setDataResponseListener(responseString -> {

            noTransactionTxt.setVisibility(View.GONE);
            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {

                    String nextPage = generalFunc.getJsonValue("NextPage", responseString);
                    JSONArray arr_transhistory = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_transhistory != null && arr_transhistory.length() > 0) {
                        for (int i = 0; i < arr_transhistory.length(); i++) {
                            //   for (int i = 0; i < 10; i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_transhistory, i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("iUserWalletId", generalFunc.getJsonValueStr("iUserWalletId", obj_temp));
                            map.put("iUserId", generalFunc.getJsonValueStr("iUserId", obj_temp));
                            map.put("eUserType", generalFunc.getJsonValueStr("eUserType", obj_temp));
                            map.put("eType", generalFunc.getJsonValueStr("eType", obj_temp));
                            map.put("iTripId", generalFunc.getJsonValueStr("iTripId", obj_temp));
                            map.put("eFor", generalFunc.getJsonValueStr("eFor", obj_temp));
                            String tDescription=generalFunc.getJsonValueStr("tDescription", obj_temp);
                            map.put("tDescription", tDescription);
                            map.put("tDescriptionConverted", generalFunc.convertNumberWithRTL(tDescription));
                            map.put("ePaymentStatus", generalFunc.getJsonValueStr("ePaymentStatus", obj_temp));
                            map.put("currentbal", generalFunc.getJsonValueStr("currentbal", obj_temp));
                            map.put("LBL_Status", generalFunc.retrieveLangLBl("", "LBL_Status"));
                            map.put("LBL_TRIP_NO", generalFunc.retrieveLangLBl("", "LBL_TRIP_NO"));
                            map.put("LBL_BALANCE_TYPE", generalFunc.retrieveLangLBl("", "LBL_BALANCE_TYPE"));
                            map.put("LBL_DESCRIPTION", generalFunc.retrieveLangLBl("", "LBL_DESCRIPTION"));
                            map.put("LBL_AMOUNT", generalFunc.retrieveLangLBl("", "LBL_AMOUNT"));

                            String dDateOrig = generalFunc.getJsonValueStr("dDateOrig", obj_temp);
                            map.put("dDateOrig", dDateOrig);
                            map.put("listingFormattedDate", generalFunc.convertNumberWithRTL(generalFunc.getDateFormatedType(dDateOrig, Utils.OriginalDateFormate, CommonUtilities.OriginalDateFormate)));

                            String iBalance = generalFunc.getJsonValueStr("iBalance", obj_temp);
                            map.put("iBalance", iBalance);
                            map.put("FormattediBalance", generalFunc.convertNumberWithRTL(iBalance));


                            list.add(map);
                        }
                    }

                    String LBL_BALANCE = generalFunc.getJsonValue("user_available_balance", responseString);

                    ((MTextView) view.findViewById(R.id.yourBalTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_USER_BALANCE"));


                    ((MTextView) view.findViewById(R.id.walletamountTxt)).setText(LBL_BALANCE);


                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }

                    wallethistoryRecyclerAdapter.notifyDataSetChanged();
                } else {
                    if (list.size() == 0) {
                        removeNextPageConfig();
                        noTransactionTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        noTransactionTxt.setVisibility(View.VISIBLE);
                    }
                }

                wallethistoryRecyclerAdapter.notifyDataSetChanged();


            } else {
                if (isLoadMore == false) {
                    removeNextPageConfig();
                    generateErrorView();
                }

            }

            mIsLoading = false;
        });

        if (!isLoadMore) {
            if (list.size() == 0) {
                exeWebServer.execute();
            }

        } else {
            exeWebServer.execute();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActivity());
    }
}
