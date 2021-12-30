package com.adapter.files;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class CategoryListItem extends RealmObject implements Serializable {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public int type;
    public String text;

    public int sectionPosition;
    public int listPosition;
    public int CountSubItems;

    public String vTitle = "";
    public String vDesc = "";
    public String iVehicleCategoryId = "";
    public String vCategory = "";
    public String eFareType = "";
    public String eFareValue = "";
    public String fFixedFare = "";
    public String fPricePerHour = "";
    public String fMinHour = "";
    public String iVehicleTypeId = "";

    public boolean isAdd;


    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    @Ignore
    private int sessionId;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }


    public String getiVehicleTypeId() {
        return iVehicleTypeId;
    }

    public void setiVehicleTypeId(String iVehicleTypeId) {
        this.iVehicleTypeId = iVehicleTypeId;
    }

    public String getfMinHour() {
        return fMinHour;
    }

    public void setfMinHour(String fMinHour) {
        this.fMinHour = fMinHour;
    }

    public String getfFixedFare() {
        return fFixedFare;
    }

    public void setfFixedFare(String fFixedFare) {
        this.fFixedFare = fFixedFare;
    }

    public String getfPricePerHour() {
        return fPricePerHour;
    }

    public void setfPricePerHour(String fPricePerHour) {
        this.fPricePerHour = fPricePerHour;
    }

    public String geteFareType() {
        return eFareType;
    }

    public void seteFareType(String eFareType) {
        this.eFareType = eFareType;
    }

    public String geteFareValue() {
        return eFareValue;
    }

    public void seteFareValue(String eFareValue) {
        this.eFareValue = eFareValue;
    }

    public CategoryListItem() {

    }

    public CategoryListItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getvDesc() {
        return vDesc;
    }

    public void setvDesc(String vDesc) {
        this.vDesc = vDesc;
    }

    public static int getITEM() {
        return ITEM;
    }

    public static int getSECTION() {
        return SECTION;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getCountSubItems() {
        return CountSubItems;
    }

    public void setCountSubItems(int countSubItems) {
        CountSubItems = countSubItems;
    }

    public String getvTitle() {
        return vTitle;
    }

    public void setvTitle(String vTitle) {
        this.vTitle = vTitle;
    }

    public String getiVehicleCategoryId() {
        return iVehicleCategoryId;
    }

    public void setiVehicleCategoryId(String iVehicleCategoryId) {
        this.iVehicleCategoryId = iVehicleCategoryId;
    }

    public String getvCategory() {
        return vCategory;
    }

    public void setvCategory(String vCategory) {
        this.vCategory = vCategory;
    }

    @Override
    public String toString() {
        return text;
    }
}


