package com.alex.rxjavademo.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alex.rxjavademo.R;
import com.alex.rxjavademo.data.beans.NameBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by Alex on 2018/4/11.
 */

public class TransformFragment extends BaseFragment {
    @BindView(R.id.btn_map)
    Button btnMap;
    @BindView(R.id.btn_flatMap)
    Button btnFlatMap;
    @BindView(R.id.btn_cast)
    Button btnCast;
    @BindView(R.id.btn_concatMap)
    Button btnConcatMap;
    @BindView(R.id.btn_flatMapIterable)
    Button btnFlatMapIterable;
    @BindView(R.id.btn_buffer)
    Button btnBuffer;
    @BindView(R.id.btn_groupBy)
    Button btnGroupBy;
    Unbinder unbinder;

    @Override
    public int initView() {
        return R.layout.fragment_transform;
    }

    @OnClick({R.id.btn_map, R.id.btn_flatMap, R.id.btn_cast, R.id.btn_concatMap, R.id.btn_flatMapIterable, R.id.btn_buffer, R.id.btn_groupBy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //map操作符，通过指定一个Func对象，将Observable转换为另一个Observable对象并发送
            case R.id.btn_map:
                Observable.just("Alex_Payne")
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                return "My Name is" + s;
                            }
                        }).subscribe(onNextAction);
                break;
            //flatMap操作符，将Observable发送的数据集合转换为Observable集合
            //flatMap的合并运行允许交叉，允许交错的发送事件
            case R.id.btn_flatMap:
                String[] observableArr = {"Alex", "Max", "Bruce", "Frank", "Tom"};
                Observable.from(observableArr).flatMap(new Func1<String, Observable<String>>() {

                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just("My Name is:" + s);
                    }
                }).subscribe(onNextAction);
                break;
            //cast操作符，将类对象进行转换
            // 需强调的一点是只能由父类对象转换为子类对象
            case R.id.btn_cast:
                Object[] objectsArr = {"1", "2", "3"};
                Observable.from(objectsArr).cast(String.class).subscribe(onNextAction);
                break;
            //concatMap操作符，将Observable发送的数据集合转换为Observable集合
            //解决了flatMap的交叉问题，将发送的数据连接发送
            case R.id.btn_concatMap:
                Observable.just("Alex", "Max", "Bruce", "Frank", "Tom").concatMap(new Func1<String, Observable<String>>() {

                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just("My Name is:" + s);
                    }
                }).subscribe(onNextAction);
                break;
            //将数据集合转换为Iterable，在Iterable中对数据进行处理
            case R.id.btn_flatMapIterable:
                Observable.just(1, 2, 3).flatMapIterable(new Func1<Integer, Iterable<Integer>>() {
                    @Override
                    public Iterable<Integer> call(Integer number) {
                        ArrayList<Integer> mList = new ArrayList<>();
                        mList.add(1000 + number);
                        return mList;
                    }
                }).subscribe(onNextAction);

                break;
            //buffer操作符，将原有Observable转换为一个新的Observable，这个新的Observable每次发送一组值，而不是一个个进行发送
            case R.id.btn_buffer:
                Observable.just(1, 2, 3, 4, 5, 6)
                    .buffer(3).subscribe(new Action1<List<Integer>>() {
                        @Override
                        public void call(List<Integer> mList) {
                            for (Integer i : mList) {
                                Toast.makeText(getActivity(), "new Number i is:" + i, Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getActivity(), "Another request is called", Toast.LENGTH_SHORT).show();
                        }
                    });
                break;
            //groupBy操作符
            //将原有的Observable对象转换为发送一组Observable集合的GroupedObservable对象，可以做分组操作
            //GroupedObservable将分组完毕的Observable对象可以继续发送
            case R.id.btn_groupBy:
                Observable.range(0, 10).groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer num) {
                        return num % 3;
                    }
                }).subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
                    @Override
                    public void call(final GroupedObservable<Integer, Integer> groupedObservable) {
                        groupedObservable.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer num) {
                                Toast.makeText(getActivity(), "当前的组别是：" + groupedObservable.getKey() + "组别内的数字是：" + num, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
        }
    }
}
