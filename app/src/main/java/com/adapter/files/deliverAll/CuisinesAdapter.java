package com.adapter.files.deliverAll;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utilities;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CuisinesAdapter extends RecyclerView.Adapter<CuisinesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HashMap<String, String>> list;
    GeneralFunctions generalFunc;
    CuisinesOnClickListener CuisinesOnClickListener;

    public CuisinesAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, CuisinesOnClickListener CuisinesOnClickListener) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.CuisinesOnClickListener = CuisinesOnClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuisines, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.cuisinesTxt.setText(list.get(position).get("cuisineName"));




        if (list.get(position).containsKey("isCheck")) {
            if (list.get(position).get("isCheck").equalsIgnoreCase("Yes")) {
                holder.selitemView.setVisibility(View.VISIBLE);
            } else {
                holder.selitemView.setVisibility(View.GONE);
            }
        } else {
            holder.selitemView.setVisibility(View.GONE);
        }

        if (list.get(position).get("vImage") != null) {
            String imageUrl = list.get(position).get("vImage");
            if (list.get(position).get("vImage").equalsIgnoreCase("")) {
                imageUrl = "Temp";
            }

            Picasso.get()
                    .load(Utilities.getResizeImgURL(mContext, imageUrl, holder.cuisinesImg.getWidth(), holder.cuisinesImg.getHeight()))
                    .placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon)
                    .into(holder.cuisinesImg, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e){
                        }
                    });

        }


        holder.mainArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CuisinesOnClickListener.setOnCuisinesclick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface CuisinesOnClickListener {
        void setOnCuisinesclick(int position);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        MTextView cuisinesTxt;

        ImageView cuisinesImg;
        LinearLayout mainArea;
        View selitemView;


        public ViewHolder(View itemView) {
            super(itemView);

            cuisinesTxt = itemView.findViewById(R.id.cuisinesTxt);
            cuisinesImg = itemView.findViewById(R.id.cuisinesImg);
            mainArea = itemView.findViewById(R.id.mainArea);
            selitemView = itemView.findViewById(R.id.itemView);
        }
    }
}
