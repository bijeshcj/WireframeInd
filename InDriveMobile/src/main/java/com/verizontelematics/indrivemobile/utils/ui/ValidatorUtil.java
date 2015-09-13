package com.verizontelematics.indrivemobile.utils.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bijesh on 6/29/2015.
 */
public class ValidatorUtil {


    public static boolean hasSpecialCharsInPassword(String password){
        boolean retFlag = false;
        Pattern regex = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = regex.matcher(password);
        if (matcher.find()){
            return true;
        }
        return retFlag;
    }

    public static PasswordValidator validatePassword(String password){
        PasswordValidator validator = new PasswordValidator();
        if(password.length() < 8){
            validator.setIsvalid(false);
            validator.setMessage("Password must be greater than 8 characters");
            return validator;
        }

        if(!atleastOneUpperCase(password)){
            validator.setIsvalid(false);
            validator.setMessage("Password must have atleast one uppercase");
            return validator;
        }

        if(!atleastOneLowerCase(password)){
            validator.setIsvalid(false);
            validator.setMessage("Password must have atleast one Lowercase");
            return validator;
        }

        if(!atleastOnenumber(password)){
            validator.setIsvalid(false);
            validator.setMessage("Password must have atleast one number");
            return validator;
        }


        return validator;
    }

    private static boolean atleastOnenumber(String password){
        boolean retFlag = false;
        String regexPatter = "[0-9]+";
        Pattern pattern = Pattern.compile(regexPatter);
        Matcher matcher = pattern.matcher(password);
        while (matcher.find()){
            return true;
        }
        return retFlag;
    }


    private static boolean atleastOneLowerCase(String password){
        boolean retFlag = false;
        String regexPatter = "[a-z]+";
        Pattern pattern = Pattern.compile(regexPatter);
        Matcher matcher = pattern.matcher(password);
        while (matcher.find()){
            return true;
        }
        return retFlag;
    }


    private static boolean atleastOneUpperCase(String password){
        boolean retFlag = false;
        String regexPatter = "[A-Z]+";
        Pattern pattern = Pattern.compile(regexPatter);
        Matcher matcher = pattern.matcher(password);
        while (matcher.find()){
            return true;
        }
        return retFlag;
    }

    static class PasswordValidator {

        boolean isvalid;
        String message;

        public boolean isIsvalid() {
            return isvalid;
        }

        public void setIsvalid(boolean isvalid) {
            this.isvalid = isvalid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }



}
