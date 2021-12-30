package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.DividerView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-07-2016.
 */
public class AllHistoryRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public GeneralFunctions generalFunc;

    ArrayList<HashMap<String, String>> list;
    Context mContext;
    boolean isFooterEnabled = false;
    View footerView;

    FooterViewHolder footerHolder;
    private OnItemClickListener mItemClickListener;
    String userProfileJson;
    int size15_dp;
    int imagewidth;


    public AllHistoryRecycleAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        size15_dp = (int) mContext.getResources().getDimension(R.dimen._15sdp);
        imagewidth=  (int) mContext.getResources().getDimension(R.dimen._50sdp);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_booking_layout, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            boolean isAnyButtonShown = false;

            String vPackageName = item.get("vPackageName");
            if (vPackageName != null && !vPackageName.equalsIgnoreCase("")) {
                viewHolder.packageTxt.setVisibility(View.VISIBLE);
                viewHolder.packageTxt.setText(vPackageName);
            } else {
                viewHolder.packageTxt.setVisibility(View.GONE);
            }

            viewHolder.historyNoHTxt.setText(item.get("LBL_BOOKING_NO") + "#");
            viewHolder.historyNoVTxt.setText(item.get("vRideNo"));

            String ConvertedTripRequestDate = item.get("ConvertedTripRequestDate");
            String ConvertedTripRequestTime = item.get("ConvertedTripRequestTime");
