package com.alex.rxjavademo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alex.rxjavademo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alex on 2018/4/22.
 */

public class BooleanFragment extends BaseFragment {
    @BindView(R.id.btn_all)
    Button btnAll;
    @BindView(R.id.btn_contains)
    Button btnContains;
    @BindView(R.id.btn_isEmpty)
    Button btnIsEmpty;
    @BindView(R.id.btn_exists)
    Button btnExists;
    @BindView(R.id.btn_sequenceEqual)
    Button btnSequenceEqual;
    Unbinder unbinder;

    @Override
    public int initView() {
        return R.layout.fragment_boolean;
    }

    @OnClick({R.id.btn_all, R.id.btn_contains, R.id.btn_isEmpty, R.id.btn_exists, R.id.btn_sequenceEqual})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //all操作符，对源Observable发送的每一个数据根据给定的条件进行判断
            //如果全部符合条件，返回true，否则返回false
            case R.id.btn_all:
                Observable.just(1, 2, 3, 4).all(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer num) {
                        return num > 3;
                    }
                }).subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
            //contains操作符，对源Observable发送的数据是否包含定义的选项进行判断
            //如果包含返回true，否则返回false
            case R.id.btn_contains:
                Observable.just(1, 2, 3, 4).contains(2).subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
            //isEmpty操作符，对源Observable发送的数据是否为空进行判断
            //如果源Observable发送的数据为空返回true，否则返回false
            case R.id.btn_isEmpty:
                Observable.just(1, 2, 3, 4).isEmpty().subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
            //exists操作符，对源Observable发送的单独一个数据根据给定的条件进行判断
            //如果有一个数据符合条件，返回true，否则返回false
            case R.id.btn_exists:
                Observable.just(1, 2, 3, 4).exists(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer num) {
                        return num > 3;
                    }
                }).subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
            //sequenceEqual操作符，对两个Observable进行判断，两个Observable相同时返回true，否则返回false
            //这里包含两个Observable的数据，发射顺序，终止状态是否相同
            case R.id.btn_sequenceEqual:
                Observable.sequenceEqual(Observable.just(1, 2, 3, 4), Observable.just(1)).subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
        }
    }
}
