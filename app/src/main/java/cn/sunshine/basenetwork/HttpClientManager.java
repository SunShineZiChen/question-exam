package cn.sunshine.basenetwork;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.sunshine.basenetwork.config.NetWorkConfig;
import cn.sunshine.basenetwork.utils.NetWorkDateUtil;
import cn.sunshine.basenetwork.utils.NetWorkStrUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import timber.log.Timber;

/**
 * 网络请求管理
 */
public class HttpClientManager {

    private static final String TAG = "HttpClient";

    // 读取超时时间
    private static final int READ_TIMEOUT = 1000 * 1000;
    // 连接超时时间
    private static final int CONNECT_TIMEOUT = 5 * 1000;

    private static HttpClientManager instance;

    private Retrofit retrofit;
    private Map<Class, Object> apiMap;

    private String token;
    private String serverTime;
    private long timeDelay;

    public synchronized static HttpClientManager getInstance() {
        if (instance == null) {
            instance = new HttpClientManager();
        }
        return instance;
    }

    public Object getMainAPI(Class apiClass) {
        Object mainAPI;

        if (retrofit == null) {
            init();
        }

        if (apiMap == null) {
            apiMap = new HashMap<>();
        }

        if (apiMap.get(apiClass) == null) {
            mainAPI = retrofit.create(apiClass);
            apiMap.put(apiClass, mainAPI);
        } else {
            mainAPI = apiMap.get(apiClass);
        }

        return mainAPI;
    }

    public void reset() {
        instance = null;
    }


    public Retrofit getRetrofit() {
        if (retrofit == null) {
            init();
        }
        return retrofit;
    }
    public ApiService getApiService(){
        return getRetrofit().create(ApiService.class);
    }
    public long getTimeDelay() {
        return timeDelay;
    }

    public String getServerTime() {
        return NetWorkStrUtil.isEmpty(serverTime) ? NetWorkDateUtil.getSystemTime() : serverTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String getToken() {
        return NetWorkStrUtil.isEmpty(token) ? "" : token;
    }

    HttpClientManager() {
        init();
    }

    private void init() {
        if (retrofit == null) {
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request originalRequest = chain.request();
                    // 设置 token
                    Request buildRequest = originalRequest.newBuilder()
                            .header("Authorization", getToken())//认证信息
                            .build();

                    // 请求返回
                    Response originalResponse = chain.proceed(buildRequest);

                    // 更新本地与服务器时间差值
                    updateLocalTimeDelay(originalResponse.header("Date"));

                    return originalResponse;

                }
            };

            OkHttpClient okHttpClient;

            if (NetWorkConfig.IS_PRINT_LOG) {

                Timber.d("NetWork Debug interceptor Log init in HttpClient.");

                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                        .addInterceptor(httpLoggingInterceptor)
                        .addInterceptor(interceptor)
                        .build();
            } else {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                        .addInterceptor(interceptor)
                        .build();
            }
            retrofit = createRetrofit(okHttpClient);

        }
    }
    public Retrofit createRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NetWorkConfig.API_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JSONConverterFactory.create())
                .build();
    }

    // 每次访问 API 的时候更新服务器时间
    private void updateLocalTimeDelay(String headerDate) {
        String serverTime = NetWorkDateUtil.convertToLocal(headerDate);
        if (!NetWorkStrUtil.isEmpty(serverTime)) {
            this.serverTime = serverTime;
            Date serverDate = NetWorkDateUtil.getDateByStr(serverTime);
            if (serverDate != null) {
                timeDelay = NetWorkDateUtil.getTimeDelay(serverDate, new Date());
            }
        }

        Timber.d("NetWork [UPDATE SERVER TIME] Server Time: %s , Time Delay : %s", serverTime, timeDelay);
    }


}
