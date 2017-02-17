package com.ajkel.codetest;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ajkel on 2/16/2017.
 */

public class SaveManager {
    private ArrayList<Map<String,String>> objectMaps;
    private RemoteStoreStrategy strategy;

    //constructor, here the arrayList is initialized and the type of strategy is set based off the passed string
    SaveManager(String strategy){
        this.objectMaps = new ArrayList<Map<String,String>>();
        switch (strategy){
            case "parse":
                this.strategy = new ParseRemoteStoreStrategy();
                break;
            case "httpUrlConn":
                this.strategy = new HttpURLConnectionRemoteStoreStrategy();
                break;
            //you can add more implementations of the save functionality by adding a new strategy class and adding a case to the switch

        }
    }

    public void setObjectMaps(Map<String, String> objects){
        this.objectMaps.add(objects);
    }

    public void addObjectMap(Map<String, String> map){
        this.objectMaps.add(map);
    }

    public Boolean save(){
        //run the implementation specific to the type of strategy set
        return this.strategy.save(this.objectMaps);
    }

}
