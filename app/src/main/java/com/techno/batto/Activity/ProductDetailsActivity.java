package com.techno.batto.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.techno.batto.Adapter.ChatListAdapter;
import com.techno.batto.Adapter.ImageListAdapter;
import com.techno.batto.Adapter.MyCustomPagerAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private ImageView img_back;
    ViewPager viewPager;
    public static List<String> images1;
    MyCustomPagerAdapter myCustomPagerAdapter;
    private TextView txt_price, txt_discription, txt_name, txt_username, txt_edittime, txt_address, txt_viewcount, txt_like;
    private CircleImageView img_user;
    private LinearLayout lay_details, lay_exchange, lay_share, lay_review, lay_like, lay_view, lay_report;
    private JSONObject obj;
    private Double lat, lon;
    private String snumber, pro_user_id, user_id, product_id, product_name, price, image_url;
    private TextView txt_extra, txt_extra_one, txt_catname, txt_exwith, txt_view;
    private Marker currentMarker;
    private GoogleMap mMap;
    private Button btn_follow;
    private FloatingActionButton fab;
    private RatingBar profile_rating;
    private TextView txt_rating;
    private RecyclerView Rvimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        product_id = getIntent().getExtras().getString("product_id");
        // product_id = "67";
        Log.e("product_id-->", "---->" + product_id);

        findId();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        images1 = new ArrayList<>();
        img_back.setOnClickListener(this);
        lay_details.setOnClickListener(this);
        btn_follow.setOnClickListener(this);
        txt_view.setOnClickListener(this);

        getProductDetailCall(product_id);

        lay_review.setOnClickListener(this);
        lay_share.setOnClickListener(this);
        fab.setOnClickListener(this);
        lay_like.setOnClickListener(this);
        lay_view.setOnClickListener(this);
        lay_report.setOnClickListener(this);

    }

    private void loadData(JSONObject obj) {
        try {
            Picasso.with(this).load(obj.getString("image")).error(R.drawable.user).into(img_user);
            snumber = obj.getString("mobile");
            pro_user_id = obj.getString("id");
            //  rating_bar.setRating(Float.parseFloat(obj.getString("rating")));
            txt_username.setText(obj.getString("user_name"));
            Float rating = Float.parseFloat(obj.getString("rating"));
            profile_rating.setRating(rating);
            txt_rating.setText("(" + rating + ")");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        txt_address = findViewById(R.id.txt_address);
        txt_exwith = findViewById(R.id.txt_exwith);

        txt_extra_one = findViewById(R.id.txt_extra_one);
        txt_extra = findViewById(R.id.txt_extra);
        txt_price = findViewById(R.id.txt_price);
        txt_discription = findViewById(R.id.txt_discription);
        txt_name = findViewById(R.id.txt_name);
        txt_catname = findViewById(R.id.txt_catname);
        txt_username = findViewById(R.id.txt_username);
        img_user = findViewById(R.id.img_user);
        //   rating_bar = findViewById(R.id.rating_bar);
        txt_edittime = findViewById(R.id.txt_edittime);
        lay_details = findViewById(R.id.lay_details);
        lay_exchange = findViewById(R.id.lay_exchange);
        btn_follow = findViewById(R.id.btn_follow);
        txt_view = findViewById(R.id.txt_view);

        lay_share = findViewById(R.id.lay_share);
        lay_review = findViewById(R.id.lay_review);

        fab = findViewById(R.id.fab);

        lay_like = findViewById(R.id.lay_like);
        lay_view = findViewById(R.id.lay_view);

        txt_like = findViewById(R.id.txt_like);
        txt_viewcount = findViewById(R.id.txt_viewcount);
        lay_report = findViewById(R.id.lay_report);
        profile_rating = findViewById(R.id.profile_rating);
        txt_rating = findViewById(R.id.txt_rating);
        Rvimage = findViewById(R.id.Rvimage);
    }

//    String ldata = getData(this, "ldata", null);
//            if (ldata != null) {
//        Intent i = new Intent(Intent.ACTION_DIAL);
//        String p = "tel:" + snumber;
//        i.setData(Uri.parse(p));
//        startActivity(i);
//    } else {
//        Toast.makeText(this, "Please Login!!!", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onClick(View v) {
        if (v == img_back) {
            finish();
        } else if (v == btn_follow) {
            String ldata = getData(this, "ldata", null);
            if (ldata != null) {
                followCall();
            } else {
                Toast.makeText(this, "Please Login!!!", Toast.LENGTH_SHORT).show();
            }
        } else if (v == txt_view) {
            Intent i = new Intent(ProductDetailsActivity.this, ViewUserProfileActivity.class);
            i.putExtra("user_id", pro_user_id);
            startActivity(i);
        } else if (v == lay_share) {
            shareOnWhatsapp();
        } else if (v == fab) {
            String ldata = getData(this, "ldata", null);
            if (ldata != null) {
                user_id = getData(this, "user_id", null);
                addIntreast();
            } else {
                Toast.makeText(this, "Please Login!!!", Toast.LENGTH_SHORT).show();
            }
        } else if (v == lay_like) {
            String ldata = getData(this, "ldata", null);
            if (ldata != null) {
                user_id = getData(this, "user_id", null);
                addToFavCall();
            } else {
                Toast.makeText(this, "Please Login!!!", Toast.LENGTH_SHORT).show();
            }
        } else if (v == lay_view) {
            String ldata = getData(this, "ldata", null);
            if (ldata != null) {
                user_id = getData(this, "user_id", null);
                addToViewCall();
            } else {
                Toast.makeText(this, "Please Login!!!", Toast.LENGTH_SHORT).show();
            }
        } else if (v == lay_review) {
            String ldata = getData(this, "ldata", null);
            if (ldata != null) {
                if (user_id.equals(pro_user_id)) {
                    Toast.makeText(this, "You can't review own product!!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(ProductDetailsActivity.this, AddReviewActivity.class);
                    i.putExtra("product_id", product_id);
                    i.putExtra("pro_user_id", pro_user_id);
                    startActivity(i);
                }
            } else {
                Toast.makeText(this, "Please Login!!!", Toast.LENGTH_SHORT).show();
            }
        } else if (v == lay_report) {
            String ldata = getData(this, "ldata", null);
            if (ldata != null) {
                if (user_id.equals(pro_user_id)) {
                    Toast.makeText(this, "You can't report own product!!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(ProductDetailsActivity.this, ReportActivity.class);
                    i.putExtra("product_id", product_id);
                    startActivity(i);
                }
            } else {
                Toast.makeText(this, "Please Login!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareOnWhatsapp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi, I'm buying " + product_name + " only in $" + price + "  are you interested in that? " + image_url);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    private void getProductDetailCall(String id) {
        Call<ResponseBody> call = AppConfig.loadInterface().get_productDetails(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("product Data :- " + object);
                        if (object.getString("status").equals("1")) {

                            if (object != null) {
                                Log.e("data--->>", "" + object);
                                try {
                                    obj = object.getJSONObject("result");
                                    String img1, img2, img3, img4;
                                    // image_url = obj.getString("image");
                                    img1 = obj.getString("image1");
                                    img2 = obj.getString("image2");
                                    img3 = obj.getString("image3");
                                    img4 = obj.getString("image4");
                                    if (img1.equalsIgnoreCase("http://52.47.70.91//uploads/images/")) {
                                    } else {
                                        images1.add(img1);
                                    }

                                    if (img2.equalsIgnoreCase("http://52.47.70.91//uploads/images/")) {

                                    } else {
                                        images1.add(img2);
                                    }

                                    if (img3.equalsIgnoreCase("http://52.47.70.91//uploads/images/")) {

                                    } else {
                                        images1.add(img3);
                                    }

                                    if (img4.equalsIgnoreCase("http://52.47.70.91//uploads/images/")) {

                                    } else {
                                        images1.add(img4);
                                    }
                                    user_id = getData(ProductDetailsActivity.this, "user_id", null);
                                    if (user_id != null) {
                                        if (obj.getString("user_id").equals(user_id)) {
                                            fab.setVisibility(View.GONE);
                                        }
                                    }
                                    product_id = obj.getString("id");
                                    product_name = obj.getString("name");
                                    price = obj.getString("price");
                                    txt_extra.setText("Negotiable: " + obj.getString("negotiable"));
                                    txt_extra_one.setText("Exchange: " + obj.getString("exchange"));
                                    txt_name.setText(obj.getString("name"));
                                    txt_catname.setText(obj.getString("category_name"));
                                    txt_exwith.setText(obj.getString("exchange_with"));
                                    txt_price.setText(obj.getString("price") + " " + obj.getString("currency"));
                                    txt_discription.setText(obj.getString("description"));
                                    txt_edittime.setText("Edit at " + obj.getString("date_time"));
                                    txt_address.setText(" " + obj.getString("address"));
                                    txt_viewcount.setText(" " + obj.getString("total_view"));
                                    txt_like.setText(" " + obj.getString("total_like"));
                                    if (obj.getString("exchange").equals("YES")) {
                                        lay_exchange.setVisibility(View.VISIBLE);
                                    } else {
                                        lay_exchange.setVisibility(View.GONE);
                                    }

                                    obj = obj.getJSONObject("user_details");
                                    loadData(obj);
                                    try {
                                        lat = Double.parseDouble(obj.getString("lat"));
                                        lon = Double.parseDouble(obj.getString("lon"));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        lat = 0.0;
                                        lon = 0.0;
                                    }

                                    final LatLng latLng = new LatLng(lat, lon);
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 70, false);
                                    if (currentMarker != null) {
                                        currentMarker.remove();
                                    }
                                    currentMarker = mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .draggable(true)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                            .title("Current Location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).tilt(45).zoom((float) 17.5).build();
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (images1 != null) {
                                myCustomPagerAdapter = new MyCustomPagerAdapter(ProductDetailsActivity.this, images1);
                                viewPager.setAdapter(myCustomPagerAdapter);

                                Rvimage.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
                                Rvimage.setLayoutManager(layoutManager);
                                Rvimage.setItemAnimator(new DefaultItemAnimator());
                                ImageListAdapter adapter = new ImageListAdapter(ProductDetailsActivity.this, images1);
                                Rvimage.setAdapter(adapter);
                            }

                            String ldata = getData(ProductDetailsActivity.this, "ldata", null);
                            if (ldata != null) {
                                try {
                                    JSONObject obj = new JSONObject(ldata);
                                    Log.e("--Login Data--", "" + obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProductDetailsActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void followCall() {
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
                                btn_follow.setText("Requested");
                            } else {
                                btn_follow.setText("Follow");
                            }
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProductDetailsActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToFavCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().addRemoveFav(user_id, product_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("product Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            txt_like.setText(object.getString("total_fav_produc"));
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProductDetailsActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToViewCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().addRemoveView(user_id, product_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("product Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            txt_viewcount.setText(object.getString("total_view_produc"));
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProductDetailsActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addIntreast() {
        Call<ResponseBody> call = AppConfig.loadInterface().addIntrestedUser(user_id, product_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println(" Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            Intent i = new Intent(ProductDetailsActivity.this, ChatActivity.class);
                            i.putExtra("reciver_id", "" + pro_user_id);
                            i.putExtra("product_id", "" + product_id);
                            startActivity(i);
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProductDetailsActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
