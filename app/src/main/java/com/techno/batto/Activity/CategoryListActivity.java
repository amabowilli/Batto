package com.techno.batto.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.techno.batto.App.AppConfig;
import com.techno.batto.Bean.ExpandableHeightGridView;
import com.techno.batto.R;
import com.techno.batto.Response.CategoryResponse;
import com.techno.batto.Result.CategoryResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Activity.AddProductActivity.cat_name;
import static com.techno.batto.Activity.AddProductActivity.category_name;
import static com.techno.batto.Activity.AddProductActivity.category;
import static com.techno.batto.Activity.EditProductDetailsActivity.cat_name1;
import static com.techno.batto.Activity.EditProductDetailsActivity.category_name1;
import static com.techno.batto.Activity.EditProductDetailsActivity.category1;

public class CategoryListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back;
    private ExpandableHeightGridView cat_grid_view;
    private int si = -1;
    public static Activity catActivity;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        type = getIntent().getExtras().getString("type");
        findId();
        catActivity = this;
        img_back.setOnClickListener(this);
        getCategoryListCall();
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
        cat_grid_view = findViewById(R.id.cat_grid_view);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        }
    }


    private void getCategoryListCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().get_category();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {

                            Gson gson = new Gson();
                            CategoryResponse categoryResponse = gson.fromJson(responseData, CategoryResponse.class);
                            cat_grid_view.setExpanded(true);
                            CategorysAdapter adaper = new CategorysAdapter(getApplicationContext(), categoryResponse.getResult());
                            cat_grid_view.setAdapter(adaper);

                        } else {
                            Toast.makeText(CategoryListActivity.this, " Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CategoryListActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class CategorysAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        List<CategoryResult> singleCategoryLists;
        private Context context;
        View view;

        public CategorysAdapter(Context context, List<CategoryResult> singleCategoryLists) {
            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.singleCategoryLists = singleCategoryLists;
        }

        @Override
        public int getCount() {
            return singleCategoryLists.size();

        }

        @Override
        public Object getItem(int position) {
            return position;

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final CategorysAdapter.ViewHolder spinnerHolder;
            if (convertView == null) {
                spinnerHolder = new CategorysAdapter.ViewHolder();
                convertView = layoutInflater.inflate(R.layout.filter_cat_pojo_lay, viewGroup, false);

                spinnerHolder.home_cat_image_pojo = convertView.findViewById(R.id.home_cat_image_pojo);
                spinnerHolder.home_cat_name_pojo = convertView.findViewById(R.id.home_cat_name_pojo);
                spinnerHolder.lay_main = convertView.findViewById(R.id.lay_main);

                spinnerHolder.home_cat_name_pojo.setText(singleCategoryLists.get(position).getCategoryName());

                Picasso.with(context).load(singleCategoryLists.get(position).getImage()).error(R.drawable.not_found).into(spinnerHolder.home_cat_image_pojo);

                if (si == position)
                    spinnerHolder.lay_main.setBackgroundColor(Color.parseColor("#f5bd04"));
                else
                    spinnerHolder.lay_main.setBackgroundColor(Color.parseColor("#FFFFFF"));


                spinnerHolder.lay_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "" + singleCategoryLists.get(position).getCategoryName(), Toast.LENGTH_SHORT).show();
                        si = position;
                        notifyDataSetChanged();
                        if (type.equals("Add")) {
                            category = singleCategoryLists.get(position).getId();
                            category_name = "" + singleCategoryLists.get(position).getCategoryName();
                            Intent i = new Intent(CategoryListActivity.this, SubCategortActivity.class);
                            cat_name.setText("" + category_name);
                            i.putExtra("cat_id", "" + category);
                            i.putExtra("type", "" + type);
                            startActivity(i);
                        }else {
                            category1 = singleCategoryLists.get(position).getId();
                            category_name1 = "" + singleCategoryLists.get(position).getCategoryName();
                            Intent i = new Intent(CategoryListActivity.this, SubCategortActivity.class);
                            cat_name1.setText("" + category_name1);
                            i.putExtra("cat_id", "" + category);
                            i.putExtra("type", "" + type);
                            startActivity(i);
                        }
                        //spinnerHolder.lay_main.setBackgroundColor(Color.parseColor("#f5bd04"));
                    }
                });
            } else {
                spinnerHolder = (CategorysAdapter.ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        class ViewHolder {
            CircleImageView home_cat_image_pojo;
            TextView home_cat_name_pojo;
            LinearLayout lay_main;
        }
    }
}
