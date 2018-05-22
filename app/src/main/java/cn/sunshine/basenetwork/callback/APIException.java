package cn.sunshine.basenetwork.callback;

/**
 * 自定义异常
 */
public class APIException extends RuntimeException {

    private int code;

    // 用于展示的异常信息
    private String displayMessage;

    public APIException(int code, Throwable throwable) {
        this(code, null, throwable);
    }

    public APIException(int code, String displayMessage) {
        this(code, displayMessage, null);
    }

    public APIException(int code, String displayMessage, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }
}
