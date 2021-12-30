package com.utils;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class DeviceData {
    public static String getDeviceData() {
        String PHONE_DETAILS_STR = "";
        try {
            HashMap<String, String> detailsOfMobile = new HashMap<>();
            detailsOfMobile.put("VERSION_RELEASE", "" + Build.VERSION.RELEASE);
            detailsOfMobile.put("VERSION_INCREMENTAL", "" + Build.VERSION.INCREMENTAL);
            detailsOfMobile.put("VERSION_SDK_NUMBER", "" + Build.VERSION.SDK_INT);
            detailsOfMobile.put("BOARD", "" + Build.BOARD);
            detailsOfMobile.put("BOOTLOADER", "" + Build.BOOTLOADER);
            detailsOfMobile.put("BRAND", "" + Build.BRAND);
            detailsOfMobile.put("CPU_ABI", "" + Build.CPU_ABI);
            detailsOfMobile.put("CPU_ABI2", "" + Build.CPU_ABI2);
            detailsOfMobile.put("DISPLAY", "" + Build.DISPLAY);
            detailsOfMobile.put("FINGERPRINT", "" + Build.FINGERPRINT);
            detailsOfMobile.put("HARDWARE", "" + Build.HARDWARE);
            detailsOfMobile.put("HOST", "" + Build.HOST);
            detailsOfMobile.put("ID", "" + Build.ID);
            detailsOfMobile.put("MANUFACTURER", "" + Build.MANUFACTURER);
            detailsOfMobile.put("MODEL", "" + Build.MODEL);
            detailsOfMobile.put("PRODUCT", "" + Build.PRODUCT);
            detailsOfMobile.put("SERIAL", "" + Build.SERIAL);
            detailsOfMobile.put("TAGS", "" + Build.TAGS);
            detailsOfMobile.put("TIME", "" + Build.TIME);
            detailsOfMobile.put("TYPE", "" + Build.TYPE);
            detailsOfMobile.put("UNKNOWN", "" + Build.UNKNOWN);
            detailsOfMobile.put("USER", "" + Build.USER);

            Gson gsonObject = (new GsonBuilder()).create();

            PHONE_DETAILS_STR = gsonObject.toJson(detailsOfMobile);
        } catch (Exception e) {

        }

        return PHONE_DETAILS_STR;
    }
}
