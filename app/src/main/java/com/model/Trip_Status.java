package com.model;

import java.util.ArrayList;

/**
 * Created by Esite on 19-04-2018.
 */

public class Trip_Status {

    String iTripId = "";
    String iActive = "";

    public String getRecepientName() {
        return RecepientName;
    }

    public void setRecepientName(String recepientName) {
        RecepientName = recepientName;
    }

    public String getRecepientNum() {
        return RecepientNum;
    }

    public void setRecepientNum(String recepientNum) {
        RecepientNum = recepientNum;
    }

    public String getePaymentByReceiver() {
        return ePaymentByReceiver;
    }

    public void setePaymentByReceiver(String ePaymentByReceiver) {
        this.ePaymentByReceiver = ePaymentByReceiver;
    }

    String ePaymentCollect = "";
    String iTripDeliveryLocationId = "";

    public String getRecepientAddress() {
        return RecepientAddress;
    }

    public void setRecepientAddress(String recepientAddress) {
        RecepientAddress = recepientAddress;
    }

    String RecepientName = "";

    public String getRecepientMaskNum() {
        return RecepientMaskNum;
    }

    public void setRecepientMaskNum(String recepientMaskNum) {
        RecepientMaskNum = recepientMaskNum;
    }

    String RecepientMaskNum = "";

    String RecepientNum = "";
    String RecepientAddress = "";
    String ePaymentByReceiver = "";

    public String getiTripId() {
        return iTripId;
    }

    public void setiTripId(String iTripId) {
        this.iTripId = iTripId;
    }

    public String getiActive() {
        return iActive;
    }

    public void setiActive(String iActive) {
        this.iActive = iActive;
    }

    public String getePaymentCollect() {
        return ePaymentCollect;
    }

    public void setePaymentCollect(String ePaymentCollect) {
        this.ePaymentCollect = ePaymentCollect;
    }

    public String getiTripDeliveryLocationId() {
        return iTripDeliveryLocationId;
    }

    public void setiTripDeliveryLocationId(String iTripDeliveryLocationId) {
        this.iTripDeliveryLocationId = iTripDeliveryLocationId;
    }

    public String getShowUpcomingLocArea() {
        return showUpcomingLocArea;
    }

    public void setShowUpcomingLocArea(String showUpcomingLocArea) {
        this.showUpcomingLocArea = showUpcomingLocArea;
    }

    public String getLBL_RECIPIENT() {
        return LBL_RECIPIENT;
    }

    public void setLBL_RECIPIENT(String LBL_RECIPIENT) {
        this.LBL_RECIPIENT = LBL_RECIPIENT;
    }

    public String getLBL_Status() {
        return LBL_Status;
    }

    public void setLBL_Status(String LBL_Status) {
        this.LBL_Status = LBL_Status;
    }

    public String getLBL_CANCELED_TRIP_TXT() {
        return LBL_CANCELED_TRIP_TXT;
    }

    public void setLBL_CANCELED_TRIP_TXT(String LBL_CANCELED_TRIP_TXT) {
        this.LBL_CANCELED_TRIP_TXT = LBL_CANCELED_TRIP_TXT;
    }

    public String getLBL_FINISHED_TRIP_TXT() {
        return LBL_FINISHED_TRIP_TXT;
    }

    public void setLBL_FINISHED_TRIP_TXT(String LBL_FINISHED_TRIP_TXT) {
        this.LBL_FINISHED_TRIP_TXT = LBL_FINISHED_TRIP_TXT;
    }

    public String getLBL_PICK_UP_INS() {
        return LBL_PICK_UP_INS;
    }

    public void setLBL_PICK_UP_INS(String LBL_PICK_UP_INS) {
        this.LBL_PICK_UP_INS = LBL_PICK_UP_INS;
    }

    public String getLBL_DELIVERY_INS() {
        return LBL_DELIVERY_INS;
    }

    public void setLBL_DELIVERY_INS(String LBL_DELIVERY_INS) {
        this.LBL_DELIVERY_INS = LBL_DELIVERY_INS;
    }

