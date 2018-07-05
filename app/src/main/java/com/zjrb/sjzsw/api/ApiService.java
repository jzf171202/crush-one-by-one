package com.zjrb.sjzsw.api;


import com.jzf.net.api.BaseResponse;
import com.zjrb.sjzsw.entity.LoginEntity;
import com.zjrb.sjzsw.entity.ProgramListEntity;
import com.zjrb.sjzsw.entity.SuccessEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author jinzifu
 * @date 2017/10/16
 * Email:jinzifu123@163.com
 * 类描述:自定义API接口类型
 */
public interface ApiService {

    /**
     * 获取直播节目列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GET("/api/live/app/list")
    Observable<BaseResponse<ProgramListEntity>> getProgramList(@Query("page") int page, @Query("pageSize") String pageSize);

    /**
     * 获取天气预报
     *
     * @return
     */
    @GET("open/api/weather/json.shtml?city=%E5%8C%97%E4%BA%AC")
    Observable<BaseResponse<Object>> getWeather();

    /**
     * 新建直播页面数据
     *
     * @param maps
     * @return
     */
    @POST("/api/live/new")
    Observable<BaseResponse<SuccessEntity>> createProjectDetail(@QueryMap Map<String, String> maps);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<BaseResponse<LoginEntity>> login(@Field("username") String username, @Field("password") String password);

}
