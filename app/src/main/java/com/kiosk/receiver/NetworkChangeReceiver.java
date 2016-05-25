package com.kiosk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by USER on 4/24/2015.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String ON_NETWORK_CHANGE = "ON_NETWORK_CHANGE";
    public static final String ONLINE = "ONLINE";
    public static final String OFFLINE = "OFFLINE";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (isOnline(context)) {
            Intent intent1 = new Intent();
            intent1.setAction(ON_NETWORK_CHANGE);
            intent1.putExtra(EXTRA_RESULT, ONLINE);
            context.sendBroadcast(intent1);
        } else {
            Intent intent1 = new Intent();
            intent1.setAction(ON_NETWORK_CHANGE);
            intent1.putExtra(EXTRA_RESULT, OFFLINE);
            context.sendBroadcast(intent1);
        }

    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
