package com.app.manojapp.customcomponents;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.manojapp.R;
import com.app.manojapp.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wadi on 16-12-2015.
 */
public class IconsLayout extends LinearLayout {

        private View icons;
        private LinearLayout lay_loading,lay_reviews,inf_reviews;
        private TextView num_reviews;
        LayoutInflater inflater;

        public IconsLayout(Context context) {
            super(context);
            initializeViews(context);
        }

        public IconsLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            initializeViews(context);
        }

        public IconsLayout(Context context,
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
            num_reviews.setVisibility(GONE);

            /*name = (TextView) reviews.findViewById(R.id.name);
            //pic = (ImageView) reviews.findViewById(R.id.pic);
           // pic.setBackgroundResource(android.R.drawable.ic_media_previous);
            rated = (TextView)reviews.findViewById(R.id.rated);
            desc = (TextView)reviews.findViewById(R.id.desc);*/

        }
    public void update_layout_for_reviews(JSONArray jsonArray)
    {
        lay_loading.setVisibility(GONE);
        lay_reviews.setVisibility(VISIBLE);
        if (jsonArray != null&&jsonArray.length()>0) {

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    icons = inflater.inflate(R.layout.icons,null);
                    String icon = jsonArray.get(i).toString();
                    if(icon.equals("rec"))
                    {
                        ((TextView)icons.findViewById(R.id.icon)).setText(getContext().getString(R.string.recy));
                        ((TextView)icons.findViewById(R.id.text)).setText("Recycle");
                    }
                    if(icon.equals("cle"))
                    {
                        ((TextView)icons.findViewById(R.id.icon)).setText(getContext().getString(R.string.sweep));
                        ((TextView)icons.findViewById(R.id.text)).setText("Clean");
                    }
                    inf_reviews.addView(icons);
                } catch (JSONException e) {

                }

            }

        } else {
            TextView im = new TextView(getContext());
            LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity= Gravity.CENTER;
            int paddingPixel = 25;
            float density = getContext().getResources().getDisplayMetrics().density;
            int paddingDp = (int)(paddingPixel * density);
            im.setPadding(0,paddingDp,0,paddingDp);
            im.setLayoutParams(params);
            im.setText("NO ICONS AVAILABLE");
            inf_reviews.addView(im);
        }
    }
}
