package com.icofficeapp.activity;

import java.lang.reflect.Field;

import com.icoffice.library.moudle.control.ImageLoaderControl;

import android.app.Fragment;
import android.os.Bundle;


public class BaseChildFragment extends BaseFragment{
	protected ImageLoaderControl mImageLoaderControl;
	public static final int PAYTYPE_UNKNOW_FLAG = 0;// UnknownPay
	public static final int PAYTYPE_CASH_FLAG = 1;// cash
	public static final int PAYTYPE_UNION_FLAG = 2;// unionpay
	public static final int PAYTYPE_WECHAT_FLAG = 3;// wechat
	public static final int PAYTYPE_ALIPAY_FLAG = 4;// ali
	public static final int PAYTYPE_ICOFFICE_FLAG = 5;// card
	public static final int PAYTYPE_EXCHANGE_FLAG = 6;// exchangepay
	public static final int PAYTYPE_YEE_FLAG = 7;// yeepay
	public static final int PAYTYPE_PAYAGAIN_FLAG = 8;// 支付成功
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mImageLoaderControl = ImageLoaderControl.getInstance();
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
