package com.zombie.app.widget.praise.animator.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

/**
 * 作者:郭生生
 **/
public class MarqueeTextAnimator extends BaseTextAnimator {
    private String mOldText;
    private String mNewText;
    int bit = 1;
    private final Paint mPaint;
    private boolean isMarquee = false;
    private float offsetOldY;
    private float offsetNewY;
    private int length;
    private int b;
    private CharSequence c;

    public MarqueeTextAnimator(int textSize, int color) {
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
    }

    public void setReadyDrawText(String old, String newText) {
        mOldText = old;
        mNewText = newText;
    }

    public void setMarquee(boolean marquee) {
        isMarquee = marquee;
    }

    public boolean getMarquee() {
        return isMarquee;
    }

    @Override
    public void drawText(Canvas canvas, float mProgress, float tX, float height, float textHeight) {
        if (mNewText == null || mOldText == null)
            return;
        bit = 1;
        //原始数据的y轴方向位移
        offsetOldY = (height + textHeight) / 2 + 2 * textHeight * (isMarquee ? 1 : -1) * mProgress;
        //新数据的y轴方向位移
        offsetNewY = (height + textHeight) / 2 + 2 * textHeight * (isMarquee ? -1 : 1) * (1 - mProgress);
        if (Math.abs(ofInteger(mOldText) - ofInteger(mNewText)) == 1) {
            //如果是增加，判断老数据的进位，如果是减少，判断新数据的进位
            b = carryBit(isMarquee ? mNewText : mOldText);
            length = isMarquee ? mOldText.length() : mNewText.length();
            //画不动的部分
            c = "";
            if (length - b > 0) {
                c = mOldText.subSequence(0, length - b);
                mPaint.setAlpha(255);
                canvas.drawText(c, 0, c.length(), tX, (height + textHeight) / 2, mPaint);
            }
            //老数据的透明度变化
            mPaint.setAlpha((int) (255 * (1 - mProgress)));
            //画老数据的部分数据，从length-b的位置开始，一直到最后
            canvas.drawText(mOldText, length - b, mOldText.length(), tX + mPaint.measureText(c.toString()), offsetOldY, mPaint);
            mPaint.setAlpha((int) (255 * mProgress));
            canvas.drawText(mNewText, length - b, mNewText.length(), tX + mPaint.measureText(c.toString()), offsetNewY, mPaint);
        } else {
            //老数据的透明度变化
            mPaint.setAlpha((int) (255 * (1 - mProgress)));
            //画老数据的部分数据，从length-b的位置开始，一直到最后
            canvas.drawText(mOldText, 0, mOldText.length(), tX, offsetOldY, mPaint);
            mPaint.setAlpha((int) (255 * mProgress));
            canvas.drawText(mNewText, 0, mNewText.length(), tX, offsetNewY, mPaint);
        }
    }

    /**
     * 判断进位，默认为1
     * 如果个位为9，说明进位，除以10以后，判断十位是否进位，递归
     */
    private int carryBit(CharSequence oldText) {
        if (ofInteger(oldText.toString()) % 10 == 9) {
            oldText = ofInteger(oldText.toString()) / 10 + "";
            bit++;
            return carryBit(oldText);
        }
        return bit;
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
}

