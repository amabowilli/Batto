package com.techno.batto.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.techno.batto.Fragment.ProfileFragment;
import com.techno.batto.R;

public class ViewUserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private String user_id;
    private static FragmentManager fm;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);
        findId();
        user_id = getIntent().getExtras().getString("user_id");
        fm = getSupportFragmentManager();
        addFragment(new ProfileFragment(user_id), false);
        img_back.setOnClickListener(this);
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
    }


    public static void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_containera, fragment, "");
        //if (!tag.equals("Home"))
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        }
    }
}
