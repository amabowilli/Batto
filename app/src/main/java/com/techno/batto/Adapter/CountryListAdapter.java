package com.techno.batto.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techno.batto.R;
import com.techno.batto.Result.CountryResult;

import java.util.List;

import static com.techno.batto.Activity.AddProductActivity.currency_name;
import static com.techno.batto.Activity.AddProductActivity.currency_type;
import static com.techno.batto.Activity.EditProductDetailsActivity.currency_name1;
import static com.techno.batto.Activity.EditProductDetailsActivity.currency_type1;
import static com.techno.batto.Bean.MySharedPref.getData;


public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {
    private Activity activity;
    private View view;
    private List<CountryResult> result;
    private String user_id, request_id, type;

    public CountryListAdapter(Activity activity, List<CountryResult> result, String type) {
        this.activity = activity;
        this.result = result;
        this.type = type;
        Log.e("----->", "" + result.size());
        user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_item, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt_name.setText(result.get(position).getName());
        holder.txt_curreency.setText(result.get(position).getCurrency());
        Picasso.with(activity).load(result.get(position).getImage()).error(R.drawable.user).into(holder.img_user);

    }

    @Override
    public int getItemCount() {
        return result.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_user;
        TextView txt_name, txt_curreency;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_user = itemView.findViewById(R.id.img_user);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_curreency = itemView.findViewById(R.id.txt_curreency);

        }

        @Override
        public void onClick(View view) {
            if (type.equals("Add")) {
                currency_type = result.get(getAdapterPosition()).getCurrency();
                currency_name.setText(result.get(getAdapterPosition()).getCurrency());
            } else {
                currency_type1 = result.get(getAdapterPosition()).getCurrency();
                currency_name1.setText(result.get(getAdapterPosition()).getCurrency());
            }
            activity.finish();
        }
    }

}
