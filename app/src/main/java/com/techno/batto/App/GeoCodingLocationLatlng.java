package com.techno.batto.App;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ritesh on 23/9/17.
 */

public class GeoCodingLocationLatlng {

    public static String getAddressFromLocation(double latitude, double longitude, Context context) {
        Geocoder geocoder;
        List<Address> addresses;
        String pickupadd;
        try {

            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                String address_city = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                pickupadd = address_city + ", " + city + " " + state + " " + country;
            } else {
                Toast.makeText(context, "Wrong Click", Toast.LENGTH_SHORT).show();
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return pickupadd;
    }

    public static LatLng getLocationFromAddress(String strAddress, Context context) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
