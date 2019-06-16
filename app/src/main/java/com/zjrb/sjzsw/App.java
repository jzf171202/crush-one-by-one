package com.zjrb.sjzsw;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.jzf.net.api.HttpClient;
import com.zjrb.sjzsw.jsBridge.module.ForwardPageModule;

import java.util.HashMap;

/**
 * @author jinzifu
 */
public class App extends Application {
    public static HashMap<String, Class<?>> bridgeModelMap = new HashMap<>();
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerBridgeModel();
        HttpClient.init(this);

//        setStrictMode();
    }

    /**
     * 设置严格模式
     */
    private void setStrictMode() {
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build();
        StrictMode.setThreadPolicy(threadPolicy);

        StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(vmPolicy);
    }

    /**
     * 注册jsBridge协议类
     */
    private void registerBridgeModel() {
        bridgeModelMap.put("forward", ForwardPageModule.class);
    }
}



