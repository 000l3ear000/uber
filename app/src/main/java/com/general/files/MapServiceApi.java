package com.general.files;

import android.content.Context;

import com.melevicarbrasil.usuario.deliverAll.MapDelegate;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MapServiceApi {
    static String GOOGLE_API_REPLACEMENT_URL = MyApp.getInstance().generalFun.retrieveValue("GOOGLE_API_REPLACEMENT_URL");
    static String TSITE_DB = MyApp.getInstance().generalFun.retrieveValue("TSITE_DB");

    public static void getAutoCompleteService(Context context, HashMap<String, String> mapdictionary, MapDelegate mapDelegate) {
        GeneralFunctions generalFunc = new GeneralFunctions(context);
        String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        String url = null;
        String mapStrategy = generalFunc.getJsonValue("MAPS_API_REPLACEMENT_STRATEGY", userProfileJson);


        try {

            url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + URLEncoder.encode(mapdictionary.get("input"), "UTF-8") + "&key=" + serverKey +
                    "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true&sessiontoken=" + mapdictionary.get("session_token");

            if (!mapdictionary.get("latitude").equalsIgnoreCase("") && !mapdictionary.get("longitude").equalsIgnoreCase("")) {
                url = url + "&location=" + mapdictionary.get("latitude") + "," + mapdictionary.get("longitude") + "&radius=20";
            }


            if (mapStrategy.equalsIgnoreCase("Advance")) {
                url = GOOGLE_API_REPLACEMENT_URL + "autocomplete?language_code=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&latitude=" + mapdictionary.get("latitude") +
                        "&longitude=" + mapdictionary.get("longitude") + "&search_query=" + mapdictionary.get("input") + "&max_latitude=" + mapdictionary.get("latitude") + "&max_longitude=" + mapdictionary.get("longitude") + "&min_latitude=" + mapdictionary.get("latitude") + "&min_longitude=" + mapdictionary.get("longitude") + "&session_token=" + mapdictionary.get("session_token");
                url = url + "&TSITE_DB=" + TSITE_DB;

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (url == null) {
            return;
        }
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(context, url, true);

        exeWebServer.setDataResponseListener(responseString -> {

            ArrayList<HashMap<String, String>> placelist = new ArrayList<>();
            if (responseString.equalsIgnoreCase("")) {
                placelist.clear();
                mapDelegate.searchResult(placelist, GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), mapdictionary.get("input"));
                return;
            }


            if (generalFunc.getJsonValue(Utils.action_str, responseString) != null && generalFunc.getJsonValue(Utils.action_str, responseString).equalsIgnoreCase("0")) {
                placelist.clear();
                mapDelegate.searchResult(placelist, GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), mapdictionary.get("input"));
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl(generalFunc.getJsonValue(Utils.message_str, responseString), generalFunc.getJsonValue(Utils.message_str, responseString)));

                return;
            }


            if (mapStrategy.equalsIgnoreCase("Advance")) {


                JSONArray predictionsArr = generalFunc.getJsonArray("data", responseString);

                if (predictionsArr == null) {
                    placelist.clear();
                    mapDelegate.searchResult(placelist, GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), mapdictionary.get("input"));
                    return;
                }


                placelist.clear();
                for (int i = 0; i < predictionsArr.length(); i++) {
                    JSONObject item = generalFunc.getJsonObject(predictionsArr, i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("main_text", generalFunc.getJsonValueStr("place_title", item).equals("") ? generalFunc.getJsonValueStr("address", item) : generalFunc.getJsonValueStr("place_title", item));
                    map.put("secondary_text", generalFunc.getJsonValueStr("place_title", item).equals("") ? "" : generalFunc.getJsonValueStr("place_sub_title", item));
                    map.put("Place_id", generalFunc.getJsonValueStr("PlaceId", item));
                    map.put("description", generalFunc.getJsonValueStr("address", item));
                    map.put("session_token", mapdictionary.get("session_token") != null ? mapdictionary.get("session_token") : "");
                    map.put("latitude", generalFunc.getJsonValueStr("latitude", item));
                    map.put("longitude", generalFunc.getJsonValueStr("longitude", item));
                    map.put("status", "OK");
                    placelist.add(map);
                    mapDelegate.searchResult(placelist, GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), mapdictionary.get("input"));
                }

            } else {

                if (generalFunc.getJsonValue("status", responseString).equals("OK")) {
                    JSONArray predictionsArr = generalFunc.getJsonArray("predictions", responseString);
                    placelist.clear();
                    for (int i = 0; i < predictionsArr.length(); i++) {
                        JSONObject item = generalFunc.getJsonObject(predictionsArr, i);

                        if (!generalFunc.getJsonValue("place_id", item.toString()).equals("")) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject structured_formatting = generalFunc.getJsonObject("structured_formatting", item);
                            map.put("main_text", generalFunc.getJsonValueStr("main_text", structured_formatting));
                            map.put("secondary_text", generalFunc.getJsonValueStr("secondary_text", structured_formatting));
                            map.put("Place_id", generalFunc.getJsonValueStr("place_id", item));
                            map.put("description", generalFunc.getJsonValueStr("description", item));
                            map.put("session_token", mapdictionary.get("session_token"));
                            map.put("status", "OK");
                            placelist.add(map);
                        }
                    }
                    mapDelegate.searchResult(placelist, GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), mapdictionary.get("input"));

                } else {
                    placelist.clear();
                    mapDelegate.searchResult(placelist, GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), mapdictionary.get("input"));

                }

            }

        });
        exeWebServer.execute();
    }


    public static void getPlaceDetailsService(Context context, HashMap<String, String> mapdictionary, MapDelegate mapDelegate) {
        GeneralFunctions generalFunc = new GeneralFunctions(context);
        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        String mapStrategy = generalFunc.getJsonValue("MAPS_API_REPLACEMENT_STRATEGY", userProfileJson);
        if (mapdictionary.get("latitude") == null || mapdictionary.get("latitude").equalsIgnoreCase("")) {
            String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);


            String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + mapdictionary.get("Place_id") + "&key=" + serverKey +
                    "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true&fields=formatted_address,name,geometry&sessiontoken=" + mapdictionary.get("session_token");

            if (mapStrategy.equalsIgnoreCase("Advance")) {
                url = GOOGLE_API_REPLACEMENT_URL + "placedetails?place_id=" + mapdictionary.get("Place_id") + "&language_code=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&session_token=" + mapdictionary.get("session_token");
                url = url + "&TSITE_DB=" + TSITE_DB;

            }


            ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(context, url, true);
            exeWebServer.setLoaderConfig(context, true, generalFunc);
            exeWebServer.setDataResponseListener(responseString -> {
                if (generalFunc.getJsonValue(Utils.action_str, responseString) != null && generalFunc.getJsonValue(Utils.action_str, responseString).equalsIgnoreCase("0")) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl(generalFunc.getJsonValue(Utils.message_str, responseString), generalFunc.getJsonValue(Utils.message_str, responseString)));
                    return;
                }

                if (mapStrategy.equalsIgnoreCase("Advance")) {
                    //JSONObject resultObj = generalFunc.getJsonObject("result", responseString);
                    // JSONObject geometryObj = generalFunc.getJsonObject("geometry", resultObj);
                    String latitude = generalFunc.getJsonValue("latitude", responseString);
                    String longitude = generalFunc.getJsonValue("longitude", responseString);
                    double lati = generalFunc.parseDoubleValue(0.0, latitude);
                    double longi = generalFunc.parseDoubleValue(0.0, longitude);
                    mapDelegate.resetOrAddDest(GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), generalFunc.getJsonValue("address", responseString), lati, longi, "" + false);

                } else {
                    if (generalFunc.getJsonValue("status", responseString).equals("OK")) {
                        JSONObject resultObj = generalFunc.getJsonObject("result", responseString);
                        JSONObject geometryObj = generalFunc.getJsonObject("geometry", resultObj);
                        JSONObject locationObj = generalFunc.getJsonObject("location", geometryObj);
                        String latitude = generalFunc.getJsonValueStr("lat", locationObj);
                        String longitude = generalFunc.getJsonValueStr("lng", locationObj);
                        double lati = generalFunc.parseDoubleValue(0.0, latitude);
                        double longi = generalFunc.parseDoubleValue(0.0, longitude);
                        mapDelegate.resetOrAddDest(GeneralFunctions.parseIntegerValue(0, mapdictionary.get("selectedPos")), mapdictionary.get("description"), lati, longi, "" + false);

                    }
                }

            });
            exeWebServer.execute();
        }
