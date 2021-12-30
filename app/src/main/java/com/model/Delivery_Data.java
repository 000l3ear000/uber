package com.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Esite on 02-04-2018.
 */

public class Delivery_Data implements Serializable {
    String iDeliveryFieldId = "";

    public String getAddressHintTxt() {
        return addressHintTxt;
    }

    public void setAddressHintTxt(String addressHintTxt) {
        this.addressHintTxt = addressHintTxt;
    }

    String eInputType = "";
    String addressHintTxt = "";

    public String gettDesc() {
        return tDesc;
    }

    public String geteAllowFloat() {
        return eAllowFloat;
    }

    public void seteAllowFloat(String eAllowFloat) {
        this.eAllowFloat = eAllowFloat;
    }

    public void settDesc(String tDesc) {
        this.tDesc = tDesc;

    }

    public String geteRequired() {
        return eRequired;
    }

    public void seteRequired(String eRequired) {
        this.eRequired = eRequired;
    }

    String isFromLoc = "";
    String tDesc = "";
    String eAllowFloat = "";
    String eRequired = "";

    public String getDestAddress() {
        return destAddress;
    }

    public String getIsFromLoc() {
        return isFromLoc;
    }

    public void setIsFromLoc(String isFromLoc) {
        this.isFromLoc = isFromLoc;
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

    public String getPaymentDoneBy() {
        return paymentDoneBy;
    }

    public void setPaymentDoneBy(String paymentDoneBy) {
        this.paymentDoneBy = paymentDoneBy;
    }

    String vFieldName = "";
    String vValue = "";

    public String getvFieldValue() {
        return vFieldValue;
    }

    public void setvFieldValue(String vFieldValue) {
        this.vFieldValue = vFieldValue;
    }

    JSONArray Options;
    String selectedOptionID = "";
    int itemID;
    String vFieldValue = "";

    public String getiDeliveryFieldId() {
        return iDeliveryFieldId;
    }

    public void setiDeliveryFieldId(String iDeliveryFieldId) {
        this.iDeliveryFieldId = iDeliveryFieldId;
    }

    public String geteInputType() {
        return eInputType;
    }

    public void seteInputType(String eInputType) {
        this.eInputType = eInputType;
    }

    public String getvFieldName() {
        return vFieldName;
    }

    public String getvValue() {
        return vValue;
    }

    public void setvValue(String vValue) {
        this.vValue = vValue;
    }


    public void setvFieldName(String vFieldName) {
        this.vFieldName = vFieldName;
    }

    public JSONArray getOptions() {
        return Options;
    }

    public void setOptions(JSONArray options) {
        Options = options;
    }

    public String getSelectedOptionID() {
        return selectedOptionID;
    }

    public void setSelectedOptionID(String selectedOptionID) {
        this.selectedOptionID = selectedOptionID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    // Multi Fields

    String destAddress = "";
    Double destLat = 0.0, destLong = 0.0;
    LatLng destLatLong;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
// Check Distance of all addresses

    long distance = 0;
    long time = 0;
    String ID = "";
    int SequenceId = 0;
    boolean isDestination = false;
    String paymentDoneBy = "No";

    /*For Trip History*/

    public String getiTripDeliveryLocationId() {
        return iTripDeliveryLocationId;
    }

    public void setiTripDeliveryLocationId(String iTripDeliveryLocationId) {
        this.iTripDeliveryLocationId = iTripDeliveryLocationId;
    }


    public String gettSaddress() {
        return tSaddress;
    }

    public void settSaddress(String tSaddress) {
        this.tSaddress = tSaddress;
    }

    public String gettDaddress() {
        return tDaddress;
    }

    public void settDaddress(String tDaddress) {
        this.tDaddress = tDaddress;
    }

    public String getePaymentByReceiver() {
        return ePaymentByReceiver;
    }

    public void setePaymentByReceiver(String ePaymentByReceiver) {
        this.ePaymentByReceiver = ePaymentByReceiver;
    }

    public double gettStartLat() {
        return tStartLat;
    }

    public void settStartLat(double tStartLat) {
        this.tStartLat = tStartLat;
    }

    public double gettStartLong() {
        return tStartLong;
    }

    public void settStartLong(double tStartLong) {
        this.tStartLong = tStartLong;
    }

    public double gettDestLat() {
        return tDestLat;
    }

    public void settDestLat(double tDestLat) {
        this.tDestLat = tDestLat;
    }

    public double gettDestLong() {
        return tDestLong;
    }

    public void settDestLong(double tDestLong) {
        this.tDestLong = tDestLong;
    }

    double tStartLong = 0.0;
    double tDestLat = 0.0;
    double tDestLong = 0.0;

    public String geteCancelled() {
        return eCancelled;
    }

    public void seteCancelled(String eCancelled) {
        this.eCancelled = eCancelled;
    }

    public String getePaymentCollect() {
        return ePaymentCollect;
    }

    public void setePaymentCollect(String ePaymentCollect) {
        this.ePaymentCollect = ePaymentCollect;
    }

    public String getReceipent_Signature() {
        return Receipent_Signature;
    }

    public void setReceipent_Signature(String receipent_Signature) {
        Receipent_Signature = receipent_Signature;
    }

    public String getvDeliveryConfirmCode() {
        return vDeliveryConfirmCode;
    }

    public void setvDeliveryConfirmCode(String vDeliveryConfirmCode) {
        this.vDeliveryConfirmCode = vDeliveryConfirmCode;
    }

    String tSaddress = "";
    String tDaddress = "";
    String ePaymentByReceiver = "";
    String iTripDeliveryLocationId = "";
    double tStartLat = 0.0;

    String eCancelled = "";
    String ePaymentCollect = "";
    String Receipent_Signature = "";
    String vDeliveryConfirmCode = "";

}
