package cn.sunshine.basenetwork.config;


/**
 * Created by SunShine on 2018/3/7.
 */

public class NetWorkConfig {
    public static boolean IS_PRINT_LOG; // 是否需要打印日志
    public static String API_HOST; // Api 服务器地址
    private static volatile NetWorkConfig instance = null;

    private NetWorkConfig() {
    }
    public static NetWorkConfig getInstance() {
        if (instance == null) {
            synchronized (NetWorkConfig.class) {
                if (instance == null) {
                    instance = new NetWorkConfig();
                }
            }
        }
        return instance;
    }

    public NetWorkConfig setAPIUrl(String url) {
        API_HOST = url;
        return this;
    }

}
