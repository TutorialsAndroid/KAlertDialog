package com.kinda.alert;

import android.app.AlertDialog;

import com.kinda.progressx.ProgressWheel;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class KAlertDialog extends AlertDialog implements View.OnClickListener {

    private View mDialogView;
    private final AnimationSet mModalInAnim;
    private final AnimationSet mModalOutAnim;
    private final Animation mOverlayOutAnim;
    private final Animation mErrorInAnim;
    private final AnimationSet mErrorXInAnim;
    private final AnimationSet mSuccessLayoutAnimSet;
    private final Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private String mTitleText;
    private String mContentText;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;
    private int mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame;
    private SuccessTickView mSuccessTick;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private Button mConfirmButton;
    private Button mCancelButton;
    private final ProgressHelper mProgressHelper;
    private FrameLayout mWarningFrame;
    private KAlertClickListener mCancelClickListener;
    private KAlertClickListener mConfirmClickListener;
    private boolean mCloseFromCancel;
    private Drawable mColor;
    private Drawable mCancelColor;

    private static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;

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
        mErrorX = mErrorFrame.findViewById(R.id.error_x);
        mSuccessFrame = findViewById(R.id.success_frame);
        mProgressFrame = findViewById(R.id.progress_dialog);
        mSuccessTick = mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mCustomImage = findViewById(R.id.custom_image);
        mWarningFrame = findViewById(R.id.warning_frame);
        mConfirmButton = findViewById(R.id.custom_confirm_button);
        mCancelButton = findViewById(R.id.cancel_button);
        mProgressHelper.setProgressWheel((ProgressWheel) findViewById(R.id.progressWheel));
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setConfirmButtonColor(mColor);
        setCancelButtonColor(mCancelColor);
        changeAlertType(mAlertType, true);
    }

    public KAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mErrorInAnim = AnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        mSuccessBowAnim = AnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) AnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        Objects.requireNonNull(mModalOutAnim).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            KAlertDialog.super.cancel();
                        } else {
                            KAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
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

    private void restore () {
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
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
    }

    private void playAnimation () {
        if (mAlertType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            switch (mAlertType) {
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);
                    setConfirmButtonColor(mColor);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    // initial rotate layout of success mask
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    setConfirmButtonColor(mColor);
                    break;
                case WARNING_TYPE:
                   // mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mWarningFrame.setVisibility(View.VISIBLE);
                    setConfirmButtonColor(mColor);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage(mCustomImgDrawable);
                    setConfirmButtonColor(mColor);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    setConfirmButtonColor(mColor);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    /**
     * Method to get the type of alert dialog
     */
    public int getAlertType() {
        return mAlertType;
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }


    /**
     * sets the title for alert dialog
     * @return mTitleText
     */
    public String getTitleText () {
        return mTitleText;
    }

    public KAlertDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    /**
     * By this method developers can put own image
     * in alert dialog
     * @param resourceId location of the image
     * @return this
     */
    public KAlertDialog setCustomImage (int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }

    private KAlertDialog setCustomImage(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    /**
     * Gets the content text of alert dialog
     * @return mContextText
     */
    public String getContentText () {
        return mContentText;
    }

    public KAlertDialog setContentText (String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText();
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    /**
     * Weather to show cancel button or not
     * @return mShowCancel
     */
    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    /**
     * Shows the cancel button
     * @param isShow weather cancel button should
     *  visible or not
     * @return this
     */
    public KAlertDialog showCancelButton (boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    /**
     * Weather to show content text
     * or not in alert dialog
     * @return mShowContent
     */
    public boolean isShowContentText () {
        return mShowContent;
    }

    /**
     * Weather to show content text or not
     */
    private void showContentText() {
        mShowContent = true;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the cancel button text in alert dialog
     * @param text manually input cancel button text
     * @return this
     */
    public KAlertDialog setCancelText (String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getCancelText () {
        return mCancelText;
    }


    /**
     * Sets the confirm button text
     * @param text manually input confirm button text
     * @return this
     */
    public KAlertDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    /**
     * On cancel button click listener
     * @param listener click listener which task should be
     * performed on cancel button click listener
     * @return this
     */
    public KAlertDialog setCancelClickListener (KAlertClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    /**
     * On confirm button click listener
     * @param listener click listener which task should be
     * performed on confirm button click listener
     * @return this
     */
    public KAlertDialog setConfirmClickListener (KAlertClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    public void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    /**
     * sets the color for confirm button
     * @param color developers can manually change the confirm
     * button colors or they can create a drawable and customize
     * the confirm button colors and rounded corners.
     * @return setConfirmButtonColor
     */
    public KAlertDialog confirmButtonColor (int color) {
        return setConfirmButtonColor(getContext().getResources().getDrawable(color));
    }

    private KAlertDialog setConfirmButtonColor(Drawable background) {
        mColor = background;
        if (mConfirmButton != null && mColor !=null) {
            mConfirmButton.setBackground(mColor);
        }
        return this;
    }

    /**
     * sets the color for cancel button
     * @param color developers can manually change the cancel
     * button colors or they can create a drawable and customize
     * the camcel button colors and rounded corners.
     * @return setCancelButtonColor
     */
    public KAlertDialog cancelButtonColor (int color) {
        return setCancelButtonColor(getContext().getResources().getDrawable(color));
    }

    private KAlertDialog setCancelButtonColor(Drawable background) {
        mCancelColor = background;
        if (mCancelButton != null && mCancelColor !=null) {
            mCancelButton.setBackground(mCancelColor);
        }
        return this;
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