package com.verizontelematics.indrivemobile.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;


/**
 * Created by z689649 on 8/22/14.
 */
public class CustomDrawer extends View {

    private float startX=0.0f;
    private float startY=0.0f;

    public enum DrawMode{
        MODE_DRAW,
        MODE_DROP
    }

    private DrawMode mode = DrawMode.MODE_DRAW;
    /**
     * recognize the point here.
     */
    public static interface TouchCallbacks{
        public void addPoint(int x, int y);
        public void clear();
        public void end();
        public void longPress(int x, int y);
    }

    private Paint paint = new Paint();
    private Path path = new Path();
    private TouchCallbacks mRecognizer = null;
    public void setRecognizer(TouchCallbacks aRecognizer){
        mRecognizer = aRecognizer;
    }

    // add point.
    public void addPoint(float x, float y){
        path.moveTo(x,y);
    }

    public void setMode(DrawMode aMode){
        mode = aMode;
    }

    private void init(){
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

    }
    public void reset(){
        path.reset();
        invalidate();
    }

    public CustomDrawer(Context context) {
        super(context);
        init();
    }

    public CustomDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
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
            Toast.makeText(getContext(), "You have pressed it long :)", Toast.LENGTH_SHORT).show();
            mRecognizer.longPress((int) CustomDrawer.this.startX, (int) CustomDrawer.this.startY);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = eventX;
                startY = eventY;

                    if(mRecognizer != null){
                        mRecognizer.clear();
                    }
                    path.reset();
                    path.moveTo(eventX, eventY);
                int long_press_delay_in_milliseconds = 1000;
                handler.postDelayed(mLongPressed, long_press_delay_in_milliseconds);
                return true;
            case MotionEvent.ACTION_MOVE:
                if(mode == DrawMode.MODE_DRAW) {
                    path.lineTo(eventX, eventY);
                    if (mRecognizer != null) {
                        mRecognizer.addPoint((int) eventX, (int) eventY);
                    }
                }
                if(minDistanceMovedFromStart(eventX,eventY)) {
                    handler.removeCallbacks(mLongPressed);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mRecognizer != null){
                    mRecognizer.end();
                }
                handler.removeCallbacks(mLongPressed);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private boolean minDistanceMovedFromStart( float x, float y){
        int minimumTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        int touchDistanceCovered = (int) Math.sqrt(Math.abs(((y-startY)*(y-startY)) + ((x-startX) * (x-startX))));
        return touchDistanceCovered > minimumTouchSlop;
    }

}
