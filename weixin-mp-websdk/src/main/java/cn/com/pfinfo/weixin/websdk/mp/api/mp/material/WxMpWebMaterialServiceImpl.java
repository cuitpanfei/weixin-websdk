package cn.com.pfinfo.weixin.websdk.mp.api.mp.material;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst;
import cn.com.pfinfo.weixin.websdk.mp.http.MpUrlBuilder;
import cn.com.pfinfo.weixin.websdk.mp.stage.CgiData;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileGroupInfo;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileGroupList;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileItemItem;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * created by cuitpanfei on 2022/07/28
 *
 * @author cuitpanfei
 */
public class WxMpWebMaterialServiceImpl implements WxMpWebMaterialService {

    final String fileTransfer = MpUrlBuilder.cgi("filetransfer").ajax().build();

    /**
     * 上传图片
     * <pre>
     * baseUrl: https://mp.weixin.qq.com/cgi-bin/filetransfer?action=upload_material&f=json
     * example: https://mp.weixin.qq.com/cgi-bin/filetransfer?action=upload_material&f=json&ticket_id=yhgzh&ticket=a52a0dac89a1c69621af22cc4da4cf81aada06f0&svr_time=1658937563&scene=1&writetype=doublewrite&groupid=1&token=433125629&lang=zh_CN&seq=1658967344714
     * form:
     *     file: file
     *
     * result body:
     *  {"base_resp":{"ret":0,"err_msg":"ok"},"location":"bizfile","type":"image","content":"100000236","cdn_url":"https:\/\/mmbiz.qlogo.cn\/mmbiz_png\/jIiaBrXsRIjFZGyMInuPIlCcLeLfFSZffTBwMnfsNMWPmEyqxN4HibDLcJ3h7WOY7icaIpj5ZnfuoKZcTv6pkqCsQ\/0?wx_fmt=png"}
     * </pre>
     *
     * @param file 图片
     */
    @Override
    public void uploadPic(File file, Integer groupId) {
        MpUrlBuilder.parse(fileTransfer).action("upload_material")
                .addQuery("ticket_id", MpApp.ticketId())
                .addQuery("ticket", MpApp.ticket())
                .addQuery("svr_time", DateUtil.currentSeconds())
                .addQuery("scene", 1)
                .addQuery("f", MpWebConst.JSON)
                .addQuery("writetype", "doublewrite")
                .addQuery("groupid", Optional.ofNullable(groupId).orElse(1))
                .addQuery("seq", System.currentTimeMillis())
                .token()
                .post()
                .form("file", file)
                .executeAsync();
    }

    /**
     * 删除图片
     * <pre>
     *     example: https://mp.weixin.qq.com/cgi-bin/modifyfile?t=ajax-response
     *     form:
     *         oper: del
     *         fileid: 100000232
     *         copyright_status: 0
     *         group_id: 1
     *         token: 433125629
     *         lang: zh_CN
     *         f: json
     *         ajax: 1
     * </pre>
     * 批量删除时，oper为batchdel，fileid为array
     * <p>
     * copyright_status在批量删除时，为重复的同一个值，
     * group_id也是如此
     *
     * @param files
     */
    @Override
    public void delPic(List<FileItemItem> files) {
        if (files.size() < 1) {
            return;
        }
        List<Integer> ids = new ArrayList<>(files.size());
        List<Integer> allCopyrightStatus = new ArrayList<>(files.size());
        List<Integer> groupIds = new ArrayList<>(files.size());
        for (int i = 0; i < files.size(); i++) {
            allCopyrightStatus.set(i, 0);
            ids.set(i, files.get(i).getFileId());
            groupIds.set(i, files.get(i).getGroupId());
        }
        MpUrlBuilder.cgi("modifyfile").addQuery("t", "ajax-response")
                .post()
                .contentType(ContentType.FORM_URLENCODED.toString(StandardCharsets.UTF_8))
                .form("oper", files.size() > 1 ? "batchdel" : "del")
                .form("fileid", ids)
                .form("copyright_status", allCopyrightStatus)
                .form("group_id", groupIds)
                .executeAsync();
    }

    /**
     * 添加图片分组，名称不能与系统分组名称（最近使用、我的图片、未分组）以及当前已存在的分组相同
     * <p>
     * 查询当前存在的分组请使用{@linkplain #loadPicGroups}
     *
     * @param name 分组名称
     */
    @Override
    public void addPicGroup(String name) {

    }

    /**
     * 获取图片所有分组信息，包含系统分组（最近使用、我的图片、未分组）以及所有自定义分组
     *
     * @return 图片所有分组信息
     */
    @Override
    public List<FileGroupInfo> loadPicGroups() {
        return Optional.ofNullable(filepage(MpWebConst.MaterialType.IMG, 0, 12))
                .map(CgiData::getFileGroupList)
                .map(FileGroupList::getFileGroup)
                .orElse(Collections.emptyList());
    }

    /**
     * 上传音频文件，格式支持mp3、wma、wav、amr、m4a
     * <p>
     * 上传成功返回网络地址， example: https://res.wx.qq.com/voice/getvoice?mediaid=${FileMedisId}
     *
     * @param file 音频文件
     * @return 音频资源网络地址
     */
    @Override
    public String uploadAudio(File file) {
        int uploadFileBlockSize = 1048576;
        MpUrlBuilder builder = MpUrlBuilder.cgi("audioupload");
        HttpResponse response = builder.action("init_upload").post()
                .contentType(ContentType.FORM_URLENCODED.toString(StandardCharsets.UTF_8))
                .form("filename", file.getName())
                .form("filesha", "undefined")
                .form("filesize", file.length()).executeAsync();
        String audioId = JSONUtil.parse(response.body()).getByPath("audio_id", String.class);
        MpUrlBuilder uploadBlockBuilder = builder.action("upload_block").addQuery("part_sha", "dddd").addQuery("audio_id", audioId);
        byte[] bytes = FileUtil.readBytes(file);
        for (int partNum = 1, loopSize = (int) Math.ceil(file.length() * 1.0 / uploadFileBlockSize); partNum <= loopSize; partNum++) {
            uploadBlockBuilder.addQuery("part_num", partNum)
                    .post().contentType(ContentType.OCTET_STREAM.toString())
                    .header(Header.CACHE_CONTROL, "no-cache")
                    .body(Arrays.copyOfRange(bytes,
                            Math.max(0, (partNum - 1) * uploadFileBlockSize),
                            (int) Math.min(file.length(), partNum * uploadFileBlockSize)))
                    .executeAsync();
        }
        uploadBlockBuilder.action("upload_finish")
                .deleteQuery("part_sha")
                .deleteQuery("part_num")
                .addQuery("t", WxWebHttpUtil.random())
                .post().executeAsync();
        MpUrlBuilder operateVoice = MpUrlBuilder.cgi("operate_voice");

        String body = operateVoice.addQuery("oper", "create").post()
                .form("audio_id", audioId)
                .form("category", 17)
                .form("title", file.getName())
                .executeAsync()
                .body();
        String mediaid = JSONUtil.parseObj(body).getStr("encode_file_id");
        return String.format("https://res.wx.qq.com/voice/getvoice?mediaid=%s", mediaid);
    }
}
