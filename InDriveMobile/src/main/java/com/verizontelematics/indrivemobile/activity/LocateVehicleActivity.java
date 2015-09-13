package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.utils.ui.CustomInfoWindowAdapter;
import com.verizontelematics.indrivemobile.utils.ui.MapUtils;

import java.util.ArrayList;

public class LocateVehicleActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,LocationListener,GoogleMap.OnMapLoadedCallback, View.OnClickListener{

    private static final String TAG = LocateVehicleActivity.class.getCanonicalName();

    private GoogleMap googleMap;
    private MapView locateVehicleMapView;
    private double mSelectedLatitude;
    private double mSelectedLongitude;
    private Marker mMarker;
    private Context mContext;
    private LocationClient mLocationClient;
    private Location mVehicleLocation;
    private Button btnGetDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_vehicle);
        locateVehicleMapView = (MapView) this.findViewById(R.id.locateVehicleMapView);
        locateVehicleMapView.onCreate(savedInstanceState);
        mContext = this;
        setupUI();
        // stubbed vehicle Location
        mSelectedLatitude = 33.7465187;
        mSelectedLongitude = -84.4046342;
        // stub ended.
    }

    private void setupUI() {

        setupHeaderView();
        googleMap = locateVehicleMapView.getMap();
        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
            googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    LatLng position = marker.getPosition();
                    if (googleMap.getMyLocation() == null) {
                        Toast.makeText(mContext, "Current location not available ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MapUtils.launchNavigationActivity((Activity)mContext, new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), position);
                }
            });
        }
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Updates the location and zoom of the MapView
        if (googleMap != null) {
            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    // TODO: ..
                }
            });
            googleMap.setOnMapLoadedCallback(this);
        }
        btnGetDirections = (Button)findViewById(R.id.google_maps_get_direction_btn);
        btnGetDirections.setOnClickListener(this);
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerLayout);
        headerRL.setBackgroundResource(R.drawable.location_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerBtn.setVisibility(View.INVISIBLE);
        headerTitleTV.setText(this.getResources().getString(R.string.locate_vehicle));
    }


    @Override
    protected void onResume() {
        super.onResume();
        locateVehicleMapView.onResume();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locateVehicleMapView.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    this,
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locate_vehicle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLoaded() {
        Log.d(TAG, "onMapLoaded");
        locateVehicle();
    }

    private LatLng getCurrentLocation() {
        // Stubbed not yet the decision to take exact current location
        // or last available location.
        if (googleMap != null && googleMap.getMyLocation() != null) {
            return new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
        }
        // stub ended.
        if (mLocationClient != null && mLocationClient.isConnected()) {
            return new LatLng(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude());
        }
        return null;
    }
    private void locateVehicle() {
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        if (mSelectedLatitude != 0 && mSelectedLongitude != 0)
            locations.add(new LatLng(mSelectedLatitude, mSelectedLongitude));

        LatLng currentLocation = getCurrentLocation();
        if (currentLocation != null)
            locations.add(currentLocation);

        mMarker = MapUtils.drawVehicleMarker(googleMap, new LatLng(mSelectedLatitude, mSelectedLongitude));

        MapUtils.animateClipTo(googleMap, locations);

        if (mMarker != null) {
            MapUtils.setMarkerWithAddress(mContext, mMarker, new LatLng(mSelectedLatitude, mSelectedLongitude));
        }
    }

    private void showLocation(){
        if (mLocationClient != null && mLocationClient.isConnected()) {
            if(mVehicleLocation == null){
                Location mCurrentLocation = mLocationClient.getLastLocation();
                animateLocations(mCurrentLocation);
            }else{
                animateLocations(mVehicleLocation);
            }
            btnGetDirections.setEnabled(true);
            btnGetDirections.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.google_maps_get_direction_btn) {
           getDirections();
        }
    }

    private void getDirections(){
        if (mSelectedLongitude == 0 || mSelectedLatitude == 0) {
            Toast.makeText(this, "Invalid vehicle location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getCurrentLocation() == null) {
            Toast.makeText(this, "Cannot get direction as current location not available", Toast.LENGTH_SHORT).show();
            return;
        }
        // below code is stubbed for demo,
        double fromLatitude = getCurrentLocation().latitude;
        double fromLongitude = getCurrentLocation().longitude;
        // stub ended.
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?" + "saddr="
                        + fromLatitude + ","
                        + fromLongitude + "&daddr="
                        + mSelectedLatitude + ","
                        + mSelectedLongitude));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void animateLocations(Location loc){
        LatLng  location = new LatLng(loc.getLatitude(), loc.getLongitude());
        mMarker = MapUtils.drawVehicleMarker(googleMap, new LatLng(loc.getLatitude(), loc.getLongitude()));
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        locations.add(location);
        MapUtils.animateClipTo(googleMap, locations);
        if (mMarker != null) {
            MapUtils.setMarkerWithAddress(mContext, mMarker, new LatLng(loc.getLatitude(), loc.getLongitude()));
        }
    }
}
