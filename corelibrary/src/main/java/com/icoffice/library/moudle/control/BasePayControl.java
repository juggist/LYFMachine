package com.icoffice.library.moudle.control;

import java.io.Serializable;

import CofficeServer.PayType;
import android.util.Log;

import com.icoffice.library.bean.db.OrderBean;
import com.icoffice.library.bean.db.RcodeBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.configs.Constant;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.Utils;

public class BasePayControl implements Serializable{
	protected String _wayId;
	protected PayType _payType;
	protected long _createTimeStamp;
	protected int _priceMoney = 0;// 售价
	protected boolean _isInterrupt = false; // 是否被中断
	protected boolean _isNoticeOutGoods = false;// 是否通知出货
	protected String _tradeTrace = ""; // 本地跟踪出货流水（由售货机使用，追踪出货行为，若不使用该特性，始终置空）
	protected boolean _useQueue = true; // 是否使用队列出货
	protected SoldGoodsBean mSoldGoodsBean;//正常售卖 商品对应的信息
	
	protected BaseMachineControl mBaseMachineControl;
	protected OrderBean mOrderBean;
	protected String rid;
	protected String oid;
	//排除现金的payControl
	protected BasePayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean, PayType payType) {
		this._payType = payType;
		this.mBaseMachineControl = mBaseMachineControl;
		this.mSoldGoodsBean = soldGoodsBean;
		this._priceMoney = Integer.parseInt(soldGoodsBean.getUnit_price());
		this._wayId = soldGoodsBean.getW_code();
		createOrderBean();
		if(!payType.equals(PayType.cash))//如果是cash的，不生成订单，只保存paycontrol在内存中，以获取way_id。最后通过补单生成订单
			insertOrder();
	}
	//exchangePayControl
	protected BasePayControl(BaseMachineControl mBaseMachineControl,
			RcodeBean rcodeBean,String rid,String oid,PayType payType) {
		this._payType = payType;
		this.mBaseMachineControl = mBaseMachineControl;
		this.mSoldGoodsBean = rcodeBean;
		this._priceMoney = Integer.parseInt(rcodeBean.getUnit_price());
		this._wayId = rcodeBean.getW_code();
		this.rid = rid;
		this.oid = oid;
		createOrderBean();
		insertOrder();
	}
	//cashPayControl 补单机制
	protected BasePayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean, PayType payType, int price) {
		this._payType = payType;
		this.mBaseMachineControl = mBaseMachineControl;
		this.mSoldGoodsBean = soldGoodsBean;
		this._priceMoney = price;
		this._wayId = soldGoodsBean.getW_code();
		createOrderBean();
		insertOrder();
	}
	//服务器再次出货的订单 paycontrol
	protected BasePayControl(BaseMachineControl mBaseMachineControl,SoldGoodsBean soldGoodsBean,OrderBean orderBean){
		this.mBaseMachineControl = mBaseMachineControl;
		this._payType = PayType.valueOf(orderBean.getPay_type());
		this.mSoldGoodsBean = soldGoodsBean;
		this._priceMoney = orderBean.getO_unit_price();
		this._wayId = orderBean.getW_code();
		mOrderBean = new OrderBean();
		mOrderBean.setClient_order_no(orderBean.getClient_order_no());
		mOrderBean.setO_number(orderBean.getO_number());
		mOrderBean.setG_code(orderBean.getG_code());
		mOrderBean.setO_amount(orderBean.getO_amount());
		mOrderBean.setO_unit_price(orderBean.getO_unit_price());
		mOrderBean.setTotal_price(orderBean.getTotal_price());
		mOrderBean.setPay_type(orderBean.getPay_type());
		mOrderBean.setO_generate_time(orderBean.getO_generate_time());
		mOrderBean.setO_complete_time(orderBean.getO_complete_time());
		mOrderBean.setO_pay_state(orderBean.getO_pay_state());
		mOrderBean.setO_state(orderBean.getO_state());
		mOrderBean.setW_code(orderBean.getW_code());
		mOrderBean.setCashIn(orderBean.getCashIn());
		mOrderBean.setCashOut(orderBean.getCashOut());
		mOrderBean.setP_state(0);
		mOrderBean.setExpand(orderBean.getExpand());
		mOrderBean.setId(orderBean.getId());
	}
	protected void createOrderBean(){
		mOrderBean = new OrderBean();
		StringBuffer client_order_no_sb = new StringBuffer();
		_createTimeStamp = System.currentTimeMillis();// 生成订单的时间戳 (秒数)
		String orderPrefix = "";
		String oid = "";
		if(_payType == PayType.exchangepay){
			oid = this.oid;
		}
		if (_payType == PayType.wechat) {
			orderPrefix = Constant.WXORDER_PREFIX;
		}
		client_order_no_sb.append(orderPrefix
				+ mBaseMachineControl.mMachineBean.getM_code() + "-"
				+ mSoldGoodsBean.getG_code() + "-" + _createTimeStamp);
		String client_order_no = client_order_no_sb.toString();
		mOrderBean.setClient_order_no(client_order_no);
		mOrderBean.setO_number("");
		mOrderBean.setG_code(mSoldGoodsBean.getG_code());
		mOrderBean.setO_amount(1);
		mOrderBean.setO_unit_price(_priceMoney);
		mOrderBean.setTotal_price(_priceMoney * 1);
		mOrderBean.setPay_type(_payType.value());
		mOrderBean.setO_generate_time(Utils.currentTime());
		mOrderBean.setO_complete_time("");
		mOrderBean.setO_pay_state(0);
		mOrderBean.setO_state(-1);
		mOrderBean.setW_code(_wayId);
		mOrderBean.setCashIn(0);
		mOrderBean.setCashOut(0);
		mOrderBean.setP_state(0);
		mOrderBean.setExpand(oid);
	}
	protected void insertOrder() {
		mOrderBean.setId(mBaseMachineControl.mDbHelper
				.insertOrderBean(mOrderBean));
	}

	protected void start() { // 开始
	}

	protected void interrupt() { // 中断
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "中断 _isInterrupt = " + _isInterrupt);
		if (!_isInterrupt) {
			_isInterrupt = true;
			mBaseMachineControl.deleteRollBasePayControl(mOrderBean
					.getClient_order_no());
		}
	}

	protected void noticeOutGoods() { // 通知出货
		synchronized (this) {
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "BasepayControl 通知出货; _isNoticeOutGoods = " + _isNoticeOutGoods);
			if (!_isNoticeOutGoods) {
				_isNoticeOutGoods = true;
				mOrderBean.setO_pay_state(1);
				mBaseMachineControl.addListOutGoodsBasePayControl(this, _useQueue);
			}
		}
	}

	protected void finish() { // 完成
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"BasePayControl finish ; _isInterrupt = " + _isInterrupt + ";_isNoticeOutGoods = " + _isNoticeOutGoods);
		if (_isNoticeOutGoods) {
			mOrderBean.setO_complete_time(Utils.currentTime());
			mBaseMachineControl.updateOrderBean(mOrderBean);
		}
	}

	public int getPayType() {
		return _payType.value();
	}

	public String getWayID() {
		return _wayId;
	}

	public int getPrice() {
		return _priceMoney;
	}
	
	public String getClient_order_no(){
		return mOrderBean.getClient_order_no();
	}

	public String getTradeTrace() {
		return _tradeTrace;
	}
	
	public void setTradeTrace(String tradeTrace) {
		_tradeTrace = tradeTrace;
	}
	
	public boolean isUseQueue() {
		return _useQueue;
	}
	
	public long getCreateTimeStamp() {
		return _createTimeStamp;
	}
	public OrderBean getmOrderBean() {
		return mOrderBean;
	}
	public void setmOrderBean(OrderBean mOrderBean) {
		this.mOrderBean = mOrderBean;
	}
	
}
