package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by groupon on 6/24/15.
 */
public class Deal {

    private String expiresAt;
    private String status;
    private String halo;
    private Integer V;
    private String endAt;
    private String type;
    private OriginalValue originalValue;
    private Integer totalPurchases;
    private String updatedAt;
    private String title;
    private Price price;
    private Integer totalRedemptions;
    private String _id;
    private RedemptionTimings redemptionTimings;
    private String createdAt;
    private DailyLimits dailyLimits;
    private String longDescription;
    private FinalValue finalValue;
    private List<Image> images = new ArrayList<Image>();
    private MerchantLocation merchantLocation;
    private List<String> tags = new ArrayList<String>();
    private String startAt;
    private Boolean isSoldOut;
    private String taxonomy;
    private Metadata metadata;

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHalo() {
        return halo;
    }

    public void setHalo(String halo) {
        this.halo = halo;
    }

    public Integer getV() {
        return V;
    }

    public void setV(Integer v) {
        V = v;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OriginalValue getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(OriginalValue originalValue) {
        this.originalValue = originalValue;
    }

    public Integer getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(Integer totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Integer getTotalRedemptions() {
        return totalRedemptions;
    }

    public void setTotalRedemptions(Integer totalRedemptions) {
        this.totalRedemptions = totalRedemptions;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public RedemptionTimings getRedemptionTimings() {
        return redemptionTimings;
    }

    public void setRedemptionTimings(RedemptionTimings redemptionTimings) {
        this.redemptionTimings = redemptionTimings;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public DailyLimits getDailyLimits() {
        return dailyLimits;
    }

    public void setDailyLimits(DailyLimits dailyLimits) {
        this.dailyLimits = dailyLimits;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public FinalValue getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(FinalValue finalValue) {
        this.finalValue = finalValue;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }


    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public Boolean getIsSoldOut() {
        return isSoldOut;
    }

    public void setIsSoldOut(Boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public MerchantLocation getMerchantLocation() {
        return merchantLocation;
    }

    public void setMerchantLocation(MerchantLocation merchantLocation) {
        this.merchantLocation = merchantLocation;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


}
