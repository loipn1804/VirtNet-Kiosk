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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 12/22/2015.
 */
public class SettingVolley extends iCallBack {

    public SettingVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getSetting(final SettingCallback callback) {
        String url = this.BASE_URL + "setting/get_configs";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject status = root.getJSONObject("status");
                    String type = status.getString("type");
                    String message = status.getString("message");
                    if (type.equalsIgnoreCase("Ok")) {
                        JSONArray data = root.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String name = object.getString("name");
                            if (name.equalsIgnoreCase("secret_password")) {
                                message = object.getString("value");
                                break;
                            }
                        }
                        callback.onSuccess(true, message);
                    } else {
                        callback.onSuccess(false, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onSuccess(false, "Data error!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }
}
