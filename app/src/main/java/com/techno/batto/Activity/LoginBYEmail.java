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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techno.batto.Bean.AlertConnection;
import com.techno.batto.Bean.ConnectionDetector;
import com.techno.batto.Bean.GPSTracker;
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

import static com.techno.batto.Bean.MySharedPref.saveData;
import static com.techno.batto.Constant.Constants.SHARED_PREF;

public class LoginBYEmail extends AppCompatActivity {

    TextView go_to_signup_text;
    Button login_btn, login_skip_btn;
    String FId, name, email, pass, phone;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    GPSTracker tracker;
    double P_latitude, P_longitude;
    ProgressDialog progressDialog;
    //MySharedPref sp;
    EditText login_email_edittext, login_pass_edittext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_byemail);

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


        //----------------------------- find view ------------------------------

        go_to_signup_text = findViewById(R.id.go_to_signup_text);
        login_btn = findViewById(R.id.login_btn);
        login_skip_btn = findViewById(R.id.login_skip_btn);
        login_email_edittext = findViewById(R.id.login_email_edittext);
        login_pass_edittext = findViewById(R.id.login_pass_edittext);

        //----------------------------- on click ---------------------------

        go_to_signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
                    FId = pref.getString("regId", "");
                    Log.e("firebase id ", " " + FId);
                    if (login_email_edittext.getText().toString().trim().equalsIgnoreCase("")) {
                        login_email_edittext.setError("Can't Be Empty");
                        login_email_edittext.requestFocus();
                    } else if (login_pass_edittext.getText().toString().trim().equalsIgnoreCase("")) {
                        login_pass_edittext.setError("Can't Be Empty");
                        login_pass_edittext.requestFocus();
                    } else {

                        email = login_email_edittext.getText().toString().trim();
                        pass = login_pass_edittext.getText().toString().trim();
                        Login_call();
                    }

                } else {
                    AlertConnection.showAlertDialog(LoginBYEmail.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
            }
        });

        login_skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
    }


    //------------------------------------ signup call -----------------------------------

    private void Login_call() {
        progressDialog = new ProgressDialog(LoginBYEmail.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.Base_Url).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        UserInterface signupInterface = retrofit.create(UserInterface.class);
        Call<ResponseBody> resultCall = signupInterface.login(email, pass, FId, String.valueOf(P_latitude), String.valueOf(P_longitude));
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        Log.e("login response** ", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");

                        if (error.equals("1")) {

                            JSONObject ressult = object.getJSONObject("result");
                            saveData(getApplicationContext(), "ldata", ressult + "");
                            saveData(getApplicationContext(), "user_id", "" + ressult.getString("id"));
                            Intent intent = new Intent(LoginBYEmail.this, Home.class);
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
