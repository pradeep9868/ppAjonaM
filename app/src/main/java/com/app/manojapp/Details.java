package com.app.manojapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.app.manojapp.customcomponents.IconsLayout;
import com.app.manojapp.customcomponents.ImagesLayout;
import com.app.manojapp.customcomponents.ReviewLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.app.manojapp.customcomponents.ActivityTransition;
import com.app.manojapp.customcomponents.ExitActivityTransition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Details extends FragmentActivity {
    private GoogleMap googleMap;
    Context context;
    JSONObject data;
    TextView title,desc,rating;
    NetworkImageView Pic;
    ImageLoader imageLoader;
    String url = Static_Catelog.geturl()+"jaime/description?id=";
    int uniq_id,num_review=0;
    ReviewLayout reviewLayout;
    FrameLayout add_review;
    IconsLayout iconsLayout;
    private ExitActivityTransition exitTransition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Pic= (NetworkImageView) findViewById(R.id.pic);
        findViewById(R.id.nonframepic).setVisibility(View.INVISIBLE);
        exitTransition = ActivityTransition.with(getIntent()).to(findViewById(R.id.frame_pic)).start(savedInstanceState);


        title= (TextView) findViewById(R.id.title);
        desc= (TextView) findViewById(R.id.desc);
        rating= (TextView) findViewById(R.id.rating);
        reviewLayout= (ReviewLayout) findViewById(R.id.review_layout);
        iconsLayout= (IconsLayout) findViewById(R.id.icons);
        add_review= (FrameLayout) findViewById(R.id.add_review);
        context=this;
        imageLoader = AppController.getInstance().getImageLoader();
        try
        {
            data = new JSONObject(getIntent().getStringExtra("data"));
        }
        catch (Exception e)
        {

        }
        try {
            Pic.setImageUrl(data.getJSONArray("pics").get(0).toString(), imageLoader);
            ((ImagesLayout)findViewById(R.id.images_layout)).update_layout_for_reviews(data.getJSONArray("pics"));
        } catch (JSONException e) {

        }

        try {
            title.setText(data.getString("title"));
        } catch (JSONException e) {

        }
        try {
            rating.setText(data.getString("rating"));
        } catch (JSONException e) {

        }
        try {
            num_review=data.getInt("review");
        } catch (JSONException e) {

        }
        try {
            uniq_id=data.getInt("uniq_id");
        } catch (JSONException e) {
            uniq_id=0;
        }
        CreateMap();

        download_data(uniq_id);
        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Add_Review.class);
                i.putExtra("id", uniq_id);
                startActivity(i);
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                findViewById(R.id.nonframepic).setVisibility(View.VISIBLE);
            }
        }, 300);

    }

    public  void CreateMap()
    {


        try {
            // Loading map
            initilizeMap();

            // Changing map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);

            googleMap.getUiSettings().setZoomControlsEnabled(false);

            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);


            double latitude ;
            double longitude ;

            try {
                latitude=data.getJSONObject("loc").getDouble("lat");
                longitude=data.getJSONObject("loc").getDouble("lng");
            } catch (JSONException e) {
                latitude=0;
                longitude=0;
            }

            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(latitude, longitude))
                    .title(" Maps ");

            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


            googleMap.addMarker(marker);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude,
                            longitude)).zoom(18).build();

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

    private void download_data(final int id) {

        String tag_json_obj = "json_obj_req";

        Log.i("Myapp", "Calling url " + url+ id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url+ id, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONArray jsonArray = response.getJSONArray("review");
                                    reviewLayout.update_layout_for_reviews(jsonArray,num_review,id);

                                } catch (Exception e) {
                                    Log.i("Myapp", "Error" + e.getMessage());
                                }
                                try
                                {
                                    JSONObject jsonObject =response.getJSONObject("data");
                                    desc.setText(jsonObject.getString("desc"));
                                    iconsLayout.update_layout_for_reviews(jsonObject.getJSONArray("icons"));
                                }
                                catch (Exception e)
                                {
                                }
                                /*Log.i("Myapp", "Before Notifying");
                                //data.notifyDataSetChanged();
                                if(len==0)
                                    Static_Catelog.Toast(context,"No Reviews Available");*/
                            }
                        });

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.nonframepic).setVisibility(View.INVISIBLE);
        exitTransition.exit(this);
    }
}
