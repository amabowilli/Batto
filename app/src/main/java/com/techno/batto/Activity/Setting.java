package com.techno.batto.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.techno.batto.R;

import static com.techno.batto.Bean.MySharedPref.saveData;

public class Setting extends AppCompatActivity {
    ImageView setting_back;
    RelativeLayout go_to_edit_profile_lay,lay_policy,lay_tnc;
    TextView logout_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(Setting.this);
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_setting);

        setting_back = findViewById(R.id.setting_back);
        lay_tnc = findViewById(R.id.lay_tnc);
        lay_policy = findViewById(R.id.lay_policy);
        go_to_edit_profile_lay = findViewById(R.id.go_to_edit_profile_lay);
        logout_text = findViewById(R.id.logout_text);

        lay_tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                i.putExtra("ttl","Terms & Conditions");
                i.putExtra("url","http://52.47.70.91/terms.html");
                startActivity(i);
            }
        });

        lay_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                i.putExtra("ttl","Privacy Policy");
                i.putExtra("url","http://52.47.70.91/Privacy.html");
                startActivity(i);
            }
        });

        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        go_to_edit_profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });

        logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Toast.makeText(Setting.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), Login.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                saveData(getApplicationContext(), "ldata", null);
                saveData(getApplicationContext(), "user_id", null);

            }
        });
    }
}
