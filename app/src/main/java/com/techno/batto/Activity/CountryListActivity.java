package com.techno.batto.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Adapter.CountryListAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.CountryResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back;
    private RecyclerView Rvcountry;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);
        type = getIntent().getExtras().getString("type");
        findId();
        img_back.setOnClickListener(this);
        getCountryCall();
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
        Rvcountry = findViewById(R.id.Rvcountry);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        }
    }


    private void getCountryCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().getCountryList();
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
                            CountryResponse requestListResponse = gson.fromJson(responseData, CountryResponse.class);

                            Rvcountry.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CountryListActivity.this);
                            Rvcountry.setLayoutManager(layoutManager);
                            Rvcountry.setItemAnimator(new DefaultItemAnimator());
                            CountryListAdapter adapter = new CountryListAdapter(CountryListActivity.this, requestListResponse.getResult(),type);
                            Rvcountry.setAdapter(adapter);

                        } else {
                            Toast.makeText(CountryListActivity.this, "Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CountryListActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
