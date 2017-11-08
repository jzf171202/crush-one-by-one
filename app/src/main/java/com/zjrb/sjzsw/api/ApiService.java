package com.zjrb.sjzsw.api;


import com.zjrb.sjzsw.entity.TopicBean;
import com.zjrb.sjzsw.entity.LoginBean;

import io.reactivex.Observable;
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
     * 获取直播节目列表
     * @param page
     * @param pageSize
     * @return
     */
    @GET("api/live/list")//page=1&pageSize=10
    Observable<TopicBean> getProgramList(@Query("page") int page, @Query("pageSize") String pageSize);


    @FormUrlEncoded
    @POST("login")
    Observable<LoginBean>  login(@Field("username") String username, @Field("password") String password);


}
