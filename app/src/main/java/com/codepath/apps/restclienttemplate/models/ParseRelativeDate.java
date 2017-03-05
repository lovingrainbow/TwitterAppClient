package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Quietus on 2017/3/4.
 */

public class ParseRelativeDate {
    //getRelativeTimeAgo("Fri Mar 03 04:00:10 +0000 2017")
    public static String getRelativeTimeAgo(String rawJsonDate){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try{
            long dateMills = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMills,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return relativeDate;
    }
}
