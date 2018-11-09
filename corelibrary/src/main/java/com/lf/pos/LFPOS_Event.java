package com.lf.pos;

//事件代码
//有两类事件，见下面的注释
public class LFPOS_Event {
	public static final int LFPOS_EVT_NONE = 0;  // 无效值，不应被使用

	// 银联事件: 与银联交互得到结果
	//   冲正和批上送由POS终端自动发起，这里通过回调来通知上层应用
	//   当操作成功时，返回对应的 LFPOS_xxx_RESULT 结构，而 desc 字段为NULL
	//     例外是 LFPOS_EVT_TRADE ，desc 字段实际上不作为指针，而是相当于
	//       LFPOS_ACCOUNT 枚举值，决定返回的是哪个 LFPOS_TRADE_xxx_RESULT 结构
	//   当操作失败时，仅返回 LFPOS_RTNHDR 结构，银联的错误信息由 desc 字段给出

	public final int LFPOS_EVT_SIGNIN = 1;    // 签到 (Sign In)
	public final int LFPOS_EVT_SETTLE = 2;   // 结算 (Settle)
	public final int LFPOS_EVT_REVERT = 3;    // 冲正 (Revert)
	public final int LFPOS_EVT_BATCHUP = 4;   // 批上送 (Batch Upload)
	public final int LFPOS_EVT_TRADE = 5;     // 消费 (Trade)
	public final int LFPOS_EVT_REVOKE = 6;    // 消费撤销 (Revoke)
	public final int LFPOS_EVT_QUERY = 7;     // 余额查询 (Query)
	public final int LFPOS_EVT_LOAD = 8;      // 圈存(Load)

	// 终端事件: 需要向顾客显示必要的提示内容
	//   如果未指明返回的结构，则仅返回 LFPOS_RTNHDR 结构

	public final int LFPOS_EVT_FROM_POS = 9;  // 不是事件，只作为两种事件的取值边界
	// POS终端发生故障，故障信息由 desc 字段给出
	public final int LFPOS_EVT_GLITCH = LFPOS_EVT_FROM_POS;
	//
	// 以下属于POS终端正常操作
	//
	// 提示刷磁卡或插入IC卡或挥卡，返回 LFPOS_ASK_CARD_INFO 结构
	public final int LFPOS_EVT_ASK_CARD = 10;
	// 读银行卡失败，返回 LFPOS_BADCARD_INFO 结构
	public final int LFPOS_EVT_BADCARD = 11;
	// 读银行卡成功，尚未消费，返回 LFPOS_GOODCARD_INFO 结构
	public final int LFPOS_EVT_GOODCARD = 12;
	// 提示输入密码，读银行卡成功后，如果需要输入密码，就发生此事件
	//   用户每次按键盘也可能发生此事件，提示当前输入的字符数
	//   返回 LFPOS_ASK_PWD_INFO 结构
	public final int LFPOS_EVT_ASK_PWD = 13;
	// 提示正在处理(与银联交互中)
	public final int LFPOS_EVT_PROCESS = 14;
	// 提示拔出卡片
	//   拔磁卡是为了读卡，拔IC卡是在操作完成后
	//   返回 LFPOS_PULL_OUT_INFO 结构
	public final int LFPOS_EVT_PULL_OUT = 15;
	// 当非指定帐户圈存时，提示读银行卡(转出卡或转入卡)
	//   返回 LFPOS_LOAD_ASK_CARD_INFO 结构
	public final int LFPOS_EVT_LOAD_ASK_CARD = 16;
	// 提示用户按键盘作出选择
	//   这时 desc 字段必须给出各选项的提示内容
	//   用于正常模式或管理模式
	public final int LFPOS_EVT_DIALOG = 17;
	// 提示用户消息（不需作选择）
	//   这时 desc 字段必须给出消息内容
	//   用于上述事件以外的正常模式或管理模式
	public final int LFPOS_EVT_MESSAGE = 18;
	// 读卡号成功，返回 LFPOS_READ_CARDINFO_RESULT 结构
	public final int LFPOS_EVT_CARDINFO = 19;
	//
	// 以下属于POS终端维护操作
	//
	// 获取版本的事件
	//   返回 LFPOS_VERSION_RESULT 结构
	public final int LFPOS_EVT_VERSION = 20;
	// 执行更新的事件
	//   返回 LFPOS_UPGRADE_RESULT 结构
	public final int LFPOS_EVT_UPGRADE = 21;
	// POS终端状态事件
	//   POS终端切换状态产生的事件
	//   返回 LFPOS_STATE_INFO 结构
	public final int LFPOS_EVT_STATE = 22;
	// 获取POS终端剩余空间可存储流水条数返回的事件
	//   返回 LFPOS_FREETRACE_INFO 结构
	public final int LFPOS_EVT_FREETRACE = 23;
	// 预授权 申请
	public final int LFPOS_EVT_AUT_REQ = 24;
	// 预授权 撤销
	public final int LFPOS_EVT_AUT_CANCEL = 25;
	// 预授权 确定
	public final int LFPOS_EVT_AUT_SURE = 26;
	//M寻卡
	public final int LFPOS_EVT_M1_SEARCH = 27;
	//M1读扇区
	public final int LFPOS_EVT_M1_READSECTOR = 28;
	//M1 写扇区
	public final int LFPOS_EVT_M1_WRITESECTOR = 29;
}