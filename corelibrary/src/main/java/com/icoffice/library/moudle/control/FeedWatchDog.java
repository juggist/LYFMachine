package com.icoffice.library.moudle.control;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FeedWatchDog {

	private static final String TAG = FeedWatchDog.class.getSimpleName();
	private static FeedWatchDog mFeedWatchDog = null;
	private final Timer mTimer = new Timer();
	private Context mCtx = null;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			startFeedDog();
		}
		
	};
	
	private FeedWatchDog() {
	}
	
	private FeedWatchDog(Context ctx) {
		this.mCtx = ctx;
	}
	
	public static FeedWatchDog getInstance(Context ctx) {
		if(mFeedWatchDog == null) {
			mFeedWatchDog = new FeedWatchDog(ctx);
		}
		
		return mFeedWatchDog;
	}

	private void startFeedDog() {
		if(mCtx == null) {
			return;
		}
		
		Intent intent = new Intent();
		intent.setAction("com.ubox.watchdog.feed");
		intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		Log.d(TAG, "feed watchdog");
		mCtx.sendBroadcast(intent);
	}
	
	private TimerTask mTask = new TimerTask() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(0);
		}
	};
	
	public void schedule(long delay, long period) {
		this.mTimer.schedule(mTask, delay, period);
	}
}
