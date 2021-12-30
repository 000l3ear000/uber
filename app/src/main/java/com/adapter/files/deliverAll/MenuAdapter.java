package com.adapter.files.deliverAll;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.deliverAll.RestaurantAllDetailsNewActivity;
import com.general.files.GeneralFunctions;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<HashMap<String, String>> list;
    Context mContext;
    GeneralFunctions generalFunc;
    private OnItemClickListener mItemClickListener;


    RestaurantAllDetailsNewActivity restAllDetailsNewAct;


    boolean isRTLmode = false;

    public MenuAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;

        if (mContext instanceof RestaurantAllDetailsNewActivity) {
            restAllDetailsNewAct = (RestaurantAllDetailsNewActivity) mContext;
        }


        isRTLmode = generalFunc.isRTLmode();
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        GridViewHolder gridViewGolder = new GridViewHolder(view);
        return gridViewGolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HashMap<String, String> mapData = list.get(position);

        GridViewHolder grideholder = (GridViewHolder) holder;
        if (mapData.get("isSelect").equalsIgnoreCase("Yes")) {
            grideholder.nameTxt.setVisibility(View.GONE);
            grideholder.SelnameTxt.setVisibility(View.VISIBLE);
        } else {
            grideholder.nameTxt.setVisibility(View.VISIBLE);
            grideholder.SelnameTxt.setVisibility(View.GONE);
        }
        grideholder.nameTxt.setText(mapData.get("name"));
        grideholder.SelnameTxt.setText(mapData.get("name"));


        grideholder.nameTxt.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onMenuItemClickList(grideholder.nameTxt, position);
            }
        });
        grideholder.SelnameTxt.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onMenuItemClickList(grideholder.nameTxt, position);
            }
        });



    }


    public class GridViewHolder extends RecyclerView.ViewHolder {


        MTextView nameTxt;
        MTextView SelnameTxt;


        public GridViewHolder(View view) {
            super(view);
            nameTxt = (MTextView) view.findViewById(R.id.nameTxt);
            SelnameTxt = (MTextView) view.findViewById(R.id.SelnameTxt);

        }
    }


    public interface OnItemClickListener {
        void onMenuItemClickList(View v, int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
