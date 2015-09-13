package com.verizontelematics.indrivemobile.utils;

/**
 * Created by Priyanga on 8/20/2014.
 */
public class AppConstants {


    public static final String NETWORK_ERROR = "NETWORK_ERROR";
    public static final int REQUEST_SUCCESS = 2000;

    public static final int TECHNICAL_ISSUE = 1002;
    public static final int USERNAME_ISSUE = 1412;
    public static final int ACCOUNT_ISSUE = 1421;
    public static final int INVALID_AUTH_CODE = 1414;
    public static final int PASSWORD_ISSUE = 1422;

    public static final String MOBILE_UNIQUE_DEVICE_ID_KEY = "MobileDeviceID";

    //DATE FORMATS
    public static final String LOG_DATE_FORMAT ="MM/dd/yy";

    //NUMERIC CONSTANTS
    public static final String MAX_MILEAGE_SIZE = "99999999";
    public static final String MIN_MILEAGE_SIZE ="1";
    public static final String MAX_FREQUENCY_SIZE = "48";
    public static final String MIN_FREQUENCY_SIZE ="1";
    public static final int MAX_SPEED_VALUE = 155;
    public static final int MIN_SPEED_VALUE =1;
    public static final int DEFAULT_SPEED_VALUE =70;
    public static final int DEFAULT_START_TIME =8;
    public static final int DEFAULT_END_TIME =17;

    //public static final String SENDER_ID = "486390043873"; self tested
    public static final String SENDER_ID = "786032522917";
    public static final String APP_ID = "3"; // 3->SF

    public enum USER_SELECTION {
        DATE("DATE"),
        MILEAGE("MILEAGE");

        USER_SELECTION(String selection) {
            this.selection = selection;
        }

        private String selection;

        public String getSelection() {
            return selection;
        }

    }

    public enum SWITCH_SELECTION {
        EMAIL("EMAIL"),
        TEXT("TEXT");

        SWITCH_SELECTION(String switchSelection) {
            this.switchSelection = switchSelection;
        }

        private String switchSelection;

        public String getSwitchSelection() {
            return switchSelection;
        }

    }


    public static final String DRIVING_DATA_TIME_PERIOD_KEY = "Driving_Data_Time_Period";
    public static final String DRIVING_DATA_CATEGORY = "Driving_Data_Category";
    public static final String APP_BACKGROUND_STATE = "AppBackground";

    public enum DrivingDataTimePeriod{
        THIS_WEEK,
        LAST_WEEK,
        THIS_MONTH,
        LAST_MONTH
    }

    public enum DrivingDataCategory{
        TOTAL_MILES,
        MAX_SPEED,
        AVERAGE_TRIP,
        CARBON_FOOTPRINT,
        CITY_MPG,
        HIGHWAY_MPG
    }

}
