package com.icoffice.library.callback;
/**
 * 网络链接接口
 * @author lufeisong
 *
 */
public interface ConnectNetCallBack {
	public void connectFail(String msg);
	public void connectSuccess(int payType);
}
