package com.icofficeapp.control;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.icoffice.library.bean.db.LYMachineSetBean;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.moudle.control.BasePayControl;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icoffice.library.utils.MachineUtil;
import com.icofficeapp.handler.BaseHandler;
import com.leiyuntop.Refrigerator.Refrigerator;
import com.leiyuntop.machine.IMachineEventHandler;
import com.leiyuntop.machine.ISCInfo;
import com.leiyuntop.machine.SCCargoRoadStatus;
import com.leiyuntop.machine.SCCoinCollection;
import com.leiyuntop.machine.SCCoinQty;
import com.leiyuntop.machine.SCElevatorStatus;
import com.leiyuntop.machine.SCInit;
import com.leiyuntop.machine.SCPickStatusStatus;
import com.leiyuntop.machine.SCSale;
import com.leiyuntop.machine.SCSaleState;
import com.leiyuntop.machine.SCState;
import com.leiyuntop.machine.SCStatus;
import com.leiyuntop.machine.SCTagButton;
import com.leiyuntop.machine.StatusCapture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
/**
 * 雷云峰机型 控制类
 * @author lufeisong
 *
 */
public class LYMachineControl extends BaseMachineControl{
	private static LYMachineControl _LeiyunMachineControl;
	private static StatusCapture _StatusCapture;
	private Refrigerator mRefrigerator;//压缩机对象 
	
