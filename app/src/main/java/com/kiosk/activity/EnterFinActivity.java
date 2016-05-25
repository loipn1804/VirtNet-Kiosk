package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;
import com.kiosk.staticfunction.StaticFunction;
import com.kiosk.view.KeyboardVisibilityListener;
import com.kiosk.volleycontroller.WorkerVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

/**
 * Created by USER on 1/6/2016.
 */
public class EnterFinActivity extends BaseActivity implements View.OnClickListener, KeyboardVisibilityListener.OnKeyboardVisibilityListener {

    private TextView txtTitle;
    private TextView txtDone;
    private TextView txtExit;
    private EditText edtPhoneNumber;
    private ImageView imvHideKeyboard;
    private KeyboardVisibilityListener keyboardVisibilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_fin);

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
        txtTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtDone:
                sendSms();
                break;
            case R.id.txtExit:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.imvHideKeyboard:
                StaticFunction.hideKeyboard(this);
                break;
        }
    }

    private void sendSms() {
        String fin = edtPhoneNumber.getText().toString().trim();
        if (fin.length() == 0) {
            showToastError(getString(R.string.blank_fin));
        } else {
            Intent intent = new Intent();
            intent.putExtra("barcode", fin);
            setResult(RESULT_OK, intent);
            finish();
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
