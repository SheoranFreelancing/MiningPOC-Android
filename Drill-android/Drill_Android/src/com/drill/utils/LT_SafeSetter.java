package com.drill.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LT_SafeSetter {

	public static String safeSetString(JSONObject json, String key) {
		if (json.has(key) && !json.isNull(key)) {
			try {
				String string = json.getString(key);
				if (string != null)
					return string;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public static boolean safeSetBoolean(Object boolValue) {
		if (boolValue != null) {
			if(boolValue instanceof Boolean)
				return ((Boolean) boolValue).booleanValue();
			else if(boolValue instanceof Integer)
				return ((Integer) boolValue).intValue() != 0;
			else if(boolValue instanceof String) {
				if(boolValue.equals("1")) 
					return true;
				if(((String)boolValue).equalsIgnoreCase("yes")) 
					return true;
				return Boolean.valueOf((String)boolValue);
			}
			else if(boolValue instanceof Long)
				return ((Long) boolValue).intValue() != 0;
			else if(boolValue instanceof Byte)
				return ((Byte) boolValue).intValue() != 0;
		}
		return false;
	}
	
	public static boolean safeSetBoolean(JSONObject json, String key) {
		if(json.has(key)) {
			try {
				boolean value = json.getBoolean(key);
				return value;
			} catch (JSONException e) {
				try {
					Object value = json.get(key);
					return LT_SafeSetter.safeSetBoolean(value);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static long safeSetLong(JSONObject json, String key) {
		if (json.has(key)) {
			try {
				long value = json.getLong(key);
				return value;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	public static int safeSetInt(JSONObject json, String key) {
		if (json.has(key)) {
			try {
				int value = json.getInt(key);
				return value;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	public static JSONObject safeSetObject(JSONObject json, String key) {
		if (json.has(key)) {
			try {
				Object object = json.get(key);
				if (object instanceof JSONObject)
					return (JSONObject) object;
				else if (object instanceof JSONArray) {
					return ((JSONArray) object).getJSONObject(0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static JSONArray safeSetArray(JSONObject json, String key) {
		if (json.has(key)) {
			try {
				Object objectList = json.get(key);
				if (objectList instanceof JSONArray)
					return (JSONArray) objectList;
				else if (objectList instanceof JSONObject) {
					JSONArray newList = new JSONArray();
					newList.put(objectList);
					return newList;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
