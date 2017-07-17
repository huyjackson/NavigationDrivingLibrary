package com.superclassgroup.navigationdriving.modules;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by huyjackson on 7/13/17.
 */

public class Step {

    Distance distance;
    Duration duration;
    LatLng endLocation;
    LatLng startLocation;
    String instructions;
    String maneuver;
    ArrayList<LatLng> points;
    String travelMode;
    int flag;

    public Step(Distance distance, Duration duration, LatLng endLocation, LatLng startLocation, String instructions, String maneuver, ArrayList<LatLng> points, String travelMode, int flag) {
        this.distance = distance;
        this.duration = duration;
        this.endLocation = endLocation;
        this.startLocation = startLocation;
        this.instructions = instructions;
        this.maneuver = maneuver;
        this.points = points;
        this.travelMode = travelMode;
        this.flag = flag;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<LatLng> points) {
        this.points = points;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