	private BaseVMCAction _currentBaseVMCAction;//当前出货时，货道，云台，门的模式组合对象。
	
	
	private String machineId = "01060099";//机器code
	public boolean respondButton = true;// 判断是否是相应物理按钮跳转内页
	public boolean isFrontView = true;//true在前台页面  false后台页面
	private IMachineEventHandler mIMachineEventHandler = new IMachineEventHandler() {
		
		@Override
		public void InfoEventHandler(int packageCode, ISCInfo obj) {
			switch(packageCode){
			case 1:
				CommonUtils.showLog(CommonUtils.LEIYUN_TAG, "获取货道");
				int way_landspace = 0;
				SCInit mSCInit = (SCInit) obj;
				HashMap<String, SCState> machineReturnWay_id = mSCInit.Param2;// 单片机返回的所有货道信息
				HashMap<String, ArrayList<String>> rowWay_idMap = new LinkedHashMap<String, ArrayList<String>>();// 存放每一行货道的map
				Iterator<Entry<String, SCState>> item = machineReturnWay_id.entrySet().iterator();
				ArrayList<String[]> wayTotal_list = new ArrayList<String[]>();//存放所有货道的list
				while (item.hasNext()) {
					Entry<String, SCState> objItem = item.next();
					if (!objItem.getValue().toString().equals("无设备")) {
						String key = objItem.getKey().toString();
						if (rowWay_idMap.containsKey(key.substring(0, 1))) {
							rowWay_idMap.get(key.substring(0, 1)).add(key);
						} else {
							ArrayList<String> rowIdList = new ArrayList<String>();
							rowIdList.add(key);
							rowWay_idMap.put(key.substring(0, 1), rowIdList);
						}
					}
				}
				Iterator<Entry<String, ArrayList<String>>> rowItem = rowWay_idMap.entrySet().iterator();
				while (rowItem.hasNext()) {
					Entry<String, ArrayList<String>> objectRowItem = rowItem.next();
					ArrayList<String> wayLandscape_list = objectRowItem.getValue();
					String[] array_str = new String[wayLandscape_list.size()];
					for(int i = 0;i < wayLandscape_list.size();i++){
						array_str[i] = wayLandscape_list.get(i);
					}
					wayTotal_list.add(array_str);
					if(wayLandscape_list.size() > way_landspace)
						way_landspace = wayLandscape_list.size();
				}
				GoodsWayUtil.leiyunArray = wayTotal_list.toArray(new String[wayTotal_list.size()][way_landspace]);
				ArrayList<int[]> leiyunBoxArray_list = new ArrayList<int[]>();
				int[] wayItem_list = new int[wayTotal_list.size()];
				for(int i = 0;i < wayTotal_list.size();i++){
					wayItem_list[i] = GoodsWayUtil.drink2BoxArray[0][GoodsWayUtil.drink2BoxArray[0].length - 1] + i + 1;
				}
				leiyunBoxArray_list.add(wayItem_list);
				GoodsWayUtil.leiyunBoxArray = leiyunBoxArray_list.toArray(new int[1][leiyunBoxArray_list.size()]);
				for(int i = 0;i < GoodsWayUtil.leiyunArray.length;i++){
					CommonUtils.showLog(CommonUtils.LEIYUN_TAG, Arrays.toString(GoodsWayUtil.leiyunArray[i]));
				}
				for(int i = 0;i < GoodsWayUtil.leiyunBoxArray.length;i++){
					CommonUtils.showLog(CommonUtils.LEIYUN_TAG, Arrays.toString(GoodsWayUtil.leiyunBoxArray[i]));
				}
				_isSinglechipStartSuccess = true;
				checkSinglechipInitStatus();
				break;
			case 6:
				SCStatus mSCStatus = (SCStatus) obj;// 售货机状态
					if(mSCStatus.Param9.equals("W")  || mSCStatus.Param9.equals("X") || mSCStatus.Param9.equals("Y") || mSCStatus.Param9.equals("Z"))
				break;
			case 7:
				SCCargoRoadStatus mSCCargoRoadStatus = (SCCargoRoadStatus) obj;// 货道状态
			    LinkedHashMap<String, SCState> mSCCargoRoadStatusMap = mSCCargoRoadStatus.Param1;
			    Iterator<Entry<String, SCState>> mSCCargoRoadStatusItem = mSCCargoRoadStatusMap.entrySet().iterator();
			    while(mSCCargoRoadStatusItem.hasNext()){
			    	Entry<String, SCState> mSCCargoRoadStatusObj = mSCCargoRoadStatusItem.next();
			    	String value = mSCCargoRoadStatusObj.getValue().toString();
			    	boolean isisSuccess = false;
			    	int msgWhat = 0;
			    	if(value.equals("正常")){
			    		isisSuccess = true;
			    		msgWhat = BaseViewHandler.OutGoodsSuccess;
			    		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "雷云峰 出货正常");
			    	}else{
			    		isisSuccess = false;
			    		msgWhat = BaseViewHandler.OutGoodsFail;
			    		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "雷云峰 出货失败");
				    }
			    	_currentBaseVMCAction.finishVMCAction(msgWhat,isisSuccess);
			    }
			    break;
			case 8:
				SCPickStatusStatus mSCPickStatusStatus = (SCPickStatusStatus) obj;// 出货检测状态(红外线)
				int[] i = mSCPickStatusStatus.Param1;
				SCState j = mSCPickStatusStatus.Param2;
				break;
			case 9:
				SCElevatorStatus mSCElevatorStatus = (SCElevatorStatus) obj;// 提升机构状态
				break;
			case 10:
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "雷云峰 出货结果判断");
				SCSale mSCSale = (SCSale) obj;// 出货状态
				SCSaleState way_idStatus = mSCSale.Param2;// 状态
				boolean isisSuccess = false;
				int msgWhat = 0;
				if(way_idStatus.toString().equals("付货正常")){
					isisSuccess = true;
					msgWhat = BaseViewHandler.OutGoodsSuccess;
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "雷云峰 出货正常");
				}else{
					isisSuccess = false;
					msgWhat = BaseViewHandler.OutGoodsFail;
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "雷云峰 出货失败");
				}
				_currentBaseVMCAction.finishVMCAction(msgWhat,isisSuccess);
				break;
			case 15:
				
				SCCoinCollection mSCCoinCollection = (SCCoinCollection) obj;// 收币金额
				int gotNotesCoins = mSCCoinCollection.Param2;//金额
				CommonUtils.showLog(CommonUtils.TAG, "zong jin e = " + gotNotesCoins);
				_StatusCapture.Coin_Qty();
				break;
			case 17:
				SCTagButton mSCTagButton = (SCTagButton) obj;
				int tag = mSCTagButton.Param1;
				CommonUtils.showLog(CommonUtils.LEIYUN_TAG,"tag = " + tag);
				if(tag == 1){
					CommonUtils.showLog(CommonUtils.LEIYUN_TAG,"开门按钮");
					CommonUtils.sendMessage(mViewHandler, BaseHandler.LEIYUN_DOOROPEN_FLAG, "");
				}else if(tag == 2){
					
				}
			case 18:
				SCCoinQty mSCCoinQty = (SCCoinQty) obj;// 硬币数量
				Hashtable<Double, Integer> coinNumMap = mSCCoinQty.Param1;
				int leftMoney = 0;//硬币器找零的钱
				int FiveCount = 0;//硬币器存在的5毛
				int TenCount = 0;//硬币器存在的10毛
				
				FiveCount = coinNumMap.get(50.0);
				TenCount = coinNumMap.get(100.0);
				CommonUtils.showLog(CommonUtils.TAG, "five = " + FiveCount + " ten = " + TenCount);
				break;
