package cn.sunshine.basenetwork.utils;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by wuhe on 2018/3/6.
 */

public class OkHttpUtils {
    /**
     *  安全 加同步
     * 私有的静态成员变量 只声明不创建
     * 私有的构造方法
     * 提供返回实例的静态方法
     */
    private static OkHttpClient okHttpClient = null;


    public OkHttpUtils() {
    }

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            //加同步安全
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    //判空 为空创建实例
                    // okHttpClient = new OkHttpClient();
                    /**
                     * 和OkHttp2.x有区别的是不能通过OkHttpClient直接设置超时时间和缓存了，而是通过OkHttpClient.Builder来设置，
                     * 通过builder配置好OkHttpClient后用builder.build()来返回OkHttpClient，
                     * 所以我们通常不会调用new OkHttpClient()来得到OkHttpClient，而是通过builder.build()：
                     */
                     //  File sdcache = getExternalCacheDir();
                    File sdcache = new File(Environment.getExternalStorageDirectory(), "cache");
                    int cacheSize = 10 * 1024 * 1024;
                    okHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).cache(new Cache(sdcache.getAbsoluteFile(), cacheSize)).build();
                }
            }

        }

        return okHttpClient;
    }

}
