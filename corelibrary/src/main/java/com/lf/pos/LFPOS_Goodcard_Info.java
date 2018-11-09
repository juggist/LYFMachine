package com.lf.pos;


//读银行卡成功时的信息
public class LFPOS_Goodcard_Info {
	private int GoodcardInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String szPan; // 卡号（左对齐，后补空格）
	// 上位机在回调函数中判断该卡是否可被增值业务所接受：
	// 可接受时回调函数返回 TRUE，
	// 不可接受时返回 FALSE (将自动取消当前交易操作)。
	
	public LFPOS_Goodcard_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		GoodcardInfoObject = createGoodcardInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyGoodcardInfoObject(GoodcardInfoObject);
		super.finalize();
	}

	public String getPan()
	{
		return this.szPan;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadGoodcardInfoObject(this, GoodcardInfoObject);
	}
	

	private static native void loadGoodcardInfoObject(LFPOS_Goodcard_Info param, int address);
	private native static int createGoodcardInfoObject();
	private native static void destroyGoodcardInfoObject(int object);
}
