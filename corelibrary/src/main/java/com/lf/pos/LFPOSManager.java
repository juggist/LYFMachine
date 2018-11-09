package com.lf.pos;

public class LFPOSManager {
	private static LFPOSManager instance = new LFPOSManager();
	
	private ResultListener listener;
	
	public static LFPOSManager getInstance() {
		return instance;
	}
	
	public void setListener(ResultListener listener) {
		this.listener = listener;
	}

	////////----------------------------- 对外接口  ------------------------------------///////////
	/**
	 * 初始化
	 * @param LFPOS_Init_Param object
	 * @return
	 */
	public int initPos(LFPOS_Init_Param param) {
		return lfpos_init(param.setNativeObject());
	}
	/**
	 * 发起签到
	 * @param 
	 * @return
	 */
	public int signinPos() {
		return lfpos_sign_in();
	}
	/**
	 * 发起结算
	 * @param 
	 * @return
	 */
	public int settlePos() {
		return lfpos_settle();
	}
	/**
	 * 发起消费
	 * @param LFPOS_Trade_Param object
	 * @return
	 */
	public int tradePos(LFPOS_Trade_Param param) {
		return lfpos_trade(param.setNativeObject());
	}
	/**
	 * 发起消费撤销
	 * @param LFPOS_Revoke_Param object
	 * @return
	 */
	public int revokePos(LFPOS_Revoke_Param param) {
		return lfpos_revoke(param.setNativeObject());
	}
	/**
	 * 查询余额
	 * @param 
	 * @return
	 */
	public int queryPos() {
		return lfpos_query();
	}
	/**
	 * 发起圈存
	 * @param uPayment 圈存金额
	 * @return
	 */
	public int loadPos(int uPayment) {
		return lfpos_load(uPayment);
	}
	/**
	 * 取消消费或余额查询
	 * @param 
	 * @return
	 */
	public int cancelPos() {
		return lfpos_cancel();
	}
	/**
	 * 获取POS终端状态
	 * @param LFPOS_Out_State object
	 * @return
	 */
	public int getstatePos(LFPOS_Out_State param) {
		lfpos_get_state(param.getLocalObject());
		param.getNativeObject();
		return 0;
	}
	/**
	 * 复位POS终端
	 * @param 
	 * @return
	 */
	public int resetPos() {
		return lfpos_reset();
	}
	/**
	 * 获取卡号信息
	 * @param LFPOS_Read_Cardno_Param object
	 * @return
	 */
	public int readcardnoPos(LFPOS_Read_Cardno_Param param) {
		lfpos_read_cardno(param.getLocalObject());
		//param.getNativeObject();
		return 0;
	}
	/**
	 * 获取POS终端程序版本信息
	 * @param LFPOS_Version_Param object
	 * @return
	 */
	public int getversionPos(LFPOS_Version_Param param) {
		lfpos_get_pos_version(param.getLocalObject());
		param.getNativeObject();
		return 0;
	}
	/**
	 * 升级程序
	 * @param LFPOS_Upgrade_Param object
	 * @return
	 */
	public int upgradePos(LFPOS_Upgrade_Param param) {
		return lfpos_upgrade(param.setNativeObject());
	}
	/**
	 * 停止升级程序
	 * @param 
	 * @return
	 */
	public int stopupgradePos() {
		return lfpos_stop_upgrade();
	}
	/**
	 * 获取POS终端剩余空间可存储流水条数
	 * @param 
	 * @return POS终端剩余空间可存储流水条数
	 */
	public int getfreetracenumPos() {
		return lfpos_get_free_trace_num();
	}
	/**
	 * 检测POS现在的状态是否可以执行消费、查余额、圈存等交易指令
	 * @param 
	 * @return 返回 0 pos已经准备好了可执行交易指令，返回非0 pos还未准备好执行交易指令
	 */
	public int checkready2transactionPos() {
		return lfpos_check_ready2_transaction();
	}
	/**
	 * 预授权申请
	 * @param LFPOS_Aut_Param object
	 * @return
	 */
	public int preauthorizationreqPos(LFPOS_Aut_Param param) {
		return lfpos_preauthorization_req(param.setNativeObject());
	}
	/**
	 * 预授权确定
	 * @param LFPOS_Aut_Param object
	 * @return
	 */
	public int preauthorizationsurePos(LFPOS_Aut_Param param) {
		return lfpos_preauthorization_sure(param.setNativeObject());
	}
	/**
	 * 预授权取消
	 * @param LFPOS_Aut_Param object
	 * @return
	 */
	public int preauthorizationcancelPos(LFPOS_Aut_Param param) {
		return lfpos_preauthorization_cancel(param.setNativeObject());
	}
	
