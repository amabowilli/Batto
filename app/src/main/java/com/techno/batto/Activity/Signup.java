package com.techno.batto.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Bean.AlertConnection;
import com.techno.batto.Bean.ConnectionDetector;
import com.techno.batto.Bean.GPSTracker;
import com.techno.batto.Bean.MySharedPref;
import com.techno.batto.Interface.Config;
import com.techno.batto.Interface.UserInterface;
import com.techno.batto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.techno.batto.Constant.Constants.SHARED_PREF;

public class Signup extends AppCompatActivity {
    ImageView singup_back;
    Button signup_btn, signup_skip_btn;
    EditText signup_name_edittext, signup_email_edittext, signup_mobile_edittext, signup_pass_edittext;
    String FId, name, email, pass, phone;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    GPSTracker tracker;
    double P_latitude, P_longitude;
    ProgressDialog progressDialog;
    MySharedPref sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //-------------------- get firebase id ---------------------------------------

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        FId = pref.getString("regId", "");
        Log.e("firebase id ", " " + FId);

        //--------------------- connection detector -----------------------------------

        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        super.onStart();

        //-------------------- Get current lat lon ------------------------------------

        tracker = new GPSTracker(getApplicationContext());
        if (tracker.canGetLocation()) {
            P_latitude = tracker.getLatitude();
            P_longitude = tracker.getLongitude();
            Log.e("current_lat ", " " + P_latitude);
            Log.e("current_Lon ", " " + P_longitude);
//            lat= String.valueOf(P_latitude);
//            lon= String.valueOf(P_longitude);
        }


        //---------------------------------- find view -------------------------------

        singup_back = findViewById(R.id.singup_back);
        signup_btn = findViewById(R.id.signup_btn);
        signup_skip_btn = findViewById(R.id.signup_skip_btn);
        signup_name_edittext = findViewById(R.id.signup_name_edittext);
        signup_email_edittext = findViewById(R.id.signup_email_edittext);
        signup_mobile_edittext = findViewById(R.id.signup_mobile_edittext);
        signup_pass_edittext = findViewById(R.id.signup_pass_edittext);

        //------------------------------- on click ----------------------------------

        singup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
                    FId = pref.getString("regId", "");
                    Log.e("firebase id ", " " + FId);
                    if (signup_name_edittext.getText().toString().trim().equalsIgnoreCase("")) {
                        signup_name_edittext.setError("Can't Be Empty");
                        signup_name_edittext.requestFocus();
                    } else if (signup_email_edittext.getText().toString().trim().equalsIgnoreCase("")) {
                        signup_email_edittext.setError("Can't Be Empty");
                        signup_email_edittext.requestFocus();
                    } else if (signup_mobile_edittext.getText().toString().trim().equalsIgnoreCase("")) {
                        signup_mobile_edittext.setError("Can't Be Empty");
                        signup_mobile_edittext.requestFocus();
                    } else if (signup_pass_edittext.getText().toString().trim().equalsIgnoreCase("")) {
                        signup_pass_edittext.setError("Can't Be Empty");
                        signup_pass_edittext.requestFocus();
                    } else {
                        name = signup_name_edittext.getText().toString().trim();
                        email = signup_email_edittext.getText().toString().trim();
                        phone = signup_mobile_edittext.getText().toString().trim();
                        pass = signup_pass_edittext.getText().toString().trim();
                        Signup_call();
                    }

                } else {
                    AlertConnection.showAlertDialog(Signup.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
            }
        });

        signup_skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
    }


    //------------------------------------ signup call -----------------------------------

    private void Signup_call() {
        progressDialog = new ProgressDialog(Signup.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.Base_Url).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        UserInterface signupInterface = retrofit.create(UserInterface.class);
        Call<ResponseBody> resultCall = signupInterface.signup(name, phone, email, pass, FId, String.valueOf(P_latitude), String.valueOf(P_longitude));
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                Log.e("Response", "" + response);
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        Log.e("get signup response** ", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");

                        if (error.equals("1")) {

                            JSONObject ressult = object.getJSONObject("result");
                            sp = new MySharedPref();
                            sp.saveData(getApplicationContext(), "ldata", ressult + "");
                            sp.saveData(getApplicationContext(), "user_id", ""+ressult.getString("id"));
                            Intent intent = new Intent(Signup.this, Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String message = object.getString("message");
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Server Problem Please try Next time...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
