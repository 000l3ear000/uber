package com.adapter.files;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.like.LikeButton;
import com.squareup.picasso.Picasso;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;

public class UberXOnlineDriverListAdapter extends RecyclerView.Adapter<UberXOnlineDriverListAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    String userprofilejson;
    private OnItemClickListener mItemClickListener;
    private double pickUpLat;
    private double pickUpLong;
    String ENABLE_FAVORITE_DRIVER_MODULE;

    public UberXOnlineDriverListAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, double pickUpLat, double pickUpLong) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.pickUpLat = pickUpLat;
        this.pickUpLong = pickUpLong;
        userprofilejson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        ENABLE_FAVORITE_DRIVER_MODULE = generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public UberXOnlineDriverListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online_driver_list_design, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HashMap<String, String> item = list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + item.get("driver_id") + "/" + item.get("driver_img");

        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_no_pic_user)
                .error(R.mipmap.ic_no_pic_user)
                .into(viewHolder.driverImgView);

        String fAmount = item.get("fAmount");

        if (generalFunc.isRTLmode()) {
            viewHolder.btnImg.setRotation(180);
            viewHolder.btnArea.setBackground(mContext.getResources().getDrawable(R.drawable.login_border_rtl));
        }

        if (fAmount != null && !fAmount.trim().equals("") && !fAmount.trim().equals("0")) {
            viewHolder.priceTxt.setText(generalFunc.convertNumberWithRTL(fAmount));
        } else {
            viewHolder.priceTxt.setVisibility(View.GONE);
        }
        String IS_PROVIDER_ONLINE = item.get("IS_PROVIDER_ONLINE");
        if (IS_PROVIDER_ONLINE != null && IS_PROVIDER_ONLINE.equalsIgnoreCase("Yes")) {
            viewHolder.driverStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.Green));
        } else {
            viewHolder.driverStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.Red));
        }


        String LBL_FEATURED_TXT = generalFunc.retrieveLangLBl("Featured", "LBL_FEATURED_TXT");

        if (item.get("eIsFeatured").equalsIgnoreCase("Yes")) {
            String LANGUAGE_IS_RTL_KEY = generalFunc.retrieveValue(Utils.LANGUAGE_IS_RTL_KEY);
            if (!LANGUAGE_IS_RTL_KEY.equals("") && LANGUAGE_IS_RTL_KEY.equals(Utils.DATABASE_RTL_STR)) {
                viewHolder.labelFeatured.setText(LBL_FEATURED_TXT);

                viewHolder.labelFeatured.setVisibility(View.VISIBLE);

                // viewHolder.cardArea.setBackgroundResource(R.drawable.bg_card_providerlist);
            } else {
                viewHolder.labelFeatured.setText(LBL_FEATURED_TXT);

                viewHolder.labelFeatured.setVisibility(View.VISIBLE);

                //  viewHolder.cardArea.setBackgroundResource(R.drawable.bg_card_providerlist);
            }

        } else if (item.get("eIsFeatured").equalsIgnoreCase("No")) {
            viewHolder.labelFeatured.setVisibility(View.GONE);

            // viewHolder.cardArea.setBackgroundResource(0);
        }

        viewHolder.ratingBar.setRating(generalFunc.parseFloatValue(0, item.get("average_rating")));
        viewHolder.driverNameTxt.setText(item.get("Name") + " " + item.get("LastName"));
        viewHolder.btn_type2.setText(item.get("LBL_SEND_REQUEST"));
        viewHolder.btnTxt.setText(item.get("LBL_MORE_INFO_TXT"));
        viewHolder.milesTxt.setText(generalFunc.convertNumberWithRTL(item.get("DIST_TO_PICKUP_INT_ROW")));

        if (ENABLE_FAVORITE_DRIVER_MODULE.equalsIgnoreCase("Yes") && item.get("eFavDriver").equalsIgnoreCase("Yes")) {
            viewHolder.likeButton.setVisibility(View.VISIBLE);
            viewHolder.likeButton.setLiked(true);
            viewHolder.likeButton.setEnabled(false);
           // viewHolder.driverStatus.setVisibility(View.GONE);
        } else {
            viewHolder.likeButton.setVisibility(View.GONE);

            //viewHolder.driverStatus.setVisibility(View.VISIBLE);
        }

        viewHolder.btnArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickList(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClickList(View v, int position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public SelectableRoundedImageView driverImgView;
        public MTextView driverNameTxt;
        public MTextView priceTxt;
        public MTextView infoTxt;
        public MTextView milesTxt;
        public MButton btn_type2;
        public LinearLayout contentArea;
        public SimpleRatingBar ratingBar;
        public CardView cardView;
        public LinearLayout cardArea;
        public MTextView labelFeatured;
        public MTextView btnTxt;
        public ImageView btnImg;

        public LikeButton likeButton;
        public ImageView driverStatus;
        LinearLayout btnArea;

        public ViewHolder(View view) {
            super(view);

            driverImgView = (SelectableRoundedImageView) view.findViewById(R.id.driverImgView);
            driverNameTxt = (MTextView) view.findViewById(R.id.driverNameTxt);
            ratingBar = (SimpleRatingBar) view.findViewById(R.id.ratingBar);
            priceTxt = (MTextView) view.findViewById(R.id.priceTxt);
            infoTxt = (MTextView) view.findViewById(R.id.infoTxt);
            milesTxt = (MTextView) view.findViewById(R.id.milesTxt);
            btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_request)).getChildView();
            contentArea = (LinearLayout) view.findViewById(R.id.contentArea);
            cardView = (CardView) view.findViewById(R.id.cardView);
            labelFeatured = (MTextView) view.findViewById(R.id.labelFeatured);
            btnTxt = (MTextView) view.findViewById(R.id.btnTxt);
            btnImg = (ImageView) view.findViewById(R.id.btnImg);
            cardArea = (LinearLayout) view.findViewById(R.id.cardArea);
            btnArea = (LinearLayout) view.findViewById(R.id.btnArea);

            driverStatus = (ImageView) view.findViewById(R.id.driverStatus);
            likeButton = (LikeButton) view.findViewById(R.id.likeButton);

        }
    }
}
