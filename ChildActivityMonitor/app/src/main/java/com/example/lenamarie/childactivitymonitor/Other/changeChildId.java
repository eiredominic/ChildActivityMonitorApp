package com.example.lenamarie.childactivitymonitor.Other;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenamarie.childactivitymonitor.MinderTabs.NavigationDrawer.MainMinderActivity;
import com.example.lenamarie.childactivitymonitor.R;

public class changeChildId extends Activity {
    String child_id;
    EditText child_id_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_child_id);
        child_id_editText = (EditText) findViewById(R.id.child_id_txt);

    }
    public void saveButton (View v) {
        if (child_id_editText.getText().toString().isEmpty())   {
            Toast.makeText(getApplicationContext(), "Please enter a Child ID",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            child_id = child_id_editText.getText().toString();
            Intent intent = new Intent(changeChildId.this, MainMinderActivity.class);
            intent.putExtra("child_id", child_id);
            startActivity(intent);
        }
    }
    public void closeDialog (View v) {
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_child_id, menu);
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
