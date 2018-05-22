package com.example.wuhe.myapplication;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by SunShine on 2018/3/8.
 */

public interface AppApiService {
    /**
     * 获取天气接口
     */
    @GET("{url}")
    Call<TianQiBean> executeTGet(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps);
    /**
     * 获取天气接口
     */
    @POST("url")
    Call<TianQiBean> executeTPost(
            @Path(value = "url", encoded = true) String url,
            @FieldMap Map<String, String> maps);
}
