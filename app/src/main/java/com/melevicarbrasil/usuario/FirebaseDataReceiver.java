package com.melevicarbrasil.usuario;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.general.files.GeneralFunctions;
import com.general.files.MyApp;

/**
 * Created by Admin on 09-08-2017.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        GeneralFunctions generalFunctions = MyApp.getInstance().getGeneralFun(context);

        generalFunctions.storeData("isnotification", true + "");
    }
}
