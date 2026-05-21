package com.developer.kalert;

import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.developer.progressx.ProgressWheel;
import com.google.android.material.textfield.TextInputEditText;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

public class KAlertDialog extends AlertDialog implements View.OnClickListener {

    private final Context context;

    private final AnimationSet mModalInAnim, mModalOutAnim, mErrorXInAnim;
    private final Animation mOverlayOutAnim, mImageAnim;

    private TextView mTitleTextView, mContentTextView;
    private WebView justifyContentTextView;
    private ImageView mErrorX, mSuccessTick, mCustomImage, mCustomBigImage;
    private Drawable mCustomImgDrawable;
    private AppCompatButton mConfirmButton, mCancelButton;
    private Drawable mColor, mCancelColor;
    private Drawable mConfirmButtonDrawable, mCancelButtonDrawable;
    private View mDialogView;
    private FrameLayout mCustomViewContainer;
    //private ViewTreeObserver mCancelButtonObserver, mConfirmButtonObserver;
    //private ViewTreeObserver.OnGlobalLayoutListener mConfirmButtonGlobalListener, mCancelButtonGlobalListener;

    private String mTitleText, mContentText, justifyContentText, justifyContentTextColor, justifyContentTextSize,
            justifyContentTextFont, justifyContentTextFontExtension, mCancelText, mConfirmText, mInputFieldHint, mInputFieldText;
    private String imageURL;
    private String titleFontAssets, contentFontAssets, confirmButtonFontAssets, cancelButtonFontAssets;
    private int displayType;
    private int titleFont = 0, contentFont = 0, confirmButtonFont = 0, cancelButtonFont = 0;
    private int titleFontWeight = Typeface.NORMAL;
    private int contentFontWeight = Typeface.NORMAL;
    private int titleColor = 0, contentColor = 0,
            confirmTextColor = android.R.color.white, cancelTextColor = android.R.color.white;
    private int drawableColor = 0;
    private Integer contentAlignment, contentGravity;
    private int titleTextLayoutGravity = Gravity.CENTER;
    private int titleTextGravity = Gravity.CENTER;
    private int titleTextAlignment = TEXT_ALIGNMENT_CENTER;
    private Integer cancelButtonHeight, confirmButtonHeight;

    private boolean mShowCancel, mShowContent, mShowTitleText, mCloseFromCancel, mShowConfirm;
    private int contentTextSize = 0;
    private int titleTextSize = 0;

    private FrameLayout mErrorFrame, mSuccessFrame, mProgressFrame, mWarningFrame;

    private final ProgressHelper mProgressHelper;
    private ProgressWheel imageLoading;

    private KAlertDialog.KAlertClickListener mCancelClickListener;
    private KAlertDialog.KAlertClickListener mConfirmClickListener;

    private InputValidator inputValidator;
    private InputConfirmListener inputConfirmListener;
    private DialogShowListener dialogShowListener;
    private DialogDismissListener dialogDismissListener;

    private String inputErrorMessage;

    private View customView;
    private int customViewLayoutRes = 0;

    private Integer dialogBackgroundColor = null;
    private Float dialogCornerRadiusDp = null;
    private Float dialogElevationDp = null;
    private Float dialogDimAmount = null;

    private int confirmButtonTextSize = 0;
    private int cancelButtonTextSize = 0;
    private int confirmButtonFontWeight = Typeface.NORMAL;
    private int cancelButtonFontWeight = Typeface.NORMAL;
    private boolean confirmButtonAllCaps = false;
    private boolean cancelButtonAllCaps = false;

    private int confirmButtonIconRes = 0;
    private int cancelButtonIconRes = 0;

    private int urlImagePlaceholderRes = 0;
    private int urlImageErrorRes = 0;

    private int mAlertType;
    public static final int NORMAL_TYPE = 0;

    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int URL_IMAGE_TYPE = 5;
    public static final int PROGRESS_TYPE = 6;

    public static final int IMAGE_BIG = 8;
    public static final int IMAGE_CIRCLE = 9;

    public static boolean DARK_STYLE = false;

    public static final int INPUT_TYPE = 7;

    public static final int CUSTOM_VIEW_TYPE = 10;
    public static final int INFO_TYPE = 11;
    public static final int QUESTION_TYPE = 12;

    public static final int STYLE_CLASSIC = 0;
    public static final int STYLE_MODERN = 1;
    public static final int STYLE_MINIMAL = 2;
    public static final int STYLE_ROUNDED = 3;

    private TextInputEditText mEditText;

    public interface KAlertClickListener {
        void onClick(KAlertDialog kAlertDialog);
    }

    public interface InputValidator {
        boolean isValid(String input);
    }

    public interface InputConfirmListener {
        void onConfirm(KAlertDialog kAlertDialog, String input);
    }

    public interface DialogShowListener {
        void onShow(KAlertDialog kAlertDialog);
    }

    public interface DialogDismissListener {
        void onDismiss(KAlertDialog kAlertDialog);
    }

    public KAlertDialog(Context context) {
        this(context, NORMAL_TYPE, false);
    }

