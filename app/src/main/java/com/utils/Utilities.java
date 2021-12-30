package com.utils;

import android.content.Context;

public class Utilities {

    public static String getResizeImgURL(final Context mContext, String imgUrl, final int width, final int height) {
        final String retrieveValue = CommonUtilities.SERVER_URL;
        imgUrl = imgUrl.replace(" ", "%20");
        return (retrieveValue.endsWith("/") ? retrieveValue : (retrieveValue + "/")) + "resizeImg.php?src=" + imgUrl + "&w=" + width + "&h=" + height;
    }

    public static String getResizeImgURL(final Context mContext, String imgUrl, final int width, final int height, final int MAX_WIDTH) {
        final String retrieveValue = CommonUtilities.SERVER_URL;
        imgUrl = imgUrl.replace(" ", "%20");
        return (retrieveValue.endsWith("/") ? retrieveValue : (retrieveValue + "/")) + "resizeImg.php?src=" + imgUrl + "&w=" + width + "&h=" + height + "&IMG_MAX_WIDTH=" + MAX_WIDTH;
    }

    public static String getResizeImgURL(final Context mContext, String imgUrl, final int width, final int height, final float MAX_HEIGHT) {
        final String retrieveValue = CommonUtilities.SERVER_URL;
        imgUrl = imgUrl.replace(" ", "%20");
        return (retrieveValue.endsWith("/") ? retrieveValue : (retrieveValue + "/")) + "resizeImg.php?src=" + imgUrl + "&w=" + width + "&h=" + height + "&IMG_MAX_HEIGHT=" + MAX_HEIGHT;
    }

    public static String getResizeImgURL(final Context mContext, String imgUrl, final int width, final int height, final int MAX_WIDTH, final int MAX_HEIGHT) {
        final String retrieveValue = CommonUtilities.SERVER_URL;
        imgUrl = imgUrl.replace(" ", "%20");
        return (retrieveValue.endsWith("/") ? retrieveValue : (retrieveValue + "/")) + "resizeImg.php?src=" + imgUrl + "&w=" + width + "&h=" + height + "&IMG_MAX_WIDTH=" + MAX_WIDTH + "&IMG_MAX_HEIGHT=" + MAX_HEIGHT;
    }
}
