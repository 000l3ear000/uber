package com.adapter.files;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
public class DonationBannerAdapter extends RecyclerView.Adapter<DonationBannerAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> list_item;
    private LayoutInflater inflater;
    private Context context;
    OnBannerItemClickList onItemClickList;
    GeneralFunctions generalFunctions;

    public DonationBannerAdapter(Context context, ArrayList<HashMap<String, String>> list_item) {
        this.context = context;
        this.list_item = list_item;
        inflater = LayoutInflater.from(context);
        generalFunctions=new GeneralFunctions(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_banner_design, parent, false);

        DonationBannerAdapter.ViewHolder viewHolder = new DonationBannerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> mapData = list_item.get(position);


        String vBannerImage = mapData.get("vImage");
        //bug_003 start
        if (!vBannerImage.equalsIgnoreCase("")) {

            Picasso.get().load(vBannerImage)
                    .placeholder(R.mipmap.ic_no_icon)
                    //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .fit().into(holder.bannerImgView);
        } else {
            Picasso.get().load("ss")
                    //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.mipmap.ic_no_icon).fit().into(holder.bannerImgView);

        }


        holder.noteTxt.setText(mapData.get("tDescription"));


        holder.titleTxt.setText(mapData.get("tTitle"));
        holder.bookNowTxt.setText(generalFunctions.retrieveLangLBl("","LBL_DONATE_NOW"));
        new CreateRoundedView(context.getResources().getColor(R.color.donateBtn), Utils.dipToPixels(context, 8), Utils.dipToPixels(context, 0), context.getResources().getColor(R.color.donateBtn), holder.bookNowTxt);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.bannerImgView.getLayoutParams();

        layoutParams.height = (int) (((Utils.getScreenPixelWidth(context) - Utils.dipToPixels(context, 20)) / 1.77777778));
        holder.bannerImgView.setLayoutParams(layoutParams);

        holder.seperatorView.setVisibility(View.VISIBLE);

        if (list_item != null && position == list_item.size() - 1) {
            holder.seperatorView.setVisibility(View.GONE);
        }


        holder.bookNowTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickList != null) {
                    onItemClickList.onBannerItemClick(position);
                }
            }
        });

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
        public CardView containerView;
        public View seperatorView;
        public View bannerAreaContainerView;
        public SelectableRoundedImageView bannerImgView;
        public MTextView titleTxt, noteTxt,bookNowTxt;

        public ViewHolder(View view) {
            super(view);

            bannerImgView = (SelectableRoundedImageView) view.findViewById(R.id.bannerImgView);
            titleTxt = (MTextView) view.findViewById(R.id.titleTxt);
            noteTxt = (MTextView) view.findViewById(R.id.noteTxt);
            bookNowTxt = (MTextView) view.findViewById(R.id.bookNowTxt);
            bannerAreaContainerView = view.findViewById(R.id.bannerAreaContainerView);
            seperatorView = view.findViewById(R.id.seperatorView);
            containerView = view.findViewById(R.id.containerView);
        }
    }


}