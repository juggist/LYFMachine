package com.icofficeapp.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.icoffice.library.moudle.control.AdTimerTaskControl;
import com.icoffice.library.utils.CommonUtils;
import com.icofficeapp.R;

public class MainActivity extends BaseFragmentActivity implements OnGestureListener {
	private GestureDetector mGestureDetector;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			CommonUtils.showLog(CommonUtils.EXCEPTION_TAG, "MainActivity onCreate 刷新广告");
			showAdView();
		}
		mGestureDetector = new GestureDetector(MainActivity.this);
	}
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mGestureDetector.onTouchEvent(ev);// 在有listview的情况下。先捕获屏幕的左右滑动事件，然后在传递给listview，防止listview覆盖掉左右滑动事件
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(currentView == 1){
				int time = 0;
				if(mAdTimerTaskControl.getAdTimer() > 60)
					time = AdTimerTaskControl.AdLastTimer;
				else
					time = AdTimerTaskControl.AdStartTimer;
				mAdTimerTaskControl.setAdTimer(time);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if(currentView == 1){
				int time = 0;
				if(mAdTimerTaskControl.getAdTimer() > 60)
					time = AdTimerTaskControl.AdLastTimer;
				else
					time = AdTimerTaskControl.AdStartTimer;
				mAdTimerTaskControl.setAdTimer(time);
			}
				
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
}
