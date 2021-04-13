package com.dealse.dealsepartner.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by harshank on 25/6/18.
 */

public class Constants {

    public static String VALUE_FIRSTAPPKEY = "4B16179D2F14DB157AD9587DBB138";
    public static String KEY_STOREID = "storeId";
    public static String KEY_TOKEN = "token";
    public static String KEY_USERNAME = "username";

    public static String convertDate(String gotdate){
        String formattedDate = "";
        if(gotdate != null && !gotdate.equalsIgnoreCase("")) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            Date parsedDate = null;
            try {
                parsedDate = inputFormat.parse(gotdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDate = outputFormat.format(parsedDate);
        }else {
            return formattedDate;
        }
       return formattedDate;
    }

    public static String convertDateToSend(String gotdate){
        //Date currentDate = new Date(Long.valueOf(gotdate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(gotdate));
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        /*Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(gotdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        String formattedDate = outputFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String dateToSendString(String gotdate){
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(gotdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(parsedDate);
        return formattedDate;
    }

    public static boolean isValidDate(String date,String currentDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date date1 = sdf.parse(date);
            Date date2 = sdf.parse(currentDate);
            int result = date1.compareTo(date2);
            return (result <= 0 ? true : false);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return true;
    }
}
