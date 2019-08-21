package com.techno.batto.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.techno.batto.R;

public class OTPVerify extends AppCompatActivity {
    ImageView otp_back;
    Button otp_submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);

        otp_back = findViewById(R.id.otp_back);
        otp_submit_btn = findViewById(R.id.otp_submit_btn);

        otp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        otp_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });
    }
}
