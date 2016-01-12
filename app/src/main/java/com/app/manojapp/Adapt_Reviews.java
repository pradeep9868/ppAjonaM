package com.app.manojapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

import java.util.ArrayList;

public class Adapt_Reviews extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<JSONObject> data;

    static class ViewHolder
    {
        public TextView name,rated,desc,pic;
    }
	public Adapt_Reviews(Context context, ArrayList<JSONObject> data) {
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

        if (convertView == null||convertView.getTag()==null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_layout, parent, false);
            holder=new ViewHolder();
            holder.name=(TextView) convertView.findViewById(R.id.name);
			holder.rated=(TextView) convertView.findViewById(R.id.rated);
            holder.desc=(TextView) convertView.findViewById(R.id.desc);
			holder.pic = (TextView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder)convertView.getTag();
        }
		try {
            String data = jsonObject.getString("name");
            holder.name.setText(data);
            holder.pic.setText(""+data.charAt(0));
        }
		catch (Exception e) {		}
        try {			holder.rated.setText(jsonObject.getString("rating"));		}
        catch (Exception e) {		}
        try {			holder.desc.setText(jsonObject.getString("desc"));		}
        catch (Exception e) {		}
		return convertView;
	}


}
