/*
 * 
 * The Verizon Telematics Software is provided by Verizon Telematics on an �AS IS� basis. 
 *  Verizon Telematics MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION 
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE, REGARDING THE Verizon Telematics SOFTWARE OR ITS USE AND 
 * OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. 
 * 
 * Copyright (C) 2014 Verizon Telematics Inc. All Rights Reserved. 
*/

package com.verizontelematics.indrivemobile.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Util class for getting Network details.
 * 
 */
public class NetworkUtil {

	/**
	 * Checks if network is available
	 * 
	 * @param context
	 * @return true if available, false otherwise
	 */
	public static boolean isNetworkAvailable(Context context) {
		Context mContext = context;
		try {
			ConnectivityManager connectivity = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
