package com.techno.batto.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Adapter.FollowerFollowingAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.FFResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;

public class FollowerFollowingActivity extends AppCompatActivity implements View.OnClickListener {
    private String type, user_id;
    private RecyclerView people_Rv;
    private ImageView search_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_following);
        type = getIntent().getExtras().getString("type");
        user_id = getData(this, "user_id", "");
        findId();
        search_back.setOnClickListener(this);
        if (type.equals("Followers")) {
            getPeopleCall(user_id,"Accept");
        }else {
            getPeopleCall(user_id,"Following");
        }
    }

    private void findId() {
        people_Rv = findViewById(R.id.people_Rv);
        search_back = findViewById(R.id.search_back);
    }


    private void getPeopleCall(String user_id, final String x) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call;
        if (type.equals("Followers")) {
            call = AppConfig.loadInterface().get_followers(user_id);
        }else {
            call = AppConfig.loadInterface().get_following(user_id);
        }
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
                            FFResponse requestListResponse = gson.fromJson(responseData, FFResponse.class);
//response create krna hyha pr!!!!
                            people_Rv.setHasFixedSize(true);
                            FollowerFollowingAdapter adapter = new FollowerFollowingAdapter(FollowerFollowingActivity.this, requestListResponse.getResult(),x);
                            people_Rv.setLayoutManager(new LinearLayoutManager(FollowerFollowingActivity.this, LinearLayoutManager.VERTICAL, false));
                            people_Rv.setAdapter(adapter);
                        } else {
                            Toast.makeText(FollowerFollowingActivity.this, "Request Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FollowerFollowingActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == search_back) {
            finish();
        }
    }
}
