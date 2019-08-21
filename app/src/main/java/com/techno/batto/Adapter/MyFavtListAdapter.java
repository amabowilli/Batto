package com.techno.batto.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.techno.batto.Activity.ProductDetailsActivity;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.FavResponse;
import com.techno.batto.Result.FavResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;
import static com.techno.batto.Fragment.FavouritiesFragment.Rvfavlist;
import static com.techno.batto.Fragment.FavouritiesFragment.lay_scrollone;


public class MyFavtListAdapter extends RecyclerView.Adapter<MyFavtListAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private View view;
    private List<FavResult> result;
    private String user_id, saler_id, request_id;

    public MyFavtListAdapter(Activity activity, String user_id, List<FavResult> result) {
        this.activity = activity;
        this.result = result;
        this.user_id = getData(activity, "user_id", "");
        this.saler_id = user_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fev_item_list, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_name.setText(result.get(position).getName());
        holder.txt_price.setText(result.get(position).getPrice());

        if (saler_id.equals(user_id)) {
            holder.img_delete.setVisibility(View.VISIBLE);
        } else {
            holder.img_delete.setVisibility(View.GONE);
        }

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProductCall(result.get(position).getId());
            }
        });

        holder.txt_status.setText(result.get(position).getStatus());

        Picasso.with(activity).load(result.get(position).getImage1()).error(R.drawable.not_found).into(holder.image_view);

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image_view, img_delete;
        TextView txt_name, txt_price, txt_status;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image_view = itemView.findViewById(R.id.image_view);
            img_delete = itemView.findViewById(R.id.img_delete);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_status = itemView.findViewById(R.id.txt_status);

        }

        @Override
        public void onClick(View view) {
            try {
                Intent i = new Intent(activity, ProductDetailsActivity.class);
                i.putExtra("product_id", "" + result.get(getAdapterPosition()).getId());
                activity.startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void deleteProductCall(String product_id) {
        Call<ResponseBody> call = AppConfig.loadInterface().addRemoveFav(user_id, product_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            user_id = getData(activity, "user_id", "");
                            getProductCall(user_id);
                        } else {
                            Toast.makeText(activity, "Category Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductCall(String id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().getFavproduct(id);
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
                            lay_scrollone.setVisibility(View.GONE);
                            Rvfavlist.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            FavResponse requestListResponse = gson.fromJson(responseData, FavResponse.class);

                            Rvfavlist.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
                            Rvfavlist.setLayoutManager(layoutManager);
                            Rvfavlist.setItemAnimator(new DefaultItemAnimator());
                            MyFavtListAdapter adapter = new MyFavtListAdapter(activity, user_id, requestListResponse.getResult());
                            Rvfavlist.setAdapter(adapter);
                        } else {
                            lay_scrollone.setVisibility(View.VISIBLE);
                            Rvfavlist.setVisibility(View.GONE);
                            Toast.makeText(activity, "Data Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
