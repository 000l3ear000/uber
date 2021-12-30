package com.adapter.files;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.SearchLocationActivity;
import com.general.files.GeneralFunctions;
import com.model.Stop_Over_Points_Data;
import com.utils.Utils;
import com.view.DividerView;
import com.view.editBox.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 09-07-2016.
 */
public class StopOverPointsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public GeneralFunctions generalFunc;
    public int pos = -1; // initial pos value
    ArrayList<Stop_Over_Points_Data> list;
    Context mContext;
    boolean addTextChangeListner = false;
    View footerView;
    boolean selecetdViewList[];
    boolean isTouch = false; // is in edit mode
    String isRental = "";
    String iscubejekRental = "";
    boolean isFirstTime = false;
    String LBL_PICK_UP_FROM, LBL_STOP_OVER_TXT, LBL_DROP_AT;
    private onStopOverClickListener onStopOverClickListener;

    public StopOverPointsAdapter(Context mContext, ArrayList<Stop_Over_Points_Data> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.selecetdViewList = new boolean[list.size()];
        isRental = ((SearchLocationActivity) mContext).isRental;
        iscubejekRental = ((SearchLocationActivity) mContext).iscubejekRental;
        Arrays.fill(selecetdViewList, false);
        isFirstTime = true;
        LBL_PICK_UP_FROM = generalFunc.retrieveLangLBl("", "LBL_PICK_UP_FROM");
        LBL_STOP_OVER_TXT = generalFunc.retrieveLangLBl("", "LBL_STOP_OVER_TXT");
        LBL_DROP_AT = generalFunc.retrieveLangLBl("", "LBL_DROP_AT");
    }

    public StopOverPointsAdapter(Context mContext, ArrayList<Stop_Over_Points_Data> list, GeneralFunctions generalFunc, boolean isFooterEnabled, boolean addTextChangeListner) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.addTextChangeListner = addTextChangeListner;
        this.selecetdViewList = new boolean[list.size()];
        isRental = ((SearchLocationActivity) mContext).isRental;
        iscubejekRental = ((SearchLocationActivity) mContext).iscubejekRental;
        Arrays.fill(selecetdViewList, false);
        isFirstTime = true;
        LBL_PICK_UP_FROM = generalFunc.retrieveLangLBl("", "LBL_PICK_UP_FROM");
        LBL_STOP_OVER_TXT = generalFunc.retrieveLangLBl("", "LBL_STOP_OVER_TXT");
        LBL_DROP_AT = generalFunc.retrieveLangLBl("", "LBL_DROP_AT");
    }


    public void setOnItemClickListener(onStopOverClickListener onStopOverClickList) {
        this.onStopOverClickListener = onStopOverClickList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.design_stopover_locations, parent, false);
        return new ViewHolder(view);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final Stop_Over_Points_Data item = list.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;


        int listSize = list.size() - 1;

        pos = addTextChangeListner ? ((SearchLocationActivity) mContext).subSelectedPos : ((SearchLocationActivity) mContext).selectedPos;
        this.selecetdViewList = new boolean[list.size()];
        Arrays.fill(selecetdViewList, false);

        if (pos != -1) {
            selecetdViewList[pos] = true;

        }

        boolean isDestinationAdded = Utils.checkText(list.get(position).getDestAddress()) ;

        item.setHintLable(position == 0 ? LBL_PICK_UP_FROM : (position == listSize ? LBL_DROP_AT : LBL_STOP_OVER_TXT));

        if (position == pos && selecetdViewList[pos]) {

            //  viewHolder.stopOverPoint.setBackgroundColor(mContext.getResources().getColor(R.color.mspSelectedColor));
            viewHolder.stopOverLocationArea.setBackground(mContext.getResources().getDrawable(R.drawable.material_card));
            viewHolder.stopOverPoint.setTextColor(mContext.getResources().getColor(R.color.mspSelected));
            isTouch = true;
            viewHolder.stopOverPoint.setCursorVisible(true);
            viewHolder.stopOverPoint.setInputType(InputType.TYPE_CLASS_TEXT);
            viewHolder.stopOverPoint.setFocusableInTouchMode(true);
            viewHolder.stopOverPoint.setFocusable(true);
            if (addTextChangeListner) {
                ((SearchLocationActivity) mContext).materialEditText = viewHolder.stopOverPoint;
                Utils.showSoftKeyboard(mContext, viewHolder.stopOverPoint);
            }


        } else {

          //  viewHolder.stopOverPoint.setBackgroundColor(mContext.getResources().getColor(R.color.mspUnSelectedColor));
            viewHolder.stopOverLocationArea.setBackground(null);
            viewHolder.stopOverPoint.setTextColor(mContext.getResources().getColor(R.color.mspUnSelected));
            viewHolder.stopOverPoint.setCursorVisible(false);
            viewHolder.stopOverPoint.setInputType(0);
            viewHolder.stopOverPoint.setFocusableInTouchMode(false);
            viewHolder.stopOverPoint.setFocusable(false);
        }

        viewHolder.stopOverPoint.setTag(position);
        viewHolder.stopOverLocationArea.setTag(position);
        viewHolder.iv_removeStopPoint.setTag(position);
        viewHolder.removeArea.setTag(position);
        viewHolder.cancelArea.setTag(position);
        viewHolder.imageCancel.setTag(position);
        viewHolder.iv_addStopPoint.setTag(position);

        //  DrawableCompat.setTint(viewHolder.iv_round_img.getDrawable(), ContextCompat.getColor(mContext, R.color.highlightedColor));
        //  viewHolder.squareImgView.setBackgroundColor(mContext.getResources().getColor(R.color.highlightedColor));

        viewHolder.squareImgView.setVisibility(position==0 || position == listSize ? View.GONE : View.VISIBLE);
        viewHolder.iv_loc_img.setVisibility(position==0 ? View.VISIBLE : View.GONE);
        viewHolder.iv_round_img.setVisibility(position == listSize ? View.VISIBLE : View.GONE);
        viewHolder.aboveLine.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        viewHolder.lowerLine.setVisibility(position == listSize ? View.INVISIBLE : View.VISIBLE);
        viewHolder.iv_addStopPoint.setVisibility((isRental.equalsIgnoreCase("true") || iscubejekRental.equalsIgnoreCase("true")) ? View.GONE : (addTextChangeListner && position != 0 ? View.VISIBLE : /*addTextChangeListner ? View.INVISIBLE :*/ View.GONE));

        if (!addTextChangeListner) {
            viewHolder.removeArea.setVisibility(position == 0 ? View.INVISIBLE : (position == listSize ? View.INVISIBLE : (Utils.checkText(item.getDestAddress()) ? View.VISIBLE : (position == 1 ? View.VISIBLE : View.INVISIBLE))));
            viewHolder.stopOverPoint.setCursorVisible(false);
            viewHolder.stopOverPoint.setSelection(0);
            Utils.removeInput(viewHolder.stopOverPoint);
        } else {
            viewHolder.removeArea.setVisibility(View.GONE);
        }


        viewHolder.iv_addStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBounceAnimation(viewHolder.iv_addStopPoint, () -> {
                    if (onStopOverClickListener != null) {
                        onStopOverClickListener.onStopOverClickList(viewHolder.stopOverPoint, "AddBlankAddress", position);
                    }
                });
            }
        });


        viewHolder.removeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBounceAnimation(viewHolder.removeArea, () -> {
                    if (onStopOverClickListener != null) {
                        onStopOverClickListener.onStopOverClickList(viewHolder.stopOverPoint, "Remove", position);
                    }

                });
            }
        });


        if (addTextChangeListner) {

            viewHolder.cancelArea.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    setBounceAnimation(viewHolder.cancelArea, () -> {
                        ((SearchLocationActivity) mContext).subSelectedPos = position;
                        viewHolder.stopOverPoint.setText("");
                        v.setVisibility(View.GONE);
                        viewHolder.imageCancel.setVisibility(View.GONE);
                    });
                    return true;
                }
            });


            viewHolder.stopOverPoint.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s != null) {

                        String searchAddress = s.toString().trim();
                        String destTempAddress = list.get(position).getDestTempAddress();
                        boolean isTempLoc = list.get(position).isTempLoc();

                        if (isTouch && pos == position) {
                            if (isTempLoc && Utils.checkText(destTempAddress) && !(destTempAddress.equalsIgnoreCase(s.toString()))) {
                                ((SearchLocationActivity) mContext).searchSourceOrDest(viewHolder.stopOverPoint.getText().toString(), position);
                            }
                            else if (searchAddress != null && !searchAddress.equalsIgnoreCase("")){
                                ((SearchLocationActivity) mContext).searchSourceOrDest(searchAddress, position);
                            }
                            else if (!(destTempAddress.equalsIgnoreCase(s.toString()))) {
                                ((SearchLocationActivity) mContext).searchSourceOrDest(destTempAddress, position);
                            }
                            list.get(position).setDestTempAddress(s.toString());

                        }
                    }


                }
            });

            viewHolder.stopOverPoint.setOnEditorActionListener((v, actionId, event) -> {
                ((SearchLocationActivity) mContext).subSelectedPos = position;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((SearchLocationActivity) mContext).getSearchGooglePlace(v.getText().toString(), position);
                    return true;
                }
                return false;
            });

        }


        viewHolder.stopOverPoint.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (addTextChangeListner) {
                    ((SearchLocationActivity) mContext).selectedPos = position;

                } else {
                    ((SearchLocationActivity) mContext).subSelectedPos = position;
                }

                isTouch = true;
               // showCancelButton(viewHolder);
                setBounceAnimation(viewHolder.stopOverPoint, () -> {

                    if (onStopOverClickListener != null) {
                        onStopOverClickListener.onStopOverClickList(viewHolder.stopOverPoint, "", position);
                    }
                });

                return false;
            }
        });


        boolean addTempLocation = addTextChangeListner && item.isTempLoc() && Utils.checkText(item.getDestTempAddress());


        String hintText = item.getHintLable();

        viewHolder.stopOverPoint.setHint(hintText /*addTempLocation ? (Utils.checkText(item.getDestTempAddress()) ? hintText : item.getDestTempAddress()) : (Utils.checkText(item.getDestAddress()) ? item.getDestAddress() : hintText)*/);

        String setText=addTempLocation ? item.getDestTempAddress() : item.getDestAddress();
        viewHolder.stopOverPoint.setText(setText);

        if (addTextChangeListner) {
            viewHolder.stopOverPoint.setSelection(position == pos && selecetdViewList[pos]?viewHolder.stopOverPoint.getText().length():0);
            viewHolder.cancelArea.setVisibility(position == pos && selecetdViewList[pos] && Utils.checkText(viewHolder.stopOverPoint) ? View.VISIBLE : View.GONE);
            viewHolder.imageCancel.setVisibility(position == pos && selecetdViewList[pos] && Utils.checkText(viewHolder.stopOverPoint) ? View.VISIBLE : View.GONE);
        }else
        {
            viewHolder.cancelArea.setVisibility(View.GONE);
            viewHolder.imageCancel.setVisibility(View.GONE);
        }

        showCancelButton(viewHolder);

    }

    private void showCancelButton(ViewHolder viewHolder) {
        boolean isRtlMode = generalFunc.isRTLmode();

        viewHolder.btnArea.setVisibility(View.VISIBLE);
        if (viewHolder.cancelArea.getVisibility() == View.VISIBLE) {
//            int padding = (int) mContext.getResources().getDimension(R.dimen._15sdp);
            int padding = 0;
            int padding1 = (int) mContext.getResources().getDimension(R.dimen._10sdp);

            int left=isRtlMode ? padding : (!isRtlMode ? padding1 : 0);
            int right=!isRtlMode ? padding : (isRtlMode ? padding1 : 0);


            viewHolder.stopOverPoint.setPadding(left, padding1, right, padding1);

        } else if (viewHolder.removeArea.getVisibility() == View.VISIBLE) {
 //            int padding = (int) mContext.getResources().getDimension(R.dimen._30sdp);
            int padding = 0;
            int padding1 = viewHolder.stopOverLocationArea.getBackground()!=null?(int) mContext.getResources().getDimension(R.dimen._10sdp):(int) mContext.getResources().getDimension(R.dimen._5sdp);

            int left=isRtlMode ? padding : (!isRtlMode ? padding1 : 0);
//            int right=!isRtlMode ? padding : (isRtlMode ? padding1 : 0);
            int right=0;

            viewHolder.stopOverPoint.setPadding(left, padding1, right, padding1);

        }else if (viewHolder.iv_addStopPoint.getVisibility() == View.VISIBLE) {
//            int padding = (int) mContext.getResources().getDimension(R.dimen._30sdp);
            int padding = 0;
            int padding1 = viewHolder.stopOverLocationArea.getBackground()!=null?(int) mContext.getResources().getDimension(R.dimen._10sdp):(int) mContext.getResources().getDimension(R.dimen._5sdp);

            int left=isRtlMode ? padding : (!isRtlMode ? padding1 : 0);
            int right=!isRtlMode ? padding : (isRtlMode ? padding1 : 0);


            viewHolder.stopOverPoint.setPadding(left, padding1, right, padding1);

        }
        else {
            int padding1 = viewHolder.stopOverLocationArea.getBackground()!=null?(int) mContext.getResources().getDimension(R.dimen._10sdp):0;
//            int left =!isRtlMode ? padding1 : 0;
//            int right =!isRtlMode ? 0 : padding1;
            viewHolder.btnArea.setVisibility(View.GONE);
            viewHolder.stopOverPoint.setPadding(padding1, 0, padding1, 0);
        }

    }


    private void setBounceAnimation(View view, BounceAnimListener bounceAnimListener) {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (bounceAnimListener != null) {
                    bounceAnimListener.onAnimationFinished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();

    }


    public interface onStopOverClickListener {
        void onStopOverClickList(MaterialEditText materialEditText, String type, int position);
    }

    private interface BounceAnimListener {
        void onAnimationFinished();
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {


        public MaterialEditText stopOverPoint;
        private ImageView iv_round_img,iv_loc_img, imageCancel, iv_addStopPoint, iv_removeStopPoint;
        private DividerView lowerLine, aboveLine;
        private View squareImgView;
        private LinearLayout cancelArea;
        private LinearLayout stopOverLocationArea;
        private LinearLayout btnArea;
        private LinearLayout removeArea;


        public ViewHolder(View view) {
            super(view);

            iv_addStopPoint = (ImageView) view.findViewById(R.id.iv_addStopPoint);
            iv_removeStopPoint = (ImageView) view.findViewById(R.id.iv_removeStopPoint);
            stopOverLocationArea = (LinearLayout) view.findViewById(R.id.stopOverLocationArea);
            btnArea = (LinearLayout) view.findViewById(R.id.btnArea);
            iv_round_img = (ImageView) view.findViewById(R.id.iv_round_img);
            iv_loc_img = (ImageView) view.findViewById(R.id.iv_loc_img);
            imageCancel = (ImageView) view.findViewById(R.id.imageCancel);
            aboveLine = (DividerView) view.findViewById(R.id.aboveLine);
            lowerLine = (DividerView) view.findViewById(R.id.lowerLine);
            squareImgView = (View) view.findViewById(R.id.squareImgView);
            stopOverPoint = (MaterialEditText) view.findViewById(R.id.stopOverPoint);
            cancelArea = (LinearLayout) view.findViewById(R.id.cancelArea);
            removeArea = (LinearLayout) view.findViewById(R.id.removeArea);
        }
    }

}
