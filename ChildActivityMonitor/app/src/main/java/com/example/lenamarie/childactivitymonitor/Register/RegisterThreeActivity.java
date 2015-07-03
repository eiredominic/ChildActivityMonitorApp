package com.example.lenamarie.childactivitymonitor.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.R;

public class RegisterThreeActivity extends Activity {
    String account_type;
    String unique_id;
    String name;

    EditText emailAddressTxt;
    EditText emailAddressTxtRepeat;
    String email_address;
    String email_address_repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_three);

        emailAddressTxt = (EditText) findViewById(R.id.passwordTxt);
        emailAddressTxtRepeat = (EditText) findViewById(R.id.emailTxtRepeat);

        Intent intent = getIntent();
        account_type = intent.getStringExtra("account_type");
        name = intent.getStringExtra("name");
        unique_id = intent.getStringExtra("unique_id");


    }
    public boolean emailCheck() {

        boolean emailMatch = false;

        email_address = emailAddressTxt.getText().toString();
        email_address_repeat = emailAddressTxtRepeat.getText().toString();

        if (email_address.equals(email_address_repeat)) {
            emailMatch = true;
        }
        return emailMatch;


    }

    /** Called when the user clicks the Prev button */
    public void prevActivity(View view) {
        Intent intent = new Intent(this, RegisterTwoActivity.class);
        intent.putExtra("account_type", account_type);
        intent.putExtra("name", name);

        startActivity(intent);

    }

    /** Called when the user clicks the Next button */
    public void nextActivity(View view) {
        if (emailCheck()) {

            Intent intent = new Intent(this, RegisterFourActivity.class);
            intent.putExtra("account_type", account_type);
            intent.putExtra("unique_id", unique_id);
            intent.putExtra("email_address", email_address);
            intent.putExtra("name", name);

            startActivity(intent);

        }
        else {
            Toast.makeText(getApplicationContext(), "Email addresses do not match",
                    Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_three, menu);
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
