package com.adapter.files;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.model.DeliveryIconDetails;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SubCategoryItemAdapter extends RecyclerView.Adapter<SubCategoryItemAdapter.ViewHolder> {

    GeneralFunctions generalFunc;
    ArrayList<DeliveryIconDetails.SubCatData> listData;
    Context mContext;
    setSubCategoryClickList setSubCategoryClickListobj;
    int width;

    public SubCategoryItemAdapter(GeneralFunctions generalFunc, ArrayList<DeliveryIconDetails.SubCatData> listData, Context mContext) {
        this.generalFunc = generalFunc;
        this.listData = listData;
        this.mContext = mContext;
        width = (int) ((Utils.getScreenPixelWidth(mContext) - Utils.dipToPixels(mContext, 16)) / 2);;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_layout, parent, false);

        SubCategoryItemAdapter.ViewHolder viewHolder = new SubCategoryItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DeliveryIconDetails.SubCatData data = listData.get(position);

        holder.boxTitleTxt.setText(data.getvSubCategory());
        holder.boxDescTxt.setText(data.gettSubCategoryDesc());
        Picasso.get()
                .load(data.getvImage())
                .into(holder.boxImage);
        holder.containerView.setOnClickListener(view -> setSubCategoryClickListobj.itemSubCategoryClick(position, data.geteDeliveryType(), data.getDataMap()));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.contentView.getLayoutParams();

        params.width = width;

        holder.contentView.setLayoutParams(params);

        if (generalFunc.isRTLmode()){
            holder.arrow_right.setRotation(180);
            holder.arrowlayout.setBackground(mContext.getResources().getDrawable(R.drawable.roundcolorectsmallright));
        }else {
            holder.arrow_right.setRotation(0);
            holder.arrowlayout.setBackground(mContext.getResources().getDrawable(R.drawable.roundcolorectsmall));
        }
    }


    public void setOnClickList(setSubCategoryClickList setSubCategoryClickListobj) {
        this.setSubCategoryClickListobj = setSubCategoryClickListobj;
    }

    public interface setSubCategoryClickList {
        void itemSubCategoryClick(int position, String eDeliveryType, HashMap<String, String> dataMap);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {


        private MTextView boxTitleTxt;
        private MTextView boxDescTxt;
        private ImageView boxImage;
        private View containerView;
        private View contentView;
        private RelativeLayout arrowlayout;
        private ImageView arrow_right;


        public ViewHolder(View view) {
            super(view);

            containerView = view;
            contentView = view.findViewById(R.id.contentView);
            boxTitleTxt = (MTextView) view.findViewById(R.id.boxTitleTxt);
            boxDescTxt = (MTextView) view.findViewById(R.id.boxDescTxt);
            boxImage = (ImageView) view.findViewById(R.id.boxImage);
            arrowlayout = (RelativeLayout) view.findViewById(R.id.arrowlayout);
            arrow_right = (ImageView) view.findViewById(R.id.arrow_right);

        }
    }
}
