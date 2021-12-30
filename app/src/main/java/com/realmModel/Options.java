package com.realmModel;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by ADMIN on 4/13/2018.
 */

public class Options extends RealmObject {


    private String iOptionId;
    private String vOptionName;
    private String fPrice;
    private String fUserPrice;
    private String fUserPriceWithSymbol;
    private String iMenuItemId;
    private String iFoodMenuId;
    private String eDefault;

    @Ignore
    private int sessionId;

    public String getiOptionId() {
        return iOptionId;
    }

    public void setiOptionId(String iOptionId) {
        this.iOptionId = iOptionId;
    }

    public String getvOptionName() {
        return vOptionName;
    }

    public void setvOptionName(String vOptionName) {
        this.vOptionName = vOptionName;
    }

    public String getfPrice() {
        return fPrice;
    }

    public void setfPrice(String fPrice) {
        this.fPrice = fPrice;
    }

    public String getfUserPrice() {
        return fUserPrice;
    }

    public void setfUserPrice(String fUserPrice) {
        this.fUserPrice = fUserPrice;
    }

    public String getfUserPriceWithSymbol() {
        return fUserPriceWithSymbol;
    }

    public void setfUserPriceWithSymbol(String fUserPriceWithSymbol) {
        this.fUserPriceWithSymbol = fUserPriceWithSymbol;
    }

    public String getiMenuItemId() {
        return iMenuItemId;
    }

    public void setiMenuItemId(String iMenuItemId) {
        this.iMenuItemId = iMenuItemId;
    }

    public String getiFoodMenuId() {
        return iFoodMenuId;
    }

    public void setiFoodMenuId(String iFoodMenuId) {
        this.iFoodMenuId = iFoodMenuId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String geteDefault() {
        return eDefault;
    }

    public void seteDefault(String eDefault) {
        this.eDefault = eDefault;
    }
}
