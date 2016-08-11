package com.drill.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.drill.adaptors.MarkerInfoWindowAdapter;
import com.drill.android.R;
import com.drill.listeners.GPSTracker;
import com.drill.sync.RetroHttpManager;
import com.drill.ui.LT_BaseActivity;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.drill.utils.Constants.LATLNG_LIST;

public class MapViewActivity extends LT_BaseActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private HashMap<Marker, LatLng> mMarkersHashMap;
    android.location.Location loc;
    private GPSTracker gpsTracker;
    private List<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_actionbar);
        actionBar.setIcon(
                new ColorDrawable(getResources().getColor(R.color.transparent)));
        TextView title = (TextView) findViewById(R.id.activityTitle);
        title.setText("Map View");
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button editButton = (Button) findViewById(R.id.saveButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapViewActivity.this, WebGraphActivity.class);
                intent.putExtra(LATLNG_LIST,new ArrayList<>(mMarkersHashMap.values()));
                startActivity(intent);
            }
        });
        editButton.setText("Graph");

        polylines = new ArrayList<>();
        mMarkersHashMap = new HashMap<Marker, LatLng>();
        gpsTracker = new GPSTracker(this);
        setUpMap();
        loc = gpsTracker.getLocation();
        if(loc != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 18.0f));
        }

        Button btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLocation = (EditText) findViewById(R.id.et_location);
                // Getting user input location
                String location = etLocation.getText().toString();
                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        });
        Button btn_map_type = (Button) findViewById(R.id.btn_map_type);
        btn_map_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapTypeDialogBox();
            }
        });

        Button btn_map_route = (Button) findViewById(R.id.btn_map_route);
        btn_map_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findRoute();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(loc != null) {
//            RetroHttpManager.create(this, RetroHttpManager.REQUEST_GET_NEARBY_MERCHANTS).sendRequest(String.valueOf(loc.getLongitude()), String.valueOf(loc.getLatitude()));
//        }
    }

    private void setUpMap()
    {
        if (mMap == null)
        {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null)
            {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        marker.showInfoWindow();
                        return true;
                    }
                });
                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(MapViewActivity.this,mMarkersHashMap));
                mMap.setOnInfoWindowClickListener(infoWindowClickListener);
                mMap.setOnMarkerDragListener(onMarkerDragListener);
                mMap.setOnMapLongClickListener(onMapLongClickListener);
            }
            else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    private GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            marker.remove();
            mMarkersHashMap.remove(marker);
        }
    };

    private GoogleMap.OnMarkerDragListener onMarkerDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            mMarkersHashMap.put(marker, marker.getPosition());
        }
    };

    private GoogleMap.OnMapLongClickListener onMapLongClickListener  = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            Marker currentMarker = mMap.addMarker(new MarkerOptions()
                    .draggable(true)
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMarkersHashMap.put(currentMarker, latLng);
        }
    };

    private void showMapTypeDialogBox() {

        String[] mapType = {"NORMAL","SATELLITE","TERRAIN","HYBRID"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Map Type")
                .setItems(mapType, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mMap.setMapType(which + 1);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void findRoute() {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new ArrayList<>(mMarkersHashMap.values()))
                .build();
        routing.execute();
    }

    @Override
    public void onSuccessResponse(RetroHttpManager manager) {
        super.onSuccessResponse(manager);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {
        int[] COLORS = new int[]{R.color.black,R.color.light_grey,R.color.background_material_dark,R.color.light_grey,R.color.primary_dark_material_light};

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }


        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            } else {
                // Clears all the existing markers on the map
                mMap.clear();

                // Adding Markers on Google Map for each matching address
                for(int i=0;i<addresses.size();i++){

                    Address address = (Address) addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(addressText);

                    mMap.addMarker(markerOptions);

                    // Locate the first location
                    if(i==0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }

        }
    }

}
