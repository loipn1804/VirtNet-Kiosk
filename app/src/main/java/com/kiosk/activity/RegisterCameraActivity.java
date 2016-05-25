package com.kiosk.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.service.OCRService;
import com.kiosk.staticfunction.Font;
import com.kiosk.view.CameraSurfaceView;

/**
 * Created by USER on 12/3/2015.
 */
public class RegisterCameraActivity extends BaseActivity implements View.OnClickListener, CameraSurfaceView.CameraSurfaceCallback {

    private FrameLayout frameCamera;
    private ImageView imvCamera;
    private RelativeLayout containView;
    private TextView txtCancel;

    private View view_1;
    private View view_2;
    //    private View view_3;
    private LayoutInflater layoutInflater;

    private CameraSurfaceView cameraSurfaceView;

    public static String FILE_NAME_1 = "reg_1.png";
    public static String FILE_NAME_2 = "reg_2.png";
    public static String FILE_NAME_3 = "reg_3.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_camera);

        SharedPreferences preferences = getSharedPreferences(OCRService.REGISTER_IMAGE_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));
        frameCamera = (FrameLayout) findViewById(R.id.frameCamera);
        imvCamera = (ImageView) findViewById(R.id.imvCamera);
        containView = (RelativeLayout) findViewById(R.id.containView);
        txtCancel = (TextView) findViewById(R.id.txtCancel);

        txtCancel.setOnClickListener(this);
        imvCamera.setOnClickListener(this);
    }

    private void initData() {
        cameraSurfaceView = new CameraSurfaceView(this, FILE_NAME_1, this);
        frameCamera.addView(cameraSurfaceView);

        layoutInflater = LayoutInflater.from(this);
        view_1 = layoutInflater.inflate(R.layout.view_reg_camera_1, null);
        view_2 = layoutInflater.inflate(R.layout.view_reg_camera_2, null);
//        view_3 = layoutInflater.inflate(R.layout.view_reg_camera_3, null);

        containView.addView(view_1);

        TextView txtTitle_1 = (TextView) view_1.findViewById(R.id.txtTitle1);
        try {
            CharSequence text = txtTitle_1.getText();
            String string = text.toString();
            int start = string.indexOf(getString(R.string.label_reg_1_underline));
            int end = start + getString(R.string.label_reg_1_underline).length();

            SpannableString s = SpannableString.valueOf(txtTitle_1.getText().toString());
            s.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtTitle_1.setText(s);
        } catch (Exception e) {

        }

        TextView txtTitle_2 = (TextView) view_2.findViewById(R.id.txtTitle2);
        try {
            CharSequence text = txtTitle_2.getText();
            String string = text.toString();
            int start = string.indexOf(getString(R.string.label_reg_2_underline));
            int end = start + getString(R.string.label_reg_2_underline).length();

            SpannableString s = SpannableString.valueOf(txtTitle_2.getText().toString());
            s.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtTitle_2.setText(s);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvCamera:
                cameraSurfaceView.TakePicture();
                imvCamera.setClickable(false);
                break;
            case R.id.txtCancel:
                finish();
                break;
        }
    }

    @Override
    public void saveImageSuccess(String filename) {
        if (filename.equalsIgnoreCase(FILE_NAME_1)) {
            containView.removeAllViews();
            containView.addView(view_2);
            cameraSurfaceView.setFilename(FILE_NAME_2);
            imvCamera.setClickable(true);
        } else if (filename.equalsIgnoreCase(FILE_NAME_2)) {
//            Intent intent2 = new Intent(this, OCRService.class);
//            intent2.setAction(OCRService.ACTION_GET_IMAGE_2);
//            startService(intent2);
//            containView.removeAllViews();
//            containView.addView(view_3);
//            cameraSurfaceView.setFilename(FILE_NAME_3);
//            imvCamera.setClickable(true);

            Intent intent2 = new Intent(this, OCRService.class);
            intent2.setAction(OCRService.ACTION_GET_IMAGE_2);
            startService(intent2);
            Intent intentRegisterForm = new Intent(this, RegisterFormActivity.class);
            startActivity(intentRegisterForm);
            finish();
        } else if (filename.equalsIgnoreCase(FILE_NAME_3)) {
//            Intent intent3 = new Intent(this, OCRService.class);
//            intent3.setAction(OCRService.ACTION_GET_IMAGE_3);
//            startService(intent3);
//            Intent intentRegisterForm = new Intent(this, RegisterFormActivity.class);
//            startActivity(intentRegisterForm);
//            finish();
        }
    }

    @Override
    public void saveImageFail() {
        imvCamera.setClickable(true);
    }
}
