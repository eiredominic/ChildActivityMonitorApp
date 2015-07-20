package com.example.lenamarie.childactivitymonitor.Register;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.R;

import java.util.Calendar;

public class RegisterOneActivity extends FragmentActivity {
    String account_type, name, address;
    EditText nameTxt, address1, address2, address3;
    LinearLayout addressLayout;
    TextView yearText, monthText, dayText, btn_fwd;
    RadioButton minderButton, parentButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        btn_fwd = (TextView)findViewById(R.id.btn_fwd);
        nameTxt = (EditText)findViewById(R.id.nameTxt);
        address1 = (EditText)findViewById(R.id.address1);
        address2 = (EditText)findViewById(R.id.address2);
        address3 = (EditText)findViewById(R.id.address3);
        addressLayout = (LinearLayout) findViewById(R.id.addressLayout);
        yearText = (TextView)findViewById(R.id.yearText);
        monthText = (TextView)findViewById(R.id.monthText);
        dayText = (TextView)findViewById(R.id.dayText);

        minderButton = (RadioButton) findViewById(R.id.radioBtn_minder);
        parentButton = (RadioButton) findViewById(R.id.radioBtn_parent);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBtn_minder:
                if (checked)
                    account_type = "Minder";
                    addressLayout.setVisibility(View.GONE);
                break;
            case R.id.radioBtn_parent:
                if (checked)
                    account_type = "Parent";
                    addressLayout.setVisibility(View.VISIBLE);
                    break;

        }
    }
    /** Called when the user clicks the Send button */
    public void nextActivity(View view) {

        // check if fields have been left empty //
        if (!minderButton.isChecked() && !parentButton.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select a Minder or Parent account",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (nameTxt.getText().toString().isEmpty())   {
            Toast.makeText(getApplicationContext(), "Please enter a name",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (account_type.equals("Parent")) {
            if (address1.getText().toString().isEmpty() || address3.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Please enter a value in Address Line 1 " +
                                "and Postcode",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (yearText.getText().toString().isEmpty() || monthText.getText().toString().isEmpty() ||
                dayText.getText().toString().isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Please enter a date of birth",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        // push text in fields to intent so that next activity can access them

        name = nameTxt.getText().toString();
        Resources res = getResources();
        String date = String.format(res.getString(R.string.date),yearText.getText().toString(),
                monthText.getText().toString(), dayText.getText().toString());

        if (account_type.equals("Parent")) {
            if (!address2.getText().toString().isEmpty()) {
                address = "" + address1.getText() + ", " + address2.getText() + ", " +
                        address3.getText();
            }
            else {
                address = "" + address1.getText() +", " + address3.getText();
            }
        }

        Intent intent = new Intent(this, RegisterTwoActivity.class);
        intent.putExtra("account_type", account_type);
        intent.putExtra("name", name);
        intent.putExtra("dob", date);
        intent.putExtra("address", address);
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
            monthText.setText(Integer.toString(month));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_one, menu);
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
