package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zjrb.sjzsw.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author jinzifu
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.leftImage)
    ImageButton leftImage;
    @BindView(R.id.titleText)
    TextView titleText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        leftImage.setVisibility(View.INVISIBLE);
        titleText.setText("测试小功能");
    }

    @OnClick({R.id.ok, R.id.text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok:
                showToast("ok");
                break;
            case R.id.text:
                break;
            default:
                break;
        }
    }
}