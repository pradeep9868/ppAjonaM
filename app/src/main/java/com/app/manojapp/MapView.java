package com.app.manojapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapView extends FragmentActivity {

    private GoogleMap googleMap;
    Context context;
    ArrayList<Class_Map_Marker> marks;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        context=this;
       // setMapTransParent((ViewGroup)itemView);
        Class_map_info map_info = (Class_map_info) getIntent().getSerializableExtra("map_info");
        type=getIntent().getIntExtra("type",0);
        marks= map_info.class_map_markers;
        CreateMap();
    }

    public  void CreateMap()
    {


        try {
            // Loading map
            initilizeMap();

            // Changing map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location
            googleMap.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            /*
            double latitude = Double.parseDouble("28.4989091");
            double longitude = Double.parseDouble("77.238997");


            double[] randomLocation = createRandLocation(latitude,
                    longitude);

            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(randomLocation[0], randomLocation[1]))
                    .title(" Maps ");

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.search_icon));


            googleMap.addMarker(marker);
            */
            MarkerOptions marker;
            for(int i=0;i<marks.size();i++)
            {
                marker = new MarkerOptions().position(
                        new LatLng(marks.get(i).latitude, marks.get(i).longitude))
                        .title(marks.get(i).title);
                if(type==0)
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.toi));
                else
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.dust));
                //marker.icon(BitmapDescriptorFactory.fromResource(marks.get(i).icon));

                googleMap.addMarker(marker);
            }
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                Log.i("Myapp","Hai"+arg0.getTitle());
                }
            });
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(marks.get(0).latitude,
                            marks.get(0).longitude)).zoom(18).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initilizeMap() {
        if (googleMap == null) {

            googleMap = ((SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(context,
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();

            }
        }
    }


    private  void setMapTransParent(ViewGroup group)
    {
        int childCount=group.getChildCount();

        for (int i=0;i<childCount;i++)
        {
            View child=group.getChildAt(i);

            if (child instanceof ViewGroup)
            {
                child.setBackgroundColor(0X00000000);
            }
            else if (child instanceof SurfaceView)
            {
                child.setBackgroundColor(0X00000000);
                ((SurfaceView)child).setZOrderOnTop(true);
            }
        }
    }

    private double[] createRandLocation(double latitude, double longitude) {

        return new double[] { latitude + ((Math.random() - 0.5) / 500),
                longitude + ((Math.random() - 0.5) / 500),
                150 + ((Math.random() - 0.5) * 10) };
    }


}
