package com.alex.rxjavademo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alex.rxjavademo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Alex on 2018/4/22.
 */

public class ConditionFragment extends BaseFragment {
    @BindView(R.id.btn_amb)
    Button btnAmb;
    @BindView(R.id.btn_defaultIfEmpty)
    Button btnDefaultIfEmpty;
    Unbinder unbinder;

    @Override
    public int initView() {
        return R.layout.fragment_condition;
    }

    @OnClick({R.id.btn_amb, R.id.btn_defaultIfEmpty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //给定多个Observable，只让第一个发送数据的Observable发送数据
            case R.id.btn_amb:
                Observable
                        .amb(Observable.range(0,3).delay(2000, TimeUnit.MILLISECONDS),Observable.range(100,3))
                        .subscribe(onNextAction);
                break;
                //如果源Observable没有发送数据，则发送一个默认数据
            case R.id.btn_defaultIfEmpty:
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onCompleted();
                    }
                }).defaultIfEmpty(404).subscribe(onNextAction,onErrorAction,onCompletedAction);
                break;
        }
    }
}
