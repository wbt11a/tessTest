package com.example.tesstest;

import com.googlecode.leptonica.android.AdaptiveMap;
import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Enhance;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.Rotate;
import com.googlecode.leptonica.android.Skew;

public class Leptonica {

	
	
	public static Pix enhance(Pix pix){
		Pix temp = Enhance.unsharpMasking(pix, 2, (float) 0.5);
		pix.recycle();
		return temp;
	}
    
	public static Pix binarize(Pix pix){
		Pix temp = Binarize.otsuAdaptiveThreshold(pix);
		pix.recycle();
		return temp;
	}
    
	public static Pix adaptiveMap(Pix pix){
		Pix temp = AdaptiveMap.backgroundNormMorph(pix);
		pix.recycle();
		return temp;
	}
    
	public static Pix deskew(Pix pix){
       float skew = Skew.findSkew(pix);
 	   Pix rotated_img = Rotate.rotate(pix, skew);
 	   pix.recycle();
 	   return rotated_img;
    }
    
	public static Pix rotateRight(Pix pix){
    	
    	Pix temp = Rotate.rotate(pix, 90);
    	pix.recycle();
    	return temp;
    }
    
	public static Pix rotateLeft(Pix pix){
    	Pix temp = Rotate.rotate(pix, -90);
    	pix.recycle();
    	return temp;
    }
}
