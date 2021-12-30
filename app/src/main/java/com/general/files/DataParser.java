package com.general.files;

import android.content.Context;
import android.graphics.Bitmap;

import com.melevicarbrasil.usuario.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.model.Multi_Delivery_Data;
import com.model.Multi_Dest_Info_Detail_Data;
import com.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// Draw Route
public class DataParser {

    public String distance, time;
    GeneralFunctions generalFunc;
    Context mContext;

    ArrayList<Multi_Delivery_Data> wayPointslist = new ArrayList<>();
    ArrayList<Multi_Delivery_Data> destPointlist = new ArrayList<>();
    ArrayList<Multi_Delivery_Data> list = new ArrayList<>();
    ArrayList<Multi_Delivery_Data> finalPointlist = new ArrayList<>();
    ArrayList<Multi_Dest_Info_Detail_Data> dest_details_Array = new ArrayList<Multi_Dest_Info_Detail_Data>();
    ArrayList<Marker> markerArrayList = new ArrayList<>();
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    private GoogleMap gMap;

    public DataParser(Context mContext, ArrayList<Multi_Delivery_Data> list, ArrayList<Multi_Delivery_Data> wayPointslist, ArrayList<Multi_Delivery_Data> destPointlist, ArrayList<Multi_Delivery_Data> finalPointlist, ArrayList<Multi_Dest_Info_Detail_Data> dest_details_array) {
        this.mContext = mContext;
        this.generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        this.list = list;
        this.wayPointslist = wayPointslist;
        this.destPointlist = destPointlist;
        this.finalPointlist = finalPointlist;
        this.dest_details_Array = dest_details_array;
    }

