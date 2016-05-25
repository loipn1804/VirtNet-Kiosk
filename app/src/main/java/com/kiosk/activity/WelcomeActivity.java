package com.kiosk.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kiosk.R;
import com.kiosk.receiver.PingReceiver;
import com.kiosk.staticfunction.Font;
import com.kiosk.volleycontroller.SettingVolley;
import com.kiosk.volleycontroller.WorkerVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

import java.util.Locale;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener, View.OnSystemUiVisibilityChangeListener {

    private int REQUEST_BARCODE = 111;

    private String ENG = "en";
    private String CHI = "cn";
    private String THA = "th";
    private String HIN = "hi";
    private String BAH = "ba";

    private ImageView imvChooseEn;
    private ImageView imvChooseCh;
    private ImageView imvChooseTa;
    private ImageView imvChooseHi;
    private ImageView imvChooseBa;
    private ImageView imvChooseTh;
    private ImageView imvNext;
    private TextView txtEn;
    private TextView txtCh;
    private TextView txtTa;
    private TextView txtHi;
    private TextView txtBa;
    private TextView txtTh;
    private TextView txtWelcome;
    private TextView txtBelowWelcome;
    private ImageView imvLogo;

    private View viewFinishAppLeft;
    private View viewFinishAppRight;
    private Long timeFinish = 0l;

    private Dialog dialog_setting;

    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        disableNotificationBar();
//        disableSoftKeyBar();

        overridePendingTransition(R.anim.anim_welcome_in, 0);

        initView();
        initData();

//        Intent intentTest = new Intent(this, TestActivity.class);
//        startActivity(intentTest);
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        imvChooseEn = (ImageView) findViewById(R.id.imvChooseEn);
        imvChooseCh = (ImageView) findViewById(R.id.imvChooseCh);
        imvChooseTa = (ImageView) findViewById(R.id.imvChooseTa);
        imvChooseHi = (ImageView) findViewById(R.id.imvChooseHi);
        imvChooseBa = (ImageView) findViewById(R.id.imvChooseBa);
        imvChooseTh = (ImageView) findViewById(R.id.imvChooseTh);
        imvNext = (ImageView) findViewById(R.id.imvNext);
        txtEn = (TextView) findViewById(R.id.txtEn);
        txtCh = (TextView) findViewById(R.id.txtCh);
        txtTa = (TextView) findViewById(R.id.txtTa);
        txtHi = (TextView) findViewById(R.id.txtHi);
        txtBa = (TextView) findViewById(R.id.txtBa);
        txtTh = (TextView) findViewById(R.id.txtTh);
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        txtBelowWelcome = (TextView) findViewById(R.id.txtBelowWelcome);
        imvLogo = (ImageView) findViewById(R.id.imvLogo);
        viewFinishAppLeft = findViewById(R.id.viewFinishAppLeft);
        viewFinishAppRight = findViewById(R.id.viewFinishAppRight);

        imvNext.setOnClickListener(this);
        txtEn.setOnClickListener(this);
        txtCh.setOnClickListener(this);
        txtTh.setOnClickListener(this);
        txtHi.setOnClickListener(this);
        txtBa.setOnClickListener(this);
//        txtCh.setOnClickListener(this);
//        imvLogo.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                showPopupSetting();
//                return false;
//            }
//        });
        viewFinishAppLeft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                timeFinish = System.currentTimeMillis();
                return false;
            }
        });
        viewFinishAppRight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if ((System.currentTimeMillis() - timeFinish) < 3000) {
//                    showPopupFinishApp();
                    showPopupSetting();
                } else {
                    timeFinish = 0l;
                }
                return false;
            }
        });

        Animation a = AnimationUtils.loadAnimation(this, R.anim.anim_arrow);
        imvNext.setAnimation(a);
        a.start();
    }

    private void initData() {
        Font.overrideFontsBold(this, txtWelcome);
        txtTa.setVisibility(View.GONE);

        imvChooseEn.setVisibility(View.INVISIBLE);
        imvChooseCh.setVisibility(View.INVISIBLE);
        imvChooseTa.setVisibility(View.INVISIBLE);
        imvChooseHi.setVisibility(View.INVISIBLE);
        imvChooseBa.setVisibility(View.INVISIBLE);
        imvChooseTh.setVisibility(View.INVISIBLE);
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language.equals(ENG)) {
            Font.overrideFontsBold(this, txtEn);
            imvChooseEn.setVisibility(View.VISIBLE);
        } else if (language.equals(CHI)) {
            Font.overrideFontsBold(this, txtCh);
            imvChooseCh.setVisibility(View.VISIBLE);
        } else if (language.equals(THA)) {
            Font.overrideFontsBold(this, txtTh);
            imvChooseTh.setVisibility(View.VISIBLE);
        } else if (language.equals(HIN)) {
            Font.overrideFontsBold(this, txtHi);
            imvChooseHi.setVisibility(View.VISIBLE);
        } else if (language.equals(BAH)) {
            Font.overrideFontsBold(this, txtBa);
            imvChooseBa.setVisibility(View.VISIBLE);
        }

//        CharSequence text = txtBelowWelcome.getText();
//        String string = text.toString();
//        int start = string.indexOf(getString(R.string.below_welcome_highlight));
//        int end = start + getString(R.string.below_welcome_highlight).length();
//
//        SpannableString s = SpannableString.valueOf(txtBelowWelcome.getText().toString());
//        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_main)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        txtBelowWelcome.setText(s);

        startPingServer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvNext:
