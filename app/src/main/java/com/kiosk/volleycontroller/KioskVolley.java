package com.kiosk.volleycontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kiosk.daocontroller.DormitoryController;
import com.kiosk.data.GlobalData;
import com.kiosk.model.Kiosk;
import com.kiosk.model.Worker;
import com.kiosk.thread.BackgroundThreadExecutor;
import com.kiosk.volleycontroller.callback.iCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.Dormitory;

/**
 * Created by HuynhBinh on 12/8/15.
 */
public class KioskVolley extends iCallBack {
    public KioskVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void registerKiosk(final KioskRegisterCallback callback, final String uuid, final String name, final String latitude, final String longitude, final String dormitory_id) {

        String url = this.BASE_URL + "kiosk/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject status = root.getJSONObject("status");
                    String type = status.getString("type");
                    String message = status.getString("message");
                    if (type.equalsIgnoreCase("Ok")) {
                        JSONObject data = root.getJSONObject("data");
                        Long kiosk_id = data.getLong("kiosk_id");

                        SharedPreferences preferences = context.getSharedPreferences("kiosk", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("kiosk_id", kiosk_id);
                        editor.putString("name", name);
                        editor.putString("dormitory_id", dormitory_id);
                        editor.commit();

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
                params.put("uuid", uuid);
                params.put("name", name);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("dormitory_id", dormitory_id);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void getDormitoryKiosk(final KioskGetDormitoryCallback callback) {

        String url = this.BASE_URL + "common/dormitory/list";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject status = root.getJSONObject("status");
                    String type = status.getString("type");
                    String message = status.getString("message");
                    if (type.equalsIgnoreCase("Ok")) {
                        DormitoryController.clearAll(context);
                        JSONArray data = root.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            Long dormitory_id = object.getLong("dormitory_id");
                            String name = object.getString("name");
                            String address = object.getString("address");
                            String description = object.getString("description");
                            Long company_id = object.getLong("company_id");

                            Dormitory dormitory = new Dormitory(dormitory_id, name, address, description, company_id);
                            DormitoryController.insert(context, dormitory);
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

    public void pingServer() {

        String url = this.BASE_URL + "kiosk/ping_server";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String android_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                params.put("uuid", android_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }
}
