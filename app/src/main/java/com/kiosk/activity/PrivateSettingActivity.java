package com.kiosk.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kiosk.R;
import com.kiosk.adapter.DormitoryAdapter;
import com.kiosk.daocontroller.DormitoryController;
import com.kiosk.data.GlobalData;
import com.kiosk.staticfunction.Font;
import com.kiosk.volleycontroller.KioskVolley;
import com.kiosk.volleycontroller.WorkerVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import greendao.Dormitory;

/**
 * Created by USER on 12/14/2015.
 */
public class PrivateSettingActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, DormitoryAdapter.OnClickOnItemCallback {

    private String FOLDER = "Config";
    private String FILE_NAME = "config.txt";

    private TextView txtTitle;
    private TextView txtRegisterKiosk;
    private EditText edtDeviceId;
    private EditText edtName;
    private EditText edtLat;
    private EditText edtLng;
    private TextView txtDormitary;
    private TextView txtHome;
    private RadioButton radioTrue;
    private RadioButton radioFalse;
    private RadioButton radioEnable;
    private RadioButton radioDisable;
    private RelativeLayout superRoot;
    private TextView txtCloseApp;

    private boolean isSetModeKiosk = false;

    private String dormitory_id = "";

    private Dialog dialog;

    // modify by loi -- location
    public final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    public final String TAG = "com.location";
    private ProgressDialog progressDialogGetLocation;

//    private Intent mIntentService;
//    private PendingIntent mPendingIntent;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    // modify by loi -- location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_setting);

        initView();
        initData();

        buildGoogleApiClient(UPDATE_INTERVAL_IN_MILLISECONDS);
        mGoogleApiClient.connect();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtRegisterKiosk = (TextView) findViewById(R.id.txtRegisterKiosk);
        edtDeviceId = (EditText) findViewById(R.id.edtDeviceId);
        edtName = (EditText) findViewById(R.id.edtName);
        edtLat = (EditText) findViewById(R.id.edtLat);
        edtLng = (EditText) findViewById(R.id.edtLng);
        txtDormitary = (TextView) findViewById(R.id.txtDormitary);
        txtHome = (TextView) findViewById(R.id.txtHome);
        radioTrue = (RadioButton) findViewById(R.id.radioTrue);
        radioFalse = (RadioButton) findViewById(R.id.radioFalse);
        radioEnable = (RadioButton) findViewById(R.id.radioEnable);
        radioDisable = (RadioButton) findViewById(R.id.radioDisable);
        superRoot = (RelativeLayout) findViewById(R.id.superRoot);
        txtCloseApp = (TextView) findViewById(R.id.txtCloseApp);

        txtHome.setOnClickListener(this);
        edtDeviceId.setEnabled(false);

        txtRegisterKiosk.setOnClickListener(this);
        txtDormitary.setOnClickListener(this);
        txtCloseApp.setOnClickListener(this);

