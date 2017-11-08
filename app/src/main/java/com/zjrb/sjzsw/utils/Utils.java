package com.zjrb.sjzsw.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.text.SimpleDateFormat;

/**
 * Created by jinzifu on 2016/11/22.
 */

public class Utils {
    /**
     * 渠道名称
     *
     * @param context
     * @return
     */
    public static String getApplicationMetaValue(Context context) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 获取现在时间
     *
     * @param time
     * @param type 返回短时间格式
     * @return 默认格式为“yyyy-MM-dd HH:mm:ss”
     */
    public static String getNowDate(String type, Long time) {
        if (TextUtils.isEmpty(type)) {
            type = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        String dateString = formatter.format(time*1000L);
        return dateString;
    }
}
