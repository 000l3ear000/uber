package com.general.files;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;


import com.melevicarbrasil.usuario.R;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import java.util.Objects;

public class SuccessDialog extends AppCompatDialog implements View.OnClickListener {

    private String message = "";
    private String messageNote = "";
    private String setButtonText = "";
    private String setCancelButtonText = "";
    private OnClickList clickListener = null;
    private OnCancelList onCancelList = null;

    public SuccessDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_success_dialog);

        MTextView messageTextView = (MTextView) findViewById(R.id.messageTextView);
        MTextView messageNoteTextView = (MTextView) findViewById(R.id.messageNoteTextView);
        MTextView cancelTxt = (MTextView) findViewById(R.id.cancelTxt);
        messageTextView.setText(message);
        MButton btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setOnClickListener(this);

        if (onCancelList != null || Utils.checkText(setCancelButtonText))
        {
            cancelTxt.setText(setCancelButtonText);
            cancelTxt.setVisibility(View.VISIBLE);
        }

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                if (onCancelList != null) {
                    onCancelList.onCancel();
                }
            }
        });
        btn_type2.setText(setButtonText);

        if (messageNote.equalsIgnoreCase("")) {
            messageNoteTextView.setVisibility(View.GONE);
        } else {
            messageNoteTextView.setVisibility(View.VISIBLE);
            messageNoteTextView.setText(messageNote);
        }

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageNote(String messageNote) {
        this.messageNote = messageNote;
    }

    public void setButtonText(String ButtonText) {
        this.setButtonText = ButtonText;
    }

    public void setCancelButtonText(String setCancelButtonText) {
        this.setCancelButtonText = setCancelButtonText;
    }

    public static SuccessDialog showSuccessDialog(Context mContext, String message, String messageNote, String buttonText, boolean isCancelable, OnClickList clickListener) {

        SuccessDialog successDialog = new SuccessDialog(mContext);

        successDialog.setCancelable(isCancelable);
        successDialog.setMessage(message);
        successDialog.setMessageNote(messageNote);
        successDialog.setButtonText(buttonText);
        successDialog.clickListener = clickListener;
        successDialog.show();
        Window window = successDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return successDialog;
    }


    public static SuccessDialog showSuccessDialog(Context mContext, String message, String messageNote, String buttonText,String cancelText, boolean isCancelable, OnClickList clickListener,OnCancelList onCancelList) {

        SuccessDialog successDialog = new SuccessDialog(mContext);

        successDialog.setCancelable(isCancelable);
        successDialog.setMessage(message);
        successDialog.setMessageNote(messageNote);
        successDialog.setButtonText(buttonText);
        successDialog.setCancelButtonText(cancelText);
        successDialog.clickListener = clickListener;
        successDialog.onCancelList = onCancelList;
        successDialog.show();
        Window window = successDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return successDialog;
    }


    @Override
    public void onClick(View v) {
        dismiss();

        if (clickListener != null) {
            clickListener.onClick();
        }
    }


    public interface OnClickList {
        void onClick();
    }

    public interface OnCancelList {
        void onCancel();
    }
}
