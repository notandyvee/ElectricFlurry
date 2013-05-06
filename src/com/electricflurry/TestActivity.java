package com.electricflurry;

import java.net.URL;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.ProfilePictureView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {
	ImageView picture;
	URL img_value = null;
	String id;
	private ProfilePictureView profilePictureView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        
      //  picture = (ImageView)findViewById(R.id.imageView1);
        profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        id = preferences.getString("id","");
 
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        
    	profilePictureView.setProfileId(id);
    	        	
    	  
       
	}
	
	
		
		
	}
	






	


