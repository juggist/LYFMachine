package com.icoffice.library.moudle.control;
/**
 * 远程更新工具类
 */
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.ice.Coordinator;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.download.DownloadProgressListener;
import com.icoffice.library.utils.download.FileDownloader;
import com.icoffice.library.utils.root.RootUtil;
import com.leepood.bsdiff.tools.PatchTools;

@SuppressLint("HandlerLeak")
public class UpgradeControl {
    public enum ResultType { Success, Fail, DownloadingApk, DownloadError };
    
    private static String ROOT_PATH = 	Environment.getExternalStorageDirectory().getAbsolutePath() + "/coffice_library/";// BasePath
    private static String APP_STR = 	"app/";
    
	private static float _maxSize;//下载apk的最大容量
	private final Coordinator _coordinator;
	private final Context _context;
	private final BaseMachineControl _machineControl;
	private String _appDirPath;
	private BaseViewHandler _viewHandler;
	private boolean _upgrading = false; // 当前是否正在升级，正在升级过程中再接收到升级请求会直接返回
	
	private Handler _handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ResultType ret = ResultType.values()[msg.what];
			if (ret == ResultType.Success) {
				Map<String, String> info = (Map<String, String>)msg.obj;
				CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"ResultType.Success status = " + info.get("status"));
				
				if(info.get("status").equals("1")){
					CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"开始下载APK："+info.get("url"));
					download(info.get("url"), new File(ROOT_PATH + APP_STR), _context.getPackageName()+".patch");
				}else{
					Message msgView = new Message();
					msgView.what = BaseViewHandler.UpgradeNothingToDo;
					sendMessageToViewHandler(msgView);

					_upgrading = false;
					
				}
			}
			else if (ret == ResultType.Fail) {
				try {
					CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"ResultType.Fail ");
					Message msgView = new Message();
					msgView.what = BaseViewHandler.UpgradeFail;
					msgView.obj = msg.obj;
					sendMessageToViewHandler(msgView);
					_upgrading = false;
				}
				catch (Exception e) {
					CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"ResultType.Fail Exception = " + e.toString());
				}
			}
			else if (ret == ResultType.DownloadingApk) {
				
				float num = msg.getData().getInt("size") / msg.getData().getFloat("maxSize");
				int currentProgress = (int) (num * 100); // 计算进度
				CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"DownloadingApk currentProgress = " + currentProgress);
				Message msgView = new Message();
				msgView.what = BaseViewHandler.UpgradeDownloading;
				msgView.obj = currentProgress;
				sendMessageToViewHandler(msgView);
				
				if (currentProgress == 100) {

					msgView.what = BaseViewHandler.UpgradeInstalling;
					sendMessageToViewHandler(msgView);
					new Thread(new Runnable() {
						public boolean install() {
							// 安装APK之前要锁定app售卖界面
							CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"upgrade control is trying to lock machine");
							if (_machineControl.lock(true)) {
								CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"upgrade control is already locked machine");
								try {
									CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"upgrade control start to install");
									PatchTools.applay_patch(_appDirPath, ROOT_PATH + APP_STR + _context.getPackageName()+".apk", ROOT_PATH + APP_STR  + _context.getPackageName()+".patch");
									RootUtil.install(ROOT_PATH + APP_STR , _context.getPackageName()+".apk", _context);
								} catch (Exception e) {
									e.printStackTrace();
									CommonUtils.showLog(CommonUtils.UPGRADE_TAG,e.toString());
									_machineControl.lock(false);
									return false;
								}
								CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"upgrade control is trying to unlock machine");
								_machineControl.lock(false);
								CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"upgrade control is already unlocked machine");
							} else {
								CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"upgrade control unable to lock machine");
								return false;
							}
							return true;
						}
						
						@Override
						public void run() {
							
							// 尝试安装apk，若无法安装则10秒后重试
							for (;;) {
								if (install()) {
									break;
								} else {
									try {
										Thread.sleep(10000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								_upgrading = false;
							}
						}
					}).start();
				}
			}
			else if (ret == ResultType.DownloadError) {

				try {
					CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"ResultType.DownloadError ");
					Message msgView = new Message();
					msgView.what = BaseViewHandler.UpgradeFail;
					msgView.obj = msg.obj;
					sendMessageToViewHandler(msgView);
					_upgrading = false;
				}
				catch (Exception e) {
					CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"ResultType.DownloadError Exception = " + e.toString());
				}
			}
			super.handleMessage(msg); 
		}
	};
	
	public UpgradeControl(Coordinator coordinator, Context context, BaseMachineControl machineControl){
		_coordinator = coordinator;
		_context = context;
		_machineControl = machineControl;
	}
	
	public void sendMessageToViewHandler(Message msg) {
		if (_viewHandler != null) {
			_viewHandler.sendMessage(msg);
		}
	}
	
	/*
	 * 与服务器版本号判断。是否需要更新
	 */
	public void checkUpgrade(final BaseViewHandler viewHandler, String appDirPath, 
			int versionCode, String appType) {
		
		synchronized(this) {
			// 正在升级中，直接返回
			if (_upgrading) {
				CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"上一个升级动作还未完成");
				return;
			}
	
			_upgrading = true;
			
			_viewHandler = viewHandler;
			_appDirPath = appDirPath;
			
			HashMap<String, String> options = new HashMap<String, String>();
			options.put("version_code", Integer.toString(versionCode));
			options.put("mt_id", appType);
			if (!_coordinator.downloadApk(options, _handler)) {
				_upgrading = false;
			}
		}
	}

	//	
	private void download(String path, File savDir, String apkName) {
		DownloadTask task = new DownloadTask(path, savDir, _context, apkName);
		new Thread(task).start();
	}

	/**
	 * 
	 * UI控件画面的重绘(更新)是由主线程负责处理的，如果在子线程中更新UI控件的值，更新后的值不会重绘到屏幕上
	 * 一定要在主线程里更新UI控件的值，这样才能在屏幕上显示出来，不能在子线程中更新UI控件的值
	 * 
	 */
	private class DownloadTask implements Runnable {
		private String path;
		private File saveDir;
		private FileDownloader loader;
		private Context mContext;
		private String apkName;
		
		public DownloadTask(String path, File saveDir,Context mContext,String apkName) {
			this.path = path;
			this.saveDir = saveDir;
			this.mContext = mContext;
			this.apkName = apkName;
		}

		/**
		 * 退出下载
		 */
		@SuppressWarnings("unused")
		public void exit() {
			if (loader != null)
				loader.exit();
		}

		DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
			@Override
			public void onDownloadSize(int size) {
				Message msg = new Message();
				msg.what = ResultType.DownloadingApk.ordinal();
				msg.getData().putInt("size", size);
				msg.getData().putFloat("maxSize", _maxSize);
				_handler.sendMessage(msg);
			}
		};

		public void run() {
			CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"download is trying to lock machine");
			if (_machineControl.lock(true)) {
				CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"download is already locked machine");
				try {
					// 实例化一个文件下载器
					loader = new FileDownloader(mContext, path,saveDir, 1,apkName);
					_maxSize = loader.getFileSize();
					loader.download(downloadProgressListener);
				} catch (Exception e) {
					e.printStackTrace();
					CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"download Exception = " + e.toString());
					Message msg = new Message();
					msg.what = ResultType.DownloadError.ordinal();
					msg.obj = e.toString();
					_handler.sendMessage(msg); // 发送一条下载错误消息对象
				}
				CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"download is trying to unlock machine");
				_machineControl.lock(false);
				CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"download is already unlocked machine");
			}
		}
	}
}
