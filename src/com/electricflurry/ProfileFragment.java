package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment{
	// created the profile fragment just copying the mingle fragment -sean
	
	public static ProfileFragment newInstance() {
		
		ProfileFragment f = new ProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment, container, false);
		
		return view;
	}//end of onCreateView
	
	
	
	

}
