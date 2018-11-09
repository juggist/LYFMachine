package com.icoffice.library.moudle.control;
/**
 * 服务器通知出货
 */

import java.util.HashMap;
import java.util.Map;

import com.icoffice.library.bean.db.OrderBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;

public class ServicePayControl extends BasePayControl{

	
	protected ServicePayControl(BaseMachineControl mBaseMachineControl,SoldGoodsBean soldGoodsBean,OrderBean orderBean) {
		super(mBaseMachineControl,soldGoodsBean,orderBean);
		noticeOutGoods();
	}


	@Override
	protected void finish() {
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
//				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"服务器出货 订单提交服务器 = " + "map." + i + " = " + map[i].toString());
//			}
//			mBaseMachineControl._coordinator.orderComplete(map);
//		}
	}

	@Override
	protected void noticeOutGoods() {
		super.noticeOutGoods();
	}
	
}
