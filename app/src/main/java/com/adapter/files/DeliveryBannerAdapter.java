package com.adapter.files;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 02-03-2017.
 */
public class DeliveryBannerAdapter extends RecyclerView.Adapter<DeliveryBannerAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> list_item;
    private Context context;
    OnBannerItemClickList onItemClickList;
    int height;
    GeneralFunctions generalFunctions;

    public DeliveryBannerAdapter(Context context, ArrayList<HashMap<String, String>> list_item) {
        this.context = context;
        this.list_item = list_item;
        height = (int) (((Utils.getScreenPixelWidth(context) - Utils.dipToPixels(context, 10)) / 1.77777778));
        generalFunctions = new GeneralFunctions(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_banner_design, parent, false);
        DeliveryBannerAdapter.ViewHolder viewHolder = new DeliveryBannerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> mapData = list_item.get(position);

        String vBannerImage = mapData.get("vImage");

        if (vBannerImage != null && !vBannerImage.isEmpty()) {
            Picasso.get().load(vBannerImage)
                    .placeholder(R.mipmap.ic_no_icon)
                    // .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .fit().into(holder.bannerImgView);
        } else {
            Picasso.get().load("ss")
                    //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.mipmap.ic_no_icon).fit().into(holder.bannerImgView);

        }

        holder.serviceNameTxt.setText(mapData.get("vCategory"));


        holder.bookNowTxt.setText(mapData.get("tBannerButtonText"));

        CardView.LayoutParams layoutParams = (CardView.LayoutParams) holder.bannerAreaContainerView.getLayoutParams();

        layoutParams.height = height;

        holder.bannerAreaContainerView.setLayoutParams(layoutParams);

        int btnRadius = Utils.dipToPixels(context, 8);
        int strokeWidth = Utils.dipToPixels(context, 0);
        int color1 = context.getResources().getColor(R.color.black);
        color1 = context.getResources().getColor(R.color.black);

        new CreateRoundedView(R.color.white, btnRadius, strokeWidth, R.color.white, holder.serviceNameTxt);
        new CreateRoundedView(R.color.white, btnRadius, strokeWidth, R.color.white, holder.bookNowTxt);

        holder.bannerImgView.setOnClickListener(v -> {
            if (onItemClickList != null) {
                onItemClickList.onBannerItemClick(position);
            }
        });

        if (generalFunctions.isRTLmode()) {
            holder.bookNowImg.setRotation(180);
        }

    }


    public void setOnItemClickList(OnBannerItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnBannerItemClickList {
        void onBannerItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public View contentArea;
        public View bannerAreaContainerView;
        public SelectableRoundedImageView bannerImgView;
        public MTextView bookNowTxt, serviceNameTxt;
        ImageView bookNowImg;

        public ViewHolder(View view) {
            super(view);

            bannerImgView = (SelectableRoundedImageView) view.findViewById(R.id.bannerImgView);
            bookNowTxt = (MTextView) view.findViewById(R.id.bookNowTxt);
            serviceNameTxt = (MTextView) view.findViewById(R.id.serviceNameTxt);
            bannerAreaContainerView = view.findViewById(R.id.bannerAreaContainerView);
            bookNowImg = view.findViewById(R.id.bookNowImg);
        }
    }


}