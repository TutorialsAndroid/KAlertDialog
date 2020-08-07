package com.developer.kalert;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.text.util.Linkify;
import android.util.TypedValue;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

@SuppressWarnings("ALL")
public class KAlertDialog extends AlertDialog implements View.OnClickListener {

    private final AnimationSet mModalInAnim, mModalOutAnim, mErrorXInAnim;
    private final Animation mOverlayOutAnim, mImageAnim;

    private TextView mTitleTextView, mContentTextView;
    private ImageView mErrorX, mSuccessTick, mCustomImage;
    private Drawable mCustomImgDrawable;
    private Button mConfirmButton, mCancelButton;
    private Drawable mColor, mCancelColor;
    private View mDialogView;
    private View mCustomView;
    private FrameLayout mCustomViewContainer;

    private String mTitleText, mContentText, mCancelText, mConfirmText;

    private boolean mShowCancel, mShowContent, mShowTitleText, mCloseFromCancel, mShowConfirm;
    private int contentTextSize = 0;
    private int titleTextSize = 0;

    private FrameLayout mErrorFrame, mSuccessFrame, mProgressFrame, mWarningFrame;

    private final ProgressHelper mProgressHelper;
    private KAlertDialog.KAlertClickListener mCancelClickListener;
    private KAlertDialog.KAlertClickListener mConfirmClickListener;

    private int mAlertType;
    public static final int NORMAL_TYPE = 0;

    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;

    public static boolean DARK_STYLE = false;

    public interface KAlertClickListener {
        void onClick(KAlertDialog kAlertDialog);
    }

    public static final int INPUT_TYPE = 6;
    private EditText mEditText;

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
        mWarningFrame = findViewById(R.id.warning_frame);
        mCustomViewContainer = findViewById(R.id.custom_view_container);
        mProgressHelper.setProgressWheel(findViewById(R.id.progressWheel));

        mConfirmButton = findViewById(R.id.custom_confirm_button);
        mCancelButton = findViewById(R.id.cancel_button);
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        setCustomView(mCustomView);
        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setConfirmButtonColor(mColor);
        setCancelButtonColor(mCancelColor);
        changeAlertType(mAlertType, true);
    }

    public KAlertDialog(Context context, int alertType) {
        super(context, DARK_STYLE ? R.style.alert_dialog_dark : R.style.alert_dialog_light);

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
                mDialogView.setVisibility(View.GONE);
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

        Objects.requireNonNull(this.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void restore() {
        mCustomImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mProgressFrame.setVisibility(View.GONE);
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
                    setConfirmButtonColor(mColor);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    setConfirmButtonColor(mColor);
                    break;
                case INPUT_TYPE:
                    mEditText.requestFocus();
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
    }

    public KAlertDialog setTitleText(String text) {

        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            showTitleText();
            if (titleTextSize != 0) {
                mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(titleTextSize, getContext()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTitleTextView.setText(Html.fromHtml(mTitleText,1));
            }else {
                mTitleTextView.setText(Html.fromHtml(mTitleText));
            }
        }
        return this;
    }

    private void showTitleText() {
        mShowTitleText = true;
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(View.VISIBLE);
            mContentTextView.setAutoLinkMask(Linkify.ALL);
        }
    }

    public KAlertDialog setCustomImage(int resourceId, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setCustomImage1(getContext().getResources().getDrawable(resourceId,context.getTheme()));
       } else {
            return setCustomImage1(getContext().getResources().getDrawable(resourceId));
        }
    }

    private KAlertDialog setCustomImage1(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public KAlertDialog setContentText(String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText();
            if (contentTextSize != 0) {
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(contentTextSize, getContext()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mContentTextView.setText(Html.fromHtml(mContentText,0));
            }else {
                mContentTextView.setText(Html.fromHtml(mContentText));
            }
            mCustomViewContainer.setVisibility(View.GONE);
        }
        return this;
    }

    public KAlertDialog showCancelButton ( boolean isShow){
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public KAlertDialog showConfirmButton (boolean isShow){
        mShowConfirm = isShow;
        if (mConfirmButton != null) {
            mConfirmButton.setVisibility(mShowConfirm ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    private void showContentText () {
        mShowContent = true;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(View.VISIBLE);
            mContentTextView.setAutoLinkMask(Linkify.ALL);
        }
    }

    public KAlertDialog setCancelClickListener (KAlertClickListener listener){
        mCancelClickListener = listener;
        return this;
    }

    public KAlertDialog setConfirmClickListener (KAlertClickListener listener){
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart () {
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

    public String getInputText () {
        return mEditText.getText().toString();
    }

    public void setInputText (String text){
        mEditText.setText(text);
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

    public KAlertDialog setCancelText (String text){
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public KAlertDialog setConfirmText (String text){
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            showConfirmButton(true);
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public KAlertDialog confirmButtonColor (int color, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setConfirmButtonColor(getContext().getResources().getDrawable(color,context.getTheme()));
        }else {
            return setConfirmButtonColor(getContext().getResources().getDrawable(color));
        }
    }

    public KAlertDialog cancelButtonColor (int color, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setCancelButtonColor(getContext().getResources().getDrawable(color,context.getTheme()));
        }else {
            return setCancelButtonColor(getContext().getResources().getDrawable(color));
        }
    }

    public KAlertDialog setTitleTextSize(int value){
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

    public KAlertDialog setCustomView (View view){
        mCustomView = view;
        if (mCustomView != null && mCustomViewContainer != null) {
            mCustomViewContainer.addView(view);
            mCustomViewContainer.setVisibility(View.VISIBLE);
        }
        return this;
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
}