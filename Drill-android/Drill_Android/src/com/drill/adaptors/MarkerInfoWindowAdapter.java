package com.drill.adaptors;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.drill.android.R;
import com.drill.responsemodel.CrawledMerchant;
import com.drill.responsemodel.Merchant;
import com.drill.responsemodel.MerchantLocation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by groupon on 9/9/15.
 */

public class MarkerInfoWindowAdapter implements InfoWindowAdapter {

    private Activity context;

    public MarkerInfoWindowAdapter(Activity context, HashMap<Marker, LatLng> markersHashMap) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = context.getLayoutInflater().inflate(R.layout.map_infowindow_layout, null);
        return v;
    }
}
