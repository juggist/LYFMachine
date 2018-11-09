package com.icofficeapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.icoffice.library.moudle.control.ImageLoaderControl;
import com.icofficeapp.R;

public class ICofficeBaseAdapter extends BaseAdapter{
	protected ImageLoaderControl mImageLoaderControl;
	public ICofficeBaseAdapter(){
		mImageLoaderControl = ImageLoaderControl.getInstance();
		
	}
	protected void displayImage(String url,ImageView iv){
		mImageLoaderControl.displayImageBig(url, iv,R.drawable.empty_detail);
	}	
	protected void displayImage(int url,ImageView iv){
		mImageLoaderControl.displayImage(url, iv);
		
	}	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}
	
}
