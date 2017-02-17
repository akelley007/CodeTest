package com.ajkel.codetest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ajkel on 2/16/2017.
 */

public class HttpURLConnectionRemoteStoreStrategy implements RemoteStoreStrategy {

    static final String URL = "http://some/arbitrary/endpoint/path";

    @Override
    public Boolean save(ArrayList<Map<String, String>> objectMaps) {
        //for a RESTful webservice implementation using HttpURLConnection Request:
        HttpURLConnection connection = null;
        Boolean success = true;
        try {
            connection = (HttpURLConnection) ((new URL(URL).openConnection()));
            StringBuilder data = new StringBuilder();;
            //convert comment map data into JSON object
            for (Map<String, String> entry : objectMaps) {
                JSONObject json = new JSONObject(entry);
                data.append(json.toString());
            }
            //set connect for POST properties and connect
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.connect();

            //Write post data(comment data to save) to the service
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data.toString());
            writer.close();
            os.close();

            //Read the response, if error occurred while processing request, getInputStream() will throw an IOExpection
            //so it is safe to assume that if no exception is thrown the response status was 200
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            //if you would want to display the response set the result = sb.toString()
            //but in this case we just want to tell the user the save was successful


        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        finally {
            //after the response has been processed or exception has been thrown terminate the connection
            connection.disconnect();
        }
        return success;
    }
}
