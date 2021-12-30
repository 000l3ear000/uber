package com.melevicarbrasil.usuario.deliverAll;

import java.util.ArrayList;
import java.util.HashMap;

public interface MapDelegate {

    public  void searchResult(ArrayList<HashMap<String, String>> placelist, int selectedPos, String input);
    public   void resetOrAddDest(int selPos, String address, double latitude, double longitude, String isSkip);
    public  void directionResult(HashMap<String, String> directionlist);
    public void  geoCodeAddressFound(String address, double latitude, double longitude, String geocodeobject);
}
