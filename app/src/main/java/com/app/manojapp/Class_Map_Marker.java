package com.app.manojapp;

import java.io.Serializable;

public class Class_Map_Marker implements Serializable {
    String title;
    double latitude;
    double longitude;
    int icon;

    Class_Map_Marker()
    {

    }
    Class_Map_Marker(String title,double latitude,double longitude,int icon)
    {
        this.title=title;
        this.latitude=latitude;
        this.longitude=longitude;
        this.icon=icon;
    }

}
