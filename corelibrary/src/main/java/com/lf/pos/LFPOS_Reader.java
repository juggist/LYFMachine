package com.lf.pos;

//读卡方式的代码(卡片类型)
public class LFPOS_Reader {
	public final int LFPOS_R_SWIPE = 1; // 刷磁卡
	public final int LFPOS_R_PLUG  = 2; // 插入IC卡
	public final int LFPOS_R_TAP   = 4; // 嘀卡或挥卡(即，非接触式读卡)
}
