package com.model;

import java.io.Serializable;

/**
 * Created by Esite on 11-04-2018.
 */

public class Multi_Dest_Info_Detail_Data implements Serializable {
    long time;
    long distance;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
