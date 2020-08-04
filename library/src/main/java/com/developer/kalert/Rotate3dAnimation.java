package com.developer.kalert;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation {
    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;
    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;

    private final float fromDegrees, toDegrees;
    private float pivotX,pivotY;
    private Camera camera;
    private final int rollType;

    private static final int ROLL_BY_X = 0;
    private static final int ROLL_BY_Y = 1;
    private static final int ROLL_BY_Z = 2;

    static class Description {
        int type;
        float value;
    }

    private Description parseValue(TypedValue value) {
        Description d = new Description();
        if (value == null) {
            d.type = ABSOLUTE;
            d.value = 0;
        } else {
            if (value.type == TypedValue.TYPE_FRACTION) {
                d.type = (value.data & TypedValue.COMPLEX_UNIT_MASK) ==
                        TypedValue.COMPLEX_UNIT_FRACTION_PARENT ?
                        RELATIVE_TO_PARENT : RELATIVE_TO_SELF;
                d.value = TypedValue.complexToFloat(value.data);
                return d;
            } else if (value.type == TypedValue.TYPE_FLOAT) {
                d.type = ABSOLUTE;
                d.value = value.getFloat();
                return d;
            } else if (value.type >= TypedValue.TYPE_FIRST_INT &&
                    value.type <= TypedValue.TYPE_LAST_INT) {
                d.type = ABSOLUTE;
                d.value = value.data;
                return d;
            }
        }

        d.type = ABSOLUTE;
        d.value = 0.0f;

        return d;
    }

    public Rotate3dAnimation (Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Rotate3dAnimation);

        fromDegrees = a.getFloat(R.styleable.Rotate3dAnimation_fromDeg, 0.0f);
        toDegrees = a.getFloat(R.styleable.Rotate3dAnimation_toDeg, 0.0f);
        rollType = a.getInt(R.styleable.Rotate3dAnimation_rollType, ROLL_BY_X);
        Description d = parseValue(a.peekValue(R.styleable.Rotate3dAnimation_pivotX));
        mPivotXType = d.type;
        mPivotXValue = d.value;

        d = parseValue(a.peekValue(R.styleable.Rotate3dAnimation_pivotY));
        mPivotYType = d.type;
        mPivotYValue = d.value;

        a.recycle();

        initializePivotPoint();
    }

    public Rotate3dAnimation (int rollType, float fromDegrees, float toDegrees) {
        this.rollType = rollType;
        this.fromDegrees = fromDegrees;
        this.toDegrees = toDegrees;
        pivotX = 0.0f;
        pivotY = 0.0f;
    }

    public Rotate3dAnimation (int rollType, float fromDegrees, float toDegrees, float pivotX, float pivotY) {
        this.rollType = rollType;
        this.fromDegrees = fromDegrees;
        this.toDegrees = toDegrees;

        mPivotXType = ABSOLUTE;
        mPivotYType = ABSOLUTE;
        mPivotXValue = pivotX;
        mPivotYValue = pivotY;
        initializePivotPoint();
    }

    public Rotate3dAnimation (int rollType, float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        this.rollType = rollType;
        this.fromDegrees = fromDegrees;
        this.toDegrees = toDegrees;

        mPivotXValue = pivotXValue;
        mPivotXType = pivotXType;
        mPivotYValue = pivotYValue;
        mPivotYType = pivotYType;
        initializePivotPoint();
    }

    private void initializePivotPoint() {
        if (mPivotXType == ABSOLUTE) {
            pivotX = mPivotXValue;
        }
        if (mPivotYType == ABSOLUTE) {
            pivotY = mPivotYValue;
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
        pivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        pivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = this.fromDegrees;
        float degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);

        final Matrix matrix = t.getMatrix();

        camera.save();
        switch (rollType) {
            case ROLL_BY_X:
                camera.rotateX(degrees);
                break;
            case ROLL_BY_Y:
                camera.rotateY(degrees);
                break;
            case ROLL_BY_Z:
                camera.rotateZ(degrees);
                break;
        }
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-pivotX, -pivotY);
        matrix.postTranslate(pivotX, pivotY);
    }
}