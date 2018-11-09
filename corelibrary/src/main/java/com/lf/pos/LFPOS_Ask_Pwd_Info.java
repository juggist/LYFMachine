package com.lf.pos;


//提示输入密码时的信息
public class LFPOS_Ask_Pwd_Info {
	private int AskPwdInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private int uCharNum; // 当前输入的字符数，取值范围 0 - 6
	
	public LFPOS_Ask_Pwd_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		AskPwdInfoObject = createAskPwdInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyAskPwdInfoObject(AskPwdInfoObject);
		super.finalize();
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadAskPwdInfoObject(this, AskPwdInfoObject);
	}

	public int getCharNum()
	{
		return this.uCharNum;
	}

	private static native void loadAskPwdInfoObject(LFPOS_Ask_Pwd_Info param, int address);
	private native static int createAskPwdInfoObject();
	private native static void destroyAskPwdInfoObject(int object);
}
