package com.electricflurry;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment{
	// created the profile fragment just copying the mingle fragment -sean
	TextView profile_phone, profile_name, facebook_url, twitter_url, google_url;
	
	public static ProfileFragment newInstance() {
		
		ProfileFragment f = new ProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment, container, false);


		profile_name.setText("");
		profile_phone.setText("");
		facebook_url.setText("");
		twitter_url.setText("");
		google_url.setText("");

		
		return view;
	}//end of onCreateView
	
}
