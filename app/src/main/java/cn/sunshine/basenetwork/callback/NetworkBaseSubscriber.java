package cn.sunshine.basenetwork.callback;


import cn.sunshine.basenetwork.utils.NetWorkStrUtil;
import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public abstract class NetworkBaseSubscriber<T> extends DefaultObserver<T> {

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        buildAPIException(e);
    }

    protected void buildAPIException(Throwable e) {
        APIException ex;
        if (e instanceof APIException) {
            ex = (APIException) e;
        } else {
            ex = new APIException(ErrorCode.UNKNOWN, e);
        }

        // 这里处理所有错误逻辑
        int errorCode = ex.getCode();
        String errorDisplayMsg = ex.getDisplayMessage();

        Timber.e("NetWork DefaultSubscriber ErrorCode: %d \nDisplay Message: %s \nMessage: %s", errorCode, ex.getDisplayMessage(), ex.getMessage());
//        ex.printStackTrace(System.err);

        // 没有错误信息的给一个默认信息
        if (NetWorkStrUtil.isEmpty(errorDisplayMsg)) {
            ex.setDisplayMessage("出现异常");
        }
        //处理特殊情况，如token过期直接退出app等
        if (!processErrorCode(errorCode, errorDisplayMsg))
            return;

        if (errorCode != ErrorCode.VIEW_NOT_ATTACHED) {
            try {
                onError(ex);
            } catch (Exception e1) {
                Timber.e("Http OnError 执行异常：%s", e.toString());
            }
        }
    }

    /**
     * @return 额外处理后返回false 则表示不再往下执行
     */
    protected boolean processErrorCode(int errorCode, String errorDisplayMsg) {

        return true;
    }

    /**
     * 错误回调
     */
    protected abstract void onError(APIException ex) throws Exception;
}
