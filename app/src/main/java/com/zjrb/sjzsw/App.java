package com.zjrb.sjzsw;

import android.app.Application;
import android.content.Context;

import com.jzf.net.api.HttpClient;
import com.tencent.bugly.crashreport.CrashReport;
import com.zjrb.sjzsw.jsBridge.module.ForwardPageModule;
import com.zjrb.sjzsw.utils.AppUtil;

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
        initBugly();
    }

    /**
     * 注册jsBridge协议类
     */
    private void registerBridgeModel() {
        bridgeModelMap.put("forward", ForwardPageModule.class);
    }

    /**
     * 配置tencent.bugly,上报进程控制
     */
    private void initBugly() {
        // 获取当前包名
        String packageName = getPackageName();
        // 获取当前进程名
        String processName = AppUtil.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //tencent.bugly初始化 建议在测试阶段建议设置成true，发布时设置为false。 8f699e3b6c为你申请的应用appid
        CrashReport.initCrashReport(getApplicationContext(), "8f699e3b6c", true);
    }
}



