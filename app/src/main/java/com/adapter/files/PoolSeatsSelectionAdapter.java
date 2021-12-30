package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;

/**
 * Created by Admin on 22-02-2017.
 */
public class PoolSeatsSelectionAdapter extends RecyclerView.Adapter<PoolSeatsSelectionAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<String> list_item;
    Context mContext;
    OnItemClickList onItemClickList;
    int selectedPos = 0;
    int btnRadius;

    public PoolSeatsSelectionAdapter(Context mContext, ArrayList<String> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
        btnRadius=Utils.dipToPixels(mContext, 80);
    }

    @Override
    public PoolSeatsSelectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_no_of_seats_pool, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setSelectedSeat(int selectedSeat) {
        this.selectedPos = selectedSeat;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        String item = list_item.get(position);

        boolean isHover = position == selectedPos ? true : false;

        int color=R.color.appThemeColor_bg_parent_1;

        new CreateRoundedView( mContext.getResources().getColor(R.color.white),btnRadius, 2, isHover ? mContext.getResources().getColor(R.color.btnnavtripselcolor) : color, viewHolder.seatsImgView);
        viewHolder.noTxt.setTextColor(mContext.getResources().getColor(R.color.black));
        viewHolder.noTxt.setText(item);
        viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickList != null) {
                    onItemClickList.onItemClick(position, "poolSeatsSelected");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position, String selectedType);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public SelectableRoundedImageView seatsImgView;
        public LinearLayout contentArea;
        public MTextView noTxt;

        public ViewHolder(View view) {
            super(view);
            seatsImgView = (SelectableRoundedImageView) view.findViewById(R.id.seatsImgView);
            contentArea = (LinearLayout) view.findViewById(R.id.contentArea);
            noTxt = (MTextView) view.findViewById(R.id.noTxt);
        }
    }

}
