package com.drill.data;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kamesh on 8/6/16.
 */

public class DotData {
    double x, y, z, dist;

    private DotData(LatLng latLng) {
        x = latLng.latitude;
        y = latLng.longitude;
        this.z = 2000;
        dist = 200;
    }

    private DotData(LatLng latLng, double z) {
        x = latLng.latitude;
        y = latLng.longitude;
        this.z = z;
        dist = 20;
    }

    private DotData(int i) {
        Random r = new Random();
        x = i*3+r.nextDouble();
        y = i*2+r.nextDouble();
        z = i+10+r.nextDouble();
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
            jsonArray.put(new DotData(i).getAsJSONObject());
        }
        return jsonArray;
    }

    public static JSONArray getJSONArrayForData(ArrayList<LatLng> latlangList) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        for(LatLng latLng : latlangList) {
            jsonArray.put(new DotData(latLng).getAsJSONObject());
            int z = (latlangList.size() - Math.abs(latlangList.size()/2 - i)) * 10;
            jsonArray.put(new DotData(latLng, z).getAsJSONObject());
            i++;
        }
        return jsonArray;
    }
}
