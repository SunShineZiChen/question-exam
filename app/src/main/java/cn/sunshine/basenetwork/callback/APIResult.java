package cn.sunshine.basenetwork.callback;

import org.json.JSONObject;

import cn.sunshine.basenetwork.utils.NetWorkDateUtil;
import cn.sunshine.basenetwork.utils.NetWorkStrUtil;
import okhttp3.Headers;

public class APIResult {
    private Headers headers;
    private JSONObject body;

    public APIResult(Headers headers, JSONObject body) {
        this.headers = headers;
        this.body = body;
    }

    public Headers getHeaders() {
        return headers;
    }

    public JSONObject getBody() {
        return body;
    }

    public String getServerTime() {
        if (headers != null) {
            String date = headers.get("Date");
            if (!NetWorkStrUtil.isEmpty(date)) {
                return NetWorkDateUtil.convertToLocal(date);
            }
        }
        return NetWorkDateUtil.getSystemTime();
    }
}
