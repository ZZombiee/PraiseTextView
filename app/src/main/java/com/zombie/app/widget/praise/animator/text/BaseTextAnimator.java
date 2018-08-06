package com.zombie.app.widget.praise.animator.text;

import android.graphics.Canvas;

/**
 * 作者:郭生生
 **/
public abstract class BaseTextAnimator {
    /**
     * tX         Text在x轴偏移量
     * height     组件的高度
     * textHeight 文字的高度
     * (height+textHeight)/2可使文字居中
     */
    public abstract void drawText(Canvas canvas, float progress, float tX, float height, float textHeight);
}