    public String getLBL_PACKAGE_DETAILS() {
        return LBL_PACKAGE_DETAILS;
    }

    public void setLBL_PACKAGE_DETAILS(String LBL_PACKAGE_DETAILS) {
        this.LBL_PACKAGE_DETAILS = LBL_PACKAGE_DETAILS;
    }

    public String getLBL_CALL_TXT() {
        return LBL_CALL_TXT;
    }

    public void setLBL_CALL_TXT(String LBL_CALL_TXT) {
        this.LBL_CALL_TXT = LBL_CALL_TXT;
    }

    public String getLBL_MESSAGE_ACTIVE_TRIP() {
        return LBL_MESSAGE_ACTIVE_TRIP;
    }

    public void setLBL_MESSAGE_ACTIVE_TRIP(String LBL_MESSAGE_ACTIVE_TRIP) {
        this.LBL_MESSAGE_ACTIVE_TRIP = LBL_MESSAGE_ACTIVE_TRIP;
    }

    public String getLBL_RESPONSIBLE_FOR_PAYMENT_TXT() {
        return LBL_RESPONSIBLE_FOR_PAYMENT_TXT;
    }

    public void setLBL_RESPONSIBLE_FOR_PAYMENT_TXT(String LBL_RESPONSIBLE_FOR_PAYMENT_TXT) {
        this.LBL_RESPONSIBLE_FOR_PAYMENT_TXT = LBL_RESPONSIBLE_FOR_PAYMENT_TXT;
    }

    String showUpcomingLocArea = "";

    public String getShowMobile() {
        return showMobile;
    }

    public ArrayList<Delivery_Data> getListOfDeliveryItems() {
        return listOfDeliveryItems;
    }

    public void setListOfDeliveryItems(ArrayList<Delivery_Data> listOfDeliveryItems) {
        this.listOfDeliveryItems = listOfDeliveryItems;
    }

    public void setShowMobile(String showMobile) {
        this.showMobile = showMobile;
    }

    public String getLBL_DROP_OFF_LOCATION_TXT() {
        return LBL_DROP_OFF_LOCATION_TXT;
    }

    public String getLBL_VIEW_SIGN_TXT() {
        return LBL_VIEW_SIGN_TXT;
    }

    public void setLBL_VIEW_SIGN_TXT(String LBL_VIEW_SIGN_TXT) {
        this.LBL_VIEW_SIGN_TXT = LBL_VIEW_SIGN_TXT;
    }

    public void setLBL_DROP_OFF_LOCATION_TXT(String LBL_DROP_OFF_LOCATION_TXT) {
        this.LBL_DROP_OFF_LOCATION_TXT = LBL_DROP_OFF_LOCATION_TXT;
    }

    String showMobile = "";
    String Receipent_Signature = "";

    public String getReceipent_Signature() {
        return Receipent_Signature;
    }

    public String getLBL_DELIVERY_STATUS_TXT() {
        return LBL_DELIVERY_STATUS_TXT;
    }

    public void setLBL_DELIVERY_STATUS_TXT(String LBL_DELIVERY_STATUS_TXT) {
        this.LBL_DELIVERY_STATUS_TXT = LBL_DELIVERY_STATUS_TXT;
    }

    public void setReceipent_Signature(String receipent_Signature) {

        Receipent_Signature = receipent_Signature;
    }
    /*Lables*/

    String LBL_RECIPIENT, LBL_Status, LBL_CANCELED_TRIP_TXT, LBL_FINISHED_TRIP_TXT, LBL_PICK_UP_INS, LBL_DELIVERY_INS, LBL_PACKAGE_DETAILS, LBL_CALL_TXT, LBL_MESSAGE_ACTIVE_TRIP, LBL_RESPONSIBLE_FOR_PAYMENT_TXT, LBL_DROP_OFF_LOCATION_TXT, LBL_VIEW_SIGN_TXT, LBL_DELIVERY_STATUS_TXT;


    /*Array Of delivery Details*/

    ArrayList<Delivery_Data> listOfDeliveryItems = new ArrayList<>();

}
