package com.alex.rxjavademo.ui.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.alex.rxjavademo.R;
import com.alex.rxjavademo.ui.fragment.BooleanFragment;
import com.alex.rxjavademo.ui.fragment.CombinationFragment;
import com.alex.rxjavademo.ui.fragment.ConditionFragment;
import com.alex.rxjavademo.ui.fragment.ConversionFragment;
import com.alex.rxjavademo.ui.fragment.CreateFragment;
import com.alex.rxjavademo.ui.fragment.ErrorFragment;
import com.alex.rxjavademo.ui.fragment.FilterFragment;
import com.alex.rxjavademo.ui.fragment.SupportFragment;
import com.alex.rxjavademo.ui.fragment.TransformFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tb_head)
    Toolbar tbHead;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.nv_menu)
    NavigationView nvMenu;
    @BindView(R.id.dl_drawer)
    DrawerLayout dlDrawer;

    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    protected Fragment switchItem(MenuItem item, Fragment fragment) {
        switch (item.getItemId()) {
            //创建操作符
            case R.id.item_create:
                fragment = new CreateFragment();
                break;
            //变换操作符
            case R.id.item_transform:
                fragment = new TransformFragment();
                break;
            //过滤操作符
            case R.id.item_filter:
                fragment = new FilterFragment();
                break;
            //组合操作符
            case R.id.item_combina:
                fragment = new CombinationFragment();
                break;
            //辅助操作符
            case R.id.item_support:
                fragment = new SupportFragment();
                break;
            //错误操作符
            case R.id.item_error:
                fragment = new ErrorFragment();
                break;
            //布尔操作符
            case R.id.item_boolean:
                fragment = new BooleanFragment();
                break;
            //条件操作符
            case R.id.item_condition:
                fragment = new ConditionFragment();
                break;
            //转换操作符
            case R.id.item_convertion:
                fragment = new ConversionFragment();
                break;
        }
        dlDrawer.closeDrawers();
        return fragment;
    }

    @Override
    public View getAnimationRootView() {
        return flContainer;
    }

    @Override
    protected Object[] obtainToolbarElement() {
        return new Object[]{dlDrawer, tbHead, nvMenu};
    }
}
