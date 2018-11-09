package com.icoffice.library.service;

/**
 * 开机自启动 service
 * 
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.icoffice.library.app.BaseApplication;

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String action_boot = "android.intent.action.BOOT_COMPLETED";
	private BaseApplication application;
	@Override
	public void onReceive(Context context, Intent intent) {
		application = (BaseApplication) context.getApplicationContext();
		if (intent.getAction().equals(action_boot)) {
			Intent ootStartIntent = new Intent(context,application.getC());
			ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(ootStartIntent);
		}

	}

}