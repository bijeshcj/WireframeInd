package com.verizontelematics.indrivemobile.utils.ui;

import java.util.HashMap;

/**
 * Created by z688522 on 1/9/15.
 */
public enum VehicleType {
      SEDAN
    , COMPACT
    , CONVERTABLE
    , MB_SEDAN
    , MB_SUV
    , MINIVAN
    , PICKUP
    , SPORTS_CAR
    , SUV
    , WAGON;


    public static int findByBodyType(String bodyType) {
        HashMap<String, VehicleType> hashMap = new HashMap<String, VehicleType>();
        hashMap.put("Sedan", SEDAN);
        hashMap.put("Coupe/Hatchback", COMPACT);
        hashMap.put("Motor Home", MINIVAN);
        hashMap.put("Comm Pickup/Van", MINIVAN);
        hashMap.put("Pickup", PICKUP);
        hashMap.put("Coupe", COMPACT);
        hashMap.put("Convertible", CONVERTABLE);
        hashMap.put("Wagon", WAGON);
        hashMap.put("SUV", SUV);
        hashMap.put("Hatchback", COMPACT);
        hashMap.put("Mini-Van", MINIVAN);
        hashMap.put("Default", SEDAN);

        VehicleType vt = hashMap.get(bodyType);
        if (vt == null)
            return hashMap.get("Default").ordinal();
        return vt.ordinal();
    }
}
