package com.general.files;

import com.model.Stop_Over_Points_Data;

public class StopOverComparator implements java.util.Comparator<Stop_Over_Points_Data> {
    private final String key;

    public StopOverComparator(String key) {
        this.key = key;
    }


    public int compare(Stop_Over_Points_Data p1, Stop_Over_Points_Data p2) {
        if (key.equals("SequenceId")) {
            return p1.getSequenceId() - p2.getSequenceId(); // Ascending
        } else {
            if (p1.getDistance() < p2.getDistance()) return -1;
            if (p1.getDistance() > p2.getDistance()) return 1;
            return 0;

        }
    }
}
