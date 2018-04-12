package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jzf.net.exception.ApiException;
import com.jzf.net.listener.ApiCallBack;
import com.jzf.net.observer.ApiObserver;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.api.NetManager;
import com.zjrb.sjzsw.entity.LoginEntity;
import com.zjrb.sjzsw.listener.NetListener;
import com.zjrb.sjzsw.presenter.LoginPresenter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    private String url = "http://apistore.baidu.com/microservice/weather?citypinyin=beijing";
    private LoginPresenter mLoginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_net;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        initPresenter(mLoginPresenter = new LoginPresenter());
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
                String acount = account.getText().toString();
                String pasword = password.getText().toString();
                mLoginPresenter.login(acount, encode(pasword), mLoginPresenter.registerObserver(
                        new ApiObserver(context, new ApiCallBack<LoginEntity>() {

                            @Override
                            public void onSuccess(LoginEntity loginEntity) {
                                infoShow.setText("" + loginEntity.getUser().getTruename());
                            }

                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(ApiException.ResponeThrowable e) {
                            }
                        })));
                break;
            default:
                break;
        }
    }

    /**
     * 加密登录参数
     *
     * @param password
     */
    private String encode(String password) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (int i = 0; i < password.length(); i++) {
                String item = password.charAt(i) + "";
                String output = URLEncoder.encode(item.trim(), "UTF-8");
                if (output.equals(item)) {
                    output = Integer.toHexString(password.charAt(i));
                }
                stringBuilder.append(output);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        password = stringBuilder.toString().replaceAll("\\%", "").toUpperCase();
        return password;
    }
}
