package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.http.R;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login.entity.RedirectInfo;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.LoginTip;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatContext;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.net.RFC3986;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import org.stringtemplate.v4.ST;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
public class WxWechatWebLoginServiceImpl implements WxWechatWebLoginService {
    private static final Log log = LogFactory.get(WxWechatWebLoginServiceImpl.class);
    /**
     * 预编译正则匹配
     */
    private static Pattern PATTERN_UUID_1 = Pattern.compile("window.QRLogin.code = (\\d+);");
    private static Pattern PATTERN_UUID_2 = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";");
    private static Pattern PATTERN_REDIRECT_URI_1 = Pattern.compile("window.code=(\\d+);");
    private static Pattern PATTERN_REDIRECT_URI_2 = Pattern.compile("window.code=(\\d+);\\s*window.redirect_uri=\"(\\S+?)\";");
    private static Pattern PATTERN_REDIRECT_URI_3 = Pattern.compile("http(s*)://wx(\\d*)\\.qq\\.com/");
    protected Setting webWxSetting = WechatApp.appSetting.getSetting("webwx");
    protected Setting loginSetting = WechatApp.appSetting.getSetting("login");

    @Override
    public JSONObject wxUuid() {
        String url = new ST(loginSetting.getStr("uuid_url"))
                .add("appid", webWxSetting.get("appid"))
                .add("_", System.currentTimeMillis())
                .render();
        String res = HttpUtil.get(url);
        Matcher matcher = PATTERN_UUID_1.matcher(res);
        if (!matcher.find()) {
            throw ExceptionUtil.wrapRuntime("返回数据错误");
        }

        String code = matcher.group(1);
        JSONObject result = new JSONObject();
        result.putOnce("code", code);
        if (!"200".equals(code)) {
            result.putOnce("msg", "错误代码(" + code + ")，请确认appid是否有效");
            return result;
        }

        matcher = PATTERN_UUID_2.matcher(res);
        if (!matcher.find()) {
            throw ExceptionUtil.wrapRuntime("没有匹配到uuid");
        }

        String uuid = matcher.group(2);
        result.putOnce(WxConsts.ReqFiled.UUID, uuid);
        if (CharSequenceUtil.isEmpty(uuid)) {
            throw ExceptionUtil.wrapRuntime("获取的uuid为空");
        }

        return result;
    }

    @Override
    public byte[] qrCode(String uuid) {
        String url = new ST(loginSetting.get("qrcode_url"))
                .add(WxConsts.ReqFiled.UUID, uuid)
                .render();
        return HttpUtil.createGet(url).execute()
                .bodyBytes();
    }

    @Override
    public R<RedirectInfo> redirect(String uuid, LoginTip tip) {
        long millis = System.currentTimeMillis();
        String url = new ST(loginSetting.get("redirect_uri"))
                .add("tip", tip.getCode())
                .add(WxConsts.ReqFiled.UUID, uuid)
                .add("r", millis / 1252L)
                .add("_", millis)
                .render();
        String res = HttpUtil.get(url);
        Matcher matcher = PATTERN_REDIRECT_URI_1.matcher(res);
        if (!matcher.find()) {
            return R.fail("返回数据错误");
        }
        String code = matcher.group(1);
        R<RedirectInfo> result = R.ok();
        result.code(code);
        if ("408".equals(code)) {
            result.message("请扫描二维码");
        } else if ("400".equals(code)) {
            result.message("二维码失效");
        } else if ("201".equals(code)) {
            result.message("请在手机上点击确认");
        } else if ("200".equals(code)) {
            matcher = PATTERN_REDIRECT_URI_2.matcher(res);
            if (!matcher.find()) {
                return R.fail("没有匹配到跳转uri");
            }
            RedirectInfo.RedirectInfoBuilder builder = RedirectInfo.builder();
            String redirectUri = matcher.group(2);
            result.message("手机确认成功");
            builder.redirectUri(redirectUri);
            matcher = PATTERN_REDIRECT_URI_3.matcher(redirectUri);
            if (!matcher.find()) {
                return R.fail("从跳转uri中没有匹配到url版本号");
            }
            String urlVersion = matcher.group(2);
            builder.urlVersion(urlVersion);
            result.payload(builder.build());
        } else {
            return R.fail("返回code错误");
        }
        return result;
    }

    @Override
    public JSONObject newlogin(String redirectUri) {
        String url = new ST(loginSetting.getStr("newlogin_url"))
                .add(WxConsts.ReqFiled.REDIRECT_URI, redirectUri)
                .render();
        String res = HttpUtil.get(url);
        JSONObject jsonObject = JSONUtil.parseFromXml(res);
        jsonObject.getConfig().setIgnoreCase(true);
        return jsonObject.getJSONObject("error");
    }

    @Override
    public void logout() {
        String logoutUrl = loginSetting.getStr("logout_url");
        WechatContext context = WechatApp.getContext();
        for (int i = 0; i < 2; i++) {
            String url = new ST(logoutUrl)
                    .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                    .add(WxConsts.ReqFiled.TYPE, i)
                    .add(WxConsts.ReqFiled.SKEY, RFC3986.FRAGMENT.encode(context.skey, StandardCharsets.UTF_8))
                    .render();
            HttpUtil.createPost(url)
                    .form(WxConsts.ReqFiled.SID,context.wxsid)
                    .form(WxConsts.ReqFiled.UIN,context.wxuin)
                    .execute();
        }
    }

    @Override
    public JSONObject pushLogin() {
        WechatContext context = WechatApp.getContext();
        String url = new ST(loginSetting.getStr("pushlogin_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .add(WxConsts.ReqFiled.UIN, context.wxuin)
                .render();
        return JSONUtil.parseObj(HttpUtil.get(url));
    }
}
