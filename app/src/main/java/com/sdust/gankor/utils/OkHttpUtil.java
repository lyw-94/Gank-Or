package com.sdust.gankor.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liu Yongwei on 2016/10/31.
 * <p>
 * version : 1.0
 */

public class OkHttpUtil {

    private OkHttpClient mOkHttpClient;
    private Call mCall;
    private Gson mGson;
    private Handler mHandler;

    private OkHttpUtil() {
        mOkHttpClient = new OkHttpClient();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    private static class OkHttpUtilHolder {
        private static final OkHttpUtil instance = new OkHttpUtil();
    }

    public static OkHttpUtil getInstance() {
        return OkHttpUtilHolder.instance;
    }

    /**
     * 获取知乎Json
     *
     * @param url
     * @param callback
     */
    public void OkHttpZhihuJson(String url, final ResultCallback callback) {
        Request request = new Request.Builder()
                .url(API.ZHIHU_BASIC_URL + url)
                .build();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                final Object o = mGson.fromJson(body, callback.mType);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 为什么这个方法要在子线程中调用
                        callback.onResponse(o, body);
                    }
                });
            }
        });
    }


    /**
     * 获取知乎JObject
     *
     * @param url
     * @param object
     * @param callback
     */
    public void okHttpZhihuJObject(String url, final String object, final JObjectCallback callback) {
        final Request request = new Request.Builder()
                .url(API.ZHIHU_BASIC_URL + url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final String string = jsonObject.getString(object);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(call, string);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取消全部网络请求
     */
    public void cancleAll() {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    public interface JObjectCallback {
        void onFailure(Call call, IOException e);

        void onResponse(Call call, String ObjectUrl);
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            //得到父类的Type
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;

            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Call call, Exception e);

        public abstract void onResponse(T response, String json);
    }

}
