package com.example.lenamarie.childactivitymonitor.SlidingTabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.lenamarie.childactivitymonitor.MinderTabs.FeedingTab;
import com.example.lenamarie.childactivitymonitor.MinderTabs.ChangingTab;
import com.example.lenamarie.childactivitymonitor.MinderTabs.MedicationTab;
import com.example.lenamarie.childactivitymonitor.MinderTabs.SleepingTab;
import com.example.lenamarie.childactivitymonitor.MinderTabs.IncidentsTab;
import com.example.lenamarie.childactivitymonitor.MinderTabs.CommentsTab;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        CharSequence Titles[]={"Feeding", "Changing", "Medication", "Sleeping", "Incidents", "Comments"};

        switch (position) {
            case 0:
                FeedingTab feedingTab = new FeedingTab();
                return feedingTab;
            case 1:
                ChangingTab changingTab = new ChangingTab();
                return changingTab;
            case 2:
                MedicationTab medicationTab = new MedicationTab();
                return medicationTab;
            case 3:
                SleepingTab sleepingTab = new SleepingTab();
                return sleepingTab;
            case 4:
                IncidentsTab incidentsTab = new IncidentsTab();
                return incidentsTab;
            case 5:
                CommentsTab commentsTab = new CommentsTab();
                return commentsTab;
            default:
                ChangingTab changingTabNew = new ChangingTab();
                return changingTabNew;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}