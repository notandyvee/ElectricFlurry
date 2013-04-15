package com.electricflurry;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VoteFragment extends Fragment{

	
	public static VoteFragment newInstance() {
		/*
		 * If you need anything to be sent in for the Fragment to initialize itself
		 * just sent it in as a paramter*/
		VoteFragment f = new VoteFragment();
		
		return f;
	}//end of static constructor
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*
		 * This is where your fragment's view is being created so do the work to make it do stuff*/
		View view = inflater.inflate(R.layout.vote_fragment, container, false);
		
		
		return view;
	}//end of onCreateView
	
	
	
	
	
	
}//end of class
