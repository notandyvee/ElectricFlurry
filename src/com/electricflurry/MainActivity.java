package com.electricflurry;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.Fragment;

public class MainActivity extends FragmentActivity {
	String PREF_NAME = "MySocialSettings";
	SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*
         * Use SharedPreferences to store certaun info that might be needed */
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        
        /*
         * The following is one way to create le Fragment*/
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_holder, MainFragment.newInstance(), "fuck");   
        
        
        ft.commit();
        
        
    }//end of onCreate class
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}//end of class