//                showPopupEnterRFID();
                Intent intent = new Intent(this, ScanBarcodeActivity.class);
                startActivityForResult(intent, REQUEST_BARCODE);
                break;
            case R.id.txtEn:
                setLocale(ENG);
                break;
            case R.id.txtCh:
                setLocale(CHI);
                break;
            case R.id.txtTh:
                setLocale(THA);
                break;
            case R.id.txtHi:
                setLocale(HIN);
                break;
            case R.id.txtBa:
                setLocale(BAH);
                break;
        }
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, WelcomeActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("kiosk", Context.MODE_PRIVATE);
        Long kiosk_id = preferences.getLong("kiosk_id", 0);
        if (kiosk_id == 0) {
            Intent intentSetting = new Intent(WelcomeActivity.this, PrivateSettingActivity.class);
            startActivity(intentSetting);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPingServer();
    }

    private void startPingServer() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, PingReceiver.class);

        PendingIntent intentExecuted = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30 * 60 * 1000, intentExecuted); // 30 min
    }

    private void stopPingServer() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, PingReceiver.class);

        PendingIntent intentExecuted = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(intentExecuted);
    }

    private void login(String RFID) {
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.loginWorkerByRfid(new iCallBack.WorkerLoginCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    Intent intentHello = new Intent(WelcomeActivity.this, HelloActivity.class);
                    startActivity(intentHello);
//                    showToastOk(GlobalData.gWorker.name);
                } else {
                    Intent intentRegisterCamera = new Intent(WelcomeActivity.this, RegisterCameraActivity.class);
                    startActivity(intentRegisterCamera);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, RFID);
        showProgressDialog(false);
    }

    private void getSetting(final String code) {
        SettingVolley settingVolley = new SettingVolley(this);
        settingVolley.getSetting(new iCallBack.SettingCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    if (code.equals(message)) {
                        dialog_setting.dismiss();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentSetting = new Intent(WelcomeActivity.this, PrivateSettingActivity.class);
                                startActivity(intentSetting);
                            }
                        }, 500);
                    } else {
                        showToastError(getString(R.string.invalid_code));
                    }
                } else {
                    showToastError(getString(R.string.try_again));
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        });
        showProgressDialog(false);
    }

    public void showPopupSetting() {
        // custom dialog
        dialog_setting = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog_setting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_setting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_setting.setCanceledOnTouchOutside(true);
        dialog_setting.setContentView(R.layout.popup_enter_code);
        Font.overrideFontsLight(this, dialog_setting.findViewById(R.id.root));

        final EditText edtCode = (EditText) dialog_setting.findViewById(R.id.edtCode);
        TextView txtDone = (TextView) dialog_setting.findViewById(R.id.txtDone);
        TextView txtTitle = (TextView) dialog_setting.findViewById(R.id.txtTitle);
        txtTitle.setText("Go to setting");

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCode.getText().toString().trim().length() == 0) {
                    showToastError(getString(R.string.blank_code));
                } else {
                    getSetting(edtCode.getText().toString().trim());
                }
            }
        });

        dialog_setting.show();
    }

    private void finishApp(final String code) {
        SettingVolley settingVolley = new SettingVolley(this);
        settingVolley.getSetting(new iCallBack.SettingCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    if (code.equals(message)) {
                        dialog_setting.dismiss();
                        finish();
                    } else {
                        showToastError(getString(R.string.invalid_code));
                    }
                } else {
                    showToastError(getString(R.string.try_again));
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        });
        showProgressDialog(false);
    }

    public void showPopupFinishApp() {
        // custom dialog
        dialog_setting = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog_setting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_setting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_setting.setCanceledOnTouchOutside(true);
        dialog_setting.setContentView(R.layout.popup_enter_code);
        Font.overrideFontsLight(this, dialog_setting.findViewById(R.id.root));

        final EditText edtCode = (EditText) dialog_setting.findViewById(R.id.edtCode);
        TextView txtDone = (TextView) dialog_setting.findViewById(R.id.txtDone);
        TextView txtTitle = (TextView) dialog_setting.findViewById(R.id.txtTitle);
        txtTitle.setText("Close app");

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCode.getText().toString().trim().length() == 0) {
                    showToastError(getString(R.string.blank_code));
                } else {
                    finishApp(edtCode.getText().toString().trim());
                }
            }
        });

        dialog_setting.show();
    }

    public void showPopupEnterRFID() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_enter_code);
        Font.overrideFontsLight(this, dialog.findViewById(R.id.root));

        final EditText edtCode = (EditText) dialog.findViewById(R.id.edtCode);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        edtCode.setInputType(InputType.TYPE_CLASS_TEXT);
        edtCode.setText("99991114");
        txtTitle.setText("RFID simulator");

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCode.getText().toString().trim().length() > 0) {
                    SharedPreferences preferences = getSharedPreferences("worker", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rfid", edtCode.getText().toString().trim());
                    editor.commit();
                    login(edtCode.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BARCODE && resultCode == RESULT_OK) {
            String barcode = data.getStringExtra("barcode");
//            showToastOk(barcode);
            SharedPreferences preferences = getSharedPreferences("worker", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("rfid", barcode);
            editor.commit();
            login(barcode);
        }
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        Toast.makeText(this, "change " + visibility, Toast.LENGTH_SHORT).show();
    }
}
