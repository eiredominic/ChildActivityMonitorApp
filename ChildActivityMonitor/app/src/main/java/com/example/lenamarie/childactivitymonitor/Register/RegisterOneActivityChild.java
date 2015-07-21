package com.example.lenamarie.childactivitymonitor.Register;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.R;

import java.util.Calendar;

public class RegisterOneActivityChild extends FragmentActivity {
    String name, parent_id;
    EditText nameTxt, parentIdText;
    TextView yearText, monthText, dayText, btn_fwd;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one_child);
        btn_fwd = (TextView) findViewById(R.id.btn_fwd);
        nameTxt = (EditText) findViewById(R.id.nameTxt);
        yearText = (TextView)findViewById(R.id.yearText);
        monthText = (TextView)findViewById(R.id.monthText);
        dayText = (TextView)findViewById(R.id.dayText);
        parentIdText = (EditText)findViewById(R.id.parentIdTxt);

        sharedPref = getSharedPreferences("loginpreferences", Context.MODE_PRIVATE);
        parent_id = sharedPref.getString("parent_id", null);

        assert parent_id != null;
        if (!parent_id.isEmpty()){
            parentIdText.setText(parent_id);
        }
    }

    /**
     * Called when the user clicks the Next button
     */
    public void nextActivity(View view) {
        if (nameTxt.getText().toString().isEmpty())   {
            Toast.makeText(getApplicationContext(), "Please enter a name",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (parentIdText.getText().toString().isEmpty())   {
            Toast.makeText(getApplicationContext(), "Please enter a parent ID",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (yearText.getText().toString().isEmpty() || monthText.getText().toString().isEmpty() ||
                dayText.getText().toString().isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Please enter a date of birth",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Resources res = getResources();
        int month = Integer.parseInt(monthText.getText().toString());
        String date = String.format(res.getString(R.string.date),yearText.getText().toString(),
                month + 1, dayText.getText().toString());


        name = nameTxt.getText().toString();
        parent_id = parentIdText.getText().toString();

        Intent intent = new Intent(this, RegisterTwoActivity.class);
        intent.putExtra("account_type", "Child");
        intent.putExtra("name", name);
        intent.putExtra("dob", date);
        intent.putExtra("parent_id", parent_id);


        startActivity(intent);
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
            monthText.setText(Integer.toString(month + 1));
            dayText.setText(Integer.toString(day));
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
}
