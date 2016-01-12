package com.app.manojapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Class_map_info implements Serializable {
    public ArrayList<Class_Map_Marker> class_map_markers;
    Class_map_info(ArrayList<JSONObject> jsonObject){
       class_map_markers=new ArrayList<>();
        for (int i=0;i<jsonObject.size();i++)
        {
            try {
                Class_Map_Marker temp= new Class_Map_Marker("Dustbin "+i,jsonObject.get(i).getJSONObject("loc").getDouble("lat"),jsonObject.get(i).getJSONObject("loc").getDouble("lng"),R.drawable.dust);
                class_map_markers.add(temp);
            } catch (JSONException e) {

            }

        }
        }
}