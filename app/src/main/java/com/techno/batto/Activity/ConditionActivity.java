package com.techno.batto.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techno.batto.R;

import static com.techno.batto.Activity.AddProductActivity.txt_conditionadd;
import static com.techno.batto.Activity.EditProductDetailsActivity.txt_conditionadd1;

public class ConditionActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView txt_one, txt_two, txt_three, txt_four, txt_five, txt_six;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        type = getIntent().getExtras().getString("type");
        findId();
        img_back.setOnClickListener(this);
        txt_one.setOnClickListener(this);
        txt_two.setOnClickListener(this);
        txt_three.setOnClickListener(this);
        txt_four.setOnClickListener(this);
        txt_five.setOnClickListener(this);
        txt_six.setOnClickListener(this);

    }

    private void findId() {
        img_back = findViewById(R.id.img_back);

        txt_one = findViewById(R.id.txt_one);
        txt_two = findViewById(R.id.txt_two);
        txt_three = findViewById(R.id.txt_three);
        txt_four = findViewById(R.id.txt_four);
        txt_five = findViewById(R.id.txt_five);
        txt_six = findViewById(R.id.txt_six);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        } else if (view == txt_one) {
            if (type.equals("Add")) {
                txt_conditionadd.setText(txt_one.getText().toString());
                finish();
            } else {
                txt_conditionadd1.setText(txt_one.getText().toString());
                finish();
            }
        } else if (view == txt_two) {
            if (type.equals("Add")) {
                txt_conditionadd.setText(txt_two.getText().toString());
                finish();
            } else {
                txt_conditionadd1.setText(txt_two.getText().toString());
                finish();
            }

        } else if (view == txt_three) {
            if (type.equals("Add")) {
                txt_conditionadd.setText(txt_three.getText().toString());
                finish();
            } else {
                txt_conditionadd1.setText(txt_three.getText().toString());
                finish();
            }

        } else if (view == txt_four) {
            if (type.equals("Add")) {
                txt_conditionadd.setText(txt_four.getText().toString());
                finish();
            } else {
                txt_conditionadd1.setText(txt_four.getText().toString());
                finish();
            }

        } else if (view == txt_five) {
            if (type.equals("Add")) {
                txt_conditionadd.setText(txt_five.getText().toString());
                finish();
            } else {
                txt_conditionadd1.setText(txt_five.getText().toString());
                finish();
            }

        } else if (view == txt_six) {
            if (type.equals("Add")) {
                txt_conditionadd.setText(txt_six.getText().toString());
                finish();
            } else {
                txt_conditionadd1.setText(txt_six.getText().toString());
                finish();
            }

        }
    }
}
