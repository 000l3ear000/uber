package com.adapter.files;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utilities;
import com.utils.Utils;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryImagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_ADD = 3;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    boolean isFooterEnabled = false;
    View footerView;
    FooterViewHolder footerHolder;
    private OnItemClickListener mItemClickListener;
    boolean isDelete = false;
    boolean isSel = false;
    int itemWidth;
    int width;
    private static final String TAG = "GalleryImagesRecyclerAd";
    public GalleryImagesRecyclerAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled, boolean isDelete, boolean isSel) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
        this.isDelete = isDelete;
        this.isSel = isSel;

        itemWidth = (int) (getScreenDPWidth() / getNumOfColumns());
        width=Utils.dipToPixels(mContext, itemWidth) - Utils.dipToPixels(mContext, getNumOfColumns() * 10);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_list, parent, false);
            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: "+list.size());
        if (holder instanceof ViewHolder) {
            Log.d(TAG, "onBindViewHolder: instanceof");
            final ViewHolder viewHolder = (ViewHolder) holder;
            final HashMap<String, String> item = list.get(position);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.contentAreaView.getLayoutParams();
            params.width = width;
            params.height = width;
            viewHolder.contentAreaView.setLayoutParams(params);

            if (item.containsKey("add")){
                viewHolder.galleryImgViewadd.setVisibility(View.VISIBLE);
                viewHolder.cardArea.setVisibility(View.GONE);
                viewHolder.galleryImgViewadd.setOnClickListener(view -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position);

                    }
                });
            }else {
                viewHolder.cardArea.setVisibility(View.VISIBLE);
                viewHolder.galleryImgViewadd.setVisibility(View.GONE);
                if (isSel) {

                    if (item.get("isSel").equalsIgnoreCase("Yes")) {
                        viewHolder.selImage.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.selImage.setVisibility(View.GONE);
                    }

                }


                if (isDelete) {
                    viewHolder.deleteImgView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.deleteImgView.setVisibility(View.GONE);
                }


                Picasso.get()
                        .load(Utilities.getResizeImgURL(mContext, item.get("vImage"), params.width, params.height))
                        .placeholder(R.mipmap.ic_no_icon).error(R.mipmap.ic_no_icon)
                        .into(viewHolder.galleryImgView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e){
                            }
                        });


                viewHolder.deleteImgView.setOnClickListener(view -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onDeleteClick(view, position);
                    }
                });

                if (isSel) {
                    viewHolder.galleryImgView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if (item.get("isSel").equalsIgnoreCase("Yes")) {
                                mItemClickListener.onDeleteClick(view, position);
                            } else {
                                mItemClickListener.onLongItemClickList(view, position);
                            }
                            return false;
                        }
                    });
                }

            }
            viewHolder.galleryImgView.setOnClickListener(view -> {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickList(view, position);

                }
            });

        }
        else {
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
        if (isFooterEnabled) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    public void addFooterView() {
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null) {
            footerHolder.progressContainer.setVisibility(View.VISIBLE);
        }
    }

    public void removeFooterView() {
        if (footerHolder != null)
            footerHolder.progressContainer.setVisibility(View.GONE);
    }


    public interface OnItemClickListener {
        void onItemClickList(View v, int position);

        void onLongItemClickList(View v, int position);

        void onDeleteClick(View v, int position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public SelectableRoundedImageView galleryImgView;

        public View contentView;
        public View contentAreaView;
        public AppCompatImageView deleteImgView;
        ImageView selImage;
        ImageView galleryImgViewadd;
        CardView cardArea;


        public ViewHolder(View view) {
            super(view);
            contentView = view;
            contentAreaView = view.findViewById(R.id.contentAreaView);
            galleryImgView = (SelectableRoundedImageView) view.findViewById(R.id.galleryImgView);
            deleteImgView = (AppCompatImageView) view.findViewById(R.id.deleteImgView);
            selImage = (ImageView) view.findViewById(R.id.selImage);
            galleryImgViewadd = (ImageView) view.findViewById(R.id.galleryImgViewadd);
            cardArea = (CardView) view.findViewById(R.id.cardArea);

        }


    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressContainer;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressContainer = (LinearLayout) itemView.findViewById(R.id.progressContainer);

        }
    }

    public float getScreenDPWidth() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpWidth = (displayMetrics.widthPixels - Utils.dipToPixels(mContext, 10)) / displayMetrics.density; // 10 is for recycler view left-right margin
        return dpWidth;
    }

    public Integer getNumOfColumns() {
        int noOfColumns = (int) (getScreenDPWidth() / 130);
        return noOfColumns;
    }
}
