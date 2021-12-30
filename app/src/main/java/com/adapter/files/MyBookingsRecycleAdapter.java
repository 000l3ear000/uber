package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.HashMap;

import static com.adapter.files.DrawerAdapter.view;

/**
 * Created by Admin on 09-07-2016.
 */
public class MyBookingsRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    boolean isFooterEnabled = false;
    View footerView;
    FooterViewHolder footerHolder;
    private OnItemClickListener mItemClickListener;
    String userProfileJsonObj;
    String SERVICE_PROVIDER_FLOW;

    public MyBookingsRecycleAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
        userProfileJsonObj = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        SERVICE_PROVIDER_FLOW = generalFunc.getJsonValue("SERVICE_PROVIDER_FLOW", userProfileJsonObj);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_bookings_design, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.myBookingNoHTxt.setText(item.get("LBL_BOOKING_NO") + "#");
            viewHolder.myBookingNoVTxt.setText(item.get("formattedVBookingNo"));
            viewHolder.dateTxt.setText(item.get("listingFormattedDate"));
            viewHolder.statusHTxt.setText(item.get("LBL_Status") + ":");
            viewHolder.sourceAddressTxt.setText(item.get("vSourceAddresss"));

            viewHolder.destAddressHTxt.setText(item.get("LBL_DEST_LOCATION"));
            viewHolder.sourceAddressHTxt.setText(item.get("LBL_PICK_UP_LOCATION"));
            viewHolder.slecttypearea.setVisibility(View.VISIBLE);

            String vServiceTitle = item.get("vServiceTitle");
            String selectedCategory = item.get("SelectedCategory");

            if (vServiceTitle != null && !vServiceTitle.equalsIgnoreCase("")) {
                viewHolder.SelectedTypeNameTxt.setText(vServiceTitle);
            } else {
                viewHolder.slecttypearea.setVisibility(View.GONE);
            }

            viewHolder.service_area.setVisibility(View.GONE);

            if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider") && list.get(position).get("moreServices").equalsIgnoreCase("Yes")) {
                viewHolder.SelectedTypeNameTxt.setText(selectedCategory);
                viewHolder.btn_type2_service.setText(item.get("LBL_VIEW_REQUESTED_SERVICES"));
                viewHolder.service_area.setVisibility(View.VISIBLE);
            }


            if (item.get("tDestAddress").equals("")) {
                viewHolder.destAddressTxt.setVisibility(View.GONE);
                viewHolder.destarea.setVisibility(View.GONE);
                viewHolder.imagedest.setVisibility(View.GONE);
                viewHolder.destAddressHTxt.setVisibility(View.GONE);
            } else {
                viewHolder.destarea.setVisibility(View.VISIBLE);
                viewHolder.imagedest.setVisibility(View.VISIBLE);
                viewHolder.destAddressTxt.setVisibility(View.VISIBLE);
                viewHolder.destAddressHTxt.setVisibility(View.VISIBLE);
                viewHolder.destAddressTxt.setText(item.get("tDestAddress"));

            }

            viewHolder.statusVTxt.setText(item.get("eStatus"));
            viewHolder.cancelBookingTxt.setText(item.get("LBL_CANCEL_BOOKING"));
            viewHolder.btn_type2.setText(item.get("LBL_CANCEL_BOOKING"));

            LinearLayout.LayoutParams statusAreaParams = (LinearLayout.LayoutParams) viewHolder.statusArea.getLayoutParams();

            String appType=item.get("appType");
            String eType=item.get("eType");
            String eStatusV=item.get("eStatusV");

            if (appType.equalsIgnoreCase("RIDE-DELIVERY") || appType.equalsIgnoreCase("RIDE-DELIVERY-UBERX")) {

                viewHolder.etypeTxt.setText(item.get("eType"));
                viewHolder.dateTxt.setVisibility(View.VISIBLE);

            } else {
                viewHolder.dateTxt.setVisibility(View.GONE);
                viewHolder.etypeTxt.setText(item.get("listingFormattedDate"));
            }

            if (appType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || appType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || appType.equalsIgnoreCase(Utils.CabGeneralType_Ride) || appType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                viewHolder.dateTxt.setVisibility(View.GONE);
                viewHolder.etypeTxt.setText(item.get("listingFormattedDate"));


            } else {

                viewHolder.etypeTxt.setText(eType.equalsIgnoreCase(Utils.CabGeneralType_Ride) ? item.get("LBL_RIDE") : ((eType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || (eType.equalsIgnoreCase("Deliver"))) ? item.get("LBL_DELIVERY") : item.get("LBL_SERVICES")));
                viewHolder.dateTxt.setVisibility(View.VISIBLE);

            }
            viewHolder.statusArea.setVisibility(View.VISIBLE);

            if (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                viewHolder.cancelBookingArea.setVisibility(View.GONE);
                viewHolder.statusArea.setVisibility(View.VISIBLE);
                viewHolder.btn_type2Area.setVisibility(View.GONE);
                viewHolder.btnarea.setVisibility(View.VISIBLE);
                viewHolder.btn_type_cancel.setText(item.get("LBL_CANCEL_BOOKING"));
                viewHolder.reschedulearea.setVisibility(View.GONE);
                viewHolder.cancelrideDelarea.setVisibility(View.VISIBLE);
                if (eStatusV.equalsIgnoreCase("Cancel") || eStatusV.equalsIgnoreCase("Declined")) {
                    viewHolder.cancelrideDelarea.setVisibility(View.VISIBLE);
                    viewHolder.btn_type2.setText(item.get("LBL_REBOOKING"));
                    viewHolder.btn_type2Area.setVisibility(View.VISIBLE);
                    viewHolder.btn_type_cancel.setText(item.get("LBL_VIEW_REASON"));
                    viewHolder.reschedulearea.setVisibility(View.GONE);
                }
            } else {
                if (!eStatusV.equals("Pending")) {
                    viewHolder.btn_type2.setVisibility(View.GONE);
                }
                if (item.get("eAutoAssign").equalsIgnoreCase("Yes")) {
                    viewHolder.btn_type2.setVisibility(View.VISIBLE);
                    viewHolder.btn_type2Area.setVisibility(View.VISIBLE);
                    viewHolder.btnarea.setVisibility(View.VISIBLE);
                    viewHolder.btn_type2.setText(item.get("LBL_RESCHEDULE"));
                    viewHolder.btn_type_cancel.setText(item.get("LBL_CANCEL_BOOKING"));

                    viewHolder.cancelrideDelarea.setVisibility(View.VISIBLE);

                    if (eStatusV.equalsIgnoreCase("Cancel") || eStatusV.equalsIgnoreCase("Declined")) {
                        viewHolder.cancelBookingArea.setVisibility(View.GONE);
                        viewHolder.statusArea.setVisibility(View.VISIBLE);
                        viewHolder.btn_type_cancel.setText(item.get("LBL_VIEW_REASON"));
                        viewHolder.reschedulearea.setVisibility(View.GONE);

                    }
                } else {
                    viewHolder.btnarea.setVisibility(View.VISIBLE);
                    viewHolder.cancelrideDelarea.setVisibility(View.VISIBLE);
                    viewHolder.reschedulearea.setVisibility(View.GONE);
                    viewHolder.btn_type2Area.setVisibility(View.GONE);

                    viewHolder.btn_type_cancel.setText(item.get("LBL_CANCEL_BOOKING"));

                    if (eStatusV.equalsIgnoreCase("Cancel") || eStatusV.equalsIgnoreCase("Declined")) {
                        viewHolder.btn_type_cancel.setText(item.get("LBL_VIEW_REASON"));
                    }

                }
            }

            viewHolder.cancelBookingArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position, false);
                    }
                }
            });


            viewHolder.btn_type2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        if (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                            mItemClickListener.onItemClickList(view, position, true);
                        } else {
                            mItemClickListener.onItemClickList(position, true);

                        }
                    }


                }
            });
            viewHolder.btn_type_booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                        mItemClickListener.onItemClickList(view, position, false);
                    } else {
                        mItemClickListener.onItemClickList(position, false);
                    }
                }
            });

            viewHolder.btn_type_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eStatusV.equalsIgnoreCase("Cancel") || eStatusV.equalsIgnoreCase("Declined")) {
                        mItemClickListener.onItemClickList(position);
                    } else {
                        mItemClickListener.onItemClickList(position, false);
                    }


                }
            });

            viewHolder.btn_type2_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onViewServiceClickList(view, position);
                    }

                }
            });
        } else {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position) && isFooterEnabled == true) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == list.size();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (isFooterEnabled == true) {
            return list.size() + 1;
        } else {
            return list.size();
        }

    }

    public void addFooterView() {
//        Logger.d("Footer", "added");
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
//        Logger.d("Footer", "removed");
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.GONE);
//        footerHolder.progressArea.setPadding(0, -1 * footerView.getHeight(), 0, 0);
    }

    public interface OnItemClickListener {
        void onItemClickList(View v, int position, boolean isSchedulebooking);

        void onItemClickList(int position, boolean isSchedulebooking);

        public void onItemClickList(int position);

        void onViewServiceClickList(View v, int position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView myBookingNoHTxt;
        public MTextView myBookingNoVTxt;
        public MTextView dateTxt;
        public MTextView sourceAddressTxt;
        public MTextView destAddressTxt;
        public MTextView statusHTxt;
        public MTextView statusVTxt;
        public MTextView cancelBookingTxt;
        public LinearLayout cancelBookingArea;
        public LinearLayout statusArea;
        public MButton btn_type2;
        public MButton btn_type_booking, btn_type_cancel;
        public LinearLayout btnarea;
        public LinearLayout btn_type2Area;

        public MTextView sourceAddressHTxt;
        public MTextView destAddressHTxt;

        public MTextView etypeTxt;
        public ImageView imagedest;
        public LinearLayout destarea;
        public LinearLayout reschedulearea, cancelrideDelarea;
        public LinearLayout slecttypearea;
        public MTextView SelectedTypeNameTxt;
        public LinearLayout service_area;
        public MButton btn_type2_service;

        public ViewHolder(View view) {
            super(view);
            int submitBtnId;
            int bookingBtnId;
            myBookingNoHTxt = (MTextView) view.findViewById(R.id.myBookingNoHTxt);
            myBookingNoVTxt = (MTextView) view.findViewById(R.id.myBookingNoVTxt);
            dateTxt = (MTextView) view.findViewById(R.id.dateTxt);
            sourceAddressTxt = (MTextView) view.findViewById(R.id.sourceAddressTxt);
            destAddressTxt = (MTextView) view.findViewById(R.id.destAddressTxt);
            statusHTxt = (MTextView) view.findViewById(R.id.statusHTxt);
            statusVTxt = (MTextView) view.findViewById(R.id.statusVTxt);
            cancelBookingTxt = (MTextView) view.findViewById(R.id.cancelBookingTxt);
            cancelBookingArea = (LinearLayout) view.findViewById(R.id.cancelBookingArea);
            statusArea = (LinearLayout) view.findViewById(R.id.statusArea);
            etypeTxt = (MTextView) view.findViewById(R.id.etypeTxt);
            btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();
            btn_type_booking = ((MaterialRippleLayout) view.findViewById(R.id.btn_type_booking)).getChildView();
            btn_type_cancel = ((MaterialRippleLayout) view.findViewById(R.id.btn_type_cancel)).getChildView();
            btnarea = (LinearLayout) view.findViewById(R.id.btnarea);
            btn_type2Area = (LinearLayout) view.findViewById(R.id.btn_type2Area);
            sourceAddressHTxt = (MTextView) view.findViewById(R.id.sourceAddressHTxt);
            destAddressHTxt = (MTextView) view.findViewById(R.id.destAddressHTxt);
            destarea = (LinearLayout) view.findViewById(R.id.destarea);
            reschedulearea = (LinearLayout) view.findViewById(R.id.reschedulearea);
            cancelrideDelarea = (LinearLayout) view.findViewById(R.id.cancelrideDelarea);
            imagedest = (ImageView) view.findViewById(R.id.imagedest);
            slecttypearea = (LinearLayout) view.findViewById(R.id.slecttypearea);
            SelectedTypeNameTxt = (MTextView) view.findViewById(R.id.SelectedTypeNameTxt);
            service_area = (LinearLayout) view.findViewById(R.id.service_area);
            btn_type2_service = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2_service)).getChildView();

            submitBtnId = Utils.generateViewId();
            btn_type2.setId(submitBtnId);


        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressArea = (LinearLayout) itemView;

        }
    }
}
