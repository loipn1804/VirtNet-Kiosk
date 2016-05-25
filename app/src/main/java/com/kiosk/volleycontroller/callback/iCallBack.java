package com.kiosk.volleycontroller.callback;

import android.content.Context;

import com.kiosk.thread.BackgroundThreadExecutor;

/**
 * Created by HuynhBinh on 10/5/15.
 */
public class iCallBack {

    //region VARIABLE
    // background thread to handle data in a separate thread from UI
    // will not lock the UI thread for improve performance
    public BackgroundThreadExecutor backgroundThreadExecutor;
    //region VARIABLE
    public Context context;
    //endregion
    public String BASE_URL = "http://virtnet.techub.io/api/";

    public int TIME_OUT = 10000;

    public interface OCRCallback {
        void onLoaded(String result);

        void onError(String error);
    }

    // Issue
    public interface SendIssueCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }

    public interface GetCategoryIssueCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }


    // Kiosk
    public interface KioskRegisterCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }

    public interface KioskGetDormitoryCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }


    // Worker
    public interface WorkerRegisterCallback {
        void onSuccess(boolean dataOk, String message, String RFID);

        void onError(String error);
    }

    public interface WorkerLoginCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }

    public interface WorkerGetWifiCodeCallback {
        void onSuccess(boolean dataOk, String message, String wifi_code);

        void onError(String error);
    }

    public interface WorkerCheckWifiCodeCallback {
        void onSuccess(boolean dataOk, String message, String data);

        void onError(String error);
    }

    public interface SendSmsCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }

    public interface GetCountryCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }


    // Setting
    public interface SettingCallback {
        void onSuccess(boolean dataOk, String message);

        void onError(String error);
    }
}
