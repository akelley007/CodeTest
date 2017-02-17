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

            //set the type of remote store you want to use
            String remoteStoreType = getString(R.string.remote_store_type);

            //create save manager using the type of remote store connection set above and save the comment
            //alternatively you could send a new instance of the type of RemoteStoreStrategy you wish to use to save the class
            SaveManager sm  = new SaveManager(remoteStoreType);
            sm.addObjectMap(commentMap);
            if(!sm.save()){
                //this is the expected output as no actual webservice is being called
                result = "save was not successful";
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
