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
     * 开启赞赏（声明原创）
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
}
