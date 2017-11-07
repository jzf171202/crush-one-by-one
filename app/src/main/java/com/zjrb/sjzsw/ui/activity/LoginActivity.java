package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Thinkpad on 2017/11/7.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.connect)
    TextView connect;

    @Override
    protected int getLayoutId() {
        return R.layout.login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.icon, R.id.connect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon:
                ActivityUtil.next(LoginActivity.this, MainActivity.class);
                break;
            case R.id.connect:
                break;
            default:
                break;
        }
    }
}
