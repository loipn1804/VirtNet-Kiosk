package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
public class CheckingWifiCodeActivity extends BaseActivity implements View.OnClickListener, KeyboardVisibilityListener.OnKeyboardVisibilityListener {

    private TextView txtCheckCode;
    private TextView txtCancel;
    private TextView txtTitle;
    private EditText edtCode;
    private ImageView imvHideKeyboard;
    private KeyboardVisibilityListener keyboardVisibilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_wifi_code);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtCheckCode = (TextView) findViewById(R.id.txtCheckCode);
        txtCancel = (TextView) findViewById(R.id.txtReportIssue);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        edtCode = (EditText) findViewById(R.id.edtCode);
        imvHideKeyboard = (ImageView) findViewById(R.id.imvHideKeyboard);

        txtCheckCode.setOnClickListener(this);
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
            case R.id.txtCheckCode:
                checkWifiCode();
                break;
            case R.id.txtReportIssue:
                finish();
                break;
            case R.id.imvHideKeyboard:
                StaticFunction.hideKeyboard(this);
                break;
        }
    }

    private void checkWifiCode() {
        String code = edtCode.getText().toString().trim();
        if (code.length() == 0) {
            showToastError(getString(R.string.blank_code));
        } else {
            WorkerVolley workerVolley = new WorkerVolley(this);
            workerVolley.checkWifiCode(new iCallBack.WorkerCheckWifiCodeCallback() {
                @Override
                public void onSuccess(boolean dataOk, String message, String data) {
                    if (dataOk) {
                        showToastOk(message);
                        Intent intentCheckingResult = new Intent(CheckingWifiCodeActivity.this, CheckingWifiCodeResulfActivity.class);
                        intentCheckingResult.putExtra("data", data);
                        startActivity(intentCheckingResult);
                    } else {
                        showToastError("Invalid code");
                        if (GlobalData.gWorker.worker_id.length() > 0) {
                            Validate validate = ValidateController.getByWorkerId(CheckingWifiCodeActivity.this, Long.parseLong(GlobalData.gWorker.worker_id));
                            if (validate != null) {
                                int currentCount = validate.getCount();
                                validate.setCount(currentCount + 1);
                                validate.setTime(System.currentTimeMillis());
                                ValidateController.update(CheckingWifiCodeActivity.this, validate);
                                if (validate.getCount() >= 5) {
                                    finish();
                                }
                            } else {
                                ValidateController.insert(CheckingWifiCodeActivity.this, new Validate(Long.parseLong(GlobalData.gWorker.worker_id), 1, System.currentTimeMillis()));
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
            }, code);
            showProgressDialog(false);
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
            if (count > before && start == s.length() - 1) {
                int counter = s.toString().split("-").length - 1;
                if (counter == 0) {
                    if (s.length() > 0 && (s.length() - counter) % 5 == 0 && !s.toString().endsWith("-")) {
                        edtCode.setText(s + "-");
                        int textLength = edtCode.getText().toString().length();
                        edtCode.setSelection(textLength, textLength);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
