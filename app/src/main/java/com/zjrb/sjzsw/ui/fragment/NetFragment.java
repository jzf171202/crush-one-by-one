package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jzf.net.exception.ApiException;
import com.jzf.net.listener.OnResultCallBack;
import com.jzf.net.observer.BaseObserver;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.api.NetManager;
import com.zjrb.sjzsw.controller.LoginController;
import com.zjrb.sjzsw.entity.LoginEntity;
import com.zjrb.sjzsw.listener.NetListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author jinzifu
 * @date 2017/9/17
 */

public class NetFragment extends BaseFragment {
    @BindView(R.id.info_show)
    TextView infoShow;
    Unbinder unbinder;
    private String url = "http://apistore.baidu.com/microservice/weather?citypinyin=beijing";
    private LoginController loginController;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_net;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        registerController(loginController = new LoginController(context));
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

    @OnClick({R.id.net_load, R.id.retrofit_load})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.net_load:
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
                break;
            case R.id.retrofit_load:
                loginController.login("chenshaohua", "12345678", loginController.registerObserver(
                        new BaseObserver(context, new OnResultCallBack<LoginEntity>() {

                            @Override
                            public void onSuccess(LoginEntity loginEntity) {
                                showToast(loginEntity.getUser().getTruename());
                            }

                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(ApiException.ResponeThrowable e) {
                                Log.d("onError", e.getMessage());
                            }
                        })));
                break;
            default:
                break;
        }
    }
}
