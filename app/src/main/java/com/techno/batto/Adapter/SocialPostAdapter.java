package com.techno.batto.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techno.batto.Activity.Login;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Result.PostListResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;


/**
 * Created by ritesh on 18/12/17.
 */

public class SocialPostAdapter extends RecyclerView.Adapter<SocialPostAdapter.ViewHolder> {
    private Activity activity;
    private List<PostListResult> result;
    private String user_id;

    public SocialPostAdapter(Activity activity, List<PostListResult> result) {
        this.activity = activity;
        this.result = result;
        user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_username.setText(result.get(position).getUserDetails().getUserName());
        holder.txt_date.setText(result.get(position).getDateTime());
        holder.txt_ttl.setText(result.get(position).getTitle());
        holder.txt_price.setText(result.get(position).getPrice());
        holder.txt_view.setText(result.get(position).getTotalView());
        holder.txt_like.setText(result.get(position).getTotalLike());

        Picasso.with(activity).load(result.get(position).getUserDetails().getImage()).error(R.drawable.user2).into(holder.img_user);
        Picasso.with(activity).load(result.get(position).getImage()).error(R.drawable.not_found).into(holder.img_post);

        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ldata = getData(activity, "ldata", null);
                if (ldata != null) {
                    addLikeCall(result.get(position).getId(), holder.txt_like);
                } else {
                    Toast.makeText(activity, "Please Login..", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, Login.class));
                }
            }
        });

        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ldata = getData(activity, "ldata", null);
                if (ldata != null) {
                    addViewCall(result.get(position).getId(), holder.txt_view);
                } else {
                    Toast.makeText(activity, "Please Login..", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, Login.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img_user;
        TextView txt_username, txt_date, txt_ttl, txt_price, txt_view, txt_like;
        ImageView img_post, img_like, img_view;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_user = itemView.findViewById(R.id.img_user);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_ttl = itemView.findViewById(R.id.txt_ttl);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_view = itemView.findViewById(R.id.txt_view);
            txt_like = itemView.findViewById(R.id.txt_like);
            img_post = itemView.findViewById(R.id.img_post);
            img_like = itemView.findViewById(R.id.img_like);
            img_view = itemView.findViewById(R.id.img_view);
        }

        @Override
        public void onClick(View view) {

        }
    }


    private void addLikeCall(String product_id, final TextView txt_like) {
        Call<ResponseBody> call = AppConfig.loadInterface().addLikeSocialPost(user_id, product_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            txt_like.setText("" + object.getString("total_like"));
                            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, " Not Found", Toast.LENGTH_SHORT).show();
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

    private void addViewCall(String product_id, final TextView txt_view) {
        Call<ResponseBody> call = AppConfig.loadInterface().addViewSocialPost(user_id, product_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                            txt_view.setText(""+object.getString("total_view"));
                        } else {
                            Toast.makeText(activity, " Not Found", Toast.LENGTH_SHORT).show();
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