package com.zombie.app.widget.praise.animator.label;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

/**
 * 作者:郭生生
 **/
public class RevealDrawableAnimator extends BaseDrawableAnimator {
    private boolean isReveal = false;
    private Path mPath = new Path();

    public RevealDrawableAnimator(Drawable drawable) {
        super(drawable);
    }

    @Override
    public void drawLabel(Canvas canvas, float progress, float tX, float tY, int w, int h) {
        if (progress == 1) {
            mDrawable.setLevel(isReveal ? 0 : 1);
            mDrawable.draw(canvas);
        } else {
            canvas.save();
            mDrawable.setLevel(isReveal ? 1 : 0);
            mDrawable.draw(canvas);
            canvas.restore();
            canvas.save();
            mPath.reset();
            mPath.addCircle(w / 2, h / 2 + 5, progress * Math.max(w, h), Path.Direction.CW);
            canvas.clipPath(mPath);
            mDrawable.setLevel(isReveal ? 0 : 1);
            mDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public void setState(boolean isReveal) {
        this.isReveal = isReveal;
    }

    public boolean getState() {
        return isReveal;
    }
}

