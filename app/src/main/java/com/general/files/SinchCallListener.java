package com.general.files;

import android.media.AudioManager;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class SinchCallListener implements CallListener {
    @Override
    public void onCallEnded(Call endedCall) {

        SinchError a = endedCall.getDetails().getError();

        MyApp.getInstance().getCurrentAct().setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
    }

    @Override
    public void onCallEstablished(Call establishedCall) {
        // callState.setText("connected");
        MyApp.getInstance().getCurrentAct().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    @Override
    public void onCallProgressing(Call progressingCall) {
    }

    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
    }
}