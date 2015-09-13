package com.verizontelematics.indrivemobile.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by z689649 on 10/2/14.
 */
public class LayeredImageView extends ImageView {
    /**
     * List of files can be sent in an array of bitmap resource Ids
     */
    private int[] resourceIds           = null;
    /**
     * List of files resource paths.
     */
    private String[] resourcePath       = null;
    /**
     * This is the merged bitmap that effectively contains all the bitmaps.
     * NOTE this order of elements in the array defines the order of their layout/rendering in the
     * final bitmap.
     */
    private Bitmap mergedBitmap         = null;
    private ArrayList<Integer> mResourceArray;

    /**
     * All bitmap must be of the same size, otherwise the algorithm would fail.
     * NOTE this order of elements in the array defines the order of their layout/rendering in the
     * final bitmap.
     * @param aResourceArray
     */
    public void setBitmapResourceArray(int[] aResourceArray){
        resourceIds = aResourceArray;
        generateMergedBitmap();
    }

    public void setBitmapResource(int position, int aResource) {
        resourceIds[position] = aResource;
        generateMergedBitmap();
    }
    /**
     * For cases where the bitmap are dynamically downloaded from the server.
     * @param aResourcePathArray
     */
    public void setBitmapResourcePathArray(String[] aResourcePathArray){
        resourcePath = aResourcePathArray;
        generateMergedBitmap();
    }

    public LayeredImageView(Context context) {
        super(context);
    }

    public LayeredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayeredImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void generateMergedBitmap(){

        // there should be at least one bitmap in the list.
        int lIndex = 0;

        Bitmap bitmap = null;
        do {
            bitmap = getBitmapAtIndex(lIndex++);
        }while (bitmap == null && lIndex < resourceIds.length);

        if (bitmap == null)
            return;
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap.recycle();
        Canvas lCanvas = new Canvas(mutableBitmap);
        Paint lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap offsetBitmap = null;
        do{
            offsetBitmap=getBitmapAtIndex(lIndex++);
            if(offsetBitmap != null){
                lCanvas.drawBitmap(offsetBitmap,0,0,lPaint);
            }
        }while(offsetBitmap != null && lIndex < resourceIds.length);

        final Bitmap finalBitmap = mutableBitmap;
        lPaint.reset();
        if(bitmap != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    setImageBitmap(finalBitmap);
                    invalidate();
                }
            });
        }
    }

    private Bitmap getBitmapAtIndex(int aIndex){

        if(resourceIds != null){
            if(aIndex >=0 &&  aIndex < resourceIds.length){
                return BitmapFactory.decodeResource(getContext().getResources(),resourceIds[aIndex]);
            }
        }else if(resourcePath != null){
            if(aIndex >=0 &&  aIndex < resourcePath.length){
                return BitmapFactory.decodeFile(resourcePath[aIndex]);
            }
        }
        return null;

    }
}
