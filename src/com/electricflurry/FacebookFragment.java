package com.electricflurry;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FacebookFragment extends Fragment {
	
	private static final String TAG = "FacebookFragment";
	private UiLifecycleHelper uiHelper;
	private Button publishButton;
	private String  facebookUserName;
	private Bitmap facebookProfilePicture;
	private URL facebookURL;
	static TextView welcome;
	ImageView profilepic;
	SharedPreferences preferences;
	private ProfilePictureView profilePictureView;
	
	public static FacebookFragment newInstance() {
		FacebookFragment f = new FacebookFragment();
		
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.facebook_fragment, container, false);
	    welcome = (TextView) view.findViewById(R.id.welcome);
	    profilepic=(ImageView)view.findViewById(R.id.profilepic);
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
	    profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
	    profilePictureView.setCropped(true);
	    publishButton = (Button) view.findViewById(R.id.publishButton);
	    publishButton.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            publishFeedDialog();        
	        }
	    });

	    return view;
	}
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback );
	    uiHelper.onCreate(savedInstanceState);
	    preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}
	
	public void Sessionchange(Session session, SessionState state, Exception exception){
		
		onSessionStateChange(session, state, exception);
		
	}

	 private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	publishButton.setVisibility(View.VISIBLE);	    	
	        Log.i(TAG, "Logged in...");
	        

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	                
	                welcome.setText("Hello there " + user.getName() + "!"); 
	                                
	               	                
	                profilePictureView.setProfileId(user.getId());
	                
	                
	                SharedPreferences.Editor editor = preferences.edit();
	                editor.putString("id", user.getId());
	                editor.putString("username", user.getName());
	                editor.commit();
	                
	                facebookUserName = user.getUsername();
	                         
	               // info.setText("User Name is - " + facebookUserName + "      Url is - " + facebookURL );
	              }
	            }
	          });
	        
	    } else if (state.isClosed()) {
	    	publishButton.setVisibility(View.INVISIBLE);
	        Log.i(TAG, "Logged out...");
	    }
	    
	    
	    
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	
	 
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	   ;
	    Session session = Session.getActiveSession();
	    if (session != null &&
	         (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private void publishFeedDialog() {
	    Bundle params = new Bundle();
	    params.putString("message", "testing 1,2,3");
	   // params.putString("caption", "Build great social apps and get more installs.");
	   // params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	  //  params.putString("link", "https://developers.facebook.com/android");
	  //  params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(getActivity(),
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(getActivity(),
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(getActivity().getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(getActivity().getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(getActivity().getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }

	        })
	        .build();
	    feedDialog.show();
	}
	
	
	
	
	
}
