package com.adapter.files;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.melevicarbrasil.usuario.R;
import com.general.files.AppFunctions;
import com.general.files.GeneralFunctions;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 03-07-18.
 */

public class ApplyCouponAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    View footerView;
    FooterViewHolder footerHolder;
    private OnItemClickListener mItemClickListener;
    private int currSelectedPosition = -1;

    private RecyclerView mRecyclerView;
    private String appliedCouponCode = "";

    public ApplyCouponAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, String appliedCouponCode, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.appliedCouponCode = appliedCouponCode;
        this.mRecyclerView = mRecyclerView;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apply_coupon, parent, false);
            return new ViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            String vCouponCode=item.get("vCouponCode");

            viewHolder.useCodeVTxt.setText(vCouponCode);
            viewHolder.tDescriptionVTxt.setText(item.get("tDescription"));

            if (vCouponCode.equalsIgnoreCase(appliedCouponCode)) {

                if (currSelectedPosition == -1) {
                    currSelectedPosition = position;
                }
                viewHolder.imgRightMark.setVisibility(View.VISIBLE);
                viewHolder.btn_type2.setText(item.get("LBL_REMOVE_TEXT"));
                viewHolder.useCodeHTxt.setVisibility(View.GONE);
                viewHolder.useCodeHTxt.setText(item.get("LBL_USE_CODE_TXT"));
                viewHolder.couponCodeArea.setPadding(10, 15, 10, 15);

            } else {
                viewHolder.imgRightMark.setVisibility(View.GONE);
                viewHolder.btn_type2.setText(item.get("LBL_APPLY"));
                viewHolder.useCodeHTxt.setVisibility(View.VISIBLE);
            }


            String attrString1 = item.get("LBL_USE_AND_SAVE_LBL");

            boolean isCash = item.get("eType").equals("cash");
            String currencySymbol = isCash ? " " + item.get("vSymbol") + "" : " ";
            String fDiscount = isCash ? item.get("fDiscount") + "" : item.get("fDiscount") + "%";


            String htmlString = "<font color=\"" + mContext.getResources().getColor(R.color.appThemeColor_2) + "\">" + attrString1 + "</font>" +
                    "<font color=\"" + mContext.getResources().getColor(R.color.appThemeColor_1) + "\">" + currencySymbol + fDiscount + "</font>";

            SpannableString spannableString = new SpannableString(htmlString);
            viewHolder.useAmountVTxt.setText(AppFunctions.fromHtml("" + spannableString));

            if (generalFunc.isRTLmode()) {
                if (viewHolder.layoutZigzag.getRotation() != 180) {
                    viewHolder.layoutZigzag.setRotation(180);
                }
            }


            if (!item.get("eValidityType").equalsIgnoreCase("PERMANENT")) {
                viewHolder.dExpiryDateVTxt.setVisibility(View.VISIBLE);
                SpannableString text = new SpannableString(item.get("LBL_VALID_TILL_TXT") + " " + item.get("dExpiryDate"));
                viewHolder.dExpiryDateVTxt.setText(text);
            } else {
                viewHolder.dExpiryDateVTxt.setVisibility(View.GONE);
            }


            viewHolder.btn_type2.setOnClickListener(view -> {
                if (vCouponCode.equalsIgnoreCase(appliedCouponCode)) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickListRemoveCode(view, position, "useCode");
                    }
                } else {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickList(view, position);
                    }
                }
            });


            if (currSelectedPosition == -1 || currSelectedPosition != position) {
                viewHolder.indicatorImg.setImageResource(R.mipmap.ic_arrow_down);
                viewHolder.detailArea.setVisibility(View.GONE);
            } else {
                viewHolder.indicatorImg.setImageResource(R.mipmap.ic_arrow_up);
                viewHolder.detailArea.setVisibility(View.VISIBLE);
            }


            viewHolder.promoArea.setOnClickListener(view -> {

                if (currSelectedPosition == position) {
                    currSelectedPosition = -1;
                    if (viewHolder.detailArea.getVisibility() == View.GONE) {
                        viewHolder.indicatorImg.setImageResource(R.mipmap.ic_arrow_up);
                        viewHolder.detailArea.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.indicatorImg.setImageResource(R.mipmap.ic_arrow_down);
                        viewHolder.detailArea.setVisibility(View.GONE);
                    }

                    return;
                }
                if (viewHolder.detailArea.getVisibility() == View.GONE) {
                    currSelectedPosition = position;
                    viewHolder.indicatorImg.setImageResource(R.mipmap.ic_arrow_up);
                    viewHolder.detailArea.setVisibility(View.VISIBLE);

                    mRecyclerView.smoothScrollToPosition(position);

                } else {
                    currSelectedPosition = -1;
                    viewHolder.indicatorImg.setImageResource(R.mipmap.ic_arrow_down);
                    viewHolder.detailArea.setVisibility(View.GONE);
                }

                Utils.hideKeyboard(mContext);

                notifyDataSetChanged();
            });


        } else {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClickList(View v, int position);

        void onItemClickListRemoveCode(View v, int position, String string);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MButton btn_type2;
        public MTextView useCodeVTxt;
        public MTextView useAmountVTxt;
        public MTextView tDescriptionVTxt;
        public MTextView dExpiryDateVTxt;
        public LinearLayout promoArea;
        public LinearLayout detailArea;
        public ImageView indicatorImg;
        public LinearLayout layoutZigzag;
        public LinearLayout saveArea;
        public ImageView imgRightMark;
        public MTextView useCodeHTxt;
        public LinearLayout couponCodeArea;

        public ViewHolder(View view) {
            super(view);

            useCodeVTxt = (MTextView) view.findViewById(R.id.useCodeVTxt);
            useAmountVTxt = (MTextView) view.findViewById(R.id.useAmountVTxt);
            tDescriptionVTxt = (MTextView) view.findViewById(R.id.tDescriptionVTxt);
            dExpiryDateVTxt = (MTextView) view.findViewById(R.id.dExpiryDateVTxt);
            btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();
            layoutZigzag = (LinearLayout) view.findViewById(R.id.layoutZigzag);
            saveArea = (LinearLayout) view.findViewById(R.id.saveArea);
            promoArea = (LinearLayout) view.findViewById(R.id.promoArea);
            detailArea = (LinearLayout) view.findViewById(R.id.detailArea);
            indicatorImg = (ImageView) view.findViewById(R.id.indicatorImg);
            imgRightMark = (ImageView) view.findViewById(R.id.imgRightMark);
            useCodeHTxt = (MTextView) view.findViewById(R.id.useCodeHTxt);
            couponCodeArea = (LinearLayout) view.findViewById(R.id.couponCodeArea);

            btn_type2.setId(Utils.generateViewId());
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
