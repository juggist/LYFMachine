package com.icoffice.library.activity;
/**
 * author: Michael.Lu
 */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.icoffice.library.app.BaseApplication;

public class BaseActivity extends FragmentActivity {
	public BaseApplication mApplication;
	public BaseActivity(){
		
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (BaseApplication) getApplication();
		mApplication.addActivity(this);
	}
}
