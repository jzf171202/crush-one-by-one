package com.zjrb.sjzsw.common;

import android.Manifest;

/**
 * @author Thinkpad
 * @date 2017/11/1
 */

public class Constant {
    public static final String NEWS_ID = "news_id";
    public static final String NEWS_TYPE = "news_type";
    public static final String CHANNEL_POSITION = "channel_position";

    /**
     * 刷新MainActivity页面的列表数据
     */
    public static final String ACTION_REFRESH_MAINACTIVITY = "ACTION_REFRESH_MAINACTIVITY";

    /**
     * 权限申请列表
     */
    public static final String[] permissionArray = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int PERMISSION_CODE_ALL = 1;

}
