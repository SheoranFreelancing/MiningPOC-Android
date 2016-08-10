package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 28/09/15.
 */
public class Image {
    private String Id;
    private String url;
    private String type;
    private List<String> tags = new ArrayList<String>();

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
