package com.ajkel.codetest;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get instance of floating action button and editText
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        final EditText commentText = (EditText) findViewById(R.id.comment_text);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //create new Comment object then retrieve comment text from editText
                // and set Comment objects commentText variable to retrieved string
                Comment comment = new Comment();
                String text = commentText.getText().toString();
                comment.setCommentText(text);

                //get current timestamp and set created date
                Long timestamp = System.currentTimeMillis()/1000;
                comment.setCreateDate(getDate(timestamp));

                //start new async task to save comment to remote data store sending a map of the comment object for easy processing
                new SaveOperation().execute(comment.toMap());
            }
        });
    }

    private class SaveOperation extends AsyncTask<Map, Void, String> {

        @Override
        protected String doInBackground(Map... params) {
            //This is the section that will need to be changed depending on the external data store method being used
            //I will provide examples of using both parse and a RESTful web service
            String result = "Save was successful!";
            Map<String, String> commentMap = (Map<String,String>) params[0];

            //for a parse implementation(even though parse is no longer a valid service):
                //ParseObject comment = new ParseObject(commentMap.get("classname"));
                //assuming language level java 8, use lambda expression to traverse comment map and add all key value pairs to parse object map
                //commentMap.forEach((k,v)->comment.put(k,v));
                //If java language level 8 is not supported, use normal for each loop to the same effect
                //for (Map.Entry<String, String> entry : commentMap.entrySet()) { comment.put(entry.getKey(),entry.getValue()); }
                //comment.saveInBackground();


            //for a RESTful webservice implementation using HttpURLConnection Request:
                //Assuming the path to the webservice is known ahead of time and not dynamic, there should be no need for modifying the async declaration
                String url = getString(R.string.storage_path);
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) ((new URL(url).openConnection()));
                    //convert comment map data into JSON object
                    JSONObject json = new JSONObject(commentMap);
                    String data = json.toString();
                    //set connect for POST properties and connect
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.connect();

                    //Write post data(comment data to save) to the service
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(data);
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
                    //change the result to unsuccessful if any exceptions were thrown(this is expected in this implementation)
                    result = "Save was not successful";
                }
                finally {
                    //after the response has been processed or exception has been thrown terminate the connection
                    connection.disconnect();
                }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //display a toast message telling the user if the save was successful or not
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            //display a toast message telling the user that the operation is starting
            Toast.makeText(MainActivity.this, "Saving your comment...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }
}
