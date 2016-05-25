package com.kiosk.volleycontroller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kiosk.daocontroller.CategoryController;
import com.kiosk.daocontroller.DormitoryController;
import com.kiosk.model.Issue;
import com.kiosk.thread.BackgroundThreadExecutor;
import com.kiosk.volleycontroller.callback.iCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.Category;

/**
 * Created by HuynhBinh on 12/8/15.
 */
public class IssueVolley extends iCallBack {

    public IssueVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void getCategoryIssue(final GetCategoryIssueCallback callback) {

        String url = this.BASE_URL + "issue/category/list";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject status = root.getJSONObject("status");
                    String type = status.getString("type");
                    String message = status.getString("message");
                    if (type.equalsIgnoreCase("Ok")) {
                        CategoryController.clearAll(context);
                        JSONArray data = root.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            Long category_id = object.getLong("category_id");
                            String name = object.getString("name");
                            String description = object.getString("description");

                            Category category = new Category(category_id, name, description);
                            CategoryController.insert(context, category);
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

    public void uploadIssue(final SendIssueCallback callback, final String kiosk_id, final String worker_id, final String category_id, final String content, final String phone) {
        String url = this.BASE_URL + "issue/create";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject status = root.getJSONObject("status");
                    String type = status.getString("type");
                    String message = status.getString("message");
                    if (type.equalsIgnoreCase("Ok")) {
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
                params.put("worker_id", worker_id);
                params.put("category_id", category_id);
                params.put("content", content);
                params.put("phone", phone);
                params.put("kiosk_id", kiosk_id);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }


}
