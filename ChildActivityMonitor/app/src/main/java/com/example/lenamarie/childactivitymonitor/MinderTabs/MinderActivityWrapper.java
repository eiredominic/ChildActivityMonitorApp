package com.example.lenamarie.childactivitymonitor.MinderTabs;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.lenamarie.childactivitymonitor.R;
import com.example.lenamarie.childactivitymonitor.SlidingTabs.SlidingTabLayout;
import com.example.lenamarie.childactivitymonitor.SlidingTabs.ViewPagerAdapter;

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

public class MinderActivityWrapper extends FragmentActivity implements
        FeedingTab.OnFragmentInteractionListener,
        ChangingTab.OnFragmentInteractionListener,
        SleepingTab.OnFragmentInteractionListener,
        MedicationTab.OnFragmentInteractionListener,
        CommentsTab.OnFragmentInteractionListener,
        IncidentsTab.OnFragmentInteractionListener {


        // Declaring Your View and Variables

        Toolbar toolbar;
        ViewPager pager;
        ViewPagerAdapter adapter;
        SlidingTabLayout tabs;
        EditText childIdTxt;
        int currentTab;
        CharSequence Titles[]={"Feeding", "Changing", "Medication", "Sleeping", "Incidents", "Comments"};
        int Numboftabs =5;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.minder_activity);

            childIdTxt = (EditText) findViewById(R.id.childIdTxt);
            Button goBtn = (Button) findViewById(R.id.goBtn);

            //toolbar = (Toolbar) findViewById(R.id.tool_bar);

            // Creating The Toolbar and setting it as the Toolbar for the activity


            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);

            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            });

            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);

             currentTab = tabs.getViewPager();


            goBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (currentTab == 0) {
                        // feeding tab
                        new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_feeding_details.php");
                    }
                    if (currentTab == 1) {
                        // changing tab
                        new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_changing_details.php");
                    }

                    if (currentTab == 2) {
                        // medication tab
                        new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_medication_details.php");
                    }

                    if (currentTab == 3) {
                        // sleeping tab
                        new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_sleeping_details.php");
                    }
                    if (currentTab == 4) {
                        // incidents tab
                        new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_incident_details.php");
                    }
                    if (currentTab == 5) {
                        // comments tab
                        new DownloadWebpageTask().execute("https://mkbdesigncouk.ipage.com/monitoractivity/view_comment_details.php");
                    }

                }
            });
        }



    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
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
            conn.setReadTimeout(10000 );
            conn.setConnectTimeout(15000);
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
            Log.d("HttpConn", "The response is: " + response);
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

        if (tabs.getViewPager() == 0) {
            ListView feedingList = (ListView) findViewById(R.id.feedingView);

            ArrayList<HashMap<String, String>> mylist = new ArrayList<>();

            String[] from = new String[]{"ref", "minderid", "childid"};
            int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3};

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
}


