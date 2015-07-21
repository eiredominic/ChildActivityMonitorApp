package com.example.lenamarie.childactivitymonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.MinderTabs.NavigationDrawer.MainMinderActivity;
import com.example.lenamarie.childactivitymonitor.ParentTabs.NavigationDrawer.MainParentActivity;
import com.example.lenamarie.childactivitymonitor.Register.RegisterOneActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends Activity {

    // URL to access the REST API
    private static String url = "https://mkbdesigncouk.ipage.com/monitoractivity/login_api.php";

    EditText uniqueId, pwdText;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fetch the text fields and buttons so we can fetch the text inside them later
        uniqueId = (EditText) findViewById(R.id.uniqueIdLogin);
        pwdText = (EditText) findViewById(R.id.pwdLogin);
        btnLogin = (Button) findViewById(R.id.mainLoginBtn);
        btnRegister = (Button) findViewById(R.id.mainRegBtn);
    }

    public void login(View v) {

        // checking the ID and password box are not empty
        if (uniqueId.getText().toString().isEmpty() || pwdText.getText().toString().isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Please ensure fields are not empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            // call our URL to get a response from the REST API
            new DownloadWebpageTask().execute(url);
        }
    }

    public void register(View v) {
        // if Register button is clicked start a new intent and open the Registration activity
        Intent intent = new Intent(MainActivity.this, RegisterOneActivity.class);
        startActivity(intent);
    }




    private static final String DEBUG_TAG = "HttpExample";

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content. We don't need anything more than this
        int len = 500;
        try {

            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            // Start the query
            conn.connect();

            OutputStream out = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);

            // Our login_api.php file expects 2 variables using POST. unique_id and password.
            // We declare them below and get the data that is entered in the text fields.
            // i.e. if 'username' is entered in the Unique ID field then our POST for it will be
            // unique_id='username'

            writer.append("unique_id=").append(URLEncoder.encode(uniqueId.getText().toString(), "UTF-8")).append("&");
            writer.append("password=").append(URLEncoder.encode(pwdText.getText().toString(), "UTF-8")).append("&");

            writer.flush();

            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static String readIt(InputStream stream, int len) throws IOException {

        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.

    private class DownloadWebpageTask extends AsyncTask<String, Integer, String> {

        // declare new Progress Dialog object
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            // Before the DownloadWebPageTask is executed set progres dialog properties and then
            // show it
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Logging in...");
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            // execute downloadUrl on the URL previously declared
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        // onPostExecute displays the results of the AsyncTask.
        // This is where we parse the response from the REST API.
        // REST API will return results in JSON format

        @Override
        protected void onPostExecute(String result) {

            // declare SharedPreferences. We will add the login details to this (if they are
            // correct) so that we can use shared preferences anywhere throughout the app to find
            // out which kind of user they are logged in as
            SharedPreferences sharedprefs = getSharedPreferences("loginpreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedprefs.edit();

            // declare JSON objects and Strings we'll assign them to
            JSONObject jObject;
            String message;
            String type;
            String success;
            String child_id;

            try {
                jObject = new JSONObject(result);
                // get the 'success_msg' and 'message' keys returned from the REST API
                success = jObject.getString("success_msg");
                message = jObject.getString("message");

                // if success = 1 then there has been an error i.e. login details are not correct
                if (success.equals("1")) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // login has been successful
                    type = jObject.getString("type");
                    dialog.dismiss();
                    Intent intent = null;
                    // fetch the type of user this is and add it to shared preferences before
                    // logging the user on
                    if (type.equals("Parent")) {
                        intent = new Intent(MainActivity.this, MainParentActivity.class);
                        if (jObject.getString("child_id") != null) {
                            child_id = jObject.getString("child_id");
                            intent.putExtra("child_id", child_id);
                        }
                        intent.putExtra("parent_id", uniqueId.getText().toString());
                    } else if (type.equals("Minder")) {
                        intent = new Intent(MainActivity.this, MainMinderActivity.class);
                        intent.putExtra("minder_id", uniqueId.getText().toString());
                    }

                    //editor.apply();
                    // start the minder or parent activity depending on the type of user
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }
}




