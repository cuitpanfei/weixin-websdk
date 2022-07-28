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
public class CgiData {
    private FileCnt fileCnt;
    private List<FileItemItem> fileItem;
    private FileGroupList fileGroupList;
    private String type;
    private int count;
    private int begin;
}