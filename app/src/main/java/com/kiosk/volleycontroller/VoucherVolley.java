package com.kiosk.volleycontroller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kiosk.model.PrepaidCode;
import com.kiosk.model.Voucher;
import com.kiosk.model.Worker;
import com.kiosk.thread.BackgroundThreadExecutor;
import com.kiosk.volleycontroller.callback.iCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuynhBinh on 12/8/15.
 */
public class VoucherVolley extends iCallBack {

    public VoucherVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }
}
