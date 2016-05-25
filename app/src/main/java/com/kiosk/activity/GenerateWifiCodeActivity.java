package com.kiosk.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.daocontroller.ValidateController;
import com.kiosk.data.GlobalData;
import com.kiosk.staticfunction.Font;
import com.kiosk.staticfunction.StaticFunction;
import com.kiosk.view.KeyboardVisibilityListener;
import com.kiosk.volleycontroller.WorkerVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

import greendao.Validate;

/**
 * Created by USER on 11/26/2015.
 */
public class GenerateWifiCodeActivity extends BaseActivity implements View.OnClickListener, KeyboardVisibilityListener.OnKeyboardVisibilityListener {

    private TextView txtGetWifiAccessCode;
    private TextView txtCancel;
    private TextView txtTitle;
    private EditText edtCode;
    private ImageView imvHideKeyboard;
    private KeyboardVisibilityListener keyboardVisibilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_wifi_code);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtGetWifiAccessCode = (TextView) findViewById(R.id.txtGetWifiAccessCode);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        edtCode = (EditText) findViewById(R.id.edtCode);
        imvHideKeyboard = (ImageView) findViewById(R.id.imvHideKeyboard);

        txtGetWifiAccessCode.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        imvHideKeyboard.setOnClickListener(this);

        imvHideKeyboard.setVisibility(View.GONE);

        keyboardVisibilityListener = new KeyboardVisibilityListener();
        keyboardVisibilityListener.setKeyboardListener(this, this,
                R.id.root);

        edtCode.addTextChangedListener(new MyTextWatcher());
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtGetWifiAccessCode:
                getWifiCode();
                break;
            case R.id.txtCancel:
                finish();
                break;
            case R.id.imvHideKeyboard:
                StaticFunction.hideKeyboard(this);
                break;
        }
    }

    private void getWifiCode() {
        String code = edtCode.getText().toString().trim();
        if (code.length() == 0) {
            showToastError(getString(R.string.blank_code));
        } else {
            SharedPreferences preferences = getSharedPreferences("kiosk", MODE_PRIVATE);
            Long kiosk_id = preferences.getLong("kiosk_id", 0);
            if (kiosk_id > 0) {
                WorkerVolley workerVolley = new WorkerVolley(this);
                workerVolley.getWifiCode(new iCallBack.WorkerGetWifiCodeCallback() {
                    @Override
                    public void onSuccess(boolean dataOk, String message, String wifi_code) {
                        if (dataOk) {
                            showToastOk(message);
                            GlobalData.wifi_code = wifi_code;
                            Intent intentGeneratedCode = new Intent(GenerateWifiCodeActivity.this, ShowGeneratedWifiCodeActivity.class);
                            intentGeneratedCode.putExtra("wifi_code", wifi_code);
                            startActivity(intentGeneratedCode);
                            finish();
                        } else {
                            showToastError(message);
                            if (GlobalData.gWorker.worker_id.length() > 0) {
                                Validate validate = ValidateController.getByWorkerId(GenerateWifiCodeActivity.this, Long.parseLong(GlobalData.gWorker.worker_id));
                                if (validate != null) {
                                    int currentCount = validate.getCount();
                                    validate.setCount(currentCount + 1);
                                    validate.setTime(System.currentTimeMillis());
                                    ValidateController.update(GenerateWifiCodeActivity.this, validate);
                                    if (validate.getCount() >= 5) {
                                        finish();
                                    }
                                } else {
                                    ValidateController.insert(GenerateWifiCodeActivity.this, new Validate(Long.parseLong(GlobalData.gWorker.worker_id), 1, System.currentTimeMillis()));
                                }
                            }
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(String error) {
                        showToastError(error);
                        hideProgressDialog();
                    }
                }, code, kiosk_id + "");
                showProgressDialog(false);
            }
        }
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (edtCode.isFocused()) {
            if (visible) {
                imvHideKeyboard.setVisibility(View.VISIBLE);
            } else {
                imvHideKeyboard.setVisibility(View.GONE);
            }
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            samsung
//            if (count > before && start == s.length() - 1) {
//                int counter = s.toString().split("-").length - 1;
//                if (s.length() > 0 && (s.length() - counter) % 4 == 0 && !s.toString().endsWith("-")) {
//                    edtCode.setText(s + "-");
//                    int textLength = edtCode.getText().toString().length();
//                    edtCode.setSelection(textLength, textLength);
//                }
//            }

//            dell
            if (count >= before) {
                int counter = s.toString().split("-").length - 1;
                if (s.length() > 0 && (s.length() - counter) % 4 == 0 && !s.toString().endsWith("-")) {
                    edtCode.setText(s + "-");
                    int textLength = edtCode.getText().toString().length();
                    edtCode.setSelection(textLength, textLength);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
