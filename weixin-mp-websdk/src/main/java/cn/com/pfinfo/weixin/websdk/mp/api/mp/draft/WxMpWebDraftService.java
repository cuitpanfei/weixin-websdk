package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft;

import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.DraftModel;
import cn.com.pfinfo.weixin.websdk.mp.http.MpUrlBuilder;
import cn.hutool.json.JSONObject;

import java.util.Optional;

/**
 * created by cuitpanfei on 2022/08/10
 *
 * @author cuitpanfei
 */
public interface WxMpWebDraftService {

    String NEW_DRAFT_PAGE_URL = MpUrlBuilder.cgi("/appmsg").action("edit").addQuery("type", "77").addQuery("t", "media/appmsg_edit_v2").build();
    String DRAFT_INFO_URL = MpUrlBuilder.cgi("/appmsg").action("edit").addQuery("type", "77").addQuery("t", "media/appmsg_edit").build();
    String LIST_DRAFT = MpUrlBuilder.cgi("/appmsg").action("list").addQuery("type", "77").addQuery("begin", 0).build();


    void createOrUpdateDraft(DraftModel draft);

    void deleteDraft(Long appMsgId);

    Optional<JSONObject> queryDrafts(Integer begin, Integer count);

}
