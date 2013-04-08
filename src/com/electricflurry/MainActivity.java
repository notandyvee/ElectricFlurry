package com.electricflurry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
         * Use SharedPreferences to store certaun info that might be needed */
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        
        try {
        	String dest = "/data/data/" + getPackageName() + "/databases/ElectricFlurryDB";
        	File f = new File(dest);
        	if (!f.exists()) {
        		copyDB(getBaseContext().getAssets().open("mydb"), new FileOutputStream(dest));
        	}
        } catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        
        if(savedInstanceState == null) {//this needs to be done because Android handles re attaching a fragment for you so it will be in Bundle apparently
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
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void copyDB(InputStream inputStream, OutputStream outputStream) throws IOException{
    	 byte[] buffer = new byte[1024];
         int length;
         while ((length = inputStream.read(buffer)) > 0) {
             outputStream.write(buffer, 0, length);
         }
         inputStream.close();
         outputStream.close();
     }

    
}//end of class
