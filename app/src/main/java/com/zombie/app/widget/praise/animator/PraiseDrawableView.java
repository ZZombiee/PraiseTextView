package com.zombie.app.widget.praise.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.zombie.app.widget.praise.animator.label.RevealDrawableAnimator;
import com.zombie.app.widget.praise.animator.text.MarqueeTextAnimator;

/**
 * 作者:郭生生
 **/
public class PraiseDrawableView extends DrawableView {
    private ValueAnimator animator;
    private float mProgress = 0f;
    private boolean isAnimator = false;
    private RevealDrawableAnimator reveal;
    private String mOldText;
    private String mNewText;
    private MarqueeTextAnimator marquee;

    public PraiseDrawableView(Context context) {
        super(context);
    }

    public PraiseDrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PraiseDrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        reveal = new RevealDrawableAnimator(mDrawable);
        marquee = new MarqueeTextAnimator(mTextSize, mTextColor);
        animator = new ValueAnimator();
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimator = true;
            }
        });
    }

    public void animator() {
        if (animator.isRunning()) {
            animator.cancel();
            animatorEnd();
        }
        mNewText = "" + (reveal.getState() ? (ofInteger(initText) - 1) : (ofInteger(initText) + 1));
        mOldText = initText;
        requestLayout();
        animator.start();
    }

    public void animator(int newText) {
        if (animator.isRunning()) {
            animator.cancel();
            animatorEnd();
        }
        mNewText = "" + newText;
        mOldText = initText;
        animator.start();
    }

    @Override
    protected void drawLabel(Canvas canvas) {
        if (isAnimator) {
            reveal.drawLabel(canvas, mProgress, getTranslateX(), getTranslateY(), imgWidth, imgHeight);
        } else
            super.drawLabel(canvas);
    }

    public void setPraise(boolean praise) {
        marquee.setMarquee(praise);
        reveal.setState(praise);
        mDrawable.setLevel(praise ? 1 : 0);
    }

    @Override
    protected void drawText(Canvas canvas) {
        if (isAnimator) {
            marquee.setReadyDrawText(mOldText, mNewText);
            marquee.drawText(canvas, mProgress, mDrawablePadding + imgWidth, getMeasuredHeight(), mTextHeight);
            if (mProgress == 1) {
                animatorEnd();
            }
        } else
            super.drawText(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPaint != null && !TextUtils.isEmpty(mNewText) && !TextUtils.isEmpty(mOldText))
            setMeasuredDimension((int) (getMeasuredWidth() + Math.max(0, Math.abs(mPaint.measureText(mOldText) - mPaint.measureText(mNewText)))), getMeasuredHeight());
    }

    private void animatorEnd() {
        isAnimator = false;
        initText = mNewText;
        reveal.setState(!reveal.getState());
        marquee.setMarquee(!marquee.getMarquee());
    }

    public Integer ofInteger(String s) {
        try {
            if (TextUtils.isEmpty(s))
                return 0;
            return Integer.valueOf(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean getState() {
        return reveal.getState();
    }
}
