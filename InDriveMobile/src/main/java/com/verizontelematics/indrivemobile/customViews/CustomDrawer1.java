package com.verizontelematics.indrivemobile.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.verizontelematics.indrivemobile.models.GestureMessage;
import com.verizontelematics.indrivemobile.models.Point;
import com.verizontelematics.indrivemobile.utils.ui.GestureDetection;
import com.verizontelematics.indrivemobile.utils.ui.MapUtils;

import java.util.ArrayList;

/**
 * Created by bijesh on 9/9/2014.
 */
@SuppressWarnings("ALL")
public class CustomDrawer1 extends View {

    private static final String TAG = CustomDrawer1.class.getCanonicalName();

    private GestureDetection mGestureDetection;
    private float startX=0.0f;
    private float startY=0.0f;
    private GoogleMap mGoogleMap;
    private boolean isLongClicked = false;
    private boolean mScaleMode = false;
    private ScaleGestureDetector mScaleDetection;

    /**
     * recognize the point here.
     */
    public static interface TouchCallbacks{
        public void addPoint(int x, int y);
        public void clear();
        public void end(boolean isLongClicked,ArrayList<LatLng> locations);
        @Deprecated
        public void longPress(int x, int y);
        public void onPinchZoomIn();
        public void onPinchZoomOut();
        public void onCircleRendered(boolean isRendered);
    }

    public void setMap(GoogleMap googleMap){
        this.mGoogleMap = googleMap;
    }

    private Paint paint = new Paint();
    private Path path = new Path();
    private TouchCallbacks mRecognizer = null;
    public void setRecognizer(TouchCallbacks aRecognizer){
        mRecognizer = aRecognizer;
    }

    public void setScaleMode(boolean flag) {
        mScaleMode = flag;
    }

    public boolean getScaleMode() { return mScaleMode; }

    // add point.
    public void addPoint(float x, float y){
        path.moveTo(x,y);
    }

    private void init(){
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        mGestureDetection = new GestureDetection();
        mScaleDetection = new ScaleGestureDetector(this.getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            public float mCurrentSpan = 0;


            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                if (mRecognizer !=null) {
                    if (mCurrentSpan < scaleGestureDetector.getCurrentSpan())
                        mRecognizer.onPinchZoomOut();
                    else
                        mRecognizer.onPinchZoomIn();
                    mCurrentSpan = scaleGestureDetector.getCurrentSpan();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

            }
        });
    }
    public void reset(){
        path.reset();
        invalidate();

    }

    public CustomDrawer1(Context context) {
        super(context);
        init();
    }

    public CustomDrawer1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDrawer1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
        //canvas.drawOval(new RectF(150, 150, 320, 440), paint);;
    }


    private final Handler handler = new Handler();
    private Runnable mLongPressed = new Runnable() {
        public void run() {
            System.out.println("$$$ on Long pressed");
            isLongClicked = true;
//            Toast.makeText(getContext(), "You have pressed it long :)", Toast.LENGTH_SHORT).show();
            mRecognizer.longPress((int) CustomDrawer1.this.startX, (int) CustomDrawer1.this.startY);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mScaleMode) {
            return mScaleDetection.onTouchEvent(event);
        }
        LatLng coordinates = null;
        float eventX = event.getX();
        float eventY = event.getY();
//        System.out.println("$$$ CustomDrawer1 onTouchEvent X :"+eventX+" Y: "+eventY+" map zoom level "+mGoogleMap.getCameraPosition().zoom);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                System.out.println("$$$ action down ");
                if(mRecognizer != null){
                    mRecognizer.clear();
                }
                startX = eventX;
                startY = eventY;
                path.reset();
                path.moveTo(eventX, eventY);
                int long_press_delay_in_milliseconds = 1000;
                handler.postDelayed(mLongPressed, long_press_delay_in_milliseconds);
                mGestureDetection.addGesturePoints(new Point(eventX, eventY));
                coordinates = mGoogleMap.getProjection().fromScreenLocation(new android.graphics.Point((int) event.getX(), (int) event.getY()));
                Log.d(TAG, " coordinates " + coordinates);
                mGestureDetection.addGestureLocations(coordinates);
//                System.out.println("$$$ coordinates "+coordinates);
                return true;
            case MotionEvent.ACTION_MOVE:
//                System.out.println("$$$ action move ");
                coordinates = mGoogleMap.getProjection().fromScreenLocation(new android.graphics.Point((int) event.getX(), (int) event.getY()));
//                System.out.println("$$$ coordinates "+coordinates);
                mGestureDetection.addGestureLocations(coordinates);
                mGestureDetection.addGesturePoints(new Point(eventX, eventY));
                path.lineTo(eventX, eventY);
                if(mRecognizer != null){
                    mRecognizer.addPoint((int)eventX,(int)eventY);
                }
                if(minDistanceMovedFromStart(eventX,eventY)) {
                    handler.removeCallbacks(mLongPressed);
                }

                break;
            case MotionEvent.ACTION_UP:
//                System.out.println("$$$ action up ");
                coordinates = mGoogleMap.getProjection().fromScreenLocation(new android.graphics.Point((int) event.getX(), (int) event.getY()));
                mGestureDetection.addGestureLocations(coordinates);
                mGestureDetection.addGesturePoints(new Point(eventX,eventY));
                if(mRecognizer != null){
                    System.out.println("$$$ before calling end() isLong Clicked "+isLongClicked);
                    mRecognizer.end(isLongClicked,mGestureDetection.getGestureLocations());
                    isLongClicked = false;
                }
                handler.removeCallbacks(mLongPressed);

                mRecognizer.onCircleRendered(true);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }



    public ArrayList<Point> getAllGesturePoints(){
        return mGestureDetection.getGesturePoints();
    }

    public ArrayList<LatLng> getAllGestureLocations(){
        return mGestureDetection.getGestureLocations();
    }

    public void resetGestureLocations(){
        mGestureDetection.resetGestureLocations();
    }

    public boolean detectGesture(){
        return mGestureDetection.detectGestureForCircle();
    }

    public GestureMessage detectGesture1(){
        return mGestureDetection.detectGestureForCircle1(MapUtils.getMapZoomLevel(mGoogleMap));
    }

    private boolean minDistanceMovedFromStart( float x, float y){
        int minimumTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        int touchDistanceCovered = (int) Math.sqrt(Math.abs(((y-startY)*(y-startY)) + ((x-startX) * (x-startX))));
        return touchDistanceCovered > minimumTouchSlop;
    }

}
