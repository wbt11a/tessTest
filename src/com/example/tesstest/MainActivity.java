package com.example.tesstest;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.leptonica.android.*;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	
	public static final int ENGINE = 0; // default - tesseract only
	public InputStream initialImage = null;
	public TessBaseAPI baseApi = new TessBaseAPI();
	public Bitmap imageViewPointer = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
    
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button clickButton1 = (Button)findViewById(R.id.button1);
        clickButton1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            	
            	String temp = getCacheDir().getAbsolutePath();
            	temp+="/tessdata";
            	File f = new File(temp);
            	File[] list = f.listFiles();
            	
                TextView myTextView = (TextView) findViewById(R.id.textView1);
            	myTextView.setText(Arrays.toString(list));
            }
        });
        
        Button clickButton2 = (Button)findViewById(R.id.button2);
        clickButton2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            	
            	MaintenanceClass.trimCache(getApplicationContext());
            		findViewById(R.id.button1).performClick();
            }
        });
        
        Button clickButton3 = (Button)findViewById(R.id.button3);
        clickButton3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        	try {
				initialImage = getAssets().open("YMt9d.png");
			
        	    Bitmap bit2 = BitmapFactory.decodeStream(initialImage);
                Pix bin_image = Binarize.otsuAdaptiveThreshold(ReadFile.readBitmap(bit2));  //binarize with Leptonica
            
          
            	Bitmap bit3 = WriteFile.writeBitmap(bin_image);  //convert from Leptonica PIX to Bitmap
            	ImageView myImage2 = (ImageView)findViewById(R.id.imageView1);
            	if(bit3!=null)
            		myImage2.setImageBitmap(bit3);
            	else
            		MaintenanceClass.popDebug(getApplicationContext(), "bit3 is null.");
            }
            catch(Exception ex){
            	MaintenanceClass.popDebug(getApplicationContext(), ex.toString());
            }
         }
            
        });
        
      
        
        try{
        	
        	
        	initialImage = getAssets().open("YMt9d.png");
        	Bitmap bit = BitmapFactory.decodeStream(initialImage);
        	ImageView myImage = (ImageView) findViewById(R.id.imageView1);
            myImage.setImageBitmap(bit);
            initialImage.close();
        	//MaintenanceClass.extractAssets(getApplicationContext());
    
       
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /*public boolean conductOCR(Context context, int Engine){
    	 baseApi.init(getCacheDir().getAbsolutePath(), "eng", ENGINE);
    	 baseApi.setImage();
         String recognizedText = baseApi.getUTF8Text();
         TextView myTextView = (TextView) findViewById(R.id.textView1);
     	myTextView.setText(recognizedText);
         //popDebug(getApplicationContext(),recognizedText);
          
        baseApi.end();
    }*/
   
}
