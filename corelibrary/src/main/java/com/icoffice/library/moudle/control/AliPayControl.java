package com.icoffice.library.moudle.control;

/**
 * author: Michael.Lu
 */
import android.os.Bundle;

import com.alipay.sonicwavenfc.SonicWaveNFC;
import com.alipay.sonicwavenfc.SonicWaveNFCHandler;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.handler.BaseHttpHandler;
import com.icoffice.library.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import CofficeServer.PayType;

public class AliPayControl extends BaseNetWorkPayControl implements
		SonicWaveNFCHandler {
	private SonicWaveNFC mSonicWaveNFC;
	private String dynamic_id;

	public AliPayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean) {
		super(mBaseMachineControl, soldGoodsBean, PayType.ali, 1 * 90 * 1000, 5);
	}

	@Override
	public void start() {
		super.start();
		mBaseMachineControl.startSound(0);
		mSonicWaveNFC = SonicWaveNFC.getInstance();
		mSonicWaveNFC.initSonicWaveNFC(mBaseMachineControl.mContext);// 实例构造后，需要检测耳机是否插入
		int iTimeoutSeconds = 100;
		int iMinAmplitude = 20; // 设置接收数据时声波幅度门限值
		mSonicWaveNFC.startReceiveData(iTimeoutSeconds, iMinAmplitude,
				mBaseMachineControl.mContext, this);
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay:" + "打开声波支付");
	}

	@Override
	public void interrupt() {
		super.interrupt();
		if (!mSonicWaveNFC.isReceiverSoincWave()) {
			closeSonicWaveNFC();
		}
	}
	@Override
	public void finish() {
		super.finish();
		
		
	}

	@Override
	public void run() {
		long timeStampNow = System.currentTimeMillis();
		long timeStampInterval = timeStampNow - timeStampFirst;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "rollTime = " + rollTime+ ";_isNoticeOutGoods = " + _isNoticeOutGoods);
		if (timeStampInterval < rollTime && !_isNoticeOutGoods) {
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "开始轮询");
			Map<String, String> m = new HashMap<String, String>();
			m.put("clientOrderNo", mOrderBean.getClient_order_no());
			mBaseMachineControl._coordinator.checkAlipayOrderState(m);
			super.run();
		} else {
			if(timeStampInterval >= rollTime)
				UnNormalInterrupt();
//			else
//				NormalInterrupt();
		}
	}

	/**
	 * 获取声波id
	 * 
	 * @return
	 */
	public String getDynamic_id() {
		return dynamic_id;
	}

	/**
	 * 
	 * 实现声波接口 (non-Javadoc)
	 * 
	 * @see com.alipay.sonicwavenfc.SonicWaveNFCHandler#onDataReceived(String)
	 */

	@Override
	public void onDataReceived(String dynamic_id) {
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "获取生波段: " + dynamic_id);
		rollRequest();
		Bundle mBundle = new Bundle();
		mBundle.putInt("shopTimer", 90);
		CommonUtils.sendMessage(mBaseMachineControl.mViewHandler, BaseHttpHandler.RefreshShopTimer, mBundle);
		
		this.dynamic_id = dynamic_id;
		mBaseMachineControl.addListRollBasePayControl(this);
		Map<String, String> m = new HashMap<String, String>();
		m.put("clientOrderNo", mOrderBean.getClient_order_no());
		m.put("gCode", mOrderBean.getG_code());
		m.put("dynamic_id", dynamic_id);
		mBaseMachineControl._coordinator.createOrderAlipay(m);
		closeSonicWaveNFC();
	}

	@Override
	public void onReceiveDataFailed(int arg0) {

	}

	@Override
	public void onReceiveDataInfo(String arg0) {

	}

	@Override
	public void onReceiveDataStarted() {

	}

	@Override
	public void onReceiveDataTimeout() {

	}

	@Override
	public void onSendDataFailed(int arg0) {

	}

	@Override
	public void onSendDataInfo(String arg0) {

	}

	@Override
	public void onSendDataStarted() {

	}

	@Override
	public void onSendDataTimeout() {

	}

	/*
	 * 关闭声波操作
	 */
	public void closeSonicWaveNFC() {
		if (null != mSonicWaveNFC) {
			mSonicWaveNFC.stopReceiveData();
		}
	}

}