	/**
	 * M1 寻卡
	 * @param 
	 * @return
	 */
	public int searchcard() {
		return lfpos_searchcard();
	}
	/**
	 * M1 读扇区
	 * @param LFPOS_MifareCard Object 
	 * @return
	 */
	public int readsectorPos(LFPOS_MifareCard param) {
		return lfpos_readsector(param.setNativeObject());
	}
	/**
	 * M1 写扇区
	 * @param LFPOS_MifareCard Object
	 * @return
	 */
	public int writesectorPos(LFPOS_MifareCard param) {
		return lfpos_writesector(param.setNativeObject());
	}
	/**
	 * 关闭
	 * @param 
	 * @return
	 */
	public int closePos() {
		return lfpos_close();
	}
	
	/**
	 * Wangrm 增加	, 用于实时检测底层库是否调用了回调函数
	 * @param 
	 * @return
	 */
	public void threadloop() {
		thread_loop();
		return;
	}
	/////////--------------------------------------------------------------------/////////////
	
	
	///////////////// 朗方JNI接口 ////////////////////////////////////////////////
	// 1.初始化
	//  成功调用此函数后，才可以调用以下各个指令及其它函数
	//不发生回调
	private native int lfpos_init( int InitParamObject );
	// 2.发起签到（为所有商户作签到）
	//  POS终端将签到POS终端内部预配置的每组商户号∕终端号。
	//  通过回调返回每组的签到结果。
	//最后一次回调时必须把 fEnd 字段设为 TRUE。
	private static native int lfpos_sign_in();
	// 3.发起结算（为所有商户作结算）
	//  POS终端将结算POS终端内部预配置的每组商户号∕终端号。
	//  通过回调返回每组的结算结果。
	//最后一次回调时必须把 fEnd 字段设为 TRUE。
	private static native int lfpos_settle();
	// 4.发起消费
	//  通过回调在读银行卡后、执行消费前返回银行卡号。
	//  通过回调返回消费的执行结果。
	//电子现金卡不需输入密码。
	private static native int lfpos_trade(int TradeParamObject);
	// 5.发起消费撤销
	// 注意：只撤销尚未结算的某次成功完成的消费。
	//   通过回调返回撤销的执行结果。
	private static native int lfpos_revoke( int RevokeParamObject );
	// 6.发起余额查询
	private native int lfpos_query();
	// 7.发起圈存
	// @ uPayment [in]: 圈存金额，取值不能为0，以人民币分为单位
	private native int lfpos_load( int uPayment );
	// 8.取消消费或余额查询
	// 不发生回调
	// 注意：仅在发出上述指令但尚未读银行卡时有效，否则
	//   返回错误代码LFPOS_E_UNEXPECTED
	private native int lfpos_cancel();
	// 9.获取POS终端状态
	//  用于现场问题的诊断
	//不发生回调
	//@ out [out]: 输出结果
	private native int lfpos_get_state( int OutStateObject );
	// 10.复位POS终端
	// 不发生回调
	private native int lfpos_reset();
	// 11.获取卡号信息
	private native int lfpos_read_cardno( int ReadCardnoParamObject );
	// 12.获取POS终端程序版本信息
	private native int lfpos_get_pos_version( int VersionParamObject );
	// 13.升级程序
	private native int lfpos_upgrade( int UpgradeParamObject );
	// 14.停止升级程序
	private native int lfpos_stop_upgrade();
	// 15.获取POS终端剩余空间可存储流水条数
	private native int lfpos_get_free_trace_num();
	// 16.检测POS现在的状态是否可以执行消费、查余额、圈存等交易指令
	//  返回 0 pos已经准备好了可执行交易指令，返回非0 pos还未准备好执行交易指令
	private native int lfpos_check_ready2_transaction();
	// 17.预授权申请
	private native int lfpos_preauthorization_req(int AutParamObject);
	// 18.预授权确定
	private native int lfpos_preauthorization_sure(int AutParamObject);
	// 18.预授权取消
	private native int lfpos_preauthorization_cancel(int AutParamObject);
	//19.M1 寻卡
	private native int lfpos_searchcard();
	//20.M1 读扇区
	private native int lfpos_readsector(int MifareCardObject);
	//21.M1 写扇区
	private native int lfpos_writesector(int MifareCardObject);
	// 19.关闭
	private native int lfpos_close();
	
	//20. Wangrm 增加	, 用于实时检测底层库是否调用了回调函数
	private native void thread_loop();
	
	//////////////////////////////////////////////////////////////////////////////////
	
//	作为JNI回调的接口
//	typedef BOOL (API_TYPE *LFPOS_CALLBACK)( LFPOS_RTNHDR const *ret, size_t ret_size );
	private static boolean lfposCallback(int event, int errorCode, String message) {
		if(getInstance().listener != null) {
			getInstance().listener.onTradeResult(event, errorCode, message);
			return false;
		}
		return true;
	}
	
	
	public interface ResultListener {
		int onTradeResult(int event, int errorCode, String message);
	}
}
