package com.electricflurry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class FoursquareAuthFragment extends Fragment{
	
	String MY_PREFS = "MySocialSettings";
	String TAG = "FoursquareAuthFragment";
	
	String CLIENT_ID = "0GX0LSV4402VK33NWCWPKWVFE5AGWB0UCQG5SGSICWQTYA01";
	String REGISTERED_REDIRECT_URI = "http://www.dummywebsite.com/redirect_uri";
	
	
	public static FoursquareAuthFragment newInstance(SharedPreferences sendPrefs) {
		FoursquareAuthFragment f = new FoursquareAuthFragment();
		
		return f;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * Stuff you'd like to initilaize here you can
		 * */
			
	}//end of onCreate
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.foursquareauth, container, false);
		
		String url ="https://foursquare.com/oauth2/authenticate"+
		    "?client_id="+CLIENT_ID+
		    "&response_type=token"+
		    "&redirect_uri="+REGISTERED_REDIRECT_URI;
		WebView webView = (WebView)view.findViewById(R.id.le_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				
				String fragment = "#access_token=";
				
                int start = url.indexOf(fragment);
                if (start > -1) {
                    // You can use the accessToken for api calls now.
                    String accessToken = url.substring(start + fragment.length(), url.length());
                    
                    //put oAuth token in settings to le access anywhere
                    SharedPreferences settings = getActivity().getSharedPreferences(MY_PREFS, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("foursquare_oauth_token", accessToken);
                    
                    //le commit the edit
                    editor.commit();
                    
                    /*
                     * Here is where Matt can redirect to his page. Since there is not work
                     * on github from anyone I have no idea where to send it.
                     * You can delete the Toast and Log stuff as thats just to show
                     * that the token was obtained successfully
                     * */
        			
                    Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");
                	
                    Toast.makeText(view.getContext(), "Token: " + accessToken, Toast.LENGTH_SHORT).show();
                }
				
			}//end of onPageStarted
			
		});
		
		
		webView.loadUrl(url);
		
		return view;
	}//end of onCreateView
	
	
	
	
	
	

}//end of class
