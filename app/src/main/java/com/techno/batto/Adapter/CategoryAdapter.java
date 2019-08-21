package com.techno.batto.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.techno.batto.App.AppConfig;
import com.techno.batto.App.GPSTracker;
import com.techno.batto.R;
import com.techno.batto.Response.ProductListResponse;
import com.techno.batto.Result.CategoryResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Fragment.HomeFragment.Rvproduct;
import static com.techno.batto.Fragment.HomeFragment.gaggeredGridLayoutManager;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Activity activity;
    private List<CategoryResult> result;
    public static String product_id = "";
    private double longitude = 0.0;
    private double latitude = 0.0;

    public CategoryAdapter(Activity activity, List<CategoryResult> result) {
        this.activity = activity;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cat_pojo_lay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cat_name.setText(result.get(position).getCategoryName());
        Glide.with(activity)
                .load(result.get(position).getImage())
                .into(holder.cat_image);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cat_name;
        ImageView cat_image;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cat_name = itemView.findViewById(R.id.cat_name);
            cat_image = itemView.findViewById(R.id.cat_image);
        }

        @Override
        public void onClick(View view) {
            product_id = result.get(getAdapterPosition()).getId();
            GPSTracker track = new GPSTracker(activity);
            if (track.canGetLocation()) {
                latitude = track.getLatitude();
                longitude = track.getLongitude();
                getProductListCall(result.get(getAdapterPosition()).getId());
            } else {
                track.showSettingsAlert();
            }

        }
    }


//    public void filter(String charText) {
//        try {
//            charText = charText.toLowerCase();
//            task.clear();
//            if (charText.length() == 0) {
//                task.addAll(arSearchlist);
//            } else {
//                if (arSearchlist != null) {
//                    for (ProductMainModel wp : arSearchlist) {
//                        if (wp.getName().toLowerCase().contains(charText))//.toLowerCase(Locale.getDefault())
//                        {
//                            task.add(wp);
//                        }
//                    }
//                }
//            }
//            notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void getProductListCall(String id) {
        Call<ResponseBody> call = AppConfig.loadInterface().get_product(id, "" + latitude, "" + longitude);
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
                            ProductListResponse categoryResponse = gson.fromJson(responseData, ProductListResponse.class);
                            Rvproduct.setHasFixedSize(true);
                            gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
                            Rvproduct.setLayoutManager(gaggeredGridLayoutManager);
                            ProductsAdapter mainadapter = new ProductsAdapter(activity, categoryResponse.getResult());
                            Rvproduct.setAdapter(mainadapter);

                        } else {

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
}
