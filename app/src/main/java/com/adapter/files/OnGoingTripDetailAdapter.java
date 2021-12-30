package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 22-02-2017.
 */
public class OnGoingTripDetailAdapter extends RecyclerView.Adapter<OnGoingTripDetailAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    Context mContext;
    OnItemClickList onItemClickList;
    int appThemecolor1,size60,whitecolor,disabledColor;
    public OnGoingTripDetailAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
        appThemecolor1=mContext.getResources().getColor(R.color.appThemeColor_1);
        whitecolor=ContextCompat.getColor(mContext, R.color.white);
        size60=Utils.dipToPixels(mContext, 60);
        disabledColor=Color.parseColor("#141414");

    }

    @Override
    public OnGoingTripDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_ongoing_trip_cell, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, String> item = list_item.get(position);
        viewHolder.tripStatusTxt.setText(item.get("msg"));
        String time=item.get("time");
        viewHolder.tripStatusTimeTxt.setText(generalFunc.convertNumberWithRTL(time));
        viewHolder.tripTimeTxt.setText(time);
        viewHolder.tripTimeTxt.setVisibility(View.GONE);

        Drawable mDrawable = null;

        if (item.get("eType").equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
            viewHolder.noTxt.setText("" + (position + 1));
            viewHolder.noTxt.setVisibility(View.VISIBLE);
        } else {

            String status = item.get("status");
            if (status.equalsIgnoreCase("Accept")) {
                mDrawable = mContext.getResources().getDrawable(R.drawable.ic_one_nobg);
            } else if (status.equalsIgnoreCase("Arrived")) {
                mDrawable = mContext.getResources().getDrawable(R.drawable.ic_two_nobg);
            } else if (status.equalsIgnoreCase("Onway")) {
                mDrawable = mContext.getResources().getDrawable(R.drawable.ic_three_nobg);
            } else if (status.equalsIgnoreCase("Delivered")) {
                mDrawable = mContext.getResources().getDrawable(R.drawable.ic_four_nobg);
            } else if (status.equalsIgnoreCase("Cancelled")) {
                mDrawable = mContext.getResources().getDrawable(R.drawable.ic_five_nobg);
            } else if (status.equalsIgnoreCase("On the way")) {
                mDrawable = mContext.getResources().getDrawable(R.drawable.ic_five_nobg);
            }
        }

        if (position==list_item.size()-1){
            new CreateRoundedView(appThemecolor1, size60, 0, appThemecolor1, viewHolder.driverImgView);
            viewHolder.driverImgView.setBorderColor(appThemecolor1);
            //mDrawable.setColorFilter(new PorterDuffColorFilter(mContext.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY));
            if (mDrawable!=null) {
                mDrawable.setColorFilter(whitecolor, android.graphics.PorterDuff.Mode.SRC_IN);
            }
            viewHolder.noTxt.setTextColor(whitecolor);
            viewHolder.tripStatusTxt.setTextColor(appThemecolor1);
            viewHolder.tripStatusTimeTxt.setTextColor(appThemecolor1);
            viewHolder.tripTimeTxt.setTextColor(appThemecolor1);



            if (position==0){
                viewHolder.topView.setVisibility(View.INVISIBLE);
                viewHolder.bottomView.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.bottomView.setVisibility(View.INVISIBLE);
                viewHolder.topView.setVisibility(View.VISIBLE);
            }

        }else {
            new CreateRoundedView(Color.parseColor("#e0e0e0"), size60, 0,
                    Color.parseColor("#787878"), viewHolder.driverImgView);
            viewHolder.driverImgView.setBorderColor(Color.parseColor("#949292"));
            //mDrawable.setColorFilter(new PorterDuffColorFilter(mContext.getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY));
            if (mDrawable!=null) {
                mDrawable.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            viewHolder.noTxt.setTextColor(disabledColor);
            viewHolder.tripStatusTxt.setTextColor(disabledColor);
            viewHolder.tripStatusTimeTxt.setTextColor(disabledColor);
            viewHolder.tripTimeTxt.setTextColor(disabledColor);
            viewHolder.bottomView.setVisibility(View.VISIBLE);
            if (position==0){
                viewHolder.topView.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.topView.setVisibility(View.VISIBLE);
            }


        }

        if (mDrawable != null) {

            viewHolder.driverImgView.setImageDrawable(mDrawable);

        }
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

        public MTextView tripStatusTxt;
        public MTextView tripStatusTimeTxt;
        public MTextView tripTimeTxt;
        public MTextView noTxt;
        public SelectableRoundedImageView driverImgView;
        public View topView,bottomView;
        public ViewHolder(View view) {
            super(view);

            tripStatusTxt = (MTextView) view.findViewById(R.id.tripStatusTxt);
            tripStatusTimeTxt = (MTextView) view.findViewById(R.id.tripStatusTimeTxt);
            tripTimeTxt = (MTextView) view.findViewById(R.id.tripTimeTxt);
            noTxt = (MTextView) view.findViewById(R.id.noTxt);
            driverImgView = (SelectableRoundedImageView) view.findViewById(R.id.driverImgView);
            topView = (View) view.findViewById(R.id.topView);
            bottomView = (View) view.findViewById(R.id.bottomView);
        }
    }

}
