package com.electricflurry;

import oauth.signpost.OAuth;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidTwitterSample extends Activity {

	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private TextView loginStatus;
	private EditText userPost;
	private String TAG = "AndroidTwitterSample";
	
    final Runnable mUpdateTwitterNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();
        }
    };

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_sample);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        loginStatus = (TextView)findViewById(R.id.login_status);
        Button tweet = (Button) findViewById(R.id.btn_tweet);
        Button clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);
        userPost = (EditText)findViewById(R.id.twitter_user_tweet);
        
        tweet.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Send a tweet. If the user hasn't authenticated to Tweeter yet, he'll be redirected via a browser
        	 * to the twitter login page. Once the user authenticated, he'll authorize the Android application to send
        	 * tweets on the users behalf.
        	 */
            public void onClick(View v) {
            	String lePost = userPost.getText().toString().trim();
            	if(!lePost.equals("")){
	            	new Thread(new Runnable(){
						@Override
						public void run() {
			            	if (TwitterUtils.isAuthenticated(prefs)) {
			            		sendTweet();
			            	} else {
			            		loginStatus.post(new Runnable(){
									@Override
									public void run() {
					    				Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
					    				i.putExtra("tweet_msg",getTweetMsg());
					    				startActivity(i);
									}
			            		});
			            	}
						}            		
	            	}){	
	            	}.start();//end of thread    
            	}//end of if that checks if user inputted any text
            	else{
            		Toast.makeText(AndroidTwitterSample.this, 
            				"Please enter a post. Maybe about how excited you are for the Electric Flurry Show", Toast.LENGTH_LONG).show();
            	}
            }
        });

        clearCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearCredentials();
            	updateLoginStatus();
            }
        });
        

        
	}//end of onCreate

	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
		Log.d("AndroidTwitterSample", "onResume is running");
	}

	public void updateLoginStatus() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				final boolean result = TwitterUtils.isAuthenticated(prefs);
				loginStatus.post(new Runnable(){
					@Override
					public void run() {
						loginStatus.setText("Logged into Twitter : " + result);
					}
				});
			}
		}){
		}.start();//end of Thread
		
		
	}//end of updateLoginStatus


	private String getTweetMsg() {
		return userPost.getText().toString();
		//return "Tweeting from Android App at " + new Date().toLocaleString();
	}	

	public void sendTweet() {
		Thread t = new Thread() {
	        public void run() {

	        	try {
	        		TwitterUtils.sendTweet(prefs,getTweetMsg());
	        		mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
	        }

	    };
	    t.start();
	}

	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}
	
	
	
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent); 
		Log.d("AndroidTwitterSample", "onNewIntent is running");
		final String msg = userPost.getText().toString();
		userPost.setText("");
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					
					TwitterUtils.sendTweet(prefs, msg);
					userPost.post(new Runnable(){
						@Override
						public void run() {
							Toast.makeText(AndroidTwitterSample.this, "Successfully Tweeted!", Toast.LENGTH_SHORT).show();
						}
					});
					
				} catch (Exception e) {
					Log.e(TAG, "OAuth - Error sending to Twitter", e);
					userPost.post(new Runnable(){
						@Override
						public void run() {
							Toast.makeText(AndroidTwitterSample.this, "An error may have occurred. Please try again.", Toast.LENGTH_SHORT).show();
							userPost.setText(msg);
						}
						
					});
				}
			}	
		}){
			
		}.start();

		
	}//end of onNewIntent
	
	
	
	
	
	
	
	
	
	
}//end of class
