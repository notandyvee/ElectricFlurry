package com.electricflurry;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MainActivity extends FragmentActivity {
	String PREF_NAME = "MySocialSettings";
	SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*
         * Use SharedPreferences to store certain info that might be needed */
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        
        
        if(savedInstanceState == null) {
        	//this needs to be done because Android handles re attaching a fragment for you so it will be in Bundle apparently
	        /*
	         * The following is one way to create le Fragment*/
	        FragmentManager fm = getSupportFragmentManager();
	        FragmentTransaction ft = fm.beginTransaction();
	        ft.add(R.id.fragment_holder, MainFragment.newInstance(), "fuck");   
	        
	        
	        ft.commit();
        }
        
        
    }//end of onCreate
    
    public void addFragment(int resource, Fragment frag) {
    	
    	FragmentManager fm = getSupportFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	ft.replace(R.id.fragment_holder, frag);
    	ft.addToBackStack(null);
    	
    	ft.commit();
    }
    
    
    
	public void popUpDialog(int voteId, int userId){

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if(prev != null) { ft.remove(prev); }
		ft.addToBackStack(null);
		
		MyDialogFragment frag = MyDialogFragment.newInstance(voteId, userId);
		
		frag.show(ft, "dialog");
		
	}//end of popUpDialog
	
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    
}//end of class
