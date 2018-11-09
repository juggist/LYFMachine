package com.lf.pos;

public class LFErrorCode {
	public final int LFPOS_E_SUCCESS = 0; // 无错误
	public final int LFPOS_E_UNKNOWN = 1; // 未知的错误（尽量不要使用这个错误代码）
	public final int LFPOS_E_NOT_INIT = 2; // 尚未调用初始化函数
	public final int LFPOS_E_INVLD_PARM = 3; // 非法参数
	public final int LFPOS_E_UNEXPECTED = 4; // 调用函数的时机不当
	public final int LFPOS_E_COMM_DOWN = 5; // 上位机和POS终端之间通讯异常
	public final int LFPOS_E_NOT_SIGNIN = 6; // POS没有签到
	// 银联的标准错误代码 = LFPOS_E_UNIONPAY + 响应报文中的错误代码的字符的ASCII值
	// 例如银联返回错误代码为"T2"，那么这里的错误代码将是0x15432
	public final int LFPOS_E_UNIONPAY = 0x10000;

	// POS终端返回的错误代码
	// 例如POS终端返回错误代码为"R1"，那么这里的错误代码将是0x25231
	public final int LFPOS_E_VENDOR = 0x20000;
	// POS终端相关
	public final int LFPOS_E_SHOP_ID = 0x25131; // 商户号错误（未设置或不匹配）
	public final int LFPOS_E_POS_ID = 0x25132; // 终端号错误（未设置或不匹配）
	public final int LFPOS_E_AMOUNT = 0x25133; // 交易金额错误（值为零）
	public final int LFPOS_E_PIN_KEY = 0x25134; // 密钥错误（未设置或不匹配）
	public final int LFPOS_E_BREACHED = 0x25135; // POS终端被拆解过
	public final int LFPOS_E_VOUCHER = 0x25136; // 消费撤销时，未找到凭证号对应的交易
	public final int LFPOS_E_BANK_CARD = 0x25137; // 消费撤销时，读取的银行卡号和原交易不一致
	public final int LFPOS_E_LOW_BALANCE = 0x25138; // 脱机交易中，余额不足
	// 银联交互相关
	public final int LFPOS_E_CONNECT = 0x25231; // 连接银联服务器失败
	public final int LFPOS_E_TRANSMIT = 0x25232; // 向银联发送报文时，网络异常
	public final int LFPOS_E_RECEIVE = 0x25233; // 从银联接收报文时，网络异常
	public final int LFPOS_E_PARSE = 0x25234; // 接收的报文不符合银联规范
	public final int LFPOS_E_NEED_SETTLE = 0x25235; // 需作结算，因为POS终端所存的流水已满
	public final int LFPOS_E_NEED_SIGNIN = 0x25236; // 需作签到，因为POS终端尚未签到
	// 操作相关
	public final int LFPOS_E_WAIT_CARD = 0x25331; // 读卡操作超时（久未读银行卡）
	public final int LFPOS_E_WAIT_PWD = 0x25332; // 输入密码操作超时（久未输入密码）
	public final int LFPOS_E_CANCEL = 0x25333; // 操作被取消（用户按键盘取消键）
	public final int LFPOS_E_READCARD = 0x25334; // 读卡错误
	public final int LFPOS_E_REFUSE = 0x25335; // 交易被上位机拒绝，参见LFPOS_GOODCARD_INFO结构
}
