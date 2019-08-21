package com.techno.batto.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techno.batto.Activity.ViewUserProfileActivity;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Result.FFResult;

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

public class FollowerFollowingAdapter extends RecyclerView.Adapter<FollowerFollowingAdapter.ViewHolder> {
    private Activity activity;
    private List<FFResult> result;
    private String user_id, x;

    public FollowerFollowingAdapter(Activity activity, List<FFResult> result, String x) {
        this.activity = activity;
        this.result = result;
        this.x = x;
        user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_username.setText(result.get(position).getUserDetail().getUserName());
        holder.btn_follow.setText("" + x);
        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ldata = getData(activity, "ldata", null);
                if (ldata != null) {
                    followCall(result.get(position).getUserDetail().getId(), holder.btn_follow);
                } else {
                    Toast.makeText(activity, "Please Login!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Picasso.with(activity).load(result.get(position).getUserDetail().getImage()).error(R.drawable.user).into(holder.img_user);
        holder.txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ldata = getData(activity, "ldata", null);
                if (ldata != null) {
                    Intent i = new Intent(activity, ViewUserProfileActivity.class);
                    i.putExtra("user_id", result.get(position).getUserDetail().getId());
                    activity.startActivity(i);
                } else {
                    Toast.makeText(activity, "Please Login!!!", Toast.LENGTH_SHORT).show();
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
        TextView txt_username, txt_view;
        Button btn_follow;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_user = itemView.findViewById(R.id.img_user);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_view = itemView.findViewById(R.id.txt_view);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private void followCall(String pro_user_id, final Button btn) {
        Call<ResponseBody> call = AppConfig.loadInterface().addFollow(user_id, pro_user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("product Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            if (object.getString("message").equals("successful")) {
                                btn.setText("Requested");
                            } else {
                                btn.setText("Follow");
                            }
                        } else {
                            Toast.makeText(activity, "Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("----", "Not Found");
                    }
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