package com.zjrb.sjzsw.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.AcDatabindingBinding;
import com.zjrb.sjzsw.entity.User;

/**
 * Created by jinzifu on 2018/4/29.
 */

public class DataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AcDatabindingBinding binding = DataBindingUtil.setContentView(this, R.layout.ac_databinding);
        binding.showAccount.setText("默认的展示");
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
