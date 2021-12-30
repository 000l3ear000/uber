package com.model;

import android.graphics.Bitmap;
import android.net.Uri;

import io.realm.RealmObject;

public class ContactModel {
    public String id="";
    public String name="";
    public String nameLbl="";
    public String hederName="";
    public String mobileNumber="";
    public String photo="";
    public String photoURI="";
    public String colorVal="";
    public String nameChar="";
    public boolean isSection=false;
    public boolean shouldremove=false;


    public  ContactModel()
    {

    }
    public ContactModel(String name, boolean isSection) {
        this.hederName = name;
        this.isSection = isSection;
    }
}