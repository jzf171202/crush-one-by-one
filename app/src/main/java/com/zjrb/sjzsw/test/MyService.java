package com.zjrb.sjzsw.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyService", "在service里解除绑定");
        return super.onUnbind(intent);
    }

   public class MyBinder extends Binder{
        public void print() {
            Log.d("MyService", "在service里执行");
        }
    }
}
