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

public class RegisterFourActivity extends Activity {
    String account_type;
    String unique_id;
    String name;
    String email_address;
    String password;
    String passwordRepeat;

    EditText passwordTxt;
    EditText passwordTxtRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_four);

        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        passwordTxtRepeat = (EditText) findViewById(R.id.passwordTxtRepeat);

        Intent intent = getIntent();
        account_type = intent.getStringExtra("account_type");
        unique_id = intent.getStringExtra("unique_id");
        name = intent.getStringExtra("name");
        email_address = intent.getStringExtra("email_address");

    }
    public boolean passwordCheck() {
        boolean passwordMatch = false;

        password = passwordTxt.getText().toString();
        passwordRepeat = passwordTxtRepeat.getText().toString();

        if (password.equals(passwordRepeat)) {
            passwordMatch = true;
        }

        return passwordMatch;
    }

    /** Called when the user clicks the Prev button */
    public void prevActivity(View view) {


        Intent intent = new Intent(this, RegisterThreeActivity.class);
        intent.putExtra("account_type", account_type);
        intent.putExtra("unique_id", unique_id);
        intent.putExtra("name", name);

        startActivity(intent);

    }
    /** Called when the user clicks the Send button */
    public void nextActivity(View view) {
        if (passwordCheck()) {

            Intent intent = new Intent(this, RegisterFiveActivity.class);
            intent.putExtra("account_type", account_type);
            intent.putExtra("unique_id", unique_id);
            intent.putExtra("name", name);

            intent.putExtra("email_address", email_address);
            intent.putExtra("password", password);

            startActivity(intent);

        }
        else {
            Toast.makeText(getApplicationContext(), "Passwords do not match",
                    Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_four, menu);
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
