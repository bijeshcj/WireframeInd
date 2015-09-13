package com.verizontelematics.indrivemobile.utils.ui;



import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bijesh on 11/3/2014.
 */
public class CurrencyFormat {

    private static final String TAG = CurrencyFormat.class.getCanonicalName();

    private static Locale locale = new Locale("en","US");

    public static String formatDoubleAsCurrency(String editable){

        String str = editable.replaceAll( "[^\\d]", "" );

        String formattedString = editable.trim();
        if(str.length() > 0) {
            boolean isEndWithZero = str.endsWith("0");
            long s1 = Long.parseLong(str);
            double doubleValue = convertToDouble(s1);
            NumberFormat nf2 = NumberFormat.getInstance(locale);
            ((DecimalFormat) nf2).applyPattern("$ ###,###.###");

            if(!canTakeInput(editable.trim(),2)) {

                if(editable.trim().length() >= 14) {
                    formattedString = editable.trim().substring(0, 14);
                }
            }else if (isEndWithZero) {
                formattedString = nf2.format(doubleValue) + appendZeros(str);
            }
            else {
                formattedString = nf2.format(doubleValue);
            }
            Log.d(TAG, "formatted string " + formattedString);
//            editable.replace(0, editable.length(), formattedString);
        }
        return formattedString;
    }

    private static boolean canTakeInput(String value,int counter){
        boolean retValue = true;
        int commaCount = 0;
        Pattern pat = Pattern.compile(",");
        Matcher mat = pat.matcher(value);
        while (mat.find()){
//            System.out.println(mat.group());
            commaCount++;
        }
        if(commaCount == counter){
            String[] args = value.split(",");
            if(args != null){

                if(args[0].length() == 3 && value.length() >= 14)//
                    retValue = false;
            }
        }
        return retValue;
    }

    private static String appendZeros(String value){
        if(value.endsWith("00")) {

            return ".00";
        }
        else if(value.endsWith("0")) {

            return "0";
        }
        return "";
    }

    private static double convertToDouble(long longValue){
        double d = longValue / 100.0;
        return d;
    }

    public static String removeCurrencyFormat(String value){
        value = value.replaceAll("[$,]","");
        System.out.println("$$$ after removeCurrencyFormat "+value);
        return value;
    }

    public static String applyCurrencyFormat(String doubleValue){
        System.out.println("inside applyCurrencyFormat ");
        String retVal;
        NumberFormat nf2 = NumberFormat.getInstance(locale);
        ((DecimalFormat) nf2).applyPattern("$ ###,###.###");
        boolean isEndWithZero = doubleValue.endsWith(".0");
        if (isEndWithZero) {
            System.out.println("inside if");
            retVal = nf2.format(Double.parseDouble(doubleValue)) + ".0";
        }else if(hasDotAndE(doubleValue)){
            System.out.println("inside else if");
            retVal = nf2.format(Double.parseDouble(doubleValue)) + ".0";
        }
        else {
            System.out.println("inside else");
            retVal = nf2.format(Double.parseDouble(doubleValue));
        }
        System.out.println("after applying the currency format "+retVal);
        return retVal;
    }

    private static boolean hasDotAndE(String value){
        boolean retValue = false;
        if(value.contains(".") && value.contains("E")){
            retValue = true;
        }

        return retValue;
    }

    public static String applyCurrencyFormat1(String doubleValue){
        System.out.println("inside applyCurrencyFormat ");
        String retVal;
        NumberFormat nf2 = NumberFormat.getInstance(locale);
        ((DecimalFormat) nf2).applyPattern("$ ###,###.###");
        boolean isEndWithZero = doubleValue.endsWith(".0");
        if (isEndWithZero) {
            System.out.println("inside if");
            retVal = nf2.format(Double.parseDouble(doubleValue)) + ".0";
        }
//        else if(hasDotAndE(doubleValue)){
//            System.out.println("inside else if");
//            retVal = nf2.format(Double.parseDouble(doubleValue)) + ".0";
//        }
        else {
            System.out.println("inside else");
            retVal = nf2.format(Double.parseDouble(doubleValue));
        }
        System.out.println("after applying the currency format "+retVal);
        return retVal;
    }

}
