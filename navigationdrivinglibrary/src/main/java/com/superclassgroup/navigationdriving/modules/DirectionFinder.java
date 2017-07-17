package com.superclassgroup.navigationdriving.modules;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?language=vi";
    private String googleDirectionAPIKey;
    private DirectionFinderListener listener;
    private String origin;
    private String destination;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
    }

    public void setDirectionFinderAPIKey(String googleDirectionAPIKey) {
        this.googleDirectionAPIKey = googleDirectionAPIKey;
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onDirectionFinderStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");

        return DIRECTION_URL_API + "&origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + googleDirectionAPIKey;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        ArrayList<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        Log.d("data:", data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);


            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
            JSONArray jsonSteps = jsonLeg.getJSONArray("steps");


            ArrayList<Step> steps = new ArrayList<>();
            for (int j = 0; j < jsonSteps.length(); j++) {
                JSONObject jsonStep = jsonSteps.getJSONObject(j);

                JSONObject jsonDistanceStep = jsonStep.getJSONObject("distance");
                JSONObject jsonDurationStep = jsonStep.getJSONObject("duration");
                JSONObject jsonEndLocationStep = jsonStep.getJSONObject("end_location");
                JSONObject jsonStartLocationStep = jsonStep.getJSONObject("start_location");
                JSONObject jsonPolyline = jsonStep.getJSONObject("polyline");

                String instructions = android.text.Html.fromHtml(jsonStep.getString("html_instructions")).toString().trim();
                Step step = new Step(
                        new Distance(jsonDistanceStep.getString("text"), jsonDistanceStep.getInt("value")),
                        new Duration(jsonDurationStep.getString("text"), jsonDurationStep.getInt("value")),
                        new LatLng(jsonEndLocationStep.getDouble("lat"), jsonEndLocationStep.getDouble("lng")),
                        new LatLng(jsonStartLocationStep.getDouble("lat"), jsonStartLocationStep.getDouble("lng")),
                        TextUtils.substring(instructions, 0, TextUtils.indexOf(instructions, "\n")),
                        jsonStep.has("maneuver") ? jsonStep.getString("maneuver") : "",
                        decodePolyLine(jsonPolyline.getString("points")),
                        jsonStep.getString("travel_mode"),
                        0
                );
                steps.add(step);

            }

            Route route = new Route(
                    new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value")),
                    new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value")),
                    jsonLeg.getString("end_address"),
                    new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng")),
                    jsonLeg.getString("start_address"),
                    new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng")),
                    decodePolyLine(overview_polylineJson.getString("points")),
                    steps
            );
            routes.add(route);
        }
        listener.onDirectionFinderResult(routes, true);
    }

    private ArrayList<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        ArrayList<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
