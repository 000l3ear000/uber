package com.adapter.files;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.melevicarbrasil.usuario.MultiDeliveryThirdPhaseActivity;
import com.melevicarbrasil.usuario.R;
import com.view.MTextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Esite on 14-04-2018.
 */

public class MultiListViewAdapter extends RecyclerView.Adapter<MultiListViewAdapter.ListItemViewHolder> {

    private List<HashMap<String, String>> recipientList;
    private Activity activity;
    public int selectedPosition = -1;
    public int lastCheckedPos = -1;
    private OnItemClickListener mItemClickListener;


    public MultiListViewAdapter(MultiDeliveryThirdPhaseActivity mActivity, List<HashMap<String, String>> recipientList) {

        this.activity = mActivity;
        if (recipientList == null) {
            throw new IllegalArgumentException(
                    "PrescriptionProductList must not be null");
        }
        this.recipientList = recipientList;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setSelectedParentPos(int lastCheckedPos) {
        this.lastCheckedPos = lastCheckedPos;
    }


    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickList(lastCheckedPos, selectedPosition);
                }

                notifyDataSetChanged();
            }
        };
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.multi_list_item,
                        viewGroup,
                        false);
        return new ListItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        holder.lstcheckBox.setTag(position); // This line is important.
        holder.recipientName.setText(recipientList.get(position).get("name"));
        if (position == selectedPosition) {
            holder.lstcheckBox.setChecked(true);
        } else holder.lstcheckBox.setChecked(false);

        holder.lstcheckBox.setOnClickListener(onStateChangedListener(holder.lstcheckBox, position));

    }

    public interface OnItemClickListener {
        void onItemClickList(int parentPos, int position);
    }


    @Override
    public int getItemCount() {
        return recipientList.size();
    }


    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        private MTextView recipientName;
        private CheckBox lstcheckBox;

        public ListItemViewHolder(View v) {
            super(v);
            lstcheckBox = (CheckBox) v.findViewById(R.id.lstcheckBox);
            recipientName = (MTextView) v.findViewById(R.id.lblListItem);
        }
    }
}