package cn.com.pfinfo.weixin.websdk.common.http;

import cn.com.pfinfo.weixin.websdk.common.http.errcode.ErrCodeParser;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.http.GlobalHeaders;
import cn.hutool.http.GlobalInterceptor;
import cn.hutool.http.Header;
import cn.hutool.http.HttpBase;
import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.util.ArrayList;
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
    private static final Log log = Log.get(WxWebHttpUtil.class);
    public static final Map<String, List<ErrCodeParser<?>>> ERR_CODE_PARSER;

    static {
        GlobalHeaders.INSTANCE.header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.81 Safari/537.36 Edg/104.0.1293.47", true);
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

    public static Object loadWxDataByEvalSimpleJS(String html, String key) {
        return loadWxDataByEvalSimpleJS(html, key, "");
    }

    public static Object loadWxDataByEvalSimpleJS(String html, String key, String preJs) {
        String[] lines = html.split("[\r]?\n");
        String[] split = key.split("\\.");
        StringBuilder sb = new StringBuilder(preJs);
        sb.append("\nvar ");
        List<String> initCode = new ArrayList<>(split.length);
        List<String> initParam = new ArrayList<>(split.length);
        for (int i = 0; i < split.length; i++) {
            initCode.add(split[i] + "={}");
            if (i > 0) {
                initParam.add(split[i - 1] + "." + split[i] + "=" + split[i]);
            }
        }
        sb.append(String.join(",", initCode)).append(";\n");
        sb.append(String.join(",", initParam)).append(";\n");
        boolean needAppend = false;
        for (String line : lines) {
            String trimLine = line.trim();
            if (needAppend || trimLine.startsWith(key)) {
                sb.append(line).append("\n");
                needAppend = !trimLine.endsWith(";") && !trimLine.split("//")[0].trim().endsWith(";");
            }
        }
        sb.append("\n").append(key).append(";");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        try {
            return engine.eval(sb.toString());
        } catch (ScriptException e) {
            log.warn(e, "error eval for {}", key);
            return null;
        }
    }

    public static Optional<JSONObject> getCgiFromPage(String html) {
        Object o = loadWxDataByEvalSimpleJS(html, "window.wx.cgiData");
        o = o == null ? loadWxDataByEvalSimpleJS(html, "wx.cgiData") : o;
        return o == null ? Optional.empty() : Optional.of(JSONUtil.parseObj(o));
    }

    public static Optional<JSONObject> getCommonData(String html) {
        String preJs = "function handlerNickname(str, escape) { // 临时对nickname decode\n" +
                "  // var ar=['&','&amp;','<','&lt;','>','&gt;',' ','&nbsp;','\"','&quot;',\"'\",'&#39;','\\\\r','<br>','\\\\n','<br>'];\n" +
                "  var ar = ['&', '&amp;', '<', '&lt;', '>', '&gt;', ' ', '&nbsp;', '\"', '&quot;', '\\'', '&#39;'];\n" +
                "  /*\n" +
                "  // 最新版的safari 12有一个BUG，如果使用字面量定义一个数组，var a = [1, 2, 3]\n" +
                "  // 当调用了 a.reverse() 方法把变量 a 元素顺序反转成 3, 2, 1 后，\n" +
                "  // 即使此页面刷新了， 或者此页面使用 A标签、 window.open 打开的页面，\n" +
                "  // 只要调用到同一段代码， 变量 a 的元素顺序都会变成 3, 2, 1\n" +
                "  // 所以这里不用 reverse 方法\n" +
                "  if (escape === false) {\n" +
                "    ar.reverse();\n" +
                "  }*/\n" +
                "  var arReverse = ['&#39;', '\\'', '&quot;', '\"', '&nbsp;', ' ', '&gt;', '>', '&lt;', '<', '&amp;', '&'];\n" +
                "  var target;\n" +
                "  if (escape === false) {\n" +
                "    target = arReverse;\n" +
                "  } else {\n" +
                "    target = ar;\n" +
                "  }\n" +
                "  var r = str;\n" +
                "  for (var i = 0; i < target.length; i += 2) {\n" +
                "    r = r.replace(new RegExp(target[i], 'g'), target[1 + i]);\n" +
                "  }\n" +
                "  return r;\n" +
                "};";
        Object o = loadWxDataByEvalSimpleJS(html, "window.wx.commonData", preJs);
        return o == null ? Optional.empty() : Optional.of(JSONUtil.parseObj(o));
    }
}
