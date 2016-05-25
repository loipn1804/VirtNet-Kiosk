package com.kiosk.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kiosk.R;
import com.kiosk.printer.PrinterManager;
import com.kiosk.staticfunction.Font;

/**
 * Created by USER on 11/26/2015.
 */
public class ShowGeneratedWifiCodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTitle;
    private TextView txtWifiCode;
    private TextView txtShowWifiCode;
    private TextView txtPrint;
    private TextView txtSms;
    private RelativeLayout rltHome;

    private Context mContext = null;

    private String wifi_code = "";

    private boolean isSentSms = false;
    private boolean isPrint = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_code);

        mContext = this;

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
        txtWifiCode = (TextView) findViewById(R.id.txtWifiCode);
        txtShowWifiCode = (TextView) findViewById(R.id.txtShowWifiCode);
        txtPrint = (TextView) findViewById(R.id.txtPrint);
        txtSms = (TextView) findViewById(R.id.txtSms);
        rltHome = (RelativeLayout) findViewById(R.id.rltHome);

        txtShowWifiCode.setOnClickListener(this);
        txtPrint.setOnClickListener(this);
        txtSms.setOnClickListener(this);
        rltHome.setOnClickListener(this);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);

        txtWifiCode.setVisibility(View.GONE);
        txtWifiCode.setText(wifi_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtShowWifiCode:
                txtShowWifiCode.setVisibility(View.GONE);
                txtWifiCode.setVisibility(View.VISIBLE);
                break;
            case R.id.txtPrint:
                isPrint = true;
                PrinterManager printerManager = new PrinterManager(ShowGeneratedWifiCodeActivity.this, printerCallback);
                printerManager.print(wifi_code);
                //Intent intentSendWifiCodeSuccess = new Intent(this, SendWifiCodeSuccessActivity.class);
                //startActivity(intentSendWifiCodeSuccess);
                break;
            case R.id.txtSms:
                isSentSms = true;
                Intent intentSms = new Intent(this, SmsActivity.class);
                intentSms.putExtra("wifi_code", wifi_code);
                startActivity(intentSms);
                break;
            case R.id.rltHome:
                if (isSentSms || isPrint) {
                    Intent intentHello = new Intent(ShowGeneratedWifiCodeActivity.this, HelloActivity.class);
                    intentHello.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentHello);
                    finish();
                } else {
                    showPopupSetting(getString(R.string.confirm_goto_home));
                }
                break;
        }
    }

    public void showPopupSetting(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);
        Font.overrideFontsLight(this, dialog.findViewById(R.id.root));

        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(message);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intentHello = new Intent(ShowGeneratedWifiCodeActivity.this, HelloActivity.class);
                intentHello.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHello);
                finish();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    PrinterManager.PrinterCallback printerCallback = new PrinterManager.PrinterCallback() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Printed", Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public void onError(final String strError) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, strError, Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public void onWarning(final String strWarning) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, strWarning, Toast.LENGTH_LONG).show();
                }
            });

        }
    };
}
