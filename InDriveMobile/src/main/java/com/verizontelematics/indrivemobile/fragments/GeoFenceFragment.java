package com.verizontelematics.indrivemobile.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.animations.AnimatedView;
import com.verizontelematics.indrivemobile.customViews.CustomDrawer;
import com.verizontelematics.indrivemobile.unistroke.dollar.Dollar;
import com.verizontelematics.indrivemobile.unistroke.dollar.Point;
import com.verizontelematics.indrivemobile.unistroke.dollar.Utils;
import com.verizontelematics.indrivemobile.utils.ui.AnimationRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by z689649 on 9/4/14.
 */
public class GeoFenceFragment extends Fragment implements CustomDrawer.TouchCallbacks, View.OnClickListener, GoogleMap.OnInfoWindowClickListener{

    private Dollar mDollar                              = new Dollar();
    private MapView mapView                             = null;
    private CustomDrawer mDrawer                        = null;
    private GoogleMap googleMap                         = null;
    private float map_zoom_level                            = 14.0f;
    private final float map_zoom_step                       = 0.3f;
    private int fill_color_circle_map                       = (128 << 24 ) | (0 << 16 ) | (0x00 << 8 ) | 0xFF;
    private int geoFenceStartRadius = 0;
    private LatLng circleCenterLatLng                       = null;
    private Circle lastCircle                               = null;
    private boolean locationChangedProcessed                = false;
    private AnimationRunnable lastAnimationPostedRunnable   = null;

    protected static final int CHARGING_BUTTON_MARGIN_IN_DP = 2;
    protected static final long STEP_TIME                   = 50;
    protected static final int MAX_STEP                     = 20;
    protected static final int MAX_OPACITY_VALUE            = 255;
    private Marker currentMarker = null;

    private View optionsParentLayout    = null;

    private Polygon polygon = null;
    private List<Marker> markerList = new ArrayList<Marker>();


     /*
    private Runnable getRunnable() {
        if (lastAnimationPostedRunnable != null) {
            lastAnimationPostedRunnable.setStopped(true);
        }
        lastAnimationPostedRunnable = new AnimationRunnable(drawRootView,
                originalColor);
        return lastAnimationPostedRunnable;
    }
    private void resetAnimation() {
        if (lastAnimationPostedRunnable != null) {
            lastAnimationPostedRunnable.setStopped(true);
        }
    }

    private void animateStartStopButton(boolean isDrawing) {
        // if we are already showing animation just return from here.
        if (isDrawing) {
            if (lastAnimationPostedRunnable != null
                    && !lastAnimationPostedRunnable.isStopped()) {
                return;
            }
        }

        resetAnimation();

        LinearLayout.LayoutParams lLayoutParams = (LinearLayout.LayoutParams) drawParentView
                .getLayoutParams();
        int lDpToPixel = 0;
        if (isDrawing) {
            lDpToPixel = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, CHARGING_BUTTON_MARGIN_IN_DP,
                    getActivity().getResources()
                            .getDisplayMetrics()
            );
            mapView.postDelayed(getRunnable(), STEP_TIME);
        } else {
            // no color.
            drawRootView.setBackgroundColor(0x00000000);
        }
        lLayoutParams
                .setMargins(lDpToPixel, lDpToPixel, lDpToPixel, lDpToPixel);
        drawParentView.setLayoutParams(lLayoutParams);
        drawParentView.requestLayout();
    }
    */

