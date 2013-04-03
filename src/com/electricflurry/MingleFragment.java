package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MingleFragment extends Fragment{
	
	
	public static MingleFragment newInstance() {
		
		MingleFragment f = new MingleFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mingle_fragment, container, false);
		
		return view;
	}//end of onCreateView
	
	
	
	

}//end of class
