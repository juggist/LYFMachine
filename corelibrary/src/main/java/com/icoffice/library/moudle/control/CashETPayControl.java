package com.icoffice.library.moudle.control;

/**
 * author: Michael.Lu
 */
import java.util.HashMap;
import java.util.Map;

import CofficeServer.PayType;

import com.icoffice.library.bean.db.OrderBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;

public class CashETPayControl extends BasePayControl {
	//点击屏幕的cash购买
	public CashETPayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean) {
		super(mBaseMachineControl,soldGoodsBean, PayType.cash);
		_useQueue = false;
	}

	//补单
	public CashETPayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean, int price) {
		super(mBaseMachineControl,soldGoodsBean, PayType.cash, price);
		_useQueue = false;
	}
	
//	@Override
//	public void createOrder(){
//		super.createOrder();
//		
//	}
	
	@Override
	public void start() {
		super.start();
		noticeOutGoods();
	}

	@Override
	public void interrupt() {
		super.interrupt();
	}

	@Override
	public void noticeOutGoods() {
		mOrderBean.setCashIn(mOrderBean.getO_unit_price());
		super.noticeOutGoods();
	}
	
	/**
	 * 直接设置出货信息，在易触通过物理键出货后补单时使用
	 * 设置出货后的信息
	 * 
	 * @param cashIn
	 */
	public void setOutGoodsInfo(int cashIn,int o_pay_state) { 
		if (!_isNoticeOutGoods) {
			_isNoticeOutGoods = true;
			mOrderBean.setCashIn(cashIn);
			mOrderBean.setO_pay_state(o_pay_state);
		}
	}
	/**
	 * 修改出货状态
	 * @param o_state
	 */
	public void setO_state(int o_state){
		mOrderBean.setO_state(o_state);
	}
	public void cashIn(int cashIn) {
		mOrderBean.setCashIn(cashIn);
	}
	
	public void cashOut(int cashOut){
		//TODO 现金找零
		mOrderBean.setCashOut(cashOut);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void finish() {
		cashOut(mBaseMachineControl._receivedMoney - mOrderBean.getO_unit_price());
		super.finish();
		mBaseMachineControl.cashOrderComplete();
//		OrderBean[] array = mBaseMachineControl.selectCashOrder();
//		if(array != null){
//			Map[] map = new Map[array.length];
//			for(int i = 0;i < array.length;i++){
//				OrderBean mOrderBean = array[i];
//				Map<String, String> m = new HashMap<String, String>();
//				Integer o_state = mOrderBean.getO_state();
//				if(o_state == 0)
//					o_state = 3;
//				m.put("clientOrderNo", mOrderBean.getClient_order_no());
//				m.put("gCode", mOrderBean.getG_code());
//				m.put("wCode", GoodsWayUtil.getWayName(mOrderBean.getW_code()));
//				m.put("generateTime", mOrderBean.getO_generate_time());
//				m.put("unitPrice", mOrderBean.getO_unit_price() + "");
//				m.put("totalPrice", mOrderBean.getTotal_price() + "");
//				m.put("state", o_state + "");
//				m.put("cashIn", mOrderBean.getCashIn() + "");
//				m.put("cashOut", mOrderBean.getCashOut() + "");
//				map[i] = m;
//			}
//			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "提交现金支付订单 订单数量"+array.length);
//			mBaseMachineControl._coordinator.createOrderCash(map);
//		}
	}
}
