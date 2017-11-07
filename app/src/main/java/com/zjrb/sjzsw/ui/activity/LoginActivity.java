package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected int getLayoutId() {
        return R.layout.login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTextChangedListener();
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
                    ActivityUtil.next(LoginActivity.this, MainActivity.class);
                }

                break;
            case R.id.connect:
                //联系我们
                break;
            default:
                break;
        }
    }
}
