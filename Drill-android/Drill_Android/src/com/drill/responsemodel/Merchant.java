package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 25/09/15.
 */
public class Merchant {

    private String _id;
    private String name;
    private String description;
    private String website;
    private String taxonomy;
    private Integer _v;
    private Metadata metadata;
    private List<Object> tags = new ArrayList<Object>();
    private String createdAt;
    private String updatedAt;
    private List<MerchantLocation> locations = new ArrayList<MerchantLocation>();


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public Integer get_v() {
        return _v;
    }

    public void set_v(Integer _v) {
        this._v = _v;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MerchantLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<MerchantLocation> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "merchantId=" + _id +
                ", merchantEnglishName='" + name + '\'' +
                '}';
    }
}
