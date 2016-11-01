package com.sdust.gankor.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.sdust.gankor.R;
import com.sdust.gankor.ui.BaseActivity;
import com.sdust.gankor.utils.API;
import com.sdust.gankor.utils.CacheUtil;
import com.sdust.gankor.utils.ImageUtil;
import com.sdust.gankor.utils.OkHttpUtil;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Liu Yongwei on 2016/11/1.
 * <p>
 * version : 1.0
 */

public class SplashActivity extends BaseActivity {

    public static final String IMG = "img";
    private CacheUtil mCache;
    private ImageView mSplashImgView;

    @Override
    protected void initView() {
        // 全屏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_splash);
        mSplashImgView = (ImageView) findViewById(R.id.iv_splash);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void initData() {
        mCache = CacheUtil.getInstance();

        // 缓存不为空加载缓存
        if (!mCache.isCacheEmpty(IMG)) {
            ImageUtil.getInstance().displayImage(mCache.getAsString(IMG), mSplashImgView);
        }

        OkHttpUtil.getInstance().okHttpZhihuJObject(API.ZHIHU_START, IMG, new OkHttpUtil.JObjectCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (mCache.isCacheEmpty(IMG)) {
                    startToMainActivity();
                }
            }

            @Override
            public void onResponse(Call call, String JObjectUrl) {
                if (JObjectUrl != null) {
                    if (mCache.isCacheEmpty(IMG)) {
                        ImageUtil.getInstance().displayImage(mCache.getAsString(IMG), mSplashImgView);
                    }
                    mCache.put(IMG, JObjectUrl);
                } else {
                    if (mCache.isCacheEmpty(IMG)) {
                        startToMainActivity();
                    }
                }
            }
        });

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startToMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
