package com.electricflurry;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionFragment extends Fragment{
	/*
	 * This Fragment is something needed for facebook to work well
	 * but can probably be edited to look how we need it to
	 * */
	//private static final int REAUTH_ACTIVITY_CODE = 100;
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private UiLifecycleHelper uiHelper;
	private EditText userInput;
	
	
	/*This private variable is the share button*/
	private Button shareButton;
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	protected static final String TAG = "SelectionFragment.java";
	private boolean pendingPublishReauthorization = false;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(final Session session, final SessionState state, final Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}//end of onCreate()
	
	/*Listener to allow user to share*/
	View.OnClickListener shareButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            publishStory();        
        }
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.selection, container, false);
	    
	    // Find the user's profile picture custom view
	    profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
	    profilePictureView.setCropped(true);

	    // Find the user's name view
	    userNameView = (TextView) view.findViewById(R.id.selection_user_name);
	    
	    //find the share button
	    shareButton = (Button) view.findViewById(R.id.shareButton);
	    shareButton.setOnClickListener(shareButtonListener);
	    userInput = (EditText)view.findViewById(R.id.user_post);
	    // Check for an open session
	    Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
	        // Get the user's data
	        makeMeRequest(session);
	    }
	    if (savedInstanceState != null) {
	    	if(savedInstanceState.getString("saved_user_post")!= null){
	    		userInput.setText(savedInstanceState.getString("saved_user_post"));
	    	}
	        pendingPublishReauthorization = 
	            savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
	        
	    }
	    
	    return view;
	}//end of onCreateView
	
	/*
	 * Private method that requests users data
	 * */
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {
	                if (user != null) {
	                    // Set the id for the ProfilePictureView
	                    // view that in turn displays the profile picture.
	                    profilePictureView.setProfileId(user.getId());
	                    // Set the Textview's text to the user's name.
	                    userNameView.setText(user.getName());
	                }
	            }
	            if (response.getError() != null) {
	                // Handle errors, will do so later.
	            }
	        }
	    });
	    request.executeAsync();
	    
	} 
	
	/*
	 * private method that will respond to session changes and call 
	 * the makeMeRequest() method if the session's open
	 * */
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	    if (session != null && session.isOpened()) {
	        // Get the user's data.
	        makeMeRequest(session);
	    }
	    /*The following checks if user is logged in and hides or shows the share button*/
	    if (state.isOpened()) {
	    	Log.d(TAG, "State is being check and its"+(state.isOpened()?" open": " closed"));
	        shareButton.setVisibility(View.VISIBLE);
	        userInput.setVisibility(View.VISIBLE);
	        if (pendingPublishReauthorization && 
	                state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
	            pendingPublishReauthorization = false;
	            publishStory();
	        }
	    } else if (state.isClosed()) {
	        shareButton.setVisibility(View.INVISIBLE);
	        userInput.setVisibility(View.INVISIBLE);
	    }
	}//end of onSessionStateChanged
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
	    super.onSaveInstanceState(bundle);
	    String temp = userInput.getText().toString().trim();
	    if(temp != null && !temp.equals(""))
	    	bundle.putString("saved_user_post", temp);
	    bundle.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
	    uiHelper.onSaveInstanceState(bundle);
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
	
	
	
	
	
	
	/*
	 * What actually publishes the status update
	 * */
	
	private void publishStory() {
		String userPost = userInput.getText().toString().trim();
		if(userPost != null && !userPost.equals("")){
		    Session session = Session.getActiveSession();
		    
		    if (session != null){
	
		        // Check for publish permissions    
		        List<String> permissions = session.getPermissions();
		        if (!isSubsetOf(PERMISSIONS, permissions)) {
		            pendingPublishReauthorization = true;
		            Session.NewPermissionsRequest newPermissionsRequest = new Session
		                    .NewPermissionsRequest(this, PERMISSIONS);
		        session.requestNewPublishPermissions(newPermissionsRequest);
		        return;
		        }
		        
		        Bundle postParams = new Bundle();
		        postParams.putString("name", "Electric Furry");
		        postParams.putString("caption", "World's Largest Foam Party!");
		        postParams.putString("message", userPost);
		        postParams.putString("link", "http://electricflurry.com");
	
		        Request.Callback callback= new Request.Callback() {
		            public void onCompleted(Response response) {
		            	boolean responseReturnedError = false;
		            	JSONObject graphResponse = null;
		            	try {
			                graphResponse = response
			                                        .getGraphObject()
			                                        .getInnerJSONObject();
		            	}
		            	catch(NullPointerException e){
		            		responseReturnedError = true;
		            		Toast.makeText(getActivity(), 
		            		"There was an error. Please try again.", Toast.LENGTH_SHORT).show();
		            	}
		            	
		                if(!responseReturnedError){
			                String postId = null;
			                try {
			                    postId = graphResponse.getString("id");
			                } catch (JSONException e) {
			                    Log.i(TAG,
			                        "JSON error "+ e.getMessage());
			                }
			                FacebookRequestError error = response.getError();
			                if (error != null) {
			                    Toast.makeText(getActivity()
			                         .getApplicationContext(),
			                         error.getErrorMessage(),
			                         Toast.LENGTH_SHORT).show();
			                    } else {
			                        Toast.makeText(getActivity()
			                             .getApplicationContext(), 
			                             postId,
			                             Toast.LENGTH_LONG).show();
			                        userInput.setText("");
			                }
		            	}
		                
		            }
		        };
	
		        Request request = new Request(session, "me/feed", postParams, 
		                              HttpMethod.POST, callback);
	
		        RequestAsyncTask task = new RequestAsyncTask(request);
		        task.execute();
		       
		    }
		}//check to see if user inputted any text
		else{
			Toast.makeText(getActivity(), "No text to share yet.", Toast.LENGTH_SHORT).show();
		}

	}//end of publishStory
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	
	
	
	
	

	
	
	
	
	
	

}//end of class
