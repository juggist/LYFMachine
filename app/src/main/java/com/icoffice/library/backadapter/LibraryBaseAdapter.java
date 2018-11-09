package com.icoffice.library.backadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.icoffice.library.moudle.control.ImageLoaderControl;
import com.icofficeapp.R;

public class LibraryBaseAdapter extends BaseAdapter{
	protected ImageLoaderControl mImageLoaderControl;
	public LibraryBaseAdapter(){
		mImageLoaderControl = ImageLoaderControl.getInstance();
		
	}
	protected void displayImage(String url,ImageView iv){
		mImageLoaderControl.displayImageSmall(url, iv,R.drawable.empty);
	}	
	protected void displayImage(int url,ImageView iv){
		mImageLoaderControl.displayImage(url, iv);
		
	}	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
