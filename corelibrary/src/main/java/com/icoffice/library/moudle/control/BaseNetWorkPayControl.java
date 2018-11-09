package com.icoffice.library.moudle.control;

/**
 * author: Michael.Lu
 */
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import CofficeServer.PayType;

import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.db.OrderBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;

public class BaseNetWorkPayControl extends BasePayControl implements Runnable {
	protected long timeStampFirst;// 第一次轮询时间
	protected long rollTime;// 轮询时长
	protected long rollInterval;// 轮询间隔
	protected ScheduledExecutorService mScheduledExecutorService;

	protected BaseNetWorkPayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean, PayType payType, long rollTime, long rollInterval) {
		super(mBaseMachineControl,soldGoodsBean, payType);
		this.rollTime = rollTime;
		this.rollInterval = rollInterval;
		mScheduledExecutorService = Executors
				.newSingleThreadScheduledExecutor();
	}

	protected void rollRequest() {
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "开始轮询");
		timeStampFirst = System.currentTimeMillis();
		mScheduledExecutorService
				.schedule(this, rollInterval, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		mScheduledExecutorService
				.schedule(this, rollInterval, TimeUnit.SECONDS);
	}
	//出货正常结束交易
//	public void NormalInterrupt() {
//		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "出货正常结束交易");
//		interrupt();
//	}

	// 非正结束交易
	public void UnNormalInterrupt(){
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "非正结束交易");
		if(mBaseMachineControl.mCurrentPayControl.getClient_order_no().equals(mOrderBean.getClient_order_no())){
			mBaseMachineControl.startSound(10);
		}
		interrupt();
	}
	@Override
	protected void noticeOutGoods() { // 通知出货
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "BaseNetWorkPayControl 通知出货; _isNoticeOutGoods = " + _isNoticeOutGoods);
		if (!_isNoticeOutGoods) {
			mBaseMachineControl.startSound(2);
			mBaseMachineControl.deleteRollBasePayControl(mOrderBean
					.getClient_order_no());
			mOrderBean.setCashIn(mOrderBean.getO_unit_price());
			super.noticeOutGoods();
		}
	}
	@SuppressWarnings("rawtypes")
	@Override
	protected void finish(){
		super.finish();
		mBaseMachineControl.netOrderComplete();
//		OrderBean[] array = mBaseMachineControl.selectNetOrder();
//	
//		if(array != null){
//			Map[] map = new Map[array.length];
//			for(int i = 0;i < array.length;i++){
//				OrderBean mOrderBean = array[i];
//				Map<String, String> m = new HashMap<String, String>();
//				Integer o_state = mOrderBean.getO_state();
//				if(o_state == 0)
//					o_state = 3;
//				m.put("clientOrderNo", mOrderBean.getClient_order_no());
//				m.put("wCode", GoodsWayUtil.getWayName(mOrderBean.getW_code()));
//				m.put("completeTime", mOrderBean.getO_complete_time());
//				m.put("state", o_state + "");
//				map[i] = m;
//			}
//			for(int i = 0;i < map.length;i++){
//				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"网络支付订单提交服务器 = " + "map." + i + " = " + map[i].toString());
//			}
//			mBaseMachineControl._coordinator.orderComplete(map);
//		}
	}

	public long getTimeStampFirst() {
		return timeStampFirst;
	}

	public void setTimeStampFirst(long timeStampFirst) {
		this.timeStampFirst = timeStampFirst;
	}
	public void setRollTime(long rollTime){
		this.rollTime = rollTime;
	}
}
