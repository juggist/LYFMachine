package com.icoffice.library.moudle.control;

/**
 * author: Michael.Lu
 */
import CofficeServer.PayType;
import android.util.Log;

import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.utils.CommonUtils;

public class CashLeiYunPayControl extends BasePayControl {
	private int advanceMoney = 0;// 预收款
	private int acceptMoney = 0;// 已收款
	private int changeMoney = 0;// 找零
	private boolean isReturnMoney = false; // 是否退款

	public CashLeiYunPayControl(BaseMachineControl mBaseMachineControl,
			SoldGoodsBean soldGoodsBean) {
		super(mBaseMachineControl, soldGoodsBean, PayType.cash);
	}

	public void setAcceptMoney(int acceptMoney) {
		this.acceptMoney += acceptMoney;
		if (this.acceptMoney >= _priceMoney) {
			noticeOutGoods();
		}
	}

	public int getAcceptMoney() { // 这个可以不开放
		return acceptMoney;
	}

	public void setAdvanceMoney(int advanceMoney) {
		this.advanceMoney = advanceMoney;
	}

	public int getChangeMoney() { // 这个可以不开放
		return changeMoney;
	}

	public void canTakeAdvanceMoney(int canReturnMoney) { // 判断是否可以接收预收款
		if (acceptMoney + advanceMoney <= canReturnMoney) {
			mBaseMachineControl.coinConfirm(true);
		} else {
			mBaseMachineControl.coinConfirm(false);
		}
		advanceMoney = 0;
	}

	@Override
	public void start() {
		super.start();
		mBaseMachineControl.coinOpen(1, 1);
	}

	@Override
	public void interrupt() {
		if (!_isNoticeOutGoods) {// 未通知出货
			if (advanceMoney > 0) {
				mBaseMachineControl.coinConfirm(false); // 退预付款
			}
			if (acceptMoney > 0) {
				mBaseMachineControl.coinReturn(acceptMoney); // 退已收款
				// acceptMoney -= acceptMoney;
				acceptMoney = 0; // 活该被吃币
				mBaseMachineControl.coinOpen(0, 0);
			}
		} else {
		}
	}

	@Override
	public void noticeOutGoods() {
		changeMoney = acceptMoney - _priceMoney;
		if (changeMoney >= 0) {
			mBaseMachineControl.coinOpen(0, 0);
			super.noticeOutGoods();
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (changeMoney > 0 && changeMoney <= acceptMoney && !isReturnMoney) {
			mBaseMachineControl.coinReturn(changeMoney);
			isReturnMoney = true;
			acceptMoney = 0;
		}
	}
}
