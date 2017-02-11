package com.ajkel.codetest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ajkel on 2/10/2017.
 */

public class Comment implements SavableObject{
    private static String CLASSNAME = "Comment";
    private String commentText = "";
    private String createDate;

    public Comment(){}

    public void setCommentText(String text){
        this.commentText = text;
    }

    public String getCommentText(){
        return this.commentText;
    }

    public void setCreateDate(String created){
        this.createDate = created;
    }

    public String getCreateDate(){
        return this.createDate;
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<String, String>();

        map.put("classname", CLASSNAME);
        map.put("text", this.commentText);
        map.put("creation date", this.createDate);

        return map;
    }
}
