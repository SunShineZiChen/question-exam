package cn.sunshine.basenetwork;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.sunshine.basenetwork.config.NetWorkConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求管理
 * 请求的数据使用 Gson 进行解析成实体类
 */
public class HttpClientTManager extends HttpClientManager {
    private static HttpClientTManager instance;
    public synchronized static HttpClientTManager getInstance() {
        if (instance == null) {
            instance = new HttpClientTManager();
        }
        return instance;
    }
    private HttpClientTManager() {
        super();
    }

    @Override
    public Retrofit createRetrofit(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        return  new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NetWorkConfig.API_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }
}
