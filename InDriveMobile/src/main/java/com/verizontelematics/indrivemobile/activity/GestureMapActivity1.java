package com.verizontelematics.indrivemobile.activity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.verizontelematics.indrivemobile.R;

import java.util.ArrayList;

/**
 * Created by bijesh on 9/10/2014.
 */
@SuppressLint("Registered")
public class GestureMapActivity1 extends FragmentActivity implements View.OnTouchListener,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_map1);
        initializeMap();
    }

    private void initializeMap() {
        if (mGoogleMap == null) {
            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            RelativeLayout mParentView = (RelativeLayout) findViewById(R.id.view);
            mParentView.setOnTouchListener(this);
        }
        if(mGoogleMap != null) {
            mGoogleMap.setOnMapLongClickListener(this);
            mGoogleMap.setOnMarkerDragListener(this);
            mGoogleMap.setOnMapClickListener(this);
        }
        if (mGoogleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
        // adding marker with different color
        double latitute = 10.0090;
        double longitude = 77.4700;
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latitute, longitude)).title("Home")
                .draggable(true);
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        // moving to particular lan and lon
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitute, longitude)).zoom(12).build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        mGoogleMap.addMarker(marker);
        // mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    @Override
    public void onMapLongClick(LatLng arg0) {
        mGoogleMap.addMarker(new MarkerOptions().position(arg0).draggable(true)
                .visible(false));

    }

    LatLng mFirst, mTemp;
    boolean isCircle = false;
    private ArrayList<LatLng> mLocations = new ArrayList<LatLng>();

    @Override
    public void onMarkerDrag(Marker arg0) {
        if (mTemp == null) {
            mTemp = arg0.getPosition();
            mGoogleMap
                    .addPolyline(new PolylineOptions().add(mFirst).add(mTemp));
        } else {
            mGoogleMap.addPolyline(new PolylineOptions().add(mTemp).add(
                    arg0.getPosition()));
        }
        if (mFirst.equals(mTemp)) {
            isCircle = true;
        }
        mLocations.add(mTemp);
        mTemp = arg0.getPosition();

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        mGoogleMap.addPolyline(new PolylineOptions().add(mTemp).add(
                arg0.getPosition()));
        mLocations.add(arg0.getPosition());

    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        mFirst = arg0.getPosition();
        mLocations.add(arg0.getPosition());

    }

    Polyline line;

    private void drawPoly(ArrayList<LatLng> latLongArrayList) {
        PolylineOptions options = new PolylineOptions().width(5)
                .color(Color.BLUE).geodesic(true);
        for (int z = 0; z < latLongArrayList.size(); z++) {
            LatLng point = latLongArrayList.get(z);
            options.add(point);
        }
        line = mGoogleMap.addPolyline(options);
    }

    @Override
    public void onMapClick(LatLng arg0) {
        mGoogleMap.addMarker(new MarkerOptions().position(arg0).draggable(true)
                .visible(true));

    }

    Point p = new Point();
    Canvas canvas;
    PolylineOptions mPolyLine;
    ArrayList<LatLng> mLatLngList = new ArrayList<LatLng>();
    LatLng mFirstTouch,mTempTouch;
    LatLng latLongPointOne, latLongPoint,  latLongPointTwo;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        LatLng coordinates = mGoogleMap.getProjection().fromScreenLocation(new Point((int) event.getX(), (int) event.getY()));
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        mPolyLine = new PolylineOptions().width(3).color(Color.BLUE).geodesic(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downy = event.getY();
                float eventX = downX;
                float eventY = downy;
                p.x = (int) downX;
                p.y = (int) downy;
                latLongPoint = mGoogleMap.getProjection().fromScreenLocation(p);
                mGoogleMap
                        .addPolyline(new PolylineOptions().add(latLongPoint));
                mFirstTouch = latLongPoint;
                mLatLngList.add(latLongPoint);
                v.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float upx = event.getX();
                float upy = event.getY();
                //v.invalidate();
                downX = upx;
                downy = upy;
                p.x = (int) upx;
                p.y = (int) upy;
                if (mTempTouch == null) {
                    mTempTouch = mGoogleMap.getProjection().fromScreenLocation(p);
                    mGoogleMap
                            .addPolyline(new PolylineOptions().add(mFirstTouch).add(mTempTouch));
                } else {
                    mGoogleMap.addPolyline(new PolylineOptions().add(mTempTouch).add(
                            mGoogleMap.getProjection().fromScreenLocation(p)));
                }

                mLocations.add(mTemp);
                mTempTouch = mGoogleMap.getProjection().fromScreenLocation(p);
                mLatLngList.add(latLongPointOne);
                v.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                p.x = (int) upX;
                p.y = (int) upY;
                latLongPointTwo = mGoogleMap.getProjection().fromScreenLocation(p);
                mGoogleMap.addPolyline(new PolylineOptions().add(mTempTouch).add(
                        latLongPointTwo));
                mLatLngList.add(latLongPointTwo);
                mFirstTouch = null;
                mTempTouch = null;
                v.invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }

        return true;
    }


}
