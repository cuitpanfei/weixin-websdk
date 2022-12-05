package cn.com.pfinfo.weixin.websdk.mp.api.mp.login;

import cn.com.pfinfo.weixin.websdk.common.enums.QrCodeScanState;
import cn.com.pfinfo.weixin.websdk.common.event.EventManager;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.WxMpWebService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.event.QrCodeScanEvent;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static cn.com.pfinfo.weixin.websdk.mp.api.mp.WxMpWebService.BASE_URL;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxMpWebLoginServiceImpl implements WxMpWebLoginService {
    public static final String SCAN_LOGIN_QRCODE_BASE_URL = BASE_URL + "/scanloginqrcode";
    public static final String BIZ_LOGIN_BASE_URL = BASE_URL + "/bizlogin";
    public static final String APPLICATION_FORM_URLENCODED_VALUE = ContentType.FORM_URLENCODED.toString(StandardCharsets.UTF_8);
    private static final Log log = LogFactory.get(WxMpWebLoginServiceImpl.class);

    static {
        Singleton.put(WxMpWebLoginService.class.getName(), Singleton.get(WxMpWebLoginServiceImpl.class));
    }

    private void prelogin() {
        WxWebHttpUtil.get(WxMpWebService.MP_WEIXIN_QQ_COM);
        WxWebHttpUtil.createPost(BIZ_LOGIN_BASE_URL)
                .body("action=prelogin&token=&lang=zh_CN&f=json&ajax=1", APPLICATION_FORM_URLENCODED_VALUE)
                .execute();
    }

    private void startlogin() {
        WxWebHttpUtil.createPost(UrlBuilder.of(BIZ_LOGIN_BASE_URL).addQuery("action", "startlogin").build())
                .body("userlang=zh_CN&redirect_url=&login_type=3&sessionid=" + System.currentTimeMillis()
                        + "0&token=&lang=zh_CN&f=json&ajax=1", APPLICATION_FORM_URLENCODED_VALUE)
                .execute();
    }

    @Override
    public String getLoginQRCodeDecodeLinkForce() {
        if (!MpApp.getAppId().isPresent()) {
            MpApp.updateAppId(RandomUtil.randomString(8));
        }
        prelogin();
        startlogin();
        return doGetLoginQRCodeDecodeLink();
    }

    @Override
    public String getLoginQRCodeDecodeLink() {
        Optional<String> info = QrCodeScanState.qrcodeInfoOf(MpApp.appId());
        return info.orElseGet(this::getLoginQRCodeDecodeLinkForce);
    }

    String doGetLoginQRCodeDecodeLink() {
        String url = UrlBuilder.of(SCAN_LOGIN_QRCODE_BASE_URL)
                .addQuery("action", "getqrcode")
                .addQuery("random", System.currentTimeMillis())
                .build();
        HttpRequest request = WxWebHttpUtil.createGet(url);
        HttpResponse response = request.cookie("wxuin=" + WxWebHttpUtil.getNewWxUin())
                .executeAsync();
        String qrcodeInfo = CharSequenceUtil.EMPTY;
        if (response.isOk()) {
            File file = FileUtil.writeFromStream(response.bodyStream(), "qrcodeInfo.png");
            qrcodeInfo = QrCodeUtil.decode(FileUtil.getInputStream(file));
            QrCodeScanState.qrcodeInfo(MpApp.appId(), qrcodeInfo);
            EventManager.publish(new QrCodeScanEvent(scanLoginQrCodeAck()));
        }
        return qrcodeInfo;
    }

    @Override
    public QrCodeScanState scanLoginQrCodeAck() {
        if (MpApp.hasCookie()) {
            String url = UrlBuilder.of(SCAN_LOGIN_QRCODE_BASE_URL)
                    .addQuery("action", "ask")
                    .addQuery("lang", "zh_CN")
                    .addQuery("f", "json")
                    .addQuery("ajax", 1)
                    .build();
            HttpResponse response = WxWebHttpUtil.createGet(url).executeAsync();
            String jsonResult = response.body();
            JSONObject obj = JSONUtil.parseObj(jsonResult);
            Integer retCode = obj.getByPath("base_resp.ret", Integer.class);
            if (retCode != null && retCode == 0) {
                int status = obj.getInt("status");
                return QrCodeScanState.statusOf(status);
            } else {
                log.info("ack 发现扫码结果失败, result=[{}]", jsonResult);
            }
        }
        return QrCodeScanState.UN_KNOW;
    }

    @Override
    public void bizLogin() {
        String url = UrlBuilder.of(BIZ_LOGIN_BASE_URL)
                .addQuery("action", "login")
                .build();
        HttpResponse response = WxWebHttpUtil.createPost(url)
                .body("userlang=zh_CN&redirect_url=&cookie_forbidden=0&cookie_cleaned=1&plugin_used=0&login_type=3&token=&lang=zh_CN&f=json&ajax=1", APPLICATION_FORM_URLENCODED_VALUE)
                .executeAsync();
        if (response.isOk()) {
            String jsonResult = response.body();
            String redirectUrl = JSONUtil.parseObj(jsonResult).getStr("redirect_url");
            if (redirectUrl != null) {
                String token = redirectUrl.replaceAll("\\D+(\\d+)\\D*", "$1");
                MpApp.setToken(token);
            }
            WxWebHttpUtil.createGet(WxMpWebService.MP_WEIXIN_QQ_COM + redirectUrl)
                    .header(Header.REFERER, redirectUrl)
                    .executeAsync();
        }
    }
}
