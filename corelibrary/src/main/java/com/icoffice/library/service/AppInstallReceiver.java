package com.icoffice.library.service;
/**
 * 监听程序更新后 自己启动
 */
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.icoffice.library.app.BaseApplication;
import com.icoffice.library.utils.CommonUtils;

public class AppInstallReceiver extends BroadcastReceiver {
	static BaseApplication mBaseApplication;
    @Override
    public void onReceive(Context context, Intent intent) {
    	mBaseApplication = (BaseApplication) context.getApplicationContext();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            CommonUtils.showToast(context, "安装成功"+packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            CommonUtils.showToast(context, "卸载成功"+packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if(packageName.equals(context.getPackageName())){
            	Intent mIntent = new Intent( ); 
        		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName comp = new ComponentName(context.getPackageName(),mBaseApplication.getClassName());  
                mIntent.setComponent(comp);   
                mIntent.setAction("android.intent.action.VIEW");   
                context.startActivity(mIntent);  
            }
            CommonUtils.showToast(context, "替换成功"+packageName);
        }
    }
}