package com.ajkel.codetest;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ajkel on 2/16/2017.
 */

public class ParseRemoteStoreStrategy implements RemoteStoreStrategy {

    @Override
    public Boolean save(ArrayList<Map<String, String>> objectMaps) {
        for (Map<String, String> entry : objectMaps) {
            //This comment block represents a parse implementation, which cannot actually be used as parse is no longer a supported service

            //ParseObject comment = new ParseObject(commentMap.get("classname"));
            //assuming language level java 8, use lambda expression to traverse comment map and add all key value pairs to parse object map
            //commentMap.forEach((k,v)->comment.put(k,v));
            //If java language level 8 is not supported, use normal for each loop to the same effect
            //for (Map.Entry<String, String> entry : commentMap.entrySet()) { comment.put(entry.getKey(),entry.getValue()); }
            //comment.saveInBackground();

            //if anything goes wrong return false
        }

        return true;
    }
}
