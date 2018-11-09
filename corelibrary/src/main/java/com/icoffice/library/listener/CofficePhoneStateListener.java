package com.icoffice.library.listener;

import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;

import com.icoffice.library.moudle.control.BaseMachineControl;

public class CofficePhoneStateListener extends PhoneStateListener {
	private BaseMachineControl _machineControl;
	public CofficePhoneStateListener(BaseMachineControl machineControl){
		_machineControl = machineControl;
	}
	private static final String TAG = PhoneStateListener.class.getSimpleName();
	
	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		super.onSignalStrengthsChanged(signalStrength);
		Log.i(TAG, signalStrength.isGsm() ? "2G" : "3G");
		if(signalStrength.isGsm())
			_machineControl.setNetworkMode("2");
		else
			_machineControl.setNetworkMode("3");
		if(signalStrength.isGsm()) {
			_machineControl.setSignalStrength(gsmLevelTODbm(signalStrength.getGsmSignalStrength()));
			Log.i(TAG, "2G信号强度: " + gsmLevelTODbm(signalStrength.getGsmSignalStrength()));
		} else {
			_machineControl.setSignalStrength(signalStrength.getCdmaDbm());
			Log.i(TAG, "联通3G信号强度: " + signalStrength.getCdmaDbm() + " dBm");
		}
		
	}

	@Override
	public void onServiceStateChanged(ServiceState serviceState) {
		super.onServiceStateChanged(serviceState);
		
		switch(serviceState.getState()) {
		case ServiceState.STATE_EMERGENCY_ONLY:
			Log.i(TAG, "STATE_EMERGENCY_ONLY");
			break;
		case ServiceState.STATE_IN_SERVICE:
			Log.i(TAG, "STATE_IN_SERVICE");
			break;
		case ServiceState.STATE_OUT_OF_SERVICE:
			Log.i(TAG, "STATE_OUT_OF_SERVICE");
			break;
		case ServiceState.STATE_POWER_OFF:
			Log.i(TAG, "STATE_POWER_OFF");
			break;
			default:
				break;
		}
	}
	
	private int gsmLevelTODbm(int i) {
		return i * 2 - 113;
	}
}
