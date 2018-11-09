
#ifndef COFFICE_SERVER_ICE
#define COFFICE_SERVER_ICE

#include <Ice/BuiltinSequences.ice>
#include <Glacier2/Session.ice>

module CofficeServer
{
	/****************************
	消息定义
	****************************/
	
	//更新APK消息，参数：
	const string MsgNewApkVersion 	= "MSG_NEW_APK_VERSION";
	
	//更新广告消息，参数：
	const string MsgNewAds 			= "MSG_NEW_ADS";

	//配置变更消息，客户端需要重新加载配置，参数：
	const string MsgConfigChanged	= "MSG_CONFIG_CHANGED";

	//重启APP消息，客户端需要重启APP：
	const string MsgRestartApp		= "MSG_RESTART_APP";

	//重启工控机消息，客户端需要重启工控机：
	const string MsgRestartMachine	= "MSG_RESTART_Machine";

	//远程出货消息，客户端需要根据客户端订单出货：
	const string MsgOutGoods		= "MSG_OUT_GOODS";
	
	/****************************
	类型定义
	****************************/
	enum MachineType { UnknownMachine, Drink, Machine };
	enum PayType { UnknownPay, cash, unionpay, wechat, ali, card, exchangepay, yeepay };
	dictionary<string, string> Dict;
	sequence<Dict> Dicts;
	sequence<byte> Bytes;
	
	/****************************
	接口定义
	****************************/
	interface MachineCallback
	{
		// 查询售货机状态
		// options选项：
		// 	basicInfo - 基础信息；
		// 	settingInfo - 设置信息；
		//	sqlQuery - 查询售货机sql
		// 返回值：
		// 
		Dict queryStatus(Dict options);
		
		// 推送消息
		// message参数：消息名称（参见消息定义）
		// options参数：根据消息名称有不同的定义（参见消息定义）
		void pushMessage(string message, Dict options);
		
		// 读取文件
		long getFileLength(string path);
		Bytes readFile(string path, long offset, int length);
		
		// 推送消息，需要返回值
		// message参数：消息名称（参见消息定义）
		// options参数：根据消息名称有不同的定义（参见消息定义）
		Dict sendMessage(string message, Dict options);
		
	};
	
	interface MachineSession extends Glacier2::Session
	{
		void setCallback(MachineCallback* cb);
	
	

//  ---------------------- 1.0.1		
//---------------------------基础服务类
		//得到货品的列表信息 
		//options参数: 
		//	mt_timestamp   	版本号（初次可以传0）
		//结果：Map<String,String>
		//	status			返回状态	42
		//	msg				返回信息
		//	mt_timestamp	版本号
		//	list			货品列表
		Dict queryMachineGoods(Dict options);
		
		
		//心跳及开关门及故障记录
		//options参数: 
		//	network			信号强度
		//	networkModel	信号模式( 2(2) / 3(3g) / 99(宽带))	
		//	-- temperature		温度   已经取消
		//	doorState		开关门状态
		//	coinState		纸硬币器状态
		//  scmState		单片机状态
		//	setTempA		A设定温度(可传)
		//	setTempB		B设定温度(可传)
		//	setTempC		C设定温度(可传)
		//	tempA			A温度(可传)
		//	tempB			B温度(可传)
		//	tempC			C温度(可传)
		//	coin05			0.5元硬币(可传)
		//	coin1			1元硬币(可传)
		//	paperMoney		纸币张数(可传)
		//结果：Map<String,String>
		//	status			返回状态	42
		//	msg				返回信息
		Dict heartBeat(Dict options);
		
		
		
//---------------------------业务支付类(本地支付)
		//现金交易记录提交(并生成订单号) 
		//orders: 由多个order组成
		//	order:
		//		clientOrderNo	客户端订单号
		//		gCode			货品编号
		//		wCode			货道编号
		//		generateTime	下单时间
		//		unitPrice		单价
		//		totalPrice		总价
		//		state			出货状态
		//		cashIn			投币金额
		//		cashOut			退币金额
		//		(以下参数可传)
		//		c1				1元纸币
		//		c10				10元纸币
		//		c20				20元纸币
		//		c5				5元纸币
		//		coin05			0.5元硬币
		//		coin1			1元硬币
		//结果：Map<String,String>
		//	status			返回状态	
		//	msg				返回信息
		//	o_count			成功下单条数
		//	successarr：		成功的订单列表
		//		clientOrderNo	客户端订单号
		//		number			服务器订单号
		Dict createOrderCash(Dicts orders);
		
		
		//银联交易记录提交(并生成订单号) ps:银联特有的属性暂未增加及维护
		//orders: 由多个order组成
		//	order:
		//		clientOrderNo	客户端订单号
		//		gCode			货品编号
		//		wCode			货道编号
		//		generateTime	下单时间
		//		unitPrice		单价
		//		totalPrice		总价
		//		state			出货状态
		//结果：Map<String,String>
		//	status			返回状态	
		//	msg				返回信息
		//	o_count			成功下单条数
		//	successarr：		成功的订单列表
		//		clientOrderNo	客户端订单号
		//		number			服务器订单号
		Dict createOrderUnionPay(Dicts orders);
		
