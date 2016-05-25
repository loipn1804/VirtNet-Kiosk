package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.data.GlobalData;
import com.kiosk.staticfunction.Font;
import com.kiosk.staticfunction.StaticFunction;
import com.kiosk.view.KeyboardVisibilityListener;
import com.kiosk.volleycontroller.WorkerVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

/**
 * Created by USER on 11/26/2015.
 */
public class SmsActivity extends BaseActivity implements View.OnClickListener, KeyboardVisibilityListener.OnKeyboardVisibilityListener {

    private TextView txtTitle;
    private TextView txtDone;
    private TextView txtExit;
    private EditText edtPhoneNumber;
    private ImageView imvHideKeyboard;
    private KeyboardVisibilityListener keyboardVisibilityListener;

    private String wifi_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        if (getIntent().hasExtra("wifi_code")) {
            wifi_code = getIntent().getStringExtra("wifi_code");
        } else {
            finish();
        }

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDone = (TextView) findViewById(R.id.txtDone);
        txtExit = (TextView) findViewById(R.id.txtExit);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        imvHideKeyboard = (ImageView) findViewById(R.id.imvHideKeyboard);

        txtDone.setOnClickListener(this);
        txtExit.setOnClickListener(this);
        imvHideKeyboard.setOnClickListener(this);

        imvHideKeyboard.setVisibility(View.GONE);

        keyboardVisibilityListener = new KeyboardVisibilityListener();
        keyboardVisibilityListener.setKeyboardListener(this, this,
                R.id.root);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtDone:
                sendSms();
                break;
            case R.id.txtExit:
                finish();
                break;
            case R.id.imvHideKeyboard:
                StaticFunction.hideKeyboard(this);
                break;
        }
    }

    private void sendSms() {
        String phone = edtPhoneNumber.getText().toString().trim();
        if (phone.length() == 0) {
            showToastError(getString(R.string.blank_phone));
        } else {
            WorkerVolley workerVolley = new WorkerVolley(this);
            workerVolley.sendSms(new iCallBack.SendSmsCallback() {
                @Override
                public void onSuccess(boolean dataOk, String message) {
                    if (dataOk) {
                        showToastOk(message);
                        Intent intentSendWifiCodeSuccess = new Intent(SmsActivity.this, SendWifiCodeSuccessActivity.class);
                        intentSendWifiCodeSuccess.putExtra("sms", true);
                        startActivity(intentSendWifiCodeSuccess);
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
            }, phone, wifi_code);
            showProgressDialog(false);
        }
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (edtPhoneNumber.isFocused()) {
            if (visible) {
                imvHideKeyboard.setVisibility(View.VISIBLE);
            } else {
                imvHideKeyboard.setVisibility(View.GONE);
            }
        }
    }
}
