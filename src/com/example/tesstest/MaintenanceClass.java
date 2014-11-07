package com.example.tesstest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

public class MaintenanceClass {
	
    public static void popDebug(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    public static void extractAssets(Context context){
     String files[] = null;
     
     try{	
	     AssetManager assetManager = context.getAssets();
	     files = assetManager.list("tessdata");	
     }  
     catch (Exception ex){
    	popDebug(context, "Error: " + ex.getMessage());
     }
     
   	 File root = new File(context.getCacheDir() + "/tessdata");
     if (!root.exists()) {
    	 root.mkdirs();
     }
   	 
     for(int x=0; x < files.length;x++){
   		
   	
	    	File f = new File(context.getCacheDir()+"/tessdata/", files[x]);
	    	if(!f.exists()) try{
	    	
	    		InputStream is = context.getAssets().open("tessdata/"+files[x]);
	    		
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
     
     
     public static Object deepClone(Object object) {
    	   try {
    	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	     ObjectOutputStream oos = new ObjectOutputStream(baos);
    	     oos.writeObject(object);
    	     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    	     ObjectInputStream ois = new ObjectInputStream(bais);
    	     return ois.readObject();
    	   }
    	   catch (Exception e) {
    	     e.printStackTrace();
    	     return null;
    	   }
    	 }

}
