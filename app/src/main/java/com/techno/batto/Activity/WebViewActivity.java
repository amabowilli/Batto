package com.techno.batto.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.techno.batto.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back;
    private String ttl, url;
    private TextView txt_ttl;
    private WebView my_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        findId();
        ttl = getIntent().getExtras().getString("ttl");
        url = getIntent().getExtras().getString("url");
        txt_ttl.setText(ttl);
        img_back.setOnClickListener(this);
        my_web.setWebViewClient(new WebViewClient());
        my_web.loadUrl(url);
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
        txt_ttl = findViewById(R.id.txt_ttl);
        my_web = findViewById(R.id.my_web);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        }
    }
}
