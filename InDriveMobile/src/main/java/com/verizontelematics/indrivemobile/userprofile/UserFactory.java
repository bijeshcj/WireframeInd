package com.verizontelematics.indrivemobile.userprofile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.verizontelematics.indrivemobile.activity.UpSellPageActivity;
import com.verizontelematics.indrivemobile.constants.UIConstants;
import com.verizontelematics.indrivemobile.fragments.BaseSubUIFragment;
import com.verizontelematics.indrivemobile.fragments.UpSellPageFragment;
import com.verizontelematics.indrivemobile.userprofile.utils.XMLUtils;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;

import java.lang.reflect.Field;

/**
 * Created by Bijesh on 31-01-2015.
 */
public class UserFactory implements GetView,UserRoleConstants{

    private static final String TAG = UserFactory.class.getCanonicalName();
    private static UserType userType;
    private static UserFactory instance;
    private static Context context;
    private static final String USER_PREFERENCE = "UserPreference";
    private static final String USER_TYPE = "userType";
    private static final String USER_ROLE = "userRole";
    private static final String CONNECT_USER = "Connect";
//    private static final String COPILOT_USER = "CoPilot";
//    private static final String GUARDIAN_USER = "Guardian";
    private static final String CONNECT_GUADIAN = "ConnectGuardian";
    private static final String CONNECT_COPILOT = "ConnectCoPilot";
    private static final String CONNECT_GUARDIAN_COPILOT = "ConnectGuardianCoPilot";





     private UserFactory(){

     }

    public static UserFactory getInstance(Context appContext){
        if(instance == null){
            context = appContext;
            instance = new UserFactory();
            userType = getCurrentUserType();
            XMLUtils.parseXML(context,userType);
            Log.d(TAG,"User type is "+userType);
        }
        return instance;
    }

    public static void setInstanceAsNull(){
        instance = null;
    }

    public static void clearFactoryInstance(){
        instance = null;
    }

    public static UserType getCurrentUserType(){
        String userType = InDrivePreference.getInstance().getStringData(USER_TYPE,CONNECT_USER);//sharedPreferences.getString(USER_TYPE, getUserType(new String[]{"SF003-58P","SF002-55P"}));
        String userRole = InDrivePreference.getInstance().getStringData(USER_ROLE,ACTIVE_ROLE);//sharedPreferences.getString(USER_ROLE, INACTIVE_ROLE);
        return getUserTypeAndRole(userType,userRole);
    }

