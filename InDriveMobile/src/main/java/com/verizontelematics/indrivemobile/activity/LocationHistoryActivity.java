package com.verizontelematics.indrivemobile.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.LocationController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.models.LocationDataModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.LocationHistoryDatum;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;
import com.verizontelematics.indrivemobile.utils.ui.MapUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class LocationHistoryActivity extends FragmentActivity implements UIInterface, Observer {
    private GoogleMap googleMap;
    private MapView locationHistoryMapView;
    private static final int GET_SELECTED_DAY = 10;
    private TextView selectedDayTV;
    private long mStartTime;
    private long mEndTime;
    private ArrayList<LocationHistoryDatum> mLocationHistoryDatumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        locationHistoryMapView = (MapView) this.findViewById(R.id.locationHistoryMapView);
        locationHistoryMapView.onCreate(savedInstanceState);
        Activity mContext = this;
        setupUI();

    }

    private void setupUI() {

        setupHeaderView();
        googleMap = locationHistoryMapView.getMap();
        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
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

                    //centerMapOnMyLocation();


                }
            });
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    MapUtils.animateClipTo(googleMap, getLocations());
                    showMarkers();
                }
            });
        }

        Button editDayBtn = (Button)this.findViewById(R.id.editDayBtn);
        editDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LocationHistoryActivity.this,LocationHistoryPickerActivity.class);
                startActivityForResult(intent, GET_SELECTED_DAY);

            }
        });
        selectedDayTV =(TextView)this.findViewById(R.id.selectedDayTV);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        mStartTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        mEndTime = calendar.getTimeInMillis();

        setupCallbacks();
    }

    private void setupCallbacks() {
        LocationController.instance().register(this);
        LocationController.instance().getLocationHistoryModel().addObserver(this);
    }

    private void showMarkers() {
        ArrayList<LatLng> locations = getLocations();
        ArrayList<String> date = getDateTime();
        ArrayList<Double> speeds = getSpeeds();
        ArrayList<String> directions = getDirections();
        Bitmap b = Bitmap.createBitmap(200, 220, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();
        for (int i=0;i<locations.size();i++) {
            Marker marker = MapUtils.drawVehicleMarker(googleMap, locations.get(i));
            MapUtils.setMarkerWithAddress(this, marker, locations.get(i), date.get(i), speeds.get(i), directions.get(i));
            // create custom marker icon.
            // code need  refactor.
            c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.car_locate_icon), 0, 10, p);
            p.setARGB(150, 0, 0, 0);
            p.setStrokeWidth(2);
            c.drawCircle(50, 50, 50, p);
            p.setARGB(220, 155, 255, 0);
            p.setTextSize(50);
            c.drawText(" " + (i + 1), 25, 65, p);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(b));
            // create marker icon done.
        }
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerLayout);
        headerRL.setBackgroundResource(R.drawable.location_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerBtn.setVisibility(View.INVISIBLE);
        headerTitleTV.setText(this.getResources().getString(R.string.location_history));
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationHistoryMapView.onResume();
        LocationController.instance().getLocationHistory(this, mStartTime, mEndTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationHistoryMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanup();
    }

    private void cleanup() {
        LocationController.instance().unregister(this);
        LocationController.instance().getLocationHistoryModel().deleteObserver(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_SELECTED_DAY) {
            if (resultCode == RESULT_OK) {

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MMMMM,EEEE dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(data.getLongExtra("SelectedDay",0));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                mStartTime = calendar.getTimeInMillis();
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                mEndTime = calendar.getTimeInMillis();
                selectedDayTV.setText(formatter.format(calendar.getTime()));
                LocationController.instance().getLocationHistory(this, mStartTime, mEndTime);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_history, menu);
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

    // Stubs
    private ArrayList<LatLng> getLocations() {
        ArrayList<LatLng> locations =  new ArrayList<LatLng>();
        /*locations.add(new LatLng(33.748556,-84.3963486));
        locations.add(new LatLng(33.7475769,-84.3957344));
        locations.add(new LatLng(33.7465187,-84.4046342));
        locations.add(new LatLng(33.7472539,-84.408018));
        locations.add(new LatLng(33.7417534,-84.4092687));*/
        for (LocationHistoryDatum location: mLocationHistoryDatumList) {
            locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        return locations;
    }

    @SuppressLint("SimpleDateFormat")
    private ArrayList<String> getDateTime() {
        ArrayList<String> locations =  new ArrayList<String>();
        /*locations.add("5/16/14  7.30 am");
        locations.add("8/18/14  8.00 am");
        locations.add("9/1/14  3.50 am");
        locations.add("10/18/14  8.00 am");
        locations.add("11/1/14  3.50 am");*/
        for (LocationHistoryDatum location: mLocationHistoryDatumList) {
            locations.add(DateDataFormat.convertMillisAsDateString(this, location.getDate(), new SimpleDateFormat("MM/DD/YY HH.MM"), false));
        }
        return locations;
    }

    private ArrayList<Double> getSpeeds(){
        ArrayList<Double> retList = new ArrayList<Double>();
        for(LocationHistoryDatum locationHistoryDatum: mLocationHistoryDatumList){
            retList.add(locationHistoryDatum.getSpeed());
        }
        return retList;
    }

    private ArrayList<String> getDirections(){
        ArrayList<String> retList = new ArrayList<String>();
        for(LocationHistoryDatum locationHistoryDatum: mLocationHistoryDatumList){
            retList.add(locationHistoryDatum.getDirection());
        }
        return retList;
    }

    @Override
    public void onProgress(Operation opr) {

    }

    @Override
    public void onError(Operation opr) {

    }

    @Override
    public void onSuccess(Operation opr) {

    }

    @Override
    public void update(Observable observable, Object o) {

        if (LocationDataModel.class.isInstance(observable)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLocationHistoryDatumList = (ArrayList<LocationHistoryDatum>)LocationController.instance().getLocationModel().getData();
                    populateUI();
                }
            });
        }
    }

    private void populateUI() {
        showMarkers();
    }
    // Stub ended
}
