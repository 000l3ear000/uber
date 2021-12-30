package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 06-09-2016.
 */
public class EmergencyContactRecycleAdapter extends RecyclerView.Adapter<EmergencyContactRecycleAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> list_item;
    Context mContext;

    OnItemClickList onItemClickList;
    GeneralFunctions generalFunctions;

    public EmergencyContactRecycleAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item) {
        this.mContext = mContext;
        this.list_item = list_item;
        generalFunctions=new GeneralFunctions(mContext);
    }

    @Override
    public EmergencyContactRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_contact_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, String> item = list_item.get(position);

        viewHolder.contactName.setText(item.get("ContactName"));
        viewHolder.contactPhone.setText(item.get("ContactPhone"));



        if (generalFunctions.isRTLmode()) {
            viewHolder.layoutShape.setBackgroundResource(R.drawable.ic_shape_rtl);
        }

        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickList != null) {
                    onItemClickList.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView contactName;
        public MTextView contactPhone;
        public RelativeLayout img_delete;
        public     LinearLayout layoutShape;

        public ViewHolder(View view) {
            super(view);

            contactName = (MTextView) view.findViewById(R.id.contactName);
            contactPhone = (MTextView) view.findViewById(R.id.contactPhone);
            img_delete = (RelativeLayout) view.findViewById(R.id.img_delete);
            layoutShape=  (LinearLayout) view.findViewById(R.id.layoutShape);
        }
    }
}
