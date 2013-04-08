package com.electricflurry;

import java.io.File;

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
	TestDB db = new TestDB(getActivity());
	
	public static ProfileFragment newInstance() {
		
		ProfileFragment f = new ProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment, container, false);
		
		db.open();
		Cursor c = db.getUser(0);

		profile_name.setText(c.getColumnIndex("KEY_USERNAME"));
		profile_phone.setText(c.getColumnIndex("KEY_PHONE"));
		facebook_url.setText(c.getColumnIndex("KEY_FACEBOOK"));
		twitter_url.setText(c.getColumnIndex("KEY_TWITTER"));
		google_url.setText(c.getColumnIndex("KEY_GOOGLE"));
		db.close();
		
		return view;
	}//end of onCreateView
	
}
