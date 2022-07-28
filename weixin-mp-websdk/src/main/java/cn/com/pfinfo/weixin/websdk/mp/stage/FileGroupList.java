package cn.com.pfinfo.weixin.websdk.mp.stage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: pannnfei
 * Created by: CommonTools on 2022/7/28
 */
@Getter
@Setter
public class FileGroupList {
    private long bizUin;
    private List<FileGroupInfo> fileGroup;
    private List<Object> transferredBizUin;
}