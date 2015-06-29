package com.example.lenamarie.childactivitymonitor;

import android.app.Activity;
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
import android.widget.TextView;

import com.example.lenamarie.childactivitymonitor.MinderTabs.MinderActivityWrapper;

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

    //URL to get JSON Array
    private static String url = "https://mkbdesigncouk.ipage.com/monitoractivity/login_api.php";

    EditText uniqueId;
    EditText pwdText;
    Button btnLogin;
    Button btnRegister;
    TextView loginConfirm;

    public void setPreferences() {
        SharedPreferences sharedprefs = getSharedPreferences("loginpreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putString("Id", uniqueId.getText().toString());
        editor.putString("Password", pwdText.getText().toString());
        editor.commit();
    }

    public String getPref(String key) {
        SharedPreferences sharedPref = getSharedPreferences("loginpreferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        uniqueId = (EditText) findViewById(R.id.uniqueIdLogin);
        pwdText = (EditText) findViewById(R.id.pwdLogin);
        btnLogin = (Button) findViewById(R.id.mainLoginBtn);
        btnRegister = (Button) findViewById(R.id.mainRegBtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPreferences();
                new DownloadWebpageTask().execute(url);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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

    private static final String DEBUG_TAG = "HttpExample";

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();

            OutputStream out = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.append("unique_id=").append(URLEncoder.encode(getPref("Id"), "UTF-8")).append("&");
            writer.append("password=").append(URLEncoder.encode(getPref("Password"), "UTF-8")).append("&");

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

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            JSONObject jObject = null;
            String type = null;
            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                type = jObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (type != null) {
                if (type.toString().equals("Parent")) {
                    Intent intent = new Intent(MainActivity.this, ParentHomeActivity.class);
                    startActivity(intent);
                }else if (type.toString().equals("Minder")) {
                    Intent intent = new Intent(MainActivity.this, MinderActivityWrapper.class);
                    startActivity(intent);
                }
            }
        }


        }
    }


