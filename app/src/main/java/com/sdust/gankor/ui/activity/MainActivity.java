package com.sdust.gankor.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sdust.gankor.R;
import com.sdust.gankor.ui.BaseActivity;
import com.sdust.gankor.ui.fragment.MainFragment;
import com.sdust.gankor.utils.CacheUtil;
import com.sdust.gankor.utils.ImageUtil;
import com.sdust.gankor.utils.ToastUtils;

import static u.aly.au.B;

public class MainActivity extends BaseActivity {

    private MainFragment mContentGank;
    private MainFragment mContentZhihu;
    private Toolbar mTitleToolbar;
    private TabLayout mTitleTabLayout;
    private NavigationView mContentNavigationView;
    private DrawerLayout mMainDrawerLayout;
    private long firstTime;
    private int mLastItemId;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mTitleToolbar = (Toolbar) findViewById(R.id.tb_title);
        mTitleTabLayout = (TabLayout) findViewById(R.id.tl_title);
        mContentNavigationView = (NavigationView) findViewById(R.id.nv_content);
        mMainDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);

        // 设置导航栏顶部图片
        View view = mContentNavigationView.getHeaderView(0);
        ImageView header = (ImageView) view.findViewById(R.id.nav_head);
        ImageUtil.getInstance().displayImage(CacheUtil.getInstance().getAsString(SplashActivity.IMG), header);

        // 设置Toolbar
        setSupportActionBar(mTitleToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mMainDrawerLayout, mTitleToolbar, R.string.meizhi, R.string.meizhi);
        mMainDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void initData() {
        setUpDrawerContent();
        mContentNavigationView.setCheckedItem(0);
    }

    private void setUpDrawerContent() {
        mLastItemId = mContentNavigationView.getMenu().getItem(0).getItemId();
        changeFragments(mLastItemId);
        mContentNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
               mMainDrawerLayout.closeDrawers();
                if (item.getItemId() == R.id.menu_introduce) {
                    startActivity(new Intent(MainActivity.this, AboutMeActivity.class));
                    item.setCheckable(true);
                } else {
                    if (mLastItemId != item.getItemId()) {
                        item.setCheckable(true);
                        changeFragments(item.getItemId());
                        mLastItemId = item.getItemId();
                    }
                }
                return true;
            }
        });
    }

    private void changeFragments(int itemId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAll(transaction);
        switch (itemId) {
            case R.id.nav_knowledge:
                if (mContentZhihu != null) {
                    transaction.show(mContentZhihu);
                } else {
                    mContentZhihu = MainFragment.newInstance(MainFragment.MENU_ZHIHU);
                    mContentZhihu.setOnBannerClickListener(this);
                    mContentZhihu.setOnDailyItemClickListener(this);
                    mContentZhihu.setOnHotItemClickListener(this);
                    transaction.add(R.id.fl_content, mContentZhihu);
                }
                initToolbar(MainFragment.MENU_ZHIHU);
                break;
            case R.id.nav_beauty:
                if (mContentGank != null) {
                    transaction.show(mContentGank);
                } else {
                    mContentGank = MainFragment.newInstance(MainFragment.MENU_GANK);
                    mContentGank.setTextListener(this);
                    mContentGank.setImageListener(this);
                    transaction.add(R.id.fl_content, mContentGank);
                }
                initToolbar(MainFragment.MENU_GANK);
                break;
        }
    }


    /**
     * 隐藏所有Fragment
     * @param transaction
     */
    private void hideAll(FragmentTransaction transaction) {
        if (mContentZhihu != null) {
            transaction.hide(mContentZhihu);
        }
        if (mContentGank != null) {
            transaction.hide(mContentGank);
        }
    }

    /**
     * 暴露给 fragment 连接 tabLayout
     * @param viewPager
     */
    public void setupViewPager(ViewPager viewPager) {
        mTitleTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTitleTabLayout.setupWithViewPager(viewPager);
    }

    public void initToolbar(String args) {
        if (args.equals(MainFragment.MENU_GANK)) {
            hideTabLayout(true);
            setToolbarTitle("妹纸");
        } else {
            hideTabLayout(false);
            setToolbarTitle("知乎");
        }
    }

    public void hideTabLayout(boolean hide) {
        if (hide) {
            mTitleTabLayout.setVisibility(View.GONE);
        } else {
            mTitleTabLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    long lastClickTime;
    @Override
    public void onBackPressed() {
        if (mContentNavigationView.isShown()) {
            mMainDrawerLayout.closeDrawers();
            return;
        }
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime >= 2000) {
            ToastUtils.show("再按一次退出");
            Snackbar snackBar = Snackbar.make(mContentNavigationView, "再按一次退出", Snackbar.LENGTH_SHORT);
            snackBar.getView().setBackgroundColor(getResources().getColor(R.color.red_300));
            lastClickTime = currentClickTime;
        } else {
            super.onBackPressed();
        }
    }
}
