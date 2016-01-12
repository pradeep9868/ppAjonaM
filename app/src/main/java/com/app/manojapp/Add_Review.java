package com.app.manojapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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


public class Add_Review extends Activity{
    TextView rated;
    EditText name,desc;
    RatingBar ratingBar;
    String url = Static_Catelog.geturl()+"jaime/add_review";
    int id;
    Context context;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_review);
        name= (EditText) findViewById(R.id.name);
        desc= (EditText) findViewById(R.id.desc);
        rated=(TextView) findViewById(R.id.rated);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        id=getIntent().getIntExtra("id",0);
        context=this;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Adding your review...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rated.setText(""+ratingBar.getRating());
                return false;
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                upload_data();
            }
        });
    }

    private void upload_data() {
        JSONObject object = new JSONObject();
        try {
            object.put("f_uniq_id",id);
            object.put("name",name.getText().toString());
            object.put("rating",Float.valueOf(rated.getText().toString()));
            object.put("desc",desc.getText().toString());

        } catch (JSONException e) {

        }

        String tag_json_obj = "upload_review";
        Log.i("Myapp", "Calling " + url+"data : "+object.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Myapp", response.toString());
                                Static_Catelog.Toast(context, "Thank you for posting your Review.");
                                if(mProgressDialog.isShowing())
                                    mProgressDialog.hide();
                                finish();
                            }
                        });

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                if(mProgressDialog.isShowing())
                    mProgressDialog.hide();
                Static_Catelog.Toast(context,"An Error Occured while posting rating please retry!");
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
