package com.general.files;

import android.content.Context;


import com.melevicarbrasil.usuario.AppLoignRegisterActivity;
import com.melevicarbrasil.usuario.deliverAll.LoginActivity;
import com.melevicarbrasil.usuario.deliverAll.SignUpActivity;
import com.view.MyProgressDialog;

/**
 * Created by Admin on 29-06-2016.
 */
public class RegisterLinkedinLoginResCallBack {
    Context mContext;
    GeneralFunctions generalFunc;


    MyProgressDialog myPDialog;

    AppLoignRegisterActivity appMainLoginAct;
    LoginActivity appLoginAct;
    SignUpActivity appSignUpAct;
    public boolean isrestart;

    public RegisterLinkedinLoginResCallBack(Context mContext) {
        this.mContext = mContext;

        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        appMainLoginAct = (AppLoignRegisterActivity) mContext;

    }

    public RegisterLinkedinLoginResCallBack(Context mContext, boolean isrestart) {
        this.mContext = mContext;
        this.isrestart = isrestart;

        generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        if (mContext instanceof LoginActivity) {
            appLoginAct = (LoginActivity) mContext;
        } else if (mContext instanceof SignUpActivity) {
            appSignUpAct = (SignUpActivity) mContext;
        }

    }

    public void continueLogin() {
        if (appMainLoginAct==null)
        {
            OpenLinkedinDialog openLinkedinDialog = new OpenLinkedinDialog(mContext, generalFunc,isrestart);
        }else
        {
            OpenLinkedinDialog openLinkedinDialog = new OpenLinkedinDialog(mContext, generalFunc);
        }


    }


}
