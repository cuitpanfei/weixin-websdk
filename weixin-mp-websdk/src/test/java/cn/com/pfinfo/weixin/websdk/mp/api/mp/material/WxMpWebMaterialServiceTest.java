package cn.com.pfinfo.weixin.websdk.mp.api.mp.material;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileGroupInfo;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.GlobalInterceptor;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        MpApp.setToken("433125629");
        MpApp.updateCookie("appmsglist_action_3895151075=card; wxuin=46385954066622; ua_id=hmpfVU5Kcs7mLqIRAAAAAE3kpWcPckX_5rnIT6M995k=; pgv_pvid=7231114880; ts_uid=9968689696; mm_lang=zh_CN; RK=Q5kdeGe0e3; ptcz=849f89b27f4038da3c8c07aaf4c3d6ebf21bec5a4c1a3927aec02531f4ab7f31; o_cookie=1730789103; pac_uid=1_1730789103; iip=0; fqm_pvqid=38f9738f-947f-4abb-af4e-9a9baff737ce; uuid=06d282082eda7ce7368ada1f38a4532d; rand_info=CAESIJEjjMl3QRHZGvvqoF434HtymNa1DyCCyMOiB41YKxJp; slave_bizuin=3895151075; data_bizuin=3895151075; bizuin=3895151075; data_ticket=/4Lc7O1hb2A5TDiRPISP+v4cgaUX/YZr6AJhPcJOnsgwbIMP6o7VCRxgXvkFjpFv; slave_sid=dHZmYVN1c21Zc2FGYWRnNUMzNjQ0SWl0dWJjQXl2UTJZTmtOc1Y0bVBqd0h2NTR5dVhHSVRQX3AwblVJbWZ3NmlYYWI4ZWR3eERWQkh1MzkwNVVvZ1kyTDlPaVdKVTVFSmpEbEdWeURkWktJQ3VDU0FoUXBRdTBya1ZJNWJDUm5oVVB6Wk4wdjJIR1FOdzRi; slave_user=gh_2b50a79cdb27; xid=03f1fb1cb0dc5a62c780ac9a59a58208");
    }

    @Test
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
        String[] files = {"100000240"};
        if (files.length < 1) {
            return;
        }
        HttpResponse response = HttpUtil.createPost("https://mp.weixin.qq.com/cgi-bin/modifyfile?t=ajax-response")
                .header(Header.REFERER, "https://mp.weixin.qq.com/")
                .form("oper", files.length > 1 ? "batchdel" : "del")
                .form("fileid", String.join(",", files))
                .form("copyright_status", "0,0")
                .form("group_id", "1,1")
                .form("token", "433125629")
                .executeAsync();

        System.out.println(response);
    }

    @Test
    void addPicGroup() {
    }

    @Test
    void loadPicGroups() {
        List<FileGroupInfo> fileGroupInfos = service.loadPicGroups();
        System.out.println(JSONUtil.parse(fileGroupInfos));
    }

    @Test
    void uploadAudio() {
        for (int i = 0; i < 30; i++) {
            service.uploadAudio(FileUtil.file("C:\\Users\\pannnfei\\Downloads\\tts.mp3"));
        }
    }
}