package com.electricflurry;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
	
	Timer timer = null;//holding this timer to be able to kill it if GPS ever warms up
	
	
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
		
		if( isCheckedIn(settings.getString("checked_in", null)) == false)	
		{
			
			gps = new GPSUtilities((LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE),
					new LocationListener() {
	
						@Override
						public void onLocationChanged(Location location) {
							/*
							 * Decided to just go with just GPS because I need it to be precise*/	
							
							if(gps.isWithinRange(location)) {
								text.setText("You are checked in!");
								gps.removeUpdates();//if checked in through network then great! Just leave since GPS would be pointless
								Calendar now = Calendar.getInstance();
								SharedPreferences.Editor editor = settings.edit();
								editor.putString("checked_in", now.get(Calendar.MONTH)+ "/"+
										now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.YEAR));
								editor.commit();
							}
							//else
								//text.setText("You are not in range! \n Lon="+location.getLongitude()+" Lat="+location.getLatitude()+
									//	"\n Accuracy is "+ location.getAccuracy());
								
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
							text.setText(provider+" is just not working.");
							
						}
				
			});
			
		}//end of if that checks if user ever checked in
		
		
		
		
	}//end of onCreate
	
	
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.checkin_fragment, container, false);
		/*
		 * Your Fragment's view is created here so do work to make it look pretty*/
		text = (TextView)view.findViewById(R.id.checkin_text);
		enterCode = (EditText)view.findViewById(R.id.enter_checkin_code);
		
		if( isCheckedIn(settings.getString("checked_in", null)) == false){
			GetShowToday task = new GetShowToday(gps, text, settings);
			task.execute();
		} else {
			text.setText("Already Checked-in. Enjoy the show!");
		}

		
		return view;
	}//end of onCreateView
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	public void onPause() {
		super.onPause();
		if(gps != null)
			gps.removeUpdates();
	}
	
	
	
	public boolean isCheckedIn(String date){
		if(date == null)
			return false;
		String[] splitDate = date.split("/");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]));
		Calendar now = Calendar.getInstance();
		if(now.after(cal))
			return false;
		
		/*I need the date of when the show was as well 
		 * or else I need to always query the server which I do not want to do.
		 * */
		
		return true;
	}
	
	
	
	
	
	
	/*
	 * New Thread that focuses on checking server for shows today
	 * */
	
	private class GetShowToday extends AsyncTask <Void, Void, Boolean> {
		String url = ServerConstants.PUBLIC_DNS+"resources/locations";
		GPSUtilities gps;
		TextView leText;
		SharedPreferences settings;
		
		public GetShowToday(GPSUtilities gps, TextView leText, SharedPreferences settings){
			/*Parameters to be used in onPostExecute will be sent in here*/
			this.gps = gps;
			this.leText = leText;
			this.settings = settings;
		}
		@Override
		protected Boolean doInBackground(Void... arg) {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder build = dbf.newDocumentBuilder();
				Document doc = build.parse(url);
				
				NodeList latList = doc.getElementsByTagName("latitude");
				NodeList lngList = doc.getElementsByTagName("longitude");
				if(latList.getLength() == 0 || lngList.getLength() == 0)
					return false;
				
				gps.setShowLocation(Double.parseDouble(latList.item(0).getTextContent()), Double.parseDouble(lngList.item(0).getTextContent()));
				
			} catch (ParserConfigurationException e) {
				Log.e("GetShowToday", "ParserConfigurationException", e);
				return false;
			} catch (SAXException e) {
				Log.e("GetShowToday", "SAXException", e);
				return false;
			} catch (IOException e) {
				Log.e("GetShowToday", "IOException", e);
				return false;
			} catch(Exception e) {
				Log.e("GetShowToday", "Exception", e);
				return false;
			}
			
			
			
			return true;
		}//end of doInBackground
		
		protected void onPostExecute(Boolean result) {
			/*I should actually return strings as errors or success messages 
			 * since I will be printing it out*/
			
			if(result == false) {
				leText.setText("There was an error downloading shows.");
				//Maybe a try again button should appear instead of the edit text
			}
			else 
			{
				//got show location coordinates so can do gps stuff
				doGPSStuff();
			}
			
		}//end of onPostExecute
		
	public void doGPSStuff() {
			
			if(gps.isWithinRange(null) == false) {
				
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						text.post(new Runnable() {
							@Override
							public void run() {
								//text.setText("The Network is too slow.");
								//gps.removeUpdates();
								//rather than just quit it actually try network as well
								gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 20f);
							}
						});
					}
				}, 20000L);//I think I will only listen for 15 seconds then just use network
				
				gps.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20f);
				
			} else {
				/*Then I know that the last known location was within range*/
				text.setText("You Have Been Successfully Checked In!");
				Calendar now = Calendar.getInstance();
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("checked_in", now.get(Calendar.MONTH)+ "/"+
						now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.YEAR));
				editor.commit();
			}
		
		
		
		
	}//end of doGPSStuff


		
	}//end of GetShowToday inner class
	
	

	
	
	
	

}//end of class
