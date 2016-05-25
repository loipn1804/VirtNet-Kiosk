package com.kiosk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;
import com.kiosk.view.CameraSurfaceView;

/**
 * Created by USER on 12/3/2015.
 */
public class RegisterStep3Activity extends BaseActivity implements View.OnClickListener, CameraSurfaceView.CameraSurfaceCallback {

    private FrameLayout frameCamera;
    private ImageView imvCamera;

    private CameraSurfaceView cameraSurfaceView;

    private String FILE_NAME = "reg_3.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_3);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));
        frameCamera = (FrameLayout) findViewById(R.id.frameCamera);
        imvCamera = (ImageView) findViewById(R.id.imvCamera);

        imvCamera.setOnClickListener(this);
    }

    private void initData() {
        cameraSurfaceView = new CameraSurfaceView(this, FILE_NAME, this);
        frameCamera.addView(cameraSurfaceView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvCamera:
                cameraSurfaceView.TakePicture();
                imvCamera.setClickable(false);
                break;

        }
    }

    @Override
    public void saveImageSuccess(String filename) {
        finish();
    }

    @Override
    public void saveImageFail() {
        imvCamera.setClickable(true);
    }
}
