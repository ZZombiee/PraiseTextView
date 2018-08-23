package com.zombie.app.widget.praise_old;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.zombie.app.utils.NumUtil;
import com.zombie.app.widget.praise_old.animator.PraiseDrawable;


/**
 * Created by Gss on 2018/5/21 0021.
 */
@SuppressLint({"AppCompatCustomView", "ObjectAnimatorBinding"})
public class PraiseTextView extends TextView {

    private Paint paint;
    private CharSequence mOldText;
    private CharSequence mNewText;
    private float mProgress = 0f;
    private Paint mPaint;
    private float mTextHeight;
    private ValueAnimator animator;
    private boolean isAdd = true;
    private float mOldTextWidth;
    private float mNewTextWidth;
    private Rect rect;
    private boolean isDirect;
    private String defaultText = "厉害了";
    private PraiseDrawable mDrawable;
    private int orientation;
    private float mDrawableWidth = 0;
    private float mDrawableHeight = 0;
    private int drawablePadding = 30;

    public PraiseTextView(Context context) {
        this(context, null);
    }

    public PraiseTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                mOldText = mNewText;
            }
        });
        Drawable[] drawables = getCompoundDrawables();
        for (int i = 0; i < drawables.length; i++) {
            if (drawables[i] != null) {
                mDrawable = new PraiseDrawable(drawables[i], context);
                orientation = i;
            }
        }
        if (mDrawable != null) {
            drawables[orientation] = mDrawable;
            setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            mDrawableWidth = mDrawable.getBitmap().getWidth();
            mDrawableHeight = mDrawable.getBitmap().getHeight();
        }
    }

    public void autoAdd() {
        animator.cancel();
        if (TextUtils.isEmpty(mOldText))
            mOldText = getText();
        int i = NumUtil.ofInteger(mOldText.toString());
        mNewText = ++i + "";
        isAdd = true;
        prepare();
        animator.start();
    }

    public void autoReduce() {
        animator.cancel();
        if (TextUtils.isEmpty(mOldText))
            mOldText = getText();
        int i = NumUtil.ofInteger(mOldText.toString());
        mNewText = --i + "";
        isAdd = false;
        prepare();
        animator.start();
    }

    private void prepare() {
        rect = new Rect();
        initPaint();
        mPaint.getTextBounds(mNewText.toString(), 0, mNewText.length(), rect);
        mTextHeight = rect.height();
        mNewTextWidth = rect.width();
        mPaint.getTextBounds(mOldText.toString(), 0, mOldText.length(), rect);
        mOldTextWidth = rect.width();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        isDirect = true;
        mNewText = mOldText = text;
        super.setText(text, type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float startX = this.getLayout().getLineLeft(0);
        float startY = this.getBaseline();
        initPaint();
        //直接设置数值
        if (!isDirect) {
            if (mNewText == null || mOldText == null)
                return;
            bit = 1;
            if (isAdd)
                mDrawable.animatorCheck(canvas, mProgress);
            else
                mDrawable.animatorUnCheck(canvas, mProgress);
            //原始数据的y轴方向位移
            float offsetOldY = startY + 2 * mTextHeight * (isAdd ? -1 : 1) * mProgress;
            //新数据的y轴方向位移
            float offsetNewY = startY + 2 * mTextHeight * (isAdd ? 1 : -1) * (1 - mProgress);
            //如果是增加，判断老数据的进位，如果是减少，判断新数据的进位
            int b = getCarryBitCount(isAdd ? mOldText.toString() : mNewText.toString());
            int length = isAdd ? mOldText.length() : mNewText.length();
            //画不动的部分
            CharSequence c = "";
            if (length - b > 0) {
                c = mOldText.subSequence(0, length - b);
                mPaint.setAlpha(255);
                canvas.drawText(c, 0, c.length(), startX + mDrawableWidth + drawablePadding, startY, mPaint);
            }
            //老数据的透明度变化
            mPaint.setAlpha((int) (255 * (1 - mProgress)));
            //画老数据的部分数据，从length-b的位置开始，一直到最后
            canvas.drawText(mOldText, length - b, mOldText.length(), startX + mDrawableWidth + drawablePadding + mPaint.measureText(c.toString()), offsetOldY, mPaint);
            if (mNewText.equals("0")) {
                mPaint.setAlpha((int) (255 * (mProgress)));
                canvas.drawText(defaultText, startX + mDrawableWidth + drawablePadding, offsetNewY, mPaint);
            } else {
                mPaint.setAlpha(255);
                canvas.drawText("赞", 0, 1, 20 + startX + mDrawableWidth + drawablePadding + mOldTextWidth - (mOldTextWidth - mNewTextWidth) * mProgress, startY, mPaint);
                //draw newText
                mPaint.setAlpha((int) (255 * (mProgress)));
                canvas.drawText(mNewText, length - b, mNewText.length(), startX + mDrawableWidth + drawablePadding + mPaint.measureText(c.toString()), offsetNewY, mPaint);
            }
        } else {
            canvas.drawBitmap(mDrawable.getBitmap(), 0, (canvas.getHeight() - mDrawable.getBitmap().getHeight()) / 2, paint);
            canvas.drawText(mNewText.toString(), startX + mDrawableWidth + drawablePadding, startY, mPaint);
        }
        isDirect = false;
    }

    private void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(Color.GRAY);
            mPaint.setTextSize(40);
        }
    }

    int bit = 1;

    /**
     * 判断进位，默认为1
     * 如果个位为9，说明进位，除以10以后，判断十位是否进位，递归
     */
    private int carryBit(CharSequence oldText) {
        if (NumUtil.ofInteger(oldText.toString()) % 10 == 9) {
            oldText = NumUtil.ofInteger(oldText.toString()) / 10 + "";
            bit++;
            return carryBit(oldText);
        }
        return bit;
    }
    /**
     * 获取进位数，默认是1
     * 根据字符串的长度即可判断进位数是几位
     * */
    private int getCarryBitCount(String oldText){
        if (TextUtils.isEmpty(oldText))
            return 1;
        try {
            int targetNum =  (Integer.valueOf(oldText)) + 1;
            int targetLength = String.valueOf(targetNum).length();
            if (targetLength > oldText.length()){
                return targetLength -1;
            }else if (targetLength == oldText.length()) {
                return 1;
            }
        }catch (NumberFormatException e){
            return 1;
        }
        return 1;
    }
}
