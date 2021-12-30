package com.adapter.files.deliverAll;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.deliverAll.RestaurantAllDetailsNewActivity;
import com.general.files.GeneralFunctions;
import com.squareup.picasso.Picasso;
import com.view.CreateRoundedView;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantRecomMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_GRID = 1;
    private final int TYPE_LIST = 2;

    ArrayList<HashMap<String, String>> list;
    Context mContext;
    GeneralFunctions generalFunc;
    private OnItemClickListener mItemClickListener;
    int cnt = 0;
    int grayColor = -1;
    Drawable noIcon = null;
    RestaurantAllDetailsNewActivity restAllDetailsNewAct;
    int imageBackColor;
    boolean isRTLmode = false;

    public RestaurantRecomMenuAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;

        if (mContext instanceof RestaurantAllDetailsNewActivity) {
            restAllDetailsNewAct = (RestaurantAllDetailsNewActivity) mContext;
        }

        grayColor = mContext.getResources().getColor(R.color.gray);
        imageBackColor = mContext.getResources().getColor(R.color.appThemeColor_1);
        noIcon = mContext.getResources().getDrawable(R.mipmap.ic_no_icon);
        isRTLmode = generalFunc.isRTLmode();
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resmenu_gridview, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HashMap<String, String> mapData = list.get(position);

        GridViewHolder grideholder = (GridViewHolder) holder;
        grideholder.title.setText(mapData.get("vItemType"));
        grideholder.title.setSelected(true);

        String eFoodType = mapData.get("eFoodType");

        if (eFoodType.equals("NonVeg")) {
            grideholder.nonVegImage.setVisibility(View.VISIBLE);
            grideholder.vegImage.setVisibility(View.GONE);
        } else if (eFoodType.equals("Veg")) {
            grideholder.nonVegImage.setVisibility(View.GONE);
            grideholder.vegImage.setVisibility(View.VISIBLE);
        }

        if (mapData.get("prescription_required").equalsIgnoreCase("Yes")) {
            grideholder.presImage.setVisibility(View.VISIBLE);
        } else {
            grideholder.presImage.setVisibility(View.GONE);
        }

        if (mapData.get("fOfferAmtNotZero").equalsIgnoreCase("Yes")) {
            grideholder.price.setText(mapData.get("StrikeoutPriceConverted"));
            grideholder.price.setPaintFlags(grideholder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            grideholder.price.setTextColor(grayColor);
            grideholder.offerPrice.setText(mapData.get("fDiscountPricewithsymbolConverted"));
            grideholder.offerPrice.setVisibility(View.VISIBLE);
        } else {
            grideholder.price.setText(mapData.get("StrikeoutPriceConverted"));
            grideholder.price.setPaintFlags(0);
            grideholder.offerPrice.setVisibility(View.GONE);
        }

        String vImage = mapData.get("vImageResized");
        boolean isBlank = (vImage != null && !TextUtils.isEmpty(vImage));

        if (!isBlank) {
            grideholder.menuImage.setVisibility(View.VISIBLE);
        }

        Picasso.get()
                .load(isBlank ? vImage : "https://www.test.com/ghg").placeholder(R.mipmap.ic_no_icon).error(noIcon)
                .into(grideholder.menuImage);

       //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) grideholder.menuImage.getLayoutParams();
       //layoutParams.height = GeneralFunctions.parseIntegerValue(0, mapData.get("heightOfImage"));
       //grideholder.menuImage.setLayoutParams(layoutParams);

        grideholder.addBtn.setText(mapData.get("LBL_ADD"));

        new CreateRoundedView(imageBackColor, 5, 0, 0, grideholder.addBtn);
        grideholder.addBtn.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onRecomItemClickList(grideholder.addBtn, position, false);
            }
        });

        grideholder.menuImage.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onRecomItemClickList(grideholder.menuImage, position, true);
            }
        });

        if (isRTLmode) {
            grideholder.tagImage.setRotation(180);
            grideholder.tagTxt.setPadding(10, 15, 0, 0);
        }
        String vHighlightName = mapData.get("vHighlightName");
        if (vHighlightName != null && !vHighlightName.equals("")) {
            grideholder.tagImage.setVisibility(View.VISIBLE);
            grideholder.tagTxt.setVisibility(View.VISIBLE);
            grideholder.tagTxt.setText(vHighlightName);
        } else {
            grideholder.tagImage.setVisibility(View.GONE);
            grideholder.tagTxt.setVisibility(View.GONE);
        }
        grideholder.vCategoryNameTxt.setText(mapData.get("vCategoryName"));
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView vegImage, nonVegImage, presImage;
        MTextView title;
        MTextView price, offerPrice;
        MTextView addBtn;
        ImageView tagImage;
        MTextView tagTxt;
        SelectableRoundedImageView menuImage;
        MTextView vCategoryNameTxt;
        LinearLayout main_layout;

        public GridViewHolder(View view) {
            super(view);
            menuImage = (SelectableRoundedImageView) view.findViewById(R.id.menuImage);
            vegImage = (ImageView) view.findViewById(R.id.vegImage);
            nonVegImage = (ImageView) view.findViewById(R.id.nonVegImage);
            presImage = (ImageView) view.findViewById(R.id.presImage);
            title = (MTextView) view.findViewById(R.id.title);
            price = (MTextView) view.findViewById(R.id.price);
            offerPrice = (MTextView) view.findViewById(R.id.offerPrice);
            addBtn = (MTextView) view.findViewById(R.id.addBtn);
            tagImage = (ImageView) view.findViewById(R.id.tagImage);
            tagTxt = (MTextView) view.findViewById(R.id.tagTxt);
            vCategoryNameTxt = (MTextView) view.findViewById(R.id.vCategoryNameTxt);
            main_layout = view.findViewById(R.id.main_layout);
            // new CreateRoundedView(Color.parseColor("#ffffff"),30,2,Color.parseColor("#cfcfcf"),main_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_GRID;
    }

    public interface OnItemClickListener {
        void onRecomItemClickList(View v, int position, boolean openGrid);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
