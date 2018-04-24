package com.alex.rxjavademo.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alex.rxjavademo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex on 2018/4/20.
 */

public class SupportFragment extends BaseFragment {
    @BindView(R.id.btn_delay)
    Button btnDelay;
    @BindView(R.id.btn_do)
    Button btnDo;
    @BindView(R.id.btn_subscribeOn)
    Button btnSubscribeOn;
    @BindView(R.id.btn_observeOn)
    Button btnObserveOn;
    @BindView(R.id.btn_timeout)
    Button btnTimeout;
    Unbinder unbinder;

    @Override
    public int initView() {
        return R.layout.fragment_support;
    }

    @OnClick({R.id.btn_delay, R.id.btn_do, R.id.btn_subscribeOn, R.id.btn_observeOn, R.id.btn_timeout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //delay操作符可以让源Observable对象发送数据之前暂停一段制定的时间
            case R.id.btn_delay:
                Observable.just(1, 2, 3).delay(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(onNextAction);
                break;
            //do操作符,其下细分有很多内容，其作用就是为源Observable对象提供回调
            case R.id.btn_do:
                Observable.range(0, 3).doOnNext(onNextAction).subscribe(onNextAction);
                break;
            //subscribeOn操作符，指定subscribe()所发生的线程，即Observable.OnSubscribe被激活时所处的线程。或者叫做事件产生的线程
            case R.id.btn_subscribeOn:
                Observable.just("当前线程的ID为：" + Thread.currentThread().getName()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onNextAction);
                break;
            //observeOn操作符,指定Subscriber所运行的线程。或者叫做事件消费的线程
            case R.id.btn_observeOn:
                Observable.just("当前的线程ID为：" + Thread.currentThread().getName()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onNextAction);
                break;
            //timeout操作符，如果源Observable对象过了一段时间没有发送数据,timeout会以onError通知终止这个Observable
            //timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other)是timeout其中的一种
            //它在超时的时候会将源Observable转换为制定的备用的Observable对象
            case R.id.btn_timeout:
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 5; i++) {
                            try {
                                Thread.sleep(i * 100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            subscriber.onNext(i);
                        }
                    }
                }).timeout(200, TimeUnit.MILLISECONDS, Observable.just(100, 200))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNextAction);
                break;
        }
    }
}
