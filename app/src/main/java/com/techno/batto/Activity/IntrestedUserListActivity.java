package com.techno.batto.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Adapter.IntrestedUserListAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.IntrestedUserResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntrestedUserListActivity extends AppCompatActivity implements View.OnClickListener {
    private String product_id;
    private RecyclerView Rvainuser;
    private ImageView img_back;
    private Button btn_sale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrested_user_list);
        product_id = getIntent().getExtras().getString("product_id");
        findId();
        getIntListCall(product_id);
        img_back.setOnClickListener(this);
        btn_sale.setOnClickListener(this);
    }


    private void findId() {
        Rvainuser = findViewById(R.id.Rvainuser);
        img_back = findViewById(R.id.img_back);
        btn_sale = findViewById(R.id.btn_sale);

    }


    private void getIntListCall(String id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(IntrestedUserListActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().get_interested_product(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            Gson gson = new Gson();
                            IntrestedUserResponse requestListResponse = gson.fromJson(responseData, IntrestedUserResponse.class);
                            Rvainuser.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(IntrestedUserListActivity.this);
                            Rvainuser.setLayoutManager(layoutManager);
                            Rvainuser.setItemAnimator(new DefaultItemAnimator());
                            IntrestedUserListAdapter adapter = new IntrestedUserListAdapter(IntrestedUserListActivity.this, requestListResponse.getResult(), product_id);
                            Rvainuser.setAdapter(adapter);
                        } else {
                            Toast.makeText(IntrestedUserListActivity.this, " Not Found", Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(IntrestedUserListActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        } else if (view == btn_sale) {
            saleProductCall();
        }
    }

    private void saleProductCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().saleproduct(product_id, "0");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(IntrestedUserListActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(IntrestedUserListActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(IntrestedUserListActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
