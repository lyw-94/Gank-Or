package com.sdust.gankor.utils;

import android.content.Context;
import android.text.TextUtils;

import com.sdust.gankor.GankOrApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Liu Yongwei on 2016/10/31.
 * <p>
 * version : 1.0
 */

public class CacheUtil {

    private ACache mCache;
    private Context mContext;

    private CacheUtil() {
        mContext = GankOrApplication.context;
        mCache = ACache.get(mContext);
    }

    private static class CacheUtilHolder {
        private static final CacheUtil instance = new CacheUtil();
    }

    public static CacheUtil getInstance() {
        return CacheUtilHolder.instance;
    }

    public String json2String(String json) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new StringReader(json));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            return readString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断缓存是否为空
     * @param key
     * @return
     */
    public boolean isCacheEmpty(String key) {
        return TextUtils.isEmpty(mCache.getAsString(key));
    }

    /**
     * 判断是否是未缓存过的
     * @param key
     * @param newJson
     * @return
     */
    public boolean isNewResponse(String key, String newJson) {
        return (!TextUtils.isEmpty(mCache.getAsString(key)) &&
                !mCache.getAsString(key).equals(json2String(newJson))) || TextUtils.isEmpty(mCache
                .getAsString(key));
    }

    public String getAsString(String key) {
        return mCache.getAsString(key);
    }

    public void put(String key, String value) {
        mCache.put(key, value);
    }
}
