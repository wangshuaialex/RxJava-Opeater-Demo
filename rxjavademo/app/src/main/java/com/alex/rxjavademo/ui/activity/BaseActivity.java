package com.alex.rxjavademo.ui.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.alex.rxjavademo.R;
import com.alex.rxjavademo.ui.fragment.CreateFragment;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alex on 2018/4/22.
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Unbinder unbinder;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View animationRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initView());
        unbinder = ButterKnife.bind(this);
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);
        //关联Toolbar与所需组件
        combinaToolBarAndNavigationView(obtainToolbarElement());
        //动画所需展示的View
        animationRootView = getAnimationRootView();
        //初始化Fragment
        initFragment();
    }

    private void combinaToolBarAndNavigationView(Object[] elements) {
        DrawerLayout dlDrawer = (DrawerLayout) elements[0];
        Toolbar toolbar = (Toolbar) elements[1];
        NavigationView nvMenu = (NavigationView) elements[2];
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, dlDrawer,toolbar , R.string.drawer_open, R.string.drawer_close);
        dlDrawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        nvMenu.setItemIconTintList(null);
        nvMenu.setNavigationItemSelectedListener(this);
    }

    protected abstract Object[] obtainToolbarElement();


    public abstract int initView();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment fragment = null;
        @SuppressLint({"NewApi", "LocalSuppress"})
        Animator animator = ViewAnimationUtils.createCircularReveal(
                animationRootView,
                0,
                0,
                0,
                animationRootView.getWidth()
        );
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
        //item条目切换所需要执行的动作
        fragment = switchItem(item, fragment);
        mFragmentTransaction.replace(R.id.fl_container, fragment);
        mFragmentTransaction.commit();
        return false;
    }

    protected abstract Fragment switchItem(MenuItem item, Fragment fragment);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

   /* public void combinaToolBarAndNavigationView(DrawerLayout dlDrawer, Toolbar tbHead, NavigationView nvMenu) {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, dlDrawer, tbHead, R.string.drawer_open, R.string.drawer_close);
        dlDrawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        nvMenu.setItemIconTintList(null);
        nvMenu.setNavigationItemSelectedListener(this);
    }*/

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        CreateFragment createFragment = new CreateFragment();
        mFragmentTransaction.replace(R.id.fl_container, createFragment);
        mFragmentTransaction.commit();
    }

    /**
     *
     * @return 获取动画所需展示的View控件
     */
    public abstract View getAnimationRootView() ;
}
