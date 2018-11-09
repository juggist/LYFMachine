package com.icoffice.library.callback;

import com.icoffice.library.bean.network.StatusRegisterBean;


/**
 * 注册机器回调
 * @author lufeisong
 *
 */
public interface RegisterMachineCallBack {
	public void success(StatusRegisterBean statusRegisterBean);
	public void fail(String msg);
}
