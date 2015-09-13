package com.verizontelematics.indrivemobile.utils.ui;

import android.util.Log;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.data.VehiclePartData;

/**
 * Created by z688522 on 11/21/14.
 */
public class VehiclePartView {

    private static final String TAG = VehiclePartView.class.getCanonicalName();

    String mPartName;
    int mWaringImageResource;
    int mErrorImageResource;
    private int mPartFadeInAnimation;
    private int mPartFadeOutAnimation;

    static int mCurrentCarTypeIndex = VehicleType.COMPACT.ordinal();

    private VehiclePartView(String partName, int warningImage, int errorImage, int fadeIn, int fadeOut) {

        mPartName = partName;
        mWaringImageResource = warningImage;
        mErrorImageResource =  errorImage;
        mPartFadeInAnimation = fadeIn;
        mPartFadeOutAnimation = fadeOut;
    }

    final static int[] mCarBackground = {
        R.drawable.sedan
        ,R.drawable.compact
        ,R.drawable.convertible
        ,R.drawable.mb_sedan
        ,R.drawable.mb_suv
        ,R.drawable.minivan
        ,R.drawable.pickup

        ,R.drawable.sportscar
        ,R.drawable.suv
        ,R.drawable.wagon
    };

    final static VehiclePartView mVehiclePartViews[] =  {
            // dummy data
            new VehiclePartView("car", R.drawable.compact_yellow, R.drawable.compact_red, R.anim.fadein, R.anim.fadeout)
            ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
            , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
            , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
            , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
            , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
            , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
    };

    final static VehiclePartView mCarVehiclePartsViews[][] = {
            // sedan
             {
            new VehiclePartView("car", R.drawable.sedan_yellow, R.drawable.sedan_red, R.anim.fadein, R.anim.fadeout)
            ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
            , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
            , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
            , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
            , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
            , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
    }
            // Compact car
            ,{
                new VehiclePartView("car", R.drawable.compact_yellow, R.drawable.compact_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)

            }
            // Convertible
            , {
                new VehiclePartView("car", R.drawable.convertible_yellow, R.drawable.convertible_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }
            // mb_sedan
            , {
                new VehiclePartView("car", R.drawable.mb_sedan_yellow, R.drawable.mb_sedan_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }
            // mb_suv
            , {
                new VehiclePartView("car", R.drawable.mb_suv_yellow, R.drawable.mb_suv_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }
            // minivan
            , {
                new VehiclePartView("car", R.drawable.minivan_yellow, R.drawable.minivan_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }
            // pickup
            , {
                new VehiclePartView("car", R.drawable.pickup_yellow, R.drawable.pickup_yellow, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }

            // sports car
            , {
                new VehiclePartView("car", R.drawable.sportscar_yellow, R.drawable.sportscar_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }
            // suv
            , {
                new VehiclePartView("car", R.drawable.suv_yellow, R.drawable.suv_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }
            // wagon
            , {
                new VehiclePartView("car", R.drawable.wagon_yellow, R.drawable.wagon_red, R.anim.fadein, R.anim.fadeout)
                ,  new VehiclePartView("Alternator", R.drawable.trunk_warning, R.drawable.trunk_broken, 0, 0)
                , new VehiclePartView("Battery", R.drawable.back_door_warning, R.drawable.back_door_broken, 0, 0)
                , new VehiclePartView("Mechanical", R.drawable.back_tire_warning, R.drawable.back_tire_broken, 0, 0)
                , new VehiclePartView("Emission", R.drawable.warning_engine, R.drawable.broken_engine, 0, 0)
                , new VehiclePartView("Radiator", R.drawable.front_door_warning, R.drawable.front_door_broken, 0, 0)
                , new VehiclePartView("Wiring", R.drawable.drivetrain_warning, R.drawable.drivetrain_broken, 0, 0)
            }
    };

    static public VehiclePartView get(VehiclePartData partData) {
        Log.d(TAG, "vehicle status get1 "+mCurrentCarTypeIndex);
        VehiclePartView aVehiclePartViews[] = mCarVehiclePartsViews[mCurrentCarTypeIndex];
        for (VehiclePartView mVehiclePartView : aVehiclePartViews) {

            if (mVehiclePartView.mPartName.equals(partData.get(VehiclePartData.PART_TITLE))) {
                return mVehiclePartView;
            }
        }
        return null;
    }

    static public VehiclePartView get(VehicleType vehicleType, VehiclePartData partData) {
        Log.d(TAG,"vehicle status get2 "+vehicleType.ordinal());
        VehiclePartView aVehiclePartViews[] = mCarVehiclePartsViews[vehicleType.ordinal()];
        for (VehiclePartView aVehiclePartView : aVehiclePartViews) {
            if (aVehiclePartView.mPartName.equals(partData.get(VehiclePartData.PART_TITLE))) {
                return aVehiclePartView;
            }
        }
        return null;
    }

    static public void setVehicleType(VehicleType type) {
        mCurrentCarTypeIndex = type.ordinal();
    }

    static public void setVehicleType(int typeIndex) {
        mCurrentCarTypeIndex = typeIndex;
    }

    static public int getCarBackground(VehicleType cartType) {
        return mCarBackground[cartType.ordinal()];
    }

    static public int getCarBackground() {
        return mCarBackground[mCurrentCarTypeIndex];
    }

    public int getErrorImageResource() {
        return mErrorImageResource;
    }

    public void setErrorImageResource(int mErrorImageResource) {
        this.mErrorImageResource = mErrorImageResource;
    }

    public String getPartName() {
        return mPartName;
    }

    public void setPartName(String mPartName) {
        this.mPartName = mPartName;
    }

    public int getWaringImageResource() {
        return mWaringImageResource;
    }

    public void setWaringImageResource(int mWaringImageResource) {
        this.mWaringImageResource = mWaringImageResource;
    }

    public static int getStatusImage(VehiclePartData partData) {

        if (partData.get(VehiclePartData.STATUS).equals(VehiclePartData.STATUS_OK))
            return -1;
        VehiclePartView vehiclePartView = VehiclePartView.get(partData);
        if (vehiclePartView == null)
            return -1;

        if (partData.get(VehiclePartData.STATUS).equals(VehiclePartData.STATUS_ERROR)) {
            return vehiclePartView.mErrorImageResource;
        }
        if (partData.get(VehiclePartData.STATUS).equals(VehiclePartData.STATUS_WARNING)) {
            return vehiclePartView.mWaringImageResource;
        }
        return -1;
    }

    public int getPartFadeInAnimation() {
        return mPartFadeInAnimation;
    }

    public int getPartFadeOutAnimation() {
        return mPartFadeOutAnimation;
    }
}
