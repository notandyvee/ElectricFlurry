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
import com.google.android.gms.plus.PlusShare;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/*	
Google+ requires google-play-services_lib
It is located in ~/android-sdk/extras/google/google_play_services/Lib_project
import it as an android project from existing code, it must be on the same HDD & partition
as your project, just select copy to workspace as g+/dev suggests. Then link EF to it the
same way FB is linked (properties > android tab > etc. etc.)
Also the visible/invisible and basic sharing stuff is temporary, it'll be slicker.	
*/

public class GooglePlusActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener, OnClickListener,
OnAccessRevokedListener {	
	
	private static final String TAG = "GooglePlusActivity";
	private static final int REQUEST_CODE = 79863;
	private PlusClient mPlusClient;
	private boolean mResolveOnFail;
	private ConnectionResult mConnectionResult;
	private ProgressDialog mConnectionProgressDialog;

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
		Button shareButton = (Button) findViewById(R.id.share_button);
		text = (EditText) findViewById(R.id.text_box);
		// Connect our sign in, sign out and disconnect buttons.
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

		
		//This is a very basic way to share on G+, will be changing this eventually.
		shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Launch the Google+ share dialog with attribution to your app.
				Intent shareIntent = new PlusShare.Builder()
				.setType("text/plain")
				.setText(getText())
				.setContentUrl(Uri.parse("https://developers.google.com/+/"))
				.getIntent();


				startActivityForResult(shareIntent, 0);
			}
		});
	}


	//Getter & Setter for converting user's msg for posting auto-fill. (Will change later with a better integration function)
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
		Log.v(TAG, "Connected. Yay!");

		mResolveOnFail = false;
		mConnectionProgressDialog.dismiss();

		//Switch between which buttons should be visible (using 1 layout atm, will try and do a tab-scroll layout later)
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
	}

	@Override
	public void onDisconnected() {
		Log.v(TAG, "Disconnected. Bye!");
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
		switch (view.getId()) {

		//Sign in button
		case R.id.sign_in_button:
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
			break;

			//Sign out button, disconnects user but does NOT clear default account / data.
		case R.id.sign_out_button:
			Log.v(TAG, "Tapped sign out");
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();

				//Switching button visibility's.
				findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
				findViewById(R.id.sign_out_button)
				.setVisibility(View.INVISIBLE);
				findViewById(R.id.share_button).setVisibility(
						View.INVISIBLE);
				findViewById(R.id.text_box).setVisibility(View.INVISIBLE); 				
				findViewById(R.id.share_button).setVisibility(View.INVISIBLE);
			}			

			break;
			//Revoke access button, will clear all user data and default account attached to app.
		case R.id.revoke_access_button:
			Log.v(TAG, "Tapped disconnect");
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.revokeAccessAndDisconnect(this);
			}
			break;
		default:
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


	//Helpful method from git to prevent annoying connection issues.
	private void startResolution() {
		try {
			mResolveOnFail = false;
			mConnectionResult.startResolutionForResult(this, REQUEST_CODE);
		} catch (SendIntentException e) {
			mPlusClient.connect();
		}
	}
}