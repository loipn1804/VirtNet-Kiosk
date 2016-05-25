package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;

/**
 * Created by USER on 11/26/2015.
 */
public class SendWifiCodeSuccessActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTitle;
    private TextView txtSuccess;
    private TextView txtHaveProblem;
    private TextView txtReportIssue;
    private TextView txtExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_wifi_code_success);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSuccess = (TextView) findViewById(R.id.txtSuccess);
        txtHaveProblem = (TextView) findViewById(R.id.txtHaveProblem);
        txtReportIssue = (TextView) findViewById(R.id.txtReportIssue);
        txtExit = (TextView) findViewById(R.id.txtExit);

        txtReportIssue.setOnClickListener(this);
        txtExit.setOnClickListener(this);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);

        if (getIntent().hasExtra("sms")) {
            txtSuccess.setText(getString(R.string.success_send_sms));
            txtHaveProblem.setText(getString(R.string.have_problem_send_sms));
        }

        txtReportIssue.setText(txtReportIssue.getText().toString() + ".");
        SpannableString s = SpannableString.valueOf(txtReportIssue.getText().toString());
        s.setSpan(new UnderlineSpan(), 0, txtReportIssue.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtReportIssue.setText(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtReportIssue:
                Intent intentReportIssue = new Intent(this, ReportIssueActivity.class);
                startActivity(intentReportIssue);
                break;
            case R.id.txtExit:
                Intent intentHello = new Intent(this, HelloActivity.class);
                intentHello.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHello);
                finish();
                break;
        }
    }
}