    private void initTutorial(View rootView){
        final RelativeLayout layout = (RelativeLayout)rootView.findViewById(R.id.animation_view_layout);
        layout.setBackgroundResource(R.color.transparent_tutorial);
        TextView startTut = (TextView)rootView.findViewById(R.id.startTutorial);
        TextView endTut = (TextView)rootView.findViewById(R.id.endTutorial);
        final AnimatedView animView = (AnimatedView)rootView.findViewById(R.id.anim_view);
        startTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.setVisibility(View.VISIBLE);
            }
        });

        endTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        // load the color values here.
        int originalColor = getActivity().getResources().getColor(
                R.color.indrive_anim_green_color);

        View lView  = inflater.inflate(R.layout.geofence_frag,null);

        initTutorial(lView);

        mDrawer = (CustomDrawer) lView.findViewById(R.id.customDrawer);
        // finally it has to be moved from a fragment to another view.
        mapView = (MapView) lView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        // draw parent and root views.


        View lDrawButton =  lView.findViewById(R.id.draw_button);
        lDrawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDrawer.setMode(CustomDrawer.DrawMode.MODE_DRAW);
                toggleDrawerView();
                toggleOptionsView();
            }
        });

        googleMap = mapView.getMap();
        if (googleMap == null)
            return lView;
        try {
            MapsInitializer.initialize(lView.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnInfoWindowClickListener(this);
        mDrawer.setRecognizer(this);

        lView.findViewById(R.id.zoomIn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mDrawer.getVisibility() == View.VISIBLE){
                    resizeCircle(true);
                }else{
                    map_zoom_level += map_zoom_step;
                    checkMaxZoomLevelAndResetCurrentMapZoomLevel();
                    changeZoomLevel();
                }
            }
        });

        lView.findViewById(R.id.zoomOut).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mDrawer.getVisibility() == View.VISIBLE){
                    resizeCircle(false);
                }else{
                    map_zoom_level -= map_zoom_step;
                    checkMinZoomLevelAndResetCurrentMapZoomLevel();
                    changeZoomLevel();
                }
            }
        });


        /*
        // correct solution for getting the current location after the map finished loading.
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                mapView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            centerMapOnMyLocation();

                    }
                }, 1000);
            }
        });
        */



        HomeActivity lActivity = (HomeActivity) getActivity();
