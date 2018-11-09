package com.lf.pos;


////////////////////////////////////////
//事件的返回结果
//
//注意，字符数组类型的字段，如果是空串(即首字节为结束符'\0')
//就表示该字段无数据

//返回结果的基本部分
public class LFPOS_RTNHDR {
	private int RTNHDRObject;
	
	private int event;		//LFPOS_EVENT
	private int errcode; // 错误代码
	// 错误代码对应的文字说明
	//   这里给出的是指针，而不是字符数组
	//   宽字节 !
	// 可以为空，表示不需提示用户
	private String desc;
	// 当给出管理模式下的提示文字时，各字段要求:
	//   event >= LFPOS_EVT_FROM_POS
	//   errcode == LFPOS_E_SUCCESS
	//   desc 非空，可以是多行文字
	// 当消费成功时，desc 字段不表示指针值，见“消费的结果”
	
	public LFPOS_RTNHDR() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		RTNHDRObject = createRTNHDRObject();
	}
	
	public int getevent(){
		return event;
	}
	
	public int geterrcode(){
		return errcode;
	}
	
	public String getdesc(){
		return desc;
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyRTNHDRObject(RTNHDRObject);
		super.finalize();
	}
	
	public void getNativeObject() {
		loadRTNHDRObject(this, RTNHDRObject);
	}
	

	private static native void loadRTNHDRObject(LFPOS_RTNHDR param, int address);
	private native static int createRTNHDRObject();
	private native static void destroyRTNHDRObject(int object);
}
