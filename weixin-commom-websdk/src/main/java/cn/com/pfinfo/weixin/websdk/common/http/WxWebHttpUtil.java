package cn.com.pfinfo.weixin.websdk.common.http;

import cn.com.pfinfo.weixin.websdk.common.http.errcode.ErrCodeParser;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.http.GlobalInterceptor;
import cn.hutool.http.HttpBase;
import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxWebHttpUtil extends HttpUtil {
    public static final Map<String, List<ErrCodeParser>> ERR_CODE_PARSER;

    static {
        ERR_CODE_PARSER = Collections.unmodifiableMap(loadAllErrCodeParser());
        ERR_CODE_PARSER.forEach((appType,list)->list.sort(Comparator.comparing(ErrCodeParser::appType).thenComparing(ErrCodeParser::name)));
        addHttpInterceptor();
    }

    static void addHttpInterceptor() {
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(CharSequenceUtil.EMPTY, WxWebHttpInterceptor.class);
        for (Class<?> clazz : classes) {
            if (clazz.isInterface()) {
                continue;
            }
            Type type = TypeUtil.getTypeArgument(clazz);
            WxWebHttpInterceptor<? extends HttpBase<?>> interceptor = (WxWebHttpInterceptor<?>) Singleton.get(clazz);
            if (type == HttpRequest.class) {
                GlobalInterceptor.INSTANCE.addRequestInterceptor((HttpInterceptor<HttpRequest>) interceptor);
            } else if (type == HttpResponse.class) {
                GlobalInterceptor.INSTANCE.addResponseInterceptor((HttpInterceptor<HttpResponse>) interceptor);
            }
        }

    }

    static Map<String, List<ErrCodeParser>> loadAllErrCodeParser() {
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(CharSequenceUtil.EMPTY, ErrCodeParser.class);
        return classes.stream().filter(clazz -> !clazz.isInterface())
                .map(clazz -> (ErrCodeParser) Singleton.get(clazz))
                .collect(Collectors.groupingBy(ErrCodeParser::appType));
    }

    public static String getNewWxUin() {
        return String.valueOf(System.nanoTime()).substring(2);
    }
}
