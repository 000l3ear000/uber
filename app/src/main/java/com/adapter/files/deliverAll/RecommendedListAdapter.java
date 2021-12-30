package com.adapter.files.deliverAll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melevicarbrasil.usuario.R;
import com.general.files.AppFunctions;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utilities;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecommendedListAdapter extends RecyclerView.Adapter<RecommendedListAdapter.DataViewHolder> {

    ArrayList<HashMap<String, String>> list;
    Context mContext;
    GeneralFunctions generalFunc;
    private OnClickListener onClickListener;

    public RecommendedListAdapter(ArrayList<HashMap<String, String>> list, Context mContext, GeneralFunctions generalFunc) {
        this.list = list;
        this.mContext = mContext;
        this.generalFunc = generalFunc;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommended_item_list_design, viewGroup, false);
        return new DataViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        HashMap<String, String> mapData = list.get(position);

        holder.title.setText(mapData.get("vItemType"));
        holder.desc.setText(mapData.get("vItemDesc"));

        int parentWidth = new AppFunctions(mContext).getViewHeightWidth(holder.parent, false);
        String vImage = Utilities.getResizeImgURL(mContext, mapData.get("vImage"), parentWidth, 0, 0);

        Picasso.get()
                .load(vImage)
                .placeholder(R.mipmap.ic_no_icon)
                .error(R.mipmap.ic_no_icon)
                .into(holder.itemImageView);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.itemImageView.getLayoutParams();
        params.width = parentWidth;
        holder.itemImageView.requestLayout();

        String eFoodType = mapData.get("eFoodType");

        if (eFoodType.equalsIgnoreCase("NonVeg")) {
            holder.nonVegImage.setVisibility(View.VISIBLE);
            holder.vegImage.setVisibility(View.GONE);
        } else if (eFoodType.equals("Veg")) {
            holder.nonVegImage.setVisibility(View.GONE);
            holder.vegImage.setVisibility(View.VISIBLE);
        }

        if (mapData.get("prescription_required").equalsIgnoreCase("Yes")) {
            holder.presImage.setVisibility(View.VISIBLE);
        } else {
            holder.presImage.setVisibility(View.GONE);
        }

        if (mapData.get("fOfferAmtNotZero").equalsIgnoreCase("Yes")) {
            holder.price.setText(mapData.get("StrikeoutPriceConverted"));
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.price.setTextColor(mContext.getResources().getColor(R.color.gray));
            holder.offerPrice.setText(mapData.get("fDiscountPricewithsymbolConverted"));
            holder.offerPrice.setVisibility(View.VISIBLE);
        } else {
            holder.price.setText(mapData.get("StrikeoutPriceConverted"));
            holder.price.setPaintFlags(0);
            holder.offerPrice.setVisibility(View.GONE);
        }

        String vHighlightName = mapData.get("vHighlightName");
        if (vHighlightName != null && !vHighlightName.equals("")) {
            holder.tagArea.setVisibility(View.VISIBLE);
            holder.tagTxt.setText(vHighlightName);
        } else {
            holder.tagArea.setVisibility(View.GONE);
        }

        holder.addBtn.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onItemClick(holder.addBtn, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImageView, presImage, vegImage, nonVegImage;
        MTextView title, addBtn, desc, price, offerPrice, tagTxt;
        LinearLayout tagArea;
        RelativeLayout parent;

        DataViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            presImage = itemView.findViewById(R.id.presImage);
            vegImage = itemView.findViewById(R.id.vegImage);
            nonVegImage = itemView.findViewById(R.id.nonVegImage);
            title = itemView.findViewById(R.id.title);
            addBtn = itemView.findViewById(R.id.addBtn);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            offerPrice = itemView.findViewById(R.id.offerPrice);
            tagArea = itemView.findViewById(R.id.tagArea);
            tagTxt = itemView.findViewById(R.id.tagTxt);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public interface OnClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }
}
