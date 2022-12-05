package cn.com.pfinfo.weixin.websdk.mp.api.mp.material;

import cn.com.pfinfo.weixin.websdk.common.exception.RRException;
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
import java.util.stream.Collectors;

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
     * @return
     */
    @Override
    public String uploadPic(File file, Integer groupId) {
        String body = MpUrlBuilder.parse(fileTransfer).action("upload_material")
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
                .executeAsync().body();
        return JSONUtil.parseObj(body).getStr("content");
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
    public Integer addPicGroup(String name) {
        String body = MpUrlBuilder.cgi("filepage").post()
                .contentType(ContentType.FORM_URLENCODED.toString(StandardCharsets.UTF_8))
                .form("action", "create_group")
                .form("name", name)
                .executeAsync().body();
        return JSONUtil.parseObj(body).getByPath("group_id", Integer.class);
    }

    /**
     * 根据组ID删除组信息
     *
     * @param groupIds 组ID，多个时由“,”分割
     */
    @Override
    public void delPicGroup(String groupIds) {
        delPicGroup(groupIds, MpWebConst.DelPicGroupType.DEL_GROUP_WITHOUT_PICS);
    }

    /**
     * 根据组ID删除组信息，根据type判断是否需要删除组内文件
     *
     * @param groupIds 组信息
     * @param type     删除的类型
     */
    @Override
    public void delPicGroup(String groupIds, MpWebConst.DelPicGroupType type) {
        String del_img = Arrays.stream(groupIds.split(","))
                .map(s -> type.code)
                .collect(Collectors.joining(","));
        String body = MpUrlBuilder.cgi("filepage").post()
                .contentType(ContentType.FORM_URLENCODED.toString(StandardCharsets.UTF_8))
                .form("action", "batch_operate_group")
                .form("delete_group_ids", groupIds)
                .form("del_img", del_img)
                .executeAsync().body();
        System.out.println(body);
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
        String audioId = uploadBlock(file, uploadFileBlockSize, MpWebConst.MaterialType.AUDIO, "audio_id");
        MpUrlBuilder operateVoice = MpUrlBuilder.cgi("operate_voice");
        String body = operateVoice.addQuery("oper", "create").post()
                .form("audio_id", audioId)
                .form("category", 17)
                .form("title", file.getName())
                .executeAsync()
                .body();
        String mediaId = JSONUtil.parseObj(body).getStr("encode_file_id");
        return String.format("https://res.wx.qq.com/voice/getvoice?mediaId=%s", mediaId);
    }

    @Override
    public String uploadVideo(File file) {
        int uploadFileBlockSize = 4194304;
        String vid = uploadBlock(file, uploadFileBlockSize, MpWebConst.MaterialType.VIDEO, "vid");
        MpUrlBuilder.parse(fileTransfer).action("upload_cdn")
                .addQuery("ticket_id", MpApp.ticketId())
                .addQuery("ticket", MpApp.ticket())
                .addQuery("svr_time", DateUtil.currentSeconds())
                .post().form("file", (File) null).executeAsync();
        return null;
    }

    String uploadBlock(File file, int blockSize, MpWebConst.MaterialType type, String fileIdName) {
        MpUrlBuilder urlBuilder;
        boolean isAudio = MpWebConst.MaterialType.AUDIO.equals(type);
        boolean isVideo = !isAudio && MpWebConst.MaterialType.VIDEO.equals(type);
        if (isAudio) {
            urlBuilder = MpUrlBuilder.cgi("audioupload");
        } else if (isVideo) {
            urlBuilder = MpUrlBuilder.cgi("videoupload");
        } else {
            throw new RRException(FileUtil.extName(file) + "是不支持上传的类型！");
        }
        HttpResponse response = urlBuilder.action("init_upload").post()
                .contentType(ContentType.FORM_URLENCODED.toString(StandardCharsets.UTF_8))
                .form("filename", file.getName())
                .form("filesha", "undefined")
                .form("filesize", file.length()).executeAsync();
        String fileId = JSONUtil.parse(response.body()).getByPath(fileIdName, String.class);

        urlBuilder.action("upload_block").addQuery("part_sha", "dddd").addQuery(fileIdName, fileId);
        byte[] bytes = FileUtil.readBytes(file);
        for (int partNum = 1, loopSize = (int) Math.ceil(file.length() * 1.0 / blockSize); partNum <= loopSize; partNum++) {
            urlBuilder.addQuery("part_num", partNum)
                    .addQuery("t", WxWebHttpUtil.random())
                    .post().contentType(ContentType.OCTET_STREAM.toString())
                    .header(Header.CACHE_CONTROL, "no-cache")
                    .body(Arrays.copyOfRange(bytes,
                            Math.max(0, (partNum - 1) * blockSize),
                            (int) Math.min(file.length(), partNum * blockSize)))
                    .executeAsync();
        }

        urlBuilder.action("upload_finish")
                .deleteQuery("part_sha")
                .deleteQuery("part_num")
                .addQuery("t", WxWebHttpUtil.random());
        if (isVideo) {
            urlBuilder.addQuery("screen_shot_type", 1);
        }
        urlBuilder.post().executeAsync();

        return fileId;
    }
}
