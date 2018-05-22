package com.example.wuhe.myapplication.bean;

/**
 * Created by zhangyipeng on 16/6/30.
 */
public class BaseInfo {

    private int code;
    private String success;
    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