    public KAlertDialog(Context context, boolean autoNightMode) {
        this(context, NORMAL_TYPE, autoNightMode);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        mDialogView = Objects.requireNonNull(getWindow()).getDecorView().findViewById(android.R.id.content);
        mTitleTextView = findViewById(R.id.title_text);
        mContentTextView = findViewById(R.id.content_text);
        justifyContentTextView = findViewById(R.id.content_text2);
        mErrorFrame = findViewById(R.id.error_frame);
        assert mErrorFrame != null;
        mErrorX = mErrorFrame.findViewById(R.id.error_x);
        mEditText = findViewById(R.id.edit_text);
        mSuccessFrame = findViewById(R.id.success_frame);
        mProgressFrame = findViewById(R.id.progress_dialog);
        mSuccessTick = mSuccessFrame.findViewById(R.id.success_x);
        mCustomImage = findViewById(R.id.custom_image);
        mCustomBigImage = findViewById(R.id.custom_big_image);
        mWarningFrame = findViewById(R.id.warning_frame);
        mCustomViewContainer = findViewById(R.id.custom_view_container);
        mProgressHelper.setProgressWheel(findViewById(R.id.progressWheel));
        imageLoading = findViewById(R.id.image_loading);

        mConfirmButton = findViewById(R.id.custom_confirm_button);
        mCancelButton = findViewById(R.id.cancel_button);
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        applyDialogWindowDefaults();

        setTitleText(mTitleText);
        setTitleTextGravity(titleTextLayoutGravity);
        setTitleTextLayoutGravity(titleTextLayoutGravity);
        setDialogTextFont(mTitleTextView, titleFont, titleFontAssets);
        setDialogTextFont(mContentTextView, contentFont, contentFontAssets);
        setDialogTextFont(mConfirmButton, confirmButtonFont, confirmButtonFontAssets);
        setDialogTextFont(mCancelButton, cancelButtonFont, cancelButtonFontAssets);
        setContentText(mContentText);
        justifyContentText(justifyContentText, justifyContentTextColor, justifyContentTextSize, justifyContentTextFont, justifyContentTextFontExtension);
        setCancelText(mCancelText);
        setCancelText(mCancelText, cancelTextColor);
        setConfirmText(mConfirmText);
        setConfirmText(mConfirmText, confirmTextColor);
        setConfirmButtonColor(mColor);
        setCancelButtonColor(mCancelColor);
        setConfirmButtonDrawable(mConfirmButtonDrawable);
        setCancelButtonDrawable(mCancelButtonDrawable);
        //setButtonMatchingHeight();
        changeAlertType(mAlertType, true);
        setInputFieldHint(mInputFieldHint);
        setInputFieldText(mInputFieldText);

        applyDialogAppearance();
        applyButtonAppearance();
        applyCustomViewIfNeeded();
    }

