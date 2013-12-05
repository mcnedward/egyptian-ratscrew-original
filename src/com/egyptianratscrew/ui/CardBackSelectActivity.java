package com.egyptianratscrew.ui;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.egyptianratscrew.R;

public class CardBackSelectActivity extends Activity {

	//setting the variables
	private static final int CAMERA_REQUEST = 2;

	private static final int IMAGE_GALLERY_REQUEST = 1;
	
	private Bitmap image;
	
	private ImageView thumbnail;

	//creating the content view of take_picture.xml
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture);
		//setting the thumbnail to be able to find
		thumbnail = (ImageView) findViewById(R.id.imgThumbnail);
	}
	/**
	 * open an existing image to use for the card
	 * @param v
	 */
	public void OpenExistingImageClick(View v) {
		// we want to pick something.
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		// find the common area where images are stored.
		Uri imageLocation = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
		// what type of data do we want to find?
		String type = "image/*";
		// assemble this all together
		photoPickerIntent.setDataAndType(imageLocation, type);
		
		// start this activity.
		startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
		
		
	}
	
	public void DefaultClicked(View v)
	{
		
	}
	
	/**
	 * the takenewpicture method and start the activity of the camera_request
	 * @param v
	 */
	public void TakeNewPicture(View v)
	{
		
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//start the activity
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}
	
	/**
	 * save a picture
	 * @param v
	 */
	public void SavePicture(View v)
	{
		Intent intentMessage=new Intent();
		 
        // put the message in Intent
        intentMessage.putExtra("IMAGE",image);
        // Set The Result in Intent
        setResult(Activity.RESULT_OK,intentMessage);
        
        // finish The activity 
        finish();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			// RESULT_OK means that everything processed successfully.
			
			if (requestCode == IMAGE_GALLERY_REQUEST) {
				// this request code means that the user chose to open the gallery view.
				
				// find the full file path of the inmage file that the user chose.
				Uri imageUri = data.getData();
				try {
					// open the full file path as a stream of data.
					InputStream inputStream = getContentResolver().openInputStream(imageUri);
					
					// take a stream of data from a file, and turn it into a bitmap object.
					image = BitmapFactory.decodeStream(inputStream);
					
					// show the image.
					thumbnail.setImageBitmap(image);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (requestCode == CAMERA_REQUEST) {
				// hande a response from the camera.
				
				// get the image data from the camera.
				image = (Bitmap) data.getExtras().get("data");
				
				// show the image.
				thumbnail.setImageBitmap(image);
			}
		}
	
	}
}
