package com.icoffice.library.moudle.control;

import java.io.File;

import android.widget.ImageView;

import com.icoffice.library.utils.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageLoaderControl {
	private static ImageLoaderControl mImageLoaderControl;
	protected ImageLoader mImageLoader;
	private ImageLoaderControl(){
		mImageLoader = ImageLoader.getInstance();
		
	}
	public static ImageLoaderControl getInstance(){
		if(mImageLoaderControl == null)
			mImageLoaderControl = new ImageLoaderControl();
		return mImageLoaderControl;
	}
	public void displayImage(String url,ImageView iv,int empty_img_id){
		// empty_img_id = R.drawable.empty_detail
		try{
			if(new File(url).exists())
				mImageLoader.displayImage("file://" + url, iv);
			else
				mImageLoader.displayImage("drawable://" + empty_img_id, iv);
		}catch(Exception e){
			CommonUtils.showLog(CommonUtils.TAG,"displayImage exception");
		}
	}	
	public void displayImageBig(String url,ImageView iv,int empty_img_id){
		// empty_img_id = R.drawable.empty_detail
		try{
			if(new File(url).exists()){
				mImageLoader.displayImage("file://" + url, iv);
			}
				
			else{
				mImageLoader.displayImage("drawable://" + empty_img_id, iv);
			}
				
		}catch(Exception e){
			CommonUtils.showLog(CommonUtils.TAG,"displayImage exception");
		}
	}	
	public void displayImageSmall(String url,ImageView iv,int empty_img_id){
		// empty_img_id = R.drawable.empty
		try{
			if(new File(url).exists())
				mImageLoader.displayImage("file://" + url, iv);
			else
				mImageLoader.displayImage("drawable://" + empty_img_id, iv);
		}catch(Exception e){
			CommonUtils.showLog(CommonUtils.TAG,"displayImage exception");
		}
	}	
	public void displayImage(int url,ImageView iv){
		try{
			mImageLoader.displayImage("drawable://" + url, iv);
		}catch(Exception e){
			CommonUtils.showLog(CommonUtils.TAG,"displayImage exception");
		}
		
	}	
}
