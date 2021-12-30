package com.general.files;

import android.content.Context;
import android.location.Location;

import com.melevicarbrasil.usuario.OnGoingTripDetailsActivity;
import com.melevicarbrasil.usuario.R;
import com.melevicarbrasil.usuario.deliverAll.MapDelegate;
import com.melevicarbrasil.usuario.deliverAll.TrackOrderActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.utils.Logger;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 02-08-2017.
 */

//public class UpdateDirections implements GetLocationUpdates.LocationUpdates, UpdateFrequentTask.OnTaskRunCalled {
public class UpdateDirections implements UpdateFrequentTask.OnTaskRunCalled, MapDelegate {

    public GoogleMap googleMap;
    public Location destinationLocation;
    public Context mcontext;
    public Location userLocation;

    GeneralFunctions generalFunctions;

    String serverKey;
    Polyline route_polyLine;

    UpdateFrequentTask updateFreqTask;

    String gMapLngCode = "en";
    String userProfileJson = "";
    String eUnit = "KMs";
    int DRIVER_ARRIVED_MIN_TIME_PER_MINUTE = 3;

    public UpdateDirections(Context mcontext, GoogleMap googleMap, Location userLocation, Location destinationLocation) {
        this.googleMap = googleMap;
        this.destinationLocation = destinationLocation;
        this.mcontext = mcontext;
        this.userLocation = userLocation;

        generalFunctions = MyApp.getInstance().getGeneralFun(mcontext);

        serverKey = generalFunctions.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);

