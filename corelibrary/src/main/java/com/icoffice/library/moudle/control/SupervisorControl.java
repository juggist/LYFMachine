package com.icoffice.library.moudle.control;

import java.io.File;
import java.io.IOException;

import com.icoffice.library.utils.FileUtil;

import android.content.Context;

public class SupervisorControl {

	private static SupervisorControl _control = null;

	private final int INTERVAL = 1;
	private final String STOP_FLAG_FILE = "stop.flag";

	private Context _context;
	private String _processName;
	private String _activityName;
	private String _workingPath;
	private String _supervisorPath;
	private String _busyboxPath;
	private Process _process;

	private SupervisorControl(Context context, String activityName) {
		_context = context;
		_activityName = activityName;
		
		File filesDir = context.getFilesDir();

		_processName = _context.getPackageName();
		_workingPath = filesDir.getAbsolutePath();
		_supervisorPath = _workingPath + "/supervisor";
		_busyboxPath = _workingPath + "/busybox-armv7l";
	}

	public static SupervisorControl getInstance(Context context,
			String activityName) {
		if (_control == null) {
			_control = new SupervisorControl(context, activityName);
		}
		return _control;
	}

	public static SupervisorControl getInstance(Context context) {
		return _control;
	}

	public void start() {
		File flagFile = new File(_workingPath+"/"+STOP_FLAG_FILE);
		flagFile.delete();
		try {
			_process = Runtime.getRuntime().exec("su");
			String cmd = _supervisorPath + " " + INTERVAL + " " + _processName + " "
					+ _activityName + " " + _busyboxPath + " "
					+ _workingPath + "\n";
			_process.getOutputStream().write(cmd.getBytes());
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}

	public void stopSupervisor() {
		File flagFile = new File(_workingPath+"/"+STOP_FLAG_FILE);
		try {
			flagFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop() {
		_process.destroy();
	}
}
