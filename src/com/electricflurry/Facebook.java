package com.electricflurry;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.model.*;

public class Facebook extends Activity {

private String  facebookUserName;
private Bitmap facebookProfilePicture;
private URL facebookURL;
TextView info;
	
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_facebook);

    // start Facebook Login
    Session.openActiveSession(this, true, new Session.StatusCallback() {

      // callback when session changes state
      @Override
      public void call(Session session, SessionState state, Exception exception) {
        if (session.isOpened()) {

          // make request to the /me API
          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

            // callback after Graph API response with user object
            @Override
            public void onCompleted(GraphUser user, Response response) {
              if (user != null) {
                TextView welcome = (TextView) findViewById(R.id.welcome);
                welcome.setText("Hello there " + user.getName() + "!"); 
                
                
                ImageView profilepic;
                profilepic=(ImageView)findViewById(R.id.profilepic);
                URL img_value = null;
                Bitmap pic=null;
                
                try {
					img_value = new URL("http://graph.facebook.com/"+user.getId()+"/picture?type=large");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
				try {
					pic = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                profilepic.setImageBitmap(pic);
                
            //     facebookProfilePicture = pic;
                try {
					facebookURL = new URL(user.getLink());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
                
              //  facebookUserName = user.getUsername();
                
                
               // info.setText("User Name is - " + facebookUserName + "      Url is - " + facebookURL );
              }
            }
          });
        }
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
  }


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facebook, menu);
		return true;
	}

}
