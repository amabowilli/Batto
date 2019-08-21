package com.techno.batto.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.squareup.picasso.Picasso;
import com.techno.batto.App.AppConfig;
import com.techno.batto.App.GeoCodingLocationLatlng;
import com.techno.batto.Bean.ExpandableHeightGridView;
import com.techno.batto.Bean.GPSTracker;
import com.techno.batto.R;
import com.techno.batto.Response.CategoryResponse;
import com.techno.batto.Result.CategoryResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Filter extends AppCompatActivity implements View.OnClickListener {
    private ImageView filter_back;
    private ExpandableHeightGridView filter_cat_grid_view;
    private RelativeLayout go_to_change_location_lay, lay_reset, lay_apply, lay_currency;

    public static TextView txt_current_address, txt_range, txt_currency;
    public static Double lat, lng;
    public static String currency, cat_id, address, distance = "10", short_by = "", posted_in = "", price_from = "", price_to = "";
    private SeekBar filter_seekbar;

    private int si = -1;

    private EditText edt_pfrom, edt_pto;

    private RelativeLayout lay_h_to_l, lay_l_to_h, lay_24hour, lay_7day, lay_30day, lay_all;

    // lay_new, lay_closest,
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        findId();
        //-------------------------- on click --------------------------
        filter_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        go_to_change_location_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChangeLocation.class));
            }
        });

        lay_reset.setOnClickListener(this);
        lay_apply.setOnClickListener(this);

        getLocation();
        getCategoryListCall();

        filter_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                distance = "" + progress;
                txt_range.setText("DISTANCE : " + progress + "Km");
                // Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

            }
        });

        // lay_new.setOnClickListener(this);
        //  lay_closest.setOnClickListener(this);
        lay_h_to_l.setOnClickListener(this);
        lay_l_to_h.setOnClickListener(this);

        lay_24hour.setOnClickListener(this);
        lay_7day.setOnClickListener(this);
        lay_30day.setOnClickListener(this);
        lay_all.setOnClickListener(this);
        lay_currency.setOnClickListener(this);


    }

    private void getLocation() {
        GPSTracker tracker = new GPSTracker(this);
        if (tracker.canGetLocation()) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();

            address = GeoCodingLocationLatlng.getAddressFromLocation(lat, lng, this);
            txt_current_address.setText(address);

        } else {
            tracker.showSettingsAlert();
        }
    }

    private void findId() {
        filter_back = findViewById(R.id.filter_back);
        go_to_change_location_lay = findViewById(R.id.go_to_change_location_lay);
        filter_cat_grid_view = findViewById(R.id.filter_cat_grid_view);
        lay_reset = findViewById(R.id.lay_reset);
        lay_apply = findViewById(R.id.lay_apply);
        txt_current_address = findViewById(R.id.txt_current_address);
        filter_seekbar = findViewById(R.id.filter_seekbar);
        txt_range = findViewById(R.id.txt_range);

        // lay_new = findViewById(R.id.lay_new);
        // lay_closest = findViewById(R.id.lay_closest);
        lay_h_to_l = findViewById(R.id.lay_h_to_l);
        lay_l_to_h = findViewById(R.id.lay_l_to_h);

        lay_24hour = findViewById(R.id.lay_24hour);
        lay_7day = findViewById(R.id.lay_7day);
        lay_30day = findViewById(R.id.lay_30day);
        lay_all = findViewById(R.id.lay_all);

        edt_pfrom = findViewById(R.id.edt_pfrom);
        edt_pto = findViewById(R.id.edt_pto);
        lay_currency = findViewById(R.id.lay_currency);
        txt_currency = findViewById(R.id.txt_currency);

    }

    @Override
    public void onClick(View view) {
        if (view == lay_reset) {
            startActivity(new Intent(getApplicationContext(), FilteredProductListActivity.class));
        } else if (view == lay_apply) {
            price_from = edt_pfrom.getText().toString();
            price_to = edt_pto.getText().toString();
            try {
                address = txt_current_address.getText().toString();
                if (!address.equalsIgnoreCase("")) {
                    LatLng latlg = GeoCodingLocationLatlng.getLocationFromAddress(address, Filter.this);
                    lat = latlg.latitude;
                    lng = latlg.longitude;
                    startActivity(new Intent(getApplicationContext(), FilteredProductListActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (view == lay_h_to_l) {
            //lay_new.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // lay_closest.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_h_to_l.setBackgroundColor(Color.parseColor("#ff8533"));
            lay_l_to_h.setBackgroundColor(Color.parseColor("#FFFFFF"));
            short_by = "high";
        } else if (view == lay_l_to_h) {
            //lay_new.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // lay_closest.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_h_to_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_l_to_h.setBackgroundColor(Color.parseColor("#ff8533"));
            short_by = "low";
        } else if (view == lay_24hour) {
            lay_24hour.setBackgroundColor(Color.parseColor("#ff8533"));
            lay_7day.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_30day.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
            posted_in = "1 days";
        } else if (view == lay_7day) {
            lay_24hour.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_7day.setBackgroundColor(Color.parseColor("#ff8533"));
            lay_30day.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
            posted_in = "7 days";
        } else if (view == lay_30day) {
            lay_24hour.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_7day.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_30day.setBackgroundColor(Color.parseColor("#ff8533"));
            lay_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
            posted_in = "30 days";
        } else if (view == lay_all) {
            lay_24hour.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_7day.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_30day.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lay_all.setBackgroundColor(Color.parseColor("#ff8533"));
            posted_in = "";
        } else if (view == lay_currency) {
            currencyPicker();
        }

    }


    private void currencyPicker() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                //img_flag.setImageDrawable(flagDrawableResID);
                // img_flag.setBackgroundResource(flagDrawableResID);
                txt_currency.setText("" + code);
                currency = code;
                picker.dismiss();
            }
        });
        picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
    }


//     else if (view == lay_new) {
//        lay_new.setBackgroundColor(Color.parseColor("#ff8533"));
//        lay_closest.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        lay_h_to_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        lay_l_to_h.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        short_by = "";
//    } else if (view == lay_closest) {
//        lay_new.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        lay_closest.setBackgroundColor(Color.parseColor("#ff8533"));
//        lay_h_to_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        lay_l_to_h.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        short_by = "";
//    }

    private class FilterCategoryAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        List<CategoryResult> singleCategoryLists;
        private Context context;
        View view;

        public FilterCategoryAdapter(Context context, List<CategoryResult> singleCategoryLists) {
            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.singleCategoryLists = singleCategoryLists;
        }

        @Override
        public int getCount() {
            return singleCategoryLists.size();

        }

        @Override
        public Object getItem(int position) {
            return position;

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder spinnerHolder;
            if (convertView == null) {
                spinnerHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.filter_cat_pojo_lay, viewGroup, false);

                spinnerHolder.home_cat_image_pojo = convertView.findViewById(R.id.home_cat_image_pojo);
                spinnerHolder.home_cat_name_pojo = convertView.findViewById(R.id.home_cat_name_pojo);
                spinnerHolder.lay_main = convertView.findViewById(R.id.lay_main);

                spinnerHolder.home_cat_name_pojo.setText(singleCategoryLists.get(position).getCategoryName());

                Picasso.with(context).load(singleCategoryLists.get(position).getImage()).error(R.drawable.not_found).into(spinnerHolder.home_cat_image_pojo);

                if (si == position)
                    spinnerHolder.lay_main.setBackgroundColor(Color.parseColor("#f5bd04"));
                else
                    spinnerHolder.lay_main.setBackgroundColor(Color.parseColor("#FFFFFF"));


                spinnerHolder.lay_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "" + singleCategoryLists.get(position).getCategoryName(), Toast.LENGTH_SHORT).show();
                        si = position;
                        notifyDataSetChanged();
                        cat_id = singleCategoryLists.get(position).getId();
                        //spinnerHolder.lay_main.setBackgroundColor(Color.parseColor("#f5bd04"));
                    }
                });


            } else {
                spinnerHolder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        class ViewHolder {
            CircleImageView home_cat_image_pojo;
            TextView home_cat_name_pojo;
            LinearLayout lay_main;
        }
    }


    private void getCategoryListCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().get_category();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {

                            Gson gson = new Gson();
                            CategoryResponse categoryResponse = gson.fromJson(responseData, CategoryResponse.class);
                            filter_cat_grid_view.setExpanded(true);
                            FilterCategoryAdapter adaper = new FilterCategoryAdapter(getApplicationContext(), categoryResponse.getResult());
                            filter_cat_grid_view.setAdapter(adaper);

                        } else {
                            Toast.makeText(Filter.this, " Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else ;
                    // AppConfig.showToast("server error");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Filter.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
