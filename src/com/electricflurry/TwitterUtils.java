package com.electricflurry;





import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.content.SharedPreferences;

public class TwitterUtils {

	

	public static boolean isAuthenticated(SharedPreferences prefs) {

		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);

		try {
			twitter.verifyCredentials();
			return true;
		} catch (TwitterException e) {
			return false;
		}
	}

	public static void sendTweet(SharedPreferences prefs,String msg) throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
        twitter.updateStatus(msg);
	}	
	
	public static String getUserName(SharedPreferences prefs){
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		String screenName;
		
			try {
				screenName = twitter.getScreenName();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "no user name was found";
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "no user name was found";
				
			}
		
		
		return  screenName ;
	}
}
