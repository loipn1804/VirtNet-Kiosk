package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;

/**
 * Created by USER on 11/27/2015.
 */
public class ReportIssueActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTitle;
    private RelativeLayout rltMail;
    private RelativeLayout rltService;
    private TextView txtReportViaForm;
    private TextView txtReportViaService;
    private TextView txtReportTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentReportViaForm = new Intent(this, ReportViaFormActivity.class);
        startActivity(intentReportViaForm);
        finish();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        rltMail = (RelativeLayout) findViewById(R.id.rltMail);
        rltService = (RelativeLayout) findViewById(R.id.rltService);
        txtReportViaForm = (TextView) findViewById(R.id.txtReportViaForm);
        txtReportViaService = (TextView) findViewById(R.id.txtReportViaService);
        txtReportTime = (TextView) findViewById(R.id.txtReportTime);

        rltMail.setOnClickListener(this);
        rltService.setOnClickListener(this);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);
        Font.overrideFontsRegular(this, txtReportViaForm);
        Font.overrideFontsRegular(this, txtReportViaService);
        Font.overrideFontsRegular(this, txtReportTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMail:
                Intent intentReportViaForm = new Intent(this, ReportViaFormActivity.class);
                startActivity(intentReportViaForm);
                break;
            case R.id.rltService:
                Intent intentReportViaService = new Intent(this, ReportViaServiceActivity.class);
                startActivity(intentReportViaService);
                break;
        }
    }
}
