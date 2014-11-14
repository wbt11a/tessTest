package com.example.tesstest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.leptonica.android.WriteFile;
import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends ActionBarActivity {

	public File imageFile = null;
	public TessBaseAPI baseApi = new TessBaseAPI();
	public Bitmap imageViewPointer = null;
	public Uri fileUri = null;
	public List<String> commandQueue = null;

	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File newImageFile) {
		this.imageFile = newImageFile;
	}

	public Bitmap getImagePointer() {
		return imageViewPointer;
	}

	public void setImagePointer(Bitmap newImg) {
		imageViewPointer = newImg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button clickButton1 = (Button) findViewById(R.id.button1);
		clickButton1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bitmap myBitmap = null;
				if (getImagePointer() == null)
					myBitmap = BitmapFactory.decodeFile(getImageFile()
							.getAbsolutePath());
				else
					myBitmap = getImagePointer();

				Pix binary = Leptonica.binarize(ReadFile.readBitmap(myBitmap));
				myBitmap.recycle();
				System.gc();

				Pix deskewed = Leptonica.deskew(binary);
				binary.recycle();
				System.gc();

				Bitmap bit3 = WriteFile.writeBitmap(deskewed); // convert from
																// Leptonica PIX
																// to Bitmap
				deskewed.recycle();
				System.gc();

				ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

				myImage2.setImageBitmap(Camera.showPhoto(
						getApplicationContext(), bit3));
				// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
				// bit3));
				setImagePointer(bit3);
				commandQueue.add("b1");  //add button1 to item[0] of commandQueue for future execution.

			}
		});

		Button clickButton2 = (Button) findViewById(R.id.button2);
		clickButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					Bitmap myBitmap = null;
					if (getImagePointer() == null)
						myBitmap = BitmapFactory.decodeFile(getImageFile()
								.getAbsolutePath());
					else
						myBitmap = getImagePointer();

					Pix enhanced = Leptonica.enhance(ReadFile
							.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();

					Bitmap bit3 = WriteFile.writeBitmap(enhanced); // convert
																	// from
																	// Leptonica
																	// PIX to
																	// Bitmap
					enhanced.recycle();
					System.gc();

					ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

					myImage2.setImageBitmap(bit3);
					setImagePointer(bit3);
					commandQueue.add("b2");

				} catch (Exception ex) {
					MaintenanceClass.popDebug(getApplicationContext(),
							ex.toString());
				}

			}
		});

		Button clickButton3 = (Button) findViewById(R.id.button3);
		clickButton3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					Bitmap myBitmap = null;
					if (getImagePointer() == null)
						myBitmap = BitmapFactory.decodeFile(getImageFile()
								.getAbsolutePath());
					else
						myBitmap = getImagePointer();

					int width = myBitmap.getWidth();
					int height = myBitmap.getHeight();
					int inPixels[] = DespeckleFilter.bitmapToIntArray(myBitmap);
					Rect transformedSpace = new Rect();
					transformedSpace.top = 0;
					transformedSpace.bottom = myBitmap.getHeight();
					transformedSpace.left = 0;
					transformedSpace.right = myBitmap.getWidth();

					DespeckleFilter filter = new DespeckleFilter();
					int newPixels[] = filter.filterPixels(width, height,
							inPixels, transformedSpace);
					/*
					 * if(Arrays.equals(newPixels,inPixels))
					 * MaintenanceClass.popDebug(getApplicationContext(),
					 * "Arrays are the exact same"); else
					 * MaintenanceClass.popDebug(getApplicationContext(),
					 * "Arrays are different");
					 */
					Bitmap applied = Bitmap.createBitmap(newPixels, width,
							height, Config.ARGB_8888);
					// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
					// applied));
					setImagePointer(applied);
					ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

					myImage2.setImageBitmap(applied);
					commandQueue.add("b3");

				} catch (Exception ex) {
					MaintenanceClass.popDebug(getApplicationContext(),
							ex.toString());
				}
			}

		});

		Button clickButton4 = (Button) findViewById(R.id.button4);
		clickButton4.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				try {
					Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = Camera.getOutputPhotoFile(getApplicationContext());
					fileUri = Uri.fromFile(Camera.getOutputPhotoFile(getApplicationContext()));
					i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ);

				} catch (Exception ex) {
					MaintenanceClass.popDebug(getApplicationContext(),
							ex.toString());
				}

			}
		});

		Button clickButton5 = (Button) findViewById(R.id.button5);
		clickButton5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					Bitmap myBitmap = null;
					if (getImagePointer() == null)
						myBitmap = BitmapFactory.decodeFile(getImageFile()
								.getAbsolutePath());
					else
						myBitmap = getImagePointer();

					Pix adaptive = Leptonica.adaptiveMap(ReadFile
							.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();

					Bitmap bit3 = WriteFile.writeBitmap(adaptive); // convert
																	// from
																	// Leptonica
																	// PIX to
																	// Bitmap
					adaptive.recycle();
					System.gc();

					ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

					myImage2.setImageBitmap(bit3);
					setImagePointer(bit3);
					commandQueue.add("b5");

				} catch (Exception ex) {
					MaintenanceClass.popDebug(getApplicationContext(),
							ex.toString());
				}

			}
		});

		Button clickButton6 = (Button) findViewById(R.id.button6);
		clickButton6.setOnClickListener(new View.OnClickListener() {

			String m_chosen;

			@Override
			public void onClick(View v) {
				Button btn4 = (Button) findViewById(R.id.button4);
				setImageFile(null);
				setImagePointer(null);

				ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

				myImage2.setImageResource(android.R.color.transparent);

				// ///////////////////////////////////////////////////////////////////////////////////////////////
				// Create FileOpenDialog and register a callback
				// ///////////////////////////////////////////////////////////////////////////////////////////////
				SimpleFileDialog FolderChooseDialog = new SimpleFileDialog(
						MainActivity.this, "FileOpen",
						new SimpleFileDialog.SimpleFileDialogListener() {
							@Override
							public void onChosenDir(String chosenDir) {
								// The code in this function will be executed
								// when the dialog OK button is pushed
								m_chosen = chosenDir;
								setImageFile(new File(chosenDir));
								// Toast.makeText(MainActivity.this,
								// "Chosen FileOpenDialog File: " +
								// m_chosen, Toast.LENGTH_LONG).show();
								ImageView myImage = (ImageView) findViewById(R.id.imageView1);
								myImage.setImageURI(Uri
										.fromFile(getImageFile()));
								
							}
						});

				FolderChooseDialog.chooseFile_or_Dir();

				// ///////////////////////////////////////////////////////////////////////////////////////////////

			}
		});

		try {
			commandQueue = new ArrayList<String>();
			MaintenanceClass.extractAssets(getApplicationContext());

			setImageFile(new File(getCacheDir().getAbsolutePath()
					+ "/tessdata/receipt.jpg"));
			// ImageView myImage = (ImageView) findViewById(R.id.imageView1);
			// myImage.setImageURI(Uri.fromFile(getImageFile()));

		} catch (Exception ex) {
			MaintenanceClass.popDebug(getApplicationContext(),
					"Error: " + ex.getMessage());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.item1) {
			try {

				Bitmap myBitmap = null;
				if (getImagePointer() == null)
					myBitmap = BitmapFactory.decodeFile(getImageFile()
							.getAbsolutePath());
				else
					myBitmap = getImagePointer();

				Pix binary = Leptonica.binarize(ReadFile.readBitmap(myBitmap));
				myBitmap.recycle();
				System.gc();

				Bitmap bit3 = WriteFile.writeBitmap(binary); // convert from
																// Leptonica PIX
																// to Bitmap
				binary.recycle();
				System.gc();

				ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

				myImage2.setImageBitmap(bit3);
				setImagePointer(bit3);
				commandQueue.add("i1");

			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}

			return true;
		}

		if (id == R.id.item2) {
			try {
				baseApi.init(getCacheDir().getAbsolutePath(), "eng");

				if (getImagePointer() == null)
					baseApi.setImage(getImageFile());
				else
					baseApi.setImage(getImagePointer());

				String recognizedText = baseApi.getUTF8Text();
				TextView myTextView = (TextView) findViewById(R.id.textView1);
				myTextView.setText(recognizedText);
				

				baseApi.end();
			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}
			return true;
		}

		if (id == R.id.item3) {
			try {
				Bitmap myBitmap = null;
				if (getImagePointer() == null)
					myBitmap = BitmapFactory.decodeFile(getImageFile()
							.getAbsolutePath());
				else
					myBitmap = getImagePointer();

				Bitmap bit3 = Gamma.doGamma(myBitmap, 1.8, 1.8, 1.8);

				ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

				myImage2.setImageBitmap(bit3);
				setImagePointer(bit3);
				commandQueue.add("i3");

			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}
			return true;
		}

		if (id == R.id.item4) {
			try {
				setImageFile(null);
				setImagePointer(null);

				ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

				myImage2.setImageResource(android.R.color.transparent);
				TextView myTextView = (TextView) findViewById(R.id.textView1);
				myTextView.setText("");
				commandQueue.clear();

			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}
			return true;
		}

		if (id == R.id.item5) {
			try {

				Bitmap myBitmap = null;
				if (getImagePointer() == null)
					myBitmap = BitmapFactory.decodeFile(getImageFile()
							.getAbsolutePath());
				else
					myBitmap = getImagePointer();

				Pix rr = Leptonica.rotateRight(ReadFile.readBitmap(myBitmap));
				myBitmap.recycle();
				System.gc();

				Bitmap bit3 = WriteFile.writeBitmap(rr); // convert from
															// Leptonica PIX to
															// Bitmap
				rr.recycle();
				System.gc();

				ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);
				myImage2.setImageBitmap(bit3);
				myImage2.setScaleType(ImageView.ScaleType.FIT_XY);
				setImagePointer(bit3);
				commandQueue.add("i5");

			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}
			return true;
		}

		if (id == R.id.item6) {
			try {

				Bitmap myBitmap = null;
				if (getImagePointer() == null)
					myBitmap = BitmapFactory.decodeFile(getImageFile()
							.getAbsolutePath());
				else
					myBitmap = getImagePointer();

				Pix rl = Leptonica.rotateLeft(ReadFile.readBitmap(myBitmap));
				myBitmap.recycle();
				System.gc();

				Bitmap bit3 = WriteFile.writeBitmap(rl); // convert from
															// Leptonica PIX to
															// Bitmap
				rl.recycle();
				System.gc();

				ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);

				myImage2.setImageBitmap(bit3);
				setImagePointer(bit3);
				commandQueue.add("i6");

			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}
			return true;
		}

		if (id == R.id.item7) {
			Bitmap myBitmap = BitmapFactory.decodeFile(getImageFile()
					.getAbsolutePath());
			
			
			for(int i = 0; i< commandQueue.size(); i++){
				
				switch (commandQueue.get(i)) {
				case "b1":
					Pix binary = Leptonica.binarize(ReadFile.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();
					Pix deskewed = Leptonica.deskew(binary);
					binary.recycle();
					System.gc();
					myBitmap= WriteFile.writeBitmap(deskewed);
					deskewed.recycle();
					System.gc();
					
					break;
				case "b2":
					Pix enhanced = Leptonica.enhance(ReadFile
							.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();
					myBitmap = WriteFile.writeBitmap(enhanced); 
					enhanced.recycle();
					System.gc();
					break;
				case "b3":

					int width = myBitmap.getWidth();
					int height = myBitmap.getHeight();
					int inPixels[] = DespeckleFilter.bitmapToIntArray(myBitmap);
					Rect transformedSpace = new Rect();
					transformedSpace.top = 0;
					transformedSpace.bottom = myBitmap.getHeight();
					transformedSpace.left = 0;
					transformedSpace.right = myBitmap.getWidth();

					DespeckleFilter filter = new DespeckleFilter();
					int newPixels[] = filter.filterPixels(width, height,
							inPixels, transformedSpace);
					myBitmap.recycle();
					System.gc();
					myBitmap = Bitmap.createBitmap(newPixels, width,
							height, Config.ARGB_8888);	
					break;
				case "b5":
					Pix adaptive = Leptonica.adaptiveMap(ReadFile
							.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();

					myBitmap = WriteFile.writeBitmap(adaptive);
					adaptive.recycle();
					System.gc();
					
					break;
				case "i1":
					Pix binary2 = Leptonica.binarize(ReadFile.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();
					myBitmap = WriteFile.writeBitmap(binary2); 
					binary2.recycle();
					System.gc();
					break;
				case "i3":
					myBitmap = Gamma.doGamma(myBitmap, 1.8, 1.8, 1.8);
					break;
				case "i5":
					Pix rr = Leptonica.rotateRight(ReadFile.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();
					myBitmap = WriteFile.writeBitmap(rr); 
					rr.recycle();
					System.gc();
					break;
				case "i6":
					Pix rl = Leptonica.rotateLeft(ReadFile.readBitmap(myBitmap));
					myBitmap.recycle();
					System.gc();
					myBitmap = WriteFile.writeBitmap(rl); 
					rl.recycle();
					System.gc();			
					break;
				default:
					
					break;
				
				
				}
			}
			final Bitmap temp = myBitmap;
			new Thread(new Runnable() {
		        public void run() {
		        	MaintenanceClass.saveTemp(getApplicationContext(), temp);
		        	
		        }
		    }).start();
			
			return true;
			//write file
		}

		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		    if (resultCode == RESULT_OK) {
		      Uri photoUri = null;
		      if (data == null) {
		        // A known bug here! The image should have saved in fileUri
		        MaintenanceClass.popDebug(getApplicationContext(), "Image saved successfully");
		   
		        photoUri = fileUri;
		        
		      } else {
		        photoUri = data.getData();
		        MaintenanceClass.popDebug(getApplicationContext(), "Image saved successfully in: " + data.getData());
		      }
		      Bitmap newBitmap = Camera.showPhoto(getApplicationContext(), photoUri);  
		      
		      ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);
		      myImage2.setImageBitmap(newBitmap);
		      setImagePointer(newBitmap);  //scaled image
		      setImageFile(new File(photoUri.getPath()));  //unscaled image
		      
		    } else if (resultCode == RESULT_CANCELED) {
		    	MaintenanceClass.popDebug(getApplicationContext(), "Cancelled");
		    } else {
		    	MaintenanceClass.popDebug(getApplicationContext(), "Callout for image failed");
		    }
		  }
		}

}