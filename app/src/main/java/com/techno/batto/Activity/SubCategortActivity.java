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
import com.techno.batto.Adapter.SubCatListAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.SubCatResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategortActivity extends AppCompatActivity implements View.OnClickListener {
    private String cat_id,type;
    private ImageView img_back;
    private RecyclerView Rvsubcat;
    public static Activity subcatAcivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categort);
        type = getIntent().getExtras().getString("type");
        cat_id = getIntent().getExtras().getString("cat_id");

        findId();

        subcatAcivity=this;

        img_back.setOnClickListener(this);

        getSubCategoryListCall();
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
        Rvsubcat = findViewById(R.id.Rvsubcat);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        }
    }

    private void getSubCategoryListCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().get_subcategory(cat_id);
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
                            SubCatResponse categoryResponse = gson.fromJson(responseData, SubCatResponse.class);

                            Rvsubcat.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SubCategortActivity.this);
                            Rvsubcat.setLayoutManager(layoutManager);
                            Rvsubcat.setItemAnimator(new DefaultItemAnimator());
                            SubCatListAdapter adapter = new SubCatListAdapter(SubCategortActivity.this, categoryResponse.getResult(),type);
                            Rvsubcat.setAdapter(adapter);

                        } else {
                            Toast.makeText(SubCategortActivity.this, " Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SubCategortActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
