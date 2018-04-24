package com.alex.rxjavademo.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alex.rxjavademo.BuildConfig;
import com.alex.rxjavademo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex on 2018/4/19.
 */

public class FilterFragment extends BaseFragment {
    @BindView(R.id.btn_filter)
    Button btnFilter;
    @BindView(R.id.btn_elementAt)
    Button btnElementAt;
    @BindView(R.id.btn_distinct)
    Button btnDistinct;
    @BindView(R.id.btn_skip)
    Button btnSkip;
    @BindView(R.id.btn_take)
    Button btnTake;
    @BindView(R.id.btn_ignoreElements)
    Button btnIgnoreElements;
    @BindView(R.id.btn_throttleFirst)
    Button btnThrottleFirst;
    @BindView(R.id.btn_throttleWithTimeOut)
    Button btnThrottleWithTimeOut;
    Unbinder unbinder;

    @Override
    public int initView() {
        return R.layout.fragment_filter;
    }

    @OnClick({R.id.btn_filter, R.id.btn_elementAt, R.id.btn_distinct, R.id.btn_skip, R.id.btn_take, R.id.btn_ignoreElements, R.id.btn_throttleFirst, R.id.btn_throttleWithTimeOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //filter过滤操作符，对Observable发送的内容根据自定义的规则进行过滤
            case R.id.btn_filter:
                Observable.range(0, 5).filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer num) {
                        return num > 2;//自定义的条件,只有符合条件的结果才会提交给观察者
                    }
                }).subscribe(onNextAction);
                break;
            //elementAt操作符，用于返回指定位置后一位的数据，即脚标+1的数据
            case R.id.btn_elementAt:
                //在这里发送0、1、2、3、4，脚标为3的数据为2，发送其后一位数据3
                Observable.range(0, 5).elementAt(3).subscribe(onNextAction);
                break;
            //distinct操作符，用于Observable发送的元素的去重
            case R.id.btn_distinct:
                Observable.just(1, 1, 2, 2, 2, 3).distinct().subscribe(onNextAction);
                break;
            //skip操作符，用于Observable发送的元素前N项去除掉
            case R.id.btn_skip:
                Observable.range(0, 5).skip(2).subscribe(onNextAction);
                break;
            //take操作符，用于Observable发送的元素只取前N项
            case R.id.btn_take:
                Observable.range(0, 5).take(2).subscribe(onNextAction);
                break;
            //ignoreElements操作符，忽略掉源Observable发送的结果，只把Observable的onCompleted或onError发送
            case R.id.btn_ignoreElements:
                Observable.range(0, 5).ignoreElements().subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
            //throttleFirst操作符，会定期发送这个时间段里源Observable发送的第一个数据
            //throttleFirst操作符默认在computaioin调度器上执行，其他的数据都会被过滤掉
            case R.id.btn_throttleFirst:
                Observable
                        .create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                for (int i = 0; i < 10; i++) {
                                    subscriber.onNext(i);
                                    //线程休眠100毫秒
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .throttleFirst(200, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNextAction);
                break;
            //throttleWithTimeout操作符
            //源发射数据时，如果两次数据的发射间隔小于指定时间，就会丢弃前一次的数据,直到指定时间内都没有新数据发射时才进行发射
            case R.id.btn_throttleWithTimeOut:
                Observable
                        .create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                subscriber.onNext(1);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    throw Exceptions.propagate(e);
                                }
                                subscriber.onNext(2);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    throw Exceptions.propagate(e);
                                }

                                subscriber.onNext(3);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw Exceptions.propagate(e);
                                }
                                subscriber.onNext(4);
                                subscriber.onNext(5);
                                subscriber.onCompleted();

                            }
                        })
                        .throttleWithTimeout(800, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNextAction);
                break;
        }
    }
}
