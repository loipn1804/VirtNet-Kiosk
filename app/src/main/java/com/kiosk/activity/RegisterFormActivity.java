package com.kiosk.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kiosk.R;
import com.kiosk.adapter.CategoryAdapter;
import com.kiosk.adapter.CountryAdapter;
import com.kiosk.adapter.DormitoryAdapter;
import com.kiosk.daocontroller.CountryController;
import com.kiosk.daocontroller.DormitoryController;
import com.kiosk.data.GlobalData;
import com.kiosk.http.MySSLSocketFactory;
import com.kiosk.model.Worker;
import com.kiosk.service.OCRService;
import com.kiosk.staticfunction.Font;
import com.kiosk.staticfunction.StaticFunction;
import com.kiosk.view.KeyboardVisibilityListener;
import com.kiosk.volleycontroller.KioskVolley;
import com.kiosk.volleycontroller.WorkerVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

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
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import greendao.Category;
import greendao.Country;
import greendao.Dormitory;

/**
 * Created by USER on 12/3/2015.
 */
public class RegisterFormActivity extends BaseActivity implements View.OnClickListener, CountryAdapter.OnClickOnItemCallback, KeyboardVisibilityListener.OnKeyboardVisibilityListener, DormitoryAdapter.OnClickOnItemCallback {

    private EditText edtName;
    private TextView txtDob;
    private RadioButton radioMale;
    private TextView txtNation;
    private EditText edtRoom;
    private EditText edtPassport;
    private TextView txtDoe;
    private EditText edtFIN;
    private EditText edtCompany;
    private EditText edtPhoneNumber;
    private TextView txtFinish;
    private TextView txtCancel;
    private ImageView imvHideKeyboard;
    private TextView txtDormitary;

    private String nation_code = "";

    private String dormitory_id = "";

    private KeyboardVisibilityListener keyboardVisibilityListener;

//    private Handler handler;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(OCRService.RECEIVER_GET_IMAGE_2);
            intentFilter.addAction(OCRService.RECEIVER_GET_IMAGE_3);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();

//        String s = "Name ";
//        if (s.contains("Name")) {
//            ((TextView) findViewById(R.id.txtImage)).setText("true");
//        } else {
//            ((TextView) findViewById(R.id.txtImage)).setText("false");
//        }

//        String imageName = "back";
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/Download");
//        String fname = imageName + ".jpg";
//        File file = new File(myDir, fname);
//        if (file != null) {
////            uploadFile(file);
//            ImageUploadTask imageUploadTask = new ImageUploadTask();
//            imageUploadTask.execute(file);
//        }

//        showProgressDialog(true);
//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                hideProgressDialog();
//            }
//        }, 15000);
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        edtName = (EditText) findViewById(R.id.edtName);
        txtDob = (TextView) findViewById(R.id.txtDob);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        txtNation = (TextView) findViewById(R.id.txtNation);
        edtRoom = (EditText) findViewById(R.id.edtRoom);
        edtPassport = (EditText) findViewById(R.id.edtPassport);
        txtDoe = (TextView) findViewById(R.id.txtDoe);
        edtFIN = (EditText) findViewById(R.id.edtFIN);
        edtCompany = (EditText) findViewById(R.id.edtCompany);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        txtFinish = (TextView) findViewById(R.id.txtFinish);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        imvHideKeyboard = (ImageView) findViewById(R.id.imvHideKeyboard);
        txtDormitary = (TextView) findViewById(R.id.txtDormitary);

        txtDob.setOnClickListener(this);
        txtDoe.setOnClickListener(this);
        txtNation.setOnClickListener(this);
        txtFinish.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        imvHideKeyboard.setOnClickListener(this);
        txtDormitary.setOnClickListener(this);

        imvHideKeyboard.setVisibility(View.GONE);

