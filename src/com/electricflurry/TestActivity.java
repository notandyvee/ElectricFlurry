package com.electricflurry;

import com.facebook.Session;
import com.facebook.SessionState;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestActivity extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        
        TextView User = (TextView)findViewById(R.id.testmessage);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        String name = preferences.getString("username","");
        if (name != null){
        User.setText(name);
        }
        else {
        	User.setText("still not Working");
        }
	}
	
	
		
		
	}
	






	


