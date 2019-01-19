package com.zjrb.sjzsw.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.AcTestBinding;
import com.zjrb.sjzsw.test.MyService;

public class TestActvity extends BaseActivity<AcTestBinding> implements View.OnClickListener {
    MyService.MyBinder binder;
    @Override
    protected int getLayoutId() {
        return R.layout.ac_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        t.test.setOnClickListener(this);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder) service;
            binder.print();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                Intent intent = new Intent(this, MyService.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //无论是否执行解绑service的操作，都会在activity销毁时解绑service
//        unbindService(serviceConnection);
        Log.d("MyService", "activity销毁");
    }
}
