package com.example.lenamarie.childactivitymonitor;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MinderHomeActivity extends Activity {


    //URL to get JSON Array
    private static String url =
            "https://mkbdesigncouk.ipage.com/monitoractivity/view_feeding_details.php";
    TextView childIdTxt;
    TextView childInfoDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        childIdTxt = (TextView)findViewById(R.id.childIdtxt);
        //childInfoDisplay = (TextView)findViewById(R.id.childInfoDisplay);
        Button viewInfoBtn = (Button)findViewById(R.id.viewInfoBtn);


        viewInfoBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new DownloadWebpageTask().execute(url);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_carer_home, menu);
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
            writer.append("childid=").append(URLEncoder.encode(childIdTxt.getText().toString(),
                    "UTF-8")).append("&");


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

            JSONArray aJsonArray = null;
            try {
                aJsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                initList(aJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //childInfoDisplay.setText(aJsonString);
        }
    }
    public void initList(JSONArray jsonarray) throws JSONException {
        ListView feedingList = (ListView) findViewById(R.id.feedingView);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

        String[] from = new String[] {"ref", "minderid", "childid"};
        int[] to = new int[] {R.id.item1, R.id.item2, R.id.item3};

        for (int i = 0; i < jsonarray.length(); i++) {
            HashMap<String, String> map = new HashMap<>();

            JSONObject jobj = jsonarray.getJSONObject(i);

            map.put("ref", "" + jobj.getString("ref"));
            map.put("minderid", "" + jobj.getString("minderid"));
            map.put("childid", "" + jobj.getString("childid"));
            //map.put("amount", "Amount: " + jobj.getString("amount"));
            //map.put("date", "Date: " + jobj.getString("date"));
            //map.put("time", "Time: " + jobj.getString("time"));
            mylist.add(map);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.feeding_list_adapter, from, to);

        feedingList.setAdapter(adapter);
    }

}
