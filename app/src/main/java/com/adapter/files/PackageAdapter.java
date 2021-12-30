package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 26-09-2017.
 */

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    Context mContext;
    int pos = 0;

    ArrayList<HashMap<String, String>> recentList;
    PackageAdapter.setPackageClickList packageClickList;
    View view;

    GeneralFunctions generalFunctions;

    public PackageAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mContext = context;
        this.recentList = list;
        generalFunctions = new GeneralFunctions(context);

    }

    public void selPos(int pos) {
        this.pos = pos;

    }

    @Override
    public PackageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_package_row, parent, false);


        return new PackageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageAdapter.ViewHolder holder, final int position) {
        HashMap<String, String> map = recentList.get(position);
        holder.pkgNameTxt.setText(generalFunctions.convertNumberWithRTL(map.get("vPackageName")));
        holder.pkgPriceTxt.setText(generalFunctions.convertNumberWithRTL(map.get("fPrice")));
        holder.pkgKmTxt.setText(generalFunctions.convertNumberWithRTL(map.get("fKiloMeter_LBL")));

        if (pos == position) {
            holder.radioBtn.setChecked(true);
        } else {
            holder.radioBtn.setChecked(false);
        }
        holder.radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packageClickList.itemPackageClick(position);
            }
        });
        holder.mainArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioBtn.performClick();
            }
        });
        if (recentList.size() != 0 && recentList.size() - 1 == position) {
            holder.pkgView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return recentList.size();
    }

    public void itemPackageClick(PackageAdapter.setPackageClickList packageClickList) {
        this.packageClickList = packageClickList;
    }

    public interface setPackageClickList {
        void itemPackageClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MTextView pkgNameTxt;
        MTextView pkgPriceTxt;
        MTextView pkgKmTxt;
        RadioButton radioBtn;
        LinearLayout mainArea;
        View pkgView;

        public ViewHolder(View itemView) {
            super(itemView);

            pkgNameTxt = (MTextView) itemView.findViewById(R.id.pkgNameTxt);
            pkgPriceTxt = (MTextView) itemView.findViewById(R.id.pkgPriceTxt);
            pkgKmTxt = (MTextView) itemView.findViewById(R.id.pkgKmTxt);
            radioBtn = (RadioButton) itemView.findViewById(R.id.radioBtn);
            mainArea = (LinearLayout) itemView.findViewById(R.id.mainArea);
            pkgView = (View) itemView.findViewById(R.id.pkgView);

        }
    }

}
