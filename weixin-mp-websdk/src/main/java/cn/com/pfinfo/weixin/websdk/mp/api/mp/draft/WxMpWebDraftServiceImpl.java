package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.AppmsgAlbumInfo;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.ArticleModel;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.DraftInfos;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.DraftModel;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.MpProfileModel;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.MultiItem;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.NewsItem;
import cn.com.pfinfo.weixin.websdk.mp.http.MpUrlBuilder;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.com.pfinfo.weixin.websdk.mp.util.CommonKit.GSON_INSTANCE;

/**
 * created by cuitpanfei on 2022/08/10
 *
 * @author cuitpanfei
 */
public class WxMpWebDraftServiceImpl implements WxMpWebDraftService {


    @Override
    public Long createOrUpdateDraft(DraftModel draft) {
        MpUrlBuilder builder = MpUrlBuilder.parse(NEW_DRAFT_PAGE_URL).token();
        String pageHtmlCode = builder.get().executeAsync().body();
        HttpResponse response = MpUrlBuilder.cgi("/searchbiz").action("search_biz").addQuery("scene", 1)
                .addQuery("begin", 0).addQuery("count", 10)
                .addQuery("query", "飞羽英华")
                .get().executeAsync();
        JSONObject mpProfile = JSONUtil.parseObj(response.body());
        JSONObject profileInfo = (JSONObject) mpProfile.getJSONArray("list").get(0);
        MpProfileModel profileModel = profileInfo.toBean(MpProfileModel.class);
        draft.getArticles().forEach(article -> {
            if (!article.getArticle().contains("<section class=\"mp_profile_iframe_wrp\">")) {
                article.setArticle(profileModel + article.getArticle());
            }
        });
        Map<String, Object> body = createBody(draft, pageHtmlCode);
        String result = MpUrlBuilder.cgi("/operate_appmsg").addQuery("t", "ajax-response")
                .addQuery("sub", draft.isUpdate() ? "update" : "create")
                .addQuery("type", "77")
                .post()
                .form(body)
                .contentType(ContentType.MULTIPART.getValue())
                .executeAsync().body();
        System.out.println(result);
        return JSONUtil.parseObj(result).getLong("appMsgId");
    }

    private MapBuilder<String, Object> initBuilder(DraftModel draft, String dataSeq) {
        List<ArticleModel> articles = draft.getArticles();
        int articlesSize = articles.size();
        MapBuilder<String, Object> builder = MapUtil.builder(new HashMap<>(16 + articlesSize * 82));
        builder.put("token", MpApp.token())
                .put("lang", "zh_CN")
                .put("f", "json")
                .put("ajax", 1)
                .put("operate_from", "Chrome")
                .put("remind_flag", null)
                .put("isnew", 0)
                .put("count", articlesSize)
                .put("is_auto_type_setting", 0)
                .put("is_auto_save", 0)
                .put("isneedsave", 0)
                .put("AppMsgId", draft.getAppMsgId())
                .put("data_seq", Optional.ofNullable(dataSeq).orElse("0"))
                .put("articlenum", articlesSize)
                .put("random", WxWebHttpUtil.random());
        return builder;
    }

    private JSONObject writerInfo(DraftModel draft) {
        String author = draft.getAuthor();
        String pageInfo = MpUrlBuilder.base("/acct/writermgr").action("search").random()
                .addQuery("author", author).addQuery("writerids", null)
                .get().executeAsync().body();
        return (JSONObject) JSONUtil.parseObj(pageInfo)
                .getJSONObject("pageinfo").getJSONArray("writerlist").get(0);
    }

