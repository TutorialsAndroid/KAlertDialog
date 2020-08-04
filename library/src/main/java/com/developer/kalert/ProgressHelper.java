package com.developer.kalert;

import android.content.Context;
import android.os.Build;
import com.developer.progressx.ProgressWheel;

@SuppressWarnings("unused")
public class ProgressHelper {

    private ProgressWheel progressWheel;
    private boolean spin, progress;
    private float spinSpeed, progressValue;
    private int barWidth, barColor, rimWidth, rimColor, circleRadius;

    ProgressHelper(Context ctx) {
        spin = true;
        spinSpeed = 0.75f;
        barWidth = ctx.getResources().getDimensionPixelSize(R.dimen.common_circle_width) + 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barColor = ctx.getResources().getColor(R.color.success_stroke_color,ctx.getTheme());
        }else {
            barColor = ctx.getResources().getColor(R.color.success_stroke_color);
        }
        rimWidth = 0;
        rimColor = 0x00000000;
        progress = false;
        progressValue = -1;
        circleRadius = ctx.getResources().getDimensionPixelOffset(R.dimen.progress_circle_radius);
    }

    void setProgressWheel(ProgressWheel progressWheel) {
        this.progressWheel = progressWheel;
        updatePropsIfNeed();
    }

    private void updatePropsIfNeed () {
        if (progressWheel != null) {
            if (!spin && progressWheel.isSpinning()) {
                progressWheel.stopSpinning();
            } else if (spin && !progressWheel.isSpinning()) {
                progressWheel.spin();
            }
            if (spinSpeed != progressWheel.getSpinSpeed()) {
                progressWheel.setSpinSpeed(spinSpeed);
            }
            if (barWidth != progressWheel.getBarWidth()) {
                progressWheel.setBarWidth(barWidth);
            }
            if (barColor != progressWheel.getBarColor()) {
                progressWheel.setBarColor(barColor);
            }
            if (rimWidth != progressWheel.getRimWidth()) {
                progressWheel.setRimWidth(rimWidth);
            }
            if (rimColor != progressWheel.getRimColor()) {
                progressWheel.setRimColor(rimColor);
            }
            if (progressValue != progressWheel.getProgress()) {
                if (progress) {
                    progressWheel.setInstantProgress(progressValue);
                } else {
                    progressWheel.setProgress(progressValue);
                }
            }
            if (circleRadius != progressWheel.getCircleRadius()) {
                progressWheel.setCircleRadius(circleRadius);
            }
        }
    }

    public ProgressWheel getProgressWheel () {
        return progressWheel;
    }

    public void resetCount() {
        if (progressWheel != null) {
            progressWheel.resetCount();
        }
    }

    public boolean isSpinning() {
        return spin;
    }

    public void spin() {
        spin = true;
        updatePropsIfNeed();
    }

    public void stopSpinning() {
        spin = false;
        updatePropsIfNeed();
    }

    public float getProgress() {
        return progressValue;
    }

    public void setProgress(float progress) {
        this.progress = false;
        progressValue = progress;
        updatePropsIfNeed();
    }

    public void setInstantProgress(float progress) {
        progressValue = progress;
        this.progress = true;
        updatePropsIfNeed();
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        updatePropsIfNeed();
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        updatePropsIfNeed();
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
        updatePropsIfNeed();
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
        updatePropsIfNeed();
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
        updatePropsIfNeed();
    }

    public float getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        this.spinSpeed = spinSpeed;
        updatePropsIfNeed();
    }
}
