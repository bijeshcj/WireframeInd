package com.verizontelematics.indrivemobile.utils.config;

import android.content.Context;
import android.util.Log;

import com.verizontelematics.indrivemobile.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by z689649 on 9/15/14.
 */
public class Config {

    // This is a list of parent tags that this parser would be looking for
    private static final String CONFIGURATION_TAG = "configuration";
    private static  Config instance = null;
    private Map<String,String> configProperties = new HashMap<String,String>();
    private Map<String,String> urlProperties = new HashMap<String,String>();
    private static final String TAG = Config.class.getCanonicalName();

    private Config(){
        // nothing.
    }

    public static synchronized Config getInstance(Context aContext){
        if(instance == null){
            if (aContext == null)
                return null;
            instance = new Config();
            instance.parseConfigXml(aContext);
        }
        return instance;
    }

    public String getUrlProperty(String aPropertyName){
        return urlProperties.get(aPropertyName);
    }

    public String getConfig(String aProperty){
        return configProperties.get(aProperty);
    }

    /**
     * Parse the config file here.
     * @param aContext
     */
    private void parseConfigXml(Context aContext){
        BufferedInputStream fileStream;
        Log.d(TAG,"parseConfig is Context null "+(aContext == null));
        if(aContext != null) {
            fileStream = new BufferedInputStream(aContext.getResources().openRawResource(R.raw.config));
            String startTag = null;
            String endTag = null;
            try {
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(fileStream, null);
                int eventType = parser.getEventType();
                boolean check = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        startTag = parser.getName();
                        check = true;
                    } else if (eventType == XmlPullParser.END_TAG) {
                        endTag = parser.getName();
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (startTag != null && check) {
                            String insideText = parser.getText();
                            setConfigProperties(startTag, insideText);
                            check = false;
                        }
                    }
                    eventType = parser.next();
                }
                // parse properties file also.
                parsePropertiesFile(aContext);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Dump entire file here.
     * @param aInputStream
     * @throws IOException
     */
    private void dumpFileRead(InputStream aInputStream) throws IOException {
        byte[] lBytes = new byte[1024*1024];
        int length = aInputStream.read(lBytes);
        String lString = new String(lBytes,0, length);
        Log.d("TagData",  lString);
    }

    /**
     * Update config Properties Map here.
     * @param aTagName
     * @param aTagValue
     */
    private void setConfigProperties(String aTagName, String aTagValue){
        configProperties.put(aTagName,aTagValue);
    }

    /**
     * return host based on ENV type.
     * @return
     */
    public String getHost(){
        // return host based on env.
        if(configProperties!=null
                && configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE) != null
                && configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE) != null) {
            if(configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE).equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_DEV)){
                return configProperties.get(ConfigKeys.CONFIG_HOST_US_DEV);
            }else if(configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE).equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_STAGING)){
                return configProperties.get(ConfigKeys.CONFIG_HOST_US_STAGING);
            }else if(configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE).equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_PRODUCTION)){
                return configProperties.get(ConfigKeys.CONFIG_HOST_PROD);
            }else if(configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE).equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_ITEST_1)) {
                return configProperties.get(ConfigKeys.CONFIG_HOST_ITEST_1);
            }else if(configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE).equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_PRE_PRODUCTION)) {
                return configProperties.get(ConfigKeys.CONFIG_HOST_PRE_PROD);
            }
            else {
                return null;
            }

        }
        return null;
    }

    /**
     * return host based on ENV type.
     * @return
     */
    public String getHost(String appEnvType){
        // return host based on env.
        if(configProperties!=null
                && configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE) != null
                && configProperties.get(ConfigKeys.CONFIG_APP_ENV_TYPE) != null) {
            if(appEnvType.equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_DEV)){
                return configProperties.get(ConfigKeys.CONFIG_HOST_US_DEV);
            }else if(appEnvType.equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_STAGING)){
                return configProperties.get(ConfigKeys.CONFIG_HOST_US_STAGING);
            }else if(appEnvType.equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_PRODUCTION)){
                return configProperties.get(ConfigKeys.CONFIG_HOST_PROD);
            }else if(appEnvType.equalsIgnoreCase(ConfigKeys.APP_ENV_TYPE_ITEST_1)) {
                return configProperties.get(ConfigKeys.CONFIG_HOST_ITEST_1);
            }else {
                return null;
            }

        }
        return null;
    }
    /**
     * Parse properties file here.
     * @param aContext
     */
    private void parsePropertiesFile(Context aContext)  {
        BufferedInputStream fileStream = null;

        int lResourceName = R.raw.application_us;
        if(configProperties.get(ConfigKeys.CONFIG_LOCALE).equalsIgnoreCase(ConfigKeys.CONFIG_LOCALE_EU)){
            // set file name to eu.properties.
            // for now we have only one properties file since we are supporting
            // only US region.
            // lResourceName =
        }

        fileStream = new BufferedInputStream(aContext.getResources().openRawResource(lResourceName));
        Properties prop = new Properties();
        try {
            prop.load(fileStream);
            int lLength = ConfigKeys.STATIC_PROPERTIES_ARRAY.length;
            for (int i = 0; i < lLength; i++) {
                String aPropertyName = ConfigKeys.STATIC_PROPERTIES_ARRAY[i];
                urlProperties.put(aPropertyName, prop.getProperty(aPropertyName));
            }
        }catch (Exception ex) {
            //omitted.
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
