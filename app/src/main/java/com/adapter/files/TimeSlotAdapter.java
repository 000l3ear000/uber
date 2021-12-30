package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.view.AutoResizeTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-10-2017.
 */

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    Context mContext;
    View view;
    public int isSelectedPos = -1;
    setRecentTimeSlotClickList setRecentTimeSlotClickList;

    ArrayList<HashMap<String, String>> timeSlotList;
    GeneralFunctions generalFunctions;
    String LBL_PROVIDER_NOT_AVAIL_NOTE="";

    public TimeSlotAdapter(Context context, ArrayList<HashMap<String, String>> timeSlotList) {
        this.mContext = context;
        this.timeSlotList = timeSlotList;
        generalFunctions = new GeneralFunctions(mContext);
        LBL_PROVIDER_NOT_AVAIL_NOTE=generalFunctions.retrieveLangLBl("", "LBL_PROVIDER_NOT_AVAIL_NOTE");
    }


    @Override
    public TimeSlotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_timeslot_view, parent, false);
        return new TimeSlotAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimeSlotAdapter.ViewHolder holder, final int position) {
        HashMap<String, String> map=timeSlotList.get(position);
        String name = map.get("name");

        holder.stratTimeTxtView.setText(name);
        holder.stratselTimeTxtView.setText(name);
        holder.driverstratselTimeTxtView.setText(name);

        String isDriverAvailable = map.get("isDriverAvailable");

        if (isDriverAvailable != null && isDriverAvailable.equalsIgnoreCase("No")) {
            holder.selmainarea.setVisibility(View.GONE);
            holder.mainarea.setVisibility(View.GONE);
            holder.driverUnselmainarea.setVisibility(View.VISIBLE);
            holder.mainarea.setClickable(false);
        } else {
            holder.driverUnselmainarea.setVisibility(View.GONE);
            holder.mainarea.setClickable(true);
            if (isSelectedPos != -1) {
                if (isSelectedPos == position) {
                    holder.selmainarea.setVisibility(View.VISIBLE);
                    holder.mainarea.setVisibility(View.GONE);
                } else {
                    holder.selmainarea.setVisibility(View.GONE);
                    holder.mainarea.setVisibility(View.VISIBLE);
                }
            }else {
                holder.selmainarea.setVisibility(View.GONE);
                holder.mainarea.setVisibility(View.VISIBLE);
            }
        }

        holder.mainarea.setOnClickListener(v -> {

            int oldItemPosition = isSelectedPos;

            isSelectedPos = position;
            if (setRecentTimeSlotClickList != null) {
                setRecentTimeSlotClickList.itemTimeSlotLocClick(position);
            }

            notifyItemChanged(oldItemPosition);
            notifyItemChanged(isSelectedPos);

//            notifyDataSetChanged();

        });

        holder.driverUnselmainarea.setOnClickListener(v -> generalFunctions.showMessage(holder.driverstratselTimeTxtView,LBL_PROVIDER_NOT_AVAIL_NOTE));
    }

    @Override
    public int getItemCount() {
        //  return recentList.size();
        // return 23;
        return timeSlotList.size();
    }

    public void setOnClickList(setRecentTimeSlotClickList setRecentTimeSlotClickList) {
        this.setRecentTimeSlotClickList = setRecentTimeSlotClickList;
    }

    public interface setRecentTimeSlotClickList {
        void itemTimeSlotLocClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AutoResizeTextView  stratselTimeTxtView, driverstratselTimeTxtView,stratTimeTxtView;
        LinearLayout mainarea, selmainarea, driverUnselmainarea;
        ;


        public ViewHolder(View itemView) {
            super(itemView);

            stratTimeTxtView = (AutoResizeTextView) itemView.findViewById(R.id.stratTimeTxtView);
            mainarea = (LinearLayout) itemView.findViewById(R.id.mainarea);
            selmainarea = (LinearLayout) itemView.findViewById(R.id.selmainarea);
            driverUnselmainarea = (LinearLayout) itemView.findViewById(R.id.driverUnselmainarea);
            stratselTimeTxtView = (AutoResizeTextView) itemView.findViewById(R.id.stratselTimeTxtView);
            driverstratselTimeTxtView = (AutoResizeTextView) itemView.findViewById(R.id.driverstratselTimeTxtView);


        }
    }


}