    public KAlertDialog(Context context, int alertType, boolean autoNightMode) {
        super(context, isNightMode(context, autoNightMode) ? R.style.alert_dialog_dark : R.style.alert_dialog_light);
        //super(context, DARK_STYLE ? R.style.alert_dialog_dark : R.style.alert_dialog_light);
        this.context = context;

        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mImageAnim = AnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        mModalInAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        Objects.requireNonNull(mModalOutAnim).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(GONE);
                mDialogView.post(() -> {
                    if (mCloseFromCancel) {
                        KAlertDialog.super.cancel();
                    } else {
                        KAlertDialog.super.dismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = Objects.requireNonNull(getWindow()).getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    private void applyDialogWindowDefaults() {
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void applyDialogAppearance() {
        if (mDialogView == null) {
            return;
        }

        if (dialogBackgroundColor != null || dialogCornerRadiusDp != null) {
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);

            if (dialogBackgroundColor != null) {
                background.setColor(dialogBackgroundColor);
            } else {
                background.setColor(Color.TRANSPARENT);
            }

            if (dialogCornerRadiusDp != null) {
                background.setCornerRadius(dpToPx(dialogCornerRadiusDp));
            }

            mDialogView.setBackground(background);
        }

        if (dialogElevationDp != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDialogView.setElevation(dpToPx(dialogElevationDp));
        }

        if (dialogDimAmount != null && getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.dimAmount = dialogDimAmount;
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    private void applyButtonAppearance() {
        if (mConfirmButton != null) {
            if (confirmButtonTextSize != 0) {
                mConfirmButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, confirmButtonTextSize);
            }

            mConfirmButton.setTypeface(mConfirmButton.getTypeface(), confirmButtonFontWeight);
            mConfirmButton.setAllCaps(confirmButtonAllCaps);

            if (confirmButtonIconRes != 0) {
                mConfirmButton.setCompoundDrawablesRelativeWithIntrinsicBounds(confirmButtonIconRes, 0, 0, 0);
                mConfirmButton.setCompoundDrawablePadding((int) dpToPx(8));
            }
        }

        if (mCancelButton != null) {
            if (cancelButtonTextSize != 0) {
                mCancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, cancelButtonTextSize);
            }

            mCancelButton.setTypeface(mCancelButton.getTypeface(), cancelButtonFontWeight);
            mCancelButton.setAllCaps(cancelButtonAllCaps);

            if (cancelButtonIconRes != 0) {
                mCancelButton.setCompoundDrawablesRelativeWithIntrinsicBounds(cancelButtonIconRes, 0, 0, 0);
                mCancelButton.setCompoundDrawablePadding((int) dpToPx(8));
            }
        }
    }

    private void applyCustomViewIfNeeded() {
        if (mCustomViewContainer == null) {
            return;
        }

        if (customView != null) {
            mCustomViewContainer.removeAllViews();
            mCustomViewContainer.addView(customView);
        } else if (customViewLayoutRes != 0) {
            View inflatedView = LayoutInflater.from(context).inflate(customViewLayoutRes, mCustomViewContainer, false);
            mCustomViewContainer.removeAllViews();
            mCustomViewContainer.addView(inflatedView);
            customView = inflatedView;
        }
    }

    private boolean validateInputIfNeeded() {
        if (mAlertType != INPUT_TYPE) {
            return true;
        }

        if (inputValidator == null) {
            return true;
        }

        String input = getInputText();
        boolean isValid = inputValidator.isValid(input);

        if (!isValid && mEditText != null) {
            if (inputErrorMessage != null) {
                mEditText.setError(inputErrorMessage);
            } else {
                mEditText.setError("Invalid input");
            }
        }

        return isValid;
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getContext().getResources().getDisplayMetrics()
        );
    }

    private void restore() {
        mCustomImage.setVisibility(GONE);
        mCustomBigImage.setVisibility(GONE);
        imageLoading.setVisibility(GONE);
        mErrorFrame.setVisibility(GONE);
        mSuccessFrame.setVisibility(GONE);
        mWarningFrame.setVisibility(GONE);
        mProgressFrame.setVisibility(GONE);

        if (mCustomViewContainer != null) {
            mCustomViewContainer.setVisibility(GONE);
        }

        mConfirmButton.setVisibility(View.VISIBLE);

        mConfirmButton.setBackgroundResource(R.drawable.button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessTick.clearAnimation();
    }

    private void playAnimation() {
        if (mAlertType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mImageAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startAnimation(mImageAnim);
            mSuccessFrame.startAnimation(mImageAnim);
        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        if (mDialogView != null) {
            if (!fromCreate) {
                restore();
            }
            switch (mAlertType) {
                case NORMAL_TYPE:
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case WARNING_TYPE:
                    mWarningFrame.setVisibility(View.VISIBLE);
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage1(mCustomImgDrawable);
                    setCustomImageColorFilter(drawableColor);
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case URL_IMAGE_TYPE:
                    setURLImage1(imageURL, displayType);
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(GONE);
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case INPUT_TYPE:
                    showInputView();
//                    setConfirmButtonColor(mColor);
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
                case CUSTOM_VIEW_TYPE:
                    showCustomView();
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;

                case INFO_TYPE:
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;

                case QUESTION_TYPE:
                    setConfirmButtonDrawable(mConfirmButtonDrawable);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);

        if (alertType != INPUT_TYPE && alertType != CUSTOM_VIEW_TYPE) {
            hideInputView();
            hideKeyboard();
        }
    }

    public KAlertDialog setTitleText(String text) {

        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            showTitleText(true);
            if (titleTextSize != 0) {
                mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(titleTextSize, getContext()));
            }
            if (titleColor != 0) {
                mTitleTextView.setTextColor(ContextCompat.getColor(context, titleColor));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTitleTextView.setText(Html.fromHtml(mTitleText, 1));
            } else {
                mTitleTextView.setText(Html.fromHtml(mTitleText));
            }
            mTitleTextView.setTypeface(mTitleTextView.getTypeface(), titleFontWeight);
        } else {
            showTitleText(false);
        }
        return this;
    }

    public KAlertDialog setTitleTextLayoutGravity(int gravity) {
        titleTextLayoutGravity = gravity;
        if (mTitleTextView != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = gravity;

            mTitleTextView.setLayoutParams(params);
        }

        return this;
    }

    public KAlertDialog setTitleTextGravity(int gravity) {
        titleTextGravity = gravity;
        if (mTitleTextView != null) {
            mTitleTextView.setGravity(gravity);
        }

        return this;
    }

    private void showTitleText(boolean isShow) {
        mShowTitleText = true;
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(isShow ? View.VISIBLE : GONE);
            mTitleTextView.setAutoLinkMask(Linkify.ALL);
        }
    }

    public KAlertDialog setCustomImage(int resourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //ResourcesCompat.getDrawable( context.getResources(), resourceId, context.getTheme() );
            return setCustomImage1(getContext().getResources().getDrawable(resourceId, context.getTheme()));
        } else {
            return setCustomImage1(getContext().getResources().getDrawable(resourceId));
        }
    }

    public KAlertDialog setDrawableTintOnNightMode(boolean isTinted, int tintColor) {
        if (isTinted && isNightMode(context, true)) {
            setCustomImageColorFilter(tintColor);
        }
        return this;
    }

    public KAlertDialog setURLImage(String imageURL, int displayType) {
        return setURLImage1(imageURL, displayType);
    }

    private KAlertDialog setCustomImage1(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    private KAlertDialog setCustomImageColorFilter(int color) {
        drawableColor = color;
        if (mCustomImage != null && drawableColor != 0) {
            mCustomImage.setColorFilter(ContextCompat.getColor(context, drawableColor),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
        return this;
    }

    private KAlertDialog setURLImage1(String imageURL, int displayType) {
        this.imageURL = imageURL;
        this.displayType = displayType;

        if (mCustomImage == null || mCustomBigImage == null || imageLoading == null) {
            return this;
        }

        if (imageURL == null || imageURL.trim().isEmpty()) {
            imageLoading.setVisibility(GONE);
            mCustomImage.setVisibility(GONE);
            mCustomBigImage.setVisibility(GONE);
            return this;
        }

        imageLoading.setVisibility(View.VISIBLE);

        RequestOptions requestOptions = new RequestOptions();

        if (urlImagePlaceholderRes != 0) {
            requestOptions = requestOptions.placeholder(urlImagePlaceholderRes);
        }

        if (urlImageErrorRes != 0) {
            requestOptions = requestOptions.error(urlImageErrorRes);
        }

        switch (displayType) {
            case IMAGE_BIG:
                mCustomImage.setVisibility(GONE);
                mCustomBigImage.setVisibility(View.VISIBLE);

                Glide.with(mCustomBigImage)
                        .load(imageURL)
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                imageLoading.setVisibility(GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                imageLoading.setVisibility(GONE);
                                return false;
                            }
                        })
                        .into(mCustomBigImage);
                break;

            case IMAGE_CIRCLE:
                mCustomBigImage.setVisibility(GONE);
                mCustomImage.setVisibility(View.VISIBLE);

                Glide.with(mCustomImage)
                        .load(imageURL)
                        .apply(requestOptions.circleCrop())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                imageLoading.setVisibility(GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                imageLoading.setVisibility(GONE);
                                return false;
                            }
                        })
                        .into(mCustomImage);
                break;

            default:
                imageLoading.setVisibility(GONE);
                break;
        }

        return this;
    }

    public KAlertDialog setContentText(String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            if (contentTextSize != 0) {
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(contentTextSize, getContext()));
            }
            if (contentColor != 0) {
                mContentTextView.setTextColor(ContextCompat.getColor(context, contentColor));
            }
            if (contentAlignment != null && contentGravity != null) {
                mContentTextView.setTextAlignment(contentAlignment);
                mContentTextView.setGravity(contentGravity);
            } else {
                mContentTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mContentTextView.setGravity(Gravity.CENTER);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mContentTextView.setText(Html.fromHtml(mContentText, 0));
            } else {
                mContentTextView.setText(Html.fromHtml(mContentText));
            }
            mContentTextView.setTypeface(mContentTextView.getTypeface(), contentFontWeight);
        } else {
            showContentText(false);
        }
        return this;
    }

    /**
     * Custom font must be placed in assets/fonts folder.
     * <p>
     * Set the font params to 'null' or an empty string to use the default font.
     *
     * @param content       dialog content text
     * @param textColor     text color as used in html css: "red", "grey", "white", "black" etc
     * @param fontSize      font size in px: "16px", "18px", "20px" etc
     * @param fontName      custom font name: "my_custom_font_name"
     * @param fontExtension font extension: ".ttf" or ".otf"
     */
    public KAlertDialog justifyContentText(String content, String textColor, String fontSize, String fontName, String fontExtension) {
        justifyContentText = content;
        justifyContentTextColor = textColor;
        justifyContentTextSize = fontSize;
        justifyContentTextFont = fontName;
        justifyContentTextFontExtension = fontExtension;
        if (justifyContentTextView != null && justifyContentText != null &&
                justifyContentTextColor != null && justifyContentTextSize != null) {
            justifyContentTextView.setBackgroundColor(Color.TRANSPARENT);
            showJustifyText(true);

            String text;
            text = "<html><style type='text/css'>@font-face{";
            text += "font-family: ";
            text += fontName;
            text += ";";
            text += "src: url('fonts/";
            text += fontName;
            text += fontExtension;
            text += "');} </style>";
            text += "<body ><p ";
            text += "style=\"color:";
            text += textColor;
            text += ";";
            text += "font-size:";
            text += justifyContentTextSize;
            text += ";";
            text += "font-family:";
            text += fontName;
            text += "\"";
            text += "align=\"justify\">";
            text += justifyContentText;
            text += "</p></body></html>";

            justifyContentTextView.loadDataWithBaseURL("file:///android_asset/", text, "text/html", "utf-8", null);
        } else {
            showJustifyText(false);
        }
        return this;
    }

    public KAlertDialog showCancelButton(boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : GONE);
        }
        return this;
    }

    public KAlertDialog showConfirmButton(boolean isShow) {
        mShowConfirm = isShow;
        if (mConfirmButton != null) {
            mConfirmButton.setVisibility(mShowConfirm ? View.VISIBLE : GONE);
        }
        return this;
    }

    /*
    Method to set the matching height of both confirm and cancel button. It helps
    when device font sized is increased or the text of any one button has greater length
    the specific button heights also get increased and this leads to un-usual height of both
    buttons. So this method set's the height of both button remain same.

    private void setButtonMatchingHeight() {
        if (mConfirmButton != null && mCancelButton != null) {
            mConfirmButtonObserver = mConfirmButton.getViewTreeObserver();
            mCancelButtonObserver = mCancelButton.getViewTreeObserver();

            if (mConfirmButton.getVisibility() == View.VISIBLE) {
                mConfirmButtonGlobalListener = () -> {
                    confirmButtonHeight = mConfirmButton.getHeight();
                    setMatchingHeight();
                };
                mConfirmButtonObserver.addOnGlobalLayoutListener(mConfirmButtonGlobalListener);
            }

            if (mCancelButton.getVisibility() == View.VISIBLE) {
                mCancelButtonGlobalListener = () -> {
                    cancelButtonHeight = mCancelButton.getHeight();
                    setMatchingHeight();
                };
                mCancelButtonObserver.addOnGlobalLayoutListener(mCancelButtonGlobalListener);
            }
        }
    }

    private void setMatchingHeight() {
        if ( confirmButtonHeight != null && cancelButtonHeight != null ) {
            //check which button value is greater
            if (confirmButtonHeight.equals(cancelButtonHeight)) { }
            else if ( confirmButtonHeight > cancelButtonHeight ) {
                mCancelButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, confirmButtonHeight));
            } else {
                mConfirmButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, cancelButtonHeight));
            }
        }
    }
    */

    private void setDialogTextFont(TextView contentText, Integer font, String path) {
        if (context != null) {
            if (contentText != null) {
                if (path != null) {
                    setTypefaceAssets(contentText, path);
                } else if (font != 0) {
                    setTypeface(contentText, font);
                }
            }
        }
    }

    private void setTypeface(TextView contentText, int font) {
        Typeface typeface = ResourcesCompat.getFont(context, font);
        contentText.setTypeface(typeface);
    }

    private void setTypefaceAssets(TextView contentText, String path) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
        contentText.setTypeface(typeface);
    }

    public KAlertDialog setTitleFont(int font) {
        this.titleFont = font;
        return this;
    }

    public KAlertDialog setContentFont(int font) {
        this.contentFont = font;
        return this;
    }

    public KAlertDialog setTitleFontWeight(int fontWeight) {
        this.titleFontWeight = fontWeight;
        if (mTitleTextView != null) {
            mTitleTextView.setTypeface(mTitleTextView.getTypeface(), fontWeight);
        }
        return this;
    }

    public KAlertDialog setContentFontWeight(int fontWeight) {
        this.contentFontWeight = fontWeight;
        if (mContentTextView != null) {
            mContentTextView.setTypeface(mContentTextView.getTypeface(), fontWeight);
        }
        return this;
    }

    public KAlertDialog setTitleFontAssets(String path) {
        this.titleFontAssets = path;
        return this;
    }

    public KAlertDialog setContentFontAssets(String path) {
        this.contentFontAssets = path;
        return this;
    }

    public KAlertDialog setConfirmButtonFont(int font) {
        this.confirmButtonFont = font;
        return this;
    }

    public KAlertDialog setCancelButtonFont(int font) {
        this.cancelButtonFont = font;
        return this;
    }

    public KAlertDialog setConfirmButtonFontAssets(String path) {
        this.confirmButtonFontAssets = path;
        return this;
    }

    public KAlertDialog setCancelButtonFontAssets(String path) {
        this.cancelButtonFontAssets = path;
        return this;
    }

    private void showContentText(boolean isShow) {
        mShowContent = true;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(isShow ? View.VISIBLE : GONE);
            mContentTextView.setAutoLinkMask(Linkify.ALL);
        }
    }

    private void showJustifyText(boolean isShow) {
        if (justifyContentTextView != null) {
            justifyContentTextView.setVisibility(isShow ? View.VISIBLE : GONE);
        }
    }

    @Deprecated
    public KAlertDialog setCancelClickListener(KAlertClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public KAlertDialog setCancelClickListener(String text, KAlertClickListener listener) {
        //noinspection deprecation
        setCancelText(text);
        mCancelClickListener = listener;
        return this;
    }

    public KAlertDialog setCancelClickListener(String text, int color, KAlertClickListener listener) {
        //noinspection deprecation
        setCancelText(text, color);
        mCancelClickListener = listener;
        return this;
    }

    @Deprecated
    public KAlertDialog setConfirmClickListener(KAlertClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public KAlertDialog setConfirmClickListener(String text, KAlertClickListener listener) {
        //noinspection deprecation
        setConfirmText(text);
        mConfirmClickListener = listener;
        return this;
    }

    public KAlertDialog setConfirmClickListener(String text, int color, KAlertClickListener listener) {
        //noinspection deprecation
        setConfirmText(text, color);
        mConfirmClickListener = listener;
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mDialogView != null && mModalInAnim != null) {
            mDialogView.startAnimation(mModalInAnim);
        }

        playAnimation();

        if (dialogShowListener != null) {
            dialogShowListener.onShow(this);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        if (dialogDismissListener != null) {
            dialogDismissListener.onDismiss(this);
        }
    }

    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    private KAlertDialog setConfirmButtonColor(Drawable background) {
        mColor = background;
        if (mConfirmButton != null && mColor != null) {
            mConfirmButton.setBackground(mColor);
        }
        return this;
    }

    private KAlertDialog setConfirmButtonColor(@ColorInt int tintColor) {
        if (mConfirmButton != null) {
            DrawableCompat.setTint(mConfirmButton.getBackground(), tintColor);
        }
        return this;
    }

    private KAlertDialog setCancelButtonColor(@ColorInt int tintColor) {
        if (mCancelButton != null) {
            DrawableCompat.setTint(mCancelButton.getBackground(), tintColor);
        }
        return this;
    }

    private KAlertDialog setCancelButtonColor(Drawable background) {
        mCancelColor = background;
        if (mCancelButton != null && mCancelColor != null) {
            mCancelButton.setBackground(mCancelColor);
        }
        return this;
    }

    private KAlertDialog setConfirmButtonDrawable(Drawable background) {
        mConfirmButtonDrawable = background;
        if (mConfirmButton != null && mConfirmButtonDrawable != null) {
            mConfirmButton.setBackground(mConfirmButtonDrawable);
        }
        return this;
    }

    private KAlertDialog setCancelButtonDrawable(Drawable background) {
        mCancelButtonDrawable = background;
        if (mCancelButton != null && mCancelButtonDrawable != null) {
            mCancelButton.setBackground(mCancelButtonDrawable);
        }
        return this;
    }

    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    public void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;

        if (mConfirmButton != null && mOverlayOutAnim != null) {
            mConfirmButton.startAnimation(mOverlayOutAnim);
        }

        if (mDialogView != null && mModalOutAnim != null) {
            mDialogView.startAnimation(mModalOutAnim);
        } else {
            if (fromCancel) {
                KAlertDialog.super.cancel();
            } else {
                KAlertDialog.super.dismiss();
            }
        }
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public int getAlertType() {
        return mAlertType;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public boolean isShowTitleText() {
        return mShowTitleText;
    }

    public String getContentText() {
        return mContentText;
    }

    public boolean isShowCancelButton() {
        return mShowCancel;
    }

    public boolean isShowConfirmButton() {
        return mShowConfirm;
    }

    public boolean isShowContentText() {
        return mShowContent;
    }

    public String getCancelText() {
        return mCancelText;
    }

    @Deprecated
    public KAlertDialog setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
            mCancelButton.setTextColor(ContextCompat.getColor(context, cancelTextColor));
        }
        return this;
    }

    @Deprecated
    public KAlertDialog setCancelText(String text, int color) {
        mCancelText = text;
        cancelTextColor = color;
        if (mCancelButton != null && mCancelText != null && cancelTextColor != 0) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
            mCancelButton.setTextColor(ContextCompat.getColor(context, cancelTextColor));
        }
        return this;
    }

    public String getConfirmText() {
        return mConfirmText;
    }

    @Deprecated
    public KAlertDialog setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            showConfirmButton(true);
            mConfirmButton.setText(mConfirmText);
            mConfirmButton.setTextColor(ContextCompat.getColor(context, confirmTextColor));
        }
        return this;
    }

    @Deprecated
    public KAlertDialog setConfirmText(String text, int color) {
        mConfirmText = text;
        confirmTextColor = color;
        if (mConfirmButton != null && mConfirmText != null && confirmTextColor != 0) {
            showConfirmButton(true);
            mConfirmButton.setText(mConfirmText);
            mConfirmButton.setTextColor(ContextCompat.getColor(context, confirmTextColor));
        }
        return this;
    }


    public KAlertDialog confirmButtonColor(int color) {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        return setConfirmButtonColor(ContextCompat.getDrawable(context, color));
        //}else {
        //return setConfirmButtonColor(ContextCompat.getDrawable(context, color));
        //}
    }

    public KAlertDialog confirmButtonDrawable(int drawableResId) {
        return setConfirmButtonDrawable(ContextCompat.getDrawable(context, drawableResId));
    }

    public KAlertDialog cancelButtonColor(int color) {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        return setCancelButtonColor(ContextCompat.getDrawable(context, color));
        //}else {
        //return setCancelButtonColor(getContext().getResources().getDrawable(color));
        //}
    }

    public KAlertDialog cancelButtonDrawable(int drawableResId) {
        return setCancelButtonDrawable(ContextCompat.getDrawable(context, drawableResId));
    }

    public KAlertDialog setTitleColor(int color) {
        this.titleColor = color;
        return this;
    }

    public KAlertDialog setContentColor(int color) {
        this.contentColor = color;
        return this;
    }

    public KAlertDialog setContentTextAlignment(int contentAlignment, int contentGravity) {
        this.contentAlignment = contentAlignment;
        this.contentGravity = contentGravity;
        return this;
    }

    public KAlertDialog setTitleTextSize(int value) {
        this.titleTextSize = value;
        return this;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public KAlertDialog setContentTextSize(int value) {
        this.contentTextSize = value;
        return this;
    }

    public int getContentTextSize() {
        return contentTextSize;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(KAlertDialog.this);
            } else {
                dismissWithAnimation();
            }

        } else if (v.getId() == R.id.custom_confirm_button) {

            if (!validateInputIfNeeded()) {
                return;
            }

            if (mAlertType == INPUT_TYPE && inputConfirmListener != null) {
                inputConfirmListener.onConfirm(KAlertDialog.this, getInputText());
                return;
            }

            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(KAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    public ProgressHelper getProgressHelper() {
        return mProgressHelper;
    }

    private void showCustomView() {
        if (mCustomViewContainer != null) {
            applyCustomViewIfNeeded();
            mCustomViewContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        }
    }

    private void showInputView() {
        if (mCustomViewContainer != null) {
            mCustomViewContainer.setVisibility(View.VISIBLE);
            showKeyboard();
        }
    }

    private void hideInputView() {
        if (mCustomViewContainer != null) {
            mCustomViewContainer.setVisibility(View.GONE);
        }
    }

    public KAlertDialog setInputFieldHint(String text) {
        mInputFieldHint = text;
        if (mEditText != null && mInputFieldHint != null) {
            mEditText.setHint(mInputFieldHint);
        }
        return this;
    }

    public KAlertDialog setInputFieldText(String text) {
        mInputFieldText = text;
        if (mEditText != null && mInputFieldText != null) {
            mEditText.setText(mInputFieldText);
        }
        return this;
    }

    public String getInputText() {
        if (mEditText == null || mEditText.getText() == null) {
            return "";
        }
        return mEditText.getText().toString();
    }

    private void showKeyboard() {
        if (mEditText == null) {
            return;
        }

        final InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm == null) {
            return;
        }

        if (!mEditText.hasFocus()) {
            mEditText.requestFocus();
        }

        mEditText.post(() -> imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT));
    }

    private void hideKeyboard() {
        if (mEditText != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        }
    }

    public static boolean isNightMode(Context context, boolean autoNightMode) {
        if (autoNightMode) {
            int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
        } else {
            return false;
        }
    }

    public KAlertDialog setCustomView(View view) {
        this.customView = view;
        this.customViewLayoutRes = 0;

        if (mCustomViewContainer != null && customView != null) {
            mCustomViewContainer.removeAllViews();
            mCustomViewContainer.addView(customView);
        }

        return this;
    }

    public KAlertDialog setCustomView(int layoutResId) {
        this.customViewLayoutRes = layoutResId;
        this.customView = null;

        if (mCustomViewContainer != null && customViewLayoutRes != 0) {
            View inflatedView = LayoutInflater.from(context).inflate(customViewLayoutRes, mCustomViewContainer, false);
            mCustomViewContainer.removeAllViews();
            mCustomViewContainer.addView(inflatedView);
            customView = inflatedView;
        }

        return this;
    }

    public View getCustomView() {
        return customView;
    }

    public KAlertDialog setDialogBackgroundColor(@ColorInt int color) {
        this.dialogBackgroundColor = color;
        applyDialogAppearance();
        return this;
    }

    public KAlertDialog setDialogBackgroundColorResource(int colorRes) {
        this.dialogBackgroundColor = ContextCompat.getColor(context, colorRes);
        applyDialogAppearance();
        return this;
    }

    public KAlertDialog setDialogCornerRadius(float radiusDp) {
        this.dialogCornerRadiusDp = radiusDp;
        applyDialogAppearance();
        return this;
    }

    public KAlertDialog setDialogElevation(float elevationDp) {
        this.dialogElevationDp = elevationDp;
        applyDialogAppearance();
        return this;
    }

    public KAlertDialog setDimAmount(float amount) {
        this.dialogDimAmount = amount;
        applyDialogAppearance();
        return this;
    }

    public KAlertDialog setInputType(int inputType) {
        if (mEditText != null) {
            mEditText.setInputType(inputType);
        }
        return this;
    }

    public KAlertDialog setInputMaxLength(int maxLength) {
        if (mEditText != null && maxLength > 0) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        return this;
    }

    public KAlertDialog setInputError(String errorMessage) {
        this.inputErrorMessage = errorMessage;

        if (mEditText != null && errorMessage != null) {
            mEditText.setError(errorMessage);
        }

        return this;
    }

    public KAlertDialog setInputValidator(InputValidator validator) {
        this.inputValidator = validator;
        return this;
    }

    public KAlertDialog setOnInputConfirmListener(InputConfirmListener listener) {
        this.inputConfirmListener = listener;
        return this;
    }

    public KAlertDialog onConfirm(KAlertClickListener listener) {
        this.mConfirmClickListener = listener;
        return this;
    }

    public KAlertDialog onCancel(KAlertClickListener listener) {
        this.mCancelClickListener = listener;
        return this;
    }

    public KAlertDialog onDialogShow(DialogShowListener listener) {
        this.dialogShowListener = listener;
        return this;
    }

    public KAlertDialog onDialogDismiss(DialogDismissListener listener) {
        this.dialogDismissListener = listener;
        return this;
    }

    public KAlertDialog setConfirmButtonTextSize(int sizeSp) {
        this.confirmButtonTextSize = sizeSp;

        if (mConfirmButton != null) {
            mConfirmButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
        }

        return this;
    }

    public KAlertDialog setCancelButtonTextSize(int sizeSp) {
        this.cancelButtonTextSize = sizeSp;

        if (mCancelButton != null) {
            mCancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
        }

        return this;
    }

    public KAlertDialog setConfirmButtonFontWeight(int fontWeight) {
        this.confirmButtonFontWeight = fontWeight;

        if (mConfirmButton != null) {
            mConfirmButton.setTypeface(mConfirmButton.getTypeface(), fontWeight);
        }

        return this;
    }

    public KAlertDialog setCancelButtonFontWeight(int fontWeight) {
        this.cancelButtonFontWeight = fontWeight;

        if (mCancelButton != null) {
            mCancelButton.setTypeface(mCancelButton.getTypeface(), fontWeight);
        }

        return this;
    }

    public KAlertDialog setConfirmButtonAllCaps(boolean allCaps) {
        this.confirmButtonAllCaps = allCaps;

        if (mConfirmButton != null) {
            mConfirmButton.setAllCaps(allCaps);
        }

        return this;
    }

    public KAlertDialog setCancelButtonAllCaps(boolean allCaps) {
        this.cancelButtonAllCaps = allCaps;

        if (mCancelButton != null) {
            mCancelButton.setAllCaps(allCaps);
        }

        return this;
    }

    public KAlertDialog setConfirmButtonIcon(int drawableResId) {
        this.confirmButtonIconRes = drawableResId;

        if (mConfirmButton != null && drawableResId != 0) {
            mConfirmButton.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableResId, 0, 0, 0);
            mConfirmButton.setCompoundDrawablePadding((int) dpToPx(8));
        }

        return this;
    }

    public KAlertDialog setCancelButtonIcon(int drawableResId) {
        this.cancelButtonIconRes = drawableResId;

        if (mCancelButton != null && drawableResId != 0) {
            mCancelButton.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableResId, 0, 0, 0);
            mCancelButton.setCompoundDrawablePadding((int) dpToPx(8));
        }

        return this;
    }

    public KAlertDialog setURLImagePlaceholder(int drawableResId) {
        this.urlImagePlaceholderRes = drawableResId;
        return this;
    }

    public KAlertDialog setURLImageError(int drawableResId) {
        this.urlImageErrorRes = drawableResId;
        return this;
    }

    public KAlertDialog setProgressColor(@ColorInt int color) {
        mProgressHelper.setBarColor(color);
        return this;
    }

    public KAlertDialog setProgressColorResource(int colorRes) {
        mProgressHelper.setBarColor(ContextCompat.getColor(context, colorRes));
        return this;
    }

    public KAlertDialog setProgressWidth(int widthPx) {
        mProgressHelper.setBarWidth(widthPx);
        return this;
    }

    public KAlertDialog setProgressValue(float progress) {
        mProgressHelper.setProgress(progress);
        return this;
    }

    public KAlertDialog setProgressInstantValue(float progress) {
        mProgressHelper.setInstantProgress(progress);
        return this;
    }

    public KAlertDialog setProgressSpinSpeed(float speed) {
        mProgressHelper.setSpinSpeed(speed);
        return this;
    }

    public KAlertDialog setProgressCircleRadius(int radiusPx) {
        mProgressHelper.setCircleRadius(radiusPx);
        return this;
    }

    public KAlertDialog applyStyle(int style) {
        switch (style) {
            case STYLE_MODERN:
                setDialogCornerRadius(28);
                setDialogElevation(12);
                setDimAmount(0.45f);
                setTitleFontWeight(Typeface.BOLD);
                setContentFontWeight(Typeface.NORMAL);
                setConfirmButtonFontWeight(Typeface.BOLD);
                setCancelButtonFontWeight(Typeface.BOLD);
                setConfirmButtonTextSize(14);
                setCancelButtonTextSize(14);
                setConfirmButtonAllCaps(false);
                setCancelButtonAllCaps(false);
                break;

            case STYLE_MINIMAL:
                setDialogCornerRadius(18);
                setDialogElevation(4);
                setDimAmount(0.30f);
                setTitleFontWeight(Typeface.BOLD);
                setContentFontWeight(Typeface.NORMAL);
                setConfirmButtonAllCaps(false);
                setCancelButtonAllCaps(false);
                break;

            case STYLE_ROUNDED:
                setDialogCornerRadius(32);
                setDialogElevation(10);
                setDimAmount(0.40f);
                setTitleFontWeight(Typeface.BOLD);
                setConfirmButtonFontWeight(Typeface.BOLD);
                setCancelButtonFontWeight(Typeface.BOLD);
                break;

            case STYLE_CLASSIC:
            default:
                break;
        }

        return this;
    }
}