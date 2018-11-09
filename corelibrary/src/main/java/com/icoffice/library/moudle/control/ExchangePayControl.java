package com.icoffice.library.moudle.control;

import java.util.HashMap;
import java.util.Map;

import CofficeServer.PayType;

import com.icoffice.library.bean.db.OrderBean;
import com.icoffice.library.bean.db.RcodeBean;
import com.icoffice.library.utils.GoodsWayUtil;
/**
 * 微商城 
 * 兑换出货
 * @author lufeisong
 *
 */
public class ExchangePayControl extends BasePayControl{
	protected ExchangePayControl(BaseMachineControl mBaseMachineControl,
			RcodeBean rcodeBean,String rid,String oid) {
		super(mBaseMachineControl,rcodeBean,rid,oid,PayType.exchangepay);
//		_useQueue = false;
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
		super.noticeOutGoods();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void finish() {
		super.finish();
		RcodeBean rcodeBean = (RcodeBean)mSoldGoodsBean;
		mBaseMachineControl.post_add_change_log(rcodeBean.getOrder_gl_id(),rcodeBean.getG_id(),rid,mOrderBean.getClient_order_no());
		mBaseMachineControl.cofficeShopOrderComplete();
//		OrderBean[] array = mBaseMachineControl.selectCofficeShopOrder();
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
//				m.put("gCode", mOrderBean.getG_code());
//				m.put("wCode", GoodsWayUtil.getWayName(mOrderBean.getW_code()));
//				m.put("generateTime", mOrderBean.getO_complete_time());
//				m.put("state", o_state + "");
//				m.put("unitPrice", mOrderBean.getO_unit_price() + "");
//				m.put("totalPrice", mOrderBean.getO_unit_price() + "");
//				m.put("sNumber", mOrderBean.getExpand());
//				map[i] = m;
//			}
//			mBaseMachineControl._coordinator.createOrderCofficeShop(map);
//		}
	}

}
