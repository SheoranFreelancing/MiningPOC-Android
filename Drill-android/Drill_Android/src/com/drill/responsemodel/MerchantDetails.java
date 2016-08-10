package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 29/09/15.
 */
public class MerchantDetails {
    private List<MerchantLocation> merchantLocations = new ArrayList<MerchantLocation>();
    private Pagination pagination;

    public List<MerchantLocation> getMerchantLocations() {
        return merchantLocations;
    }

    public void setMerchantLocations(List<MerchantLocation> merchantLocations) {
        this.merchantLocations = merchantLocations;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
