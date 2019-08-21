package com.techno.batto.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Adapter.MyReviewListAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.ReviewResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class ExchangesFragment extends Fragment {
    private View view;
    private ScrollView lay_card;
    public static RecyclerView Rvratinglist;
    private String user_id;

    @SuppressLint("ValidFragment")
    public ExchangesFragment(String logid) {
        this.user_id = logid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_fragment_layout, container, false);
        findId();
        getProductCall(user_id);
        return view;
    }

    private void findId() {
        lay_card = view.findViewById(R.id.lay_card);
        Rvratinglist = view.findViewById(R.id.Rvratinglist);
    }


    private void getProductCall(String id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().get_reviews(id);
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
                            lay_card.setVisibility(View.GONE);
                            Rvratinglist.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            ReviewResponse requestListResponse = gson.fromJson(responseData, ReviewResponse.class);

                            Rvratinglist.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            Rvratinglist.setLayoutManager(layoutManager);
                            Rvratinglist.setItemAnimator(new DefaultItemAnimator());
                            MyReviewListAdapter adapter = new MyReviewListAdapter(getActivity(), user_id, requestListResponse.getResult());
                            Rvratinglist.setAdapter(adapter);
                        } else {
                            lay_card.setVisibility(View.VISIBLE);
                            Rvratinglist.setVisibility(View.GONE);
                       //     Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
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
