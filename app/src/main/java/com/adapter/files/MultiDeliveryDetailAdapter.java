package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.model.Delivery_Data;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;

/**
 * Created by Esite on 14-04-2018.
 */

public class MultiDeliveryDetailAdapter extends RecyclerView.Adapter<MultiDeliveryDetailAdapter.ListItemViewHolder> {

    private ArrayList<Delivery_Data> recipientList;
    private Context activity;

    public MultiDeliveryDetailAdapter(Context mActivity, ArrayList<Delivery_Data> recipientList) {

        this.activity = mActivity;
        if (recipientList == null) {
            throw new IllegalArgumentException(
                    "RecipientList must not be null");
        }
        this.recipientList = recipientList;
    }


    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.multi_delivery_details_design,
                        viewGroup,
                        false);
        return new ListItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        Delivery_Data item=recipientList.get(position);
        holder.itemDetailArea.setTag(position); // This line is important.
        holder.itemTitleTxt.setText(item.getvFieldName());
        holder.itemValueTxt.setText(Utils.checkText(item.getvValue()) ? item.getvValue() : "--");

        if (position == recipientList.size() - 1) {
            holder.bottomLine.setVisibility(View.GONE);
        } else {
            holder.bottomLine.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener {
        void onItemClickList(int parentPos, int position);
    }


    @Override
    public int getItemCount() {
        return recipientList.size();
    }


    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        private MTextView itemTitleTxt, itemValueTxt;
        private LinearLayout itemDetailArea;
        private View bottomLine;

        public ListItemViewHolder(View v) {
            super(v);
            itemTitleTxt = (MTextView) v.findViewById(R.id.itemTitleTxt);
            itemDetailArea = (LinearLayout) v.findViewById(R.id.itemDetailArea);
            itemValueTxt = (MTextView) v.findViewById(R.id.itemValueTxt);
            bottomLine = (View) v.findViewById(R.id.bottomLine);
        }
    }
}