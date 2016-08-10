package com.drill.db;

import android.provider.BaseColumns;

/**
 * Created by groupon on 8/10/15.
 */

public class MerchantReaderContract {

    public MerchantReaderContract() {
    }
    /* Inner class that defines the table contents */
    public static abstract class MerchantEntry implements BaseColumns {
        public static final String TABLE_NAME = "merchant";
        public static final String COLUMN_NAME_MERCHANT_ID = "_id";
        public static final String COLUMN_NAME_MERCHANT_NAME_ENGLISH = "name";
        public static final String COLUMN_NAME_MERCHANT_DESCRIPTION = "description";
        public static final String COLUMN_NAME_MERCHANT_CREATEDAT = "createdAt";
        public static final String COLUMN_NAME_MERCHANT_UPDATEDAT = "updatedAt";
        public static final String COLUMN_NAME_MERCHANT_TAXONOMY = "taxonomy";
        public static final String COLUMN_NAME_MERCHANT_TAGS = "tags";
        public static final String COLUMN_NAME_MERCHANT_WEBSITE = "website";
        public static final String COLUMN_NAME_MERCHANT_META_DATA = "meta_data";

    }

    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_LOCATION_ID = "_id";
        public static final String COLUMN_NAME_LOCATION_MERCHANT_ID = "merchantId";
        public static final String COLUMN_NAME_LOCATION_HALO_ID = "haloId";
        public static final String COLUMN_NAME_LOCATION_NAME = "name";
        public static final String COLUMN_NAME_LOCATION_CREATEDAT = "createdAt";
        public static final String COLUMN_NAME_LOCATION_UPDATEDAT = "updatedAt";
        public static final String COLUMN_NAME_LOCATION_WEBSITE = "website";
        public static final String COLUMN_NAME_LOCATION_DESCRIPTION = "description";
        public static final String COLUMN_NAME_LOCATION_LQS = "lqs";
        public static final String COLUMN_NAME_LOCATION_STATUS = "status";
        public static final String COLUMN_NAME_LOCATION_ADDRESS = "address";
        public static final String COLUMN_NAME_LOCATION_GEOCOORDINATES = "geocoordinates";
        public static final String COLUMN_NAME_LOCATION_TIMINGS = "timings";
        public static final String COLUMN_NAME_LOCATION_META_DATA = "meta_data";
        public static final String COLUMN_NAME_LOCATION_CONTACT = "contact";
        public static final String COLUMN_NAME_LOCATION_MANAGEMENT_CONTACT = "managementContact";
        public static final String COLUMN_NAME_LOCATION_IMAGE_URLS = "image_urls";
        public static final String COLUMN_NAME_LOCATION_CONTRACT = "contract";

    }

    public static abstract class DealEntry implements BaseColumns {
        public static final String TABLE_NAME = "deal";
        public static final String COLUMN_NAME_DEAL_ID = "_id";
        public static final String COLUMN_NAME_DEAL_TITLE = "title";
        public static final String COLUMN_NAME_DEAL_HALO = "halo";
        public static final String COLUMN_NAME_DEAL_MERCHANT_LOCATION = "merchantLocation";
        public static final String COLUMN_NAME_DEAL_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_NAME_DEAL_LONG_DESCRIPTION = "longDescription";
        public static final String COLUMN_NAME_DEAL_CREATEDAT = "createdAt";
        public static final String COLUMN_NAME_DEAL_UPDATEDAT = "updatedAt";
        public static final String COLUMN_NAME_DEAL_EXPIRESAT = "expiresAt";
        public static final String COLUMN_NAME_DEAL_STARTAT = "startAt";
        public static final String COLUMN_NAME_DEAL_ENDAT = "endAt";
        public static final String COLUMN_NAME_DEAL_ACTIVATESAT = "activatesAt";
        public static final String COLUMN_NAME_DEAL_TAXONOMY = "taxonomy";
        public static final String COLUMN_NAME_DEAL_ISSOLDOUT = "isSoldOut";
        public static final String COLUMN_NAME_DEAL_TOTAL_REDEMPTIONS = "totalRedemptions";
        public static final String COLUMN_NAME_DEAL_TOTAL_PURCHASES = "totalPurchases";
        public static final String COLUMN_NAME_DEAL_TYPE = "type";
        public static final String COLUMN_NAME_DEAL_STATUS = "status";
        public static final String COLUMN_NAME_DEAL_REDEMPTION_TIMINGS = "redemptionTimings";
        public static final String COLUMN_NAME_DEAL_FEATURE_LIMIT = "featureLimits";
        public static final String COLUMN_NAME_DEAL_USER_LIMIT = "userLimits";
        public static final String COLUMN_NAME_DEAL_FINAL_VALUE = "finalValue";
        public static final String COLUMN_NAME_DEAL_ORIGINAL_VALUE = "originalValue";
        public static final String COLUMN_NAME_DEAL_PRICE = "price";
        public static final String COLUMN_NAME_DEAL_IMAGES = "images";
        public static final String COLUMN_NAME_DEAL_META_DATA = "meta_data";
        public static final String COLUMN_NAME_DEAL_TAGS = "tags";

    }
}