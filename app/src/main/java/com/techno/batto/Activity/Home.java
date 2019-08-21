package com.techno.batto.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.techno.batto.Bean.BottomNavigationViewHelper;
import com.techno.batto.Fragment.ChatFragment;
import com.techno.batto.Fragment.HomeFragment;
import com.techno.batto.Fragment.ProfileFragment;
import com.techno.batto.Fragment.SocialFragment;
import com.techno.batto.R;

import static com.techno.batto.Bean.MySharedPref.getData;

public class Home extends AppCompatActivity {
    public static BottomNavigationView navigation;
    private static FragmentManager fm;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadFullAdd();
        //------------------------- bottom navigation code ------------------------------

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();
        addFragment(new HomeFragment(), false);
        navigation.setSelectedItemId(R.id.home_nav);
        String ldata = getData(getApplicationContext(), "ldata", null);

        mAdView = (AdView) findViewById(R.id.adView);
        //mAdView.setAdSize(AdSize.BANNER);
        //  mAdView.setAdUnitId(getString(R.string.BANNER_ID_1));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);


    }

    private void loadFullAdd() {
        final InterstitialAd mInterstitialAd = new InterstitialAd(Home.this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String ldata;
            switch (item.getItemId()) {
                case R.id.home_nav:
                    addFragment(new HomeFragment(), false);
                    return true;
                case R.id.social_nav:
                    addFragment(new SocialFragment(), false);
                    return true;
                case R.id.camera_nav:
                    // addFragment(new HomeFragment(), false);
                    ldata = getData(getApplicationContext(), "ldata", null);
                    if (ldata != null) {
                        startActivity(new Intent(Home.this, AddProductActivity.class));
                    } else {
                        Toast.makeText(Home.this, "Please Login..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this, Login.class));
                    }
                    return true;
                case R.id.chat_nav:
                    ldata = getData(getApplicationContext(), "ldata", null);
                    if (ldata != null) {
                        addFragment(new ChatFragment(), false);
                    } else {
                        Toast.makeText(Home.this, "Please Login..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this, Login.class));
                    }
                    return true;
                case R.id.profile_nav:
                    ldata = getData(getApplicationContext(), "ldata", null);
                    if (ldata != null) {
                        addFragment(new ProfileFragment(getData(getApplicationContext(), "user_id", "")), false);
                    } else {
                        Toast.makeText(Home.this, "Please Login..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this, Login.class));
                    }
                    return true;
            }
            return false;
        }
    };

    public static void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_container, fragment, "");
        //if (!tag.equals("Home"))
        try {
            if (addToBackStack)
                transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {

        }
    }
}
