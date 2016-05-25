package com.kiosk.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import com.kiosk.http.MySSLSocketFactory;
import com.kiosk.staticfunction.StaticFunction;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

/**
 * Created by USER on 12/7/2015.
 */
public class OCRService extends IntentService {

    String URL = "https://ocr.space/api/Parse/Image";
    public static String REGISTER_IMAGE_INFO = "register_image_info";
    private int TIME_OUT = 10000;
    private String FOLDER = "Virtnet";
    private String FILE_1 = "reg_1.png";
    private String FILE_2 = "reg_2.png";
    private String FILE_3 = "reg_3.png";

    // action
    public static final String ACTION_GET_IMAGE_1 = "ACTION_GET_IMAGE_1";
    public static final String ACTION_GET_IMAGE_2 = "ACTION_GET_IMAGE_2";
    public static final String ACTION_GET_IMAGE_3 = "ACTION_GET_IMAGE_3";

    // receiver
    public static final String RECEIVER_GET_IMAGE_1 = "RECEIVER_GET_IMAGE_1";
    public static final String RECEIVER_GET_IMAGE_2 = "RECEIVER_GET_IMAGE_2";
    public static final String RECEIVER_GET_IMAGE_3 = "RECEIVER_GET_IMAGE_3";

    // result
    public static final String EXTRA_RESULT_MESSAGE = "EXTRA_RESULT_MESSAGE";
    public static String RESULT_MESSAGE = "";
    public static final String EXTRA_RESULT_IS_SUCCESS = "EXTRA_RESULT_IS_SUCCESS";

