package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 25/09/15.
 */
public class CrawledMerchant {
    private String _id;
    private String Merchantid;
    private String city;
    private String country;
    private Double distanceFromHaloEpiCenter;
    private String extrainfo;
    private String featuredate;
    private List<Double> geolocation = new ArrayList<Double>();
    private String lunch;
    private String merchantname;
    private String phone;
    private String postalcode;
    private String ratings;
    private String reviewcount;
    private String source;
    private String sourcecategories;
    private String sourceurl;
    private String street;
    private String unformattedphone;
    private String website;
    private String claimStatus;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMerchantid() {
        return Merchantid;
    }

    public void setMerchantid(String merchantid) {
        Merchantid = merchantid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getDistanceFromHaloEpiCenter() {
        return distanceFromHaloEpiCenter;
    }

    public void setDistanceFromHaloEpiCenter(Double distanceFromHaloEpiCenter) {
        this.distanceFromHaloEpiCenter = distanceFromHaloEpiCenter;
    }

    public String getExtrainfo() {
        return extrainfo;
    }

    public void setExtrainfo(String extrainfo) {
        this.extrainfo = extrainfo;
    }

    public String getFeaturedate() {
        return featuredate;
    }

    public void setFeaturedate(String featuredate) {
        this.featuredate = featuredate;
    }

    public List<Double> getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(List<Double> geolocation) {
        this.geolocation = geolocation;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReviewcount() {
        return reviewcount;
    }

    public void setReviewcount(String reviewcount) {
        this.reviewcount = reviewcount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourcecategories() {
        return sourcecategories;
    }

    public void setSourcecategories(String sourcecategories) {
        this.sourcecategories = sourcecategories;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnformattedphone() {
        return unformattedphone;
    }

    public void setUnformattedphone(String unformattedphone) {
        this.unformattedphone = unformattedphone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
}
