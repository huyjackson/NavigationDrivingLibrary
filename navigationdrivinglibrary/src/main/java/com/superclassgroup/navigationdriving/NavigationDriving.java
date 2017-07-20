package com.superclassgroup.navigationdriving;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.superclassgroup.navigationdriving.modules.DirectionFinder;
import com.superclassgroup.navigationdriving.modules.DirectionFinderListener;
import com.superclassgroup.navigationdriving.modules.Route;
import com.superclassgroup.navigationdriving.utils.CameraUtils;
import com.superclassgroup.navigationdriving.utils.Constant;
import com.superclassgroup.navigationdriving.utils.MapsUtils;
import com.superclassgroup.navigationdriving.utils.StreamAudioServiceUtils;
import com.superclassgroup.navigationdriving.utils.TextToSpeechUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by huyjackson on 7/17/17.
 */

public class NavigationDriving implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionFinderListener {


    private GoogleMap googleMap;
    private Activity activity;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private ArrayList<Marker> originMarkers = new ArrayList<>();
    private ArrayList<Marker> destinationMarkers = new ArrayList<>();
    private ArrayList<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private OnNavigationDrivingListener callbackNavigationDriving;
    private Location preLocation;
    private Location newLocation;
    private Route route;
    private Boolean isNavigationDriving;
    private Boolean isSpeech = false;
    private String googleDirectionAPIKey;
    private boolean isCustomNavigationDiriving;
    private int voiceMode;
    private TextToSpeechUtils ttsUtils;

    public NavigationDriving(Activity activity, GoogleMap googleMap) {
        this.activity = activity;
        this.googleMap = googleMap;
        this.isCustomNavigationDiriving = false;
        this.voiceMode = Constant.NAVIGATION_DIRIVING_GTTS_VOICE_MODE;
    }

    public void setCustomNavigationDiriving(boolean isCustom) {
        this.isCustomNavigationDiriving = isCustom;
    }

    public void setVoiceMode(int voiceMode) {
        this.voiceMode = voiceMode;
    }

    public void setDirectionFinderAPIKey(String googleDirectionAPIKey) {
        this.googleDirectionAPIKey = googleDirectionAPIKey;
    }

    public void setOnNavigationDrivingListener(OnNavigationDrivingListener callbackNavigationDriving) {
        this.callbackNavigationDriving = callbackNavigationDriving;
    }

