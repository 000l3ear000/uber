package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 08-03-18.
 */

public class HelpSubCategoryAdapter extends RecyclerView.Adapter<HelpSubCategoryAdapter.ViewHolder> {
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    Context mContext;
    OnItemClickList onItemClickList;

    public HelpSubCategoryAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
    }

    @Override
    public HelpSubCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_help_main_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, String> item = list_item.get(position);

        viewHolder.titleTxt.setText(item.get("vTitle"));

        viewHolder.titleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickList != null) {
                    onItemClickList.onItemClick(position);
                }
            }
        });

        if (position == (list_item.size() - 1)) {
            viewHolder.seperationLine.setVisibility(View.GONE);
        } else {
            viewHolder.seperationLine.setVisibility(View.GONE);
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
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView titleTxt;
        public View seperationLine;
        ImageView imagearrow;

        public ViewHolder(View view) {
            super(view);

            titleTxt = (MTextView) view.findViewById(R.id.titleTxt);
            seperationLine = view.findViewById(R.id.seperationLine);
            imagearrow = (ImageView) view.findViewById(R.id.imagearrow);
        }
    }

}
