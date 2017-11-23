package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.api.NetManager;
import com.zjrb.sjzsw.listener.NetListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jinzifu on 2017/9/17.
 */

public class NetFragment extends BaseFragment {
    @BindView(R.id.info_show)
    TextView infoShow;
    Unbinder unbinder;
    private String url = "http://apistore.baidu.com/microservice/weather?citypinyin=beijing";

    @Override
    protected int getLayoutId() {
        return R.layout.fr_net;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        NetManager.get(url, new NetListener() {
            @Override
            public void onResponseListener(final String string) {
                infoShow.post(new Runnable() {
                    @Override
                    public void run() {
                        infoShow.setText(string);
                    }
                });
            }

            @Override
            public void onErrorListener(int responseCode) {
                Log.e("onErrorListener", "" + responseCode);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
