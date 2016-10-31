package com.sdust.gankor.utils;

import android.util.Log;

/**
 * Created by Liu Yongwei on 2016/10/30.
 * <p>
 * version : 1.0
 */

public class LogUtils {

    private static boolean printLog = true;

    public static void e(String tag, String msg) {
        if (printLog) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (printLog) {
            Log.e("Test", msg);
        }
    }

    public static void d(String tag, String msg) {
        if (printLog) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (printLog) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        Log.e(tag, msg);
    }
}
