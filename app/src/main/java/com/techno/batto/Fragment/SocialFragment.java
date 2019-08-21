package com.techno.batto.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Activity.AddPostActivity;
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

public class SocialFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RecyclerView post_Rv;
    private ImageView image_post;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.social_fragment_layout, container, false);
        findID();
        getPostCall("");
        image_post.setOnClickListener(this);
        return view;
    }

    private void findID() {
        image_post = view.findViewById(R.id.image_post);
        post_Rv = view.findViewById(R.id.post_Rv);
    }


    @Override
    public void onClick(View view) {
        if (view == image_post) {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        }
    }


    private void getPostCall(String id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().getPostList();
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
                            SocialPostAdapter adapter = new SocialPostAdapter(getActivity(),requestListResponse.getResult());
                            post_Rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            post_Rv.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "Request Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
