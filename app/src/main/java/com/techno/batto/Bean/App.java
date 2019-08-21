package com.techno.batto.Bean;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by pintu22 on 15/9/17.
 */

public class App extends MultiDexApplication {
    private static App instance = null;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor ;
    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        printHashKey();
//        pref = this.getSharedPreferences(Constant.APP_NAME,this.MODE_PRIVATE);
//        editor = pref.edit();
        //....
    }

    public static App getInstance() {
        return instance;
    }

    public void setInstance(App instance) {
        App.instance = instance;
    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.t.classified",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


}
