package com.drill.data;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Kamesh on 8/6/16.
 */

public class DotData {

    public static final String MARKERS_JSON_KEY = "markers_json";
    public static final String RODS_JSON_KEY = "rods_json";
    public static final int DEFAULT_ELEVATION = 0;
    public static final double MAX_DEPTH = 3.048; // 10feet
    public static final double ROD_LENGTH = 3.048; // 10feet
    public static final double BORING_ANGLE_DEGREES = 27; // degrees
    public static final double MAX_BEND_RADIUS = 32.85; // 107.8feet
    public static final double SIN_27_DEGREES = 0.45399049974;
    public static final double COS_27_DEGREES = 0.89100652418;
    public static final double TAN_27_DEGREES = 0.50952544949;
    public static final double RADIANS_OF_27DEGREES = 0.471239;
    public static final double GRADUAL_DECREMENT_ZRADIANS = 0.094;// (27/5)
    public static final double EARTH_RADIUS = 637100;//kmeters

    double x, y, z;
    double yRadians, zRadians;

    private DotData(double x, double y, double z, double zRadians) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.zRadians = zRadians;
    }

    private JSONObject getAsJSONObject() throws JSONException {
        JSONObject jsonObject =  new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("z", z);
        return jsonObject;
    }

    public static JSONObject getJSONArrayForData(ArrayList<LatLng> latlangList) throws JSONException {
        JSONArray jsonArrayMarkers = new JSONArray();
        //latlangList = getRandomLatLngList(5);
        ArrayList<DotData> markerCoordinatesList = new ArrayList<DotData>(latlangList.size());
        int i = 0;
        for(LatLng latLng : latlangList) {
            double x = getXCoord(latLng.latitude, latLng.longitude);
            double y = getYCoord(latLng.latitude, latLng.longitude);
            DotData dd = new DotData(x, y, DEFAULT_ELEVATION, 0);
            markerCoordinatesList.add(dd);
            jsonArrayMarkers.put(dd.getAsJSONObject());
            i++;
        }

        JSONObject jsonData = new JSONObject();
        jsonData.put(MARKERS_JSON_KEY, jsonArrayMarkers);
        jsonData.put(RODS_JSON_KEY, getRodPositionsList(markerCoordinatesList));

        return jsonData;
    }

    private static ArrayList<LatLng> getRandomLatLngList(int size) {
        ArrayList<LatLng> latlangList = new ArrayList<LatLng>(size);
        for(int i = 0; i < size; i++) {
            LatLng l = new LatLng(81 + 0.0000001*i*3, 74 + 0.0000001*i*3);
            latlangList.add(l);
        }
        return latlangList;
    }

    private static JSONArray getRodPositionsList(ArrayList<DotData> markerCoordinatesList) throws JSONException {
        JSONArray jsonRodsMarkers = new JSONArray();
        double hypotenuse = ROD_LENGTH/SIN_27_DEGREES;
        double p1 = ROD_LENGTH/TAN_27_DEGREES;
        double p2 = ROD_LENGTH/TAN_27_DEGREES;
        DotData first = markerCoordinatesList.get(0);
        DotData last = markerCoordinatesList.get(markerCoordinatesList.size()-1);
        final double averageSlope = (last.y - first.y) / (last.x - first.x);

        DotData rodData = new DotData(first.x, first.y, DEFAULT_ELEVATION, RADIANS_OF_27DEGREES);
        jsonRodsMarkers.put(rodData.getAsJSONObject());

        double noOfRodsInCurvedSection = hypotenuse/ROD_LENGTH * 2;
        double totalRouteLength = Math.abs(last.x - first.x);
        int totalNoOfRods = ((int)(noOfRodsInCurvedSection + (totalRouteLength-p1-p2)/ROD_LENGTH))+1;
        for (int i = 1; i<totalNoOfRods; i++) {
            DRILL_DIRECTION drillDirection = DRILL_DIRECTION.NONE;
            if(i < 5) {
                drillDirection = DRILL_DIRECTION.GOING_DOWN;
            } else if(i > totalNoOfRods - 4) {
                drillDirection = DRILL_DIRECTION.GOING_UP;
            }
            rodData = getNextRodData(rodData, drillDirection, averageSlope);
            jsonRodsMarkers.put(rodData.getAsJSONObject());
        }

//        rodData = new DotData(last, DEFAULT_ELEVATION);
//        jsonRodsMarkers.put(rodData.getAsJSONObject());

//        for(int i = 0; i < latlangList.size(); i++) {
//            DotData rodData = new DotData(latlangList.get(i), DEFAULT_ELEVATION + 20);
//            jsonRodsMarkers.put(rodData.getAsJSONObject());
//        }
        return jsonRodsMarkers;
    }

    private static DotData getNextRodData(DotData previousRod, DRILL_DIRECTION drillDirection, double averageSlope) {
        double deltaZRadians = previousRod.zRadians + (drillDirection.getValue() * GRADUAL_DECREMENT_ZRADIANS);
        double deltaX = Math.cos(deltaZRadians) * ROD_LENGTH;
        double deltaZ = Math.sin(deltaZRadians) * ROD_LENGTH * drillDirection.getValue();
        double deltaY = averageSlope * deltaX;
        DotData dd = new DotData(previousRod.x + deltaX, previousRod.y + deltaY, previousRod.z + deltaZ, deltaZRadians);
        return dd;
    }

    private static double getXCoord(double lat, double lon)
    {
        double x = (EARTH_RADIUS * Math.cos(toRadians(lat)) * Math.cos(toRadians(lon)));
//        if(x < 0) x = -x;
        return x;
    }

    private static double getYCoord(double lat, double lon)
    {
        double y = (EARTH_RADIUS * Math.cos(toRadians(lat)) * Math.sin(toRadians(lon)));
//        if(y < 0) y = -y;
        return y;
    }

    private static double toRadians(double valueInDegrees)
    {
        return java.lang.Math.toRadians(valueInDegrees);
    }
}
