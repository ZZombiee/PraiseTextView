package com.zombie.app.widget.praise_old.animator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.zombie.app.R;

/**
 * Created by Gss on 2018/5/22 0022.
 */

public class PraiseDrawable extends AnimatorDrawable {
    private Drawable mShinDrawable;
    private Context mContext;

    public PraiseDrawable(Drawable mDrawable, Context mContext) {
        super(mDrawable);
        this.mContext = mContext;
        mShinDrawable = mContext.getResources().getDrawable(R.mipmap.ic_messages_like_selected_shining);
    }

    @Override
    public void animatorCheck(Canvas canvas, float progress) {
        //重置画笔
        mPaint.reset();
        //将此时画布状态保存，因为只对图片资源的动画做操作
        canvas.save();
        //选中的状态
        mDrawable.setLevel(2);
        //得到图片资源的宽高和画布的高度，以便画的时候居中
        int centerX = getBitmap().getWidth() / 2;
        int centerY = getBitmap().getHeight() / 2;
        int canvasHeight = canvas.getHeight();
        //将画布下移
        canvas.translate(0, (canvasHeight - centerY * 2) / 2);
        //放大1~1.2倍
        canvas.scale((float) 0.2 * progress + 1, (float) 0.2 * progress + 1, centerX, centerY);
        mDrawable.draw(canvas);
        canvas.restore();
        canvas.save();
        //小点点做一个透明度的变化，实际上即刻应该还有缩放变化，这里简单透明度处理一下
        mShinDrawable.setAlpha((int) (255 * progress));
        //移到大致大拇指的位置
        canvas.translate(centerX / 2 - 4, canvasHeight / 2 - centerY * 2);
        //这里一定要设置，否则画不出小点点的那个图片
        mShinDrawable.setBounds(0, 0, mShinDrawable.getIntrinsicWidth(), mShinDrawable.getIntrinsicHeight());
        mShinDrawable.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.translate(0, (canvas.getHeight() - centerY * 2) / 2);
        //设置画笔的着色器，自内向外辐射，颜色从透明到橙色
        Shader shader = new RadialGradient(centerX, centerY, (float) (Math.max(centerX, centerY) * progress * 1.2 + 1), Color.parseColor("#00000000"), Color.parseColor("#e58f00"), Shader.TileMode.CLAMP);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setShader(shader);
        //这里是透明度的峰值变化，一开始是透明的，到进度的一半达到完全不透明，然后结束的时候透明
        mPaint.setAlpha((int) ((0.5 - Math.abs(0.5 - progress)) * 510));
        canvas.drawCircle(centerX, centerY, progress * Math.max(centerX, centerY) + centerX / 2, mPaint);
        canvas.restore();
    }

    @Override
    public void animatorUnCheck(Canvas canvas, float progress) {
        //非点赞效果类似于点赞，不过我处理的就简单很多，只是从1.2~1倍进行缩小
        mDrawable.setLevel(1);
        mPaint.reset();
        canvas.save();
        int centerY = getBitmap().getHeight() / 2;
        int canvasHeight = canvas.getHeight();
        canvas.translate(0, (canvasHeight - centerY * 2) / 2);
        canvas.scale((float) (-0.2 * progress + 1.2), (float) (-0.2 * progress + 1.2), (float) getBitmap().getWidth() / 2, (float) getBitmap().getWidth() / 2);
        mDrawable.draw(canvas);
        canvas.restore();
    }
}
