package com.techno.batto.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Activity.Home;
import com.techno.batto.Adapter.MySoldProductListAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.MyProductResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;

@SuppressLint("ValidFragment")
public class SoldFragment extends Fragment implements View.OnClickListener {
    private View view;
    public static ScrollView lay_scroll;
    public static RecyclerView Rvsold;
    private String user_id;
    private Button btn_selling;

    @SuppressLint("ValidFragment")
    public SoldFragment(String logid) {
        user_id = logid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sold_fragment_layout, container, false);
        findId();
        user_id = getData(getActivity(), "user_id", "");
        getProductCall(user_id);
        btn_selling.setOnClickListener(this);
        return view;
    }

    private void findId() {
        Rvsold = view.findViewById(R.id.Rvsold);
        lay_scroll = view.findViewById(R.id.lay_scroll);
        btn_selling = view.findViewById(R.id.btn_selling);
    }


    private void getProductCall(String id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().getMySoldProduct(id);
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
                            lay_scroll.setVisibility(View.GONE);
                            Rvsold.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            MyProductResponse requestListResponse = gson.fromJson(responseData, MyProductResponse.class);

                            Rvsold.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            Rvsold.setLayoutManager(layoutManager);
                            Rvsold.setItemAnimator(new DefaultItemAnimator());
                            MySoldProductListAdapter adapter = new MySoldProductListAdapter(getActivity(), requestListResponse.getResult());
                            Rvsold.setAdapter(adapter);
                        } else {
                            // Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        if (view == btn_selling) {
            startActivity(new Intent(getActivity(), Home.class));
            getActivity().finish();
        }
    }
}
