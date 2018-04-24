package com.alex.rxjavademo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alex.rxjavademo.R;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Alex on 2018/4/11.
 */

public abstract class BaseFragment<T> extends Fragment {
    Unbinder unbinder;
    public static final String TAG = "RxJava";
    //onNext响应成功方法的包装
    Action1<T> onNextAction = new Action1<T>() {
        @Override
        public void call(T t) {
            Toast.makeText(getActivity(), "onNext:" + t, Toast.LENGTH_SHORT).show();
        }
    };
    //onError返回失败信息方法的包装
    Action1<Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(getActivity(), "onError,Error Info is:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    //onCompleted()完成方法的包装
    Action0 onCompletedAction = new Action0() {
        @Override
        public void call() {
            Toast.makeText(getActivity(), "onCompleted", Toast.LENGTH_SHORT).show();
        }
    };
    //观察者
    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onStart() {
            Log.e(TAG, "onStart");
        }

        @Override
        public void onCompleted() {
            Toast.makeText(getActivity(), "onCompleted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getActivity(), "onError,Error Info Is:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(String s) {
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(initView(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    //设置视图
    public abstract int initView();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
