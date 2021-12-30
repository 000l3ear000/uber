package com.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Esite on 11-04-2018.
 */

public class Multi_Delivery_Data implements Serializable {

    ArrayList<Delivery_Data> dt = new ArrayList<>();

    String destAddress = "";
    Double destLat = 0.0, destLong = 0.0;
    LatLng destLatLong;
    int listPos;
    String hintLable = "";
    String LBL_TO_TXT = "";
    String LBL_FR_TXT = "";


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

    public String getIsFromLoc() {
        return isFromLoc;
    }

    public void setIsFromLoc(String isFromLoc) {
        this.isFromLoc = isFromLoc;
    }


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    String paymentType = "No";
    String isFromLoc = "No";

    public String getePaymentByReceiver() {
        return ePaymentByReceiver;
    }

    public void setePaymentByReceiver(String ePaymentByReceiver) {
        this.ePaymentByReceiver = ePaymentByReceiver;
    }

    String ePaymentByReceiver = "No";


    // Check Distance of all addresses

    public boolean isDetailsAdded() {
        return isDetailsAdded;
    }

    public void setDetailsAdded(boolean detailsAdded) {
        isDetailsAdded = detailsAdded;
    }

    long distance = 0;
    long time = 0;
    String ID = "";

    public ArrayList<Delivery_Data> getDt() {
        return dt;
    }

    public void setDt(ArrayList<Delivery_Data> dt) {
        this.dt = dt;
    }

    public String getDestAddress() {
        return destAddress;
    }

    int SequenceId = 0;
    boolean isDestination = false;
    boolean isDetailsAdded = false;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public Double getDestLat() {
        return destLat;
    }

    public void setDestLat(Double destLat) {
        this.destLat = destLat;
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

    public void setFRLable(String LBL_FR_TXT) {
        this.LBL_FR_TXT = LBL_FR_TXT;
    }

    public String getFRLable() {
        return LBL_FR_TXT;
    }

    public void setTOLable(String LBL_TO_TXT) {
        this.LBL_TO_TXT = LBL_TO_TXT;
    }

    public String getToLable() {
        return LBL_TO_TXT;
    }
}
