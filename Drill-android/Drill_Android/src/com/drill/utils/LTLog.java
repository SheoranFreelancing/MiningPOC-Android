package com.drill.utils;

import android.util.Log;

public class LTLog {

	public static final int OFF = 0;
	public static final int V = 1;
	public static final int D = 2;
	public static final int I = 3;
	public static final int W = 4;
	public static final int E = 5;

	public static final int LT_UI = 10;
	public static final int LT_RF = 11;
	public static final int LT_DB = 12;

	public static int loggerLevel = 1;
	
	private static String ltLogTag = "#LTLog#- ";

	public static void d(String tag, String msg) {
		if (loggerLevel >= D) {
			Log.d(tag, ltLogTag + msg);
		}
	}

	public static void i(String tag, String msg) {
		if (loggerLevel >= I) {
			Log.i(tag, ltLogTag + msg);
		}
	}

	public static void e(String tag, String msg) {
		if (loggerLevel >= E) {
			Log.e(tag, ltLogTag + msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (loggerLevel >= E) {
			Log.e(tag, ltLogTag + msg, tr);
		}
	}

	public static void v(String tag, String msg) {
		if (loggerLevel >= V) {
			Log.v(tag, ltLogTag + msg);
		}
	}

	public static void w(String tag, String msg) {
		if (loggerLevel >= W) {
			Log.w(tag, ltLogTag + msg);
		}
	}

	public static void setLoggerLevel(int level) {
		loggerLevel = level;
	}

	public static int getLoggerCode(String className) {
		return 0;
	}
}