        gMapLngCode = generalFunctions.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY);

        userProfileJson = generalFunctions.retrieveValue(Utils.USER_PROFILE_JSON);
        eUnit = generalFunctions.getJsonValue("eUnit", userProfileJson);
        DRIVER_ARRIVED_MIN_TIME_PER_MINUTE = generalFunctions.parseIntegerValue(3, generalFunctions.getJsonValue("DRIVER_ARRIVED_MIN_TIME_PER_MINUTE", userProfileJson));
    }

    public void scheduleDirectionUpdate() {


        String DESTINATION_UPDATE_TIME_INTERVAL = generalFunctions.retrieveValue("DESTINATION_UPDATE_TIME_INTERVAL");
        updateFreqTask = new UpdateFrequentTask((int) (generalFunctions.parseDoubleValue(2, DESTINATION_UPDATE_TIME_INTERVAL) * 60 * 1000));
        updateFreqTask.setTaskRunListener(this);
        updateFreqTask.startRepeatingTask();

    }

    public void releaseTask() {
        Logger.d("Task", ":: releaseTask called");
        if (updateFreqTask != null) {
            updateFreqTask.stopRepeatingTask();
            updateFreqTask = null;
        }

        // Utils.runGC();
    }


    public void updateDirections() {

        if (userLocation == null || destinationLocation == null) {
            return;
        }


        if (userProfileJson != null && !generalFunctions.getJsonValue("ENABLE_DIRECTION_SOURCE_DESTINATION_USER_APP", userProfileJson).equalsIgnoreCase("Yes")) {


            double distance = Utils.CalculationByLocation(userLocation.getLatitude(), userLocation.getLongitude(), destinationLocation.getLatitude(), destinationLocation.getLongitude(), "");

            if (userProfileJson != null && !generalFunctions.getJsonValue("eUnit", userProfileJson).equalsIgnoreCase("KMs")) {
                distance = distance * 0.000621371;
            } else {
                distance = distance * 0.00099999969062399994;
            }


            if (userProfileJson != null && !generalFunctions.getJsonValue("eUnit", userProfileJson).equalsIgnoreCase("KMs")) {
                distance = distance * 0.000621371;
            }

            distance = generalFunctions.round(distance, 2);
            int time = ((int) (distance * DRIVER_ARRIVED_MIN_TIME_PER_MINUTE));

            if (time < 1) {
                time = 1;
            }
            if (mcontext instanceof OnGoingTripDetailsActivity) {
                OnGoingTripDetailsActivity onGoingTripDetailsActivity = (OnGoingTripDetailsActivity) mcontext;
                onGoingTripDetailsActivity.setTimetext(generalFunctions.formatUpto2Digit(distance) + "", getTimeTxt((time % 3600) / 60));
            }

            if (mcontext instanceof TrackOrderActivity) {
                TrackOrderActivity trackOrderActivity = (TrackOrderActivity) mcontext;
                trackOrderActivity.setEta("" + time, "" + distance, generalFunctions.getJsonValue("eUnit", userProfileJson));
            }

            return;
        }


        String originLoc = userLocation.getLatitude() + "," + userLocation.getLongitude();
        String destLoc = destinationLocation.getLatitude() + "," + destinationLocation.getLongitude();

       // String directionURL = "https://maps.googleapis.com/maps/api/directions/json?origin=" + originLoc + "&destination=" + destLoc + "&sensor=true&key=" + serverKey + "&language=" + gMapLngCode + "&sensor=true";

        HashMap<String, String> hashMap = new HashMap<>();


        hashMap.put("s_latitude", userLocation.getLatitude() + "");
        hashMap.put("s_longitude", userLocation.getLongitude() + "");
        hashMap.put("d_latitude", destinationLocation.getLatitude() + "");
        hashMap.put("d_longitude", destinationLocation.getLongitude() + "");


//        if (userProfileJson != null && !eUnit.equalsIgnoreCase("KMs")) {
//            directionURL = directionURL + "&units=imperial";
//        }

        String trip_data = generalFunctions.getJsonValue("TripDetails", userProfileJson);

        String eTollSkipped = generalFunctions.getJsonValue("eTollSkipped", trip_data);

        if (eTollSkipped == "Yes") {
          //  directionURL = directionURL + "&avoid=tolls";
            hashMap.put("toll_avoid", "Yes");
        }

        String parameters = "origin=" + originLoc + "&destination=" + destLoc;

        hashMap.put("parameters", parameters);

        MapServiceApi.getDirectionservice(mcontext, hashMap, this, null,false);
//
//        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(mcontext, directionURL, true);
//        exeWebServer.setDataResponseListener(responseString -> {
//
//            if (responseString != null && !responseString.equals("")) {
//
//                String status = generalFunctions.getJsonValue("status", responseString);
//
//                if (status.equals("OK")) {
//
//                    JSONArray obj_routes = generalFunctions.getJsonArray("routes", responseString);
//                    if (obj_routes != null && obj_routes.length() > 0) {
//                        JSONObject obj_legs = generalFunctions.getJsonObject(generalFunctions.getJsonArray("legs", generalFunctions.getJsonObject(obj_routes, 0).toString()), 0);
//
//
//                        String distance = "" + generalFunctions.getJsonValue("value",
//                                generalFunctions.getJsonValue("distance", obj_legs.toString()).toString());
//                        String time = "" + generalFunctions.getJsonValue("text",
//                                generalFunctions.getJsonValue("duration", obj_legs.toString()).toString());
//
//                        double distance_final = generalFunctions.parseDoubleValue(0.0, distance);
//
//
//                        if (userProfileJson != null && !generalFunctions.getJsonValue("eUnit", userProfileJson).equalsIgnoreCase("KMs")) {
//                            distance_final = distance_final * 0.000621371;
//                        } else {
//                            distance_final = distance_final * 0.00099999969062399994;
//                        }
//
//                        distance_final = generalFunctions.round(distance_final, 2);
//
//                        if (mcontext instanceof OnGoingTripDetailsActivity) {
//                            OnGoingTripDetailsActivity onGoingTripDetailsActivity = (OnGoingTripDetailsActivity) mcontext;
//                            onGoingTripDetailsActivity.setTimetext(String.format("%.2f", (float) distance_final) + "", time);
//                        }
//
//                        if (mcontext instanceof TrackOrderActivity) {
//                            TrackOrderActivity trackOrderActivity = (TrackOrderActivity) mcontext;
//                            trackOrderActivity.setEta(time, "" + distance_final, generalFunctions.getJsonValue("eUnit", userProfileJson));
//                        }
//                    }
//
//
//                    if (googleMap != null) {
//
//                        PolylineOptions lineOptions = generalFunctions.getGoogleRouteOptions(responseString, Utils.dipToPixels(mcontext, 5), mcontext.getResources().getColor(R.color.black));
//
//                        if (lineOptions != null) {
//                            if (route_polyLine != null) {
//                                route_polyLine.remove();
//                            }
//                            route_polyLine = googleMap.addPolyline(lineOptions);
//
//                        }
//                    }
//
//                }
//
//            }
//        });
        //   exeWebServer.execute();
    }


    public String getTimeTxt(int duration) {

        if (duration < 1) {
            duration = 1;
        }
        String durationTxt = "";
        String timeToreach = duration == 0 ? "--" : "" + duration;

        timeToreach = duration > 60 ? formatHoursAndMinutes(duration) : timeToreach;


        durationTxt = (duration < 60 ? generalFunctions.retrieveLangLBl("", "LBL_MINS_SMALL") : generalFunctions.retrieveLangLBl("", "LBL_HOUR_TXT"));

        durationTxt = duration == 1 ? generalFunctions.retrieveLangLBl("", "LBL_MIN_SMALL") : durationTxt;
        durationTxt = duration > 120 ? generalFunctions.retrieveLangLBl("", "LBL_HOURS_TXT") : durationTxt;

        Logger.d("durationTxt", "::" + durationTxt);
        return timeToreach + " " + durationTxt;
    }

    public static String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }

    @Override
    public void onTaskRun() {
        Utils.runGC();
        updateDirections();
    }

    public void changeUserLocation(Location location) {
        if (location != null) {
            this.userLocation = location;
        }
    }

    @Override
    public void searchResult(ArrayList<HashMap<String, String>> placelist, int selectedPos, String input) {

    }

    @Override
    public void resetOrAddDest(int selPos, String address, double latitude, double longitude, String isSkip) {

    }

    @Override
    public void directionResult(HashMap<String, String> directionlist) {

        String responseString = directionlist.get("routes");


        if (responseString != null && !responseString.equalsIgnoreCase("") && directionlist.get("distance") == null) {


            if (directionlist.get("status").equalsIgnoreCase("OK")) {

                JSONArray obj_routes = generalFunctions.getJsonArray("routes", responseString);
                if (obj_routes != null && obj_routes.length() > 0) {
                    JSONObject obj_legs = generalFunctions.getJsonObject(generalFunctions.getJsonArray("legs", generalFunctions.getJsonObject(obj_routes, 0).toString()), 0);


                    String distance = "" + generalFunctions.getJsonValue("value",
                            generalFunctions.getJsonValue("distance", obj_legs.toString()).toString());
                    String time = "" + generalFunctions.getJsonValue("value",
                            generalFunctions.getJsonValue("duration", obj_legs.toString()).toString());

                    double distance_final = generalFunctions.parseDoubleValue(0.0, distance);


                    if (userProfileJson != null && !generalFunctions.getJsonValue("eUnit", userProfileJson).equalsIgnoreCase("KMs")) {
                        distance_final = distance_final * 0.000621371;
                    } else {
                        distance_final = distance_final * 0.00099999969062399994;
                    }

                    distance_final = generalFunctions.round(distance_final, 2);

                    if (mcontext instanceof OnGoingTripDetailsActivity) {
                        OnGoingTripDetailsActivity onGoingTripDetailsActivity = (OnGoingTripDetailsActivity) mcontext;
                        onGoingTripDetailsActivity.setTimetext(String.format("%.2f", (float) distance_final) + "", time);
                    }

                    if (mcontext instanceof TrackOrderActivity) {
                        TrackOrderActivity trackOrderActivity = (TrackOrderActivity) mcontext;
                        trackOrderActivity.setEta(time, "" + distance_final, generalFunctions.getJsonValue("eUnit", userProfileJson));
                    }
                }


                if (googleMap != null) {

                    PolylineOptions lineOptions = generalFunctions.getGoogleRouteOptions(responseString, Utils.dipToPixels(mcontext, 5), mcontext.getResources().getColor(R.color.black));

                    if (lineOptions != null) {
                        if (route_polyLine != null) {
                            route_polyLine.remove();
                        }
                        route_polyLine = googleMap.addPolyline(lineOptions);

                    }
                }

            }

        } else {


            double distance_final = generalFunctions.parseDoubleValue(0.0, directionlist.get("distance"));

            if (userProfileJson != null && !generalFunctions.getJsonValue("eUnit", userProfileJson).equalsIgnoreCase("KMs")) {
                distance_final = distance_final * 0.000621371;
            } else {
                distance_final = distance_final * 0.00099999969062399994;
            }
            distance_final= generalFunctions.round(distance_final, 2);

            String  time = directionlist.get("duration");



            String timeToreach="1";
            int duration = (int) Math.round((generalFunctions.parseDoubleValue(0.0,
                    directionlist.get("duration")) / 60));
            if (duration < 1) {
                duration = 1;
            }

            timeToreach=  duration > 60 ? formatHoursAndMinutes(duration) : timeToreach;

            String durationTxt = (duration < 60 ? generalFunctions.retrieveLangLBl("", "LBL_MINS_SMALL") : generalFunctions.retrieveLangLBl("", "LBL_HOUR_TXT"));

            if (mcontext instanceof OnGoingTripDetailsActivity) {
                OnGoingTripDetailsActivity onGoingTripDetailsActivity = (OnGoingTripDetailsActivity) mcontext;
                onGoingTripDetailsActivity.setTimetext(String.format("%.2f", (float) distance_final) + "", timeToreach+" "+durationTxt);
            }
            if (mcontext instanceof TrackOrderActivity) {
                TrackOrderActivity trackOrderActivity = (TrackOrderActivity) mcontext;
                trackOrderActivity.setEta(timeToreach+" "+durationTxt, "" + distance_final, generalFunctions.getJsonValue("eUnit", userProfileJson));
            }



            if (googleMap != null) {

                PolylineOptions lineOptions = generalFunctions.getGoogleRouteOptions(responseString, Utils.dipToPixels(mcontext, 5), mcontext.getResources().getColor(R.color.black));

                if (lineOptions != null) {
                    if (route_polyLine != null) {
                        route_polyLine.remove();
                    }
                    route_polyLine = googleMap.addPolyline(lineOptions);

                }
            }

        }

        }

    @Override
    public void geoCodeAddressFound(String address, double latitude, double longitude, String geocodeobject) {

    }


}
