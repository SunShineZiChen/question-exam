package cn.sunshine.basenetwork.callback;

import timber.log.Timber;

public abstract class HttpCallback<T> extends NetworkBaseSubscriber<T> {


    /**
     * 主线程，处理失败
     *
     * @param ex
     */
    @Override
    protected void onError(APIException ex) {

    }

    @Override
    public void onNext(T o) {
        try {
            onSuccess(o);
        } catch (Exception e) {
//            e.printStackTrace();
            Timber.e("Http OnSuccess 执行异常：%s", e.toString());
            throw new APIException(ErrorCode.UNKNOWN, "");
        }
    }

    /**
     * 主线程,处理成功
     *
     * @param data 成功的数据
     */
    public abstract void onSuccess(T data) throws Exception;

}
