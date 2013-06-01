package com.electricflurry;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.android.AndroidTwitterLogin;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TwitterActivity extends FragmentActivity{
	
	private static final String MY_TWITTER_KEY = "MeJ8BrMgphOvSMLNXnE3jg";
	private static final String MY_TWITTER_SECRET = "g4jyLfTjZ8aVXfdWlzjeLoDvvk7AHwRdMrAVTwDsOY";

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_activity);
		


		
		Button but = (Button)findViewById(R.id.tweet);
		but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				OAuthConsumer consumer = new DefaultOAuthConsumer(
		                // the consumer key of this app (replace this with yours)
						MY_TWITTER_KEY,
		                // the consumer secret of this app (replace this with yours)
						MY_TWITTER_SECRET);

		        OAuthProvider provider = new DefaultOAuthProvider(
		                "http://twitter.com/oauth/request_token",
		                "http://twitter.com/oauth/access_token",
		                "http://twitter.com/oauth/authorize");

		        /****************************************************
		         * The following steps should only be performed ONCE
		         ***************************************************/

		        // we do not support callbacks, thus pass OOB
		        try {
					String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthNotAuthorizedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		        // bring the user to authUrl, e.g. open a web browser and note the PIN code
		        // ...         

		        //String pinCode = // ... you have to ask this from the user, or obtain it
		        // from the callback if you didn't do an out of band request

		        // user must have granted authorization at this point
		        //provider.retrieveAccessToken(consumer, pinCode);

				
			}
		});
		

		
		
		
		
		
		
		
		
		
	}//end of onCreate
	
	
	Thread thread = new Thread(new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}
		
	}){

	};//.start();

	
	

}//end of class
