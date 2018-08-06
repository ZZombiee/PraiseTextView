package com.zombie.app.widget.praise.animator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zombie.app.R;
import com.zombie.app.utils.WidgetUtil;
import com.zombie.app.widget.BaseCustomView;

/**
 * 作者:郭生生
 **/
@SuppressLint("ResourceType")
public class DrawableView extends BaseCustomView {
    protected int imgWidth;
    protected int imgHeight;
    private int mOrientation;
    private int mScaleType;
    private Matrix mDrawableMatrix;
    protected String initText;
    protected Drawable mDrawable;
    protected int mTextSize;
    protected int mDrawablePadding;
    protected Paint mPaint;
    protected int mTextColor;
    protected ColorStateList mTextColorStateList;
    private Paint.FontMetrics mFontMetrics;
    protected float mTextHeight;
    private float mViewHeight;
    private int mSpannableColor = -1;
    private int mSpannableStart;
    private int mSpannableEnd;
    private boolean resetText = false;
    private boolean labelGravity;

    public DrawableView(Context context) {
        super(context);
    }

    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        if (mAttrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(mAttrs, R.styleable.DrawableView);
            imgWidth = (int) ta.getDimension(R.styleable.DrawableView_labelWidth, 0);
            imgHeight = (int) ta.getDimension(R.styleable.DrawableView_labelHeight, 0);
            mDrawable = ta.getDrawable(R.styleable.DrawableView_labelSource);
            mOrientation = ta.getInt(R.styleable.DrawableView_labelOrientation, 1);
            mScaleType = ta.getInt(R.styleable.DrawableView_labelType, 1);
            mTextSize = (int) ta.getDimension(R.styleable.DrawableView_labelTextSize, 25);
            mDrawablePadding = (int) ta.getDimension(R.styleable.DrawableView_labelPadding, 0);
            mTextColorStateList = ta.getColorStateList(R.styleable.DrawableView_labelTextColor);
            initText = ta.getString(R.styleable.DrawableView_labelInitText);
            labelGravity = ta.getBoolean(R.styleable.DrawableView_labelGravity, false);
            mTextColor = mTextColorStateList.getColorForState(getDrawableState(), getResources().getColor(R.color.text_black_666666));
            if (!isInEditMode()) {
                imgWidth = WidgetUtil.auto_width_px(mContext, imgWidth);
                imgHeight = WidgetUtil.auto_height_px(mContext, imgHeight);
                mTextSize = WidgetUtil.auto_width_px(mContext, mTextSize);
                mDrawablePadding = WidgetUtil.auto_width_px(mContext, mDrawablePadding);
            }
            ta.recycle();
        }
        mPaint = new Paint();
        initPaint();
        setDrwableBound();
        setDrwableMatrix();
        invalidate();
    }

    private void initPaint() {
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        mFontMetrics = mPaint.getFontMetrics();
        Rect rect = new Rect();
        mPaint.getTextBounds(initText, 0, initText.length(), rect);
        mTextHeight = -(rect.bottom + rect.top);
        mViewHeight = mFontMetrics.bottom - mFontMetrics.top;
    }

    private void setDrwableMatrix() {
        if (mDrawable != null) {
            matrixDrawable(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        }
    }

    private void matrixDrawable(int intrinsicWidth, int intrinsicHeight) {
        float scale;
        float dx = 0, dy = 0;
        if (mScaleType == 1) {
            if (intrinsicWidth <= imgWidth && intrinsicHeight <= imgHeight) {
                scale = 1.0f;
            } else {
                scale = Math.min(imgWidth / (float) intrinsicWidth, imgHeight / (float) intrinsicHeight);
            }

            dx = Math.round((imgWidth - intrinsicWidth * scale) * 0.5f);
            dy = Math.round((imgHeight - intrinsicHeight * scale) * 0.5f);
        } else {
            if (intrinsicWidth * imgHeight > imgWidth * intrinsicHeight) {
                scale = imgHeight / (float) intrinsicHeight;
                dx = (imgWidth - intrinsicWidth * scale) * 0.5f;
            } else {
                scale = imgWidth / (float) intrinsicWidth;
                dy = (imgHeight - intrinsicHeight * scale) * 0.5f;
            }
        }
        mDrawableMatrix = new Matrix();
        mDrawableMatrix.setScale(scale, scale);
        mDrawableMatrix.postTranslate(Math.round(dx), Math.round(dy));
    }

    private void setDrwableBound() {
        if (mDrawable != null) {
            mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        }
    }

    public void setText(String s) {
        initText = s;
        requestLayout();
    }

    public void setText(int s) {
        try {
            initText = getResources().getString(s);
        } catch (Exception e) {
            initText = s + "";
        } finally {
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        switch (mOrientation) {
            case 1:
                if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY)
                    width = imgWidth + mDrawablePadding + (int) mPaint.measureText(initText);
                if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY)
                    height = Math.max(imgHeight, (int) mViewHeight);
                break;
            case 2:
                if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY)
                    width = Math.max(imgWidth, (int) mPaint.measureText(initText));
                if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY)
                    height = imgHeight + mDrawablePadding + (int) mViewHeight;
                break;
            case 3:
                if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY)
                    width = imgWidth + mDrawablePadding + (int) mPaint.measureText(initText);
                if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY)
                    height = Math.max(imgHeight, (int) mViewHeight);
                break;
            case 4:
                if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY)
                    width = Math.max(imgWidth, (int) mPaint.measureText(initText));
                if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY)
                    height = imgHeight + mDrawablePadding + (int) mViewHeight;
                break;
        }
        setMeasuredDimension(width, height);
    }

    public void setLabelSourceValidate(@DrawableRes int res) {
        mDrawable = getResources().getDrawable(res);
        setDrwableBound();
        setDrwableMatrix();
        invalidate();
    }

    public DrawableView setLabelSource(@DrawableRes int res) {
        mDrawable = getResources().getDrawable(res);
        setDrwableBound();
        setDrwableMatrix();
        return this;
    }

    public DrawableView setTextColorSpannable(@ColorRes int color, int start, int end) {
        mSpannableColor = getResources().getColor(color);
        mSpannableStart = start;
        mSpannableEnd = end;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawable != null) {
            canvas.save();
            switch (mOrientation) {
                //左
                case 1:
                    canvas.translate(translateX(labelGravity ? (getMeasuredWidth() - imgWidth - mPaint.measureText(initText) - mDrawablePadding) / 2 : 0), translateY(imgHeight >= getMeasuredHeight() ?
                            0 : (getMeasuredHeight() - imgHeight) / 2));
                    canvas.concat(mDrawableMatrix);
                    drawLabel(canvas);
                    canvas.restore();
                    drawText(canvas);
                    break;
                //上
                case 2:
                    canvas.translate(translateX((getMeasuredWidth() - imgWidth) / 2), translateY(labelGravity ? (getMeasuredHeight() - imgHeight - mDrawablePadding - mViewHeight) / 2 : 0));
                    canvas.concat(mDrawableMatrix);
                    drawLabel(canvas);
                    canvas.restore();
                    drawText(canvas);
                    break;
                //右
                case 3:
                    canvas.translate(translateX(labelGravity ? (getMeasuredWidth() + mPaint.measureText(initText) + mDrawablePadding - 2 * imgWidth) / 2 : mPaint.measureText(initText) + mDrawablePadding), translateY((getMeasuredHeight() - imgHeight) / 2));
                    canvas.concat(mDrawableMatrix);
                    drawLabel(canvas);
                    canvas.restore();
                    drawText(canvas);
                    break;
                //下
                case 4:
                    canvas.translate(translateX((getMeasuredWidth() - imgWidth) / 2), translateY(labelGravity ? ((getMeasuredHeight() + mViewHeight + mDrawablePadding - imgHeight) / 2) : (mViewHeight + mDrawablePadding)));
                    canvas.concat(mDrawableMatrix);
                    drawLabel(canvas);
                    canvas.restore();
                    drawText(canvas);
                    break;
            }
        }
    }

    protected float mTranslateX;
    protected float mTranslateY;

    private float translateX(float x) {
        mTranslateX = x;
        return x;
    }

    private float translateY(float y) {
        mTranslateY = y;
        return y;
    }

    protected float getTranslateX() {
        return mTranslateX;
    }

    protected float getTranslateY() {
        return mTranslateY;
    }

    protected void drawLabel(Canvas canvas) {
        mDrawable.draw(canvas);
    }

    protected void drawText(Canvas canvas) {
        float height = 0;
        float width = 0;
        mPaint.setColor(mTextColor);
        if (mOrientation == 1 || mOrientation == 3) {
            width = labelGravity ? (getMeasuredWidth() + (mOrientation == 1 ? 1 : -1) * imgWidth + (mOrientation == 1 ? 1 : -1) * mDrawablePadding - mPaint.measureText(initText)) / 2 : (mOrientation == 1 ? (mDrawablePadding + imgWidth) : 0);
            height = (getMeasuredHeight() + mTextHeight) / 2;
        } else {
            width = (getMeasuredWidth() - mPaint.measureText(initText)) / 2;
            height = labelGravity ? (getMeasuredHeight() + (mOrientation == 2 ? 1 : -1) * imgHeight + (mOrientation == 2 ? 1 : -1) * mDrawablePadding + mViewHeight) / 2 : (mOrientation == 2 ? (mDrawablePadding + imgHeight + mTextHeight) : mViewHeight);
        }
        if (mSpannableColor != -1) {
            String spannableText = initText.substring(mSpannableStart, mSpannableEnd);
            String leftText = initText.substring(0, mSpannableStart);
            String rightText = initText.substring(mSpannableEnd, initText.length());
            canvas.drawText(leftText, width, height, mPaint);
            mPaint.setColor(mSpannableColor);
            canvas.drawText(spannableText, width + mPaint.measureText(leftText), height, mPaint);
            mPaint.setColor(mTextColor);
            canvas.drawText(rightText, width + mPaint.measureText(leftText + spannableText), height, mPaint);
        } else
            canvas.drawText(initText, width, height, mPaint);
        resetText = false;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setTextColor(ColorStateList colors) {
        mTextColorStateList = colors;
        mTextColor = colors.getColorForState(getDrawableState(), mTextColor);
        invalidate();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mDrawable.setState(onCreateDrawableState(0));
        mTextColor = mTextColorStateList.getColorForState(getDrawableState(), mTextColor);
        invalidate();
    }

    private void clearSpannableColor() {
        mSpannableColor = -1;
        mSpannableStart = 0;
        mSpannableEnd = 0;
    }
}

