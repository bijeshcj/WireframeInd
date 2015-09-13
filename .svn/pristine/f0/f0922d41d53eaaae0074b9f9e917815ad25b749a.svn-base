package com.verizontelematics.indrivemobile.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.customViews.CustomDrawer1;
import com.verizontelematics.indrivemobile.models.GestureMessage;

/**
 * Created by bijesh on 9/9/2014.
 */
@SuppressLint("Registered")
public class GestureMapActivity extends FragmentActivity {

    private CustomDrawer1 mDrawer;
    Button btnRadius;
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_map);
        mGoogleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.gesture_map)).getMap();
        mDrawer = (CustomDrawer1)findViewById(R.id.customDrawer);
        mDrawer.setMap(mGoogleMap);
        Button lDrawButton = (Button) findViewById(R.id.btnDraw);
        btnRadius = (Button)findViewById(R.id.btnRadius);
        lDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.getVisibility() == View.VISIBLE) {

                    mDrawer.setVisibility(View.GONE);
                    mDrawer.resetGestureLocations();
                } else {

                    mDrawer.reset();
                    mDrawer.setVisibility(View.VISIBLE);
                }
            }
        });

        btnRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawer.getAllGestureLocations() == null || mDrawer.getAllGestureLocations().size() == 0)
                    return;
//                boolean isCircle = mDrawer.detectGesture1();
                GestureMessage message = mDrawer.detectGesture1();
                boolean isCircle = message.isFlag();
                String gesMessage = "Not a circle";
                if(isCircle) {
                    gesMessage = "Is a Circle";
//                    drawGeoFenceOnMap(message.getCenterLatLng(),message.getRadius());
                }else{
                    System.out.println("not a circle ");
                    if(message.getMessage().length() > 0)
                        gesMessage = message.getMessage();
                }
                Toast.makeText(GestureMapActivity.this,gesMessage,Toast.LENGTH_SHORT).show();
                mDrawer.resetGestureLocations();
            }
        });

        double latitude = 12.975202119565362;
        double longitude = 80.1917839050293;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();



        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));



    }

     public void drawGeoFenceOnMap(LatLng center, float radius){
//Instantiates a new CircleOptions object +  center/radius
          int fill_color_circle_map                   = (128 << 24 ) | (0 << 16 ) | (0x00 << 8 ) | 0xFF;
         CircleOptions circleOptions = new CircleOptions();
         circleOptions.center(center);
         circleOptions.radius(radius);
         circleOptions.strokeColor(Color.RED).fillColor(fill_color_circle_map);
         Circle circle = mGoogleMap.addCircle(circleOptions);
     }




}
