package cn.sunshine.basenetwork.callback;

public class ErrorCode {

    //////////////////////////////////////////
    // 本地错误代码  不可与后台的错误码重合
    /**
     * 未知错误
     */
    public static final int UNKNOWN = -10001;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = -10002;
    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = -10003;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = -10004;

    /**
     * View 没有绑定到 Presenter
     */
    public static final int VIEW_NOT_ATTACHED = -10005;

    /**
     * 解析错误
     */
    public static final int SQL_ERROR = -10006;

    /**
     * View 没有绑定到 Presenter
     */
    public static final int API_NO_CALL_BACK = -10007;

    /**
     * 未验证
     */
    public static final int UNAUTHORIZED = -10008;

}
