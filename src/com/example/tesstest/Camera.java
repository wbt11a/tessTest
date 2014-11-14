package com.example.tesstest;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

public class Camera {

	public static Bitmap showPhoto(Context context, String photoUri) {

		Bitmap scaled = null;
		File imageFile = new File(photoUri);

		if (imageFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imageFile
					.getAbsolutePath());
			Bitmap d = new BitmapDrawable(context.getResources(), bitmap)
					.getBitmap();
			int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
			scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
		}
		return scaled;
	}

	public static Bitmap showPhoto(Context context, Bitmap photobmp) {

		Bitmap scaled = null;

		if (photobmp != null) {

			Bitmap d = new BitmapDrawable(context.getResources(), photobmp)
					.getBitmap();
			int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
			scaled = Bitmap.createScaledBitmap(d, 512, nh, true);


		}
		return scaled;
	}

	public static void galleryAddPic(Context context, String fileURI) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(fileURI);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	@SuppressWarnings("deprecation")
	public static  void setPic(View view, String fileURI) {
		// Get the dimensions of the View
		ImageView myImage2 = (ImageView) view.findViewById(R.id.imageView1);
		int targetW = myImage2.getWidth();
		int targetH = myImage2.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileURI, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(fileURI, bmOptions);
		myImage2.setImageBitmap(bitmap);
	}

}
