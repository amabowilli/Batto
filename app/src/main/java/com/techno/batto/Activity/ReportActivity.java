package com.techno.batto.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back, img_user;
    private String product_id, user_id, report;
    private TextView txt_pname, txt_catname;
    private Button btn_submit;
    private RadioGroup radioGroup;
    private RadioButton rdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        product_id = getIntent().getExtras().getString("product_id");
        findId();
        user_id = getData(this, "user_id", null);
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        getProductDetailCall(product_id);
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
        txt_pname = findViewById(R.id.txt_pname);
        txt_catname = findViewById(R.id.txt_catname);
        img_user = findViewById(R.id.img_user);
        btn_submit = findViewById(R.id.btn_submit);
        radioGroup = findViewById(R.id.radioGroup);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        } else if (view == btn_submit) {
            validate();
        }
    }

    private void validate() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        rdButton = (RadioButton) findViewById(selectedId);
        if (rdButton==null) {
            Toast.makeText(ReportActivity.this, "Please select a report!!!", Toast.LENGTH_SHORT).show();
        } else {
            report = rdButton.getText().toString();
            Toast.makeText(ReportActivity.this, "--->" + rdButton.getText(), Toast.LENGTH_SHORT).show();
            addReportCall();
        }
    }


    private void addReportCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().addReport(user_id, product_id, report);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("product Data :- " + object);
                        if (object.getString("status").equals("1")) {
                            final PrettyDialog pDialog = new PrettyDialog(ReportActivity.this);
                            pDialog.setTitle("Success")
                                    .setIcon(R.drawable.checked)
                                    .setMessage("Your Report added successfully")
                                    .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_green,
                                            new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    pDialog.dismiss();
                                                    finish();
                                                }
                                            }
                                    ).show();
                        } else {
                            Toast.makeText(ReportActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReportActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
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
                                    JSONObject obj = object.getJSONObject("result");
                                    String img1 = obj.getString("image1");
                                    txt_pname.setText(obj.getString("name"));
                                    txt_catname.setText(obj.getString("category_name"));
                                    Picasso.with(ReportActivity.this).load(img1).error(R.drawable.not_found).into(img_user);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            String ldata = getData(ReportActivity.this, "ldata", null);
                            if (ldata != null) {
                                try {
                                    JSONObject obj = new JSONObject(ldata);
                                    Log.e("--Login Data--", "" + obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            Toast.makeText(ReportActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReportActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