		//客非微商城兑换码兑换商品(并生成订单号) 
		//orders: 由多个order组成
		//	order:
		//		clientOrderNo	客户端订单号
		//		gCode			货品编号
		//		wCode			货道编号
		//		generateTime	下单时间
		//		unitPrice		单价(是否需要设定，还是默认给0)
		//		totalPrice		总价(是否需要设定，还是默认给0)
		//		state			出货状态
		//		sNumber			微商城兑换订单号
		//结果：Map<String,String>
		//	status			返回状态	
		//	msg				返回信息
		//	o_count			成功下单条数
		//	successarr：		成功的订单列表
		//		clientOrderNo	客户端订单号
		//		number			服务器订单号
		Dict createOrderCofficeShop(Dicts orders);
		
		
//---------------------------业务支付类(网络支付)		
		//支付宝下单(并生成订单号) 
		//options参数: 
		//		clientOrderNo  	客户端定义的订单号
		//		gCode			货品编号
		//		dynamic_id		支付宝动态ID
		//结果：Map<String,String>
		//	status			返回状态	61,82,101,102,103,104,105,112,113
		//	msg				返回信息
		//	number			服务器订单号
		//	result_code		支付宝返回码
		//	clientOrderNo  	客户端定义的订单号
		Dict createOrderAlipay(Dict options);
		
		
		//客非储值卡交易记录提交(并生成订单号) 
		//options参数: 
		//		clientOrderNo  	客户端定义的订单号
		//		gCode			货品编号
		//		vcCode			储值卡卡号
		//结果：Map<String,String>
		//	status			返回状态	61,82,605,609,621,622
		//	msg				返回信息
		//	number			服务器订单号
		//	balance			客非卡余额
		//问题:之前验证余额是够的，但是扣款以后余额不足，(还是会拦截)
		Dict createOrderCofficeCard(Dict options);
		

//---------------------------业务支付类(其他)			
		//检查wx订单是否已经完成交易 
		//options参数: 
		//		clientOrderNo  	客户端定义的订单号
		//结果：Map<String,String>
		//	status			返回状态	81,89
		//	msg				返回信息
		//	number			服务器订单号
		//	clientOrderNo  	客户端定义的订单号
		Dict checkWeixinOrderState(Dict options);
	
		
		//检查alipay 支付宝 订单是否已经完成交易 
		//options参数: 
		//		clientOrderNo  	客户端定义的订单号
		//结果：Map<String,String>
		//	status			返回状态	81,89
		//	msg				返回信息
		//	number			服务器订单号
		//	clientOrderNo  	客户端定义的订单号
		Dict checkAlipayOrderState(Dict options);
		
		
		
