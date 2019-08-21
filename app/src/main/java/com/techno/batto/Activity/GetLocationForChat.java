package com.techno.batto.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techno.batto.App.AppConfig;
import com.techno.batto.App.GPSTracker;
import com.techno.batto.App.GeoCodingLocationLatlng;
import com.techno.batto.R;
import com.techno.batto.autoaddress.GeoAutoCompleteAdapter;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetLocationForChat extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView location_change_back;
    String reciver_id, sender_id,product_id;
    private LatLng currntlatlng;
    private Marker currentMarker;
    private GoogleMap mMap;
    private int countDrop1 = 0;
    private AutoCompleteTextView edit_pick;
    private Button change_location_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location_for_chat);
        reciver_id=getIntent().getExtras().getString("reciver_id");
        sender_id=getIntent().getExtras().getString("sender_id");
        product_id=getIntent().getExtras().getString("product_id");

        Log.e("reciver_id", reciver_id);
        Log.e("sender_id", sender_id);
        Log.e("product_id", product_id);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_one);
        mapFragment.getMapAsync(this);

        location_change_back = findViewById(R.id.location_change_back);
        edit_pick = findViewById(R.id.edit_pick);
        change_location_btn = findViewById(R.id.change_location_btn);

        location_change_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        change_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GetLocationForChat.this, "Clicked", Toast.LENGTH_SHORT).show();
                if (edit_pick.getText().toString().length() > 10) {
                    //txt_current_address.setText(edit_pick.getText().toString());
                    try {
                        LatLng latlg = GeoCodingLocationLatlng.getLocationFromAddress(edit_pick.getText().toString(), GetLocationForChat.this);
                        ChatActivity.latitude = latlg.latitude;
                        ChatActivity.longitude = latlg.longitude;
                        currntlatlng=new LatLng( ChatActivity.latitude,ChatActivity.longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sendMsgCall();
            }
        });


        edit_pick.setThreshold(1);
        edit_pick.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    loadDataDrop(edit_pick.getText().toString());
                }
            }
        });


    }

    private void loadDataDrop(String s) {
        try {
            if (countDrop1 == 0) {
                List<String> l1 = new ArrayList<>();
                if (s == null) {

                } else {
                    l1.add(s);
                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(this, l1, "" + ChatActivity.latitude, "" + ChatActivity.longitude, edit_pick);
                    edit_pick.setAdapter(ga);
                }
            }
            countDrop1++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GPSTracker track = new GPSTracker(this);
        if (track.canGetLocation()) {
            ChatActivity.latitude = track.getLatitude();
            ChatActivity.longitude = track.getLongitude();
            currntlatlng = new LatLng(ChatActivity.latitude, ChatActivity.longitude);
            moveMap();
        } else {
            track.showSettingsAlert();
        }
    }


    private void moveMap() {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 60, 70, false);
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(currntlatlng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currntlatlng));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currntlatlng).tilt(45).zoom((float) 17.5).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void sendMsgCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().insertChat(sender_id, reciver_id,product_id, "Location","Location",""+currntlatlng.latitude,""+currntlatlng.longitude);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("result").equals("successful")) {
                            Toast.makeText(GetLocationForChat.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(GetLocationForChat.this, "Not Send", Toast.LENGTH_SHORT).show();
                        }
                    } else ;
                    // AppConfig.showToast("server error");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(GetLocationForChat.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
