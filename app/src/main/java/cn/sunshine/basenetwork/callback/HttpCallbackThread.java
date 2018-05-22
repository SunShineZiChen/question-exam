package cn.sunshine.basenetwork.callback;

/**
 * Created by caiqikao on 17/10/26.
 * PackageName com.wuhe.network
 */

public abstract class HttpCallbackThread<T> extends HttpCallback<T> {

    /**
     * 子线程中完成
     *
     * @param apiResult http 返回数据
     * @return 解析完成的返回结果
     */
    public abstract T onProcessData(APIResult apiResult) throws Exception;

}
