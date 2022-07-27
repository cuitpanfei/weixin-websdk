package cn.com.pfinfo.weixin.websdk.common.exception;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class RRException extends RuntimeException {


    public RRException() {
    }

    public RRException(String message, int code) {
        this(message);
        this.code = code;
    }

    public RRException(String message) {
        super(message);
    }

    public RRException(String message, Throwable cause) {
        super(message, cause);
    }

    public RRException(Throwable cause) {
        super(cause);
    }

    public RRException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private int code;

    public int getCode() {
        return code;
    }
}
