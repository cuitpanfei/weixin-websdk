package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft;

import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.DraftModel;
import cn.com.pfinfo.weixin.websdk.mp.http.MpUrlBuilder;

/**
 * created by cuitpanfei on 2022/08/10
 *
 * @author cuitpanfei
 */
public interface WxMpWebDraftService {

    String NEW_DRAFT_PAGE_URL = MpUrlBuilder.cgi("/appmsg").action("edit").addQuery("t", "media/appmsg_edit_v2").build();
    String DRAFT_INFO_URL = MpUrlBuilder.cgi("/appmsg").action("edit").addQuery("t", "media/appmsg_edit").build();


    void createDraft(DraftModel draft);

    void deleteDraft();

    void modifyDraft();

    void queryDrafts();

}
