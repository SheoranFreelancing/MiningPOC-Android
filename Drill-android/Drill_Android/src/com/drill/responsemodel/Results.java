package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 25/09/15.
 */
public class Results {

    private List<Merchant> merchants = new ArrayList<Merchant>();
    private List<CrawledMerchant> crawledmerchants = new ArrayList<CrawledMerchant>();

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }

    public List<CrawledMerchant> getCrawledmerchants() {
        return crawledmerchants;
    }

    public void setCrawledmerchants(List<CrawledMerchant> crawledmerchants) {
        this.crawledmerchants = crawledmerchants;
    }
}
