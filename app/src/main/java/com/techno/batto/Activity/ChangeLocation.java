package com.techno.batto.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.techno.batto.App.GPSTracker;
import com.techno.batto.App.GeoCodingLocationLatlng;
import com.techno.batto.R;
import com.techno.batto.autoaddress.GeoAutoCompleteAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.techno.batto.Activity.Filter.lng;
import static com.techno.batto.Activity.Filter.lat;
import static com.techno.batto.Activity.Filter.txt_current_address;

public class ChangeLocation extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView location_change_back;
    private Double latitude, longitude;
    private LatLng currntlatlng;
    private Marker currentMarker;
    private GoogleMap mMap;
    private int countDrop1 = 0;
    private AutoCompleteTextView edit_pick;
    private Button change_location_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

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
                Toast.makeText(ChangeLocation.this, "Clicked", Toast.LENGTH_SHORT).show();
                if (edit_pick.getText().toString().length() > 10) {
                    txt_current_address.setText(edit_pick.getText().toString());
                    try {
                        LatLng latlg = GeoCodingLocationLatlng.getLocationFromAddress(edit_pick.getText().toString(), ChangeLocation.this);
                        lat = latlg.latitude;
                        lng = latlg.longitude;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish();
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
                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(this, l1, "" + latitude, "" + longitude, edit_pick);
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
            latitude = track.getLatitude();
            longitude = track.getLongitude();
            currntlatlng = new LatLng(latitude, longitude);
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

}
