package com.example.z1310_000.sharedppx.entity;

public class ResponseResult<T> {
    //是否成功
    private Boolean ret = false;
    //返回信息
    private String msg = null;
    //返回状态
    private int code = -100;
    //返回状态对应消息
    private String message = null;
    //指定泛型
    private T data;

    public Boolean getRet() {
        return ret;
    }

    public void setRet(Boolean ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
