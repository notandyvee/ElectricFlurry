package com.electricflurry;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PhotoTaker extends Activity {

	int TAKE_PHOTO_CODE = 3;
	public static int count;
	File newFile;
	String targetDir;
	LinearLayout myGallery;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_taker);

		// Creates a folder called ElectricFlurry
		targetDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/ElectricFlurryPictures/";
		File dir = new File(targetDir);
		if (dir.isDirectory()) {
			Toast.makeText(this, "directory exists", Toast.LENGTH_SHORT).show();
		} else {
			
			dir.mkdirs();
			count = 0;
		}
		
		
        myGallery = (LinearLayout)findViewById(R.id.mygallery);
        
                 
        File[] files = dir.listFiles();
        for (File file : files){
         myGallery.addView(insertPhoto(file.getAbsolutePath()));
        } 
        
    		Button capture = (Button) findViewById(R.id.btnCapture);
    		capture.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {

    				// creates a jpg called ElectricFlurry followed by the number
    				// that the photo is in the folder
    				count++;
    				String file = targetDir + "ElectricFlurry" + count + ".jpg";
    				newFile = new File(file);
    				try {
    					newFile.createNewFile();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}

    				Uri outputFileUri = Uri.fromFile(newFile);

    				Intent cameraIntent = new Intent(
    						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
    				// sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
    				// Uri.parse("file://"+ file)));

    				startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    				// sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
    				// Uri.parse("file://"+ file)));
    			}
    		});

    		Button send = (Button) findViewById(R.id.btnSend);
    		send.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {

    				Toast.makeText(PhotoTaker.this, "Currently under construction",
    						Toast.LENGTH_SHORT).show();

    			}
    		});
    }

    View insertPhoto(String path){
     Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
     
     LinearLayout layout = new LinearLayout(getApplicationContext());
     layout.setLayoutParams(new LayoutParams(250, 250));
     layout.setGravity(Gravity.CENTER);
     
     ImageView imageView = new ImageView(getApplicationContext());
     imageView.setLayoutParams(new LayoutParams(220, 220));
     imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
     imageView.setImageBitmap(bm);
     
     layout.addView(imageView);
     return layout;
    }
    
    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
     Bitmap bm = null;
     
     // First decode with inJustDecodeBounds=true to check dimensions
     final BitmapFactory.Options options = new BitmapFactory.Options();
     options.inJustDecodeBounds = true;
     BitmapFactory.decodeFile(path, options);
     
     // Calculate inSampleSize
     options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
     
     // Decode bitmap with inSampleSize set
     options.inJustDecodeBounds = false;
     bm = BitmapFactory.decodeFile(path, options); 
     
     return bm;  
    }
    
    public int calculateInSampleSize(
      
     BitmapFactory.Options options, int reqWidth, int reqHeight) {
     // Raw height and width of image
     final int height = options.outHeight;
     final int width = options.outWidth;
     int inSampleSize = 1;
        
     if (height > reqHeight || width > reqWidth) {
      if (width > height) {
       inSampleSize = Math.round((float)height / (float)reqHeight);   
      } else {
       inSampleSize = Math.round((float)width / (float)reqWidth);   
      }   
     }
     
     return inSampleSize;   

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 3) {
			// Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			Log.d("CameraDemo", "Pic saved");

			super.onActivityResult(requestCode, resultCode, data);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://"
							+ Environment.getExternalStorageDirectory())));
		}
	}

}
