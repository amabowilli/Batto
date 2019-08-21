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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.techno.batto.Activity.EditProductDetailsActivity;
import com.techno.batto.Activity.IntrestedUserListActivity;
import com.techno.batto.Activity.ProductDetailsActivity;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.MyProductResponse;
import com.techno.batto.Result.MyProductResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;
import static com.techno.batto.Fragment.SellingProfileFragment.Rvselling;
import static com.techno.batto.Fragment.SellingProfileFragment.lay_scroll;


public class MyProductListAdapter extends RecyclerView.Adapter<MyProductListAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private View view;
    private List<MyProductResult> result;
    private String user_id, saler_id, request_id;

    public MyProductListAdapter(Activity activity, String user_id, List<MyProductResult> result) {
        this.activity = activity;
        this.result = result;
        saler_id = user_id;
        this.user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_items, parent, false);
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
            holder.lay_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final PrettyDialog pDialog = new PrettyDialog(activity);
                    pDialog.setTitle("What do you Want??")
                            .setIcon(R.drawable.report)
                            .setMessage("please select atleast one option")
                            .addButton("View", R.color.pdlg_color_white, R.color.pdlg_color_green,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            pDialog.dismiss();
                                            Intent i = new Intent(activity, ProductDetailsActivity.class);
                                            i.putExtra("product_id", "" + result.get(position).getId());
                                            activity.startActivity(i);
                                        }
                                    }
                            ).addButton("Edit", R.color.pdlg_color_white, R.color.pdlg_color_blue,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {

                                    Intent i = new Intent(activity, EditProductDetailsActivity.class);
                                    i.putExtra("product_id", "" + result.get(position).getId());
                                    activity.startActivity(i);
                                    pDialog.dismiss();
                                }
                            }
                    )
                            .addButton("Sold", R.color.pdlg_color_white, R.color.pdlg_color_gray,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            pDialog.dismiss();
                                            Intent i = new Intent(activity, IntrestedUserListActivity.class);
                                            i.putExtra("product_id", "" + result.get(position).getId());
                                            activity.startActivity(i);
                                        }
                                    }
                            )
                            .addButton("Cancel", R.color.pdlg_color_white, R.color.pdlg_color_red,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            pDialog.dismiss();
                                        }
                                    }
                            )
                            .show();

                }
            });
        } else {
            holder.img_delete.setVisibility(View.GONE);
        }

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PrettyDialog pDialog = new PrettyDialog(activity);
                pDialog.setTitle("What do you Want??")
                        .setIcon(R.drawable.report)
                        .setMessage("Are you sure do you want to delete it?")
                        .addButton("Yes", R.color.pdlg_color_white, R.color.pdlg_color_green,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                        deleteProductCall(result.get(position).getId());
                                    }
                                }
                        )

                        .addButton("No", R.color.pdlg_color_white, R.color.pdlg_color_red,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                    }
                                }
                        ).show();

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
        LinearLayout lay_one;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image_view = itemView.findViewById(R.id.image_view);
            img_delete = itemView.findViewById(R.id.img_delete);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_status = itemView.findViewById(R.id.txt_status);
            lay_one = itemView.findViewById(R.id.lay_one);

        }

        @Override
        public void onClick(View view) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void deleteProductCall(String product_id) {
        Call<ResponseBody> call = AppConfig.loadInterface().deleteproduct(product_id);
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
        Call<ResponseBody> call = AppConfig.loadInterface().getMyProduct(id);
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
                            Rvselling.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            MyProductResponse requestListResponse = gson.fromJson(responseData, MyProductResponse.class);

                            Rvselling.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
                            Rvselling.setLayoutManager(layoutManager);
                            Rvselling.setItemAnimator(new DefaultItemAnimator());
                            MyProductListAdapter adapter = new MyProductListAdapter(activity, user_id, requestListResponse.getResult());
                            Rvselling.setAdapter(adapter);
                        } else {
                            lay_scroll.setVisibility(View.VISIBLE);
                            Rvselling.setVisibility(View.GONE);
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
