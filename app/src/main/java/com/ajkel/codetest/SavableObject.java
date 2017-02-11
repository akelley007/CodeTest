package com.ajkel.codetest;

import java.util.Map;

/**
 * Created by ajkel on 2/10/2017.
 */
//This interface should be implemented for all classes that will be saved to remote store
public interface SavableObject {
    public Map<String,String> toMap();
}
