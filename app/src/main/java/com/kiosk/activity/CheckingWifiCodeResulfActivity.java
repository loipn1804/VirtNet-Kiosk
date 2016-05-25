package com.kiosk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 12/15/2015.
 */
public class CheckingWifiCodeResulfActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTitle;
    private TextView txtActivateAt;
    private TextView txtExpiryAt;
    private TextView txtWifiPackage;
    private TextView txtDone;
    private TextView txtExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_wifi_code_result);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtActivateAt = (TextView) findViewById(R.id.txtActivateAt);
        txtExpiryAt = (TextView) findViewById(R.id.txtExpiryAt);
        txtWifiPackage = (TextView) findViewById(R.id.txtWifiPackage);
        txtDone = (TextView) findViewById(R.id.txtDone);
        txtExit = (TextView) findViewById(R.id.txtExit);

        txtDone.setOnClickListener(this);
        txtExit.setOnClickListener(this);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);

        if (getIntent().hasExtra("data")) {
            try {
                JSONObject data = new JSONObject(getIntent().getStringExtra("data"));
                String activate_at = data.getString("used_at");
                if (activate_at.equalsIgnoreCase("null")) activate_at = "Not Used";
                String expiry_at = data.getString("expires_at");
                if (expiry_at.equalsIgnoreCase("null")) expiry_at = "Not Used";
                String duration = data.getString("expire");
                String code = data.getString("code");

                txtActivateAt.setText(activate_at);
                txtExpiryAt.setText(expiry_at);
                txtWifiPackage.setText(duration + " mins");
                txtTitle.setText(getString(R.string.your_code) + ": " + code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtDone:
                finish();
                break;
            case R.id.txtExit:
                finish();
                break;
        }
    }
}
