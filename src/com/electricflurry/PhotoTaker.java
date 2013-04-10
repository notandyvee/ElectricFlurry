package com.electricflurry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PhotoTaker extends Activity {
	
	int TAKE_PHOTO_CODE = 3;
	public static int count=0;
	File newFile;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.photo_taker);

	// Creates a folder called ElectricFlurry
	        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ElectricFlurryPictures/"; 
	        File newdir = new File(dir); 
	        newdir.mkdirs();
	      

	    Button capture = (Button) findViewById(R.id.btnCapture);
	    capture.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {

	            // creates a jpg called ElectricFlurry followed by the number that the photo is in the folder
	            count++;
	            String file = dir+"ElectricFlurry"+count+".jpg";
	            newFile = new File(file);
	            try {
	                newFile.createNewFile();
	            } catch (IOException e) {
	            	
	            }       

	            Uri outputFileUri = Uri.fromFile(newFile);

	            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
	           sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ file)));
	            
	            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
	            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ file)));
	        }
	    });
	    Button crop = (Button) findViewById(R.id.btnCrop);
	    crop.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {

	            Toast.makeText(PhotoTaker.this, "Currently under construction", Toast.LENGTH_SHORT).show();
	          
	        }
	    });
	   
	    Button send = (Button) findViewById(R.id.btnSend);
	    send.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {

	            Toast.makeText(PhotoTaker.this, "Currently under construction", Toast.LENGTH_SHORT).show();
	          
	        }
	    });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
 
	        Log.d("CameraDemo", "Pic saved");


	    }
	}

	}
