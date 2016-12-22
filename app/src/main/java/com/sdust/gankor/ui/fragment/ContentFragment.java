package com.sdust.gankor.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sdust.gankor.R;
import com.sdust.gankor.ui.BaseFragment;
import com.sdust.gankor.utils.CacheUtil;
import com.sdust.gankor.utils.LogUtils;
import com.sdust.gankor.utils.OkHttpUtil;
import com.sdust.gankor.utils.ToastUtils;
import com.sdust.gankor.view.PullLoadRecyclerView;

/**
 * Created by Liu Yongwei on 2016/11/13.
 * <p> 懒加载fragment
 * version : 1.0
 */

public abstract class ContentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    /** 数据是否加载完毕 */
    protected boolean isDataLoaded;
    /** 视图是否加载完成 */
    protected boolean isViewCreated;

    protected OkHttpUtil mOkUtil;
    protected CacheUtil mCache;
    protected Gson mGson;
    protected PullLoadRecyclerView mContentRecyclerView;
    protected SwipeRefreshLayout mContentSwipeRefreshLayout;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x122:
                    mContentSwipeRefreshLayout.setRefreshing(false);
                    ToastUtils.show("网络没有连接哦");
                    break;
                case 0x121:
//                    下拉刷新
                    loadDataFromNet(getFirstPageUrl());
                default:
                    break;
            }
        }
    };

    public ContentFragment() {

    }

    /**
     * 获取最初的 url （刷新或者第一次加载时的 url）
     * @return
     */
    protected abstract String getFirstPageUrl();

    protected boolean isFirstPage(String url) {
        return getFirstPageUrl().equals(url);
    }

    /**
     * 1. 缓存为空时第一次加载缓存 或者刷新
     * 2. 上拉加载更多
     *
     * @param url 前者使用 getFirstPageUrl() 后者需自己传入 是固定值
     */
    protected abstract void loadDataFromNet(String url);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState)
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        mContentRecyclerView = (PullLoadRecyclerView) view.findViewById(R.id.rv_content);
        mContentSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_content);

        SpacesItemDecoration decoration = new SpacesItemDecoration((int) (Math.random() * 5 + 15));
        mContentRecyclerView.addItemDecoration(decoration);

        initView(inflater, container);
        initSwipeRefreshLayout();

        isViewCreated = true;

        return view;
    }

    protected abstract void initView(LayoutInflater inflater, ViewGroup container);

    protected void initSwipeRefreshLayout() {
        mContentSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android
                .R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color
                .holo_red_dark);
        mContentSwipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isDataLoaded && isViewCreated) {
            // ViewPager 其他页面的 fragment，我们进行判断后再加载数据
            initData();
        }
    }

    /**
     * 注意CallSuper注解
     */
    @CallSuper
    protected void initData() {
        isDataLoaded = true;
        mOkUtil = mOkUtil.getInstance();
        mCache = CacheUtil.getInstance();
        mGson = new Gson();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            initData();
        }
    }

    public boolean isNetConnected() {
        return mActivity.isNetConnect();
    }


    @Override
    public void onRefresh() {
        if (isNetConnected()) {
            mHandler.sendEmptyMessage(0x121);
        } else {
            mHandler.sendEmptyMessage(0x122);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e(getClass().getSimpleName(), " onStop");
        mOkUtil.cancleAll();
        if (mContentSwipeRefreshLayout.isRefreshing()) {
            mContentSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(getClass().getSimpleName(), " onDestroy");
        mActivity = null;
    }
}
