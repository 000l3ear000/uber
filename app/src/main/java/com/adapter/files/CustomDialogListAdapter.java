package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomDialogListAdapter extends RecyclerView.Adapter<CustomDialogListAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<HashMap<String, String>> listItem;
    OnItemClickList onItemClickList;
    int selectedItemPosition;
    String keyToShowAsTitle;

    int selectedColor;
    int nonSelectedColor;

    boolean onlyStringArrayList;
    private ArrayList<String> stringListItem;

    public CustomDialogListAdapter(Context mContext, ArrayList<HashMap<String, String>> listItem, int selectedItemPosition, String keyToShowAsTitle) {
        this.mContext = mContext;
        this.listItem = listItem;
        this.selectedItemPosition = selectedItemPosition;
        this.keyToShowAsTitle = keyToShowAsTitle;

        selectedColor = mContext.getResources().getColor(R.color.appThemeColor_1);
        nonSelectedColor = Color.parseColor("#646464");
    }

    public CustomDialogListAdapter(Context mContext, ArrayList<String> stringListItem, int selectedItemPosition,boolean onlyStringArrayList) {
        this.mContext = mContext;
        this.stringListItem = stringListItem;
        this.selectedItemPosition = selectedItemPosition;
        this.onlyStringArrayList = onlyStringArrayList;

        selectedColor = mContext.getResources().getColor(R.color.appThemeColor_1);
        nonSelectedColor = Color.parseColor("#646464");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        viewHolder.rowTitleTxtView.setText(onlyStringArrayList?stringListItem.get(i):listItem.get(i).get(keyToShowAsTitle));

        if (selectedItemPosition == i) {
            viewHolder.rowTitleTxtView.setTextColor(selectedColor);
            viewHolder.rowTitleTxtView.setTypeface(viewHolder.rowTitleTxtView.getTypeface(), Typeface.BOLD);
            viewHolder.imgCheck.setColorFilter(selectedColor, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            viewHolder.rowTitleTxtView.setTextColor(nonSelectedColor);
            viewHolder.rowTitleTxtView.setTypeface(viewHolder.rowTitleTxtView.getTypeface(), Typeface.NORMAL);
            viewHolder.imgCheck.setColorFilter(nonSelectedColor, android.graphics.PorterDuff.Mode.SRC_IN);
        }

        /*String selectedSortValue = listItem.get(i).get("selectedSortValue");


        if (!selectedSortValue.equals("") && selectedSortValue.equals(listItem.get(i).get("vName"))) {
            viewHolder.rowTitleTxtView.setTextColor(mContext.getResources().getColor(R.color.appThemeColor_1));
            viewHolder.rowTitleTxtView.setTypeface(viewHolder.rowTitleTxtView.getTypeface(), Typeface.BOLD);
            viewHolder.imgCheck.setColorFilter(ContextCompat.getColor(mContext, R.color.appThemeColor_1), android.graphics.PorterDuff.Mode.SRC_IN);
        }*/

        viewHolder.mainArea.setOnClickListener(v -> {
            if (onItemClickList != null) {
                onItemClickList.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return onlyStringArrayList?stringListItem.size():listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MTextView rowTitleTxtView;
        ImageView imgCheck;
        LinearLayout mainArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowTitleTxtView = itemView.findViewById(R.id.rowTitleTxtView);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            mainArea = itemView.findViewById(R.id.mainArea);
        }
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }
}
