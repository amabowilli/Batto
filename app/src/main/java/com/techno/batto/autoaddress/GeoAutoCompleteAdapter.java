package com.techno.batto.autoaddress;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.techno.batto.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by ritesh on 19/12/16.
 */

public class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private Activity context;
    private List<String> l2 = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private WebOperations wo;
    private String lat, lon;
    private AutoCompleteTextView edit;

    public GeoAutoCompleteAdapter(Activity context, List<String> l2, String lat, String lon, AutoCompleteTextView edit) {
        this.context = context;
        this.l2 = l2;
        this.lat = lat;
        this.lon = lon;
        this.edit = edit;
        layoutInflater = LayoutInflater.from(context);
        wo = new WebOperations(context);
    }

    @Override
    public int getCount() {

        return l2.size();
    }

    @Override
    public Object getItem(int i) {
        return l2.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = layoutInflater.inflate(R.layout.geo_search_result, viewGroup, false);
        TextView geo_search_result_text = (TextView) view.findViewById(R.id.geo_search_result_text);
        try {
            geo_search_result_text.setText(l2.get(i));

        } catch (Exception e) {

        }
        geo_search_result_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                edit.setText(l2.get(i));
                edit.dismissDropDown();

            }
        });


        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
//                    wo.setUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyB886BhBESygKs1NbVzFXfkAseAzKmNWEk" +
//                            ""+constraint.toString().trim().replaceAll(" ","+")+"&location="+"22.7028438"+","+"75.8716298"+"+&radius=20000&types=geocode&sensor=true");

                    wo.setUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDKAlEB1H9GElpQiE-o_T-So5jihEZnX3w&input=" + constraint.toString().trim().replaceAll(" ", "+") + "&location=" + lat + "," + lon + "+&radius=20000&types=geocode&sensor=true");
                    System.out.println("URL***" + "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDKAlEB1H9GElpQiE-o_T-So5jihEZnX3w&input=" + constraint.toString().trim().replaceAll(" ", "+") + "&location=" + lat + "," + lon + "+&radius=20000&types=geocode&sensor=true");

                    String result = null;

                    try {
                        result = new MyTask(wo, 3).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    parseJson(result);

                    System.out.println("FilterResults===============================" + result);
                    Log.e("Location===========", "Come" + result);
                    // Assign the data to the FilterResults
                    filterResults.values = l2;
                    filterResults.count = l2.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println("publishResults===============================" + results);
                if (results != null && results.count != 0) {
                    l2 = (List) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private void parseJson(String result) {
        try {
            l2 = new ArrayList<>();
            JSONObject jk = new JSONObject(result);
            Log.e("Check this GACA jk", ">>>" + jk);
            JSONArray predictions = jk.getJSONArray("predictions");
            for (int i = 0; i < predictions.length(); i++) {
                JSONObject js = predictions.getJSONObject(i);
                l2.add(js.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Address> findLocations(Context context, String query_text) {

        List<Address> geo_search_results = new ArrayList<Address>();

        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
        List<Address> addresses = null;

        try {
            // Getting a maximum of 15 Address that matches the input text
            addresses = geocoder.getFromLocationName(query_text, 15);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses;
    }
}
