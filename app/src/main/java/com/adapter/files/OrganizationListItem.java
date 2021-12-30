package com.adapter.files;

public class OrganizationListItem {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public final int type;
    public final String text;

    public int sectionPosition;
    String iOrganizationId;
    String iUserProfileMasterId;


    public String getiOrganizationId() {
        return iOrganizationId;
    }

    public void setiOrganizationId(String iOrganizationId) {
        this.iOrganizationId = iOrganizationId;
    }

    public String getiUserProfileMasterId() {
        return iUserProfileMasterId;
    }

    public void setiUserProfileMasterId(String iUserProfileMasterId) {
        this.iUserProfileMasterId = iUserProfileMasterId;
    }

    public OrganizationListItem(int type, String text) {
        this.type = type;
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }

}
