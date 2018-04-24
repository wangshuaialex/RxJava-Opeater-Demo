package com.alex.rxjavademo.ui.fragment;

import android.view.View;
import android.widget.Button;

import com.alex.rxjavademo.R;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Alex on 2018/4/21.
 */

public class ErrorFragment extends BaseFragment {
    @BindView(R.id.btn_onErrorReturn)
    Button btnOnErrorReturn;
    @BindView(R.id.btn_onErrorResumeNext)
    Button btnOnErrorResumeNext;
    @BindView(R.id.btn_onExceptionResumeNext)
    Button btnOnExceptionResumeNext;
    @BindView(R.id.btn_retry)
    Button btnRetry;

    @Override
    public int initView() {
        return R.layout.fragment_error;
    }

    @OnClick({R.id.btn_onErrorReturn, R.id.btn_onErrorResumeNext, R.id.btn_onExceptionResumeNext, R.id.btn_retry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //onErrorReturn，会在遇到错误时，
            // 停止源Observable的，并调用用户自定义的返回请求
            // 实质上就是调用一次OnNext方法进行内容发送后，停止消息发送
            case R.id.btn_onErrorReturn:
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 5; i++) {
                            if (i > 3) {
                                subscriber.onError(new Throwable("User Alex Defined Error"));
                            }
                            subscriber.onNext(i);
                        }
                    }
                }).onErrorReturn(new Func1<Throwable, Integer>() {
                    @Override
                    public Integer call(Throwable throwable) {
                        return 404;
                    }
                }).subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
            //onErrorResumeNext，会在源Observable遇到错误时，立即停止源Observable的数据发送
            //并取用新的Observable对象进行新的数据发送
            case R.id.btn_onErrorResumeNext:
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 5; i++) {
                            if (i > 3) {
                                subscriber.onError(new Throwable("User Alex Defined Error"));
                            }
                            subscriber.onNext(i);
                        }
                    }
                }).onErrorResumeNext(new Func1<Throwable, Observable<? extends Integer>>() {
                    @Override
                    public Observable<? extends Integer> call(Throwable throwable) {
                        return Observable.just(100,101,102);
                    }
                }).subscribe(onNextAction,onErrorAction,onCompletedAction);
                break;
            //onExceptionResumeNext，会将错误发给Observer，而不会调用备用的Observable
            case R.id.btn_onExceptionResumeNext:
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 5; i++) {
                            if (i > 3) {
                                subscriber.onError(new Throwable("User Alex Defined Error"));
                            }
                            subscriber.onNext(i);
                        }
                    }
                }).onExceptionResumeNext(Observable.just(100,101,102)).subscribe(onNextAction,onErrorAction,onCompletedAction);
                break;
            case R.id.btn_retry:
                //retry操作符，当遇到exception时会进行重试，重试次数可以由用户进行定义
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        for (int i = 0; i < 5; i++) {
                            if (i > 1) {
                                subscriber.onError(new Throwable("User Alex Defined Error"));
                            }
                            subscriber.onNext(i);
                        }
                    }
                }).retry(2).subscribe(onNextAction,onErrorAction,onCompletedAction);
                break;
        }
    }
}