    private Map<String, Object> createBody(DraftModel draft, String pageHtmlCode) {
        String infos = ArticleMaterialKit.findInfosFromHtml(pageHtmlCode);
        DraftInfos draftInfos = DraftInfos.from(infos);
        List<NewsItem> newsItems = draftInfos.getItem();
        if (newsItems.isEmpty()) {
            newsItems.add(new NewsItem());
        }
        NewsItem newsItem = newsItems.get(0);
        List<MultiItem> items = Optional.ofNullable(newsItem.getMultiItem()).orElseGet(() -> Collections.singletonList(new MultiItem()));
        List<ArticleModel> articles = draft.getArticles();
        int articlesSize = articles.size();
        MapBuilder<String, Object> builder = initBuilder(draft, newsItem.getDataSeq());
        JSONObject writerInfo = writerInfo(draft);
        String author = draft.getAuthor();
        for (int i = 0; i < articlesSize; i++) {
            ArticleModel content = articles.get(i);
            MultiItem item = Optional.ofNullable(i < items.size() ? items.get(i) : null).orElseGet(MultiItem::new);
            String summary = Optional.ofNullable(item.getDigest()).orElseGet(() -> {
                String digest = CharSequenceUtil.isNotEmpty(content.getSummary()) ? content.getSummary() : HtmlUtil.cleanHtmlTag(content.getArticle()).trim();
                return digest.substring(0, Math.min(digest.length(), 100));
            });
            String appmsgAlbumInfo = GSON_INSTANCE.toJson(Optional.ofNullable(item.getAppmsgAlbumInfo()).orElseGet(() -> {
                AppmsgAlbumInfo info = new AppmsgAlbumInfo();
                info.setAppmsgAlbumInfos(new ArrayList<>());
                return info;
            }));
            String other = "其他";
            String category = Optional.ofNullable(content.getCategory()).orElse(other);
            if (!category.equals(other)) {
                category = category + "_" + Optional.ofNullable(content.getSubCategory()).orElse(other);
            }
            String categoriesList = GSON_INSTANCE.toJson(Optional.ofNullable(item.getCategoriesList())
                    .orElseGet(ArrayList::new));
            ArticleMaterialKit.initByIndex(builder, i);
            // 合集的信息，每篇文章最多添加5个合集
            builder.put("appmsgAlbumInfo" + i, appmsgAlbumInfo).put("audio_info" + i, "{\"audio_infos\":[]}")
                    .put("author" + i, Optional.ofNullable(item.getAuthor()).orElse(author))
                    .put("categories_list" + i, categoriesList).put("content" + i, content.getArticle())
                    .put("digest" + i, summary)
                    .put("sourceurl" + i, Optional.ofNullable(item.getSourceUrl()).orElse(content.getTargetLink()))
                    .put("title" + i, Optional.ofNullable(item.getTitle()).orElse(content.getTitle()))
                    .put("copyright_type" + i, content.getCopyrightType());
            if (content.isAppreciation()) {
                builder.putAll(ArticleMaterialKit.initAppreciationAccount(i, category, writerInfo));
            }
            if (CharSequenceUtil.isEmpty(item.getCdnUrlBack())) {
                ArticleMaterialKit.cdnFromContentImg(content.getArticle(), i, "生活", builder);
            } else {
                builder.put("cdn_url" + i, item.getCdnUrl())
                        .put("cdn_235_1_url" + i, item.getCdn2351Url())
                        .put("cdn_16_9_url" + i, item.getCdn169Url())
                        .put("cdn_1_1_url" + i, item.getCdn11Url())
                        .put("cdn_url_back" + i, item.getCdnUrlBack());
                String crop_list = "[]";
                builder.put("crop_list", "{\"crop_list\":" + crop_list + "}");
            }
        }
        return builder.build();
    }


    @Override
    public void deleteDraft(Long appMsgId) {
        MpUrlBuilder.cgi("/operate_appmsg").addQuery("t", "ajax-response").addQuery("sub", "del")
                .post()
                .contentType(ContentType.FORM_URLENCODED.toString(StandardCharsets.UTF_8))
                .form("AppMsgId", appMsgId)
                .executeAsync();
    }

    @Override
    public Optional<JSONObject> queryDrafts(Integer begin, Integer count) {
        String body = MpUrlBuilder.parse(LIST_DRAFT).addQuery("begin", begin).addQuery("count", count).token()
                .get()
                .executeAsync().body();
        return WxWebHttpUtil.getCgiFromPage(body);
    }
}
