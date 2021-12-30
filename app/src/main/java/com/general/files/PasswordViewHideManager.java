package com.general.files;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;

import com.melevicarbrasil.usuario.R;
import com.utils.Logger;
import com.view.editBox.MaterialEditText;

public class PasswordViewHideManager {

    MaterialEditText inputBox;
    Context context;
    boolean isPassShow = false;
    GeneralFunctions generalFunctions;

    public PasswordViewHideManager(Context context, MaterialEditText inputBox, GeneralFunctions generalFunctions) {
        this.inputBox = inputBox;
        this.context = context;
        this.generalFunctions = generalFunctions;
        if (generalFunctions.isRTLmode()) {
            inputBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hide, 0, 0, 0);
        } else {
            inputBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_hide, 0);
        }
        ManageView();

    }

    public void ManageView() {

        inputBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (generalFunctions.isRTLmode()) {
                        Logger.d("x_Value", "::" + inputBox.getLeft() + ":::" + event.getRawX());
                        if (event.getX() <= (inputBox.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                            // if (event.getRawX() <= (inputBox.getLeft() + inputBox.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                            // your action here

                            if (isPassShow) {
                                isPassShow = false;
                                inputBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hide, 0, 0, 0);
                                inputBox.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            } else {
                                isPassShow = true;
                                inputBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view, 0, 0, 0);
                                inputBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                            }

                            return true;
                        }

                    } else {
                        Logger.d("x_Value", "::" + inputBox.getLeft() + ":::" + event.getRawX());
                        if (event.getRawX() >= (inputBox.getRight() - inputBox.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here

                            if (isPassShow) {
                                isPassShow = false;
                                inputBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_hide, 0);
                                inputBox.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            } else {
                                isPassShow = true;
                                inputBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                inputBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            }

                            return true;
                        }
                    }
                }
                return false;
            }
        });

    }
}
