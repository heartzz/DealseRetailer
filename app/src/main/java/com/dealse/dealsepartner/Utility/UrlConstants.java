package com.dealse.dealsepartner.Utility;

import android.text.TextUtils;

/**
 * Created by Harshank Sananse on 1/31/2017.
 */
public class UrlConstants {
   // public static String BASE_URL = "http://dealse.mepass.in/";
      public static String BASE_URL = "http://vayotsavfoundation.org/";
   // public static String BASE_URL = "http://192.168.2.103:45457/";

    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || str.toString().equals("null");
    }
}
