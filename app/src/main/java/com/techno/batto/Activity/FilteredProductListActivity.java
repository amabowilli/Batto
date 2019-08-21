package com.techno.batto.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Adapter.FilteredProductsAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.FilterLiestResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Activity.Filter.cat_id;
import static com.techno.batto.Activity.Filter.currency;
import static com.techno.batto.Activity.Filter.distance;
import static com.techno.batto.Activity.Filter.lat;
import static com.techno.batto.Activity.Filter.lng;
import static com.techno.batto.Activity.Filter.posted_in;
import static com.techno.batto.Activity.Filter.price_from;
import static com.techno.batto.Activity.Filter.price_to;
import static com.techno.batto.Activity.Filter.short_by;

public class FilteredProductListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView search_back;
    public static RecyclerView Rvproduct;
    public static StaggeredGridLayoutManager gaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_product_list);
        findId();
        search_back.setOnClickListener(this);
        Log.e("LatLng", lat + " --- " + lng)              ;
        getProductListCall();
    }

    private void findId() {
        search_back = findViewById(R.id.search_back);
        Rvproduct = findViewById(R.id.Rvproduct);
    }

    @Override
    public void onClick(View view) {
        if (view == search_back) {
            finish();
        }
    }

    private void getProductListCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().get_filter_product(cat_id, price_from, price_to, posted_in, short_by, distance, currency, "" + lat, "" + lng);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Datartrtretretre :- " + object);
                        if (object.getString("status").equals("1")) {

                            Gson gson = new Gson();
                            FilterLiestResponse categoryResponse = gson.fromJson(responseData, FilterLiestResponse.class);
                            Rvproduct.setHasFixedSize(true);
                            gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
                            Rvproduct.setLayoutManager(gaggeredGridLayoutManager);
                            FilteredProductsAdapter mainadapter = new FilteredProductsAdapter(FilteredProductListActivity.this, categoryResponse.getResult());
                            Rvproduct.setAdapter(mainadapter);

                        } else {
                            //   Toast.makeText(getActivity(), "Category Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FilteredProductListActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
