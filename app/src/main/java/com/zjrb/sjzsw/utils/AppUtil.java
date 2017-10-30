package com.zjrb.sjzsw.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;

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
    /**
     * 查询手机内所有应用包括系统应用
     *
     * @param context
     */
    public static List<PackageInfo> getAllApps(Context context) {
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        return paklist;
    }


    /**
     * 查询手机内非系统应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllAppsNoSystem(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
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
     * 查询手机内所有支持分享的应用
     *
     * @param context
     * @return
     */
    public static List<ResolveInfo> getShareApps(Context context) {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

        return mApps;
    }

    /**
     * 判断手机已安装某程序的方法
     * @param context
     * @param packageName 目标程序的包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = getAllAppsNoSystem(context);
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

    private static long mLastClickTime = 0;
    private static final int SPACE_TIME = 500;

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
