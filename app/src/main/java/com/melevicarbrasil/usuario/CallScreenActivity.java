package com.melevicarbrasil.usuario;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.general.files.AudioPlayer;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.SinchService;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MButton;
import com.view.SelectableRoundedImageView;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CallScreenActivity extends BaseActivity {

    static final String TAG = CallScreenActivity.class.getSimpleName();

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;

    private String mCallId;
    private long mCallStart = 0;

    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    SelectableRoundedImageView driverImageView;
    ImageView btn_mute, btn_speaker;

    boolean isSpeaker = false;
    boolean ismute = false;
    boolean iscallStart = false;
    GeneralFunctions generalFunctions;

    String LBL_CALL_ENDED="";
    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(() -> updateCallDuration());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen);

        mAudioPlayer = new AudioPlayer(this);
        generalFunctions = MyApp.getInstance().getGeneralFun(getActContext());

        LBL_CALL_ENDED= generalFunctions.retrieveLangLBl("", "LBL_CALL_ENDED");
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        MButton endCallButton = (MButton) findViewById(R.id.hangupButton);
        btn_mute = (ImageView) findViewById(R.id.btn_mute);
        btn_speaker = (ImageView) findViewById(R.id.btn_speaker);
        btn_mute.setOnClickListener(new setOnClickList());
        btn_speaker.setOnClickListener(new setOnClickList());

        new CreateRoundedView(Color.parseColor("#d2494a"), 5, 0, 0, endCallButton);

        driverImageView = (SelectableRoundedImageView) findViewById(R.id.driverImageView);
        driverImageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_no_pic_user));

        String vImage=getIntent().getStringExtra("vImage");
        if (vImage != null && !vImage.equals("")) {
            Picasso.get().load(vImage).error(R.mipmap.ic_no_pic_user).into(driverImageView);
        }
        mCallerName.setText(getIntent().getStringExtra("vName"));

        endCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallDuration.setText(generalFunctions.retrieveLangLBl("", "LBL_END_CALL"));
                endCall();
            }
        });
        mCallStart = System.currentTimeMillis();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

        int btnRadius=Utils.dipToPixels(this, 35);
        int backColor=getResources().getColor(android.R.color.transparent);
        int strokeColor=Color.parseColor("#FFFFFF");
        new CreateRoundedView(backColor, btnRadius, 2, strokeColor, btn_speaker);
        new CreateRoundedView(backColor, btnRadius, 2, strokeColor, btn_mute);

        mCallDuration.setText(generalFunctions.retrieveLangLBl("", "LBL_CALLING"));
        endCallButton.setText(generalFunctions.retrieveLangLBl("","LBL_END_CALL"));
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            // mCallerName.setText(call.getRemoteUserId());
            mCallState.setText(call.getState().toString());
            getSinchServiceInterface().getSinchClient().getAudioController().disableSpeaker();
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
    }

    public Activity getActContext() {
        return this;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int btnRadius=Utils.dipToPixels(getActContext(), 35);
            int backColor=getResources().getColor(android.R.color.transparent);
            int backColor1=Color.parseColor("#FFFFFF");
            int strokeColor=Color.parseColor("#FFFFFF");
            int filterColor=getActContext().getResources().getColor(R.color.black);

            if (view.getId() == btn_mute.getId()) {


                if (ismute) {
                    ismute = false;
                    new CreateRoundedView(backColor, btnRadius, 2, strokeColor, btn_mute);

                    getSinchServiceInterface().getSinchClient().getAudioController().unmute();
                    btn_mute.setColorFilter(getActContext().getResources().getColor(R.color.white));
                } else {
                    ismute = true;
                    new CreateRoundedView(backColor1, btnRadius, 2, strokeColor, btn_mute);

                    getSinchServiceInterface().getSinchClient().getAudioController().mute();
                    btn_mute.setColorFilter(filterColor);
                }

            } else if (view.getId() == btn_speaker.getId()) {

                if (isSpeaker) {
                    isSpeaker = false;
                    new CreateRoundedView(backColor, btnRadius, 2, strokeColor, btn_speaker);

                    getSinchServiceInterface().getSinchClient().getAudioController().disableSpeaker();
                    btn_speaker.setColorFilter(getActContext().getResources().getColor(R.color.white));
                } else {
                    isSpeaker = true;
                    new CreateRoundedView(backColor1, btnRadius, 2, strokeColor, btn_speaker);

                    getSinchServiceInterface().getSinchClient().getAudioController().enableSpeaker();
                    btn_speaker.setColorFilter(filterColor);
                }

            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        getSinchServiceInterface().getSinchClient().setPushNotificationDisplayName(LBL_CALL_ENDED);
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        if (mCallStart > 0) {
            if (iscallStart) {
                mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
            }
        }
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();

            mCallDuration.setText(LBL_CALL_ENDED);
            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mCallStart = System.currentTimeMillis();
            iscallStart = true;
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }
}
