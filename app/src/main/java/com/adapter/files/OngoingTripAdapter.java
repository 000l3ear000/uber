package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.OnGoingTripsActivity;
import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-07-2016.
 */
public class OngoingTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    boolean isFooterEnabled = false;
    View footerView;
    FooterViewHolder footerHolder;
    OnGoingTripsActivity onGoingTripsActivity;
    private OnItemClickListener mItemClickListener;


    public OngoingTripAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
        onGoingTripsActivity = (OnGoingTripsActivity) mContext;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_ongoing_trips_detail, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.bookingNoLblTxt.setText(item.get("Booking_LBL") + "# ");
            viewHolder.bookingNoTxt.setText(item.get("vRideNo"));
//            viewHolder.bookingDateTxt.setText("");
            viewHolder.userNameTxt.setText(item.get("driverName"));
            viewHolder.userAddressTxt.setText(item.get("tSaddress"));
            viewHolder.ratingBar.setRating(GeneralFunctions.parseFloatValue(0, item.get("driverRating")));
            viewHolder.dateTxt.setText(generalFunc.getDateFormatedType(item.get("dDateOrig"), Utils.OriginalDateFormate, Utils.getDetailDateFormat(mContext)));

            String eTypeName = item.containsKey("eTypeName") ? item.get("eTypeName") : "";
            if (Utils.checkText(eTypeName)) {
                viewHolder.etypeTxt.setVisibility(View.VISIBLE);
                viewHolder.etypeTxt.setText(eTypeName);
                viewHolder.dateTxt.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                viewHolder.etypeTxt.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            }

            viewHolder.viewDetailsBtn.setText(item.get("viewDetailLBL"));

            String vServiceTitle = item.get("vServiceTitle");

            if (vServiceTitle != null && !vServiceTitle.equals("")) {
                viewHolder.SelectedTypeNameTxt.setVisibility(View.VISIBLE);
                viewHolder.SelectedTypeNameTxt.setText(vServiceTitle);
            } else {
                viewHolder.SelectedTypeNameTxt.setVisibility(View.GONE);
            }

            int btnRadius = Utils.dipToPixels(mContext, 8);
            int strokeWidth = Utils.dipToPixels(mContext, 1);
            int strokeColor = mContext.getResources().getColor(R.color.appThemeColor_bg_parent_1);

            new CreateRoundedView(Color.parseColor("#ffffff"), btnRadius, strokeWidth, Color.parseColor("#CECECE"), viewHolder.contentArea);
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_2), btnRadius, strokeWidth, strokeColor, viewHolder.liveTrackBtnArea);
            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_2), btnRadius, strokeWidth, strokeColor, viewHolder.viewDetailsBtnArea);


//            viewHolder.liveTrackBtnArea.setVisibility(View.GONE);

            if (item.get("eType").equalsIgnoreCase(Utils.eType_Multi_Delivery) && item.containsKey("liveTrackLBL") && !item.get("driverStatus").equalsIgnoreCase("Finished")) {
                viewHolder.liveTrackBtn.setText(item.get("liveTrackLBL"));
                viewHolder.liveTrackBtnArea.setVisibility(View.VISIBLE);

            } else {
                viewHolder.liveTrackBtnArea.setVisibility(View.GONE);

            }

            viewHolder.liveTrackBtnArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList("Live Track", position);
                    }
                }
            });


            viewHolder.viewDetailsBtnArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList("View Detail", position);
                    }
                }
            });

            viewHolder.liveTrackBtnArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList("Live Track", position);
                    }
                }
            });


            String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + item.get("iDriverId") + "/" + item.get("driverImage");
            Picasso.get()
                    .load(image_url)
                    .placeholder(R.mipmap.ic_no_pic_user)
                    .error(R.mipmap.ic_no_pic_user)
                    .into(viewHolder.user_img);
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
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.GONE);
    }


    public interface OnItemClickListener {
        void onItemClickList(String type, int position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {


        private SelectableRoundedImageView user_img;
        private MTextView userAddressTxt;
        private MTextView bookingNoLblTxt;
        private MTextView bookingNoTxt;
        private MTextView bookingDateTxt, etypeTxt;
        private MTextView userNameTxt;
        private SimpleRatingBar ratingBar;
        private LinearLayout liveTrackBtnArea;
        private LinearLayout viewDetailsBtnArea;
        private LinearLayout contentArea;
        private MTextView dateTxt;
        private MTextView SelectedTypeNameTxt;
        private MTextView viewDetailsBtn;
        private MTextView liveTrackBtn;


        public ViewHolder(View view) {
            super(view);

            user_img = (SelectableRoundedImageView) view.findViewById(R.id.user_img);
            userAddressTxt = (MTextView) view.findViewById(R.id.userAddressTxt);
            bookingNoLblTxt = (MTextView) view.findViewById(R.id.bookingNoLblTxt);
            bookingDateTxt = (MTextView) view.findViewById(R.id.bookingDateTxt);
            etypeTxt = (MTextView) view.findViewById(R.id.etypeTxt);
            bookingNoTxt = (MTextView) view.findViewById(R.id.bookingNoTxt);
            userNameTxt = (MTextView) view.findViewById(R.id.userNameTxt);
            ratingBar = (SimpleRatingBar) view.findViewById(R.id.ratingBar);
            liveTrackBtnArea = (LinearLayout) view.findViewById(R.id.liveTrackBtnArea);
            viewDetailsBtnArea = (LinearLayout) view.findViewById(R.id.viewDetailsBtnArea);
            contentArea = (LinearLayout) view.findViewById(R.id.contentArea);
            dateTxt = (MTextView) view.findViewById(R.id.dateTxt);
            SelectedTypeNameTxt = (MTextView) view.findViewById(R.id.SelectedTypeNameTxt);
            viewDetailsBtn = (MTextView) view.findViewById(R.id.viewDetailsBtn);
            liveTrackBtn = (MTextView) view.findViewById(R.id.liveTrackBtn);
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
