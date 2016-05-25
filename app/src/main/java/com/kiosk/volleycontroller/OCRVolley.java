package com.kiosk.volleycontroller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kiosk.thread.BackgroundThreadExecutor;
import com.kiosk.volleycontroller.callback.iCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 12/7/2015.
 */
public class OCRVolley extends iCallBack {

    public OCRVolley(Context context) {
        // init background thread and ui thread
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }
}
