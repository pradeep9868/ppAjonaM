package com.app.manojapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.manojapp.customcomponents.PullAndLoadMore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Reviews extends Activity{
    PullAndLoadMore lv;
    Context context;
    Adapt_Reviews adap;
    String url = Static_Catelog.geturl()+"jaime/reviews?id=";
    ArrayList<JSONObject> list_item;
    int pg=0;
    int id;
    FrameLayout add_review;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.all_reviews);
        context =this;
        id=getIntent().getIntExtra("id",0);
        lv= (PullAndLoadMore) findViewById(R.id.review_list);
        list_item=new ArrayList<>();
        add_review= (FrameLayout) findViewById(R.id.add_review);
        back= (TextView) findViewById(R.id.back);
        adap = new Adapt_Reviews(this,list_item);
        lv.setAdapter(adap);
        lv.setOnLoadMoreListener(new PullAndLoadMore.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (pg != 99)
                    download_list_data(pg);

            }
        });
        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Add_Review.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        download_list_data(pg);
    }

    private void download_list_data(int  page) {

        String tag_json_obj = "json_obj_req";
        Log.i("Myapp", "Calling " + url + id+"&p="+page);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url + id+"&p="+page, null,
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
                                        if(len==10)
                                            pg=pg+1;
                                        else
                                            pg=99;
                                    } else {
                                        pg=99;
                                        Log.i("Myapp", "null array");
                                    }
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