        radioTrue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    showToastOk(isChecked + "");
                    writeToFile("1");
                } else {
//                    showToastError(isChecked + "");
                    writeToFile("0");
                }
            }
        });

        if (readFromFile().equals("0")) {
            radioFalse.setChecked(true);
        } else {
            radioTrue.setChecked(true);
        }

        radioEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isSetModeKiosk) {
                    setSystemUIEnabled(isChecked);
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.e("root", superRoot.getMeasuredHeight() + "");
//                Toast.makeText(PrivateSettingActivity.this, superRoot.getMeasuredHeight() + "", Toast.LENGTH_LONG).show();
                if (superRoot.getMeasuredHeight() > 750) {
                    radioEnable.setChecked(true);
                } else {
                    radioDisable.setChecked(true);
                }
                isSetModeKiosk = true;
            }
        }, 500);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);

        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        edtDeviceId.setText(android_id);
        String device_name = Build.MANUFACTURER + " " + Build.MODEL;
        edtName.setText(device_name);

        setData();

        getDormitoryKiosk();
    }

    private void setData() {
        SharedPreferences preferences = getSharedPreferences("kiosk", Context.MODE_PRIVATE);
        Long kiosk_id = preferences.getLong("kiosk_id", 0);
        if (kiosk_id == 0) {
            txtTitle.setText(getString(R.string.private_setting) + " (kiosk: N/A)");
        } else {
            txtTitle.setText(getString(R.string.private_setting) + " (kiosk: " + kiosk_id + ")");
        }
        String name = preferences.getString("name", "");
        if (name.length() > 0) {
            edtName.setText(name);
        }
        dormitory_id = preferences.getString("dormitory_id", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtRegisterKiosk:
                if (dormitory_id.length() > 0) {
                    registerKiosk();
                } else {
                    finish();
                }
                break;
            case R.id.txtDormitary:
                List<Dormitory> list = DormitoryController.getAll(PrivateSettingActivity.this);
                showPopupSpinner(list);
                break;
            case R.id.txtHome:
                finish();
                break;
            case R.id.txtCloseApp:
                closeApp();
                break;
        }
    }

    public void setSystemUIEnabled(boolean enabled) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("pm " + (enabled ? "disable" : "enable") + " com.android.systemui\n");
            os.writeBytes("input keyevent KEYCODE_BACK\n");
            os.writeBytes("input keyevent KEYCODE_BACK\n");
            os.writeBytes("reboot\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeApp() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("input keyevent KEYCODE_BACK\n");
            os.writeBytes("input keyevent KEYCODE_BACK\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerKiosk() {
        KioskVolley kioskVolley = new KioskVolley(this);
        kioskVolley.registerKiosk(new iCallBack.KioskRegisterCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    showToastOk(message);
                    setData();
                } else {
                    showToastError(message);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, edtDeviceId.getText().toString(), edtName.getText().toString(), edtLat.getText().toString(), edtLng.getText().toString(), dormitory_id);
        showProgressDialog(false);
    }

    private void getDormitoryKiosk() {
        KioskVolley kioskVolley = new KioskVolley(this);
        kioskVolley.getDormitoryKiosk(new iCallBack.KioskGetDormitoryCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    List<Dormitory> list = DormitoryController.getAll(PrivateSettingActivity.this);
                    if (list.size() > 0) {
                        if (dormitory_id.length() == 0) {
                            txtDormitary.setText(list.get(0).getName());
                            dormitory_id = list.get(0).getDormitory_id() + "";
                        } else {
                            for (Dormitory dormitory : list) {
                                if (dormitory_id.equals(dormitory.getDormitory_id() + "")) {
                                    txtDormitary.setText(dormitory.getName());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    showToastError(message);
                }
//                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
//                hideProgressDialog();
            }
        });
//        showProgressDialog(false);
    }

    public void showPopupSpinner(final List<Dormitory> list) {
        // custom dialog
        dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_spinner);
        Font.overrideFontsLight(this, dialog.findViewById(R.id.root));

        ListView lvSpinner = (ListView) dialog.findViewById(R.id.lvSpinner);
        LinearLayout lnlCover = (LinearLayout) dialog.findViewById(R.id.lnlCover);

        DormitoryAdapter dormitoryAdapter = new DormitoryAdapter(PrivateSettingActivity.this, list, this);
        lvSpinner.setAdapter(dormitoryAdapter);

        lnlCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void writeToFile(String data) {
        FileOutputStream outStream = null;
        try {
//                String absPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String root = Environment.getExternalStorageDirectory().toString();
            String mCurrentPhotoPath = root + "/" + FOLDER + "/" + FILE_NAME;
            File myDir = new File(root + "/" + FOLDER);

            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            File file = new File(mCurrentPhotoPath);
//            if (file.exists()) file.delete();

            outStream = new FileOutputStream(mCurrentPhotoPath);
            outStream.write(data.getBytes());
            outStream.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }


    private String readFromFile() {
        String contents = "0";
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            String mCurrentPhotoPath = root + "/" + FOLDER + "/" + FILE_NAME;
            File file = new File(mCurrentPhotoPath);
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            FileInputStream in = new FileInputStream(file);

            in.read(bytes);
            in.close();
            contents = new String(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        return contents;
    }

    // modify by loi -- location

    protected synchronized void buildGoogleApiClient(long requestTimeInterval) {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        createLocationRequest(requestTimeInterval);

        buildLocationSettingsRequest();
    }

    protected void createLocationRequest(long requestTimeInterval) {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(requestTimeInterval);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(requestTimeInterval);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        // not use anymore
        // now we will ask user to choose YES in pre login actyivity
        // checkLocationSettings();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            edtLat.setText(location.getLatitude() + "");
            edtLng.setText(location.getLongitude() + "");
            Toast.makeText(this, "Location Ok", Toast.LENGTH_LONG).show();
            Log.e("GOT LOC", "LOCATION OK");
        } else {
            Log.e("NO LOC", "LOC = NULL");
            checkLocationSettings();
        }

//        startLocationUpdates();
    }

    public void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    protected void startLocationUpdates() {
        // use location listener
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        // use pending intent
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
//                startLocationUpdates();
                runTimerLocation();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
            progressDialogGetLocation = new ProgressDialog(this);
            progressDialogGetLocation.setMessage("Getting location...");
            progressDialogGetLocation.show();
            runTimerLocation();
        } else {
            Toast.makeText(this, "Location Not turned on", Toast.LENGTH_LONG).show();
        }
    }

    private void runTimerLocation() {
        new CountDownTimer(500, 10) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {
                    edtLat.setText(location.getLatitude() + "");
                    edtLng.setText(location.getLongitude() + "");
                    //Toast.makeText(MainUserActivity.this, "Location Ok", Toast.LENGTH_LONG).show();
                    progressDialogGetLocation.dismiss();
                } else {
                    //Toast.makeText(MainUserActivity.this, "Location Not found", Toast.LENGTH_LONG).show();
                    runTimerLocation();
                }
            }
        }.start();
    }

    @Override
    public void onCLickItem(String name, Long dormitory_id) {
        txtDormitary.setText(name);
        this.dormitory_id = dormitory_id + "";
        dialog.dismiss();
    }

    // modify by loi -- location
}
