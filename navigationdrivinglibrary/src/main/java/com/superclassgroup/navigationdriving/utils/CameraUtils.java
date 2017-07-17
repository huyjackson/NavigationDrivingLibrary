package com.superclassgroup.navigationdriving.utils;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 * Created by huyjackson on 7/17/17.
 */

public class CameraUtils {

    public static void goToLocation(GoogleMap googleMap, double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(ll);
        googleMap.moveCamera(cameraUpdate);
    }

    public static void goToLocation(GoogleMap googleMap, double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        googleMap.animateCamera(cameraUpdate);
    }

    public static void goToLocation(GoogleMap googleMap, double lat, double lng, float zoom, float tilt) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(zoom)
                .tilt(tilt)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition));
    }

    public static void goToLocation(GoogleMap googleMap, double lat, double lng, float zoom, float tilt, float bearing) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .bearing(bearing)
                .zoom(zoom)
                .tilt(tilt)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition));
    }

}
