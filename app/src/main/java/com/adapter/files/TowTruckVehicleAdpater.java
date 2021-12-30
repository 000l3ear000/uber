package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class TowTruckVehicleAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> vTypeList;
    Context mContext;
    OnItemClickListener onItemClickListener;

    public TowTruckVehicleAdpater(Context mContext, ArrayList<HashMap<String, String>> vTypeList, GeneralFunctions generalFunc) {
        this.vTypeList = vTypeList;
        this.mContext = mContext;
        this.generalFunc = generalFunc;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_towtruck_vehicle_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = vTypeList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.vehicleNameTxtView.setText(item.get("vMake") + " (" + item.get("iYear") + ") ");
            viewHolder.vehicleColorNameTxtView.setText(item.get("vColour"));

            viewHolder.contentArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.setOnClick("Select", position);
                    }
                }
            });
            viewHolder.editVehicleImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.setOnClick("edit", position);
                    }
                }
            });
            viewHolder.deleteVehicleImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.setOnClick("delete", position);

                    }

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return vTypeList.size();
    }

    public void setOnClickList(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void setOnClick(String action, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView vehicleNameTxtView;
        public MTextView vehicleColorNameTxtView;
        public SelectableRoundedImageView vehicleImgView;
        public View contentArea;
        ImageView deleteVehicleImgView, editVehicleImgView;

        public ViewHolder(View view) {
            super(view);

            vehicleNameTxtView = (MTextView) view.findViewById(R.id.vehicleNameTxtView);
            vehicleColorNameTxtView = (MTextView) view.findViewById(R.id.vehicleColorNameTxtView);
            editVehicleImgView = (ImageView) view.findViewById(R.id.editVehicleImgView);
            deleteVehicleImgView = (ImageView) view.findViewById(R.id.deleteVehicleImgView);
            vehicleImgView = (SelectableRoundedImageView) view.findViewById(R.id.vehicleImgView);
            contentArea = (View) view.findViewById(R.id.contentArea);
        }
    }
}
