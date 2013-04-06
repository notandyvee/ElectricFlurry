package com.electricflurry;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class CheckInFragment extends Fragment{
	
	String TAG = "CheckInFragment";
	TextView text;
	EditText enterCode;
	GPSUtilities gps;
	SharedPreferences settings;
	String PREF_NAME = "MySocialSettings";
	
	
	public static CheckInFragment newInstance() {
		
		CheckInFragment f = new CheckInFragment();
		
		return f;	
		
	}//end of static constructor
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		settings = getActivity().getSharedPreferences(PREF_NAME, 0);
		/*
		 * Here I should check if check_in in SharedPreferences is a day before or more from the current day
		 * so I can delete it since they shouldn't be checked in longer to access certain pages after the show
		 * */
		
		if(settings.getString("checked_in", null) == null) {
			
			gps = new GPSUtilities((LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE),
					new LocationListener() {
	
						@Override
						public void onLocationChanged(Location location) {
							
							/*
							 * Decided to just go with just GPS because I need it to be precise*/	
							
							if(gps.isWithinRange(location)) {
								text.setText("You are checked in! You fall in range of who can check in");
								gps.removeUpdates();//if checked in through network then great! Just leave since GPS would be pointless
								Calendar now = Calendar.getInstance();
								SharedPreferences.Editor editor = settings.edit();
								editor.putString("checked_in", now.get(Calendar.MONTH)+ "/"+
										now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.YEAR));
								editor.commit();
							}
							else
								text.setText("You are not in range! \n Lon="+location.getLongitude()+" Lat="+location.getLatitude()+
										"\n Accuracy is "+ location.getAccuracy());
								
						}//end of onLocationChanged
	
						@Override
						public void onProviderDisabled(String provider) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void onProviderEnabled(String provider) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void onStatusChanged(String provider, int status,
								Bundle extras) {
							text.setText(provider+" is just not working. \n Status code is: "+status);
							
						}
				
			});
			
		}//end of if trhat checks if user ever checked in
		
		
		
		
	}//end of onCreate
	
	
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.checkin_fragment, container, false);
		/*
		 * Your Fragment's view is created here so do work to make it look pretty*/
		text = (TextView)view.findViewById(R.id.checkin_text);
		enterCode = (EditText)view.findViewById(R.id.enter_checkin_code);
		
		if(settings.getString("checked_in", null) == null) {
			
			if(gps.isWithinRange(null) == false) {
				
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						text.post(new Runnable() {
							@Override
							public void run() {
								text.setText("The Network is too slow. Please enter the code provided at the show.");
								gps.removeUpdates();
								enterCode.setVisibility(View.VISIBLE);
							}
						});
					}
				}, 120000L);
				
				gps.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20f);
				
			} else {
				/*Then I know that the last known location was within range*/
				text.setText("You Have Been Successfully Logged In!");
				Calendar now = Calendar.getInstance();
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("checked_in", now.get(Calendar.MONTH)+ "/"+
						now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.YEAR));
				editor.commit();
			}
			
		} else {
			
			text.setText("You are already checked in!\n Enjoy the show!");
			
		}
		
		return view;
	}//end of onCreateView
	
	
	
	public void onPause() {
		super.onPause();
		if(gps != null)
			gps.removeUpdates();
	}
	
	

	
	
	
	

}//end of class
