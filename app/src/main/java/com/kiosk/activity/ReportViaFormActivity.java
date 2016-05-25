package com.kiosk.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kiosk.R;
import com.kiosk.adapter.CategoryAdapter;
import com.kiosk.adapter.DormitoryAdapter;
import com.kiosk.daocontroller.CategoryController;
import com.kiosk.daocontroller.DormitoryController;
import com.kiosk.data.GlobalData;
import com.kiosk.staticfunction.Font;
import com.kiosk.staticfunction.StaticFunction;
import com.kiosk.view.KeyboardVisibilityListener;
import com.kiosk.volleycontroller.IssueVolley;
import com.kiosk.volleycontroller.KioskVolley;
import com.kiosk.volleycontroller.callback.iCallBack;

import java.util.List;

import greendao.Category;
import greendao.Dormitory;

/**
 * Created by USER on 11/27/2015.
 */
public class ReportViaFormActivity extends BaseActivity implements View.OnClickListener, CategoryAdapter.OnClickOnItemCallback, KeyboardVisibilityListener.OnKeyboardVisibilityListener {

    private TextView txtTitle;
    private TextView txtType;
    private EditText edtPhoneNumber;
    private EditText edtMessage;
    private TextView txtSubmit;
    private TextView txtCancel;
    private ImageView imvHideKeyboard;

    private String category_id = "";
    private Dialog dialog;

    private KeyboardVisibilityListener keyboardVisibilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_via_form);

        initView();
        initData();
    }

    private void initView() {
        Font.overrideFontsLight(this, findViewById(R.id.root));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtType = (TextView) findViewById(R.id.txtType);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        txtSubmit = (TextView) findViewById(R.id.txtSubmit);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        imvHideKeyboard = (ImageView) findViewById(R.id.imvHideKeyboard);

        txtSubmit.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtType.setOnClickListener(this);
        imvHideKeyboard.setOnClickListener(this);

        imvHideKeyboard.setVisibility(View.GONE);

        keyboardVisibilityListener = new KeyboardVisibilityListener();
        keyboardVisibilityListener.setKeyboardListener(this, this,
                R.id.root);
    }

    private void initData() {
        Font.overrideFontsBold(this, txtTitle);
        Font.overrideFontsRegular(this, txtType);
        Font.overrideFontsRegular(this, edtPhoneNumber);
        Font.overrideFontsRegular(this, edtMessage);

        edtPhoneNumber.setText(GlobalData.gWorker.phone);

        getCategoryIssue();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSubmit:
                uploadIssue();
                break;
            case R.id.txtCancel:
                finish();
                break;
            case R.id.txtType:
                List<Category> list = CategoryController.getAll(ReportViaFormActivity.this);
                showPopupSpinner(list);
                break;
            case R.id.imvHideKeyboard:
                StaticFunction.hideKeyboard(this);
                break;
        }
    }

    private void getCategoryIssue() {
        IssueVolley issueVolley = new IssueVolley(this);
        issueVolley.getCategoryIssue(new iCallBack.GetCategoryIssueCallback() {
            @Override
            public void onSuccess(boolean dataOk, String message) {
                if (dataOk) {
                    List<Category> list = CategoryController.getAll(ReportViaFormActivity.this);
                    if (list.size() > 0) {
                        txtType.setText(list.get(0).getName());
                        category_id = list.get(0).getCategory_id() + "";
                    }
                } else {
                    showToastError(message);
                }
//                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
//                hideProgressDialog();
            }
        });
//        showProgressDialog(false);
    }

    private void uploadIssue() {
        String content = edtMessage.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();
        if (content.length() == 0) {
            showToastError(getString(R.string.blank_message));
        } else if (phone.length() == 0) {
            showToastError(getString(R.string.blank_phone));
        } else {
            SharedPreferences preferences = getSharedPreferences("kiosk", Context.MODE_PRIVATE);
            Long kiosk_id = preferences.getLong("kiosk_id", 0);
            IssueVolley issueVolley = new IssueVolley(this);
            issueVolley.uploadIssue(new iCallBack.SendIssueCallback() {
                @Override
                public void onSuccess(boolean dataOk, String message) {
                    if (dataOk) {
                        Intent intentThankYou = new Intent(ReportViaFormActivity.this, ThankYouActivity.class);
                        startActivity(intentThankYou);
                    } else {
                        showToastError(message);
                    }
                    hideProgressDialog();
                }

                @Override
                public void onError(String error) {
                    showToastError(error);
                    hideProgressDialog();
                }
            }, kiosk_id + "", GlobalData.gWorker.worker_id, category_id, content, phone);
            showProgressDialog(false);
        }
    }

    public void showPopupSpinner(final List<Category> list) {
        // custom dialog
        dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_spinner);
        Font.overrideFontsLight(this, dialog.findViewById(R.id.root));

        ListView lvSpinner = (ListView) dialog.findViewById(R.id.lvSpinner);
        LinearLayout lnlCover = (LinearLayout) dialog.findViewById(R.id.lnlCover);

        CategoryAdapter categoryAdapter = new CategoryAdapter(ReportViaFormActivity.this, list, this);
        lvSpinner.setAdapter(categoryAdapter);

        lnlCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onCLickItem(String name, Long category_id) {
        txtType.setText(name);
        this.category_id = category_id + "";
        dialog.dismiss();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (edtMessage.isFocused()) {
            if (visible) {
                imvHideKeyboard.setVisibility(View.VISIBLE);
            } else {
                imvHideKeyboard.setVisibility(View.GONE);
            }
        }
    }
}
