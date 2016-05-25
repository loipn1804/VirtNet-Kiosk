package com.kiosk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;
import com.kiosk.view.Barcode2CameraSurfaceView;
import com.kiosk.view.CameraSurfaceView;
import com.kiosk.volleycontroller.WorkerVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

/**
 * Created by USER on 11/27/2015.
 */
public class ScanBarcodeActivity extends BaseActivity implements View.OnClickListener, Barcode2CameraSurfaceView.CameraSurfaceCallback {

    private int REQUEST_BARCODE = 112;

    private FrameLayout frameCamera;
    private Barcode2CameraSurfaceView cameraSurfaceView;
    private TextView txtCancel;
    private TextView txtCannotScan;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));
        frameCamera = (FrameLayout) findViewById(R.id.frameCamera);
    }

    private void initData() {
        cameraSurfaceView = new Barcode2CameraSurfaceView(this, this);
        frameCamera.addView(cameraSurfaceView);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtCannotScan = (TextView) findViewById(R.id.txtCannotScan);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        txtCancel.setOnClickListener(this);
        txtCannotScan.setOnClickListener(this);

        try {
            CharSequence text = txtTitle.getText();
            String string = text.toString();
            int start = string.indexOf(getString(R.string.barcode));
            int end = start + getString(R.string.barcode).length();

            SpannableString s = SpannableString.valueOf(txtTitle.getText().toString());
            s.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtTitle.setText(s);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtCancel:
                finish();
                break;
            case R.id.txtCannotScan:
                Intent intent = new Intent(this, EnterFinActivity.class);
                startActivityForResult(intent, REQUEST_BARCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BARCODE) {
            if (resultCode == RESULT_OK) {
                String barcode = data.getStringExtra("barcode");
                Intent intent = new Intent();
                intent.putExtra("barcode", barcode);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                finish();
            }
        }
    }

    @Override
    public void getBarcodeSuccess(String barcode) {
        Intent intent = new Intent();
        intent.putExtra("barcode", barcode);
        setResult(RESULT_OK, intent);
        finish();
    }
}
