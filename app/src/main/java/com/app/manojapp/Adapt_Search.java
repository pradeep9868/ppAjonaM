package com.app.manojapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Adapt_Search extends BaseAdapter implements Filterable {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
    private ArrayList<JSONObject>data = null;
    private ArrayList<JSONObject>filteredData = null;
    private ItemFilter mFilter = new ItemFilter();

    static class ViewHolder
    {
        public TextView text_location;

    }
	public Adapt_Search(Context context, ArrayList<JSONObject> data) {
		this.context = context;
		this.data = data;
        this.filteredData=data;
	}
	@Override
	public int getCount() {
		return filteredData.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

    public Filter getFilter() {
        return mFilter;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;

        try {
            Log.i("Myapp","Printing position "+ position+" "+filteredData.get(position).getString("title"));
        } catch (JSONException e) {

        }
        if (convertView == null||convertView.getTag()==null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_search, parent, false);
            holder=new ViewHolder();
            holder.text_location=(TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder)convertView.getTag();
        }
        try {
            holder.text_location.setText(filteredData.get(position).getString("title"));
        }catch (Exception e){
            Log.i("Myapp","New Exception");
        }
		return convertView;
	}

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<JSONObject> list = data;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString="" ;

            for (int i = 0; i < count; i++) {

                try {
                    filterableString = list.get(i).getString("title");
                } catch (JSONException e) {

                }
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<JSONObject>) results.values;
            notifyDataSetChanged();
        }

    }

    @Override
    public void notifyDataSetChanged() {
        this.filteredData=data;
        super.notifyDataSetChanged();
    }
}