//        lActivity.showTabBar(false);
        View moreButton = lView.findViewById(R.id.more);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOptionsView();
            }
        });

        optionsParentLayout = lView.findViewById(R.id.optionsParentLayout);

        lView.findViewById(R.id.drop_button_geofence).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                toggleDrawerView();
                toggleOptionsView();
                mDrawer.setMode(CustomDrawer.DrawMode.MODE_DROP);
            }
        });

        centerMapOnMyLocation();

        return lView;
    }



    private void toggleDrawerView() {
        if(mDrawer.getVisibility() == View.VISIBLE){
            mDrawer.setVisibility(View.GONE);
        }
        else{
            mDrawer.reset();
            //animateStartStopButton(true);
            mDrawer.setVisibility(View.VISIBLE);
        }

    }

    private void toggleOptionsView(){
        if(optionsParentLayout.getAnimation() != null){
            return;
        }

        int slideInAnimationDuration = 500;
        if(optionsParentLayout.getVisibility() == View.VISIBLE){
            int lHeight = optionsParentLayout.getHeight();
            TranslateAnimation anim = new TranslateAnimation( 0,0,0,lHeight);
            anim.setDuration(slideInAnimationDuration);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    optionsParentLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            optionsParentLayout.startAnimation(anim);


        }else{
            resetRadioSelection();
            optionsParentLayout.setVisibility(View.VISIBLE);
            int lHeight = optionsParentLayout.getHeight();
            TranslateAnimation anim = new TranslateAnimation( 0,0,lHeight,0);
            anim.setDuration(slideInAnimationDuration);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            optionsParentLayout.startAnimation(anim);
        }
    }

    private void resetRadioSelection(){
        RadioGroup lRg = (RadioGroup) optionsParentLayout.findViewById(R.id.radio_group_id);
        lRg.clearCheck();
    }
    private void changeZoomLevel() {
        if (googleMap != null)
            googleMap.animateCamera( CameraUpdateFactory.zoomTo(map_zoom_level) );
    }

    private void checkMaxZoomLevelAndResetCurrentMapZoomLevel(){
        if(googleMap != null && googleMap.getMaxZoomLevel() < map_zoom_level){
            map_zoom_level = googleMap.getMaxZoomLevel();
        }
    }

    private void checkMinZoomLevelAndResetCurrentMapZoomLevel(){
        if(googleMap != null && googleMap.getMinZoomLevel() > map_zoom_level){
            map_zoom_level = googleMap.getMinZoomLevel();
        }
    }

    private void centerMapOnMyLocation() {
        if (googleMap == null) {
            return;
        }
        Location location = getCurrentLocation();
        if (location != null) {
            LatLng myLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());
            animateMapViewTo(myLocation);
        }
    }

    Location getCurrentLocation()
    {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        Location location = null;
        String[] providers = new String[3];
        providers[0] = LocationManager.GPS_PROVIDER;
        providers[1] = LocationManager.NETWORK_PROVIDER;
        providers[2] = LocationManager.PASSIVE_PROVIDER;

        for ( String provider : providers)
        {
            if (!locationManager.isProviderEnabled(provider)) continue;
            if (locationManager != null)
            {
                location = locationManager.getLastKnownLocation(provider);
                if (location != null)
                {
                    break;
                }
            }
        }
        return location;
    }

    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    public void onResume(){
        super.onResume();
        mapView.onResume();
    }


    public void onStart(){
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HomeActivity lActivity = (HomeActivity) getActivity();
        lActivity.showTabBar(true);
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void animateMapViewTo(LatLng position) {
        int default_map_tilt = 0;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(map_zoom_level)
                .tilt(default_map_tilt)
                .build();
        if (googleMap != null)
            googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        googleMap.animateCamera( CameraUpdateFactory.zoomTo(map_zoom_level) );
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void addPoint(int x, int y) {
        mDollar.addPoint(x,y);
    }

    @Override
    public void clear() {
        mDollar.clear();
        if (googleMap != null)
            googleMap.clear();

    }

    @Override
    public void end() {
        mDollar.recognize();
        String lShape = mDollar.getName();
        if(lShape.equalsIgnoreCase("circle CCW") || lShape.equalsIgnoreCase("circle CW")){
            drawGeofenceOnMap(mDollar.getPoints());
        }else{

        }
        toggleDrawerView();
        mDrawer.reset();

    }

    @Override
    public void longPress(int x, int y) {
        drawCircleForLongPress(x,y);
    }
    /**
     * Draw a polygon on map.
     */
    public void drawGeofenceOnMap(Vector<Point> points){
        if (googleMap == null) {
            return;
        }

        /*
        clearMarkersAndPolygon();
        markerList.clear();
        populateMarkers(points);
        toggleDrawerView();
        */

        Point lCenterPoint = Utils.Centroid(points);
        CircleOptions lCircleOptions = new CircleOptions();
        circleCenterLatLng = googleMap.getProjection().fromScreenLocation(new android.graphics.Point((int) lCenterPoint.X, (int) lCenterPoint.Y));
        Point lMostRemotePoint = Utils.getMostRemotePoint(lCenterPoint, points);
        //LatLng lStartingPointLatLng = googleMap.getProjection().fromScreenLocation(new android.graphics.Point((int) points.get(0).X, (int)  points.get(0).Y));
        LatLng lStartingPointLatLng = googleMap.getProjection().fromScreenLocation(new android.graphics.Point((int) lMostRemotePoint.X, (int)  lMostRemotePoint.Y));

        lCircleOptions.center(circleCenterLatLng);

        Location lLocationCenter = new Location("TestProvider");
        lLocationCenter.setLatitude(circleCenterLatLng.latitude);
        lLocationCenter.setLongitude(circleCenterLatLng.longitude);

        Location lLocationStartingPoint = new Location("TestProvider");
        lLocationStartingPoint.setLatitude(lStartingPointLatLng.latitude);
        lLocationStartingPoint.setLongitude(lStartingPointLatLng.longitude);

        float radius = lLocationCenter.distanceTo(lLocationStartingPoint);
        geoFenceStartRadius = (int)radius;
        lCircleOptions.radius(geoFenceStartRadius);
        lCircleOptions.strokeColor(Color.RED).fillColor(fill_color_circle_map);
        if(lastCircle != null) lastCircle.remove();
        lastCircle = googleMap.addCircle(lCircleOptions);
        currentMarker = googleMap.addMarker(new MarkerOptions().position(circleCenterLatLng).title("GeoFence 1"));
        getAddress();


    }

    private void clearMarkersAndPolygon(){
        for(Marker lMarker:markerList){
            lMarker.remove();
        }
        if(polygon != null){
            polygon.remove();
        }
    }

    private void populateMarkers(Vector<Point> aPoints){
        clearMarkersAndPolygon();
        int lSize = aPoints.size();
        int lNumberOfMarkers = lSize / 10 ;
        for(int i=0; i < lSize ; i+= lNumberOfMarkers){
            Marker lMarker = googleMap.addMarker(new MarkerOptions().position(googleMap.getProjection().fromScreenLocation(new android.graphics.Point((int) aPoints.get(i).X, (int)  aPoints.get(i).Y))));
            markerList.add(lMarker);
        }
        DrawPolygon(true);
    }

   private void DrawPolygon(boolean aBothPolygonAndMarkers){

       if(aBothPolygonAndMarkers) {
           clearMarkersAndPolygon();
       }else{
           if(polygon != null){
               polygon.remove();
           }
       }
        PolygonOptions polygonOptions = new PolygonOptions();
        ArrayList<Marker> markerList = new ArrayList<Marker>();
        for(int i=0; i < this.markerList.size(); i++ ) {


            LatLng lPosition = this.markerList.get(i).getPosition();
            polygonOptions.add(lPosition);
            if(aBothPolygonAndMarkers) {
                Marker lMarker = googleMap.addMarker(new MarkerOptions().position(lPosition).title("GeoFence"));
                markerList.add(lMarker);
                lMarker.setDraggable(true);
            }

        }

       if(aBothPolygonAndMarkers) {
           this.markerList.clear();
           this.markerList = markerList;
       }

        googleMap.setOnMarkerDragListener( new GoogleMap.OnMarkerDragListener() {
           @Override
           public void onMarkerDragStart(Marker marker) {
               // nothing.
           }

           @Override
           public void onMarkerDrag(Marker marker) {
               DrawPolygon(false);
           }

           @Override
           public void onMarkerDragEnd(Marker marker) {
               DrawPolygon(true);
           }
        });
        polygonOptions.fillColor(Color.GREEN);
        polygonOptions.strokeColor(Color.RED);
        polygon =  googleMap.addPolygon(polygonOptions);
    }
    /**
     * addCircle for long press.
     * @param x
     * @param y
     */
    public void drawCircleForLongPress(int x, int y) {
        if (googleMap == null)
            return;
        CircleOptions lCircleOptions = new CircleOptions();
        circleCenterLatLng = googleMap.getProjection().fromScreenLocation(new android.graphics.Point(x, y));
        lCircleOptions.center(circleCenterLatLng);
        int default_geofence_radius_in_meters = 500;
        geoFenceStartRadius = default_geofence_radius_in_meters;
        lCircleOptions.radius(geoFenceStartRadius);
        lCircleOptions.strokeColor(Color.RED).fillColor(fill_color_circle_map);
        lastCircle =  googleMap.addCircle(lCircleOptions);
        currentMarker = googleMap.addMarker(new MarkerOptions().position(circleCenterLatLng).title("GeoFence 1"));
        getAddress();

    }
    /**
     *
     * @param ZoomIn
     */
    public void resizeCircle(boolean ZoomIn){

        if(circleCenterLatLng == null) return;

        if (googleMap == null)
            return;

        CircleOptions lCircleOptions = new CircleOptions();
        lCircleOptions.center(circleCenterLatLng);

        int geofenceRadiusIncrement = 10;
        if(ZoomIn) {
            geoFenceStartRadius += geofenceRadiusIncrement;
        }else{
            geoFenceStartRadius -= geofenceRadiusIncrement;
        }

        lCircleOptions.radius(geoFenceStartRadius);
        lCircleOptions.strokeColor(Color.RED).fillColor(fill_color_circle_map);

        final Circle tempLastCircle = googleMap.addCircle(lCircleOptions);
        if(lastCircle != null) lastCircle.remove();
        lastCircle = tempLastCircle;
        currentMarker = googleMap.addMarker(new MarkerOptions().position(circleCenterLatLng).title("GeoFence 1"));
        getAddress();
    }

    private void getAddress(){
        (new GetAddressTask(getActivity())).execute(circleCenterLatLng);

    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private class GetAddressTask extends
            AsyncTask<LatLng, Void, String> {
        Context mContext;
        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @params params One or more Location objects
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         */
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            LatLng loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(loc.latitude,
                        loc.longitude, 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.latitude) +
                        " , " +
                        Double.toString(loc.longitude) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }

        /**
         * A method that's called once doInBackground() completes. Turn
         * off the indeterminate activity indicator and set
         * the text of the UI element that shows the address. If the
         * lookup failed, display the error message.
         */
        @Override
        protected void onPostExecute(String address) {
            // add this as the title of the marker that is added to the map.
            if(currentMarker != null){
                currentMarker.setTitle(address);
            }
        }

    }
}
