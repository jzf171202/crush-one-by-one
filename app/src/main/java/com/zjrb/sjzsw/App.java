package com.zjrb.sjzsw;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.jzf.net.api.HttpClient;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.zjrb.sjzsw.utils.AppUtil;
import com.zjrb.sjzsw.utils.SpUtil;


/**
 * Created by shiwei on 2017/3/21.
 */

public class App extends Application {

    private static App sAppContext;
    private static String THEME_KEY = "theme_mode";
    private boolean isNight;
    private String test;

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
        //初始化主题模式
        initThemeMode();
        //配置tencent.bugly,上报进程控制
        initBugly();
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

    /**
     * 初始化主题模式：日间模式/夜间模式
     */
    private void initThemeMode() {
        isNight = SpUtil.getBoolean(THEME_KEY, false);
        if (isNight) {
            //夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void setThemes(AppCompatActivity activity, boolean mode) {
        if (isNight == mode) {
            return;
        }
        if (!mode) {
            //白天模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            //夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        isNight = mode;
        SpUtil.putBoolean(THEME_KEY, isNight);
        activity.recreate();
    }

    /**
     * 刷新UI_MODE模式
     */
    public void refreshResources(Activity activity) {
        isNight = SpUtil.getBoolean(THEME_KEY, false);
        if (isNight) {
            updateConfig(activity, Configuration.UI_MODE_NIGHT_YES);
        } else {
            updateConfig(activity, Configuration.UI_MODE_NIGHT_NO);
        }
    }


    /**
     * google官方bug，暂时解决方案
     * 手机切屏后重新设置UI_MODE模式（因为在dayNight主题下，切换横屏后UI_MODE会出错，会导致资源获取出错，需要重新设置回来）
     */
    private void updateConfig(Activity activity, int uiNightMode) {
        Configuration newConfig = new Configuration(activity.getResources().getConfiguration());
        newConfig.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        newConfig.uiMode |= uiNightMode;
        activity.getResources().updateConfiguration(newConfig, null);
    }

}
