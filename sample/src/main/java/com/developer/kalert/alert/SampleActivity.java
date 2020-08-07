package com.developer.kalert.alert;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import androidx.core.content.ContextCompat;

import com.developer.kalert.KAlertDialog;

public class SampleActivity extends Activity implements View.OnClickListener{

    private int i = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_AppCompat);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        findViewById(R.id.basic_test).setOnClickListener(this);
        findViewById(R.id.under_text_test).setOnClickListener(this);
        findViewById(R.id.error_text_test).setOnClickListener(this);
        findViewById(R.id.success_text_test).setOnClickListener(this);
        findViewById(R.id.warning_confirm_test).setOnClickListener(this);
        findViewById(R.id.warning_cancel_test).setOnClickListener(this);
        findViewById(R.id.custom_img_test).setOnClickListener(this);
        findViewById(R.id.progress_dialog).setOnClickListener(this);

        findViewById(R.id.checkbox1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basic_test:
                KAlertDialog sd = new KAlertDialog(this);
                sd.setTitleText("Title");
                sd.setContentText("Content");
                sd.setConfirmText("Ok");
                sd.setCancelable(true);
                sd.setCanceledOnTouchOutside(true);
                sd.show();
                break;
            case R.id.under_text_test:
                new KAlertDialog(this)
                        .setTitleText("Title Text")
                        .setContentText("Hello")
                        .setConfirmText("Ok")
                        .show();

                break;
            case R.id.error_text_test:
                new KAlertDialog(this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Opps.")
                        .setContentText("Something went wrong!")
                        .setConfirmText("Ok")
                        .show();
                break;
            case R.id.success_text_test:
                new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("You clicked the button!")
                        .setConfirmText("Ok")
                        .show();

                break;
            case R.id.warning_confirm_test:
                new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(sDialog -> sDialog.setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(KAlertDialog.SUCCESS_TYPE))
                        .show();
                break;
            case R.id.warning_cancel_test:
                new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setCancelText("No,cancel plx!")
                        .setConfirmText("Yes,delete it!")
                        .showCancelButton(true)
                        .setCancelClickListener(sDialog -> sDialog.setTitleText("Cancelled!")
                                .setContentText("Your imaginary file is safe :)")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(KAlertDialog.ERROR_TYPE))
                        .setConfirmClickListener(sDialog -> sDialog

                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE))

                        .show();
                break;
            case R.id.custom_img_test:
                new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("KAlertDialog")
                        .setContentText("Here's a custom image.")
                        .setCustomImage(R.mipmap.ic_launcher,SampleActivity.this)
                        .setConfirmText("OK")
                        .show();
                break;
            case R.id.progress_dialog:
                final KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(800 * 7, 800) {
                    public void onTick(long millisUntilFinished) {
                        i++;
                        switch (i) {
                            case 0:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (SampleActivity.this,R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (SampleActivity.this,R.color.material_deep_teal_50));
                                break;
                            case 2:
                            case 6:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (SampleActivity.this,R.color.success_stroke_color));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (SampleActivity.this,R.color.material_deep_teal_20));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (SampleActivity.this,R.color.material_blue_grey_80));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (SampleActivity.this,R.color.warning_stroke_color));
                                break;
                        }
                    }

                    public void onFinish() {
                        i = -1;
                        pDialog.setTitleText("Success!")
                                .setConfirmText("OK")
                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                    }
                }.start();
                break;
            case R.id.checkbox1:
                KAlertDialog.DARK_STYLE = ((CheckBox) v).isChecked();
                break;
        }
    }
}
