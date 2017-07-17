package com.superclassgroup.navigationdriving.utils;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by huyjackson on 7/17/17.
 */

public class MapsUtils {

    public String getStreetNameByLocation(Activity activity, Location location) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                }
                Log.d("street", strReturnedAddress.toString());
                return strReturnedAddress.toString();
            } else {
                Log.d("street", "No Address returned!");
                return "";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("street", "Can not get Address!");
            return "";
        }

    }

    public static float calculateBearing(LatLng start, LatLng dest) {
        Location startLocation = new Location("");
        Location destLocation = new Location("");

        startLocation.setLatitude(start.latitude);
        startLocation.setLongitude(start.longitude);

        destLocation.setLatitude(dest.latitude);
        destLocation.setLongitude(dest.longitude);

        return startLocation.bearingTo(destLocation);
    }


    public static void setMapPadding(GoogleMap googleMap) {
        googleMap.setPadding(0, 140, 0, 0);
    }

    public static Marker addMarker(GoogleMap googleMap, Place place) {
        MarkerOptions markerOptions = new MarkerOptions()
                .title(place.getName().toString())
                .snippet(place.getAddress().toString())
                .position(place.getLatLng());
        Marker marker = googleMap.addMarker(markerOptions);
        return marker;
    }
}
