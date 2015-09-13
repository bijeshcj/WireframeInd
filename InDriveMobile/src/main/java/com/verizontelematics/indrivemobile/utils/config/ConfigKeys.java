package com.verizontelematics.indrivemobile.utils.config;

/**
 * Created by z689649 on 9/15/14.
 */
public interface ConfigKeys {

    public static final String URL_1 = "some_url_keys_from_properties";
    public static final String URL_2 = "some_url_keys_from_properties_2";

    public static final String CONFIG_APP_VERSION = "app_version";
    public static final String CONFIG_APP_LAST_UPDATE = "app_last_update";
    public static final String CONFIG_LOCALE = "app_env";
    public static final String CONFIG_APP_ENV_TYPE = "app_env_type";
    public static final String CONFIG_HOST_ITEST_1 = "host_itest1";
    public static final String CONFIG_HOST_ITEST_2 = "host_itest2";
    public static final String CONFIG_HOST_ITEST_3 = "host_itest3";
    public static final String CONFIG_HOST_PROD = "host_prod";
    String CONFIG_HOST_PRE_PROD = "host_pre_prod";
    public static final String CONFIG_HOST_US_STAGING = "host_us_stage";
    public static final String CONFIG_HOST_US_DEV = "host_us_dev";
    public static final String CONFIG_APP_FLAVOR = "app_flavor";


    public static final String CONFIG_LOCALE_US = "us";
    public static final String CONFIG_LOCALE_EU = "eu";

    public static final String APP_ENV_TYPE_DEV = "dev";
    public static final String APP_ENV_TYPE_STAGING = "stage";
    public static final String APP_ENV_TYPE_PRODUCTION = "production";
    public static final String APP_ENV_TYPE_ITEST_1 = "itest1";
    public static final String APP_ENV_TYPE_PRE_PRODUCTION = "preproduction";

    // The following signifies property names against which we will match the URLS.
    public static final String PROP_FETCH_MAINTENANCE_LOGS_REMINDERS = "fetch_maintenance_logs_reminders";
    public static final String PROP_ADD_MAINTENANCE_LOGS = "add_maintenance_logs";
    public static final String PROP_DELETE_MAINTENANCE_LOGS = "delete_maintenance_logs";
    public static final String PROP_UPDATE_MAINTENANCE_LOGS = "update_maintenance_logs";

    public static final String PROP_ADD_MAINTENANCE_REMINDERS = "add_maintenance_reminders";
    public static final String PROP_DELETE_MAINTENANCE_REMINDERS = "delete_maintenance_reminders";
    public static final String PROP_UPDATE_MAINTENANCE_REMINDERS = "update_maintenance_reminders";

    public String PROP_FETCH_RECALLS = "get_recalls";
    public String PROP_SET_RECALL_COMPLETE = "set_recall_complete";
    public String PROP_FETCH_VEHICLE_HEALTH = "fetch_vehicle_health";

    //    Location Module
    public static final String PROP_GET_LOCATION_HISTORY = "get_location_history";
    public static final String PROP_GET_LOCATION = "get_location";

    // Driving Data
    public static final String PROP_FETCH_DRIVING_DATA = "get_driving_data";

    public static final String PROP_FETCH_ALERTS_DATA = "get_alerts";
    public static final String PROP_FETCH_ALERTS_HISTORY = "get_alerts_history";
    public static final String PROP_UPDATE_SPEED_ALERTS = "update_speed_alerts";
    public static final String PROP_UPDATE_DIAGNOSTIC_ALERTS = "update_diagnostic_alerts";
    public static final String PROP_CREATE_LOCATION_ALERT = "create_location_alert";
    public static final String PROP_UPDATE_LOCATION_ALERT = "update_location_alert";
    public static final String PROP_DELETE_LOCATION_ALERT = "delete_location_alert";

    public static final String PROP_CREATE_VALET_ALERT = "create_valet_alert";
    public static final String PROP_UPDATE_VALET_ALERT = "update_valet_alert";


    //    Authenticate
    public static final String PROP_AUTHENTICATE = "authenticate";
    public static final String PROP_VALIDATE_REGISTRATION = "validate_registration";
    public static final String PROP_GET_CERT_ID = "get_cert_id";
    public static final String PROP_GET_USER_VEHICLES = "get_user_vehicles";
    public static final String PROP_SEND_AUTHORIZATION_TOKEN = "send_authorization_token";
    public static final String PROP_GET_USER_ACCOUNT_DEVICE_INFO = "get_user_account_device_info";
    public static final String PROP_FORGOT_USER_NAME = "forgot_user_name";
    public static final String PROP_FORGOT_USER_PASSWORD = "forgot_user_password";
    public static final String PROP_UPDATE_PASSWORD = "update_password";
    public static final String PROP_FIND_USER = "find_user";
    public static final String PROP_GET_USER_PREFERENCE = "get_user_preference";
    public static final String PROP_UPDATE_USER_PREFERENCE = "update_user_preference";


//    GCM
    public static final String PROP_REGISTER_GCM = "register_gcm";


    public static final String[] STATIC_PROPERTIES_ARRAY = new String[]{PROP_FETCH_MAINTENANCE_LOGS_REMINDERS
            , PROP_ADD_MAINTENANCE_LOGS
            , PROP_DELETE_MAINTENANCE_LOGS
            , PROP_UPDATE_MAINTENANCE_LOGS
            , PROP_ADD_MAINTENANCE_REMINDERS
            , PROP_DELETE_MAINTENANCE_REMINDERS
            , PROP_UPDATE_MAINTENANCE_REMINDERS
            , PROP_FETCH_RECALLS
            , PROP_SET_RECALL_COMPLETE
            , PROP_FETCH_VEHICLE_HEALTH
            , PROP_FETCH_DRIVING_DATA
            , PROP_FETCH_ALERTS_DATA
            , PROP_UPDATE_SPEED_ALERTS
            , PROP_UPDATE_DIAGNOSTIC_ALERTS
            , PROP_CREATE_LOCATION_ALERT
            , PROP_DELETE_LOCATION_ALERT
            , PROP_UPDATE_LOCATION_ALERT
            , PROP_FETCH_ALERTS_HISTORY
            , PROP_GET_LOCATION_HISTORY
            , PROP_CREATE_VALET_ALERT
            , PROP_UPDATE_VALET_ALERT
            , PROP_GET_LOCATION
            , PROP_AUTHENTICATE,
            PROP_VALIDATE_REGISTRATION,
            PROP_GET_CERT_ID,
            PROP_GET_USER_VEHICLES,
            PROP_SEND_AUTHORIZATION_TOKEN,
            PROP_GET_USER_ACCOUNT_DEVICE_INFO,
            PROP_FORGOT_USER_NAME,
            PROP_FORGOT_USER_PASSWORD,
            PROP_UPDATE_PASSWORD,
            PROP_FIND_USER,
            PROP_GET_USER_PREFERENCE,
            PROP_UPDATE_USER_PREFERENCE,
            PROP_REGISTER_GCM


    };
}
