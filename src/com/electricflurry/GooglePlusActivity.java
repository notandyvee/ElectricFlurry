package com.electricflurry;

import java.io.IOException;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.PlusClient.OnPersonLoadedListener;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*	
Google+ requires google-play-services_lib to be imported
as a project in order to work.

Steps:
-Using Android SDK Manager Extras > GooglePlayServices install
-Import an android project from existing code
-Directory:  ~/android-sdk/extras/google/google_play_services/Lib_project
-Copy to workspace
-Right click ElectricFlurry, properties, android tab, add library
-Set the target to Android 4.2.2 (not google API's)

**Note: After pulling from github you may have to re-add the library.

[Remove]
The old version of G+ that was pushed is no longer necessary
Classes to be removed:
-GooglePlayServicesErrorDialogFragment
-GooglePlusErrorDialogFragment
-GPlusVisFragment
-PlusClientFragment
Layouts to be removed:
-google_plus_menu
-google_plus_share
-google_plus_sign_in
Leave them commented out or delete / rm them from github.


[Do not remove]
The only classes needed for Google+ in the ElectricFlurry project are:
Class:
-GooglePlusActivity
Layout:
-activity_google_plus
AndroidManifest permission that was added:
-GET_ACCOUNTS
 */

public class GooglePlusActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener, OnClickListener,
OnAccessRevokedListener, OnPersonLoadedListener {	

	private static final String TAG = "GooglePlusActivity";
	private static final int REQUEST_CODE = 79863;
	private PlusClient mPlusClient;
	private boolean mResolveOnFail;
	private ConnectionResult mConnectionResult;
	private ProgressDialog mConnectionProgressDialog;
	//private String plusUrl;

	String MY_PREFS = "MySocialSettings";
	EditText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_google_plus);
		mPlusClient = new PlusClient.Builder(this, this, this)
		.setVisibleActivities("http://schemas.google.com/AddActivity")
		.build();

		mResolveOnFail = false;

		//One layout switching between visible/invisible, buttons view status'
		//Buttons & EditText
		Button shareButton = (Button) findViewById(R.id.share_button);
		text = (EditText) findViewById(R.id.text_box);
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
		findViewById(R.id.revoke_access_button).setOnClickListener(this);
		text.setOnClickListener(this); 							
		shareButton.setOnClickListener(this);
		findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);
		text.setVisibility(View.INVISIBLE);						
		shareButton.setVisibility(View.INVISIBLE);

		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");

		
		//Basic sharing
		shareButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {

				Intent shareIntent = new PlusShare.Builder()
				.setType("text/plain")
				.setText(getText())
				//.setContentUrl(Uri.parse("https://plus.google.com/u/0/b/112596573196143676178/112596573196143676178/posts"))
				.getIntent();
				startActivityForResult(shareIntent, 0);
			}
		});
	}
	
	//For sharing auto-fill.
	private String setText(){
		return this.text.getText().toString();
	}
	public String getText() {
		return setText();
	}	
	
	//On activity start PlusClient attempts to connect.
	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "Start");
		mPlusClient.connect();
	}

	
	//When exiting the activity PlusClient disconnects. Connection only exists when app & activity are open.
	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "Stop");
		mPlusClient.disconnect();
	}

	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.v(TAG, "ConnectionFailed");
		if (result.hasResolution()) {
			mConnectionResult = result;
			if (mResolveOnFail) {
				startResolution();
			}
		}
	}

	
	@Override
	public void onConnected() {
		Log.v(TAG, "Connected");

		mResolveOnFail = false;
		mConnectionProgressDialog.dismiss();

		//Switch between which buttons should be visible
		findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
		findViewById(R.id.revoke_access_button).setVisibility(View.VISIBLE);
		findViewById(R.id.text_box).setVisibility(View.VISIBLE);
		findViewById(R.id.share_button).setVisibility(View.VISIBLE);

		//Retrieve the oAuth 2.0 access token.
		final Context context = this.getApplicationContext();
		AsyncTask task = new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				String scope = "oauth2:" + Scopes.PLUS_LOGIN;
				try {
					String token = GoogleAuthUtil.getToken(context,
							mPlusClient.getAccountName(), scope);
				} catch (UserRecoverableAuthException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (GoogleAuthException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		task.execute((Void) null);

		//loadPerson method is used for fetching profile information.
		mPlusClient.loadPerson(this, "me");
		//Displays account name (email) connected.
		Toast.makeText(this, mPlusClient.getAccountName() + " is connected.", Toast.LENGTH_SHORT).show();
	}

	
	@Override
	public void onDisconnected() {
		Log.v(TAG, "Disconnected");
	}

	
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		Log.v(TAG, "ActivityResult: " + requestCode);
		if (requestCode == REQUEST_CODE && responseCode == RESULT_OK) {
			mResolveOnFail = true;
			mPlusClient.connect();
		} else if (requestCode == REQUEST_CODE && responseCode != RESULT_OK) {
			mConnectionProgressDialog.dismiss();
		}
	}
	
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.sign_in_button) {
			Log.v(TAG, "Tapped sign in");
			if (!mPlusClient.isConnected()) {
				mConnectionProgressDialog.show();
				mResolveOnFail = true;
				if (mConnectionResult != null) {
					startResolution();
				} else {
					mPlusClient.connect();
				}
			}
		} else if (id == R.id.sign_out_button) {
			Log.v(TAG, "Tapped sign out");
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();

				//Switching button visibility's.
				findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
				findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
				findViewById(R.id.share_button).setVisibility(View.INVISIBLE);
				findViewById(R.id.text_box).setVisibility(View.INVISIBLE);
				findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);
			}
		} else if (id == R.id.revoke_access_button) {
			Log.v(TAG, "Tapped disconnect");
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.revokeAccessAndDisconnect(this);
			}
		}
	}

	
	@Override
	public void onAccessRevoked(ConnectionResult status) {

		mPlusClient.connect();

		//Button visibilities
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.text_box).setVisibility(View.INVISIBLE);
		findViewById(R.id.share_button).setVisibility(View.INVISIBLE);

	}


	//Helpful method from gistfile to prevent annoying connection issues.
	private void startResolution() {
		try {
			mResolveOnFail = false;
			mConnectionResult.startResolutionForResult(this, REQUEST_CODE);
		} catch (SendIntentException e) {
			mPlusClient.connect();
		}
	}

	
	//Google+ URL stored to SharedPreferences
	//Accessing user's profile information
	@Override
	public void onPersonLoaded(ConnectionResult status, Person person) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
		
			//Adding Google+ URL to SharedPreferences under google_plus_url
			String plusUrl = person.getUrl();
			if (plusUrl != null) {
				SharedPreferences settings = getSharedPreferences(MY_PREFS, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("google_plus_url", plusUrl);
				editor.commit();
				Log.d(TAG, "User's Url has been added to SharedPreferences.");
			} else {
				Log.d(TAG, "Url not added to SharedPreferences.");
			}

			//For testing in LogCat
			//Log.d(TAG, "Display Name: " + person.getDisplayName());
			//Log.d(TAG, "URL: " + person.getUrl());
			//Log.d(TAG, "e-mail: " + mPlusClient.getAccountName());
		}
	}
	
}//End GooglePlusActivity