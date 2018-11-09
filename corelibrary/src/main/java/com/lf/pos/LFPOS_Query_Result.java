package com.lf.pos;


//余额查询的结果
public class LFPOS_Query_Result {
	private int QueryResultObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String szPan;    // 卡号
	private int eType; // 银行卡帐户类型, 取值为 LFPOS_ACCOUNT
	private int uBalance;       // 余额，以人民币分为单位
	
	public LFPOS_Query_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		QueryResultObject = createQueryResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyQueryResultObject(QueryResultObject);
		super.finalize();
	}
	
	public String getPan()
	{
		return this.szPan;
	}
	
	public int getBalance()
	{
		return this.uBalance;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadQueryResultObject(this, QueryResultObject);
	}
	

	private static native void loadQueryResultObject(LFPOS_Query_Result param, int address);
	private native static int createQueryResultObject();
	private native static void destroyQueryResultObject(int object);
}
