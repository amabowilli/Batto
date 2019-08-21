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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Activity.AddProductActivity;
import com.techno.batto.Adapter.YouAdapter;
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

public class YouFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button btn_selling;
    private RecyclerView you_Rv;
    private String user_id;
    private LinearLayout lay_main;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.you_fragment_layout, container, false);
        findID();
        btn_selling.setOnClickListener(this);
        user_id = getData(getActivity(), "user_id", "");
        getPeopleCall(user_id);
        return view;
    }

    private void findID() {
        btn_selling = view.findViewById(R.id.btn_selling);
        you_Rv = view.findViewById(R.id.you_Rv);
        lay_main = view.findViewById(R.id.lay_main);
    }


    @Override
    public void onClick(View view) {
        if (view == btn_selling) {
            Intent i = new Intent(getActivity(), AddProductActivity.class);
            startActivity(i);
            getActivity().finish();
        }
    }


    private void getPeopleCall(String user_id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().get_followers(user_id);
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
                            you_Rv.setVisibility(View.VISIBLE);
                            lay_main.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            FFResponse requestListResponse = gson.fromJson(responseData, FFResponse.class);
                            you_Rv.setHasFixedSize(true);
                            YouAdapter adapter = new YouAdapter(getActivity(), requestListResponse.getResult());
                            you_Rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            you_Rv.setAdapter(adapter);
                        } else {
                          //  Toast.makeText(getActivity(), "Request Not Found", Toast.LENGTH_SHORT).show();
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
