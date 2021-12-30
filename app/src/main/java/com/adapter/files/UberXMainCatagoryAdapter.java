package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.squareup.picasso.Picasso;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class UberXMainCatagoryAdapter extends RecyclerView.Adapter<UberXMainCatagoryAdapter.ViewHolder> {

    Context mContext;
    ItemClickListener itemClickList;
    private ArrayList<HashMap<String, String>> catagoryDataList;

    public UberXMainCatagoryAdapter(Context context, ArrayList<HashMap<String, String>> catagoryDataList) {

        mContext = context;
        this.catagoryDataList = catagoryDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_uberx_main_catagory_design, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        HashMap<String, String> map=catagoryDataList.get(position);

        viewHolder.uberXcategoryNameTxtView.setText(map.get("vCategory"));
        Picasso.get().load(map.get("vLogo_image")).into(viewHolder.uberXcategoryImgView);

        viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemClickList != null) {
                    itemClickList.onClick(position, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return catagoryDataList.size();
    }

    public void setItemClickList(ItemClickListener itemClickList) {
        this.itemClickList = itemClickList;
    }

    public interface ItemClickListener {
        void onClick(int position, int btnId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView uberXcategoryImgView;
        public MTextView uberXcategoryNameTxtView;
        public LinearLayout contentArea;

        public ViewHolder(View itemView) {
            super(itemView);
            uberXcategoryImgView = (ImageView) itemView.findViewById(R.id.uberXcategoryImgView);
            uberXcategoryNameTxtView = (MTextView) itemView.findViewById(R.id.uberXcategoryNameTxtView);
            contentArea = (LinearLayout) itemView.findViewById(R.id.contentArea);
        }
    }
}