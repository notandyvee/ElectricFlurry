

package com.electricflurry;


import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Prepares a OAuthConsumer and OAuthProvider 
 * 
 * OAuthConsumer is configured with the consumer key & consumer secret.
 * OAuthProvider is configured with the 3 OAuth endpoints.
 * 
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * 
 * After the request is authorized, a callback is made here.
 * 
 */
@SuppressLint("NewApi")
public class PrepareRequestTokenActivity extends Activity {

	final String TAG = getClass().getName();
    private static int counter = 0;
	public static boolean checker = true;
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    
    public void OAuthRequestTokenTask(Context context, CommonsHttpOAuthConsumer consumer,CommonsHttpOAuthProvider provider) {
    		            this.provider = provider;
    		            this.consumer = consumer;
    		            
    		        }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	try {
    		this.consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    	    this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,Constants.ACCESS_URL,Constants.AUTHORIZE_URL);
    	} catch (Exception e) {
    		Log.e(TAG, "Error creating consumer / provider",e);
		}

        Log.i(TAG, "Starting task to retrieve request token.");
		new OAuthRequestTokenTask(this,consumer,provider).execute();
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the request token).
	 * The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent); 
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i(TAG, "Callback received : " + uri);
			Log.i(TAG, "Retrieving Access Token");
			new RetrieveAccessTokenTask(this,consumer,provider,prefs).execute(uri);
			finish();	
		}
	}

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private Context	context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;
		
		

		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs=prefs;
		}


		/**
		 * Retrieve the oauth_verifier, and store the oauth and oauth_token_secret 
		 * for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri...params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				edit.commit();

				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

				consumer.setTokenWithSecret(token, secret);
				context.startActivity(new Intent(context,AndroidTwitterSample.class));

				//executeAfterAccessTokenRetrieval();
				
				Toast.makeText(context ,getString(counter), Toast.LENGTH_LONG).show();
				Log.i(TAG, "OAuth - Access Token Retrieved");

			} catch (Exception e) {
				
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
			}

			return null;
		}


		private void executeAfterAccessTokenRetrieval() {
			
			try {
				TwitterUtils.sendTweet(prefs, "messin with my android app---testing");
			} catch (Exception e) {
				Log.e(TAG, "OAuth - Error sending to Twitter", e);
			}
		}
	}	

}