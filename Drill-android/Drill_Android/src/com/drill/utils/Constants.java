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
    public static final String LATLNG_LIST = "latlng_list";

    public static final String DEFAULT_TIME = "HH:MM";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MMM-dd";
    public static final String DEFAULT_DATE_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String[] salesStatus = {"not-started", "called", "visited", "met", "verbal-agreement", "written-agreement", "rejected", "paused"};
    public static final String[] specialStatus = {"in-verification","verified"};
    public static final String[] specialProgress = {"Not Started", "Agreed", "Details Acceptable", "Web Images", "Waiting Clarification", "Verified"};

    public static final String _ID = "_id";
}
