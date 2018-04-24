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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Alex on 2018/4/19.
 */

public class CombinationFragment extends BaseFragment {
    @BindView(R.id.btn_startWith)
    Button btnStartWith;
    @BindView(R.id.btn_merge)
    Button btnMerge;
    @BindView(R.id.btn_concat)
    Button btnConcat;
    @BindView(R.id.btn_zip)
    Button btnZip;
    @BindView(R.id.btn_combineLastest)
    Button btnCombineLastest;

    @Override
    public int initView() {
        return R.layout.fragment_combina;
    }

    @OnClick({R.id.btn_startWith, R.id.btn_merge, R.id.btn_concat, R.id.btn_zip, R.id.btn_combineLastest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //startWith操作符，会在发送的数据之前插入数据
            case R.id.btn_startWith:
                Observable.range(3, 5).startWith(0, 10086).subscribe(onNextAction);
                break;
            //merge操作符，会将多个Observable对象合并到一个Observable对象中进行发送，merge可能会让合并的数据错乱
            case R.id.btn_merge:
                Observable<Integer> firstObservable = Observable.just(0, 1, 2).subscribeOn(Schedulers.io());
                Observable<Integer> secondObservable = Observable.just(3, 4, 5);
                Observable.merge(firstObservable, secondObservable).subscribe(onNextAction, onErrorAction);
                break;
            //concat操作符，会将多个Observable对象合并到一个Observable对象中进行发送，严格按照顺序进行发送
            case R.id.btn_concat:
                firstObservable = Observable.just(0, 1, 2).subscribeOn(Schedulers.io());
                secondObservable = Observable.just(3, 4, 5);
                Observable.concat(firstObservable, secondObservable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNextAction);
                break;
            //zip操作符，会将多个Observable对象转换成一个Observable对象然后进行发送，转换关系可根据需求自定义
            case R.id.btn_zip:
                Observable<Integer> integerObservable = Observable.range(0, 4);
                Observable<String> stringObservable = Observable.just("a", "b", "c", "d");
                Observable.zip(integerObservable, stringObservable, new Func2<Integer, String, String>() {
                    @Override
                    public String call(Integer num, String info) {
                        //在这里的转换关系为将数字与字串内容进行拼接
                        return "数字为:" + num + "……字符为：" + info;
                    }
                }).subscribe(onNextAction);
                break;
            //combineLastest操作符，会将多个Observable对象转换一个Observable对象然后进行发送，转换关系可以根据需求自定义
            //不同于zip操作符的是，会将最新发送的数据组合到一起
            case R.id.btn_combineLastest:
                integerObservable = Observable.just(1, 2, 3);
                stringObservable = Observable.just("a", "b", "c");
                Observable.combineLatest(integerObservable, stringObservable, new Func2<Integer, String, String>() {
                    @Override
                    public String call(Integer num, String info) {
                        //在这里的转换关系为将数字与字串内容进行拼接
                        return "数字为:" + num + "……字符为：" + info;
                    }
                }).subscribe(onNextAction);
                break;
        }
    }
}
