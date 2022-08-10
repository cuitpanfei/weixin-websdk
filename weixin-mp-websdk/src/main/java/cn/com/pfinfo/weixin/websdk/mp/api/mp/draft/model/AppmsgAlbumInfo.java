
package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class AppmsgAlbumInfo {

    @SerializedName("appmsg_album_infos")
    private List<Object> appmsgAlbumInfos;

    public List<Object> getAppmsgAlbumInfos() {
        return appmsgAlbumInfos;
    }

    public void setAppmsgAlbumInfos(List<Object> appmsgAlbumInfos) {
        this.appmsgAlbumInfos = appmsgAlbumInfos;
    }

}
