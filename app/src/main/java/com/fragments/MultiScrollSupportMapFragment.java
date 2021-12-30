package com.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Admin on 11-07-2016.
 */
public class MultiScrollSupportMapFragment extends SupportMapFragment {
    private OnTouchListener mListener;
    private OnTouchListener mDragListener;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance) {
        View layout = super.onCreateView(layoutInflater, viewGroup, savedInstance);

        TouchableWrapper frameLayout = new TouchableWrapper(getActivity());

        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        ((ViewGroup) layout).addView(frameLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return layout;
    }

    public void setListener(OnTouchListener listener) {
        mListener = listener;
    }

    public void setOnDragListener(OnDragListener mOnDragListener) {
        this.mOnDragListener = mOnDragListener;
    }

    public interface OnDragListener {
        public void onDrag(MotionEvent motionEvent);
    }

    private OnDragListener mOnDragListener;


    public interface OnTouchListener {
        public abstract void onTouch(boolean b);

    }


    public class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }


        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {

            if (mOnDragListener != null) {
                mOnDragListener.onDrag(event);
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mListener.onTouch(false);
            } else {
                mListener.onTouch(true);
            }

            return super.dispatchTouchEvent(event);
        }
    }
}
