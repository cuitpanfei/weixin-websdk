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
public class MpProfileModel {
    private String fakeid;
    private String round_head_img;
    private String nickname;
    private String alias;
    private String signature;

    @Override
    public String toString() {
        return String.format("<section class=\"mp_profile_iframe_wrp\">" +
                "<mpprofile class=\"js_uneditable custom_select_card mp_profile_iframe\" " +
                "data-pluginname=\"mpprofile\" data-id=\"%s\" data-headimg=\"%s\" " +
                "data-nickname=\"%s\" data-alias=\"%s\" data-signature=\"%s\" data-from=\"0\" " +
                "contenteditable=\"false\"></mpprofile></section>", fakeid, round_head_img, nickname, alias, signature);
    }
}
