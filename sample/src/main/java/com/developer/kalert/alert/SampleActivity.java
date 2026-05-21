package com.developer.kalert.alert;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.developer.kalert.KAlertDialog;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SAMPLE_IMAGE_URL =
            "https://images.unsplash.com/photo-1659098602926-969fc12ef61a?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80";

    private int progressStep = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        bindClick(R.id.btn_modern_success);
        bindClick(R.id.btn_custom_appearance);
        bindClick(R.id.btn_font_weight);

        bindClick(R.id.btn_basic_message);
        bindClick(R.id.btn_error_dialog);
        bindClick(R.id.btn_warning_flow);

        bindClick(R.id.btn_input_validation);
        bindClick(R.id.btn_custom_view);

        bindClick(R.id.btn_custom_icon);
        bindClick(R.id.btn_url_circle);
        bindClick(R.id.btn_url_big);

        bindClick(R.id.btn_custom_buttons);
        bindClick(R.id.btn_button_icons);
        bindClick(R.id.btn_progress_shortcuts);
        bindClick(R.id.btn_callbacks);
    }

    private void bindClick(int viewId) {
        View view = findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_modern_success) {
            showModernSuccessDialog();
        } else if (id == R.id.btn_custom_appearance) {
            showCustomAppearanceDialog();
        } else if (id == R.id.btn_font_weight) {
            showFontWeightDialog();
        } else if (id == R.id.btn_basic_message) {
            showBasicMessageDialog();
        } else if (id == R.id.btn_error_dialog) {
            showErrorDialog();
        } else if (id == R.id.btn_warning_flow) {
            showWarningFlowDialog();
        } else if (id == R.id.btn_input_validation) {
            showInputValidationDialog();
        } else if (id == R.id.btn_custom_view) {
            showCustomViewDialog();
        } else if (id == R.id.btn_custom_icon) {
            showCustomIconDialog();
        } else if (id == R.id.btn_url_circle) {
            showUrlCircleDialog();
        } else if (id == R.id.btn_url_big) {
            showUrlBigDialog();
        } else if (id == R.id.btn_custom_buttons) {
            showCustomDrawableButtonDialog();
        } else if (id == R.id.btn_button_icons) {
            showButtonIconDialog();
        } else if (id == R.id.btn_progress_shortcuts) {
            showProgressShortcutDialog();
        } else if (id == R.id.btn_callbacks) {
            showCallbacksDialog();
        }
    }

    private void showModernSuccessDialog() {
        new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE, true)
                .setTitleText("Success")
                .setContentText("Your changes were saved successfully. This dialog uses the new modern style preset.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .setConfirmClickListener("Done", dialog -> dialog.dismissWithAnimation())
                .show();
    }

    private void showCustomAppearanceDialog() {
        new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                .setTitleText("Custom Appearance")
                .setContentText("This example shows custom corner radius, elevation and dim amount for a more premium dialog feel.")
                .setTitleFontWeight(Typeface.BOLD)
                .setContentFontWeight(Typeface.NORMAL)
                .setDialogCornerRadius(30)
                .setDialogElevation(14)
                .setDimAmount(0.55f)
                .setConfirmButtonFontWeight(Typeface.BOLD)
                .setConfirmButtonAllCaps(false)
                .setConfirmClickListener("Looks Good", dialog -> dialog.dismissWithAnimation())
                .show();
    }

    private void showFontWeightDialog() {
        new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                .setTitleText("Font Weight Support")
                .setContentText("You can now control title, content and button font weights directly from the dialog API.")
                .setTitleFontWeight(Typeface.BOLD)
                .setContentFontWeight(Typeface.ITALIC)
                .setConfirmButtonFontWeight(Typeface.BOLD)
                .setConfirmButtonTextSize(15)
                .setConfirmButtonAllCaps(false)
                .setConfirmClickListener("Awesome", dialog -> dialog.dismissWithAnimation())
                .show();
    }

    private void showBasicMessageDialog() {
        KAlertDialog dialog = new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true);
        dialog.setTitleText("Basic Message");
        dialog.setContentText("This is a clean basic message dialog. Existing old APIs still work perfectly.");
        dialog.setTitleTextLayoutGravity(Gravity.CENTER);
        dialog.setConfirmClickListener("OK", null);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showErrorDialog() {
        new KAlertDialog(this, KAlertDialog.ERROR_TYPE, true)
                .setTitleText("Something went wrong")
                .setContentText("We could not complete this action. Please check your connection and try again.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .setConfirmClickListener("Try Again", dialog -> dialog.dismissWithAnimation())
                .show();
    }

    private void showWarningFlowDialog() {
        new KAlertDialog(this, KAlertDialog.WARNING_TYPE, true)
                .setTitleText("Delete this file?")
                .setContentText("This action cannot be undone. You can confirm or cancel this action.")
                .applyStyle(KAlertDialog.STYLE_ROUNDED)
                .showCancelButton(true)
                .setCancelClickListener("Cancel", dialog -> dialog
                        .setTitleText("Cancelled")
                        .setContentText("Your file is safe. No changes were made.")
                        .showCancelButton(false)
                        .setConfirmClickListener("OK", null)
                        .changeAlertType(KAlertDialog.ERROR_TYPE))
                .setConfirmClickListener("Delete", dialog -> dialog
                        .setTitleText("Deleted")
                        .setContentText("The file has been deleted successfully.")
                        .showCancelButton(false)
                        .setConfirmClickListener("OK", null)
                        .changeAlertType(KAlertDialog.SUCCESS_TYPE))
                .show();
    }

    private void showInputValidationDialog() {
        new KAlertDialog(this, KAlertDialog.INPUT_TYPE, true)
                .setTitleText("Create Project")
                .setContentText("Enter a project name with at least 3 characters.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .setInputFieldHint("Project name")
                .setInputFieldText("KAlertDialog")
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .setInputMaxLength(30)
                .setInputError("Project name must be at least 3 characters")
                .setInputValidator(input -> input != null && input.trim().length() >= 3)
                .setOnInputConfirmListener((dialog, input) -> {
                    dialog.dismissWithAnimation();
                    Toast.makeText(this, "Created: " + input.trim(), Toast.LENGTH_SHORT).show();
                })
                .setConfirmClickListener("Create", null)
                .show();
    }

    private void showCustomViewDialog() {
        KAlertDialog dialog = new KAlertDialog(this, KAlertDialog.CUSTOM_VIEW_TYPE, true)
                .setTitleText("Custom View")
                .setContentText("This dialog uses your custom XML layout inside KAlertDialog.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .setCustomView(R.layout.customedittext)
                .showCancelButton(true)
                .setCancelClickListener("Cancel", kAlertDialog -> kAlertDialog.dismissWithAnimation());

        dialog.setConfirmClickListener("Save", kAlertDialog -> {
            View customView = kAlertDialog.getCustomView();

            if (customView == null) {
                Toast.makeText(this, "Custom view not found", Toast.LENGTH_SHORT).show();
                return;
            }

            EditText editText = customView.findViewById(R.id.edit_query);

            if (editText == null) {
                Toast.makeText(this, "EditText not found", Toast.LENGTH_SHORT).show();
                return;
            }

            String value = editText.getText().toString().trim();

            if (value.length() < 3) {
                editText.setError("Please enter at least 3 characters");
                return;
            }

            kAlertDialog.dismissWithAnimation();
            Toast.makeText(this, "Saved: " + value, Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void showCustomIconDialog() {
        new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE, true)
                .setTitleText("Custom Icon")
                .setContentText("You can use your app icon, vector drawable or any custom drawable inside the dialog.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .setCustomImage(R.mipmap.ic_launcher)
                .setDrawableTintOnNightMode(true, android.R.color.white)
                .setConfirmClickListener("OK", null)
                .show();
    }

    private void showUrlCircleDialog() {
        new KAlertDialog(this, KAlertDialog.URL_IMAGE_TYPE, true)
                .setTitleText("Circle URL Image")
                .setContentText("This example loads a remote image using circle crop mode.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .setURLImagePlaceholder(R.mipmap.ic_launcher)
                .setURLImageError(R.mipmap.ic_launcher)
                .setURLImage(SAMPLE_IMAGE_URL, KAlertDialog.IMAGE_CIRCLE)
                .setConfirmClickListener("Nice", null)
                .show();
    }

    private void showUrlBigDialog() {
        new KAlertDialog(this, KAlertDialog.URL_IMAGE_TYPE, true)
                .setTitleText("Big URL Image")
                .setContentText("This example loads a large image inside the dialog with placeholder and error fallback.")
                .applyStyle(KAlertDialog.STYLE_ROUNDED)
                .setURLImagePlaceholder(R.mipmap.ic_launcher)
                .setURLImageError(R.mipmap.ic_launcher)
                .setURLImage(SAMPLE_IMAGE_URL, KAlertDialog.IMAGE_BIG)
                .setConfirmClickListener("Close", null)
                .show();
    }

    private void showCustomDrawableButtonDialog() {
        new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                .setTitleText("Custom Buttons")
                .setContentText("Confirm and cancel buttons can use custom drawable backgrounds.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .showCancelButton(true)
                .setConfirmClickListener("Confirm", null)
                .setCancelClickListener("Cancel", null)
                .confirmButtonDrawable(R.drawable.red_button_background)
                .cancelButtonDrawable(R.drawable.button_background)
                .show();
    }

    private void showButtonIconDialog() {
        new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                .setTitleText("Button Styling")
                .setContentText("This example shows button text size, font weight, all caps control and button icons.")
                .applyStyle(KAlertDialog.STYLE_MINIMAL)
                .showCancelButton(true)
                .setConfirmButtonTextSize(15)
                .setCancelButtonTextSize(15)
                .setConfirmButtonFontWeight(Typeface.BOLD)
                .setCancelButtonFontWeight(Typeface.BOLD)
                .setConfirmButtonAllCaps(false)
                .setCancelButtonAllCaps(false)
                .setConfirmButtonIcon(android.R.drawable.checkbox_on_background)
                .setCancelButtonIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setConfirmClickListener("Accept", dialog -> dialog.dismissWithAnimation())
                .setCancelClickListener("Decline", dialog -> dialog.dismissWithAnimation())
                .show();
    }

    private void showProgressShortcutDialog() {
        progressStep = -1;

        final KAlertDialog progressDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE, true)
                .setTitleText("Processing")
                .setContentText("Please wait while we prepare your request.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .setProgressColor(Color.parseColor("#10B981"))
                .setProgressSpinSpeed(1.1f);

        progressDialog.setCancelable(false);
        progressDialog.show();

        new CountDownTimer(800L * 7, 800L) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressStep++;

                switch (progressStep) {
                    case 0:
                        progressDialog.setProgressColor(ContextCompat.getColor(SampleActivity.this, R.color.blue_btn_bg_color));
                        break;

                    case 1:
                        progressDialog.setProgressColor(ContextCompat.getColor(SampleActivity.this, R.color.material_deep_teal_50));
                        break;

                    case 2:
                        progressDialog.setProgressColor(ContextCompat.getColor(SampleActivity.this, R.color.success_stroke_color));
                        break;

                    case 3:
                        progressDialog.setProgressColor(ContextCompat.getColor(SampleActivity.this, R.color.material_deep_teal_20));
                        break;

                    case 4:
                        progressDialog.setProgressColor(ContextCompat.getColor(SampleActivity.this, R.color.material_blue_grey_80));
                        break;

                    case 5:
                        progressDialog.setProgressColor(ContextCompat.getColor(SampleActivity.this, R.color.warning_stroke_color));
                        break;

                    default:
                        progressDialog.setProgressColor(Color.parseColor("#10B981"));
                        break;
                }
            }

            @Override
            public void onFinish() {
                progressStep = -1;

                progressDialog
                        .setTitleText("Completed")
                        .setContentText("Your request has been completed successfully.")
                        .setConfirmClickListener("Done", null)
                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
            }
        }.start();
    }

    private void showCallbacksDialog() {
        new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, true)
                .setTitleText("Callbacks")
                .setContentText("This dialog shows onDialogShow(), onDialogDismiss(), onConfirm() and onCancel() examples.")
                .applyStyle(KAlertDialog.STYLE_MODERN)
                .showCancelButton(true)
                .onDialogShow(dialog ->
                        Toast.makeText(this, "Dialog shown", Toast.LENGTH_SHORT).show())
                .onDialogDismiss(dialog ->
                        Toast.makeText(this, "Dialog dismissed", Toast.LENGTH_SHORT).show())
                .onConfirm(dialog -> {
                    Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT).show();
                    dialog.dismissWithAnimation();
                })
                .onCancel(dialog -> {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    dialog.dismissWithAnimation();
                })
                .setConfirmClickListener("Confirm", null)
                .setCancelClickListener("Cancel", null)
                .show();
    }
}