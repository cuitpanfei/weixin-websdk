package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ShortContent {

    @Expose
    private List<Object> list;

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

}
