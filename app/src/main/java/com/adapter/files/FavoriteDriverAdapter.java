package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteDriverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HashMap<String, String>> list;
    Context mContext;
    private OnItemClickListener mItemClickListener;
    boolean isFooterEnabled = false;
    FooterViewHolder footerHolder;
    public final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    View footerView;
    GeneralFunctions generalFunc;
    String userProfileJson = "";
    private int lastPosition = -1;


    public FavoriteDriverAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
    }


    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_driver_heder_design, parent, false);
            HeaderViewHolder headerViewGolder = new HeaderViewHolder(view);
            return headerViewGolder;
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_driver_design, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;

            String driverImageName = item.get("vImage");
            if (driverImageName == null || driverImageName.isEmpty() || driverImageName.equals("NONE")) {
                viewHolder.driverProfileImgView.setImageResource(R.mipmap.ic_no_pic_user);
            } else {
                String image_url = driverImageName;
                Picasso.get()
                        .load(image_url)
                        .placeholder(R.mipmap.ic_no_pic_user)
                        .error(R.mipmap.ic_no_pic_user)
                        .into(viewHolder.driverProfileImgView);
            }

            viewHolder.ratingBar.setRating(generalFunc.parseFloatValue(0, item.get("vRating")));
            viewHolder.nameTxt.setText(item.get("vName"));
            viewHolder.eTypeTxt.setVisibility(Utils.checkText(item.get("eType"))?View.VISIBLE:View.GONE);
            viewHolder.eTypeTxt.setText(item.get("eType"));
            viewHolder.likeButton.setLiked(item.get("eFavDriver").equalsIgnoreCase("Yes"));

            viewHolder.eTypeTxt.setTextColor(Color.parseColor(item.get("vService_TEXT_color")));
            viewHolder.eTypeTxt.getBackground().setColorFilter(Color.parseColor(item.get("vService_BG_color")),
                    PorterDuff.Mode.SRC_ATOP);

            viewHolder.likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(likeButton, position);
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(likeButton, position);
                    }

                }
            });


        } else if (holder instanceof HeaderViewHolder) {
            final HashMap<String, String> item = list.get(position);

            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.eTypeTxt.setText(item.get("eType"));
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position) && isFooterEnabled == true) {
            return TYPE_FOOTER;
        } else if (list.get(position).containsKey("Type") && list.get(position).get("Type").equalsIgnoreCase("HEADER")) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }


    private boolean isPositionFooter(int position) {
        return position == list.size();
    }


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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleRatingBar ratingBar;
        public LinearLayout contentArea;
        public MTextView nameTxt;
        public MTextView eTypeTxt;
        SelectableRoundedImageView driverProfileImgView;
        LikeButton likeButton;

        public ViewHolder(View view) {
            super(view);

            ratingBar = (SimpleRatingBar) view.findViewById(R.id.ratingBar);
            contentArea = (LinearLayout) view.findViewById(R.id.contentArea);
            nameTxt = (MTextView) view.findViewById(R.id.nameTxt);
            eTypeTxt = (MTextView) view.findViewById(R.id.eTypeTxt);
            driverProfileImgView = (SelectableRoundedImageView) view.findViewById(R.id.driverProfileImgView);
            likeButton = (LikeButton) view.findViewById(R.id.likeButton);

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        MTextView eTypeTxt;

        public HeaderViewHolder(View view) {
            super(view);
            eTypeTxt = (MTextView) view.findViewById(R.id.eTypeTxt);
        }
    }


    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressArea = (LinearLayout) itemView;

        }
    }

    public interface OnItemClickListener {
        void onItemClickList(View v, int position);

    }

}
