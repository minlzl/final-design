package com.example.finalend.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class Resp<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public static <T> Resp<T> of(int code, String msg, T data) {
        final Resp<T> resp = new Resp<>();
        resp.code = code;
        resp.msg = msg;
        resp.data = data;
        return resp;
    }

    public static <T> Resp<T> success() {
        return success(null);
    }

    public static <T> Resp<T> success(T data) {
        return of(SUCCESS, "操作成功", data);
    }

    public static <T> Resp<T> failure(String msg) {
        return of(FAILURE, msg, null);
    }

    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int NO_LOGIN = 2;
    public static final int NO_PERMISSION = 3;
    public static final int PARAMETER_VALIDATION_FAILED = 4;
    public static final int MALFORMED_REQUEST = 5;
    public static final int UNKNOWN_ERROR = 6;
}
