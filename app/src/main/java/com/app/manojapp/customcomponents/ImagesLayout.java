package com.app.manojapp.customcomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.manojapp.AppController;
import com.app.manojapp.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Wadi on 16-12-2015.
 */
public class ImagesLayout extends LinearLayout {

        private LinearLayout lay_loading,lay_reviews,lay_noimgavail;
        LayoutInflater inflater;
        ImageLoader imageLoader;

        public ImagesLayout(Context context) {
            super(context);
            initializeViews(context);
        }

        public ImagesLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            initializeViews(context);
        }

        public ImagesLayout(Context context,
                            AttributeSet attrs,
                            int defStyle) {
            super(context, attrs, defStyle);
            initializeViews(context);
        }

        private void initializeViews(Context context) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.details_images, this);
            imageLoader = AppController.getInstance().getImageLoader();

        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            lay_loading= (LinearLayout) this.findViewById(R.id.lay_loading);
            lay_reviews= (LinearLayout) this.findViewById(R.id.lay_images);
            lay_noimgavail= (LinearLayout) this.findViewById(R.id.noimages);

        }
    public void update_layout_for_reviews(JSONArray jsonArray)
    {
        lay_loading.setVisibility(GONE);
        lay_reviews.setVisibility(VISIBLE);
        if (jsonArray != null&&jsonArray.length()>0) {

            if(jsonArray.length()>4)
            {
                 findViewById(R.id.num_images).setVisibility(VISIBLE);
                ((Button)findViewById(R.id.num_images)).setText("+"+(jsonArray.length()-4));
            }
            int len=0;
            if(jsonArray.length()>=4)
                len=4;
            else
                len=jsonArray.length();

            for (int i = 0; i < len; i++) {
                try {

                    NetworkImageView im;
                    switch (i)
                    {
                        case 0: im = (NetworkImageView) findViewById(R.id.image1); break;
                        case 1: im = (NetworkImageView) findViewById(R.id.image2); break;
                        case 2: im = (NetworkImageView) findViewById(R.id.image3); break;
                        case 3: im = (NetworkImageView) findViewById(R.id.image4); break;
                        default: im = (NetworkImageView) findViewById(R.id.image1); break;
                    }
                    im.setVisibility(VISIBLE);
                    im.setImageUrl(jsonArray.get(i).toString(), imageLoader);
                } catch (JSONException e) {

                }

            }

        } else {
            lay_reviews.setVisibility(GONE);
            lay_noimgavail.setVisibility(VISIBLE);
        }
    }
}
