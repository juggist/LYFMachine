package com.lf.pos;


//更新的数据
public class LFPOS_Upgrade_Param {
	private int UpgradeParamObject;
	
	private int fType;   // 文件的类型  例如：eType = (LFPOSFtype).LFPOS_F_UNK 值为LFPOSFtype里面的值
	private String szLocalPkg;   // 更新文件的本地路径
	
	public LFPOS_Upgrade_Param() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		UpgradeParamObject = createUpgradeParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyUpgradeParamObject(UpgradeParamObject);
		super.finalize();
	}
	
	public void setfType(int fType) {
		this.fType = fType;
	}
	
	public void setLocalPkg(String szLocalPkg) {
		this.szLocalPkg = szLocalPkg;
	}
	
	public int setNativeObject() {
		saveUpgradeParamObject(this, UpgradeParamObject);
		return UpgradeParamObject;
	}
	
	private static native void saveUpgradeParamObject(LFPOS_Upgrade_Param param, int address);
	private native static int createUpgradeParamObject();
	private native static void destroyUpgradeParamObject(int object);
}