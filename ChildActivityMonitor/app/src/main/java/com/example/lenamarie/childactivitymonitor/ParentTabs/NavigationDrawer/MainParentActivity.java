package com.example.lenamarie.childactivitymonitor.ParentTabs.NavigationDrawer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.lenamarie.childactivitymonitor.ParentTabs.ChangingTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.CommentsTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.FeedingTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.IncidentsTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.MedicationTab;
import com.example.lenamarie.childactivitymonitor.ParentTabs.SleepingTab;

import com.example.lenamarie.childactivitymonitor.R;


public class MainParentActivity extends ActionBarActivity implements NavigationInteraction
        .NavigationDrawerCallbacks {

    private NavigationInteraction mNavigationInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parent_activity);

        mNavigationInteraction = (NavigationInteraction)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set a toolbar which will replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ChangingTab())
                        .commit();
                break;
            case 1:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FeedingTab())
                        .commit();
                break;
            case 2:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new SleepingTab())
                        .commit();
                break;
            case 3:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MedicationTab())
                        .commit();
                break;
            case 4:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new IncidentsTab())
                        .commit();
                break;
            case 5:

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new CommentsTab())
                        .commit();
                break;
        }



    }
}