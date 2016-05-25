package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;

/**
 * Created by USER on 11/27/2015.
 */
public class ThankYouActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtHome = (TextView) findViewById(R.id.txtHome);

        txtHome.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtHome:
                Intent intentHello = new Intent(this, HelloActivity.class);
                intentHello.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHello);
                finish();
                break;
        }
    }
}
