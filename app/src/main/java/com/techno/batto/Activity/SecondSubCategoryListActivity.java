package com.techno.batto.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Adapter.SecondSubCatListAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.ChildCatResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondSubCategoryListActivity extends AppCompatActivity implements View.OnClickListener {
    private String sub_cat_id;
    private RecyclerView Rvssubcat;
    private ImageView img_back;
    public static Activity childCatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_sub_category_list);

        sub_cat_id = getIntent().getExtras().getString("sub_cat_id");

        findId();

        childCatActivity = this;

        img_back.setOnClickListener(this);

        getSecondSubCategoryListCall();

    }

    private void findId() {
        Rvssubcat = findViewById(R.id.Rvssubcat);
        img_back = findViewById(R.id.img_back);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        }
    }


    private void getSecondSubCategoryListCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().get_secondsubcategory(sub_cat_id);
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
                            ChildCatResponse categoryResponse = gson.fromJson(responseData, ChildCatResponse.class);

                            Rvssubcat.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SecondSubCategoryListActivity.this);
                            Rvssubcat.setLayoutManager(layoutManager);
                            Rvssubcat.setItemAnimator(new DefaultItemAnimator());
                            SecondSubCatListAdapter adapter = new SecondSubCatListAdapter(SecondSubCategoryListActivity.this, categoryResponse.getResult());
                            Rvssubcat.setAdapter(adapter);

                        } else {
                            Toast.makeText(SecondSubCategoryListActivity.this, " Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SecondSubCategoryListActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
