package com.general.files;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.melevicarbrasil.usuario.IncomingCallScreenActivity;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;
import com.utils.Logger;
import com.utils.Utils;

import java.util.Map;

public class SinchService extends Service {


    public static final String CALL_ID = "CALL_ID";
    static final String TAG = SinchService.class.getSimpleName();

    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private SinchClient mSinchClient;
    private String mUserId;

    private StartFailedListener mListener;

    private PersistedSettings mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = new PersistedSettings(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void start(String userName) {
        GeneralFunctions generalFunctions = MyApp.getInstance().getGeneralFun(getApplicationContext());
        if (mSinchClient == null) {
            mUserId = userName;
            mSettings.setUsername(userName);
            if (generalFunctions.retrieveValue(Utils.SINCH_APP_KEY) == null || generalFunctions.retrieveValue(Utils.SINCH_APP_KEY).equalsIgnoreCase("")) {
                return;
            }
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(generalFunctions.retrieveValue(Utils.SINCH_APP_KEY))
                    .applicationSecret(generalFunctions.retrieveValue(Utils.SINCH_APP_SECRET_KEY))
                    .environmentHost(generalFunctions.retrieveValue(Utils.SINCH_APP_ENVIRONMENT_HOST)).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.setSupportActiveConnectionInBackground(true);
            mSinchClient.setSupportPushNotifications(true);
            mSinchClient.startListeningOnActiveConnection();
            mSinchClient.addSinchClientListener(new MySinchClientListener());

            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.getCallClient().setRespectNativeCalls(true);

            //mSinchClient.registerPushNotificationData();
            mSinchClient.setSupportManagedPush(true);
            mSinchClient.start();
        }
    }

    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }

    public class SinchServiceInterface extends Binder {

        public Call callPhoneNumber(String phoneNumber) {
            return mSinchClient.getCallClient().callPhoneNumber(phoneNumber);
        }

//        public Call callUser(String userId) {
//
//            Map<String, String> headers = new HashMap<String, String>();
//            headers.put("vName", "Test");
//            return mSinchClient.getCallClient().callUser(userId, headers);
//        }

        public Call callUser(String userId, Map<String, String> headers) {

            return mSinchClient.getCallClient().callUser(userId, headers);
        }

        public VideoController getVideoController() {
            return mSinchClient.getVideoController();
        }

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName) {
            start(userName);
        }

        public void stopClient() {
            stop();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }

        public SinchClient getSinchClient() {
            return mSinchClient;
        }

        public NotificationResult relayRemotePushNotificationPayload(final Map payload) {
            if (mSinchClient == null && !mSettings.getUsername().isEmpty()) {
                createClient(mSettings.getUsername());
            } else if (mSinchClient == null && mSettings.getUsername().isEmpty()) {
                Log.e(TAG, "Can't start a SinchClient as no username is available, unable to relay push.");
                return null;
            }
            return mSinchClient.relayRemotePushNotificationPayload(payload);
        }

    }

    private void createClient(String username) {
        GeneralFunctions generalFunctions = MyApp.getInstance().getGeneralFun(getApplicationContext());
        mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(username)
                .applicationKey(generalFunctions.retrieveValue(Utils.SINCH_APP_KEY))
                .applicationSecret(generalFunctions.retrieveValue(Utils.SINCH_APP_SECRET_KEY))
                .environmentHost(generalFunctions.retrieveValue(Utils.SINCH_APP_ENVIRONMENT_HOST)).build();

        mSinchClient.setSupportCalling(true);
        mSinchClient.setSupportManagedPush(true);

        try {
            mSinchClient.checkManifest();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        mSinchClient.addSinchClientListener(new MySinchClientListener());
        mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
    }

    public interface StartFailedListener {

        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Log.d(TAG, "SinchClient stopped");
        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {

            Intent intent = new Intent(SinchService.this, IncomingCallScreenActivity.class);
            intent.putExtra(CALL_ID, call.getCallId());
            Logger.d("SinchCallClientListenerName", "::" + call.getHeaders().get("Name"));
            intent.putExtra("Name", call.getHeaders().get("Name"));
            intent.putExtra("PImage", call.getHeaders().get("PImage"));
            intent.putExtra("Id", call.getHeaders().get("Id"));
            intent.putExtra("type", call.getHeaders().get("type"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            SinchService.this.startActivity(intent);

        }
    }

    private class PersistedSettings {

        private SharedPreferences mStore;

        private static final String PREF_KEY = "Sinch";

        public PersistedSettings(Context context) {
            mStore = context.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        }

        public String getUsername() {
            return mStore.getString("Username", "");
        }

        public void setUsername(String username) {
            SharedPreferences.Editor editor = mStore.edit();
            editor.putString("Username", username);
            editor.commit();
        }
    }


}