package com.sdust.gankor.utils;

import android.content.Context;
import android.widget.Toast;

import com.sdust.gankor.GankOrApplication;

/**
 * Created by Liu Yongwei on 2016/10/30.
 * <p>
 * version : 1.0
 */

public class ToastUtils {
    private static Context mContext = GankOrApplication.context;
    private static Toast mToast;

    public static void show(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