//            String formattedListingDate = item.get("formattedListingDate");
//            String formattedListingTime = item.get("formattedListingTime");

            if (ConvertedTripRequestDate != null) {
                viewHolder.dateTxt.setText(ConvertedTripRequestDate);
                viewHolder.timeTxt.setText(ConvertedTripRequestTime);
            }/* else {
                viewHolder.dateTxt.setText(formattedListingDate);
                viewHolder.timeTxt.setText(formattedListingTime);
            }
*/

            viewHolder.sourceAddressTxt.setText(item.get("tSaddress"));
            viewHolder.sAddressTxt.setText(item.get("tSaddress"));
            viewHolder.sourceAddressHTxt.setText(item.get("LBL_PICK_UP_LOCATION"));
            viewHolder.destAddressHTxt.setText(item.get("LBL_DEST_LOCATION"));

            String vServiceTitle = item.get("vServiceTitle");
            if (vServiceTitle != null && !vServiceTitle.equalsIgnoreCase("")) {
                viewHolder.typeArea.setVisibility(View.VISIBLE);
                viewHolder.SelectedTypeNameTxt.setText(vServiceTitle);
            } else {
                viewHolder.typeArea.setVisibility(View.GONE);
            }


            String tDaddress = item.get("tDaddress");
            if (Utils.checkText(tDaddress)) {
                viewHolder.destarea.setVisibility(View.VISIBLE);
                viewHolder.pickupLocArea.setPadding(0, 0, 0, size15_dp);
                viewHolder.aboveLine.setVisibility(View.VISIBLE);
                viewHolder.destAddressTxt.setText(tDaddress);
            } else {
                viewHolder.destarea.setVisibility(View.GONE);
                viewHolder.aboveLine.setVisibility(View.GONE);
                viewHolder.pickupLocArea.setPadding(0, 0, 0, 0);

            }

            String vBookingType = item.get("vBookingType");
            String eShowHistory = item.get("eShowHistory");
            String iActive = item.get("iActive");

            if (Utils.checkText(iActive)) {
                viewHolder.statusArea.setVisibility(View.VISIBLE);
                viewHolder.statusVTxt.setText(item.get("iActive"));
            }

            if (generalFunc.isRTLmode()) {
                viewHolder.statusArea.setRotation(180);
                viewHolder.statusVTxt.setRotation(180);
            }


            viewHolder.SelectedTypeNameTxt.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            viewHolder.SelectedTypeNameTxt.setSelected(true);
            viewHolder.SelectedTypeNameTxt.setSingleLine(true);

            viewHolder.statusVTxt.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            viewHolder.statusVTxt.setSelected(true);
            viewHolder.statusVTxt.setSingleLine(true);


            viewHolder.liveTrackBtn.setText(item.get("liveTrackLBL"));
            viewHolder.viewDetailsBtn.setText(item.get("viewDetailLBL"));

            viewHolder.typeArea.setCardBackgroundColor(Color.parseColor(item.get("vService_BG_color")));
            viewHolder.SelectedTypeNameTxt.setTextColor(Color.parseColor(item.get("vService_TEXT_color")));


            viewHolder.liveTrackBtnArea.setVisibility(View.GONE);
            viewHolder.viewDetailsBtnArea.setVisibility(View.GONE);
            viewHolder.reScheduleArea.setVisibility(View.GONE);
            viewHolder.reBookingarea.setVisibility(View.GONE);
            viewHolder.reBookingBtnArea.setVisibility(View.GONE);
            viewHolder.cancelBookingBtnArea.setVisibility(View.GONE);
            viewHolder.cancelBookingArea.setVisibility(View.GONE);
            viewHolder.viewCancelReasonBtnArea.setVisibility(View.GONE);
            viewHolder.viewCancelReasonArea.setVisibility(View.GONE);
            viewHolder.viewRequestedServiceBtnArea.setVisibility(View.GONE);

            String eType = item.get("eType");
            boolean showUfxMultiArea = eType.equalsIgnoreCase(Utils.CabGeneralType_UberX) || eType.equalsIgnoreCase(Utils.eType_Multi_Delivery);

            if (showUfxMultiArea) {

                viewHolder.ufxMultiArea.setVisibility(View.VISIBLE);
                viewHolder.ufxMultiBtnArea.setVisibility(View.VISIBLE);

                viewHolder.noneUfxMultiArea.setVisibility(View.GONE);
                viewHolder.noneUfxMultiBtnArea.setVisibility(View.GONE);

                viewHolder.providerNameTxt.setText(item.get("vName"));

                String image_url = item.get("vImage");
                if (Utils.checkText(image_url)) {
                    Picasso.get()
                            .load(Utilities.getResizeImgURL(mContext,image_url, imagewidth, imagewidth))
                            .placeholder(R.mipmap.ic_no_pic_user)
                            .error(R.mipmap.ic_no_pic_user)
                            .into(viewHolder.providerImgView);
                } else {
                    viewHolder.providerImgView.setImageResource(R.mipmap.ic_no_pic_user);
                }

                viewHolder.ratingBar.setRating(Float.parseFloat(item.get("vAvgRating")));


            } else {
                viewHolder.ufxMultiArea.setVisibility(View.GONE);
                viewHolder.ufxMultiBtnArea.setVisibility(View.GONE);

                viewHolder.noneUfxMultiArea.setVisibility(View.VISIBLE);
                viewHolder.noneUfxMultiBtnArea.setVisibility(View.VISIBLE);
            }


            if (vBookingType.equalsIgnoreCase("schedule") /*&& showUfxMultiArea*/) {

                viewHolder.viewRequestedServiceBtn.setText(item.get("LBL_VIEW_REQUESTED_SERVICES"));

                viewHolder.btn_type_reschedule.setText(item.get("LBL_RESCHEDULE"));


                String LBL_REBOOKING = item.get("LBL_REBOOKING");
                viewHolder.reBookingBtn.setText(LBL_REBOOKING);
                viewHolder.btn_type_rebooking.setText(LBL_REBOOKING);

                String LBL_VIEW_REASON = item.get("LBL_VIEW_REASON");
                viewHolder.viewCancelReasonBtn.setText(LBL_VIEW_REASON);
                viewHolder.btn_type_view_cancel_reason.setText(LBL_VIEW_REASON);

                String LBL_CANCEL_BOOKING = item.get("LBL_CANCEL_BOOKING");
                viewHolder.cancelBookingBtn.setText(LBL_CANCEL_BOOKING);
                viewHolder.btn_type_cancel.setText(LBL_CANCEL_BOOKING);


                if (item.get("showReScheduleBtn").equalsIgnoreCase("Yes")) {
                    isAnyButtonShown = true;
                    viewHolder.reScheduleArea.setVisibility(View.VISIBLE);
                }

                if (item.get("showReBookingBtn").equalsIgnoreCase("Yes")) {
                    isAnyButtonShown = true;
                    viewHolder.reBookingarea.setVisibility(View.VISIBLE);
                    viewHolder.reBookingBtnArea.setVisibility(View.VISIBLE);
                }

                if (item.get("showCancelBookingBtn").equalsIgnoreCase("Yes")) {
                    isAnyButtonShown = true;
                    viewHolder.cancelBookingBtnArea.setVisibility(View.VISIBLE);
                    viewHolder.cancelBookingArea.setVisibility(View.VISIBLE);
                }

                if (item.get("showViewCancelReasonBtn").equalsIgnoreCase("Yes")) {
                    isAnyButtonShown = true;
                    viewHolder.viewCancelReasonBtnArea.setVisibility(View.VISIBLE);
                }


                if (item.get("showViewRequestedServicesBtn").equalsIgnoreCase("Yes")) {
                    isAnyButtonShown = true;
                    viewHolder.viewRequestedServiceBtnArea.setVisibility(View.VISIBLE);
                }


                viewHolder.cancelBookingBtnArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "CancelBooking");
                        }
                    }
                });

                viewHolder.cancelBookingArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "CancelBooking");
                        }
                    }
                });

                viewHolder.reBookingarea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "ReBooking");
                        }
                    }
                });

                viewHolder.reBookingBtnArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "ReBooking");
                        }
                    }
                });

                viewHolder.viewRequestedServiceBtnArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "ViewRequestedServices");
                        }
                    }
                });

                viewHolder.reScheduleArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "ReSchedule");
                        }
                    }
                });

                viewHolder.viewCancelReasonBtnArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "ViewCancelReason");
                        }
                    }
                });

                viewHolder.viewCancelReasonArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickList(view, position, "ViewCancelReason");
                        }
                    }
                });


            }


            if (item.get("showLiveTrackBtn").equalsIgnoreCase("Yes")) {
                isAnyButtonShown = true;
                viewHolder.liveTrackBtnArea.setVisibility(View.VISIBLE);
            }

            viewHolder.liveTrackBtnArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position, "LiveTrack");
                    }
                }
            });

            if (item.get("showViewDetailBtn").equalsIgnoreCase("Yes")) {
                isAnyButtonShown = true;
                viewHolder.viewDetailsBtnArea.setVisibility(View.VISIBLE);
            }

            if (!isAnyButtonShown) {
                if (showUfxMultiArea) {
                    viewHolder.ufxMultiBtnArea.setVisibility(View.GONE);
                } else {
                    viewHolder.noneUfxMultiBtnArea.setVisibility(View.GONE);

                }
            }

            viewHolder.viewDetailsBtnArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position, "ViewDetail");
                    }
                }
            });

            if (eShowHistory.equalsIgnoreCase("Yes")) {
                viewHolder.contentArea.setOnClickListener(view -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position, "ShowDetail");
                    }
                });
            }
            else {
                viewHolder.contentArea.setOnClickListener(null);
            }
    } else

    {
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
        if (footerHolder != null){
            isFooterEnabled=false;
            footerHolder.progressArea.setVisibility(View.GONE);
        }

//        footerHolder.progressArea.setPadding(0, -1 * footerView.getHeight(), 0, 0);
    }


