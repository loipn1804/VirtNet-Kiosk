package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;

/**
 * Created by USER on 11/27/2015.
 */
public class ReportViaServiceActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTitle;
    private TextView txtEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_via_service);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtEnd = (TextView) findViewById(R.id.txtEnd);

        txtEnd.setOnClickListener(this);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtEnd:
                Intent intentHello = new Intent(this, HelloActivity.class);
                intentHello.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHello);
                finish();
                break;
        }
    }
}
