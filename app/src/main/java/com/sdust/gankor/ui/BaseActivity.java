package com.sdust.gankor.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sdust.gankor.utils.LogUtils;
import com.sdust.gankor.utils.NetUtil;

/**
 * Created by Liu Yongwei on 2016/10/30.
 * <p>
 * version : 1.0
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(getClass().getSimpleName(), "onCreate");
        setActivityState();
        initView();
        initData();
    }

    public void setActivityState() {
//        设置 APP 只能竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected abstract void initView();

    protected abstract void initData();

    public boolean isNetConnect() {
        return NetUtil.isNetAvaliable(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e(getClass().getSimpleName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(getClass().getSimpleName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(getClass().getSimpleName(), "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(getClass().getSimpleName(), "onDestroy");
    }
}
