package com.viewholder;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melevicarbrasil.usuario.R;
import com.model.RestaurantCataParentModel;

public class RestaurntCataParentViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 270f;

    private TextView tvName;
    private TextView tvAge;
    private ImageView ivCollapse;
    private LinearLayout llItem;

    public RestaurntCataParentViewHolder(View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.textview_name);
        tvAge = (TextView) itemView.findViewById(R.id.textview_age);
        ivCollapse = (ImageView) itemView.findViewById(R.id.image_view_collapse);
        llItem = (LinearLayout) itemView.findViewById(R.id.restaurantAdptrLayout);
    }

    public void bind(RestaurantCataParentModel biodataHead) {

        tvName.setText(biodataHead.getName());



/*
        if (position % 2 == 0) {
            llItem.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            llItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
*/
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                ivCollapse.setImageResource(R.mipmap.ic_food_arrow__expand);
                ivCollapse.setRotation(180);
                tvName.setTextColor(Color.parseColor("#7cc830"));
            } else {
                ivCollapse.setImageResource(R.mipmap.ic_food_arrow__);
                tvName.setTextColor(Color.parseColor("#131313"));
                ivCollapse.setRotation(0);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(300);
            rotateAnimation.setFillAfter(true);
            ivCollapse.startAnimation(rotateAnimation);
        }
    }
}
