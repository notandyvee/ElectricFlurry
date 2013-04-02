package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment{
	String textToDis;
	
	public static final String LE_FRAGMENT = "fragment_holder";

	
	
	
	public static MainFragment newInstance() {
		MainFragment f = new MainFragment();
		
		/*
		 * Don't really need to put any arguments right now so won't bother*/
		//Bundle args = new Bundle();
		//args.putString("test", "Text I put when creating myself");
		//f.setArguments(args);
		
		return f;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		/*Seems Fragment has automatic access to some values here like getArguments()*/
		super.onCreate(savedInstanceState);
		//textToDis = getArguments().getString("test");
		
		
		
		
		
	}//end of onCreate
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		/*
		 * Where yo shit will go that will populate 
		 * the Fragments Views dynamically
		 * */
		//TextView text = (TextView)view.findViewById(R.id.name);
		//text.setText(new Foursquare().returnLeString());
		
		
		
		
		return view;
		
	}//end of onCreateView
	
	

}
