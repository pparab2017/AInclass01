package com.amad.ainclass01;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pushparajparab on 8/28/17.
 */

public class Utils {

    public enum Api_url {
        LOGIN("http://ec2-13-59-39-123.us-east-2.compute.amazonaws.com/api/login"),
        USER_INFO("http://ec2-13-59-39-123.us-east-2.compute.amazonaws.com/api/myInfo"),
        SIGN_UP("http://ec2-13-59-39-123.us-east-2.compute.amazonaws.com/api/signup"),
        UPDATE_INFO("http://ec2-13-59-39-123.us-east-2.compute.amazonaws.com/api/update/myInfo");

        private final String text;
        private Api_url(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
