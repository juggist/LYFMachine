package com.icofficeapp.control;//package com.icofficeapp.control;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import CofficeServer.PayType;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.avm.serialport_142.MainHandler;
//import com.avm.serialport_142.service.CommService;
//import com.avm.serialport_142.service.CommServiceThread;
//import com.avm.serialport_142.utils.Avm;
//import com.icoffice.library.bean.db.AdStatusBean;
//import com.icoffice.library.handler.BaseViewHandler;
//import com.icoffice.library.moudle.control.BaseMachineControl;
//import com.icoffice.library.moudle.control.BasePayControl;
//import com.icoffice.library.moudle.control.CashETPayControl;
//import com.icoffice.library.utils.FileUtil;
//import com.icoffice.library.utils.GoodsWayUtil;
//import com.icoffice.library.utils.MachineUtil;
//import com.icoffice.library.utils.RootUtil;
////import com.icofficeapp.util.BitmapUtil;
//import com.icoffice.library.utils.CommonUtils;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//public class MachineControl extends BaseMachineControl {
//
////	private static String LOG_TAG = "iCofficeApp";
//
//	private static MachineControl _instance = null;
//
////	public boolean respondButton = true;// 判断是否是相应物理按钮跳转内页
////	public int AdTimer;// 当前跳回广告为到倒计时
////	public static int AdLastTimer = Integer.MAX_VALUE;// 广告回跳最大延时，默认为不跳回广告
////	public static int AdStartTimer = 60;// 广告回跳开始倒计时
//
////	public BitmapUtil mBitmapUtil;
////	public FileUtil mFileUtil;
//	private String _serialNumber = "";//3位流水号
//	
//
//	private Handler mMachineControlHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			
//			switch(msg.what){
//			case 1:
//				activate(true);
//				break;
//			}
//			super.handleMessage(msg);
//		}
//		
//	};
//	private CommService _commService = new CommService() {
//
//		@Override
//		public void result(int res) {
//			// TODO Auto-generated method stub
//			if (CommService.ERROR_SYSTEM_SERVICE == res) {
//				Toast.makeText(mContext, "system service error：串口通讯不通或者网络异常",
//						Toast.LENGTH_LONG).show();
//			} else if (CommService.ERROR_SYSTEM_TIME == res) {
//				Toast.makeText(mContext, "system time error：系统时间错误", Toast.LENGTH_LONG)
//						.show();
//			} else if (CommService.ERROR_CODE_NO_EXIST == res) {
//				Toast.makeText(mContext, "code not exist：激活码不存在", Toast.LENGTH_LONG)
//						.show();
//			} else if (CommService.ERROR_SYSTEM_CODE == res) {
//				Toast.makeText(mContext, "code error：激活码错误", Toast.LENGTH_LONG)
//						.show();
//			} else if (CommService.ERROR_ACTIVATE_CHECK == res) {
//				Toast.makeText(mContext, "check error", Toast.LENGTH_LONG)
//						.show();
//			} else if (CommService.ERROR_CODE_USED == res) {
//				Toast.makeText(mContext, "code is used：激活码已使用", Toast.LENGTH_LONG)
//						.show();
//			} else if (CommService.ERROR_OTHER == res) {
//				Toast.makeText(mContext, "activate error：未知激活错误",
//						Toast.LENGTH_LONG).show();
//			} else if (CommServiceThread.ERROR_IO_PROBLEM == res) {
//				Toast.makeText(mContext, "serial error：io", Toast.LENGTH_LONG)
//						.show();
//			} else if (CommServiceThread.ERROR_NOT_CONFIG == res) {
//				Toast.makeText(mContext, "aserial error：no config",
//						Toast.LENGTH_LONG).show();
//			} else if (CommServiceThread.ERROR_PERMISSION_REJECT == res) {
//				Toast.makeText(mContext, "serial error：no permission",
//						Toast.LENGTH_LONG).show();
//			} else if (CommServiceThread.ERROR_UNKNOWN == res) {
//				Toast.makeText(mContext, "serial error：unknown",
//						Toast.LENGTH_LONG).show();
//			} else {
//				Toast.makeText(mContext, "activate and start success：连接成功",
//						Toast.LENGTH_LONG).show();
//				CommonUtils.showLog(CommonUtils.TAG,"iCofficeApp init SMC");
//				_isSinglechipStartSuccess = true;
//				checkSinglechipInitStatus();
//				refreshCloseDoorStatus();
//			}
//		}
//	};
//
//	private MachineControl(Context context) {
//		super(context);
//		initActivity();
//	}
//	void initActivity(){
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(10000);
//					while (!checkNet(mContext)) {
//						CommonUtils.showLog(CommonUtils.TAG,"connect fail");
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//					CommonUtils.sendMessage(mMachineControlHandler, 1, "");
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
//	public void activate(boolean retryWhenFail) {
//		if (!_isSinglechipStartSuccess) {
//			int state = MainHandler.load(mContext);
//			if (state == MainHandler.ERROR_NO_SDCARD) {
//				if (retryWhenFail) {
//					activate(false);
//				} else {
//					Toast.makeText(mContext, "Can not find sdcard：找不到sd卡",
//						Toast.LENGTH_LONG).show();
//					CommonUtils.showLog(CommonUtils.TAG, "Can not find sdcard");
//					exitApp(true);
//				}
//			} else if (state == MainHandler.ERROR_EMPTY_DATA) {
//				if (retryWhenFail) {
//					//尝试复制assets文件夹
//					File sdFiles = mContext.getExternalFilesDir(null);
//					String setDir = sdFiles.getParent()+"/set";
//					FileUtil.CopyAssets(mContext, "set", setDir);
//					activate(false);
//				} else {
//					Toast.makeText(mContext, "Data read error：配置文件读取错误", Toast.LENGTH_LONG)
//					.show();
//					CommonUtils.showLog(CommonUtils.TAG, "Data read error");
//					exitApp(true);
//				}
//			} else if (state == MainHandler.ERROR_NET_NOT_AVAILABLE) {
//				Toast.makeText(mContext, "net error：网络错误", Toast.LENGTH_LONG).show();
//				CommonUtils.showLog(CommonUtils.TAG, "net error");
//			} else {
//				// 激活并打开驱动程序
//				_commService.connect(mContext, "549783692484", 0);
//			}
//		}
//	}
//
//	public static MachineControl getInstance(Context mContext) {
//		if (_instance == null)
//			_instance = new MachineControl(mContext);
//		return _instance;
//	}
//
//
//	
//	
//	@Override
//	protected void outGoods(BasePayControl basePayControl) {
//
//		// 出货指令：15位字符串，具体分别为：
//		// 2货道+1出货方式（1或2，目前使用1）+2货柜+8金额（分为单位）+2出货代码
//		String way = GoodsWayUtil.getW_code(basePayControl.getWayID());
//		
//		ArrayList<String> list = GoodsWayUtil.parseWayId(way);
//		String box = list.get(0), road = list.get(1);
//		String outInfo = box + "1" + road
//				+ String.format("%08d", basePayControl.getPrice());
//		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "执行出货指令: 指令信息 = " + "way_id = " + basePayControl.getWayID()+ ";outInfo = " + outInfo);
//				
//		switch (PayType.valueOf(basePayControl.getPayType())) {
//
//		case cash:
//			outInfo += Avm.OUT_GOODS_CASH;
//			break;
//		case unionpay:
//			outInfo += Avm.OUT_GOODS_UNIONPAY;
//			break;
//		case ali:
//			outInfo += Avm.OUT_GOODS_ALIPAY;
//			break;
//		case wechat:
//			outInfo += Avm.OUT_GOODS_WEIXINPAY;
//			break;
//		default:
//			outInfo += Avm.OUT_GOODS_TIHUO;
//			break;
//		}
//		
//		// 生成6位流水并保存到支付对象中
//		SimpleDateFormat sd = new SimpleDateFormat("HHmmss", Locale.US);
//		final String tradeTrace = sd.format(new Date());
//		basePayControl.setTradeTrace(tradeTrace);
//		
//		
//
//		// 请求出货
//		boolean success = true;
//		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "请求出货: " + "outInfo=" + outInfo + "; tradeTrace=" + tradeTrace);
//		if(MainHandler.noticeAvmOutGoods(outInfo, tradeTrace)){
//			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "请求出货 指令发送状态_01 = true" );
//		}else{
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(MainHandler.noticeAvmOutGoods(outInfo, tradeTrace)){
//				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "请求出货 指令发送状态_02 = true" );
//			}else{
//				success = false;
//				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "请求出货 指令发送状态_02 = false" );
//			}
//		}
//		
//		// 发送出货中指令
//		final Message msg = new Message();
//		msg.what = BaseViewHandler.OutGoods;
//		mViewHandler.sendMessage(msg);
//		if(basePayControl.getPayType() == PayType.ali.value() || basePayControl.getPayType() == PayType.wechat.value() || basePayControl.getPayType() == PayType.card.value()){
//			if (!success) {
//				msg.what = BaseViewHandler.OutGoodsFail;
//				outGoodsFinish(msg.what, success, tradeTrace);
//			}else{
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						try {
//							Thread.sleep(15000);
//							msg.what = BaseViewHandler.OutGoodsFail;
//							outGoodsFinish(msg.what, false, tradeTrace);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}// TODO Auto-generated method stub
//						
//					}
//				}) .start();
//			}
//		}
//	}
//
//	@Override
//	protected void coinInit() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void coinConfirm(boolean confirm) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void coinStatus() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void coinOpen(int CFlag, int BFlag) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void coinReturn(double amount) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void coinQty() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public String getFactoryCode() {
//		String machineNo = "";
//		try {
//			machineNo = MainHandler.getMachNo();
//		} catch (Exception e) {
//			machineNo = "00000000"; // 异常的机器编码
//		}
//		return machineNo;
//	}
//
//	private List<String> getTemperature(String box) {
//		String temperature = "";
//		List<String> ret = new ArrayList<String>();
//
//		if (!_isSinglechipStartSuccess) {
//			return ret;
//		}
//
//		try {
//			// 8售卖配置（后两位有效）+ 2照明状态+ 8日光灯时间 +2左室状态+2右室状态+8节能时间 【此行共30位】
//			// +2制冷温度（带符号，该温度-4：机器关闭制冷，该温度+2，机器开启制冷）+2左室实际温度（带符号）【此行共6位】
//			// +2进入加热温度（带符号，该温度+3：机器关闭制热，该温度-3，机器开启制热）+2右室实际温度（带符号）【此行共6位】
//			String config = MainHandler.getAVMConfigInfo(Integer.parseInt(box));
//			temperature = config.substring(30, 33); // 进入制冷温度
//			ret.add(temperature);
//			temperature = config.substring(33, 36); // 左室实际温度
//			ret.add(temperature);
//			temperature = config.substring(36, 39); // 进入加热温度
//			ret.add(temperature);
//			temperature = config.substring(39); // 右室实际温度
//			ret.add(temperature);
//
//			CommonUtils.showLog(CommonUtils.TAG, "config=" + config);
//			CommonUtils.showLog(CommonUtils.TAG, "temperature=" + temperature);
//		} catch (Exception e) {
//
//		}
//
//		return ret;
//	}
//
//	@Override
//	public List<String> getTemperature() {
//		List<String> ret = new ArrayList<String>();
//
//		if (!_isSinglechipStartSuccess) {
//			return ret;
//		}
//
//		ret.addAll(getTemperature(GoodsWayUtil.FOODS_BOX_ID));
//		ret.addAll(getTemperature(GoodsWayUtil.DRINKS_BOX_ID));
//
//		return ret;
//	}
//
//	@Override
//	public List<String> getCoinNotesStatus() {
//		List<String> ret = new ArrayList<String>();
//		String coinNotesStatus = "1";
//		
//		if (!_isSinglechipStartSuccess) {
//			return ret;
//		}
//
//		String quantity50 = "";
//		String quantity100 = "";
//		String quantityNotes = "";
//		try {
//			// 00000000 00001000 00000001 00000001 0000 0000 0000
//			// 8运行状态（后两位有效）+8售货机状态+8纸币器状态+8硬币器状态
//			// +8硬币器检测到的硬币数量（4位0.5元+4位1元）+4纸币数量
//			String info = MainHandler.getMachRunInfo();
//			String coinInfo = info.substring(16, 24);
//			String notesInfo = info.substring(24, 32);
//			quantity50 = info.substring(32, 36);
//			quantity100 = info.substring(36, 40);
//			quantityNotes = info.substring(40, 44);
//			CommonUtils.showLog(CommonUtils.TAG,  "info=" + info);
//			CommonUtils.showLog(CommonUtils.TAG,  "coinInfo=" + coinInfo);
//			CommonUtils.showLog(CommonUtils.TAG,  "notesInfo=" + notesInfo);
//			coinNotesStatus = Integer.valueOf(notesInfo + coinInfo, 2) + "";
//		} catch (Exception e) {
//
//		}
//		CommonUtils.showLog(CommonUtils.TAG, "coinNotesStatus = " + coinNotesStatus);
//		ret.add(coinNotesStatus);
//		ret.add(quantity50);
//		ret.add(quantity100);
//		ret.add(quantityNotes);
//		return ret;
//	}
//
//	@Override
//	public boolean checkRoad(String wayId) {
//		if (!_isSinglechipStartSuccess || wayId == null || wayId.equals("")) {
//			return false;
//		}
//		ArrayList<String> list = GoodsWayUtil.parseWayId(wayId);
//		String box = list.get(0), road = list.get(1);
//		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"checkRoad:box_02 = " + box + ";road_02 = " + road);
//		// 1位描述(0有货，1无货,2故障,9数据异常)+2位商品编号(EE表示不启用)+6位金额0-65535以分为单位
//		String info = MainHandler.getGoodsInfo(Integer.parseInt(box),
//				Integer.parseInt(road));
//		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"checkRoad:info = " + info);
//		boolean noGoods = info.substring(0, 1).equals("0");
//		boolean isValid = info.substring(1, 3).equals("EE");
//		return noGoods;
//	}
//
//	@Override
//	@SuppressLint("DefaultLocale")
//	public boolean setRoad(final Handler handler, final String wayId,
//			final boolean isValid, final int price, final int quantity) {
//		if (!_isSinglechipStartSuccess) {
//			return false;
//		}
//
//		ArrayList<String> list = GoodsWayUtil.parseWayId(wayId);
//		final String box = list.get(0), road = list.get(1);
//		String info = String.format("%02d%02d%02x%04d", Integer.parseInt(box),
//				Integer.parseInt(road),
//				isValid ? Integer.parseInt(road) : 0xEE, price / 10);
//		// 参数：2(货柜)+2(料道)+2(商品序号16进制)(EE表示不启用)+4(商品价格，以角为单位)
//		MainHandler.setRoad(info);
//
//		info = String.format("%02d%02d%02d", Integer.parseInt(box),
//				Integer.parseInt(road), quantity);
//		// 参数：2(货柜)+2(料道)+2(商品数量)
//		MainHandler.setAddGoods(info);
//
//		// 启动线程检测设置结果
//		new Thread() {
//			@Override
//			public void run() {
//				super.run();
//				try {
//					// 因设置需经过时间，在调用上面的方法后，要在500到800毫秒后查询
//					Thread.sleep(1200);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//				// 返回：0，设置成功；1，设置未完成；其他，设置失败
//				int roadRst = MainHandler.getRtSetRoad();
//				int goodsRst = MainHandler.getRtAddGoods();
//
//				Message msg = new Message();
//				if (roadRst == 0 && goodsRst == 0) {
//					msg.what = BaseViewHandler.SetRoadSuccess;
//				} else {
//					msg.what = BaseViewHandler.SetRoadFail;
//				}
//				ArrayList<Object> args = new ArrayList<Object>();
//				args.add(wayId);
//				args.add(isValid);
//				args.add(price);
//				args.add(quantity);
//				msg.obj = args;
//				CommonUtils.showLog(CommonUtils.SCM_TAG,"setRoad:wayId = "
//						+ wayId + ";isValid = " + isValid + ";price = " + price
//						+ ";quantity = " + quantity);
//				handler.sendMessage(msg);
//			}
//		}.start();
//		return true;
//	}
//
//	@Override
//	public String getMachineMac() {
//		// TODO Auto-generated method stub
//		return MachineUtil.getLocalMacAdress(mContext);
//	}
//
//	@Override
//	public String getM_No() {
//		return mMachineBean.getM_no();
//	}
//
//	@Override
//	public void checkOutGoodsResult() {
//		// 默认情况下，在未收到出货结果信息时，该方法只返回"-1",在收到出货信息后，返回：
//		// 2货柜号+2货道号+2商品编号（16进制）+6售卖金额（分为单位）+3机器流水+
//		// 2支付方式代码+1位出货结果（出货信息码为0时，表示出货成功，其他表示出货失败，
//		// 其中返回4，表示现金和刷卡支付时的支付失败）+20卡号（除了一卡通外，无卡号时补0）
//		// +6提交的流水号+8销售的总数量+10销售的总金额。
//		
//		String outGoodsRst = MainHandler.getTranResult();
//		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"检测出货结果 = " + outGoodsRst);
//		String serialNumber = outGoodsRst.substring(12, 15);
//		if(serialNumber.equals(_serialNumber)){
//			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "serialNumber 流水号相同 = " + serialNumber);
//			return;
//		}else{
//			_serialNumber = serialNumber;
//		}
//		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"检测出货结果 3位流水号= " + _serialNumber);
//		String result = "-1";
//		if (outGoodsRst != "-1") {
//			try {
//				result = outGoodsRst.substring(17, 18);
//			} catch (Exception e) {
//				e.printStackTrace();
//				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"checkOutGoodsResult发现异常数据:" + outGoodsRst);
//				return;
//			}
//		}
//
//		boolean isSuccess = true;
//		Message msg = new Message();
//		if ("0".equals(result)) {
//			msg.what = BaseViewHandler.OutGoodsSuccess;
//		} else {
//			isSuccess = false;
//			msg.what = BaseViewHandler.OutGoodsFail;
//		}
//		String box = "", road = "", goodsNo = "", price = "";
//		String payType = "", cardNo = "", tradeTrace = "";
//		box = outGoodsRst.substring(0, 2);
//		road = outGoodsRst.substring(2, 4);
//		goodsNo = outGoodsRst.substring(4, 6);
//		price = outGoodsRst.substring(6, 12);
//		payType = outGoodsRst.substring(15, 17);
//		cardNo = outGoodsRst.substring(18, 38);
//		tradeTrace = outGoodsRst.substring(38, 44);
//		if (outGoodsFinish(msg.what, isSuccess, tradeTrace)) {
//
//		} else {
//			// 出货结果未被处理，属于物理按钮出货
//			outGoodsBypass(msg.what,isSuccess, box, road, price, payType, cardNo);
//		}
//	}
//
//	@Override
//	public void refreshCloseDoorStatus() {
//		setCloseDoorStatus(!MainHandler.isDoorOpen());
//	}
//
//	@Override
//	public List<String> checkSelectInfo() {
//		List<String> ret = new ArrayList<String>();
//		String selectInfo = MainHandler.getSelectInfo();
//		ret.add(selectInfo.substring(0, 2));
//		ret.add(selectInfo.substring(2, 4));
//		ret.add(selectInfo.substring(6, 12));
//		return ret;
//	}
//
//	@Override
//	public boolean getAvmStatus() {
//		boolean avmStatus = MainHandler.isAvmRunning();
//		return avmStatus;
//	};
//
//	@Override
//	public void refreshReceivedMoney(int money) {
//		if (money > _receivedMoney) {
//			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"收币，当前金额：" + money);
//			// 投币
//			if(mCurrentPayControl != null)
//				if (PayType.valueOf(mCurrentPayControl.getPayType()) == PayType.cash) {
//					CashETPayControl payControl = (CashETPayControl) mCurrentPayControl;
//					payControl.cashIn(money);
//					CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"现金支付收币，当前金额：" + money);
//				}
//		} else {
//			// 退币或消费
//			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"退币或消费，当前金额：" + money);
//		}
//
//		super.refreshReceivedMoney(money);
//	}
//	
//}
