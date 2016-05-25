package com.kiosk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kiosk.volleycontroller.KioskVolley;

/**
 * Created by USER on 12/22/2015.
 */
public class PingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "ping", Toast.LENGTH_SHORT).show();
        KioskVolley kioskVolley = new KioskVolley(context);
        kioskVolley.pingServer();
    }
}