    public static String getCurrentUser(){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);

//        return sharedPreferences.getString(USER_TYPE, CONNECT_GUARDIAN_COPILOT);
        return InDrivePreference.getInstance().getStringData(USER_TYPE,CONNECT_USER);
    }

    public static String getCurrentUserRole(){
//        Log.d(TAG,"$$$ isContext null"+(context == null));
//        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(USER_ROLE, INACTIVE_ROLE);
        return InDrivePreference.getInstance().getStringData(USER_ROLE,ACTIVE_ROLE);
    }

    private static String getUserType(String[] productCodes){
        String guardian = "Guardian";
        String coPilot = "CoPilot";
        StringBuilder retString = new StringBuilder(CONNECT_USER);
        String[] guardians =  {"SF002-556","SF002-551","SF002-55P","SF002-TEST"};
        String[] coPilots = { "SF003-58P","SF003-581","SF003-586","SF003-TEST"};
        String[] connect = { "SF001-506","SF004-521","SF001-501D","SF001-501",
                "SF004-526","SF001-506D","SF004-TEST","SF001-TEST","SF000-600","SF001-587","SF001-577","SF-601","SF-555","SF001-800","SF-666","SF001-589","SF-579"};

        for(String productCode:productCodes){
            for(String guard:guardians){
                if(guard.equalsIgnoreCase(productCode)){
                    retString.append(guardian);
                    break;
                }
            }
        }

        for(String productCode:productCodes){
            for(String co:coPilots){
                if(co.equalsIgnoreCase(productCode)){
                    retString.append(coPilot);
                    break;
                }
            }
        }


        return retString.toString();
    }


    public static void setUserType(String[] productCodes){
        Log.d(TAG," setUserType");
        for(String pc:productCodes)
          Log.d(TAG," pcs: "+pc);

        String guardian = "Guardian";
        String coPilot = "CoPilot";
        StringBuilder retString = new StringBuilder(CONNECT_USER);
        String[] guardians =  {"SF002-556","SF002-551","SF002-55P","SF002-TEST"};
        String[] coPilots = { "SF003-58P","SF003-581","SF003-586","SF003-TEST"};
        String[] connect = { "SF001-506","SF004-521","SF001-501D","SF001-501",
                "SF004-526","SF001-506D","SF004-TEST","SF001-TEST","SF000-600","SF001-587","SF001-577","SF-601","SF-555","SF001-800","SF-666","SF001-589","SF-579"};

        outerLoop:
        for(String productCode:productCodes){
            for(String guard:guardians){
                if(guard.equalsIgnoreCase(productCode) && !retString.toString().contains("Guardian")){
                    retString.append(guardian);
                    break outerLoop;
                }
            }
        }

        outerLoop:
        for(String productCode:productCodes){
            for(String co:coPilots){
                if(co.equalsIgnoreCase(productCode) && !retString.toString().contains("CoPilot")){
                    retString.append(coPilot);
                    break outerLoop;
                }
            }
        }
        Log.d(TAG,"$$$ retString as user "+retString.toString());

        InDrivePreference.getInstance().setStringData(USER_TYPE,retString.toString());

//        return retString.toString();
    }



    public static void setUserRole(String role){
        InDrivePreference.getInstance().setStringData(USER_ROLE,role);
    }


    private static UserType getUserTypeAndRole(String userType,String userRole){
        if(userType.equalsIgnoreCase(CONNECT_USER)){
             if(userRole.equalsIgnoreCase(ACTIVE_ROLE)){
                 return  new Connect(UserRole.ACTIVE);
             }else{
                 return new Connect(UserRole.IN_ACTIVE);
             }
        }
        else if(userType.equalsIgnoreCase(CONNECT_GUADIAN)){
            if(userRole.equalsIgnoreCase(ACTIVE_ROLE)){
                return  new ConnectGuardian(UserRole.ACTIVE);
            }else{
                return new ConnectGuardian(UserRole.IN_ACTIVE);
            }
        }
        else if(userType.equalsIgnoreCase(CONNECT_COPILOT)){
            if(userRole.equalsIgnoreCase(ACTIVE_ROLE)){
                return  new ConnectCoPilot(UserRole.ACTIVE);
            }else{
                return new ConnectCoPilot(UserRole.IN_ACTIVE);
            }
        }
        else if(userType.equalsIgnoreCase(CONNECT_GUARDIAN_COPILOT)){
            if(userRole.equalsIgnoreCase(ACTIVE_ROLE)){
                return  new ConnectGuardianCoPilot(UserRole.ACTIVE);
            }else{
                return new ConnectGuardianCoPilot(UserRole.IN_ACTIVE);
            }
        }
        return null;
    }


//    creates the User based on login from the values saved on shared prefs

     public static UserType getUserType(){
         return userType;
     }


