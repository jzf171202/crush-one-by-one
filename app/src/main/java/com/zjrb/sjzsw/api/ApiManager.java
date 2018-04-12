package com.zjrb.sjzsw.api;


import com.jzf.net.api.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinzifu on 2017/10/23.
 * Email:jinzifu123@163.com
 * 类描述:
 */

public class ApiManager {
    private static Map<Class, Object> map = new HashMap();

    /**
     * 获取API接口类
     * 注：可以写多个API接口类，并在此配置
     *
     * @return
     */
    public static ApiService getApiService() {
        return provideService(ApiService.class);
    }

    private static <T> T provideService(Class cls) {
        Object object = map.get(cls);
        if (object == null) {
            synchronized (cls) {
                object = map.get(cls);
                if (object == null) {
                    object = HttpClient.getInstance().createService(cls);
                    map.put(cls, object);
                }
            }
        }
        return (T) object;
    }
}
