package com.verizontelematics.indrivemobile.utils.ui;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by bijesh on 8/25/2014.
 */
public class ActionBarUtil {
    private static final String TAG = ActionBarUtil.class.getSimpleName();

    public static void setHasEmbeddedTabs(Object inActionBar, final boolean inHasEmbeddedTabs) {
        // get the ActionBar class
        Class<?> actionBarClass = inActionBar.getClass();

        // if it is a Jelly Bean implementation (ActionBarImplJB), get the super class (ActionBarImplICS)
        if ("android.support.v7.app.ActionBarImplJB".equals(actionBarClass.getName())) {
            actionBarClass = actionBarClass.getSuperclass();
        }

        try {
            // try to get the mActionBar field, because the current ActionBar is probably just a wrapper Class
            // if this fails, no worries, this will be an instance of the native ActionBar class or from the ActionBarImplBase class
            final Field actionBarField = actionBarClass.getDeclaredField("mActionBar");
            actionBarField.setAccessible(true);
            inActionBar = actionBarField.get(inActionBar);
            actionBarClass = inActionBar.getClass();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        }

        try {
            final Method method = actionBarClass.getDeclaredMethod("setHasEmbeddedTabs", new Class[]{Boolean.TYPE});
            method.setAccessible(true);
            method.invoke(inActionBar, inHasEmbeddedTabs);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        } catch (InvocationTargetException e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        }
    }
}
