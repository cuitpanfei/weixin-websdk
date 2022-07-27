package cn.com.pfinfo.weixin.websdk.common.event;

import java.util.EventObject;
import java.util.Optional;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxSdkEvent<Source> extends EventObject {


    protected WxSdkEvent(Source source) {
        super(Optional.ofNullable(source));
    }

    public static <T> WxSdkEvent<T> of(T source) {
        return new WxSdkEvent<>(source);
    }


    @Override
    public Optional<Source> getSource() {
        return (Optional<Source>) super.getSource();
    }


}
