package com.verizontelematics.indrivemobile.animations;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by bijesh on 9/24/2014.
 */
@SuppressLint("Registered")
public class TestAnimationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_main);
        findPhoneDensity();
    }
    private void findPhoneDensity(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;

    }
    public void onSkipDemo(View view){
        finish();

    }
}
