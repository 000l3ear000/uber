package com.adapter.files;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.view.MTextView;

import java.util.ArrayList;

/**
 * Created by Admin on 01-07-2016.
 */
public class DrawerAdapter extends BaseAdapter {
    public static View view;
    static Context mContext;
    ArrayList<String[]> list_item;
    GeneralFunctions generalFunc;

    public DrawerAdapter(ArrayList<String[]> list_item, Context mContext) {
        this.list_item = list_item;
        this.mContext = mContext;

        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
    }


    @Override
    public int getCount() {
        return list_item.size();
    }

    @Override
    public Object getItem(int i) {
        return list_item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.drawer_list_item, null);

        this.view = view;


        ImageView menuIcon = (ImageView) view.findViewById(R.id.menuIcon);
        MTextView menuTitleTxt = (MTextView) view.findViewById(R.id.menuTitleTxt);
        String[] menuData=list_item.get(i);
        menuIcon.setImageResource(generalFunc.parseIntegerValue(0, menuData[0]));
        menuTitleTxt.setText(menuData[1]);

        menuTitleTxt.setTextColor(mContext.getResources().getColor(R.color.menu_list_txt_color));
        menuIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.menu_list_txt_color));


        return view;
    }
}
