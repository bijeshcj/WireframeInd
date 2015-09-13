package com.verizontelematics.indrivemobile.utils.ui;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by z688522 on 9/26/14.
 */
public class VehicleHealthErrorCodes {


    private static VehicleHealthErrorCode[] mErrorCodes = {
            new VehicleHealthErrorCode(0, 0),
            new VehicleHealthErrorCode(1, R.drawable.car_background),
            new VehicleHealthErrorCode(2,R.drawable.back_door_broken),
            new VehicleHealthErrorCode(3,R.drawable.back_tire_broken),
            new VehicleHealthErrorCode(4,R.drawable.broken_engine),
            new VehicleHealthErrorCode(5,R.drawable.broken_windshield),
            new VehicleHealthErrorCode(6,R.drawable.drivetrain_broken),
            new VehicleHealthErrorCode(7,R.drawable.front_door_broken),
            new VehicleHealthErrorCode(8,R.drawable.front_tire_broken),
            new VehicleHealthErrorCode(9,R.drawable.trunk_broken)
    };


    public static VehicleHealthErrorCode get(int errorCode) {
        for (VehicleHealthErrorCode mErrorCode : mErrorCodes) {
            if (mErrorCode.getErrorCode() == errorCode) {
                return mErrorCode;
            }
        }
        return null;
    }

    public static class VehicleHealthErrorCode {
        private final int mErrorCode;
        private final int mImageResourceId;

        public VehicleHealthErrorCode(int errorCode, int imgResId) {
            mErrorCode = errorCode;
            mImageResourceId = imgResId;
        }

        public int getImageResource() {
            return mImageResourceId;
        }

        public int getErrorCode() {
            return mErrorCode;
        }
    }
}
