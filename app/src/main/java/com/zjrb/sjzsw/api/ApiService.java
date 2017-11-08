package com.zjrb.sjzsw.api;


import com.zjrb.sjzsw.entity.HomeBean;
import com.zjrb.sjzsw.entity.LoginBean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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


    @FormUrlEncoded
    @POST("login")
    Observable<LoginBean>  login(@Field("username") String username, @Field("password") String password);


}
