package com.icoffice.library.moudle.control;

/**
 * author: Michael.Lu
 */
import java.util.HashMap;
import java.util.Map;

import CofficeServer.PayType;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.utils.CommonUtils;

/**
 * 微信支付的操作
 * @author Administrator
 *
 */
public class WechatPayControl extends BaseNetWorkPayControl {
	private boolean rollFrist = true;//是否是第一次轮询到用户已经获取二维码发送的链接的状态
	public WechatPayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean) {
		super(mBaseMachineControl,soldGoodsBean, PayType.wechat, 1 * 60 * 1000 , 3);
	}

	@Override
	public void start() {
		super.start();
		mBaseMachineControl.addListRollBasePayControl(this);
		rollRequest();
	}

//	@Override
//	public void interrupt() {
//		super.interrupt();
//	}

	@Override
	public void finish() {
		super.finish();
	}
/**
 * 向服务器查询交易结果
 */
	@Override
	public void run() {
		long timeStampNow = System.currentTimeMillis();//当前的时间
		long timeStampInterval = timeStampNow - timeStampFirst;//时间戳的间隔设定
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "rollTime = " 
		+ rollTime + ";timeStampFirst = " + timeStampFirst + 
		";timeStampInterval = " + timeStampInterval + 
		";_isNoticeOutGoods = " + _isNoticeOutGoods + ";" +
				"clientOrderNo = " + mOrderBean.getClient_order_no());
		
		
		if (timeStampInterval < rollTime && !_isNoticeOutGoods) {
			if (timeStampInterval < 1000 * 30) {
			} else if (timeStampInterval < 1000 * 60) {
				rollInterval = 2;
			} else if (timeStampInterval < 1000 * 90) {
				rollInterval = 1;
			} else {
				rollInterval = 1;
			}
			Map<String, String> m = new HashMap<String, String>();
			m.put("clientOrderNo", mOrderBean.getClient_order_no());
			mBaseMachineControl._coordinator.checkWeixinOrderState(m);
			super.run();
		} else {
			if(timeStampInterval >= rollTime )
				UnNormalInterrupt();
//			else
//				NormalInterrupt();
		}
	}

	public boolean isRollFrist() {
		return rollFrist;
	}

	public void setRollFrist(boolean rollFrist) {
		this.rollFrist = rollFrist;
	}
	
}
