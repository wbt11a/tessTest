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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	//InputStream bitmap = null;
    	//InputStream datafile = null;
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
        
        try{
        	
        	String[] files = getAssets().list("tessdata");
        	TextView myTextView = (TextView) findViewById(R.id.textView1);
        	myTextView.setText(Arrays.toString(files));
        	
        }
        catch (Exception ex){
        	popDebug(getApplicationContext(), "Error: " + ex.toString());
        }
        
        TessBaseAPI baseApi = new TessBaseAPI();
        //baseApi.init(dataPath, "eng");
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(extractAssets(), options);
        
       
        
        ImageView myImage = (ImageView) findViewById(R.id.imageView1);
        myImage.setImageBitmap(bitmap);
        //baseApi.setImage(bitmap);
        //String recognizedText = baseApi.getUTF8Text();
       // popDebug(getApplicationContext(),recognizedText);
        baseApi.end();
        
    }
    
    public String extractAssets(){
    	File f = new File(getCacheDir(), "test_image.jpg");
    	if(!f.exists()) try{
    		InputStream is = getAssets().open("test_image.jpg");
    		int size = is.available();
    		byte[] buffer = new byte[size];
    		is.read(buffer);
    		is.close();
    		
    		FileOutputStream fos = new FileOutputStream(f);
    		fos.write(buffer);
    		fos.close();
    	}catch(Exception e) { throw new RuntimeException(e);}
    	
    	return f.getPath();
    }
    	
    
    
    public static void popDebug(Context context, String message) {
        Toast.makeText(context, message, 1000).show();
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
}
