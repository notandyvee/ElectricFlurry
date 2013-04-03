/*
 * This class is used to house 
 * le foursquare API stuff*/
package com.electricflurry;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class Foursquare {

	String fqUrl = 
		"https://api.foursquare.com/v2/venues/search?";
	
	String listOfVenues = "";
	
	public Foursquare(String oAuthToken, String ll) {
		/*
		 * Must append the longitude and latitude
		 * */
		if(ll != null)
			fqUrl = fqUrl+"ll="+ll;
		
		/*
		 * Appending the oAuthToken since it is neccessary
		 * */
		if(oAuthToken != null)
			fqUrl = fqUrl+"&oauth_token="+oAuthToken;
		
		fqUrl = fqUrl+"&v=20130403";
		
		Log.d("Foursquare", fqUrl);
	}//end of constructor
	
	
	public void queryFoursquareData() throws NetworkOnMainThreadException{
		
		/*
		 * right now this doesn't do anything useful as
		 * foursquare doesn't really have any posting stuff
		 * and the checkin feature will be implemented by me
		 * but I am planning on getting restaurants and bars near 
		 * the shows that Electric Flurry will play at for before
		 * and after the show*/
		try {
			URL url = new URL(fqUrl);
			InputStream conn = url.openStream();
			DataInputStream stream = new DataInputStream(new BufferedInputStream(conn));
			
			String firstThing = stream.readLine();
			try {
				JSONObject jsonObj = new JSONObject(firstThing);
				jsonObj = jsonObj.getJSONObject("response");
				JSONArray venues = jsonObj.getJSONArray("venues");
				
				for(int i =0; i < venues.length(); i++) {
					Log.d("foursquare", "running in the fourloop to parse stuff");
					JSONObject oneVenue = venues.getJSONObject(i);
					listOfVenues = listOfVenues + " "+ oneVenue.getString("name");
					
					//just a way to add commas
					listOfVenues = listOfVenues+ (i == venues.length() -1 ?"" :",");
				}
				
				
			}
			catch(JSONException e) {
				Log.d("Foursquare", "JSON issue", e);
			}
			
			conn.close();
		}
		catch(MalformedURLException e) { Log.d("Foursquare", "The URL failed"); }
		catch(IOException e) { 
			Log.d("Foursquare", "InputStream failed for some reason"); 
			Log.d("Foursquare", "InputStream Stacktrace", e);
			}
		
		
		
	}
	
	
	public String returnLeString() {
		return listOfVenues;
	}//end of returnLeString
	
	
	
	

}//end of Foursquare class
