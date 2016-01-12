package com.app.manojapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.manojapp.customcomponents.MyImageView;

import org.json.JSONObject;

import java.util.ArrayList;

public class Adapt_Catalog extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<JSONObject> data;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    static class ViewHolder
    {
        public TextView text_location,rating,review_photos;
        public MyImageView Pic;

    }
	public Adapt_Catalog(Context context, ArrayList<JSONObject> data) {
		this.context = context;
		this.data = data;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		JSONObject jsonObject =data.get(position);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null||convertView.getTag()==null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter, parent, false);
            holder=new ViewHolder();
            holder.text_location=(TextView) convertView.findViewById(R.id.text_location);
            holder.Pic= (MyImageView) convertView.findViewById(R.id.pic);
            holder.rating=(TextView) convertView.findViewById(R.id.rating);
            holder.review_photos=(TextView) convertView.findViewById(R.id.review_photo);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder)convertView.getTag();
        }
        try {
            if(jsonObject.has("pics")&&jsonObject.getJSONArray("pics").length()>0)
            holder.Pic.setImageUrl(jsonObject.getJSONArray("pics").get(0).toString(), imageLoader);		}
        catch (Exception e) {		}

		try {			holder.text_location.setText(jsonObject.getString("title"));		}
		catch (Exception e) {		}
        try {			holder.rating.setText(jsonObject.getString("rating"));		}
        catch (Exception e) {		}
        try {			holder.review_photos.setText(jsonObject.getString("review")+" Reviews | "+jsonObject.getJSONArray("pics").length()+" Photos");		}
        catch (Exception e) {		}
		return convertView;
	}


}
