package com.icoffice.machine.serial;

public class SerialService {

	static
	{
		System.loadLibrary("serial_service");
	}
	
	public int resetLfPos(int comNo){
		serial_port_reset_lf_pos(3);
		return comNo;
	}

	public int cancelLfPos(int comNo){
		serial_port_cancel_lf_pos(3);
		return comNo;
	}
	
	private static native int serial_port_reset_lf_pos(int portNo);
	private static native int serial_port_cancel_lf_pos(int portNo);
}
