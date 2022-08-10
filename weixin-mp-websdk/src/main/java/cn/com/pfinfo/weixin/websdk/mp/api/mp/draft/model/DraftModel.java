package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by cuitpanfei on 2022/08/10
 *
 * @author cuitpanfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DraftModel {
    private Long appMsgId;
    private String author;
    List<ArticleModel> articles;

    public final boolean isUpdate() {
        return appMsgId != null;
    }
}
