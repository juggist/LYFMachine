package com.leiyuntop.machine;

public class SCSaleMotorState
{
	public static final int 正常 = 0x00;
	public static final int 道号错误 = 0x01;
	public static final int 到位开关到位错误 = 0x02;
	public static final int 到位开关切开超时 = 0x04;
	public static final int 到位开关闭合超时 = 0x08;
	public static final int 电机运行过流保护 = 0x10;
	public static final int 电机无启动电流 = 0x20;
}
