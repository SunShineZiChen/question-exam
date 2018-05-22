package cn.sunshine.basenetwork.callback;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by wuhe on 2018/3/8.
 */

public abstract class HttpCallbackT<T> implements retrofit2.Callback<T>{
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T body = response.body();
            try {
                Timber.e("Http OnSuccess 成功：%s", response.body().toString());
                onSuccess(body);
            } catch (Exception e) {
                //            e.printStackTrace();
                Timber.e("Http OnSuccess 执行异常：%s", e.toString());
                throw new APIException(ErrorCode.UNKNOWN, "");
            }
        } else {
            onError(new APIException(ErrorCode.UNKNOWN, ""));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(new APIException(ErrorCode.NETWORK_ERROR,t));
    }
    /**
     * 主线程,处理成功
     *
     * @param data 成功的数据
     */
    public abstract void onSuccess(T data) throws Exception;
    /**
     * 主线程,处理失败
     */
    public abstract void onError(APIException e) ;
}
