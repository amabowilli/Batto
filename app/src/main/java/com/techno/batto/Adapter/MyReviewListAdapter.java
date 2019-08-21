package com.techno.batto.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techno.batto.R;
import com.techno.batto.Result.ReviewResult;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.techno.batto.Bean.MySharedPref.getData;


public class MyReviewListAdapter extends RecyclerView.Adapter<MyReviewListAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private View view;
    private List<ReviewResult> result;
    private String user_id, saler_id, request_id;

    public MyReviewListAdapter(Activity activity, String user_id, List<ReviewResult> result) {
        this.activity = activity;
        this.result = result;
        this.user_id = getData(activity, "user_id", "");
        this.saler_id = user_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (result.get(position).getUserDetails().getUserName().equalsIgnoreCase("")) {
            holder.txt_username.setText(result.get(position).getUserDetails().getFirstName() + " " + result.get(position).getUserDetails().getLastName());
        } else {
            holder.txt_username.setText(result.get(position).getUserDetails().getUserName());
        }
        Picasso.with(activity).load(result.get(position).getUserDetails().getImage()).error(R.drawable.not_found).into(holder.img_user);
        holder.txt_ttl.setText(result.get(position).getTitle());
        holder.txt_comment.setText(result.get(position).getComment());

        holder.ratingbarfeedback.setRating(Float.parseFloat(result.get(position).getRating()));


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
        RatingBar ratingbarfeedback;
        TextView txt_username, txt_ttl, txt_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_user = itemView.findViewById(R.id.img_user);
            ratingbarfeedback = itemView.findViewById(R.id.ratingbarfeedback);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_ttl = itemView.findViewById(R.id.txt_ttl);
            txt_comment = itemView.findViewById(R.id.txt_comment);

        }

        @Override
        public void onClick(View view) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