    public void findDirection(Place startPlace, Place destPlace) {
        try {
            DirectionFinder directionFinder = new DirectionFinder(this, startPlace.getAddress().toString(), destPlace.getAddress().toString());
            directionFinder.setDirectionFinderAPIKey(googleDirectionAPIKey);
            directionFinder.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void findDirection(Location startLocation, Location destLocation) {
        try {
            String origin = String.valueOf(startLocation.getLatitude()) + "," + String.valueOf(startLocation.getLongitude());
            String destination = String.valueOf(destLocation.getLatitude()) + "," + String.valueOf(destLocation.getLongitude());
            DirectionFinder directionFinder = new DirectionFinder(this, origin, destination);
            directionFinder.setDirectionFinderAPIKey(googleDirectionAPIKey);
            directionFinder.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public int startNavigationDriving() {
        if (!isNavigationDriving) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(activity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    buildGoogleApiClient();
                    isNavigationDriving = true;
                } else {
                    //Request Location Permission
                    return Constant.NAVIGATION_DIRIVING_REQUEST_LOCATION_PERMISSION;
                }
            } else {
                buildGoogleApiClient();
                isNavigationDriving = true;
            }
            return Constant.NAVIGATION_DIRIVING_START_SUCCESS;
        } else {
            return Constant.NAVIGATION_DIRIVING_CURRENTLY_RUNNING;
        }
    }

    public int stopNavigationDriving() {

        if (googleApiClient != null && isNavigationDriving) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            isNavigationDriving = false;
            return Constant.NAVIGATION_DIRIVING_STOP_SUCCESS;
        } else {
            return Constant.NAVIGATION_DIRIVING_CURRENTLY_NOT_RUNNING;
        }
    }

    public void setLocationRequest(long interval, long fastestInterval, int priority) {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(interval);
            locationRequest.setFastestInterval(fastestInterval);
            locationRequest.setPriority(priority);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setLocationRequest(1000, 1000, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            if (!isCustomNavigationDiriving) {
                if (preLocation == null) {
                    preLocation = location;
                }
                newLocation = location;
                if (!isSpeech) {
                    if (newLocation != null) {
                        if (route != null) {
                            Location startLocation = new Location("");
                            for (int i = 0; i < route.getSteps().size(); i++) {
                                startLocation.setLatitude(route.getSteps().get(i).getStartLocation().latitude);
                                startLocation.setLongitude(route.getSteps().get(i).getStartLocation().longitude);
                                if (newLocation.distanceTo(startLocation) < 30 && route.getSteps().get(i).getFlag() == 0) {
                                    route.getSteps().get(i).setFlag(1);

                                    switch (voiceMode) {
                                        case Constant.NAVIGATION_DIRIVING_TTS_VOICE_MODE: {

                                            String maneuver = route.getSteps().get(i).getManeuver();
                                            if (ttsUtils != null) {
                                                ttsUtils.speakText(maneuver);
                                            } else {
                                                ttsUtils = new TextToSpeechUtils(activity);
                                            }
                                        }
                                        ;
                                        break;
                                        case Constant.NAVIGATION_DIRIVING_GTTS_VOICE_MODE: {
                                            String url = "";
                                            url += Constant.URL;
                                            String text = route.getSteps().get(i).getInstructions().trim();
                                            url += text;
                                            Intent startIntent = new Intent(activity, StreamAudioServiceUtils.class);
                                            startIntent.putExtra("URL", url);
                                            activity.startService(startIntent);
                                            Toast.makeText(activity, route.getSteps().get(i).getInstructions(), Toast.LENGTH_SHORT).show();
                                        }
                                        ;
                                        break;
                                    }

                                }
                            }
                        }
                    }
                }
                if (isNavigationDriving) {
                    CameraUtils.goToLocation(googleMap, newLocation.getLatitude(), newLocation.getLongitude(), 20, 60, preLocation.bearingTo(location));
                }
                preLocation = location;
                this.callbackNavigationDriving.onLocationChanged(location);
            } else {
                this.callbackNavigationDriving.onLocationChanged(location);
            }
        }
    }

    @Override
    public void onDirectionFinderStart() {

        progressDialog = ProgressDialog.show(activity, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }

        callbackNavigationDriving.onDirectionFinderStart();

    }

    @Override
    public void onDirectionFinderResult(ArrayList<Route> routes, boolean isSuccess) {

        progressDialog.dismiss();

        if (isSuccess) {

            polylinePaths = new ArrayList<>();
            originMarkers = new ArrayList<>();
            destinationMarkers = new ArrayList<>();

            for (Route route : routes) {

                CameraUtils.goToLocation(googleMap, route.getStartLocation().latitude, route.getStartLocation().longitude, 20, 60, MapsUtils.calculateBearing(route.getStartLocation(), route.getPoints().get(1)));

                originMarkers.add(googleMap.addMarker(new MarkerOptions()
                        .title(route.getStartAddress())
                        .position(route.getStartLocation())));
                destinationMarkers.add(googleMap.addMarker(new MarkerOptions()
                        .title(route.getEndAddress())
                        .position(route.getEndLocation())));

                PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(Color.BLUE).
                        width(50);

                for (int i = 0; i < route.getPoints().size(); i++)
                    polylineOptions.add(route.getPoints().get(i));

                polylinePaths.add(googleMap.addPolyline(polylineOptions));
            }

            this.route = routes.get(0);
            this.callbackNavigationDriving.onDirectionFinderSuccess();

        } else {
            this.callbackNavigationDriving.onDirectionFinderFailed();
        }
    }

    public interface OnNavigationDrivingListener {


        public void onLocationChanged(Location location);

        public void onDirectionFinderStart();

        public void onDirectionFinderSuccess();

        public void onDirectionFinderFailed();
    }


}
