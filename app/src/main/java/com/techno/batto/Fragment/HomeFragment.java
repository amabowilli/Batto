package com.techno.batto.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Activity.Filter;
import com.techno.batto.Activity.Login;
import com.techno.batto.Activity.Notification;
import com.techno.batto.Activity.Search;
import com.techno.batto.Adapter.CategoryAdapter;
import com.techno.batto.Adapter.ProductsAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.App.GPSTracker;
import com.techno.batto.R;
import com.techno.batto.Response.CategoryResponse;
import com.techno.batto.Response.ProductListResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Adapter.CategoryAdapter.product_id;
import static com.techno.batto.Bean.MySharedPref.getData;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView home_cat_recycler_view;
    private View view;
    private ImageView home_search_image, noti_image, filter_image;
    public static RecyclerView Rvproduct;
    public static StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private SwipeRefreshLayout swipeLayout;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private CardView lay_head;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment_lay, container, false);
        findId();
        home_search_image.setOnClickListener(this);
        noti_image.setOnClickListener(this);
        filter_image.setOnClickListener(this);
        getCategoryListCall();

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

//        Rvproduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    lay_head.setVisibility(View.GONE);
//                } else {
//                    lay_head.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    // Do something
//                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    // Do something
//                } else {
//                    // Do something
//                }
//            }
//        });

        return view;
    }

    private void findId() {
        swipeLayout = view.findViewById(R.id.swipe_container);
        Rvproduct = view.findViewById(R.id.Rvproduct);
        home_cat_recycler_view = view.findViewById(R.id.home_cat_recycler_view);
        home_search_image = view.findViewById(R.id.home_search_image);
        noti_image = view.findViewById(R.id.noti_image);
        filter_image = view.findViewById(R.id.filter_image);
        lay_head = view.findViewById(R.id.lay_head);
    }


    @Override
    public void onClick(View view) {
        if (view == home_search_image) {
            startActivity(new Intent(getActivity(), Search.class));
        } else if (view == noti_image) {
            String ldata = getData(getActivity(), "ldata", null);
            if (ldata != null) {
                startActivity(new Intent(getActivity(), Notification.class));
            } else {
                Toast.makeText(getActivity(), "Please Login..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Login.class));
            }
        } else if (view == filter_image) {
            startActivity(new Intent(getActivity(), Filter.class));
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
                            CategoryAdapter adaper = new CategoryAdapter(getActivity(), categoryResponse.getResult());
                            home_cat_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            home_cat_recycler_view.setAdapter(adaper);
                            adaper.notifyDataSetChanged();
                            if (getActivity() != null) {
                                GPSTracker track = new GPSTracker(getActivity());
                                if (track.canGetLocation()) {
                                    latitude = track.getLatitude();
                                    longitude = track.getLongitude();


                                    latitude = 32.7347699;
                                    longitude = -96.5582632;

                                    getProductListCall("");
                                } else {
                                    track.showSettingsAlert();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Category Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getProductListCall(String id) {
        Call<ResponseBody> call = AppConfig.loadInterface().get_product(id, "" + latitude, "" + longitude);
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
                            ProductListResponse categoryResponse = gson.fromJson(responseData, ProductListResponse.class);
                            Rvproduct.setHasFixedSize(true);
                            gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
                            Rvproduct.setLayoutManager(gaggeredGridLayoutManager);
                            ProductsAdapter mainadapter = new ProductsAdapter(getActivity(), categoryResponse.getResult());
                            Rvproduct.setAdapter(mainadapter);
                            swipeLayout.setRefreshing(false);

                        } else {
                            swipeLayout.setRefreshing(false);
                            //   Toast.makeText(getActivity(), "Category Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        GPSTracker track = new GPSTracker(getActivity());
        if (track.canGetLocation()) {
            latitude = track.getLatitude();
            longitude = track.getLongitude();
            getProductListCall(product_id);
        } else {
            track.showSettingsAlert();
        }
    }
}
