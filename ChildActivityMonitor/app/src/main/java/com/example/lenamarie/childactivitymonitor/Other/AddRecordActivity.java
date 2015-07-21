package com.example.lenamarie.childactivitymonitor.Other;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.MinderTabs.NavigationDrawer.MainMinderActivity;
import com.example.lenamarie.childactivitymonitor.ParentTabs.NavigationDrawer.MainParentActivity;
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
import java.util.Calendar;

public class AddRecordActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout feedingLayout, changingLayout, sleepingLayout, medicationLayout, incidentLayout, commentsLayout;
    String type, url, child_id, time, date;

    // variables used in all insertions
    Spinner typeSpinner;
    EditText child_id_editText;

    // variables for changing insert
    String changing_type;
    // variables for comment insert
    String comment_text;
    // variables for feeding insert
    String food_amount;
    // variables for incident insert
    String incident_description;
    // variables for medication insert
    String medication_type;
    String medication_amount;
    // variables for sleeping insert
    String sleep_amount;
    SharedPreferences sharedPref;
    String minder_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        sharedPref = getSharedPreferences("loginpreferences", Context.MODE_PRIVATE);
        minder_id = sharedPref.getString("minder_id", null);

        child_id_editText = (EditText) findViewById(R.id.child_id_txt);
        Intent intent = getIntent();
        child_id = intent.getStringExtra("child_id");
        child_id_editText.setText(child_id);
        feedingLayout = (LinearLayout) findViewById(R.id.add_feeding_layout);
        changingLayout = (LinearLayout) findViewById(R.id.add_changing_layout);
        sleepingLayout = (LinearLayout) findViewById(R.id.add_sleep_layout);
        medicationLayout = (LinearLayout) findViewById(R.id.add_medication_layout);
        incidentLayout = (LinearLayout) findViewById(R.id.add_incident_layout);
        commentsLayout = (LinearLayout) findViewById(R.id.add_comment_layout);

        Button selectTime = (Button) findViewById(R.id.selectTimeButton);
        typeSpinner = (Spinner) findViewById(R.id.record_type_spinner);

        typeSpinner.setOnItemSelectedListener(this);


        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        Button saveButton = (Button) findViewById(R.id.SaveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = typeSpinner.getSelectedItem().toString();
                int status = getData(type);
                if (status == 0) {
                    insertRecord(type);
                }
            }
        });
    }

    public int getData(String type) {

        child_id = child_id_editText.getText().toString();
        if (child_id_editText.getText().toString().isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Please enter a child ID",
                    Toast.LENGTH_SHORT).show();
            return 1;
        }
        TextView yearText = (TextView) findViewById(R.id.yearText);
        TextView monthText = (TextView) findViewById(R.id.monthText);
        TextView dayText = (TextView) findViewById(R.id.dayText);
        TextView timeHour = (TextView) findViewById(R.id.hourText);
        TextView timeMinute = (TextView) findViewById(R.id.minuteText);


        Resources res = getResources();

        if (yearText.getText().toString().isEmpty() || monthText.getText().toString().isEmpty() ||
                dayText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a date",
                    Toast.LENGTH_SHORT).show();
            return 1;
        }
        else {
            int month = Integer.parseInt(monthText.getText().toString());

            date = String.format(res.getString(R.string.date), yearText.getText().toString(),
                    month + 1, dayText.getText().toString());
        }
        if (timeHour.getText().toString().isEmpty() || timeMinute.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a time",
                    Toast.LENGTH_SHORT).show();
            return 1;
        }
        else {
            time = String.format(res.getString(R.string.time), timeHour.getText().toString(),
                    timeMinute.getText().toString());
        }


            switch (type) {
                case "Changing":
                    Spinner changing_type_spinner = (Spinner) findViewById(R.id.changingTypesSpinner);
                    changing_type = changing_type_spinner.getSelectedItem().toString();
                    if (changing_type_spinner.getSelectedItem().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select a changing type",
                                Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                    break;
                case "Feeding":
                    Spinner food_amount_editText = (Spinner) findViewById(R.id.foodAmount);
                    food_amount = food_amount_editText.getSelectedItem().toString();
                    if (food_amount_editText.getSelectedItem().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select a food amount",
                                Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                    break;

                case "Sleeping":
                    EditText sleep_amount_editText = (EditText) findViewById(R.id.sleepAmountText);
                    sleep_amount = sleep_amount_editText.getText().toString();
                    if (sleep_amount_editText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a sleep amount",
                                Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                    break;

                case "Medication":
                    EditText medication_type_editText = (EditText) findViewById(R.id.medicationType);
                    EditText medication_amount_editText = (EditText) findViewById(R.id.medAmountText);

                    medication_type = medication_type_editText.getText().toString();
                    medication_amount = medication_amount_editText.getText().toString();

                    if (medication_type_editText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a medication type",
                                Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                    if (medication_amount_editText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a medication amount",
                                Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                    break;
                case "Comments":
                    EditText comment_editText = (EditText) findViewById(R.id.comment_txt);
                    comment_text = comment_editText.getText().toString();
                    if (comment_editText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a comment",
                                Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                    break;
                case "Incidents":
                    EditText incident_editText = (EditText) findViewById(R.id.descriptionText);
                    incident_description = incident_editText.getText().toString();
                    if (incident_editText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a description",
                                Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                    break;
            }
        return 0;

    }

    public void closeDialog (View v) {
        finish();
    }
    public void insertRecord(String type){

        switch (type)
        {
            case "Changing":
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/insert_changing_record.php";
                new DownloadWebpageTask().execute(url);
                break;
            case "Feeding":
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/insert_feeding_record.php";
                new DownloadWebpageTask().execute(url);
                break;
            case "Sleeping":
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/insert_sleeping_record.php";
                new DownloadWebpageTask().execute(url);
                break;
            case "Medication":
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/insert_medication_record.php";
                new DownloadWebpageTask().execute(url);
                break;
            case "Comments":
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/insert_comment_record.php";
                new DownloadWebpageTask().execute(url);
                break;
            case "Incidents":
                url = "https://mkbdesigncouk.ipage.com/monitoractivity/insert_incident_record.php";
                new DownloadWebpageTask().execute(url);
                break;
        }


    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        switch (pos)
        {
            case 0:
                //changing
                changingLayout.setVisibility(View.VISIBLE);
                feedingLayout.setVisibility(View.GONE);
                sleepingLayout.setVisibility(View.GONE);
                medicationLayout.setVisibility(View.GONE);
                incidentLayout.setVisibility(View.GONE);
                commentsLayout.setVisibility(View.GONE);
                break;
            case 1:
                // feeding
                changingLayout.setVisibility(View.GONE);
                feedingLayout.setVisibility(View.VISIBLE);
                sleepingLayout.setVisibility(View.GONE);
                medicationLayout.setVisibility(View.GONE);
                incidentLayout.setVisibility(View.GONE);
                commentsLayout.setVisibility(View.GONE);
                break;
            case 2:
                //sleeping
                changingLayout.setVisibility(View.GONE);
                feedingLayout.setVisibility(View.GONE);
                sleepingLayout.setVisibility(View.VISIBLE);
                medicationLayout.setVisibility(View.GONE);
                incidentLayout.setVisibility(View.GONE);
                commentsLayout.setVisibility(View.GONE);
                break;
            case 3:
                //medication
                changingLayout.setVisibility(View.GONE);
                feedingLayout.setVisibility(View.GONE);
                sleepingLayout.setVisibility(View.GONE);
                medicationLayout.setVisibility(View.VISIBLE);
                incidentLayout.setVisibility(View.GONE);
                commentsLayout.setVisibility(View.GONE);
                break;
            case 4:
                //comments
                changingLayout.setVisibility(View.GONE);
                feedingLayout.setVisibility(View.GONE);
                sleepingLayout.setVisibility(View.GONE);
                medicationLayout.setVisibility(View.GONE);
                incidentLayout.setVisibility(View.GONE);
                commentsLayout.setVisibility(View.VISIBLE);
                break;
            case 5:
                //incidents
                changingLayout.setVisibility(View.GONE);
                feedingLayout.setVisibility(View.GONE);
                sleepingLayout.setVisibility(View.GONE);
                medicationLayout.setVisibility(View.GONE);
                incidentLayout.setVisibility(View.VISIBLE);
                commentsLayout.setVisibility(View.GONE);
                break;
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            TextView timeHour = (TextView) getActivity().findViewById(R.id.hourText);
            TextView timeMinute = (TextView) getActivity().findViewById(R.id.minuteText);

            String hourOfDayStr = Integer.toString(hourOfDay);
            String minuteStr = Integer.toString(minute);

            timeHour.setText(hourOfDayStr);
            timeMinute.setText(minuteStr);
        }

    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView yearText = (TextView) getActivity().findViewById(R.id.yearText);
            TextView monthText = (TextView) getActivity().findViewById(R.id.monthText);
            TextView dayText = (TextView) getActivity().findViewById(R.id.dayText);

            yearText.setText(Integer.toString(year));
            monthText.setText(Integer.toString(month + 1 ));
            dayText.setText(Integer.toString(day));

        }
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
            writer.append("minderid=").append(URLEncoder.encode(minder_id, "UTF-8")).append("&");
            writer.append("childid=").append(URLEncoder.encode(child_id, "UTF-8")).append("&");
            writer.append("date=").append(URLEncoder.encode(date, "UTF-8")).append("&");
            writer.append("time=").append(URLEncoder.encode(time, "UTF-8")).append("&");

            switch (type)
            {
                case "Changing":
                    writer.append("type=").append(URLEncoder.encode(changing_type, "UTF-8")).append("&");
                    break;
                case "Feeding":
                    writer.append("amount=").append(URLEncoder.encode(food_amount, "UTF-8")).append("&");
                    break;
                case "Sleeping":
                    writer.append("amount=").append(URLEncoder.encode(sleep_amount, "UTF-8")).append("&");
                    break;
                case "Medication":
                    writer.append("medication=").append(URLEncoder.encode(medication_type, "UTF-8")).append("&");
                    writer.append("amount=").append(URLEncoder.encode(medication_amount, "UTF-8")).append("&");
                    break;
                case "Comments":
                    writer.append("comment=").append(URLEncoder.encode(comment_text, "UTF-8")).append("&");
                    break;
                case "Incidents":
                    writer.append("description=").append(URLEncoder.encode(incident_description, "UTF-8")).append("&");
                    break;
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
        protected void onPostExecute(String result) {
            // declare JSON objects and Strings we'll assign them to
            JSONObject jObject;
            String message;
            String success;

            try {
                jObject = new JSONObject(result);
                // get the 'success_msg' and 'message' keys returned from the REST API
                success = jObject.getString("success_msg");
                message = jObject.getString("message");

                // if success = 1 then there has been an error
                if (success.equals("1")) {
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_SHORT).show();

                } else {
                    // add record has been successful
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
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
