package com.adapter.files;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.melevicarbrasil.usuario.MultiDeliveryThirdPhaseActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.CustomLinearLayoutManager;
import com.general.files.GeneralFunctions;
import com.utils.Logger;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tarwindersingh on 06/01/18.
 */

public class MultiPaymentTypeRecyclerAdapter extends RecyclerView.Adapter<MultiPaymentTypeRecyclerAdapter.ViewHolder> implements MultiListViewAdapter.OnItemClickListener {

    GeneralFunctions generalFunc;
    Context mContext;
    MultiDeliveryThirdPhaseActivity mActivity;
    OnTypeSelectListener onTypeSelectListener;
    ArrayList<HashMap<String, String>> paymentTypeList;
    ArrayList<HashMap<String, String>> recipientList;
    int pos;
    private int lastCheckedPosition = -1;
    private int selectedRecipient = -1;
    MultiListViewAdapter adapter;

    String LBL_RECIPIENT_PAY_INST_TXT;

    public MultiPaymentTypeRecyclerAdapter(MultiDeliveryThirdPhaseActivity mActivity, GeneralFunctions generalFunc, ArrayList<HashMap<String, String>> recipientList, ArrayList<HashMap<String, String>> paymentTypeList, Context mContext) {
        this.generalFunc = generalFunc;
        this.paymentTypeList = paymentTypeList;
        this.recipientList = recipientList;
        this.mContext = mContext;
        this.mActivity = mActivity;
        LBL_RECIPIENT_PAY_INST_TXT =generalFunc.retrieveLangLBl("Each recipient has to pay", "LBL_RECIPIENT_PAY_INST_TXT");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_item_selected_payment_method, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    public void setOnTypeSelectListener(OnTypeSelectListener OnTypeSelectListener) {
        this.onTypeSelectListener = OnTypeSelectListener;
    }

    public void setlastCheckedPosition(int pos) {
        this.lastCheckedPosition = pos;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        HashMap<String, String> map = paymentTypeList.get(position);

        Logger.d("PAYMENT_DONE_BY", "::" + map.get("PaymentDoneBy") + "::" + position);
        holder.typeTitleTxt.setText(map.get("name"));

        String PaymentDoneBy = map.get("PaymentDoneBy");
        String Payment_Type_2 = mActivity.Payment_Type_2;

        if (position==1) {

            if (adapter == null) {
                RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(mActivity);
                holder.paymentDetailArea.setLayoutManager(layoutManager);
                adapter = new MultiListViewAdapter(mActivity, recipientList);
                holder.paymentDetailArea.setAdapter(adapter);
                adapter.setOnItemClickListener(this);
            }

        }


        holder.radioBtn.setChecked(position == lastCheckedPosition);

        if (holder.radioBtn.isChecked() && position==1) {
            holder.paymentDetailArea.setVisibility(View.VISIBLE);
        } else {
            holder.paymentDetailArea.setVisibility(View.GONE);
        }

        if (holder.radioBtn.isChecked() && position==2) {
            holder.typeInstTxt.setVisibility(View.VISIBLE);
            holder.typeInstTxt.setText("" + LBL_RECIPIENT_PAY_INST_TXT + " " + ((MultiDeliveryThirdPhaseActivity) mActivity).currencySymbol + (((MultiDeliveryThirdPhaseActivity) mActivity).individualFare) + " " + generalFunc.retrieveLangLBl("", "LBL_AMOUNT"));
        } else {
            holder.typeInstTxt.setVisibility(View.GONE);
        }

//        holder.card_view.setTag(position);
//        holder.card_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.radioBtn.performClick();
//            }
//        });

    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {

//        Logger.d("RecyclerItem", "paymentTypeList Size" + paymentTypeList.size());
        return paymentTypeList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onItemClickList(int parentPos, int position) {
        if (onTypeSelectListener != null) {
            selectedRecipient = position;
            onTypeSelectListener.onTypeSelect(parentPos, position);
        }
    }


    public interface OnTypeSelectListener {
        void onTypeSelect(int position, int recipientPos);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {


        private MTextView typeTitleTxt;
        private MTextView typeInstTxt;
        private LinearLayout mainArea;
        private RadioButton radioBtn;
        private View containerView;
        private RecyclerView paymentDetailArea;
        // private CardView card_view;

        public ViewHolder(View view) {
            super(view);

            containerView = view;
            typeTitleTxt = (MTextView) view.findViewById(R.id.typeTitleTxt);
            typeInstTxt = (MTextView) view.findViewById(R.id.typeInstTxt);
            radioBtn = (RadioButton) view.findViewById(R.id.radioBtn);
            mainArea = (LinearLayout) view.findViewById(R.id.mainArea);
            paymentDetailArea = (RecyclerView) view.findViewById(R.id.paymentDetailArea);
            //  card_view = (CardView) view.findViewById(R.id.card_view);

            mainArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioBtn.performClick();
                }
            });
            radioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    //because of this blinking problem occurs so
                    //i have a suggestion to add notifyDataSetChanged();
                    //   notifyItemRangeChanged(0, list.length);//blink list problem

                    if (onTypeSelectListener != null) {
                        onTypeSelectListener.onTypeSelect(lastCheckedPosition, selectedRecipient);
                    }

                    if (adapter != null) {
                        adapter.setSelectedParentPos(lastCheckedPosition);

                    }

                    notifyDataSetChanged();

                }
            });

        }
    }
}
