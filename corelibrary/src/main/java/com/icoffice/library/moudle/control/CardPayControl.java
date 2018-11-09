package com.icoffice.library.moudle.control;

import java.util.HashMap;
import java.util.Map;

import CofficeServer.PayType;
import android.os.Bundle;

import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.handler.BaseHttpHandler;
import com.icoffice.library.utils.CommonUtils;

/**
 * 客非卡支付方式
 * author: Michael.Lu
 */

public class CardPayControl extends BaseNetWorkPayControl {
	
	private String _cardNO = "";
	
	
	public String get_cardNO() {
		return _cardNO;
	}

	public void set_cardNO(String _cardNO) {
		this._cardNO = _cardNO;
	}

	private String _balance = "";
	
	
	public CardPayControl(BaseMachineControl baseMachineControl,
			SoldGoodsBean soldGoodsBean) {
		super(baseMachineControl, soldGoodsBean, PayType.card,0,0);
	}

	@Override
	public void start() {
		super.start();
		onCardNoReaded(_cardNO);
	}

	@Override
	public void interrupt() {
		super.interrupt();
		
	}

	@Override
	public void noticeOutGoods() {
		super.noticeOutGoods();
	}

	@Override
	public void finish() {
		super.finish();
	}

	public String getCardNo() {
		return _cardNO;
	}

	public String getBalance() {
		return _balance;
	}

	public void onCardNoReaded(String cardNo) {
		
		synchronized (this) {
			mBaseMachineControl.addListRollBasePayControl(this);
			final HashMap<String, String> options = new HashMap<String, String>();
			options.put("clientOrderNo", mOrderBean.getClient_order_no());
			options.put("gCode", mOrderBean.getG_code());
			options.put("vcCode", _cardNO);
					Map<String, String> ret = mBaseMachineControl._coordinator.createOrderCofficeCard(options);
					if(ret!=null){
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "CardPayControl ret = " + ret.toString());
						Bundle bundle = new Bundle();
						_balance = ret.get("balance");
						String msg = ret.get("msg"); 
						String status = ret.get("status");
						
						bundle.putString("balance", _balance);
						bundle.putString("msg", msg);
						bundle.putString("status", status);
						
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, msg);
						if (ret.get("status").equals("1")) {
							CommonUtils.showLog(CommonUtils.PURCHASE_TAG, msg);
							noticeOutGoods();
						}else {
							CommonUtils.sendMessage(mBaseMachineControl.mViewHandler, BaseHttpHandler.ICOFFICECARD_PAY_FAIL,bundle);
						}
					}else {
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "连接失败");
					}
		}
		
		}



	
	
	
}