    public OCRService() {
        super(OCRService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_GET_IMAGE_1)) {
            if (StaticFunction.isNetworkAvailable(this)) {
                boolean bResult = uploadOCRImage_1();
                if (bResult) {
                    sendBroadCastReceiver(RECEIVER_GET_IMAGE_1, bResult);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_IMAGE_1, bResult);
                }
            }
        } else if (action.equals(ACTION_GET_IMAGE_2)) {
            if (StaticFunction.isNetworkAvailable(this)) {
                boolean bResult = uploadOCRImage_2();
                if (bResult) {
                    sendBroadCastReceiver(RECEIVER_GET_IMAGE_2, bResult);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_IMAGE_2, bResult);
                }
            }
        } else if (action.equals(ACTION_GET_IMAGE_3)) {
            if (StaticFunction.isNetworkAvailable(this)) {
                boolean bResult = uploadOCRImage_3();
                if (bResult) {
                    sendBroadCastReceiver(RECEIVER_GET_IMAGE_3, bResult);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_IMAGE_3, bResult);
                }
            }
        }
    }

    private void sendBroadCastReceiver(String action, Boolean isSuccess) {
        Intent i = new Intent();
        i.setAction(action);
        i.putExtra(EXTRA_RESULT_MESSAGE, RESULT_MESSAGE);
        i.putExtra(EXTRA_RESULT_IS_SUCCESS, isSuccess);
        sendBroadcast(i);
    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private boolean uploadOCRImage_1() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + FOLDER);
            String fname = FILE_1;
            File file = new File(myDir, fname);
            if (file != null) {
                HttpClient httpClient = getNewHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(URL);
                HttpParams httpParams = httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);

                byte[] bFile = new byte[(int) file.length()];
                FileInputStream fileInputStream = null;
                //convert file into array of bytes
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();
                ContentBody bin = new ByteArrayBody(bFile, "back.jpg");

                MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
                multiPartEntityBuilder.addPart("file", bin);
                multiPartEntityBuilder.addTextBody("apikey", "helloworld");
                multiPartEntityBuilder.addTextBody("language", "eng");

                httpPost.setEntity(multiPartEntityBuilder.build());
                HttpResponse response = httpClient.execute(httpPost, localContext);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                //                RESULT_MESSAGE = sResponse;
                parseImage_1(sResponse);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void parseImage_1(String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONArray ParsedResults = root.getJSONArray("ParsedResults");
            JSONObject objParsedResults = ParsedResults.getJSONObject(0);
            String ParsedText = objParsedResults.getString("ParsedText");
            RESULT_MESSAGE = ParsedText;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean uploadOCRImage_2() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + FOLDER);
            String fname = FILE_2;
            File file = new File(myDir, fname);
            if (file != null) {
                HttpClient httpClient = getNewHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(URL);
                HttpParams httpParams = httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);

                byte[] bFile = new byte[(int) file.length()];
                FileInputStream fileInputStream = null;
                //convert file into array of bytes
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();
                ContentBody bin = new ByteArrayBody(bFile, "back.jpg");

                MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
                multiPartEntityBuilder.addPart("file", bin);
                multiPartEntityBuilder.addTextBody("apikey", "helloworld");
                multiPartEntityBuilder.addTextBody("language", "eng");

                httpPost.setEntity(multiPartEntityBuilder.build());
                HttpResponse response = httpClient.execute(httpPost, localContext);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                //                RESULT_MESSAGE = sResponse;
                parseImage_2(sResponse);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void parseImage_2(String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONArray ParsedResults = root.getJSONArray("ParsedResults");
            JSONObject objParsedResults = ParsedResults.getJSONObject(0);
            String ParsedText = objParsedResults.getString("ParsedText");
            RESULT_MESSAGE = ParsedText;

            SharedPreferences preferences = getSharedPreferences(REGISTER_IMAGE_INFO, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            String[] infos = ParsedText.split("\\r\\n");
            int lenght = infos.length;
            for (int i = 0; i < infos.length; i++) {
                if (infos[i].contains("Name")) {
                    if (i + 1 < lenght) {
                        editor.putString("name", infos[i + 1].trim());
                    }
                }
                if (infos[i].contains("Pass")) {
                    if (i + 1 < lenght) {
                        editor.putString("passport", infos[i + 1].trim());
                    }
                }
                if (infos[i].contains("Date")) {
                    if (infos[i].contains("B")) {
                        if (i + 1 < lenght) {
                            editor.putString("dob", infos[i + 1].trim());
                        }
                    } else if (infos[i].contains("E")) {
                        if (i + 1 < lenght) {
                            editor.putString("doe", infos[i + 1].trim());
                        }
                        if (i + 3 < lenght) {
                            if (infos[i + 3].contains("Sex")) {
                                String fin_nat = infos[i + 2].trim();
                                String[] split_fin_nat = fin_nat.split(" ");
                                if (split_fin_nat.length > 1) {
                                    editor.putString("nation", split_fin_nat[1]);
                                }
                            } else {
                                editor.putString("nation", infos[i + 3].trim());
                            }
                        }
                    }
                }
            }

            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean uploadOCRImage_3() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + FOLDER);
            String fname = FILE_3;
            File file = new File(myDir, fname);
            if (file != null) {
                HttpClient httpClient = getNewHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(URL);
                HttpParams httpParams = httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);

                byte[] bFile = new byte[(int) file.length()];
                FileInputStream fileInputStream = null;
                //convert file into array of bytes
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();
                ContentBody bin = new ByteArrayBody(bFile, "back.jpg");

                MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
                multiPartEntityBuilder.addPart("file", bin);
                multiPartEntityBuilder.addTextBody("apikey", "helloworld");
                multiPartEntityBuilder.addTextBody("language", "eng");

                httpPost.setEntity(multiPartEntityBuilder.build());
                HttpResponse response = httpClient.execute(httpPost, localContext);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                //                RESULT_MESSAGE = sResponse;
                parseImage_3(sResponse);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void parseImage_3(String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONArray ParsedResults = root.getJSONArray("ParsedResults");
            JSONObject objParsedResults = ParsedResults.getJSONObject(0);
            String ParsedText = objParsedResults.getString("ParsedText");
            RESULT_MESSAGE = ParsedText;

            SharedPreferences preferences = getSharedPreferences(REGISTER_IMAGE_INFO, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            String[] infos = ParsedText.split("\\r\\n");
            int pos_room = 0;
            for (int i = 0; i < infos.length; i++) {
                if (infos[i].contains("-")) {
                    pos_room = i;
                    break;
                }
            }
            String name = "";
            for (int j = 0; j < pos_room; j++) {
                name += infos[j].trim() + " ";
            }
            name = name.trim();
//            editor.putString("name", name);
            editor.putString("room", infos[pos_room].trim());
            if (pos_room + 1 < infos.length) {
                editor.putString("fin", infos[pos_room + 1].trim());
            }
            String company = "";
            if (pos_room + 2 < infos.length) {
                company = infos[pos_room + 2].trim();
            }
            if (pos_room + 3 < infos.length) {
                String com_line_2 = infos[pos_room + 3].trim();
                if (com_line_2 == com_line_2.toUpperCase()) {
                    company += " " + com_line_2;
                }
            }
            editor.putString("company", company);

            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
