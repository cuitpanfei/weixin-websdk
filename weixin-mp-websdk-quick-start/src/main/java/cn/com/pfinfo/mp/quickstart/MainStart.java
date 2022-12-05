package cn.com.pfinfo.mp.quickstart;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.WxWebService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.WxMpWebService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.WxMpWebDraftService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.ArticleModel;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.DraftModel;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.material.WxMpWebMaterialService;
import cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * created by cuitpanfei on 2022/08/11
 *
 * @author cuitpanfei
 */
public class MainStart {
    public static final Log log = LogFactory.get(MainStart.class);

    public static void premain() {
        WxWebHttpUtil.getNewWxUin();
        MpApp.updateAppId("1354636841");
        MpApp.setToken("1354636841");
        MpApp.updateCookie("appmsglist_action_3895151075=card; ua_id=wrkIJwT6jVoKslDGAAAAALKpokWAlfb2vNhPNFgTYzE=; mm_lang=zh_CN; pgv_pvid=4368920818; iip=0; pac_uid=0_f871b711bd216; wxuin=47583230377848; fqm_pvqid=15fdc9a7-dd1a-4393-8e72-4b92e8bcc388; noticeLoginFlag=1; tvfe_boss_uuid=869138f5f8b0fa98; sd_userid=38171660295863407; sd_cookie_crttime=1660295863407; uuid=61962d6a1bc3b7a6b14081aadfb90009; rand_info=CAESILwmKhQThCrbEiEZZXzWjXGKKRkEhvCTa7eezJF0mabM; slave_bizuin=3895151075; data_bizuin=3895151075; bizuin=3895151075; data_ticket=9jq3kBnD3sIOaft14SIJhN2obz/K7uVMbCz1lG18rHXqPCNmIgs9fdqcBpLDZkn+; slave_sid=VFFVeHA3aVJMcHlBVTdBQXFDS1BTU1BHbVljaGFobDhUbzlYcGw4RFEyRk9DUV9oMDdzYWE0NVlNbGx5YTFKdGdpSDZyVzI5Vm5Ma1NRanhXOHE0UlhlQ0ZaZlhZWnlBcmdWS3lmUzlDSm5MQmVGZHk5d29ieUZVbmI4Qjd5ZjJFR3BJaHBJYzVqcHlYc0Zi; slave_user=gh_2b50a79cdb27; xid=5087b25c12d631ff18b2e5d3ce93d95f");

    }

    public static void main(String[] args) throws InterruptedException {
        premain();
        WxMpWebService mpWebService = WxWebService.me().mpWebService();
        WxMpWebLoginService wxMpWebLoginService = mpWebService.loginService();
        WxMpWebMaterialService materialService = mpWebService.materialService();
        WxMpWebDraftService wxMpWebDraftService = mpWebService.draftService();
        if (MpApp.token() == null) {
            CountDownLatch latch = new CountDownLatch(1);
            String loginQRCodeDecodeLink = wxMpWebLoginService.getLoginQRCodeDecodeLink();
            log.info("appId=[{}] \t qrTicketUrl=[{}]", MpApp.appId(), loginQRCodeDecodeLink);
            String appId = MpApp.appId();
            ThreadUtil.newThread(() -> {
                MpApp.updateAppId(appId);
                int i = 20;
                while (i-- >= 0 && MpApp.token() == null) {
                    ThreadUtil.sleep(2, TimeUnit.SECONDS);
                }
                latch.countDown();
            }, "wait login", true).start();
            //   当计数为0时结束阻塞
            latch.await();
        }
        log.info("token:[{}], ticket:[{}], ticketId:[{}], ", MpApp.token(), MpApp.ticket(), MpApp.ticketId());
        String name = "groupName";
        Integer groupId = materialService.addPicGroup(name);
        log.info("add pic group[{}], groupId=[{}]", name, groupId);
        String picId = materialService.uploadPic(FileUtil.file("C:\\Users\\pannnfei\\Pictures\\gamemap.jpg"), groupId);
        log.info("add pic into group[{}], picId=[{}]", name, picId);
        materialService.delPicGroup(groupId.toString(), MpWebConst.DelPicGroupType.DEL_WITH_PICS);
        log.info("delete group[{}] with pics.", name);

        int begin = 0;
        int count = 20;
        Optional<JSONObject> optional = wxMpWebDraftService.queryDrafts(begin, count);
        log.info("queryDrafts: begin=[{}], count=[{}]", begin, count);
        optional.ifPresent(json -> log.info("queryDrafts: result: {}", json));

        ArticleModel article1 = ArticleModel.builder()
                .title("测试")
                .author("飞羽英华01")
                .article("这是一次测试的文章<a target=\"_blank\" href=\"https://rsshub.pfinfo.com.cn/\" textvalue=\"M78安全团队\" linktype=\"text\" imgurl=\"\" imgdata=\"null\" tab=\"innerlink\">M78安全团队</a>")
                .appreciation(true)
                .category("其他")
                .targetLink("www.baidu.com")
                .build();
        ArticleModel article2 = ArticleModel.builder()
                .title("测试2")
                .author("飞羽英华02")
                .article("这是一次测试的文章<a target=\"_blank\" href=\"https://rsshub.pfinfo.com.cn/\" " +
                        "textvalue=\"rsshub\" linktype=\"text\" imgurl=\"\" imgdata=\"null\" tab=\"innerlink\">rsshub</a>")
                .appreciation(true)
                .copyrightType(1)
                .category("其他")
                .build();
        DraftModel draftModel = DraftModel.builder()
                .author("飞羽英华")
                .articles(Arrays.asList(article1, article2))
                .build();
        Long appMsgId = wxMpWebDraftService.createOrUpdateDraft(draftModel);
        draftModel.setAppMsgId(appMsgId);
        wxMpWebDraftService.createOrUpdateDraft(draftModel);
        wxMpWebDraftService.deleteDraft(appMsgId);
    }
}
