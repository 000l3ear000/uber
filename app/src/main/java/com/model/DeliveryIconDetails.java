package com.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryIconDetails implements Serializable {

    String vCategory;
    String tCategoryDesc;
    ArrayList<SubCatData> subdatalist;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    int pos;

    public String getvCategory() {
        return vCategory;
    }

    public void setvCategory(String vCategory) {
        this.vCategory = vCategory;
    }

    public String gettCategoryDesc() {
        return tCategoryDesc;
    }

    public void settCategoryDesc(String tCategoryDesc) {
        this.tCategoryDesc = tCategoryDesc;
    }

    public void setSubData(ArrayList<SubCatData> subdatalist) {
        this.subdatalist = subdatalist;
    }

    public ArrayList<SubCatData> getSubDataList() {
        return subdatalist;
    }

    public static class SubCatData {

        String eCatType;
        String iServiceId;
        String tSubCategoryDesc;
        String vSubCategory;
        String vImage;
        String eDeliveryType;
        HashMap<String, String> dataMap;

        public HashMap<String, String> getDataMap() {
            return dataMap;
        }

        public void setDataMap(HashMap<String, String> dataMap) {
            this.dataMap = dataMap;
        }

        public String geteDeliveryType() {
            return eDeliveryType;
        }

        public void seteDeliveryType(String eDeliveryType) {
            this.eDeliveryType = eDeliveryType;
        }

        public String geteCatType() {
            return eCatType;
        }

        public void seteCatType(String eCatType) {
            this.eCatType = eCatType;
        }

        public String getiServiceId() {
            return iServiceId;
        }

        public void setiServiceId(String iServiceId) {
            this.iServiceId = iServiceId;
        }

        public String gettSubCategoryDesc() {
            return tSubCategoryDesc;
        }

        public void settSubCategoryDesc(String tSubCategoryDesc) {
            this.tSubCategoryDesc = tSubCategoryDesc;
        }

        public String getvSubCategory() {
            return vSubCategory;
        }

        public void setvSubCategory(String vSubCategory) {
            this.vSubCategory = vSubCategory;
        }

        public String getvImage() {
            return vImage;
        }

        public void setvImage(String vImage) {
            this.vImage = vImage;
        }
    }


}

