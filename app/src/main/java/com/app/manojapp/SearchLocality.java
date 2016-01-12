package com.app.manojapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchLocality extends Activity {
    ListView lv;
    LinearLayout use_curr_locat;
    Context context;
    EditText search_box;
    TextView cancel_search;
    Boolean more_item_avail = true;
    ArrayList<JSONObject> list_item;
    Adapt_Search data;
    LocationManager locationManager;
    LocationListener locationListener;
    ProgressDialog mProgressDialog;
    String url = Static_Catelog.geturl()+"jaime/location?q=";
    TextView back_but;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_locality);
        list_item = new ArrayList<>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Retrieving Current Location...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);

        use_curr_locat = (LinearLayout) findViewById(R.id.use_curr_locat);
        search_box = (EditText) findViewById(R.id.search_box);
        cancel_search = (TextView) findViewById(R.id.cancel_search);
        back_but= (TextView) findViewById(R.id.back_but);
        type = getIntent().getIntExtra("selected", 0);
        if (type == 0) {
            ((LinearLayout) findViewById(R.id.mainlayout)).setBackgroundResource(R.drawable.toilet);
        } else {
            ((LinearLayout) findViewById(R.id.mainlayout)).setBackgroundResource(R.drawable.dustbin);
        }
        lv = (ListView) findViewById(R.id.listview);
        context = this;
        data = new Adapt_Search(this, list_item);
        //data=new ArrayAdapter(this,R.layout.adapter_search,R.id.text,list_item);
        lv.setAdapter(data);
        locationManager = (LocationManager)
                getSystemService(this.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        resetall();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data_s = (String) data.getItem(i).toString();
                Intent intent = new Intent(context, CatalogPage.class);
                intent.putExtra("location", data_s);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
        use_curr_locat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    mProgressDialog.show();
//                    locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                }

            }
        });
        cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetall();
            }
        });
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    cancel_search.setVisibility(View.VISIBLE);
                    if (list_item.size() == 0)
                        download_list(s);
                    else
                        data.getFilter().filter(s, new Filter.FilterListener() {
                            @Override
                            public void onFilterComplete(int count) {
                                Static_Catelog.Toast(context, "Filter total now:" + count);
                                if (count < 2) {
                                    download_list(s);
                                }
                            }
                        });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void resetall() {
        more_item_avail = true;
        cancel_search.setVisibility(View.INVISIBLE);
        search_box.setText("");
        list_item.clear();
        data.notifyDataSetChanged();
    }

    private void download_list(CharSequence s) {
        list_item.clear();
        download_list_data(s);
    }

    private void download_list_data(CharSequence q) {

        String tag_json_obj = "getting_list";
        String param;
        try {
            param = URLEncoder.encode(new StringBuilder(q).toString(), "UTF-8");
        } catch (Exception e) {
            param="";
        }
        if(type==0)
        {
            param=param+"&t=toilet";
        }
        else {
            param=param+"&t=dustbin";
        }


        Log.i("Myapp", "Calling " + url+ param);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url+ param, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list_item.clear();
                                int len=0;
                                Log.i("Myapp", response.toString());
                                try {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray != null) {
                                        len = jsonArray.length();
                                        for (int i = 0; i < len; i++) {
                                            list_item.add((JSONObject) jsonArray.get(i));
                                        }
                                    } else {
                                        Log.i("Myapp", "null array");
                                    }
                                } catch (Exception e) {
                                    Log.i("Myapp", "Error" + e.getMessage());
                                }
                                Log.i("Myapp", "Before Notifying");
                                data.notifyDataSetChanged();
                                if(len==0)
                                    Static_Catelog.Toast(context,"No More Results Available");
                               // Log.i("Myapp", "After Notifying");
                               // lv.setAdapter(data);
                              /*  lv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        lv.setAdapter(data);
                                    }
                                });*/
                                Log.i("Myapp", "Hello" + list_item.size());
                            }
                        });

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "" + loc.getLongitude();
            String latitude = "" + loc.getLatitude();
            //editLocation.setText(s);
            Intent intent = new Intent(context, CatalogPage.class);
            intent.putExtra("lat", latitude);
            intent.putExtra("lon", longitude);
            intent.putExtra("type",type);
            startActivity(intent);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            mProgressDialog.show();
//
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
        }
    }
}
