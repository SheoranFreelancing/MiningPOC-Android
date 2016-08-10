package com.drill.utils;

import android.text.TextUtils;

/**
 * Created by groupon on 6/15/15.
 */
public class Utils {
    public static String EMPTY_STRING = "";
    public static String setSafeString(String value) {
        if(null == value || TextUtils.isEmpty(value) )
            return EMPTY_STRING;
        return value;
    }

}
