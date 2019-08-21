package com.techno.batto.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;
import com.techno.batto.Result.IntrestedUserResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;


public class IntrestedUserListAdapter extends RecyclerView.Adapter<IntrestedUserListAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private View view;
    private List<IntrestedUserResult> result;
    private String user_id, product_id;

    public IntrestedUserListAdapter(Activity activity, List<IntrestedUserResult> result, String product_id) {
        this.activity = activity;
        this.result = result;
        this.product_id = product_id;
        user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.int_user_list_item, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_int_name.setText(result.get(position).getUserName());

//        Glide.with(activity)
//                .load(result.get(position).getImage())
//                .into(holder.img_user);

        Picasso.with(activity).load(result.get(position).getImage()).error(R.drawable.user).into(holder.img_user);

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
        TextView txt_int_name;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_user = itemView.findViewById(R.id.img_user);
            txt_int_name = itemView.findViewById(R.id.txt_int_name);

        }

        @Override
        public void onClick(View view) {
            final PrettyDialog pDialog = new PrettyDialog(activity);
            pDialog.setTitle("What do you Want??")
                    .setMessage("Are you sure do you want to sale it?")
                    .addButton("Yes", R.color.pdlg_color_white, R.color.pdlg_color_green,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                    saleProductCall(result.get(getAdapterPosition()).getId());
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
                    )
                    .show();
        }
    }


    private void saleProductCall(String user_id) {
        Call<ResponseBody> call = AppConfig.loadInterface().saleproduct(product_id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(activity, "Success!!!", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        } else {
                            Toast.makeText(activity, "Not Found", Toast.LENGTH_SHORT).show();
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
