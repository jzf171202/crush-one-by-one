package com.zjrb.sjzsw.utils;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.text.TextUtils;

import com.zjrb.sjzsw.App;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinzifu on 2017/3/6.
 * 通过 PackageInfo  获取具体信息方法：
 * 包名获取方法：packageInfo.packageName
 * icon获取获取方法：packageManager.getApplicationIcon(applicationInfo)
 * 应用名称获取方法：packageManager.getApplicationLabel(applicationInfo)
 * 使用权限获取方法：packageManager.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS).requestedPermissions通过 ResolveInfo 获取具体信息方法：
 * 包名获取方法：resolve.activityInfo.packageName
 * icon获取获取方法：resolve.loadIcon(packageManager)
 * 应用名称获取方法：resolve.loadLabel(packageManager).toString()
 */

public class AppUtil {
    private static final int SPACE_TIME = 500;
    private static long mLastClickTime = 0;

    /**
     * 查询手机内所有应用包括系统应用
     */
    public static List<PackageInfo> getAllApps() {
        PackageManager pManager = App.context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        return paklist;
    }

    /**
     * 查询手机内非系统应用
     *
     * @return
     */
    public static List<PackageInfo> getAllAppsNoSystem() {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = App.context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取版本号/名
     *
     * @return
     */
    public static String[] getAppVersion() {
        String[] versionNo = new String[2];
        PackageManager packageManager = App.context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.zjrb.sjzsw", 0);
            versionNo[0] = "" + packageInfo.versionCode;
            versionNo[1] = "" + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionNo;
    }

    /**
     * 查询手机内所有支持分享的应用
     *
     * @return
     */
    public static List<ResolveInfo> getShareApps() {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = App.context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

        return mApps;
    }

    /**
     * 判断手机已安装某程序的方法
     *
     * @param packageName 目标程序的包名
     * @return
     */
    public static boolean isAvilible(String packageName) {
        List<PackageInfo> pinfo = getAllAppsNoSystem();
        //用于存储所有已安装程序的包名
        List<String> pName = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        //判断pName中是否有目标程序的包名，有TRUE，没有FALSE
        return pName.contains(packageName);
    }

    /**
     * 防止过快重复点击 true为太快点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - mLastClickTime <= SPACE_TIME) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }
}
