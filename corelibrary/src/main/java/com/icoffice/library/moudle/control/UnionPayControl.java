package com.icoffice.library.moudle.control;

import CofficeServer.PayType;

import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.db.SoldGoodsBean;

/**
 * author: Michael.Lu
 */

public class UnionPayControl extends BasePayControl {

	public UnionPayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean) {
		super(mBaseMachineControl, soldGoodsBean, PayType.unionpay);
	}

	@Override
	public void start() {
		super.start();
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
}
