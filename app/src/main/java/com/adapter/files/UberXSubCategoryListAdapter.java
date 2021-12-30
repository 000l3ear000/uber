package com.adapter.files;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.squareup.picasso.Picasso;
import com.view.GenerateAlertBox;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;


public class UberXSubCategoryListAdapter extends BaseAdapter {

    Context mContext;
    GeneralFunctions generalFunc;
    GenerateAlertBox generateAlertBox;
    private ArrayList<HashMap<String, String>> catagoryDataList;


    public UberXSubCategoryListAdapter(Context mContext, ArrayList<HashMap<String, String>> dataList) {
        this.mContext = mContext;
        this.catagoryDataList = dataList;
        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        generateAlertBox = new GenerateAlertBox(mContext);

    }

    @Override
    public int getCount() {
        return catagoryDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return catagoryDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_sub_catagory_list_design, null);
        }

        HashMap<String, String> item=catagoryDataList.get(position);
        MTextView catagoryTxt = (MTextView) convertView.findViewById(R.id.catagoryTxt);
        catagoryTxt.setText(item.get("vCategory"));
        ImageView catagoryImageView = (ImageView) convertView.findViewById(R.id.catagoryImageView);
        ImageView arrowImageView = (ImageView) convertView.findViewById(R.id.arrowImageView);
        if (generalFunc != null && generalFunc.isRTLmode()) {
            arrowImageView.setRotation(180);
        }

        Picasso.get().load(item.get("vLogo_image")).into(catagoryImageView);

        return convertView;
    }


}
