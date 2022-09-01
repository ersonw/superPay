package com.example.superpay.entity;

import com.example.superpay.util.ToolsUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString(includeFieldNames = true)
@Setter
@Getter
@Document(collection = "shortLink")
public class ShortLink {
    public ShortLink(String link) {
        id = ToolsUtil.getToken();
        this.link = link;
        this.addTime = System.currentTimeMillis();
    }
    public ShortLink(){
        id = ToolsUtil.getToken();
    }
    @Id
    private String id;
    private String link=null;
    private long addTime=0L;
}
