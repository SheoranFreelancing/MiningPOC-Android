package com.drill.utils;

/**
 * Created by groupon on 6/8/15.
 */
public class Constants {

    public static long LOGIN_DURATION = 14 * 24 * 60 * 60 * 1000;
    public static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    public static final String PREFS_NAME = "L3PrefsFile";
    public static final String LAST_ACCOUNT_NAME = "lastAccountName";
    public static final String LAST_LOGIN_TIME = "lastLoginTime";
    public static final String INVALID_LOGIN = "InvalidAccount";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String EMAIL_GROUPON = "groupon.com";
    public static final String MERCHANT_ID = "merchant_id";
    public static final String LOCATION_ID = "location_id";
    public static final String CRAWLED_LOCATION_ID = "crawled_location_id";
    public static final String DEAL_ID = "deal_id";
    public static final String EDIT_MERCHANT = "edit_merchant";
    public static final String EDIT_LOCATION = "edit_location";
    public static final String EDIT_DEAL = "edit_deal";
    public static final String CREATE_NEW_MERCHANT_FROM_CRAWL = "create_new_merchant_from_crawl";
    public static final String JP_CURRENCY_CODE = "JPY";
    public static final String JP_LANGUAGE_CODE = "ja";
    public static final String CONTRACT_APPROVED = "approved";
    public static final String CONTRACT_NOT_STARTED = "not-started";
    public static final String CONTRACT_PENDING_APPROVAL = "pending-approval";
    public static final String CONTRACT_APPROVAL_SENT = "approval-sent";
    public static final String CONTRACT_REQUEST_SENT = "request-sent";
    public static final String CONTRACT_URL = "https://s3-ap-southeast-1.amazonaws.com/com.groupon.halo.development/termsAndConditions/html/Mikke_Terms_%26_Condtitions_JP_20150821.html";

    public static final String DEFAULT_TIME = "HH:MM";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MMM-dd";
    public static final String DEFAULT_DATE_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String[] salesStatus = {"not-started", "called", "visited", "met", "verbal-agreement", "written-agreement", "rejected", "paused"};
    public static final String[] specialStatus = {"in-verification","verified"};
    public static final String[] specialProgress = {"Not Started", "Agreed", "Details Acceptable", "Web Images", "Waiting Clarification", "Verified"};

    public static final String _ID = "_id";
    public static class DealJson {
        public static final String DEAL = "deal";
        public static final String DEALS = "deals";
        public static final String DEAL_TITLE = "title";
        public static final String DEAL_DESCRIPTION = "description";
        public static final String DEAL_SHORT_DESCRIPTION = "shortDescription";
        public static final String DEAL_LONG_DESCRIPTION = "longDescription";
        public static final String DEAL_TAXONOMY = "taxonomy";
        public static final String DEAL_MERCHANT_LOCATION = "merchantLocation";
        public static final String DEAL_END_AT = "endAt";
        public static final String DEAL_DEAL_EXPIRES_AT = "expiresAt";
        public static final String DEAL_REDEMPTION_TIMINGS = "redemptionTimings";
        public static final String DEAL_IS_SOLD_OUT = "isSoldOut";
        public static final String DEAL_DAILY_LIMITS = "dailyLimits";
        public static final String DEAL_USER_LIMITS = "userLimits";
        public static final String DEAL_IMAGES = "images";
        public static final String DEAL_ORIGINAL_VALUE = "originalValue";
        public static final String DEAL_FINAL_VALUE = "finalValue";
        public static final String DEAL_PRICE = "price";
        public static final String DEAL_AMOUNT = "amount";
        public static final String DEAL_CURRENCY_CODE = "currencyCode";
        public static final String DEAL_FORMATTED_AMOUNT = "formattedAmount";
        public static final String DEAL_TOTAL_REDEMPTIONS = "totalRedemptions";
        public static final String DEAL_TOTAL_PURCHASES = "totalPurchases";
        public static final String DEAL_TYPE = "type";
        public static final String DEAL_STATUS = "status";
        public static final String DEAL_START_AT = "startAt";
        public static final String DEAL_HALO = "halo";
        public static final String DEAL_UPDATED_AT = "updatedAt";
        public static final String DEAL_CREATED_AT = "createdAt";
        public static final String DEAL_MAX_PURCHASE_QUANTITY = "maxPurchaseQuantity";
        public static final String DEAL_MAX_REDEMPTION_QUANTITY = "maxRedemptionQuantity";
        public static final String DEAL_OPEN_AT = "openAt";
        public static final String DEAL_CLOSE_AT = "closeAt";
        public static final String DEAL_DAYS_OPEN = "daysOpen";
        public static final String DEAL_TIME_MESSAGE = "message";
        public static final String DEAL_TAGS = "tags";
        public static final String DEAL_META_DATA = "metadata";
    }

    public static class MarchantJson {
        public static final String MERCHANT = "merchant";
        public static final String MERCHANTS = "merchants";
        public static final String MERCHANT_TITLE = "name";
        public static final String MERCHANT_DESCRIPTION = "description";
        public static final String MERCHANT_WEBSITE = "website";
        public static final String MERCHANT_TAXONOMY = "taxonomy";
        public static final String MERCHANT_META_DATA = "metadata";
        public static final String MERCHANT_UPDATED_AT = "updatedAt";
        public static final String MERCHANT_CREATED_AT = "createdAt";
    }
}
