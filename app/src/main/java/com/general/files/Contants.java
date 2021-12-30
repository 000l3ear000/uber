package com.general.files;

import java.io.Serializable;

/**
 * Created by Admin on 22-03-2018.
 */

public class Contants implements Serializable {

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String[] getSubName() {
        return subName;
    }

    public void setSubName(String[] subName) {
        this.subName = subName;
    }

    public static String[] image;
    public static String[] name;
    public static String[] subName;
}
