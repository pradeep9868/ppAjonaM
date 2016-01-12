package com.app.manojapp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Wadi on 13-10-2015.
 */
public class Static_Catelog {
    public static String getcolorscheme(Context context)
    {
        return context.getResources().getString(R.string.app_color).toString();
    }
    public static void Toast(Context context,String s)
    {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
    public static String geturl()
    {
        //return "http://192.168.0.13:8000/";
        return "http://lannister-api.elasticbeanstalk.com/";
    }
}
