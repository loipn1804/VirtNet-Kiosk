package com.kiosk.printer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HuynhBinh on 12/3/15.
 */
public class PrinterHelper
{

    public static final String USB_ID = "USB_ID";
    public static final String TIMES = "TIMES";

    public static void writeUSBID(Activity context, String data)
    {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USB_ID, data);
        editor.commit();
    }

    public static String readUSBID(Activity context)
    {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);

        String data = sharedPref.getString(USB_ID, "");

        return data;
    }


    public static void writeTime(Activity context, int data)
    {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(TIMES, data);
        editor.commit();

    }

    public static int readTime(Activity context)
    {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);

        int data = sharedPref.getInt(TIMES, 1);

        return data;
    }
}
