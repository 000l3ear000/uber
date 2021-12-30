package com.general.files;

import com.utils.Utils;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Esite on 13-03-2018.
 */

public class favDriverComparator implements Comparator<HashMap<String, String>> {

    String key;
    double ignoreValue;

    public favDriverComparator(String key,double ignoreValue) {
        this.key = key;
        this.ignoreValue = ignoreValue;
    }

    @Override
    public int compare(HashMap<String,String> o1, HashMap<String,String> o2)
    {
        boolean key1=o1.get(key).equalsIgnoreCase("yes");
        boolean key2=o2.get(key).equalsIgnoreCase("yes");

        return Boolean.compare(key1,key2);
    }


}
