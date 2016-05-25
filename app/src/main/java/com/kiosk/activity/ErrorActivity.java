package com.kiosk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.receiver.NetworkChangeReceiver;
import com.kiosk.staticfunction.CustomTypefaceSpan;
import com.kiosk.staticfunction.Font;

/**
 * Created by USER on 11/27/2015.
 */
public class ErrorActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtSystem;
    private TextView txtMaintenance;
    private TextView txtBitStudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NetworkChangeReceiver.ON_NETWORK_CHANGE);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtSystem = (TextView) findViewById(R.id.txtSystem);
        txtMaintenance = (TextView) findViewById(R.id.txtMaintenance);
        txtBitStudio = (TextView) findViewById(R.id.txtBitStudio);

        txtSystem.setOnClickListener(this);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtSystem);
        Font.overrideFontsRegular(this, txtMaintenance);
        Font.overrideFontsBold(this, txtBitStudio);

        CharSequence text = txtMaintenance.getText();
        String string = text.toString();
        int start = string.indexOf(getString(R.string.maintenance_hightlight));
        int end = start + getString(R.string.maintenance_hightlight).length();

        SpannableString s = SpannableString.valueOf(txtMaintenance.getText().toString());
        s.setSpan(new CustomTypefaceSpan("", Font.Semibold(this)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtMaintenance.setText(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSystem:

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(NetworkChangeReceiver.ON_NETWORK_CHANGE)) {
                String result = intent.getStringExtra(NetworkChangeReceiver.EXTRA_RESULT);
                if (result.equalsIgnoreCase(NetworkChangeReceiver.OFFLINE)) {

                } else if (result.equalsIgnoreCase(NetworkChangeReceiver.ONLINE)) {
                    finish();
                }
            }
        }
    };
}
