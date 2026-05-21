package com.developer.kalert;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

public class SuccessTickView extends View {

    private float mDensity = -1;

    private Paint mPaint;

    private final float CONST_RADIUS = dip2px(1.2f);
    private float rectWeight = dip2px(3);

    private final float CONST_LEFT_RECT_W = dip2px(15);
    private final float CONST_RIGHT_RECT_W = dip2px(25);
    private final float MIN_LEFT_RECT_W = dip2px(3.3f);
    private final float MAX_RIGHT_RECT_W = CONST_RIGHT_RECT_W + dip2px(6.7f);

    private float maxLeftRectWidth;
    private float leftRectWidth;
    private float rightRectWidth;

    private boolean mLeftRectGrowMode;

    private final RectF leftRect = new RectF();
    private final RectF rightRect = new RectF();

    private long tickAnimationDuration = 750;
    private long tickAnimationStartOffset = 100;

    public SuccessTickView(Context context) {
        super(context);
        init();
    }

    public SuccessTickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.success_stroke_color));

        leftRectWidth = CONST_LEFT_RECT_W;
        rightRectWidth = CONST_RIGHT_RECT_W;
        mLeftRectGrowMode = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int totalW = getWidth();
        int totalH = getHeight();

        if (totalW <= 0 || totalH <= 0) {
            return;
        }

        canvas.save();
        canvas.rotate(45, totalW >> 1, totalH >> 1);

        float drawW = totalW / 1.2f;
        float drawH = totalH / 1.4f;

        maxLeftRectWidth = (drawW + CONST_LEFT_RECT_W) / 2f + rectWeight - 1;

        if (mLeftRectGrowMode) {
            leftRect.left = 0;
            leftRect.right = leftRect.left + leftRectWidth;
        } else {
            leftRect.right = (drawW + CONST_LEFT_RECT_W) / 2f + rectWeight - 1;
            leftRect.left = leftRect.right - leftRectWidth;
        }

        leftRect.top = (drawH + CONST_RIGHT_RECT_W) / 2f;
        leftRect.bottom = leftRect.top + rectWeight;

        canvas.drawRoundRect(leftRect, CONST_RADIUS, CONST_RADIUS, mPaint);

        rightRect.bottom = (drawH + CONST_RIGHT_RECT_W) / 2f + rectWeight - 1;
        rightRect.left = (drawW + CONST_LEFT_RECT_W) / 2f;
        rightRect.right = rightRect.left + rectWeight;
        rightRect.top = rightRect.bottom - rightRectWidth;

        canvas.drawRoundRect(rightRect, CONST_RADIUS, CONST_RADIUS, mPaint);

        canvas.restore();
    }

    private float dip2px(float dpValue) {
        if (mDensity == -1) {
            mDensity = getResources().getDisplayMetrics().density;
        }
        return dpValue * mDensity + 0.5f;
    }

    public void startTickAnim() {
        leftRectWidth = 0;
        rightRectWidth = 0;
        invalidate();

        Animation tickAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                if (0.54f < interpolatedTime && 0.7f >= interpolatedTime) {
                    mLeftRectGrowMode = true;
                    leftRectWidth = maxLeftRectWidth * ((interpolatedTime - 0.54f) / 0.16f);

                    if (0.65f < interpolatedTime) {
                        rightRectWidth = MAX_RIGHT_RECT_W * ((interpolatedTime - 0.65f) / 0.19f);
                    }

                    invalidate();

                } else if (0.7f < interpolatedTime && 0.84f >= interpolatedTime) {
                    mLeftRectGrowMode = false;
                    leftRectWidth = maxLeftRectWidth * (1 - ((interpolatedTime - 0.7f) / 0.14f));
                    leftRectWidth = Math.max(leftRectWidth, MIN_LEFT_RECT_W);
                    rightRectWidth = MAX_RIGHT_RECT_W * ((interpolatedTime - 0.65f) / 0.19f);

                    invalidate();

                } else if (0.84f < interpolatedTime && 1f >= interpolatedTime) {
                    mLeftRectGrowMode = false;
                    leftRectWidth = MIN_LEFT_RECT_W + (CONST_LEFT_RECT_W - MIN_LEFT_RECT_W) * ((interpolatedTime - 0.84f) / 0.16f);
                    rightRectWidth = CONST_RIGHT_RECT_W + (MAX_RIGHT_RECT_W - CONST_RIGHT_RECT_W) * (1 - ((interpolatedTime - 0.84f) / 0.16f));

                    invalidate();
                }
            }
        };

        tickAnim.setDuration(tickAnimationDuration);
        tickAnim.setStartOffset(tickAnimationStartOffset);
        startAnimation(tickAnim);
    }

    public void setTickColor(@ColorInt int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public void setTickColorResource(int colorRes) {
        mPaint.setColor(ContextCompat.getColor(getContext(), colorRes));
        invalidate();
    }

    public void setTickStrokeWidth(float strokeWidthDp) {
        rectWeight = dip2px(strokeWidthDp);
        invalidate();
    }

    public void setTickAnimationDuration(long durationMillis) {
        tickAnimationDuration = durationMillis;
    }

    public void setTickAnimationStartOffset(long startOffsetMillis) {
        tickAnimationStartOffset = startOffsetMillis;
    }
}