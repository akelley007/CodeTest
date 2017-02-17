package com.ajkel.codetest;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ajkel on 2/16/2017.
 */

//this interface is intended to be implemented as part of the strategy design pattern
//you can add any number of ways to perform the save by creating a new class that implements this interface then implementing the 'save' function
public interface RemoteStoreStrategy {
    public Boolean save(ArrayList<Map<String, String>> objectMaps);
}
