package com.lf.pos;

//设备状态代码
public class LFPOS_State {
	public static final int LFPOS_S_UNK = 0; 	// 未知（不应出现）
	public static final int LFPOS_S_INIT = 1;    // 初始化
	public static final int LFPOS_S_MGMT = 2;    // 管理模式
	public static final int LFPOS_S_IDLE = 3;	// 正常模式，空闲状态, 这时可以下发指令
	public static final int LFPOS_S_BUSY = 4;	// 正常模式，功能运行中, 这时下发新指令，返回错误代码LFPOS_E_UNEXPECTED
}