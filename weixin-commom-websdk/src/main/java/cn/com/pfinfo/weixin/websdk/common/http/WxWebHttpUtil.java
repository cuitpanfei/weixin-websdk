package cn.com.pfinfo.weixin.websdk.common.http;

import cn.com.pfinfo.weixin.websdk.common.http.errcode.ErrCodeParser;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.http.GlobalInterceptor;
import cn.hutool.http.HttpBase;
import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxWebHttpUtil extends HttpUtil {
    public static final Map<String, List<ErrCodeParser<?>>> ERR_CODE_PARSER;

    static {
        Comparator<ErrCodeParser<?>> comparator = Comparator.comparing(ErrCodeParser::appType);
        Comparator<ErrCodeParser<?>> comparator2 = Comparator.comparing(ErrCodeParser::name);
        ERR_CODE_PARSER = Collections.unmodifiableMap(loadAllErrCodeParser());
        ERR_CODE_PARSER.forEach((appType, list) -> list.sort(comparator.thenComparing(comparator2)));
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

    static Map<String, List<ErrCodeParser<?>>> loadAllErrCodeParser() {
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(CharSequenceUtil.EMPTY, ErrCodeParser.class);
        return classes.stream().filter(clazz -> !clazz.isInterface())
                .map(clazz -> (ErrCodeParser<?>) Singleton.get(clazz))
                .collect(Collectors.groupingBy(ErrCodeParser::appType));
    }

    public static String getNewWxUin() {
        return String.valueOf(System.nanoTime()).substring(2);
    }


    /**
     * 0到1范围内的随机数，小数位为16位
     *
     * @return 小数位为16位的随机数
     */
    public static double random() {
        return RandomUtil.randomDouble(1, 16, RoundingMode.HALF_UP);
    }

    public static Optional<JSONObject> getCgiFromPage(String html) {
        String[] lines = html.split("[\r]?\n");
        JSONObject obj = new JSONObject();
        Arrays.stream(lines).filter(line -> line.trim().startsWith("wx.cgiData"))
                .forEach(line -> {
                    if (line.trim().startsWith("wx.cgiData =") && line.trim().endsWith("};")) {
                        String value = line.replaceAll("^[ ]+wx\\.cgiData =(.*?);", "$1");
                        obj.putAll(JSONUtil.parseObj(value));
                    } else {
                        String key = line.replaceAll("^[ ]+wx\\.cgiData\\.(.*?) =(.*?);", "$1");
                        String valueStr = line.replaceAll("^[ ]+wx\\.cgiData\\.(.*?) =(.*?);", "$2");
                        Object value;
                        if (valueStr.contains("||")) {
                            valueStr = valueStr.split("\\|\\|")[0].trim();
                        }
                        if (valueStr.contains("*")) {
                            String[] split = valueStr.replaceAll("\"|'", "").split("\\*");
                            value = Arrays.stream(split).mapToInt(Integer::valueOf).reduce((l, r) -> l * r).getAsInt();
                        } else {
                            value = valueStr;
                        }
                        obj.append(key, value);
                    }
                });
        return obj.isEmpty() ? Optional.empty() : Optional.ofNullable(obj);
    }

    public static Optional<JSONObject> getCommonData(String html) {
        String[] lines = html.split("[\r]?\n");
        JSONObject obj = new JSONObject();
        return obj.isEmpty() ? Optional.empty() : Optional.ofNullable(obj);
    }
}
