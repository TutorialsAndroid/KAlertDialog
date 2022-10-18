package com.developer.kalert;

import static android.view.View.GONE;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.developer.progressx.ProgressWheel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class KAlertDialog extends AlertDialog implements View.OnClickListener {

    private final Context context;

    private final AnimationSet mModalInAnim, mModalOutAnim, mErrorXInAnim;
    private final Animation mOverlayOutAnim, mImageAnim;

    private TextView mTitleTextView, mContentTextView;
    private ImageView mErrorX, mSuccessTick, mCustomImage, mCustomBigImage;
    private Drawable mCustomImgDrawable;
    private AppCompatButton mConfirmButton, mCancelButton;
    private Drawable mColor, mCancelColor;
    private View mDialogView;
    private FrameLayout mCustomViewContainer;

    private String mTitleText, mContentText, mCancelText, mConfirmText, mInputFieldHint;
    private String imageURL;
    private String titleFont, contentFont;
    private int displayType;
    private int titleColor = 0, contentColor = 0,
            confirmTextColor = android.R.color.white, cancelTextColor = android.R.color.white;
    private int drawableColor = 0;
    private Integer contentAlignment, contentGravity;
    private int titleTextGravity = Gravity.CENTER;

    private boolean mShowCancel, mShowContent, mShowTitleText, mCloseFromCancel, mShowConfirm;
    private int contentTextSize = 0;
    private int titleTextSize = 0;

    private FrameLayout mErrorFrame, mSuccessFrame, mProgressFrame, mWarningFrame;

    private final ProgressHelper mProgressHelper;
    private ProgressWheel imageLoading;
    private KAlertDialog.KAlertClickListener mCancelClickListener;
    private KAlertDialog.KAlertClickListener mConfirmClickListener;

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
    private TextInputEditText mEditText;

    public interface KAlertClickListener {
        void onClick(KAlertDialog kAlertDialog);
    }

    public KAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        mDialogView = Objects.requireNonNull(getWindow()).getDecorView().findViewById(android.R.id.content);
        mTitleTextView = findViewById(R.id.title_text);
        mContentTextView = findViewById(R.id.content_text);
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

        setTitleText(mTitleText);
        setTitleTextGravity(titleTextGravity);
        dialogTitleFont(titleFont);
        dialogContentFont(contentFont);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setCancelText(mCancelText, cancelTextColor);
        setConfirmText(mConfirmText);
        setConfirmText(mConfirmText, confirmTextColor);
        setConfirmButtonColor(mColor);
        setCancelButtonColor(mCancelColor);
        changeAlertType(mAlertType, true);
        setInputFieldHint(mInputFieldHint);
    }

    public KAlertDialog(Context context, int alertType) {
        super(context, isNightMode(context) ? R.style.alert_dialog_dark : R.style.alert_dialog_light);
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
            public void onAnimationStart(Animation animation) { }

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

    private void restore() {
        mCustomImage.setVisibility(GONE);
        mCustomBigImage.setVisibility(GONE);
        imageLoading.setVisibility(GONE);
        mErrorFrame.setVisibility(GONE);
        mSuccessFrame.setVisibility(GONE);
        mWarningFrame.setVisibility(GONE);
        mProgressFrame.setVisibility(GONE);
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
                    setConfirmButtonColor(mColor);
                    break;
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);
                    setConfirmButtonColor(mColor);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    setConfirmButtonColor(mColor);
                    break;
                case WARNING_TYPE:
                    mWarningFrame.setVisibility(View.VISIBLE);
                    setConfirmButtonColor(mColor);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage1(mCustomImgDrawable);
                    setCustomImageColorFilter(drawableColor);
                    setConfirmButtonColor(mColor);
                    break;
                case URL_IMAGE_TYPE:
                    setURLImage1(imageURL, displayType);
                    setConfirmButtonColor(mColor);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(GONE);
                    setConfirmButtonColor(mColor);
                    break;
                case INPUT_TYPE:
                    showInputView();
                    setConfirmButtonColor(mColor);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
        hideInputView();
        hideKeyboard();
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
                mTitleTextView.setText(Html.fromHtml(mTitleText,1));
            }else {
                mTitleTextView.setText(Html.fromHtml(mTitleText));
            }
        } else {
            showTitleText(false);
        }
        return this;
    }

    public KAlertDialog setTitleTextGravity(int gravity) {
        titleTextGravity = gravity;
        if ( mTitleTextView != null ) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = gravity;

            mTitleTextView.setLayoutParams(params);
        }

        return this;
    }

    private void showTitleText(boolean isShow) {
        mShowTitleText = true;
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility( isShow ? View.VISIBLE : GONE );
            mTitleTextView.setAutoLinkMask(Linkify.ALL);
        }
    }

    public KAlertDialog setCustomImage(int resourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //ResourcesCompat.getDrawable( context.getResources(), resourceId, context.getTheme() );
            return setCustomImage1(getContext().getResources().getDrawable( resourceId, context.getTheme() ) );
       } else {
            return setCustomImage1(getContext().getResources().getDrawable(resourceId));
        }
    }

    public KAlertDialog setDrawableTintOnNightMode(boolean isTinted, int tintColor) {
        if ( isTinted && isNightMode(context) ) {
            setCustomImageColorFilter( tintColor );
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
        if (mCustomImage != null && mCustomBigImage != null && imageLoading != null) {
            imageLoading.setVisibility(View.VISIBLE);

            switch (displayType) {
                case IMAGE_BIG:
                    mCustomBigImage.setVisibility(View.VISIBLE);
                    Glide.with(mCustomBigImage)
                            .load(imageURL)
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
                    mCustomImage.setVisibility(View.VISIBLE);
                    Glide.with(mCustomImage)
                            .load(imageURL)
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
                            .circleCrop()
                            .into(mCustomImage);
                    break;
            }
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
                mContentTextView.setText(Html.fromHtml(mContentText,0));
            }else {
                mContentTextView.setText(Html.fromHtml(mContentText));
            }
        } else {
            showContentText(false);
        }
        return this;
    }

    public KAlertDialog showCancelButton ( boolean isShow){
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : GONE);
        }
        return this;
    }

    public KAlertDialog showConfirmButton (boolean isShow){
        mShowConfirm = isShow;
        if (mConfirmButton != null) {
            mConfirmButton.setVisibility(mShowConfirm ? View.VISIBLE : GONE);
        }
        return this;
    }

    private KAlertDialog dialogTitleFont(String path) {
        if (context != null) {
            if (mTitleTextView != null && path != null) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), path);
                //Typeface typeface = ResourcesCompat.getFont(context, titleFont);
                mTitleTextView.setTypeface(font);
            }
        }
        return this;
    }

    private KAlertDialog dialogContentFont(String path) {
        if (context != null) {
            if (mContentTextView != null && path != null) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), path);
                //Typeface typeface = ResourcesCompat.getFont(context, contentFont);
                mContentTextView.setTypeface(font);
            }
        }
        return this;
    }

    private void showContentText (boolean isShow) {
        mShowContent = true;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(isShow ? View.VISIBLE : GONE);
            mContentTextView.setAutoLinkMask(Linkify.ALL);
        }
    }

    @Deprecated
    public KAlertDialog setCancelClickListener (KAlertClickListener listener){
        mCancelClickListener = listener;
        return this;
    }
    public KAlertDialog setCancelClickListener (String text, KAlertClickListener listener){
        //noinspection deprecation
        setCancelText(text);
        mCancelClickListener = listener;
        return this;
    }
    public KAlertDialog setCancelClickListener (String text, int color, KAlertClickListener listener) {
        //noinspection deprecation
        setCancelText(text, color);
        mCancelClickListener = listener;
        return this;
    }

    @Deprecated
    public KAlertDialog setConfirmClickListener (KAlertClickListener listener){
        mConfirmClickListener = listener;
        return this;
    }
    public KAlertDialog setConfirmClickListener (String text, KAlertClickListener listener){
        //noinspection deprecation
        setConfirmText(text);
        mConfirmClickListener = listener;
        return this;
    }
    public KAlertDialog setConfirmClickListener (String text, int color, KAlertClickListener listener) {
        //noinspection deprecation
        setConfirmText(text, color);
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart () {
        super.onStart();
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    @Override
    public void cancel () {
        dismissWithAnimation(true);
    }

    private KAlertDialog setConfirmButtonColor (Drawable background){
        mColor = background;
        if (mConfirmButton != null && mColor != null) {
            mConfirmButton.setBackground(mColor);
        }
        return this;
    }

    private KAlertDialog setCancelButtonColor (Drawable background){
        mCancelColor = background;
        if (mCancelButton != null && mCancelColor != null) {
            mCancelButton.setBackground(mCancelColor);
        }
        return this;
    }

    public void dismissWithAnimation () {
        dismissWithAnimation(false);
    }

    public void dismissWithAnimation ( boolean fromCancel){
        mCloseFromCancel = fromCancel;
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    public static int spToPx ( float sp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public int getAlertType () {
        return mAlertType;
    }

    public String getTitleText () {
        return mTitleText;
    }

    public boolean isShowTitleText () {
        return mShowTitleText;
    }

    public String getContentText () {
        return mContentText;
    }

    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public boolean isShowConfirmButton () {
        return mShowConfirm;
    }

    public boolean isShowContentText () {
        return mShowContent;
    }

    public String getCancelText () {
        return mCancelText;
    }

    @Deprecated
    public KAlertDialog setCancelText (String text){
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
            mCancelButton.setTextColor( ContextCompat.getColor(context, cancelTextColor) );
        }
        return this;
    }

    @Deprecated
    public KAlertDialog setCancelText (String text, int color){
        mCancelText = text;
        cancelTextColor = color;
        if (mCancelButton != null && mCancelText != null && cancelTextColor != 0) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
            mCancelButton.setTextColor( ContextCompat.getColor(context, cancelTextColor) );
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    @Deprecated
    public KAlertDialog setConfirmText (String text){
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            showConfirmButton(true);
            mConfirmButton.setText(mConfirmText);
            mConfirmButton.setTextColor(ContextCompat.getColor(context, confirmTextColor));
        }
        return this;
    }

    @Deprecated
    public KAlertDialog setConfirmText (String text, int color) {
        mConfirmText = text;
        confirmTextColor = color;
        if (mConfirmButton != null && mConfirmText != null && confirmTextColor != 0) {
            showConfirmButton(true);
            mConfirmButton.setText(mConfirmText);
            mConfirmButton.setTextColor( ContextCompat.getColor(context, confirmTextColor) );
        }
        return this;
    }


    public KAlertDialog confirmButtonColor (int color){
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setConfirmButtonColor(ContextCompat.getDrawable(context, color));
        //}else {
            //return setConfirmButtonColor(ContextCompat.getDrawable(context, color));
        //}
    }

    public KAlertDialog cancelButtonColor (int color){
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setCancelButtonColor(ContextCompat.getDrawable(context, color));
        //}else {
            //return setCancelButtonColor(getContext().getResources().getDrawable(color));
        //}
    }

    public KAlertDialog setTitleColor(int color) {
        this.titleColor = color;
        return this;
    }

    public KAlertDialog setContentColor(int color) {
        this.contentColor = color;
        return this;
    }

    public KAlertDialog setTitleTTFFont(String fontPath) {
        this.titleFont = fontPath;
        return this;
    }

    public KAlertDialog setContentTTFFont(String fontPath) {
        this.contentFont = fontPath;
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

    public KAlertDialog setContentTextSize ( int value){
        this.contentTextSize = value;
        return this;
    }

    public int getContentTextSize ()
    {
        return contentTextSize;
    }

    @Override
    public void onClick (View v){
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(KAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.custom_confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(KAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    public ProgressHelper getProgressHelper () {
        return mProgressHelper;
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

    public String getInputText () {
        return mEditText.getText().toString();
    }

    private void showKeyboard() {
        final InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!mEditText.hasFocus()) {
            mEditText.requestFocus();
        }

        mEditText.post(() -> imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT));
    }

    private void hideKeyboard() {
        if (mEditText != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService
                    (Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }

    public static boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}