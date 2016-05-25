package com.kiosk.rfid;

import android.content.Context;

/**
 * Created by HuynhBinh on 12/7/15.
 */
public class RFIDReaderManager
{

    private Context mContext = null;

    RFIDReaderCallback callback = null;

    public interface RFIDReaderCallback
    {
        void onSuccess(String rfid);

        void onError(String strError);

        void onWarning(String strWarning);
    }

    public RFIDReaderManager(Context context, RFIDReaderCallback callback)
    {
        this.mContext = context;
        this.callback = callback;
        startRead();

    }

    public void startRead()
    {
        String rfidData = "RFID123456";
        if (callback != null)
        {
            callback.onSuccess(rfidData);
        }
    }


}
