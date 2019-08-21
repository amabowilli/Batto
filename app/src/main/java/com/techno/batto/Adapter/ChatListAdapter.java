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
import com.techno.batto.Activity.ChatActivity;
import com.techno.batto.Activity.IntrestedUserListActivity;
import com.techno.batto.Activity.ProductDetailsActivity;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Response.ChatListResponse;
import com.techno.batto.Result.ChatListResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private View view;
    private List<ChatListResult> result;
    private String user_id, request_id, type;
    private RecyclerView recyclerView;

    public ChatListAdapter(Activity activity, List<ChatListResult> result, String type, RecyclerView recyclerView) {
        this.activity = activity;
        this.result = result;
        this.type = type;
        this.recyclerView = recyclerView;
        user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (result.get(position).getUserDetails().getUserName().equalsIgnoreCase("")) {
            holder.txt_name.setText(result.get(position).getUserDetails().getFirstName() + " " + result.get(position).getUserDetails().getLastName());
        } else {
            holder.txt_name.setText(result.get(position).getUserDetails().getUserName());
        }
        holder.txt_msg.setText(result.get(position).getChatMessage());

        Picasso.with(activity).load(result.get(position).getUserDetails().getImage()).error(R.drawable.user).into(holder.img_user);

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PrettyDialog pDialog = new PrettyDialog(activity);
                pDialog.setTitle("What do you Want??")
                        .setIcon(R.drawable.report)
                        .setMessage("Are sure do you want to delete this!!!")
                        .addButton("YES", R.color.pdlg_color_white, R.color.pdlg_color_blue,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                        deleteChat(user_id,result.get(position).getUserDetails().getId(), result.get(position).getProductId());
                                    }
                                }
                        )
                        .addButton("NO", R.color.pdlg_color_white, R.color.pdlg_color_red,
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
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img_user;
        TextView txt_name, txt_msg;
        ImageView img_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_user = itemView.findViewById(R.id.img_user);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_msg = itemView.findViewById(R.id.txt_msg);
            img_delete = itemView.findViewById(R.id.img_delete);

        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(activity, ChatActivity.class);
            i.putExtra("reciver_id", "" + result.get(getAdapterPosition()).getUserDetails().getId());
            i.putExtra("product_id", "" + result.get(getAdapterPosition()).getProductId());
            activity.startActivity(i);
        }
    }


    private void deleteChat(String receiver_id, String sender_id, String product_id) {
        Call<ResponseBody> call = AppConfig.loadInterface().delete_chat(receiver_id, sender_id, product_id);
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
                            getChatCall(user_id);

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

    private void getChatCall(String id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call;
        if (type.equals("Buying")) {
            call = AppConfig.loadInterface().getBuingConversetion(id);
        } else {
            call = AppConfig.loadInterface().getSalingConversetion(id);
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
                            ChatListResponse requestListResponse = gson.fromJson(responseData, ChatListResponse.class);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            ChatListAdapter adapter = new ChatListAdapter(activity, requestListResponse.getResult(), type, recyclerView);
                            recyclerView.setAdapter(adapter);
                        } else {
                            // Toast.makeText(getActivity(), "Request Not Found", Toast.LENGTH_SHORT).show();
                            List<ChatListResult> result = new ArrayList<>();
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            ChatListAdapter adapter = new ChatListAdapter(activity, result, type, recyclerView);
                            recyclerView.setAdapter(adapter);
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
