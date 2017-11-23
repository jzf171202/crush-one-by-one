package com.zjrb.sjzsw.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Locale;

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
        String dateString = formatter.format(time);
        return dateString;
    }


    /**
     * 根据标签分割字符串
     *
     * @param type   转义字符要提前加上
     * @param string
     * @return
     */
    public static List<String> splitStr(String type, String string) {
        List list = null;
        if (!TextUtils.isEmpty(string)) {
            list = Arrays.asList(string.split(type));
        }
        return list;
    }

    /**
     * 获取特定格式时间
     * @param date
     * @return
     */
    public static String getFormatData(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd HH:mm");
        return format.format(date);
    }
    /**
     * 获取特定格式的数据
     * @param cnt
     * @return   00:00:00的时间格式
     */
    public static String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }

    /**
     * 获取长整型数字的位数
     * @param num
     * @return
     */
    public static int getNumLenght(long num){
        num = num>0?num:-num;
        if (num==0) {
            return 1;
        }
        return (int) Math.log10(num)+1;
    }

}
