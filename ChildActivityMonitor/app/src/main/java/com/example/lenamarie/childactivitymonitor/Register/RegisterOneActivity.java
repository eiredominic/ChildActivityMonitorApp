package com.example.lenamarie.childactivitymonitor.Register;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lenamarie.childactivitymonitor.R;

public class RegisterOneActivity extends Activity {
    TextView btn_fwd;
    String account_type;
    String name;
    EditText nameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        btn_fwd = (TextView)findViewById(R.id.btn_fwd);
        nameTxt = (EditText)findViewById(R.id.nameTxt);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBtn_minder:
                if (checked)
                    account_type = "Minder";
                    break;
            case R.id.radioBtn_parent:
                if (checked)
                    account_type = "Parent";
                    break;
            case R.id.radioBtn_child:
                if (checked)
                    account_type = "Child";
                    break;
        }
    }
    /** Called when the user clicks the Send button */
    public void nextActivity(View view) {
        name = nameTxt.getText().toString();
        Intent intent = new Intent(this, RegisterTwoActivity.class);
        intent.putExtra("account_type", account_type);
        intent.putExtra("name", name);
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
