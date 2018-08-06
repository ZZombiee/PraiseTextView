package com.zombie.app.utils;

import android.content.Context;

/**
 * 作者:郭生生
 **/
public class WidgetUtil {
    public static int auto_height_px(Context context, float px) {
        if (getScreenHeight(context) / getScreenWidth(context) >= 2)
            return auto_width_px(context, px);
        return (int) (context.getResources().getDisplayMetrics().heightPixels * px / 1920);
    }

    private static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    private static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int auto_width_px(Context context, float px) {
        return (int) (context.getResources().getDisplayMetrics().widthPixels * px / 1080);
    }
}