        keyboardVisibilityListener = new KeyboardVisibilityListener();
        keyboardVisibilityListener.setKeyboardListener(this, this,
                R.id.root);
    }

    private void initData() {
//        setData();

//        List<Country> list = CountryController.getAll(RegisterFormActivity.this);
//        if (list.size() > 0 && txtNation.getText().toString().trim().length() == 0) {
//            txtNation.setText(list.get(0).getName());
//        }
        getCountry();
        setDormitory();
        getDormitoryKiosk();
        setFIN();
    }

    private void setData() {
        SharedPreferences preferences = getSharedPreferences(OCRService.REGISTER_IMAGE_INFO, MODE_PRIVATE);
        edtName.setText(preferences.getString("name", ""));
        txtDob.setText(validateDate(preferences.getString("dob", "")));
        edtRoom.setText(preferences.getString("room", ""));
        edtPassport.setText(preferences.getString("passport", ""));
        txtDoe.setText(validateDate(preferences.getString("doe", "")));
//        edtFIN.setText(preferences.getString("fin", ""));
        edtCompany.setText(preferences.getString("company", ""));

//        txtNation.setText(preferences.getString("nation", ""));
        String nation = preferences.getString("nation", "");
        if (nation.length() > 0) {
            String code = CountryController.getCodeByName(this, nation);
            if (code.length() > 0) {
                txtNation.setText(nation);
                nation_code = code;
            }
        }
    }

    private void setFIN() {
        SharedPreferences preferences = getSharedPreferences("worker", Context.MODE_PRIVATE);
        String rfid = preferences.getString("rfid", "");
        edtFIN.setText(rfid);
    }

    private void setDormitory() {
        SharedPreferences preferences = getSharedPreferences("kiosk", Context.MODE_PRIVATE);
        dormitory_id = preferences.getString("dormitory_id", "");
        if (dormitory_id.length() > 0) {
            try {
                Long aLong = Long.parseLong(dormitory_id);
                Dormitory dormitory = DormitoryController.getByDormitoryId(this, aLong);
                txtDormitary.setText(dormitory.getName());
                dormitory_id = dormitory.getDormitory_id() + "";
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtDob:
                showDatePicker(txtDob);
                break;
            case R.id.txtDoe:
                showDatePicker(txtDoe);
                break;
            case R.id.txtNation:
                showPopupSpinner();
                break;
            case R.id.txtFinish:
                register();
                break;
            case R.id.txtCancel:
                finish();
                break;
            case R.id.edtPhoneNumber:
                finish();
                break;
            case R.id.imvHideKeyboard:
                StaticFunction.hideKeyboard(this);
                break;
            case R.id.txtDormitary:
                List<Dormitory> list = DormitoryController.getAll(this);
                showPopupSpinnerDormitory(list);
                break;
        }
    }

    private void getCountry() {
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.getCountry(new iCallBack.GetCountryCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
//                    List<Country> list = CountryController.getAll(RegisterFormActivity.this);
//                    if (list.size() > 0 && txtNation.getText().toString().trim().length() == 0) {
//                        txtNation.setText(list.get(0).getName());
//                    }
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

    public void showPopupSpinner() {
        // custom dialog
        List<Country> list = CountryController.getAll(RegisterFormActivity.this);
        dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_spinner_country);
        Font.overrideFontsLight(this, dialog.findViewById(R.id.root));

        ListView lvSpinner = (ListView) dialog.findViewById(R.id.lvSpinner);
        RelativeLayout rltCover = (RelativeLayout) dialog.findViewById(R.id.rltCover);
        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch);

        final CountryAdapter countryAdapter = new CountryAdapter(RegisterFormActivity.this, list, this);
        lvSpinner.setAdapter(countryAdapter);

        rltCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Country> list = CountryController.getAllByName(RegisterFormActivity.this, s.toString());
                countryAdapter.setListData(list);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();
    }

    public void showPopupSpinnerDormitory(final List<Dormitory> list) {
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

        DormitoryAdapter dormitoryAdapter = new DormitoryAdapter(RegisterFormActivity.this, list, this);
        lvSpinner.setAdapter(dormitoryAdapter);

        lnlCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String validateDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (java.text.ParseException e) {
            Calendar c = Calendar.getInstance();
            try {
                date = formatter.parse(formatter.format(c.getTime()));
            } catch (java.text.ParseException eCatch) {

            }
        }

        return formatter.format(date);
    }

    private void showDatePicker(final TextView txtDate) {

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
////        String dateInString = txtDay.getText().toString();
//        Date date = null;
//        try {
//            date = formatter.parse(dateInString);
//        } catch (java.text.ParseException e) {
//
//            e.printStackTrace();
//        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        if (txtDate.getText().toString().length() > 0) {
            try {
                date = formatter.parse(txtDate.getText().toString());
            } catch (java.text.ParseException e) {
                Calendar c = Calendar.getInstance();
                try {
                    date = formatter.parse(formatter.format(c.getTime()));
                } catch (java.text.ParseException eCatch) {

                }
            }
        } else {
            Calendar c = Calendar.getInstance();
            try {
                date = formatter.parse(formatter.format(c.getTime()));
            } catch (java.text.ParseException eCatch) {

            }
        }

        SimpleDateFormat ftDay = new SimpleDateFormat("dd");
        SimpleDateFormat ftMonth = new SimpleDateFormat("MM");
        SimpleDateFormat ftYear = new SimpleDateFormat("yyyy");

        DatePickerDialog dp = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd/MM/yyyy");
                        String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        Date date = null;
                        try {
                            date = formatter.parse(dateInString);
                        } catch (java.text.ParseException e) {

                            e.printStackTrace();
                        }

                        txtDate.setText(formatter.format(date));
                    }

                }, Integer.parseInt(ftYear.format(date)),
                Integer.parseInt(ftMonth.format(date)) - 1,
                Integer.parseInt(ftDay.format(date)));
        dp.setTitle("Calender");
        dp.setMessage("Select Day.");

        dp.show();
    }

    private void register() {
        if (edtName.getText().toString().trim().length() == 0) {
            showToastError(getString(R.string.blank_name));
        } else if (edtPhoneNumber.getText().toString().trim().length() == 0) {
            showToastError(getString(R.string.blank_phone));
        } else if (nation_code.length() == 0) {
            showToastError(getString(R.string.blank_nation));
        } else if (dormitory_id.length() == 0) {
            showToastError(getString(R.string.blank_dormitory));
        } else if (txtDob.getText().toString().length() == 0) {
            showToastError(getString(R.string.blank_dob));
        } else if (txtDoe.getText().toString().length() == 0) {
            showToastError(getString(R.string.blank_doe));
        } else if (edtRoom.getText().toString().trim().length() == 0) {
            showToastError(getString(R.string.blank_room));
        } else if (edtPassport.getText().toString().trim().length() == 0) {
            showToastError(getString(R.string.blank_passport));
        } else if (edtFIN.getText().toString().trim().length() == 0) {
            showToastError(getString(R.string.blank_fin));
        } else if (edtCompany.getText().toString().trim().length() == 0) {
            showToastError(getString(R.string.blank_company));
        } else {
            Worker worker = new Worker();
            worker.name = edtName.getText().toString().trim();
            worker.phone = edtPhoneNumber.getText().toString().trim();
            worker.DOB = StaticFunction.convert_ddmmyyyy_to_yyyymmdd(txtDob.getText().toString());
            worker.gender = radioMale.isChecked() ? "M" : "F";
            worker.nationality = nation_code;
            worker.room = edtRoom.getText().toString().trim();
            worker.passport_no = edtPassport.getText().toString().trim();
            worker.DOE = StaticFunction.convert_ddmmyyyy_to_yyyymmdd(txtDoe.getText().toString());
            worker.FIN = edtFIN.getText().toString().trim();
            worker.company = edtCompany.getText().toString().trim();
            worker.dormitory_id = dormitory_id;
            SharedPreferences preferences = getSharedPreferences("worker", Context.MODE_PRIVATE);
            worker.RFID = preferences.getString("rfid", "");

            WorkerVolley workerVolley = new WorkerVolley(this);
            workerVolley.registerWorker(worker, new iCallBack.WorkerRegisterCallback() {
                @Override
                public void onSuccess(boolean dataOk, String message, String RFID) {
                    if (dataOk) {
                        login(RFID);
                    } else {
                        showToastError(message);
                        hideProgressDialog();
                    }
                }

                @Override
                public void onError(String error) {
                    showToastError(error);
                    hideProgressDialog();
                }
            });
            showProgressDialog(false);
        }
    }

    private void login(String RFID) {
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.loginWorkerByRfid(new iCallBack.WorkerLoginCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    Intent intentHello = new Intent(RegisterFormActivity.this, HelloActivity.class);
                    startActivity(intentHello);
//                    showToastOk(GlobalData.gWorker.name);
                    finish();
                } else {
                    Intent intentRegisterCamera = new Intent(RegisterFormActivity.this, RegisterCameraActivity.class);
                    startActivity(intentRegisterCamera);
                    finish();
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, RFID);
        showProgressDialog(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    private void getDormitoryKiosk() {
        KioskVolley kioskVolley = new KioskVolley(this);
        kioskVolley.getDormitoryKiosk(new iCallBack.KioskGetDormitoryCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    setDormitory();
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

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(OCRService.RECEIVER_GET_IMAGE_2)) {
                if (intent.getBooleanExtra(OCRService.EXTRA_RESULT_IS_SUCCESS, false)) {
                    setData();
                }
//                showToastOk("" + intent.getBooleanExtra(OCRService.EXTRA_RESULT_IS_SUCCESS, false));
            } else if (intent.getAction().equalsIgnoreCase(OCRService.RECEIVER_GET_IMAGE_3)) {
                if (intent.getBooleanExtra(OCRService.EXTRA_RESULT_IS_SUCCESS, false)) {
                    setData();
                }
//                showToastOk("" + intent.getBooleanExtra(OCRService.EXTRA_RESULT_IS_SUCCESS, false));
//                handler.removeCallbacksAndMessages(null);
//                hideProgressDialog();
            }
        }
    };

    @Override
    public void onCLickItem(String name, String code) {
        txtNation.setText(name);
        nation_code = code;
        dialog.dismiss();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (edtFIN.isFocused()) {
            if (visible) {
                imvHideKeyboard.setVisibility(View.VISIBLE);
            } else {
                imvHideKeyboard.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCLickItem(String name, Long dormitory_id) {
        txtDormitary.setText(name);
        this.dormitory_id = dormitory_id + "";
        dialog.dismiss();
    }
}
