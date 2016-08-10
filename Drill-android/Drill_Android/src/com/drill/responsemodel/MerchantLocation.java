package com.drill.responsemodel;

import android.text.TextUtils;

import com.drill.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 29/09/15.
 */
public class MerchantLocation {
    private String _id;
    private String name;
    private String merchant;
    private String description;
    private String website;
    private MerchantTimings merchantTimings;
    private Contact contact;
    private Management management;
    private Integer V;
    private List<Double> location = new ArrayList<Double>();
    private List<Double> geolocation = new ArrayList<Double>();
    private Contract contract;
    private Metadata metadata;
    private Address address;
    private List<Image> images = new ArrayList<Image>();
    private String status;
    private String halo;
    private String createdAt;
    private String updatedAt;

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

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
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

    public MerchantTimings getMerchantTimings() {
        return merchantTimings;
    }

    public void setMerchantTimings(MerchantTimings merchantTimings) {
        this.merchantTimings = merchantTimings;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Management getManagement() {
        return management;
    }

    public void setManagement(Management management) {
        this.management = management;
    }

    public Integer getV() {
        return V;
    }

    public void setV(Integer v) {
        V = v;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public List<Double> getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(List<Double> geolocation) {
        this.geolocation = geolocation;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getSalesStatus() {
        int index = 0;
        String status = "";
            status = getStatus();
            for (int i = 0; i < Constants.salesStatus.length; i++) {
                if (Constants.salesStatus[i].equalsIgnoreCase(status)) {
                    index = i;
                    break;
                }
            }
        return index;
    }

    public String getHalo() {
        return halo;
    }

    public void setHalo(String halo) {
        this.halo = halo;
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

    public String getAddressDetails() {
        Address address = getAddress();

        String[] data = {address.getStreetNumber(),address.getStreetName(),address.getCity(),address.getState(),address.getPostalCode()};
        String output = "";
        for (int i = 0; i <data.length; i++) {
            if(!TextUtils.isEmpty(data[i])) {
                output += data[i]+", ";
            }
        }
        return output.substring(0,output.length()-2);
    }
}
