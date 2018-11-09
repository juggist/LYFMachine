package com.lf.pos;


//初始化数据(不包含回调函数)
//当字段值不属于以下的有效值时，返回错误代码LFPOS_E_INVLD_PARM
public class LFPOS_Init_Param {
	private int InitParamObject;
	
	// 连接POS终端的通讯类型，支持以太网和串口
    // 0--串口
    // 1--网络口
	private int comm_type;
	// 连接POS终端的端口号,由comm_type决定是串口号还是网络通讯的socket
    // 串口号:（波特率恒为115200）有效取值：0～15
    // socket: 已经成功建立连接的socket id
	private int  comm_port;
	// 银联前置机IP地址
    // 连接银联的方式为直接连接方式1时有效
	// 有效时不能为空串
	private String unionpay_ip;
	// 银联前置机端口
    // 连接银联的方式为直接连接方式1时有效
	// 有效时不能小于1024
	private int unionpay_port;
	// GPRS业务参数设置
	private String unionpay_apn;
	// CDMA用户名
	private String unionpay_name;
	// CDMA用户密码
	private String unionpay_pwd;
	// 未读到银行卡的超时，单位为秒
	//   超时发生时，已下发的要求读银行卡的指令自动被取消
	// 有效取值：0 或 30～300
	// 为 0 时使用默认值 60
	private int timeout_card;
	// 已读银行卡、但未输入密码的超时，单位为秒
	//   超时发生时，已下发的指令自动被取消
	// 有效取值：0 或 30～300
	// 为 0 时使用默认值 60
	// 在不要求输入密码的场合(如电子现金卡)，此项设置无意义
	private int timeout_pwd;
	// 连接银联的方式(支持以下方式)
	// 有效取值：
	//   0－直接方式1：通过网络模块，直接与银联交互，使用动态库设置的银联前置机的IP地址和端口
	//   1－间接方式1：通过串口，由上位机的动态库双向转发，使用动态库设置的银联前置机的IP地址和端口
	//   2－直接方式2：通过网络模块，直接与银联交互，使用POS终端内部设置的银联前置机的IP地址和端口
	//   3－间接方式2：通过串口，由上位机的动态库双向转发，使用POS终端内部设置的银联前置机的IP地址和端口
	//   4－借助方式：上位机借助POS终端的通讯模块和后台服务器进行通讯(上位机需给出IP和端口)
	private int link_mode;
	// 是否输出内部诊断信息到文本文件
	//    注意，诊断信息应输出POS终端软件的版本号
	//          还应输出各次函数调用的输入参数内容
	// 有效取值：
	//   0－不输出
	//   1－输出到文件
	// 输出文本文件名为 lfpos_api.log ，位于动态库所在的目录
	// 仅用于调试，正常运行时不必设置此项
	private int diagnosis;
	// 是否允许进入管理模式
    // 用于POS终端出厂首次设置或后续管理维护
    // 正常运营时不应允许POS终端进入管理模式
	// 有效取值：
	//   0－不允许
	//   1－允许
	private int manager;
	// 打印票据的方式（支持以下方式）
	//   0－不打印
	//   1－POS打印
	//   2－上位机打印
	private int print_mode;
	// 闪付余额不足的处理方式
	//   0－自动转换成借贷记账户联机交易
	//   1－不转换成借贷记账户联机交易
	private int low_balance;
	// 签到模式
	//   0－不自动签到
	//   1－自动签到
	private int signin_mode;
	
	// POS是否上送POS操作事件,
	// 上位机不处理POS操作事件，并且POS有液晶时可设置不产生POS操作事件。
	//   0－产生POS操作事件
	//   1－不产生POS操作事件
	private int operation_event_mode;
	
	// 结算模式
    // 配置说明见结构定义
    LFPOS_Settle_Mode settle_mode;
	
	public LFPOS_Init_Param() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		InitParamObject = createInitParamObject();
		settle_mode = new LFPOS_Settle_Mode();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		settle_mode.finalize();
		destroyInitParamObject(InitParamObject);
		super.finalize();
	}
	
	public void setCommType(int comm_type) {
		this.comm_type = comm_type;
	}
	
	public void setCommPort(int comm_port) {
		this.comm_port = comm_port;
	}
	
	public void setUnionpayIp(String unionpay_ip) {
		this.unionpay_ip = unionpay_ip;
	}
	
	public void setUnionpayPort(int unionpay_port) {
		this.unionpay_port = unionpay_port;
	}
	
	public void setUnionpayApn(String unionpay_apn) {
		this.unionpay_apn = unionpay_apn;
	}
	
	public void setUnionpayName(String unionpay_name) {
		this.unionpay_name = unionpay_name;
	}
	
	public void setUnionpayPwd(String unionpay_pwd) {
		this.unionpay_pwd = unionpay_pwd;
	}
	
	public void setTimeoutCard(int timeout_card) {
		this.timeout_card = timeout_card;
	}
	
	public void setTimeoutPwd(int timeout_pwd) {
		this.timeout_pwd = timeout_pwd;
	}
	
	public void setLinkMode(int link_mode) {
		this.link_mode = link_mode;
	}
	
	public void setDiagnosis(int diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	public void setManager(int manager) {
		this.manager = manager;
	}
	
	public void setPrintMode(int print_mode) {
		this.print_mode = print_mode;
	}
	
	public void setLowBalance(int low_balance) {
		this.low_balance = low_balance;
	}
	
	public void setSigninMode(int signin_mode) {
		this.signin_mode = signin_mode;
	}
	
	public void setOperationEventMode(int operation_event_mode) {
		this.operation_event_mode = operation_event_mode;
	}
	
	public void setSettleMode(int settle_mode) {
		this.settle_mode.setSettleMode(settle_mode);
	}
	
	public void setSettleTime(int[] settle_time) {
		this.settle_mode.setSettleTime(settle_time);
	}
	
	public int setNativeObject() {
		settle_mode.setNativeObject();
		saveInitParamObject(this, InitParamObject);
		return InitParamObject;
	}
	private static native void saveInitParamObject(LFPOS_Init_Param param, int address);
	private native static int createInitParamObject();
	private native static void destroyInitParamObject(int object);
}
