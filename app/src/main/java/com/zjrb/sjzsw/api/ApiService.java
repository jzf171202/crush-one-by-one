package com.zjrb.sjzsw.api;


import com.zjrb.sjzsw.entity.HomeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jinzifu on 2017/10/16.
 * Email:jinzifu123@163.com
 * 类描述:自定义API接口类型
 */
public interface ApiService {

    /**
     * 获取美女列表
     * @param key
     * @param num
     * @return
     */
    @GET("meinv/")
    Observable<HomeBean> getGirls(@Query("key") String key, @Query("num") int num);

}
