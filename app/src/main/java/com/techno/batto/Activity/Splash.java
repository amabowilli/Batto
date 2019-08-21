package com.techno.batto.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
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

import static com.techno.batto.Bean.MySharedPref.getData;

public class Splash extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 3000;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        byte[] sha1 = {
                (byte) 0x4E, (byte) 0x4B, (byte) 0x31, (byte) 0x2E, (byte) 0xBB, (byte) 0x3C, (byte) 0x74, (byte) 0x9B, (byte) 0x7B, (byte) 0xE7, (byte) 0xDD, (byte) 0xED, (byte) 0xF0, 0x58, (byte) 0x83, (byte) 0x1A, (byte) 0xA2, (byte) 0x25, (byte) 0x23, (byte) 0xB3

                                                                                           //4E:4B:31:2E:BB:3C:74:9B:7B:E7:DD:ED:F0:58:83:1A:A2:25:23:B3

        };

        byte[] sha2 = {
                0x08, (byte) 0x92, (byte) 0x43, (byte) 0x92, (byte) 0x6A, (byte) 0x22, (byte) 0xBA, (byte) 0x82, (byte) 0xCD, (byte) 0xA5, (byte) 0x0D, (byte) 0x7D, (byte) 0x19, (byte) 0xAC, (byte) 0x37, (byte) 0x50, (byte) 0x88, (byte) 0x50, (byte) 0xFC, (byte) 0x82

                                                                                             //08:92:43:92:6A:22:BA:82:CD:A5:0D:7D:19:AC:37:50:88:50:FC:82
        };
        System.out.println("keyhashGooglePlaySignIn1:" + Base64.encodeToString(sha1, Base64.NO_WRAP));
        System.out.println("keyhashGooglePlaySignIn2:" + Base64.encodeToString(sha2, Base64.NO_WRAP));

        //--------------------------------------------------------------------------
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[5])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[6])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[7])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[8])
                            != MockPackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            } else {
                new Handler().postDelayed(new Runnable() {
                    /*
                     * Showing splash screen with a timer. This will be useful when you
                     * want to show case your app logo / company
                     */
                    @Override
                    public void run() {
                        String ldata = getData(Splash.this, "ldata", null);
                        if (ldata == null) {
                            Intent in = new Intent(Splash.this, Welcome.class);
                            startActivity(in);
                            finish();

                        } else {
                            String id = getData(Splash.this, "user_id", null);
                            GetProfile_call(id);
                        }
                    }
                }, SPLASH_TIME_OUT);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 9 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[7] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[8] == MockPackageManager.PERMISSION_GRANTED
            ) {
                new Handler().postDelayed(new Runnable() {
                    /*
                     * Showing splash screen with a timer. This will be useful when you
                     * want to show case your app logo / company
                     */
                    @Override
                    public void run() {
                        String ldata = getData(Splash.this, "ldata", null);
                        if (ldata == null) {
                            Intent in = new Intent(Splash.this, Welcome.class);
                            startActivity(in);
                            finish();

                        } else {
                            String id = getData(Splash.this, "user_id", null);
                            GetProfile_call(id);
                        }
                    }
                }, SPLASH_TIME_OUT);
            } else {
                Toast.makeText(Splash.this, "Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void GetProfile_call(String logid) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.Base_Url).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        UserInterface signupInterface = retrofit.create(UserInterface.class);
        Call<ResponseBody> resultCall = signupInterface.get_profile(logid);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                // progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        Log.e("get profile response** ", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");

                        if (error.equals("1")) {
                            Intent in = new Intent(Splash.this, Home.class);
                            startActivity(in);
                            finish();

                        } else {
                            Intent in = new Intent(Splash.this, Welcome.class);
                            startActivity(in);
                            finish();
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
                // progressDialog.dismiss();
                Toast.makeText(Splash.this, "Server Problem Please try Next time...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