//    @Override
//    public Class getActivityView(Class defaultActivity,String subModuleType) {
//
//        try {
//            Class userClass = getUserClass(userType.getClass().getSimpleName());
//            Field field = userClass.getDeclaredField(subModuleType);
//            field.setAccessible(true);
//            Object valueAsObject = field.get(userType);
//            System.out.println("$$$ valueAsObject "+valueAsObject);
//            if(valueAsObject.toString().equalsIgnoreCase("UpsellPage")){
//                defaultActivity = UpSellPageActivity.class;
//            }
//        }catch (NoSuchFieldException e){
//            e.printStackTrace();
//        }catch (IllegalAccessException e){
//            e.printStackTrace();
//        }
//        return defaultActivity;
//    }



    public Intent getActivityViewIntent(Context context,Class defaultActivity,String subModuleType) {
        Intent intent = new Intent(context,defaultActivity);
        try {
            Class userClass = getUserClass(userType.getClass().getSimpleName());
            Field field = userClass.getDeclaredField(subModuleType);
            field.setAccessible(true);
            Object valueAsObject = field.get(userType);
            System.out.println("$$$ valueAsObject "+valueAsObject);
            if(valueAsObject.toString().equalsIgnoreCase("UpsellPage")){
                defaultActivity = UpSellPageActivity.class;
                intent = new Intent(context,defaultActivity);
                intent.putExtra(BUNDLE_UPSELL_PAGE_MODULE_KEY,subModuleType);
            } else{
                if(valueAsObject.toString().equalsIgnoreCase(UserRole.ACTIVE.toString())){
//                    deliberate empty block
                }else{
//                    Bundle bundle = new Bundle();
//                    bundle.putCharSequence(UIConstants.BUNDLE_KEY_USER_ROLE,UserRole.IN_ACTIVE.toString());
//                    defaultBaseSubUIFragment.setArguments(bundle);
                    intent.putExtra(UIConstants.BUNDLE_KEY_USER_ROLE,UserRole.IN_ACTIVE.toString());
                }
            }
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return intent;
    }

    @Override
    public Fragment getFragmentView(Fragment defaultFragment) {
        return null;
    }

    @Override
    public BaseSubUIFragment getBaseSubUIFragment(BaseSubUIFragment defaultBaseSubUIFragment,String subModuleType) {
        Log.d(TAG, "$$$ Current user type " + userType.getClass().getSimpleName());

        try {
            Class userClass = getUserClass(userType.getClass().getSimpleName());
            Field field = userClass.getDeclaredField(subModuleType);
            field.setAccessible(true);
            Object valueAsObject = field.get(userType);
            System.out.println("$$$ valueAsObject "+valueAsObject);
            if(valueAsObject.toString().equalsIgnoreCase("UpsellPage")){
                defaultBaseSubUIFragment = new UpSellPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_UPSELL_PAGE_MODULE_KEY,subModuleType);
                defaultBaseSubUIFragment.setArguments(bundle);
            }
            else{
                if(valueAsObject.toString().equalsIgnoreCase(UserRole.ACTIVE.toString())){
//                    deliberate empty block
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(UIConstants.BUNDLE_KEY_USER_ROLE,UserRole.IN_ACTIVE.toString());
                    if(defaultBaseSubUIFragment != null)
                      defaultBaseSubUIFragment.setArguments(bundle);
                }
            }
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return defaultBaseSubUIFragment;
    }


    public BaseSubUIFragment getBaseSubUIFragment(BaseSubUIFragment defaultBaseSubUIFragment,String[] subModuleTypes) {
        Log.d(TAG, "$$$ Current user type " + userType.getClass().getSimpleName());
        Bundle bundle = new Bundle();
//        boolean isActive = true;
        try {
            Class userClass = getUserClass(userType.getClass().getSimpleName());
            for(String sub:subModuleTypes){
                Field field = userClass.getDeclaredField(sub);
                field.setAccessible(true);
                Object valueAsObject = field.get(userType);
                System.out.println("$$$ valueAsObject "+valueAsObject);
                if(valueAsObject.toString().equalsIgnoreCase("UpsellPage")){
                    defaultBaseSubUIFragment = new UpSellPageFragment();
                }else{
                    if(valueAsObject.toString().equalsIgnoreCase(UserRole.ACTIVE.toString())){
//                       isActive = true;
                    }else{
                        System.out.println("$$$ inactive here setting in bundle");
                        bundle.putCharSequence(sub,UserRole.IN_ACTIVE.toString());
                        defaultBaseSubUIFragment.setArguments(bundle);
                    }
                }

            }
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return defaultBaseSubUIFragment;
    }



    private Class getUserClass(String userTypeClassName){
        if(userTypeClassName.equalsIgnoreCase(CONNECT_USER)){
            return Connect.class;
        }else if(userTypeClassName.equalsIgnoreCase(CONNECT_COPILOT)){
            return ConnectCoPilot.class;
        }else if(userTypeClassName.equalsIgnoreCase(CONNECT_GUADIAN)){
            return ConnectGuardian.class;
        }
        else
            return ConnectGuardianCoPilot.class;
    }


    public static UserRole initUserRole(Bundle bundle){
        if(bundle != null) {
            String userRole = bundle.getString(UIConstants.BUNDLE_KEY_USER_ROLE);
            if (userRole != null && userRole.equalsIgnoreCase(UserRole.IN_ACTIVE.toString())) {
                return UserRole.IN_ACTIVE;
            }
        }
        return UserRole.ACTIVE;
    }

    //    public BaseSubUIFragment getFragmentView(HomeActivity activity,BaseUIScreenFragment homeFragment,Fragment defaultFragment,
//                                             FragmentTransaction transaction,String title,String addToBackStackString,int titlePosition) {
//
//        BaseSubUIFragment fragment = null;
//
////        if (fragment != null) {
//////            ((HomeActivity)getActivity()).hideCustomActionBar(title);
////            activity.hideCustomActionBar(title);
////            fragment.setHomeFragment(homeFragment);
////            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
////            transaction.addToBackStack(addToBackStackString);
////            transaction.add(R.id.container_id_location, fragment, addToBackStackString);
////            transaction.commit();
////            mTitle = getActivity().getResources().getStringArray(R.array.location_list_options)[position-1];
////            homeFragment.pushFragmentStack(addToBackStackString);
////        }
//
//        return null;
//    }
}
