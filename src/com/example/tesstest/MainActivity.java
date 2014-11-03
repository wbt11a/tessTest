package com.example.tesstest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.support.v7.app.ActionBarActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private String[] files = null;
	public static final int ENGINE = 0; // default - tesseract only
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	InputStream bitmap = null;
    	//InputStream datafile = null;
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button clickButton1 = (Button)findViewById(R.id.button1);
        clickButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//extractAssets();
            //	String temp = getCacheDir().getAbsolutePath();
            //	temp+="/tessdata";
            //	File f = new File(temp);
            	//File[] list = f.listFiles();
            	
                //TextView myTextView = (TextView) findViewById(R.id.textView1);
            	//myTextView.setText(Arrays.toString(list));
            }
        });
      
        AssetManager assetManager = this.getAssets();
        try{
        	
        	this.files=assetManager.list("tessdata");
        	bitmap = getAssets().open("test_image.jpg");
        	extractAssets();
        	
        	//trimCache(getApplicationContext());
        	//extractAssets();
        }
        catch (Exception ex){
        	popDebug(getApplicationContext(), "Error: " + ex.getMessage());
        }
        
        
    	
        TessBaseAPI baseApi = new TessBaseAPI();
        
        /*String tessDir = null;
        for (File t : dir){
        	if(t.isDirectory())
        		tessDir = t.getAbsolutePath();
        }
        popDebug(getApplicationContext(), "tessdir:" + tessDir);*/
        
       baseApi.init(getCacheDir().getAbsolutePath(), "eng", ENGINE);
        
       Bitmap bit = BitmapFactory.decodeStream(bitmap);
              
        ImageView myImage = (ImageView) findViewById(R.id.imageView1);
        myImage.setImageBitmap(bit);
        baseApi.setImage(bit);
        String recognizedText = baseApi.getUTF8Text();
        TextView myTextView = (TextView) findViewById(R.id.textView1);
    	myTextView.setText(recognizedText);
        //popDebug(getApplicationContext(),recognizedText);
        baseApi.end();
        
        
        
    }
 
    
    public void extractAssets(){
    	
    	 File root = new File(getCacheDir() + "/tessdata");
         if (!root.exists()) {
             root.mkdirs();
          }
    	for(int x=0; x< files.length;x++){
    		//popDebug(getApplicationContext(),"Extracting " + files[x] + " from assets.");
    	
	    	File f = new File(getCacheDir()+"/tessdata/", files[x]);
	    	if(!f.exists()) try{
	    	
	    		InputStream is = getAssets().open("tessdata/"+files[x]);
	    		
	    		int size = is.available();
	    		byte[] buffer = new byte[size];
	    		is.read(buffer);
	    		is.close();
	    		
	    		FileOutputStream fos = new FileOutputStream(f);
	    		fos.write(buffer);
	    		fos.close();
	    		
	    	}
	    	
	    	catch(Exception e) { throw new RuntimeException(e.getMessage());}
    	}
    
    }
    
    
    
    
    public static void popDebug(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
    
    public static void trimCache(Context context) {
        try {
           File dir = context.getCacheDir();
           if (dir != null && dir.isDirectory()) {
              deleteDir(dir);
           }
        } catch (Exception e) {
           // TODO: handle exception
        }
     }

     public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
           String[] children = dir.list();
           for (int i = 0; i < children.length; i++) {
              boolean success = deleteDir(new File(dir, children[i]));
              if (!success) {
                 return false;
              }
           }
        }

        // The directory is now empty so delete it
        return dir.delete();
     }
}
