package com.example.lenamarie.childactivitymonitor.Register;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.R;

public class RegisterTwoActivity extends Activity {

    EditText uniqueIdTxt;
    String unique_id, account_type, name, date, address, parent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        uniqueIdTxt = (EditText)findViewById(R.id.uniqueIdTxt);

        Intent intent = getIntent();
        account_type = intent.getStringExtra("account_type");
        name = intent.getStringExtra("name");
        date = intent.getStringExtra("dob");

        if (account_type.equals("Parent")) {
            address = intent.getStringExtra("address");
        }
        if (account_type.equals("Child")) {
            parent_id = intent.getStringExtra("parent_id");
        }
    }

    /** Called when the user clicks the Prev button */
    public void prevActivity(View view) {
        Intent intent = new Intent(this, RegisterOneActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Next button */
    public void nextActivity(View view) {

        if (uniqueIdTxt.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a unique id",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        unique_id = uniqueIdTxt.getText().toString();
        Intent intent;
        if (account_type.equals("Child")) {
            intent = new Intent(this, RegisterFourActivity.class);
            intent.putExtra("parent_id", parent_id);
        }
        else {
            intent = new Intent(this, RegisterThreeActivity.class);
        }

        if (account_type.equals("Parent")) {
            intent.putExtra("address", address);
        }
        intent.putExtra("account_type", account_type);
        intent.putExtra("name", name);
        intent.putExtra("dob", date);
        intent.putExtra("unique_id", unique_id);
        startActivity(intent);
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
