package cn.sunshine.basenetwork.callback;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

/**
 * 网络请求Http status_code 200 以外的异常处理
 */
public class ExceptionManager {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private static final int SUCCESS = 200;

    public static APIException handleException(Result<JSONObject> result) {

        if (result.isError()) {
            return new APIException(ErrorCode.NETWORK_ERROR, "网络连接失败", result.error());
        }

        APIException ex = null;

        Response<JSONObject> response = result.response();
        int errorCode = response.code();
        if (errorCode != SUCCESS) {
            try {
                String errorMsg = response.errorBody().string();
                Throwable throwable = new Throwable(errorMsg);

                switch (errorCode) {
                    case UNAUTHORIZED:
                        ex = new APIException(ErrorCode.UNAUTHORIZED, "用户未验证", throwable);
                        break;
                    case FORBIDDEN:
                    case NOT_FOUND:
                    case REQUEST_TIMEOUT:
                    case GATEWAY_TIMEOUT:
                    case INTERNAL_SERVER_ERROR:
                    case BAD_GATEWAY:
                    case SERVICE_UNAVAILABLE:
                    default:
                        ex = new APIException(ErrorCode.HTTP_ERROR, "网络错误", throwable);
                        break;
                }

            } catch (IOException e) {
                ex = new APIException(ErrorCode.PARSE_ERROR, e);
            }

        }

        return ex;
    }

}
