package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-07-2016.
 */
public class HelpCategoryRecycleAdapter extends RecyclerView.Adapter<HelpCategoryRecycleAdapter.ViewHolder> {

    private final int size40,size45;
    private final float text12,text15;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    Context mContext;
    OnItemClickList onItemClickList;
    Typeface font1;
    Typeface font2;
    public HelpCategoryRecycleAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
        font1 = Typeface.createFromAsset(
                mContext.getAssets(),
                "fonts/Poppins_SemiBold.ttf");
        font2 = Typeface.createFromAsset(
                mContext.getAssets(),
                "fonts/Poppins_Regular.ttf");

        size40=(int) mContext.getResources().getDimension(R.dimen._40sdp);
        size45=(int)mContext.getResources().getDimensionPixelSize(R.dimen._45sdp);
        text12=mContext.getResources().getDimension(R.dimen._12ssp);
        text15=mContext.getResources().getDimension(R.dimen._15ssp);
    }

    @Override
    public HelpCategoryRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_category_help, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, String> item = list_item.get(position);

        if (item.containsKey("vTitle")){

            viewHolder.titleTxt.setText(item.get("vTitle"));
            viewHolder.titleTxt.setTypeface(font1);
            viewHolder.titleTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, text15);
            viewHolder.titleTxt.setTextColor(Color.parseColor("#000000"));
            viewHolder.imagearrow.setVisibility(View.GONE);
            viewHolder.layoutBackground.setBackgroundColor(Color.parseColor("#f1f1f1"));
            viewHolder.titleTxt.setClickable(false);
            viewHolder.layoutBackground.setMinimumHeight(size40);
        }else {
            viewHolder.titleTxt.setText(item.get("vSubTitle"));
            viewHolder.titleTxt.setTypeface(font2);
            viewHolder.titleTxt.setTextColor(Color.parseColor("#141414"));
            viewHolder.titleTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX,text12 );
            viewHolder.imagearrow.setVisibility(View.VISIBLE);
            viewHolder.layoutBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.layoutBackground.setMinimumHeight(size45);
            viewHolder.titleTxt.setClickable(true);
            viewHolder.titleTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickList != null) {
                        onItemClickList.onItemClick(position,item.get("vSubTitle"),item.get("tAnswer"));
                    }
                }
            });
        }



        if (position == (list_item.size() - 1)) {
           // viewHolder.seperationLine.setVisibility(View.GONE);
        } else {
           // viewHolder.seperationLine.setVisibility(View.GONE);
        }
        String LANGUAGE_IS_RTL_KEY =generalFunc.retrieveValue(Utils.LANGUAGE_IS_RTL_KEY);

        if (!LANGUAGE_IS_RTL_KEY.equals("") && LANGUAGE_IS_RTL_KEY.equals(Utils.DATABASE_RTL_STR)) {

            viewHolder.imagearrow.setRotation(-270);
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
        void onItemClick(int position,String question,String answer);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView titleTxt;
        public View seperationLine;
        ImageView imagearrow;
        LinearLayout layoutBackground;
        public ViewHolder(View view) {
            super(view);
            layoutBackground = (LinearLayout) view.findViewById(R.id.layoutBackground);
            titleTxt = (MTextView) view.findViewById(R.id.titleTxt);
            seperationLine = view.findViewById(R.id.seperationLine);
            imagearrow = (ImageView) view.findViewById(R.id.imagearrow);
        }
    }

}
