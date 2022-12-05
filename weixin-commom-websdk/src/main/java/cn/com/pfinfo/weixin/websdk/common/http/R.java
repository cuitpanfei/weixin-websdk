package cn.com.pfinfo.weixin.websdk.common.http;

import lombok.Builder;
import lombok.Data;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
@Data
public class R<T> {

    /**
     * Server response data
     */
    private T payload;

    /**
     * The request was successful
     */
    private boolean success;

    /**
     * Error message
     */
    private String msg;

    /**
     * Status code
     */
    @Builder.Default
    private int code = 0;

    public R() {
    }

    public R(boolean success) {
        this.success = success;
    }

    public R(boolean success, T payload) {
        this.success = success;
        this.payload = payload;
    }

    public static <T> R<T> ok() {
        return new R<T>().success(true);
    }

    public static <T> R<T> ok(T payload) {
        return new R<T>().success(true).payload(payload);
    }

    public static <T> R ok(T payload, int code) {
        return new R<T>().success(true).payload(payload).code(code);
    }

    public static <T> R<T> fail() {
        return new R<T>().success(false);
    }

    public static <T> R<T> fail(String message) {
        return new R<T>().success(false).message(message);
    }

    public static <T> R fail(int code, String message) {
        return new R<T>().success(false).message(message).code(code);
    }

    public T getPayload() {
        return payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public R<T> peek(Runnable runnable) {
        runnable.run();
        return this;
    }

    public R<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public R<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    public R<T> code(int code) {
        this.code = code;
        return this;
    }

    public R<T> code(String code) {
        return this.code(Integer.parseInt(code));
    }

    public R<T> message(String msg) {
        this.msg = msg;
        return this;
    }
}
