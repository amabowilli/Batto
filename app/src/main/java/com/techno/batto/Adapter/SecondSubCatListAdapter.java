package com.techno.batto.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techno.batto.Activity.GetCatTextActivity;
import com.techno.batto.R;
import com.techno.batto.Result.ChildCatResult;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.techno.batto.Bean.MySharedPref.getData;


public class SecondSubCatListAdapter extends RecyclerView.Adapter<SecondSubCatListAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private View view;
    List<ChildCatResult> result;
    private String user_id, request_id;

    public SecondSubCatListAdapter(Activity activity, List<ChildCatResult> result) {
        this.activity = activity;
        this.result = result;
        user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_cat_item_view, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_catname.setText(result.get(position).getName());

    }
    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img_cat;
        TextView txt_catname;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_cat = itemView.findViewById(R.id.img_cat);
            txt_catname = itemView.findViewById(R.id.txt_catname);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(activity, GetCatTextActivity.class);
            i.putExtra("child_cat_id", "" + result.get(getAdapterPosition()).getId());
            activity.startActivity(i);
        }
    }

}
