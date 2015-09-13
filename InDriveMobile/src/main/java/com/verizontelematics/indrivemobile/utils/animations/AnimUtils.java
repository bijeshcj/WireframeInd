package com.verizontelematics.indrivemobile.utils.animations;

import com.verizontelematics.indrivemobile.animations.TutorialPoint;

/**
 * Created by bijesh on 10/1/2014.
 */
public class AnimUtils {

    private static final int X_BASE_FACTOR = 1080;
    private static final int Y_BASE_FACTOR = 1845;

    public static DeviceType getPhoneType(double density){
        DeviceType type = DeviceType.XXHDPI;
        if(density == 1.5){
            type = DeviceType.HDPI;
        }else if(density == 2.0){
            type = DeviceType.XHDPI;
        }else if(density == 3.0){
            type = DeviceType.XXHDPI;
        }
        return type;
    }

    public static TutorialPoint[] getPointsBasedOnDevice(DeviceType deviceType,TutorialPoint[] points,int width,int height){

        if(deviceType.getScale_factor() == 3)
            return points;

        for(int i=0;i<points.length;i++){
             TutorialPoint point = points[i];
//             System.out.println("actual point "+point);
             point = getDevicePoints(point,width,height);
//             System.out.println("refactored point "+point);
             points[i] = point;
        }

        return points;
    }
//     canvas.drawCircle(((370.02487f / 720) * getWidth()),((940.9751f / 1230) * getHeight()),20f,mPaint);
    private static TutorialPoint getDevicePoints(TutorialPoint point,int width,int height){
//        TutorialPoint retPoint = new TutorialPoint();
        float a_x = point.x;
        float a_y = point.y;
//        System.out.println("width "+width+" height "+height);
        float ret_x = ((a_x / X_BASE_FACTOR) * width);
        float ret_y = ((a_y / Y_BASE_FACTOR) * height);

        point.x = ret_x;
        point.y = ret_y;

        return point;
    }

}
