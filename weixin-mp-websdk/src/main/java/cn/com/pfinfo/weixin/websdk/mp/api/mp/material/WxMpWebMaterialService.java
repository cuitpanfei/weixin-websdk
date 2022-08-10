package cn.com.pfinfo.weixin.websdk.mp.api.mp.material;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst;
import cn.com.pfinfo.weixin.websdk.mp.http.MpUrlBuilder;
import cn.com.pfinfo.weixin.websdk.mp.stage.CgiData;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileGroupInfo;
import cn.com.pfinfo.weixin.websdk.mp.stage.FileItemItem;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * 素材服务，提供包括媒体素材（图片、音频以及视频）管理处理接口。
 * <p>
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public interface WxMpWebMaterialService {

    String FILE_PAGE = MpUrlBuilder.cgi("filepage").ajax().build();
    //================= 通用接口 ======================

    /**
     * 分页查询文件列表
     *
     * @param type  文件类型
     * @param query 关键字，用于查询信息
     * @param begin 开始位置
     * @param count 单页大小
     * @return 文件列表信息
     */
    default CgiData filepage(MpWebConst.MaterialType type, Integer begin, Integer count) {
        return filepage(type, null, begin, count);
    }

    /**
     * 分页查询文件列表
     *
     * @param type  文件类型
     * @param query 关键字，用于查询信息
     * @param begin 开始位置
     * @param count 单页大小
     * @return 文件列表信息
     */
    default CgiData filepage(MpWebConst.MaterialType type, String query, Integer begin, Integer count) {
        HttpResponse response = MpUrlBuilder.parse(FILE_PAGE).token()
                .addQuery("type", type.code).addQuery("begin", begin)
                .addQuery("count", count).get()
                .executeAsync();
        String body = response.body();
        Optional<JSONObject> cgi = ContentType.get(body).equals(ContentType.JSON) ?
                Optional.ofNullable(JSONUtil.parseObj(body).getJSONObject("page_info")) : WxWebHttpUtil.getCgiFromPage(body);
        return cgi.map(entries -> entries.toBean(CgiData.class)).orElse(null);
    }

    //================= 图片接口 ======================

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
     * @param file    图片
     * @param groupId 分组id，默认为1
     */
    void uploadPic(File file, Integer groupId);

    default void uploadPic(File file) {
        uploadPic(file, null);
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
    void delPic(List<FileItemItem> files);

    /**
     * 添加图片分组，名称不能与系统分组名称（最近使用、我的图片、未分组）以及当前已存在的分组相同
     * <p>
     * 查询当前存在的分组请使用{@linkplain #loadPicGroups}
     *
     * @param name 分组名称
     * @return 组ID
     */
    Integer addPicGroup(String name);

    /**
     * 根据组ID删除组信息
     *
     * @param groupIds 组ID，多个时由“,”分割
     */
    void delPicGroup(String groupIds);

    /**
     * 获取图片所有分组信息，包含系统分组（最近使用、我的图片、未分组）以及所有自定义分组
     *
     * @return 图片所有分组信息
     */
    List<FileGroupInfo> loadPicGroups();
    //================= 音频接口 ======================

    /**
     * 上传音频文件，格式支持mp3、wma、wav、amr、m4a
     * <p>
     * 上传成功返回网络地址，
     * example: https://res.wx.qq.com/voice/getvoice?mediaid=${FileMedisId}
     *
     * @param file 音频文件
     * @return 音频资源网络地址
     */
    String uploadAudio(File file);

    //================= 视频接口 ======================
}
