package com.techno.batto.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.techno.batto.R;

import static com.techno.batto.Activity.CategoryListActivity.catActivity;
import static com.techno.batto.Activity.SecondSubCategoryListActivity.childCatActivity;
import static com.techno.batto.Activity.SubCategortActivity.subcatAcivity;

public class GetCatTextActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back;
    private Button btn_submit;
    private String child_cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cat_text);
        findID();
        child_cat_id = getIntent().getExtras().getString("child_cat_id");
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private void findID() {
        img_back = findViewById(R.id.img_back);
        btn_submit = findViewById(R.id.btn_submit);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        } else if (view == btn_submit) {
            try {
                catActivity.finish();
                subcatAcivity.finish();
                childCatActivity.finish();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
