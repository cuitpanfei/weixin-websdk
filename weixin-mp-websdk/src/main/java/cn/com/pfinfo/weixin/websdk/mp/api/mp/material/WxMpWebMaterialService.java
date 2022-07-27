package cn.com.pfinfo.weixin.websdk.mp.api.mp.material;

import java.util.List;

/**
 * 素材服务，提供包括媒体素材（图片、音频以及视频）管理处理接口。
 * <p>
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public interface WxMpWebMaterialService {
    //================= 通用接口 ======================

    //================= 图片接口 ======================

    /**
     * 添加图片分组，名称不能与系统分组名称（最近使用、我的图片、未分组）以及当前已存在的分组相同
     * <p>
     * 查询当前存在的分组请使用{@linkplain #loadPicGroups}
     *
     * @param name 分组名称
     */
    void addPicGroup(String name);

    /**
     * 获取图片所有分组信息，包含系统分组（最近使用、我的图片、未分组）以及所有自定义分组
     *
     * @return 图片所有分组信息
     */
    List<String> loadPicGroups();
    //================= 音频接口 ======================
    //================= 视频接口 ======================
}
