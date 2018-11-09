// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Chat Demo is licensed to you under the terms described
// in the CHAT_DEMO_LICENSE file included in this distribution.
//
// **********************************************************************

package com.icoffice.library.ice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Ice.Current;
import android.content.pm.PackageManager.NameNotFoundException;

import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.ApkUtil;
import com.icoffice.library.utils.CommonUtils;

//
// This class implements the ChatRoomCallback interface.
//
class MachineCallbackI extends CofficeServer._MachineCallbackDisp {
	public MachineCallbackI(BaseMachineControl machineControl) {
		_machineControl = machineControl;
	}

	private final BaseMachineControl _machineControl;

	@Override
	public Map<String, String> queryStatus(Map<String, String> options,
			Current __current) {
		Map<String, String> ret = new HashMap<String, String>();
		CommonUtils.showLog(CommonUtils.ICE_TAG, "服务器查询客户端 options = " + options.toString() + ";__current = " + __current.toString());
		ret.put("state", "running");
		if(options.get("settingInfo") != null)
		{
			ArrayList<String> list = _machineControl.getMachineSetStatus();
			try{
				ret.put("coinAndnoteStatus", list.get(0));
				ret.put("coinWaitTime", list.get(1));
				ret.put("physicsButtonStatus", list.get(2));
				CommonUtils.showLog(CommonUtils.ICE_TAG, "coinAndnoteStatus = " + list.get(0) + " coinWaitTime = " + list.get(1) + " physicsButtonStatus" + list.get(2));
			}catch(Exception e){
				
			}
			
			
		}
		if (options.get("basicInfo") != null)
		{
			try {
				ret.put("apk_ver", Integer.toString(ApkUtil.getVerCode(_machineControl
						.getContext())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				ret.put("simInfo", _machineControl.analysisSIM());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				ret.put("startTime", _machineControl.getRestartTime());
				CommonUtils.showLog(CommonUtils.ICE_TAG, "startTime = " + _machineControl.getRestartTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (options.get("sqlQuery") != null)
		{
			try {
				String sql = options.get("sqlQuery");
				ret.put("sqlQuery", _machineControl.analysisCursor(sql));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CommonUtils.showLog(CommonUtils.ICE_TAG, "客户端返回服务器的 ret = "  + ret.toString());
		return ret;
	}

	@Override
	public void pushMessage(String message, Map<String, String> options,
			Current __current) {
		if (message.equals(CofficeServer.MsgNewApkVersion.value)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						_machineControl.upgradeApk(null);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			
		} else if (message.equals(CofficeServer.MsgNewAds.value)) {
			_machineControl.socketAd();
		} else if (message.equals(CofficeServer.MsgConfigChanged.value)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					_machineControl.reloadConfig();					
				}
			}).start();
		} else if (message.equals(CofficeServer.MsgRestartMachine.value)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					_machineControl.exitRebootByNet();
				}
			}).start();
			
		} else if (message.equals(CofficeServer.MsgRestartApp.value)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					_machineControl.exitAppByNet();
				}
			}).start();
			
		}
	}

	@Override
	public long getFileLength(String path, Current __current) {
		File inputFile = new File(path);
		long file_length = inputFile.length();
		
//		CommonUtils.showLog(CommonUtils.ICE_TAG, "getFileLength = " + file_length);
		return file_length;
	}

	@Override
	public byte[] readFile(String path, long offset, int length, Current __current) {
		// TODO Auto-generated method stub
		File inputFile = new File(path);
		inputFile.length();
		try {
			FileInputStream in = new FileInputStream(inputFile);
			byte[] content = new byte[length];
			in.skip(offset);
			in.read(content, 0, length);
//			CommonUtils.showLog(CommonUtils.ICE_TAG, "readFile 输出文件长度 = " + length + ";content = " + new String(content));
			
			return content;
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			CommonUtils.showLog(CommonUtils.ICE_TAG, "readFile 输出文件长度 exception = " + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 发送出货指令
	 * 
	 */
	@Override
	public Map<String, String> sendMessage(String message,Map<String, String> options, Current __current) {
		CommonUtils.showLog(CommonUtils.ICE_TAG, "message = " + message);
		Map<String,String> map = new HashMap<String, String>();
		if (message.equals(CofficeServer.MsgNewApkVersion.value)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						_machineControl.upgradeApk(null);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			
		} else if (message.equals(CofficeServer.MsgNewAds.value)) {
			_machineControl.socketAd();
		} else if (message.equals(CofficeServer.MsgConfigChanged.value)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					_machineControl.reloadConfig();					
				}
			}).start();
		} else if (message.equals(CofficeServer.MsgRestartMachine.value)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					_machineControl.exitRebootByNet();
				}
			}).start();
			
		} else if (message.equals(CofficeServer.MsgRestartApp.value)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					_machineControl.exitAppByNet();
				}
			}).start();
			
		} else if (message.equals(CofficeServer.MsgOutGoods.value)) {
			String client_order_no = options.get("client_order_no");
			String msg = _machineControl.serverOutGoods(client_order_no);
			map.put("msg", msg);
		}
		return map;
	}

}