public interface OnItemClickListener {
    void onItemClickList(View v, int position, String type);

}

// inner class to hold a reference to each item of RecyclerView
public class ViewHolder extends RecyclerView.ViewHolder {

    public MTextView providerNameTxt;
    public MTextView sAddressTxt;
    public SelectableRoundedImageView providerImgView;
    public SimpleRatingBar ratingBar;

    public MTextView historyNoHTxt;
    public MTextView historyNoVTxt;
    public MTextView dateTxt;
    public MTextView timeTxt;
    public MTextView sourceAddressTxt;
    public MTextView destAddressTxt;
    public MTextView sourceAddressHTxt;
    public MTextView destAddressHTxt;
    public MTextView statusHTxt;
    public MTextView statusVTxt;
    public LinearLayout contentArea;
    public MTextView SelectedTypeNameTxt;
    public MTextView packageTxt;
    public LinearLayout pickupLocArea, destarea;
    public DividerView aboveLine;
    public LinearLayout statusArea;

    public LinearLayout noneUfxMultiArea;
    public LinearLayout noneUfxMultiBtnArea;
    public LinearLayout reBookingarea;
    public MTextView btn_type_rebooking;
    public LinearLayout reScheduleArea;
    public MTextView btn_type_reschedule;
    public LinearLayout cancelBookingArea;
    public MTextView btn_type_cancel;
    public LinearLayout viewCancelReasonArea;
    public MTextView btn_type_view_cancel_reason;

    public LinearLayout ufxMultiArea;
    public LinearLayout ufxMultiBtnArea;
    public LinearLayout cancelBookingBtnArea;
    public MTextView cancelBookingBtn;
    public LinearLayout reBookingBtnArea;
    public MTextView reBookingBtn;
    public LinearLayout viewCancelReasonBtnArea;
    public MTextView viewCancelReasonBtn;
    public LinearLayout liveTrackBtnArea;
    public MTextView liveTrackBtn;
    public LinearLayout viewDetailsBtnArea;
    public MTextView viewDetailsBtn;
    public LinearLayout viewRequestedServiceBtnArea;
    public MTextView viewRequestedServiceBtn;
    public CardView typeArea;


