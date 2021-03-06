package com.verizontelematics.indrivemobile.userprofile.utils;

import android.content.Context;
import android.util.Log;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.userprofile.UserType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bijesh on 2/3/2015.
 */
public class XMLUtils implements XMLHelper{

    private static final String TAG = XMLUtils.class.getCanonicalName();

    /**
     * Method used to initialize the configuration xml from the raw and parse it and persist on Singleton class
     * @param context
     */
    public static void parseXML(Context context,UserType userType){
        BufferedInputStream bufferedInputStream;
        boolean isScanningCurrentUserType = false;
//        Connect connect;
//        Guardian guardian;
//        CoPilot coPilot;
        if(context != null) {
            bufferedInputStream = getFileAsBufferedStream(context,R.raw.user_roles_config);
            try {
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(bufferedInputStream, null);
                String startTag = "";
                String tagType = userType.getClass().getSimpleName();
//                System.out.println("$$ userType "+tagType);
                int eventType = parser.getEventType();
//                Log.d(TAG,"$$$ eventType "+eventType);
                while (eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_TAG){
                        startTag  = parser.getName();
//                        System.out.println("$$$ start Tag "+startTag);
                        if(startTag.equalsIgnoreCase(tagType)){

                            isScanningCurrentUserType = true;
                        }
                    } else  if(eventType == XmlPullParser.TEXT){
                        if(isScanningCurrentUserType){
                            if(isSkipOtherUserTags(tagType,startTag)){
                                break;
                            }
                        }
                        String value = parser.getText();
//                        System.out.println("$$$ value "+value);
                        if(value != null && value.trim().length() > 0) {
                            populateUserType(userType, startTag, value);
                        }
                    }

                    eventType = parser.next();
                }
            }catch (XmlPullParserException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        Log.d(TAG,"App has now "+userType);
    }

    private static boolean isSkipOtherUserTags(String userType,String currentTag){
        boolean retFlag = false;
        List<String> users = new LinkedList<String>(Arrays.asList("Connect","ConnectCoPilot","ConnectGuardianCoPilot","ConnectGuardian"));//getUsers();
        String removeUser = "";
//        removing the current user
        for(String user:users){
            if(user.equalsIgnoreCase(userType)){
                removeUser = user;
            }
        }
        users.remove(removeUser);
//        determining whether to skip the parsing
        for(String user:users){
            if (user.equalsIgnoreCase(currentTag)){
                retFlag = true;
                break;
            }
        }

        return retFlag;
    }

    /**
     * initialize the User
     */
    private static void populateUserType(UserType userType,String tagName,String value){

         if(!tagName.equalsIgnoreCase("InDrive") && !tagName.equalsIgnoreCase("Connect") && !tagName.equalsIgnoreCase("CoPilot") && !tagName.equalsIgnoreCase("Guardian")) {
             Field[] fields = userType.getClass().getDeclaredFields();
             for (Field field : fields) {
                 field.setAccessible(true);

//                 System.out.println("$$$ field name " + field.getName()+" tagName "+tagName);
                 if(tagName.equalsIgnoreCase(field.getName())){
                     try {
//                         System.out.println("$$$ its login variable ");
                         field.set(userType, value);

                         break;
                     }catch (IllegalAccessException e){
                         e.printStackTrace();
                     }
                 }
             }
         }
    }



    private static BufferedInputStream getFileAsBufferedStream(Context context,int configurationXMLFile){
        return new BufferedInputStream(context.getResources().openRawResource(configurationXMLFile));
    }

}
