package com.general.files;

import android.widget.Toast;

import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

public class SinchCallClientListener implements CallClientListener {
    @Override
    public void onIncomingCall(CallClient callClient, Call incomingCall) {

        incomingCall.answer();
        incomingCall.addCallListener(new SinchCallListener());
    }
}