    public ViewHolder(View view) {
        super(view);
        packageTxt = (MTextView) view.findViewById(R.id.packageTxt);
        contentArea = (LinearLayout) view.findViewById(R.id.contentArea);

        providerNameTxt = (MTextView) view.findViewById(R.id.providerNameTxt);
        sAddressTxt = (MTextView) view.findViewById(R.id.sAddressTxt);
        providerImgView = (SelectableRoundedImageView) view.findViewById(R.id.providerImgView);
        ratingBar = (SimpleRatingBar) view.findViewById(R.id.ratingBar);
        historyNoHTxt = (MTextView) view.findViewById(R.id.historyNoHTxt);
        historyNoVTxt = (MTextView) view.findViewById(R.id.historyNoVTxt);
        dateTxt = (MTextView) view.findViewById(R.id.dateTxt);
        timeTxt = (MTextView) view.findViewById(R.id.timeTxt);
        sourceAddressTxt = (MTextView) view.findViewById(R.id.sourceAddressTxt);
        destAddressTxt = (MTextView) view.findViewById(R.id.destAddressTxt);
        sourceAddressHTxt = (MTextView) view.findViewById(R.id.sourceAddressHTxt);
        destAddressHTxt = (MTextView) view.findViewById(R.id.destAddressHTxt);
        statusHTxt = (MTextView) view.findViewById(R.id.statusHTxt);
        statusVTxt = (MTextView) view.findViewById(R.id.statusVTxt);
        destarea = (LinearLayout) view.findViewById(R.id.destarea);
        SelectedTypeNameTxt = (MTextView) view.findViewById(R.id.SelectedTypeNameTxt);
        statusArea = (LinearLayout) view.findViewById(R.id.statusArea);
        pickupLocArea = (LinearLayout) view.findViewById(R.id.pickupLocArea);
        aboveLine = (DividerView) view.findViewById(R.id.aboveLine);

        noneUfxMultiArea = (LinearLayout) view.findViewById(R.id.noneUfxMultiArea);
        noneUfxMultiBtnArea = (LinearLayout) view.findViewById(R.id.noneUfxMultiBtnArea);
        reBookingarea = (LinearLayout) view.findViewById(R.id.reBookingarea);
        btn_type_rebooking = (MTextView) view.findViewById(R.id.btn_type_rebooking);
        reScheduleArea = (LinearLayout) view.findViewById(R.id.reScheduleArea);
        btn_type_reschedule = (MTextView) view.findViewById(R.id.btn_type_reschedule);
        cancelBookingArea = (LinearLayout) view.findViewById(R.id.cancelArea);
        btn_type_cancel = (MTextView) view.findViewById(R.id.btn_type_cancel);
        viewCancelReasonArea = (LinearLayout) view.findViewById(R.id.viewCancelReasonArea);
        btn_type_view_cancel_reason = (MTextView) view.findViewById(R.id.btn_type_view_cancel_reason);


        ufxMultiArea = (LinearLayout) view.findViewById(R.id.ufxMultiArea);
        ufxMultiBtnArea = (LinearLayout) view.findViewById(R.id.ufxMultiBtnArea);
        cancelBookingBtnArea = (LinearLayout) view.findViewById(R.id.cancelBookingBtnArea);
        cancelBookingBtn = (MTextView) view.findViewById(R.id.cancelBookingBtn);
        reBookingBtnArea = (LinearLayout) view.findViewById(R.id.reBookingBtnArea);
        reBookingBtn = (MTextView) view.findViewById(R.id.reBookingBtn);
        viewCancelReasonBtnArea = (LinearLayout) view.findViewById(R.id.viewCancelReasonBtnArea);
        viewCancelReasonBtn = (MTextView) view.findViewById(R.id.viewCancelReasonBtn);
        liveTrackBtnArea = (LinearLayout) view.findViewById(R.id.liveTrackBtnArea);
        liveTrackBtn = (MTextView) view.findViewById(R.id.liveTrackBtn);
        viewDetailsBtnArea = (LinearLayout) view.findViewById(R.id.viewDetailsBtnArea);
        viewDetailsBtn = (MTextView) view.findViewById(R.id.viewDetailsBtn);
        viewRequestedServiceBtnArea = (LinearLayout) view.findViewById(R.id.viewRequestedServiceBtnArea);
        viewRequestedServiceBtn = (MTextView) view.findViewById(R.id.viewRequestedServiceBtn);
        typeArea = (CardView) view.findViewById(R.id.typeArea);

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
