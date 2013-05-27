package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashFragment extends Fragment{
	/*
	 * This Fragment is something needed for facebook to work well
	 * but can probably be edited to look how we need it to
	 * */
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
		
	    View view = inflater.inflate(R.layout.splash, 
	            container, false);
	    
	    
	    
	    return view;
	}

}//end of SplashFragment
