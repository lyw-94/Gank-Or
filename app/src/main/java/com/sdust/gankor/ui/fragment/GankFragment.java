package com.sdust.gankor.ui.fragment;


import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdust.gankor.adapter.GankRecyclerAdapter;
import com.sdust.gankor.model.GankWelfare;
import com.sdust.gankor.utils.API;
import com.sdust.gankor.utils.CommonUtils;
import com.sdust.gankor.utils.OkHttpUtil;
import com.sdust.gankor.utils.ToastUtils;
import com.sdust.gankor.view.PullLoadRecyclerView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.internal.Util;

import static android.R.attr.data;

/**
 * Created by Liu Yongwei on 2016/11/19.
 * <p>
 * version : 1.0
 */

public class GankFragment extends ContentFragment implements GankRecyclerAdapter.ImageViewListener, GankRecyclerAdapter.TextViewListener {

    public final static String GANK_WELFARE_JSON = "gank_welfare_json";
    public final static String GANK_VIDEO_JSON = "gank_video_json";
    public GankRecyclerAdapter mAdapter;
    public StaggeredGridLayoutManager manager;
    private List<GankWelfare.ResultsBean> mWelfare;
    private HashMap<GankWelfare.ResultsBean, GankWelfare.ResultsBean> dataMap;
    private int page = API.GANK_FIRST_PAGE;
    private GankRecyclerAdapter.TextViewListener mTextListener;
    private GankRecyclerAdapter.ImageViewListener mImageListener;

    public GankFragment() {
    }

    @Override
    protected String getFirstPageUrl() {
        return page + "";
    }

    @Override
    protected void loadDataFromNet(final String url) {
        mContentSwipeRefreshLayout.setRefreshing(true);
        //        Gank 福利图片
        mOkUtil.okHttpGankGson(API.GANK_WELFARE + url, new OkHttpUtil
                .ResultCallback<GankWelfare>() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(GankWelfare response, String json) {
                if (response != null && !response.isError()) {
                    if (isFirstPage(url)) {
                        mCache.put(GANK_WELFARE_JSON, json);
                    }
                    mWelfare = response.getResults();
                    loadGankVideo(url);
                }
            }
        });
    }

    @Override
    protected void initView(LayoutInflater inflater, ViewGroup container) {
        dataMap = new LinkedHashMap<GankWelfare.ResultsBean, GankWelfare.ResultsBean>();
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mContentRecyclerView.setLayoutManager(manager);
        mAdapter = new GankRecyclerAdapter(mContentRecyclerView.getContext(), dataMap);
        mAdapter.setImageListener(this);
        mAdapter.setTextListener(this);
        mContentRecyclerView.setAdapter(mAdapter);
        mContentRecyclerView.setPullLoadListener(new PullLoadRecyclerView.onPullLoadListener() {
            @Override
            public void onPullLoad() {
                loadDataFromNet(++page + "");
            }
        });
    }

    public void initUrl() {
        page = API.GANK_FIRST_PAGE;
    }

    @Override
    protected void initData() {
        super.initData();
//        缓存不为空时直接加载缓存，否则在联网情况下加载数据
        if (!mCache.isCacheEmpty(GANK_WELFARE_JSON) && !mCache.isCacheEmpty(GANK_VIDEO_JSON)) {
            // 取出缓存
            List<GankWelfare.ResultsBean> welfare = mGson.fromJson(mCache.getAsString(GANK_WELFARE_JSON), GankWelfare.class).getResults();
            List<GankWelfare.ResultsBean> video = mGson.fromJson(mCache.getAsString(GANK_VIDEO_JSON),
                    GankWelfare
                            .class).getResults();
            for (int i = 0; i < mWelfare.size(); i++) {
                dataMap.put(welfare.get(i), video.get(i));
            }
            mAdapter.addDataMap(dataMap);
        } else {
            if (isNetConnected()) {
                // 缓存为空联网加载
                loadDataFromNet(getFirstPageUrl());
            } else {
                ToastUtils.show("网络未连接");
            }
        }
    }

    private void loadGankVideo(final String url) {
        //        Gank 休息视频
        mOkUtil.okHttpGankGson(API.GANK_VIDEO + url, new OkHttpUtil.ResultCallback<GankWelfare>
                () {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(GankWelfare response, String json) {
                if (response != null && !response.isError()) {
                    if (isFirstPage(url)) {
                        mAdapter.clearList();
                        mCache.put(GANK_VIDEO_JSON, json);
                        initUrl();
                    }

                    List<GankWelfare.ResultsBean> video = response.getResults();

                    dataMap.clear();
                    for (int i = 0; i < video.size(); i++) {
                        dataMap.put(mWelfare.get(i), video.get(i));
                    }
                    mAdapter.addDataMap(dataMap);
                }
                mContentSwipeRefreshLayout.setRefreshing(false);
                mContentRecyclerView.setIsLoading(false);
            }
        });
    }

    public void setTextListener(GankRecyclerAdapter.TextViewListener textListener) {
        mTextListener = textListener;
    }

    public void setImageListener(GankRecyclerAdapter.ImageViewListener imageListener) {
        mImageListener = imageListener;
    }

    @Override
    public void onGankImageClick(View image, String url, String desc) {
        if (mImageListener != null) {
            mImageListener.onGankImageClick(image, url, desc);
        }
    }

    @Override
    public void onGankTextClick(String url) {
        if (mTextListener != null) {
            mTextListener.onGankTextClick(url);
        }
    }
}
