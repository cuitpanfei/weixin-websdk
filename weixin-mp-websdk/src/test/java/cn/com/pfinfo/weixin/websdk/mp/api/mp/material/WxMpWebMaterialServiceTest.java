package cn.com.pfinfo.weixin.websdk.mp.api.mp.material;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileGroupInfo;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileItemItem;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * created by cuitpanfei on 2022/07/28
 *
 * @author cuitpanfei
 */
class WxMpWebMaterialServiceTest {

    WxMpWebMaterialService service = Singleton.get(WxMpWebMaterialServiceImpl.class);

    @BeforeEach
    void before() {
        WxWebHttpUtil.getNewWxUin();
        MpApp.updateAppId("asd");
        MpApp.setToken("541941378");
        MpApp.updateCookie("appmsglist_action_3895151075=card; ua_id=wrkIJwT6jVoKslDGAAAAALKpokWAlfb2vNhPNFgTYzE=; mm_lang=zh_CN; pgv_pvid=4368920818; iip=0; pac_uid=0_f871b711bd216; wxuin=47583230377848; fqm_pvqid=15fdc9a7-dd1a-4393-8e72-4b92e8bcc388; noticeLoginFlag=1; uuid=67a54392fc7e9fe97cb761e66d251fa5; rand_info=CAESIMu7iA9xdUeLHrkdsBd0aEIi6hHSN0NY4/8q8SUair4U; slave_bizuin=3895151075; data_bizuin=3895151075; bizuin=3895151075; data_ticket=J6tgOdNpBdKK1WiPvt7rSIv2szt9uuTUUXlHtUh9JPGghIjzlMeOqESqUQk06kVj; slave_sid=Uk0wVzhqRFA1WWNrTDdVclpTWU5FejVLcGt1dEE4VUhrcThrM1pCblk4TG5ZM1pIcEdSZ09wakhDN0x0M1RQS25IN3BwTk5Za0xzQ2xfS1lMRVU3RXRJS1JCUmk3XzQxbVloY2JITUI2WF9RXzl3dFBYdEdUUmhlclp2S2V0OTRqTjVpMWg0dmFHV2NuQ2JG; slave_user=gh_2b50a79cdb27; xid=2089fe13635ec72e64834ef131c95707; rewardsn=; wxtokenkey=777");
    }

    @Test
    void updateCookie() {
        String cookie= "appmsglist_action_3895151075=card; ua_id=wrkIJwT6jVoKslDGAAAAALKpokWAlfb2vNhPNFgTYzE=; " +
            "mm_lang=zh_CN; " +
            "pgv_pvid=4368920818; iip=0; pac_uid=0_f871b711bd216; wxuin=47583230377848; fqm_pvqid=15fdc9a7-dd1a-4393-8e72-4b92e8bcc388; noticeLoginFlag=1; uuid=67a54392fc7e9fe97cb761e66d251fa5; rand_info=CAESIMu7iA9xdUeLHrkdsBd0aEIi6hHSN0NY4/8q8SUair4U; slave_bizuin=3895151075; data_bizuin=3895151075; bizuin=3895151075; data_ticket=J6tgOdNpBdKK1WiPvt7rSIv2szt9uuTUUXlHtUh9JPGghIjzlMeOqESqUQk06kVj; slave_sid=Uk0wVzhqRFA1WWNrTDdVclpTWU5FejVLcGt1dEE4VUhrcThrM1pCblk4TG5ZM1pIcEdSZ09wakhDN0x0M1RQS25IN3BwTk5Za0xzQ2xfS1lMRVU3RXRJS1JCUmk3XzQxbVloY2JITUI2WF9RXzl3dFBYdEdUUmhlclp2S2V0OTRqTjVpMWg0dmFHV2NuQ2JG; slave_user=gh_2b50a79cdb27; xid=2089fe13635ec72e64834ef131c95707; rewardsn=; wxtokenkey=777";
        Optional.ofNullable(cookie)
                .filter(CharSequenceUtil::isNotBlank)
                .map(s-> Stream.of(s.split(";"))
                        .flatMap(c-> HttpCookie.parse(c).stream())
                        .collect(Collectors.toList()))
                .ifPresent(list->{
                    JSONUtil.toJsonStr(list);
                });
    }
    void uploadPic() {
        HttpResponse response = HttpUtil.createPost("https://mp.weixin.qq.com/cgi-bin/filetransfer?action=upload_material&f=json&ticket_id=fyyhgzh&ticket=105127733413a008744f013e8dc2b03dadf30a31&svr_time=" +
                +(System.currentTimeMillis() / 1000)
                + "&scene=1&writetype=doublewrite&groupid=1&token=433125629&lang=zh_CN&seq=" + System.currentTimeMillis())
                .form("file", FileUtil.file("C:\\Users\\pannnfei\\Pictures\\man2 (2).png"))
                .executeAsync();

        System.out.println(response.body());
        System.out.println(response);
    }

    @Test
    void delPic() {
        List<FileItemItem> files = new ArrayList<>();
        service.delPic(files);
    }

    @Test
    void addPicGroup() {
        Integer groupId = service.addPicGroup("ceshi");
        System.out.println(groupId);
    }


    @Test
    void delPicGroup() {
        service.delPicGroup("101,102");
    }

    @Test
    void loadPicGroups() {
        List<FileGroupInfo> fileGroupInfos = service.loadPicGroups();
        System.out.println(JSONUtil.parse(fileGroupInfos));
    }

    @Test
    void uploadAudio() {
        for (int i = 0; i < 20; i++) {
            service.uploadAudio(FileUtil.file("C:\\Users\\pannnfei\\Downloads\\tts.mp3"));
        }
    }

    @Test
    void searchAudio() {
        List<FileItemItem> tts = service.searchAudio("tts", 0, 20);
        System.out.println(JSONUtil.parse(tts));
    }

    @Test
    void delAudio() {
        List<FileItemItem> tts = service.searchAudio("tts", 0, 20);
        String fileIds = tts.stream().map(FileItemItem::getVoiceEncodeFileid).collect(Collectors.joining(","));
        service.delAudio(fileIds);
    }
}