package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft;

import cn.com.pfinfo.weixin.websdk.mp.http.MpUrlBuilder;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jsoup.Jsoup;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * created by cuitpanfei on 2022/08/10
 *
 * @author cuitpanfei
 */
public class ArticleMaterialKit {


    /**
     * <pre>
     * {
     *   "base_resp": {
     *     "err_msg": "ok",
     *     "ret": 0
     *   },
     *   "result": [{
     *     "cdnurl": "http://mmbiz.qpic.cn/mmbiz_jpg/jIiaBrXsRIjEvEOwUHaTAKkgJNS5ZVvic4fxUQpGPYnAogrqDic7poibRmMj1eVg2nibKdoxMubGLSzia1plTicdialaeg/0?wx_fmt=jpeg",
     *     "file_id": 100000102
     *   }, {
     *     "cdnurl": "http://mmbiz.qpic.cn/mmbiz_jpg/jIiaBrXsRIjEvEOwUHaTAKkgJNS5ZVvic4FDFvV9gqmDHJLS42C63JibicY07CBNrJ6NH2Ihfvavy7AMeAAgqzos2A/0?wx_fmt=jpeg",
     *     "file_id": 100000103
     *   }]
     * }
     * </pre>
     *
     * @return
     */
    public static Map<String, String> cropMulti(String img) {
        double size0_x1 = 0;
        double size0_y1 = 0.1810810810810811;
        double size0_x2 = 1;
        double size0_y2 = 0.8182288671650374;
        double size1_x1 = 0;
        double size1_y1 = 0.1810810810810811;
        double size1_x2 = 1;
        double size1_y2 = 0.8182288671650374;
        Map<String, Object> build = MapUtil.builder(new HashMap<String, Object>())
                .put("imgurl", img)
                .put("size_count", 2)
                .put("size0_x1", size0_x1).put("size0_y1", size0_y1)
                .put("size0_x2", size0_x2).put("size0_y2", size0_y2)
                .put("size1_x1", size1_x1).put("size1_y1", size1_y1)
                .put("size1_x2", size1_x2).put("size1_y2", size1_y2)
                .put("token", MpApp.token())
                .put("lang", "zh_CN")
                .put("f", "json")
                .put("ajax", 1).build();
        HttpRequest httpRequest = MpUrlBuilder.cgi("/cropimage").action("crop_multi").deleteQuery("lang").post()
                .form(build)
                .contentType(ContentType.FORM_URLENCODED.getValue());
        String cropMultiInfo = httpRequest
                .executeAsync().body();
        JSONObject object = JSONUtil.parseObj(cropMultiInfo);
        JSONArray array = object.getJSONArray("result");
        String cdnUrl0 = ((JSONObject) array.get(0)).getStr("cdnurl");
        String cdnUrl1 = ((JSONObject) array.get(1)).getStr("cdnurl");
        File file = new File("0.jpg");
        HttpUtil.downloadFile(cdnUrl0, file);
        BufferedImage image = ImgUtil.read(file);
        int picWitdh = image.getWidth();
        int picHeight = image.getHeight();
        FileUtil.del(file);
        String crop_list = JSONUtil.toJsonStr(Arrays.asList(
                MapUtil.builder().put("ratio", "2.35_1")
                        .put("x1", (int) (size0_x1 * picWitdh)).put("y1", (int) (size0_y1 * picHeight))
                        .put("x2", (int) (size0_x2 * picWitdh)).put("y2", (int) (size0_y2 * picHeight)).build(),
                MapUtil.builder().put("ratio", "1_1")
                        .put("x1", (int) (size1_x1 * picWitdh)).put("y1", (int) (size1_y1 * picHeight))
                        .put("x2", (int) (size1_x2 * picWitdh)).put("y2", (int) (size1_y2 * picHeight)).build()
        ));
        return MapUtil.builder(new HashMap<String, String>(3)).put("crop_list", crop_list)
                .put("cdnUrl0", cdnUrl0)
                .put("cdnUrl1", cdnUrl1)
                .build();
    }


    public static String photoGallery(String query) {
        return MpUrlBuilder.cgi("/photogallery").action("search").addQuery("query", query)
                .addQuery("type", 1).addQuery("limit", 16).addQuery("last_seq", "0")
                .get().executeAsync().body();
    }

