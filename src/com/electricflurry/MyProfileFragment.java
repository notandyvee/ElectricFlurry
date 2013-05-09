package com.electricflurry;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.electricflurry.R;

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
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileFragment extends Fragment implements ConsumeCursor {
	EditText edit_name, edit_phone;
	LinearLayout hazUserLayout, noUserLayout;
	String name, phone,facebookURL, twitterURL, googleURL;
	TextView nameView, phoneNumberView, serverIdView;
	Profile profile = new Profile();
	ElectricFlurryDatabase db;
	String result;
	
	//created the myprofile fragment by just copying the mingle fragment -sean

	public static MyProfileFragment newInstance() {

		MyProfileFragment f = new MyProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		View view = inflater.inflate(R.layout.myprofile_fragment, container, false);
		
		db = new ElectricFlurryDatabase(getActivity());
		hazUserLayout = (LinearLayout)view.findViewById(R.id.haz_user);
		noUserLayout = (LinearLayout)view.findViewById(R.id.no_haz_user);
		nameView = (TextView)view.findViewById(R.id.saved_name);
		phoneNumberView = (TextView)view.findViewById(R.id.saved_phone);
		serverIdView = (TextView)view.findViewById(R.id.server_id);
		
		try {
			db.leQuery("user", new String[] {"name", "phone", "server_id"}, null, null, null, null, null, this);
		} catch (CursorIndexOutOfBoundsException e) {
			
		}
		
		
		if(profile.getName().equals("Unavailable") && profile.getPhoneNumber().equals("Unavailable")) {
			
			noUserLayout.setVisibility(View.VISIBLE);
			edit_name = (EditText) view.findViewById(R.id.edit_name);
			edit_phone = (EditText) view.findViewById(R.id.edit_phone);
			//edit_facebook = (EditText) view.findViewById(R.id.edit_facebook);
			//edit_twitter = (EditText) view.findViewById(R.id.edit_twitter);
			//edit_google = (EditText) view.findViewById(R.id.edit_google);
			
			
			Button save = (Button) view.findViewById(R.id.save);
			
			edit_name.setHint("My Name");
			edit_phone.setHint("My Phone Number");
			//edit_facebook.setHint("My Facebook URL");
			//edit_twitter.setHint("My Twitter URL");
			//edit_google.setHint("My Google+ URL");
			
			save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					name = edit_name.getText().toString();
					phone = edit_phone.getText().toString();
					//facebookURL = edit_facebook.getText().toString();
					//twitterURL = edit_twitter.getText().toString();
					//googleURL = edit_google.getText().toString();
					
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
						    	HttpClient httpclient = new DefaultHttpClient();
							    HttpPost httppost = new HttpPost(
							    		"http://ec2-54-244-101-0.us-west-2.compute.amazonaws.com:8080/electricflurry/resources/newuser/"+name+"/"+phone
							    		);
						    	/*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
						        nameValuePairs.add(new BasicNameValuePair("username", name));
						        nameValuePairs.add(new BasicNameValuePair("phonenumber", phone));
						        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/
						        
						        HttpResponse response = httpclient.execute(httppost);
						        
						        result = EntityUtils.toString(response.getEntity());
						        result = result.trim();
						        if(!result.equals("Oh no! Failed :(")) {
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
							        db.submitFirstUser(name, phone, Integer.parseInt(result));
							        
						        } else {
						        	MyProfileFragment.this.errorToast();
						        }
						        
						        
						    }
						    catch(ClientProtocolException e){
						    	MyProfileFragment.this.errorToast();
						    }
						    catch (IOException e) {
						    	MyProfileFragment.this.errorToast();
						    }
						}//end of run
						
					}).start();
					
				}//end of button onClick()
			});
		
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
	
	
	
	
	
	
	
	
	
	
}
