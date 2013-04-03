package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SocialNetworkFragment extends Fragment{
	
	
	public static SocialNetworkFragment newInstance() {
		SocialNetworkFragment f = new SocialNetworkFragment();
		
		return f;
	}//end of static constructor
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.social_network_fragment, container, false);
		
		return view;
	}//end of onCreateView
	
	
	
	

}//end of class
