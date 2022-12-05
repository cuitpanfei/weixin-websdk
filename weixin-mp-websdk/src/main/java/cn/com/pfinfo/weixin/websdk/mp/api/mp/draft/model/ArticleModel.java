package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by cuitpanfei on 2022/08/10
 *
 * @author cuitpanfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleModel {
    /**
     * 标题
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 正文
     */
    private String article;
    /**
     * 摘要
     */
    private String summary;

    /**
     * 声明原创，0：非原创；1：原创
     */
    private int copyrightType = 0;
    /**
     * 开启赞赏（声明原创才有效）
     */
    private boolean appreciation;
    /**
     * 一级分类
     */
    private String category;
    /**
     * 二级分类
     */
    private String subCategory;
    /**
     * 原文链接
     */
    private String targetLink;

    public boolean isAppreciation() {
        return copyrightType == 1 && appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        setCopyrightType(1);
        this.appreciation = appreciation;
    }
}
