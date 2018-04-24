package com.alex.rxjavademo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alex.rxjavademo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex on 2018/4/11.
 */

public class CreateFragment extends BaseFragment {
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_just)
    Button btnJust;
    @BindView(R.id.btn_from)
    Button btnFrom;
    Unbinder unbinder;
    @BindView(R.id.btn_interval)
    Button btnInterval;
    @BindView(R.id.btn_range)
    Button btnRange;
    @BindView(R.id.btn_repeat)
    Button btnRepeat;

    @Override
    public int initView() {
        return R.layout.fragment_create;
    }

    @OnClick({R.id.btn_create, R.id.btn_just, R.id.btn_from, R.id.btn_interval, R.id.btn_range, R.id.btn_repeat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //创建的基础用法（create操作符）
            case R.id.btn_create:
                //被观察者
                Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Alex");
                        subscriber.onNext("Payne");
                        subscriber.onCompleted();
                    }
                });
                //被观察者和观察者建立订阅关系
                observable.subscribe(subscriber);
                break;
            //just操作符，创建将逐个内容进行发送的Observable，其内部发送内容在内部以from的操作符的方式进行转换
            case R.id.btn_just:
                Observable.just("Alex", "Payne").subscribe(onNextAction);
                break;
            //from操作符，创建以数组内容发送事件的Observable
            case R.id.btn_from:
                String[] observableArr = new String[]{"Alex", "Payne"};
                //onNextAction、onErrorAction提取到父类中
                Observable.from(observableArr).subscribe(onNextAction, onErrorAction);
                break;
            //interval操作符,创建以1秒为事件间隔发送整数序列的Observable
            case R.id.btn_interval:
                Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(onNextAction);
                break;
            //range操作符，创建以发送范围内的整数序列的Observable
            case R.id.btn_range:
                Observable.range(0, 3).subscribe(onNextAction);
                break;
            //repeat操作符,创建一个以N次重复发送数据的Observable
            case R.id.btn_repeat:
                Observable.range(0, 3).repeat(2).subscribe(onNextAction);
                break;
        }
    }
}
