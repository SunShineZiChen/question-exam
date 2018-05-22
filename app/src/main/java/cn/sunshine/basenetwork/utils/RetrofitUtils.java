package cn.sunshine.basenetwork.utils;

import android.util.Log;

import java.io.IOException;

import cn.sunshine.basenetwork.ApiService;
import cn.sunshine.basenetwork.config.NetWorkConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wuhe on 2018/3/6.
 */

public class RetrofitUtils {
    private RetrofitUtils() {
    }

    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (RetrofitUtils.class) {
                if (apiService == null) {

                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.i("aaa", "message====" + message);
                        }
                    });
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request();
                                    Log.i("zzz", "request====111111111111111111111111111111");
                                    Log.i("zzz", "request====" + request.headers().toString());
                                    okhttp3.Response proceed = chain.proceed(request);
                                    Log.i("zzz", "proceed====" + proceed.headers().toString());
                                    return proceed;
                                }
                            })
                            .addInterceptor(httpLoggingInterceptor)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(okHttpClient)
                            .baseUrl(NetWorkConfig.API_HOST)
                            .build();
                    apiService = retrofit.create(ApiService.class);
                }
            }
        }

        return apiService;
    }
}