//        else if (!lat.equals("") && !lng.equalsIgnoreCase("")) {
//
//            double lati = generalFunc.parseDoubleValue(0.0, lat);
//            double longi = generalFunc.parseDoubleValue(0.0, lng);
//            resetOrAddDest(selectedPos, address, lati, longi, "" + false);
//
//        }

    }



    public static void getDirectionservice(Context context, HashMap<String, String> mapdictionary, MapDelegate mapDelegate, ArrayList<String> data_waypoints,boolean isLoader) {

        GeneralFunctions generalFunc = new GeneralFunctions(context);
        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        String mapStrategy = generalFunc.getJsonValue("MAPS_API_REPLACEMENT_STRATEGY", userProfileJson);
        String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);


        String url = "https://maps.googleapis.com/maps/api/directions/json?" + mapdictionary.get("parameters") + "&key=" + serverKey + "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true" + ((mapdictionary.get("toll_avoid") != null && !mapdictionary.get("toll_avoid").equalsIgnoreCase("") ? "&avoid=tolls" : ""));


        if (mapStrategy.equalsIgnoreCase("Advance")) {


            JSONArray jsonArray = new JSONArray();
            if (data_waypoints != null && data_waypoints.size() > 0) {
                for (int i = 0; i < data_waypoints.size(); i++) {
                    jsonArray.put(data_waypoints.get(i));
                }


            }
            url = GOOGLE_API_REPLACEMENT_URL + "direction?language_code=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&source_latitude=" + mapdictionary.get("s_latitude") + "&source_longitude=" + mapdictionary.get("s_longitude") +
                    "&dest_latitude=" + mapdictionary.get("d_latitude") + "&dest_longitude=" + mapdictionary.get("d_longitude")
                    + "&session_token=" + "&sensor=true&toll_avoid=" + (mapdictionary.get("toll_avoid") != null ? mapdictionary.get("toll_avoid") : "" + "&waypoints=" + jsonArray);
            url = url + "&TSITE_DB=" + TSITE_DB;
        }


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(context, url, true);
        if(isLoader)
        {
            exeWebServer.setLoaderConfig(context,true,generalFunc);
        }
        exeWebServer.setDataResponseListener(new ExecuteWebServerUrl.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {


                if (responseString != null && !responseString.equals("")) {


                    if (generalFunc.getJsonValue(Utils.action_str, responseString) != null && generalFunc.getJsonValue(Utils.action_str, responseString).equalsIgnoreCase("0")) {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl(generalFunc.getJsonValue(Utils.message_str, responseString), generalFunc.getJsonValue(Utils.message_str, responseString)));
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("routes", "");
                        mapDelegate.directionResult(hashMap);
                        return;
                    }

                    String status = generalFunc.getJsonValue("status", responseString);

                    if (mapStrategy.equalsIgnoreCase("Advance")) {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("routes", generalFunc.getJsonArray("data", responseString) + "");
                        hashMap.put("waypoint_order", generalFunc.getJsonArray("waypoint_order", responseString) + "");
                        hashMap.put("SourceAddress", generalFunc.getJsonValue("SourceAddress", responseString) + "");
                        hashMap.put("DestinationAddress", generalFunc.getJsonValue("DestinationAddress", responseString) + "");
                        hashMap.put("distance", generalFunc.getJsonValue("distance", responseString) + "");
                        hashMap.put("duration", generalFunc.getJsonValue("duration", responseString) + "");
                        hashMap.put("s_latitude", mapdictionary.get("s_latitude"));
                        hashMap.put("s_longitude", mapdictionary.get("s_longitude"));
                        hashMap.put("d_latitude", mapdictionary.get("d_latitude"));
                        hashMap.put("d_longitude", mapdictionary.get("d_longitude"));
                        mapDelegate.directionResult(hashMap);


                    } else {
                        if (responseString != null && !responseString.equals("")) {
                            status = generalFunc.getJsonValue("status", responseString);
                            if (status.equals("OK")) {
                                HashMap<String, String> hashMap = new HashMap<>();
//                                hashMap.put("routes", generalFunc.getJsonArray("routes", responseString) + "");
                                hashMap.put("routes", responseString);
                                hashMap.put("status", "OK");
                                mapDelegate.directionResult(hashMap);

                            } else {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("status", "");
                                hashMap.put("routes", "");
                                mapDelegate.directionResult(hashMap);
                            }
                        }
                    }

                }
            }
        });
        exeWebServer.execute();


    }

    public static void getGeoCodeService(Context context, HashMap<String, String> mapdictionary, MapDelegate mapDelegate) {


        GeneralFunctions generalFunc = new GeneralFunctions(context);
        String serverKey = generalFunc.retrieveValue(Utils.GOOGLE_SERVER_ANDROID_PASSENGER_APP_KEY);
        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

       String mapStrategy = generalFunc.getJsonValue("MAPS_API_REPLACEMENT_STRATEGY", userProfileJson);
        String url_str = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mapdictionary.get("latitude") + "," + mapdictionary.get("longitude") + "&key=" + serverKey + "&language=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&sensor=true";


        if (mapStrategy.equalsIgnoreCase("Advance")) {
            url_str = GOOGLE_API_REPLACEMENT_URL + "reversegeocode?language_code=" + generalFunc.retrieveValue(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY) + "&latitude=" + mapdictionary.get("latitude") + "&longitude=" + mapdictionary.get("longitude") + "&sensor=true";
            url_str = url_str + "&TSITE_DB=" + TSITE_DB;
        }

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(context, url_str, true);

        exeWebServer.setDataResponseListener(responseString -> {


            if (responseString != null && !responseString.equals("")) {

                if (generalFunc.getJsonValue(Utils.action_str, responseString) != null && generalFunc.getJsonValue(Utils.action_str, responseString).equalsIgnoreCase("0")) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl(generalFunc.getJsonValue(Utils.message_str, responseString), generalFunc.getJsonValue(Utils.message_str, responseString)));
                    return;
                }


                if (mapStrategy.equalsIgnoreCase("Advance")) {


                    mapDelegate.geoCodeAddressFound(generalFunc.getJsonValue("address", responseString), GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("latitude", responseString)),
                            GeneralFunctions.parseDoubleValue(0, generalFunc.getJsonValue("longitude", responseString))
                            , responseString);

                } else {
                    String status = generalFunc.getJsonValue("status", responseString);

                    if (status.equals("OK")) {
                        String address_loc = "";

                        JSONArray arr = generalFunc.getJsonArray("results", responseString);

                        if (arr.length() > 0) {

                            JSONObject obj = generalFunc.getJsonObject(arr, 0);

                            String formatted_address = generalFunc.getJsonValueStr("formatted_address", obj);

//                            Logger.d("formatted_address","::"+formatted_address);
                            String[] addressArr = formatted_address.split(",");

                            boolean first_input = true;
                            for (int i = 0; i < addressArr.length; i++) {
                                if (!addressArr[i].contains("Unnamed") && !addressArr[i].contains("null")) {

                                    if (i == 0 && addressArr[0].matches("^[0-9]+$")) {
                                        continue;
                                    }
                                    if (first_input) {
                                        address_loc = addressArr[i];
                                        first_input = false;
                                    } else {
                                        address_loc = address_loc + "," + addressArr[i];
                                    }

                                }
                            }

                            if (address_loc.trim().equalsIgnoreCase("")) {
                                address_loc = formatted_address;
                            }


                            mapDelegate.geoCodeAddressFound(address_loc, GeneralFunctions.parseDoubleValue(0, mapdictionary.get("latitude")),
                                    GeneralFunctions.parseDoubleValue(0, mapdictionary.get("longitude"))
                                    , responseString);

                        }

                    }

                }
            }


        });
        exeWebServer.execute();
    }

}
