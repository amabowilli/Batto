package com.techno.batto.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.techno.batto.Bean.ConnectionDetector;
import com.techno.batto.Bean.GPSTracker;
import com.techno.batto.Interface.Config;
import com.techno.batto.Interface.UserInterface;
import com.techno.batto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.techno.batto.Bean.MySharedPref.saveData;
import static com.techno.batto.Constant.Constants.SHARED_PREF;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button go_to_loign_btn;
    RelativeLayout go_to_otp_lay, google_login_lay, fb_login_lay,guest_lay;
    String FId, name, email, pass, phone;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    GPSTracker tracker;
    double P_latitude, P_longitude;
    ProgressDialog progressDialog;
    //MySharedPref sp;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;
    String googlename, googleid, googleemail;
    LoginButton loginButton;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    private String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    String fb_name, fb_id, fb_email, fb_profile_url, facebook_status, g_status, verify_with;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(Login.this);
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);


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


        //---------------------------- find view --------------------

        go_to_loign_btn = findViewById(R.id.go_to_loign_btn);
        go_to_otp_lay = findViewById(R.id.go_to_otp_lay);
        google_login_lay = findViewById(R.id.google_login_lay);
        fb_login_lay = findViewById(R.id.fb_login_lay);
        guest_lay = findViewById(R.id.guest_lay);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));

        //-------------------------- facebook login ------------------------------------

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebook_id = f_name = m_name = l_name = gender = profile_image = full_name = email_id = "";
                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        facebook_id = profile.getId();
                        Log.e("facebook_id", facebook_id);
                        f_name = profile.getFirstName();
                        Log.e("f_name", f_name);
                        m_name = profile.getMiddleName();
                        Log.e("m_name", m_name);
                        l_name = profile.getLastName();
                        Log.e("l_name", l_name);
                        full_name = profile.getName();
                        Log.e("full_name", full_name);
                        profile_image = profile.getProfilePictureUri(400, 400).toString();
                        Log.e("profile_image", profile_image);
                    }
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

        //----------------------------- on click --------------------

        fb_login_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fb_login_lay) {
                    verify_with = "Facebook";
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
                    FId = pref.getString("regId", "");
                    Log.e("firebase id ", " " + FId);
                    loginButton.performClick();
                }
            }
        });

        go_to_loign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginBYEmail.class));
            }
        });

        go_to_otp_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OTPVerify.class));
            }
        });

        guest_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });


        //--------------------- google login -------------------------------------------

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        google_login_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_with = "Google";
                SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
                FId = pref.getString("regId", "");
                Log.e("firebase id ", " " + FId);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                //Starting intent for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //--------------------------------- facebook request data ----------------------------

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                System.out.println("Json data :" + json);
                try {
                    if (json != null) {
                        String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email");
                        JSONObject profile_pic_data = new JSONObject(json.get("picture").toString());
                        JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString("data").toString());
                        System.out.println("profile_pic_url" + profile_pic_url.getString("url"));
                        System.out.println("emailsss" + json.getString("email"));
                        System.out.println("profilessss" + Html.fromHtml(text));
                        System.out.println("acebookid" + json.getString("id"));
                        fb_id = json.getString("id");
                        fb_name = json.getString("name");
                        fb_email = json.getString("email");
                        fb_profile_url = profile_pic_url.getString("url");

                        FB_Login_call();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        } else callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    //-------------------------- After the signing ----------------------------

    private void handleSignInResult(GoogleSignInResult result) {

        Log.e("google ", "" + result);
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            googlename = acct.getDisplayName();
            googleemail = acct.getEmail();
            //   String image=  acct.getPhotoUrl().toString();
            googleid = acct.getId();
            acct.getId();
            Google_Login_call();
        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    //-------------------------------------- google login ----------------------------------

    private void Google_Login_call() {

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.Base_Url).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        UserInterface signupInterface = retrofit.create(UserInterface.class);
        Call<ResponseBody> resultCall = signupInterface.social_login(googlename, googleemail, FId, googleid,"Google", String.valueOf(P_latitude), String.valueOf(P_longitude), "");
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        Log.e("google login response ", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");

                        if (error.equals("1")) {

                            JSONObject ressult = object.getJSONObject("result");
                            //sp = new MySharedPref();
                            saveData(getApplicationContext(), "ldata", ressult + "");
                            saveData(getApplicationContext(), "user_id", "" + ressult.getString("id"));
                            Intent intent = new Intent(Login.this, Home.class);
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

    //-------------------------------------- FB_Login_call login ----------------------------------

    private void FB_Login_call() {

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.Base_Url).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        UserInterface signupInterface = retrofit.create(UserInterface.class);
        Call<ResponseBody> resultCall = signupInterface.social_login(fb_name, fb_email, FId, fb_id,"Facebook", String.valueOf(P_latitude), String.valueOf(P_longitude), "");
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        Log.e("fb login response ", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");

                        if (error.equals("1")) {

                            JSONObject ressult = object.getJSONObject("result");
                            saveData(getApplicationContext(), "ldata", ressult + "");
                            saveData(getApplicationContext(), "user_id", "" + ressult.getString("id"));
                            Intent intent = new Intent(Login.this, Home.class);
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
