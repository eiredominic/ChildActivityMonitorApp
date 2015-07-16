package com.example.lenamarie.childactivitymonitor.ParentTabs.NavigationDrawer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.lenamarie.childactivitymonitor.ParentTabs.ChangingTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.CommentsTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.FeedingTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.IncidentsTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.MedicationTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.SleepingTab;

import com.example.lenamarie.childactivitymonitor.R;

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


public class MainParentActivity extends AppCompatActivity implements NavigationInteraction
        .NavigationDrawerCallbacks {

    String child_id;
    private NavigationInteraction mNavigationInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parent_activity);
        Intent intent = getIntent();
        child_id = intent.getStringExtra("child_id");

        mNavigationInteraction = (NavigationInteraction)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set a toolbar which will replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Set up the drawer.
        mNavigationInteraction.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        // Load Fragment1 when the app starts
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new ChangingTab())
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mNavigationInteraction.getDrawerToggle().syncState();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ChangingTab(), "Changing")
                        .commit();
                break;
            case 1:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FeedingTab(), "Feeding")
                        .commit();
                break;
            case 2:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new SleepingTab(), "Sleeping")
                        .commit();
                break;
            case 3:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MedicationTab(), "Medication")
                        .commit();
                break;
            case 4:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new IncidentsTab(), "Incidents")
                        .commit();
                break;
            case 5:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new CommentsTab(), "Comments")
                        .commit();
                break;
        }



    }

    private static final String DEBUG_TAG = "HTTP POST";



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
            writer.append("childid=").append(URLEncoder.encode(child_id, "UTF-8")).append("&");

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

            }

    }

    public void initList(JSONArray jsonarray) throws JSONException {
        Fragment feedingFragment = getSupportFragmentManager().findFragmentByTag("Feeding");
        if (feedingFragment != null && feedingFragment.isVisible()) {

            ListView feedingList = (ListView) findViewById(R.id.feedingView);

            ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

            String[] from = new String[]{"ref", "minderid", "childid", "amount", "date", "time"};
            int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6};

            for (int i = 0; i < jsonarray.length(); i++) {
                HashMap<String, String> map = new HashMap<>();

                JSONObject jobj = jsonarray.getJSONObject(i);

                map.put("ref", "" + jobj.getString("ref"));
                map.put("minderid", "" + jobj.getString("minderid"));
                map.put("childid", "" + jobj.getString("childid"));
                map.put("amount", "Amount: " + jobj.getString("amount"));
                map.put("date", "Date: " + jobj.getString("date"));
                map.put("time", "Time: " + jobj.getString("time"));
                mylist.add(map);
            }


            SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.adapter_feeding_list, from, to);

            feedingList.setAdapter(adapter);
        }
    }
}