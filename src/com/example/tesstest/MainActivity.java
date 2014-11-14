package com.example.tesstest;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
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
					// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
					// bit3));
					setImagePointer(bit3);

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
					// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
					// bit3));
					setImagePointer(bit3);

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
				// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
				// bit3));
				setImagePointer(bit3);

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
				// popDebug(getApplicationContext(),recognizedText);

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
				// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
				// bit3));
				setImagePointer(bit3);

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
				// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
				// bit3));
				setImagePointer(bit3);

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
				// setImageFile(MaintenanceClass.saveTemp(getApplicationContext(),
				// bit3));
				setImagePointer(bit3);

			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}
			return true;
		}

		if (id == R.id.item7) {
			try {

			} catch (Exception ex) {
				MaintenanceClass.popDebug(getApplicationContext(),
						ex.toString());
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}