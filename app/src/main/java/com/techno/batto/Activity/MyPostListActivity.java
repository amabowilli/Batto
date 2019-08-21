package com.techno.batto.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Adapter.SocialPostAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.PostListResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostListActivity extends AppCompatActivity implements View.OnClickListener {
    private String user_id;
    private ImageView img_back;
    private RecyclerView post_Rv;
    private ScrollView lay_scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_list);
        user_id = getIntent().getExtras().getString("user_id");
        findID();
        img_back.setOnClickListener(this);
        getPostCall(user_id);
    }

    private void findID() {
        img_back = findViewById(R.id.img_back);
        post_Rv = findViewById(R.id.post_Rv);
        lay_scroll = findViewById(R.id.lay_scroll);
    }

    @Override
    public void onClick(View view) {
        if (view==img_back){
            finish();
        }
    }


    private void getPostCall(String id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MyPostListActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().get_my_post(id);
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
                            PostListResponse requestListResponse = gson.fromJson(responseData, PostListResponse.class);

                            post_Rv.setHasFixedSize(true);
                            SocialPostAdapter adapter = new SocialPostAdapter(MyPostListActivity.this,requestListResponse.getResult());
                            post_Rv.setLayoutManager(new LinearLayoutManager(MyPostListActivity.this, LinearLayoutManager.VERTICAL, false));
                            post_Rv.setAdapter(adapter);
                        } else {
                            lay_scroll.setVisibility(View.VISIBLE);
                            post_Rv.setVisibility(View.GONE);
                            Toast.makeText(MyPostListActivity.this, "Request Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MyPostListActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
