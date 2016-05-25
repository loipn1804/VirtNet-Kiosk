package com.kiosk.volleycontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kiosk.daocontroller.CategoryController;
import com.kiosk.daocontroller.CountryController;
import com.kiosk.data.GlobalData;
import com.kiosk.model.Worker;
import com.kiosk.thread.BackgroundThreadExecutor;
import com.kiosk.volleycontroller.callback.iCallBack;
import com.kiosk.volleycontroller.customrequest.RegisterWorkerPhotoRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import greendao.Category;
import greendao.Country;

/**
 * Created by HuynhBinh on 12/8/15.
 */
public class WorkerVolley extends iCallBack {
    public WorkerVolley(Context context) {
        this.backgroundThreadExecutor = BackgroundThreadExecutor.getInstance();
        this.context = context;
    }

    public void registerWorker(final Worker worker, final WorkerRegisterCallback callback) {

        String url = this.BASE_URL + "worker/register";
        File reg_1 = getFile("reg_1.png");
        File reg_2 = getFile("reg_2.png");
//        File reg_3 = getFile("reg_3.png");
        if (reg_1 != null && reg_2 != null) {
            RegisterWorkerPhotoRequest registerWorkerPhotoRequest = new RegisterWorkerPhotoRequest(url, worker, reg_1, reg_2, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        JSONObject root = new JSONObject(response.toString());
                        JSONObject status = root.getJSONObject("status");
                        String type = status.getString("type");
                        String message = status.getString("message");
                        if (type.equalsIgnoreCase("Ok")) {
                            callback.onSuccess(true, message, worker.RFID);
                        } else {
                            callback.onSuccess(false, message, worker.RFID);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onSuccess(false, "Data error!", worker.RFID);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };
            registerWorkerPhotoRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyFactory.getRequestQueue(context).add(registerWorkerPhotoRequest);
        }
    }

    public void loginWorkerByRfid(final WorkerLoginCallback callback, String RFID) {
        String url = this.BASE_URL + "worker/get_by_rfid/" + RFID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject status = root.getJSONObject("status");
                    String type = status.getString("type");
                    String message = status.getString("message");
                    if (type.equalsIgnoreCase("Ok")) {
                        JSONObject data = root.getJSONObject("data");
                        String worker_id = data.getString("worker_id");
                        String rfid_code = data.getString("rfid_code");
                        String dormitory_id = data.getString("dormitory_id");
                        if (dormitory_id.equalsIgnoreCase("null")) dormitory_id = "";
                        String kiosk_id = data.getString("kiosk_id");
                        if (kiosk_id.equalsIgnoreCase("null")) kiosk_id = "";
                        String full_name = data.getString("full_name");
                        String email = data.getString("email");
                        if (email.equalsIgnoreCase("null")) email = "";
                        String phone = data.getString("phone");
                        if (phone.equalsIgnoreCase("null")) phone = "";
                        String password = data.getString("password");
                        if (password.equalsIgnoreCase("null")) password = "";
                        String fin = data.getString("fin");
                        String dob = data.getString("dob");
                        String gender = data.getString("gender");
                        String room = data.getString("room");
                        String company = data.getString("company");
                        String passport_no = data.getString("passport_no");
                        String nationality = data.getString("nationality");
                        String doe = data.getString("expires_at");
                        String permit_no = data.getString("permit_no");
                        if (permit_no.equalsIgnoreCase("null")) permit_no = "";

                        GlobalData.gWorker = new Worker();
                        GlobalData.gWorker.worker_id = worker_id;
                        GlobalData.gWorker.RFID = rfid_code;
                        GlobalData.gWorker.dormitory_id = dormitory_id;
                        GlobalData.gWorker.kiosk_id = kiosk_id;
                        GlobalData.gWorker.name = full_name;
                        GlobalData.gWorker.email = email;
                        GlobalData.gWorker.phone = phone;
                        GlobalData.gWorker.password = password;
                        GlobalData.gWorker.FIN = fin;
                        GlobalData.gWorker.DOB = dob;
                        GlobalData.gWorker.gender = gender;
                        GlobalData.gWorker.room = room;
                        GlobalData.gWorker.company = company;
                        GlobalData.gWorker.passport_no = passport_no;
                        GlobalData.gWorker.nationality = nationality;
                        GlobalData.gWorker.DOE = doe;
                        GlobalData.gWorker.permit_no = permit_no;

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
//                params.put("key", worker.RFID);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void getWifiCode(final WorkerGetWifiCodeCallback callback, final String code, final String kiosk_id) {
        String url = this.BASE_URL + "worker/get_wifi_code";
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
                        String code = data.getString("code");

                        callback.onSuccess(true, message, code);
                    } else {
                        callback.onSuccess(false, message, "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onSuccess(false, "Data error!", "");
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
                params.put("code", code);
                params.put("worker_id", GlobalData.gWorker.worker_id);
                params.put("kiosk_id", kiosk_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void checkWifiCode(final WorkerCheckWifiCodeCallback callback, final String code) {
        String url = this.BASE_URL + "worker/check_wifi_code";
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

                        callback.onSuccess(true, message, data.toString());
                    } else {
                        callback.onSuccess(false, message, "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onSuccess(false, "Data error!", "");
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
                params.put("code", code);
                params.put("worker_id", GlobalData.gWorker.worker_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    public void getCountry(final GetCountryCallback callback) {

        String url = this.BASE_URL + "common/country/list";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject status = root.getJSONObject("status");
                    String type = status.getString("type");
                    String message = status.getString("message");
                    if (type.equalsIgnoreCase("Ok")) {
                        CountryController.clearAll(context);
                        JSONArray data = root.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            Long order = object.getLong("order");
                            String code = object.getString("code");
                            String slug = object.getString("slug");
                            String name = object.getString("name");

                            Country country = new Country(order, code, slug, name);
                            CountryController.insert(context, country);
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

    public void sendSms(final SendSmsCallback callback, final String phone_number, final String code) {

        String url = this.BASE_URL + "worker/send_sms";
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
                params.put("phone_number", phone_number);
                params.put("code", code);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyFactory.getRequestQueue(context).add(stringRequest);
    }

    private File getFile(String filename) {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Virtnet");
            File file = new File(myDir, filename);
            if (file != null) {

                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