		//客户端通知服务器 已经完成交易(已经出货) 
		//orders: 由多个order组成
		//	order:
		//		clientOrderNo	客户端订单号
		//		wCode			货道编号
		//		completeTime	下单时间
		//		state			出货状态
		//结果：Map<String,String>
		//	status			返回状态	
		//	msg				返回信息
		//	o_count			成功下单条数
		//	successarr：		成功的订单列表
		//		clientOrderNo	客户端订单号
		Dict orderComplete(Dicts orders);
		
		
//---------------------------基础服务类(涉及主动推送)		
		//apk下载 
		//options参数: 
		//		version_code	当前版本号
		//		mt_id			设备类型
		//结果：Map<String,String>
		//	status			返回状态	21,22,23,98
		//	msg				返回信息
		//	url				apk差异包下载路径
		Dict downloadApk(Dict options);
		
	
		//读取售货机配置参数 (接口暂未开发)
		//machineCode参数：	设备编码 需要验证
		//结果：Dict 
		//  heartbeat_interval： 心跳间隔，秒为单位
		//  ad_show_interval： 广告展示间隔，秒为单位
		//  pay_type_list： 开启的支付方式。以","分隔，支付方式的value为值。例如"1,3,4"为开启现金、微信、支付宝
		Dict getConfig(string machineCode);
		
		
//  ---------------------- 1.1.0		

		//得到广告的列表信息 
		//options参数:预留, 暂时不用 
		//结果：Map<String,String>
		//	status			返回状态	
		//	msg				返回信息
		//	list			广告列表
		Dict queryMachineAd(Dict options);
		
		
		
		
		
		
		
		
//---------------------------以下接口暂不使用，定义规则也不确定		
		//暂不使用
		//用户登录验证
		//options参数: 
		//	u_code 		工号
		//	password 	密码 (注：md5加密一遍)
		//	m_code		注册前可以不传，但是注册完了以后，必须要传
		//结果：JSON String
		//string login(Dict options);
		
		//暂不使用
		//设备注册 (每台设备初次使用前必要的注册工作)  
		//options参数: 
		//	mac  			mac地址
		//	factory_code	设备编码(出厂编码)
		//	mt_id			设备类型
		//结果：JSON String
		//string registerMachine(Dict options);
		
		
		//暂不使用
		//库存更改(补货/出货) 
		//machineCode参数：	设备编码 需要验证
		//options参数: 
		//	records   		机器货品code及数量		'1,8|2,9|3,10|108,15' 
		//					数量为正则是入库/补货, 数量为负则是出货
		//	u_id			操作人员ID
		//	time			补货时间	 不传为系统默认时间
		//结果：JSON String
		//string storeChange(Dict options);
		
		
		//暂不使用
		//售货机货品销售状态调整 
		//machineCode参数：	设备编码 需要验证
		//options参数: 
		//	status			机器货品code，销售状态(1:正常 0:暂停) , 价格(可选) 		
		//					多条以’| ’分割
		//					G0001,1,700| G0002,0
		//结果：JSON String
		//string storeState(Dict options);
		
		
		//暂不使用
		//客户端通知服务器 交易失败(出货失败) 
		//machineCode参数：	设备编码 需要验证
		//options参数: 
		//	o_numbers	订单号
		//		“o_numbers” : “o_number | o_number | … | o_number”
		//结果：JSON String
		//string orderFail(Dict options);
	};
	
	interface ServerManager
	{
		// 查询售货机状态
		// id参数：			机器id
		// options参数：		要查询的状态，可设置：basicInfo（基础信息）、sqlQuery（查询sql）
		//
		// 返回值：
		// 					可能包含的返回值有：apk_ver（apk版本，归属basicInfo）、simInfo（sim卡信息，归属basicInfo）、sqlQuery（sql查询结果，归属sqlQuery）
		// 
		Dict queryMachineStatus(string id, Dict options);
		
		// 推送消息
		// id参数：机器ID
		// message参数：消息名称（参见消息定义）
		// options参数：根据消息名称有不同的定义（参见消息定义）
		Dict pushMachineMessage(string id, string message, Dict options);
		
		// 读取客户端文件，将文件存储到服务端，并将服务端文件路径返回
		// id参数：机器ID
		// path参数：售货机上的文件路径
		//
		// 返回值：文件路径，或者错误信息 
		Dict getFile(string id, string path);
	};
};

#endif