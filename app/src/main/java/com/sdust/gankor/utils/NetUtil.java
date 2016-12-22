package com.sdust.gankor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sdust.gankor.GankOrApplication;

/**
 * Created by Liu Yongwei on 2016/10/31.
 * <p>
 * version : 1.0
 */

public class NetUtil {

    /**
     * 判断网络是否连接
     * @return
     */
    public static boolean isNetAvaliable() {
        ConnectivityManager cm = (ConnectivityManager) GankOrApplication.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null && info.isAvailable();
    }
}
