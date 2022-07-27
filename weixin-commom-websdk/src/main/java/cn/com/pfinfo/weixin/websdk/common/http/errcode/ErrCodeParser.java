package cn.com.pfinfo.weixin.websdk.common.http.errcode;

/**
 * 错误码解析器
 * <p>
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public interface ErrCodeParser<T> {

    /**
     * 错误码解析器所属应用
     *
     * @return 解析器所属应用
     * @see cn.com.pfinfo.weixin.websdk.common.consts.WxConsts.AppType
     */
    String appType();

    /**
     * 错误码解析器的名称
     *
     * @return 解析器的名称
     */
    String name();

    /**
     * 错误码解析器的核心功能函数，解析错误码为对应详细信息
     *
     * @param code 错误码
     * @param <T>  错误码的类型，因为有的错误码可能表现为字符类型，枚举类型或者数字类型，所以这里直接使用范型进行兼容
     * @return 错误码为对应详细信息
     */
    String msg4ErrCode(T code);

}
