package com.example.lenamarie.childactivitymonitor.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lenamarie.childactivitymonitor.MainActivity;
import com.example.lenamarie.childactivitymonitor.R;

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

public class RegisterFiveActivity extends Activity {
    String account_type, unique_id, name, email_address, password, date, parent_id, address, url;
    TextView successTxt, uniqueIdTxt, registrationStatusTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_five);
        uniqueIdTxt = (TextView)findViewById(R.id.uniqueIdTxt);
        registrationStatusTxt = (TextView)findViewById(R.id.registrationStatusTxt);
        successTxt = (TextView)findViewById(R.id.successTxt);

        Intent intent = getIntent();
        unique_id = intent.getStringExtra("unique_id");
        account_type = intent.getStringExtra("account_type");
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        date = intent.getStringExtra("dob");

        switch (account_type) {
            case "Minder":
                email_address = intent.getStringExtra("email_address");
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/register_minder.php";
                break;
            case "Parent":
                email_address = intent.getStringExtra("email_address");
                address = intent.getStringExtra("address");
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/register_parent.php";
                break;
            case "Child":
                parent_id = intent.getStringExtra("parent_id");
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/register_child.php";
        }

        new DownloadWebpageTask().execute(url);

    }
    /** Called when the user clicks the Send button */
    public void nextActivity(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_five, menu);
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
    private static final String DEBUG_TAG = "HTTP REGISTER POST";



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
            if (account_type.equals("Child")) {
                writer.append("unique_id=").append(URLEncoder.encode(unique_id, "UTF-8")).append("&");
                writer.append("name=").append(URLEncoder.encode(name, "UTF-8")).append("&");
                writer.append("password=").append(URLEncoder.encode(password, "UTF-8")).append("&");
                writer.append("dob=").append(URLEncoder.encode(date, "UTF-8")).append("&");
                writer.append("type=").append(URLEncoder.encode(account_type, "UTF-8")).append("&");
                writer.append("parent_id=").append(URLEncoder.encode(parent_id, "UTF-8"));
            }
            if (account_type.equals("Parent")) {
                writer.append("unique_id=").append(URLEncoder.encode(unique_id, "UTF-8")).append("&");
                writer.append("name=").append(URLEncoder.encode(name, "UTF-8")).append("&");
                writer.append("password=").append(URLEncoder.encode(password, "UTF-8")).append("&");
                writer.append("email=").append(URLEncoder.encode(email_address, "UTF-8")).append("&");
                writer.append("dob=").append(URLEncoder.encode(date, "UTF-8")).append("&");
                writer.append("address=").append(URLEncoder.encode(address, "UTF-8")).append("&");
                writer.append("type=").append(URLEncoder.encode(account_type, "UTF-8"));
            }
            if (account_type.equals("Minder")) {
                writer.append("unique_id=").append(URLEncoder.encode(unique_id, "UTF-8")).append("&");
                writer.append("name=").append(URLEncoder.encode(name, "UTF-8")).append("&");
                writer.append("password=").append(URLEncoder.encode(password, "UTF-8")).append("&");
                writer.append("dob=").append(URLEncoder.encode(date, "UTF-8")).append("&");
                writer.append("email=").append(URLEncoder.encode(email_address, "UTF-8")).append("&");
                writer.append("type=").append(URLEncoder.encode(account_type, "UTF-8"));
            }
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
            String aJsonString = null;

            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                aJsonString = jObject.getString("success_msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (aJsonString.equals("0")){
                if (account_type.equals("Child")) {
                    registrationStatusTxt.setText("Registration Successful!");
                    successTxt.setText("Unique ID for child is " + unique_id);
                }
                else {
                    registrationStatusTxt.setText("Registration Successful!");
                    successTxt.setText("Your unique ID is " + unique_id + ". Please keep this for " +
                            "your records as you will need it to logon");
                }
            }
            else {
                registrationStatusTxt.setText("There has been a problem with your registration." +
                        "Please contact admin");

            }
        }

    }
}
