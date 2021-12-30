package com.model;

import com.google.android.gms.maps.model.LatLng;
import com.utils.Utils;

import java.io.Serializable;

/**
 * Created by Esite on 11-04-2018.
 */

public class Stop_Over_Points_Data implements Serializable {
    boolean isTempLoc = false;
    String destAddress = "";
    String destTempAddress = "";
    Double destLat = 0.0, destLong = 0.0;
    LatLng destLatLong;
    int listPos;
    String hintLable = "";
    long distance = 0;
    long time = 0;
    String ID = "";
    int SequenceId = 0;
    boolean isDestination = false;
    boolean isAddressAdded = false;
    String addressHintTxt = "";
    String currentSearchQuery = "";
    String session_token = "";
    boolean removable;

    public String getDestTempAddress() {
        return destTempAddress;
    }

    public void setDestTempAddress(String destTempAddress) {
        setTempLoc(Utils.checkText(destTempAddress) ? true : false);
        this.destTempAddress = destTempAddress;
    }

    public boolean isTempLoc() {
        return isTempLoc;
    }

    public void setTempLoc(boolean tempLoc) {
        isTempLoc = tempLoc;
    }

    public String getCurrentSearchQuery() {
        return currentSearchQuery;
    }

    public void setCurrentSearchQuery(String currentSearchQuery) {
        this.currentSearchQuery = currentSearchQuery;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public boolean isRemovable() {
        return removable;
    }

    public String getAddressHintTxt() {
        return addressHintTxt;
    }

    public void setAddressHintTxt(String addressHintTxt) {
        this.addressHintTxt = addressHintTxt;
    }

    public boolean getRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }


    public String getHintLable() {
        return hintLable;
    }

    public void setHintLable(String hintLable) {
        this.hintLable = hintLable;
    }

    public int getListPos() {
        return listPos;
    }

    public void setListPos(int listPos) {
        this.listPos = listPos;
    }

    // Check Distance of all addresses

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        setTempLoc(false);
        this.destAddress = destAddress;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getDestLat() {
        return destLat;
    }

    public void setDestLat(Double destLat) {
        this.destLat = destLat;
    }

    public boolean isAddressAdded() {
        return isAddressAdded;
    }

    public void setAddressAdded(boolean addressAdded) {
        isAddressAdded = addressAdded;
    }

    public Double getDestLong() {
        return destLong;
    }

    public void setDestLong(Double destLong) {
        this.destLong = destLong;
    }

    public LatLng getDestLatLong() {
        return destLatLong;
    }

    public void setDestLatLong(LatLng destLatLong) {
        this.destLatLong = destLatLong;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getSequenceId() {
        return SequenceId;
    }

    public void setSequenceId(int sequenceId) {
        SequenceId = sequenceId;
    }

    public boolean isDestination() {
        return isDestination;
    }

    public void setDestination(boolean destination) {
        isDestination = destination;
    }

}
