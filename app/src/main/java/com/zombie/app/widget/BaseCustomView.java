package com.zombie.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者:郭生生
 **/
public abstract class BaseCustomView extends View {
    protected Context mContext;
    protected AttributeSet mAttrs;

    public BaseCustomView(Context context) {
        this(context, null);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    protected abstract void init();
}

