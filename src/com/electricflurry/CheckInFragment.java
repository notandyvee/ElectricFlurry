package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CheckInFragment extends Fragment{
	
	
	public static CheckInFragment newInstance() {
		
		CheckInFragment f = new CheckInFragment();
		
		return f;	
		
	}//end of static constructor
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.checkin_fragment, container, false);
		/*
		 * Your Fragment's view is created here so do work to make it look pretty*/
		
		return view;
	}//end of onCreateView
	
	
	
	

}//end of class
