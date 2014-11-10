package com.example.tesstest;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.leptonica.android.*;

import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.googlecode.eyesfree.textdetect.HydrogenTextDetector;
import com.googlecode.eyesfree.textdetect.Thresholder;


public class MainActivity extends ActionBarActivity {
	
	public static final int ENGINE = 0; // default - tesseract only
	public File imageFile = null;
	public TessBaseAPI baseApi = new TessBaseAPI();
	public Bitmap imageViewPointer = null;
	 private static final String TAG = "CallCamera";
	 private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
	 private Uri fileUri = null;
	   private ImageView photoImage = null;
	
	public File getImageFile(){
		return imageFile;
	}
	
	public void setImageFile(File newImageFile){
		this.imageFile = newImageFile;
	}
	
	public Bitmap getImagePointer(){
		return imageViewPointer;
	}
	
	public void setImagePointer(Bitmap newImg){
		imageViewPointer = newImg;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
    	
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button clickButton1 = (Button)findViewById(R.id.button1);
        clickButton1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Bitmap myBitmap = null;
        	if(getImagePointer()==null)
	        	myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
        	else        		
        		myBitmap = getImagePointer();
        	
	        	Pix binary = binarize(ReadFile.readBitmap(myBitmap));
	     	    myBitmap.recycle();
	     	    System.gc();
			    
			    Pix deskewed = deskew(binary);
	    	    binary.recycle();
		        System.gc();
		        
	        	Bitmap bit3 = WriteFile.writeBitmap(deskewed);  //convert from Leptonica PIX to Bitmap
	        	deskewed.recycle();
	        	System.gc();
	        	
	        	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
	        	
	        	myImage2.setImageBitmap(bit3);
	        	//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), bit3));
	            setImagePointer(bit3);
            	
            }
        });
        
        Button clickButton2 = (Button)findViewById(R.id.button2);
        clickButton2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        	try {
        		

        		Bitmap myBitmap = null;
            	if(getImagePointer()==null)
    	        	myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
            	else        		
            		myBitmap = getImagePointer();
    		    
    		    Pix enhanced = enhance(ReadFile.readBitmap(myBitmap));
        	    myBitmap.recycle();
        	    System.gc();
    	          
            	Bitmap bit3 = WriteFile.writeBitmap(enhanced);  //convert from Leptonica PIX to Bitmap
            	enhanced.recycle();
            	System.gc();
            	
            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageBitmap(bit3);
            	//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), bit3));
            	setImagePointer(bit3);
        	
        	
        }
        catch(Exception ex){
        	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	  
        	
        	
            }
        });
        
        
        
        Button clickButton3 = (Button)findViewById(R.id.button3);
        clickButton3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        	try {
        		

        			Bitmap myBitmap = null;
        			if(getImagePointer()==null)
        				myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
        			else        		
        				myBitmap = getImagePointer();
        		    
        			int width = myBitmap.getWidth();
        			int height = myBitmap.getHeight();
        			int inPixels[] = bitmapToIntArray(myBitmap);
        			Rect transformedSpace = new Rect();
        			transformedSpace.top = 0;
        			transformedSpace.bottom = myBitmap.getHeight();
        			transformedSpace.left = 0;
        			transformedSpace.right = myBitmap.getWidth();
        			
        			
        			DespeckleFilter filter = new DespeckleFilter();
        			int newPixels[] = filter.filterPixels(width, height, inPixels, transformedSpace);
        		    /*if(Arrays.equals(newPixels,inPixels))
        		    	MaintenanceClass.popDebug(getApplicationContext(), "Arrays are the exact same");
        		    else
        		    	MaintenanceClass.popDebug(getApplicationContext(), "Arrays are different");
        			*/
        			Bitmap applied = Bitmap.createBitmap(newPixels, width, height, Config.ARGB_8888);
        			//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), applied));
        			setImagePointer(applied);
        			ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
                	
                	myImage2.setImageBitmap(applied);
        	
        			
            	
            }
            catch(Exception ex){
            	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
            }
         }
            
        });
     
       
        Button clickButton4 = (Button)findViewById(R.id.button4);
        clickButton4.setOnClickListener(new View.OnClickListener() {
        	
      	    
        public void onClick(View v) {
        	 photoImage = (ImageView) findViewById(R.id.imageView1);
        	
        	try {
        		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	      //  File file = getOutputPhotoFile();
    	        fileUri = Uri.fromFile(getOutputPhotoFile());
    	        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    	        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
        		
        		
        	
        	}
        catch(Exception ex){
        	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	  
        	
        	
            }
        });
        
        Button clickButton5 = (Button)findViewById(R.id.button5);
        clickButton5.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        	try {
        		
        		Bitmap myBitmap = null;
            	if(getImagePointer()==null)
    	        	myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
            	else        		
            		myBitmap = getImagePointer();
    		    
    		    Pix adaptive = adaptiveMap(ReadFile.readBitmap(myBitmap));
        	    myBitmap.recycle();
        	    System.gc();
    	          
            	Bitmap bit3 = WriteFile.writeBitmap(adaptive);  //convert from Leptonica PIX to Bitmap
            	adaptive.recycle();
            	System.gc();
            	
            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageBitmap(bit3);
            	//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), bit3));
            	setImagePointer(bit3);
        	
        	
        }
        catch(Exception ex){
        	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	  
        	
        	
            }
        });
        
    	Button clickButton6  = (Button) findViewById(R.id.button6);
    	clickButton6.setOnClickListener(new View.OnClickListener() { 
    		
    		String m_chosen;
    		@Override
    		public void onClick(View v) {
    			Button btn4 = (Button) findViewById(R.id.button4);
    			setImageFile(null);
        		setImagePointer(null);

            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageResource(android.R.color.transparent);
        		
    			
    			/////////////////////////////////////////////////////////////////////////////////////////////////
    			//Create FileOpenDialog and register a callback
    			/////////////////////////////////////////////////////////////////////////////////////////////////
    			SimpleFileDialog FolderChooseDialog =  new SimpleFileDialog(MainActivity.this, "FileOpen",
    					new SimpleFileDialog.SimpleFileDialogListener()
    			{
    				@Override
    				public void onChosenDir(String chosenDir) 
    				{
    					// The code in this function will be executed when the dialog OK button is pushed
    					m_chosen = chosenDir;
    					setImageFile(new File(chosenDir));
    					//Toast.makeText(MainActivity.this, "Chosen FileOpenDialog File: " + 
    					//		m_chosen, Toast.LENGTH_LONG).show();
    					ImageView myImage = (ImageView) findViewById(R.id.imageView1);
    					myImage.setImageURI(Uri.fromFile(getImageFile()));
    				}
    			});

    			FolderChooseDialog.chooseFile_or_Dir();

    			/////////////////////////////////////////////////////////////////////////////////////////////////

    		

    		
    	}
    	});
    	
        
      
        
        try{
        	MaintenanceClass.extractAssets(getApplicationContext());
        	
        	setImageFile(new File(getCacheDir().getAbsolutePath()+"/tessdata/receipt.jpg"));
        //	ImageView myImage = (ImageView) findViewById(R.id.imageView1);
        //	myImage.setImageURI(Uri.fromFile(getImageFile()));
       
        }
        catch (Exception ex){
        	MaintenanceClass.popDebug(getApplicationContext(), "Error: " + ex.getMessage());
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
    			if(getImagePointer()==null)
    				myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
    			else        		
    				myBitmap = getImagePointer();
    		    
    		    Pix binary = binarize(ReadFile.readBitmap(myBitmap));
        	    myBitmap.recycle();
        	    System.gc();
    	          
            	Bitmap bit3 = WriteFile.writeBitmap(binary);  //convert from Leptonica PIX to Bitmap
            	binary.recycle();
            	System.gc();
            	
            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageBitmap(bit3);
            	//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), bit3));
            	setImagePointer(bit3);
        	
        	
        }
        catch(Exception ex){
        	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	

        	
            return true;
        }
        
        if( id == R.id.item2){
        	try{
        		 baseApi.init(getCacheDir().getAbsolutePath(), "eng", ENGINE);
            	 
        		 if(getImagePointer()==null)
        			 baseApi.setImage(getImageFile());
        		 else baseApi.setImage(getImagePointer());
        		 
                 String recognizedText = baseApi.getUTF8Text();
                 TextView myTextView = (TextView) findViewById(R.id.textView1);
             	myTextView.setText(recognizedText);
                 //popDebug(getApplicationContext(),recognizedText);
                  
                baseApi.end();
        	}
        	catch(Exception ex){
            	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	return true;
        }
        
        if( id == R.id.item3){
        	try{
        		Bitmap myBitmap = null;
    			if(getImagePointer()==null)
    				myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
    			else        		
    				myBitmap = getImagePointer();
    			
    			Bitmap bit3 = doGamma(myBitmap,1.8,1.8,1.8);
    			
    			
    			
    			ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageBitmap(bit3);
            	//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), bit3));
            	setImagePointer(bit3);
        		
        		
        	}
        	catch(Exception ex){
            	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	return true;
        }
        
        if( id == R.id.item4){
        	try{
        		setImageFile(null);
        		setImagePointer(null);

            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageResource(android.R.color.transparent);
            	TextView myTextView = (TextView) findViewById(R.id.textView1);
              	myTextView.setText("");
        		
        	}
        	catch(Exception ex){
            	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	return true;
        }
        
        if( id == R.id.item5){
        	try{

    			Bitmap myBitmap = null;
    			if(getImagePointer()==null)
    				myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
    			else        		
    				myBitmap = getImagePointer();
    		    
    		    Pix rr = rotateRight(ReadFile.readBitmap(myBitmap));
        	    myBitmap.recycle();
        	    System.gc();
    	          
            	Bitmap bit3 = WriteFile.writeBitmap(rr);  //convert from Leptonica PIX to Bitmap
            	rr.recycle();
            	System.gc();
            	
            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageBitmap(bit3);
            	//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), bit3));
            	setImagePointer(bit3);
        		
        	}
        	catch(Exception ex){
            	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	return true;
        }
        
        if( id == R.id.item6){
        	try{

    			Bitmap myBitmap = null;
    			if(getImagePointer()==null)
    				myBitmap = BitmapFactory.decodeFile(getImageFile().getAbsolutePath());
    			else        		
    				myBitmap = getImagePointer();
    		    
    		    Pix rl = rotateLeft(ReadFile.readBitmap(myBitmap));
        	    myBitmap.recycle();
        	    System.gc();
    	          
            	Bitmap bit3 = WriteFile.writeBitmap(rl);  //convert from Leptonica PIX to Bitmap
            	rl.recycle();
            	System.gc();
            	
            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	
            	myImage2.setImageBitmap(bit3);
            	//setImageFile(MaintenanceClass.saveTemp(getApplicationContext(), bit3));
            	setImagePointer(bit3);
        		
        	}
        	catch(Exception ex){
            	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
        }
        	return true;
        }
        		
        
        return super.onOptionsItemSelected(item);
    }
    
    
    private Pix enhance(Pix pix){
		Pix temp = Enhance.unsharpMasking(pix, 2, (float) 0.5);
		pix.recycle();
		return temp;
	}
    
    private Pix binarize(Pix pix){
		Pix temp = Binarize.otsuAdaptiveThreshold(pix);
		pix.recycle();
		return temp;
	}
    
    private Pix adaptiveMap(Pix pix){
		Pix temp = AdaptiveMap.backgroundNormMorph(pix);
		pix.recycle();
		return temp;
	}
    
    private Pix deskew(Pix pix){
       float skew = Skew.findSkew(pix);
 	   Pix rotated_img = Rotate.rotate(pix, skew);
 	   pix.recycle();
 	   return rotated_img;
    }
    
    private Pix rotateRight(Pix pix){
    	
    	Pix temp = Rotate.rotate(pix, 90);
    	pix.recycle();
    	return temp;
    }
    
    private Pix rotateLeft(Pix pix){
    	Pix temp = Rotate.rotate(pix, -90);
    	pix.recycle();
    	return temp;
    }

    
    public static int[] bitmapToIntArray(Bitmap bitmap){
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
    
        int[] colors = new int[bitmapWidth *  bitmapHeight];
        bitmap.getPixels(colors, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
    
        return colors;
    }
    
    public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {
        // create output image
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // get image size
        int width = src.getWidth();
        int height = src.getHeight();
        // color information
        int A, R, G, B;
        int pixel;
        // constant value curve
        final int    MAX_SIZE = 256;
        final double MAX_VALUE_DBL = 255.0;
        final int    MAX_VALUE_INT = 255;
        final double REVERSE = 1.0;
     
        // gamma arrays
        int[] gammaR = new int[MAX_SIZE];
        int[] gammaG = new int[MAX_SIZE];
        int[] gammaB = new int[MAX_SIZE];
     
        // setting values for every gamma channels
        for(int i = 0; i < MAX_SIZE; ++i) {
            gammaR[i] = (int)Math.min(MAX_VALUE_INT,
                    (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
            gammaG[i] = (int)Math.min(MAX_VALUE_INT,
                    (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
            gammaB[i] = (int)Math.min(MAX_VALUE_INT,
                    (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
        }
     
        // apply gamma table
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // look up gamma
                R = gammaR[Color.red(pixel)];
                G = gammaG[Color.green(pixel)];
                B = gammaB[Color.blue(pixel)];
                // set new color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
     
        // return final image
        return bmOut;
    }
    
    private File getOutputPhotoFile() {
  	  File directory = new File(Environment.getExternalStorageDirectory()+"/demo/");
  	  if (!directory.exists()) {
  	    if (!directory.mkdirs()) {
  	      Log.e(TAG, "Failed to create storage directory.");
  	      return null;
  	    }
  	  }
  	  String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
  	  return new File(directory.getPath() + File.separator + "IMG_"  
  	                    + timeStamp + ".jpg");
  	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  	  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
  	    if (resultCode == RESULT_OK) {
  	      Uri photoUri = null;
  	      if (data == null) {
  	        // A known bug here! The image should have saved in fileUri
  	        Toast.makeText(this, "Image saved successfully", 
  	                       Toast.LENGTH_LONG).show();
  	        photoUri = fileUri;
  	      } else {
  	        photoUri = data.getData();
  	        Toast.makeText(this, "Image saved successfully in: " + data.getData(), 
  	                       Toast.LENGTH_LONG).show();
  	      }
  	      showPhoto(photoUri.getPath());
  	    } else if (resultCode == RESULT_CANCELED) {
  	      Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
  	    } else {
  	      Toast.makeText(this, "Callout for image capture failed!", 
  	                     Toast.LENGTH_LONG).show();
  	    }
  	  }
  	}
    
    private void showPhoto(String photoUri) {

  	
  	File imageFile = new File(photoUri);
    if (imageFile.exists()){
       Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
       //BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
       Bitmap d = new BitmapDrawable(this.getResources() , bitmap).getBitmap();
       int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
       Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);

       ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
       myImage2.setImageBitmap(scaled);
       setImagePointer(scaled);
    }       
  	 }       
  	
    
}
