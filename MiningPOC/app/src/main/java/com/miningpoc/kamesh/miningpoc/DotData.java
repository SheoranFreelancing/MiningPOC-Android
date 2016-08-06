package com.miningpoc.kamesh.miningpoc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Kamesh on 8/6/16.
 */

public class DotData {
    double x, y, z, dist;
    private DotData() {
        Random r = new Random();
        x = Math.pow(r.nextDouble(), 2);
        y = Math.pow(r.nextDouble(), 2);
        z = Math.pow(r.nextDouble(), 2);
        dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    private JSONObject getAsJSONObject() throws JSONException {
        JSONObject jsonObject =  new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("z", z);
        jsonObject.put("dist", dist);
        return jsonObject;
    }

    public static JSONArray getJSONArrayForData(final int size) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < size; i++) {
            jsonArray.put(new DotData().getAsJSONObject());
        }
        return jsonArray;
    }
}
