package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 06-07-2016.
 */
public class RequestPickUpAdapter extends RecyclerView.Adapter<RequestPickUpAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    Context mContext;
    OnItemClickList onItemClickList;

    boolean isFirstRun = true;
    int color,color1,icon;

    public RequestPickUpAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
        color=mContext.getResources().getColor(R.color.appThemeColor_1);
        color1=Color.parseColor("#FFFFFF");
        icon=R.mipmap.ic_launcher;
    }

    @Override
    public RequestPickUpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design_request_pick_up, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (position == 0 && isFirstRun == true) {
            setData(viewHolder, position, true);
            isFirstRun = false;
        } else {
            setData(viewHolder, position, false);
        }
    }

    public void setData(RequestPickUpAdapter.ViewHolder viewHolder, final int position, boolean isHover) {

        HashMap<String, String> item = list_item.get(position);
        viewHolder.txt.setText(item.get("Title").replace(" ", "\n"));


        viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (onItemClickList != null) {
//                    onItemClickList.onItemClick(position);
//                }
                performClick(position);
            }
        });

        if (isHover == true) {
            viewHolder.img.setImageResource(generalFunc.parseIntegerValue(icon, item.get("IconHover")));
            viewHolder.img.setBorderColor(color);
            viewHolder.txt.setTextColor(color);
        } else {
            viewHolder.img.setImageResource(generalFunc.parseIntegerValue(icon, item.get("Icon")));
            viewHolder.img.setBorderColor(color1);
            viewHolder.txt.setTextColor(color1);
        }

        if (item.get("isDivider").equals("true")) {
            viewHolder.seperationView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.seperationView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void performClick(int position) {
        if (onItemClickList != null) {
            onItemClickList.onItemClick(position);
        }
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public SelectableRoundedImageView img;
        public MTextView txt;
        public LinearLayout contentArea;
        public View seperationView;

        public ViewHolder(View view) {
            super(view);

            img = (SelectableRoundedImageView) view.findViewById(R.id.img);
            txt = (MTextView) view.findViewById(R.id.txt);
            contentArea = (LinearLayout) view.findViewById(R.id.contentArea);
            seperationView = view.findViewById(R.id.seperationView);

        }
    }

}
