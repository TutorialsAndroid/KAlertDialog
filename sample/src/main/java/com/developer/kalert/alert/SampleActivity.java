package com.developer.kalert.alert;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.developer.kalert.KAlertDialog;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

    private int i = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        findViewById(R.id.basic_test).setOnClickListener(this);
        findViewById(R.id.under_text_test).setOnClickListener(this);
        findViewById(R.id.title_with_font_changed).setOnClickListener(this);
        findViewById(R.id.content_text_with_font_changed).setOnClickListener(this);
        findViewById(R.id.title_gravity).setOnClickListener(this);
        findViewById(R.id.error_text_test).setOnClickListener(this);
        findViewById(R.id.success_text_test).setOnClickListener(this);
        findViewById(R.id.warning_confirm_test).setOnClickListener(this);
        findViewById(R.id.warning_cancel_test).setOnClickListener(this);
        findViewById(R.id.custom_img_test).setOnClickListener(this);
        findViewById(R.id.custom_url_image_circle).setOnClickListener(this);
        findViewById(R.id.custom_url_image_big).setOnClickListener(this);
        findViewById(R.id.edit_text_dialog).setOnClickListener(this);
        findViewById(R.id.progress_dialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.basic_test) {
            KAlertDialog sd = new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true);
            sd.setTitleText("Lorem Ipsum");
            sd.setContentText("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.");
            sd.setConfirmClickListener("OK", null);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
        }

        if (v.getId() == R.id.under_text_test) {
            new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                    .setTitleText("Lorem Ipsum")
                    .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.title_with_font_changed) {
            new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                    .setTitleText("Lorem Ipsum")
                    .setTitleFontAssets("fonts/sf.ttf")
                    .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.content_text_with_font_changed) {
            new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                    .setTitleText("Lorem Ipsum")
                    .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                    .setContentFont(R.font.sf)
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.title_gravity) {
            new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                    .setTitleText("Lorem Ipsum")
                    .setTitleTextLayoutGravity(Gravity.START) //you can specify your own gravity
                    .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.error_text_test) {
            new KAlertDialog(this, KAlertDialog.ERROR_TYPE, true)
                    .setTitleText("Oops.")
                    .setContentText("Something went wrong!")
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.success_text_test) {
            new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE, true)
                    .setTitleText("Good job!")
                    .setContentText("You clicked the button!")
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.warning_confirm_test) {
            new KAlertDialog(this, KAlertDialog.WARNING_TYPE, true)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to recover this file!")
                    .setConfirmClickListener("Yes,delete it!", sDialog -> sDialog.setTitleText("Deleted!")
                            .setContentText("Your imaginary file has been deleted!")
                            .setConfirmClickListener("OK", null)
                            .changeAlertType(KAlertDialog.SUCCESS_TYPE))
                    .show();
        }

        if (v.getId() == R.id.warning_cancel_test) {
            new KAlertDialog(this, KAlertDialog.WARNING_TYPE, true)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to recover this file!")
                    .showCancelButton(true)
                    .setConfirmButtonFont(R.font.sf)
                    .setCancelButtonFontAssets("fonts/sf.ttf")
                    .setCancelClickListener("No,cancel plx!", sDialog -> sDialog.setTitleText("Cancelled!")
                            .setContentText("Your imaginary file is safe :)")
                            .showCancelButton(false)
                            .setConfirmClickListener("OK", null)
                            .changeAlertType(KAlertDialog.ERROR_TYPE))
                    .setConfirmClickListener("Yes,delete it!", sDialog -> sDialog.setTitleText("Deleted!")
                            .showCancelButton(false)
                            .setContentText("Your imaginary file has been deleted!")
                            .setConfirmClickListener("OK", null)
                            .changeAlertType(KAlertDialog.SUCCESS_TYPE))
                    .show();
        }

        if (v.getId() == R.id.custom_img_test) {
            new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE, true)
                    .setTitleText("KAlertDialog")
                    .setContentText("Here's a custom image.")
                    .setCustomImage(R.mipmap.ic_launcher)
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.custom_url_image_circle) {
            new KAlertDialog(this, KAlertDialog.URL_IMAGE_TYPE, true)
                    .setTitleText("KAlertDialog")
                    .setContentText("Here's a custom image.")
                    .setURLImage("https://images.unsplash.com/photo-1659098602926-969fc12ef61a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80",
                            KAlertDialog.IMAGE_CIRCLE
                    )
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.custom_url_image_big) {
            new KAlertDialog(this, KAlertDialog.URL_IMAGE_TYPE, true)
                    .setTitleText("KAlertDialog")
                    .setContentText("Here's a custom image.")
                    .setURLImage("https://images.unsplash.com/photo-1659098602926-969fc12ef61a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80",
                            KAlertDialog.IMAGE_BIG
                    )
                    .setConfirmClickListener("OK", null)
                    .show();
        }

        if (v.getId() == R.id.edit_text_dialog) {
            KAlertDialog dialog = new KAlertDialog(this, KAlertDialog.INPUT_TYPE, true);
            dialog.setInputFieldHint("Write message");
            dialog.setTitleText("A Very Big Edit Text Title With Line Break On Small Devices");
            dialog.setConfirmClickListener("OK", kAlertDialog -> {
                kAlertDialog.dismissWithAnimation();
                kAlertDialog.getInputText(); //you get the input text by calling this
                Toast.makeText(this, kAlertDialog.getInputText(), Toast.LENGTH_SHORT).show();
            });
            dialog.show();
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); //this will allow to show keyboard
        }

        if (v.getId() == R.id.progress_dialog) {
            final KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE, true)
                    .setTitleText("Loading");
            pDialog.show();
            pDialog.setCancelable(false);
            new CountDownTimer(800 * 7, 800) {
                public void onTick(long millisUntilFinished) {
                    i++;
                    switch (i) {
                        case 0:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (SampleActivity.this, R.color.blue_btn_bg_color));
                            break;
                        case 1:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (SampleActivity.this, R.color.material_deep_teal_50));
                            break;
                        case 2:
                        case 6:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (SampleActivity.this, R.color.success_stroke_color));
                            break;
                        case 3:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (SampleActivity.this, R.color.material_deep_teal_20));
                            break;
                        case 4:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (SampleActivity.this, R.color.material_blue_grey_80));
                            break;
                        case 5:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (SampleActivity.this, R.color.warning_stroke_color));
                            break;
                    }
                }

                public void onFinish() {
                    i = -1;
                    pDialog.setTitleText("Success!")
                            .setConfirmClickListener("OK", null)
                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                }
            }.start();
        }
    }
}
