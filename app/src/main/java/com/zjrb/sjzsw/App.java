package com.zjrb.sjzsw;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.jzf.net.api.HttpClient;
import com.jzf.image.core.ImageLoader;
import com.jzf.image.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.zjrb.sjzsw.utils.AppUtil;


/**
 * @author jinzifu
 */
public class App extends Application {

    private static App sAppContext;

    public static App getAppContext() {
        if (sAppContext == null) {
            sAppContext = new App();
        }
        return sAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        HttpClient.init(this);

        //LeakCanary初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        //配置tencent.bugly,上报进程控制
        initBugly();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);

        //初始化APP工具类
        Utils.init(this);
    }

    /**
     * 配置tencent.bugly,上报进程控制
     */
    private void initBugly() {
        // 获取当前包名
        String packageName = sAppContext.getPackageName();
        // 获取当前进程名
        String processName = AppUtil.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(sAppContext);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //tencent.bugly初始化 建议在测试阶段建议设置成true，发布时设置为false。 8f699e3b6c为你申请的应用appid
        CrashReport.initCrashReport(getApplicationContext(), "8f699e3b6c", true);
    }
}



