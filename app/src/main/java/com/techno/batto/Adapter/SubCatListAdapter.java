package com.techno.batto.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techno.batto.R;
import com.techno.batto.Result.SubCatResult;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.techno.batto.Activity.AddProductActivity.cat_name;
import static com.techno.batto.Activity.AddProductActivity.category_name;
import static com.techno.batto.Activity.CategoryListActivity.catActivity;
import static com.techno.batto.Activity.EditProductDetailsActivity.cat_name1;
import static com.techno.batto.Activity.EditProductDetailsActivity.category_name1;
import static com.techno.batto.Bean.MySharedPref.getData;


public class SubCatListAdapter extends RecyclerView.Adapter<SubCatListAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private View view;
    List<SubCatResult> result;
    private String user_id, request_id, type;

    public SubCatListAdapter(Activity activity, List<SubCatResult> result, String type) {
        this.activity = activity;
        this.result = result;
        this.type = type;
        user_id = getData(activity, "user_id", "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_cat_item_view, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_catname.setText(result.get(position).getName());

        Picasso.with(activity).load(result.get(position).getImage()).error(R.drawable.user).into(holder.img_cat);

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

//            Intent i = new Intent(activity, SecondSubCategoryListActivity.class);
//            i.putExtra("sub_cat_id", "" + result.get(getAdapterPosition()).getId());
//            activity.startActivity(i);

            if (type.equals("Add")) {
                category_name = category_name + " , " + result.get(getAdapterPosition()).getName();
                cat_name.setText("" + category_name);
                catActivity.finish();
                activity.finish();
            } else {
                category_name1 = category_name1 + " , " + result.get(getAdapterPosition()).getName();
                cat_name1.setText("" + category_name1);
                catActivity.finish();
                activity.finish();
            }
        }
    }

}
