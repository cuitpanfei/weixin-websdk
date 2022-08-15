package cn.com.pfinfo.mp.quickstart;

import cn.com.pfinfo.weixin.websdk.mp.api.WxWebService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.WxMpWebService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.WxMpWebDraftService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.material.WxMpWebMaterialService;
import cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * created by cuitpanfei on 2022/08/11
 *
 * @author cuitpanfei
 */
public class MainStart {
    public static void main(String[] args) {
        WxMpWebService mpWebService = WxWebService.me().mpWebService();
        WxMpWebLoginService wxMpWebLoginService = mpWebService.loginService();
        WxMpWebMaterialService materialService = mpWebService.materialService();
        WxMpWebDraftService wxMpWebDraftService = mpWebService.draftService();
        CountDownLatch latch = new CountDownLatch(1);
        String loginQRCodeDecodeLink = wxMpWebLoginService.getLoginQRCodeDecodeLink();
        System.out.printf("appId=[%s] \t qrTicketUrl=[%s]\n", MpApp.appId(), loginQRCodeDecodeLink);
        String appId = MpApp.appId();
        ThreadUtil.newThread(() -> {
            MpApp.updateAppId(appId);
            int i = 20;
            while (i-- >= 0 && MpApp.token() == null) {
                ThreadUtil.sleep(2, TimeUnit.SECONDS);
            }
            latch.countDown();
        },"wait login").start();
        try {
            //   当计数为0时结束阻塞
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(MpApp.token());
        System.out.println(MpApp.ticket());
        System.out.println(MpApp.ticketId());

        Integer groupId = materialService.addPicGroup("ceshi");
        String picId = materialService.uploadPic(FileUtil.file("C:\\Users\\pannnfei\\Pictures\\gamemap.jpg"), groupId);
        materialService.delPicGroup(groupId.toString(), MpWebConst.DelPicGroupType.DEL_WITH_PICS);


    }
}
