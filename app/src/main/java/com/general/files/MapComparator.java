package com.general.files;

import com.model.Multi_Delivery_Data;

import java.util.Comparator;

public class MapComparator implements Comparator<Multi_Delivery_Data> {
    private final String key;

    public MapComparator(String key) {
        this.key = key;
    }


    public int compare(Multi_Delivery_Data p1, Multi_Delivery_Data p2) {
        if (key.equals("SequenceId")) {
            return p1.getSequenceId() - p2.getSequenceId(); // Ascending
        } else {
//                return p1.getDistance() - p2.getDistance(); // Ascending
            if (p1.getDistance() < p2.getDistance()) return -1;
            if (p1.getDistance() > p2.getDistance()) return 1;
            return 0;

        }
    }
}
