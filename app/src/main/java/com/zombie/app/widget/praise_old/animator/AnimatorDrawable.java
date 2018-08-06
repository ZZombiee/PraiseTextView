package com.zombie.app.widget.praise_old.animator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Gss on 2018/5/22 0022.
 */

public abstract class AnimatorDrawable extends Drawable {
    protected Drawable mDrawable;
    private Bitmap bitmap;
    protected Canvas mCanvas;
    protected Path mPath;
    protected Paint mPaint;

    public AnimatorDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mDrawable.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return mDrawable.getOpacity();
    }

    public abstract void animatorCheck(Canvas canvas, float progress);

    public abstract void animatorUnCheck(Canvas canvas, float progress);

    public Bitmap getBitmap() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(
                    mDrawable.getIntrinsicWidth(),
                    mDrawable.getIntrinsicHeight(),
                    mDrawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            mCanvas = new Canvas(bitmap);
            //canvas.setBitmap(bitmap);
            mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
            mDrawable.draw(mCanvas);
        }
        return bitmap;
    }
}
