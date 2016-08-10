package com.drill.ws.parsers;

import android.util.Log;

import com.drill.utils.LTLog;
import com.drill.utils.LT_SafeSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class GenericParser implements ResponseParser {

	public final static int INVALID_AUTHTOKEN = -53;
    public final static int NULL_RESPONSE_STATE = 0;
    public final static int STRING_RESPONSE_STATE = 1;
    public final static int JSONOBJECT_RESPONSE_STATE = 2;
    public final static int JSONARRAY_RESPONSE_STATE = 3;
    public final static int MAP_RESPONSE_STATE = 4;
    public final static int BAD_JSON_RESPONSE_STATE = -1;

	protected int requestType;
    public int responseState = NULL_RESPONSE_STATE;
    protected String message;
    private int status = 200;

	@Override
	public boolean processResponse(Object response) {
		if (response == null) {
			LTLog.e("GenericParser", "<<<NULL RESPONSE>>>");
            responseState = NULL_RESPONSE_STATE;
		}
        if(response instanceof JSONArray)
            responseState = JSONARRAY_RESPONSE_STATE;
        else if(response instanceof JSONObject) {
            responseState = JSONOBJECT_RESPONSE_STATE;
            updateMessage((JSONObject) response);
        }
        else if(response instanceof String)
            responseState = STRING_RESPONSE_STATE;
        else if(response instanceof Map)
            responseState = MAP_RESPONSE_STATE;

		return isValid();
	}

    public void updateMessage(JSONObject jsonObject) {
        try {
            if(jsonObject.has("error")) {
                JSONObject errorJson = jsonObject.getJSONObject("error");
                status = LT_SafeSetter.safeSetInt(errorJson, "status");
                message = LT_SafeSetter.safeSetString(errorJson, "message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MerchantsParser", e.getMessage());
        }
    }
	public boolean isValid() {
		return responseState > 0 && status == 200;
	}

	public int getRequestType() {
		return requestType;
	}

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
