package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.daocontroller.ValidateController;
import com.kiosk.data.GlobalData;
import com.kiosk.staticfunction.Font;

import greendao.Validate;

/**
 * Created by USER on 11/26/2015.
 */
public class HelloActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtGeneralWifiCode;
    private TextView txtCheckingWifiCode;
    private TextView txtReportIssue;
    private TextView txtSignOut;
    private TextView txtTitle;
    private TextView txtName;

    private int MIN_BLOCK = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtGeneralWifiCode = (TextView) findViewById(R.id.txtGeneralWifiCode);
        txtCheckingWifiCode = (TextView) findViewById(R.id.txtCheckingWifiCode);
        txtReportIssue = (TextView) findViewById(R.id.txtReportIssue);
        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtName = (TextView) findViewById(R.id.txtName);

        txtGeneralWifiCode.setOnClickListener(this);
        txtCheckingWifiCode.setOnClickListener(this);
        txtReportIssue.setOnClickListener(this);
        txtSignOut.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateWorker();
    }

    private void validateWorker() {
        Validate validate = ValidateController.getByWorkerId(this, Long.parseLong(GlobalData.gWorker.worker_id));
        if (validate != null) {
            if (validate.getCount() >= 5) {
                Long remainmili = (validate.getTime() + MIN_BLOCK * 60 * 1000) - System.currentTimeMillis();
                int min = (int) (remainmili / 1000 / 60);
                if (min > 0) {
                    showToastError(getString(R.string.validate_msg) + " " + (min + 1) + " " + getString(R.string.validate_min));
                    finish();
                } else {
                    ValidateController.clearByObject(this, validate);
                }
            } else {
                Long remainmili = (validate.getTime() + MIN_BLOCK * 60 * 1000) - System.currentTimeMillis();
                int min = (int) (remainmili / 1000 / 60);
                if (min > 0) {

                } else {
                    ValidateController.clearByObject(this, validate);
                }
            }
        }
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);

        txtName.setText(GlobalData.gWorker.name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtGeneralWifiCode:
                Intent intentGeneralWifiCode = new Intent(this, GenerateWifiCodeActivity.class);
                startActivity(intentGeneralWifiCode);
                break;
            case R.id.txtCheckingWifiCode:
                Intent intentCheckingWifiCode = new Intent(this, CheckingWifiCodeActivity.class);
                startActivity(intentCheckingWifiCode);
                break;
            case R.id.txtReportIssue:
                Intent intentReportIssue = new Intent(this, ReportIssueActivity.class);
                startActivity(intentReportIssue);
                break;
            case R.id.txtSignOut:
                finish();
                break;
        }
    }
}