    public DataParser(Context mContext, ArrayList<Multi_Delivery_Data> list, ArrayList<Multi_Delivery_Data> wayPointslist, ArrayList<Multi_Delivery_Data> destPointlist, ArrayList<Multi_Delivery_Data> finalPointlist, ArrayList<Multi_Dest_Info_Detail_Data> dest_details_array, ArrayList<Marker> markerArrayList, GoogleMap gMap, LatLngBounds.Builder builder) {
        this.mContext = mContext;
        this.generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        this.list = list;
        this.wayPointslist = wayPointslist;
        this.destPointlist = destPointlist;
        this.finalPointlist = finalPointlist;
        this.dest_details_Array = dest_details_array;
        this.markerArrayList = markerArrayList;
        this.gMap = gMap;
        this.builder = builder;
    }

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and longitude
     */
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        Logger.d("Api", "jObject" + jObject.toString());
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {

            distance = "";
            long distanceVal = 0;
            long timeVal = 0;
            time = "";

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {


                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();


                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {

                    JSONObject distanceObj = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    Multi_Dest_Info_Detail_Data dt = new Multi_Dest_Info_Detail_Data();

                    long dis = generalFunc.parseLongValue(0, generalFunc.getJsonValue("value",
                            distanceObj.toString()));
                    dt.setDistance(dis);
                    distanceVal = distanceVal + (dis);

                    if (j == 0) {
                        list.get(0).setDistance(dis);
                    } else if (j == jLegs.length() - 1) {
                        destPointlist.get(0).setDistance(dis);
                    } else {
                        wayPointslist.get(j - 1).setDistance(dis);
                    }


                    JSONObject durationObj = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    long vTime = generalFunc.parseLongValue(0, generalFunc.getJsonValue("value", durationObj.toString()));

                    dt.setTime(vTime);

                    timeVal = timeVal + (vTime);

                    dest_details_Array.add(dt);

                    if (j == 0) {
                        list.get(0).setTime(vTime);
                    } else if (j == jLegs.length() - 1) {
                        destPointlist.get(0).setTime(vTime);
                    } else {
                        wayPointslist.get(j - 1).setTime(vTime);
                    }


                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    int pinIcon = R.drawable.pin_dest_yellow;

                    String pinText = generalFunc.retrieveLangLBl("TO", "LBL_MULTI_TO_TXT");
                    if (jLegs.length() > 1) {
                        pinText = "" + (j + 1);
                    }

                    Bitmap marker_time_ic = generalFunc.writeTextOnDrawable(mContext, pinIcon, pinText, false, R.string.defaultFont);

                    JSONObject end_location = ((JSONObject) jLegs.get(j)).getJSONObject("end_location");

                    double lat = generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lat", end_location.toString()).toString());
                    double lng = generalFunc.parseDoubleValue(0.0, generalFunc.getJsonValue("lng",
                            end_location.toString()).toString());

                    if (markerArrayList.size()>j+1 && markerArrayList.get(j+1)!=null) {
                        markerArrayList.get(j+1).remove();
                    }

                    Marker dest_marker = gMap.addMarker(
                            new MarkerOptions().position(new LatLng(lat, lng))
                                    .icon(BitmapDescriptorFactory.fromBitmap(marker_time_ic)));
                    builder.include(dest_marker.getPosition());
                    markerArrayList.add(dest_marker);


                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude));
                            hm.put("lng", Double.toString((list.get(l)).longitude));

                            path.add(hm);
                        }
                    }
                    routes.add(path);

                    //     distance = "" + (distanceVal / 1000);
                    //  time = "" + (timeVal / 60);
                    distance = "" + (distanceVal);

                    time = "" + (timeVal);

                    // get way points rested ordering
                }
            }

            JSONArray waypoint_order = ((JSONObject) jRoutes.get(0)).getJSONArray("waypoint_order");

            for (int l = 0; l < waypoint_order.length(); l++) {

                int ordering = generalFunc.parseIntegerValue(0, waypoint_order.get(l).toString());
                Logger.d("Api", "waypoint_order sequence : ordering" + ordering);
                wayPointslist.get(l).setSequenceId(ordering);
                destPointlist.get(0).setSequenceId(waypoint_order.length());
            }

            finalPointlist.addAll(wayPointslist);
            finalPointlist.addAll(destPointlist);

            if (finalPointlist.size() > 0) {
                Collections.sort(finalPointlist, new MapComparator("SequenceId"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return routes;
    }

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and longitude
     */
    public void getDistanceArray(JSONObject jObject) {
        JSONArray jRoutes;
        JSONArray jLegs;
        try {

            distance = "";
            long distanceVal = 0;
            long timeVal = 0;
            time = "";

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {


                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {

                    JSONObject distanceObj = ((JSONObject) jLegs.get(j)).getJSONObject("distance");


                    Multi_Dest_Info_Detail_Data dt = new Multi_Dest_Info_Detail_Data();

                    long dis = generalFunc.parseLongValue(0, generalFunc.getJsonValue("value",
                            distanceObj.toString()));
                    dt.setDistance(dis);

                    distanceVal = distanceVal + (dis);

                    if (j == 0) {
                        list.get(0).setDistance(dis);
                    } else if (j == jLegs.length() - 1) {
                        destPointlist.get(0).setDistance(dis);
                    } else {
                        wayPointslist.get(j - 1).setDistance(dis);
                    }


                    JSONObject durationObj = ((JSONObject) jLegs.get(j)).getJSONObject("duration");

                    long vTime = generalFunc.parseLongValue(0, generalFunc.getJsonValue("value",
                            durationObj.toString()));

                    dt.setTime(vTime);

                    timeVal = timeVal + (vTime);

                    dest_details_Array.add(dt);

                    if (j == 0) {
                        list.get(0).setTime(vTime);
                    } else if (j == jLegs.length() - 1) {
                        destPointlist.get(0).setTime(vTime);
                    } else {
                        wayPointslist.get(j - 1).setTime(vTime);
                    }

                    //     distance = "" + (distanceVal / 1000);
                    //  time = "" + (timeVal / 60);
                    distance = "" + (distanceVal);
                    time = "" + (timeVal);

                    // get way points rested ordering
                }
            }

            JSONArray waypoint_order = ((JSONObject) jRoutes.get(0)).getJSONArray("waypoint_order");

            for (int l = 0; l < waypoint_order.length(); l++) {

                int ordering = generalFunc.parseIntegerValue(0, waypoint_order.get(l).toString());
                Logger.d("Api", "waypoint_order sequence : ordering" + ordering);
                wayPointslist.get(l).setSequenceId(ordering);
                destPointlist.get(0).setSequenceId(waypoint_order.length());
            }

            finalPointlist.addAll(wayPointslist);
            finalPointlist.addAll(destPointlist);

            if (finalPointlist.size() > 0) {
                Collections.sort(finalPointlist, new MapComparator("SequenceId"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}

