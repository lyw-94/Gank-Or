package com.sdust.gankor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdust.gankor.R;
import com.sdust.gankor.adapter.GankRecyclerAdapter;
import com.sdust.gankor.adapter.MainAdapter;
import com.sdust.gankor.ui.BaseFragment;
import com.sdust.gankor.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu Yongwei on 2016/11/10.
 * <p>
 * version : 1.0
 */

public class MainFragment extends BaseFragment implements GankRecyclerAdapter.ImageViewListener,
        GankRecyclerAdapter.TextViewListener {
    public final static String MENU_GANK = "menu_gank";
    public final static String MENU_ZHIHU = "menu_zhihu";
    public final static String MENU_ID = "menu_id";
    public GankFragment mGankFragment;
    public ZhihuDailyNewsFragment mDailyNewsFragment;
    public ZhihuHotNewsFragment mHotNewsFragment;
    private MainAdapter adapter;
    private ViewPager mContentViewPager;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();


    private GankRecyclerAdapter.TextViewListener mTextListener;
    private GankRecyclerAdapter.ImageViewListener mImageListener;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String menuId) {
        Bundle args = new Bundle();
        args.putString(MENU_ID, menuId);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(args);
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContentViewPager = (ViewPager) view.findViewById(R.id.vp_content);
        adapter = new MainAdapter(getChildFragmentManager(), mFragments, mTitles);
        initFragments();

        return view;
    }

    public void initFragments() {
        String args = getArguments().getString(MENU_ID);

        if (MENU_GANK.equals(args)) {
            mGankFragment = new GankFragment();
            //  mGankFragment.setImageListener(this);
            //  mGankFragment.setTextListener(this);

            mFragments.add(mGankFragment);
            mTitles.add("妹纸");

            adapter.changeDataList(mTitles, mFragments);
        } else {
            mDailyNewsFragment = new ZhihuDailyNewsFragment();
            //  mDailyNewsFragment.setOnItemClickListener(this);
            //  mDailyNewsFragment.setOnBannerClickListener(this);
            mHotNewsFragment = new ZhihuHotNewsFragment();
            //  mHotNewsFragment.setOnItemClickListener(this);

            mFragments.add(mDailyNewsFragment);
            mFragments.add(mHotNewsFragment);
            mTitles.add("知乎日报");
            mTitles.add("热门消息");

            adapter.changeDataList(mTitles, mFragments);
        }
        mContentViewPager.setAdapter(adapter);
        // 调用 MainActivity 的 setupViewPager() 方法
        ((MainActivity) mActivity).setupViewPager(mContentViewPager);
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
