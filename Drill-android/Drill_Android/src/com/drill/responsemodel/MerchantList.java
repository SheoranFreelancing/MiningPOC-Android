package com.drill.responsemodel;

import java.util.List;

/**
 * Created by GROUPON on 24/09/15.
 */
public class MerchantList {

    private Pagination pagination;

    List<Merchant> merchants = null;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }
}
