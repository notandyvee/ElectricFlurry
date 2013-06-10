package com.electricflurry;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpStatus;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileFragment extends Fragment implements ConsumeCursor {
	EditText edit_name, edit_phone;
	LinearLayout noUserLayout;
	RelativeLayout hazUserLayout;
	String name, phone,facebookURL, twitterURL, googleURL;
	TextView nameView, phoneNumberView, serverIdView;
	Profile profile = new Profile();
	ElectricFlurryDatabase db;
	String result;
	Button save;
	boolean attemptingUpload = false;//used to kepe the user from continuously uploading a user until it was successful
	
	//created the myprofile fragment by just copying the mingle fragment -sean

	public static MyProfileFragment newInstance() {

		MyProfileFragment f = new MyProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		View view = inflater.inflate(R.layout.myprofile_fragment, container, false);
		
		hazUserLayout = (RelativeLayout)view.findViewById(R.id.haz_user);
		noUserLayout = (LinearLayout)view.findViewById(R.id.no_haz_user);
		nameView = (TextView)view.findViewById(R.id.saved_name);
		phoneNumberView = (TextView)view.findViewById(R.id.saved_phone);
		serverIdView = (TextView)view.findViewById(R.id.server_id);
		
		SharedPreferences settings = getActivity().getSharedPreferences("profile", 0);
		if(settings.getString("name", null) != null) {
			profile.setName(settings.getString("name", null));
			profile.setPhoneNumber(settings.getString("phone_number", null));
			profile.setServerId(settings.getInt("server_id", 0));
		}
		
		
		if(profile.getName().equals("Unavailable") && profile.getPhoneNumber().equals("Unavailable")) {
			
			noUserLayout.setVisibility(View.VISIBLE);
			edit_name = (EditText) view.findViewById(R.id.edit_name);
			edit_phone = (EditText) view.findViewById(R.id.edit_phone);
			
			
			save = (Button) view.findViewById(R.id.save);
			
			edit_name.setHint("My Name");
			edit_phone.setHint("My Phone Number");
			
			save.setOnClickListener(uploadUserListener);
		
		} else {
			/*
			 * Then I know that a user was inputted thus don't even give the user to input a user
			 * */
			
			hazUserLayout.setVisibility(View.VISIBLE);
			nameView.setText(profile.getName());
			phoneNumberView.setText(profile.getPhoneNumber());
			serverIdView.setText(profile.getServerId()+"");
		}
		
		
		return view;
	}//end of onCreateView


	@Override
	public void consumeCursor(Cursor cursor) {
		// TODO Auto-generated method stub
 
		cursor.moveToFirst();
		profile.setName(cursor.getString(0));
		profile.setPhoneNumber(cursor.getString(1));
		profile.setServerId(cursor.getInt(2));
	}
	
	public void errorToast() {
    	edit_name.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getActivity(), "Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
			}
        });
	}
	
	public boolean isAnyBadResponse(int code){
		switch(code) {
			case HttpStatus.SC_GATEWAY_TIMEOUT :
				return true;
		}
		
		return false;
	}
	
	View.OnClickListener uploadUserListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(!attemptingUpload) {
				attemptingUpload = true;
				save.setOnClickListener(null);
				name = edit_name.getText().toString();
				phone = edit_phone.getText().toString();
				
				if (!name.equalsIgnoreCase("")) {
					profile.setName(name);
				}
				if (!phone.equalsIgnoreCase("")) {
					profile.setPhoneNumber(phone);
				}
				
				new Thread(new Runnable() {
	
					@Override
					public void run() {
						
						/*I will add the user on the server first then add it in the database if successful
						 * */
						
					    try {
					    	HttpParams httpParameters = new BasicHttpParams();
					    	int timeoutConnection = 3000;
					    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
					    	/*The above is basically setting up a timeout to keep it from hanging*/
					    	
					    	HttpClient httpclient = new DefaultHttpClient(httpParameters);
						    HttpPost httppost = new HttpPost(
						    		"http://ec2-54-214-95-164.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/newuser/"+name+"/"+phone
						    		);
	
					        
					        HttpResponse response = httpclient.execute(httppost);
					        int responseStatus = response.getStatusLine().getStatusCode();
					        
					        result = EntityUtils.toString(response.getEntity());
					        result = result.trim();
					        if(!result.equals("Oh no! Failed :(") && isAnyBadResponse(responseStatus) != true) {
						        edit_name.post(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
										noUserLayout.setVisibility(View.GONE);
										hazUserLayout.setVisibility(View.VISIBLE);
										nameView.setText(name);
										phoneNumberView.setText(phone);
										serverIdView.setText(result);
									}
						        });
						        /*I haz success so you can now submit user but I will 
						         * want to use the returned ID and add it to the database*/
						        SharedPreferences settings = getActivity().getSharedPreferences("profile", 0);
						        SharedPreferences.Editor edit = settings.edit();
						        edit.putString("name", name);
						        edit.putString("phone_number", phone);
						        edit.putInt("server_id", Integer.parseInt(result));
						        edit.commit();
						        
					        } else {
					        	MyProfileFragment.this.errorToast();
					        	save.setOnClickListener(uploadUserListener);
					        	attemptingUpload = false;
					        }
					        
					        
					    }
					    catch(SocketTimeoutException e){
					    	Log.e("MyProfileFragment", "A SocketTimeoutException occurred!");
					    	Toast.makeText(getActivity(), "SocketTimeoutException occurred!", Toast.LENGTH_LONG).show();
					    	save.setOnClickListener(uploadUserListener);
					    	attemptingUpload = false;
					    }
					    catch(ClientProtocolException e){
					    	MyProfileFragment.this.errorToast();
					    	save.setOnClickListener(uploadUserListener);
					    	attemptingUpload = false;
					    }
					    catch (IOException e) {
					    	MyProfileFragment.this.errorToast();
					    	save.setOnClickListener(uploadUserListener);
					    	attemptingUpload = false;
					    }
					    
					}//end of run
					
				}).start();
				
			}//end of attemptingUpload check
			
		}//end of button onClick()
	};
	
	
	
	
	
	
	
	
	
	
}//end of class
