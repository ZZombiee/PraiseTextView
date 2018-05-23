package com.zombie.app.utils;

import android.text.TextUtils;

/**
 * Created by Gss on 2017/11/22 0022.
 */

public class NumUtil {
    public static Long ofLong(String s) {
        try {
            if (TextUtils.isEmpty(s))
                return 0L;
            return Long.valueOf(s);
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Integer ofInteger(String s) {
        try {
            if (TextUtils.isEmpty(s))
                return 0;
            return Integer.valueOf(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int addOne(int n) {
        return n + 1;
    }

    public static int reduce(int n) {
        return n - 1;
    }
}