    public static void cdnFromContentImg(String articles, int index, String query, MapBuilder<String, Object> builder) {
        String img = Jsoup.parse(articles).getElementsByTag("img").attr("data-src");
        if (CharSequenceUtil.isEmpty(img)) {
            JSONArray result = JSONUtil.parseObj(ArticleMaterialKit.photoGallery(query)).getJSONArray("result");
            JSONObject photo = (JSONObject) result.get(RandomUtil.randomInt(0, result.size()));
            img = photo.getJSONObject("photo").getStr("url");
            img = MpUrlBuilder.cgi("/photogalleryproxy").action("proxy")
                    .addQuery("url", img)
                    .addQuery("supplier", 5).addQuery("from_public_pic", 1)
                    .build();
        }
        Map<String, String> cropMultiInfo = ArticleMaterialKit.cropMulti(img);
        String cdnUrl0 = cropMultiInfo.get("cdnUrl0");
        String cdnUrl1 = cropMultiInfo.get("cdnUrl1");
        String cropList = cropMultiInfo.get("crop_list");
        builder.put("cdn_url" + index, cdnUrl0)
                .put("cdn_235_1_url" + index, cdnUrl0)
                .put("cdn_16_9_url" + index, cdnUrl0)
                .put("cdn_1_1_url" + index, cdnUrl1)
                .put("cdn_url_back" + index, img)
                .put("crop_list" + index, "{\"crop_list\":" + cropList + "}");
    }

    public static void initByIndex(MapBuilder<String, Object> builder, Integer index) {
        builder.put("ad_id" + index, "").put("ad_video_transition" + index, "")
                .put("applyori" + index, 0)
                //自动生成摘要？
                .put("auto_gen_digest" + index, 0)
                //是否可以插入广告
                .put("can_insert_ad" + index, 1)
                .put("cardid" + index, "").put("cardlimit" + index, "")
                .put("copyright_img_list" + index, "").put("copyright_type" + index, 1)
                .put("cardquantity" + index, "").put("compose_info" + index, "")
                .put("dot" + index, "{}")
                .put("fee" + index, 0).put("fileid" + index, "")
                .put("finder_draft_id" + index, 0).put("free_content" + index, "")
                .put("guide_words" + index, "").put("hit_nickname" + index, "")
                .put("insert_ad_mode" + index, "").put("is_cartoon_copyright" + index, 0)
                .put("is_finder_video" + index, 0).put("is_pay_subscribe" + index, 0)
                .put("is_set_sync_to_finder" + index, 0).put("is_share_copyright" + index, 0)
                .put("is_video_recommend" + index, 0).put("last_choose_cover_from" + index, 1)
                .put("music_id" + index, "").put("need_open_comment" + index, 0)
                .put("only_fans_can_comment" + index, 0).put("only_fans_days_can_comment" + index, 0)
                .put("open_fansmsg" + index, 1).put("pay_album_info" + index, "")
                .put("pay_desc" + index, "").put("pay_fee" + index, "")
                .put("pay_gifts_count" + index, 0).put("pay_preview_percent" + index, "")
                .put("platform" + index, "").put("related_video" + index, "")
                .put("releasefirst" + index, "").put("reply_flag" + index, 0)
                .put("reprint_recommend_content" + index, "").put("reprint_recommend_title" + index, "")
                .put("reward_reply_id" + index, "").put("share_copyright_url" + index, "")
                .put("share_imageinfo" + index, "{\"list\":[]}").put("share_page_type" + index, 0)
                .put("share_video_id" + index, "").put("share_voice_id" + index, "")
                .put("shortvideofileid" + index, "").put("show_cover_pic" + index, 0)
                .put("source_article_type" + index, "").put("supervoteid" + index, "")
                .put("sync_to_finder_cover" + index, "")
                .put("sync_to_finder_cover_source" + index, "")
                .put("video_id" + index, "").put("video_ori_status" + index, "")
                .put("vid_type" + index, "").put("voteid" + index, "")
                .put("voteismlt" + index, "");
    }


    /**
     * 返回原创声明的相关信息
     *
     * @param index      当前文章索引
     * @param category   当前文章分类
     * @param writerInfo 作者信息
     * @return
     */
    public static Map<String, Object> initAppreciationAccount(int index, String category,
                                                              JSONObject writerInfo) {
        return MapBuilder.create(new HashMap<String, Object>())
                .put("can_reward" + index, writerInfo.getInt("can_reward"))
                .put("copyright_type" + index, 1)
                .put("reprint_permit_type" + index, 1)
                .put("allow_reprint" + index, 0)
                .put("allow_reprint_modify" + index, 0)
                .put("original_article_type" + index, category)
                .put("writerid" + index, writerInfo.getStr("writerid"))
                .put("author" + index, writerInfo.getStr("nickname"))
                .put("ori_white_list" + index, "{\"white_list\":[]}").build();

    }

    public static String findInfosFromHtml(String pageHtmlCode) {
        return Arrays.stream(pageHtmlCode.split("\\n"))
                .filter(s -> s.trim().startsWith("infos") && s.trim().endsWith(","))
                .findFirst()
                .map(s -> {
                    String result = s.trim().replaceFirst("infos = ", "");
                    return result.substring(0, result.lastIndexOf(","));
                }).orElse("{}");
    }
}
