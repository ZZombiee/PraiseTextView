package com.zombie.app.widget.praise.animator.label;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * 作者:郭生生
 **/
public abstract class BaseDrawableAnimator {
    protected Drawable mDrawable;

    public BaseDrawableAnimator(Drawable drawable) {
        mDrawable = drawable;
    }

    /**
     * tX  Drawable在x轴偏移量
     * tY  Drawable在y轴偏移量
     * w   Drawable的宽
     * h   Drawable的长
     */
    public abstract void drawLabel(Canvas canvas, float progress, float tX, float tY, int w, int h);
}
