package com.adapter.files;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.melevicarbrasil.usuario.R;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 26-09-2017.
 */

public class CustSpinnerAdapter extends BaseAdapter {

    Context mContext;

    ArrayList<HashMap<String, String>> list;
    CustSpinnerAdapter.setRecentLocClickList locClickList;
    View view;
    LayoutInflater inflter;

    public CustSpinnerAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mContext = context;
        this.list = list;
        inflter = (LayoutInflater.from(context));

    }

    public void itemRecentLocClick(CustSpinnerAdapter.setRecentLocClickList setRecentLocClickList) {
        this.locClickList = setRecentLocClickList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_spinnertextview, null);

        MTextView names = (MTextView) view.findViewById(R.id.spinnerTextView);

        names.setText(list.get(i).get("title"));
        return view;
    }

    public interface setRecentLocClickList {
        void itemRecentLocClick(int position);
    }


}
