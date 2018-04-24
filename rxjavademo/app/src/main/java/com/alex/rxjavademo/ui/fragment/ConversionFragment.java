package com.alex.rxjavademo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alex.rxjavademo.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Alex on 2018/4/22.
 */

public class ConversionFragment extends BaseFragment {
    @BindView(R.id.btn_toList)
    Button btnToList;
    @BindView(R.id.btn_toSortedList)
    Button btnToSortedList;
    @BindView(R.id.btn_toMap)
    Button btnToMap;
    Unbinder unbinder;

    @Override
    public int initView() {
        return R.layout.fragment_conversion;
    }

    @OnClick({R.id.btn_toList, R.id.btn_toSortedList, R.id.btn_toMap})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //toList操作符，将源Observable发送的数据组合为一个List集合
            //然后再次在onNext方法中将转换完的List集合进行传递
            case R.id.btn_toList:
                Observable.just(1, 2, 3).toList().subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> numList) {
                        for (Integer i : numList) {
                            Toast.makeText(getActivity(), "i:" + i, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            //toSortedList操作符,会将源Observable发送的数据组合为一个List集合,并会按照升序的方式进行排序
            //然后再次在onNext方法中将转换完的List集合进行传递
            case R.id.btn_toSortedList:
                Observable.just(40, 10, 80, 30).toSortedList().subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> numList) {
                        for (Integer i : numList) {
                            Toast.makeText(getActivity(), "i:" + i, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            //toMap操作符，将源Observable发送的数据作为Map集合中的值，需要值进行键的定义
            //将转换完毕的Map集合在onNext方法中进行发送
            case R.id.btn_toMap:
              Observable.just("Alex","Payne").toMap(new Func1<String, Integer>() {
                  @Override
                  public Integer call(String s) {
                      return s.equals("Alex")?0:1;
                  }
              }).subscribe(new Action1<Map<Integer, String>>() {
                  @Override
                  public void call(Map<Integer, String> convertMap) {
                      for (int i = 0; i < convertMap.size(); i++) {
                          Toast.makeText(getActivity(), convertMap.get(i), Toast.LENGTH_SHORT).show();
                      }
                  }
              });
                break;
        }
    }


}
