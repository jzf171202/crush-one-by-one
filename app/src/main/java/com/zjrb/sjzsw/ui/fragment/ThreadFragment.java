package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.entity.SycnListEntity;
import com.zjrb.sjzsw.manager.ThreadPoolManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/2 1556
 */

public class ThreadFragment extends BaseFragment {
    private final String TAG = "ThreadFragment";
    //Vector
    @BindView(R.id.text)
    TextView text;
    Unbinder unbinder;
    private int number = 10;
    private MyRunnable myRunnable;
    private SycnListEntity sycnListEntity;
    private List<Integer> integerList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    @Override
    protected int getLayoutId() {
        return R.layout.fr_thread;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        myRunnable = new MyRunnable();
        sycnListEntity = new SycnListEntity(integerList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.ok1, R.id.ok2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok1:
                ThreadPoolManager.getInstance().execute(myRunnable);
                ThreadPoolManager.getInstance().execute(myRunnable);
                break;
            case R.id.ok2:
                break;
            default:
                break;
        }
    }

    private void syncUpdateEntity() {
        synchronized (SycnListEntity.class) {
            sycnListEntity.removeItem();
        }
    }

    /**
     * 同步更新number值
     */
    private synchronized void sycnUpdateNum() {
        if (number > 0) {
            Log.d(TAG, "执行任务=" + number);
            number = number - 1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                sycnUpdateNum();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
