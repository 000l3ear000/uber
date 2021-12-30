package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 02-03-2017.
 */
public class UberXMoreCategoryAdapter extends RecyclerView.Adapter<UberXMoreCategoryAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    Context mContext;
    OnMoreItemClickList onItemClickList;

    String CAT_TYPE_MODE = "0";

    public UberXMoreCategoryAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
    }

    @Override
    public UberXMoreCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(CAT_TYPE_MODE.equals("0") ? R.layout.item_uberx_cat_grid_design : R.layout.item_uberx_cat_list_design, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setCategoryMode(String CAT_TYPE_MODE) {
        this.CAT_TYPE_MODE = CAT_TYPE_MODE;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, String> item = list_item.get(position);
        String vLogo_image = item.get("vLogo_image");
        String vCategory = item.get("vCategory");


        if (vCategory.matches("\\w*")) {
            viewHolder.uberXCatNameTxtView.setMaxLines(1);
            viewHolder.uberXCatNameTxtView.setText(vCategory);
        } else {
            viewHolder.uberXCatNameTxtView.setMaxLines(2);
            viewHolder.uberXCatNameTxtView.setText(vCategory);
        }

        Picasso.get().load(vLogo_image.equals("") ? CommonUtilities.SERVER : vLogo_image).placeholder(R.mipmap.ic_no_icon).into(viewHolder.catImgView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e){

                if (!vLogo_image.contains("http") && !vLogo_image.equals("")) {
                    Picasso.get().load(GeneralFunctions.parseIntegerValue(0, vLogo_image)).placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon).into(viewHolder.catImgView);
                }
            }
        });

        viewHolder.contentArea.setOnClickListener(view -> {
            if (onItemClickList != null) {
                onItemClickList.onMoreItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void setOnItemClickList(OnMoreItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnMoreItemClickList {
        void onMoreItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView uberXCatNameTxtView;
        public View contentArea;
        public ImageView catImgView;

        public ViewHolder(View view) {
            super(view);

            uberXCatNameTxtView = (MTextView) view.findViewById(R.id.uberXCatNameTxtView);

            contentArea = view.findViewById(R.id.contentArea);
            catImgView = (ImageView) view.findViewById(R.id.catImgView);
        }
    }


}
