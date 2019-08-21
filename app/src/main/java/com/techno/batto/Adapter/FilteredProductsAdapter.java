package com.techno.batto.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techno.batto.Activity.ProductDetailsActivity;
import com.techno.batto.R;
import com.techno.batto.Result.FilterLiestResult;

import java.util.List;

public class FilteredProductsAdapter extends RecyclerView.Adapter<FilteredProductsAdapter.ViewHolder> {
    private Activity activity;
    private List<FilterLiestResult> result;
    Handler handler = new Handler();

    public FilteredProductsAdapter(Activity activity, List<FilterLiestResult> result) {
        this.activity = activity;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        // Log.e("Value", "" + viewType % 2);
        if (viewType % 2 == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout_gv_adapter, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout_gv_adapter_two, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_name.setText(result.get(position).getName());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 1000);

        if (activity != null) {
            Log.e("Image--->", "------------> " + result.get(position).getImage1());
            if (result.get(position).getImage1().equalsIgnoreCase("")) {

            } else {
                Picasso.with(activity).load(result.get(position).getImage1()).error(R.drawable.img).into(holder.im_test);
                // Glide.with(activity).load(result.get(position).getImage1()).into(holder.im_test);
            }
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView im_test;
        TextView txt_name;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            im_test = itemView.findViewById(R.id.im_test);
            txt_name = itemView.findViewById(R.id.txt_name);

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


}
