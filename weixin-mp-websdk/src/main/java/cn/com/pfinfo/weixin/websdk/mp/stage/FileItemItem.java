package cn.com.pfinfo.weixin.websdk.mp.stage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: pannnfei
 * Created by: CommonTools on 2022/7/28
 */
@Setter
@Getter
public class FileItemItem {
    private int acceptAac;
    private String cdnUrl;
    private int createTime;
    private int fileId;
    private String name;
    private int playLength;
    private String size;
    private String title;
    private int groupId;
    private int transState;
    private int type;
    private int updateTime;
    private String videoCdnId;
    private String videoCdnUrl;
    private String videoThumbCdnUrl;
    private int voiceAacMediaSize;
    private int voiceCategory;
    private String voiceEncodeFileid;
    private int voiceHighMediaSize;
    private int voiceLowMediaSize;
    private String voiceMd;
    private int voiceVerifyState;
    private String titleQueryEncode;
    private List<OperationGroupsItem> operationGroups;
    private boolean canEditName;
    private boolean canDownload;

}