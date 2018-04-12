package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.api.NetManager;
import com.zjrb.sjzsw.biz.INetCallBack;
import com.zjrb.sjzsw.biz.viewBiz.IVLogin;
import com.zjrb.sjzsw.entity.LoginEntity;
import com.zjrb.sjzsw.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author jinzifu
 * @date 2017/9/17
 */

public class NetFragment extends BaseFragment implements IVLogin {
    @BindView(R.id.info_show)
    TextView infoShow;
    Unbinder unbinder;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    private String url = "http://www.weather.com.cn/data/cityinfo/101010100.html";
    private LoginPresenter mLoginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_net;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        //this和context和getApplictionContext的区别:https://blog.csdn.net/wang_8649/article/details/70535978
        initPresenter(mLoginPresenter = new LoginPresenter(mContext, this));
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
                NetManager.get(url, new INetCallBack() {
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
                mLoginPresenter.login(acount, pasword);
                break;
            default:
                break;
        }
    }

    @Override
    public void showInfo(LoginEntity loginEntity) {
        infoShow.setText("" + loginEntity.getUser().getTruename());
    }
}
