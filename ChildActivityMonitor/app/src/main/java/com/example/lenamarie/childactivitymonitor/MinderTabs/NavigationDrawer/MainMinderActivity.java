package com.example.lenamarie.childactivitymonitor.MinderTabs.NavigationDrawer;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.MinderTabs.ChangingTab;
import com.example.lenamarie.childactivitymonitor.Other.AddRecordActivity;
import com.example.lenamarie.childactivitymonitor.Other.changeChildId;
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


public class MainMinderActivity extends AppCompatActivity  implements NavigationInteraction
        .NavigationDrawerCallbacks {

    private NavigationInteraction mNavigationInteraction;
    String child_id;
    String currentTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_minder_activity);

        mNavigationInteraction = (NavigationInteraction)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set a toolbar which will replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        child_id = intent.getStringExtra("child_id");
        
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

    public void addRecord(View view) {
        Intent i = new Intent(MainMinderActivity.this, AddRecordActivity.class);
        if (child_id != null) {
            i.putExtra("child_id", child_id);
        }
        startActivity(i);

   }

    public void searchRecord(View view) {
        Intent i = new Intent(MainMinderActivity.this, changeChildId.class);
        startActivity(i);
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
                currentTab = "Changing";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new com.example.lenamarie.childactivitymonitor.ParentTabs.ChangingTab(), "Changing")
                        .commit();
                if(child_id != null && !child_id.isEmpty()) {
                    new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_changing_details.php");
                }
                break;
            case 1:
                currentTab = "Feeding";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new com.example.lenamarie.childactivitymonitor.ParentTabs.FeedingTab(), "Feeding")
                        .commit();
                if(child_id != null && !child_id.isEmpty()) {
                    new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_feeding_details.php");
                }
                break;
            case 2:
                currentTab = "Sleeping";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new com.example.lenamarie.childactivitymonitor.ParentTabs.SleepingTab(), "Sleeping")
                        .commit();
                if(child_id != null && !child_id.isEmpty()) {
                    new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_sleeping_details.php");
                }
                break;
            case 3:
                currentTab = "Medication";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new com.example.lenamarie.childactivitymonitor.ParentTabs.MedicationTab(), "Medication")
                        .commit();
                if(child_id != null && !child_id.isEmpty()) {
                    new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_medication_details.php");
                }
                break;
            case 4:
                currentTab = "Incidents";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new com.example.lenamarie.childactivitymonitor.ParentTabs.IncidentsTab(), "Incidents")
                        .commit();
                if(child_id != null && !child_id.isEmpty()) {
                    new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_incident_details.php");
                }
                break;
            case 5:
                currentTab = "Comments";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new com.example.lenamarie.childactivitymonitor.ParentTabs.CommentsTab(), "Comments")
                        .commit();
                if(child_id != null && !child_id.isEmpty()) {
                    new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_comment_details.php");
                }
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
                Toast.makeText(getApplicationContext(), "Could not connect to server",
                        Toast.LENGTH_SHORT).show();
                return "Unable to reach server";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONArray jArray = null;


            try {
                jArray = new JSONArray(result);
                switch (getCurrentTab()) {
                    case "Feeding":
                        initFeedingList(jArray);
                        break;
                    case "Changing":
                        initChangingList(jArray);
                        break;
                    case "Medication":
                        initMedicationList(jArray);
                        break;
                    case "Incidents":
                        initIncidentList(jArray);
                        break;
                    case "Sleeping":
                        initSleepingList(jArray);
                        break;
                    case "Comments":
                        initCommentList(jArray);
                        break;
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "No records found",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String getCurrentTab() {
        return this.currentTab;
    }

    public void initFeedingList(JSONArray jsonarray) throws JSONException {

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
            map.put("amount", "" + jobj.getString("amount"));
            map.put("date", "" + jobj.getString("date"));
            map.put("time", "" + jobj.getString("time"));
            mylist.add(map);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.adapter_feeding_list, from, to);

        feedingList.setAdapter(adapter);

    }
    public void initChangingList(JSONArray jsonarray) throws JSONException {
        ListView changingList = (ListView) findViewById(R.id.changingView);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

        String[] from = new String[]{"ref", "minderid", "childid", "type", "date", "time"};
        int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6};

        for (int i = 0; i < jsonarray.length(); i++) {
            HashMap<String, String> map = new HashMap<>();

            JSONObject jobj = jsonarray.getJSONObject(i);

            map.put("ref", "" + jobj.getString("ref"));
            map.put("minderid", "" + jobj.getString("minderid"));
            map.put("childid", "" + jobj.getString("childid"));
            map.put("type", "" + jobj.getString("type"));
            map.put("date", "" + jobj.getString("date"));
            map.put("time", "" + jobj.getString("time"));
            mylist.add(map);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.adapter_changing_list, from, to);

        changingList.setAdapter(adapter);
    }
    public void initMedicationList(JSONArray jsonarray) throws JSONException {

        ListView medicationList = (ListView) findViewById(R.id.medicationView);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

        String[] from = new String[]{"ref", "minderid", "childid", "medication", "amount", "date", "time"};
        int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6, R.id.item7};

        for (int i = 0; i < jsonarray.length(); i++) {
            HashMap<String, String> map = new HashMap<>();

            JSONObject jobj = jsonarray.getJSONObject(i);

            map.put("ref", "" + jobj.getString("ref"));
            map.put("minderid", "" + jobj.getString("minderid"));
            map.put("childid", "" + jobj.getString("childid"));
            map.put("medication", "" + jobj.getString("medication"));
            map.put("amount", "" + jobj.getString("amount"));
            map.put("date", "" + jobj.getString("date"));
            map.put("time", "" + jobj.getString("time"));
            mylist.add(map);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.adapter_medication_list, from, to);

        medicationList.setAdapter(adapter);
    }
    public void initIncidentList(JSONArray jsonarray) throws JSONException {

        ListView incidentList = (ListView) findViewById(R.id.incidentView);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

        String[] from = new String[]{"ref", "minderid", "childid", "description", "date", "time"};
        int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6};

        for (int i = 0; i < jsonarray.length(); i++) {
            HashMap<String, String> map = new HashMap<>();

            JSONObject jobj = jsonarray.getJSONObject(i);

            map.put("ref", "" + jobj.getString("ref"));
            map.put("minderid", "" + jobj.getString("minderid"));
            map.put("childid", "" + jobj.getString("childid"));
            map.put("description", "" + jobj.getString("description"));
            map.put("date", "" + jobj.getString("date"));
            map.put("time", "" + jobj.getString("time"));
            mylist.add(map);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.adapter_incidents_list, from, to);

        incidentList.setAdapter(adapter);
    }
    public void initSleepingList(JSONArray jsonarray) throws JSONException {

        ListView sleepingList = (ListView) findViewById(R.id.sleepingView);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

        String[] from = new String[]{"ref", "minderid", "childid", "amount", "date", "time"};
        int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6};

        for (int i = 0; i < jsonarray.length(); i++) {
            HashMap<String, String> map = new HashMap<>();

            JSONObject jobj = jsonarray.getJSONObject(i);

            map.put("ref", "" + jobj.getString("ref"));
            map.put("minderid", "" + jobj.getString("minderid"));
            map.put("childid", "" + jobj.getString("childid"));
            map.put("amount", "" + jobj.getString("amount"));
            map.put("date", "" + jobj.getString("date"));
            map.put("time", "" + jobj.getString("time"));
            mylist.add(map);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.adapter_sleeping_list, from, to);

        sleepingList.setAdapter(adapter);
    }
    public void initCommentList(JSONArray jsonarray) throws JSONException {

        ListView commentList = (ListView) findViewById(R.id.commentsView);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

        String[] from = new String[]{"ref", "minderid", "childid", "comment", "date", "time"};
        int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6};

        for (int i = 0; i < jsonarray.length(); i++) {
            HashMap<String, String> map = new HashMap<>();

            JSONObject jobj = jsonarray.getJSONObject(i);

            map.put("ref", "" + jobj.getString("ref"));
            map.put("minderid", "" + jobj.getString("minderid"));
            map.put("childid", "" + jobj.getString("childid"));
            map.put("comment", "" + jobj.getString("comment"));
            map.put("date", "" + jobj.getString("date"));
            map.put("time", "" + jobj.getString("time"));
            mylist.add(map);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.adapter_comments_list, from, to);

        commentList.setAdapter(adapter);
    }

}
