package com.zjrb.sjzsw.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzf.net.exception.ApiException;
import com.jzf.net.listener.OnResultCallBack;
import com.jzf.net.observer.BaseObserver;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.controller.LoginController;
import com.zjrb.sjzsw.entity.LoginBean;
import com.zjrb.sjzsw.utils.ActivityUtil;
import com.zjrb.sjzsw.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Thinkpad on 2017/11/7.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.word)
    ImageView word;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.account_line)
    View accountLine;
    @BindView(R.id.account_note)
    TextView accountNote;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.psw_line)
    View pswLine;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.connect)
    TextView connect;

    private LoginController loginController;

    @Override
    protected int getLayoutId() {
        return R.layout.login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        registerController(loginController = new LoginController(this));
        setTextChangedListener();
        initData();
    }

    private void initData() {
        account.setText(SpUtil.getString("account"));
        account.setSelection(account.getText().toString().length());
        password.setText(SpUtil.getString("password"));
    }

    private void setTextChangedListener() {
        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!account.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_corner));
                } else {
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_grey_corner));
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!account.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_corner));
                } else {
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_grey_corner));
                }
            }
        });
    }

    private void login() {
        //"chenshaohua", "12345678"
        loginController.login(account.getText().toString(), password.getText().toString(),
                loginController.registerObserver(new BaseObserver(new OnResultCallBack<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean loginBean) {
                        Log.d("loginBean", "" + loginBean.getCode());
                        if(loginBean.getCode().equals("0"))
                        {
                            SpUtil.putString("account", account.getText().toString());
                            SpUtil.putString("password", password.getText().toString());
                            ActivityUtil.next(LoginActivity.this, MainActivity.class);
                        }
                        else
                        {
                            showToast(loginBean.getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(ApiException.ResponeThrowable e) {
                        Log.e("onError", "" + e.getMessage());
                    }
                })));

    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick({R.id.icon, R.id.login, R.id.connect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon:
                ActivityUtil.next(LoginActivity.this, MainActivity.class);
                break;
            case R.id.login:
                if (account.getText().toString().isEmpty()) {
                    showToast(getString(R.string.enter_id));
                } else if (password.getText().toString().isEmpty()) {
                    showToast(getString(R.string.enter_psw));
                } else {
                    login();
                }

                break;
            case R.id.connect:
                //联系我们
                call("12345678");
                break;
            default:
                break;
        }
    }
}
