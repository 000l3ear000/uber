package com.general.files;

import com.sls.yalgaar_api.interfaces.ConnectionCallback;
import com.sls.yalgaar_api.interfaces.PublishMessageCallback;
import com.sls.yalgaar_api.interfaces.SubscribeCallback;
import com.sls.yalgaar_api.interfaces.UnSubscribeCallback;
import com.utils.Logger;
import com.utils.Utils;

import java.util.ArrayList;

public class ConfigYalgaarConnectionResponseListener implements SubscribeCallback, ConnectionCallback, UnSubscribeCallback, PublishMessageCallback {
    ConfigYalgaarConnectionListener listener;

    public ConfigYalgaarConnectionResponseListener(ConfigYalgaarConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void connectSuccessfullyCallback(String s) {
        if (listener != null) {
            listener.onClientConnected();
        }
    }

    @Override
    public void connectionErrorCallback(String s) {

    }

    @Override
    public void connectionDisconnectCallback(String s) {
        if (listener != null) {
            listener.onClientDisConnected();
        }
    }

    @Override
    public void successSubCallback(ArrayList<String> arrayList, String s) {
        Logger.e("isClientConnected", ":successSubCallback:" + arrayList.toString() + "::STR::" + s);
    }

    @Override
    public void messageReceiveSubCallback(ArrayList<String> arrayList, String s) {
        if (listener != null) {
            listener.onDataReceived(s);
        }
    }

    @Override
    public void errorSubCallback(String s) {
        Logger.e("isClientConnected", ":errorSubCallback:" + "::STR::" + s);

    }

    @Override
    public void successUnSubCallback(ArrayList<String> arrayList, String s) {
        Logger.e("isClientConnected", ":successUnSubCallback:" + arrayList.toString() + "::STR::" + s);

    }

    @Override
    public void errorUnSubCallback(String s) {
        Logger.e("isClientConnected", ":errorUnSubCallback:" + "::STR::" + s);

    }

    @Override
    public void successCallback() {
        Logger.e("isClientConnected", ":successCallback");

    }

    @Override
    public void errorCallback(String s) {
        Logger.e("isClientConnected", ":errorCallback:" + "::STR::" + s);


    }

    public interface ConfigYalgaarConnectionListener {
        void onClientConnected();

        void onClientDisConnected();

        void onDataReceived(String data);
    }
}