//			case 21:
//				
//				SCID mSCID = (SCID) obj;//机器编号
//				machineId = mSCID.Param1;
//				break;
			}
		}
		
		@Override
		public void ErrorEventHandler(int arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}
	};
	protected LYMachineControl(Context context) {
		super(context);
//		initActivity();
	}
	public static LYMachineControl getInstance(Context context){
		if(_LeiyunMachineControl == null)
			_LeiyunMachineControl = new LYMachineControl(context);
		return _LeiyunMachineControl;
	}
	//初始化雷云峰单片机
	void initActivity(){
		_StatusCapture = new StatusCapture();
		mRefrigerator = new Refrigerator();
		_StatusCapture.SetEventHandler(mIMachineEventHandler);
		boolean startInfo = _StatusCapture.Start("/dev/ttymxc1", 8);
		boolean startRefrigerator = mRefrigerator.Start("/dev/ttymxc2");
		CommonUtils.showLog(CommonUtils.LEIYUN_TAG, "初始化雷云峰单片机状态 startInfo:" + startInfo);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_StatusCapture.Coin_Init();
				_StatusCapture.Coin_Acceptor(1, 1);
				
			}
		}).start();
	}
	@Override
	public void checkOutGoodsResult() {
	}

	@Override
	public boolean getAvmStatus() {
		return true;
	}

	@Override
	protected void outGoods(BasePayControl basePayControl) {
		String way_id = basePayControl.getWayID();
		String outgoods_way_id = GoodsWayUtil.getW_code(basePayControl.getWayID());
		// 生成6位流水并保存到支付对象中
		SimpleDateFormat sd = new SimpleDateFormat("HHmmss", Locale.US);
		final String tradeTrace = sd.format(new Date());
		basePayControl.setTradeTrace(tradeTrace);
		
		LYMachineSetBean mLYMachineSetBean = selectLYMachineSetBean();
		String wayType = selectWayType(way_id);
		Integer doorStatus = mLYMachineSetBean.getDoorStatus();
		Integer elevatorStatus =  mLYMachineSetBean.getElevatorStatus();
		if(wayType.equals("1")){
			if(doorStatus == 0 && elevatorStatus == 0){
				_currentBaseVMCAction = new TedynnVMCACTION(outgoods_way_id);
			}else if(doorStatus == 0 && elevatorStatus == 1){
				_currentBaseVMCAction = new TedyynVMCACTION(outgoods_way_id);
			}else if(doorStatus == 1 && elevatorStatus == 0){
				_currentBaseVMCAction = new TedynyVMCACTION(outgoods_way_id);
			}else if(doorStatus == 1 && elevatorStatus == 1){
				_currentBaseVMCAction = new TedyyyVMCACTION(outgoods_way_id);
			}
		}else if(wayType.equals("0")){
			if(doorStatus == 0 && elevatorStatus == 0){
				_currentBaseVMCAction = new TednnnVMCACTION(outgoods_way_id);
			}else if(doorStatus == 0 && elevatorStatus == 1){
				_currentBaseVMCAction = new TednynVMCACTION(outgoods_way_id);
			}else if(doorStatus == 1 && elevatorStatus == 0){
				_currentBaseVMCAction = new TednnyVMCACTION(outgoods_way_id);
			}else if(doorStatus == 1 && elevatorStatus == 1){
				_currentBaseVMCAction = new TednyyVMCACTION(outgoods_way_id);
			}
		}
		_currentBaseVMCAction.setTradeTrace(tradeTrace);
		_currentBaseVMCAction.set_BasePayControl(basePayControl);

	}

	@Override
	protected void coinInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void coinConfirm(boolean confirm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void coinStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinOpen(int CFlag, int BFlag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinReturn(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinQty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFactoryCode() {
		return machineId;
	}

	@Override
	public String getM_No() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMachineMac() {
		return MachineUtil.getLocalMacAdress(mContext);
	}

	@Override
	public List<String> getTemperature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshCloseDoorStatus() {
		if(isFrontView()){
			setCloseDoorStatus(true);
		}else{
			setCloseDoorStatus(false);
		}
	}
	@Override
	public List<String> checkSelectInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getCoinNotesStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkRoad(String wayId) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean setRoad(Handler handler, String wayId, boolean isValid,
			int price, int quantity) {
		Message msg = new Message();
		msg.what = BaseViewHandler.SetRoadSuccess;
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(wayId);
		args.add(isValid);
		args.add(price);
		args.add(quantity);
		msg.obj = args;
		CommonUtils.showLog(CommonUtils.SCM_TAG,"setRoad:wayId = "
				+ wayId + ";isValid = " + isValid + ";price = " + price
				+ ";quantity = " + quantity);
		handler.sendMessage(msg);
		return true;
	}
	public boolean isRespondButton() {
		return respondButton;
	}

	public void setRespondButton(boolean respondButton) {
		this.respondButton = respondButton;
	}
	public boolean isFrontView() {
		return isFrontView;
	}
	public void setFrontView(boolean isFrontView) {
		this.isFrontView = isFrontView;
	}
	
	public Refrigerator getmRefrigerator() {
		return mRefrigerator;
	}
	/**
	 * 设置云台位置
	 * @param location
	 */
	public void VMC_Elevator(String location){
		_StatusCapture.VMC_Elevator(location);
	}
	public void setAutoCloseDoor(boolean doorStatus){
		_StatusCapture.setAutoCloseDoor(doorStatus);
		
	}
	/**
	 * 履带模式，使用云台，门关闭
	 * t履带
	 * e云台
	 * d门
	 * y履带模式
	 * y使用云台
	 * y关门
	 */
	private class TedyyyVMCACTION extends BaseVMCAction{
		public TedyyyVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.setAutoCloseDoor(true);
			_StatusCapture.VMC_Sale(_way_id);
			
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isisSuccess) {
			super.finishVMCAction(msgWhat,isisSuccess);
		}
		
	}
	/**
	 * 履带模式，使用云台，门打开
	 * t履带
	 * e云台
	 * d门
	 * y履带模式
	 * y使用云台
	 * n开门
	 */
	private class TedyynVMCACTION extends BaseVMCAction{
		public TedyynVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.setAutoCloseDoor(false);
			_StatusCapture.VMC_Sale(_way_id);
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isisSuccess) {
			super.finishVMCAction(msgWhat,isisSuccess);
		}
		
	}
	/**
	 * 履带模式，不使用云台，门打开
	 * t履带
	 * e云台
	 * d门
	 * y履带模式
	 * n不使用云台
	 * n开门
	 */
	private class TedynnVMCACTION extends BaseVMCAction{
		public TedynnVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.setAutoCloseDoor(false);
			_StatusCapture.VMC_Sale(_way_id);
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isisSuccess) {
			super.finishVMCAction(msgWhat,isisSuccess);
		}
		
	}
	/**
	 * 履带模式，不使用云台，门关闭
	 * t履带
	 * e云台
	 * d门
	 * y履带模式
	 * n不使用云台
	 * y关门
	 */
	private class TedynyVMCACTION extends BaseVMCAction{
		public TedynyVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.setAutoCloseDoor(true);
			_StatusCapture.VMC_Sale(_way_id);
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isSuccess) {
			super.finishVMCAction(msgWhat,isSuccess);
		}
		
	}
	/**
	 * 非履带模式，使用云台，门关闭
	 * t履带
	 * e云台
	 * d门
	 * n非履带模式
	 * y使用云台
	 * y关门
	 */
	private class TednyyVMCACTION extends BaseVMCAction{
		public TednyyVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.setAutoCloseDoor(true);
			_StatusCapture.VMC_Sale(_way_id);
			
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isSuccess) {
			super.finishVMCAction(msgWhat,isSuccess);
		}
		
	}
	/**
	 * 非履带模式，使用云台，门打开
	 * t履带
	 * e云台
	 * d门
	 * n非履带模式
	 * y使用云台
	 * n开门
	 */
	private class TednynVMCACTION extends BaseVMCAction{
		public TednynVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.setAutoCloseDoor(false);
			_StatusCapture.VMC_Sale(_way_id);
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isSuccess) {
			super.finishVMCAction(msgWhat,isSuccess);
		}
		
	}
	/**
	 * 非履带模式，不使用云台，门打开
	 * t履带
	 * e云台
	 * d门
	 * n非履带模式
	 * n不使用云台
	 * n开门
	 */
	private class TednnnVMCACTION extends BaseVMCAction{
		public TednnnVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.VMC_Elevator("Z");
			_StatusCapture.setAutoCloseDoor(false);
			_StatusCapture.VMC_Sale2(_way_id);
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isSuccess) {
			super.finishVMCAction(msgWhat,isSuccess);
		}
		
	}
	/**
	 * 非履带模式，不使用云台，门关闭
	 * t履带
	 * e云台
	 * d门
	 * n非履带模式
	 * n不使用云台
	 * y关门
	 */
	private class TednnyVMCACTION extends BaseVMCAction{
		public TednnyVMCACTION(String way_id){
			super(way_id);
		}
		@Override
		public void startVMCAction() {
			_StatusCapture.VMC_Elevator("Z");
			_StatusCapture.setAutoCloseDoor(false);
			_StatusCapture.VMC_Sale2(_way_id);
		}

		@Override
		public void finishVMCAction(int msgWhat,boolean isSuccess) {
			super.finishVMCAction(msgWhat,isSuccess);
			_StatusCapture.setAutoCloseDoor(true);
		}
		
	}
	/**
	 * 基础抽象类
	 * @author lufeisong
	 *
	 */
	public abstract class BaseVMCAction{
		protected String _way_id;
		protected String tradeTrace;
		protected BasePayControl _BasePayControl;
		public BaseVMCAction(String way_id){
			_way_id = way_id;
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "雷云峰 startVMCAction");
			startVMCAction();
		}
		public abstract void startVMCAction();
		public void finishVMCAction(int msgWhat,boolean isisSuccess){
			outGoodsFinishByLY(msgWhat, isisSuccess, tradeTrace);
		};
		public String get_way_id() {
			return _way_id;
		}
		public void set_way_id(String _way_id) {
			this._way_id = _way_id;
		}
		public String getTradeTrace() {
			return tradeTrace;
		}
		public void setTradeTrace(String tradeTrace) {
			this.tradeTrace = tradeTrace;
		}
		public BasePayControl get_BasePayControl() {
			return _BasePayControl;
		}
		public void set_BasePayControl(BasePayControl _BasePayControl) {
			this._BasePayControl = _BasePayControl;
		}
		
	}
}
