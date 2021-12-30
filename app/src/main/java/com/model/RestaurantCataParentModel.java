package com.model;

import java.util.List;

/**
 * Created by putuguna on 04/01/17.
 */

public class RestaurantCataParentModel implements ParentListItem {

    private String name;
    private String id;
    private String nameCnt;
    private List<RestaurantCataChildModel> mListChild;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RestaurantCataParentModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCnt() {
        return nameCnt;
    }

    public void setNameCnt(String nameCnt) {
        this.nameCnt = nameCnt;
    }

    public List<RestaurantCataChildModel> getmListChild() {
        return mListChild;
    }

    public void setmListChild(List<RestaurantCataChildModel> mListChild) {
        this.mListChild = mListChild;
    }

    @Override
    public List<?> getChildItemList() {
        return mListChild;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
