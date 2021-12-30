package com.realmModel;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by ADMIN on 4/12/2018.
 */

public class Cart extends RealmObject {

    private String vItemType;
    private String Qty;
    private String vImage;
    private String fDiscountPrice;
    private String iMenuItemId;
    private String eFoodType;
    private String iFoodMenuId;
    private String iCompanyId;
    private String vCompany;
    private String iToppingId;
    private String isTooping;
    private String isOption;

    private String iOptionId;
    private String currencySymbol;

    private String iServiceId;

    public String getiServiceId() {
        return iServiceId;
    }

    public void setiServiceId(String iServiceId) {
        this.iServiceId = iServiceId;
    }

    public String getIspriceshow() {
        return ispriceshow;
    }

    public void setIspriceshow(String ispriceshow) {
        this.ispriceshow = ispriceshow;
    }

    private String ispriceshow;


    @Ignore
    private int sessionId;

    public String getiToppingId() {
        return iToppingId;
    }

    public String getiOptionId() {
        return iOptionId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public void setiOptionId(String iOptionId) {
        this.iOptionId = iOptionId;
    }

    public void setiToppingId(String iToppingId) {
        this.iToppingId = iToppingId;
    }

    public String getIsTooping() {
        return isTooping;
    }

    public void setIsTooping(String isTooping) {
        this.isTooping = isTooping;
    }

    public String getIsOption() {
        return isOption;
    }

    public void setIsOption(String isOption) {
        this.isOption = isOption;
    }

    public String getvItemType() {
        return vItemType;
    }

    public void setvItemType(String vItemType) {
        this.vItemType = vItemType;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getvImage() {
        return vImage;
    }

    public void setvImage(String vImage) {
        this.vImage = vImage;
    }

    public String getfDiscountPrice() {
        return fDiscountPrice;
    }

    public void setfDiscountPrice(String fDiscountPrice) {
        this.fDiscountPrice = fDiscountPrice;
    }

    public String getiMenuItemId() {
        return iMenuItemId;
    }

    public void setiMenuItemId(String iMenuItemId) {
        this.iMenuItemId = iMenuItemId;
    }

    public String geteFoodType() {
        return eFoodType;
    }

    public void seteFoodType(String eFoodType) {
        this.eFoodType = eFoodType;
    }

    public String getiFoodMenuId() {
        return iFoodMenuId;
    }

    public void setiFoodMenuId(String iFoodMenuId) {
        this.iFoodMenuId = iFoodMenuId;
    }

    public String getiCompanyId() {
        return iCompanyId;
    }

    public void setiCompanyId(String iCompanyId) {
        this.iCompanyId = iCompanyId;
    }

    public String getvCompany() {
        return vCompany;
    }

    public void setvCompany(String vCompany) {
        this.vCompany = vCompany;
    }


    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }


    public long milliseconds;

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }
}
