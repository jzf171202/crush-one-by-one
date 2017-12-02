package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.sjzsw.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 类描述：Rxjava2背压策略案例
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/28 1627
 */

public class Rxjava2Fragment extends BaseFragment {
    private static String TAG = "rxjava2";
    Unbinder unbinder;
    @BindView(R.id.ok)
    TextView ok;
    private Subscription subscription;
    private String filePath = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ?
            Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";

    @Override
    protected int getLayoutId() {
        return R.layout.fr_rxjava2;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
//        rxjavaSimple();
        readFile("test.txt");
    }

    private void readFile(final String filename) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                File file = new File(Environment.getExternalStorageDirectory(), filename);
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String string = "";
                while (e.requested() > 0 && !TextUtils.isEmpty(string = bufferedReader.readLine())) {
                    e.onNext(string);
                }
            }
        }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    StringBuilder stringBuilder = new StringBuilder();
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        subscription.request(1);
                    }

                    @Override
                    public void onNext(final String s) {
                        Log.d(TAG, s);
                        stringBuilder.append(s+"\n");
                        ok.post(new Runnable() {
                            @Override
                            public void run() {
                                ok.setText(stringBuilder.toString());
                            }
                        });
                        try {
                            Thread.sleep(1000);
                            subscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * rxjava 2.x实例
     */
    private void rxjavaSimple() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "e.requested() == " + e.requested());
                for (int i = 0; ; i++) {
                    boolean flag = false;
                    while (e.requested() == 0) {
                        if (!flag) {
                            Log.d(TAG, "e.requested() == " + e.requested());
                            flag = true;
                        }
                    }
                    e.onNext(i + 1);
                    Log.d(TAG, "已发送" + (i + 1) + "个——剩下" + e.requested());
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer s) {
                        Log.d(TAG, "接收 ——" + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "接收错误——" + t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.button, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (subscription != null) {
                    subscription.request(96);
                    Log.d(TAG, "下游请求了96个");
                }
                break;
            case R.id.ok:
                break;
        }
    }
}
