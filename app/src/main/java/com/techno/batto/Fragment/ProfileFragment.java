package com.techno.batto.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.techno.batto.Activity.EditProfile;
import com.techno.batto.Activity.FollowerFollowingActivity;
import com.techno.batto.Activity.MyPostListActivity;
import com.techno.batto.Activity.Setting;
import com.techno.batto.Bean.AlertConnection;
import com.techno.batto.Bean.ConnectionDetector;
import com.techno.batto.Bean.MySharedPref;
import com.techno.batto.Interface.Config;
import com.techno.batto.Interface.UserInterface;
import com.techno.batto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.techno.batto.Bean.MySharedPref.getData;

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView edit_profile_image, setting_image, img_fb, img_google, img_email;
    MySharedPref sp;
    String user_id, logid, email, mobile, user_name, total_posts, total_followers, total_following, verify_with, image, bio, gender, website;
    float rating;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ProgressDialog progressDialog;
    CircleImageView profile_image;
    TextView txt_rating, profile_name_text, profile_post_text, profile_followers_text, profile_following_text;
    RatingBar profile_rating;

    @SuppressLint("ValidFragment")
    public ProfileFragment(String user_id) {
        logid = user_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment_layout, container, false);

        //--------------------- connection detector -----------------------------------

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        super.onStart();

        user_id = getData(getActivity(), "user_id", "null");

        viewPager = (ViewPager) view.findViewById(R.id.viewpager_profile);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_profile);
        edit_profile_image = view.findViewById(R.id.edit_profile_image);
        setting_image = view.findViewById(R.id.setting_image);
        profile_image = view.findViewById(R.id.profile_image);
        profile_name_text = view.findViewById(R.id.profile_name_text);
        profile_rating = view.findViewById(R.id.profile_rating);
        profile_post_text = view.findViewById(R.id.profile_post_text);
        profile_followers_text = view.findViewById(R.id.profile_followers_text);
        profile_following_text = view.findViewById(R.id.profile_following_text);
        txt_rating = view.findViewById(R.id.txt_rating);
        img_fb = view.findViewById(R.id.img_fb);
        img_google = view.findViewById(R.id.img_google);
        img_email = view.findViewById(R.id.img_email);

        img_fb.setOnClickListener(this);
        img_google.setOnClickListener(this);
        img_email.setOnClickListener(this);
        //------------------------------------ get profile --------------------------------

        if (isInternetPresent) {
            GetProfile_call();
        } else {
            AlertConnection.showAlertDialog(getActivity(), "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        if (user_id.equals(logid)) {
            setting_image.setVisibility(View.VISIBLE);
            edit_profile_image.setVisibility(View.VISIBLE);
        } else {
            setting_image.setVisibility(View.GONE);
            edit_profile_image.setVisibility(View.GONE);
        }

        tabLayout.addTab(tabLayout.newTab().setText("Selling"));
        tabLayout.addTab(tabLayout.newTab().setText("Sold"));
        tabLayout.addTab(tabLayout.newTab().setText("Favourites"));
        tabLayout.addTab(tabLayout.newTab().setText("Rating & Feedback"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Creating our pager adapter
        Pager adapter = new Pager(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

        edit_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });

        profile_followers_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FollowerFollowingActivity.class);
                i.putExtra("type", "Followers");
                startActivity(i);
            }
        });

        profile_post_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyPostListActivity.class);
                i.putExtra("user_id", "" + user_id);
                startActivity(i);
            }
        });

        profile_following_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FollowerFollowingActivity.class);
                i.putExtra("type", "Followings");
                startActivity(i);
            }
        });

        return view;
    }

    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }


    public void onTabUnselected(TabLayout.Tab tab) {

    }


    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        if (view == img_fb) {

        } else if (view == img_google) {

        } else if (view == img_email) {

        }
    }

    public class Pager extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount = tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    SellingProfileFragment tab1 = new SellingProfileFragment(logid);
                    return tab1;
                case 1:
                    SoldFragment tab3 = new SoldFragment(logid);
                    return tab3;
                case 2:
                    FavouritiesFragment tab4 = new FavouritiesFragment(logid);
                    return tab4;
                case 3:
                    ExchangesFragment tab2 = new ExchangesFragment(logid);
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    //------------------------------------ get profile call -----------------------------------

    private void GetProfile_call() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.Base_Url).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        UserInterface signupInterface = retrofit.create(UserInterface.class);
        Call<ResponseBody> resultCall = signupInterface.get_profile(logid);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        Log.e("get profile response** ", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");

                        if (error.equals("1")) {

                            JSONObject jsonObject = object.getJSONObject("result");
                            user_name = jsonObject.getString("user_name");
                            mobile = jsonObject.getString("mobile");
                            email = jsonObject.getString("email");
                            image = jsonObject.getString("image");
                            verify_with = jsonObject.getString("verify_with");
                            website = jsonObject.getString("website");
                            bio = jsonObject.getString("bio");
                            gender = jsonObject.getString("gender");
                            rating = Float.parseFloat(jsonObject.getString("rating"));
                            total_posts = jsonObject.getString("total_posts");
                            total_followers = jsonObject.getString("total_followers");
                            total_following = jsonObject.getString("total_following");

                            // Glide.with(getActivity()).load(image).into(profile_image);
                            Picasso.with(getActivity()).load(image).error(R.drawable.user2).into(profile_image);
                            profile_name_text.setText(user_name);
                            profile_rating.setRating(rating);
                            txt_rating.setText("(" + rating + ")");
                            profile_post_text.setText(String.valueOf(total_posts) + " Post");
                            profile_followers_text.setText(String.valueOf(total_followers) + " Followers");
                            profile_following_text.setText(String.valueOf(total_following) + " Following");
                            if (verify_with.equals("Email")) {
                                Picasso.with(getActivity()).load(R.drawable.vmsg_red).error(R.drawable.user).into(img_email);
                            } else if (verify_with.equals("Facebook")) {
                                Picasso.with(getActivity()).load(R.drawable.vfb_red).error(R.drawable.user).into(img_fb);
                            } else if (verify_with.equals("Google")) {
                                Picasso.with(getActivity()).load(R.drawable.vggl_red).error(R.drawable.user).into(img_google);
                            }
                        } else {
                            String message = object.getString("message");
                            Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("hgdhgfgdf", "dtrdfuydrfgjhjjfyt");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Server Problem Please try Next time...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
