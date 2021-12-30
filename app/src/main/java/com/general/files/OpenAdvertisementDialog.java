package com.general.files;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.melevicarbrasil.usuario.R;
import com.utils.Utils;

import java.util.HashMap;

public class OpenAdvertisementDialog {

    Context mContext;
    HashMap<String, String> data;
    GeneralFunctions generalFunc;

    androidx.appcompat.app.AlertDialog alertDialog;

    public OpenAdvertisementDialog(Context mContext, HashMap<String, String> data, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.data = data;
        this.generalFunc = generalFunc;

        show();
    }


    public void show() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.theme_advertise_dialog);
        builder.setTitle("");

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.advertisement_dailog, null);
        dialogView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        builder.setView(dialogView);

        ImageView bannerImage = (ImageView) dialogView.findViewById(R.id.bannerImage);
        View advBannerArea = dialogView.findViewById(R.id.advBannerArea);
        View mProgressBar = dialogView.findViewById(R.id.mProgressBar);

        if (data.get("vImageWidth") != null && data.get("vImageHeight") != null) {
            double vImageWidth = GeneralFunctions.parseIntegerValue((int) Utils.getScreenPixelWidth(mContext), data.get("vImageWidth"));
            double vImageHeight = GeneralFunctions.parseIntegerValue((int) Utils.getScreenPixelHeight(mContext), data.get("vImageHeight"));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advBannerArea.getLayoutParams();
            params.width = (int) vImageWidth;
            params.height = (int) vImageHeight;

            advBannerArea.setLayoutParams(params);
        }

        String image_url = data.get("image_url");
        if (image_url != null && !image_url.equalsIgnoreCase("")) {
            mProgressBar.setVisibility(View.VISIBLE);

            RequestBuilder imgRequestBuilder;
            if(data.get("IS_GIF_IMAGE") != null && data.get("IS_GIF_IMAGE").equalsIgnoreCase("Yes")){
                imgRequestBuilder = Glide.with(mContext).asGif();
            }else{
                imgRequestBuilder = Glide.with(mContext).asDrawable();
            }

            imgRequestBuilder.load(image_url).listener(new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    mProgressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    mProgressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(bannerImage);

            /*Picasso.get()
                    .load(image_url)
                    .placeholder(R.mipmap.ic_no_icon)
                    .error(R.mipmap.ic_no_icon)
                    .into(bannerImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e){
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });*/
        }

        (dialogView.findViewById(R.id.cancelBtn)).setOnClickListener(view -> {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        });

        (dialogView.findViewById(R.id.bannerImage)).setOnClickListener(view -> {
            try {
                if (data.get("tRedirectUrl") != null && !data.get("tRedirectUrl").equalsIgnoreCase("")) {
                    String redirect_url = data.get("tRedirectUrl");
                    (new StartActProcess(mContext)).openURL(redirect_url);
                }

                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            } catch (Exception e) {

            }
        });


        alertDialog = builder.create();
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog);
        }
        try {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } catch (Exception e) {

        }
        alertDialog.show();

    }


}
