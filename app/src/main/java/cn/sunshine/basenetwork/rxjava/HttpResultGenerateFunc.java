package cn.sunshine.basenetwork.rxjava;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sunshine.basenetwork.callback.APIException;
import cn.sunshine.basenetwork.callback.APIResult;
import cn.sunshine.basenetwork.callback.ErrorCode;
import cn.sunshine.basenetwork.callback.ExceptionManager;
import io.reactivex.functions.Function;
import okhttp3.Headers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import timber.log.Timber;

/**
 * Json data 数据规则配置及服务器返回的异常处理
 * 针对各自的项目 进行解析 Json 处理
 */

public class HttpResultGenerateFunc implements Function<Result<JSONObject>, APIResult> {

    @Override
    public APIResult apply(Result<JSONObject> httpResult) throws Exception {
        // 因为所有的请求都会返回 response 不管成功还是失败，所以要先处理异常
        APIException ex = ExceptionManager.handleException(httpResult);
        if (ex != null) {
            throw ex;
        }

        // 请求成功解析数据
        Response<JSONObject> response = httpResult.response();
        System.out.println("这里是处理接口返回的数据");
        Timber.d("NetWork HttpResult : %s", response.body().toString());
        JSONObject body = response.body();
        Headers headers = response.headers();
        //status_code 不存在 默认为status_code = 0
        if (!body.has("code")) {
            try {
                body.put("code", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (body.has("code")) {
            try {
                if (body.getInt("code") != 200) {
                    // 服务器返回 error msg
                    if (body.has("msg")) {
                        throw new APIException(body.getInt("code"), body.getString("msg"));
                    } else {
                        throw new APIException(ErrorCode.PARSE_ERROR, "Has error code but has no message!");
                    }
                }

                checkData(body);

                body = convertToJsonObject(body);

                // 返回
                return new APIResult(headers, body.getJSONObject("data"));

            } catch (JSONException e) {
                throw new APIException(ErrorCode.PARSE_ERROR, e);
            }

        } else {
            throw new APIException(ErrorCode.PARSE_ERROR, "status_code none!");
        }
    }

    private JSONObject convertToJsonObject(JSONObject httpResult) throws JSONException {
        Object object = httpResult.get("data");

        if (object instanceof JSONArray) {
            JSONObject dataJsonObject = new JSONObject();
            // It's an array
            JSONArray jsonArray = (JSONArray) object;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list", jsonArray);

            dataJsonObject.put("data", jsonObject);
            return dataJsonObject;
        }
        return httpResult;
    }

    private JSONObject checkData(JSONObject httpResult) throws JSONException {
        if (!httpResult.has("data")) {
            JSONObject jsonObject = new JSONObject();
            httpResult.put("data", jsonObject);
        }

        Timber.d("NetWork HttpResult : %s", httpResult.toString());
        return httpResult;
    }

}
