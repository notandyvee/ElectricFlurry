/*
 * This class is used to house 
 * le foursquare API stuff*/
package com.electricflurry;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class Foursquare {

	String fqUrl = 
		"https://api.foursquare.com/v2/venues/search?";
	final String CLIENT_ID = "&client_id=0GX0LSV4402VK33NWCWPKWVFE5AGWB0UCQG5SGSICWQTYA01";
	final String CLIENT_SECRET = "&client_secret=S2I30NR4V2IMM0ZORFBEFFHNFQ2KYV25PTDHDJPQAT4PTY35";
	
	ArrayList<Venue> venuesList = null;
	
	public Foursquare(String oAuthToken, String ll) {
		/*
		 * Must append the longitude and latitude
		 * */
		if(ll != null)
			fqUrl = fqUrl+"ll="+ll+CLIENT_ID+CLIENT_SECRET;
		
		/*
		 * Appending the oAuthToken since it is neccessary
		 * */
		//if(oAuthToken != null)
			//fqUrl = fqUrl+"&oauth_token="+oAuthToken;
		
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
			//DataInputStream stream = new DataInputStream(new BufferedInputStream(conn));
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn));
			String firstThing = reader.readLine();
			//String firstThing = stream.readLine();
			try {
				JSONObject jsonObj = new JSONObject(firstThing);
				jsonObj = jsonObj.getJSONObject("response");
				JSONArray venues = jsonObj.getJSONArray("venues");
				
				venuesList = new ArrayList<Venue>();
				
				for(int i =0; i < venues.length(); i++) {
					//Log.d("foursquare", "running in the fourloop to parse stuff");
					
					JSONObject oneVenue = venues.getJSONObject(i);
					
					Venue v = new Venue();
					v.putName(oneVenue.getString("name"));
					JSONObject location = oneVenue.getJSONObject("location");
					
					try{ v.putAddress(location.getString("address")); }catch(JSONException e) { v.putAddress(""); }
					try { v.putCity(location.getString("city")); }catch(JSONException e){ v.putCity("");}
					try { v.putState(location.getString("state")); }catch(JSONException e){ v.putState("");}
					
					//v.putLocation(location.getString("address"), location.getString("city"), location.getString("state"));
					
					/*
					 * can get categories JSONArray in here and then get the
					 * JSONObject in index 0 which should be the only index
					 * but for now I will not get it for testing*/
					venuesList.add(v);
					
				}//end of for loop
				
				
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
		
		
		
	}//end of queryFourSquareData
	
	
	public void searchForFun() {
		/*
		 * This method does the actual work of going through Venues to get the restaurants and bars
		 * */
		
		
		
	}//end of searchForFun
	
	
	
	
	public ArrayList<Venue> returnLeVenues() {
		return venuesList;
	}//end of 
	
	
	
	

}//end of Foursquare class
