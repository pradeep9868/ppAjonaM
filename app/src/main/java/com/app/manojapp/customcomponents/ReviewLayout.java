package com.app.manojapp.customcomponents;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.manojapp.Details;
import com.app.manojapp.R;
import com.app.manojapp.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Wadi on 16-12-2015.
 */
public class ReviewLayout extends LinearLayout {

        private View reviews;
        private LinearLayout lay_loading,lay_reviews,inf_reviews;
        private TextView num_reviews;
        LayoutInflater inflater;

        public ReviewLayout(Context context) {
            super(context);
            initializeViews(context);
        }

        public ReviewLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            initializeViews(context);
        }

        public ReviewLayout(Context context,
                           AttributeSet attrs,
                           int defStyle) {
            super(context, attrs, defStyle);
            initializeViews(context);
        }

        private void initializeViews(Context context) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.review_main, this);

        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            lay_loading= (LinearLayout) this.findViewById(R.id.lay_loading);
            lay_reviews= (LinearLayout) this.findViewById(R.id.lay_reviews);
            inf_reviews= (LinearLayout) this.findViewById(R.id.reviews);
            num_reviews= (TextView) this.findViewById(R.id.num_reviews);

            /*name = (TextView) reviews.findViewById(R.id.name);
            //pic = (ImageView) reviews.findViewById(R.id.pic);
           // pic.setBackgroundResource(android.R.drawable.ic_media_previous);
            rated = (TextView)reviews.findViewById(R.id.rated);
            desc = (TextView)reviews.findViewById(R.id.desc);*/

        }
    public void update_layout_for_reviews(JSONArray jsonArray,int num,final int id)
    {
        lay_loading.setVisibility(GONE);
        lay_reviews.setVisibility(VISIBLE);
        if (jsonArray != null&&jsonArray.length()>0) {

            for (int i = 0; i < jsonArray.length(); i++) {
                reviews = inflater.inflate(R.layout.review_layout,null);
                try {
                    String data = ((JSONObject)jsonArray.get(i)).getString("name");
                    data=data.substring(0, 1).toUpperCase() + data.substring(1);
                    ((TextView)reviews.findViewById(R.id.name)).setText(data);
                    ((TextView)reviews.findViewById(R.id.pic)).setText(""+data.charAt(0));
                } catch (JSONException e) {

                }
                try {
                    ((TextView)reviews.findViewById(R.id.rated)).setText(((JSONObject) jsonArray.get(i)).getString("rating"));
                } catch (JSONException e) {

                }
                try {
                    ((TextView)reviews.findViewById(R.id.desc)).setText(((JSONObject)jsonArray.get(i)).getString("desc"));
                } catch (JSONException e) {

                }
                inf_reviews.addView(reviews);
            }
            num_reviews.setText("READ ALL REVIEWS ("+num+")");
            num_reviews.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(getContext(), Reviews.class);
                    i.putExtra("id",id);
                    getContext().startActivity(i);
                }
            });
        } else {
            ImageView im = new ImageView(getContext());
            LinearLayout.LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity= Gravity.CENTER;
            im.setLayoutParams(params);
            im.setImageResource(R.drawable.dog);
            inf_reviews.addView(im);
            num_reviews.setText("NO REVIEWS AVAILABLE");
        }
    }
}
