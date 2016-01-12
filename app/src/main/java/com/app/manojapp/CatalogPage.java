package com.app.manojapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.manojapp.customcomponents.PullAndLoadMore;
import com.app.manojapp.customcomponents.ActivityTransitionLauncher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;


public class CatalogPage extends Activity implements View.OnClickListener {
    PullAndLoadMore lv;
    private final String[] menuTitles = {"Item 1",
            "Item 2",
            "Item 3",
            "Item 4",
            "Item 5",
            "Item 6"};
    Context context;
    Boolean usingfinelocation;
    JSONObject location;
    String lat,lng;
    TextView text_location,title;
    TextView back,filter,add_new;
    Adapt_Catalog adap;
    String url = Static_Catelog.geturl()+"jaime/search?p=";
    ArrayList<JSONObject> list_item;
    int pg=0;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        context =this;
        text_location= (TextView) findViewById(R.id.location);
        title= (TextView) findViewById(R.id.title);
        if(getIntent().hasExtra("location"))
        {
            usingfinelocation=false;
            try {
                location=new JSONObject(getIntent().getStringExtra("location"));
                type = getIntent().getIntExtra("type", 0);
                text_location.setText(location.getString("title"));
                JSONObject locat=location.getJSONObject("loc");
                lat=locat.getString("lat");
                lng=locat.getString("lng");
            } catch (JSONException e) {

            }
        }
        else if(getIntent().hasExtra("lat"))
        {
            usingfinelocation=true;
            lat=getIntent().getStringExtra("lat");
            lng=getIntent().getStringExtra("lon");
            type = getIntent().getIntExtra("type", 0);
            text_location.setText("Using Current Location");
        }

        (back= (TextView) findViewById(R.id.back)).setOnClickListener(this);
        (filter= (TextView) findViewById(R.id.filter)).setOnClickListener(this);
        (add_new= (TextView) findViewById(R.id.add_new)).setOnClickListener(this);


        lv= (PullAndLoadMore) findViewById(R.id.catalog_list);
        list_item=new ArrayList<>();

        adap = new Adapt_Catalog(this,list_item);
        lv.setAdapter(adap);
        lv.setOnLoadMoreListener(new PullAndLoadMore.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (pg != 99)
                    download_list_data(pg);

            }
        });
        TextView map_view = (TextView) findViewById(R.id.map_view);
        map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MapView.class);
                Class_map_info map_info=new Class_map_info(list_item);
                intent.putExtra("map_info",map_info);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String data_s = (String) adap.getItem(i);
                Intent intent = new Intent(context, Details.class);
                intent.putExtra("data", adap.getItem(i).toString());
                //startActivity(intent);
                ActivityTransitionLauncher.with(CatalogPage.this).from(view.findViewById(R.id.pic)).launch(intent);
            }
        });
        if(type==0)
        {
           title.setText("PAYPEE: Toilets");
        }
        else {
            title.setText("PAYPEE: Dustbins");
        }
        download_list_data(pg);
    }

    @Override
    public void onClick(View v) {
    switch(v.getId())
    {
        case R.id.back:
            finish();
            break;
        case R.id.filter:
            Toast.makeText(this,"Here Add Filter",Toast.LENGTH_SHORT).show();
            break;
        case R.id.add_new:
            Intent i = new Intent(CatalogPage.this,Add_New.class);
            startActivity(i);
            break;

    }
    }
    private void download_list_data(int  page) {

        String tag_json_obj = "json_obj_req";
        String param="&lat="+lat+"&lng="+lng;
        if(type==0)
        {
            param=param+"&t=toilet";
        }
        else {
            param=param+"&t=dustbin";
        }
        Log.i("Myapp", "Calling " + url + page+param);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url+ page+param, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Myapp", response.toString());
                                try {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray != null) {
                                        int len = jsonArray.length();
                                        for (int i = 0; i < len; i++) {
                                            list_item.add((JSONObject) jsonArray.get(i));
                                        }
                                    } else {
                                        Log.i("Myapp", "null array");
                                    }
                                    pg=pg+1;
                                } catch (Exception e) {
                                    Log.i("Myapp", "Error" + e.getMessage());
                                    pg=99;
                                }
                                Log.i("Myapp", "Before Notifying");
                                adap.notifyDataSetChanged();

                                Log.i("Myapp", "Hello" + list_item.size());
                              //  lv.onLoadMoreComplete();
                            }
                        });

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
               // lv.onLoadMoreComplete();
                pg=99;
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
